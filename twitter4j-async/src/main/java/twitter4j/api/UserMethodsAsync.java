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

import twitter4j.ProfileImage;
import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface UserMethodsAsync {
    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter. The author's most recent status will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/show.json
     *
     * @param screenName the screen name of the user for whom to request the detail
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/show">GET users/show | Twitter Developers</a>
     */
    void showUser(String screenName);

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter. The author's most recent status will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/show.json
     *
     * @param userId the ID of the user for whom to request the retrieve
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/show">GET users/show | Twitter Developers</a>
     */
    void showUser(long userId);

    /**
     * Return up to 100 users worth of extended information, specified by either ID, screen name, or combination of the two. The author's most recent status (if the authenticating user has permission) will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/lookup.json
     *
     * @param screenNames Specifies the screen names of the users to retrieve.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/lookup">GET users/lookup | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void lookupUsers(String[] screenNames);

    /**
     * Return up to 100 users worth of extended information, specified by either ID, screen name, or combination of the two. The author's most recent status (if the authenticating user has permission) will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/lookup.json
     *
     * @param ids Specifies the screen names of the users to retrieve.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/lookup">GET users/lookup | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void lookupUsers(long[] ids);

    /**
     * Run a search for users similar to the Find People button on Twitter.com; the same results returned by people search on Twitter.com will be returned by using this API.<br>
     * Usage note: It is only possible to retrieve the first 1000 matches from this API.
     * <br>This method calls http://api.twitter.com/1/users/search.json
     *
     * @param query The query to run against people search.
     * @param page  Specifies the page of results to retrieve. Number of statuses per page is fixed to 20.
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/search">GET users/search | Twitter Developers</a>
     */
    void searchUsers(String query, int page);

    /**
     * Access to Twitter's suggested user list. This returns the list of suggested user categories. The category can be used in the users/suggestions/category endpoint to get the users in that category.
     * <br>This method calls http://api.twitter.com/1/users/suggestions/:slug.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/suggestions/:slug">GET users/suggestions/:slug | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getSuggestedUserCategories();

    /**
     * Access the users in a given category of the Twitter suggested user list.<br>
     * It is recommended that end clients cache this data for no more than one hour.
     * <br>This method calls http://api.twitter.com/1/users/suggestions/:slug.json
     *
     * @param categorySlug slug
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/suggestions/slug">GET users/suggestions/slug | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getUserSuggestions(String categorySlug);

    /**
     * Access the users in a given category of the Twitter suggested user list and return their most recent status if they are not a protected user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/users/suggestions/:slug/members.json
     *
     * @param categorySlug slug
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    void getMemberSuggestions(String categorySlug);

    /**
     * Access the profile image in various sizes for the user with the indicated screen_name. If no size is provided the normal image is returned. This resource does not return JSON or XML, but instead returns a 302 redirect to the actual image resource.
     * This method should only be used by application developers to lookup or check the profile image URL for a user. This method must not be used as the image source URL presented to users of your application.
     * <br>This method calls http://api.twitter.com/1/users/profile_image/:screen_name.json
     *
     * @param screenName The screen name of the user for whom to return results for.
     * @param size       Specifies the size of image to fetch. Not specifying a size will give the default, normal size of 48px by 48px. Valid options include: BIGGER - 73px by 73px NORMAL - 48px by 48px MINI - 24px by 24px
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/profile_image/:screen_name">GET users/profile_image/:screen_name | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    void getProfileImage(String screenName, ProfileImage.ImageSize size);

    /**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/friends">GET statuses/friends | Twitter Developers</a>
     * @since Twitter4J 2.0.9
     * @deprecated use {@link FriendsFollowersMethodsAsync#getFriendsIDs(long)} and {@link UserMethodsAsync#lookupUsers(long[])} instead
     */
    void getFriendsStatuses(long cursor);

    /**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @param screenName the screen name of the user for whom to request a list of friends
     * @param cursor     Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/friends">GET statuses/friends | Twitter Developers</a>
     * @since Twitter4J 2.0.9
     * @deprecated use {@link FriendsFollowersMethodsAsync#getFriendsIDs(String, long)} and {@link UserMethodsAsync#lookupUsers(long[])} instead
     */
    void getFriendsStatuses(String screenName, long cursor);

    /**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @param userId the screen name of the user for whom to request a list of friends
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/friends">GET statuses/friends | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     * @deprecated use {@link FriendsFollowersMethodsAsync#getFriendsIDs(long, long)} and {@link UserMethodsAsync#lookupUsers(long[])} instead
     */
    void getFriendsStatuses(long userId, long cursor);

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers.json
     *
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/followers">GET statuses/followers | Twitter Developers</a>
     * @since Twitter4J 2.0.9
     * @deprecated use {@link FriendsFollowersMethodsAsync#getFollowersIDs(long)} and {@link UserMethodsAsync#lookupUsers(long[])} instead
     */
    void getFollowersStatuses(long cursor);

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers.json
     *
     * @param screenName The screen name of the user for whom to request a list of followers.
     * @param cursor     Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/followers">GET statuses/followers | Twitter Developers</a>
     * @since Twitter4J 2.0.9
     * @deprecated use {@link FriendsFollowersMethodsAsync#getFollowersIDs(String, long)} and {@link UserMethodsAsync#lookupUsers(long[])} instead
     */
    void getFollowersStatuses(String screenName, long cursor);

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers.json
     *
     * @param userId The ID of the user for whom to request a list of followers.
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/followers">GET statuses/followers | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     * @deprecated use {@link FriendsFollowersMethodsAsync#getFollowersIDs(long, long)} and {@link UserMethodsAsync#lookupUsers(long[])} instead
     */
    void getFollowersStatuses(long userId, long cursor);
}
