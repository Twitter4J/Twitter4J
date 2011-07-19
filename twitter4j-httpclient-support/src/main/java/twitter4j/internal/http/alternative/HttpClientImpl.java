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

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpClientConfiguration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpRequest;
import twitter4j.internal.http.RequestMethod;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.z_T4JInternalStringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient implementation for Apache HttpClient 4.0.x
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public class HttpClientImpl implements twitter4j.internal.http.HttpClient {
    private static final Logger logger = Logger.getLogger(HttpClientImpl.class);
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
        cm.setMaxTotal(conf.getHttpMaxTotalConnections());
        cm.setDefaultMaxPerRoute(conf.getHttpDefaultMaxPerRoute());
        DefaultHttpClient client = new DefaultHttpClient(cm);
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, conf.getHttpConnectionTimeout());
        HttpConnectionParams.setSoTimeout(params, conf.getHttpReadTimeout());

        if (conf.getHttpProxyHost() != null && !conf.getHttpProxyHost().equals("")) {
            HttpHost proxy = new HttpHost(conf.getHttpProxyHost(), conf.getHttpProxyPort());
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            if (conf.getHttpProxyUser() != null && !conf.getHttpProxyUser().equals("")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Proxy AuthUser: " + conf.getHttpProxyUser());
                    logger.debug("Proxy AuthPassword: " + z_T4JInternalStringUtil.maskString(conf.getHttpProxyPassword()));
                }
                client.getCredentialsProvider().setCredentials(
                        new AuthScope(conf.getHttpProxyHost(), conf.getHttpProxyPort()),
                        new UsernamePasswordCredentials(conf.getHttpProxyUser(), conf.getHttpProxyPassword()));
            }
        }
        this.client = client;
    }

    public void shutdown() {
        client.getConnectionManager().shutdown();
    }

    public twitter4j.internal.http.HttpResponse request(twitter4j.internal.http.HttpRequest req) throws TwitterException {
        try {
            HttpRequestBase commonsRequest;

            if (req.getMethod() == RequestMethod.GET) {
                commonsRequest = new HttpGet(composeURL(req));

            } else if (req.getMethod() == RequestMethod.POST) {
                HttpPost post = new HttpPost(req.getURL());
                // parameter has a file?
                boolean hasFile = false;
                if (req.getParameters() != null) {
                    for (HttpParameter parameter : req.getParameters()) {
                        if (parameter.isFile()) {
                            hasFile = true;
                            break;
                        }
                    }
                    if (!hasFile) {
                        List<NameValuePair> nameValuePair = asNameValuePairList(req);
                        if (nameValuePair != null) {
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
            if (req.getAuthorization() != null
                    && (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req)) != null) {
                commonsRequest.addHeader("Authorization", authorizationHeader);
            }

            ApacheHttpClientHttpResponseImpl res = new ApacheHttpClientHttpResponseImpl(client.execute(commonsRequest), conf);
            if (200 != res.getStatusCode()) {
                throw new TwitterException(res.asString(), res);
            }
            return res;
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    private String composeURL(HttpRequest req) {
        List<NameValuePair> params = asNameValuePairList(req);
        if (params != null) {
            return req.getURL() + "?" + URLEncodedUtils.format(params, "UTF-8");
        } else {
            return req.getURL();
        }
    }

    private List<NameValuePair> asNameValuePairList(HttpRequest req) {
        if (req.getParameters() != null && req.getParameters().length > 0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (HttpParameter parameter : req.getParameters()) {
                params.add(new BasicNameValuePair(parameter.getName(), parameter.getValue()));
            }
            return params;
        } else {
            return null;
        }
    }
}
