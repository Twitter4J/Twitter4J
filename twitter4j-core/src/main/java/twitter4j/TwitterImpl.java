/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j;

import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.util.z_T4JInternalStringUtil;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import static twitter4j.internal.http.HttpParameter.getParameterArray;

/**
 * A java representation of the <a href="https://dev.twitter.com/docs/api">Twitter REST API</a><br>
 * This class is thread safe and can be cached/re-used and used concurrently.<br>
 * Currently this class is not carefully designed to be extended. It is suggested to extend this class only for mock testing purpose.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class TwitterImpl extends TwitterBaseImpl implements Twitter {
    private static final long serialVersionUID = -1486360080128882436L;
    private final String IMPLICIT_PARAMS_STR;
    private final HttpParameter[] IMPLICIT_PARAMS;
    private final HttpParameter INCLUDE_MY_RETWEET;

    private static final Map<Configuration, HttpParameter[]> implicitParamsMap = new HashMap<Configuration, HttpParameter[]>();
    private static final Map<Configuration, String> implicitParamsStrMap = new HashMap<Configuration, String>();

    /*package*/
    TwitterImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        INCLUDE_MY_RETWEET = new HttpParameter("include_my_retweet", conf.isIncludeMyRetweetEnabled());
        HttpParameter[] implicitParams = implicitParamsMap.get(conf);
        String implicitParamsStr = implicitParamsStrMap.get(conf);
        if (implicitParams == null) {
            String includeEntities = conf.isIncludeEntitiesEnabled() ? "1" : "0";
            String includeRTs = conf.isIncludeRTsEnabled() ? "1" : "0";
            boolean contributorsEnabled = conf.getContributingTo() != -1L;
            implicitParamsStr = "include_entities=" + includeEntities + "&include_rts=" + includeRTs
                    + (contributorsEnabled ? "&contributingto=" + conf.getContributingTo() : "");
            implicitParamsStrMap.put(conf, implicitParamsStr);

            List<HttpParameter> params = new ArrayList<HttpParameter>();
            params.add(new HttpParameter("include_entities", includeEntities));
            params.add(new HttpParameter("include_rts", includeRTs));
            if (contributorsEnabled) {
                params.add(new HttpParameter("contributingto", conf.getContributingTo()));
            }
            implicitParams = params.toArray(new HttpParameter[params.size()]);
            implicitParamsMap.put(conf, implicitParams);
        }
        IMPLICIT_PARAMS = implicitParams;
        IMPLICIT_PARAMS_STR = implicitParamsStr;
    }

    /* Timelines Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getMentions() throws TwitterException {
        return getMentionsTimeline();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getMentionsTimeline() throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "statuses/mentions_timeline.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getMentions(Paging paging) throws TwitterException {
        return getMentionsTimeline(paging);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getMentionsTimeline(Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/mentions_timeline.json", paging.asPostParameterArray()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/home_timeline.json", new HttpParameter[]{INCLUDE_MY_RETWEET}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/home_timeline.json", mergeParameters(paging.asPostParameterArray(), new HttpParameter[]{INCLUDE_MY_RETWEET})));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/retweets_of_me.json"));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/retweets_of_me.json", paging.asPostParameterArray()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName)
                        , INCLUDE_MY_RETWEET}
                        , paging.asPostParameterArray())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserTimeline(long userId, Paging paging)
            throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId)
                        , INCLUDE_MY_RETWEET}
                        , paging.asPostParameterArray())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserTimeline(long userId) throws TwitterException {
        return getUserTimeline(userId, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserTimeline() throws
            TwitterException {
        return getUserTimeline(new Paging());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserTimeline(Paging paging) throws
            TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() +
                "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{INCLUDE_MY_RETWEET}
                        , paging.asPostParameterArray())));
    }

    /* Tweets Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "statuses/retweets/" + statusId
                + ".json?count=100"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status showStatus(long id) throws TwitterException {
        return factory.createStatus(get(conf.getRestBaseURL() + "statuses/show/" + id + ".json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status destroyStatus(long statusId) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "statuses/destroy/" + statusId + ".json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status updateStatus(String status) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status updateStatus(StatusUpdate status) throws TwitterException {
        String url = conf.getRestBaseURL() + (status.isWithMedia() ?
                "statuses/update_with_media.json" : "statuses/update.json");
        return factory.createStatus(post(url, status.asHttpParameterArray()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status retweetStatus(long statusId) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "statuses/retweet/" + statusId + ".json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OEmbed getOEmbed(OEmbedRequest req) throws TwitterException {
        return factory.createOEmbed(get(conf.getRestBaseURL()
                + "statuses/oembed.json", req.asHttpParameterArray()));
    }

    /* Search Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public QueryResult search(Query query) throws TwitterException {
        if (query.nextPage() != null) {
            return factory.createQueryResult(get(conf.getRestBaseURL()
                    + "search/tweets.json" + query.nextPage()), query);
        } else {
            return factory.createQueryResult(get(conf.getRestBaseURL()
                    + "search/tweets.json", query.asHttpParameterArray()), query);
        }
    }

    /* Direct Messages Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        return factory.createDirectMessageList(get(conf.getRestBaseURL() + "direct_messages.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        return factory.createDirectMessageList(get(conf.getRestBaseURL() + "direct_messages.json"
                , paging.asPostParameterArray()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<DirectMessage> getSentDirectMessages() throws
            TwitterException {
        return factory.createDirectMessageList(get(conf.getRestBaseURL() + "direct_messages/sent.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws
            TwitterException {
        return factory.createDirectMessageList(get(conf.getRestBaseURL() +
                "direct_messages/sent.json", paging.asPostParameterArray()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectMessage showDirectMessage(long id) throws TwitterException {
        return factory.createDirectMessage(get(conf.getRestBaseURL() + "direct_messages/show.json?id=" + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectMessage destroyDirectMessage(long id) throws
            TwitterException {
        return factory.createDirectMessage(post(conf.getRestBaseURL() + "direct_messages/destroy.json?id=" + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectMessage sendDirectMessage(long userId, String text)
            throws TwitterException {
        return factory.createDirectMessage(post(conf.getRestBaseURL() + "direct_messages/new.json",
                new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("text", text)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        return factory.createDirectMessage(post(conf.getRestBaseURL() + "direct_messages/new.json",
                new HttpParameter[]{new HttpParameter("screen_name", screenName), new HttpParameter("text", text)}));
    }

    /* Friends & Followers Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getFriendsIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friends/ids.json?cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getFriendsIDs(long userId, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId +
                "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getFriendsIDs(String screenName, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friends/ids.json?screen_name=" + screenName
                + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getFollowersIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "followers/ids.json?cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getFollowersIDs(long userId, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId
                + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getFollowersIDs(String screenName, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "followers/ids.json?screen_name="
                + screenName + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Friendship> lookupFriendships(long[] ids) throws TwitterException {
        return factory.createFriendshipList(get(conf.getRestBaseURL() + "friendships/lookup.json?user_id=" + z_T4JInternalStringUtil.join(ids)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Friendship> lookupFriendships(String[] screenNames) throws TwitterException {
        return factory.createFriendshipList(get(conf.getRestBaseURL()
                + "friendships/lookup.json?screen_name=" + z_T4JInternalStringUtil.join(screenNames)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getIncomingFriendships(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friendships/incoming.json?cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getOutgoingFriendships(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friendships/outgoing.json?cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createFriendship(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createFriendship(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/create.json?screen_name=" + screenName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createFriendship(long userId, boolean follow) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId + "&follow="
                + follow));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/create.json?screen_name="
                + screenName + "&follow=" + follow));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User destroyFriendship(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/destroy.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User destroyFriendship(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/destroy.json?screen_name=" + screenName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship updateFriendship(long userId, boolean enableDeviceNotification
            , boolean retweets) throws TwitterException {
        return factory.createRelationship((post(conf.getRestBaseURL() + "friendships/update.json",
                new HttpParameter[]{
                        new HttpParameter("user_id", userId),
                        new HttpParameter("device", enableDeviceNotification),
                        new HttpParameter("retweets", retweets)
                })));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship updateFriendship(String screenName, boolean enableDeviceNotification
            , boolean retweets) throws TwitterException {
        return factory.createRelationship(post(conf.getRestBaseURL() + "friendships/update.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName),
                        new HttpParameter("device", enableDeviceNotification),
                        new HttpParameter("retweets", enableDeviceNotification)
                }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship showFriendship(long sourceId, long targetId) throws TwitterException {
        return factory.createRelationship(get(conf.getRestBaseURL() + "friendships/show.json"
                , new HttpParameter[]{new HttpParameter("source_id", sourceId), new HttpParameter("target_id", targetId)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship showFriendship(String sourceScreenName, String targetScreenName) throws TwitterException {
        return factory.createRelationship(get(conf.getRestBaseURL() + "friendships/show.json",
                getParameterArray("source_screen_name", sourceScreenName, "target_screen_name", targetScreenName)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getFriendsList(long userId, long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "friends/list.json?user_id=" + userId
                + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getFriendsList(String screenName, long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "friends/list.json?screen_name=" + screenName
                + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getFollowersList(long userId, long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "followers/list.json?user_id=" + userId
                + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getFollowersList(String screenName, long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "followers/list.json?screen_name=" + screenName
                + "&cursor=" + cursor));
    }

    /* Users Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public AccountSettings getAccountSettings() throws TwitterException {
        return factory.createAccountSettings(get(conf.getRestBaseURL() + "account/settings.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User verifyCredentials() throws TwitterException {
        return super.fillInIDAndScreenName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccountSettings updateAccountSettings(Integer trend_locationWoeid,
                                                 Boolean sleep_timeEnabled, String start_sleepTime,
                                                 String end_sleepTime, String time_zone, String lang)
            throws TwitterException {
        List<HttpParameter> profile = new ArrayList<HttpParameter>(6);
        if (trend_locationWoeid != null) {
            profile.add(new HttpParameter("trend_location_woeid", trend_locationWoeid));
        }
        if (sleep_timeEnabled != null) {
            profile.add(new HttpParameter("sleep_time_enabled", sleep_timeEnabled.toString()));
        }
        if (start_sleepTime != null) {
            profile.add(new HttpParameter("start_sleep_time", start_sleepTime));
        }
        if (end_sleepTime != null) {
            profile.add(new HttpParameter("end_sleep_time", end_sleepTime));
        }
        if (time_zone != null) {
            profile.add(new HttpParameter("time_zone", time_zone));
        }
        if (lang != null) {
            profile.add(new HttpParameter("lang", lang));
        }
        return factory.createAccountSettings(post(conf.getRestBaseURL() + "account/settings.json"
                , profile.toArray(new HttpParameter[profile.size()])));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateProfile(String name, String url
            , String location, String description) throws TwitterException {
        List<HttpParameter> profile = new ArrayList<HttpParameter>(4);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return factory.createUser(post(conf.getRestBaseURL() + "account/update_profile.json"
                , profile.toArray(new HttpParameter[profile.size()])));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateProfileBackgroundImage(File image, boolean tile)
            throws TwitterException {
        checkFileValidity(image);
        return factory.createUser(post(conf.getRestBaseURL() + "account/update_profile_background_image.json",
                new HttpParameter[]{new HttpParameter("image", image), new HttpParameter("tile", tile)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateProfileBackgroundImage(InputStream image, boolean tile)
            throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "account/update_profile_background_image.json"
                , new HttpParameter[]{new HttpParameter("image", "image", image), new HttpParameter("tile", tile)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateProfileColors(
            String profileBackgroundColor,
            String profileTextColor,
            String profileLinkColor,
            String profileSidebarFillColor,
            String profileSidebarBorderColor)
            throws TwitterException {
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
        return factory.createUser(post(conf.getRestBaseURL() +
                "account/update_profile_colors.json",
                colors.toArray(new HttpParameter[colors.size()])));
    }

    private void addParameterToList(List<HttpParameter> colors,
                                    String paramName, String color) {
        if (color != null) {
            colors.add(new HttpParameter(paramName, color));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        return factory.createUser(post(conf.getRestBaseURL()
                + "account/update_profile_image.json"
                , new HttpParameter[]{new HttpParameter("image", image)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateProfileImage(InputStream image) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL()
                + "account/update_profile_image.json"
                , new HttpParameter[]{new HttpParameter("image", "image", image)}));
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
    @Override
    public PagableResponseList<User> getBlocksList() throws
            TwitterException {
        return getBlocksList(-1L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getBlocksList(long cursor) throws
            TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "blocks/list.json?cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getBlocksIDs() throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "blocks/ids.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDs getBlocksIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "blocks/ids.json?cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createBlock(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "blocks/create.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createBlock(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "blocks/create.json?screen_name=" + screenName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User destroyBlock(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "blocks/destroy.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User destroyBlock(String screen_name) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "blocks/destroy.json?screen_name=" + screen_name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> lookupUsers(long[] ids) throws TwitterException {
        return factory.createUserList(post(conf.getRestBaseURL() + "users/lookup.json"
                , new HttpParameter[]{new HttpParameter("user_id", z_T4JInternalStringUtil.join(ids))}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> lookupUsers(String[] screenNames) throws TwitterException {
        return factory.createUserList(post(conf.getRestBaseURL() + "users/lookup.json"
                , new HttpParameter[]{new HttpParameter("screen_name", z_T4JInternalStringUtil.join(screenNames))}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User showUser(long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() + "users/show.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User showUser(String screenName) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() + "users/show.json?screen_name=" + screenName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> searchUsers(String query, int page) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/search.json"
                , new HttpParameter[]{new HttpParameter("q", query), new HttpParameter("per_page", 20)
                , new HttpParameter("page", page)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> getContributees(long userId) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/contributees.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> getContributees(String screenName) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/contributees.json?screen_name=" + screenName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> getContributors(long userId) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/contributors.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> getContributors(String screenName) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/contributors.json?screen_name=" + screenName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeProfileBanner() throws TwitterException {
        post(conf.getRestBaseURL()
                + "account/remove_profile_banner.json");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProfileBanner(File image) throws TwitterException {
        checkFileValidity(image);
        post(conf.getRestBaseURL()
                + "account/update_profile_banner.json"
                , new HttpParameter[]{new HttpParameter("banner", image)});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProfileBanner(InputStream image) throws TwitterException {
        post(conf.getRestBaseURL()
                + "account/update_profile_banner.json"
                , new HttpParameter[]{new HttpParameter("banner", "banner", image)});
    }

    /* Suggested Users Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> getUserSuggestions(String categorySlug) throws TwitterException {
        HttpResponse res = null;
        try {
            res = get(conf.getRestBaseURL() + "users/suggestions/" + URLEncoder.encode(categorySlug, "UTF-8") + ".json");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return factory.createUserListFromJSONArray_Users(res);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Category> getSuggestedUserCategories() throws TwitterException {
        return factory.createCategoryList(get(conf.getRestBaseURL() + "users/suggestions.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<User> getMemberSuggestions(String categorySlug) throws TwitterException {
        HttpResponse res = null;
        try {
            res = get(conf.getRestBaseURL() + "users/suggestions/" + URLEncoder.encode(categorySlug, "UTF-8") + "/members.json");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return factory.createUserListFromJSONArray(res);
    }

    /* Favorites Resources */

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getFavorites() throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getFavorites(long userId) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getFavorites(String screenName) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json?screen_name=" + screenName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getFavorites(Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json", paging.asPostParameterArray()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getFavorites(long userId, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json" ,
                mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId)}
                        , paging.asPostParameterArray())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getFavorites(String screenName, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json" ,
                mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName)}
                        , paging.asPostParameterArray())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status destroyFavorite(long id) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "favorites/destroy.json?id=" + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status createFavorite(long id) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "favorites/create.json?id=" + id));
    }

    /* Lists Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<UserList> getUserLists(String listOwnerScreenName) throws TwitterException {
        return factory.createUserListList(get(conf.getRestBaseURL() + "lists/list.json?screen_name=" + listOwnerScreenName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<UserList> getUserLists(long listOwnerUserId) throws TwitterException {
        return factory.createUserListList(get(conf.getRestBaseURL() + "lists/list.json?user_id=" + listOwnerUserId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserListStatuses(int listId, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "lists/statuses.json"
                , mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT)
                , new HttpParameter("list_id", listId))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserListStatuses(long ownerId, String slug, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "lists/statuses.json"
                , mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT)
                , new HttpParameter[]{new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug)})));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Status> getUserListStatuses(String ownerScreenName,
            String slug, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "lists/statuses.json"
                , mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT)
                , new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName)
                , new HttpParameter("slug", slug)})));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserListMember(int listId, long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy.json",
                new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("user_id", userId)}));
    }

    @Override
    public UserList deleteUserListMember(int listId, long userId) throws TwitterException {
        return destroyUserListMember(listId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug), new HttpParameter("user_id", userId)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserListMember(String ownerScreenName, String slug,
            long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName)
                , new HttpParameter("slug", slug), new HttpParameter("user_id", userId)}));
    }

    @Override
    public UserList deleteUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return destroyUserListMember(ownerId, slug, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<UserList> getUserListMemberships(long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL() + "lists/memberships.json?cursor=" + cursor));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberScreenName, cursor, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberId, cursor, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL()
                + "lists/memberships.json?user_id=" + listMemberId + "&cursor=" + cursor + "&filter_to_owned_lists="
                + filterToOwnedLists));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL()
                + "lists/memberships.json?screen_name=" + listMemberScreenName + "&cursor=" + cursor
                + "&filter_to_owned_lists=" + filterToOwnedLists));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getUserListSubscribers(int listId, long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "lists/subscribers.json?list_id=" + listId + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "lists/subscribers.json?owner_id=" + ownerId
                + "&slug=" + slug + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getUserListSubscribers(
            String ownerScreenName, String slug, long cursor)
            throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "lists/subscribers.json?owner_screen_name=" + ownerScreenName
                + "&slug=" + slug + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListSubscription(int listId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListSubscription(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug)}));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListSubscription(String ownerScreenName,
            String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName)
                , new HttpParameter("slug", slug)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User showUserListSubscription(int listId, long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/subscribers/show.json?list_id=" + listId + "&user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User showUserListSubscription(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/subscribers/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User showUserListSubscription(String ownerScreenName, String slug,
            long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/subscribers/show.json?owner_screen_name=" + ownerScreenName + "&slug=" + slug + "&user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserListSubscription(int listId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/subscribers/destroy.json", new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserListSubscription(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/subscribers/destroy.json"
                , new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserListSubscription(String ownerScreenName,
            String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/subscribers/destroy.json"
                , new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMembers(int listId, long[] userIds) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("user_id"
                        , z_T4JInternalStringUtil.join(userIds))}));
    }

    @Override
    public UserList addUserListMembers(int listId, long[] userIds) throws TwitterException {
        return createUserListMembers(listId, userIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMembers(long ownerId, String slug, long[] userIds) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)
                        , new HttpParameter("user_id", z_T4JInternalStringUtil.join(userIds))}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMembers(String ownerScreenName, String slug,
            long[] userIds) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)
                        , new HttpParameter("user_id", z_T4JInternalStringUtil.join(userIds))}));
    }

    @Override
    public UserList addUserListMembers(long ownerId, String slug, long[] userIds) throws TwitterException {
        return createUserListMembers(ownerId, slug, userIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMembers(int listId, String[] screenNames) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create_all.json",
                new HttpParameter[]{
                        new HttpParameter("list_id", listId),
                        new HttpParameter("screen_name", z_T4JInternalStringUtil.join(screenNames))}));
    }

    @Override
    public UserList addUserListMembers(int listId, String[] screenNames) throws TwitterException {
        return createUserListMembers(listId, screenNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMembers(long ownerId, String slug, String[] screenNames) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)
                        , new HttpParameter("screen_name", z_T4JInternalStringUtil.join(screenNames))}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMembers(String ownerScreenName, String slug,
            String[] screenNames) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)
                        , new HttpParameter("screen_name", z_T4JInternalStringUtil.join(screenNames))}));
    }

    @Override
    public UserList addUserListMembers(long ownerId, String slug, String[] screenNames) throws TwitterException {
        return createUserListMembers(ownerId, slug, screenNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User showUserListMembership(int listId, long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/members/show.json?list_id=" + listId + "&user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User showUserListMembership(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/members/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User showUserListMembership(String ownerScreenName, String slug,
            long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/members/show.json?owner_screen_name=" + ownerScreenName + "&slug=" + slug + "&user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getUserListMembers(int listId
            , long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() +
                "lists/members.json?list_id=" + listId + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getUserListMembers(long ownerId, String slug, long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() +
                "lists/members.json?owner_id=" + ownerId + "&slug=" + slug + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<User> getUserListMembers(String ownerScreenName,
            String slug, long cursor) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() +
                "lists/members.json?owner_screen_name=" + ownerScreenName + "&slug=" + slug + "&cursor=" + cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMember(int listId, long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId)
                , new HttpParameter("list_id", listId)}));
    }

    @Override
    public UserList addUserListMember(int listId, long userId) throws TwitterException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId)
                , new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserListMember(String ownerScreenName, String slug,
            long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId)
                , new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)}));
    }

    @Override
    public UserList addUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserList(int listId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/destroy.json",
                new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserList(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/destroy.json",
                new HttpParameter[]{new HttpParameter("owner_id", ownerId)
                        , new HttpParameter("slug", slug)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList destroyUserList(String ownerScreenName, String slug)
            throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/destroy.json",
                new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName)
                        , new HttpParameter("slug", slug)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList updateUserList(int listId, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("list_id", listId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList updateUserList(long ownerId, String slug, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList updateUserList(String ownerScreenName, String slug,
            String newListName, boolean isPublicList, String newDescription)
            throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_screen_name", ownerScreenName)
                , new HttpParameter("slug", slug));
    }

    private UserList updateUserList(String newListName, boolean isPublicList, String newDescription, HttpParameter... params) throws TwitterException {
        List<HttpParameter> httpParams = new ArrayList<HttpParameter>();
        Collections.addAll(httpParams, params);
        if (newListName != null) {
            httpParams.add(new HttpParameter("name", newListName));
        }
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (newDescription != null) {
            httpParams.add(new HttpParameter("description", newDescription));
        }
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/update.json", httpParams.toArray(new HttpParameter[httpParams.size()])));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList createUserList(String listName, boolean isPublicList, String description) throws TwitterException {
        List<HttpParameter> httpParams = new ArrayList<HttpParameter>();
        httpParams.add(new HttpParameter("name", listName));
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (description != null) {
            httpParams.add(new HttpParameter("description", description));
        }
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/create.json",
                httpParams.toArray(new HttpParameter[httpParams.size()])));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList showUserList(int listId) throws TwitterException {
        return factory.createAUserList(get(conf.getRestBaseURL() + "lists/show.json?list_id=" + listId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList showUserList(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(get(conf.getRestBaseURL() + "lists/show.json?owner_id=" + ownerId + "&slug="
                + slug));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserList showUserList(String ownerScreenName, String slug)
            throws TwitterException {
        return factory.createAUserList(get(conf.getRestBaseURL() + "lists/show.json?owner_screen_name=" + ownerScreenName + "&slug="
                + slug));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(String listOwnerScreenName, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL() + "lists/subscriptions.json?screen_name="
                + listOwnerScreenName + "&cursor=" + cursor));
    }

    /* Saved Searches Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<SavedSearch> getSavedSearches() throws TwitterException {
        return factory.createSavedSearchList(get(conf.getRestBaseURL() + "saved_searches/list.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SavedSearch showSavedSearch(int id) throws TwitterException {
        return factory.createSavedSearch(get(conf.getRestBaseURL() + "saved_searches/show/" + id
                + ".json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SavedSearch createSavedSearch(String query) throws TwitterException {
        return factory.createSavedSearch(post(conf.getRestBaseURL() + "saved_searches/create.json"
                , new HttpParameter[]{new HttpParameter("query", query)}));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SavedSearch destroySavedSearch(int id) throws TwitterException {
        return factory.createSavedSearch(post(conf.getRestBaseURL()
                + "saved_searches/destroy/" + id + ".json"));
    }

    /* Places & Geo Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public Place getGeoDetails(String placeId) throws TwitterException {
        return factory.createPlace(get(conf.getRestBaseURL() + "geo/id/" + placeId
                + ".json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Place> reverseGeoCode(GeoQuery query) throws TwitterException {
        try {
            return factory.createPlaceList(get(conf.getRestBaseURL()
                    + "geo/reverse_geocode.json", query.asHttpParameterArray()));
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return factory.createEmptyResponseList();
            } else {
                throw te;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Place> searchPlaces(GeoQuery query) throws TwitterException {
        return factory.createPlaceList(get(conf.getRestBaseURL()
                + "geo/search.json", query.asHttpParameterArray()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimilarPlaces getSimilarPlaces(GeoLocation location, String name, String containedWithin, String streetAddress) throws TwitterException {
        List<HttpParameter> params = new ArrayList<HttpParameter>(3);
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        params.add(new HttpParameter("name", name));
        if (containedWithin != null) {
            params.add(new HttpParameter("contained_within", containedWithin));
        }
        if (streetAddress != null) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return factory.createSimilarPlaces(get(conf.getRestBaseURL()
                + "geo/similar_places.json", params.toArray(new HttpParameter[params.size()])));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Place createPlace(String name, String containedWithin, String token, GeoLocation location, String streetAddress) throws TwitterException {
        List<HttpParameter> params = new ArrayList<HttpParameter>(3);
        params.add(new HttpParameter("name", name));
        params.add(new HttpParameter("contained_within", containedWithin));
        params.add(new HttpParameter("token", token));
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        if (streetAddress != null) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return factory.createPlace(post(conf.getRestBaseURL() + "geo/place.json"
                , params.toArray(new HttpParameter[params.size()])));
    }

    /* Trends Resources */

    /**
     * {@inheritDoc}
     */
    @Override
    public Trends getLocationTrends(int woeid) throws TwitterException {
        return getPlaceTrends(woeid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trends getPlaceTrends(int woeid) throws TwitterException {
        return factory.createTrends(get(conf.getRestBaseURL()
                + "trends/place.json?id=" + woeid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Location> getAvailableTrends() throws TwitterException {
        return factory.createLocationList(get(conf.getRestBaseURL()
                + "trends/available.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Location> getAvailableTrends(GeoLocation location) throws TwitterException {
        return getClosestTrends(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Location> getClosestTrends(GeoLocation location) throws TwitterException {
        return factory.createLocationList(get(conf.getRestBaseURL()
                + "trends/closest.json",
                new HttpParameter[]{new HttpParameter("lat", location.getLatitude())
                        , new HttpParameter("long", location.getLongitude())
                }));
    }

    /* Spam Reporting Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public User reportSpam(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "users/report_spam.json?user_id=" + userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User reportSpam(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "users/report_spam.json?screen_name=" + screenName));
    }

    /* Help Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public TwitterAPIConfiguration getAPIConfiguration() throws TwitterException {
        return factory.createTwitterAPIConfiguration(get(conf.getRestBaseURL() + "help/configuration.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseList<Language> getLanguages() throws TwitterException {
        return factory.createLanguageList(get(conf.getRestBaseURL() + "help/languages.json"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrivacyPolicy() throws TwitterException {
        try {
            return get(conf.getRestBaseURL() + "help/privacy.json").asJSONObject().getString("privacy");
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTermsOfService() throws TwitterException {
        try {
            return get(conf.getRestBaseURL() + "help/tos.json").asJSONObject().getString("tos");
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String ,RateLimitStatus> getRateLimitStatus() throws TwitterException {
        return factory.createRateLimitStatuses(get(conf.getRestBaseURL() + "application/rate_limit_status.json"));
    }

    @Override
    public Map<String, RateLimitStatus> getRateLimitStatus(String... resources) throws TwitterException {
        return factory.createRateLimitStatuses(get(conf.getRestBaseURL() + "application/rate_limit_status.json?resources="+ z_T4JInternalStringUtil.join(resources)));
    }

    /* Undocumented Resources */
    /**
     * {@inheritDoc}
     */
    @Override
    public RelatedResults getRelatedResults(long statusId) throws TwitterException {
        return factory.createRelatedResults(get("https://api.twitter.com/1/related_results/show.json?id="+ Long.toString(statusId)));
    }

    private HttpResponse get(String url) throws TwitterException {
        ensureAuthorizationEnabled();
        if (url.contains("?")) {
            url = url + "&" + IMPLICIT_PARAMS_STR;
        } else {
            url = url + "?" + IMPLICIT_PARAMS_STR;
        }
        if (!conf.isMBeanEnabled()) {
            return http.get(url, auth);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.get(url, auth);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    private HttpResponse get(String url, HttpParameter[] params) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!conf.isMBeanEnabled()) {
            return http.get(url, mergeImplicitParams(params), auth);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.get(url, mergeImplicitParams(params), auth);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    private HttpResponse post(String url) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!conf.isMBeanEnabled()) {
            return http.post(url, IMPLICIT_PARAMS, auth);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.post(url, IMPLICIT_PARAMS, auth);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    private HttpResponse post(String url, HttpParameter[] params) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!conf.isMBeanEnabled()) {
            return http.post(url, mergeImplicitParams(params), auth);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.post(url, mergeImplicitParams(params), auth);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter[] params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[params1.length + params2.length];
            System.arraycopy(params1, 0, params, 0, params1.length);
            System.arraycopy(params2, 0, params, params1.length, params2.length);
            return params;
        }
        if (null == params1 && null == params2) {
            return new HttpParameter[0];
        }
        if (params1 != null) {
            return params1;
        } else {
            return params2;
        }
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[params1.length + 1];
            System.arraycopy(params1, 0, params, 0, params1.length);
            params[params.length - 1] = params2;
            return params;
        }
        if (null == params1 && null == params2) {
            return new HttpParameter[0];
        }
        if (params1 != null) {
            return params1;
        } else {
            return new HttpParameter[]{params2};
        }
    }

    private HttpParameter[] mergeImplicitParams(HttpParameter[] params) {
        return mergeParameters(params, IMPLICIT_PARAMS);
    }

    private boolean isOk(HttpResponse response) {
        return response != null && response.getStatusCode() < 300;
    }

    @Override
    public String toString() {
        return "TwitterImpl{" +
                "INCLUDE_MY_RETWEET=" + INCLUDE_MY_RETWEET +
                '}';
    }
}
