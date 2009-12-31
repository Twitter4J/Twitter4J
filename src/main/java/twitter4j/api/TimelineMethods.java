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

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface TimelineMethods
{
	/**
	 * Returns the 20 most recent statuses from non-protected users who have set a custom user icon. <a href="http://groups.google.com/group/twitter-development-talk/browse_thread/thread/f881564598a947a7#">The public timeline is cached for 60 seconds</a> so requesting it more often than that is a waste of resources.
	 * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
	 *
	 * @return list of statuses of the Public Timeline
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-public_timeline">Twitter API Wiki / Twitter REST API Method: statuses public_timeline</a>
	 */
	ResponseList<Status> getPublicTimeline()
			throws TwitterException;

	/**
	 * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
	 * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
	 *
	 * @return list of the home Timeline
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
	 * @since Twitter4J 2.0.10
	 */
	ResponseList<Status> getHomeTimeline()
			throws TwitterException;


	/**
	 * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
	 * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
	 *
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return list of the home Timeline
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
	 * @since Twitter4J 2.0.10
	 */
	ResponseList<Status> getHomeTimeline(Paging paging)
			throws TwitterException;

	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating1 user and that user's friends.
	 * It's also possible to request another user's friends_timeline via the id parameter below.<br>
     * Note: Retweets will not appear in the friends_timeline for backwards compatibility. If you want retweets included use getHomeTimeline().
	 * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
	 *
	 * @return list of the Friends Timeline
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 */
    ResponseList<Status> getFriendsTimeline() throws TwitterException;

    /**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
	 *
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return list of the Friends Timeline
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 */
	ResponseList<Status> getFriendsTimeline(Paging paging)
			throws TwitterException;


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified screen name.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline.json
	 *
	 * @param screenName specifies the screen name of the user for whom to return the user_timeline
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return list of the user Timeline
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 */
	ResponseList<Status> getUserTimeline(String screenName, Paging paging)
			throws TwitterException;

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified screen name.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline.json
	 *
	 * @param userId specifies the ID of the user for whom to return the user_timeline
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return list of the user Timeline
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 */
	ResponseList<Status> getUserTimeline(int userId, Paging paging)
			throws TwitterException;

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param screenName specifies the screen name of the user for whom to return the user_timeline
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 */
	ResponseList<Status> getUserTimeline(String screenName)
			throws TwitterException;

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param user_id specifies the ID of the user for whom to return the user_timeline
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 * @since Twitter4J 2.1.0
	 */
	ResponseList<Status> getUserTimeline(int user_id)
			throws TwitterException;

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 */
	ResponseList<Status> getUserTimeline()
			throws TwitterException;

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 * @since Twitter4J 2.0.1
	 */
	ResponseList<Status> getUserTimeline(Paging paging)
			throws TwitterException;

	/**
	 * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/mentions
	 *
	 * @return the 20 most recent replies
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
	 */
	ResponseList<Status> getMentions()
			throws TwitterException;

	/**
	 * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/mentions
	 *
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return the 20 most recent replies
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
	 */
	ResponseList<Status> getMentions(Paging paging)
			throws TwitterException;

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
	 *
	 * @return the 20 most recent retweets posted by the authenticating user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
	 */
	ResponseList<Status> getRetweetedByMe()
			throws TwitterException;

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
	 *
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return the 20 most recent retweets posted by the authenticating user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
	 */
	ResponseList<Status> getRetweetedByMe(Paging paging)
			throws TwitterException;

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user's friends.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
	 *
	 * @return the 20 most recent retweets posted by the authenticating user's friends.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
	 */
	ResponseList<Status> getRetweetedToMe()
			throws TwitterException;

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user's friends.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
	 *
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return the 20 most recent retweets posted by the authenticating user's friends.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
	 */
	ResponseList<Status> getRetweetedToMe(Paging paging)
			throws TwitterException;

	/**
	 * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
	 *
	 * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
	 */
	ResponseList<Status> getRetweetsOfMe()
			throws TwitterException;

	/**
	 * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
	 *
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
	 */
	ResponseList<Status> getRetweetsOfMe(Paging paging)
			throws TwitterException;
}
