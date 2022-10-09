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
        // setupTwitter twitter = new TwitterFactory(builder.build()).getInstance();
        OAuth2Authorization oAuth2Authorization = OAuth2Authorization.getInstance(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumer(browserConsumerKey, browserConsumerSecret)
                .oAuth2Token(token).build();

        Map<String, RateLimitStatus> rateLimitStatus = twitter.v1().help().getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);

        try {
            twitter.v1().users().getAccountSettings();
            fail("should throw TwitterException");

        } catch (TwitterException e) {
            assertEquals(403, e.getStatusCode());
            assertEquals(220, e.getErrorCode());
            assertTrue(e.getErrorMessage().contains("Your credentials do not allow access to this resource"));
        }
    }

    @Test
    void testAuthWithBuildingConf2() throws Exception {
        // exercise & verify
        OAuth2Authorization oAuth2Authorization = OAuth2Authorization.getInstance(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumer(browserConsumerKey, browserConsumerSecret)
                .oAuth2Token(token).build();

        Map<String, RateLimitStatus> rateLimitStatus = twitter.v1().help().getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    @Test
    void testSettingAccessToken1() throws TwitterException {
        OAuth2Authorization oAuth2Authorization = OAuth2Authorization.getInstance(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();

        // exercise & verify
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumer(browserConsumerKey, browserConsumerSecret)
                .oAuth2Token(token).build();

//        oAuth2Authorization.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
//        oAuth2Authorization.setOAuth2Token(token);

        Map<String, RateLimitStatus> rateLimitStatus = twitter.v1().help().getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    @Test
    void testSettingAccessToken2() throws TwitterException {
        OAuth2Authorization oAuth2Authorization = OAuth2Authorization.getInstance(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();

        // exercise & verify
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumer(browserConsumerKey, browserConsumerSecret)
                .oAuth2Token(token).build();

        Map<String, RateLimitStatus> rateLimitStatus = twitter.v1().help().getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);
    }

    @Test
    void testInvalidation() throws Exception {
        OAuth2Authorization oAuth2Authorization = OAuth2Authorization.getInstance(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = oAuth2Authorization.getOAuth2Token();

        // exercise
        oAuth2Authorization.invalidateOAuth2Token();
        Twitter twitter = Twitter.newBuilder()
                .applicationOnlyAuthEnabled(true)
                .oAuthConsumer(browserConsumerKey, browserConsumerSecret)
                .oAuth2Token(token).build();

        try {
            twitter.v1().help().getRateLimitStatus();
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
            OAuth2Authorization oAuth2Authorization = OAuth2Authorization.newBuilder().build();

            OAuth2Token token = oAuth2Authorization.getOAuth2Token();
            assertEquals("bearer", token.getTokenType());

            Twitter twitter = Twitter.newBuilder().oAuth2Token(token).build();
            // verify
            Map<String, RateLimitStatus> rateLimitStatus = twitter.v1().help().getRateLimitStatus("search");
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
            OAuth2Authorization oAuth2Authorization = OAuth2Authorization.getInstance(browserConsumerKey, browserConsumerSecret);

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
            Map<String, RateLimitStatus> rateLimitStatus = twitter.v1().help().getRateLimitStatus("search");
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
