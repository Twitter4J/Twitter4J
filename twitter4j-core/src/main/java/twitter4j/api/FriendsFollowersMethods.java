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

import twitter4j.IDs;
import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface FriendsFollowersMethods {

    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param cursor Causes the list of connections to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filterd out after connections are queried. <br/>
     *               To begin paging provide a value of -1 as the cursor. The response from the API will include a previous_cursor and next_cursor to allow paging back and forth.
     * @return an array of numeric IDs for every user the authenticating user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFriendsIDs(long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the friends list.
     * @param cursor Causes the list of connections to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filterd out after connections are queried. <br/>
     *               To begin paging provide a value of -1 as the cursor. The response from the API will include a previous_cursor and next_cursor to allow paging back and forth.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFriendsIDs(long userId, long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the friends list.
     * @param cursor     Causes the list of connections to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filterd out after connections are queried. <br/>
     *                   To begin paging provide a value of -1 as the cursor. The response from the API will include a previous_cursor and next_cursor to allow paging back and forth.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/friends/ids">GET friends/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFriendsIDs(String screenName, long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param cursor Causes the list of connections to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filterd out after connections are queried. <br/>
     *               To begin paging provide a value of -1 as the cursor. The response from the API will include a previous_cursor and next_cursor to allow paging back and forth.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFollowersIDs(long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the followers list.
     * @param cursor Causes the list of connections to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filterd out after connections are queried. <br/>
     *               To begin paging provide a value of -1 as the cursor. The response from the API will include a previous_cursor and next_cursor to allow paging back and forth.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFollowersIDs(long userId, long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the followers list.
     * @param cursor     Causes the list of connections to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filterd out after connections are queried. <br/>
     *                   To begin paging provide a value of -1 as the cursor. The response from the API will include a previous_cursor and next_cursor to allow paging back and forth.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/followers/ids">GET followers/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFollowersIDs(String screenName, long cursor) throws TwitterException;
}
