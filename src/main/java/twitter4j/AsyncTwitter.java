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

import twitter4j.api.AccountMethodsAsync;
import twitter4j.api.BlockMethodsAsync;
import twitter4j.api.DirectMessageMethodsAsync;
import twitter4j.api.FavoriteMethodsAsync;
import twitter4j.api.FriendshipMethodsAsync;
import twitter4j.api.HelpMethodsAsync;
import twitter4j.api.ListMembersMethodsAsync;
import twitter4j.api.ListMethodsAsync;
import twitter4j.api.ListSubscribersMethodsAsync;
import twitter4j.api.LocalTrendsMethodsAsync;
import twitter4j.api.NotificationMethodsAsync;
import twitter4j.api.SavedSearchesMethodsAsync;
import twitter4j.api.SearchMethodsAsync;
import twitter4j.api.SocialGraphMethodsAsync;
import twitter4j.api.SpamReportingMethodsAsync;
import twitter4j.api.StatusMethodsAsync;
import twitter4j.api.TimelineMethodsAsync;
import twitter4j.api.UserMethodsAsync;

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
public class AsyncTwitter extends Twitter
        implements
        SearchMethodsAsync,
        TimelineMethodsAsync,
        StatusMethodsAsync,
        UserMethodsAsync,
        ListMethodsAsync,
        ListMembersMethodsAsync,
        ListSubscribersMethodsAsync,
        DirectMessageMethodsAsync,
        FriendshipMethodsAsync,
        SocialGraphMethodsAsync,
        AccountMethodsAsync,
        FavoriteMethodsAsync,
        NotificationMethodsAsync,
        BlockMethodsAsync,
        SpamReportingMethodsAsync,
        SavedSearchesMethodsAsync,
        LocalTrendsMethodsAsync,
        HelpMethodsAsync {
    private static final long serialVersionUID = -2008667933225051907L;

    public AsyncTwitter(String id, String password) {
        super(id, password);
    }

    /**
     * {@inheritDoc}
     */
    public void searchAsync(Query query, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH, listener, new Object[]{query}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.searched(search((Query) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void getPublicTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getPublicTimelineAsync(long sinceID, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, new Long[] {sinceID}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline( (Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getHomeTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotHomeTimeline(getHomeTimeline());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getHomeTimelineAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotHomeTimeline(getHomeTimeline((Paging) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsTimelineAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void getUserTimelineAsync(String screenName, TwitterListener listener) {
        getUserTimelineAsync(screenName, new Paging(), listener);
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimelineAsync(int userId, TwitterListener listener) {
        getUserTimelineAsync(userId, new Paging(), listener);
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getMentionsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotMentions(getMentions());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getMentionsAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotMentions(getMentions((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByMeAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedByMe(getRetweetedByMe());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByMeAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedByMe(getRetweetedByMe((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedToMeAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedToMe(getRetweetedToMe());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedToMeAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedToMe(getRetweetedToMe((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetsOfMeAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetsOfMe(getRetweetsOfMe());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetsOfMeAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetsOfMe(getRetweetsOfMe((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showStatusAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_STATUS, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotShowStatus(showStatus( (Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatusAsync(String status, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new String[] {status}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updatedStatus(updateStatus( (String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatusAsync(String status, GeoLocation location, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new Object[] {status, location}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updatedStatus(updateStatus( (String) args[0], (GeoLocation)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatusAsync(String status, long inReplyToStatusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new Object[]{status, inReplyToStatusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedStatus(updateStatus((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatusAsync(String status, long inReplyToStatusId, GeoLocation location, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new Object[]{status, inReplyToStatusId, location}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedStatus(updateStatus((String) args[0], (Long) args[1], (GeoLocation)args[2]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyStatusAsync(long statusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_STATUS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedStatus(destroyStatus(((Long) args[0])));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void retweetStatusAsync(long statusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEET_STATUS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.retweetedStatus(retweetStatus(((Long) args[0])));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetsAsync(long statusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotRetweets(getRetweets((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showUserAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listener, new Object[] {screenName}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserDetail(showUser( (String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showUserAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listener, new Object[] {userId}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserDetail(showUser( (Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatusesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(getFriendsStatuses());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatusesAsync(long cursor,TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[]{cursor}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(getFriendsStatuses((Long)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatusesAsync(String screenName, TwitterListener listener) {
        getFriendsStatusesAsync(screenName, -1l, listener);
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatusesAsync(int userId, TwitterListener listener) {
        getFriendsStatusesAsync(userId, -1l, listener);
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatusesAsync(String screenName, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[] {screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(getFriendsStatuses((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatusesAsync(int userId, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[] {userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(getFriendsStatuses((Integer) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatusesAsync(TwitterListener listener) {
        getFollowersStatusesAsync(-1l, listener);
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatusesAsync(long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(getFollowersStatuses((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatusesAsync(String screenName, TwitterListener listener) {
        getFollowersStatusesAsync(screenName, -1l, listener);
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatusesAsync(int userId, TwitterListener listener) {
        getFollowersStatusesAsync(userId, -1l, listener);
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatusesAsync(String screenName, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(getFollowersStatuses((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatusesAsync(int userId, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(getFollowersStatuses((Integer) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDirectMessagesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDirectMessagesAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages( (Paging) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getSentDirectMessagesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SENT_DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getSentDirectMessagesAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SENT_DIRECT_MESSAGES, listener, new Object[] {paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages( (Paging) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void sendDirectMessageAsync(String screenName, String text, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new String[] {screenName, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(sendDirectMessage( (String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void sendDirectMessageAsync(int userId, String text, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new Object[] {userId, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(sendDirectMessage( (Integer) args[0], (String) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyDirectMessageAsync(int id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_DIRECT_MESSAGES, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyedDirectMessage(destroyDirectMessage((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendshipAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendshipAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendshipAsync(String screenName, boolean follow, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Object[]{screenName,follow}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((String) args[0],(Boolean)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendshipAsync(int userId, boolean follow, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Object[]{userId,follow}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((Integer) args[0],(Boolean)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFriendshipAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFriendship(destroyFriendship((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFriendshipAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFriendship(destroyFriendship((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsFriendshipAsync(String userA, String userB, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_FRIENDSHIP, listener, new String[]{userA, userB}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsFriendship(existsFriendship((String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showFriendhipAsync(String sourceScreenName, String targetScreenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listener, new String[]{sourceScreenName, targetScreenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotShowFriendship(showFriendship((String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showFriendshipAsync(int sourceId, int targetId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listener, new Integer[]{sourceId, targetId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotShowFriendship(showFriendship((Integer) args[0], (Integer) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs());
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void getFriendsIDsAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDsAsync(int userId, long cursor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new Object[]{userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((Integer) args[0],(Long)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDsAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void getFollowersIDsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs());
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void getFollowersIDsAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void getFollowersIDsAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void getRateLimitStatusAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RATE_LIMIT_STATUS, listener, new Object[]{}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotRateLimitStatus(getRateLimitStatus());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateDeliveryDeviceAsync(Device device, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_DELIVERY_DEVICE, listener, new Object[]{device}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedDeliveryDevice(updateDeliveryDevice((Device) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void getFavoritesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(getFavorites());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavoritesAsync(int page, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(getFavorites((Integer)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavoritesAsync(String id,TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(getFavorites((String)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavoritesAsync(String id,int page, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {id,page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(getFavorites((String)args[0],(Integer)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFavoriteAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FAVORITE, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFavorite(createFavorite((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFavoriteAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FAVORITE, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFavorite(destroyFavorite((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void enableNotificationAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.enabledNotification((enableNotification((String) args[0])));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void enableNotificationAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.enabledNotification((enableNotification((Integer) args[0])));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void disableNotificationAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DISABLE_NOTIFICATION, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.disabledNotification(disableNotification((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void createBlockAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdBlock(createBlock((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createBlockAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdBlock(createBlock((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyBlockAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedBlock(destroyBlock((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyBlockAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedBlock(destroyBlock((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsBlockAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsBlock(existsBlock((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsBlockAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsBlock(existsBlock((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getBlockingUsersAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotBlockingUsers(getBlockingUsers());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getBlockingUsersAsync(int page, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS, listener, new Integer[]{page}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotBlockingUsers(getBlockingUsers((Integer)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
