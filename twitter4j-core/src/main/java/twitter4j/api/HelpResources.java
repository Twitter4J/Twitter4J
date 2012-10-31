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
     * <br>This method calls http://api.twitter.com/1.1/legal/privacy.json
     *
     * @return privacy policy
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/legal/privacy">GET legal/privacy | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    String getPrivacyPolicy() throws TwitterException;

    /**
     * Returns Twitter's' Terms of Service.
     * <br>This method calls http://api.twitter.com/1.1/legal/tos.json
     *
     * @return Terms of Service
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/legal/tos">GET legal/tos | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    String getTermsOfService() throws TwitterException;

    /**
     * Returns the remaining number of API requests available to the requesting user before the API limit is reached for the current hour. Calls to rate_limit_status do not count against the rate limit.  If authentication credentials are provided, the rate limit status for the authenticating user is returned.  Otherwise, the rate limit status for the requester's IP address is returned.<br>
     * <br>This method calls http://api.twitter.com/1.1/account/rate_limit_status.json
     *
     * @return the rate limit status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/account/rate_limit_status">GET account/rate_limit_status | Twitter Developers</a>
     * @since Twitter4J 1.1.4
     */
    Map<String ,RateLimitStatus> getRateLimitStatus() throws TwitterException;
}
