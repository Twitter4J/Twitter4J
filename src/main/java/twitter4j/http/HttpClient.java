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

import twitter4j.conf.Configuration;
import twitter4j.TwitterException;

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
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static twitter4j.http.RequestMethod.*;

/**
 * A utility class to handle HTTP request/response.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class HttpClient implements java.io.Serializable {
    private static final Configuration conf = Configuration.getInstance();
    private static final int OK = 200;// OK: Success!
    private static final int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    private static final int BAD_REQUEST = 400;// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    private static final int NOT_AUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    private static final int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    private static final int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    private static final int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    /**
     *     @see <a href="http://groups.google.com/group/twitter-api-announce/browse_thread/thread/3f3b0fd38deb9b0f?hl=en">Search API: new HTTP response code 420 for rate limiting starting 1/18/2010</a>
     */
    private static final int EXCEEDED_RATE_LIMIT_QUOTA = 420;// Not registered in RFC.
    private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Twitter team can investigate.
    private static final int BAD_GATEWAY = 502;// Bad Gateway: Twitter is down or being upgraded.
    private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.

    private String proxyHost = conf.getProxyHost();
    private int proxyPort = conf.getProxyPort();
    private String proxyAuthUser = conf.getProxyUser();
    private String proxyAuthPassword = conf.getProxyPassword();
    private int httpConnectionTimeout = conf.getHttpConnectionTimeout();
    private int httpReadTimeout = conf.getHttpReadTimeout();
    private static final long serialVersionUID = 808018030183407996L;
    private static boolean isJDK14orEarlier = false;
//    private Map<String, String> requestHeaders = new HashMap<String, String>();
    private List<HttpResponseListener> httpResponseListeners = new ArrayList<HttpResponseListener>();
//    HttpRequestFactory requestFactory;

    static {
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (null != versionStr) {
                isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
            }
        } catch (AccessControlException ace) {
            isJDK14orEarlier = true;
        }
    }

    public HttpClient() {
    }

    public Response get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, null, null, null));
    }

    public Response post(String url, PostParameter[] params) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, params, null, null));
    }

    public Response request(HttpRequest req) throws TwitterException {
        int retriedCount;
        int retry = conf.getRetryCount() + 1;
        Response res = null;
        for (retriedCount = 0; retriedCount < retry; retriedCount++) {
            int responseCode = -1;
            try {
                HttpURLConnection con = null;
                OutputStream os = null;
                try {
                    con = getConnection(req.url);
                    con.setDoInput(true);
                    setHeaders(req, con);
                    con.setRequestMethod(req.requestMethod.name());
                    if (req.requestMethod == POST) {
                        if (PostParameter.containsFile(req.postParams)) {
                            String boundary = "----Twitter4J-upload" + System.currentTimeMillis();
                            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                            boundary = "--" + boundary;
                            con.setDoOutput(true);
                            os = con.getOutputStream();
                            DataOutputStream out = new DataOutputStream(os);
                            for (PostParameter param : req.postParams) {
                                if (param.isFile()) {
                                    write(out, boundary + "\r\n");
                                    write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"; filename=\"" + param.file.getName() + "\"\r\n");
                                    write(out, "Content-Type: " + param.getContentType() + "\r\n\r\n");
                                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(param.file));
                                    int buff = 0;
                                    while ((buff = in.read()) != -1) {
                                        out.write(buff);
                                    }
                                    write(out, "\r\n");
                                    in.close();
                                } else {
                                    write(out, boundary + "\r\n");
                                    write(out, "Content-Disposition: form-data; name=\"" + param.name + "\"\r\n");
                                    write(out, "Content-Type: text/plain; charset=UTF-8\r\n\r\n");
                                    log(param.value);
                                    out.write(encode(param.value).getBytes("UTF-8"));
                                    write(out, "\r\n");
                                }
                            }
                            write(out, boundary + "--\r\n");
                            write(out, "\r\n");

                        } else {
                            con.setRequestProperty("Content-Type",
                                    "application/x-www-form-urlencoded");
                            String postParam = encodeParameters(req.postParams);
                            log("Post Params: ", postParam);
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
                    res = new Response(con);
                    responseCode = con.getResponseCode();
                    if (conf.isDebug()) {
                        log("Response: ");
                        Map<String, List<String>> responseHeaders = con.getHeaderFields();
                        for (String key : responseHeaders.keySet()) {
                            List<String> values = responseHeaders.get(key);
                            for (String value : values) {
                                if (null != key) {
                                    log(key + ": " + value);
                                } else {
                                    log(value);
                                }
                            }
                        }
                    }
                    if (responseCode != OK) {
                        if (responseCode == SERVICE_UNAVAILABLE || responseCode == EXCEEDED_RATE_LIMIT_QUOTA){
                            // application exceeded the rate limitation
                            // Search API returns Retry-After header that instructs the application when it is safe to continue.
                            // @see <a href="http://apiwiki.twitter.com/Rate-limiting">Rate limiting</a>
                            int retryAfter = -1;
                            try {
                                retryAfter = Integer.valueOf(con.getHeaderField("Retry-After"));
                            } catch (NumberFormatException ignore) {
                            }
                            throw TwitterException.createRateLimitedTwitterException(getCause(responseCode)
                                    , responseCode, retryAfter);
                        }
                        if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == conf.getRetryCount()) {
                            throw new TwitterException(getCause(responseCode) + "\n" + res.asString(), responseCode);
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
                if (retriedCount == conf.getRetryCount()) {
                    throw new TwitterException(ioe.getMessage(), ioe, responseCode);
                }
            }
            try {
                if (conf.isDebug() && null != res) {
                    res.asString();
                }
                log("Sleeping " + conf.getRetryIntervalMilliSecs() + " milli seconds for next retry.");
                Thread.sleep(conf.getRetryIntervalMilliSecs());
            } catch (InterruptedException ignore) {
                //nothing to do
            }
        }
        fireHttpResponseEvent(new HttpResponseEvent(req, res));
        return res;
    }

    private void write(DataOutputStream out, String outStr) throws IOException {
        out.writeBytes(outStr);
        log(outStr);
    }


    private void fireHttpResponseEvent(HttpResponseEvent httpResponseEvent) {
        for (HttpResponseListener listener : httpResponseListeners) {
            listener.httpResponseReceived(httpResponseEvent);
        }
    }

    public void addHttpResponseListener(HttpResponseListener listener) {
        httpResponseListeners.add(listener);
    }

    public static String encodeParameters(PostParameter[] postParams) {
        if (null == postParams) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (postParams[j].isFile()) {
                throw new IllegalArgumentException("parameter [" + postParams[j].name + "]should be text");
            }
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(postParams[j].name, "UTF-8"))
                        .append("=").append(URLEncoder.encode(postParams[j].value, "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();

    }

    public static String encodeParameter(PostParameter postParam) {
        if (postParam.isFile()) {
            throw new IllegalArgumentException("parameter [" + postParam.name + "]should be text");
        }
        StringBuffer buf = new StringBuffer(postParam.name.length() + postParam.value.length() + 1);
        buf.append(encode(postParam.name))
                .append("=").append(encode(postParam.value));
        return buf.toString();
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
        log("Request: ");
        log(req.requestMethod.name() + " ", req.url);

        if (null != req.authentication) {
            req.authentication.setAuthorizationHeader(req.requestMethod.name(), req.url, req.postParams, connection);
        }
        if (null != req.requestHeaders) {
            for (String key : req.requestHeaders.keySet()) {
                connection.addRequestProperty(key, req.requestHeaders.get(key));
                log(key + ": " + req.requestHeaders.get(key));
            }
        }
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
            if (conf.isDebug()) {
                log("Opening proxied connection(" + proxyHost + ":" + proxyPort + ")");
            }
            con = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else {
            con = (HttpURLConnection) new URL(url).openConnection();
        }
        if (httpConnectionTimeout > 0 && !isJDK14orEarlier) {
            con.setConnectTimeout(httpConnectionTimeout);
        }
        if (httpReadTimeout > 0 && !isJDK14orEarlier) {
            con.setReadTimeout(httpReadTimeout);
        }
        return con;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpClient)) return false;

        HttpClient that = (HttpClient) o;

        if (httpConnectionTimeout != that.httpConnectionTimeout) return false;
        if (proxyPort != that.proxyPort) return false;
        if (httpReadTimeout != that.httpReadTimeout) return false;
        if (!httpResponseListeners.equals(that.httpResponseListeners))
            return false;
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
        result = 31 * result + httpConnectionTimeout;
        result = 31 * result + httpReadTimeout;
        result = 31 * result + httpResponseListeners.hashCode();
        return result;
    }

    private static void log(String message) {
        if (conf.isDebug()) {
            System.out.println("[" + new java.util.Date() + "]" + message);
        }
    }

    private static void log(String message, String message2) {
        if (conf.isDebug()) {
            log(message + message2);
        }
    }

    private static String getCause(int statusCode) {
        String cause = null;
        // http://apiwiki.twitter.com/HTTP-Response-Codes-and-Errors
        switch (statusCode) {
            case NOT_MODIFIED:
                break;
            case BAD_REQUEST:
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            case NOT_AUTHORIZED:
                cause = "Authentication credentials were missing or incorrect.";
                break;
            case FORBIDDEN:
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            case NOT_FOUND:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            case NOT_ACCEPTABLE:
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            case EXCEEDED_RATE_LIMIT_QUOTA:
                cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            case INTERNAL_SERVER_ERROR:
                cause = "Something is broken.  Please post to the group so the Twitter team can investigate.";
                break;
            case BAD_GATEWAY:
                cause = "Twitter is down or being upgraded.";
                break;
            case SERVICE_UNAVAILABLE:
                cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            default:
                cause = "";
        }
        return statusCode + ":" + cause;
    }
}
