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
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

import java.util.Map;

import static twitter4j.http.RequestMethod.DELETE;
import static twitter4j.http.RequestMethod.GET;
import static twitter4j.http.RequestMethod.HEAD;
import static twitter4j.http.RequestMethod.POST;
import static twitter4j.http.RequestMethod.PUT;

/**
 * HTTP Client wrapper with handy request methods, ResponseListener mechanism 
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class HttpClientWrapper implements java.io.Serializable {
    private final HttpClientWrapperConfiguration wrapperConf;
    private HttpClient http;

    private final Map<String, String> requestHeaders;
    private static final long serialVersionUID = -6511977105603119379L;
    private HttpResponseListener httpResponseListener;

    public HttpClientWrapper(HttpClientWrapperConfiguration wrapperConf) {
        this.wrapperConf = wrapperConf;
        requestHeaders = wrapperConf.getRequestHeaders();
        http = new HttpClient(wrapperConf);
    }
    // never used with this project. Just for handiness for those using this class.
    public HttpClientWrapper() {
        this.wrapperConf = ConfigurationContext.getInstance();
        requestHeaders = wrapperConf.getRequestHeaders();
        http = new HttpClient(wrapperConf);
    }
    private HttpResponse request(HttpRequest req) throws TwitterException {
        HttpResponse res = http.request(req);
        //fire HttpResponseEvent
        if (null != httpResponseListener) {
            httpResponseListener.httpResponseReceived(new HttpResponseEvent(http, req, res));
        }
        return res;
    }

    public void setHttpResponseListener(HttpResponseListener listener) {
        httpResponseListener = listener;
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
