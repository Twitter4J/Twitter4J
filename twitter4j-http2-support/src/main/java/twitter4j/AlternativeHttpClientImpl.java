/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package twitter4j;

import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import twitter4j.conf.ConfigurationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @author Yuuto Uehara - muemi.himazin at gmail.com
 * @since Twitter4J 3.0.6
 */
public class AlternativeHttpClientImpl extends HttpClientBase implements HttpResponseCode, java.io.Serializable {
    private static final long serialVersionUID = 1757413669925424213L;
    private static final Logger logger = Logger.getLogger(AlternativeHttpClientImpl.class);

    private static final int MAX_CONNECTIONS = 5;
    private static final int KEEP_ALIVE_DURATION_MS = 300;

    private static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");
    private static final MediaType FORM_URL_ENCODED = MediaType.parse("application/x-www-form-urlencoded");
    private static final MediaType APPLICATION_JSON = MediaType.parse("application/json");

    private OkHttpClient okHttpClient;

    //for test
    public static boolean sPreferSpdy = true;
    public static boolean sPreferHttp2 = true;
    private Protocol lastRequestProtocol = null;

    public AlternativeHttpClientImpl() {
        super(ConfigurationContext.getInstance().getHttpClientConfiguration());
    }

    public AlternativeHttpClientImpl(HttpClientConfiguration conf) {
        super(conf);
    }


    @Override
    HttpResponse handleRequest(HttpRequest req) throws TwitterException {
        prepareOkHttpClient();

        OkHttpResponse res = null;
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(req.getURL()).headers(getHeaders(req));
        switch (req.getMethod()) {
            case HEAD:
            case PUT:
                break;
            case DELETE:
                requestBuilder.delete();
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
        for (retriedCount = 0; retriedCount < retry; retriedCount++) {
            int responseCode = -1;
            try {
                Call call = okHttpClient.newCall(request);
                res = new OkHttpResponse(call, okHttpClient, CONF);
                lastRequestProtocol = res.getProtocol();
                responseCode = res.getStatusCode();

                if (logger.isDebugEnabled()) {
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
        if (HttpParameter.containsFile(req.getParameters())) {
            final String boundary = "----Twitter4J-upload" + System.currentTimeMillis();

            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM);
            for (HttpParameter parameter : req.getParameters()) {
                if (parameter.isFile()) {
                    if (parameter.hasFileBody()) {
                        multipartBodyBuilder.addPart(
                                Headers.of("Content-Disposition", "form-data; name=\"" + parameter.getName() + "\"; filename=\"" + parameter.getFile().getName() + "\""),
                                createInputStreamRequestBody(MediaType.parse(parameter.getContentType()), parameter.getFileBody())
                        );
                    } else {
                        multipartBodyBuilder.addPart(
                                Headers.of("Content-Disposition", "form-data; name=\"" + parameter.getName() + "\"; filename=\"" + parameter.getFile().getName() + "\""),
                                RequestBody.create(MediaType.parse(parameter.getContentType()), parameter.getFile())
                        );
                    }
                } else {
                    multipartBodyBuilder.addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"" + parameter.getName() + "\""),
                            RequestBody.create(TEXT, parameter.getValue().getBytes("UTF-8"))
                    );
                }
            }
            return multipartBodyBuilder.build();
        } else if (HttpParameter.containsJson(req.getParameters())) {
            return RequestBody.create(APPLICATION_JSON, req.getParameters()[0].getJsonObject().toString());
        } else {
            return RequestBody.create(FORM_URL_ENCODED, HttpParameter.encodeParameters(req.getParameters()).getBytes("UTF-8"));
        }
    }

    private Headers getHeaders(HttpRequest req) {

        Headers.Builder builder = new Headers.Builder();

        if (logger.isDebugEnabled()) {
            logger.debug("Request: ");
            logger.debug(req.getMethod().name() + " ", req.getURL());
        }

        String authorizationHeader;
        if (req.getAuthorization() != null && (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req)) != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Authorization: ", authorizationHeader.replaceAll(".", "*"));
            }
            builder.add("Authorization", authorizationHeader);
        }
        if (req.getRequestHeaders() != null) {
            for (Map.Entry<String, String> entry : req.getRequestHeaders().entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
                logger.debug(entry.getKey() + ": " + entry.getValue());
            }
        }
        return builder.build();
    }

    public static RequestBody createInputStreamRequestBody(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    private void prepareOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //set protocols
            List<Protocol> protocols = new ArrayList<Protocol>();
            protocols.add(Protocol.HTTP_1_1);
            if (sPreferHttp2) protocols.add(Protocol.HTTP_2);
            if (sPreferSpdy) protocols.add(Protocol.SPDY_3);
            builder.protocols(protocols);

            //connectionPool setup
            builder.connectionPool(new ConnectionPool(MAX_CONNECTIONS, KEEP_ALIVE_DURATION_MS, TimeUnit.MILLISECONDS));

            //redirect disable
            builder.followSslRedirects(false);

            //for proxy
            if (isProxyConfigured()) {
                if (CONF.getHttpProxyUser() != null && !CONF.getHttpProxyUser().equals("")) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Proxy AuthUser: " + CONF.getHttpProxyUser());
                        logger.debug("Proxy AuthPassword: " + CONF.getHttpProxyPassword().replaceAll(".", "*"));
                    }

                    Authenticator.setDefault(new Authenticator() {
                        @Override
                        protected PasswordAuthentication
                        getPasswordAuthentication() {
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
                builder.proxy(proxy);
            }

            //connection timeout
            if (CONF.getHttpConnectionTimeout() > 0) {
                builder.connectTimeout(CONF.getHttpConnectionTimeout(), TimeUnit.MILLISECONDS);
            }

            //read timeout
            if (CONF.getHttpReadTimeout() > 0) {
                builder.readTimeout(CONF.getHttpReadTimeout(), TimeUnit.MILLISECONDS);
            }

            okHttpClient = builder.build();
        }
    }

    //for test
    public Protocol getLastRequestProtocol() {
        return lastRequestProtocol;
    }

    public OkHttpClient getOkHttpClient() {
        prepareOkHttpClient();
        return okHttpClient;
    }
}
