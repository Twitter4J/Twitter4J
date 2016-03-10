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

package twitter4j.auth;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * @author KOMIYA Atsushi - komiya.atsushi at gmail.com
 */
public class ApplicationOnlyAuthTest extends TwitterTestBase {
    private ConfigurationBuilder builder;

    public ApplicationOnlyAuthTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
    }

    // --- Authentication

    public void testAuthWithBuildingConf1() throws Exception {
        // setup
        Twitter twitter = new TwitterFactory(builder.build()).getInstance();

        // exercise & verify
        twitter.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = twitter.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);

        try {
            twitter.getAccountSettings();
            fail("should throw TwitterException");

        } catch (TwitterException e) {
            assertEquals(403, e.getStatusCode());
            assertEquals(220, e.getErrorCode());
            assertEquals("Your credentials do not allow access to this resource", e.getErrorMessage());
        }
    }

    public void testAuthWithBuildingConf2() throws Exception {
        // setup
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        Twitter twitter = new TwitterFactory(builder.build()).getInstance();

        // exercise & verify
        OAuth2Token token = twitter.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    public void testSettingAccessToken1() throws TwitterException {
        // setup
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

        // exercise & verify
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true);

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        twitter.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        twitter.setOAuth2Token(token);

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    public void testSettingAccessToken2() throws TwitterException {
        // setup
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

        // exercise & verify
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true);
        cb.setOAuthConsumerKey(browserConsumerKey);
        cb.setOAuthConsumerSecret(browserConsumerSecret);
        cb.setOAuth2TokenType(token.getTokenType());
        cb.setOAuth2AccessToken(token.getAccessToken());

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    public void testInvalidation() throws Exception {
        // setup
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        Twitter twitter = new TwitterFactory(builder.build()).getInstance();
        OAuth2Token token = twitter.getOAuth2Token();

        // exercise
        twitter.invalidateOAuth2Token();

        try {
            twitter.getRateLimitStatus();
            fail("show throw IllegalStateException");

        } catch (IllegalStateException ignore) {
        }

        twitter.setOAuth2Token(token);
        try {
            twitter.getRateLimitStatus();
            fail("should throw TwitterException");

        } catch (TwitterException e) {
            assertEquals(401, e.getStatusCode());
            assertEquals(89, e.getErrorCode());
        }
    }

    public void testAuthWithPropertyFile() throws Exception {
        String filename = "./twitter4j.properties";

        try {
            // setup
            writeFile(filename,
                    "enableApplicationOnlyAuth=true",
                    "http.useSSL=true",
                    "oauth.consumerKey=" + browserConsumerKey,
                    "oauth.consumerSecret=" + browserConsumerSecret
            );

            // exercise
            Twitter twitter = new TwitterFactory(new ConfigurationBuilder().build()).getInstance();
            OAuth2Token token = twitter.getOAuth2Token();
            assertEquals("bearer", token.getTokenType());

            // verify
            Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
            RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
            assertNotNull(searchTweetsRateLimit);
            assertEquals(searchTweetsRateLimit.getLimit(), 450);

        } finally {
            deleteFile(filename);
        }
    }

    public void testSettingAccessTokenFromPropertyFile() throws Exception {
        String filename = "./twitter4j.properties";

        try {
            // setup
            builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
            OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();
            writeFile(filename,
                    "enableApplicationOnlyAuth=true",
                    "http.useSSL=true",
                    "oauth.consumerKey=" + browserConsumerKey,
                    "oauth.consumerSecret=" + browserConsumerSecret,
                    "oauth2.tokenType=" + token.getTokenType(),
                    "oauth2.accessToken=" + token.getAccessToken()
            );

            Twitter twitter = new TwitterFactory(new ConfigurationBuilder().build()).getInstance();

            // exercise & verify
            Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
            RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
            assertNotNull(searchTweetsRateLimit);
            assertEquals(searchTweetsRateLimit.getLimit(), 450);

        } finally {
            deleteFile(filename);
        }
    }

    private void writeFile(String filename, String... lines) throws Exception {
        File file = new File(filename);
        file.delete();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        try {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    private void deleteFile(String filename) {
        File file = new File(filename);
        file.delete();
    }
}
