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

import twitter4j.api.HelpMethods;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.async.DispatcherFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static twitter4j.TwitterMethod.*;

/**
 * Twitter API with a series of asynchronous APIs.<br>
 * With this class, you can call TwitterAPI asynchronously.<br>
 * Note that currently this class is NOT compatible with Google App Engine as it is maintaining threads internally.<br>
 * Currently this class is not carefully designed to be extended. It is suggested to extend this class only for mock testing purporse.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterListener
 */
class AsyncTwitterImpl extends TwitterBaseImpl
        implements AsyncTwitter {
    private static final long serialVersionUID = -2008667933225051907L;
    private final Twitter twitter;
    private final List<TwitterListener> listeners = new ArrayList<TwitterListener>();

    /*package*/
    AsyncTwitterImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        twitter = new TwitterFactory(conf).getInstance(auth);
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(TwitterListener listener) {
        this.listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void search(final Query query) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                QueryResult result = twitter.search(query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searched(result);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDailyTrends() {
        getDispatcher().invokeLater(new AsyncTask(DAILY_TRENDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws
                    TwitterException {
                ResponseList<Trends> trendsList = twitter.getDailyTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDailyTrends(trendsList);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDailyTrends(final Date date, final boolean excludeHashTags) {
        getDispatcher().invokeLater(new AsyncTask(DAILY_TRENDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws
                    TwitterException {
                ResponseList<Trends> trendsList = twitter.getDailyTrends(date, excludeHashTags);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDailyTrends(trendsList);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getWeeklyTrends() {
        getDispatcher().invokeLater(new AsyncTask(WEEKLY_TRENDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws
                    TwitterException {
                ResponseList<Trends> trendsList = twitter.getWeeklyTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotWeeklyTrends(trendsList);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getWeeklyTrends(final Date date, final boolean excludeHashTags) {
        getDispatcher().invokeLater(new AsyncTask(WEEKLY_TRENDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws
                    TwitterException {
                ResponseList<Trends> trendsList = twitter.getWeeklyTrends(date, excludeHashTags);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotWeeklyTrends(trendsList);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getPublicTimeline() {
        getDispatcher().invokeLater(new AsyncTask(PUBLIC_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getPublicTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotPublicTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getHomeTimeline() {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getHomeTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotHomeTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getHomeTimeline(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(HOME_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getHomeTimeline(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotHomeTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsTimeline() {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFriendsTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsTimeline(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFriendsTimeline(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(final String screenName, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserTimeline(screenName,
                        paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(final long userId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserTimeline(userId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserTimeline(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(final String screenName) {
        getUserTimeline(screenName, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline(final long userId) {
        getUserTimeline(userId, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public void getUserTimeline() {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getMentions() {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getMentions();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMentions(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getMentions(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getMentions(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMentions(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByMe() {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_ME, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetedByMe();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByMe(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByMe(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_ME, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetedByMe(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByMe(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedToMe() {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_ME, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetedToMe();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedToMe(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedToMe(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_ME, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetedToMe(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedToMe(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetsOfMe() {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetsOfMe();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetsOfMe(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetsOfMe(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS_OF_ME, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetsOfMe(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetsOfMe(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByUser(final String screenName, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_USER, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetedByUser(screenName, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByUser(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByUser(final long userId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_USER, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetedByUser(userId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByUser(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedToUser(final String screenName, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_USER, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetedToUser(screenName, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedToUser(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedToUser(final long userId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_TO_USER, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweetedToUser(userId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedToUser(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showStatus(final long id) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_STATUS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.showStatus(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowStatus(status);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatus(final String statusText) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.updateStatus(statusText);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedStatus(status);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatus(final StatusUpdate latestStatus) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_STATUS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.updateStatus(latestStatus);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedStatus(status);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyStatus(final long statusId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_STATUS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.destroyStatus(statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedStatus(status);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void retweetStatus(final long statusId) {
        getDispatcher().invokeLater(new AsyncTask(RETWEET_STATUS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.retweetStatus(statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.retweetedStatus(status);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweets(final long statusId) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getRetweets(statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweets(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedBy(final long statusId) {
        getRetweetedBy(statusId, new Paging(1, 100));
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedBy(final long statusId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getRetweetedBy(statusId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedBy(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByIDs(final long statusId) {
        getRetweetedByIDs(statusId, new Paging(1, 100));
    }

    /**
     * {@inheritDoc}
     */
    public void getRetweetedByIDs(final long statusId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(RETWEETED_BY_IDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getRetweetedByIDs(statusId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByIDs(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /* User Methods */

    /**
     * {@inheritDoc}
     */
    public void showUser(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUser(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserDetail(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showUser(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_USER, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUser(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserDetail(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void lookupUsers(final String[] screenNames) {
        getDispatcher().invokeLater(new AsyncTask(LOOKUP_USERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.lookupUsers(screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedupUsers(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void lookupUsers(final long[] ids) {
        getDispatcher().invokeLater(new AsyncTask(LOOKUP_USERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.lookupUsers(ids);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedupUsers(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void searchUsers(final String query, final int page) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH_USERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.searchUsers(query, page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searchedUser(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getSuggestedUserCategories() {
        getDispatcher().invokeLater(new AsyncTask(SUGGESTED_USER_CATEGORIES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Category> categories = twitter.getSuggestedUserCategories();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSuggestedUserCategories(categories);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserSuggestions(final String categorySlug) {
        getDispatcher().invokeLater(new AsyncTask(USER_SUGGESTIONS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getUserSuggestions(categorySlug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserSuggestions(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getMemberSuggestions(final String categorySlug) {
        getDispatcher().invokeLater(new AsyncTask(MEMBER_SUGGESTIONS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getMemberSuggestions(categorySlug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMemberSuggestions(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getProfileImage(final String screenName, final ProfileImage.ImageSize size) {
        getDispatcher().invokeLater(new AsyncTask(PROFILE_IMAGE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ProfileImage profileImage = twitter.getProfileImage(screenName, size);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotProfileImage(profileImage);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getAccountTotals() {
        getDispatcher().invokeLater(new AsyncTask(ACCOUNT_TOTALS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccountTotals accountTotals = twitter.getAccountTotals();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAccountTotals(accountTotals);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getAccountSettings() {
        getDispatcher().invokeLater(new AsyncTask(ACCOUNT_SETTINGS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccountSettings accountSettings = twitter.getAccountSettings();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAccountSettings(accountSettings);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateAccountSettings(final Integer trend_locationWoeid, final Boolean sleep_timeEnabled, final String start_sleepTime, final String end_sleepTime, final String time_zone, final String lang) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_ACCOUNT_SETTINGS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccountSettings accountSettings = twitter.updateAccountSettings(trend_locationWoeid, sleep_timeEnabled, start_sleepTime, end_sleepTime, time_zone, lang);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedAccountSettings(accountSettings);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFriendsStatuses(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsStatuses(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses(final String screenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFriendsStatuses(screenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsStatuses(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsStatuses(final long userId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_STATUSES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFriendsStatuses(userId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsStatuses(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFollowersStatuses(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersStatuses(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses(final String screenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFollowersStatuses(screenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersStatuses(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersStatuses(final long userId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_STATUSES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getFollowersStatuses(userId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersStatuses(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /*List Methods*/

    /**
     * {@inheritDoc}
     */
    public void createUserList(final String listName, final boolean isPublicList, final String description) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_USER_LIST, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserList(listName, isPublicList, description);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserList(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateUserList(final int listId, final String newListName, final boolean isPublicList, final String newDescription) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_USER_LIST, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.updateUserList(listId, newListName, isPublicList, newDescription);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedUserList(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserLists(final String listOwnerScreenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(USER_LISTS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserLists(listOwnerScreenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserLists(lists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserLists(final long listOwnerUserId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(USER_LISTS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserLists(listOwnerUserId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserLists(lists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showUserList(final String listOwnerScreenName, final int listId) {
        showUserList(listId);
    }

    /**
     * {@inheritDoc}
     */
    public void showUserList(final int listId) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_USER_LIST, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.showUserList(listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowUserList(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyUserList(final int listId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_USER_LIST, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.destroyUserList(listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedUserList(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListStatuses(final String listOwnerScreenName, final int id, final Paging paging) {
        getUserListStatuses(id, paging);
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListStatuses(final long listOwnerId, final int id, final Paging paging) {
        getUserListStatuses(id, paging);
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListStatuses(final int listId, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_STATUSES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getUserListStatuses(listId, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListStatuses(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

     /**
     * {@inheritDoc}
     */
    public void getUserListMemberships(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_MEMBERSHIPS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserListMemberships(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMemberships(lists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListMemberships(final String listMemberScreenName, final long cursor) {
        getUserListMemberships(listMemberScreenName, cursor, false);
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListMemberships(final long listMemberId, final long cursor) {
        getUserListMemberships(listMemberId, cursor, false);
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListMemberships(final String listMemberScreenName, final long cursor, final boolean filterToOwnedLists) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_MEMBERSHIPS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserListMemberships(listMemberScreenName, cursor, filterToOwnedLists);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMemberships(lists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListMemberships(final long listMemberId, final long cursor, final boolean filterToOwnedLists) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_MEMBERSHIPS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserListMemberships(listMemberId, cursor, filterToOwnedLists);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMemberships(lists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListSubscriptions(final String listOwnerScreenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(USER_LIST_SUBSCRIPTIONS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = twitter.getUserListSubscriptions(listOwnerScreenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListSubscriptions(lists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getAllSubscribingUserLists(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(ALL_USER_LISTS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<UserList> lists = twitter.getAllUserLists(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAllUserLists(lists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getAllSubscribingUserLists(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(ALL_USER_LISTS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<UserList> lists = twitter.getAllUserLists(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAllUserLists(lists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /*List Members Methods*/

    /**
     * {@inheritDoc}
     */
    public void getUserListMembers(final String listOwnerScreenName, final int listId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(LIST_MEMBERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getUserListMembers(listOwnerScreenName, listId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMembers(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListMembers(final long listOwnerId, final int listId, final long cursor) {
        getUserListMembers(listId, cursor);
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListMembers(final int listId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(LIST_MEMBERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getUserListMembers(listId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMembers(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void addUserListMember(final int listId, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(ADD_LIST_MEMBER, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.addUserListMember(listId, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.addedUserListMember(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void addUserListMembers(final int listId, final long[] userIds) {
        getDispatcher().invokeLater(new AsyncTask(ADD_LIST_MEMBERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.addUserListMembers(listId, userIds);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.addedUserListMembers(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void addUserListMembers(final int listId, final String[] screenNames) {
        getDispatcher().invokeLater(new AsyncTask(ADD_LIST_MEMBERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.addUserListMembers(listId, screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.addedUserListMembers(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void deleteUserListMember(final int listId, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DELETE_LIST_MEMBER, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.deleteUserListMember(listId, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.deletedUserListMember(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void checkUserListMembership(final String listOwnerScreenName, final int listId, final long userId) {
        showUserListMembership(listId, userId);
    }

    /**
     * {@inheritDoc}
     */
    public void showUserListMembership(final int listId, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CHECK_LIST_MEMBERSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUserListMembership(listId, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.checkedUserListMembership(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /*List Subscribers Methods*/

    /**
     * {@inheritDoc}
     */
    public void getUserListSubscribers(final String listOwnerScreenName, final int listId, final long cursor) {
        getUserListSubscribers(listId, cursor);
    }

    /**
     * {@inheritDoc}
     */
    public void getUserListSubscribers(final int listId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(LIST_SUBSCRIBERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = twitter.getUserListSubscribers(listId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListSubscribers(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void subscribeUserList(final String listOwnerScreenName, final int listId) {
        createUserListSubscription(listId);
    }

    /**
     * {@inheritDoc}
     */
    public void createUserListSubscription(final int listId) {
        getDispatcher().invokeLater(new AsyncTask(SUBSCRIBE_LIST, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.createUserListSubscription(listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.subscribedUserList(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void unsubscribeUserList(final String listOwnerScreenName, final int listId) {
        destroyUserListSubscription(listId);
    }

    /**
     * {@inheritDoc}
     */
    public void destroyUserListSubscription(final int listId) {
        getDispatcher().invokeLater(new AsyncTask(UNSUBSCRIBE_LIST, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = twitter.destroyUserListSubscription(listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.unsubscribedUserList(list);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void checkUserListSubscription(final String listOwnerScreenName, final int listId, final long userId) {
        showUserListSubscription(listId, userId);
    }

    /**
     * {@inheritDoc}
     */
    public void showUserListSubscription(final int listId, final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CHECK_LIST_SUBSCRIPTION, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.showUserListSubscription(listId, userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.checkedUserListSubscription(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /*Direct Message Methods */

    /**
     * {@inheritDoc}
     */
    public void getDirectMessages() {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<DirectMessage> directMessages = twitter.getDirectMessages();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDirectMessages(directMessages);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getDirectMessages(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<DirectMessage> directMessages = twitter.getDirectMessages(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDirectMessages(directMessages);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getSentDirectMessages() {
        getDispatcher().invokeLater(new AsyncTask(SENT_DIRECT_MESSAGES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<DirectMessage> directMessages = twitter.getSentDirectMessages();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSentDirectMessages(directMessages);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getSentDirectMessages(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(SENT_DIRECT_MESSAGES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<DirectMessage> directMessages = twitter.getSentDirectMessages(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSentDirectMessages(directMessages);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void sendDirectMessage(final String screenName, final String text) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = twitter.sendDirectMessage(screenName, text);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.sentDirectMessage(directMessage);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void sendDirectMessage(final long userId, final String text) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = twitter.sendDirectMessage(userId, text);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.sentDirectMessage(directMessage);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyDirectMessage(final long id) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_DIRECT_MESSAGE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = twitter.destroyDirectMessage(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedDirectMessage(directMessage);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showDirectMessage(final long id) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = twitter.showDirectMessage(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDirectMessage(directMessage);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /*Friendship Methods*/

    /**
     * {@inheritDoc}
     */
    public void createFriendship(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createFriendship(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendship(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createFriendship(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendship(final String screenName, final boolean follow) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createFriendship(screenName, follow);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFriendship(final long userId, final boolean follow) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createFriendship(userId, follow);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFriendship(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyFriendship(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFriendship(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFriendship(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyFriendship(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFriendship(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsFriendship(final String userA, final String userB) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                for (TwitterListener listener : listeners) {
                    boolean exists = twitter.existsFriendship(userA, userB);
                    try {
                        listener.gotExistsFriendship(exists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showFriendship(final String sourceScreenName, final String targetScreenName) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = twitter.showFriendship(sourceScreenName, targetScreenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowFriendship(relationship);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showFriendship(final long sourceId, final long targetId) {
        getDispatcher().invokeLater(new AsyncTask(SHOW_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = twitter.showFriendship(sourceId, targetId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowFriendship(relationship);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getIncomingFriendships(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(INCOMING_FRIENDSHIPS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getIncomingFriendships(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotIncomingFriendships(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getOutgoingFriendships(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(OUTGOING_FRIENDSHIPS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getOutgoingFriendships(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOutgoingFriendships(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void lookupFriendships(final String[] screenNames) {
        getDispatcher().invokeLater(new AsyncTask(LOOKUP_FRIENDSHIPS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Friendship> friendships = twitter.lookupFriendships(screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedUpFriendships(friendships);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void lookupFriendships(final long[] ids) {
        getDispatcher().invokeLater(new AsyncTask(LOOKUP_FRIENDSHIPS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Friendship> friendships = twitter.lookupFriendships(ids);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedUpFriendships(friendships);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateFriendship(final String screenName
            , final boolean enableDeviceNotification, final boolean retweet) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = twitter.updateFriendship(screenName
                        , enableDeviceNotification, retweet);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedFriendship(relationship);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateFriendship(final long userId
            , final boolean enableDeviceNotification, final boolean retweet) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_FRIENDSHIP, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = twitter.updateFriendship(userId
                        , enableDeviceNotification, retweet);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedFriendship(relationship);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getNoRetweetIds() {
        getDispatcher().invokeLater(new AsyncTask(NO_RETWEET_IDS, listeners) {
            @Override
            void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getNoRetweetIds();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotNoRetweetIds(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /* Social Graph Methods */

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                IDs ids = twitter.getFriendsIDs(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs(final long userId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getFriendsIDs(userId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFriendsIDs(final String screenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                IDs ids = twitter.getFriendsIDs(screenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs(final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getFollowersIDs(cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs(final long userId, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getFollowersIDs(userId, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFollowersIDs(final String screenName, final long cursor) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getFollowersIDs(screenName, cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void verifyCredentials() {
        getDispatcher().invokeLater(new AsyncTask(VERIFY_CREDENTIALS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.verifyCredentials();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.verifiedCredentials(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfile(final String name, final String url
            , final String location, final String description) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.updateProfile(name, url, location, description);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfile(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getRateLimitStatus() {
        getDispatcher().invokeLater(new AsyncTask(RATE_LIMIT_STATUS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                RateLimitStatus rateLimitStatus = twitter.getRateLimitStatus();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRateLimitStatus(rateLimitStatus);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfileColors(
            final String profileBackgroundColor, final String profileTextColor,
            final String profileLinkColor, final String profileSidebarFillColor,
            final String profileSidebarBorderColor) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_COLORS,
                listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                User user = twitter.updateProfileColors(
                        profileBackgroundColor, profileTextColor,
                        profileLinkColor, profileSidebarFillColor,
                        profileSidebarBorderColor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileColors(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfileImage(final File image) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_IMAGE,
                listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                User user = twitter.updateProfileImage(image);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileImage(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfileImage(final InputStream image) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_IMAGE,
                listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                User user = twitter.updateProfileImage(image);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileImage(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfileBackgroundImage(final File image
            , final boolean tile) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_BACKGROUND_IMAGE,
                listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                User user = twitter.updateProfileBackgroundImage(image, tile);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileBackgroundImage(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void updateProfileBackgroundImage(final InputStream image
            , final boolean tile) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_BACKGROUND_IMAGE,
                listeners) {
            public void invoke(List<TwitterListener> listeners)
                    throws TwitterException {
                User user = twitter.updateProfileBackgroundImage(image, tile);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileBackgroundImage(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites() {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites(final int page) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites(final String id) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites(final String id, final int page) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(id, page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites(final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getFavorites(final String id, final Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = twitter.getFavorites(id, paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createFavorite(final long id) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FAVORITE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.createFavorite(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFavorite(status);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyFavorite(final long id) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FAVORITE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = twitter.destroyFavorite(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFavorite(status);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void enableNotification(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.enableNotification(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.enabledNotification(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void enableNotification(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.enableNotification(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.enabledNotification(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void disableNotification(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DISABLE_NOTIFICATION, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.disableNotification(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.disabledNotification(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void disableNotification(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DISABLE_NOTIFICATION, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.disableNotification(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.disabledNotification(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /* Block Methods */

    /**
     * {@inheritDoc}
     */
    public void createBlock(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createBlock(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdBlock(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void createBlock(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_BLOCK, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.createBlock(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdBlock(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyBlock(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyBlock(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedBlock(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void destroyBlock(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_BLOCK, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.destroyBlock(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedBlock(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsBlock(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_BLOCK, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                boolean exists = twitter.existsBlock(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotExistsBlock(exists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void existsBlock(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_BLOCK, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                boolean exists = twitter.existsBlock(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotExistsBlock(exists);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getBlockingUsers() {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getBlockingUsers();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlockingUsers(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getBlockingUsers(final int page) {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = twitter.getBlockingUsers(page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlockingUsers(users);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getBlockingUsersIDs() {
        getDispatcher().invokeLater(new AsyncTask(BLOCKING_USERS_IDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = twitter.getBlockingUsersIDs();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlockingUsersIDs(ids);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /* Spam Reporting Methods */

    /**
     * {@inheritDoc}
     */
    public void reportSpam(final long userId) {
        getDispatcher().invokeLater(new AsyncTask(REPORT_SPAM, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.reportSpam(userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.reportedSpam(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void reportSpam(final String screenName) {
        getDispatcher().invokeLater(new AsyncTask(REPORT_SPAM, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = twitter.reportSpam(screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.reportedSpam(user);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }
    /* Saved Searches Methods */
    /* Local Trend Methods */

    /**
     * {@inheritDoc}
     */
    public void getAvailableTrends() {
        getDispatcher().invokeLater(new AsyncTask(AVAILABLE_TRENDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Location> locations = twitter.getAvailableTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAvailableTrends(locations);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getAvailableTrends(final GeoLocation location) {
        getDispatcher().invokeLater(new AsyncTask(AVAILABLE_TRENDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Location> locations = twitter.getAvailableTrends(location);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAvailableTrends(locations);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getLocationTrends(final int woeid) {
        getDispatcher().invokeLater(new AsyncTask(LOCATION_TRENDS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Trends trends = twitter.getLocationTrends(woeid);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotLocationTrends(trends);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /* Geo Methods */

    /**
     * {@inheritDoc}
     */
    public void searchPlaces(final GeoQuery query) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH_PLACES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Place> places = twitter.searchPlaces(query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searchedPlaces(places);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    public void getSimilarPlaces(final GeoLocation location, final String name, final String containedWithin
            , final String streetAddress) {
        getDispatcher().invokeLater(new AsyncTask(SIMILAR_PLACES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                SimilarPlaces similarPlaces = twitter.getSimilarPlaces(location, name, containedWithin, streetAddress);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSimilarPlaces(similarPlaces);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void reverseGeoCode(final GeoQuery query) {
        getDispatcher().invokeLater(new AsyncTask(REVERSE_GEO_CODE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Place> places = twitter.reverseGeoCode(query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotReverseGeoCode(places);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getGeoDetails(final String id) {
        getDispatcher().invokeLater(new AsyncTask(GEO_DETAILS, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Place place = twitter.getGeoDetails(id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotGeoDetails(place);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    public void createPlace(final String name, final String containedWithin, final String token
            , final GeoLocation location, final String streetAddress) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_PLACE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Place place = twitter.createPlace(name, containedWithin, token, location, streetAddress);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdPlace(place);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }
    /* Leagl Resources */

    /**
     * {@inheritDoc}
     */
    public void getTermsOfService() {
        getDispatcher().invokeLater(new AsyncTask(TERMS_OF_SERVICE, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                String tos = twitter.getTermsOfService();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotTermsOfService(tos);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getPrivacyPolicy() {
        getDispatcher().invokeLater(new AsyncTask(PRIVACY_POLICY, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                String privacyPolicy = twitter.getPrivacyPolicy();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotPrivacyPolicy(privacyPolicy);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /* #newtwitter Methods */

    public void getRelatedResults(final long statusId) {
        getDispatcher().invokeLater(new AsyncTask(RELATED_RESULTS, listeners) {
            @Override
            void invoke(List<TwitterListener> listeners) throws TwitterException {
                RelatedResults relatedResults = twitter.getRelatedResults(statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRelatedResults(relatedResults);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /* Help Methods */

    /**
     * {@inheritDoc}
     */
    public void test() {
        getDispatcher().invokeLater(new AsyncTask(TEST, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                boolean ok = twitter.test();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.tested(ok);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getAPIConfiguration() {
        getDispatcher().invokeLater(new AsyncTask(CONFIGURATION, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                TwitterAPIConfiguration apiConf = twitter.getAPIConfiguration();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAPIConfiguration(apiConf);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void getLanguages() {
        getDispatcher().invokeLater(new AsyncTask(LANGUAGES, listeners) {
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<HelpMethods.Language> languages = twitter.getLanguages();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotLanguages(languages);
                    } catch (Exception ignore) {
                    }
                }
            }
        });
    }

    private static transient Dispatcher dispatcher;

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        super.shutdown();
        synchronized (AsyncTwitterImpl.class) {
            if (dispatcher != null) {
                dispatcher.shutdown();
                dispatcher = null;
            }
        }
        twitter.shutdown();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return twitter.getOAuthRequestToken();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    @Override
    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        return twitter.getOAuthAccessToken(oauthVerifier);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    @Override
    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        return twitter.getOAuthAccessToken(requestToken);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    @Override
    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return twitter.getOAuthAccessToken(requestToken, oauthVerifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOAuthAccessToken(AccessToken accessToken) {
        twitter.setOAuthAccessToken(accessToken);
    }

    /**
     * {@inheritDoc}
     */
    public AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        return twitter.getOAuthAccessToken(screenName, password);
    }

    abstract class AsyncTask implements Runnable {
        List<TwitterListener> listeners;
        TwitterMethod method;

        AsyncTask(TwitterMethod method, List<TwitterListener> listeners) {
            this.method = method;
            this.listeners = listeners;
        }

        abstract void invoke(List<TwitterListener> listeners) throws TwitterException;

        public void run() {
            try {
                invoke(listeners);
            } catch (TwitterException te) {
                if (listeners != null) {
                    for (TwitterListener listener : listeners) {
                        try {
                            listener.onException(te, method);
                        } catch (Exception ignore) {
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
