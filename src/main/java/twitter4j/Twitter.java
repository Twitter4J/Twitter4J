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

import twitter4j.api.AccountMethods;
import twitter4j.api.BlockMethods;
import twitter4j.api.DirectMessageMethods;
import twitter4j.api.FavoriteMethods;
import twitter4j.api.FriendshipMethods;
import twitter4j.api.HelpMethods;
import twitter4j.api.ListMembersMethods;
import twitter4j.api.ListMethods;
import twitter4j.api.ListSubscribersMethods;
import twitter4j.api.LocalTrendsMethods;
import twitter4j.api.NotificationMethods;
import twitter4j.api.SavedSearchesMethods;
import twitter4j.api.SearchMethods;
import twitter4j.api.SocialGraphMethods;
import twitter4j.api.SpamReportingMethods;
import twitter4j.api.StatusMethods;
import twitter4j.api.TimelineMethods;
import twitter4j.api.UserMethods;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.*;
import static twitter4j.http.HttpParameter.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A java representation of the <a href="http://apiwiki.twitter.com/">Twitter API</a>
 * <br>This class is thread safe and can be cached/re-used and used concurrently.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class Twitter extends TwitterOAuthSupportBase
        implements java.io.Serializable,
        SearchMethods,
        TimelineMethods,
        StatusMethods,
        UserMethods,
        ListMethods,
        ListMembersMethods,
        ListSubscribersMethods,
        DirectMessageMethods,
        FriendshipMethods,
        SocialGraphMethods,
        AccountMethods,
        FavoriteMethods,
        NotificationMethods,
        BlockMethods,
        SpamReportingMethods,
        SavedSearchesMethods,
        LocalTrendsMethods,
        HelpMethods {
    private static final long serialVersionUID = -1486360080128882436L;
    Twitter(Configuration conf){
        super(conf);
    }


    /**
     * Creates an unauthenticated Twitter instance
     * @deprecated use TwitterFactory.getInstance() instead
     */
    public Twitter() {
        super(ConfigurationContext.getInstance());
    }

    /**
     * Creates a Twitter instance with supplied id
     *
     * @param screenName the screen name of the user
     * @param password   the password of the user
     * @deprecated use TwitterFactory.getBasicAuthenticatedInstance(screenName, password) instead
     */
    public Twitter(String screenName, String password) {
        super(ConfigurationContext.getInstance(), screenName, password);
    }
    /*package*/
    Twitter(Configuration conf, String screenName, String password) {
        super(conf, screenName, password);
    }
    /*package*/
    Twitter(Configuration conf, Authorization auth) {
        super(conf, auth);
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter[] params2){
        if(null != params1 && null != params2){
            HttpParameter[] params = new HttpParameter[params1.length + params2.length];
            System.arraycopy(params1,0,params,0,params1.length);
            System.arraycopy(params2,0,params,params1.length,params2.length);
            return params;
        }
        if(null == params1 && null == params2){
            return new HttpParameter[0];
        }
        if (null != params1) {
            return params1;
        } else {
            return params2;
        }
    }

    private transient String screenName = null;
    private transient int id = 0;

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
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (null == screenName) {
            if (auth instanceof BasicAuthorization) {
                screenName = ((BasicAuthorization) auth).getUserId();
                if (-1 != screenName.indexOf("@")) {
                    screenName = null;
                }
            }
            if (null == screenName) {
                // retrieve the screen name if this instance is authenticated with OAuth or email address
                verifyCredentials();
            }
        }
        return screenName;
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
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if(0 == id){
            verifyCredentials();
        }
        // retrieve the screen name if this instance is authenticated with OAuth or email address
        return id;
    }


    /**
     * {@inheritDoc}
     */
    public QueryResult search(Query query) throws TwitterException {
        try {
            return new QueryResultJSONImpl(http.get(conf.getSearchBaseURL() + "search.json", query.asPostParameters(), null));
        } catch (TwitterException te) {
            if (404 == te.getStatusCode()) {
                return new QueryResultJSONImpl(query);
            } else {
                throw te;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Trends getTrends() throws TwitterException {
        return TrendsJSONImpl.createTrends(http.get(conf.getSearchBaseURL() + "trends.json"));
    }

    /**
     * {@inheritDoc}
     */
    public Trends getCurrentTrends() throws TwitterException {
        return TrendsJSONImpl.createTrendsList(http.get(conf.getSearchBaseURL() + "trends/current.json")).get(0);
    }

    /**
     * {@inheritDoc}
     */
    public Trends getCurrentTrends(boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(http.get(conf.getSearchBaseURL() + "trends/current.json"
                + (excludeHashTags ? "?exclude=hashtags" : ""))).get(0);
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getDailyTrends() throws TwitterException {
        return TrendsJSONImpl.createTrendsList(http.get(conf.getSearchBaseURL() + "trends/daily.json"));
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getDailyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(http.get(conf.getSearchBaseURL()
                + "trends/daily.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : "")));
    }

    private String toDateStr(Date date) {
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getWeeklyTrends() throws TwitterException {
        return TrendsJSONImpl.createTrendsList(http.get(conf.getSearchBaseURL()
                + "trends/weekly.json"));
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getWeeklyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(http.get(conf.getSearchBaseURL()
                + "trends/weekly.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : "")));
    }

    /* Status Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getPublicTimeline() throws
            TwitterException {
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() +
                "statuses/public_timeline.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + "statuses/home_timeline.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + "statuses/home_timeline.json", paging.asPostParameterArray(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFriendsTimeline() throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + "statuses/friends_timeline.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFriendsTimeline(Paging paging) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/friends_timeline.json", paging.asPostParameterArray(), auth));
    }


    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName)}
                , paging.asPostParameterArray()), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(int userId, Paging paging)
            throws TwitterException {
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId)}
                , paging.asPostParameterArray()), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(int user_id) throws TwitterException {
        return getUserTimeline(user_id, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline() throws
            TwitterException {
        return getUserTimeline(new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(Paging paging) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() +
                "statuses/user_timeline.json", paging.asPostParameterArray(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getMentions() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() +
                "statuses/mentions.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getMentions(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/mentions.json", paging.asPostParameterArray(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedByMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweeted_by_me.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedByMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweeted_by_me.json", paging.asPostParameterArray(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedToMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweeted_to_me.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedToMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() +
                "statuses/retweeted_to_me.json", paging.asPostParameterArray(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweets_of_me.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweets_of_me.json", paging.asPostParameterArray(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status showStatus(long id) throws TwitterException {
        return new StatusJSONImpl(http.get(conf.getRestBaseURL() + "statuses/show/" + id + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status), new HttpParameter("source", conf.getSource())}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, GeoLocation location) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status),
                        new HttpParameter("lat", location.getLatitude()),
                        new HttpParameter("long", location.getLongitude()),
                        new HttpParameter("source", conf.getSource())}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, long inReplyToStatusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status), new HttpParameter("in_reply_to_status_id", inReplyToStatusId), new HttpParameter("source", conf.getSource())}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, long inReplyToStatusId
            , GeoLocation location) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status),
                        new HttpParameter("lat", location.getLatitude()),
                        new HttpParameter("long", location.getLongitude()),
                        new HttpParameter("in_reply_to_status_id",inReplyToStatusId),
                        new HttpParameter("source", conf.getSource())}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status destroyStatus(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL()
                + "statuses/destroy/" + statusId + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status retweetStatus(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "statuses/retweet/" + statusId + ".json",
                new HttpParameter[]{new HttpParameter("source", conf.getSource())}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweets/" + statusId + ".json", auth));
    }

    /* User Methods */

    /**
     * {@inheritDoc}
     */
    public User showUser(String screenName) throws TwitterException {
        return new UserJSONImpl(http.get(conf.getRestBaseURL() + "users/show.json?screen_name="
                + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User showUser(int userId) throws TwitterException {
        return new UserJSONImpl(http.get(conf.getRestBaseURL() + "users/show.json?user_id="
                + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> searchUsers(String query, int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(http.get(conf.getRestBaseURL() +
                "users/search.json", new HttpParameter[]{
                new HttpParameter("q", query),
                new HttpParameter("per_page", 20),
                new HttpParameter("page", page)}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses() throws TwitterException {
        return getFriendsStatuses(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL()
                + "statuses/friends.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(String screenName) throws TwitterException {
        return getFriendsStatuses(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(int userId) throws TwitterException {
        return getFriendsStatuses(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(String screenName, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL()
                + "statuses/friends.json?screen_name=" + screenName + "&cursor="
                + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(int userId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL()
                + "statuses/friends.json?user_id=" + userId + "&cursor=" + cursor
                , null, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses() throws TwitterException {
        return getFollowersStatuses(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL()
                + "statuses/followers.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(String screenName) throws TwitterException {
        return getFollowersStatuses(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(int userId) throws TwitterException {
        return getFollowersStatuses(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(String screenName, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL()
                + "statuses/followers.json?screen_name=" + screenName
                + "&cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(int userId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL()
                + "statuses/followers.json?user_id=" + userId + "&cursor=" + cursor, auth));
    }

    /*List Methods*/

    /**
     * {@inheritDoc}
     */
    public UserList createUserList(String listName, boolean isPublicList, String description) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> httpParams = new ArrayList<HttpParameter>();
        httpParams.add(new HttpParameter("name", listName));
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (description != null) {
            httpParams.add(new HttpParameter("description", description));
        }
        return new UserListJSONImpl(http.post(conf.getRestBaseURL() + getScreenName() +
                "/lists.json",
                httpParams.toArray(new HttpParameter[httpParams.size()]),
                auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList updateUserList(int listId, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> httpParams = new ArrayList<HttpParameter>();
        if (newListName != null) {
            httpParams.add(new HttpParameter("name", newListName));
        }
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (newDescription != null) {
            httpParams.add(new HttpParameter("description", newDescription));
        }
        return new UserListJSONImpl(http.post(conf.getRestBaseURL() + getScreenName() + "/lists/"
                + listId + ".json", httpParams.toArray(new HttpParameter[httpParams.size()]), auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserLists(String listOwnerScreenName, long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserListJSONImpl.createUserListList(http.get(conf.getRestBaseURL() +
                listOwnerScreenName + "/lists.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList showUserList(String listOwnerScreenName, int id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(http.get(conf.getRestBaseURL() + listOwnerScreenName + "/lists/"
                + id + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList destroyUserList(int listId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(http.delete(conf.getRestBaseURL() + getScreenName() +
                "/lists/" + listId + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserListStatuses(String listOwnerScreenName, int id, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + listOwnerScreenName +
                "/lists/" + id + "/statuses.json", paging.asPostParameterArray(Paging.SMCP, Paging.PER_PAGE), auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserListMemberships(String listOwnerScreenName, long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserListJSONImpl.createUserListList(http.get(conf.getRestBaseURL() +
                listOwnerScreenName + "/lists/memberships.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserListSubscriptions(String listOwnerScreenName, long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserListJSONImpl.createUserListList(http.get(conf.getRestBaseURL() +
                listOwnerScreenName + "/lists/subscriptions.json?cursor=" + cursor, auth));
    }

    /*List Members Methods*/

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getUserListMembers(String listOwnerScreenName, int listId
            , long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL() +
                listOwnerScreenName + "/" + listId + "/members.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList addUserListMember(int listId, int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(http.post(conf.getRestBaseURL() + getScreenName() +
                "/" + listId + "/members.json?id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList deleteUserListMember(int listId, int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(http.delete(conf.getRestBaseURL() + getScreenName() +
                "/" + listId + "/members.json?id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User checkUserListMembership(String listOwnerScreenName, int listId, int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.get(conf.getRestBaseURL() + listOwnerScreenName + "/" + listId
                + "/members/" + userId + ".json", auth));
    }

    /*List Subscribers Methods*/

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getUserListSubscribers(String listOwnerScreenName
            , int listId, long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL() +
                listOwnerScreenName + "/" + listId + "/subscribers.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList subscribeUserList(String listOwnerScreenName, int listId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(http.post(conf.getRestBaseURL() + listOwnerScreenName +
                "/" + listId + "/subscribers.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList unsubscribeUserList(String listOwnerScreenName, int listId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(http.delete(conf.getRestBaseURL() + listOwnerScreenName +
                "/" + listId + "/subscribers.json?id=" + verifyCredentials().getId(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public User checkUserListSubscription(String listOwnerScreenName, int listId, int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.get(conf.getRestBaseURL() + listOwnerScreenName + "/" + listId
                + "/subscribers/" + userId + ".json", auth));
    }

    /*Direct Message Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(http.get(conf.getRestBaseURL() + "direct_messages.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(http.get(conf.getRestBaseURL()
                + "direct_messages.json", paging.asPostParameterArray(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getSentDirectMessages() throws
            TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(http.get(conf.getRestBaseURL() +
                "direct_messages/sent.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(http.get(conf.getRestBaseURL() +
                "direct_messages/sent.json", paging.asPostParameterArray(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(http.post(conf.getRestBaseURL() + "direct_messages/new.json",
                new HttpParameter[]{new HttpParameter("screen_name", screenName),
                        new HttpParameter("text", text)}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage sendDirectMessage(int userId, String text)
            throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(http.post(conf.getRestBaseURL() + "direct_messages/new.json",
                new HttpParameter[]{new HttpParameter("user_id", userId),
                        new HttpParameter("text", text)}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage destroyDirectMessage(int id) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(http.post(conf.getRestBaseURL() +
                "direct_messages/destroy/" + id + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/create.json?screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/create.json?screen_name=" + screenName
                + "&follow=" + follow, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(int userId, boolean follow) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId
                + "&follow=" + follow, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyFriendship(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/destroy.json?screen_name="
                + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyFriendship(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/destroy.json?user_id="
                + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsFriendship(String userA, String userB) throws TwitterException {
        return -1 != http.get(conf.getRestBaseURL() + "friendships/exists.json",
                getParameterArray("user_a", userA, "user_b", userB), auth).
                asString().indexOf("true");
    }

    /**
     * {@inheritDoc}
     */
    public Relationship showFriendship(String sourceScreenName, String targetScreenName) throws TwitterException {
        return new RelationshipJSONImpl(http.get(conf.getRestBaseURL() + "friendships/show.json",
                getParameterArray("source_screen_name", sourceScreenName,
                        "target_screen_name", targetScreenName), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Relationship showFriendship(int sourceId, int targetId) throws TwitterException {
        return new RelationshipJSONImpl(http.get(conf.getRestBaseURL() + "friendships/show.json",
                getParameterArray("source_id", sourceId, "target_id", targetId), auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs() throws TwitterException {
        return getFriendsIDs(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(long cursor) throws TwitterException {
        return IDsJSONImpl.getFriendsIDs(http.get(conf.getRestBaseURL() + "friends/ids.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(int userId) throws TwitterException {
        return getFriendsIDs(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(int userId, long cursor) throws TwitterException {
        return IDsJSONImpl.getFriendsIDs(http.get(conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId +
                "&cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(String screenName) throws TwitterException {
        return getFriendsIDs(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(String screenName, long cursor) throws TwitterException {
        return IDsJSONImpl.getFriendsIDs(http.get(conf.getRestBaseURL() + "friends/ids.json?screen_name=" + screenName
                + "&cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs() throws TwitterException {
        return getFollowersIDs(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(long cursor) throws TwitterException {
        return IDsJSONImpl.getFriendsIDs(http.get(conf.getRestBaseURL() + "followers/ids.json?cursor=" + cursor
                , auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(int userId) throws TwitterException {
        return getFollowersIDs(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(int userId, long cursor) throws TwitterException {
        return IDsJSONImpl.getFriendsIDs(http.get(conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId
                + "&cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(String screenName) throws TwitterException {
        return getFollowersIDs(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(String screenName, long cursor) throws TwitterException {
        return IDsJSONImpl.getFriendsIDs(http.get(conf.getRestBaseURL() + "followers/ids.json?screen_name="
                + screenName + "&cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User verifyCredentials() throws TwitterException {
        User user = new UserJSONImpl(http.get(conf.getRestBaseURL() + "account/verify_credentials.json"
                , auth));
        this.screenName = user.getScreenName();
        this.id = user.getId();
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfile(String name, String email, String url
            , String location, String description) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> profile = new ArrayList<HttpParameter>(5);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "email", email);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "account/update_profile.json"
                , profile.toArray(new HttpParameter[profile.size()]), auth));
    }

    /**
     * {@inheritDoc}
     */
    public RateLimitStatus getRateLimitStatus() throws TwitterException {
        return RateLimitStatusJSONImpl.createFromJSONResponse(http.get(conf.getRestBaseURL() + "account/rate_limit_status.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public User updateDeliveryDevice(Device device) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "account/update_delivery_device.json", new HttpParameter[]{new HttpParameter("device", device.getName())}, auth));
    }


    /**
     * {@inheritDoc}
     */
    public User updateProfileColors(
            String profileBackgroundColor,
            String profileTextColor,
            String profileLinkColor,
            String profileSidebarFillColor,
            String profileSidebarBorderColor)
            throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> colors = new ArrayList<HttpParameter>(5);
        addParameterToList(colors, "profile_background_color"
                , profileBackgroundColor);
        addParameterToList(colors, "profile_text_color"
                , profileTextColor);
        addParameterToList(colors, "profile_link_color"
                , profileLinkColor);
        addParameterToList(colors, "profile_sidebar_fill_color"
                , profileSidebarFillColor);
        addParameterToList(colors, "profile_sidebar_border_color"
                , profileSidebarBorderColor);
        return new UserJSONImpl(http.post(conf.getRestBaseURL() +
                "account/update_profile_colors.json",
                colors.toArray(new HttpParameter[colors.size()]), auth));
    }

    private void addParameterToList(List<HttpParameter> colors,
                                    String paramName, String color) {
        if (null != color) {
            colors.add(new HttpParameter(paramName, color));
        }
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL()
                + "account/update_profile_image.json",
                new HttpParameter[]{new HttpParameter("image", image)}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfileBackgroundImage(File image, boolean tile)
            throws TwitterException {
        ensureAuthorizationEnabled();
        checkFileValidity(image);
        return new UserJSONImpl(http.post(conf.getRestBaseURL()
                + "account/update_profile_background_image.json",
                new HttpParameter[]{new HttpParameter("image", image),
                        new HttpParameter("tile", tile)}, auth));
    }

    /**
     * Check the existence, and the type of the specified file.
     *
     * @param image image to be uploaded
     * @throws TwitterException when the specified file is not found (FileNotFoundException will be nested)
     *                          , or when the specified file object is not representing a file(IOException will be nested).
     */
    private void checkFileValidity(File image) throws TwitterException {
        if (!image.exists()) {
            //noinspection ThrowableInstanceNeverThrown
            throw new TwitterException(new FileNotFoundException(image + " is not found."));
        }
        if (!image.isFile()) {
            //noinspection ThrowableInstanceNeverThrown
            throw new TwitterException(new IOException(image + " is not a file."));
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "favorites.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + "favorites.json"
                , new HttpParameter[]{new HttpParameter("page", page)}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(String id) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() +
                "favorites/" + id + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(String id, int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + "favorites/" + id + ".json",
                getParameterArray("page", page), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status createFavorite(long id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "favorites/create/" + id + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status destroyFavorite(long id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "favorites/destroy/" + id + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public User enableNotification(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "notifications/follow.json?screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User enableNotification(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "notifications/follow.json?userId=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User disableNotification(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "notifications/leave.json?screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User disableNotification(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "notifications/leave.json?user_id=" + userId, auth));
    }

    /* Block Methods */

    /**
     * {@inheritDoc}
     */
    public User createBlock(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "blocks/create.json?screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createBlock(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "blocks/create.json?user_id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyBlock(String screen_name) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "blocks/destroy.json?screen_name=" + screen_name, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyBlock(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "blocks/destroy.json?user_id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsBlock(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            // @todo this method looks to be always returning false as it's expecting an XML format.
            return -1 == http.get(conf.getRestBaseURL() + "blocks/exists.json?screen_name=" + screenName, auth).
                    asString().indexOf("<error>You are not blocking this user.</error>");
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return false;
            }
            throw te;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsBlock(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return -1 == http.get(conf.getRestBaseURL() + "blocks/exists.json?user_id=" + userId, auth).
                    asString().indexOf("<error>You are not blocking this user.</error>");
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return false;
            }
            throw te;
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getBlockingUsers() throws
            TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(http.get(conf.getRestBaseURL() +
                "blocks/blocking.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getBlockingUsers(int page) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(http.get(conf.getRestBaseURL() +
                "blocks/blocking.json?page=" + page, auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getBlockingUsersIDs() throws TwitterException {
        ensureAuthorizationEnabled();
        return IDsJSONImpl.getBlockIDs(http.get(conf.getRestBaseURL() + "blocks/blocking/ids.json", auth));
    }

    /* Spam Reporting Methods */

    /**
     * {@inheritDoc}
     */
    public User reportSpam(int userId) throws TwitterException{
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "report_spam.json?user_id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User reportSpam(String screenName) throws TwitterException{
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "report_spam.json?screenName=" + screenName, auth));
    }

    /* Saved Searches Methods */

    /**
     * {@inheritDoc}
     */
    public List<SavedSearch> getSavedSearches() throws TwitterException {
        ensureAuthorizationEnabled();
        return SavedSearchJSONImpl.createSavedSearchList(http.get(conf.getRestBaseURL() + "saved_searches.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch showSavedSearch(int id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new SavedSearchJSONImpl(http.get(conf.getRestBaseURL() + "saved_searches/show/" + id
                + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch createSavedSearch(String query) throws TwitterException {
        ensureAuthorizationEnabled();
        return new SavedSearchJSONImpl(http.post(conf.getRestBaseURL() + "saved_searches/create.json"
                , new HttpParameter[]{new HttpParameter("query", query)}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch destroySavedSearch(int id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new SavedSearchJSONImpl(http.post(conf.getRestBaseURL()
                + "saved_searches/destroy/" + id + ".json", auth));
    }

    /* Help Methods */

    /**
     * {@inheritDoc}
     */
    public boolean test() throws TwitterException {
        return -1 != http.get(conf.getRestBaseURL() + "help/test.json").
                asString().indexOf("ok");
    }

    /* OAuth support methods */

    private OAuthSupport getOAuth() {
        if (!(auth instanceof OAuthSupport)) {
            throw new IllegalStateException(
                    "OAuth consumer key/secret combination not supplied");
        }
        return (OAuthSupport)auth;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
        if (auth instanceof NullAuthorization) {
            auth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        }else if(auth instanceof BasicAuthorization){
            throw new IllegalStateException("Basic authenticated instance.");
        }else if(auth instanceof OAuthAuthorization){
            throw new IllegalStateException("consumer key/secret pair already set.");
        }
    }

    // implementation for OAuthSupport interface
    /**
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    /**
     * {@inheritDoc}
     */
    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl);
    }

    /**
     * {@inheritDoc}
     */
    public AccessToken getOAuthAccessToken() throws TwitterException {
        AccessToken oauthAccessToken = getOAuth().getOAuthAccessToken();
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    /**
     * {@inheritDoc}
     */
    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        AccessToken oauthAccessToken = getOAuth().getOAuthAccessToken(oauthVerifier);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        OAuthSupport oauth = getOAuth();
        AccessToken oauthAccessToken = oauth.getOAuthAccessToken(requestToken);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
    }

    /**
     * {@inheritDoc}
     */
    public void setOAuthAccessToken(AccessToken accessToken) {
        getOAuth().setOAuthAccessToken(accessToken);
    }

    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws TwitterException {
        return getOAuth().getOAuthAccessToken(new RequestToken(token,tokenSecret));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized AccessToken getOAuthAccessToken(String token
            , String tokenSecret, String pin) throws TwitterException {
        return getOAuthAccessToken(new RequestToken(token, tokenSecret), pin);
    }

    /**
     * Sets the access token
     *
     * @param token access token
     * @param tokenSecret access token secret
     * @since Twitter 2.0.0
     * @deprecated Use Twitter getInstance(AccessToken accessToken)
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public void setOAuthAccessToken(String token, String tokenSecret) {
        getOAuth().setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }

    /**
     * tests if the instance is authenticated by Basic
     * @return returns true if the instance is authenticated by Basic
     */
    public boolean isOAuthEnabled() {
        return auth instanceof OAuthAuthorization && auth.isEnabled();
    }

    @Override
    public String toString() {
        return "Twitter{" +
                "auth='" + auth + '\'' +
                '}';
    }
}
