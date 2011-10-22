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

/**
 * A listner for receiving asynchronous responses from Twitter Async APIs.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterAdapter
 */
public interface TwitterListener {
    /*Search API Methods*/
    void searched(QueryResult queryResult);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotCurrentTrends(Trends trends);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotDailyTrends(ResponseList<Trends> trendsList);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotWeeklyTrends(ResponseList<Trends> trendsList);

    /*Timeline Methods*/
    void gotPublicTimeline(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.0.10
     */
    void gotHomeTimeline(ResponseList<Status> statuses);

    void gotFriendsTimeline(ResponseList<Status> statuses);

    void gotUserTimeline(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.0.1
     */
    void gotMentions(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetedByMe(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetedToMe(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetsOfMe(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.1.9
     */
    void gotRetweetedByUser(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.1.9
     */
    void gotRetweetedToUser(ResponseList<Status> statuses);

    /*Status Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    void gotShowStatus(Status status);

    void updatedStatus(Status status);

    void destroyedStatus(Status destroyedStatus);

    /**
     * @since Twitter4J 2.0.10
     */
    void retweetedStatus(Status retweetedStatus);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotRetweets(ResponseList<Status> retweets);

    /**
     * @since Twitter4J 2.1.3
     */
    void gotRetweetedBy(ResponseList<User> users);

    /**
     * @since Twitter4J 2.1.3
     */
    void gotRetweetedByIDs(IDs ids);

    /*User Methods*/
    void gotUserDetail(User user);

    /**
     * @since Twitter4J 2.1.1
     */
    void lookedupUsers(ResponseList<User> users);

    /**
     * @since Twitter4J 2.1.0
     */
    void searchedUser(ResponseList<User> userList);

    /**
     * @since Twitter4J 2.1.1
     */
    void gotSuggestedUserCategories(ResponseList<Category> category);

    /**
     * @since Twitter4J 2.1.1
     */
    void gotUserSuggestions(ResponseList<User> users);

    /**
     * @since Twitter4J 2.1.9
     */
    void gotMemberSuggestions(ResponseList<User> users);

    /**
     * @since Twitter4J 2.1.7
     */
    void gotProfileImage(ProfileImage image);


    void gotFriendsStatuses(PagableResponseList<User> users);

    void gotFollowersStatuses(PagableResponseList<User> users);

    /*List Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    void createdUserList(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void updatedUserList(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserLists(PagableResponseList<UserList> userLists);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotShowUserList(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void destroyedUserList(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListStatuses(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListMemberships(PagableResponseList<UserList> userLists);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListSubscriptions(PagableResponseList<UserList> userLists);

    /**
     * @since Twitter4J 2.1.9
     */
    void gotAllUserLists(ResponseList<UserList> lists);

    /*List Members Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListMembers(PagableResponseList<User> users);

    /**
     * @since Twitter4J 2.1.0
     */
    void addedUserListMember(UserList userList);

    /**
     * @since Twitter4J 2.1.7
     */
    void addedUserListMembers(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void deletedUserListMember(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void checkedUserListMembership(User users);

    /*List Subscribers Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListSubscribers(PagableResponseList<User> users);

    /**
     * @since Twitter4J 2.1.0
     */
    void subscribedUserList(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void unsubscribedUserList(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void checkedUserListSubscription(User user);

    /*Direct Message Methods*/
    void gotDirectMessages(ResponseList<DirectMessage> messages);

    void gotSentDirectMessages(ResponseList<DirectMessage> messages);

    void sentDirectMessage(DirectMessage message);

    /**
     * @since Twitter4J 2.1.9
     */
    void gotDirectMessage(DirectMessage message);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedDirectMessage(DirectMessage message);

    /*Friendship Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    void createdFriendship(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedFriendship(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void gotExistsFriendship(boolean exists);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotShowFriendship(Relationship relationship);

    /**
     * @since Twitter4J 2.1.2
     */
    void gotIncomingFriendships(IDs ids);

    /**
     * @since Twitter4J 2.1.2
     */
    void gotOutgoingFriendships(IDs ids);

    /*Social Graph Methods*/
    void gotFriendsIDs(IDs ids);

    void gotFollowersIDs(IDs ids);

    /**
     * @since Twitter4J 2.1.9
     */
    void lookedUpFriendships(ResponseList<Friendship> friendships);

    /**
     * @since Twitter4J 2.1.9
     */
    void updatedFriendship(Relationship relationship);

    /**
     * @since
     */
    void gotNoRetweetIds(IDs ids);

    /*Account Methods*/

    void verifiedCredentials(User user);

    void gotRateLimitStatus(RateLimitStatus rateLimitStatus);

    void updatedProfileColors(User user);

    /**
     * @param totals account totals
     * @since Twitter4J 2.1.9
     */
    void gotAccountTotals(AccountTotals totals);

    /**
     * @param settings account settings
     * @since Twitter4J 2.1.9
     */
    void gotAccountSettings(AccountSettings settings);

    /**
     * @param settings account settings
     * @since Twitter4J 2.2.4
     */
    void updatedAccountSettings(AccountSettings settings);

    /**
     * @since Twitter4J 2.1.0
     */
    void updatedProfileImage(User user);

    /**
     * @since Twitter4J 2.1.0
     */
    void updatedProfileBackgroundImage(User user);

    /**
     * @since Twitter4J 2.0.2
     */
    void updatedProfile(User user);

    /*Favorite Methods*/
    void gotFavorites(ResponseList<Status> statuses);

    void createdFavorite(Status status);

    void destroyedFavorite(Status status);

    /*Notification Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    void enabledNotification(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void disabledNotification(User user);

    /*Block Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    void createdBlock(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedBlock(User user);

    /**
     * @since Twitter4J 2.0.4
     */
    void gotExistsBlock(boolean blockExists);

    /**
     * @since Twitter4J 2.0.4
     */
    void gotBlockingUsers(ResponseList<User> blockingUsers);

    /**
     * @since Twitter4J 2.0.4
     */
    void gotBlockingUsersIDs(IDs blockingUsersIDs);

    /*Spam Reporting Methods*/
    void reportedSpam(User reportedSpammer);

    /*Saved Searches Methods*/
    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /*Local Trends Methods*/

    /**
     * @param locations the locations
     * @since Twitter4J 2.1.1
     */
    void gotAvailableTrends(ResponseList<Location> locations);

    /**
     * @param trends trends
     * @since Twitter4J 2.1.1
     */
    void gotLocationTrends(Trends trends);

    /*Geo Methods*/
    void searchedPlaces(ResponseList<Place> places);

    void gotSimilarPlaces(SimilarPlaces places);

    void gotNearByPlaces(ResponseList<Place> places);

    void gotReverseGeoCode(ResponseList<Place> places);

    void gotGeoDetails(Place place);

    void createdPlace(Place place);

    /* Legal Resources*/

    /**
     * @since Twitter4J 2.1.7
     */
    void gotTermsOfService(String tof);

    /**
     * @since Twitter4J 2.1.7
     */
    void gotPrivacyPolicy(String privacyPolicy);

    /* #newtwitter Methods */

    void gotRelatedResults(RelatedResults relatedResults);

    /*Help Methods*/
    void tested(boolean test);

    void gotAPIConfiguration(TwitterAPIConfiguration conf);

    void gotLanguages(ResponseList<HelpMethods.Language> languages);

    /**
     * @param te     TwitterException
     * @param method
     */
    void onException(TwitterException te, TwitterMethod method);

    /*Search API Methods*/
    TwitterMethod SEARCH = TwitterMethod.SEARCH;

    TwitterMethod DAILY_TRENDS = TwitterMethod.DAILY_TRENDS;
    TwitterMethod WEEKLY_TRENDS = TwitterMethod.WEEKLY_TRENDS;

    /*Timeline Methods*/
    TwitterMethod PUBLIC_TIMELINE = TwitterMethod.PUBLIC_TIMELINE;
    TwitterMethod HOME_TIMELINE = TwitterMethod.HOME_TIMELINE;
    /**
     * @deprecated use {@link #HOME_TIMELINE} instead
     */
    TwitterMethod FRIENDS_TIMELINE = TwitterMethod.FRIENDS_TIMELINE;
    TwitterMethod USER_TIMELINE = TwitterMethod.USER_TIMELINE;
    TwitterMethod MENTIONS = TwitterMethod.MENTIONS;
    TwitterMethod RETWEETED_BY_ME = TwitterMethod.RETWEETED_BY_ME;
    TwitterMethod RETWEETED_TO_ME = TwitterMethod.RETWEETED_TO_ME;
    TwitterMethod RETWEETS_OF_ME = TwitterMethod.RETWEETS_OF_ME;
    TwitterMethod RETWEETED_BY_USER = TwitterMethod.RETWEETED_BY_USER;
    TwitterMethod RETWEETED_TO_USER = TwitterMethod.RETWEETED_TO_USER;

    /*Status Methods*/
    TwitterMethod SHOW_STATUS = TwitterMethod.SHOW_STATUS;
    TwitterMethod UPDATE_STATUS = TwitterMethod.UPDATE_STATUS;
    TwitterMethod DESTROY_STATUS = TwitterMethod.DESTROY_STATUS;
    TwitterMethod RETWEET_STATUS = TwitterMethod.RETWEET_STATUS;
    TwitterMethod RETWEETS = TwitterMethod.RETWEETS;
    TwitterMethod RETWEETED_BY = TwitterMethod.RETWEETED_BY;
    TwitterMethod RETWEETED_BY_IDS = TwitterMethod.RETWEETED_BY_IDS;


    /*User Methods*/
    TwitterMethod SHOW_USER = TwitterMethod.SHOW_USER;
    TwitterMethod LOOKUP_USERS = TwitterMethod.LOOKUP_USERS;
    TwitterMethod SEARCH_USERS = TwitterMethod.SEARCH_USERS;
    TwitterMethod SUGGESTED_USER_CATEGORIES = TwitterMethod.SUGGESTED_USER_CATEGORIES;
    TwitterMethod USER_SUGGESTIONS = TwitterMethod.USER_SUGGESTIONS;
    TwitterMethod MEMBER_SUGGESTIONS = TwitterMethod.MEMBER_SUGGESTIONS;
    TwitterMethod PROFILE_IMAGE = TwitterMethod.PROFILE_IMAGE;
    TwitterMethod FRIENDS_STATUSES = TwitterMethod.FRIENDS_STATUSES;
    TwitterMethod FOLLOWERS_STATUSES = TwitterMethod.FOLLOWERS_STATUSES;

    /*List Methods*/
    TwitterMethod CREATE_USER_LIST = TwitterMethod.CREATE_USER_LIST;
    TwitterMethod UPDATE_USER_LIST = TwitterMethod.UPDATE_USER_LIST;
    TwitterMethod USER_LISTS = TwitterMethod.USER_LISTS;
    TwitterMethod SHOW_USER_LIST = TwitterMethod.SHOW_USER_LIST;
    TwitterMethod DSTROY_USER_LIST = TwitterMethod.DESTROY_USER_LIST;
    TwitterMethod USER_LIST_STATUSES = TwitterMethod.USER_LIST_STATUSES;
    TwitterMethod USER_LIST_MEMBERSHIPS = TwitterMethod.USER_LIST_MEMBERSHIPS;
    TwitterMethod USER_LIST_SUBSCRIPTIONS = TwitterMethod.USER_LIST_SUBSCRIPTIONS;
    TwitterMethod ALL_USER_LISTS = TwitterMethod.ALL_USER_LISTS;

    /*List Members Methods*/
    TwitterMethod LIST_MEMBERS = TwitterMethod.LIST_MEMBERS;
    TwitterMethod ADD_LIST_MEMBER = TwitterMethod.ADD_LIST_MEMBER;
    TwitterMethod ADD_LIST_MEMBERS = TwitterMethod.ADD_LIST_MEMBERS;
    TwitterMethod DELETE_LIST_MEMBER = TwitterMethod.DELETE_LIST_MEMBER;
    TwitterMethod CHECK_LIST_MEMBERSHIP = TwitterMethod.CHECK_LIST_MEMBERSHIP;

    /*List Subscribers Methods*/
    TwitterMethod LIST_SUBSCRIBERS = TwitterMethod.LIST_SUBSCRIBERS;
    TwitterMethod SUBSCRIBE_LIST = TwitterMethod.SUBSCRIBE_LIST;
    TwitterMethod UNSUBSCRIBE_LIST = TwitterMethod.UNSUBSCRIBE_LIST;
    TwitterMethod CHECK_LIST_SUBSCRIPTION = TwitterMethod.CHECK_LIST_SUBSCRIPTION;

    /*Direct Message Methods*/
    TwitterMethod DIRECT_MESSAGES = TwitterMethod.DIRECT_MESSAGES;
    TwitterMethod SENT_DIRECT_MESSAGES = TwitterMethod.SENT_DIRECT_MESSAGES;
    TwitterMethod SEND_DIRECT_MESSAGE = TwitterMethod.SEND_DIRECT_MESSAGE;
    TwitterMethod DESTROY_DIRECT_MESSAGE = TwitterMethod.DESTROY_DIRECT_MESSAGE;

    /*Friendship Methods*/
    TwitterMethod CREATE_FRIENDSHIP = TwitterMethod.CREATE_FRIENDSHIP;
    TwitterMethod DESTROY_FRIENDSHIP = TwitterMethod.DESTROY_FRIENDSHIP;
    TwitterMethod EXISTS_FRIENDSHIP = TwitterMethod.EXISTS_FRIENDSHIP;
    TwitterMethod SHOW_FRIENDSHIP = TwitterMethod.SHOW_FRIENDSHIP;
    TwitterMethod INCOMING_FRIENDSHIP = TwitterMethod.INCOMING_FRIENDSHIPS;
    TwitterMethod OUTGOING_FRIENDSHIPS = TwitterMethod.OUTGOING_FRIENDSHIPS;
    TwitterMethod LOOKUP_FRIENDSHIPS = TwitterMethod.LOOKUP_FRIENDSHIPS;
    TwitterMethod UPDATE_FRIENDSHIP = TwitterMethod.UPDATE_FRIENDSHIP;

    /*Social Graph Methods*/
    TwitterMethod FRIENDS_IDS = TwitterMethod.FRIENDS_IDS;
    TwitterMethod FOLLOWERS_IDS = TwitterMethod.FOLLOWERS_IDS;

    /*Account Methods*/
    TwitterMethod VERIFY_CREDENTIALS = TwitterMethod.VERIFY_CREDENTIALS;
    TwitterMethod RATE_LIMIT_STATUS = TwitterMethod.RATE_LIMIT_STATUS;
    TwitterMethod UPDATE_PROFILE_COLORS = TwitterMethod.UPDATE_PROFILE_COLORS;
    TwitterMethod UPDATE_PROFILE_IMAGE = TwitterMethod.UPDATE_PROFILE_IMAGE;
    TwitterMethod UPDATE_PROFILE_BACKGROUND_IMAGE = TwitterMethod.UPDATE_PROFILE_BACKGROUND_IMAGE;
    TwitterMethod UPDATE_PROFILE = TwitterMethod.UPDATE_PROFILE;
    TwitterMethod ACCOUNT_TOTALS = TwitterMethod.ACCOUNT_TOTALS;
    TwitterMethod ACCOUNT_SETTINGS = TwitterMethod.ACCOUNT_SETTINGS;
    TwitterMethod UPDATE_ACCOUNT_SETTINGS = TwitterMethod.UPDATE_ACCOUNT_SETTINGS;

    /*Favorite Methods*/
    TwitterMethod FAVORITES = TwitterMethod.FAVORITES;
    TwitterMethod CREATE_FAVORITE = TwitterMethod.CREATE_FAVORITE;
    TwitterMethod DESTROY_FAVORITE = TwitterMethod.DESTROY_FAVORITE;

    /*Notification Methods*/
    TwitterMethod ENABLE_NOTIFICATION = TwitterMethod.ENABLE_NOTIFICATION;
    TwitterMethod DISABLE_NOTIFICATION = TwitterMethod.DISABLE_NOTIFICATION;

    /*Block Methods*/
    TwitterMethod CREATE_BLOCK = TwitterMethod.CREATE_BLOCK;
    TwitterMethod DESTROY_BLOCK = TwitterMethod.DESTROY_BLOCK;
    TwitterMethod EXISTS_BLOCK = TwitterMethod.EXISTS_BLOCK;
    TwitterMethod BLOCKING_USERS = TwitterMethod.BLOCKING_USERS;
    TwitterMethod BLOCKING_USERS_IDS = TwitterMethod.BLOCKING_USERS_IDS;

    /*Spam Reporting Methods*/
    TwitterMethod REPORT_SPAM = TwitterMethod.REPORT_SPAM;

    /*Saved Searches Methods*/
    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /*Local Trends Methods*/
    TwitterMethod AVAILABLE_TRENDS = TwitterMethod.AVAILABLE_TRENDS;
    TwitterMethod LOCATION_TRENDS = TwitterMethod.LOCATION_TRENDS;

    /*Geo Methods*/
    TwitterMethod SEARCH_PLACES = TwitterMethod.SEARCH_PLACES;
    TwitterMethod SIMILAR_PLACES = TwitterMethod.SIMILAR_PLACES;
    TwitterMethod NEAR_BY_PLACES = TwitterMethod.NEAR_BY_PLACES;
    TwitterMethod REVERSE_GEO_CODE = TwitterMethod.REVERSE_GEO_CODE;
    TwitterMethod GEO_DETAILS = TwitterMethod.GEO_DETAILS;
    TwitterMethod CREATE_PLACE = TwitterMethod.CREATE_PLACE;

    /* Regal Resources */
    TwitterMethod TERMS_OF_SERVICE = TwitterMethod.TERMS_OF_SERVICE;
    TwitterMethod PRIVACY_POLICY = TwitterMethod.PRIVACY_POLICY;

    /* #newtwitter Methods */
    TwitterMethod RELATED_RESULT = TwitterMethod.RELATED_RESULTS;

    /*Help Methods*/
    TwitterMethod TEST = TwitterMethod.TEST;
    TwitterMethod CONFIGURATION = TwitterMethod.CONFIGURATION;
    TwitterMethod LANGUAGES = TwitterMethod.LANGUAGES;

}
