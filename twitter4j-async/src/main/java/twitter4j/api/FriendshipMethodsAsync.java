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
public interface FriendshipMethodsAsync {
    /**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
     * <br>This method calls http://api.twitter.com/1/friendships/create
     *
     * @param screenName the screen name of the user to be befriended
     * @see <a href="https://dev.twitter.com/docs/api/1/post/friendships/create">POST friendships/create | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void createFriendship(String screenName);

    /**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
     * <br>This method calls http://api.twitter.com/1/friendships/create
     *
     * @param userId the ID of the user to be befriended
     * @see <a href="https://dev.twitter.com/docs/api/1/post/friendships/create">POST friendships/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void createFriendship(long userId);

    /**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
     * <br>This method calls http://api.twitter.com/1/friendships/create
     *
     * @param screenName the screen name of the user to be befriended
     * @param follow     Enable notifications for the target user in addition to becoming friends.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/friendships/create">POST friendships/create | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void createFriendship(String screenName, boolean follow);

    /**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
     * <br>This method calls http://api.twitter.com/1/friendships/create
     *
     * @param userId the ID of the user to be befriended
     * @param follow Enable notifications for the target user in addition to becoming friends.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/friendships/create">POST friendships/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void createFriendship(long userId, boolean follow);

    /**
     * Allows the authenticating users to unfollow the user specified in the ID parameter.<br>
     * Returns the unfollowed user in the requested format when successful. Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1/friendships/destroy
     *
     * @param screenName the screen name of the user to be befriended
     * @see <a href="https://dev.twitter.com/docs/api/1/post/friendships/destroy">POST friendships/destroy | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void destroyFriendship(String screenName);

    /**
     * Allows the authenticating users to unfollow the user specified in the ID parameter.<br>
     * Returns the unfollowed user in the requested format when successful. Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1/friendships/destroy
     *
     * @param userId the screen name of the user to be befriended
     * @see <a href="https://dev.twitter.com/docs/api/1/post/friendships/destroy">POST friendships/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void destroyFriendship(long userId);

    /**
     * Tests for the existence of friendship between two users. Will return true if user_a follows user_b, otherwise will return false.
     * <br>This method calls http://api.twitter.com/1/friendships/exists
     *
     * @param userA The ID or screen_name of the first user to test friendship for.
     * @param userB The ID or screen_name of the second user to test friendship for.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friendships/exists">GET friendships/exists | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void existsFriendship(String userA, String userB);

    /**
     * Returns detailed information about the relationship between two users.
     * <br>This method calls http://api.twitter.com/1/friendships/show.json
     *
     * @param sourceScreenName the screen name of the source user
     * @param targetScreenName the screen name of the target user
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friendships/show">GET friendships/show | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void showFriendship(String sourceScreenName, String targetScreenName);

    /**
     * Returns detailed information about the relationship between two users.
     * <br>This method calls http://api.twitter.com/1/friendships/show.json
     *
     * @param sourceId the ID of the source user
     * @param targetId the ID of the target user
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friendships/show">GET friendships/show | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void showFriendship(long sourceId, long targetId);

    /**
     * Returns an array of numeric IDs for every user who has a pending request to follow the authenticating user.
     * <br>This method calls http://api.twitter.com/1/friendships/incoming.json
     *
     * @param cursor Breaks the results into pages. A single page contains 5000 identifiers. Provide a value of -1 to begin paging.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friendships/incoming">GET friendships/incoming | Twitter Developers</a>
     * @since Twitter4J 2.1.2
     */
    void getIncomingFriendships(long cursor);

    /**
     * Returns an array of numeric IDs for every protected user for whom the authenticating user has a pending follow request.
     * <br>This method calls http://api.twitter.com/1/friendships/outgoing.json
     *
     * @param cursor Breaks the results into pages. A single page contains 5000 identifiers. Provide a value of -1 to begin paging.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friendships/outgoing">GET friendships/outgoing | Twitter Developers</a>
     * @since Twitter4J 2.1.2
     */
    void getOutgoingFriendships(long cursor);

    /**
     * Returns the relationship of the authenticating user to the specified users.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/friendships/lookup.json
     *
     * @param screenNames array of the screen names to lookup
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    void lookupFriendships(String[] screenNames);

    /**
     * Returns the relationship of the authenticating user to the specified users.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/friendships/lookup.json
     *
     * @param ids array of the ids to lookup
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    void lookupFriendships(long[] ids);


    /**
     * Allows you to enable or disable retweets and device notifications from the specified user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/friendships/update.json
     *
     * @param screenName               screen name to update
     * @param enableDeviceNotification set true to enable device notification
     * @param retweets                 set true to enable retweets
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    void updateFriendship(String screenName, boolean enableDeviceNotification
            , boolean retweets);

    /**
     * Allows you to enable or disable retweets and device notifications from the specified user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/friendships/update.json
     *
     * @param userId                   user id to update
     * @param enableDeviceNotification set true to enable device notification
     * @param retweets                 set true to enable retweets
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    void updateFriendship(long userId, boolean enableDeviceNotification
            , boolean retweets);

    /**
     * Returns the list of user_ids for which the authenticating user has said they do not want to receive retweets from when successful.
     * <br>Now the return value had no data for paging.
     * <br>This methos calls http://api.twitter.com/1/friendships/no_retweet_ids.json
     *
     * @see <a href="http://groups.google.com/group/twitter-api-announce/browse_thread/thread/6f734611ac57e281">Some changes and updates to the API and Tweet Button - Twitter API Announcements | Google Groups</a>
     */
    void getNoRetweetIds();
}
