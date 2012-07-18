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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public enum TwitterMethod {
    /*Search API Methods*/
    SEARCH,

    DAILY_TRENDS,
    WEEKLY_TRENDS,

    /*Timeline Methods*/
    HOME_TIMELINE,
    USER_TIMELINE,
    MENTIONS,
    RETWEETED_BY_ME,
    RETWEETED_TO_ME,
    RETWEETS_OF_ME,
    RETWEETED_BY_USER,
    RETWEETED_TO_USER,

    /*Status Methods*/
    SHOW_STATUS,
    UPDATE_STATUS,
    DESTROY_STATUS,
    RETWEET_STATUS,
    RETWEETS,
    RETWEETED_BY,
    RETWEETED_BY_IDS,

    /*User Methods*/
    SHOW_USER,
    LOOKUP_USERS,
    SEARCH_USERS,
    SUGGESTED_USER_CATEGORIES,
    PROFILE_IMAGE,
    USER_SUGGESTIONS,
    MEMBER_SUGGESTIONS,
    CONTRIBUTORS,
    CONTRIBUTEEES,

    /*List Methods*/
    CREATE_USER_LIST,
    UPDATE_USER_LIST,
    USER_LISTS,
    SHOW_USER_LIST,
    DESTROY_USER_LIST,
    USER_LIST_STATUSES,
    USER_LIST_MEMBERSHIPS,
    USER_LIST_SUBSCRIPTIONS,
    ALL_USER_LISTS,

    /*List Members Methods*/
    LIST_MEMBERS,
    ADD_LIST_MEMBER,
    ADD_LIST_MEMBERS,
    DELETE_LIST_MEMBER,
    CHECK_LIST_MEMBERSHIP,

    /*List Subscribers Methods*/
    LIST_SUBSCRIBERS,
    SUBSCRIBE_LIST,
    UNSUBSCRIBE_LIST,
    CHECK_LIST_SUBSCRIPTION,

    /*Direct Message Methods*/
    DIRECT_MESSAGES,
    SENT_DIRECT_MESSAGES,
    SEND_DIRECT_MESSAGE,
    DESTROY_DIRECT_MESSAGE,
    DIRECT_MESSAGE,

    /*Friendship Methods*/
    CREATE_FRIENDSHIP,
    DESTROY_FRIENDSHIP,
    EXISTS_FRIENDSHIP,
    SHOW_FRIENDSHIP,
    INCOMING_FRIENDSHIPS,
    OUTGOING_FRIENDSHIPS,
    LOOKUP_FRIENDSHIPS,
    UPDATE_FRIENDSHIP,
    NO_RETWEET_IDS,

    /*Social Graph Methods*/
    FRIENDS_IDS,
    FOLLOWERS_IDS,

    /*Account Methods*/
    VERIFY_CREDENTIALS,
    RATE_LIMIT_STATUS,
    UPDATE_PROFILE_COLORS,
    UPDATE_PROFILE_IMAGE,
    UPDATE_PROFILE_BACKGROUND_IMAGE,
    UPDATE_PROFILE,
    ACCOUNT_TOTALS,
    ACCOUNT_SETTINGS,
    UPDATE_ACCOUNT_SETTINGS,

    /*Favorite Methods*/
    FAVORITES,
    CREATE_FAVORITE,
    DESTROY_FAVORITE,

    /*Notification Methods*/
    ENABLE_NOTIFICATION,
    DISABLE_NOTIFICATION,

    /*Block Methods*/
    CREATE_BLOCK,
    DESTROY_BLOCK,
    EXISTS_BLOCK,
    BLOCKING_USERS,
    BLOCKING_USERS_IDS,

    /*Spam Reporting Methods*/
    REPORT_SPAM,

    /*Saved Searches Methods*/
    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /*Local Trends Methods*/
    AVAILABLE_TRENDS,
    LOCATION_TRENDS,

    /*Geo Methods*/
    SEARCH_PLACES,
    SIMILAR_PLACES,
    REVERSE_GEO_CODE,
    GEO_DETAILS,
    CREATE_PLACE,

    /* Legal Resources */
    TERMS_OF_SERVICE,
    PRIVACY_POLICY,

    /* #newtwitter Methods */
    RELATED_RESULTS,

    /*Help Methods*/
    TEST,
    CONFIGURATION,
    LANGUAGES,

    /*OAuth Methods*/
    OAUTH_REQUEST_TOKEN,
    OAUTH_ACCESS_TOKEN
}
