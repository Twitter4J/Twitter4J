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
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface TimelinesResources {
    /**
     * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
     * <br>This method calls http://api.twitter.com/1.1/statuses/mentions
     *
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/mentions">GET statuses/mentions | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     * @deprecated use {@link #getMentionsTimeline()} instead
     */
    ResponseList<Status> getMentions() throws TwitterException;

    /**
     * Returns the 20 most recent mentions (tweets containing a users's @screen_name) for the authenticating user.<br>
     * The timeline returned is the equivalent of the one seen when you view your mentions on twitter.com.<br>
     * This method can only return up to 800 tweets.<br>
     * See <a href="https://dev.twitter.com/docs/working-with-timelines"></a>Working with Timelines</a> for instructions on traversing timelines.
     * <br>This method calls http://api.twitter.com/1.1/statuses/mentions_timeline
     *
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/mentions">GET statuses/mentions | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<Status> getMentionsTimeline() throws TwitterException;

    /**
     * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
     * <br>This method calls http://api.twitter.com/1.1/statuses/mentions
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/mentions">GET statuses/mentions | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     * @deprecated use {@link #getMentionsTimeline(twitter4j.Paging)} instead
     */
    ResponseList<Status> getMentions(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent mentions (tweets containing a users's @screen_name) for the authenticating user.<br>
     * The timeline returned is the equivalent of the one seen when you view your mentions on twitter.com.<br>
     * This method can only return up to 800 tweets.<br>
     * See <a href="https://dev.twitter.com/docs/working-with-timelines"></a>Working with Timelines</a> for instructions on traversing timelines.
     * <br>This method calls http://api.twitter.com/1.1/statuses/mentions_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, zcount parameters.
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/mentions">GET statuses/mentions | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<Status> getMentionsTimeline(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline.json
     *
     * @param screenName specifies the screen name of the user for whom to return the user_timeline
     * @param paging     controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the user Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    ResponseList<Status> getUserTimeline(String screenName, Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline.json
     *
     * @param userId specifies the ID of the user for whom to return the user_timeline
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the user Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    ResponseList<Status> getUserTimeline(long userId, Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @param screenName specifies the screen name of the user for whom to return the user_timeline
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     */
    ResponseList<Status> getUserTimeline(String screenName) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @param userId specifies the ID of the user for whom to return the user_timeline
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    ResponseList<Status> getUserTimeline(long userId) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     */
    ResponseList<Status> getUserTimeline() throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1.1/statuses/user_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    ResponseList<Status> getUserTimeline(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.<br>
     * Usage note: This home_timeline call is identical to statuses/friends_timeline, except that home_timeline also contains retweets, while statuses/friends_timeline does not for backwards compatibility reasons. In a future version of the API, statuses/friends_timeline will be deprected and replaced by home_timeline.
     * <br>This method calls http://api.twitter.com/1.1/statuses/home_timeline
     *
     * @return list of the home Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline">GET statuses/home_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getHomeTimeline() throws TwitterException;

    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.<br>
     * Usage note: This home_timeline call is identical to statuses/friends_timeline, except that home_timeline also contains retweets, while statuses/friends_timeline does not for backwards compatibility reasons. In a future version of the API, statuses/friends_timeline will be deprected and replaced by home_timeline.
     * <br>This method calls http://api.twitter.com/1.1/statuses/home_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the home Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline">GET statuses/home_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getHomeTimeline(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1.1/statuses/retweets_of_me.json
     *
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/retweets_of_me">GET statuses/retweets_of_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetsOfMe() throws TwitterException;

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1.1/statuses/retweets_of_me.json
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/retweets_of_me">GET statuses/retweets_of_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException;
}
