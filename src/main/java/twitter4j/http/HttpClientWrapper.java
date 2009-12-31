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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static twitter4j.http.RequestMethod.DELETE;
import static twitter4j.http.RequestMethod.GET;
import static twitter4j.http.RequestMethod.HEAD;
import static twitter4j.http.RequestMethod.POST;
import static twitter4j.http.RequestMethod.PUT;

/**
 * HTTP Client wrapper.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class HttpClientWrapper implements java.io.Serializable {
    private final Map<String, String> requestHeaders;
    private static final long serialVersionUID = -6511977105603119379L;
    private List<HttpResponseListener> httpResponseListeners;

    public HttpClientWrapper(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    private static final Map<HttpRequestFactoryConfiguration, HttpClientWrapper> instanceMap = new HashMap<HttpRequestFactoryConfiguration, HttpClientWrapper>(1);

    private HttpClientWrapper(HttpRequestFactoryConfiguration conf) {
        this.requestHeaders = conf.getRequestHeaders();
    }
    private HttpClient http;

    public static HttpClientWrapper getInstance(HttpRequestFactoryConfiguration conf, HttpClientConfiguration httpConf) {
        HttpClientWrapper wrapper = instanceMap.get(conf);
        if (null == wrapper) {
            wrapper = new HttpClientWrapper(conf);
            instanceMap.put(conf, wrapper);
        }
        wrapper.http = new HttpClient(httpConf);
        return wrapper;
    }
    private HttpResponse request(HttpRequest req) throws TwitterException {
        HttpResponse res = http.request(req);
        fireHttpResponseEvent(new HttpResponseEvent(http, req, res));
        return res;
    }

    private HttpResponse fireHttpResponseEvent(HttpResponseEvent httpResponseEvent) {
        if (null != httpResponseListeners) {
            for (HttpResponseListener listener : httpResponseListeners) {
                listener.httpResponseReceived(httpResponseEvent);
            }
        }
        return httpResponseEvent.getResponse();
    }
    public void addHttpResponseListener(HttpResponseListener listener) {
        if (null == httpResponseListeners) {
            httpResponseListeners = new ArrayList<HttpResponseListener>();
        }
        httpResponseListeners.add(listener);
    }

    public HttpResponse get(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException{
        return request(new HttpRequest(GET, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse get(String url, HttpParameter[] parameters) throws TwitterException{
        return request(new HttpRequest(GET, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse get(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(GET, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse get(String url) throws TwitterException{
        return request(new HttpRequest(GET, url, null, null, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException{
        return request(new HttpRequest(POST, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters) throws TwitterException{
        return request(new HttpRequest(POST, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse post(String url, Authorization authorization) throws TwitterException{
        return request(new HttpRequest(POST, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse post(String url) throws TwitterException{
        return request(new HttpRequest(POST, url, null, null, this.requestHeaders));
    }

    public HttpResponse delete(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException{
        return request(new HttpRequest(DELETE, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse delete(String url, HttpParameter[] parameters) throws TwitterException{
        return request(new HttpRequest(DELETE, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse delete(String url,
                              Authorization authorization) throws TwitterException{
        return request(new HttpRequest(DELETE, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse delete(String url) throws TwitterException{
        return request(new HttpRequest(DELETE, url, null, null, this.requestHeaders));
    }

    public HttpResponse head(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException{
        return request(new HttpRequest(HEAD, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse head(String url, HttpParameter[] parameters) throws TwitterException{
        return request(new HttpRequest(HEAD, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse head(String url
            , Authorization authorization) throws TwitterException{
        return request(new HttpRequest(HEAD, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse head(String url) throws TwitterException{
        return request(new HttpRequest(HEAD, url, null, null, this.requestHeaders));
    }

    public HttpResponse put(String url, HttpParameter[] parameters
            , Authorization authorization) throws TwitterException{
        return request(new HttpRequest(PUT, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse put(String url, HttpParameter[] parameters) throws TwitterException{
        return request(new HttpRequest(PUT, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse put(String url, Authorization authorization) throws TwitterException{
        return request(new HttpRequest(PUT, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse put(String url) throws TwitterException{
        return request(new HttpRequest(PUT, url, null, null, this.requestHeaders));
    }

}
