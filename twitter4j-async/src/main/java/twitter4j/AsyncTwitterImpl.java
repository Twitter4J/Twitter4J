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

import twitter4j.api.HelpResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static twitter4j.TwitterMethod.*;

/**
 * Twitter API with a series of asynchronous APIs.<br>
 * With this class, you can call TwitterAPI asynchronously.<br>
 * Note that currently this class is NOT compatible with Google App Engine as it is maintaining threads internally.<br>
 * Currently this class is not carefully designed to be extended. It is suggested to extend this class only for mock testing purporse.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class AsyncTwitterImpl extends TwitterBaseImpl implements AsyncTwitter {
    private static final long serialVersionUID = 6491978434917152443L;
    private static final Logger logger = Logger.getLogger(AsyncTwitterImpl.class);
    private final Twitter twitter;
    private final List<TwitterListener> listeners = new ArrayList<TwitterListener>();

    /*package*/
    AsyncTwitterImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        twitter = new TwitterFactory(conf).getInstance(auth);
    }

    @Override
    public void addListener(TwitterListener listener) {
        this.listeners.add(listener);
    }
    /* Timelines Resources */

    @Override
    public void getMentions() {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS_TIMELINE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getMentionsTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMentions(statuses);
                    } catch (Exception e) { 
                        logger.warn("Exception at getMentions", e);
                    }
                }
            }
        });
    }

    @Override
    public void getMentions(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS_TIMELINE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getMentionsTimeline(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMentions(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getMentions", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserTimeline(final String screenName, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserTimeline(screenName,
                        paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserTimeline", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserTimeline(final long userId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserTimeline(userId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserTimeline", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserTimeline(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserTimeline(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserTimeline", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserTimeline(final String screenName) {
        getUserTimeline(screenName, new Paging());
    }

    @Override
    public void getUserTimeline(final long userId) {
        getUserTimeline(userId, new Paging());
    }

    @Override
    public void getUserTimeline() {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserTimeline", e);
                    }
                }
            }
        });
    }

    @Override
    public void getHomeTimeline() {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getHomeTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotHomeTimeline(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getHomeTimeline", e);
                    }
                }
            }
        });
    }

    @Override
    public void getHomeTimeline(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getHomeTimeline(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotHomeTimeline(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getHomeTimeline", e);
                    }
                }
            }
        });
    }

    @Override
    public void getRetweetsOfMe() {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetsOfMe();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetsOfMe(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getRwtweetsOfMe", e);
                    }
                }
            }
        });
    }

    @Override
    public void getRetweetsOfMe(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetsOfMe(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetsOfMe(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getRetweetsOfMe", e);
                    }
                }
            }
        });
    }

    /* Tweets Resources */

    @Override
    public void getRetweets(final long statusId) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweets(statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweets(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getRetweets", e);
                    }
                }
            }
        });
    }

    @Override
    public void showStatus(final long id) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_STATUS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.showStatus(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowStatus(status);
                    } catch (Exception e) {
                        logger.warn("Exception at showStatus", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyStatus(final long statusId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_STATUS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.destroyStatus(statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedStatus(status);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyStatus", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateStatus(final String statusText) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.updateStatus(statusText);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedStatus(status);
                    } catch (Exception e) {
                        logger.warn("Exception at updatedStatus", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateStatus(final StatusUpdate latestStatus) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.updateStatus(latestStatus);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedStatus(status);
                    } catch (Exception e) {
                        logger.warn("Exception at updateStatus", e);
                    }
                }
            }
        });
    }

    @Override
    public void retweetStatus(final long statusId) {
        getDispatcher().invokeLater(new AsyncTask(RETWEET_STATUS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.retweetStatus(statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.retweetedStatus(status);
                    } catch (Exception e) {
                        logger.warn("Exception at retweetStatus", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOEmbed(final OEmbedRequest req) {
        getDispatcher().invokeLater(new AsyncTask(OEMBED, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                OEmbed oembed = twitter.getOEmbed(req);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOEmbed(oembed);
                    } catch (Exception e) {
                        logger.warn("Exception at getOEmbed", e);
                    }
                }
            }
        });
    }

    @Override
    public void lookup(final long... ids) {
        getDispatcher().invokeLater(new AsyncTask(RETWEET_STATUS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.lookup(ids);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedup(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at lookup", e);
                    }
                }
            }
        });
    }

    /* Search Resources */

    @Override
    public void search(final Query query) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                QueryResult result = twitter.search(query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searched(result);
                    } catch (Exception e) {
                        logger.warn("Exception at search", e);
                    }
                }
            }
        });
    }

    /* Direct Messages Resources */

    @Override
    public void showDirectMessage(final long id) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = twitter.showDirectMessage(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDirectMessage(directMessage);
                    } catch (Exception e) {
                        logger.warn("Exception at showDirectMessage", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyDirectMessage(final long id) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_DIRECT_MESSAGE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                twitter.destroyDirectMessage(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedDirectMessage(id);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyDirectMessage", e);
                    }
                }
            }
        });
    }

    @Override
    public void sendDirectMessage(final long userId, final String text) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = twitter.sendDirectMessage(userId, text);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.sentDirectMessage(directMessage);
                    } catch (Exception e) {
                        logger.warn("Exception at sendDirectMessage", e);
                    }
                }
            }
        });
    }

    @Override
    public void sendDirectMessage(final String screenName, final String text) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = twitter.sendDirectMessage(screenName, text);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.sentDirectMessage(directMessage);
                    } catch (Exception e) {
                        logger.warn("Exception at sendDirectMessage", e);
                    }
                }
            }
        });
    }

    /* Friends & Followers Resources */

    @Override
    public void getFriendsIDs(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                IDs ids = twitter.getFriendsIDs(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getFriendsIDs", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFriendsIDs(final long userId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getFriendsIDs(userId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getFriendsIDs", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFriendsIDs(final String screenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                IDs ids = twitter.getFriendsIDs(screenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getFriendsIDs", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFollowersIDs(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getFollowersIDs(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getFollowersIDs", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFollowersIDs(final long userId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getFollowersIDs(userId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getFollowersIDs", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFollowersIDs(final String screenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getFollowersIDs(screenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getFollowersIDs", e);
                    }
                }
            }
        });
    }


    @Override
    public void lookupFriendships(final long... ids) {
        getDispatcher().invokeLater(new AsyncTask(LOOKUP_FRIENDSHIPS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Friendship> friendships = twitter.lookupFriendships(ids);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedUpFriendships(friendships);
                    } catch (Exception e) {
                        logger.warn("Exception at lookupFriendships", e);
                    }
                }
            }
        });
    }

    @Override
    public void lookupFriendships(final String... screenNames) {
        getDispatcher().invokeLater(new AsyncTask(LOOKUP_FRIENDSHIPS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Friendship> friendships = twitter.lookupFriendships(screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedUpFriendships(friendships);
                    } catch (Exception e) {
                        logger.warn("Exception at lookupFriendships", e);
                    }
                }
            }
        });
    }

    @Override
    public void getIncomingFriendships(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(INCOMING_FRIENDSHIPS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getIncomingFriendships(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotIncomingFriendships(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getIncomingFriendships", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOutgoingFriendships(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(OUTGOING_FRIENDSHIPS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getOutgoingFriendships(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOutgoingFriendships(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getOutgoingFriendships", e);
                    }
                }
            }
        });
    }

    @Override
    public void createFriendship(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createFriendship(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception e) {
                        logger.warn("Exception at createdFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void createFriendship(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createFriendship(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception e) {
                        logger.warn("Exception at createdFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void createFriendship(final long userId, final boolean follow) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createFriendship(userId, follow);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception e) {
                        logger.warn("Exception at createdFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void createFriendship(final String screenName, final boolean follow) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createFriendship(screenName, follow);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception e) {
                        logger.warn("Exception at createdFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyFriendship(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyFriendship(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFriendship(user);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyFriendship(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyFriendship(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFriendship(user);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateFriendship(final long userId
            , final boolean enableDeviceNotification, final boolean retweet) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = twitter.updateFriendship(userId
                        , enableDeviceNotification, retweet);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedFriendship(relationship);
                    } catch (Exception e) {
                        logger.warn("Exception at updateFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateFriendship(final String screenName
            , final boolean enableDeviceNotification, final boolean retweet) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = twitter.updateFriendship(screenName
                        , enableDeviceNotification, retweet);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedFriendship(relationship);
                    } catch (Exception e) {
                        logger.warn("Exception at updateFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void showFriendship(final long sourceId, final long targetId) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = twitter.showFriendship(sourceId, targetId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowFriendship(relationship);
                    } catch (Exception e) {
                        logger.warn("Exception at showFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void showFriendship(final String sourceScreenName, final String targetScreenName) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = twitter.showFriendship(sourceScreenName, targetScreenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowFriendship(relationship);
                    } catch (Exception e) {
                        logger.warn("Exception at showFriendship", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFriendsList(final long userId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFriendsList(userId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsList(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getFriendsList", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFriendsList(final String screenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFriendsList(screenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsList(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getFriendsList", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFollowersList(final long userId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFollowersList(userId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersList(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getFollowersList", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFollowersList(final String screenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFollowersList(screenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersList(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getFollowersList", e);
                    }
                }
            }
        });
    }

    /* Users Resources */

    @Override
    public void getAccountSettings() {
        getDispatcher().invokeLater(new AsyncTask(ACCOUNT_SETTINGS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccountSettings accountSettings = twitter.getAccountSettings();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAccountSettings(accountSettings);
                    } catch (Exception e) {
                        logger.warn("Exception at getAccountSettings", e);
                    }
                }
            }
        });
    }

    @Override
    public void verifyCredentials() {
        getDispatcher().invokeLater(new AsyncTask(VERIFY_CREDENTIALS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.verifyCredentials();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.verifiedCredentials(user);
                    } catch (Exception e) {
                        logger.warn("Exception at verifyCredentials", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateAccountSettings(final Integer trend_locationWoeid, final Boolean sleep_timeEnabled, final String start_sleepTime, final String end_sleepTime, final String time_zone, final String lang) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_ACCOUNT_SETTINGS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccountSettings accountSettings = twitter.updateAccountSettings(trend_locationWoeid, sleep_timeEnabled, start_sleepTime, end_sleepTime, time_zone, lang);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedAccountSettings(accountSettings);
                    } catch (Exception e) {
                        logger.warn("Exception at updateAccountSettings", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateProfile(final String name, final String url
            , final String location, final String description) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.updateProfile(name, url, location, description);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfile(user);
                    } catch (Exception e) {
                        logger.warn("Exception at updateProfile", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateProfileImage(final File image) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_IMAGE,
                listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                User user = twitter.updateProfileImage(image);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileImage(user);
                    } catch (Exception e) {
                        logger.warn("Exception at updateProfileImage", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateProfileImage(final InputStream image) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_IMAGE,
                listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                User user = twitter.updateProfileImage(image);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileImage(user);
                    } catch (Exception e) {
                        logger.warn("Exception at updateProfileImage", e);
                    }
                }
            }
        });
    }

    @Override
    public void getBlocksList() {
        getDispatcher().invokeLater(new AsyncTask(BLOCK_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getBlocksList();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlocksList(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getBlocksList", e);
                    }
                }
            }
        });
    }

    @Override
    public void getBlocksList(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(BLOCK_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getBlocksList(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlocksList(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getBlocksList", e);
                    }
                }
            }
        });
    }

    @Override
    public void getBlocksIDs() {
        getDispatcher().invokeLater(new AsyncTask(BLOCK_LIST_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getBlocksIDs();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlockIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getBlocksIDs", e);
                    }
                }
            }
        });
    }

    @Override
    public void getBlocksIDs(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(BLOCK_LIST_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getBlocksIDs(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlockIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getBlocksIDs", e);
                    }
                }
            }
        });
    }

    @Override
    public void createBlock(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createBlock(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdBlock(user);
                    } catch (Exception e) {
                        logger.warn("Exception at createBlock", e);
                    }
                }
            }
        });
    }

    @Override
    public void createBlock(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createBlock(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdBlock(user);
                    } catch (Exception e) {
                        logger.warn("Exception at createBlock", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyBlock(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyBlock(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedBlock(user);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyBlock", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyBlock(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyBlock(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedBlock(user);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyBlock", e);
                    }
                }
            }
        });
    }

    @Override
    public void getMutesList(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(MUTE_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getMutesList(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMutesList(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getMutesList", e);
                    }
                }
            }
        });
    }

    @Override
    public void getMutesIDs(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(MUTE_LIST_IDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getMutesIDs(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMuteIDs(ids);
                    } catch (Exception e) {
                        logger.warn("Exception at getMutesIDs", e);
                    }
                }
            }
        });
    }

    @Override
    public void createMute(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_MUTE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createMute(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdMute(user);
                    } catch (Exception e) {
                        logger.warn("Exception at createdMute", e);
                    }
                }
            }
        });
    }

    @Override
    public void createMute(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_MUTE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createMute(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdMute(user);
                    } catch (Exception e) {
                        logger.warn("Exception at createdMute", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyMute(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_MUTE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyMute(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedMute(user);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyMute", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyMute(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_MUTE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyMute(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedMute(user);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyMute", e);
                    }
                }
            }
        });
    }

    @Override
    public void lookupUsers(final long... ids) {
        getDispatcher().invokeLater(new AsyncTask(LOOKUP_USERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.lookupUsers(ids);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedupUsers(users);
                    } catch (Exception e) {
                        logger.warn("Exception at lookupUsers", e);
                    }
                }
            }
        });
    }

    @Override
    public void lookupUsers(final String... screenNames) {
        getDispatcher().invokeLater(new AsyncTask(LOOKUP_USERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.lookupUsers(screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedupUsers(users);
                    } catch (Exception e) {
                        logger.warn("Exception at lookupUsers", e);
                    }
                }
            }
        });
    }

    @Override
    public void showUser(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUser(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserDetail(user);
                    } catch (Exception e) {
                        logger.warn("Exception at showUser", e);
                    }
                }
            }
        });
    }

    @Override
    public void showUser(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUser(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserDetail(user);
                    } catch (Exception e) {
                        logger.warn("Exception at showUser", e);
                    }
                }
            }
        });
    }

    @Override
    public void searchUsers(final String query, final int page) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH_USERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.searchUsers(query, page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searchedUser(users);
                    } catch (Exception e) {
                        logger.warn("Exception at searchUsers", e);
                    }
                }
            }
        });
    }

    @Override
    public void getContributees(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CONTRIBUTEEES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getContributors(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotContributees(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getContributees", e);
                    }
                }
            }
        });
    }

    @Override
    public void getContributees(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CONTRIBUTEEES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getContributors(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotContributees(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getContributees", e);
                    }
                }
            }
        });
    }

    @Override
    public void getContributors(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CONTRIBUTORS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getContributors(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotContributors(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getContributors", e);
                    }
                }
            }
        });
    }

    @Override
    public void getContributors(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CONTRIBUTORS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getContributors(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotContributors(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getContributors", e);
                    }
                }
            }
        });
    }

    @Override
    public void removeProfileBanner() {
        getDispatcher().invokeLater(new AsyncTask(REMOVE_PROFILE_BANNER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                twitter.removeProfileBanner();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.removedProfileBanner();
                    } catch (Exception e) {
                        logger.warn("Exception at removeProfileBanner", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateProfileBanner(final File image) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_BANNER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                twitter.updateProfileBanner(image);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileBanner();
                    } catch (Exception e) {
                        logger.warn("Exception at updateProfileBanner", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateProfileBanner(final InputStream image) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_BANNER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                twitter.updateProfileBanner(image);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileBanner();
                    } catch (Exception e) {
                        logger.warn("Exception at updateProfileBanner", e);
                    }
                }
            }
        });
    }

    /* Suggested Users Resources */

    @Override
    public void getUserSuggestions(final String categorySlug) {
        getDispatcher().invokeLater(new AsyncTask(USER_SUGGESTIONS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getUserSuggestions(categorySlug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserSuggestions(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserSuggestions", e);
                    }
                }
            }
        });
    }

    @Override
    public void getSuggestedUserCategories() {
        getDispatcher().invokeLater(new AsyncTask(SUGGESTED_USER_CATEGORIES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Category> categories = twitter.getSuggestedUserCategories();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSuggestedUserCategories(categories);
                    } catch (Exception e) {
                        logger.warn("Exception at getSuggestedUserCategories", e);
                    }
                }
            }
        });
    }

    @Override
    public void getMemberSuggestions(final String categorySlug) {
        getDispatcher().invokeLater(new AsyncTask(MEMBER_SUGGESTIONS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getMemberSuggestions(categorySlug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMemberSuggestions(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getMemberSuggestions", e);
                    }
                }
            }
        });
    }

    /* Favorites Resources */

    @Override
    public void getFavorites() {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getFavorites", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFavorites(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getFavorites", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFavorites(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getFavorites", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFavorites(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getFavorites", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFavorites(final long userId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(userId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getFavorites", e);
                    }
                }
            }
        });
    }

    @Override
    public void getFavorites(final String screenName, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(screenName, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getFavorites", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyFavorite(final long id) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FAVORITE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.destroyFavorite(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFavorite(status);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyFavorite", e);
                    }
                }
            }
        });
    }

    @Override
    public void createFavorite(final long id) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FAVORITE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.createFavorite(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFavorite(status);
                    } catch (Exception e) {
                        logger.warn("Exception at createFavorite", e);
                    }
                }
            }
        });
    }

    /* Lists Resources */

    @Override
    public void getUserLists(final long listOwnerUserId) {
        getDispatcher().invokeLater(new AsyncTask(USER_LISTS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<UserList> lists = twitter.getUserLists(listOwnerUserId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserLists(lists);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserLists", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserLists(final String listOwnerScreenName) {
        getDispatcher().invokeLater(new AsyncTask(USER_LISTS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<UserList> lists = twitter.getUserLists(listOwnerScreenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserLists(lists);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserLists", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListStatuses(final long listId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_STATUSES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserListStatuses(listId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListStatuses(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListStatuses", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListStatuses(final long ownerId, final String slug, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_STATUSES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserListStatuses(ownerId, slug, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListStatuses(statuses);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListStatuses", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyUserListMember(final long listId, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_LIST_MEMBER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.destroyUserListMember(listId, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedUserListMember(list);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyUserListMember", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyUserListMember(final long ownerId, final String slug, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_LIST_MEMBER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.destroyUserListMember(ownerId, slug, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedUserListMember(list);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyUserListMember", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListMemberships(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_MEMBERSHIPS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserListMemberships(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMemberships(lists);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListMemberships", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListMemberships(final String listMemberScreenName, final long cursor) {
        getUserListMemberships(listMemberScreenName, cursor, false);
    }

    @Override
    public void getUserListMemberships(final long listMemberId, final long cursor) {
        getUserListMemberships(listMemberId, cursor, false);
    }

    @Override
    public void getUserListMemberships(final String listMemberScreenName, final long cursor, final boolean filterToOwnedLists) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_MEMBERSHIPS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserListMemberships(listMemberScreenName, cursor, filterToOwnedLists);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMemberships(lists);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListMemberships", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListMemberships(final long listMemberId, final long cursor, final boolean filterToOwnedLists) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_MEMBERSHIPS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserListMemberships(listMemberId, cursor, filterToOwnedLists);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMemberships(lists);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListMemberships", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListSubscribers(final long listId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(LIST_SUBSCRIBERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getUserListSubscribers(listId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListSubscribers(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListSubscribers", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListSubscribers(final long ownerId, final String slug, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(LIST_SUBSCRIBERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getUserListSubscribers(ownerId, slug, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListSubscribers(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListSubscribers", e);
                    }
                }
            }
        });
    }

    @Override
    public void createUserListSubscription(final long listId) {
        getDispatcher().invokeLater(new AsyncTask(SUBSCRIBE_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListSubscription(listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.subscribedUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createUserListSubscription", e);
                    }
                }
            }
        });
    }

    @Override
    public void createUserListSubscription(final long ownerId, final String slug) {
        getDispatcher().invokeLater(new AsyncTask(SUBSCRIBE_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListSubscription(ownerId, slug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.subscribedUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createUserListSubscription", e);
                    }
                }
            }
        });
    }

    @Override
    public void showUserListSubscription(final long listId, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CHECK_LIST_SUBSCRIPTION, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUserListSubscription(listId, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.checkedUserListSubscription(user);
                    } catch (Exception e) {
                        logger.warn("Exception at showUserListSubscription", e);
                    }
                }
            }
        });
    }

    @Override
    public void showUserListSubscription(final long ownerId, final String slug, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CHECK_LIST_SUBSCRIPTION, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUserListSubscription(ownerId, slug, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.checkedUserListSubscription(user);
                    } catch (Exception e) {
                        logger.warn("Exception at showUserListSubscription", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyUserListSubscription(final long listId) {
        getDispatcher().invokeLater(new AsyncTask(UNSUBSCRIBE_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.destroyUserListSubscription(listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.unsubscribedUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyUserListSubscription", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyUserListSubscription(final long ownerId, final String slug) {
        getDispatcher().invokeLater(new AsyncTask(UNSUBSCRIBE_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.destroyUserListSubscription(ownerId, slug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.unsubscribedUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyUserListSubscription", e);
                    }
                }
            }
        });
    }


    @Override
    public void createUserListMembers(final long listId, final long... userIds) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_LIST_MEMBERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListMembers(listId, userIds);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserListMembers(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createdUserListMembers", e);
                    }
                }
            }
        });
    }

    @Override
    public void createUserListMembers(final long ownerId, final String slug, final long... userIds) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_LIST_MEMBERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListMembers(ownerId, slug, userIds);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserListMembers(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createUserListMembers", e);
                    }
                }
            }
        });
    }

    @Override
    public void createUserListMembers(final long listId, final String... screenNames) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_LIST_MEMBERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListMembers(listId, screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserListMembers(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createUserListMembers", e);
                    }
                }
            }
        });
    }

    @Override
    public void createUserListMembers(final long ownerId, final String slug, final String... screenNames) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_LIST_MEMBERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListMembers(ownerId, slug, screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserListMembers(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createUserListMembers", e);
                    }
                }
            }
        });
    }

    @Override
    public void showUserListMembership(final long listId, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CHECK_LIST_MEMBERSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUserListMembership(listId, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.checkedUserListMembership(user);
                    } catch (Exception e) {
                        logger.warn("Exception at showUserListMembership", e);
                    }
                }
            }
        });
    }

    @Override
    public void showUserListMembership(final long ownerId, final String slug, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CHECK_LIST_MEMBERSHIP, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUserListMembership(ownerId, slug, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.checkedUserListMembership(user);
                    } catch (Exception e) {
                        logger.warn("Exception at showUserListMembership", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListMembers(final long listId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(LIST_MEMBERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getUserListMembers(listId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMembers(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListMembers", e);
                    }
                }
            }
        });
    }

    @Override
    public void getUserListMembers(final long ownerId, final String slug, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(LIST_MEMBERS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getUserListMembers(ownerId, slug, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMembers(users);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListMembers", e);
                    }
                }
            }
        });
    }

    @Override
    public void createUserListMember(final long listId, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_LIST_MEMBER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListMember(listId, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserListMember(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createUserListMember", e);
                    }
                }
            }
        });
    }

    @Override
    public void createUserListMember(final long ownerId, final String slug, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_LIST_MEMBER, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListMember(ownerId, slug, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserListMember(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createUserListMember", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyUserList(final long listId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_USER_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.destroyUserList(listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyUserList", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroyUserList(final long ownerId, final String slug) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_USER_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.destroyUserList(ownerId, slug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at destroyUserList", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateUserList(final long listId, final String newListName, final boolean isPublicList, final String newDescription) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_USER_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.updateUserList(listId, newListName, isPublicList, newDescription);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at updateUserList", e);
                    }
                }
            }
        });
    }

    @Override
    public void updateUserList(final long ownerId, final String slug, final String newListName, final boolean isPublicList, final String newDescription) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_USER_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.updateUserList(ownerId, slug, newListName, isPublicList, newDescription);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at updateUserList", e);
                    }
                }
            }
        });
    }

    @Override
    public void createUserList(final String listName, final boolean isPublicList, final String description) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_USER_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserList(listName, isPublicList, description);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at createUserList", e);
                    }
                }
            }
        });
    }

    @Override
    public void showUserList(final long listId) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.showUserList(listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at showUserList", e);
                    }
                }
            }
        });
    }

    @Override
    public void showUserList(final long ownerId, final String slug) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER_LIST, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.showUserList(ownerId, slug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowUserList(list);
                    } catch (Exception e) {
                        logger.warn("Exception at showUserList", e);
                    }
                }
            }
        });
    }


    @Override
    public void getUserListSubscriptions(final String listOwnerScreenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_SUBSCRIPTIONS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserListSubscriptions(listOwnerScreenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListSubscriptions(lists);
                    } catch (Exception e) {
                        logger.warn("Exception at getUserListSubscriptions", e);
                    }
                }
            }
        });
    }



    /* Saved Searches Resources */

    @Override
    public void getSavedSearches() {
        getDispatcher().invokeLater(new AsyncTask(SAVED_SEARCHES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<SavedSearch> savedSearches = twitter.getSavedSearches();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSavedSearches(savedSearches);
                    } catch (Exception e) {
                        logger.warn("Exception at getSavedSearches", e);
                    }
                }
            }
        });
    }

    @Override
    public void showSavedSearch(final int id) {
        getDispatcher().invokeLater(new AsyncTask(SAVED_SEARCH, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                SavedSearch savedSearch = twitter.showSavedSearch(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSavedSearch(savedSearch);
                    } catch (Exception e) {
                        logger.warn("Exception at showSavedSearch", e);
                    }
                }
            }
        });
    }

    @Override
    public void createSavedSearch(final String query) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_SAVED_SEARCH, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                SavedSearch savedSearch = twitter.createSavedSearch(query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdSavedSearch(savedSearch);
                    } catch (Exception e) {
                        logger.warn("Exception at createSavedSearch", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroySavedSearch(final int id) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_SAVED_SEARCH, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                SavedSearch savedSearch = twitter.destroySavedSearch(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedSavedSearch(savedSearch);
                    } catch (Exception e) {
                        logger.warn("Exception at destroySavedSearch", e);
                    }
                }
            }
        });
    }

    /* Places & Geo Resources */

    @Override
    public void getGeoDetails(final String id) {
        getDispatcher().invokeLater(new AsyncTask(GEO_DETAILS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Place place = twitter.getGeoDetails(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotGeoDetails(place);
                    } catch (Exception e) {
                        logger.warn("Exception at getGeoDetails", e);
                    }
                }
            }
        });
    }

    @Override
    public void reverseGeoCode(final GeoQuery query) {
        getDispatcher().invokeLater(new AsyncTask(REVERSE_GEO_CODE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Place> places = twitter.reverseGeoCode(query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotReverseGeoCode(places);
                    } catch (Exception e) {
                        logger.warn("Exception at reverseGeoCode", e);
                    }
                }
            }
        });
    }

    @Override
    public void searchPlaces(final GeoQuery query) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH_PLACES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Place> places = twitter.searchPlaces(query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searchedPlaces(places);
                    } catch (Exception e) {
                        logger.warn("Exception at searchPlaces", e);
                    }
                }
            }
        });
    }

    @Override
    public void getSimilarPlaces(final GeoLocation location, final String name, final String containedWithin
            , final String streetAddress) {
        getDispatcher().invokeLater(new AsyncTask(SIMILAR_PLACES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Place> similarPlaces = twitter.getSimilarPlaces(location, name, containedWithin, streetAddress);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSimilarPlaces(similarPlaces);
                    } catch (Exception e) {
                        logger.warn("Exception at getSimilarPlaces", e);
                    }
                }
            }
        });
    }

    /* Trends Resources */

    @Override
    public void getPlaceTrends(final int woeid) {
        getDispatcher().invokeLater(new AsyncTask(PLACE_TRENDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Trends trends = twitter.getPlaceTrends(woeid);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotPlaceTrends(trends);
                    } catch (Exception e) {
                        logger.warn("Exception at getPlaceTrends", e);
                    }
                }
            }
        });
    }

    @Override
    public void getAvailableTrends() {
        getDispatcher().invokeLater(new AsyncTask(AVAILABLE_TRENDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Location> locations = twitter.getAvailableTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAvailableTrends(locations);
                    } catch (Exception e) {
                        logger.warn("Exception at getAvailableTrends", e);
                    }
                }
            }
        });
    }

    @Override
    public void getClosestTrends(final GeoLocation location) {
        getDispatcher().invokeLater(new AsyncTask(CLOSEST_TRENDS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Location> locations = twitter.getClosestTrends(location);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotClosestTrends(locations);
                    } catch (Exception e) {
                        logger.warn("Exception at getClosestTrends", e);
                    }
                }
            }
        });
    }

    /* Spam Reporting Resources */

    @Override
    public void reportSpam(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(REPORT_SPAM, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.reportSpam(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.reportedSpam(user);
                    } catch (Exception e) {
                        logger.warn("Exception at reportSpam", e);
                    }
                }
            }
        });
    }

    @Override
    public void reportSpam(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(REPORT_SPAM, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.reportSpam(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.reportedSpam(user);
                    } catch (Exception e) {
                        logger.warn("Exception at reportSpam", e);
                    }
                }
            }
        });
    }

    /* Help Resources */

    @Override
    public void getAPIConfiguration() {
        getDispatcher().invokeLater(new AsyncTask(CONFIGURATION, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                TwitterAPIConfiguration apiConf = twitter.getAPIConfiguration();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAPIConfiguration(apiConf);
                    } catch (Exception e) {
                        logger.warn("Exception at getAPIConfiguration", e);
                    }
                }
            }
        });
    }

    @Override
    public void getLanguages() {
        getDispatcher().invokeLater(new AsyncTask(LANGUAGES, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<HelpResources.Language> languages = twitter.getLanguages();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotLanguages(languages);
                    } catch (Exception e) {
                        logger.warn("Exception at getLanguages", e);
                    }
                }
            }
        });
    }

    @Override
    public void getPrivacyPolicy() {
        getDispatcher().invokeLater(new AsyncTask(PRIVACY_POLICY, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                String privacyPolicy = twitter.getPrivacyPolicy();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotPrivacyPolicy(privacyPolicy);
                    } catch (Exception e) {
                        logger.warn("Exception at getPrivacyPolicy", e);
                    }
                }
            }
        });
    }

    @Override
    public void getTermsOfService() {
        getDispatcher().invokeLater(new AsyncTask(TERMS_OF_SERVICE, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                String tos = twitter.getTermsOfService();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotTermsOfService(tos);
                    } catch (Exception e) {
                        logger.warn("Exception at getTermsOfService", e);
                    }
                }
            }
        });
    }

    @Override
    public void getRateLimitStatus() {
        getDispatcher().invokeLater(new AsyncTask(RATE_LIMIT_STATUS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRateLimitStatus(rateLimitStatus);
                    } catch (Exception e) {
                        logger.warn("Exception at getRateLimitStatus", e);
                    }
                }
            }
        });
    }

    @Override
    public void getRateLimitStatus(final String... resources) {
        getDispatcher().invokeLater(new AsyncTask(RATE_LIMIT_STATUS, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus(resources);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRateLimitStatus(rateLimitStatus);
                    } catch (Exception e) {
                        logger.warn("Exception at getRateLimitStatus", e);
                    }
                }
            }
        });
    }

    // implementation for AsyncOAuthSupport

    @Override
    public void getOAuthRequestTokenAsync() {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_REQUEST_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                RequestToken token = twitter.getOAuthRequestToken();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthRequestToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthRequestTokenAsync", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOAuthRequestTokenAsync(final String callbackURL) {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_REQUEST_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                RequestToken token = twitter.getOAuthRequestToken(callbackURL);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthRequestToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthRequestTokenAsync", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOAuthRequestTokenAsync(final String callbackURL, final String xAuthAccessType) {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_REQUEST_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                RequestToken token = twitter.getOAuthRequestToken(callbackURL, xAuthAccessType);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthRequestToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthRequestTokenAsync", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOAuthRequestTokenAsync(final String callbackURL, final String xAuthAccessType, final String xAuthMode) {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_REQUEST_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                RequestToken token = twitter.getOAuthRequestToken(callbackURL, xAuthAccessType, xAuthMode);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthRequestToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthRequestTokenAsync", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOAuthAccessTokenAsync() {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_ACCESS_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccessToken token = twitter.getOAuthAccessToken();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthAccessToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthRequestTokenAsync", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOAuthAccessTokenAsync(final String oauthVerifier) {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_ACCESS_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccessToken token = twitter.getOAuthAccessToken(oauthVerifier);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthAccessToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthRequestTokenAsync", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOAuthAccessTokenAsync(final RequestToken requestToken) {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_ACCESS_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccessToken token = twitter.getOAuthAccessToken(requestToken);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthAccessToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthRequestTokenAsync", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOAuthAccessTokenAsync(final RequestToken requestToken, final String oauthVerifier) {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_ACCESS_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccessToken token = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthAccessToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthAccessTokenAsync", e);
                    }
                }
            }
        });
    }

    @Override
    public void getOAuthAccessTokenAsync(final String screenName, final String password) {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_ACCESS_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccessToken token = twitter.getOAuthAccessToken(screenName, password);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuthAccessToken(token);
                    } catch (Exception e) {
                        logger.warn("Exception at getOAuthAccessTokenAsync", e);
                    }
                }
            }
        });
    }

    private static transient volatile Dispatcher dispatcher;

    @Override
    public void shutdown() {
        synchronized (AsyncTwitterImpl.class) {
            if (dispatcher != null) {
                dispatcher.shutdown();
                dispatcher = null;
            }
        }
    }

    private Dispatcher getDispatcher() {
        if (null == AsyncTwitterImpl.dispatcher) {
            synchronized (AsyncTwitterImpl.class) {
                if (null == AsyncTwitterImpl.dispatcher) {
                    // dispatcher is held statically, but it'll be instantiated with
                    // the configuration instance associated with this TwitterStream
                    // instance which invokes getDispatcher() on the first time.
                    AsyncTwitterImpl.dispatcher = new DispatcherFactory(conf).getInstance();
                }
            }
        }
        return AsyncTwitterImpl.dispatcher;
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

    /**
     * {@inheritDoc}
     * Basic authenticated instance of this class will try acquiring an AccessToken using xAuth.<br>
     * In order to get access acquire AccessToken using xAuth, you must apply by sending an email to <a href="mailto:api@twitter.com">api@twitter.com</a> all other applications will receive an HTTP 401 error.  Web-based applications will not be granted access, except on a temporary basis for when they are converting from basic-authentication support to full OAuth support.<br>
     * Storage of Twitter usernames and passwords is forbidden. By using xAuth, you are required to store only access tokens and access token secrets. If the access token expires or is expunged by a user, you must ask for their login and password again before exchanging the credentials for an access token.
     *
     * @throws TwitterException When Twitter service or network is unavailable, when the user has not authorized, or when the client application is not permitted to use xAuth
     * @see <a href="https://dev.twitter.com/docs/oauth/xauth">xAuth | Twitter Developers</a>
     */
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
    public AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        return twitter.getOAuthAccessToken(screenName, password);
    }

    @Override
    public synchronized OAuth2Token getOAuth2Token() throws TwitterException {
        return twitter.getOAuth2Token();
    }

    @Override
    public void setOAuth2Token(OAuth2Token oauth2Token) {
        twitter.setOAuth2Token(oauth2Token);
    }

    @Override
    public void getOAuth2TokenAsync() {
        getDispatcher().invokeLater(new AsyncTask(OAUTH_ACCESS_TOKEN, listeners) {
            @Override
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                OAuth2Token token = twitter.getOAuth2Token();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOAuth2Token(token);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override
    public synchronized void invalidateOAuth2Token() throws TwitterException {
        twitter.invalidateOAuth2Token();
    }


    abstract class AsyncTask implements Runnable {
        final List<TwitterListener> listeners;
        final TwitterMethod method;

        AsyncTask(TwitterMethod method, List<TwitterListener> listeners) {
            this.method = method;
            this.listeners = listeners;
        }

        abstract void invoke(List<TwitterListener> listeners) throws TwitterException;

        @Override
        public void run() {
            try {
                invoke(listeners);
            } catch (TwitterException te) {
                if (listeners != null) {
                    for (TwitterListener listener : listeners) {
                        try {
                            listener.onException(te, method);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AsyncTwitterImpl that = (AsyncTwitterImpl) o;

        if (listeners != null ? !listeners.equals(that.listeners) : that.listeners != null)
            return false;
        if (twitter != null ? !twitter.equals(that.twitter) : that.twitter != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (twitter != null ? twitter.hashCode() : 0);
        result = 31 * result + (listeners != null ? listeners.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AsyncTwitterImpl{" +
                "twitter=" + twitter +
                ", listeners=" + listeners +
                '}';
    }
}
