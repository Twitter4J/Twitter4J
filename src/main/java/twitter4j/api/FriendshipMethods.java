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

import twitter4j.Relationship;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface FriendshipMethods
{
	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
	 * <br>This method calls http://api.twitter.com/1/friendships/create/[id].json
	 *
	 * @param screenName the screen name of the user to be befriended
	 * @return the befriended user
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships create</a>
	 */
	User createFriendship(String screenName)
			throws TwitterException;

	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
	 * <br>This method calls http://api.twitter.com/1/friendships/create/[id].json
	 *
	 * @param userId the ID of the user to be befriended
	 * @return the befriended user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships create</a>
	 */
	User createFriendship(int userId)
			throws TwitterException;
	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
	 * <br>This method calls http://api.twitter.com/1/friendships/create/[id].json
	 *
	 * @param screenName the screen name of the user to be befriended
	 * @param follow Enable notifications for the target user in addition to becoming friends.
	 * @return the befriended user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships create</a>
	 */
	User createFriendship(String screenName, boolean follow)
			throws TwitterException;

	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
	 * <br>This method calls http://api.twitter.com/1/friendships/create/[id].json
	 *
	 * @param userId the ID of the user to be befriended
	 * @param follow Enable notifications for the target user in addition to becoming friends.
	 * @return the befriended user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships create</a>
	 */
	User createFriendship(int userId, boolean follow)
			throws TwitterException;

	/**
	 * Discontinues friendship with the user specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
	 * <br>This method calls http://api.twitter.com/1/friendships/destroy/[id].json
	 *
	 * @param screenName the screen name of the user for whom to request a list of friends
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: friendships destroy</a>
	 */
	User destroyFriendship(String screenName)
			throws TwitterException;

	/**
	 * Discontinues friendship with the user specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
	 * <br>This method calls http://api.twitter.com/1/friendships/destroy/[id].json
	 *
	 * @param userId the ID of the user for whom to request a list of friends
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: friendships destroy</a>
	 * @since Twitter4J 2.1.0
	 */
	User destroyFriendship(int userId)
			throws TwitterException;

	/**
	 * Tests if a friendship exists between two users.
	 * <br>This method calls http://api.twitter.com/1/friendships/exists.json
	 *
	 * @param userA The ID or screen_name of the first user to test friendship for.
	 * @param userB The ID or screen_name of the second user to test friendship for.
	 * @return if a friendship exists between two users.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-exists">Twitter API Wiki / Twitter REST API Method: friendships exists</a>
	 */
	boolean existsFriendship(String userA, String userB)
			throws TwitterException;

	/**
	 * Gets the detailed relationship status between a source user and a target user
	 * <br>This method calls http://api.twitter.com/1/friendships/show.json
	 * @param sourceScreenName the screen name of the source user
	 * @param targetScreenName the screen name of the target user
	 * @return Relationship
	 *
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">Twitter API Wiki / Twitter REST API Method: friendships show</a>
	 */
	Relationship showFriendship(String sourceScreenName, String targetScreenName)
			throws TwitterException;

	/**
	 * Gets the detailed relationship status between a source user and a target user
	 * <br>This method calls http://api.twitter.com/1/friendships/show.json
	 *
	 * @param sourceId the screen ID of the source user
	 * @param targetId the screen ID of the target user
	 * @return Relationship
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">Twitter API Wiki / Twitter REST API Method: friendships show</a>
	 */
	Relationship showFriendship(int sourceId, int targetId)
			throws TwitterException;
}
