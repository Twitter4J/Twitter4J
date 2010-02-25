/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface UserMethods
{
	/**
	 * Returns extended information of a given user, specified by screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
	 * <br>This method calls http://api.twitter.com/1/users/show.json
	 *
	 * @param screenName the screen name of the user for whom to request the detail
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-users%C2%A0show">Twitter API Wiki / Twitter REST API Method: users show</a>
	 */
	User showUser(String screenName)
			throws TwitterException;

	/**
	 * Returns extended information of a given user, specified by ID.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
	 * <br>This method calls http://api.twitter.com/1/users/show
	 *
	 * @param userId the ID of the user for whom to request the detail
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-users%C2%A0show">Twitter API Wiki / Twitter REST API Method: users show</a>
	 * @since Twitter4J 2.1.0
	 */
	User showUser(int userId)
			throws TwitterException;

    /**
     * Run a search for users similar to Find People button on Twitter.com; the same results returned by people search on Twitter.com will be returned by using this API (about being listed in the People Search).  It is only possible to retrieve the first 1000 matches from this API.
     * <br>This method calls http://api.twitter.com/1/users/search.json
     *
     * @param query The query to run against people search. 
     * @param page Specifies the page of results to retrieve. Number of statuses per page is fixed to 20.
     * @return the list of Users matches the provided
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-users-search">Twitter API Wiki / Twitter REST API Method: users search</a>
     */
    ResponseList<User> searchUsers(String query, int page) throws TwitterException;

    /**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 */
	PagableResponseList<User> getFriendsStatuses()
			throws TwitterException;

	/**
	 * Returns the user's friends, each with current status inline.<br>
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 */
	PagableResponseList<User> getFriendsStatuses(long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param screenName the screen name of the user for whom to request a list of friends
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 * @since Twitter4J 2.0.9
	 */
	PagableResponseList<User> getFriendsStatuses(String screenName)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param userId the ID of the user for whom to request a list of friends
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 * @since Twitter4J 2.1.0
	 */
	PagableResponseList<User> getFriendsStatuses(int userId)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param screenName the screen name of the user for whom to request a list of friends
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 */
	PagableResponseList<User> getFriendsStatuses(String screenName, long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param userId the ID of the user for whom to request a list of friends
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 */
	PagableResponseList<User> getFriendsStatuses(int userId, long cursor)
			throws TwitterException;


	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).<br>
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 * @since Twitter4J 2.0.9
	 */
	PagableResponseList<User> getFollowersStatuses()
			throws TwitterException;

	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param screenName The screen name of the user for whom to request a list of followers.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(String screenName)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param userId The ID of the user for whom to request a list of followers.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(int userId)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param screenName The screen name of the user for whom to request a list of followers.
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(String screenName, long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param userId   The ID of the user for whom to request a list of followers.
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(int userId, long cursor)
			throws TwitterException;
}
