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

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import junit.framework.TestCase;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.internal.http.HttpRequest;
import twitter4j.internal.http.RequestMethod;
import twitter4j.internal.http.alternative.HttpClientImpl;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import com.squareup.okhttp.OkHttpClient;

/**
 * Test case for HttpCient
 *
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @since Twitter4J 3.x.x
 */
public class SpdyHttpClientTest extends TestCase {
    
    protected Properties p = new Properties();
    protected Configuration conf;
    
    public SpdyHttpClientTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        
        InputStream is = SpdyHttpClientTest.class.getResourceAsStream("/test.properties");
        p.load(is);
        is.close();
        
        conf = new PropertyConfiguration(p);
    }

    protected void tearDown() {
    }
    
    public void testSpdy() throws Exception {
        HttpClientImpl http = doSomeRequest();
        
        // check SPDY
        Field f = http.getClass().getDeclaredField("client");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNotNull(client);  // OkHttpClient was used
        
//        ConnectionPool p = client.getConnectionPool();
//        assertEquals(1, p.getConnectionCount());
//        assertEquals(0, p.getHttpConnectionCount());
//        assertEquals(1, p.getSpdyConnectionCount());
    }
    
    public void testNoSpdy() throws Exception {
        HttpClientImpl.sPreferSpdy = false;
        
        HttpClientImpl http = doSomeRequest();
        
        // check not SPDY
        Field f = http.getClass().getDeclaredField("client");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNull(client);     // OkHttpClient was NOT used
    }

    private HttpClientImpl doSomeRequest() throws TwitterException, JSONException {
        
        HttpClientImpl http = new HttpClientImpl(conf);
        String url = "https://api.twitter.com/1.1/account/verify_credentials.json";
        
        OAuthAuthorization oauth = new OAuthAuthorization(conf);
        String accessToken = conf.getOAuthAccessToken();
        String accessTokenSecret = conf.getOAuthAccessTokenSecret();
        oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
        
        HttpRequest req = new HttpRequest(RequestMethod.GET, url, null, oauth, null);
        JSONObject json = http.request(req).asJSONObject();
        System.out.println(json.getString("screen_name"));
        
        return http;
    }
}
