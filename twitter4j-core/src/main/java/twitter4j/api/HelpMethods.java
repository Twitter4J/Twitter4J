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

import twitter4j.ResponseList;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface HelpMethods {
    /**
     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
     *
     * @return true if the API is working
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/help/test">GET help/test | dev.twitter.com</a>
     * @since Twitter4J 1.0.4
     */
    boolean test() throws TwitterException;

    /**
     * Returns the current configuration used by Twitter including twitter.com slugs which are not usernames, maximum photo resolutions, and t.co URL lengths.</br>
     * It is recommended applications request this endpoint when they are loaded, but no more than once a day.
     *
     * @return configuration
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/help/configuration">GET help/configuration | dev.twitter.com</a>
     * @since Twitter4J 2.2.3
     */
    TwitterAPIConfiguration getAPIConfiguration() throws TwitterException;

    /**
     * Returns the list of languages supported by Twitter along with their ISO 639-1 code. The ISO 639-1 code is the two letter value to use if you include lang with any of your requests.
     *
     * @return list of languages supported by Twitter
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/help/languages">GET help/languages | dev.twitter.com</a>
     * @since Twitter4J 2.2.3
     */
    ResponseList<Language> getLanguages() throws TwitterException;

    public interface Language {
        String getName();

        String getCode();

        String getStatus();
    }
}
