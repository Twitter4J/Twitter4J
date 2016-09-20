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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public class HttpResponseImpl extends HttpResponse {
    private HttpURLConnection con;

    HttpResponseImpl(HttpURLConnection con, HttpClientConfiguration conf) throws IOException {
        super(conf);
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

    // for test purpose
    /*package*/ HttpResponseImpl(String content) {
        super();
        this.responseAsString = content;
    }

    @Override
    public String getResponseHeader(String name) {
        return con.getHeaderField(name);
    }

    @Override
    public Map<String, List<String>> getResponseHeaderFields() {
        return con.getHeaderFields();
    }

    @Override
    public void disconnect() {
        con.disconnect();
    }
}
