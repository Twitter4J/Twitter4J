/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.internal.http.alternative;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpClientConfiguration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpRequest;
import twitter4j.internal.http.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient implementation for Apache HttpClient 4.0.x
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public class HttpClientImpl implements twitter4j.internal.http.HttpClient {
    private final HttpClientConfiguration conf;
    private final HttpClient client;

    public HttpClientImpl(HttpClientConfiguration conf) {
        this.conf = conf;

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                 new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(
                 new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
        cm.setMaxTotalConnections(conf.getHttpMaxTotalConnections());
        cm.setDefaultMaxPerRoute(conf.getHttpDefaultMaxPerRoute());
        client = new DefaultHttpClient(cm);
    }

    public void shutdown() {
      client.getConnectionManager().shutdown();
    }

    public twitter4j.internal.http.HttpResponse request(twitter4j.internal.http.HttpRequest req) throws TwitterException {
        try {
            HttpRequestBase commonsRequest = null;

            if (req.getMethod() == RequestMethod.GET) {
                commonsRequest = new HttpGet(composeURL(req));

            } else if (req.getMethod() == RequestMethod.POST) {
                HttpPost post = new HttpPost(req.getURL());
                // parameter has a file?
                boolean hasFile = false;
                if (null != req.getParameters()) {
                    for (HttpParameter parameter : req.getParameters()) {
                        if (parameter.isFile()) {
                            hasFile = true;
                            break;
                        }
                    }
                    if (!hasFile) {
                        List<NameValuePair> nameValuePair = asNameValuePairList(req);
                        if (null != nameValuePair) {
                            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePair, "UTF-8");
                            post.setEntity(entity);
                        }
                    } else {
                        MultipartEntity me = new MultipartEntity();
                        for (HttpParameter parameter : req.getParameters()) {
                            if (parameter.isFile()) {
                                me.addPart(parameter.getName(), new FileBody(parameter.getFile(), parameter.getContentType()));
                            } else {
                                me.addPart(parameter.getName(), new StringBody(parameter.getValue()));
                            }
                        }
                        post.setEntity(me);

                    }
                }
                post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
                commonsRequest = post;
            } else if (req.getMethod() == RequestMethod.DELETE) {
                commonsRequest = new HttpDelete(composeURL(req));
            } else if (req.getMethod() == RequestMethod.HEAD) {
                commonsRequest = new HttpHead(composeURL(req));
            } else if (req.getMethod() == RequestMethod.PUT) {
                commonsRequest = new HttpPut(composeURL(req));
            } else {
                throw new AssertionError();
            }
            Map<String, String> headers = req.getRequestHeaders();
            for (String headerName : headers.keySet()) {
                commonsRequest.addHeader(headerName, headers.get(headerName));
            }
            String authorizationHeader;
            if (null != req.getAuthorization()
                    && null != (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req))) {
                commonsRequest.addHeader("Authorization", authorizationHeader);
            }

            ApacheHttpClientHttpResponseImpl res = new ApacheHttpClientHttpResponseImpl(client.execute(commonsRequest));
            if(200 != res.getStatusCode()){
                throw new TwitterException(res.asString() ,res);
            }
            return res;
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }
    private String composeURL(HttpRequest req){
        List<NameValuePair> params = asNameValuePairList(req);
        if (null != params) {
            return req.getURL() + "?" + URLEncodedUtils.format(params, "UTF-8");
        } else {
            return req.getURL();
        }
    }
    private List<NameValuePair> asNameValuePairList(HttpRequest req){
        if (null != req.getParameters() && req.getParameters().length > 0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (HttpParameter parameter : req.getParameters()) {
                params.add(new BasicNameValuePair(parameter.getName(), parameter.getValue()));
            }
            return params;
        }else{
            return null;
        }
    }
}
