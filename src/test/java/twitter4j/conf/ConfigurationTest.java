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
package twitter4j.conf;

import junit.framework.Assert;
import junit.framework.TestCase;
import twitter4j.Version;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class ConfigurationTest  extends TestCase {

    public ConfigurationTest(String name) {
        super(name);
    }


    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testGetInstance() throws Exception {
        Configuration conf = ConfigurationContext.getInstance();
        assertNotNull(conf);
    }
    public void testFixURL() throws Exception {
        assertEquals("http://www.bea.com", ConfigurationBase.fixURL(false, "http://www.bea.com"));
        assertEquals("http://www.bea.com", ConfigurationBase.fixURL(false, "https://www.bea.com"));
        assertEquals("https://www.bea.com", ConfigurationBase.fixURL(true, "http://www.bea.com"));
        assertEquals("https://www.bea.com", ConfigurationBase.fixURL(true, "https://www.bea.com"));
        assertNull(ConfigurationBase.fixURL(false, null));
        assertNull(ConfigurationBase.fixURL(true, null));
    }

    public void testConfiguration() throws Exception {
        ConfigurationBase conf = new PropertyConfiguration();

        String test = "t4j";
        String override = "system property";


        System.getProperties().remove("twitter4j.user");
        conf = new PropertyConfiguration();
        assertNull(conf.getUser());

        conf.setUser(test);
        assertEquals(test, conf.getUser());
        System.setProperty("twitter4j.user", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getUser());
        conf.setUser(test);
        assertEquals(test, conf.getUser());
        System.getProperties().remove("twitter4j.user");

        System.getProperties().remove("twitter4j.password");
        conf = new PropertyConfiguration();
        assertNull(conf.getPassword());

        conf.setPassword(test);
        assertEquals(test, conf.getPassword());
        System.setProperty("twitter4j.password", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getPassword());
        conf.setPassword(test);
        assertEquals(test, conf.getPassword());
        System.getProperties().remove("twitter4j.password");


        System.getProperties().remove("twitter4j.source");
        conf = new PropertyConfiguration();
        assertEquals("Twitter4J", conf.getSource());

        conf.setSource(test);
        assertEquals(test, conf.getSource());
        System.setProperty("twitter4j.source", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getSource());
        conf.setSource(test);
        assertEquals(test, conf.getSource());
        System.getProperties().remove("twitter4j.source");


        System.getProperties().remove("twitter4j.clientVersion");
        conf = new PropertyConfiguration();
        Assert.assertEquals(Version.getVersion(), conf.getClientVersion());

        conf.setClientVersion(test);
        assertEquals(test, conf.getClientVersion());
        System.setProperty("twitter4j.clientVersion", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getClientVersion());
        conf.setClientVersion(test);
        assertEquals(test, conf.getClientVersion());
        System.getProperties().remove("twitter4j.clientVersion");


        System.getProperties().remove("twitter4j.clientURL");
        conf = new PropertyConfiguration();
        assertEquals("http://yusuke.homeip.net/twitter4j/en/twitter4j-" + Version.getVersion() + ".xml", conf.getClientURL());

        conf.setClientURL(test);
        assertEquals(test, conf.getClientURL());
        System.setProperty("twitter4j.clientURL", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getClientURL());
        conf.setClientURL(test);
        assertEquals(test, conf.getClientURL());
        System.getProperties().remove("twitter4j.clientURL");


        System.getProperties().remove("twitter4j.http.userAgent");
        conf = new PropertyConfiguration();
        assertEquals("twitter4j http://yusuke.homeip.net/twitter4j/ /" + Version.getVersion(), conf.getUserAgent());

        conf.setUserAgent(test);
        assertEquals(test, conf.getUserAgent());
        System.setProperty("twitter4j.http.userAgent", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getUserAgent());
        conf.setUserAgent(test);
        assertEquals(test, conf.getUserAgent());
        System.getProperties().remove("twitter4j.http.userAgent");

        System.getProperties().remove("twitter4j.http.proxyHost");
        conf = new PropertyConfiguration();
        assertEquals(null, conf.getHttpProxyHost());

        System.setProperty("twitter4j.http.proxyHost", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getHttpProxyHost());
        System.getProperties().remove("twitter4j.http.proxyHost");

        System.getProperties().remove("twitter4j.http.proxyPort");
        conf = new PropertyConfiguration();
        assertEquals(-1, conf.getHttpProxyPort());

        System.setProperty("twitter4j.http.proxyPort", "100");
        conf = new PropertyConfiguration();
        assertEquals(100, conf.getHttpProxyPort());
        System.getProperties().remove("twitter4j.http.proxyPort");


        System.getProperties().remove("twitter4j.http.proxyUser");
        conf = new PropertyConfiguration();
        assertEquals(null, conf.getHttpProxyUser());

        System.setProperty("twitter4j.http.proxyUser", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getHttpProxyUser());
        System.getProperties().remove("twitter4j.http.proxyUser");


        System.getProperties().remove("twitter4j.http.proxyPassword");
        conf = new PropertyConfiguration();
        assertEquals(null, conf.getHttpProxyPassword());

        System.setProperty("twitter4j.http.proxyPassword", override);
        conf = new PropertyConfiguration();
        assertEquals(override, conf.getHttpProxyPassword());
        System.getProperties().remove("twitter4j.http.proxyPassword");


        System.getProperties().remove("twitter4j.http.connectionTimeout");
        conf = new PropertyConfiguration();
        assertEquals(20000, conf.getHttpConnectionTimeout());

        conf.setHttpConnectionTimeout(10);
        assertEquals(10, conf.getHttpConnectionTimeout());
        System.setProperty("twitter4j.http.connectionTimeout", "100");
        conf = new PropertyConfiguration();
        assertEquals(100, conf.getHttpConnectionTimeout());
        conf.setHttpConnectionTimeout(10);
        assertEquals(10, conf.getHttpConnectionTimeout());
        System.getProperties().remove("twitter4j.http.connectionTimeout");


        System.getProperties().remove("twitter4j.http.readTimeout");
        conf = new PropertyConfiguration();
        assertEquals(120000, conf.getHttpReadTimeout());

        conf.setHttpReadTimeout(10);
        assertEquals(10, conf.getHttpReadTimeout());
        System.setProperty("twitter4j.http.readTimeout", "100");
        conf = new PropertyConfiguration();
        assertEquals(100, conf.getHttpReadTimeout());
        conf.setHttpReadTimeout(10);
        assertEquals(10, conf.getHttpReadTimeout());
        System.getProperties().remove("twitter4j.http.readTimeout");

        assertFalse(conf.isDalvik());


        writeFile("./twitter4j.properties", "twitter4j.http.readTimeout=1234");
        conf = new PropertyConfiguration();
        assertEquals(1234, conf.getHttpReadTimeout());
        writeFile("./twitter4j.properties", "twitter4j.http.readTimeout=4321");
        conf = new PropertyConfiguration();
        assertEquals(4321, conf.getHttpReadTimeout());
        deleteFile("./twitter4j.properties");
        conf = new PropertyConfiguration();

    }

    public void testSSL() throws Exception {
        Configuration conf;

        // disable SSL
        writeFile("./twitter4j.properties", "twitter4j.restBaseURL=http://somewhere.com/"
        + "\n" + "twitter4j.http.useSSL=false");
        conf = new PropertyConfiguration("/");
        assertEquals("http://somewhere.com/", conf.getRestBaseURL());

        // explicitly enabling SSL
        writeFile("./twitter4j.properties", "twitter4j.restBaseURL=http://somewhere.com/"
        + "\n" + "twitter4j.http.useSSL=true");
        conf = new PropertyConfiguration("/");
        assertEquals("https://somewhere.com/", conf.getRestBaseURL());
        deleteFile("./twitter4j.properties");
        conf = new PropertyConfiguration();

        // uses SSL by default
        System.getProperties().remove("twitter4j.http.useSSL");

        writeFile("./twitter4j.properties", "restBaseURL=http://somewhere.com/");
        conf = new PropertyConfiguration("/");
        assertEquals("https://somewhere.com/", conf.getRestBaseURL());

    }

    public void testTwitter4jPrefixOmittable() throws Exception {
        System.getProperties().remove("http.useSSL");
        System.getProperties().remove("twitter4j.http.useSSL");
        Configuration conf;
        writeFile("./twitter4j.properties", "twitter4j.restBaseURL=http://somewhere.com/");
        conf = new PropertyConfiguration("/");
        assertEquals("https://somewhere.com/", conf.getRestBaseURL());
        writeFile("./twitter4j.properties", "restBaseURL=http://somewhere2.com/");

        conf = new PropertyConfiguration("/");
        assertEquals("https://somewhere2.com/", conf.getRestBaseURL());
    }

    public void testTreeConfiguration() throws Exception {
        Configuration conf;
        writeFile("./twitter4j.properties", "twitter4j.restBaseURL=http://somewhere.com/"
        + "\n" + "twitter4j.http.useSSL=false");
        conf = new PropertyConfiguration("/");
        assertEquals("http://somewhere.com/", conf.getRestBaseURL());
        writeFile("./twitter4j.properties", "twitter4j.restBaseURL=http://somewhere.com/"
                + "\n" + "twitter4j.http.useSSL=false"
                + "\n" + "china.twitter4j.restBaseURL=http://somewhere.cn/");

        conf = new PropertyConfiguration("/china");
        assertEquals("http://somewhere.cn/", conf.getRestBaseURL());

        conf = new PropertyConfiguration("/china/");
        assertEquals("http://somewhere.cn/", conf.getRestBaseURL());
        deleteFile("./twitter4j.properties");

        // configuration for two different countries and default
        writeFile("./twitter4j.properties", "restBaseURL=http://somewhere.com/"
                + "\n" + "http.useSSL=false"
                + "\n" + "user=one"
                + "\n" + "china.restBaseURL=http://somewhere.cn/"
                + "\n" + "china.user=two"
                + "\n" + "japan.restBaseURL=http://yusuke.homeip.net/"
                + "\n" + "japan.user=three"
        );
        conf = new PropertyConfiguration();
        assertEquals("one", conf.getUser());
        conf = new PropertyConfiguration("/china");
        assertEquals("two", conf.getUser());
        conf = new PropertyConfiguration("/japan");
        assertEquals("three", conf.getUser());


        writeFile("./twitter4j.properties", "restBaseURL=http://somewhere.com/"
                + "\n" + "http.useSSL=false"
                + "\n" + "user=one"
                + "\n" + "password=pasword-one"
                + "\n" + "china.restBaseURL=http://somewhere.cn/"
                + "\n" + "china.user1.user=two"
                + "\n" + "china.user1.password=pasword-two"
                + "\n" + "china.user2.user=three"
                + "\n" + "china.user2.password=pasword-three"
        );
        conf = new PropertyConfiguration();
        assertEquals("one", conf.getUser());
        conf = new PropertyConfiguration("/china/user1");
        assertEquals("two", conf.getUser());
        assertEquals("pasword-two", conf.getPassword());
        conf = new PropertyConfiguration("/china/user2");
        assertEquals("three", conf.getUser());
        assertEquals("pasword-three", conf.getPassword());

        deleteFile("./twitter4j.properties");
    }
    private void writeFile(String path, String content) throws IOException {
        File file = new File(path);
        file.delete();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.close();
    }

    private void deleteFile(String path) throws IOException {
        File file = new File(path);
        file.delete();
    }
}
