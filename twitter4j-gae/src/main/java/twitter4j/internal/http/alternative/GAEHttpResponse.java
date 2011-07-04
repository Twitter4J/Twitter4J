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
package twitter4j.internal.http.alternative;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.http.HttpResponse;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONObject;

/**
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @since Twitter4J 2.2.4
 */
class GAEHttpResponse extends HttpResponse {
    private Future<HTTPResponse> future;
    private boolean responseGot;
    private Map<String, String> headers;
    private static Logger logger = Logger.getLogger(GAEHttpResponse.class);

    GAEHttpResponse(Future<HTTPResponse> futureResponse) {
        super(ConfigurationContext.getInstance());
        this.future = futureResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseHeader(String name) {
        ensureResponse();
        return headers.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getResponseHeaderFields() {
        ensureResponse();
        Map<String, List<String>> ret = new TreeMap<String, List<String>>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            ret.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream asStream() {
        ensureResponse();
        return super.asStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString() throws TwitterException {
        ensureResponse();
        return super.asString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JSONObject asJSONObject() throws TwitterException {
        ensureResponse();
        return super.asJSONObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JSONArray asJSONArray() throws TwitterException {
        ensureResponse();
        return super.asJSONArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Reader asReader() {
        ensureResponse();
        return super.asReader();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() throws IOException {
        if (!future.isDone() && !future.isCancelled()) {
            future.cancel(true);
        }
    }

    private void ensureResponse() {
        if (responseGot) return;
        logger.debug("ensureResponse called");
        if (future.isCancelled())
            throw new IllegalStateException("HttpResponse already disconnected.");
        try {
            HTTPResponse r = future.get();
            headers = new HashMap<String, String>();
            for (HTTPHeader h : r.getHeaders()) {
                headers.put(h.getName(), h.getValue());
            }
            statusCode = r.getResponseCode();
            byte[] content = r.getContent();
            if (logger.isDebugEnabled()) {
                logger.debug(new String(content, "UTF-8"));
            }
            is = new ByteArrayInputStream(content);
            responseGot = true;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "GAEHttpResponse{" +
                "future=" + future +
                ", responseGot=" + responseGot +
                ", headers=" + headers +
                '}';
    }
}
