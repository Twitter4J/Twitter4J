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

import twitter4j.auth.Authorization;
import twitter4j.auth.NullAuthorization;
import twitter4j.auth.OAuth2Authorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;


/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AuthorizationTest extends TwitterTestBase {

    public AuthorizationTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAnonymousInstance() throws Exception {
        Twitter twitter = new TwitterFactory().getInstance();
        Authorization auth = twitter.getAuthorization();
        assertTrue(auth instanceof NullAuthorization);
    }

    public void testOAuthInstance() throws Exception {
        String consumerSecret;
        String consumerKey;
        consumerSecret = p.getProperty("browser.oauth.consumerSecret");
        consumerKey = p.getProperty("browser.oauth.consumerSecret");

        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        try {
            twitter.setOAuthConsumer(consumerSecret, consumerKey);
            fail("should throw IllegalStateException");
        } catch (IllegalStateException ignore) {

        }

        Authorization auth = twitter.getAuthorization();
        assertTrue(auth instanceof OAuthAuthorization);
    }

    public void testOAuth2Instance() throws Exception {
        String consumerSecret = p.getProperty("browser.oauth.consumerSecret");
        String consumerKey = p.getProperty("browser.oauth.consumerSecret");

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true);

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        try {
            twitter.setOAuthConsumer(consumerSecret, consumerKey);
            fail("should throw IllegalStateException");
        } catch (IllegalStateException ignore) {
        }

        Authorization auth = twitter.getAuthorization();
        assertTrue(auth instanceof OAuth2Authorization);
    }
}
