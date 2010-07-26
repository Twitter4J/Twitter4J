/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.internal.http;

import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.AccessControlException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static twitter4j.internal.http.RequestMethod.POST;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public class HttpClientImpl implements HttpClient, HttpResponseCode, java.io.Serializable {
    private static final Logger logger = Logger.getLogger(HttpClientImpl.class);

    private String proxyHost = null;
    private int proxyPort = -1;
    private String proxyAuthUser = null;
    private String proxyAuthPassword = null;
    private int connectionTimeout = 20000;
    private int readTimeout = 120000;
    private int retryCount = 0;
    private int retryIntervalSeconds = 5 * 1000;
    private static boolean isJDK14orEarlier = false;
    private static final long serialVersionUID = -8819171414069621503L;

    static {
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (null != versionStr) {
                isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
            }
            if (ConfigurationContext.getInstance().isDalvik()) {
                // quick and dirty workaround for TFJ-296
                // it must be an Android/Dalvik/Harmony side issue!!!!
                System.setProperty("http.keepAlive", "false");
            }
        } catch (AccessControlException ace) {
            isJDK14orEarlier = true;
        }
    }

    public HttpClientImpl() {
    }
    public HttpClientImpl(HttpClientConfiguration conf) {
        setProxyHost(conf.getHttpProxyHost());
        setProxyPort(conf.getHttpProxyPort());
        setProxyAuthUser(conf.getHttpProxyUser());
        setProxyAuthPassword(conf.getHttpProxyPassword());
        setConnectionTimeout(conf.getHttpConnectionTimeout());
        setReadTimeout(conf.getHttpReadTimeout());
        setRetryCount(conf.getHttpRetryCount());
        setRetryIntervalSeconds(conf.getHttpRetryIntervalSeconds());
    }

    public void shutdown() {
    }

    private static final Map<HttpClientConfiguration, HttpClient> instanceMap = new HashMap<HttpClientConfiguration, HttpClient>(1);

    public static HttpClient getInstance(HttpClientConfiguration conf) {
        HttpClient client = instanceMap.get(conf);
        if (null == client) {
            client = new HttpClientImpl(conf);
            instanceMap.put(conf, client);
        }
        return client;
    }


    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * Sets proxy host.
     *
     * @param proxyHost
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets proxy port.
     *
     * @param proxyPort
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyAuthUser() {
        return proxyAuthUser;
    }

    /**
     * Sets proxy authentication user.
     * System property -Dtwitter4j.http.proxyUser overrides this attribute.
     *
     * @param proxyAuthUser
     */
    public void setProxyAuthUser(String proxyAuthUser) {
        this.proxyAuthUser = proxyAuthUser;
    }

    public String getProxyAuthPassword() {
        return proxyAuthPassword;
    }

    /**
     * Sets proxy authentication password.
     * System property -Dtwitter4j.http.proxyPassword overrides this attribute.
     *
     * @param proxyAuthPassword
     */
    public void setProxyAuthPassword(String proxyAuthPassword) {
        this.proxyAuthPassword = proxyAuthPassword;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the resource referenced by this URLConnection.
     *
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
     *
     * @param readTimeout - an int that specifies the timeout value to be used in milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setRetryCount(int retryCount) {
        if (retryCount >= 0) {
            this.retryCount = retryCount;
        } else {
            throw new IllegalArgumentException("RetryCount cannot be negative.");
        }
    }

    public void setRetryIntervalSeconds(int retryIntervalSeconds) {
        if (retryIntervalSeconds >= 0) {
            this.retryIntervalSeconds = retryIntervalSeconds;
        } else {
            throw new IllegalArgumentException(
                    "RetryInterval cannot be negative.");
        }
    }


    public HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, null, null, null));
    }

    public HttpResponse post(String url, HttpParameter[] params) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, params, null, null));
    }

    public HttpResponse request(HttpRequest req) throws TwitterException {
        int retriedCount;
        int retry = retryCount + 1;
        HttpResponse res = null;
        for (retriedCount = 0; retriedCount < retry; retriedCount++) {
            int responseCode = -1;
            try {
                HttpURLConnection con = null;
                OutputStream os = null;
                try {
                    con = getConnection(req.getURL());
                    con.setDoInput(true);
                    setHeaders(req, con);
                    con.setRequestMethod(req.getMethod().name());
                    if (req.getMethod() == POST) {
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
                                    		param.hasFileBody() ? param.getFileBody() :new FileInputStream(param.getFile())
                                    		);
                                    int buff = 0;
                                    while ((buff = in.read()) != -1) {
                                        out.write(buff);
                                    }
                                    write(out, "\r\n");
                                    in.close();
                                } else {
                                    write(out, boundary + "\r\n");
                                    write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"\r\n");
                                    write(out, "Content-Type: text/plain; charset=UTF-8\r\n\r\n");
                                    logger.debug(param.getValue());
//                                    out.write(encode(param.getValue()).getBytes("UTF-8"));
                                    out.write(param.getValue().getBytes("UTF-8"));
                                    write(out, "\r\n");
                                }
                            }
                            write(out, boundary + "--\r\n");
                            write(out, "\r\n");

                        } else {
                            con.setRequestProperty("Content-Type",
                                    "application/x-www-form-urlencoded");
                            String postParam = HttpParameter.encodeParameters(req.getParameters());
                            logger.debug("Post Params: ", postParam);
                            byte[] bytes = postParam.getBytes("UTF-8");
                            con.setRequestProperty("Content-Length",
                                    Integer.toString(bytes.length));
                            con.setDoOutput(true);
                            os = con.getOutputStream();
                            os.write(bytes);
                        }
                        os.flush();
                        os.close();
                    }
                    res = new HttpResponseImpl(con);
                    responseCode = con.getResponseCode();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response: ");
                        Map<String, List<String>> responseHeaders = con.getHeaderFields();
                        for (String key : responseHeaders.keySet()) {
                            List<String> values = responseHeaders.get(key);
                            for (String value : values) {
                                if (null != key) {
                                    logger.debug(key + ": " + value);
                                } else {
                                    logger.debug(value);
                                }
                            }
                        }
                    }
                    if (responseCode < OK || MULTIPLE_CHOICES <= responseCode) {
                        if (responseCode == ENHANCE_YOUR_CLAIM ||
                                responseCode == SERVICE_UNAVAILABLE ||
                                responseCode == BAD_REQUEST ||
                                responseCode < INTERNAL_SERVER_ERROR ||
                                retriedCount == retryCount) {
                            throw new TwitterException(res.asString(), res);
                        }
                        // will retry if the status code is INTERNAL_SERVER_ERROR
                    } else {
                        break;
                    }
                } finally {
                    try {
                        os.close();
                    } catch (Exception ignore) {
                    }
                }
            } catch (IOException ioe) {
                // connection timeout or read timeout
                if (retriedCount == retryCount) {
                    throw new TwitterException(ioe.getMessage(), ioe, responseCode);
                }
            }
            try {
                if (logger.isDebugEnabled() && null != res) {
                    res.asString();
                }
                logger.debug("Sleeping " + retryIntervalSeconds + " seconds until the next retry.");
                Thread.sleep(retryIntervalSeconds * 1000);
            } catch (InterruptedException ignore) {
                //nothing to do
            }
        }
        return res;
    }

    private void write(DataOutputStream out, String outStr) throws IOException {
        out.writeBytes(outStr);
        logger.debug(outStr);
    }

    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (java.io.UnsupportedEncodingException neverHappen) {
            throw new AssertionError("will never happen");
        }
    }

    /**
     * sets HTTP headers
     * @param req The request
     * @param connection    HttpURLConnection
     */
    private void setHeaders(HttpRequest req, HttpURLConnection connection) {
        if (logger.isDebugEnabled()) {
            logger.debug("Request: ");
            logger.debug(req.getMethod().name() + " ", req.getURL());
        }

        String authorizationHeader;
        if (null != req.getAuthorization() && null != (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req))) {
            if (logger.isDebugEnabled()) {
                logger.debug("Authorization: ", StringUtil.maskString(authorizationHeader));
            }
            connection.addRequestProperty("Authorization", authorizationHeader);
        }
        if (null != req.getRequestHeaders()) {
            for (String key : req.getRequestHeaders().keySet()) {
                connection.addRequestProperty(key, req.getRequestHeaders().get(key));
                logger.debug(key + ": " + req.getRequestHeaders().get(key));
            }
        }
    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con = null;
        if (proxyHost != null && !proxyHost.equals("")) {
            if (proxyAuthUser != null && !proxyAuthUser.equals("")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Proxy AuthUser: " + proxyAuthUser);
                    logger.debug("Proxy AuthPassword: " + StringUtil.maskString(proxyAuthPassword));
                }
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
            final Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress
                    .createUnresolved(proxyHost, proxyPort));
            if (logger.isDebugEnabled()) {
                logger.debug("Opening proxied connection(" + proxyHost + ":" + proxyPort + ")");
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpClientImpl)) return false;

        HttpClientImpl that = (HttpClientImpl) o;

        if (connectionTimeout != that.connectionTimeout) return false;
        if (proxyPort != that.proxyPort) return false;
        if (readTimeout != that.readTimeout) return false;
        if (retryCount != that.retryCount) return false;
        if (retryIntervalSeconds != that.retryIntervalSeconds) return false;
        if (proxyAuthPassword != null ? !proxyAuthPassword.equals(that.proxyAuthPassword) : that.proxyAuthPassword != null)
            return false;
        if (proxyAuthUser != null ? !proxyAuthUser.equals(that.proxyAuthUser) : that.proxyAuthUser != null)
            return false;
        if (proxyHost != null ? !proxyHost.equals(that.proxyHost) : that.proxyHost != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = proxyHost != null ? proxyHost.hashCode() : 0;
        result = 31 * result + proxyPort;
        result = 31 * result + (proxyAuthUser != null ? proxyAuthUser.hashCode() : 0);
        result = 31 * result + (proxyAuthPassword != null ? proxyAuthPassword.hashCode() : 0);
        result = 31 * result + connectionTimeout;
        result = 31 * result + readTimeout;
        result = 31 * result + retryCount;
        result = 31 * result + retryIntervalSeconds;
        return result;
    }

    @Override
    public String toString() {
        return "HttpClientImpl{" +
                "proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                ", proxyAuthUser='" + proxyAuthUser + '\'' +
                ", proxyAuthPassword='" + proxyAuthPassword + '\'' +
                ", connectionTimeout=" + connectionTimeout +
                ", readTimeout=" + readTimeout +
                ", retryCount=" + retryCount +
                ", retryIntervalSeconds=" + retryIntervalSeconds +
                '}';
    }
}
