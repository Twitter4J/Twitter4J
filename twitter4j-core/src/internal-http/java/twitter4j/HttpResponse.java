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

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * A data class representing HTTP Response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class HttpResponse {
    private static final Logger logger = Logger.getLogger();
    protected final Configuration CONF;
    private HttpURLConnection con;

    HttpResponse(HttpURLConnection con, Configuration conf) throws IOException {
        this.CONF = conf;
        this.con = con;
        try {
            this.statusCode = con.getResponseCode();
        } catch (IOException e) {
            /*
             * If the user has revoked the access token in use, then Twitter naughtily returns a 401 with no "WWW-Authenticate" header.
             *
             * This causes an IOException in the getResponseCode() method call. See https://dev.twitter.com/issues/1114
             * This call can, however, me made a second time without exception.
             */
            if ("Received authentication challenge is null".equals(e.getMessage())) {
                this.statusCode = con.getResponseCode();
            } else {
                throw e;
            }
        }
        if (null == (is = con.getErrorStream())) {
            is = con.getInputStream();
        }
        if (is != null && "gzip".equals(con.getContentEncoding())) {
            // the response is gzipped
            is = new StreamingGZIPInputStream(is);
        }
    }

    HttpResponse(Configuration conf) {
        this.CONF = conf;
    }

    protected int statusCode;
    protected String responseAsString = null;
    protected InputStream is;
    private boolean streamConsumed = false;

    int getStatusCode() {
        return statusCode;
    }

    String getResponseHeader(String name) {
        return con.getHeaderField(name);
    }

    Map<String, List<String>> getResponseHeaderFields() {
        return con.getHeaderFields();
    }

    /**
     * Returns the response stream.<br>
     * This method cannot be called after calling asString() or asDcoument()<br>
     * It is suggested to call disconnect() after consuming the stream.
     * <p>
     * Disconnects the internal HttpURLConnection silently.
     *
     * @return response body stream
     * @see #disconnect()
     */
    InputStream asStream() {
        if (streamConsumed) {
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return is;
    }

    /**
     * Returns the response body as string.<br>
     * Disconnects the internal HttpURLConnection silently.
     *
     * @return response body
     * @throws TwitterException when there is any network issue upon response body consumption
     */
    String asString() throws TwitterException {
        if (null == responseAsString) {
            BufferedReader br = null;
            InputStream stream = null;
            try {
                stream = asStream();
                if (null == stream) {
                    return null;
                }
                br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                StringBuilder buf = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                this.responseAsString = buf.toString();
                logger.debug(responseAsString);
                stream.close();
                streamConsumed = true;
            } catch (IOException ioe) {
                throw new TwitterException(ioe.getMessage(), ioe);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignore) {
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ignore) {
                    }
                }
                disconnectForcibly();
            }
        }
        return responseAsString;
    }

    private JSONObject json = null;

    /**
     * Returns the response body as twitter4j.JSONObject.<br>
     * Disconnects the internal HttpURLConnection silently.
     *
     * @return response body as twitter4j.JSONObject
     * @throws TwitterException when the response body is not in JSON Object format
     */
    JSONObject asJSONObject() throws TwitterException {
        if (json == null) {
            try {
                json = new JSONObject(asString());
                if (CONF.prettyDebug) {
                    logger.debug(json.toString(1));
                } else {
                    logger.debug(responseAsString != null ? responseAsString :
                        json.toString());
                }
            } catch (JSONException jsone) {
                if (responseAsString == null) {
                    throw new TwitterException(jsone.getMessage(), jsone);
                } else {
                    throw new TwitterException(jsone.getMessage() + ":" + this.responseAsString, jsone);
                }
            } finally {
                disconnectForcibly();
            }
        }
        return json;
    }

    private JSONArray jsonArray = null;

    /**
     * Returns the response body as twitter4j.JSONArray.<br>
     * Disconnects the internal HttpURLConnection silently.
     *
     * @return response body as twitter4j.JSONArray
     * @throws TwitterException when the response body is not in JSON Array format
     */
    JSONArray asJSONArray() throws TwitterException {
        if (jsonArray == null) {
            try {
                jsonArray = new JSONArray(asString());
                if (CONF.prettyDebug) {
                    logger.debug(jsonArray.toString(1));
                } else {
                    logger.debug(responseAsString != null ? responseAsString :
                        jsonArray.toString());
                }
            } catch (JSONException jsone) {
                if (logger.isDebugEnabled()) {
                    throw new TwitterException(jsone.getMessage() + ":" + this.responseAsString, jsone);
                } else {
                    throw new TwitterException(jsone.getMessage(), jsone);
                }
            } finally {
                disconnectForcibly();
            }
        }
        return jsonArray;
    }

    @SuppressWarnings("unused")
    Reader asReader() {
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    private void disconnectForcibly() {
        try {
            disconnect();
        } catch (Exception ignore) {
        }
    }

    void disconnect() {
        con.disconnect();
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
            "statusCode=" + statusCode +
            ", responseAsString='" + responseAsString + '\'' +
            ", is=" + is +
            ", streamConsumed=" + streamConsumed +
            '}';
    }
}