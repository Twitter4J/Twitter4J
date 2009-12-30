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
import twitter4j.http.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A java representation of the <a href="http://apiwiki.twitter.com/">Twitter API</a>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Twitter extends OAuthTwitterSupport
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

    /**
     * Creates an unauthenticated Twitter instance
     */
    public Twitter() {
        super();
    }

    /**
     * Creates a Twitter instance with supplied id
     *
     * @param screenName the screen name of the user
     * @param password   the password of the user
     * @deprecated use TwitterFactory.getBasicAuthenticatedInstance(screenName, password) instead
     */
    public Twitter(String screenName, String password) {
        super(screenName, password);
    }
    /*package*/
    Twitter(Authorization auth) {
        super(auth);
    }


    /**
     * Returns authenticating user's screen name.<br>
     * This method automatically retrieves userId using verifyCredentials if the instance is using OAuth based authentication.
     *
     * @return the authenticating screen name
     * @throws TwitterException      when verifyCredentials threw an exception.
     * @throws IllegalStateException if no credentials are supplied
     */
    protected String getScreenName() throws TwitterException, IllegalStateException {
        if(null != screenName){
            return screenName;
        }
        if (!auth.isAuthenticationEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (auth instanceof BasicAuthorization) {
            screenName = ((BasicAuthorization) auth).getUserId();
            if (-1 != screenName.indexOf("@")) {
                screenName = null;
            }
        }
        // retrieve the screen name if this instance is authenticated with OAuth or email address
        return screenName = this.verifyCredentials().getScreenName();
    }


    /**
     * Issues an HTTP GET request.
     *
     * @param url            the request url
     * @param authorization if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    protected Response get(String url, Authorization authorization) throws TwitterException {
        return get(url, null, authorization);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url            the request url
     * @param authorization if true, the request will be sent with BASIC authentication header
     * @param name1          the name of the first parameter
     * @param value1         the value of the first parameter
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    protected Response get(String url, String name1, String value1, Authorization authorization) throws TwitterException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1)}, authorization);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url            the request url
     * @param name1          the name of the first parameter
     * @param value1         the value of the first parameter
     * @param name2          the name of the second parameter
     * @param value2         the value of the second parameter
     * @param authorization if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    protected Response get(String url, String name1, String value1, String name2, String value2, Authorization authorization) throws TwitterException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1), new PostParameter(name2, value2)}, authorization);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, Authorization authenticate) throws TwitterException {
        if (null != params && params.length > 0) {
            url += "?" + HttpClient.encodeParameters(params);
        }
        return http.request(requestFactory.createGetRequest(url, authenticate));
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url            the request url
     * @param params         the request parameters
     * @param pagingParams   controls pagination
     * @param authorization if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, List<PostParameter> pagingParams, Authorization authorization) throws TwitterException {
        if (null != params) {
            if (null != pagingParams) {
                pagingParams.addAll(Arrays.asList(params));
                return get(url, pagingParams.toArray(new PostParameter[pagingParams.size()]), authorization);
            } else {
                return get(url, params, authorization);
            }
        } else if (null != pagingParams) {
            return get(url, pagingParams.toArray(new PostParameter[pagingParams.size()]), authorization);
        } else {
            return get(url, authorization);
        }
    }


    /**
     * {@inheritDoc}
     */
    public QueryResult search(Query query) throws TwitterException {
        try {
            return new QueryResultJSONImpl(get(conf.getSearchBaseURL() + "search.json", query.asPostParameters(), null));
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
        return TrendsJSONImpl.createTrends(get(conf.getSearchBaseURL() + "trends.json", null));
    }

    /**
     * {@inheritDoc}
     */
    public Trends getCurrentTrends() throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(conf.getSearchBaseURL() + "trends/current.json"
                , null)).get(0);
    }

    /**
     * {@inheritDoc}
     */
    public Trends getCurrentTrends(boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(conf.getSearchBaseURL() + "trends/current.json"
                + (excludeHashTags ? "?exclude=hashtags" : ""), null)).get(0);
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getDailyTrends() throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(conf.getSearchBaseURL() + "trends/daily.json", null));
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getDailyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(conf.getSearchBaseURL()
                + "trends/daily.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : ""), null));
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
        return TrendsJSONImpl.createTrendsList(get(conf.getSearchBaseURL()
                + "trends/weekly.json", null));
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getWeeklyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(conf.getSearchBaseURL()
                + "trends/weekly.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : ""), null));
    }

    /* Status Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getPublicTimeline() throws
            TwitterException {
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() +
                "statuses/public_timeline.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/home_timeline.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/home_timeline.json", null, paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFriendsTimeline() throws
            TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/friends_timeline.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFriendsTimeline(Paging paging) throws
            TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/friends_timeline.json", null, paging.asPostParameterList(), auth));
    }


    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL()
                + "statuses/user_timeline.json",
                new PostParameter[]{new PostParameter("screen_name", screenName)}
                , paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(int userId, Paging paging)
            throws TwitterException {
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL()
                + "statuses/user_timeline.json",
                new PostParameter[]{new PostParameter("user_id", userId)}
                , paging.asPostParameterList(), auth));
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
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/user_timeline.json"
                , null, paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getMentions() throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/mentions.json",
                null, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getMentions(Paging paging) throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/mentions.json",
                null, paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedByMe() throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/retweeted_by_me.json",
                null, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedByMe(Paging paging) throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/retweeted_by_me.json",
                null, paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedToMe() throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/retweeted_to_me.json",
                null, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedToMe(Paging paging) throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/retweeted_to_me.json",
                null, paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/retweets_of_me.json",
                null, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "statuses/retweets_of_me.json",
                null, paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status showStatus(long id) throws TwitterException {
        return new StatusJSONImpl(get(conf.getRestBaseURL() + "statuses/show/" + id + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status) throws TwitterException {
        ensureAuthenticationEnabled();
        return new StatusJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", conf.getSource())}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, GeoLocation location) throws TwitterException {
        ensureAuthenticationEnabled();
        return new StatusJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", location.getLatitude()),
                        new PostParameter("long", location.getLongitude()),
                        new PostParameter("source", conf.getSource())}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, long inReplyToStatusId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new StatusJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("in_reply_to_status_id", String.valueOf(inReplyToStatusId)), new PostParameter("source", conf.getSource())}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, long inReplyToStatusId
            , GeoLocation location) throws TwitterException {
        ensureAuthenticationEnabled();
        return new StatusJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", location.getLatitude()),
                        new PostParameter("long", location.getLongitude()),
                        new PostParameter("in_reply_to_status_id",
                                String.valueOf(inReplyToStatusId)),
                        new PostParameter("source", conf.getSource())}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public Status destroyStatus(long statusId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new StatusJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "statuses/destroy/" + statusId + ".json",
                new PostParameter[0], auth)));
    }

    /**
     * {@inheritDoc}
     */
    public Status retweetStatus(long statusId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new StatusJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "statuses/retweet/" + statusId + ".json",
                new PostParameter[]{new PostParameter("source", conf.getSource())}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL()
                + "statuses/retweets/" + statusId + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public User showUser(String screenName) throws TwitterException {
        return new UserJSONImpl(get(conf.getRestBaseURL() + "users/show.json?screen_name="
                + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User showUser(int userId) throws TwitterException {
        return new UserJSONImpl(get(conf.getRestBaseURL() + "users/show.json?user_id="
                + userId, auth));
    }

    /* User Methods */

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
        return UserJSONImpl.createPagableUserList(get(conf.getRestBaseURL()
                + "statuses/friends.json?cursor=" + cursor, null,
                auth));
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
        return UserJSONImpl.createPagableUserList(get(conf.getRestBaseURL()
                + "statuses/friends.json?screen_name=" + screenName + "&cursor=" + cursor
                , null, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(int userId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(conf.getRestBaseURL()
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
        return UserJSONImpl.createPagableUserList(get(conf.getRestBaseURL()
                + "statuses/followers.json?cursor=" + cursor, null, auth));
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
        return UserJSONImpl.createPagableUserList(get(conf.getRestBaseURL() + "statuses/followers.json?screen_name=" + screenName +
                "&cursor=" + cursor, null, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(int userId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(conf.getRestBaseURL() + "statuses/followers.json?user_id=" + userId +
                "&cursor=" + cursor, null, auth));
    }

    /*List Methods*/

    /**
     * {@inheritDoc}
     */
    public UserList createUserList(String listName, boolean isPublicList, String description) throws TwitterException {
        ensureAuthenticationEnabled();
        List<PostParameter> postParams = new ArrayList<PostParameter>();
        postParams.add(new PostParameter("name", listName));
        postParams.add(new PostParameter("mode", isPublicList ? "public" : "private"));
        if (description != null) {
            postParams.add(new PostParameter("description", description));
        }
        return new UserListJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + getScreenName() +
                "/lists.json",
                postParams.toArray(new PostParameter[postParams.size()]),
                auth)));
    }

    /**
     * {@inheritDoc}
     */
    public UserList updateUserList(int listId, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        ensureAuthenticationEnabled();
        List<PostParameter> postParams = new ArrayList<PostParameter>();
        if (newListName != null) {
            postParams.add(new PostParameter("name", newListName));
        }
        postParams.add(new PostParameter("mode", isPublicList ? "public" : "private"));
        if (newDescription != null) {
            postParams.add(new PostParameter("description", newDescription));
        }
        return new UserListJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + getScreenName() + "/lists/"
                + listId + ".json", postParams.toArray(new PostParameter[postParams.size()]), auth)));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserLists(String listOwnerScreenName, long cursor) throws TwitterException {
        ensureAuthenticationEnabled();
        return UserListJSONImpl.createUserListList(get(conf.getRestBaseURL() +
                listOwnerScreenName + "/lists.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList showUserList(String listOwnerScreenName, int id) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserListJSONImpl(get(conf.getRestBaseURL() + listOwnerScreenName + "/lists/"
                + id + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList destroyUserList(int listId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserListJSONImpl(http.request(requestFactory.createDeleteRequest(conf.getRestBaseURL() + getScreenName() +
                "/lists/" + listId + ".json", auth)));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserListStatuses(String listOwnerScreenName, int id, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + listOwnerScreenName +
                "/lists/" + id + "/statuses.json", new PostParameter[0],
                paging.asPostParameterList(Paging.SMCP, Paging.PER_PAGE), auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserListMemberships(String listOwnerScreenName, long cursor) throws TwitterException {
        ensureAuthenticationEnabled();
        return UserListJSONImpl.createUserListList(get(conf.getRestBaseURL() +
                listOwnerScreenName + "/lists/memberships.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserListSubscriptions(String listOwnerScreenName, long cursor) throws TwitterException {
        ensureAuthenticationEnabled();
        return UserListJSONImpl.createUserListList(get(conf.getRestBaseURL() +
                listOwnerScreenName + "/lists/subscriptions.json?cursor=" + cursor, auth));
    }

    /*List Members Methods*/

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getUserListMembers(String listOwnerScreenName, int listId
            , long cursor) throws TwitterException {
        ensureAuthenticationEnabled();
        return UserJSONImpl.createPagableUserList(get(conf.getRestBaseURL() +
                listOwnerScreenName + "/" + listId + "/members.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList addUserListMember(int listId, int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserListJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + getScreenName() +
                "/" + listId + "/members.json?id=" + userId, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public UserList deleteUserListMember(int listId, int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserListJSONImpl(http.request(requestFactory.createDeleteRequest(conf.getRestBaseURL() + getScreenName() +
                "/" + listId + "/members.json?id=" + userId, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User checkUserListMembership(String listOwnerScreenName, int listId, int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(get(conf.getRestBaseURL() + listOwnerScreenName + "/" + listId
                + "/members/" + userId + ".json", auth));
    }

    /*List Subscribers Methods*/

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getUserListSubscribers(String listOwnerScreenName
            , int listId, long cursor) throws TwitterException {
        ensureAuthenticationEnabled();
        return UserJSONImpl.createPagableUserList(get(conf.getRestBaseURL() +
                listOwnerScreenName + "/" + listId + "/subscribers.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList subscribeUserList(String listOwnerScreenName, int listId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserListJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + listOwnerScreenName +
                "/" + listId + "/subscribers.json", auth)));
    }

    /**
     * {@inheritDoc}
     */
    public UserList unsubscribeUserList(String listOwnerScreenName, int listId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserListJSONImpl(http.request(requestFactory.createDeleteRequest(conf.getRestBaseURL() + listOwnerScreenName +
                "/" + listId + "/subscribers.json?id=" + verifyCredentials().getId(), auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User checkUserListSubscription(String listOwnerScreenName, int listId, int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(get(conf.getRestBaseURL() + listOwnerScreenName + "/" + listId
                + "/subscribers/" + userId + ".json", auth));
    }

    /*Direct Message Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        ensureAuthenticationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(get(conf.getRestBaseURL() + "direct_messages.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        ensureAuthenticationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(get(conf.getRestBaseURL()
                + "direct_messages.json", null, paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getSentDirectMessages() throws
            TwitterException {
        ensureAuthenticationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(get(conf.getRestBaseURL() +
                "direct_messages/sent.json", new PostParameter[0], auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws
            TwitterException {
        ensureAuthenticationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(get(conf.getRestBaseURL() +
                "direct_messages/sent.json", new PostParameter[0],
                paging.asPostParameterList(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        ensureAuthenticationEnabled();
        return new DirectMessageJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "direct_messages/new.json",
                new PostParameter[]{new PostParameter("screen_name", screenName),
                        new PostParameter("text", text)}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage sendDirectMessage(int userId, String text)
            throws TwitterException {
        ensureAuthenticationEnabled();
        return new DirectMessageJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "direct_messages/new.json",
                new PostParameter[]{new PostParameter("user_id", userId),
                        new PostParameter("text", text)}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage destroyDirectMessage(int id) throws
            TwitterException {
        ensureAuthenticationEnabled();
        return new DirectMessageJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() +
                "direct_messages/destroy/" + id + ".json", new PostParameter[0], auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(String screenName) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "friendships/create.json?screen_name=" + screenName, new PostParameter[0], auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId, new PostParameter[0], auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "friendships/create.json?screen_name=" + screenName
                + "&follow=" + follow, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(int userId, boolean follow) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId
                + "&follow=" + follow, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyFriendship(String screenName) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "friendships/destroy.json?screen_name="
                + screenName, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyFriendship(int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "friendships/destroy.json?user_id="
                + userId, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsFriendship(String userA, String userB) throws TwitterException {
        return -1 != get(conf.getRestBaseURL() + "friendships/exists.json", "user_a", userA, "user_b", userB, auth).
                asString().indexOf("true");
    }

    /**
     * {@inheritDoc}
     */
    public Relationship showFriendship(String sourceScreenName, String targetScreenName) throws TwitterException {
        return new RelationshipJSONImpl(get(conf.getRestBaseURL() + "friendships/show.json", "source_screen_name", sourceScreenName,
                "target_screen_name", targetScreenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Relationship showFriendship(int sourceId, int targetId) throws TwitterException {
        return new RelationshipJSONImpl(get(conf.getRestBaseURL() + "friendships/show.json", "source_id", String.valueOf(sourceId),
                "target_id", String.valueOf(targetId), auth));
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
        return IDsJSONImpl.getFriendsIDs(get(conf.getRestBaseURL() + "friends/ids.json?cursor=" + cursor, auth));
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
        return IDsJSONImpl.getFriendsIDs(get(conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId +
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
        return IDsJSONImpl.getFriendsIDs(get(conf.getRestBaseURL() + "friends/ids.json?screen_name=" + screenName
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
        return IDsJSONImpl.getFriendsIDs(get(conf.getRestBaseURL() + "followers/ids.json?cursor=" + cursor
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
        return IDsJSONImpl.getFriendsIDs(get(conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId
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
        return IDsJSONImpl.getFriendsIDs(get(conf.getRestBaseURL() + "followers/ids.json?screen_name="
                + screenName + "&cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User verifyCredentials() throws TwitterException {
        return new UserJSONImpl(get(conf.getRestBaseURL() + "account/verify_credentials.json"
                , auth));
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfile(String name, String email, String url
            , String location, String description) throws TwitterException {
        ensureAuthenticationEnabled();
        List<PostParameter> profile = new ArrayList<PostParameter>(5);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "email", email);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "account/update_profile.json"
                , profile.toArray(new PostParameter[profile.size()]), auth)));
    }

    /**
     * {@inheritDoc}
     */
    public RateLimitStatus getRateLimitStatus() throws TwitterException {
        return RateLimitStatusJSONImpl.createFromJSONResponse(http.request(requestFactory.createGetRequest(conf.getRestBaseURL() + "account/rate_limit_status.json", auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User updateDeliveryDevice(Device device) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "account/update_delivery_device.json", new PostParameter[]{new PostParameter("device", device.getName())}, auth)));
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
        ensureAuthenticationEnabled();
        List<PostParameter> colors = new ArrayList<PostParameter>(5);
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
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() +
                "account/update_profile_colors.json",
                colors.toArray(new PostParameter[colors.size()]), auth)));
    }

    private void addParameterToList(List<PostParameter> colors,
                                    String paramName, String color) {
        if (null != color) {
            colors.add(new PostParameter(paramName, color));
        }
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL()
                + "account/update_profile_image.json",
                new PostParameter[]{new PostParameter("image", image)}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfileBackgroundImage(File image, boolean tile)
            throws TwitterException {
        ensureAuthenticationEnabled();
        checkFileValidity(image);
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL()
                + "account/update_profile_background_image.json",
                new PostParameter[]{new PostParameter("image", image),
                        new PostParameter("tile", tile)}, auth)));
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
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "favorites.json", new PostParameter[0], auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(int page) throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "favorites.json", "page", String.valueOf(page), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(String id) throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "favorites/" + id + ".json", new PostParameter[0], auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(String id, int page) throws TwitterException {
        ensureAuthenticationEnabled();
        return StatusJSONImpl.createStatusList(get(conf.getRestBaseURL() + "favorites/" + id + ".json", "page", String.valueOf(page), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status createFavorite(long id) throws TwitterException {
        ensureAuthenticationEnabled();
        return new StatusJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "favorites/create/" + id + ".json", auth)));
    }

    /**
     * {@inheritDoc}
     */
    public Status destroyFavorite(long id) throws TwitterException {
        ensureAuthenticationEnabled();
        return new StatusJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "favorites/destroy/" + id + ".json", auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User enableNotification(String screenName) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "notifications/follow.json?screen_name=" + screenName, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User enableNotification(int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "notifications/follow.json?userId=" + userId, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User disableNotification(String screenName) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "notifications/leave.json?screen_name=" + screenName, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User disableNotification(int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "notifications/leave.json?user_id=" + userId, auth)));
    }

    /* Block Methods */

    /**
     * {@inheritDoc}
     */
    public User createBlock(String screenName) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "blocks/create.json?screen_name=" + screenName, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User createBlock(int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "blocks/create.json?user_id=" + userId, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyBlock(String screen_name) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "blocks/destroy.json?screen_name=" + screen_name, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyBlock(int userId) throws TwitterException {
        ensureAuthenticationEnabled();
        return new UserJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "blocks/destroy.json?user_id=" + userId, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsBlock(String screenName) throws TwitterException {
        ensureAuthenticationEnabled();
        try {
            // @todo this method looks to be always returning false as it's expecting an XML format.
            return -1 == get(conf.getRestBaseURL() + "blocks/exists.json?screen_name=" + screenName, auth).
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
        ensureAuthenticationEnabled();
        try {
            return -1 == get(conf.getRestBaseURL() + "blocks/exists.json?user_id=" + userId, auth).
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
        ensureAuthenticationEnabled();
        return UserJSONImpl.createUserList(get(conf.getRestBaseURL() +
                "blocks/blocking.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getBlockingUsers(int page) throws
            TwitterException {
        ensureAuthenticationEnabled();
        return UserJSONImpl.createUserList(get(conf.getRestBaseURL() +
                "blocks/blocking.json?page=" + page, auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getBlockingUsersIDs() throws TwitterException {
        ensureAuthenticationEnabled();
        return IDsJSONImpl.getBlockIDs(get(conf.getRestBaseURL() + "blocks/blocking/ids.json", auth));
    }

    /* Saved Searches Methods */

    /**
     * {@inheritDoc}
     */
    public List<SavedSearch> getSavedSearches() throws TwitterException {
        ensureAuthenticationEnabled();
        return SavedSearchJSONImpl.createSavedSearchList(get(conf.getRestBaseURL() + "saved_searches.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch showSavedSearch(int id) throws TwitterException {
        ensureAuthenticationEnabled();
        return new SavedSearchJSONImpl(get(conf.getRestBaseURL() + "saved_searches/show/" + id
                + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch createSavedSearch(String query) throws TwitterException {
        ensureAuthenticationEnabled();
        return new SavedSearchJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL() + "saved_searches/create.json"
                , new PostParameter[]{new PostParameter("query", query)}, auth)));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch destroySavedSearch(int id) throws TwitterException {
        ensureAuthenticationEnabled();
        return new SavedSearchJSONImpl(http.request(requestFactory.createPostRequest(conf.getRestBaseURL()
                + "saved_searches/destroy/" + id + ".json", auth)));
    }

    /* Help Methods */

    /**
     * {@inheritDoc}
     */
    public boolean test() throws TwitterException {
        return -1 != get(conf.getRestBaseURL() + "help/test.json", null).
                asString().indexOf("ok");
    }

    @Override
    public boolean equals(Object o) {
        if(!super.equals(o)){
            return false;
        }
        if (this == o) return true;
        if (!(o instanceof Twitter)) return false;
        if (!super.equals(o)) return false;

        Twitter twitter = (Twitter) o;

        if (screenName != null ? !screenName.equals(twitter.screenName) : twitter.screenName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (screenName != null ? screenName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Twitter{" +
                "screenName='" + screenName + '\'' +
                "auth='" + auth + '\'' +
                '}';
    }
}
