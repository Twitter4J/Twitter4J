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

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpClientConfiguration;
import twitter4j.internal.http.HttpRequest;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseCode;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.z_T4JInternalStringUtil;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.*;
import java.security.GeneralSecurityException;

/**
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @since Twitter4J 3.0.6
 */
public class HttpClientImpl extends twitter4j.internal.http.HttpClientImpl implements HttpResponseCode, java.io.Serializable {
    private static final Logger logger = Logger.getLogger(HttpClientImpl.class);

    private static final int MAX_IDLE_CONNECTIONS = 5;

    private static final long KEEP_ALIVE_DURATION_MS = 300;

    public static boolean sPreferSpdy = true;

    private OkHttpClient client = null;

    private String lastRequestProtocol = null;
    
    public HttpClientImpl() {
        super();
    }

    public HttpClientImpl(HttpClientConfiguration conf) {
        super(conf);
    }
    
    protected HttpURLConnection getConnection(String url) throws IOException {
        if (!sPreferSpdy) {
            return super.getConnection(url);
        }
        
        prepareClient();
        
        HttpURLConnection con;
        if (isProxyConfigured()) {
            if (CONF.getHttpProxyUser() != null && !CONF.getHttpProxyUser().equals("")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Proxy AuthUser: " + CONF.getHttpProxyUser());
                    logger.debug("Proxy AuthPassword: " + z_T4JInternalStringUtil.maskString(CONF.getHttpProxyPassword()));
                }
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        //respond only to proxy auth requests
                        if (getRequestorType().equals(RequestorType.PROXY)) {
                            return new PasswordAuthentication(CONF.getHttpProxyUser(),
                                    CONF.getHttpProxyPassword().toCharArray());
                        } else {
                            return null;
                        }
                    }
                });
            }
            final Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress
                    .createUnresolved(CONF.getHttpProxyHost(), CONF.getHttpProxyPort()));
            if (logger.isDebugEnabled()) {
                logger.debug("Opening proxied connection(" + CONF.getHttpProxyHost() + ":" + CONF.getHttpProxyPort() + ")");
            }
            client.setProxy(proxy);
        }
        con = client.open(new URL(url));
        
        if (CONF.getHttpConnectionTimeout() > 0) {
            con.setConnectTimeout(CONF.getHttpConnectionTimeout());
        }
        if (CONF.getHttpReadTimeout() > 0) {
            con.setReadTimeout(CONF.getHttpReadTimeout());
        }
        con.setInstanceFollowRedirects(false);
        return con;
    }
    
    @Override
    public HttpResponse request(HttpRequest req) throws TwitterException {
        HttpResponse res = super.request(req);
        
        if (res != null) {
            lastRequestProtocol = res.getResponseHeader("OkHttp-Selected-Protocol");
        }
        
        return res;
    }
    
    public String getLastRequestProtocol() {
        return lastRequestProtocol;
    }
    
    private void prepareClient() {
        
        if (client == null) {
            client = new OkHttpClient();
            
            // if not set connection-pool, 
            // it used the default pool invalidly that is disabled the keep-alive feature for TFJ-296.
            client.setConnectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION_MS));
            
            // to avoid the crash default to using a single global SSL context.
            // see https://github.com/square/okhttp/issues/184#issuecomment-18772733
            SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
            } catch (GeneralSecurityException e) {
                throw new AssertionError(); // The system has no TLS. Just give up.
            }
            client.setSslSocketFactory(sslContext.getSocketFactory());
        }
    }
}
