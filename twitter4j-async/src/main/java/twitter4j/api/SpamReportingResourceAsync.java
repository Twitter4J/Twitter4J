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

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */

/**
 * Not yet supported.
 * <p/>
 * http://yusuke.homeip.net/jira/browse/TFJ-223
 */
public interface SpamReportingResourceAsync {
    /**
     * The user specified in the id is blocked by the authenticated user and reported as a spammer.
     * <br>This method calls http://api.twitter.com/1.1/report_spam.json
     *
     * @param userId The ID of the user you want to report as a spammer.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/report_spam">POST report_spam | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void reportSpam(long userId);

    /**
     * The user specified in the id is blocked by the authenticated user and reported as a spammer.
     * <br>This method calls http://api.twitter.com/1.1/report_spam.json
     *
     * @param screenName The screen name of the user you want to report as a spammer.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/report_spam">POST report_spam | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void reportSpam(String screenName);
}
