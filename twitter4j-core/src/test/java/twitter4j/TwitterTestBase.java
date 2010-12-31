/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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
package twitter4j;

import junit.framework.TestCase;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;

import java.io.InputStream;
import java.util.Properties;

public class TwitterTestBase extends TestCase {
    public TwitterTestBase(String name) {
        super(name);
    }

    protected Twitter twitter1, twitter2, twitter3,
            unauthenticated, twitterAPIBestFriend1, twitterAPIBestFriend2;
    protected Properties p = new Properties();

    protected String numberId, numberPass, followsOneWay;
    protected int numberIdId;
    protected TestUserInfo id1, id2, id3, bestFriend1, bestFriend2;
    protected Configuration conf1, conf2, conf3;

    protected class TestUserInfo {
        public String screenName;
        public String password;
        public int id;
        public String accessToken;
        public String accessTokenSecret;

        TestUserInfo(String screenName) {
            this.screenName = p.getProperty(screenName + ".user");
            this.password = p.getProperty(screenName + ".password");
            this.id = Integer.valueOf(p.getProperty(screenName + ".id"));
            this.accessToken = p.getProperty(screenName + ".oauth.accessToken");
            this.accessTokenSecret = p.getProperty(screenName + ".oauth.accessTokenSecret");
        }
    }

    protected String desktopConsumerSecret;
    protected String desktopConsumerKey;
    protected String browserConsumerSecret;
    protected String browserConsumerKey;

    protected void setUp() throws Exception {
        super.setUp();
        InputStream is = TwitterTestBase.class.getResourceAsStream("/test.properties");
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
        Configuration bestFriend1Conf = new PropertyConfiguration(p, "/bestFriend1");
        bestFriend1 = new TestUserInfo("bestFriend1");
        Configuration bestFriend2Conf = new PropertyConfiguration(p, "/bestFriend2");
        bestFriend2 = new TestUserInfo("bestFriend2");

        numberId = p.getProperty("numberid.user");
        numberPass = p.getProperty("numberid.password");
//        id1id = Integer.valueOf(p.getProperty("id1id"));
        numberIdId = Integer.valueOf(p.getProperty("numberid.id"));

        twitter1 = new TwitterFactory(conf1).getInstance();

        twitter2 = new TwitterFactory(conf2).getInstance();

        twitter3 = new TwitterFactory(conf3).getInstance();

        twitterAPIBestFriend1 = new TwitterFactory(bestFriend1Conf).getInstance();

        twitterAPIBestFriend2 = new TwitterFactory(bestFriend2Conf).getInstance();

        unauthenticated = new TwitterFactory().getInstance();

        followsOneWay = p.getProperty("followsOneWay");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
