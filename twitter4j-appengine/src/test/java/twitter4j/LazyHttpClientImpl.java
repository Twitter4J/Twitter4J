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
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class LazyHttpClientImpl extends HttpClientImpl {
    private static final long serialVersionUID = 7325894890960046422L;
    private final HttpClient client;

    public LazyHttpClientImpl(HttpClientConfiguration conf) {
        client = new AlternativeHttpClientImpl(conf);
    }

    @Override
    public HttpResponse handleRequest(HttpRequest req) throws TwitterException {
        return new LazyHttpResponseImpl(client, req);
    }

}

class LazyHttpResponseImpl extends HttpResponse {
    private HttpClient client;
    private HttpRequest req;
    private HttpResponse actualResponse = null;

    LazyHttpResponseImpl(HttpClient client, HttpRequest req) {
        this.client = client;
        this.req = req;
    }

    @Override
    public Map<String, List<String>> getResponseHeaderFields() {
        ensure();
        return actualResponse.getResponseHeaderFields();
    }

    @Override
    public void disconnect() throws IOException {
        ensure();
        actualResponse.disconnect();
    }

    public int getStatusCode() {
        ensure();
        return actualResponse.getStatusCode();
    }

    public String getResponseHeader(String name) {
        ensure();
        return actualResponse.getResponseHeader(name);
    }

    public InputStream asStream() {
        ensure();
        return actualResponse.asStream();
    }

    public String asString() throws TwitterException {
        ensure();
        return actualResponse.asString();
    }

    public JSONObject asJSONObject() throws TwitterException {
        ensure();
        return actualResponse.asJSONObject();
    }

    public JSONArray asJSONArray() throws TwitterException {
        ensure();
        return actualResponse.asJSONArray();
    }

    public Reader asReader() {
        ensure();
        return actualResponse.asReader();
    }

    private void ensure() {
        if (actualResponse == null) {
            try {
                actualResponse = client.request(req);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
    }
}