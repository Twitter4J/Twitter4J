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

package twitter4j.internal.http;

import twitter4j.TwitterException;
import twitter4j.auth.Authorization;
import twitter4j.conf.ConfigurationContext;

import java.util.HashMap;
import java.util.Map;

import static twitter4j.internal.http.RequestMethod.*;

/**
 * HTTP Client wrapper with handy request methods, ResponseListener mechanism
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class HttpClientWrapper implements java.io.Serializable {
    private final HttpClientWrapperConfiguration wrapperConf;
    private HttpClient http;

    private final Map<String, String> requestHeaders;
    private static final long serialVersionUID = -6511977105603119379L;
    private HttpResponseListener httpResponseListener;

    public HttpClientWrapper(HttpClientWrapperConfiguration wrapperConf) {
        this.wrapperConf = wrapperConf;
        requestHeaders = wrapperConf.getRequestHeaders();
        http = HttpClientFactory.getInstance(wrapperConf);
    }

    // never used with this project. Just for handiness for those using this class.
    public HttpClientWrapper() {
        this.wrapperConf = ConfigurationContext.getInstance();
        requestHeaders = wrapperConf.getRequestHeaders();
        http = HttpClientFactory.getInstance(wrapperConf);
    }

    public void shutdown() {
        http.shutdown();
    }

    private HttpResponse request(HttpRequest req) throws TwitterException {
        HttpResponse res;
        try {
            res = http.request(req);
            //fire HttpResponseEvent
            if (httpResponseListener != null) {
                httpResponseListener.httpResponseReceived(new HttpResponseEvent(req, res, null));
            }
        } catch (TwitterException te) {
            if (httpResponseListener != null) {
                httpResponseListener.httpResponseReceived(new HttpResponseEvent(req, null, te));
            }
            throw te;
        }
        return res;
    }

    public void setHttpResponseListener(HttpResponseListener listener) {
        httpResponseListener = listener;
    }

    public HttpResponse get(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException {
        return request(new HttpRequest(GET, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse get(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(GET, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse get(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(GET, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(GET, url, null, null, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException {
        return request(new HttpRequest(POST, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(POST, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters, Map<String, String> requestHeaders) throws TwitterException {
        Map<String, String> headers = new HashMap<String, String>(this.requestHeaders);
        if (requestHeaders != null)
            headers.putAll(requestHeaders);

        return request(new HttpRequest(POST, url, parameters, null, headers));
    }

    public HttpResponse post(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(POST, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse post(String url) throws TwitterException {
        return request(new HttpRequest(POST, url, null, null, this.requestHeaders));
    }

    public HttpResponse delete(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException {
        return request(new HttpRequest(DELETE, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse delete(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(DELETE, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse delete(String url,
                               Authorization authorization) throws TwitterException {
        return request(new HttpRequest(DELETE, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse delete(String url) throws TwitterException {
        return request(new HttpRequest(DELETE, url, null, null, this.requestHeaders));
    }

    public HttpResponse head(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException {
        return request(new HttpRequest(HEAD, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse head(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(HEAD, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse head(String url
            , Authorization authorization) throws TwitterException {
        return request(new HttpRequest(HEAD, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse head(String url) throws TwitterException {
        return request(new HttpRequest(HEAD, url, null, null, this.requestHeaders));
    }

    public HttpResponse put(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException {
        return request(new HttpRequest(PUT, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse put(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(PUT, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse put(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(PUT, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse put(String url) throws TwitterException {
        return request(new HttpRequest(PUT, url, null, null, this.requestHeaders));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpClientWrapper that = (HttpClientWrapper) o;

        if (!http.equals(that.http)) return false;
        if (!requestHeaders.equals(that.requestHeaders)) return false;
        if (!wrapperConf.equals(that.wrapperConf)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = wrapperConf.hashCode();
        result = 31 * result + http.hashCode();
        result = 31 * result + requestHeaders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HttpClientWrapper{" +
                "wrapperConf=" + wrapperConf +
                ", http=" + http +
                ", requestHeaders=" + requestHeaders +
                ", httpResponseListener=" + httpResponseListener +
                '}';
    }
}
