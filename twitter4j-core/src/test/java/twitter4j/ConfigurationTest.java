/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@SuppressWarnings("rawtypes")
@Execution(ExecutionMode.CONCURRENT)
public class ConfigurationTest {


    @Test
    void testGetInstance() {
        Configuration conf = Configuration.getInstance();
        assertNotNull(conf);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void testConfiguration() throws Exception {

        String test = "t4j";
        String override = "system property";

        System.getProperties().remove("twitter4j.user");
        Configuration conf = new Configuration();
        assertNull(conf.user);

        conf.user(test);
        assertEquals(test, conf.user);
        System.setProperty("twitter4j.user", override);
        conf = new Configuration();
        assertEquals(override, conf.user);
        conf.user(test);
        assertEquals(test, conf.user);
        System.getProperties().remove("twitter4j.user");

        System.getProperties().remove("twitter4j.password");
        conf = new Configuration();
        assertNull(conf.password);

        conf.password(test);
        assertEquals(test, conf.password);
        System.setProperty("twitter4j.password", override);
        conf = new Configuration();
        assertEquals(override, conf.password);
        conf.password(test);
        assertEquals(test, conf.password);
        System.getProperties().remove("twitter4j.password");

        System.getProperties().remove("twitter4j.http.proxyHost");
        conf = new Configuration();
        assertNull(conf.httpProxyHost);

        System.setProperty("twitter4j.http.proxyHost", override);
        conf = new Configuration();
        assertEquals(override, conf.httpProxyHost);
        System.getProperties().remove("twitter4j.http.proxyHost");

        System.getProperties().remove("twitter4j.http.proxyPort");
        conf = new Configuration();
        assertEquals(-1, conf.httpProxyPort);

        System.setProperty("twitter4j.http.proxyPort", "100");
        conf = new Configuration();
        assertEquals(100, conf.httpProxyPort);
        System.getProperties().remove("twitter4j.http.proxyPort");


        System.getProperties().remove("twitter4j.http.proxyUser");
        conf = new Configuration();
        assertNull(conf.httpProxyUser);

        System.setProperty("twitter4j.http.proxyUser", override);
        conf = new Configuration();
        assertEquals(override, conf.httpProxyUser);
        System.getProperties().remove("twitter4j.http.proxyUser");


        System.getProperties().remove("twitter4j.http.proxyPassword");
        conf = new Configuration();
        assertNull(conf.httpProxyPassword);

        System.setProperty("twitter4j.http.proxyPassword", override);
        conf = new Configuration();
        assertEquals(override, conf.httpProxyPassword);
        System.getProperties().remove("twitter4j.http.proxyPassword");


        System.getProperties().remove("twitter4j.http.connectionTimeout");
        conf = new Configuration();
        assertEquals(20000, conf.httpConnectionTimeout);

        conf.httpConnectionTimeout(10);
        assertEquals(10, conf.httpConnectionTimeout);
        System.setProperty("twitter4j.http.connectionTimeout", "100");
        conf = new Configuration();
        assertEquals(100, conf.httpConnectionTimeout);
        conf.httpConnectionTimeout(10);
        assertEquals(10, conf.httpConnectionTimeout);
        System.getProperties().remove("twitter4j.http.connectionTimeout");


        System.getProperties().remove("twitter4j.http.readTimeout");
        conf = new Configuration();
        assertEquals(120000, conf.httpReadTimeout);

        conf.httpReadTimeout(10);
        assertEquals(10, conf.httpReadTimeout);
        System.setProperty("twitter4j.http.readTimeout", "100");
        conf = new Configuration();
        assertEquals(100, conf.httpReadTimeout);
        conf.httpReadTimeout(10);
        assertEquals(10, conf.httpReadTimeout);
        System.getProperties().remove("twitter4j.http.readTimeout");

        writeFile("./twitter4j.properties", "twitter4j.http.readTimeout=1234");
        conf = new Configuration();
        assertEquals(1234, conf.httpReadTimeout);
        writeFile("./twitter4j.properties", "twitter4j.http.readTimeout=4321");
        conf = new Configuration();
        assertEquals(4321, conf.httpReadTimeout);
        deleteFile("./twitter4j.properties");
    }


    @SuppressWarnings("rawtypes")
    @Test
    void testConfigurationBuilder() {
        deleteFile("./twitter4j.properties");
        Configuration builder;
        Configuration conf;
        builder = new Configuration();
        conf = builder.buildConfiguration();

        assertEquals(0, conf.restBaseURL.indexOf("https://"));
        assertEquals(0, conf.oAuthAuthenticationURL.indexOf("https://"));
        assertEquals(0, conf.oAuthAuthorizationURL.indexOf("https://"));
        assertEquals(0, conf.oAuthAccessTokenURL.indexOf("https://"));
        assertEquals(0, conf.oAuthRequestTokenURL.indexOf("https://"));

        builder = new Configuration();
        builder.oAuthConsumer("key", "secret");
        conf = builder.buildConfiguration();
        assertEquals(0, conf.restBaseURL.indexOf("https://"));
        assertEquals(0, conf.oAuthAuthenticationURL.indexOf("https://"));
        assertEquals(0, conf.oAuthAuthorizationURL.indexOf("https://"));
        assertEquals(0, conf.oAuthAccessTokenURL.indexOf("https://"));
        assertEquals(0, conf.oAuthRequestTokenURL.indexOf("https://"));

        RequestToken rt = new RequestToken("key", "secret");

        // TFJ-328 RequestToken.getAuthenticationURL()/getAuthorizationURL() should return URLs starting with https:// for security reasons
        assertEquals(0, rt.getAuthenticationURL().indexOf("https://"));
        assertEquals(0, rt.getAuthorizationURL().indexOf("https://"));
        assertEquals(0, conf.oAuthAccessTokenURL.indexOf("https://"));
        assertEquals(0, conf.oAuthRequestTokenURL.indexOf("https://"));

        deleteFile("./twitter4j.properties");
    }


    private static Object serializeDeserialize(Object obj) throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
        oos.writeObject(obj);
        byteOutputStream.close();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(byteInputStream);
        Object that = ois.readObject();
        byteInputStream.close();
        ois.close();
        return that;
    }

    private void writeFile(@SuppressWarnings("SameParameterValue") String path, String content) throws IOException {
        File file = new File(path);
        //noinspection ResultOfMethodCallIgnored
        file.delete();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.close();
    }

    private void deleteFile(@SuppressWarnings("SameParameterValue") String path) {
        File file = new File(path);
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }
}
