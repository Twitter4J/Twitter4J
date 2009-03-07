package twitter4j.http;

import twitter4j.TwitterException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class to handle HTTP request/response.
 */
public class HttpClient implements java.io.Serializable {
    private final int OK = 200;
    private final int NOT_MODIFIED = 304;
    private final int UNAUTHORIZED = 401;
    private final int FORBIDDEN = 403;

    private final boolean DEBUG = Boolean.getBoolean("twitter4j.debug");

    private final int INTERNAL_SERVER_ERROR = 500;
    private String userAgent =
            "twitter4j http://yusuke.homeip.net/twitter4j/ /1.1.6";
    private String basic;
    private int retryCount = 0;
    private int retryIntervalMillis = 10000;
    private String userId = null;
    private String password = null;
    private String proxyHost = System.getProperty("twitter4j.http.proxyHost",System.getProperty("http.proxyHost"));
    private int proxyPort = 0;
    private String proxyAuthUser = System.getProperty("twitter4j.http.proxyUser");
    private String proxyAuthPassword = System.getProperty("twitter4j.http.proxyPassword");
    private int connectionTimeout = 0;
    private int readTimeout = 0;
    private static final long serialVersionUID = 808018030183407996L;

    public HttpClient(String userId, String password) {
        this();
        setUserId(userId);
        setPassword(password);
    }

    public HttpClient() {
        this.basic = null;
        try {
            proxyPort = Integer.parseInt(System.getProperty("twitter4j.http.proxyPort", System.getProperty("http.proxyPort")));
        } catch (NumberFormatException ignore) {
        }
        try {
            connectionTimeout = Integer.parseInt(System.getProperty("twitter4j.http.connectionTimeout","10000"));
        } catch (NumberFormatException ignore) {
        }
        try {
            readTimeout = Integer.parseInt(System.getProperty("twitter4j.http.readTimeout","30000"));
        } catch (NumberFormatException ignore) {
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
        encodeBasicAuthenticationString();
    }

    public void setPassword(String password) {
        this.password = password;
        encodeBasicAuthenticationString();
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyAuthUser() {
        return proxyAuthUser;
    }

    public void setProxyAuthUser(String proxyAuthUser) {
        this.proxyAuthUser = proxyAuthUser;
    }

    public String getProxyAuthPassword() {
        return proxyAuthPassword;
    }

    public void setProxyAuthPassword(String proxyAuthPassword) {
        this.proxyAuthPassword = proxyAuthPassword;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the resource referenced by this URLConnection.
     * @param connectionTimeout - an int that specifies the connect timeout value in milliseconds
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds.
     * @param readTimeout - an int that specifies the timeout value to be used in milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    private void encodeBasicAuthenticationString() {
        if (null != userId && null != password) {
            this.basic = "Basic " +
                    new String(new BASE64Encoder().encode((userId + ":" + password).getBytes()));
        }
    }

    public void setRetryCount(int retryCount) {
        if (retryCount >= 0) {
            this.retryCount = retryCount;
        } else {
            throw new IllegalArgumentException("RetryCount cannot be negative.");
        }
    }

    public void setUserAgent(String ua) {
        this.userAgent = ua;
    }

    public void setRetryIntervalSecs(int retryIntervalSecs) {
        if (retryIntervalSecs >= 0) {
            this.retryIntervalMillis = retryIntervalSecs * 1000;
        } else {
            throw new IllegalArgumentException(
                    "RetryInterval cannot be negative.");
        }
    }

    public Response post(String url, PostParameter[] PostParameters,
                         boolean authenticated) throws TwitterException {
        return httpRequest(url, PostParameters, authenticated);
    }

    public Response post(String url, boolean authenticated) throws TwitterException {
        return httpRequest(url, new PostParameter[0], authenticated);
    }

    public Response post(String url, PostParameter[] PostParameters) throws
            TwitterException {
        return httpRequest(url, PostParameters, false);
    }

    public Response post(String url) throws
            TwitterException {
        return httpRequest(url, new PostParameter[0], false);
    }

    public Response get(String url, boolean authenticated) throws
            TwitterException {
        return httpRequest(url, null, authenticated);
    }

    public Response get(String url) throws TwitterException {
        return httpRequest(url, null, false);
    }

    //for test purpose
    /*package*/ int retriedCount = 0;
    /*package*/ String lastURL;

    private Response httpRequest(String url, PostParameter[] postParams,
                                 boolean authenticated) throws TwitterException {
        int retry = retryCount + 1;
        Response res = null;
        // update the status
        lastURL = url;
        for (retriedCount = 0; retriedCount < retry; retriedCount++) {
            int responseCode = -1;
            try {
                HttpURLConnection con = null;
                InputStream is = null;
                OutputStream osw = null;
                try {
                    con = getConnection(url);
                    con.setDoInput(true);
                    setHeaders(con, authenticated);
                    if (null != postParams) {
                        log("POST ", url);
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type",
                                "application/x-www-form-urlencoded");
                        con.setDoOutput(true);
                        String postParam = encodeParameters(postParams);
                        log("Post Params: ", postParam);
                        byte[] bytes = postParam.getBytes("UTF-8");

                        con.setRequestProperty("Content-Length",
                                Integer.toString(bytes.length));
                        osw = con.getOutputStream();
                        osw.write(bytes);
                        osw.flush();
                        osw.close();
                    } else {
                        log("GET " + url);
                        con.setRequestMethod("GET");
                    }
                    responseCode = con.getResponseCode();
                    log("Response code: ", String.valueOf(responseCode));
                    if (responseCode == UNAUTHORIZED || responseCode == FORBIDDEN) {
                        is = con.getErrorStream();
                    } else {
                        is = con.getInputStream(); // this will throw IOException in case response code is 4xx 5xx
                    }
                    res = new Response(con.getResponseCode(), is);
                    log("Response: ", res.toString());
                    if (responseCode == UNAUTHORIZED || responseCode == FORBIDDEN) {
                        throw new TwitterException(res.toString(), responseCode);
                    }

                    break;
                } finally {
                    try {
                        is.close();
                    } catch (Exception ignore) {
                    }
                    try {
                        osw.close();
                    } catch (Exception ignore) {
                    }
                    try {
                        con.disconnect();
                    } catch (Exception ignore) {
                    }
                }
            } catch (IOException ioe) {
                if (responseCode == UNAUTHORIZED || responseCode == FORBIDDEN) {
                    //throw TwitterException without reply since this request won't success
                    if (DEBUG) {
                        ioe.printStackTrace();
                    }
                    throw new TwitterException(ioe.getMessage(), responseCode);
                }
                if (retriedCount == retryCount) {
                    throw new TwitterException(ioe.getMessage(), responseCode);
                }
            }
            try {
                Thread.sleep(retryIntervalMillis);
            } catch (InterruptedException ignore) {
                //nothing to do
            }
        }
        return res;
    }

    public static String encodeParameters(PostParameter[] postParams) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(postParams[j].name).append("=").append(
                        URLEncoder.encode(postParams[j].value, "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();

    }

    /**
     * sets HTTP headers
     *
     * @param connection    HttpURLConnection
     * @param authenticated boolean
     */
    private void setHeaders(HttpURLConnection connection, boolean authenticated) {
        if (authenticated) {
            if (basic == null) {
                throw new IllegalStateException(
                        "user ID/password combination not supplied");
            }
            connection.addRequestProperty("Authorization", this.basic);
        }
        for (String key : requestHeaders.keySet()) {
            connection.addRequestProperty(key, requestHeaders.get(key));
        }
    }

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    public void setRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con = null;
        if (proxyHost != null && !proxyHost.equals("")) {
            if (proxyAuthUser != null && !proxyAuthUser.equals("")) {
                log("Proxy AuthUser: " + proxyAuthUser);
                log("Proxy AuthPassword: " + proxyAuthPassword);
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        //respond only to proxy auth requests
                        if (getRequestorType().equals(RequestorType.PROXY)) {
                            return new PasswordAuthentication(proxyAuthUser,
                                    proxyAuthPassword
                                            .toCharArray());
                        } else {
                            return null;
                        }
                    }
                });
            }
            final Proxy proxy = new Proxy(Type.HTTP, InetSocketAddress
                    .createUnresolved(proxyHost, proxyPort));
            if(DEBUG){
                log("Opening proxied connection(" + proxyHost + ":" + proxyPort + ")");
            }
            con = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else {
            con = (HttpURLConnection) new URL(url).openConnection();
        }
        if (connectionTimeout > 0) {
            con.setConnectTimeout(connectionTimeout);
        }
        if (readTimeout > 0) {
            con.setReadTimeout(readTimeout);
        }
        return con;
    }

    @Override
    public int hashCode() {
        return this.retryCount + this.retryIntervalMillis + this.basic.hashCode()
                + requestHeaders.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof HttpClient) {
            HttpClient that = (HttpClient) obj;
            return this.retryCount == that.retriedCount &&
                    this.retryIntervalMillis
                            == that.retryIntervalMillis && this.basic.equals(that.basic)
                    && this.requestHeaders.equals(that.requestHeaders);
        }
        return false;
    }

    private void log(String message) {
        if (DEBUG) {
            System.out.println("[" + new java.util.Date() + "]" + message);
        }
    }

    private void log(String message, String message2) {
        if (DEBUG) {
            log(message + message2);
        }
    }

}
