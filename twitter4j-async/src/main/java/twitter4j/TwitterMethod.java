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
    /* Timelines Resources */
    MENTIONS_TIMELINE,
    USER_TIMELINE,
    HOME_TIMELINE,

    /* Tweets Resources */
    RETWEETS,
    SHOW_STATUS,
    DESTROY_STATUS,
    UPDATE_STATUS,
    RETWEET_STATUS,

    /* Search Resources */
    SEARCH,

    /* Direct Messages Resources */
    DIRECT_MESSAGES,
    SENT_DIRECT_MESSAGES,
    DIRECT_MESSAGE,
    DESTROY_DIRECT_MESSAGE,
    SEND_DIRECT_MESSAGE,

    /* Friends & Followers Resources */
    FRIENDS_IDS,
    FOLLOWERS_IDS,
    LOOKUP_FRIENDSHIPS,
    INCOMING_FRIENDSHIPS,
    OUTGOING_FRIENDSHIPS,
    CREATE_FRIENDSHIP,
    DESTROY_FRIENDSHIP,
    UPDATE_FRIENDSHIP,
    SHOW_FRIENDSHIP,

    /* Users Resources */
    ACCOUNT_SETTINGS,
    VERIFY_CREDENTIALS,
    UPDATE_ACCOUNT_SETTINGS,
    // UPDATE_DELIVERY_DEVICE
    UPDATE_PROFILE,
    UPDATE_PROFILE_BACKGROUND_IMAGE,
    UPDATE_PROFILE_COLORS,
    UPDATE_PROFILE_IMAGE,
    BLOCK_LIST,
    BLOCK_LIST_IDS,
    CREATE_BLOCK,
    DESTROY_BLOCK,

    SHOW_USER,
    LOOKUP_USERS,
    SEARCH_USERS,

    CONTRIBUTORS,
    CONTRIBUTEEES,

    RATE_LIMIT_STATUS,

    /* Suggested Users Resources */
    USER_SUGGESTIONS,
    SUGGESTED_USER_CATEGORIES,
    MEMBER_SUGGESTIONS,

    /* Favorites Resources */
    FAVORITES,
    DESTROY_FAVORITE,
    CREATE_FAVORITE,

    /* Lists Resources */
    USER_LISTS,
    USER_LIST_STATUSES,
    DELETE_LIST_MEMBER,
    USER_LIST_MEMBERSHIPS,
    LIST_SUBSCRIBERS,
    SUBSCRIBE_LIST,
    CHECK_LIST_SUBSCRIPTION,
    UNSUBSCRIBE_LIST,
    ADD_LIST_MEMBERS,
    CHECK_LIST_MEMBERSHIP,
    LIST_MEMBERS,
    ADD_LIST_MEMBER,
    DESTROY_USER_LIST,
    UPDATE_USER_LIST,
    CREATE_USER_LIST,
    SHOW_USER_LIST,
    USER_LIST_SUBSCRIPTIONS,

    /* Saved Searches Resources */
    SAVED_SEARCHES,
    SAVED_SEARCH,
    CREATE_SAVED_SEARCH,
    DESTROY_SAVED_SEARCH,

    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /* Places & Geo Resources */
    /* Trends Resources */
    /* Spam Reporting Resources */
    /* OAuth Resources */
    /* Help Resources */


    /*User Methods*/
    /*List Methods*/



    /*Account Methods*/
    /*Favorite Methods*/

    /*Block Methods*/

    /*Spam Reporting Methods*/
    REPORT_SPAM,

    /*Saved Searches Methods*/

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
    CONFIGURATION,
    LANGUAGES,

    /*OAuth Methods*/
    OAUTH_REQUEST_TOKEN,
    OAUTH_ACCESS_TOKEN
}
