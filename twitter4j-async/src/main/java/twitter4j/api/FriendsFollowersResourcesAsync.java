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
public interface FriendsFollowersResourcesAsync {
    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1.1/friends/ids.json
     *
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFriendsIDs(long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1.1/friends/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the friends list.
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFriendsIDs(long userId, long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1.1/friends/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the friends list.
     * @param cursor     Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFriendsIDs(String screenName, long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1.1/followers/ids.json
     *
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFollowersIDs(long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1.1/followers/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the followers list.
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFollowersIDs(long userId, long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1.1/followers/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the followers list.
     * @param cursor     Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFollowersIDs(String screenName, long cursor);

    /**
     * Returns the relationship of the authenticating user to the specified users.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1.1/friendships/lookup.json
     *
     * @param ids array of the ids to lookup
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    void lookupFriendships(long[] ids);

    /**
     * Returns the relationship of the authenticating user to the specified users.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1.1/friendships/lookup.json
     *
     * @param screenNames array of the screen names to lookup
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    void lookupFriendships(String[] screenNames);

    /**
     * Returns an array of numeric IDs for every user who has a pending request to follow the authenticating user.
     * <br>This method calls http://api.twitter.com/1.1/friendships/incoming.json
     *
     * @param cursor Breaks the results into pages. A single page contains 5000 identifiers. Provide a value of -1 to begin paging.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friendships/incoming">GET friendships/incoming | Twitter Developers</a>
     * @since Twitter4J 2.1.2
     */
    void getIncomingFriendships(long cursor);

    /**
     * Returns an array of numeric IDs for every protected user for whom the authenticating user has a pending follow request.
     * <br>This method calls http://api.twitter.com/1.1/friendships/outgoing.json
     *
     * @param cursor Breaks the results into pages. A single page contains 5000 identifiers. Provide a value of -1 to begin paging.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friendships/outgoing">GET friendships/outgoing | Twitter Developers</a>
     * @since Twitter4J 2.1.2
     */
    void getOutgoingFriendships(long cursor);

    /**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
     * <br>This method calls http://api.twitter.com/1.1/friendships/create
     *
     * @param userId the ID of the user to be befriended
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/friendships/create">POST friendships/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void createFriendship(long userId);

    /**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
     * <br>This method calls http://api.twitter.com/1.1/friendships/create
     *
     * @param screenName the screen name of the user to be befriended
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/friendships/create">POST friendships/create | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void createFriendship(String screenName);

    /**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
     * <br>This method calls http://api.twitter.com/1.1/friendships/create
     *
     * @param userId the ID of the user to be befriended
     * @param follow Enable notifications for the target user in addition to becoming friends.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/friendships/create">POST friendships/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void createFriendship(long userId, boolean follow);

    /**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
     * <br>This method calls http://api.twitter.com/1.1/friendships/create
     *
     * @param screenName the screen name of the user to be befriended
     * @param follow     Enable notifications for the target user in addition to becoming friends.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/friendships/create">POST friendships/create | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void createFriendship(String screenName, boolean follow);

    /**
     * Allows the authenticating users to unfollow the user specified in the ID parameter.<br>
     * Returns the unfollowed user in the requested format when successful. Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1.1/friendships/destroy
     *
     * @param userId the screen name of the user to be befriended
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/friendships/destroy">POST friendships/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void destroyFriendship(long userId);

    /**
     * Allows the authenticating users to unfollow the user specified in the ID parameter.<br>
     * Returns the unfollowed user in the requested format when successful. Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1.1/friendships/destroy
     *
     * @param screenName the screen name of the user to be befriended
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/friendships/destroy">POST friendships/destroy | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void destroyFriendship(String screenName);

    /**
     * Allows you to enable or disable retweets and device notifications from the specified user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1.1/friendships/update.json
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
     * Allows you to enable or disable retweets and device notifications from the specified user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1.1/friendships/update.json
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
     * Returns detailed information about the relationship between two users.
     * <br>This method calls http://api.twitter.com/1.1/friendships/show.json
     *
     * @param sourceId the ID of the source user
     * @param targetId the ID of the target user
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friendships/show">GET friendships/show | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void showFriendship(long sourceId, long targetId);

    /**
     * Returns detailed information about the relationship between two users.
     * <br>This method calls http://api.twitter.com/1.1/friendships/show.json
     *
     * @param sourceScreenName the screen name of the source user
     * @param targetScreenName the screen name of the target user
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friendships/show">GET friendships/show | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void showFriendship(String sourceScreenName, String targetScreenName);

    /**
     * Returns a cursored collection of user objects for every user the specified user is following (otherwise known as their "friends").<br>
     * At this time, results are ordered with the most recent following first — however, this ordering is subject to unannounced change and eventual consistency issues. Results are given in groups of 20 users and multiple "pages" of results can be navigated through using the next_cursor value in subsequent requests. See <a href="https://dev.twitter.com/docs/misc/cursoring">Using cursors to navigate collections</a> for more information.
     * <br>This method calls http://api.twitter.com/1.1/friends/list.json
     *
     * @param userId The ID of the user for whom to return results for.
     * @param cursor Causes the results to be broken into pages of no more than 20 records at a time.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friends/list">GET friends/list | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    void getFriendsList(long userId, long cursor);

    /**
     * Returns a cursored collection of user objects for every user the specified user is following (otherwise known as their "friends").<br>
     * At this time, results are ordered with the most recent following first — however, this ordering is subject to unannounced change and eventual consistency issues. Results are given in groups of 20 users and multiple "pages" of results can be navigated through using the next_cursor value in subsequent requests. See <a href="https://dev.twitter.com/docs/misc/cursoring">Using cursors to navigate collections</a> for more information.
     * <br>This method calls http://api.twitter.com/1.1/friends/list.json
     *
     * @param screenName The screen name of the user for whom to return results for.
     * @param cursor Causes the results to be broken into pages of no more than 20 records at a time.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friends/list">GET friends/list | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    void getFriendsList(String screenName, long cursor);

    /**
     * Returns a cursored collection of user objects for users following the specified user.<br>
     * At this time, results are ordered with the most recent following first — however, this ordering is subject to unannounced change and eventual consistency issues. Results are given in groups of 20 users and multiple "pages" of results can be navigated through using the next_cursor value in subsequent requests. See <a href="https://dev.twitter.com/docs/misc/cursoring">Using cursors to navigate collections</a> for more information.
     * <br>This method calls http://api.twitter.com/1.1/followers/list.json
     *
     * @param userId The ID of the user for whom to return results for.
     * @param cursor Causes the results to be broken into pages of no more than 20 records at a time.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/followers/list">GET followers/list | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    void getFollowersList(long userId, long cursor);

    /**
     * Returns a cursored collection of user objects for users following the specified user.<br>
     * At this time, results are ordered with the most recent following first — however, this ordering is subject to unannounced change and eventual consistency issues. Results are given in groups of 20 users and multiple "pages" of results can be navigated through using the next_cursor value in subsequent requests. See <a href="https://dev.twitter.com/docs/misc/cursoring">Using cursors to navigate collections</a> for more information.
     * <br>This method calls http://api.twitter.com/1.1/followers/list.json
     *
     * @param screenName The screen name of the user for whom to return results for.
     * @param cursor Causes the results to be broken into pages of no more than 20 records at a time.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/followers/list">GET followers/list | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    void getFollowersList(String screenName, long cursor);
}
