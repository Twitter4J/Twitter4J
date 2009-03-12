/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
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
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class HttpClient implements java.io.Serializable {
    private final int OK = 200;
    private final int NOT_MODIFIED = 304;
    private final int UNAUTHORIZED = 401;
    private final int FORBIDDEN = 403;

    private final boolean DEBUG = Boolean.getBoolean("twitter4j.debug");

    private final int INTERNAL_SERVER_ERROR = 500;
    private String basic;
    private int retryCount = 0;
    private int retryIntervalMillis = 10000;
    private String userId = null;
    private String password = null;
    private String proxyHost = null;
    private int proxyPort = 0;
    private String proxyAuthUser = null;
    private String proxyAuthPassword = null;
    private int connectionTimeout = 0;
    private int readTimeout = 0;
    private static final long serialVersionUID = 808018030183407996L;
    private boolean isJDK14orEarlier = false;
    private Map<String, String> requestHeaders = new HashMap<String, String>();

    public HttpClient(String userId, String password) {
        this();
        setUserId(userId);
        setPassword(password);
    }

    public HttpClient() {
        this.basic = null;
        //forcibly read system properties
        setProxyPort(0);
        setProxyHost(null);
        setConnectionTimeout(10000);
        setReadTimeout(30000);
        setProxyAuthUser(null);
        setProxyAuthPassword(null);

        String versionStr = System.getProperty("java.specification.version");
        if (null != versionStr) {
            isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
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

    /**
     * Sets proxy host.
     * System property -Dtwitter4j.http.proxyHost or http.proxyHost overrides this attribute.
     * @param proxyHost
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = System.getProperty("twitter4j.http.proxyHost", System.getProperty("http.proxyHost", proxyHost));
    }

    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets proxy port.
     * System property -Dtwitter4j.http.proxyPort or -Dhttp.proxyPort overrides this attribute.
     * @param proxyPort
     */
    public void setProxyPort(int proxyPort) {
        try {
            this.proxyPort = Integer.parseInt(System.getProperty("twitter4j.http.proxyPort", System.getProperty("http.proxyPort", String.valueOf(proxyPort))));
        } catch (NumberFormatException ignore) {
        }
    }

    public String getProxyAuthUser() {
        return proxyAuthUser;
    }

    /**
     * Sets proxy authentication user.
     * System property -Dtwitter4j.http.proxyUser overrides this attribute.
     * @param proxyAuthUser
     */
    public void setProxyAuthUser(String proxyAuthUser) {
        this.proxyAuthUser = System.getProperty("twitter4j.http.proxyUser", proxyAuthUser);
    }

    public String getProxyAuthPassword() {
        return proxyAuthPassword;
    }

    /**
     * Sets proxy authentication password.
     * System property -Dtwitter4j.http.proxyPassword overrides this attribute.
     * @param proxyAuthPassword
     */
    public void setProxyAuthPassword(String proxyAuthPassword) {
        this.proxyAuthPassword = System.getProperty("twitter4j.http.proxyPassword", proxyAuthPassword);
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the resource referenced by this URLConnection.
     * System property -Dtwitter4j.http.connectionTimeout overrides this attribute.
     * @param connectionTimeout - an int that specifies the connect timeout value in milliseconds
     */
    public void setConnectionTimeout(int connectionTimeout) {
        try {
            this.connectionTimeout = Integer.parseInt(System.getProperty("twitter4j.http.connectionTimeout",String.valueOf(connectionTimeout)));
        } catch (NumberFormatException ignore) {
        }

    }
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds. System property -Dtwitter4j.http.readTimeout overrides this attribute.
     * @param readTimeout - an int that specifies the timeout value to be used in milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        try {
            this.readTimeout = Integer.parseInt(System.getProperty("twitter4j.http.readTimeout",String.valueOf(readTimeout)));
        } catch (NumberFormatException ignore) {
        }
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
        setRequestHeader("User-Agent", ua);
    }
    public String getUserAgent(){
        return getRequestHeader("User-Agent");
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

    public void setRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    public String getRequestHeader(String name) {
        return requestHeaders.get(name);
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
        if (connectionTimeout > 0 && !isJDK14orEarlier) {
            con.setConnectTimeout(connectionTimeout);
        }
        if (readTimeout > 0 && !isJDK14orEarlier) {
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
