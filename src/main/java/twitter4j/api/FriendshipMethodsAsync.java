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

import twitter4j.TwitterListener;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface FriendshipMethodsAsync
{
	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/friendships/create
	 *
	 * @param screenName the screen name of the user to be befriended
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0create</a>
	 */
	void createFriendshipAsync(String screenName, TwitterListener listener);

	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/friendships/create
	 *
	 * @param userId the ID of the user to be befriended
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0create</a>
	 */
	void createFriendshipAsync(int userId, TwitterListener listener);

	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/friendships/create
	 *
	 * @param screenName the screen name of the user to be befriended
	 * @param follow Enable notifications for the target user in addition to becoming friends.
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0create</a>
	 */
	void createFriendshipAsync(String screenName, boolean follow, TwitterListener listener);

	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/friendships/create
	 *
	 * @param userId the ID of the user to be befriended
	 * @param follow Enable notifications for the target user in addition to becoming friends.
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0create</a>
	 */
	void createFriendshipAsync(int userId, boolean follow, TwitterListener listener);

	/**
	 * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/friendships/destroy
	 *
	 * @param screenName the screen name of the user to be befriended
	 * @param listener   a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0destroy</a>
	 * @since Twitter4J 2.0.1
	 */
	void destroyFriendshipAsync(String screenName, TwitterListener listener);

	/**
	 * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/friendships/destroy
	 *
	 * @param userId the screen name of the user to be befriended
	 * @param listener   a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0destroy</a>
	 * @since Twitter4J 2.1.0
	 */
	void destroyFriendshipAsync(int userId, TwitterListener listener);

	/**
	 * Tests if a friendship exists between two users.
	 * <br>This method calls http://api.twitter.com/1/friendships/exists
	 *
	 * @param userA The ID or screen_name of the first user to test friendship for.
	 * @param userB The ID or screen_name of the second user to test friendship for.
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships-exists">Twitter API Wiki / Twitter REST API Method: friendships exists</a>
	 */
	void existsFriendshipAsync(String userA, String userB, TwitterListener listener);

	/**
	 * Gets the detailed relationship status between a source user and a target user
	 * <br>This method calls http://api.twitter.com/1/friendships/show.json
	 *
	 * @param sourceScreenName the screen name of the source user
	 * @param targetScreenName the screen name of the target user
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">Twitter API Wiki / Twitter REST API Method: friendships show</a>
	 */
	void showFriendhipAsync(String sourceScreenName, String targetScreenName, TwitterListener listener);

	/**
	 * Gets the detailed relationship status between a source user and a target user
	 * <br>This method calls http://api.twitter.com/1/friendships/show.json
	 *
	 * @param sourceId the screen ID of the source user
	 * @param targetId the screen ID of the target user
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">Twitter API Wiki / Twitter REST API Method: friendships show</a>
	 */
	void showFriendshipAsync(int sourceId, int targetId, TwitterListener listener);
}
