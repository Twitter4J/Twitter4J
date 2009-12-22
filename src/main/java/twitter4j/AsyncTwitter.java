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
package twitter4j;

import java.io.File;
import java.util.Date;
import static twitter4j.TwitterMethod.*;

/**
 * Twitter API with a series of asynchronous APIs.<br>
 * With this class, you can call TwitterAPI asynchronously.<br>
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterListener
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AsyncTwitter extends Twitter {
    private static final long serialVersionUID = -2008667933225051907L;

    public AsyncTwitter(String id, String password) {
        super(id, password);
    }

    /**
     * Returns tweets that match a specified query.
     * <br>This method calls http://search.twitter.com/search
     *
     * @param query - the search condition
     * @param listener a listener object that receives the response
     * @since Twitter4J 1.1.7
     * @see <a href="http://apiwiki.twitter.com/Search-API-Documentation">Twitter API / Search API Documentation</a>
     * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
     */
    public void searchAsync(Query query, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH, listener, new Object[]{query}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.searched(search((Query) args[0]));
            }
        });
    }

    /**
     * Returns the top ten topics that are currently trending on Twitter.  The response includes the time of the request, the name of each trend, and the url to the <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
     * <br>This method calls http://search.twitter.com/trends
     * @since Twitter4J 2.0.2
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
     */
    public void getTrendsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(TRENDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotTrends(getTrends());
            }
        });
    }

    /**
     * Returns the current top 10 trending topics on Twitter.  The response includes the time of the request, the name of each trending topic, and query used on <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
     * <br>This method calls http://search.twitter.com/trends/current
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
     */
    public void getCurrentTrendsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CURRENT_TRENDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotCurrentTrends(getCurrentTrends());
            }
        });
    }

    /**
     * Returns the current top 10 trending topics on Twitter.  The response includes the time of the request, the name of each trending topic, and query used on <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
     * <br>This method calls http://search.twitter.com/trends/current
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @since Twitter4J 2.0.2
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
     */
    public void getCurrentTrendsAsync(boolean excludeHashTags, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CURRENT_TRENDS, listener
                , new Object[]{excludeHashTags}) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotCurrentTrends(getCurrentTrends((Boolean)args[0]));
            }
        });
    }

    /**
     * Returns the top 20 trending topics for each hour in a given day.
     * <br>This method calls http://search.twitter.com/trends/daily
     * @since Twitter4J 2.0.2
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-daily">Twitter Search API Method: trends daily</a>
     */
    public void getDailyTrendsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DAILY_TRENDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotDailyTrends(getDailyTrends());
            }
        });
    }

    /**
     * Returns the top 20 trending topics for each hour in a given day.
     * <br>This method calls http://search.twitter.com/trends/daily
     * @param date Permits specifying a start date for the report.
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-daily">Twitter Search API Method: trends daily</a>
     */
    public void getDailyTrendsAsync(Date date, boolean excludeHashTags, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DAILY_TRENDS, listener
                , new Object[]{date, excludeHashTags}) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotDailyTrends(getDailyTrends((Date) args[0]
                        , (Boolean) args[1]));
            }
        });
    }

    /**
     * Returns the top 30 trending topics for each day in a given week.
     * <br>This method calls http://search.twitter.com/trends/weekly
     * @since Twitter4J 2.0.2
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-weekly">Twitter Search API Method: trends weekly</a>
     */
    public void getWeeklyTrendsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(WEEKLY_TRENDS, listener
                , null) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotWeeklyTrends(getWeeklyTrends());
            }
        });
    }

    /**
     * Returns the top 30 trending topics for each day in a given week.
     * <br>This method calls http://search.twitter.com/trends/weekly
     * @param date Permits specifying a start date for the report.
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @since Twitter4J 2.0.2
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-weekly">Twitter Search API Method: trends weekly</a>
     */
    public void getWeeklyTrendsAsync(Date date, boolean excludeHashTags, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(WEEKLY_TRENDS, listener
                , new Object[]{date, excludeHashTags}) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotWeeklyTrends(getWeeklyTrends((Date) args[0]
                        , (Boolean) args[1]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses from non-protected users who have set a custom user icon. <a href="http://groups.google.com/group/twitter-development-talk/browse_thread/thread/f881564598a947a7#">The public timeline is cached for 60 seconds</a> so requesting it more often than that is a waste of resources.
     * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-public_timeline">Twitter API Wiki / Twitter REST API Method: statuses public_timeline</a>
     */
    public void getPublicTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline());
            }
        });
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
     * @param sinceID String
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-public_timeline">Twitter API Wiki / Twitter REST API Method: statuses public_timeline</a>
     */
    public void getPublicTimelineAsync(long sinceID, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, new Long[] {sinceID}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline( (Long) args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
     * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
     *
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
     * @since Twitter4J 2.0.10
     */
    public void getHomeTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotHomeTimeline(getHomeTimeline());
            }
        });
    }

    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
     * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
     *
     * @param paging   controls pagination
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
     * @since Twitter4J 2.0.10
     */
    public void getHomeTimelineAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotHomeTimeline(getHomeTimeline((Paging) args[0]));
            }
        });
    }


    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
     *  It's also possible to request another user's friends_timeline via the id parameter below.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     */
    public void getFriendsTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline());
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
     *  It's also possible to request another user's friends_timeline via the id parameter below.
     * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     * @since Twitter4J 2.0.1
     */

    public void getFriendsTimelineAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline((Paging)args[0]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified screenName.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param screenName Specifies the screen name of the user for whom to return the user_timeline.
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
     * @since Twitter4J 2.0.1
     */
    public void getUserTimelineAsync(String screenName, Paging paging,
                                                  TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener,
                new Object[]{screenName, paging}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotUserTimeline(getUserTimeline((String) args[0],
                        (Paging) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified screenName.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param userId Specifies the ID of the user for whom to return the user_timeline.
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
     * @since Twitter4J 2.1.0
     */
    public void getUserTimelineAsync(int userId, Paging paging,
                                                  TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener,
                new Object[]{userId, paging}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotUserTimeline(getUserTimeline((Integer) args[0],
                        (Paging) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param paging   controls pagination
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
     * @since Twitter4J 2.0.1
     */
    public void getUserTimelineAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener,
                new Object[]{paging}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotUserTimeline(getUserTimeline((Paging) args[0]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param screenName Specifies the screen name of the user for whom to return the user_timeline.
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
     */
    public void getUserTimelineAsync(String screenName, TwitterListener listener) {
        getUserTimelineAsync(screenName, new Paging(), listener);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param userId   Specifies the ID of the user for whom to return the user_timeline.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
     * @since Twitter4J 2.1.0
     */
    public void getUserTimelineAsync(int userId, TwitterListener listener) {
        getUserTimelineAsync(userId, new Paging(), listener);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
     */
    public void getUserTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline());
            }
        });
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * <br>This method calls http://api.twitter.com/1/statuses/mentions
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
     */
    public void getMentionsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotMentions(getMentions());
            }
        });
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * <br>This method calls http://api.twitter.com/1/statuses/mentions
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
     */
    public void getMentionsAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotMentions(getMentions((Paging)args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
     */
    public void getRetweetedByMeAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedByMe(getRetweetedByMe());
            }
        });
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
     */
    public void getRetweetedByMeAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedByMe(getRetweetedByMe((Paging)args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
     */
    public void getRetweetedToMeAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedToMe(getRetweetedToMe());
            }
        });
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
     */
    public void getRetweetedToMeAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedToMe(getRetweetedToMe((Paging)args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
     */
    public void getRetweetsOfMeAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetsOfMe(getRetweetsOfMe());
            }
        });
    }

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
     */
    public void getRetweetsOfMeAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetsOfMe(getRetweetsOfMe((Paging)args[0]));
            }
        });
    }

    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     * <br>This method calls http://api.twitter.com/1/statuses/show
     * @param id int
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0show">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0show</a>
     */
    public void showStatusAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_STATUS, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotShowStatus(showStatus( (Long) args[0]));
            }
        });
    }

    /**
     *
     * Updates the user's status asynchronously
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status String
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0update</a>
     * @since Twitter4J 2.0.1
     */
    public void updateStatusAsync(String status, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new String[] {status}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updatedStatus(updateStatus( (String) args[0]));
            }
        });
    }

    /**
     *
     * Updates the user's status asynchronously
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status String
     * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0update</a>
     * @since Twitter4J 2.0.1
     */
    public void updateStatusAsync(String status, long inReplyToStatusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new Object[]{status, inReplyToStatusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedStatus(updateStatus((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * Destroys the status specified by the required ID parameter. asynchronously
     * <br>This method calls http://api.twitter.com/1/statuses/destroy
     *
     * @param statusId String
     * @param listener a listener object that receives the response
     * @since 1.1.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0destroy</a>
     */
    public void destroyStatusAsync(long statusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_STATUS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedStatus(destroyStatus(((Long) args[0])));
            }
        });
    }

    /**
     * Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.
     * <br>This method calls http://api.twitter.com/1/statuses/retweet
     * @param statusId The ID of the status to retweet.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweet">Twitter API Wiki / Twitter REST API Method: statuses retweet</a>
     */
    public void retweetStatusAsync(long statusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEET_STATUS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.retweetedStatus(retweetStatus(((Long) args[0])));
            }
        });
    }

    /**
     * Retrieves extended information of a given user, specified by screen name.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     * <br>This method calls http://api.twitter.com/1/users/show
     * @param screenName the screen name of the user for whom to request the detail
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-users%C2%A0show">Twitter API Wiki / Twitter REST API Method: users%C2%A0show</a>
     */
    public void showUserAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listener, new Object[] {screenName}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserDetail(showUser( (String) args[0]));
            }
        });
    }

    /**
     * Retrieves extended information of a given user, specified by screen name.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     * <br>This method calls http://api.twitter.com/1/users/show
     * @param userId the ID of the user for whom to request the detail
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-users%C2%A0show">Twitter API Wiki / Twitter REST API Method: users%C2%A0show</a>
     */
    public void showUserAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listener, new Object[] {userId}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserDetail(showUser( (Integer) args[0]));
            }
        });
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0friends</a>
     * @since Twitter4J 2.0.9
     */
    public void getFriendsStatusesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(getFriendsStatuses());
            }
        });
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0friends</a>
     */
    public void getFriendsStatusesAsync(long cursor,TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[]{cursor}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(getFriendsStatuses((Long)args[0]));
            }
        });
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     * @param screenName the screen name of the user for whom to request a list of friends
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0friends</a>
     * @since Twitter4J 2.0.9
     */
    public void getFriendsStatusesAsync(String screenName, TwitterListener listener) {
        getFriendsStatusesAsync(screenName, -1l, listener);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     * @param userId the ID of the user for whom to request a list of friends
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0friends</a>
     * @since Twitter4J 2.1.0
     */
    public void getFriendsStatusesAsync(int userId, TwitterListener listener) {
        getFriendsStatusesAsync(userId, -1l, listener);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     * @param screenName the screen name of the user for whom to request a list of friends
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0friends</a>
     * @since Twitter4J 2.0.9
     */
    public void getFriendsStatusesAsync(String screenName, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[] {screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(getFriendsStatuses((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     * @param userId the screen name of the user for whom to request a list of friends
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0friends</a>
     * @since Twitter4J 2.1.0
     */
    public void getFriendsStatusesAsync(int userId, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[] {userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(getFriendsStatuses((Integer) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0followers</a>
     * @since Twitter4J 2.0.9
     */
    public void getFollowersStatusesAsync(TwitterListener listener) {
        getFollowersStatusesAsync(-1l, listener);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0followers</a>
     */
    public void getFollowersStatusesAsync(long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(getFollowersStatuses((Long) args[0]));
            }
        });
    }

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     *
     * @param screenName The screen name of the user for whom to request a list of followers.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0followers</a>
     */
    public void getFollowersStatusesAsync(String screenName, TwitterListener listener) {
        getFollowersStatusesAsync(screenName, -1l, listener);
    }

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     *
     * @param userId The ID of the user for whom to request a list of followers.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0followers</a>
     */
    public void getFollowersStatusesAsync(int userId, TwitterListener listener) {
        getFollowersStatusesAsync(userId, -1l, listener);
    }

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     * @param screenName The screen name of the user for whom to request a list of followers.
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0followers</a>
     */
    public void getFollowersStatusesAsync(String screenName, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(getFollowersStatuses((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     * @param userId The ID of the user for whom to request a list of followers.
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0followers</a>
     */
    public void getFollowersStatusesAsync(int userId, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(getFollowersStatuses((Integer) args[0], (Long) args[1]));
            }
        });
    }


    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * <br>This method calls http://api.twitter.com/1/direct_messages
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages">Twitter API Wiki / Twitter REST API Method: direct_messages</a>
     */
    public void getDirectMessagesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages());
            }
        });
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * <br>This method calls http://api.twitter.com/1/direct_messages
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages">Twitter API Wiki / Twitter REST API Method: direct_messages</a>
     */
    public void getDirectMessagesAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages( (Paging) args[0]));
            }
        });
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/direct_messages/sent
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0sent">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0sent</a>
     */
    public void getSentDirectMessagesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SENT_DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages());
            }
        });
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/direct_messages/sent
     * @param paging controls pagination
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0sent">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0sent</a>
     */
    public void getSentDirectMessagesAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SENT_DIRECT_MESSAGES, listener, new Object[] {paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages( (Paging) args[0]));
            }
        });
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimed if the length of the text is exceeding 140 characters.
     * <br>This method calls http://api.twitter.com/1/direct_messages/new
     * @param screenName the screen name of the user to whom send the direct message
     * @param text The text of your direct message.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0new">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0new</a>
     */
    public void sendDirectMessageAsync(String screenName, String text, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new String[] {screenName, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(sendDirectMessage( (String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimed if the length of the text is exceeding 140 characters.
     * <br>This method calls http://api.twitter.com/1/direct_messages/new
     * @param userId the screen name of the user to whom send the direct message
     * @param text The text of your direct message.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0new">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0new</a>
     * @since Twitter4j 2.1.0
     */
    public void sendDirectMessageAsync(int userId, String text, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new Object[] {userId, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(sendDirectMessage( (Integer) args[0], (String) args[1]));
            }
        });
    }

    /**
     * Delete specified direct message
     * <br>This method calls http://api.twitter.com/1/direct_messages/destroy
     * @param id int
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0destroy</a>
     * @since Twitter4J 2.0.1
     */
    public void destroyDirectMessageAsync(int id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_DIRECT_MESSAGES, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyedDirectMessage(destroyDirectMessage((Integer) args[0]));
            }
        });
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/friendships/create
     *
     * @param screenName the screen name of the user to be befriended
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0create</a>
     */
    public void createFriendshipAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((String) args[0]));
            }
        });
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/friendships/create
     *
     * @param userId the ID of the user to be befriended
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0create</a>
     */
    public void createFriendshipAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((Integer) args[0]));
            }
        });
    }

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
    public void createFriendshipAsync(String screenName, boolean follow, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Object[]{screenName,follow}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((String) args[0],(Boolean)args[1]));
            }
        });
    }

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
    public void createFriendshipAsync(int userId, boolean follow, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Object[]{userId,follow}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((Integer) args[0],(Boolean)args[1]));
            }
        });
    }

    /**
     * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/friendships/destroy
     *
     * @param screenName the screen name of the user to be befriended
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0destroy</a>
     * @since Twitter4J 2.0.1
     */
    public void destroyFriendshipAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFriendship(destroyFriendship((String) args[0]));
            }
        });
    }

    /**
     * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/friendships/destroy
     *
     * @param userId the screen name of the user to be befriended
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: friendships%C2%A0destroy</a>
     * @since Twitter4J 2.1.0
     */
    public void destroyFriendshipAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFriendship(destroyFriendship((Integer) args[0]));
            }
        });
    }

    /**
     * Tests if a friendship exists between two users.
     * <br>This method calls http://api.twitter.com/1/friendships/exists
     *
     * @param userA The ID or screen_name of the first user to test friendship for.
     * @param userB The ID or screen_name of the second user to test friendship for.
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships-exists">Twitter API Wiki / Twitter REST API Method: friendships exists</a>
     */
    public void existsFriendshipAsync(String userA, String userB, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_FRIENDSHIP, listener, new String[]{userA, userB}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsFriendship(existsFriendship((String) args[0], (String) args[1]));
            }
        });
    }

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
    public void showFriendhipAsync(String sourceScreenName, String targetScreenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listener, new String[]{sourceScreenName, targetScreenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotShowFriendship(showFriendship((String) args[0], (String) args[1]));
            }
        });
    }

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
    public void showFriendshipAsync(int sourceId, int targetId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listener, new Integer[]{sourceId, targetId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotShowFriendship(showFriendship((Integer) args[0], (Integer) args[1]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
     *
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
     */
    public void getFriendsIDsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs());
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
     *
     * @param listener a listener object that receives the response
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
     * @since Twitter4J 2.0.10
     */
    public void getFriendsIDsAsync(long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener
                , new Object[]{cursor}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((Long) args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
     *
     * @param userId   Specfies the ID of the user for whom to return the friends list.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
     * @since Twitter4J 2.0.0
     */
    public void getFriendsIDsAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((Integer) args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
     *
     * @param userId   Specifies the ID of the user for whom to return the friends list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
     * @since Twitter4J 2.0.10
     */
    public void getFriendsIDsAsync(int userId, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new Object[]{userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((Integer) args[0],(Long)args[1]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
     *
     * @param screenName Specifies the screen name of the user for whom to return the friends list.
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
     * @since Twitter4J 2.0.0
     */
    public void getFriendsIDsAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((String) args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
     *
     * @param screenName Specfies the screen name of the user for whom to return the friends list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
     * @since Twitter4J 2.0.10
     */
    public void getFriendsIDsAsync(String screenName, long cursor
            , TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener
                , new Object[]{screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((String) args[0]
                        , (Long)args[1]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids
     *
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
     * @since Twitter4J 2.0.0
     */
    public void getFollowersIDsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs());
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids
     *
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
     * @since Twitter4J 2.0.10
     */
    public void getFollowersIDsAsync(long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener
                , new Object[]{cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((Long)(args[0])));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids
     *
     * @param userId   Specfies the ID of the user for whom to return the followers list.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
     * @since Twitter4J 2.0.0
     */
    public void getFollowersIDsAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((Integer) args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids
     *
     * @param userId   Specfies the ID of the user for whom to return the followers list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
     * @since Twitter4J 2.0.10
     */
    public void getFollowersIDsAsync(int userId, long cursor
            , TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener
                , new Object[]{userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((Integer) args[0],(Long)args[1]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids
     *
     * @param screenName Specfies the screen name of the user for whom to return the followers list.
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
     * @since Twitter4J 2.0.0
     */
    public void getFollowersIDsAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((String) args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids
     *
     * @param screenName Specfies the screen name of the user for whom to return the followers list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
     * @since Twitter4J 2.0.10
     */
    public void getFollowersIDsAsync(String screenName, long cursor
            , TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener
                , new Object[]{screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((String) args[0]
                        , (Long) args[1]));
            }
        });
    }

    /**
     * Sets values that users are able to set under the "Account" tab of their settings page. Only the parameters specified(non-null) will be updated.
     *
     * @param name        Optional. Maximum of 20 characters.
     * @param email       Optional. Maximum of 40 characters. Must be a valid email address.
     * @param url         Optional. Maximum of 100 characters. Will be prepended with "http://" if not present.
     * @param location    Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @param listener    a listener object that receives the response
     * @since Twitter4J 2.0.2
     */
    public void updateProfileAsync(String name, String email, String url
            , String location, String description, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE, listener,
                new String[]{name, email, url, location, description}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedProfile(updateProfile((String) args[0]
                        , (String) args[1], (String) args[2], (String) args[3]
                        , (String) args[4]));
            }
        });
    }

    /**
     * Gets the remaining number of API requests available to the requesting user before the API limit is reached for the current hour. Calls to rate_limit_status do not count against the rate limit.  If authentication credentials are provided, the rate limit status for the authenticating user is returned.  Otherwise, the rate limit status for the requester's IP address is returned.
     * <br>This method calls http://api.twitter.com/1/account/rate_limit_status
     *
     * @since Twitter4J 1.1.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account%C2%A0rate_limit_status</a>
     */
    public void rateLimitStatusAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RATE_LIMIT_STATUS, listener, new Object[]{}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotRateLimitStatus(rateLimitStatus());
            }
        });
    }

    /**
     * Sets which device Twitter delivers updates to for the authenticating user.  Sending none as the device parameter will disable IM or SMS updates.
     * <br>This method calls http://api.twitter.com/1/account/update_delivery_device
     *
     * @param device   new Delivery device. Must be one of: IM, SMS, NONE.
     * @param listener a listener object that receives the response
     * @since Twitter4J 1.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-account%C2%A0update_delivery_device">Twitter API Wiki / Twitter REST API Method: account%C2%A0update_delivery_device</a>
     */
    public void updateDeliverlyDeviceAsync(Device device, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_DELIVERY_DEVICE, listener, new Object[]{device}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedDeliveryDevice(updateDeliveryDevice((Device) args[0]));
            }
        });
    }

    /**
     * Sets one or more hex values that control the color scheme of the authenticating user's profile page on twitter.com.  These values are also returned in the getUserDetail() method.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_colors
     * @param profileBackgroundColor optional, can be null
     * @param profileTextColor optional, can be null
     * @param profileLinkColor optional, can be null
     * @param profileSidebarFillColor optional, can be null
     * @param profileSidebarBorderColor optional, can be null
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-account%C2%A0update_profile_colors">Twitter API Wiki / Twitter REST API Method: account%C2%A0update_profile_colors</a>
     */
    public void updateProfileColorsAsync(
            String profileBackgroundColor, String profileTextColor,
            String profileLinkColor, String profileSidebarFillColor,
            String profileSidebarBorderColor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_COLORS,
                listener, new Object[]{profileBackgroundColor, profileTextColor,
                        profileLinkColor, profileSidebarFillColor,
                        profileSidebarBorderColor}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.updatedProfileColors(updateProfileColors(
                        (String) args[0], (String) args[1], (String) args[2],
                        (String) args[3], (String) args[4]));
            }
        });
    }

    /**
     * Updates the authenticating user's profile image.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_image.json
     * @param image Must be a valid GIF, JPG, or PNG image of less than 700 kilobytes in size.  Images with width larger than 500 pixels will be scaled down.
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_profile_image">Twitter API Wiki / Twitter REST API Method: account update_profile_image</a>
     */
    public void updateProfileImageAsync(File image, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_IMAGE,
                listener, new Object[]{image}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.updatedProfileImage(updateProfileImage(
                        (File) args[0]));
            }
        });
    }

    /**
     * Updates the authenticating user's profile background image.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_background_image.json
     * @param image Must be a valid GIF, JPG, or PNG image of less than 800 kilobytes in size.  Images with width larger than 2048 pixels will be forceably scaled down.
     * @param tile If set to true the background image will be displayed tiled. The image will not be tiled otherwise.
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_profile_background_image">Twitter API Wiki / Twitter REST API Method: account update_profile_background_image</a>
     */
    public void updateProfileBackgroundImageAsync(File image, boolean tile
            , TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_BACKGROUND_IMAGE,
                listener, new Object[]{image, tile}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.updatedProfileBackgroundImage(
                        updateProfileBackgroundImage((File) args[0], (Boolean) args[1])
                );
            }
        });
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls http://api.twitter.com/1/favorites
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
     * @since Twitter4J 2.0.1
     */
    public void getFavoritesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(getFavorites());
            }
        });
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls http://api.twitter.com/1/favorites
     * @param page number of page to retrieve favorites
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
     * @since Twitter4J 2.0.1
     */
    public void getFavoritesAsync(int page, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(getFavorites((Integer)args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls http://api.twitter.com/1/favorites
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
     * @since Twitter4J 2.0.1
     */
    public void getFavoritesAsync(String id,TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(getFavorites((String)args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls http://api.twitter.com/1/favorites
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
     * @param page retrieves the 20 next most recent favorite statuses.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
     * @since Twitter4J 2.0.1
     */
    public void getFavoritesAsync(String id,int page, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {id,page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(getFavorites((String)args[0],(Integer)args[1]));
            }
        });
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * <br>This method calls http://api.twitter.com/1/favorites/create%C2%A0
     *
     * @param id       the ID or screen name of the user for whom to request a list of favorite statuses.
     * @param listener a listener object that receives the response
     * @since 1.1.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites%C2%A0create">Twitter API Wiki / Twitter REST API Method: favorites%C2%A0create</a>
     */
    public void createFavoriteAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FAVORITE, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFavorite(createFavorite((Long) args[0]));
            }
        });
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * <br>This method calls http://api.twitter.com/1/favorites/destroy
     *
     * @param id       the ID or screen name of the user for whom to request a list of un-favorite statuses.
     * @param listener a listener object that receives the response
     * @since 1.1.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: favorites%C2%A0destroy</a>
     */
    public void destroyFavoriteAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FAVORITE, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFavorite(destroyFavorite((Long) args[0]));
            }
        });
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/follow
     *
     * @param screenName Specifies the screen name of the user to follow with device updates.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications%C2%A0follow</a>
     */
    public void enableNotificationAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.enabledNotification((enableNotification((String) args[0])));
            }
        });
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/follow
     *
     * @param userId Specifies the ID of the user to follow with device updates.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications%C2%A0follow</a>
     */
    public void enableNotificationAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.enabledNotification((enableNotification((Integer) args[0])));
            }
        });
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/leave
     *
     * @param screenName Specifies the screen name of the user to disable device notifications.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications%C2%A0leave</a>
     */
    public void disableNotificationAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DISABLE_NOTIFICATION, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.disabledNotification(disableNotification((String) args[0]));
            }
        });
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/leave
     *
     * @param userId Specifies the ID of the user to disable device notifications.
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications%C2%A0leave</a>
     */
    public void disableNotificationAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DISABLE_NOTIFICATION, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.disabledNotification(disableNotification((Integer) args[0]));
            }
        });
    }

    /* Block Methods */

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
     *
     * @param screenName the screen_name of the user to block
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-blocks%C2%A0create">Twitter API Wiki / Twitter REST API Method: blocks%C2%A0create</a>
     */
    public void createBlockAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdBlock(createBlock((String) args[0]));
            }
        });
    }

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
     *
     * @param userId the screen_name of the user to block
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-blocks%C2%A0create">Twitter API Wiki / Twitter REST API Method: blocks%C2%A0create</a>
     */
    public void createBlockAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdBlock(createBlock((Integer) args[0]));
            }
        });
    }

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
     *
     * @param screenName the screen_name of the user to block
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-blocks%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: blocks%C2%A0destroy</a>
     */
    public void destroyBlockAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedBlock(destroyBlock((String) args[0]));
            }
        });
    }

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
     *
     * @param userId the ID of the user to block
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-blocks%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: blocks%C2%A0destroy</a>
     */
    public void destroyBlockAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedBlock(destroyBlock((Integer) args[0]));
            }
        });
    }

    /**
     * Tests if a friendship exists between two users.
     * <br>This method calls http://api.twitter.com/1/blocks/exists/id.xml
     *
     * @param screenName The screen_name of the potentially blocked user.
     * @since Twitter4J 2.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-exists">Twitter API Wiki / Twitter REST API Method: blocks exists</a>
     */
    public void existsBlockAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsBlock(existsBlock((String) args[0]));
            }
        });
    }

    /**
     * Tests if a friendship exists between two users.
     * <br>This method calls http://api.twitter.com/1/blocks/exists/id.xml
     *
     * @param userId The ID of the potentially blocked user.
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-exists">Twitter API Wiki / Twitter REST API Method: blocks exists</a>
     */
    public void existsBlockAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsBlock(existsBlock((Integer) args[0]));
            }
        });
    }

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking.xml
     *
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking">Twitter API Wiki / Twitter REST API Method: blocks blocking</a>
     */
    public void getBlockingUsersAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotBlockingUsers(getBlockingUsers());
            }
        });
    }

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking.xml
     *
     * @param page the number of page
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking">Twitter API Wiki / Twitter REST API Method: blocks blocking</a>
     */
    public void getBlockingUsersAsync(int page, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS, listener, new Integer[]{page}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotBlockingUsers(getBlockingUsers((Integer)args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric user ids the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking/ids
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking-ids">Twitter API Wiki / Twitter REST API Method: blocks blocking ids</a>
     */
    public void getBlockingUsersIDsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotBlockingUsersIDs(getBlockingUsersIDs());
            }
        });
    }

    /* Help Methods */

    /**
     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
     * <br>This method calls http://api.twitter.com/1/help/test
     *
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-help%C2%A0test">Twitter API Wiki / Twitter REST API Method: help%C2%A0test</a>
     */
    public void testAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(TEST, listener, new Object[]{}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.tested(test());
            }
        });
    }

    private static transient Dispatcher dispatcher;
    private boolean shutdown = false;

    /**
     * Shuts down internal dispather thread.
     *
     * @since Twitter4J 2.0.2
     */
    public void shutdown(){
        synchronized (AsyncTwitter.class) {
            if (shutdown = true) {
                throw new IllegalStateException("Already shut down");
            }
            getDispatcher().shutdown();
            dispatcher = null;
            shutdown = true;
        }
    }
    private Dispatcher getDispatcher(){
        if(true == shutdown){
            throw new IllegalStateException("Already shut down");
        }
        if (null == dispatcher) {
            dispatcher = new Dispatcher("Twitter4J Async Dispatcher", Configuration.getNumberOfAsyncThreads());
        }
        return dispatcher;
    }

    /**
     * Returns the same text displayed on http://twitter.com/home when a maintenance window is scheduled, in the requested format.
     *
     * @since Twitter4J 1.0.4
     */
    public void getDowntimeScheduleAsync() {
        throw new RuntimeException(
                "this method is not supported by the Twitter API anymore"
                , new NoSuchMethodException("this method is not supported by the Twitter API anymore"));
    }

    /**
     * Retrieves extended information of the authenticated user.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.<br>
     * The call Twitter.getAuthenticatedUser() is equivalent to the call:<br>
     * twitter.getUserDetailAsync(twitter.getUserId(), listener);
     *
     * @since Twitter4J 1.1.3
     */
    public void getAuthenticatedUserAsync(TwitterListener listener) {
        if (null == getUserId()) {
            throw new IllegalStateException("User Id not specified.");
        }
        showUserAsync(getUserId(), listener);
    }

	abstract class AsyncTask implements Runnable {
        TwitterListener listener;
        Object[] args;
        TwitterMethod method;
        AsyncTask(TwitterMethod method, TwitterListener listener, Object[] args) {
            this.method = method;
            this.listener = listener;
            this.args = args;
        }

        abstract void invoke(TwitterListener listener,Object[] args) throws TwitterException;

        public void run() {
            try {
                   invoke(listener,args);
            } catch (TwitterException te) {
                if (null != listener) {
                    listener.onException(te,method);
                }
            }
        }
    }
}
