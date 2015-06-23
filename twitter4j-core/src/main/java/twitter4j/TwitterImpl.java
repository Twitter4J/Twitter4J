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

import twitter4j.api.*;
import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static twitter4j.HttpParameter.getParameterArray;

/**
 * A java representation of the <a href="https://dev.twitter.com/docs/api">Twitter REST API</a><br>
 * This class is thread safe and can be cached/re-used and used concurrently.<br>
 * Currently this class is not carefully designed to be extended. It is suggested to extend this class only for mock testing purpose.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class TwitterImpl extends TwitterBaseImpl implements Twitter {
    private static final long serialVersionUID = 9170943084096085770L;
    private final String IMPLICIT_PARAMS_STR;
    private final HttpParameter[] IMPLICIT_PARAMS;
    private final HttpParameter INCLUDE_MY_RETWEET;

    private static final ConcurrentHashMap<Configuration, HttpParameter[]> implicitParamsMap = new ConcurrentHashMap<Configuration, HttpParameter[]>();
    private static final ConcurrentHashMap<Configuration, String> implicitParamsStrMap = new ConcurrentHashMap<Configuration, String>();

    /*package*/
    TwitterImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        INCLUDE_MY_RETWEET = new HttpParameter("include_my_retweet", conf.isIncludeMyRetweetEnabled());
        if (implicitParamsMap.containsKey(conf)) {
            this.IMPLICIT_PARAMS = implicitParamsMap.get(conf);
            this.IMPLICIT_PARAMS_STR = implicitParamsStrMap.get(conf);
        } else {
            String implicitParamsStr = conf.isIncludeEntitiesEnabled() ? "include_entities=" + true : "";
            boolean contributorsEnabled = conf.getContributingTo() != -1L;
            if (contributorsEnabled) {
                if (!"".equals(implicitParamsStr)) {
                    implicitParamsStr += "?";
                }
                implicitParamsStr += "contributingto=" + conf.getContributingTo();
            }

            List<HttpParameter> params = new ArrayList<HttpParameter>(3);
            if (conf.isIncludeEntitiesEnabled()) {
                params.add(new HttpParameter("include_entities", "true"));
            }
            if (contributorsEnabled) {
                params.add(new HttpParameter("contributingto", conf.getContributingTo()));
            }
            if (conf.isTrimUserEnabled()) {
                params.add(new HttpParameter("trim_user", "1"));
            }
            HttpParameter[] implicitParams = params.toArray(new HttpParameter[params.size()]);

            // implicitParamsMap.containsKey() is evaluated in the above if clause.
            // thus implicitParamsStrMap needs to be initialized first
            implicitParamsStrMap.putIfAbsent(conf, implicitParamsStr);
            implicitParamsMap.putIfAbsent(conf, implicitParams);

            this.IMPLICIT_PARAMS = implicitParams;
            this.IMPLICIT_PARAMS_STR = implicitParamsStr;
        }
    }

    /* Timelines Resources */

    @Override
    public ResponseList<Status> getMentionsTimeline() throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "statuses/mentions_timeline.json"));
    }

    @Override
    public ResponseList<Status> getMentionsTimeline(Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/mentions_timeline.json", paging.asPostParameterArray()));
    }

    @Override
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/home_timeline.json", INCLUDE_MY_RETWEET));
    }

    @Override
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/home_timeline.json", mergeParameters(paging.asPostParameterArray(), new HttpParameter[]{INCLUDE_MY_RETWEET})));
    }

    @Override
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/retweets_of_me.json"));
    }

    @Override
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                + "statuses/retweets_of_me.json", paging.asPostParameterArray()));
    }

    @Override
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                        + "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName)
                        , INCLUDE_MY_RETWEET}
                        , paging.asPostParameterArray())
        ));
    }

    @Override
    public ResponseList<Status> getUserTimeline(long userId, Paging paging)
            throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL()
                        + "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId)
                        , INCLUDE_MY_RETWEET}
                        , paging.asPostParameterArray())
        ));
    }

    @Override
    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, new Paging());
    }

    @Override
    public ResponseList<Status> getUserTimeline(long userId) throws TwitterException {
        return getUserTimeline(userId, new Paging());
    }

    @Override
    public ResponseList<Status> getUserTimeline() throws
            TwitterException {
        return getUserTimeline(new Paging());
    }

    @Override
    public ResponseList<Status> getUserTimeline(Paging paging) throws
            TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() +
                        "statuses/user_timeline.json",
                mergeParameters(new HttpParameter[]{INCLUDE_MY_RETWEET}
                        , paging.asPostParameterArray())
        ));
    }

    /* Tweets Resources */

    @Override
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "statuses/retweets/" + statusId
                + ".json?count=100"));
    }

    @Override
    public IDs getRetweeterIds(long statusId, long cursor) throws TwitterException {
        return getRetweeterIds(statusId, 100, cursor);
    }

    @Override
    public IDs getRetweeterIds(long statusId, int count, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "statuses/retweeters/ids.json?id=" + statusId
                + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public Status showStatus(long id) throws TwitterException {
        return factory.createStatus(get(conf.getRestBaseURL() + "statuses/show/" + id + ".json", new HttpParameter[]{INCLUDE_MY_RETWEET}));
    }

    @Override
    public Status destroyStatus(long statusId) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "statuses/destroy/" + statusId + ".json"));
    }

    @Override
    public Status updateStatus(String status) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "statuses/update.json",
                new HttpParameter[]{new HttpParameter("status", status)}));
    }

    @Override
    public Status updateStatus(StatusUpdate status) throws TwitterException {
        String url = conf.getRestBaseURL() + (status.isForUpdateWithMedia() ?
                "statuses/update_with_media.json" : "statuses/update.json");
        return factory.createStatus(post(url, status.asHttpParameterArray()));
    }

    @Override
    public Status retweetStatus(long statusId) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "statuses/retweet/" + statusId + ".json"));
    }

    @Override
    public OEmbed getOEmbed(OEmbedRequest req) throws TwitterException {
        return factory.createOEmbed(get(conf.getRestBaseURL()
                + "statuses/oembed.json", req.asHttpParameterArray()));
    }

    @Override
    public ResponseList<Status> lookup(long... ids) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "statuses/lookup.json?id=" + StringUtil.join(ids)));
    }

    @Override
    public UploadedMedia uploadMedia(File image) throws TwitterException {
        checkFileValidity(image);
        return new UploadedMedia(post(conf.getUploadBaseURL() + "media/upload.json"
                , new HttpParameter[]{new HttpParameter("media", image)}).asJSONObject());
    }

    @Override
    public UploadedMedia uploadMedia(String fileName, InputStream image) throws TwitterException {
        return new UploadedMedia(post(conf.getUploadBaseURL() + "media/upload.json"
                , new HttpParameter[]{new HttpParameter("media", fileName, image)}).asJSONObject());
    }

    /* Search Resources */

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

    @Override
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        return factory.createDirectMessageList(get(conf.getRestBaseURL() + "direct_messages.json?full_text=true"));
    }

    @Override
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        return factory.createDirectMessageList(get(conf.getRestBaseURL() + "direct_messages.json"
            , mergeParameters(paging.asPostParameterArray(), new HttpParameter("full_text", true))));
    }

    @Override
    public ResponseList<DirectMessage> getSentDirectMessages() throws TwitterException {
        return factory.createDirectMessageList(get(conf.getRestBaseURL() + "direct_messages/sent.json?full_text=true"));
    }

    @Override
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws TwitterException {
        return factory.createDirectMessageList(get(conf.getRestBaseURL() +
            "direct_messages/sent.json"
            , mergeParameters(paging.asPostParameterArray(), new HttpParameter("full_text", true))));
    }

    @Override
    public DirectMessage showDirectMessage(long id) throws TwitterException {
        return factory.createDirectMessage(get(conf.getRestBaseURL() + "direct_messages/show.json?id=" + id
            + "&full_text=true"));
    }

    @Override
    public DirectMessage destroyDirectMessage(long id) throws
        TwitterException {
        return factory.createDirectMessage(post(conf.getRestBaseURL() + "direct_messages/destroy.json?id=" + id
            + "&full_text=true"));
    }

    @Override
    public DirectMessage sendDirectMessage(long userId, String text)
        throws TwitterException {
        return factory.createDirectMessage(post(conf.getRestBaseURL() + "direct_messages/new.json"
            , new HttpParameter("user_id", userId), new HttpParameter("text", text)
            , new HttpParameter("full_text", true)));
    }

    @Override
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        return factory.createDirectMessage(post(conf.getRestBaseURL() + "direct_messages/new.json"
            , new HttpParameter("screen_name", screenName), new HttpParameter("text", text)
            , new HttpParameter("full_text", true)));
    }

    @Override
    public InputStream getDMImageAsStream(String url) throws TwitterException {
        return get(url).asStream();
    }

    /* Friends & Followers Resources */

    @Override
    public IDs getNoRetweetsFriendships() throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friendships/no_retweets/ids.json"));
    }

    @Override
    public IDs getFriendsIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friends/ids.json?cursor=" + cursor));
    }

    @Override
    public IDs getFriendsIDs(long userId, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId +
                "&cursor=" + cursor));
    }

    @Override
    public IDs getFriendsIDs(long userId, long cursor, int count) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId +
                "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public IDs getFriendsIDs(String screenName, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friends/ids.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("cursor", cursor)));
    }

    @Override
    public IDs getFriendsIDs(String screenName, long cursor, int count) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friends/ids.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("cursor", cursor),
                new HttpParameter("count", count)));
    }

    @Override
    public IDs getFollowersIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "followers/ids.json?cursor=" + cursor));
    }

    @Override
    public IDs getFollowersIDs(long userId, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId
                + "&cursor=" + cursor));
    }

    @Override
    public IDs getFollowersIDs(long userId, long cursor, int count) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId
                + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public IDs getFollowersIDs(String screenName, long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "followers/ids.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("cursor", cursor)));
    }

    @Override
    public IDs getFollowersIDs(String screenName, long cursor, int count) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "followers/ids.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("cursor", cursor),
                new HttpParameter("count", count)));
    }

    @Override
    public ResponseList<Friendship> lookupFriendships(long... ids) throws TwitterException {
        return factory.createFriendshipList(get(conf.getRestBaseURL() + "friendships/lookup.json?user_id=" + StringUtil.join(ids)));
    }

    @Override
    public ResponseList<Friendship> lookupFriendships(String... screenNames) throws TwitterException {
        return factory.createFriendshipList(get(conf.getRestBaseURL()
                + "friendships/lookup.json?screen_name=" + StringUtil.join(screenNames)));
    }

    @Override
    public IDs getIncomingFriendships(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friendships/incoming.json?cursor=" + cursor));
    }

    @Override
    public IDs getOutgoingFriendships(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "friendships/outgoing.json?cursor=" + cursor));
    }

    @Override
    public User createFriendship(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId));
    }

    @Override
    public User createFriendship(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/create.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName)
                }
        ));
    }

    @Override
    public User createFriendship(long userId, boolean follow) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId + "&follow="
                + follow));
    }

    @Override
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/create.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName),
                        new HttpParameter("follow", follow)
                }
        ));
    }

    @Override
    public User destroyFriendship(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/destroy.json?user_id=" + userId));
    }

    @Override
    public User destroyFriendship(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "friendships/destroy.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName)
                }
        ));
    }

    @Override
    public Relationship updateFriendship(long userId, boolean enableDeviceNotification
            , boolean retweets) throws TwitterException {
        return factory.createRelationship((post(conf.getRestBaseURL() + "friendships/update.json",
                new HttpParameter("user_id", userId),
                new HttpParameter("device", enableDeviceNotification),
                new HttpParameter("retweets", retweets))));
    }

    @Override
    public Relationship updateFriendship(String screenName, boolean enableDeviceNotification
            , boolean retweets) throws TwitterException {
        return factory.createRelationship(post(conf.getRestBaseURL() + "friendships/update.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("device", enableDeviceNotification),
                new HttpParameter("retweets", retweets)));
    }

    @Override
    public Relationship showFriendship(long sourceId, long targetId) throws TwitterException {
        return factory.createRelationship(get(conf.getRestBaseURL() + "friendships/show.json"
                , new HttpParameter("source_id", sourceId), new HttpParameter("target_id", targetId)));
    }

    @Override
    public Relationship showFriendship(String sourceScreenName, String targetScreenName) throws TwitterException {
        return factory.createRelationship(get(conf.getRestBaseURL() + "friendships/show.json",
                getParameterArray("source_screen_name", sourceScreenName, "target_screen_name", targetScreenName)));
    }

    @Override
    public PagableResponseList<User> getFriendsList(long userId, long cursor) throws TwitterException {
        return getFriendsList(userId, cursor, 20);
    }

    @Override
    public PagableResponseList<User> getFriendsList(long userId, long cursor, int count) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "friends/list.json?user_id=" + userId
                + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public PagableResponseList<User> getFriendsList(String screenName, long cursor) throws TwitterException {
        return getFriendsList(screenName, cursor, 20);
    }

    @Override
    public PagableResponseList<User> getFriendsList(String screenName, long cursor, int count) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "friends/list.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("cursor", cursor),
                new HttpParameter("count", count)));
    }

    @Override
    public PagableResponseList<User> getFriendsList(long userId, long cursor, int count, boolean skipStatus, boolean includeUserEntities) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "friends/list.json?user_id=" + userId
                + "&cursor=" + cursor + "&count=" + count
                + "&skip_status=" + skipStatus + "&include_user_entities=" + includeUserEntities));
    }

    @Override
    public PagableResponseList<User> getFriendsList(String screenName, long cursor, int count,
                                                    boolean skipStatus, boolean includeUserEntities) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "friends/list.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("cursor", cursor),
                new HttpParameter("count", count),
                new HttpParameter("skip_status", skipStatus),
                new HttpParameter("include_user_entities", includeUserEntities)));
    }

    @Override
    public PagableResponseList<User> getFollowersList(long userId, long cursor) throws TwitterException {
        return getFollowersList(userId, cursor, 20);
    }

    @Override
    public PagableResponseList<User> getFollowersList(String screenName, long cursor) throws TwitterException {
        return getFollowersList(screenName, cursor, 20);
    }

    @Override
    public PagableResponseList<User> getFollowersList(long userId, long cursor, int count) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "followers/list.json?user_id=" + userId
                + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public PagableResponseList<User> getFollowersList(String screenName, long cursor, int count) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "followers/list.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("cursor", cursor),
                new HttpParameter("count", count)));
    }

    @Override
    public PagableResponseList<User> getFollowersList(long userId, long cursor, int count,
                                                      boolean skipStatus, boolean includeUserEntities) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "followers/list.json?user_id=" + userId
                + "&cursor=" + cursor + "&count=" + count
                + "&skip_status=" + skipStatus + "&include_user_entities=" + includeUserEntities));
    }

    @Override
    public PagableResponseList<User> getFollowersList(String screenName, long cursor, int count,
                                                      boolean skipStatus, boolean includeUserEntities) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "followers/list.json",
                new HttpParameter("screen_name", screenName),
                new HttpParameter("cursor", cursor),
                new HttpParameter("count", count),
                new HttpParameter("skip_status", skipStatus),
                new HttpParameter("include_user_entities", includeUserEntities)));
    }

    /* Users Resources */

    @Override
    public AccountSettings getAccountSettings() throws TwitterException {
        return factory.createAccountSettings(get(conf.getRestBaseURL() + "account/settings.json"));
    }

    @Override
    public User verifyCredentials() throws TwitterException {
        return super.fillInIDAndScreenName();
    }

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

    @Override
    public User updateProfileBackgroundImage(File image, boolean tile)
            throws TwitterException {
        checkFileValidity(image);
        return factory.createUser(post(conf.getRestBaseURL() + "account/update_profile_background_image.json",
                new HttpParameter[]{new HttpParameter("image", image), new HttpParameter("tile", tile)}));
    }

    @Override
    public User updateProfileBackgroundImage(InputStream image, boolean tile)
            throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "account/update_profile_background_image.json"
                , new HttpParameter[]{new HttpParameter("image", "image", image), new HttpParameter("tile", tile)}));
    }

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
                colors.toArray(new HttpParameter[colors.size()])
        ));
    }

    private void addParameterToList(List<HttpParameter> colors,
                                    String paramName, String color) {
        if (color != null) {
            colors.add(new HttpParameter(paramName, color));
        }
    }

    @Override
    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        return factory.createUser(post(conf.getRestBaseURL()
                + "account/update_profile_image.json"
                , new HttpParameter[]{new HttpParameter("image", image)}));
    }

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

    @Override
    public PagableResponseList<User> getBlocksList() throws
            TwitterException {
        return getBlocksList(-1L);
    }

    @Override
    public PagableResponseList<User> getBlocksList(long cursor) throws
            TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "blocks/list.json?cursor=" + cursor));
    }

    @Override
    public IDs getBlocksIDs() throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "blocks/ids.json"));
    }

    @Override
    public IDs getBlocksIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "blocks/ids.json?cursor=" + cursor));
    }

    @Override
    public User createBlock(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "blocks/create.json?user_id=" + userId));
    }

    @Override
    public User createBlock(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "blocks/create.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName)
                }
        ));
    }

    @Override
    public User destroyBlock(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "blocks/destroy.json?user_id=" + userId));
    }

    @Override
    public User destroyBlock(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "blocks/destroy.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName),
                }
        ));
    }


    @Override
    public PagableResponseList<User> getMutesList(long cursor) throws
            TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "mutes/users/list.json?cursor=" + cursor));
    }

    @Override
    public IDs getMutesIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(conf.getRestBaseURL() + "mutes/users/ids.json?cursor=" + cursor));
    }

    @Override
    public User createMute(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "mutes/users/create.json?user_id=" + userId));
    }

    @Override
    public User createMute(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "mutes/users/create.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName)
                }
        ));
    }

    @Override
    public User destroyMute(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "mutes/users/destroy.json?user_id=" + userId));
    }

    @Override
    public User destroyMute(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "mutes/users/destroy.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName)
                }
        ));
    }

    @Override
    public ResponseList<User> lookupUsers(long... ids) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/lookup.json"
                , new HttpParameter("user_id", StringUtil.join(ids))));
    }

    @Override
    public ResponseList<User> lookupUsers(String... screenNames) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/lookup.json"
                , new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }

    @Override
    public User showUser(long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() + "users/show.json?user_id=" + userId));
    }

    @Override
    public User showUser(String screenName) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() + "users/show.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName)
                }
        ));
    }

    @Override
    public ResponseList<User> searchUsers(String query, int page) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/search.json"
                , new HttpParameter("q", query), new HttpParameter("per_page", 20)
                , new HttpParameter("page", page)));
    }

    @Override
    public ResponseList<User> getContributees(long userId) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/contributees.json?user_id=" + userId));
    }

    @Override
    public ResponseList<User> getContributees(String screenName) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/contributees.json",
                new HttpParameter("screen_name", screenName)));
    }

    @Override
    public ResponseList<User> getContributors(long userId) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/contributors.json?user_id=" + userId));
    }

    @Override
    public ResponseList<User> getContributors(String screenName) throws TwitterException {
        return factory.createUserList(get(conf.getRestBaseURL() + "users/contributors.json",
                new HttpParameter("screen_name", screenName)));
    }

    @Override
    public void removeProfileBanner() throws TwitterException {
        post(conf.getRestBaseURL()
                + "account/remove_profile_banner.json");
    }

    @Override
    public void updateProfileBanner(File image) throws TwitterException {
        checkFileValidity(image);
        post(conf.getRestBaseURL()
                + "account/update_profile_banner.json"
                , new HttpParameter("banner", image));
    }

    @Override
    public void updateProfileBanner(InputStream image) throws TwitterException {
        post(conf.getRestBaseURL()
                + "account/update_profile_banner.json"
                , new HttpParameter("banner", "banner", image));
    }

    /* Suggested Users Resources */

    @Override
    public ResponseList<User> getUserSuggestions(String categorySlug) throws TwitterException {
        HttpResponse res;
        try {
            res = get(conf.getRestBaseURL() + "users/suggestions/" + URLEncoder.encode(categorySlug, "UTF-8") + ".json");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return factory.createUserListFromJSONArray_Users(res);
    }

    @Override
    public ResponseList<Category> getSuggestedUserCategories() throws TwitterException {
        return factory.createCategoryList(get(conf.getRestBaseURL() + "users/suggestions.json"));
    }

    @Override
    public ResponseList<User> getMemberSuggestions(String categorySlug) throws TwitterException {
        HttpResponse res;
        try {
            res = get(conf.getRestBaseURL() + "users/suggestions/" + URLEncoder.encode(categorySlug, "UTF-8") + "/members.json");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return factory.createUserListFromJSONArray(res);
    }

    /* Favorites Resources */

    @Override
    public ResponseList<Status> getFavorites() throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json"));
    }

    @Override
    public ResponseList<Status> getFavorites(long userId) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json?user_id=" + userId));
    }

    @Override
    public ResponseList<Status> getFavorites(String screenName) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json",
                new HttpParameter("screen_name", screenName)));
    }

    @Override
    public ResponseList<Status> getFavorites(Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json", paging.asPostParameterArray()));
    }

    @Override
    public ResponseList<Status> getFavorites(long userId, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId)}
                        , paging.asPostParameterArray())
        ));
    }

    @Override
    public ResponseList<Status> getFavorites(String screenName, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "favorites/list.json",
                mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName)}
                        , paging.asPostParameterArray())
        ));
    }

    @Override
    public Status destroyFavorite(long id) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "favorites/destroy.json?id=" + id));
    }

    @Override
    public Status createFavorite(long id) throws TwitterException {
        return factory.createStatus(post(conf.getRestBaseURL() + "favorites/create.json?id=" + id));
    }

    /* Lists Resources */

    @Override
    public ResponseList<UserList> getUserLists(String listOwnerScreenName) throws TwitterException {
        return getUserLists(listOwnerScreenName, false);
    }

    @Override
    public ResponseList<UserList> getUserLists(String listOwnerScreenName, boolean reverse) throws TwitterException {
        return factory.createUserListList(get(conf.getRestBaseURL() + "lists/list.json",
                new HttpParameter("screen_name", listOwnerScreenName),
                new HttpParameter("reverse", reverse)));
    }

    @Override
    public ResponseList<UserList> getUserLists(long listOwnerUserId) throws TwitterException {
        return getUserLists(listOwnerUserId, false);
    }

    @Override
    public ResponseList<UserList> getUserLists(long listOwnerUserId, boolean reverse) throws TwitterException {
        return factory.createUserListList(get(conf.getRestBaseURL() + "lists/list.json",
                new HttpParameter("user_id", listOwnerUserId),
                new HttpParameter("reverse", reverse)));
    }

    @Override
    public ResponseList<Status> getUserListStatuses(long listId, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "lists/statuses.json"
                , mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT)
                , new HttpParameter("list_id", listId))));
    }

    @Override
    public ResponseList<Status> getUserListStatuses(long ownerId, String slug, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "lists/statuses.json"
                , mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT)
                , new HttpParameter[]{new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug)})));
    }

    @Override
    public ResponseList<Status> getUserListStatuses(String ownerScreenName,
                                                    String slug, Paging paging) throws TwitterException {
        return factory.createStatusList(get(conf.getRestBaseURL() + "lists/statuses.json"
                , mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT)
                , new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName)
                , new HttpParameter("slug", slug)})));
    }

    @Override
    public UserList destroyUserListMember(long listId, long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                        "lists/members/destroy.json",
                new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("user_id", userId)}
        ));
    }

    @Override
    public UserList destroyUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug), new HttpParameter("user_id", userId)}));
    }

    @Override
    public UserList destroyUserListMember(long listId, String screenName) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy.json", new HttpParameter[]{new HttpParameter("list_id", listId),
                new HttpParameter("screen_name", screenName)}));
    }

    @Override
    public UserList destroyUserListMember(String ownerScreenName, String slug,
                                          long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName)
                , new HttpParameter("slug", slug), new HttpParameter("user_id", userId)}));
    }

    @Override
    public UserList destroyUserListMembers(long listId, String[] screenNames) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy_all.json", new HttpParameter[]{new HttpParameter("list_id", listId),
                new HttpParameter("screen_name", StringUtil.join(screenNames))}));
    }

    @Override
    public UserList destroyUserListMembers(long listId, long[] userIds) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy_all.json", new HttpParameter[]{new HttpParameter("list_id", listId),
                new HttpParameter("user_id", StringUtil.join(userIds))}));
    }

    @Override
    public UserList destroyUserListMembers(String ownerScreenName, String slug, String[] screenNames) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/destroy_all.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName),
                new HttpParameter("slug", slug),
                new HttpParameter("screen_name", StringUtil.join(screenNames))}));
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long cursor) throws TwitterException {
        return getUserListMemberships(20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL() + "lists/memberships.json",
          new HttpParameter("cursor", cursor),
          new HttpParameter("count", count)));
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberScreenName, cursor, false);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, int count, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberScreenName, count, cursor, false);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return getUserListMemberships(listMemberScreenName, 20, cursor, filterToOwnedLists);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, int count, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL()
                + "lists/memberships.json",
          new HttpParameter("screen_name", listMemberScreenName),
          new HttpParameter("count", count),
          new HttpParameter("cursor", cursor),
          new HttpParameter("filter_to_owned_lists", filterToOwnedLists)));
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberId, cursor, false);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, int count, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberId, count, cursor, false);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return getUserListMemberships(listMemberId, 20, cursor, filterToOwnedLists);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, int count, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL()
                + "lists/memberships.json",
                new HttpParameter("user_id", listMemberId),
                new HttpParameter("count", count),
                new HttpParameter("cursor", cursor),
                new HttpParameter("filter_to_owned_lists", filterToOwnedLists)));
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long listId, long cursor) throws TwitterException {
        return getUserListSubscribers(listId, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long listId, int count, long cursor) throws TwitterException {
        return getUserListSubscribers(listId, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long listId, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "lists/subscribers.json",
          new HttpParameter("list_id", listId),
          new HttpParameter("count", count),
          new HttpParameter("cursor", cursor),
          new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, long cursor) throws TwitterException {
        return getUserListSubscribers(ownerId, slug, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, int count, long cursor) throws TwitterException {
        return getUserListSubscribers(ownerId, slug, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "lists/subscribers.json",
          new HttpParameter("owner_id", ownerId),
          new HttpParameter("slug", slug),
          new HttpParameter("count", count),
          new HttpParameter("cursor", cursor),
          new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(String ownerScreenName, String slug, long cursor) throws TwitterException {
        return getUserListSubscribers(ownerScreenName, slug, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(String ownerScreenName, String slug, int count, long cursor) throws TwitterException {
        return getUserListSubscribers(ownerScreenName, slug, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(
            String ownerScreenName, String slug, int count, long cursor, boolean skipStatus)
            throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "lists/subscribers.json",
                new HttpParameter("owner_screen_name", ownerScreenName),
                new HttpParameter("slug", slug),
                new HttpParameter("count", count),
                new HttpParameter("cursor", cursor),
                new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public UserList createUserListSubscription(long listId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    @Override
    public UserList createUserListSubscription(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug)}));
    }


    @Override
    public UserList createUserListSubscription(String ownerScreenName,
                                               String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName)
                , new HttpParameter("slug", slug)}));
    }

    @Override
    public User showUserListSubscription(long listId, long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/subscribers/show.json?list_id=" + listId + "&user_id=" + userId));
    }

    @Override
    public User showUserListSubscription(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/subscribers/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }

    @Override
    public User showUserListSubscription(String ownerScreenName, String slug,
                                         long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/subscribers/show.json",
                new HttpParameter[]{
                        new HttpParameter("owner_screen_name", ownerScreenName),
                        new HttpParameter("slug", slug),
                        new HttpParameter("user_id", userId)
                }
        ));
    }

    @Override
    public UserList destroyUserListSubscription(long listId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/subscribers/destroy.json", new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    @Override
    public UserList destroyUserListSubscription(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/subscribers/destroy.json"
                , new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)}));
    }

    @Override
    public UserList destroyUserListSubscription(String ownerScreenName,
                                                String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/subscribers/destroy.json"
                , new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)}));
    }

    @Override
    public UserList createUserListMembers(long listId, long... userIds) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("user_id"
                        , StringUtil.join(userIds))}
        ));
    }

    @Override
    public UserList createUserListMembers(long ownerId, String slug, long... userIds) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)
                        , new HttpParameter("user_id", StringUtil.join(userIds))}
        ));
    }

    @Override
    public UserList createUserListMembers(String ownerScreenName, String slug,
                                          long... userIds) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)
                        , new HttpParameter("user_id", StringUtil.join(userIds))}
        ));
    }

    @Override
    public UserList createUserListMembers(long listId, String... screenNames) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                        "lists/members/create_all.json",
                new HttpParameter[]{
                        new HttpParameter("list_id", listId),
                        new HttpParameter("screen_name", StringUtil.join(screenNames))}
        ));
    }

    @Override
    public UserList createUserListMembers(long ownerId, String slug, String... screenNames) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                        "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)
                        , new HttpParameter("screen_name", StringUtil.join(screenNames))}
        ));
    }

    @Override
    public UserList createUserListMembers(String ownerScreenName, String slug,
                                          String... screenNames) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                        "lists/members/create_all.json",
                new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)
                        , new HttpParameter("screen_name", StringUtil.join(screenNames))}
        ));
    }

    @Override
    public User showUserListMembership(long listId, long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/members/show.json?list_id=" + listId + "&user_id=" + userId));
    }

    @Override
    public User showUserListMembership(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/members/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }

    @Override
    public User showUserListMembership(String ownerScreenName, String slug,
                                       long userId) throws TwitterException {
        return factory.createUser(get(conf.getRestBaseURL() +
                "lists/members/show.json",
                new HttpParameter[]{
                        new HttpParameter("owner_screen_name", ownerScreenName),
                        new HttpParameter("slug", slug),
                        new HttpParameter("user_id", userId)
                }
        ));
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long listId
            , long cursor) throws TwitterException {
        return getUserListMembers(listId, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long listId, int count, long cursor) throws TwitterException {
        return getUserListMembers(listId, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long listId, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() + "lists/members.json",
          new HttpParameter("list_id", listId),
          new HttpParameter("count", count),
          new HttpParameter("cursor", cursor),
          new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long ownerId, String slug, long cursor) throws TwitterException {
        return getUserListMembers(ownerId, slug, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long ownerId, String slug, int count, long cursor) throws TwitterException {
        return getUserListMembers(ownerId, slug, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long ownerId, String slug, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() +
                "lists/members.json",
          new HttpParameter("owner_id", ownerId),
          new HttpParameter("slug", slug),
          new HttpParameter("count", count),
          new HttpParameter("cursor", cursor),
          new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String ownerScreenName, String slug, long cursor) throws TwitterException {
      return getUserListMembers(ownerScreenName, slug, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String ownerScreenName, String slug, int count, long cursor) throws TwitterException {
      return getUserListMembers(ownerScreenName, slug, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String ownerScreenName,
                                                        String slug, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(conf.getRestBaseURL() +
                "lists/members.json",
                new HttpParameter("owner_screen_name", ownerScreenName),
                new HttpParameter("slug", slug),
                new HttpParameter("count", count),
                new HttpParameter("cursor", cursor),
                new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public UserList createUserListMember(long listId, long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId)
                , new HttpParameter("list_id", listId)}));
    }

    @Override
    public UserList createUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId)
                , new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)}));
    }

    @Override
    public UserList createUserListMember(String ownerScreenName, String slug,
                                         long userId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() +
                "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId)
                , new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)}));
    }

    @Override
    public UserList destroyUserList(long listId) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/destroy.json",
                new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    @Override
    public UserList destroyUserList(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/destroy.json",
                new HttpParameter[]{new HttpParameter("owner_id", ownerId)
                        , new HttpParameter("slug", slug)}
        ));
    }

    @Override
    public UserList destroyUserList(String ownerScreenName, String slug)
            throws TwitterException {
        return factory.createAUserList(post(conf.getRestBaseURL() + "lists/destroy.json",
                new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName)
                        , new HttpParameter("slug", slug)}
        ));
    }

    @Override
    public UserList updateUserList(long listId, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("list_id", listId));
    }

    @Override
    public UserList updateUserList(long ownerId, String slug, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug));
    }

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

    @Override
    public UserList showUserList(long listId) throws TwitterException {
        return factory.createAUserList(get(conf.getRestBaseURL() + "lists/show.json?list_id=" + listId));
    }

    @Override
    public UserList showUserList(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(get(conf.getRestBaseURL() + "lists/show.json?owner_id=" + ownerId + "&slug="
                + slug));
    }

    @Override
    public UserList showUserList(String ownerScreenName, String slug)
            throws TwitterException {
        return factory.createAUserList(get(conf.getRestBaseURL() + "lists/show.json",
                new HttpParameter[]{
                        new HttpParameter("owner_screen_name", ownerScreenName),
                        new HttpParameter("slug", slug)
                }
        ));
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(String listSubscriberScreenName, long cursor) throws TwitterException {
        return getUserListSubscriptions(listSubscriberScreenName, 20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(String listSubscriberScreenName, int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL() + "lists/subscriptions.json",
                          new HttpParameter("screen_name", listSubscriberScreenName)
                        , new HttpParameter("count", count)
                        , new HttpParameter("cursor", cursor)));
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(long listSubscriberId, long cursor) throws TwitterException {
        return getUserListSubscriptions(listSubscriberId, 20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(long listSubscriberId, int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL() + "lists/subscriptions.json",
                          new HttpParameter("user_id", listSubscriberId),
                          new HttpParameter("count", count),
                          new HttpParameter("cursor", cursor)));
    }

    public PagableResponseList<UserList> getUserListsOwnerships(String listOwnerScreenName, long cursor) throws TwitterException {
      return getUserListsOwnerships(listOwnerScreenName, 20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(String listOwnerScreenName, int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL() + "lists/ownerships.json",
                new HttpParameter("screen_name", listOwnerScreenName)
                , new HttpParameter("count", count)
                , new HttpParameter("cursor", cursor)));
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(long listOwnerId, long cursor) throws TwitterException {
      return getUserListsOwnerships(listOwnerId, 20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(long listOwnerId, int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(conf.getRestBaseURL() + "lists/ownerships.json",
                new HttpParameter("user_id", listOwnerId)
                , new HttpParameter("count", count)
                , new HttpParameter("cursor", cursor)));
    }

    /* Saved Searches Resources */

    @Override
    public ResponseList<SavedSearch> getSavedSearches() throws TwitterException {
        return factory.createSavedSearchList(get(conf.getRestBaseURL() + "saved_searches/list.json"));
    }

    @Override
    public SavedSearch showSavedSearch(long id) throws TwitterException {
        return factory.createSavedSearch(get(conf.getRestBaseURL() + "saved_searches/show/" + id
                + ".json"));
    }

    @Override
    public SavedSearch createSavedSearch(String query) throws TwitterException {
        return factory.createSavedSearch(post(conf.getRestBaseURL() + "saved_searches/create.json"
                , new HttpParameter("query", query)));
    }

    @Override
    public SavedSearch destroySavedSearch(long id) throws TwitterException {
        return factory.createSavedSearch(post(conf.getRestBaseURL()
                + "saved_searches/destroy/" + id + ".json"));
    }

    /* Places & Geo Resources */

    @Override
    public Place getGeoDetails(String placeId) throws TwitterException {
        return factory.createPlace(get(conf.getRestBaseURL() + "geo/id/" + placeId
                + ".json"));
    }

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

    @Override
    public ResponseList<Place> searchPlaces(GeoQuery query) throws TwitterException {
        return factory.createPlaceList(get(conf.getRestBaseURL()
                + "geo/search.json", query.asHttpParameterArray()));
    }

    @Override
    public ResponseList<Place> getSimilarPlaces(GeoLocation location, String name, String containedWithin, String streetAddress) throws TwitterException {
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
        return factory.createPlaceList(get(conf.getRestBaseURL()
                + "geo/similar_places.json", params.toArray(new HttpParameter[params.size()])));
    }

    /* Trends Resources */

    @Override
    public Trends getPlaceTrends(int woeid) throws TwitterException {
        return factory.createTrends(get(conf.getRestBaseURL()
                + "trends/place.json?id=" + woeid));
    }

    @Override
    public ResponseList<Location> getAvailableTrends() throws TwitterException {
        return factory.createLocationList(get(conf.getRestBaseURL()
                + "trends/available.json"));
    }

    @Override
    public ResponseList<Location> getClosestTrends(GeoLocation location) throws TwitterException {
        return factory.createLocationList(get(conf.getRestBaseURL()
                        + "trends/closest.json",
                new HttpParameter("lat", location.getLatitude())
                , new HttpParameter("long", location.getLongitude())));
    }

    /* Spam Reporting Resources */

    @Override
    public User reportSpam(long userId) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "users/report_spam.json?user_id=" + userId));
    }

    @Override
    public User reportSpam(String screenName) throws TwitterException {
        return factory.createUser(post(conf.getRestBaseURL() + "users/report_spam.json",
                new HttpParameter[]{
                        new HttpParameter("screen_name", screenName)
                }
        ));
    }

    /* Help Resources */

    @Override
    public TwitterAPIConfiguration getAPIConfiguration() throws TwitterException {
        return factory.createTwitterAPIConfiguration(get(conf.getRestBaseURL() + "help/configuration.json"));
    }

    @Override
    public ResponseList<Language> getLanguages() throws TwitterException {
        return factory.createLanguageList(get(conf.getRestBaseURL() + "help/languages.json"));
    }

    @Override
    public String getPrivacyPolicy() throws TwitterException {
        try {
            return get(conf.getRestBaseURL() + "help/privacy.json").asJSONObject().getString("privacy");
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    @Override
    public String getTermsOfService() throws TwitterException {
        try {
            return get(conf.getRestBaseURL() + "help/tos.json").asJSONObject().getString("tos");
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    @Override
    public Map<String, RateLimitStatus> getRateLimitStatus() throws TwitterException {
        return factory.createRateLimitStatuses(get(conf.getRestBaseURL() + "application/rate_limit_status.json"));
    }

    @Override
    public Map<String, RateLimitStatus> getRateLimitStatus(String... resources) throws TwitterException {
        return factory.createRateLimitStatuses(get(conf.getRestBaseURL() + "application/rate_limit_status.json?resources=" + StringUtil.join(resources)));
    }

    @Override
    public TimelinesResources timelines() {
        return this;
    }

    @Override
    public TweetsResources tweets() {
        return this;
    }

    @Override
    public SearchResource search() {
        return this;
    }

    @Override
    public DirectMessagesResources directMessages() {
        return this;
    }

    @Override
    public FriendsFollowersResources friendsFollowers() {
        return this;
    }

    @Override
    public UsersResources users() {
        return this;
    }

    @Override
    public SuggestedUsersResources suggestedUsers() {
        return this;
    }

    @Override
    public FavoritesResources favorites() {
        return this;
    }

    @Override
    public ListsResources list() {
        return this;
    }

    @Override
    public SavedSearchesResources savedSearches() {
        return this;
    }

    @Override
    public PlacesGeoResources placesGeo() {
        return this;
    }

    @Override
    public TrendsResources trends() {
        return this;
    }

    @Override
    public SpamReportingResource spamReporting() {
        return this;
    }

    @Override
    public HelpResources help() {
        return this;
    }

    private HttpResponse get(String url) throws TwitterException {
        ensureAuthorizationEnabled();
        if (IMPLICIT_PARAMS_STR.length() > 0) {
            if (url.contains("?")) {
                url = url + "&" + IMPLICIT_PARAMS_STR;
            } else {
                url = url + "?" + IMPLICIT_PARAMS_STR;
            }
        }
        if (!conf.isMBeanEnabled()) {
            return http.get(url, null, auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.get(url, null, auth, this);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    private HttpResponse get(String url, HttpParameter... params) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!conf.isMBeanEnabled()) {
            return http.get(url, mergeImplicitParams(params), auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.get(url, mergeImplicitParams(params), auth, this);
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
            return http.post(url, IMPLICIT_PARAMS, auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.post(url, IMPLICIT_PARAMS, auth, this);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    private HttpResponse post(String url, HttpParameter... params) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!conf.isMBeanEnabled()) {
            return http.post(url, mergeImplicitParams(params), auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.post(url, mergeImplicitParams(params), auth, this);
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

    private HttpParameter[] mergeImplicitParams(HttpParameter... params) {
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
