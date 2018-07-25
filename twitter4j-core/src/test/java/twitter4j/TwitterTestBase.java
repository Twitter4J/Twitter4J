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

import junit.framework.TestCase;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TwitterTestBase extends TestCase {
    public TwitterTestBase(String name) {
        super(name);
    }

    Twitter twitter1, twitter2, twitter3,
            twitterAPIBestFriend1, twitterAPIBestFriend2,
            rwPrivateMessage, readonly;
    protected final Properties p = new Properties();

    protected String numberId, numberPass, followsOneWay;
    protected long numberIdId;
    protected TestUserInfo id1, id2, id3, bestFriend1, bestFriend2;
    Configuration conf1, conf2, conf3;

    protected class TestUserInfo {
        public final String screenName;
        public final String password;
        public final long id;
        public final String accessToken;
        public final String accessTokenSecret;

        TestUserInfo(String screenName) {
            this.screenName = p.getProperty(screenName + ".user");
            this.password = p.getProperty(screenName + ".password");
            this.id = Long.valueOf(p.getProperty(screenName + ".id"));
            this.accessToken = p.getProperty(screenName + ".oauth.accessToken");
            this.accessTokenSecret = p.getProperty(screenName + ".oauth.accessTokenSecret");
        }
    }

    protected String desktopConsumerSecret;
    protected String desktopConsumerKey;
    protected String browserConsumerSecret;
    protected String browserConsumerKey;

    private static int currentIndex;
    private static int maxTestPropertyIndex;
    static {
        for (int i = 0; i < 100; i++) {
            // lookup test[i].properties
            InputStream resource = TwitterTestBase.class.getResourceAsStream("/test" + i + ".properties");
            if (resource != null) {
                try {
                    resource.close();
                } catch (IOException ignore) {
                }
                maxTestPropertyIndex = i;
            } else {
                break;
            }

        }
        currentIndex = (int) (System.currentTimeMillis() % (maxTestPropertyIndex + 1));
    }

    /**
     * rotate test property file
     * @return test[index].properties as InputStream
     */
    private static InputStream getNextProperty(){
        currentIndex++;
        if (currentIndex > maxTestPropertyIndex) {
            currentIndex = 0;
        }
        return TwitterTestBase.class.getResourceAsStream("/test" + currentIndex + ".properties");
    }

    protected void setUp() throws Exception {
        super.setUp();
        InputStream is = getNextProperty();
        p.load(is);
        is.close();

        desktopConsumerSecret = p.getProperty("oauth.consumerSecret");
        desktopConsumerKey = p.getProperty("oauth.consumerKey");
        browserConsumerSecret = p.getProperty("browser.oauth.consumerSecret");
        browserConsumerKey = p.getProperty("browser.oauth.consumerKey");

        conf1 = new PropertyConfiguration(p, "/id1");
        id1 = new TestUserInfo("id1");
        conf2 = new PropertyConfiguration(p, "/id2");
        id2 = new TestUserInfo("id2");
        conf3 = new PropertyConfiguration(p, "/id3");
        id3 = new TestUserInfo("id3");
        rwPrivateMessage = new TwitterFactory(new PropertyConfiguration(p, "/r-w-private")).getInstance();
        Configuration bestFriend1Conf = new PropertyConfiguration(p, "/bestFriend1");
        bestFriend1 = new TestUserInfo("bestFriend1");
        Configuration bestFriend2Conf = new PropertyConfiguration(p, "/bestFriend2");
        bestFriend2 = new TestUserInfo("bestFriend2");

        numberId = p.getProperty("numberid.user");
        numberPass = p.getProperty("numberid.password");
//        id1id = Integer.valueOf(p.getProperty("id1id"));
        numberIdId = Long.valueOf(p.getProperty("numberid.id"));

        twitter1 = new TwitterFactory(conf1).getInstance();

        twitter2 = new TwitterFactory(conf2).getInstance();

        twitter3 = new TwitterFactory(conf3).getInstance();

        twitterAPIBestFriend1 = new TwitterFactory(bestFriend1Conf).getInstance();

        twitterAPIBestFriend2 = new TwitterFactory(bestFriend2Conf).getInstance();

        followsOneWay = p.getProperty("followsOneWay");

        readonly = new TwitterFactory(new PropertyConfiguration(p, "/readonly")).getInstance();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
