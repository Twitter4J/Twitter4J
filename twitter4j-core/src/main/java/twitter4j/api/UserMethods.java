/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.api;

import twitter4j.Category;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface UserMethods {
	/**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter. The author's most recent status will be returned inline.
	 * <br>This method calls http://api.twitter.com/1/users/show.json
	 *
	 * @param screenName the screen name of the user for whom to request the detail
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/users/show">GET users/show | dev.twitter.com</a>
	 */
    User showUser(String screenName) throws TwitterException;

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter. The author's most recent status will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/show.json
	 *
	 * @param userId the ID of the user for whom to request the detail
	 * @return users
	 * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/users/show">GET users/show | dev.twitter.com</a>
	 * @since Twitter4J 2.1.0
	 */
    User showUser(int userId) throws TwitterException;

    /**
     * Return up to 100 users worth of extended information, specified by either ID, screen name, or combination of the two. The author's most recent status (if the authenticating user has permission) will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/lookup.json
     * @param screenNames Specifies the screen names of the users to return.
     * @return users
     * @see <a href="http://dev.twitter.com/doc/get/users/lookup">GET users/lookup | dev.twitter.com</a>
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> lookupUsers(String[] screenNames) throws TwitterException;

    /**
     * Return up to 100 users worth of extended information, specified by either ID, screen name, or combination of the two. The author's most recent status (if the authenticating user has permission) will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/lookup.json
     * @param ids Specifies the screen names of the users to return.
     * @return users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/users/lookup">GET users/lookup | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> lookupUsers(int[] ids) throws TwitterException;

    /**
     * Run a search for users similar to the Find People button on Twitter.com; the same results returned by people search on Twitter.com will be returned by using this API.<br>
     * Usage note: It is only possible to retrieve the first 1000 matches from this API.
     * <br>This method calls http://api.twitter.com/1/users/search.json
     *
     * @param query The query to run against people search. 
     * @param page Specifies the page of results to retrieve. Number of statuses per page is fixed to 20.
     * @return the list of Users matches the provided
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/users/search">GET users/search | dev.twitter.com</a>
     */
    ResponseList<User> searchUsers(String query, int page) throws TwitterException;

    /**
     * Access to Twitter's suggested user list. This returns the list of suggested user categories. The category can be used in the users/suggestions/category endpoint to get the users in that category.
     * <br>This method calls http://api.twitter.com/1/users/suggestions.json
     * @return list of suggested user categories.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.1
     * @see <a href="http://dev.twitter.com/doc/get/users/suggestions">GET users/suggestions | dev.twitter.com</a>
     */
    ResponseList<Category> getSuggestedUserCategories() throws TwitterException;

    /**
     * Access the users in a given category of the Twitter suggested user list.<br>
     * It is recommended that end clients cache this data for no more than one hour.
     * <br>This method calls http://api.twitter.com/1/users/suggestions/:slug.json
     * @param categorySlug slug
     * @return list of suggested users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/users/suggestions/slug">GET users/suggestions/slug | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> getUserSuggestions(String categorySlug) throws TwitterException;

    /**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
	 * <br>This method calls http://api.twitter.com/1/statuses/friends.json
	 *
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/statuses/friends">GET statuses/friends | dev.twitter.com</a>
	 */
    PagableResponseList<User> getFriendsStatuses() throws TwitterException;

    /**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
	 * <br>This method calls http://api.twitter.com/1/statuses/friends.json
	 *
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
     * @see <a href="http://dev.twitter.com/doc/get/statuses/friends">GET statuses/friends | dev.twitter.com</a>
	 */
	PagableResponseList<User> getFriendsStatuses(long cursor)
			throws TwitterException;

	/**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends.json
	 *
	 * @param screenName the screen name of the user for whom to request a list of friends
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/statuses/friends">GET statuses/friends | dev.twitter.com</a>
	 * @since Twitter4J 2.0.9
	 */
	PagableResponseList<User> getFriendsStatuses(String screenName)
			throws TwitterException;

	/**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends.json
	 *
	 * @param userId the ID of the user for whom to request a list of friends
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/statuses/friends">GET statuses/friends | dev.twitter.com</a>
	 * @since Twitter4J 2.1.0
	 */
	PagableResponseList<User> getFriendsStatuses(int userId)
			throws TwitterException;

	/**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
	 * <br>This method calls http://api.twitter.com/1/statuses/friends.json
	 *
	 * @param screenName the screen name of the user for whom to request a list of friends
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
     * @see <a href="http://dev.twitter.com/doc/get/statuses/friends">GET statuses/friends | dev.twitter.com</a>
	 */
	PagableResponseList<User> getFriendsStatuses(String screenName, long cursor)
			throws TwitterException;

	/**
     * Returns a user's friends, each with current status inline. They are ordered by the order in which the user followed them, most recently followed first, 100 at a time. (Please note that the result set isn't guaranteed to be 100 every time as suspended users will be filtered out.)
	 * <br>This method calls http://api.twitter.com/1/statuses/friends.json
	 *
	 * @param userId the ID of the user for whom to request a list of friends
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/get/statuses/friends">GET statuses/friends | dev.twitter.com</a>
	 */
	PagableResponseList<User> getFriendsStatuses(int userId, long cursor)
			throws TwitterException;

	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).<br>
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/followers.json
	 *
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/statuses/followers">GET statuses/followers | dev.twitter.com</a>
	 * @since Twitter4J 2.0.9
	 */
    PagableResponseList<User> getFollowersStatuses() throws TwitterException;

    /**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers.json
	 *
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
     * @see <a href="http://dev.twitter.com/doc/get/statuses/followers">GET statuses/followers | dev.twitter.com</a>
	 */
	PagableResponseList<User> getFollowersStatuses(long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers.json
	 *
	 * @param screenName The screen name of the user for whom to request a list of followers.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
     * @see <a href="http://dev.twitter.com/doc/get/statuses/followers">GET statuses/followers | dev.twitter.com</a>
	 */
	PagableResponseList<User> getFollowersStatuses(String screenName)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers.json
	 *
	 * @param userId The ID of the user for whom to request a list of followers.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/get/statuses/followers">GET statuses/followers | dev.twitter.com</a>
	 */
	PagableResponseList<User> getFollowersStatuses(int userId)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers.json
	 *
	 * @param screenName The screen name of the user for whom to request a list of followers.
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
     * @see <a href="http://dev.twitter.com/doc/get/statuses/followers">GET statuses/followers | dev.twitter.com</a>
	 */
	PagableResponseList<User> getFollowersStatuses(String screenName, long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers.json
	 *
	 * @param userId   The ID of the user for whom to request a list of followers.
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/get/statuses/followers">GET statuses/followers | dev.twitter.com</a>
	 */
	PagableResponseList<User> getFollowersStatuses(int userId, long cursor)
			throws TwitterException;
}
