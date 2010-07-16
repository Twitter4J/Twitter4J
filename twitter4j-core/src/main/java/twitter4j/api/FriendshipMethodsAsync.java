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
	 * @since Twitter4J 2.0.1
     * @see <a href="http://dev.twitter.com/doc/post/friendships/create/:id">POST friendships/create/:id | dev.twitter.com</a>
	 */
	void createFriendship(String screenName);

	/**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
	 * <br>This method calls http://api.twitter.com/1/friendships/create
	 *
	 * @param userId the ID of the user to be befriended
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/post/friendships/create/:id">POST friendships/create/:id | dev.twitter.com</a>
	 */
	void createFriendship(int userId);

	/**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
	 * <br>This method calls http://api.twitter.com/1/friendships/create
	 *
	 * @param screenName the screen name of the user to be befriended
	 * @param follow Enable notifications for the target user in addition to becoming friends.
	 * @since Twitter4J 2.0.1
     * @see <a href="http://dev.twitter.com/doc/post/friendships/create/:id">POST friendships/create/:id | dev.twitter.com</a>
	 */
	void createFriendship(String screenName, boolean follow);

	/**
     * Allows the authenticating users to follow the user specified in the ID parameter.<br>
     * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.
	 * <br>This method calls http://api.twitter.com/1/friendships/create
	 *
	 * @param userId the ID of the user to be befriended
	 * @param follow Enable notifications for the target user in addition to becoming friends.
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/post/friendships/create/:id">POST friendships/create/:id | dev.twitter.com</a>
	 */
	void createFriendship(int userId, boolean follow);

	/**
     * Allows the authenticating users to unfollow the user specified in the ID parameter.<br>
     * Returns the unfollowed user in the requested format when successful. Returns a string describing the failure condition when unsuccessful.
	 * <br>This method calls http://api.twitter.com/1/friendships/destroy
	 *
	 * @param screenName the screen name of the user to be befriended
     * @see <a href="http://dev.twitter.com/doc/post/friendships/destroy">POST friendships/destroy | dev.twitter.com</a>
	 * @since Twitter4J 2.0.1
	 */
	void destroyFriendship(String screenName);

	/**
     * Allows the authenticating users to unfollow the user specified in the ID parameter.<br>
     * Returns the unfollowed user in the requested format when successful. Returns a string describing the failure condition when unsuccessful.
	 * <br>This method calls http://api.twitter.com/1/friendships/destroy
	 *
	 * @param userId the screen name of the user to be befriended
     * @see <a href="http://dev.twitter.com/doc/post/friendships/destroy">POST friendships/destroy | dev.twitter.com</a>
	 * @since Twitter4J 2.1.0
	 */
	void destroyFriendship(int userId);

	/**
     * Tests for the existence of friendship between two users. Will return true if user_a follows user_b, otherwise will return false.
	 * <br>This method calls http://api.twitter.com/1/friendships/exists
	 *
	 * @param userA The ID or screen_name of the first user to test friendship for.
	 * @param userB The ID or screen_name of the second user to test friendship for.
	 * @since Twitter4J 2.0.1
     * @see <a href="http://dev.twitter.com/doc/get/friendships/exists">GET friendships/exists | dev.twitter.com</a>
	 */
	void existsFriendship(String userA, String userB);

	/**
     * Returns detailed information about the relationship between two users.
	 * <br>This method calls http://api.twitter.com/1/friendships/show.json
	 *
	 * @param sourceScreenName the screen name of the source user
	 * @param targetScreenName the screen name of the target user
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/get/friendships/show">GET friendships/show | dev.twitter.com</a>
	 */
	void showFriendship(String sourceScreenName, String targetScreenName);

	/**
     * Returns detailed information about the relationship between two users.
	 * <br>This method calls http://api.twitter.com/1/friendships/show.json
	 *
	 * @param sourceId the screen ID of the source user
	 * @param targetId the screen ID of the target user
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/get/friendships/show">GET friendships/show | dev.twitter.com</a>
	 */
	void showFriendship(int sourceId, int targetId);

    /**
     * Returns an array of numeric IDs for every user who has a pending request to follow the authenticating user.
     * <br>This method calls http://api.twitter.com/1/friendships/incoming.json
     *
     * @param cursor Breaks the results into pages. A single page contains 5000 identifiers. Provide a value of -1 to begin paging.
     * @since Twitter4J 2.1.2
     * @see <a href="http://dev.twitter.com/doc/get/friendships/incoming">GET friendships/incoming | dev.twitter.com</a>
     */
    void getIncomingFriendships(long cursor);

    /**
     * Returns an array of numeric IDs for every protected user for whom the authenticating user has a pending follow request.
     * <br>This method calls http://api.twitter.com/1/friendships/outgoing.json
     *
     * @param cursor Breaks the results into pages. A single page contains 5000 identifiers. Provide a value of -1 to begin paging.
     * @since Twitter4J 2.1.2
     * @see <a href="http://dev.twitter.com/doc/get/friendships/outgoing">GET friendships/outgoing | dev.twitter.com</a>
     */
    void getOutgoingFriendships(long cursor);
}
