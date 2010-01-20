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

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface TimelineMethodsAsync {
	/**
	 * Returns the 20 most recent statuses from non-protected users who have set a custom user icon. <a href="http://groups.google.com/group/twitter-development-talk/browse_thread/thread/f881564598a947a7#">The public timeline is cached for 60 seconds</a> so requesting it more often than that is a waste of resources.
	 * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-public_timeline">Twitter API Wiki / Twitter REST API Method: statuses public_timeline</a>
	 */
	void getPublicTimeline();

	/**
	 * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
	 * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
	 *
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
	 * @since Twitter4J 2.0.10
	 */
	void getHomeTimeline();

	/**
	 * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
	 * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
	 *
	 * @param paging   controls pagination
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
	 * @since Twitter4J 2.0.10
	 */
	void getHomeTimeline(Paging paging);


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
	 *  It's also possible to request another user's friends_timeline via the id parameter below.
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 */
	void getFriendsTimeline();

	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
	 *  It's also possible to request another user's friends_timeline via the id parameter below.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
	 * @param paging controls pagination
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFriendsTimeline(Paging paging);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified screenName.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param screenName Specifies the screen name of the user for whom to return the user_timeline.
	 * @param paging controls pagination
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 * @since Twitter4J 2.0.1
	 */
	void getUserTimeline(String screenName, Paging paging);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified screenName.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param userId Specifies the ID of the user for whom to return the user_timeline.
	 * @param paging controls pagination
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 * @since Twitter4J 2.1.0
	 */
	void getUserTimeline(int userId, Paging paging);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified user id.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param paging   controls pagination
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 * @since Twitter4J 2.0.1
	 */
	void getUserTimeline(Paging paging);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified user id.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param screenName Specifies the screen name of the user for whom to return the user_timeline.
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 */
	void getUserTimeline(String screenName);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified user id.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param userId   Specifies the ID of the user for whom to return the user_timeline.
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 * @since Twitter4J 2.1.0
	 */
	void getUserTimeline(int userId);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 */
	void getUserTimeline();

	/**
	 * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.twitter.com/1/statuses/mentions
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
	 */
	void getMentions();

	/**
	 * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.twitter.com/1/statuses/mentions
	 * @param paging controls pagination
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
	 */
	void getMentions(Paging paging);

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
	 */
	void getRetweetedByMe();

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
	 * @param paging controls pagination
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
	 */
	void getRetweetedByMe(Paging paging);

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user's friends.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
	 */
	void getRetweetedToMe();

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user's friends.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
	 * @param paging controls pagination
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
	 */
	void getRetweetedToMe(Paging paging);

	/**
	 * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
	 */
	void getRetweetsOfMe();

	/**
	 * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
	 * @param paging controls pagination
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
	 */
	void getRetweetsOfMe(Paging paging);
}
