/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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

import twitter4j.api.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.Authorization;
import twitter4j.http.AuthorizationFactory;
import twitter4j.http.BasicAuthorization;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.util.StringUtil;

import static twitter4j.internal.http.HttpParameter.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A java representation of the <a href="http://apiwiki.twitter.com/">Twitter API</a><br>
 * This class is thread safe and can be cached/re-used and used concurrently.<br>
 * Currently this class is not carefully designed to be extended. It is suggested to extend this class only for mock testing purporse.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Twitter extends TwitterOAuthSupportBaseImpl
        implements java.io.Serializable,
        SearchMethods,
        TrendsMethods,
        TimelineMethods,
        StatusMethods,
        UserMethods,
        ListMethods,
        ListMembersMethods,
        ListSubscribersMethods,
        DirectMessageMethods,
        FriendshipMethods,
        FriendsFollowersMethods,
        AccountMethods,
        FavoriteMethods,
        NotificationMethods,
        BlockMethods,
        SpamReportingMethods,
        SavedSearchesMethods,
        LocalTrendsMethods,
        GeoMethods,
        LegalResources,
        NewTwitterMethods,
        HelpMethods {
    private static final long serialVersionUID = -1486360080128882436L;

    Twitter(Configuration conf) {
        this(conf, AuthorizationFactory.getInstance(conf, true));
    }


    /**
     * Creates an unauthenticated Twitter instance
     *
     * @deprecated use {@link TwitterFactory#getInstance()} instead
     */
    public Twitter() {
        super(ConfigurationContext.getInstance());
        INCLUDE_ENTITIES = new HttpParameter("include_entities", ConfigurationContext.getInstance().isIncludeEntitiesEnabled());
        INCLUDE_RTS = new HttpParameter("include_rts", conf.isIncludeRTsEnabled());
    }

    /**
     * Creates a Twitter instance with supplied id
     *
     * @param screenName the screen name of the user
     * @param password   the password of the user
     * @deprecated use {@link TwitterFactory#getInstance(String,String)} instead
     */
    public Twitter(String screenName, String password) {
        super(ConfigurationContext.getInstance(), screenName, password);
        INCLUDE_ENTITIES = new HttpParameter("include_entities", ConfigurationContext.getInstance().isIncludeEntitiesEnabled());
        INCLUDE_RTS = new HttpParameter("include_rts", conf.isIncludeRTsEnabled());
    }
    /*package*/

    Twitter(Configuration conf, String screenName, String password) {
        super(conf, screenName, password);
        INCLUDE_ENTITIES = new HttpParameter("include_entities", conf.isIncludeEntitiesEnabled());
        INCLUDE_RTS = new HttpParameter("include_rts", conf.isIncludeRTsEnabled());
    }
    /*package*/

    Twitter(Configuration conf, Authorization auth) {
        super(conf, auth);
        INCLUDE_ENTITIES = new HttpParameter("include_entities", conf.isIncludeEntitiesEnabled());
        INCLUDE_RTS = new HttpParameter("include_rts", conf.isIncludeRTsEnabled());
    }
    private final HttpParameter INCLUDE_ENTITIES;
    private final HttpParameter INCLUDE_RTS;


    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter[] params2) {
        if (null != params1 && null != params2) {
            HttpParameter[] params = new HttpParameter[params1.length + params2.length];
            System.arraycopy(params1, 0, params, 0, params1.length);
            System.arraycopy(params2, 0, params, params1.length, params2.length);
            return params;
        }
        if (null == params1 && null == params2) {
            return new HttpParameter[0];
        }
        if (null != params1) {
            return params1;
        } else {
            return params2;
        }
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter params2) {
        if (null != params1 && null != params2) {
            HttpParameter[] params = new HttpParameter[params1.length + 1];
            System.arraycopy(params1, 0, params, 0, params1.length);
            params[params.length-1] = params2;
            return params;
        }
        if (null == params1 && null == params2) {
            return new HttpParameter[0];
        }
        if (null != params1) {
            return params1;
        } else {
            return new HttpParameter[]{params2};
        }
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
     * @throws TwitterException      when verifyCredentials threw an exception.
     * @throws IllegalStateException if no credentials are supplied. i.e.) this is an anonymous Twitter instance
     * @since Twitter4J 2.1.1
     */
    public int getId() throws TwitterException, IllegalStateException {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (0 == id) {
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
            return new QueryResultJSONImpl(http.get(conf.getSearchBaseURL() + "search.json", query.asHttpParameterArray(), null));
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
        return new TrendsJSONImpl(http.get(conf.getSearchBaseURL() + "trends.json"));
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
                "statuses/public_timeline.json?include_entities=" + conf.isIncludeEntitiesEnabled() + "&include_rts=" + conf.isIncludeRTsEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + "statuses/home_timeline.json?include_entities=" + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/home_timeline.json", mergeParameters(paging.asPostParameterArray(),INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFriendsTimeline() throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/friends_timeline.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&include_rts=" + conf.isIncludeRTsEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFriendsTimeline(Paging paging) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/friends_timeline.json",
                mergeParameters(new HttpParameter[]{INCLUDE_RTS}
                        , paging.asPostParameterArray()), auth));

    }


    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName)
                        , INCLUDE_RTS
                        , INCLUDE_ENTITIES}
                        , paging.asPostParameterArray()), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(int userId, Paging paging)
            throws TwitterException {
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId)
                        , INCLUDE_RTS
                        , INCLUDE_ENTITIES}
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
    public ResponseList<Status> getUserTimeline(int userId) throws TwitterException {
        return getUserTimeline(userId, new Paging());
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
                "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{INCLUDE_RTS
                        , INCLUDE_ENTITIES}
                        , paging.asPostParameterArray()), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getMentions() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() +
                "statuses/mentions.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&include_rts=" + conf.isIncludeRTsEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getMentions(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/mentions.json",
                mergeParameters(new HttpParameter[]{INCLUDE_RTS
                        , INCLUDE_ENTITIES}
                        , paging.asPostParameterArray()), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedByMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweeted_by_me.json?include_entities=" + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedByMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweeted_by_me.json", mergeParameters(paging.asPostParameterArray()
                , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedToMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweeted_to_me.json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedToMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() +
                "statuses/retweeted_to_me.json", mergeParameters(paging.asPostParameterArray()
                , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweets_of_me.json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweets_of_me.json", mergeParameters(paging.asPostParameterArray()
                , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getRetweetedBy(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(http.get(conf.getRestBaseURL()
                + "statuses/" + statusId + "/retweeted_by.json?count=100&include_entities"
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getRetweetedBy(long statusId, Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(http.get(conf.getRestBaseURL()
                + "statuses/" + statusId + "/retweeted_by.json", mergeParameters(paging.asPostParameterArray()
                , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getRetweetedByIDs(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(http.get(conf.getRestBaseURL()
                + "statuses/" + statusId + "/retweeted_by/ids.json?count=100&include_entities"
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getRetweetedByIDs(long statusId, Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(http.get(conf.getRestBaseURL()
                + "statuses/" + statusId + "/retweeted_by/ids.json", mergeParameters(paging.asPostParameterArray()
                , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status showStatus(long id) throws TwitterException {
        return new StatusJSONImpl(http.get(conf.getRestBaseURL() + "statuses/show/" + id + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status)
                        , INCLUDE_ENTITIES}, auth));
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
                        INCLUDE_ENTITIES}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, long inReplyToStatusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status)
                        , new HttpParameter("in_reply_to_status_id", inReplyToStatusId)
                        , INCLUDE_ENTITIES}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, long inReplyToStatusId
            , GeoLocation location) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status)
                        , new HttpParameter("lat", location.getLatitude())
                        , new HttpParameter("long", location.getLongitude())
                        , new HttpParameter("in_reply_to_status_id", inReplyToStatusId)
                        , INCLUDE_ENTITIES}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(StatusUpdate latestStatus) throws TwitterException{
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL()
                + "statuses/update.json",
                mergeParameters(latestStatus.asHttpParameterArray(),
                        INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status destroyStatus(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL()
                + "statuses/destroy/" + statusId + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status retweetStatus(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL()
                + "statuses/retweet/" + statusId + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "statuses/retweets/" + statusId + ".json?count=100&include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /* User Methods */

    /**
     * {@inheritDoc}
     */
    public User showUser(String screenName) throws TwitterException {
        return new UserJSONImpl(http.get(conf.getRestBaseURL() + "users/show.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User showUser(int userId) throws TwitterException {
        return new UserJSONImpl(http.get(conf.getRestBaseURL() + "users/show.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> lookupUsers(String[] screenNames) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(http.get(conf.getRestBaseURL() +
                "users/lookup.json", new HttpParameter[]{
                new HttpParameter("screen_name", toCommaSeparatedString(screenNames))
                , INCLUDE_ENTITIES}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> lookupUsers(int[] ids) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(http.get(conf.getRestBaseURL() +
                "users/lookup.json", new HttpParameter[]{
                new HttpParameter("user_id", toCommaSeparatedString(ids))
                , INCLUDE_ENTITIES}, auth));
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
                new HttpParameter("page", page)
                , INCLUDE_ENTITIES}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Category> getSuggestedUserCategories() throws TwitterException {
        return CategoryJSONImpl.createCategoriesList(http.get(conf.getRestBaseURL() +
                "users/suggestions.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getUserSuggestions(String categorySlug) throws TwitterException {
        HttpResponse res = http.get(conf.getRestBaseURL() + "users/suggestions/"
                + categorySlug + ".json", auth);
        try {
            return UserJSONImpl.createUserList(res.asJSONObject().getJSONArray("users"), res);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ProfileImage getProfileImage(String screenName, ProfileImage.ImageSize size) throws TwitterException {
        return new ProfileImageImpl(http.get(conf.getRestBaseURL() + "users/profile_image/"
                + screenName + ".json?size="+size.getName(), auth));
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
                + "statuses/friends.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&cursor=" + cursor, auth));
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
                + "statuses/friends.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName + "&cursor="
                + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(int userId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL()
                + "statuses/friends.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId
                + "&cursor=" + cursor, null, auth));
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
                + "statuses/followers.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&cursor=" + cursor, auth));
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
                + "statuses/followers.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName + "&cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(int userId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL()
                + "statuses/followers.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId + "&cursor=" + cursor, auth));
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
        return UserListJSONImpl.createPagableUserListList(http.get(conf.getRestBaseURL() +
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
                "/lists/" + id + "/statuses.json", mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.PER_PAGE)
                , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserListJSONImpl.createPagableUserListList(http.get(conf.getRestBaseURL() +
                listMemberScreenName + "/lists/memberships.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserListSubscriptions(String listOwnerScreenName, long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserListJSONImpl.createPagableUserListList(http.get(conf.getRestBaseURL() +
                listOwnerScreenName + "/lists/subscriptions.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<UserList> getAllUserLists(String screenName)
            throws TwitterException{
        return UserListJSONImpl.createUserListList(http.get(conf.getRestBaseURL()
                + "lists/all.json?screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<UserList> getAllUserLists(int userId)
            throws TwitterException{
        return UserListJSONImpl.createUserListList(http.get(conf.getRestBaseURL()
                + "lists/all.json?user_id=" + userId, auth));
    }

    /*List Members Methods*/

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getUserListMembers(String listOwnerScreenName, int listId
            , long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL() +
                listOwnerScreenName + "/" + listId + "/members.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&cursor=" + cursor, auth));
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
    public UserList addUserListMembers(int listId, int[] userIds) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(http.post(conf.getRestBaseURL() + getScreenName() +
                "/" + listId + "/members/create_all.json?user_id=" + toCommaSeparatedString(userIds), auth));
    }

    /**
     * {@inheritDoc}
     */
    public UserList addUserListMembers(int listId, String[] screenNames) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(http.post(conf.getRestBaseURL() + getScreenName() +
                "/" + listId + "/members/create_all.json?screen_name=" + toCommaSeparatedString(screenNames), auth));
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
                + "/members/" + userId + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /*List Subscribers Methods*/

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getUserListSubscribers(String listOwnerScreenName
            , int listId, long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createPagableUserList(http.get(conf.getRestBaseURL() +
                listOwnerScreenName + "/" + listId + "/subscribers.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&cursor=" + cursor, auth));
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
                + "/subscribers/" + userId + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /*Direct Message Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(http.get(conf.getRestBaseURL()
                + "direct_messages.json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(http.get(conf.getRestBaseURL()
                + "direct_messages.json", mergeParameters(paging.asPostParameterArray()
                , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getSentDirectMessages() throws
            TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(http.get(conf.getRestBaseURL() +
                "direct_messages/sent.json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(http.get(conf.getRestBaseURL() +
                "direct_messages/sent.json", mergeParameters(paging.asPostParameterArray()
                , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(http.post(conf.getRestBaseURL() + "direct_messages/new.json",
                new HttpParameter[]{new HttpParameter("screen_name", screenName)
                        , new HttpParameter("text", text)
                        , INCLUDE_ENTITIES}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage sendDirectMessage(int userId, String text)
            throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(http.post(conf.getRestBaseURL() + "direct_messages/new.json",
                new HttpParameter[]{new HttpParameter("user_id", userId),
                        new HttpParameter("text", text)
                        , INCLUDE_ENTITIES}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage destroyDirectMessage(int id) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(http.post(conf.getRestBaseURL() +
                "direct_messages/destroy/" + id + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage getDirectMessage(int id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(http.get(conf.getRestBaseURL()
                + "direct_messages/show/" + id + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/create.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/create.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/create.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName + "&follow=" + follow, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(int userId, boolean follow) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/create.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId + "&follow=" + follow, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyFriendship(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/destroy.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyFriendship(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "friendships/destroy.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId, auth));
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
    public IDs getIncomingFriendships(long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "friendships/incoming.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getOutgoingFriendships(long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "friendships/outgoing.json?cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Friendship> lookupFriendships(String[] screenNames) throws TwitterException {
        ensureAuthorizationEnabled();
        return FriendshipJSONImpl.createFriendshipList(http.get(conf.getRestBaseURL() + "friendships/lookup.json?screen_name=" + StringUtil.join(screenNames), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Friendship> lookupFriendships(int[] ids) throws TwitterException {
        ensureAuthorizationEnabled();
        return FriendshipJSONImpl.createFriendshipList(http.get(conf.getRestBaseURL() + "friendships/lookup.json?user_id=" + StringUtil.join(ids), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Relationship updateFriendship(String screenName, boolean enableDeviceNotification
            , boolean retweets) throws TwitterException {
        ensureAuthorizationEnabled();
        return new RelationshipJSONImpl(http.post(conf.getRestBaseURL() + "friendships/update.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName),
                        new HttpParameter("device", enableDeviceNotification),
                        new HttpParameter("retweets", enableDeviceNotification)
                }, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Relationship updateFriendship(int userId, boolean enableDeviceNotification
            , boolean retweets) throws TwitterException {
        ensureAuthorizationEnabled();
        return new RelationshipJSONImpl(http.post(conf.getRestBaseURL() + "friendships/update.json",
                new HttpParameter[]{
                        new HttpParameter("user_id", userId),
                        new HttpParameter("device", enableDeviceNotification),
                        new HttpParameter("retweets", enableDeviceNotification)
                }, auth));
    }

    /* Social Graph Methods */

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
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "friends/ids.json?cursor=" + cursor, auth));
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
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId +
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
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "friends/ids.json?screen_name=" + screenName
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
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "followers/ids.json?cursor=" + cursor
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
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId
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
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "followers/ids.json?screen_name="
                + screenName + "&cursor=" + cursor, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User verifyCredentials() throws TwitterException {
        User user = new UserJSONImpl(http.get(conf.getRestBaseURL() + "account/verify_credentials.json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
        this.screenName = user.getScreenName();
        this.id = user.getId();
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public RateLimitStatus getRateLimitStatus() throws TwitterException {
        return new RateLimitStatusJSONImpl(http.get(conf.getRestBaseURL() + "account/rate_limit_status.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfile(String name, String email, String url
            , String location, String description) throws TwitterException {
        ensureAuthorizationEnabled();
        return updateProfile(name, url, location, description);
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfile(String name, String url
            , String location, String description) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> profile = new ArrayList<HttpParameter>(4);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        profile.add(INCLUDE_ENTITIES);
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "account/update_profile.json"
                , profile.toArray(new HttpParameter[profile.size()]), auth));
    }

    /**
     * {@inheritDoc}
     */
    public AccountTotals getAccountTotals() throws TwitterException {
        ensureAuthorizationEnabled();
        return new AccountTotalsJSONImpl(http.get(conf.getRestBaseURL() + "account/totals.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public AccountSettings getAccountSettings() throws TwitterException {
        ensureAuthorizationEnabled();
        return new AccountSettingsJSONImpl(http.get(conf.getRestBaseURL() + "account/settings.json", auth));
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
        List<HttpParameter> colors = new ArrayList<HttpParameter>(6);
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
        colors.add(INCLUDE_ENTITIES);
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
                + "account/update_profile_image.json"
                , new HttpParameter[]{new HttpParameter("image", image)
                        , INCLUDE_ENTITIES}, auth));
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
                new HttpParameter[]{new HttpParameter("image", image)
                        , new HttpParameter("tile", tile)
                        , INCLUDE_ENTITIES}, auth));
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
                + "favorites.json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + "favorites.json"
                , new HttpParameter[]{new HttpParameter("page", page)
                        , INCLUDE_ENTITIES}, auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(String id) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL()
                + "favorites/" + id + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(String id, int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(http.get(conf.getRestBaseURL() + "favorites/" + id + ".json",
                mergeParameters(getParameterArray("page", page)
                        , INCLUDE_ENTITIES), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status createFavorite(long id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "favorites/create/" + id + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public Status destroyFavorite(long id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(http.post(conf.getRestBaseURL() + "favorites/destroy/" + id + ".json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public User enableNotification(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "notifications/follow.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User enableNotification(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "notifications/follow.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&userId=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User disableNotification(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "notifications/leave.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User disableNotification(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "notifications/leave.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId, auth));
    }

    /* Block Methods */

    /**
     * {@inheritDoc}
     */
    public User createBlock(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "blocks/create.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User createBlock(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "blocks/create.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyBlock(String screen_name) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "blocks/destroy.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screen_name, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyBlock(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "blocks/destroy.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId, auth));
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
                "blocks/blocking.json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getBlockingUsers(int page) throws
            TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(http.get(conf.getRestBaseURL() +
                "blocks/blocking.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&page=" + page, auth));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getBlockingUsersIDs() throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(http.get(conf.getRestBaseURL() + "blocks/blocking/ids.json", auth));
    }

    /* Spam Reporting Methods */

    /**
     * {@inheritDoc}
     */
    public User reportSpam(int userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "report_spam.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&user_id=" + userId, auth));
    }

    /**
     * {@inheritDoc}
     */
    public User reportSpam(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(http.post(conf.getRestBaseURL() + "report_spam.json?include_entities="
                + conf.isIncludeEntitiesEnabled() + "&screen_name=" + screenName, auth));
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
    /* Local Trends Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<Location> getAvailableTrends() throws TwitterException {
        return LocationJSONImpl.createLocationList(http.get(conf.getRestBaseURL()
                + "trends/available.json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Location> getAvailableTrends(GeoLocation location) throws TwitterException {
        return LocationJSONImpl.createLocationList(http.get(conf.getRestBaseURL()
                + "trends/available.json",
                new HttpParameter[]{new HttpParameter("lat", location.getLatitude())
                        ,new HttpParameter("long", location.getLongitude())
                }, auth));
    }

    /**
     * {@inheritDoc}
     */
    public Trends getLocationTrends(int woeid) throws TwitterException {
        return new TrendsJSONImpl(http.get(conf.getRestBaseURL()
                + "trends/" + woeid + ".json", auth));
    }

    /* Geo Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<Place> searchPlaces(GeoQuery query) throws TwitterException {
        try{
            return PlaceJSONImpl.createPlaceList(http.get(conf.getRestBaseURL()
                    + "geo/search.json", query.asHttpParameterArray(), auth));
        }catch(TwitterException te){
            if(te.getStatusCode() == 404){
                return new ResponseListImpl<Place>(0, null);
            }else{
                throw te;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public SimilarPlaces getSimilarPlaces(GeoLocation location, String name, String containedWithin, String streetAddress) throws TwitterException {
        List<HttpParameter> params = new ArrayList<HttpParameter>(3);
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        params.add(new HttpParameter("name", name));
        if (null != containedWithin) {
            params.add(new HttpParameter("contained_within", containedWithin));
        }
        if (null != streetAddress) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return SimilarPlacesImpl.createSimilarPlaces(http.get(conf.getRestBaseURL()
                + "geo/similar_places.json", params.toArray(new HttpParameter[params.size()]), auth));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Place> getNearbyPlaces(GeoQuery query) throws TwitterException {
        try{
            return PlaceJSONImpl.createPlaceList(http.get(conf.getRestBaseURL()
                    + "geo/nearby_places.json", query.asHttpParameterArray(), auth));
        }catch(TwitterException te){
            if(te.getStatusCode() == 404){
                return new ResponseListImpl<Place>(0, null);
            }else{
                throw te;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Place> reverseGeoCode(GeoQuery query) throws TwitterException {
        try{
            return PlaceJSONImpl.createPlaceList(http.get(conf.getRestBaseURL()
                    + "geo/reverse_geocode.json", query.asHttpParameterArray(), auth));
        }catch(TwitterException te){
            if(te.getStatusCode() == 404){
                return new ResponseListImpl<Place>(0, null);
            }else{
                throw te;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Place getGeoDetails(String id) throws TwitterException {
        return new PlaceJSONImpl(http.get(conf.getRestBaseURL() + "geo/id/" + id
                + ".json", auth));
    }

    /**
     * {@inheritDoc}
     */
    public Place createPlace(String name, String containedWithin, String token, GeoLocation location, String streetAddress) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> params = new ArrayList<HttpParameter>(3);
        params.add(new HttpParameter("name", name));
        params.add(new HttpParameter("contained_within", containedWithin));
        params.add(new HttpParameter("token", token));
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        if (null != streetAddress) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return new PlaceJSONImpl(http.post(conf.getRestBaseURL() + "geo/place.json"
                , params.toArray(new HttpParameter[params.size()]), auth));
    }

    /* Legal Resources */
    /**
     * {@inheritDoc}
     */
    public String getTermsOfService() throws TwitterException {
        try {
            return http.get(conf.getRestBaseURL() + "legal/tos.json", auth).asJSONObject().getString("tos");
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getPrivacyPolicy() throws TwitterException {
        try {
            return http.get(conf.getRestBaseURL() + "legal/privacy.json", auth).asJSONObject().getString("privacy");
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /* #newtwitter Methods */

    /**
     * {@inheritDoc}
     */
    public RelatedResults getRelatedResults(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new RelatedResultsJSONImpl(http.get(conf.getRestBaseURL() + "related_results/show/"
                + Long.toString(statusId) + ".json", auth));
    }

    /* Help Methods */

    /**
     * {@inheritDoc}
     */
    public boolean test() throws TwitterException {
        return -1 != http.get(conf.getRestBaseURL() + "help/test.json").
                asString().indexOf("ok");
    }

    private static String toCommaSeparatedString(String[] strArray){
        StringBuffer buf = new StringBuffer(strArray.length * 8);
        for (String value : strArray) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(value);
        }
        return buf.toString();
    }
    private static String toCommaSeparatedString(int[] strArray){
        StringBuffer buf = new StringBuffer(strArray.length * 8);
        for (int value : strArray) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(value);
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        return "Twitter{" +
                "auth='" + auth + '\'' +
                '}';
    }
}
