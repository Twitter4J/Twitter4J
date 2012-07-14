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

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import twitter4j.TwitterException;
import twitter4j.TwitterRuntimeException;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseCode;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

/**
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @since Twitter4J 2.2.4
 */
final class AppEngineHttpResponseImpl extends HttpResponse implements HttpResponseCode {
    private Future<HTTPResponse> future;
    private boolean responseGot;
    private Map<String, String> headers;
    private static Logger logger = Logger.getLogger(AppEngineHttpResponseImpl.class);

    AppEngineHttpResponseImpl(Future<HTTPResponse> futureResponse) {
        super(ConfigurationContext.getInstance());
        this.future = futureResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatusCode() {
        ensureResponseEvaluated();
        return statusCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseHeader(String name) {
        ensureResponseEvaluated();
        return headers.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getResponseHeaderFields() {
        ensureResponseEvaluated();
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
        ensureResponseEvaluated();
        return super.asStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString() throws TwitterException {
        ensureResponseEvaluated();
        return super.asString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JSONObject asJSONObject() throws TwitterException {
        ensureResponseEvaluated();
        return super.asJSONObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JSONArray asJSONArray() throws TwitterException {
        ensureResponseEvaluated();
        return super.asJSONArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Reader asReader() {
        ensureResponseEvaluated();
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

    private Throwable th = null;

    private void ensureResponseEvaluated() {
        if (th != null) {
            throw new TwitterRuntimeException(th);
        }
        if (responseGot) {
            return;
        }
        responseGot = true;
        if (future.isCancelled()) {
            th = new TwitterException("HttpResponse already disconnected.");
            throw new TwitterRuntimeException(th);
        }
        try {
            HTTPResponse r = future.get();
            statusCode = r.getResponseCode();
            headers = new HashMap<String, String>();
            for (HTTPHeader h : r.getHeaders()) {
                headers.put(h.getName().toLowerCase(Locale.ENGLISH), h.getValue());
            }
            byte[] content = r.getContent();
            is = new ByteArrayInputStream(content);
            if ("gzip".equals(headers.get("content-encoding"))) {
                // the response is gzipped
                try {
                    is = new GZIPInputStream(is);
                } catch (IOException e) {
                    th = e;
                    throw new TwitterRuntimeException(th);
                }
            }
            responseAsString = inputStreamToString(is);
            if (statusCode < OK || (statusCode != FOUND && MULTIPLE_CHOICES <= statusCode)) {
                if (statusCode == ENHANCE_YOUR_CLAIM ||
                        statusCode == BAD_REQUEST ||
                        statusCode < INTERNAL_SERVER_ERROR) {
                    th = new TwitterException(responseAsString, null, statusCode);
                    throw new TwitterRuntimeException(th);
                }
            }
        } catch (ExecutionException e) {
            th = e.getCause();
        } catch (InterruptedException e) {
            th = e.getCause();
        }
        if (th != null) {
            throw new TwitterRuntimeException(th);
        }
    }

    private String inputStreamToString(InputStream is) {
        if (responseAsString == null) {
            StringBuilder buf = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    buf.append(line);
                }
            } catch (IOException e) {
                return null;
            }
            responseAsString = buf.toString();
        }
        return responseAsString;
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
