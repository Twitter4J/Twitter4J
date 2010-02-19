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
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.RequestToken;

import java.io.File;
import java.util.Date;
import static twitter4j.TwitterMethod.*;

/**
 * Twitter API with a series of asynchronous APIs.<br>
 * With this class, you can call TwitterAPI asynchronously.<br>
 * Note that currently this class is NOT compatible with Google App Engine as it is maintaining threads internally.
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterListener
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AsyncTwitter extends TwitterOAuthSupportBase implements java.io.Serializable,
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
    private Twitter twitter;
    private TwitterListener listener;

    /**
     * Creates a basic authenticated AsyncTwitter instance.
     * @param screenName screen name
     * @param password password
     * @param listener
     * @deprecated use new AsyncTwitterFactory.getBasicAuthorizedInstance() instead.
     */
    public AsyncTwitter(String screenName, String password, TwitterListener listener) {
        super(ConfigurationContext.getInstance(), screenName, password);
        twitter = new TwitterFactory().getInstance(auth);
        this.listener = listener;
    }

    /*package*/
    AsyncTwitter(Configuration conf, Authorization auth, TwitterListener listener) {
        super(conf, auth);
        twitter = new TwitterFactory(conf).getInstance(auth);
        this.listener = listener;
    }

    /**
     * Returns authenticating user's screen name.<br>
     * This method may internally call verifyCredentials() on the first invocation if<br>
     * - this instance is authenticated by Basic and email address is supplied instead of screen name, or
     * - this instance is authenticated by OAuth.<br>
     * Note that this method returns a transiently cached (will be lost upon serialization) screen name while it is possible to change a user's screen name.<br>
     *
     * @return the authenticating screen name
     * @throws TwitterException      when verifyCredentials threw an exception.
     * @throws IllegalStateException if no credentials are supplied. i.e.) this is an anonymous Twitter instance
     * @since Twitter4J 2.1.1
     */
    public String getScreenName() throws TwitterException, IllegalStateException {
        return twitter.getScreenName();
    }

    /**
     * Returns authenticating user's user id.<br>
     * This method may internally call verifyCredentials() on the first invocation if<br>
     * - this instance is authenticated by Basic and email address is supplied instead of screen name, or
     * - this instance is authenticated by OAuth.<br>
     *
     * @return the authenticating user's id
     * @throws TwitterException when verifyCredentials threw an exception.
     * @throws IllegalStateException if no credentials are supplied. i.e.) this is an anonymous Twitter instance
     * @since Twitter4J 2.1.1
     */
    public int getId() throws TwitterException, IllegalStateException {
        return twitter.getId();
    }

    /**
     * {@inheritDoc}
     */
    public void search(Query query) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH, listener, new Object[]{query}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.searched(twitter.search((Query) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getTrends() {
        getDispatcher().invokeLater(new AsyncTask(TRENDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotTrends(twitter.getTrends());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getCurrentTrends() {
        getDispatcher().invokeLater(new AsyncTask(CURRENT_TRENDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotCurrentTrends(twitter.getCurrentTrends());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getCurrentTrends(boolean excludeHashTags) {
        getDispatcher().invokeLater(new AsyncTask(CURRENT_TRENDS, listener
                , new Object[]{excludeHashTags}) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotCurrentTrends(twitter.getCurrentTrends((Boolean)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDailyTrends() {
        getDispatcher().invokeLater(new AsyncTask(DAILY_TRENDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotDailyTrends(twitter.getDailyTrends());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDailyTrends(Date date, boolean excludeHashTags) {
        getDispatcher().invokeLater(new AsyncTask(DAILY_TRENDS, listener
                , new Object[]{date, excludeHashTags}) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotDailyTrends(twitter.getDailyTrends((Date) args[0]
                        , (Boolean) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getWeeklyTrends() {
        getDispatcher().invokeLater(new AsyncTask(WEEKLY_TRENDS, listener
                , null) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotWeeklyTrends(twitter.getWeeklyTrends());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getWeeklyTrends(Date date, boolean excludeHashTags) {
        getDispatcher().invokeLater(new AsyncTask(WEEKLY_TRENDS, listener
                , new Object[]{date, excludeHashTags}) {
            public void invoke(TwitterListener listener, Object[] args) throws
                    TwitterException {
                listener.gotWeeklyTrends(twitter.getWeeklyTrends((Date) args[0]
                        , (Boolean) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getPublicTimeline() {
        getDispatcher().invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(twitter.getPublicTimeline());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getHomeTimeline() {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotHomeTimeline(twitter.getHomeTimeline());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getHomeTimeline(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotHomeTimeline(twitter.getHomeTimeline((Paging) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsTimeline() {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(twitter.getFriendsTimeline());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsTimeline(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(twitter.getFriendsTimeline((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(String screenName, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener,
                new Object[]{screenName, paging}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotUserTimeline(twitter.getUserTimeline((String) args[0],
                        (Paging) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(int userId, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener,
                new Object[]{userId, paging}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotUserTimeline(twitter.getUserTimeline((Integer) args[0],
                        (Paging) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener,
                new Object[]{paging}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotUserTimeline(twitter.getUserTimeline((Paging) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(String screenName) {
        getUserTimeline(screenName, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(int userId) {
        getUserTimeline(userId, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline() {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(twitter.getUserTimeline());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getMentions() {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotMentions(twitter.getMentions());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getMentions(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotMentions(twitter.getMentions((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByMe() {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedByMe(twitter.getRetweetedByMe());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByMe(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedByMe(twitter.getRetweetedByMe((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedToMe() {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedToMe(twitter.getRetweetedToMe());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedToMe(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetedToMe(twitter.getRetweetedToMe((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetsOfMe() {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetsOfMe(twitter.getRetweetsOfMe());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetsOfMe(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotRetweetsOfMe(twitter.getRetweetsOfMe((Paging)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showStatus(long id) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_STATUS, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotShowStatus(twitter.showStatus( (Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatus(String status) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new String[] {status}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updatedStatus(twitter.updateStatus( (String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatus(String status, GeoLocation location) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new Object[] {status, location}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updatedStatus(twitter.updateStatus( (String) args[0], (GeoLocation)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatus(String status, long inReplyToStatusId) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new Object[]{status, inReplyToStatusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedStatus(twitter.updateStatus((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatus(String status, long inReplyToStatusId, GeoLocation location) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listener, new Object[]{status, inReplyToStatusId, location}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedStatus(twitter.updateStatus((String) args[0], (Long) args[1], (GeoLocation)args[2]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyStatus(long statusId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_STATUS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedStatus(twitter.destroyStatus(((Long) args[0])));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void retweetStatus(long statusId) {
        getDispatcher().invokeLater(new AsyncTask(RETWEET_STATUS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.retweetedStatus(twitter.retweetStatus(((Long) args[0])));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweets(long statusId) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotRetweets(twitter.getRetweets((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showUser(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listener, new Object[] {screenName}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserDetail(twitter.showUser( (String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showUser(int userId) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listener, new Object[] {userId}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserDetail(twitter.showUser( (Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void searchUsers(String query, int page) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listener, new Object[] {query, page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.searchedUser(twitter.searchUsers( (String) args[0], (Integer)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses() {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(twitter.getFriendsStatuses());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[]{cursor}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(twitter.getFriendsStatuses((Long)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses(String screenName) {
        getFriendsStatuses(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses(int userId) {
        getFriendsStatuses(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses(String screenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[] {screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(twitter.getFriendsStatuses((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses(int userId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listener, new Object[] {userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsStatuses(twitter.getFriendsStatuses((Integer) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses() {
        getFollowersStatuses(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(twitter.getFollowersStatuses((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses(String screenName) {
        getFollowersStatuses(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses(int userId) {
        getFollowersStatuses(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses(String screenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(twitter.getFollowersStatuses((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses(int userId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listener, new Object[]{userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersStatuses(twitter.getFollowersStatuses((Integer) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDirectMessages() {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(twitter.getDirectMessages());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDirectMessages(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(twitter.getDirectMessages( (Paging) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getSentDirectMessages() {
        getDispatcher().invokeLater(new AsyncTask(SENT_DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(twitter.getSentDirectMessages());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getSentDirectMessages(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(SENT_DIRECT_MESSAGES, listener, new Object[] {paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(twitter.getSentDirectMessages( (Paging) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void sendDirectMessage(String screenName, String text) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new String[] {screenName, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(twitter.sendDirectMessage( (String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void sendDirectMessage(int userId, String text) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new Object[] {userId, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(twitter.sendDirectMessage( (Integer) args[0], (String) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyDirectMessage(int id) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_DIRECT_MESSAGES, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyedDirectMessage(twitter.destroyDirectMessage((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendship(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(twitter.createFriendship((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendship(int userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(twitter.createFriendship((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendship(String screenName, boolean follow) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Object[]{screenName,follow}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(twitter.createFriendship((String) args[0],(Boolean)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendship(int userId, boolean follow) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new Object[]{userId,follow}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(twitter.createFriendship((Integer) args[0],(Boolean)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFriendship(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFriendship(twitter.destroyFriendship((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFriendship(int userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFriendship(twitter.destroyFriendship((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsFriendship(String userA, String userB) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_FRIENDSHIP, listener, new String[]{userA, userB}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsFriendship(twitter.existsFriendship((String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showFriendship(String sourceScreenName, String targetScreenName) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listener, new String[]{sourceScreenName, targetScreenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotShowFriendship(twitter.showFriendship((String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showFriendship(int sourceId, int targetId) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listener, new Integer[]{sourceId, targetId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotShowFriendship(twitter.showFriendship((Integer) args[0], (Integer) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs() {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(twitter.getFriendsIDs());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener
                , new Object[]{cursor}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotFriendsIDs(twitter.getFriendsIDs((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs(int userId) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(twitter.getFriendsIDs((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs(int userId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new Object[]{userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(twitter.getFriendsIDs((Integer) args[0],(Long)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(twitter.getFriendsIDs((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs(String screenName, long cursor
            ) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener
                , new Object[]{screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotFriendsIDs(twitter.getFriendsIDs((String) args[0]
                        , (Long)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs() {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(twitter.getFollowersIDs());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener
                , new Object[]{cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(twitter.getFollowersIDs((Long)(args[0])));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs(int userId) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(twitter.getFollowersIDs((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs(int userId, long cursor
            ) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener
                , new Object[]{userId, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(twitter.getFollowersIDs((Integer) args[0],(Long)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(twitter.getFollowersIDs((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs(String screenName, long cursor
            ) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener
                , new Object[]{screenName, cursor}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(twitter.getFollowersIDs((String) args[0]
                        , (Long) args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfile(String name, String email, String url
            , String location, String description) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE, listener,
                new String[]{name, email, url, location, description}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedProfile(twitter.updateProfile((String) args[0]
                        , (String) args[1], (String) args[2], (String) args[3]
                        , (String) args[4]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRateLimitStatus() {
        getDispatcher().invokeLater(new AsyncTask(RATE_LIMIT_STATUS, listener, new Object[]{}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotRateLimitStatus(twitter.getRateLimitStatus());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateDeliveryDevice(Device device) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_DELIVERY_DEVICE, listener, new Object[]{device}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedDeliveryDevice(twitter.updateDeliveryDevice((Device) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfileColors(
            String profileBackgroundColor, String profileTextColor,
            String profileLinkColor, String profileSidebarFillColor,
            String profileSidebarBorderColor) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_COLORS,
                listener, new Object[]{profileBackgroundColor, profileTextColor,
                        profileLinkColor, profileSidebarFillColor,
                        profileSidebarBorderColor}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.updatedProfileColors(twitter.updateProfileColors(
                        (String) args[0], (String) args[1], (String) args[2],
                        (String) args[3], (String) args[4]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfileImage(File image) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_IMAGE,
                listener, new Object[]{image}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.updatedProfileImage(twitter.updateProfileImage(
                        (File) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfileBackgroundImage(File image, boolean tile
            ) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_BACKGROUND_IMAGE,
                listener, new Object[]{image, tile}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.updatedProfileBackgroundImage(twitter.
                        updateProfileBackgroundImage((File) args[0], (Boolean) args[1])
                );
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites() {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(twitter.getFavorites());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites(int page) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(twitter.getFavorites((Integer)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites(String id) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(twitter.getFavorites((String)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites(String id,int page) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {id,page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(twitter.getFavorites((String)args[0],(Integer)args[1]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFavorite(long id) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FAVORITE, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFavorite(twitter.createFavorite((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFavorite(long id) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FAVORITE, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFavorite(twitter.destroyFavorite((Long) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void enableNotification(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.enabledNotification(twitter.enableNotification((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void enableNotification(int userId) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.enabledNotification(twitter.enableNotification((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void disableNotification(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DISABLE_NOTIFICATION, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.disabledNotification(twitter.disableNotification((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void disableNotification(int userId) {
        getDispatcher().invokeLater(new AsyncTask(DISABLE_NOTIFICATION, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.disabledNotification(twitter.disableNotification((Integer) args[0]));
            }
        });
    }

    /* Block Methods */

    /**
     * {@inheritDoc}
     */
    public void createBlock(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdBlock(twitter.createBlock((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createBlockAsync(int userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdBlock(twitter.createBlock((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyBlock(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedBlock(twitter.destroyBlock((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyBlock(int userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedBlock(twitter.destroyBlock((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsBlock(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_BLOCK, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsBlock(twitter.existsBlock((String) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsBlock(int userId) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_BLOCK, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsBlock(twitter.existsBlock((Integer) args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getBlockingUsers() {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotBlockingUsers(twitter.getBlockingUsers());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getBlockingUsers(int page) {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS, listener, new Integer[]{page}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotBlockingUsers(twitter.getBlockingUsers((Integer)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getBlockingUsersIDs() {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotBlockingUsersIDs(twitter.getBlockingUsersIDs());
            }
        });
    }

    /* Spam Reporting Methods */

    /**
     * {@inheritDoc}
     */
    public void reportSpam(int userId) throws TwitterException{
        getDispatcher().invokeLater(new AsyncTask(REPORT_SPAM, listener, userId) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.reportedSpam(twitter.reportSpam((Integer)args[0]));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void reportSpam(String screenName) throws TwitterException{
        getDispatcher().invokeLater(new AsyncTask(REPORT_SPAM, listener, screenName) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.reportedSpam(twitter.reportSpam((String)args[0]));
            }
        });
    }

    /* Help Methods */

    /**
     * {@inheritDoc}
     */
    public void test() {
        getDispatcher().invokeLater(new AsyncTask(TEST, listener, new Object[]{}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.tested(twitter.test());
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
            if (shutdown) {
                throw new IllegalStateException("Already shut down");
            }
            getDispatcher().shutdown();
            dispatcher = null;
            shutdown = true;
        }
    }
    private Dispatcher getDispatcher(){
        if(shutdown){
            throw new IllegalStateException("Already shut down");
        }
        if (null == dispatcher) {
            dispatcher = new Dispatcher(conf, "Twitter4J Async Dispatcher", ConfigurationContext.getInstance().getAsyncNumThreads());
        }
        return dispatcher;
    }

    @Override
    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    @Override
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return twitter.getOAuthRequestToken();
    }

    @Override
    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return twitter.getOAuthRequestToken(callbackUrl);
    }

    @Override
    public AccessToken getOAuthAccessToken() throws TwitterException {
        return twitter.getOAuthAccessToken();
    }

    @Override
    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        return twitter.getOAuthAccessToken(oauthVerifier);
    }

    @Override
    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        return twitter.getOAuthAccessToken(requestToken);
    }

    @Override
    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return twitter.getOAuthAccessToken(requestToken, oauthVerifier);
    }

    @Override
    public void setOAuthAccessToken(AccessToken accessToken) {
        twitter.setOAuthAccessToken(accessToken);
    }

    @Override
    public AccessToken getOAuthAccessToken(String token, String tokenSecret) throws TwitterException {
        return twitter.getOAuthAccessToken(token, tokenSecret);
    }

    @Override
    public AccessToken getOAuthAccessToken(String token, String tokenSecret, String pin) throws TwitterException {
        return twitter.getOAuthAccessToken(token, tokenSecret, pin);
    }

    @Override
    public void setOAuthAccessToken(String token, String tokenSecret) {
        twitter.setOAuthAccessToken(token, tokenSecret);
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
        AsyncTask(TwitterMethod method, TwitterListener listener, Object arg) {
            this.method = method;
            this.listener = listener;
            this.args = new Object[]{arg};
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
