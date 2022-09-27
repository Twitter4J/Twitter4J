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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author KOMIYA Atsushi - komiya.atsushi at gmail.com
 */
@Execution(ExecutionMode.SAME_THREAD)
public class ApplicationOnlyAuthTest extends TwitterTestBase {

    // --- Authentication

    @Test
    void testAuthWithBuildingConf1() throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        // setupTwitter twitter = new TwitterFactory(builder.build()).getInstance();
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(builder.build());

        // exercise & verify
        oAuth2Authorization.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());

        Twitter twitter = new TwitterFactory(new ConfigurationBuilder()
                .setApplicationOnlyAuthEnabled(true)
                .setOAuthConsumerKey(browserConsumerKey)
                .setOAuthConsumerSecret(browserConsumerSecret)
                .setOAuth2TokenType(token.getTokenType())
                .setOAuth2AccessToken(token.getAccessToken()).build()).getInstance();

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
            assertTrue(e.getErrorMessage().contains("Your credentials do not allow access to this resource"));
        }
    }

    @Test
    void testAuthWithBuildingConf2() throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        // setup
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);

        // exercise & verify
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(builder.build());

        OAuth2Token token = oAuth2Authorization.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());
        Twitter twitter = new TwitterFactory(new ConfigurationBuilder()
                .setApplicationOnlyAuthEnabled(true)
                .setOAuthConsumerKey(browserConsumerKey)
                .setOAuthConsumerSecret(browserConsumerSecret)
                .setOAuth2TokenType(token.getTokenType())
                .setOAuth2AccessToken(token.getAccessToken()).build()).getInstance();

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    @Test
    void testSettingAccessToken1() throws TwitterException {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(builder.build());
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();

        // exercise & verify
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true);

        Twitter twitter = new TwitterFactory(cb.setOAuthConsumerKey(browserConsumerKey)
                .setOAuthConsumerSecret(browserConsumerSecret)
                .setOAuth2TokenType(token.getTokenType())
                .setOAuth2AccessToken(token.getAccessToken()).build()).getInstance();

        oAuth2Authorization.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        oAuth2Authorization.setOAuth2Token(token);

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    @Test
    void testSettingAccessToken2() throws TwitterException {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(builder.build());

        OAuth2Token token = oAuth2Authorization.getOAuth2Token();

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

    @Test
    void testInvalidation() throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        Configuration build = builder.build();
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(build);

        OAuth2Token token = oAuth2Authorization.getOAuth2Token();


        // exercise
        oAuth2Authorization.invalidateOAuth2Token();
        Twitter twitter = new TwitterFactory(new ConfigurationBuilder()
                .setApplicationOnlyAuthEnabled(true)
                .setOAuthConsumerKey(browserConsumerKey)
                .setOAuthConsumerSecret(browserConsumerSecret)
                .setOAuth2TokenType(token.getTokenType())
                .setOAuth2AccessToken(token.getAccessToken()).build()).getInstance();

        try {
            twitter.getRateLimitStatus();
            fail("show throw IllegalStateException");
        } catch (TwitterException expected) {
        }
    }

    @Test
    void testAuthWithPropertyFile() throws Exception {
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
            Configuration build = new ConfigurationBuilder().build();
            OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(build);

            OAuth2Token token = oAuth2Authorization.getOAuth2Token();
            assertEquals("bearer", token.getTokenType());

            Twitter twitter = new TwitterFactory(new ConfigurationBuilder().setOAuth2TokenType(token.getTokenType())
                    .setOAuth2AccessToken(token.getAccessToken()).build()).getInstance();
            // verify
            Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
            RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
            assertNotNull(searchTweetsRateLimit);
            assertEquals(searchTweetsRateLimit.getLimit(), 450);

        } finally {
            deleteFile(filename);
        }
    }

    @Test
    void testSettingAccessTokenFromPropertyFile() throws Exception {
        String filename = "./twitter4j.properties";
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setApplicationOnlyAuthEnabled(true);
            builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
            Configuration build = builder.build();
            OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(build);

            OAuth2Token token = oAuth2Authorization.getOAuth2Token();
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
        //noinspection ResultOfMethodCallIgnored
        file.delete();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void deleteFile(String filename) {
        File file = new File(filename);
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }
}
