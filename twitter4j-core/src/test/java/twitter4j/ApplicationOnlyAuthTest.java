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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

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
        Configuration builder = new Configuration();
        builder.applicationOnlyAuthEnabled(true);
        // setupTwitter twitter = new TwitterFactory(builder.build()).getInstance();
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(builder.buildConfiguration());

        // exercise & verify
        oAuth2Authorization.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());

        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumerKey(browserConsumerKey)
                .oAuthConsumerSecret(browserConsumerSecret)
                .oAuth2TokenType(token.getTokenType())
                .oAuth2AccessToken(token.getAccessToken()).build();

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
        Configuration builder = new Configuration();
        builder.applicationOnlyAuthEnabled(true);
        // setup
        builder.oAuthConsumerKey(browserConsumerKey).oAuthConsumerSecret(browserConsumerSecret);

        // exercise & verify
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(builder.buildConfiguration());

        OAuth2Token token = oAuth2Authorization.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumerKey(browserConsumerKey)
                .oAuthConsumerSecret(browserConsumerSecret)
                .oAuth2TokenType(token.getTokenType())
                .oAuth2AccessToken(token.getAccessToken()).build();

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    @Test
    void testSettingAccessToken1() throws TwitterException {
        Configuration builder = new Configuration();
        builder.applicationOnlyAuthEnabled(true);
        builder.oAuthConsumerKey(browserConsumerKey).oAuthConsumerSecret(browserConsumerSecret);
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(builder.buildConfiguration());
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();

        // exercise & verify
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumerKey(browserConsumerKey)
                .oAuthConsumerSecret(browserConsumerSecret)
                .oAuth2TokenType(token.getTokenType())
                .oAuth2AccessToken(token.getAccessToken()).build();

        twitter.verifyCredentials();
//        oAuth2Authorization.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
//        oAuth2Authorization.setOAuth2Token(token);

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    @Test
    void testSettingAccessToken2() throws TwitterException {
        Configuration builder = new Configuration();
        builder.applicationOnlyAuthEnabled(true);
        builder.oAuthConsumerKey(browserConsumerKey).oAuthConsumerSecret(browserConsumerSecret);
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(builder.buildConfiguration());

        OAuth2Token token = oAuth2Authorization.getOAuth2Token();

        // exercise & verify
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumerKey(browserConsumerKey)
                .oAuthConsumerSecret(browserConsumerSecret)
                .oAuth2TokenType(token.getTokenType())
                .oAuth2AccessToken(token.getAccessToken()).build();

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    @Test
    void testInvalidation() throws Exception {
        Configuration builder = new Configuration();
        builder.applicationOnlyAuthEnabled(true);
        builder.oAuthConsumerKey(browserConsumerKey).oAuthConsumerSecret(browserConsumerSecret);
        Configuration build = builder.buildConfiguration();
        OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(build);

        OAuth2Token token = oAuth2Authorization.getOAuth2Token();


        // exercise
        oAuth2Authorization.invalidateOAuth2Token();
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumerKey(browserConsumerKey)
                .oAuthConsumerSecret(browserConsumerSecret)
                .oAuth2TokenType(token.getTokenType())
                .oAuth2AccessToken(token.getAccessToken()).build();

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
            Configuration build = new Configuration().buildConfiguration();
            OAuth2Authorization oAuth2Authorization = new OAuth2Authorization(build);

            OAuth2Token token = oAuth2Authorization.getOAuth2Token();
            assertEquals("bearer", token.getTokenType());

            Twitter twitter = Twitter.newBuilder().oAuth2TokenType(token.getTokenType())
                    .oAuth2AccessToken(token.getAccessToken()).build();
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
            Configuration builder = new Configuration();
            builder.applicationOnlyAuthEnabled(true);
            builder.oAuthConsumerKey(browserConsumerKey).oAuthConsumerSecret(browserConsumerSecret);
            Configuration build = builder.buildConfiguration();
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

            Twitter twitter = Twitter.newBuilder().build();

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
