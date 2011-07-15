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
public interface FriendsFollowersMethodsAsync {
    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFriendsIDs(long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the friends list.
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFriendsIDs(long userId, long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the friends list.
     * @param cursor     Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFriendsIDs(String screenName, long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFollowersIDs(long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the followers list.
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFollowersIDs(long userId, long cursor);

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the followers list.
     * @param cursor     Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void getFollowersIDs(String screenName, long cursor);
}
