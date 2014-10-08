package twitter4j;

import com.squareup.okhttp.*;
import twitter4j.conf.ConfigurationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuuto Uehara
 * @since Twitter4J 4.0.3
 */
public class AlternativeHttpClientImpl extends HttpClientBase implements HttpResponseCode, java.io.Serializable {
	private static final long serialVersionUID = 1757413669925424213L;
	private static final Logger logger = Logger.getLogger(AlternativeHttpClientImpl.class);

	private static final int MAX_CONNECTIONS = 5;
	private static final int KEEP_ALIVE_DURATION_MS = 300;

	private static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");
	private static final MediaType FORM_URL_ENCODED = MediaType.parse("application/x-www-form-urlencoded");

	private OkHttpClient okHttpClient;

	//for test
	static boolean sPreferSpdy = true;
	static boolean sPreferHttp2 = true;
	private String lastRequestProtocol = null;

	public AlternativeHttpClientImpl(){
		super(ConfigurationContext.getInstance().getHttpClientConfiguration());
	}

	public AlternativeHttpClientImpl(HttpClientConfiguration conf) {
		super(conf);
	}


	@Override
	HttpResponse handleRequest(HttpRequest req) throws TwitterException {
		prepareOkHttpClient();

		HttpResponse res = null;
		Request.Builder requestBuilder = new Request.Builder();
		requestBuilder.url(req.getURL()).headers(getHeaders(req));
		switch (req.getMethod()){
			case HEAD:
			case DELETE:
			case PUT:
				break;
			case GET:
				requestBuilder.get();
				break;
			case POST:
				try {
					requestBuilder.post(getRequestBody(req));
				} catch (UnsupportedEncodingException e) {
					throw new TwitterException(e.getMessage(), e);
				}
				break;
		}
		final Request request = requestBuilder.build();


		int retriedCount;
		int retry = CONF.getHttpRetryCount() + 1;
		for(retriedCount = 0; retriedCount < retry; retriedCount++){
			int responseCode = -1;
			try {
				Response response = okHttpClient.newCall(request).execute();
				lastRequestProtocol = response.header("OkHttp-Selected-Protocol");
				res = new OkHttpResponse(response, CONF);
				responseCode = response.code();

				if(logger.isDebugEnabled()){
					logger.debug("Response: ");
					Map<String, List<String>> responseHeaders = res.getResponseHeaderFields();
					for (String key : responseHeaders.keySet()) {
						List<String> values = responseHeaders.get(key);
						for (String value : values) {
							if (key != null) {
								logger.debug(key + ": " + value);
							} else {
								logger.debug(value);
							}
						}
					}
				}
				if (responseCode < OK || (responseCode != FOUND && MULTIPLE_CHOICES <= responseCode)) {
					if (responseCode == ENHANCE_YOUR_CLAIM ||
							responseCode == BAD_REQUEST ||
							responseCode < INTERNAL_SERVER_ERROR ||
							retriedCount == CONF.getHttpRetryCount()) {

						throw new TwitterException(res.asString(), res);
					}
					// will retry if the status code is INTERNAL_SERVER_ERROR
				} else {
					break;
				}

			} catch (IOException e) {
				if (retriedCount == CONF.getHttpRetryCount()) {
					throw new TwitterException(e.getMessage(), e, responseCode);
				}
			}
			try {
				if (logger.isDebugEnabled() && res != null) {
					res.asString();
				}
				logger.debug("Sleeping " + CONF.getHttpRetryIntervalSeconds() + " seconds until the next retry.");
				Thread.sleep(CONF.getHttpRetryIntervalSeconds() * 1000);
			} catch (InterruptedException ignore) {
				//nothing to do
			}

		}
		return res;
	}

	private RequestBody getRequestBody(HttpRequest req) throws UnsupportedEncodingException {
		if(HttpParameter.containsFile(req.getParameters())){//multipart/form-data
			final String boundary = "----Twitter4J-upload" + System.currentTimeMillis();
			MultipartBuilder multipartBuilder = new MultipartBuilder(boundary).type(MultipartBuilder.FORM);
			for(HttpParameter parameter:req.getParameters()){
				if(parameter.isFile()) {
					multipartBuilder.addPart(
							Headers.of("Content-Disposition","form-data; name=\"" + parameter.getName() + "\"; filename=\"" + parameter.getFile().getName()),
							RequestBody.create(MediaType.parse(parameter.getContentType()),parameter.getFile())
					);
				}else {
					multipartBuilder.addPart(
							Headers.of("Content-Disposition","form-data; name=" + parameter.getName()),
							RequestBody.create(TEXT,parameter.getValue().getBytes("UTF-8"))
					);
				}
			}
			return multipartBuilder.build();
		}else {//application/x-www-form-urlencoded
			return RequestBody.create(FORM_URL_ENCODED,HttpParameter.encodeParameters(req.getParameters()).getBytes("UTF-8"));
		}
	}

	private Headers getHeaders(HttpRequest req){

		Headers.Builder builder = new Headers.Builder();

		if (logger.isDebugEnabled()) {
			logger.debug("Request: ");
			logger.debug(req.getMethod().name() + " ", req.getURL());
		}

		String authorizationHeader;
		if(req.getAuthorization()!=null && (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req))!=null ){
			if (logger.isDebugEnabled()) {
				logger.debug("Authorization: ", authorizationHeader.replaceAll(".", "*"));
			}
			builder.add("Authorization", authorizationHeader);
		}
		if(req.getRequestHeaders() != null){
			for(Map.Entry<String,String> entry:req.getRequestHeaders().entrySet()){
				builder.add(entry.getKey(), entry.getValue());
				logger.debug(entry.getKey() + ": " + entry.getValue());
			}
		}
		return builder.build();

	}

	private void prepareOkHttpClient(){
		if(okHttpClient == null){
			okHttpClient = new OkHttpClient();
			//set protocols
			//don't need protocol comment out
			List<Protocol> protocols = new ArrayList<Protocol>();
			protocols.add(Protocol.HTTP_1_1);
			if (sPreferHttp2)protocols.add(Protocol.HTTP_2);
			if (sPreferSpdy)protocols.add(Protocol.SPDY_3);

			okHttpClient.setProtocols(protocols);

			okHttpClient.setConnectionPool(new ConnectionPool(MAX_CONNECTIONS,KEEP_ALIVE_DURATION_MS));

			if (isProxyConfigured()) {
				if (CONF.getHttpProxyUser() != null && !CONF.getHttpProxyUser().equals("")) {
					if (logger.isDebugEnabled()) {
						logger.debug("Proxy AuthUser: " + CONF.getHttpProxyUser());
						logger.debug("Proxy AuthPassword: " + CONF.getHttpProxyPassword().replaceAll(".", "*"));
					}
					java.net.Authenticator.setDefault(new java.net.Authenticator() {
						@Override
						protected PasswordAuthentication
						getPasswordAuthentication() {
							//respond only to proxy auth requests
							if (getRequestorType().equals(RequestorType.PROXY)) {
								return new PasswordAuthentication(CONF.getHttpProxyUser(),
										CONF.getHttpProxyPassword().toCharArray());
							} else {
								return null;
							}
						}
					});
				}
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress
						.createUnresolved(CONF.getHttpProxyHost(), CONF.getHttpProxyPort()));
				if (logger.isDebugEnabled()) {
					logger.debug("Opening proxied connection(" + CONF.getHttpProxyHost() + ":" + CONF.getHttpProxyPort() + ")");
				}
				okHttpClient.setProxy(proxy);
			}

			if (CONF.getHttpConnectionTimeout() > 0) {
				okHttpClient.setConnectTimeout(CONF.getHttpConnectionTimeout(), TimeUnit.MILLISECONDS);
			}
			if (CONF.getHttpReadTimeout() > 0) {
				okHttpClient.setReadTimeout(CONF.getHttpReadTimeout(),TimeUnit.MILLISECONDS);
			}
		}
	}

	//for test
	public String getLastRequestProtocol() {
		return lastRequestProtocol;
	}

}
