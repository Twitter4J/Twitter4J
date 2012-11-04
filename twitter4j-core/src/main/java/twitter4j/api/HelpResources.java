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

package twitter4j.api;

import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;

import java.util.List;
import java.util.Map;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface HelpResources {
    /**
     * Returns the current configuration used by Twitter including twitter.com slugs which are not usernames, maximum photo resolutions, and t.co URL lengths.</br>
     * It is recommended applications request this endpoint when they are loaded, but no more than once a day.
     *
     * @return configuration
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/help/configuration">GET help/configuration | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    TwitterAPIConfiguration getAPIConfiguration() throws TwitterException;

    /**
     * Returns the list of languages supported by Twitter along with their ISO 639-1 code. The ISO 639-1 code is the two letter value to use if you include lang with any of your requests.
     *
     * @return list of languages supported by Twitter
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/help/languages">GET help/languages | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    ResponseList<Language> getLanguages() throws TwitterException;

    public interface Language {
        String getName();

        String getCode();

        String getStatus();
    }

    /**
     * Returns Twitter's Privacy Policy.
     * <br>This method calls http://api.twitter.com/1.1/help/privacy.json
     *
     * @return privacy policy
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/help/privacy">GET help/privacy | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    String getPrivacyPolicy() throws TwitterException;

    /**
     * Returns Twitter's' Terms of Service.
     * <br>This method calls http://api.twitter.com/1.1/help/tos.json
     *
     * @return Terms of Service
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/help/tos">GET help/tos | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    String getTermsOfService() throws TwitterException;

    /**
     * Returns the current rate limits for methods belonging to the specified resource families.<br>
     * Each 1.1 API resource belongs to a "resource family" which is indicated in its method documentation. You can typically determine a method's resource family from the first component of the path after the resource version.<br>
     * This method responds with a map of methods belonging to the families specified by the resources parameter, the current remaining uses for each of those resources within the current rate limiting window, and its expiration time in epoch time. It also includes a rate_limit_context field that indicates the current access token context.<br>
     * You may also issue requests to this method without any parameters to receive a map of all rate limited GET methods. If your application only uses a few of methods, please explicitly provide a resources parameter with the specified resource families you work with.<br>
     * Read more about REST API Rate Limiting in v1.1 and review the limits.<br>
     * <br>This method calls http://api.twitter.com/1.1/application/rate_limit_status.json
     *
     * @return the rate limit statuses
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/application/rate_limit_status">GET application/rate_limit_status</a>
     * @since Twitter4J 3.0.0
     */
    Map<String ,RateLimitStatus> getRateLimitStatus() throws TwitterException;

    /**
     * Returns the current rate limits for methods belonging to the specified resource families.<br>
     * Each 1.1 API resource belongs to a "resource family" which is indicated in its method documentation. You can typically determine a method's resource family from the first component of the path after the resource version.<br>
     * This method responds with a map of methods belonging to the families specified by the resources parameter, the current remaining uses for each of those resources within the current rate limiting window, and its expiration time in epoch time. It also includes a rate_limit_context field that indicates the current access token context.<br>
     * You may also issue requests to this method without any parameters to receive a map of all rate limited GET methods. If your application only uses a few of methods, please explicitly provide a resources parameter with the specified resource families you work with.<br>
     * Read more about REST API Rate Limiting in v1.1 and review the limits.<br>
     * As of Nov 4th 2012, supported resource names are as follows:
     * &quot;trends&quot;,&quot;application&quot;,&quot;users&quot;,&quot;saved_searches&quot;,&quot;geo&quot;,&quot;direct_messages&quot;,&quot;blocks&quot;,&quot;favorites&quot;,&quot;statuses&quot;,&quot;followers&quot;,&quot;help&quot;,&quot;friends&quot;,&quot;search&quot;,&quot;friendships&quot;,&quot;account&quot;,&quot;lists&quot;
     * <br>This method calls http://api.twitter.com/1.1/application/rate_limit_status.json
     *
     * @return the rate limit statuses
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/application/rate_limit_status">GET application/rate_limit_status</a>
     * @since Twitter4J 3.0.0
     */
    Map<String ,RateLimitStatus> getRateLimitStatus(String... resources) throws TwitterException;
}
