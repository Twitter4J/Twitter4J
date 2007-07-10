package twitter4j.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import twitter4j.TwitterException;

/**
 * A utility class to handle HTTP request/response.
 */
public class HttpClient implements java.io.Serializable {
    private final int OK = 200;
    private final int NOT_MODIFIED = 304;
    private final int UNAUTHORIZED = 401;
    private final int FORBIDDEN = 403;

    private final int INTERNAL_SERVER_ERROR = 500;
    private String userAgent =
        "twitter4j http://yusuke.homeip.net/twitter4j/ /1.0";
    private String basic;
    private int retryCount = 0;
    private int retryIntervalMillis = 10000;
    public HttpClient(String userId, String password) {
        this.basic = "Basic " +
            new String(new sun.misc.BASE64Encoder().encode((userId + ":" + password).getBytes()));
    }

    public HttpClient() {
        this.basic = null;
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

    public Response post(String url, PostParameter[] PostParameters) throws
        TwitterException {
        return httpRequest(url, PostParameters, false);
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
    /*package*/String lastURL;
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
                    con = (HttpURLConnection)new URL(url).openConnection();
                    con.setDoInput(true);
                    setHeaders(con, authenticated);
                    if (null != postParams) {
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type",
                                               "application/x-www-form-urlencoded");
                        con.setDoOutput(true);
                        StringBuffer buf = new StringBuffer();
                        for (int j = 0; j < postParams.length; j++) {
                            if (j != 0) {
                                buf.append("&");
                            }
                            buf.append(postParams[j].name).append("=").append(
                                URLEncoder.encode(postParams[j].value, "UTF-8"));
                        }
                        byte[] bytes = buf.toString().getBytes("UTF-8");
                        con.setRequestProperty("Content-Length",
                                               Integer.toString(bytes.length));
                        osw = con.getOutputStream();
                        osw.write(bytes);
                        osw.flush();
                        osw.close();
                    } else {
                        con.setRequestMethod("GET");
                    }
                    responseCode = con.getResponseCode();
                    is = con.getInputStream(); // this will throw IOException in case response code is 4xx 5xx
                    res = new Response(con.getResponseCode(), is);
                    break;
                } finally {
                    try {
                        is.close();
                    } catch (Exception ignore) {}
                    try {
                        osw.close();
                    } catch (Exception ignore) {}
                    try {
                        con.disconnect();
                    } catch (Exception ignore) {}
                }
            } catch (IOException ioe) {
                if (responseCode == UNAUTHORIZED || responseCode == FORBIDDEN) {
                    //throw TwitterException without reply since this request won't success
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

    /**
     * sets HTTP headers
     * @param connection HttpURLConnection
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

    private Map<String, String> requestHeaders = new HashMap<String, String> ();

    public void addRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    @Override public int hashCode() {
        return this.retryCount + this.retryIntervalMillis + this.basic.hashCode()
            + requestHeaders.hashCode();
    }

    @Override public boolean equals(Object obj) {
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

}
