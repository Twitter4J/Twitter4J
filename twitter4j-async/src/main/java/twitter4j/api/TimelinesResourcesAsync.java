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

import twitter4j.Paging;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface TimelinesResourcesAsync {
    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * <br>This method calls http://api.twitter.com/1.1/statuses/mentions
     *
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/mentions">GET statuses/mentions | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void getMentions();

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * <br>This method calls http://api.twitter.com/1.1/statuses/mentions
     *
     * @param paging controls pagination
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/mentions">GET statuses/mentions | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void getMentions(Paging paging);

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @param screenName Specifies the screen name of the user for whom to return the user_timeline.
     * @param paging     controls pagination
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void getUserTimeline(String screenName, Paging paging);

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @param userId Specifies the ID of the user for whom to return the user_timeline.
     * @param paging controls pagination
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void getUserTimeline(long userId, Paging paging);

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @param paging controls pagination
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void getUserTimeline(Paging paging);

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @param screenName Specifies the screen name of the user for whom to return the user_timeline.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     */
    void getUserTimeline(String screenName);

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @param userId Specifies the ID of the user for whom to return the user_timeline.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void getUserTimeline(long userId);

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     */
    void getUserTimeline();

    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.<br>
     * Usage note: This home_timeline call is identical to statuses/friends_timeline, except that home_timeline also contains retweets, while statuses/friends_timeline does not for backwards compatibility reasons. In a future version of the API, statuses/friends_timeline will be deprected and replaced by home_timeline.
     * <br>This method calls http://api.twitter.com/1.1/statuses/home_timeline
     *
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline">GET statuses/home_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getHomeTimeline();

    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.<br>
     * Usage note: This home_timeline call is identical to statuses/friends_timeline, except that home_timeline also contains retweets, while statuses/friends_timeline does not for backwards compatibility reasons. In a future version of the API, statuses/friends_timeline will be deprected and replaced by home_timeline.
     * <br>This method calls http://api.twitter.com/1.1/statuses/home_timeline
     *
     * @param paging controls pagination
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline">GET statuses/home_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getHomeTimeline(Paging paging);

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1.1/statuses/retweets_of_me.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/retweets_of_me">GET statuses/retweets_of_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getRetweetsOfMe();

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1.1/statuses/retweets_of_me.json
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/retweets_of_me">GET statuses/retweets_of_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getRetweetsOfMe(Paging paging);
}
