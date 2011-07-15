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
public interface TimelineMethods {
    /**
     * Returns the 20 most recent statuses from non-protected users who have set a custom user icon. The public timeline is cached for 60 seconds and requesting it more often than that is unproductive and a waste of resources.
     * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
     *
     * @return list of statuses of the Public Timeline
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/public_timeline">GET statuses/public_timeline | Twitter Developers</a>
     */
    ResponseList<Status> getPublicTimeline() throws TwitterException;

    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.<br>
     * Usage note: This home_timeline call is identical to statuses/friends_timeline, except that home_timeline also contains retweets, while statuses/friends_timeline does not for backwards compatibility reasons. In a future version of the API, statuses/friends_timeline will be deprected and replaced by home_timeline.
     * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
     *
     * @return list of the home Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/home_timeline">GET statuses/home_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getHomeTimeline() throws TwitterException;


    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.<br>
     * Usage note: This home_timeline call is identical to statuses/friends_timeline, except that home_timeline also contains retweets, while statuses/friends_timeline does not for backwards compatibility reasons. In a future version of the API, statuses/friends_timeline will be deprected and replaced by home_timeline.
     * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the home Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/home_timeline">GET statuses/home_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getHomeTimeline(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
     * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
     *
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/friends_timeline">GET statuses/friends_timeline | Twitter Developers</a>
     * @deprecated use {@link #getHomeTimeline()} instead
     */
    ResponseList<Status> getFriendsTimeline() throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
     * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/friends_timeline">GET statuses/friends_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     * @deprecated use {@link #getHomeTimeline(twitter4j.Paging)} instead
     */
    ResponseList<Status> getFriendsTimeline(Paging paging) throws TwitterException;


    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline.json
     *
     * @param screenName specifies the screen name of the user for whom to return the user_timeline
     * @param paging     controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the user Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    ResponseList<Status> getUserTimeline(String screenName, Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline.json
     *
     * @param userId specifies the ID of the user for whom to return the user_timeline
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the user Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    ResponseList<Status> getUserTimeline(long userId, Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param screenName specifies the screen name of the user for whom to return the user_timeline
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     */
    ResponseList<Status> getUserTimeline(String screenName) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param userId specifies the ID of the user for whom to return the user_timeline
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    ResponseList<Status> getUserTimeline(long userId) throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     */
    ResponseList<Status> getUserTimeline() throws TwitterException;

    /**
     * Returns the 20 most recent statuses posted from the authenticating user. It's also possible to request another user's timeline via the id parameter.<br>
     * This is the equivalent of the Web / page for your own user, or the profile page for a third party.<br>
     * For backwards compatibility reasons, retweets are stripped out of the user_timeline when calling in XML or JSON (they appear with 'RT' in RSS and Atom). If you'd like them included, you can merge them in from statuses retweeted_by_me.<br>
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/user_timeline">GET statuses/user_timeline | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    ResponseList<Status> getUserTimeline(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/mentions
     *
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/mentions">GET statuses/mentions | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    ResponseList<Status> getMentions() throws TwitterException;

    /**
     * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/mentions
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/mentions">GET statuses/mentions | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    ResponseList<Status> getMentions(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
     *
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/retweeted_by_me">GET statuses/retweeted_by_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetedByMe() throws TwitterException;

    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/retweeted_by_me">GET statuses/retweeted_by_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetedByMe(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
     *
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/retweeted_to_me">GET statuses/retweeted_to_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetedToMe() throws TwitterException;

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/retweeted_to_me">GET statuses/retweeted_to_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetedToMe(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
     *
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/retweets_of_me">GET statuses/retweets_of_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetsOfMe() throws TwitterException;

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/retweets_of_me">GET statuses/retweets_of_me | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException;

    /**
     * Returns the 20 most recent retweets posted by users the specified user follows. This method is identical to statuses/retweeted_to_me except you can choose the user to view.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_user
     *
     * @param screenName the user to view
     * @param paging     controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    ResponseList<Status> getRetweetedToUser(String screenName, Paging paging)
            throws TwitterException;

    /**
     * Returns the 20 most recent retweets posted by users the specified user follows. This method is identical to statuses/retweeted_to_me except you can choose the user to view.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_user
     *
     * @param userId the user to view
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    ResponseList<Status> getRetweetedToUser(long userId, Paging paging)
            throws TwitterException;

    /**
     * Returns the 20 most recent retweets posted by the specified user. This method is identical to statuses/retweeted_by_me except you can choose the user to view.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_user
     *
     * @param screenName the user to view
     * @param paging     controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetedByUser(String screenName, Paging paging)
            throws TwitterException;

    /**
     * Returns the 20 most recent retweets posted by the specified user. This method is identical to statuses/retweeted_by_me except you can choose the user to view.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_user
     *
     * @param userId the user to view
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetedByUser(long userId, Paging paging)
            throws TwitterException;
}
