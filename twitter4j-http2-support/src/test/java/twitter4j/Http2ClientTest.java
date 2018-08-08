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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

import java.lang.reflect.Field;

/**
 * Test case for HttpCient
 * The fllowing argument is requred to run this test case:
 * -Xbootclasspath/p:~/.m2/repository/org/mortbay/jetty/alpn/alpn-boot/8.1.12.v20180117/alpn-boot-8.1.12.v20180117.jar
 *
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @since Twitter4J 3.0.6
 */
public class Http2ClientTest extends TestCase {

    static boolean alpnBootJarFoundInBootClassPath;

    // specify running order
    public static Test suite() {

        TestSuite suite = new TestSuite();
        alpnBootJarFoundInBootClassPath = System.getProperty("sun.boot.class.path").matches(".*alpn-boot-[0-9.]+\\.v[0-9]+\\.jar.*");
        if (alpnBootJarFoundInBootClassPath) {
            System.out.println("alpn jar found in boot classpath.");
        } else {
            System.out.println("alpn jar not found in boot classpath.");
        }

        suite.addTest(new Http2ClientTest("testNoPreferOption"));    // must be called first
        suite.addTest(new Http2ClientTest("testHttp2"));

        return suite;
    }

    private Http2ClientTest(String name) {
        super(name);
    }

    protected void tearDown() {
    }

    public void testNoPreferOption() throws Exception {
        if (alpnBootJarFoundInBootClassPath) {
            AlternativeHttpClientImpl http = callOembed();

            // check HTTP/2.0
            Field f = http.getClass().getDeclaredField("okHttpClient");
            f.setAccessible(true);
            OkHttpClient client = (OkHttpClient) f.get(http);
            assertNotNull("ensure that OkHttpClient is used", client);

            ConnectionPool p = client.connectionPool();
            assertEquals(1, p.connectionCount());

            assertEquals(Protocol.HTTP_2, http.getLastRequestProtocol());
        }
    }

    public void testHttp2() throws Exception {
        if (alpnBootJarFoundInBootClassPath) {
            AlternativeHttpClientImpl.sPreferSpdy = false;
            AlternativeHttpClientImpl.sPreferHttp2 = true;
            AlternativeHttpClientImpl http = callOembed();

            // check HTTP/2.0
            Field f = http.getClass().getDeclaredField("okHttpClient");
            f.setAccessible(true);
            OkHttpClient client = (OkHttpClient) f.get(http);
            assertNotNull("ensure that OkHttpClient is used", client);

            ConnectionPool p = client.connectionPool();
            assertEquals(1, p.connectionCount());

            assertEquals(Protocol.HTTP_2, http.getLastRequestProtocol());
        }
    }

    private AlternativeHttpClientImpl callOembed() throws TwitterException {
        AlternativeHttpClientImpl http = new AlternativeHttpClientImpl();
        String url = "https://api.twitter.com/1/statuses/oembed.json?id=441617258578583554";

        HttpRequest req = new HttpRequest(RequestMethod.GET, url, null, null, null);
        // just to ensure the response body is consumed
        http.request(req).asJSONObject();

        return http;
    }

    public void testUploadMediaFromStream() throws Exception {
        if (alpnBootJarFoundInBootClassPath) {
            Twitter twitter = TwitterFactory.getSingleton();
            UploadedMedia media2 = twitter.uploadMedia("fromInputStream",
                    Http2ClientTest.class.getResourceAsStream("/twitter4j.jpg"));

            StatusUpdate update = new StatusUpdate("from input stream");
            update.setMediaIds(media2.getMediaId());
            Status status = twitter.updateStatus(update);
            assertEquals("from input stream", status.getText());
        }
    }
}
