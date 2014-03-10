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

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Field;

/**
 * Test case for HttpCient
 *
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @since Twitter4J 3.0.6
 */
public class SpdyHttpClientTest extends TestCase {

    // specify running order
    public static Test suite() {

        TestSuite suite = new TestSuite();

        suite.addTest(new SpdyHttpClientTest("testNoPreferOption"));    // must be called first
        suite.addTest(new SpdyHttpClientTest("testHttp2"));
        suite.addTest(new SpdyHttpClientTest("testSpdy"));
        suite.addTest(new SpdyHttpClientTest("testNoSpdy"));

        return suite;
    }

    public SpdyHttpClientTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() {
    }

    public void testNoPreferOption() throws Exception {
        AlternativeHttpClientImpl http = callOembed();

        // check HTTP/2.0
        Field f = http.getClass().getDeclaredField("client");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNotNull("ensure that OkHttpClient is used", client);

        ConnectionPool p = client.getConnectionPool();
        assertEquals(1, p.getConnectionCount());
        assertEquals(0, p.getHttpConnectionCount());
        assertEquals(1, p.getSpdyConnectionCount());

        assertEquals("HTTP-draft-09/2.0", http.getLastRequestProtocol());
    }

    public void testSpdy() throws Exception {
        AlternativeHttpClientImpl.sPreferSpdy = true;
        AlternativeHttpClientImpl.sPreferHttp2 = false;
        AlternativeHttpClientImpl http = callOembed();

        // check SPDY
        Field f = http.getClass().getDeclaredField("client");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNotNull("ensure that OkHttpClient is used", client);

        ConnectionPool p = client.getConnectionPool();
        assertEquals(1, p.getConnectionCount());
        assertEquals(0, p.getHttpConnectionCount());
        assertEquals(1, p.getSpdyConnectionCount());

        assertEquals("spdy/3.1", http.getLastRequestProtocol());
    }

    public void testHttp2() throws Exception {
        AlternativeHttpClientImpl.sPreferSpdy = false;
        AlternativeHttpClientImpl.sPreferHttp2 = true;
        AlternativeHttpClientImpl http = callOembed();

        // check HTTP/2.0
        Field f = http.getClass().getDeclaredField("client");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNotNull("ensure that OkHttpClient is used", client);

        ConnectionPool p = client.getConnectionPool();
        assertEquals(1, p.getConnectionCount());
        assertEquals(0, p.getHttpConnectionCount());
        assertEquals(1, p.getSpdyConnectionCount());

        assertEquals("HTTP-draft-09/2.0", http.getLastRequestProtocol());
    }

    public void testNoSpdy() throws Exception {
        AlternativeHttpClientImpl.sPreferSpdy = false;
        AlternativeHttpClientImpl.sPreferHttp2 = false;

        AlternativeHttpClientImpl http = callOembed();

        // check not SPDY
        Field f = http.getClass().getDeclaredField("client");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNull(client);     // OkHttpClient was NOT used
    }

    private AlternativeHttpClientImpl callOembed() throws TwitterException, JSONException {
        AlternativeHttpClientImpl http = new AlternativeHttpClientImpl();
        String url = "https://api.twitter.com/1/statuses/oembed.json?id=441617258578583554";

        HttpRequest req = new HttpRequest(RequestMethod.GET, url, null, null, null);
        // just to ensure the response body is consumed
        http.request(req).asJSONObject();

        return http;
    }
}
