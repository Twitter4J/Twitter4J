package twitter4j;

import twitter4j.conf.ConfigurationContext;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class HttpClient implements HttpResponseCode, Serializable {
    private final static ConcurrentHashMap<HttpClientConfiguration, HttpClient> confClientMap = new ConcurrentHashMap<>();

    static HttpClient getInstance(HttpClientConfiguration conf) {
        return confClientMap.computeIfAbsent(conf, e -> new HttpClient(conf));
    }

    private static final Logger logger = Logger.getLogger();
    private static final long serialVersionUID = -8016974810651763053L;
    private final HttpClientConfiguration CONF;

    private final Map<String, String> requestHeaders;

    HttpClient(){
        this(ConfigurationContext.getInstance().getHttpClientConfiguration());
    }
    HttpClient(HttpClientConfiguration conf) {
        this.CONF = conf;
        requestHeaders = new HashMap<>();
        requestHeaders.put("X-Twitter-Client-Version", Version.getVersion());
        requestHeaders.put("X-Twitter-Client-URL", "https://twitter4j.org/en/twitter4j-" + Version.getVersion() + ".xml");
        requestHeaders.put("X-Twitter-Client", "Twitter4J");
        requestHeaders.put("User-Agent", "twitter4j https://twitter4j.org/ /" + Version.getVersion());
        if (conf.isGZIPEnabled()) {
            requestHeaders.put("Accept-Encoding", "gzip");
        }
    }

    private boolean isProxyConfigured() {
        return CONF.getHttpProxyHost() != null && !CONF.getHttpProxyHost().equals("");
    }

    void write(DataOutputStream out, String outStr) throws IOException {
        out.writeBytes(outStr);
        logger.debug(outStr);
    }

    @SuppressWarnings("SameParameterValue")
    void addDefaultRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    HttpResponse request(HttpRequest req) throws TwitterException {
        return handleRequest(req);
    }

    HttpResponse request(HttpRequest req, HttpResponseListener listener) throws TwitterException {
        try {
            HttpResponse res = handleRequest(req);
            if (listener != null) {
                listener.httpResponseReceived(new HttpResponseEvent(req, res, null));
            }
            return res;
        } catch (TwitterException te) {
            if (listener != null) {
                listener.httpResponseReceived(new HttpResponseEvent(req, null, te));
            }
            throw te;
        }
    }

    HttpResponse handleRequest(HttpRequest req) throws TwitterException {
        int retriedCount;
        int retry = CONF.getHttpRetryCount() + 1;
        HttpResponse res = null;
        for (retriedCount = 0; retriedCount < retry; retriedCount++) {
            int responseCode = -1;
            try {
                HttpURLConnection con;
                OutputStream os = null;
                try {
                    con = getConnection(req.getURL());
                    con.setDoInput(true);
                    setHeaders(req, con);
                    con.setRequestMethod(req.getMethod().name());
                    if (req.getMethod() == RequestMethod.POST) {
                        if (HttpParameter.containsFile(req.getParameters())) {
                            String boundary = "----Twitter4J-upload" + System.currentTimeMillis();
                            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                            boundary = "--" + boundary;
                            con.setDoOutput(true);
                            os = con.getOutputStream();
                            DataOutputStream out = new DataOutputStream(os);
                            for (HttpParameter param : req.getParameters()) {
                                if (param.isFile()) {
                                    write(out, boundary + "\r\n");
                                    write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"; filename=\"" + param.getFile().getName() + "\"\r\n");
                                    write(out, "Content-Type: " + param.getContentType() + "\r\n\r\n");
                                    BufferedInputStream in = new BufferedInputStream(
                                            param.hasFileBody() ? param.getFileBody() : Files.newInputStream(param.getFile().toPath())
                                    );
                                    byte[] buff = new byte[1024];
                                    int length;
                                    while ((length = in.read(buff)) != -1) {
                                        out.write(buff, 0, length);
                                    }
                                    write(out, "\r\n");
                                    in.close();
                                } else {
                                    write(out, boundary + "\r\n");
                                    write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"\r\n");
                                    write(out, "Content-Type: text/plain; charset=UTF-8\r\n\r\n");
                                    logger.debug(param.getValue());
                                    out.write(param.getValue().getBytes(StandardCharsets.UTF_8));
                                    write(out, "\r\n");
                                }
                            }
                            write(out, boundary + "--\r\n");
                            write(out, "\r\n");
                        } else {
                            String postParam;
                            if (HttpParameter.containsJson(req.getParameters())) {
                                con.setRequestProperty("Content-Type",
                                        "application/json");
                                postParam = req.getParameters()[0].getJsonObject().toString();
                            } else {
                                con.setRequestProperty("Content-Type",
                                        "application/x-www-form-urlencoded");
                                postParam = HttpParameter.encodeParameters(req.getParameters());
                            }
                            logger.debug("Post Params: ", postParam);
                            byte[] bytes = postParam.getBytes(StandardCharsets.UTF_8);
                            con.setRequestProperty("Content-Length",
                                    Integer.toString(bytes.length));
                            con.setDoOutput(true);
                            os = con.getOutputStream();
                            os.write(bytes);
                        }
                        os.flush();
                        os.close();
                    }
                    res = new HttpResponse(con, CONF);
                    responseCode = con.getResponseCode();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response: ");
                        Map<String, List<String>> responseHeaders = con.getHeaderFields();
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
                        if (responseCode < INTERNAL_SERVER_ERROR ||
                                retriedCount == CONF.getHttpRetryCount()) {
                            throw new TwitterException(res.asString(), res);
                        }
                        // will retry if the status code is INTERNAL_SERVER_ERROR
                    } else {
                        break;
                    }
                } finally {
                    try {
                        if (os != null) {
                            os.close();
                        }
                    } catch (Exception ignore) {
                    }
                }
            } catch (IOException ioe) {
                // connection timeout or read timeout
                if (retriedCount == CONF.getHttpRetryCount()) {
                    throw new TwitterException(ioe.getMessage(), ioe, responseCode);
                }
            }
            try {
                if (logger.isDebugEnabled() && res != null) {
                    res.asString();
                }
                logger.debug("Sleeping " + CONF.getHttpRetryIntervalSeconds() + " seconds until the next retry.");
                Thread.sleep(CONF.getHttpRetryIntervalSeconds() * 1000L);
            } catch (InterruptedException ignore) {
                //nothing to do
            }
        }
        return res;
    }
    /**
     * sets HTTP headers
     *
     * @param req        The request
     * @param connection HttpURLConnection
     */
    private void setHeaders(HttpRequest req, HttpURLConnection connection) {
        if (logger.isDebugEnabled()) {
            logger.debug("Request: ");
            logger.debug(req.getMethod().name() + " ", req.getURL());
        }

        String authorizationHeader;
        if (req.getAuthorization() != null && (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req)) != null) {
            if (logger.isDebugEnabled()) {
                //noinspection SuspiciousRegexArgument
                logger.debug("Authorization: ", authorizationHeader.replaceAll(".", "*"));
            }
            connection.addRequestProperty("Authorization", authorizationHeader);
        }
        if (req.getRequestHeaders() != null) {
            for (String key : req.getRequestHeaders().keySet()) {
                connection.addRequestProperty(key, req.getRequestHeaders().get(key));
                logger.debug(key + ": " + req.getRequestHeaders().get(key));
            }
        }
    }

    HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con;
        if (isProxyConfigured()) {
            if (CONF.getHttpProxyUser() != null && !CONF.getHttpProxyUser().equals("")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Proxy AuthUser: " + CONF.getHttpProxyUser());
                    //noinspection SuspiciousRegexArgument
                    logger.debug("Proxy AuthPassword: " + CONF.getHttpProxyPassword().replaceAll(".", "*"));
                }
                Authenticator.setDefault(new Authenticator() {
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
            final Proxy proxy = new Proxy(CONF.isHttpProxySocks() ? Proxy.Type.SOCKS : Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved(CONF.getHttpProxyHost(), CONF.getHttpProxyPort()));
            if (logger.isDebugEnabled()) {
                logger.debug("Opening proxied connection(" + CONF.getHttpProxyHost() + ":" + CONF.getHttpProxyPort() + ")");
            }
            con = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else {
            con = (HttpURLConnection) new URL(url).openConnection();
        }
        if (CONF.getHttpConnectionTimeout() > 0) {
            con.setConnectTimeout(CONF.getHttpConnectionTimeout());
        }
        if (CONF.getHttpReadTimeout() > 0) {
            con.setReadTimeout(CONF.getHttpReadTimeout());
        }
        con.setInstanceFollowRedirects(false);
        return con;
    }

    HttpResponse get(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, parameters, authorization, this.requestHeaders), listener);
    }

    HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, null, null, this.requestHeaders));
    }

    HttpResponse post(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, parameters, authorization, this.requestHeaders), listener);
    }
    @SuppressWarnings("unused")
    HttpResponse post(String url, HttpParameter[] params) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, params, null, null));
    }

    HttpResponse post(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, null, null, this.requestHeaders));
    }

    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    HttpResponse delete(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, parameters, authorization, this.requestHeaders), listener);
    }

    @SuppressWarnings("unused")
    HttpResponse delete(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, null, null, this.requestHeaders));
    }

    @SuppressWarnings("UnusedReturnValue")
    HttpResponse head(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, null, null, this.requestHeaders));
    }

    @SuppressWarnings("unused")
    HttpResponse put(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, parameters, authorization, this.requestHeaders), listener);
    }

    @SuppressWarnings("unused")
    HttpResponse put(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, null, null, this.requestHeaders));
    }
}