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
import com.squareup.okhttp.Protocol;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Field;

/**
 * Test case for HttpCient
 *
 * @author Yuuto Uehara
 * @since Twitter4J 4.0.3
 */
public class HttpClientTest extends TestCase {

    // specify running order
    public static Test suite() {

        TestSuite suite = new TestSuite();

        suite.addTest(new HttpClientTest("testNoOption"));
        suite.addTest(new HttpClientTest("testHttp2"));
        suite.addTest(new HttpClientTest("testSpdy"));
        suite.addTest(new HttpClientTest("testHttp1_1"));

        return suite;
    }

    public HttpClientTest(String name) {
        super(name);
    }

    protected void tearDown() {
    }

    public void testNoOption() throws Exception {
        AlternativeHttpClientImpl http = callOembed();

        Field f = http.getClass().getDeclaredField("okHttpClient");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNotNull("ensure that OkHttpClient is used", client);

        ConnectionPool p = client.getConnectionPool();
        assertEquals(1, p.getConnectionCount());
        assertEquals(0, p.getHttpConnectionCount());
        assertEquals(1, p.getSpdyConnectionCount());

        assertEquals(Protocol.SPDY_3.toString(), http.getLastRequestProtocol());
    }

    public void testSpdy() throws Exception {
        AlternativeHttpClientImpl.sPreferSpdy = true;
        AlternativeHttpClientImpl.sPreferHttp2 = false;
        AlternativeHttpClientImpl http = callOembed();

        Field f = http.getClass().getDeclaredField("okHttpClient");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNotNull("ensure that OkHttpClient is used", client);

        ConnectionPool p = client.getConnectionPool();
        assertEquals(1, p.getConnectionCount());
        assertEquals(0, p.getHttpConnectionCount());
        assertEquals(1, p.getSpdyConnectionCount());

        assertEquals(Protocol.SPDY_3.toString(), http.getLastRequestProtocol());
    }

    public void testHttp2() throws Exception {
        AlternativeHttpClientImpl.sPreferSpdy = false;
        AlternativeHttpClientImpl.sPreferHttp2 = true;
        AlternativeHttpClientImpl http = callOembed();

        Field f = http.getClass().getDeclaredField("okHttpClient");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
        assertNotNull("ensure that OkHttpClient is used", client);

        ConnectionPool p = client.getConnectionPool();
        assertEquals(1, p.getConnectionCount());
        assertEquals(1, p.getHttpConnectionCount());
        assertEquals(0, p.getSpdyConnectionCount());

        assertEquals(Protocol.HTTP_2.toString(), http.getLastRequestProtocol());
    }

    public void testHttp1_1() throws Exception {
        AlternativeHttpClientImpl.sPreferSpdy = false;
        AlternativeHttpClientImpl.sPreferHttp2 = false;

        AlternativeHttpClientImpl http = callOembed();

        Field f = http.getClass().getDeclaredField("okHttpClient");
        f.setAccessible(true);
        OkHttpClient client = (OkHttpClient) f.get(http);
		assertNotNull("ensure that OkHttpClient is used", client);

		ConnectionPool p = client.getConnectionPool();
		assertEquals(1, p.getConnectionCount());
		assertEquals(1, p.getHttpConnectionCount());
		assertEquals(0, p.getSpdyConnectionCount());

		assertEquals(Protocol.HTTP_1_1.toString(), http.getLastRequestProtocol());
    }

    private AlternativeHttpClientImpl callOembed() throws JSONException {
        AlternativeHttpClientImpl http = new AlternativeHttpClientImpl();
        String url = "https://api.twitter.com/1.1/statuses/oembed.json?id=441617258578583554";

        HttpRequest req = new HttpRequest(RequestMethod.GET, url, null, null, null);
        // just to ensure the response body is consumed
		try {
			http.request(req).asJSONObject();
		}catch (TwitterException e){

		}

        return http;
    }
}
