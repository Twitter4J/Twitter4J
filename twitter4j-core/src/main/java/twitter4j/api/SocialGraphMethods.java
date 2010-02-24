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

import twitter4j.IDs;
import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface SocialGraphMethods {
    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @return an array of numeric IDs for every user the authenticating user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
     * @since Twitter4J 2.0.0
     */
    IDs getFriendsIDs() throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the authenticating user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFriendsIDs(long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is following.<br>
     * all IDs are attempted to be returned, but large sets of IDs will likely fail with timeout errors.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the friends list.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
     * @since Twitter4J 2.0.0
     */
    IDs getFriendsIDs(int userId) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the friends list.
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFriendsIDs(int userId, long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the friends list.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
     * @since Twitter4J 2.0.0
     */
    IDs getFriendsIDs(String screenName) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the friends list.
     * @param cursor     Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFriendsIDs(String screenName, long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     * @since Twitter4J 2.0.0
     */
    IDs getFollowersIDs() throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFollowersIDs(long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the followers list.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     * @since Twitter4J 2.0.0
     */
    IDs getFollowersIDs(int userId) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param userId Specifies the ID of the user for whom to return the followers list.
     * @param cursor Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFollowersIDs(int userId, long cursor) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the followers list.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     * @since Twitter4J 2.0.0
     */
    IDs getFollowersIDs(String screenName) throws TwitterException;

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     *
     * @param screenName Specifies the screen name of the user for whom to return the followers list.
     * @param cursor     Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     * @since Twitter4J 2.0.10
     */
    IDs getFollowersIDs(String screenName, long cursor) throws TwitterException;
}
