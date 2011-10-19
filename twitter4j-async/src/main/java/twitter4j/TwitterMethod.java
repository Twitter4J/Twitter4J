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

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class TwitterMethod implements java.io.Serializable {
    String name;
    private static final long serialVersionUID = 5776633408291563058L;

    private TwitterMethod() {
        throw new AssertionError();
    }

    private TwitterMethod(String name) {
        this.name = name;
        instances.put(name, this);
    }

    private static final Map<String, TwitterMethod> instances = new HashMap<String, TwitterMethod>();

    public final String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterMethod)) return false;

        TwitterMethod that = (TwitterMethod) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                '}';
    }

    private static TwitterMethod getInstance(String name) {
        return instances.get(name);
    }

    // assures equality after deserialization
    private Object readResolve() throws ObjectStreamException {
        return getInstance(name);
    }

    /*Search API Methods*/
    public static final TwitterMethod SEARCH = getInstance("SEARCH");

    public static final TwitterMethod DAILY_TRENDS = new TwitterMethod("DAILY_TRENDS");
    public static final TwitterMethod WEEKLY_TRENDS = new TwitterMethod("WEEKLY_TRENDS");

    /*Timeline Methods*/
    public static final TwitterMethod PUBLIC_TIMELINE = new TwitterMethod("PUBLIC_TIMELINE");
    public static final TwitterMethod HOME_TIMELINE = new TwitterMethod("HOME_TIMELINE");
    /**
     * @deprecated use {@link #HOME_TIMELINE} instead
     */
    public static final TwitterMethod FRIENDS_TIMELINE = new TwitterMethod("FRIENDS_TIMELINE");
    public static final TwitterMethod USER_TIMELINE = new TwitterMethod("USER_TIMELINE");
    public static final TwitterMethod MENTIONS = new TwitterMethod("MENTIONS");
    public static final TwitterMethod RETWEETED_BY_ME = new TwitterMethod("RETWEETED_BY_ME");
    public static final TwitterMethod RETWEETED_TO_ME = new TwitterMethod("RETWEETED_TO_ME");
    public static final TwitterMethod RETWEETS_OF_ME = new TwitterMethod("RETWEETS_OF_ME");
    public static final TwitterMethod RETWEETED_BY_USER = new TwitterMethod("RETWEETED_BY_USER");
    public static final TwitterMethod RETWEETED_TO_USER = new TwitterMethod("RETWEETED_TO_USER");

    /*Status Methods*/
    public static final TwitterMethod SHOW_STATUS = new TwitterMethod("SHOW_STATUS");
    public static final TwitterMethod UPDATE_STATUS = new TwitterMethod("UPDATE_STATUS");
    public static final TwitterMethod DESTROY_STATUS = new TwitterMethod("DESTROY_STATUS");
    public static final TwitterMethod RETWEET_STATUS = new TwitterMethod("RETWEET_STATUS");
    public static final TwitterMethod RETWEETS = new TwitterMethod("RETWEETS");
    public static final TwitterMethod RETWEETED_BY = new TwitterMethod("RETWEETED_BY");
    public static final TwitterMethod RETWEETED_BY_IDS = new TwitterMethod("RETWEETED_BY_IDS");

    /*User Methods*/
    public static final TwitterMethod SHOW_USER = new TwitterMethod("SHOW_USER");
    public static final TwitterMethod LOOKUP_USERS = new TwitterMethod("LOOKUP_USERS");
    public static final TwitterMethod SEARCH_USERS = new TwitterMethod("SEARCH_USERS");
    public static final TwitterMethod SUGGESTED_USER_CATEGORIES = new TwitterMethod("SUGGESTED_USER_CATEGORIES");
    public static final TwitterMethod PROFILE_IMAGE = new TwitterMethod("PROFILE_IMAGE");
    public static final TwitterMethod USER_SUGGESTIONS = new TwitterMethod("USER_SUGGESTIONS");
    public static final TwitterMethod MEMBER_SUGGESTIONS = new TwitterMethod("MEMBER_SUGGESTIONS");
    /**
     * @deprecated use {@link #FRIENDS_IDS} and {@link #LOOKUP_USERS} instead
     */
    public static final TwitterMethod FRIENDS_STATUSES = new TwitterMethod("FRIENDS_STATUSES");
    /**
     * @deprecated use {@link #FOLLOWERS_IDS} and {@link #LOOKUP_USERS} instead
     */
    public static final TwitterMethod FOLLOWERS_STATUSES = new TwitterMethod("FOLLOWERS_STATUSES");

    /*List Methods*/
    public static final TwitterMethod CREATE_USER_LIST = new TwitterMethod("CREATE_USER_LIST");
    public static final TwitterMethod UPDATE_USER_LIST = new TwitterMethod("UPDATE_USER_LIST");
    public static final TwitterMethod USER_LISTS = new TwitterMethod("USER_LISTS");
    public static final TwitterMethod SHOW_USER_LIST = new TwitterMethod("SHOW_USER_LIST");
    public static final TwitterMethod DESTROY_USER_LIST = new TwitterMethod("DELETE_USER_LIST");
    public static final TwitterMethod USER_LIST_STATUSES = new TwitterMethod("USER_LIST_STATUSES");
    public static final TwitterMethod USER_LIST_MEMBERSHIPS = new TwitterMethod("USER_LIST_MEMBERSHIPS");
    public static final TwitterMethod USER_LIST_SUBSCRIPTIONS = new TwitterMethod("USER_LIST_SUBSCRIPTIONS");
    public static final TwitterMethod ALL_USER_LISTS = new TwitterMethod("ALL_USER_LISTS");

    /*List Members Methods*/
    public static final TwitterMethod LIST_MEMBERS = new TwitterMethod("LIST_MEMBERS");
    public static final TwitterMethod ADD_LIST_MEMBER = new TwitterMethod("ADD_LIST_MEMBER");
    public static final TwitterMethod ADD_LIST_MEMBERS = new TwitterMethod("ADD_LIST_MEMBERS");
    public static final TwitterMethod DELETE_LIST_MEMBER = new TwitterMethod("DELETE_LIST_MEMBER");
    public static final TwitterMethod CHECK_LIST_MEMBERSHIP = new TwitterMethod("CHECK_LIST_MEMBERSHIP");

    /*List Subscribers Methods*/
    public static final TwitterMethod LIST_SUBSCRIBERS = new TwitterMethod("LIST_SUBSCRIBERS");
    public static final TwitterMethod SUBSCRIBE_LIST = new TwitterMethod("SUBSCRIBE_LIST");
    public static final TwitterMethod UNSUBSCRIBE_LIST = new TwitterMethod("UNSUBSCRIBE_LIST");
    public static final TwitterMethod CHECK_LIST_SUBSCRIPTION = new TwitterMethod("CHECK_LIST_SUBSCRIPTION");

    /*Direct Message Methods*/
    public static final TwitterMethod DIRECT_MESSAGES = new TwitterMethod("DIRECT_MESSAGES");
    public static final TwitterMethod SENT_DIRECT_MESSAGES = new TwitterMethod("SENT_DIRECT_MESSAGES");
    public static final TwitterMethod SEND_DIRECT_MESSAGE = new TwitterMethod("SEND_DIRECT_MESSAGE");
    public static final TwitterMethod DESTROY_DIRECT_MESSAGE = new TwitterMethod("DESTROY_DIRECT_MESSAGE");
    public static final TwitterMethod DIRECT_MESSAGE = new TwitterMethod("DIRECT_MESSAGE");

    /*Friendship Methods*/
    public static final TwitterMethod CREATE_FRIENDSHIP = new TwitterMethod("CREATE_FRIENDSHIP");
    public static final TwitterMethod DESTROY_FRIENDSHIP = new TwitterMethod("DESTROY_FRIENDSHIP");
    public static final TwitterMethod EXISTS_FRIENDSHIP = new TwitterMethod("EXISTS_FRIENDSHIP");
    public static final TwitterMethod SHOW_FRIENDSHIP = new TwitterMethod("SHOW_FRIENDSHIP");
    public static final TwitterMethod INCOMING_FRIENDSHIPS = new TwitterMethod("INCOMING_FRIENDSHIPS");
    public static final TwitterMethod OUTGOING_FRIENDSHIPS = new TwitterMethod("OUTGOING_FRIENDSHIPS");
    public static final TwitterMethod LOOKUP_FRIENDSHIPS = new TwitterMethod("LOOKUP_FRIENDSHIPS");
    public static final TwitterMethod UPDATE_FRIENDSHIP = new TwitterMethod("UPDATE_FRIENDSHIP");
    public static final TwitterMethod NO_RETWEET_IDS = new TwitterMethod("NO_RETWEET_IDS");

    /*Social Graph Methods*/
    public static final TwitterMethod FRIENDS_IDS = new TwitterMethod("FRIENDS_IDS");
    public static final TwitterMethod FOLLOWERS_IDS = new TwitterMethod("FOLLOWERS_IDS");

    /*Account Methods*/
    public static final TwitterMethod VERIFY_CREDENTIALS = new TwitterMethod("VERIFY_CREDENTIALS");
    public static final TwitterMethod RATE_LIMIT_STATUS = new TwitterMethod("RATE_LIMIT_STATUS");
    public static final TwitterMethod UPDATE_PROFILE_COLORS = new TwitterMethod("UPDATE_PROFILE_COLORS");
    public static final TwitterMethod UPDATE_PROFILE_IMAGE = new TwitterMethod("UPDATE_PROFILE_IMAGE");
    public static final TwitterMethod UPDATE_PROFILE_BACKGROUND_IMAGE = new TwitterMethod("UPDATE_PROFILE_BACKGROUND_IMAGE");
    public static final TwitterMethod UPDATE_PROFILE = new TwitterMethod("UPDATE_PROFILE");
    public static final TwitterMethod ACCOUNT_TOTALS = new TwitterMethod("ACCOUNT_TOTALS");
    public static final TwitterMethod ACCOUNT_SETTINGS = new TwitterMethod("ACCOUNT_SETTINGS");
    public static final TwitterMethod UPDATE_ACCOUNT_SETTINGS = new TwitterMethod("UPDATE_ACCOUNT_SETTINGS");

    /*Favorite Methods*/
    public static final TwitterMethod FAVORITES = new TwitterMethod("FAVORITES");
    public static final TwitterMethod CREATE_FAVORITE = new TwitterMethod("CREATE_FAVORITE");
    public static final TwitterMethod DESTROY_FAVORITE = new TwitterMethod("DESTROY_FAVORITE");

    /*Notification Methods*/
    public static final TwitterMethod ENABLE_NOTIFICATION = new TwitterMethod("ENABLE_NOTIFICATION");
    public static final TwitterMethod DISABLE_NOTIFICATION = new TwitterMethod("DISABLE_NOTIFICATION");

    /*Block Methods*/
    public static final TwitterMethod CREATE_BLOCK = new TwitterMethod("CREATE_BLOCK");
    public static final TwitterMethod DESTROY_BLOCK = new TwitterMethod("DESTROY_BLOCK");
    public static final TwitterMethod EXISTS_BLOCK = new TwitterMethod("EXISTS_BLOCK");
    public static final TwitterMethod BLOCKING_USERS = new TwitterMethod("BLOCKING_USERS");
    public static final TwitterMethod BLOCKING_USERS_IDS = new TwitterMethod("BLOCKING_USERS_IDS");

    /*Spam Reporting Methods*/
    public static final TwitterMethod REPORT_SPAM = new TwitterMethod("REPORT_SPAM");

    /*Saved Searches Methods*/
    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /*Local Trends Methods*/
    public static final TwitterMethod AVAILABLE_TRENDS = new TwitterMethod("AVAILABLE_TRENDS");
    public static final TwitterMethod LOCATION_TRENDS = new TwitterMethod("LOCATION_TRENDS");

    /*Geo Methods*/
    public static final TwitterMethod SEARCH_PLACES = new TwitterMethod("SEARCH_PLACES");
    public static final TwitterMethod SIMILAR_PLACES = new TwitterMethod("SIMILAR_PLACES");
    public static final TwitterMethod NEAR_BY_PLACES = new TwitterMethod("NEAR_BY_PLACES");
    public static final TwitterMethod REVERSE_GEO_CODE = new TwitterMethod("REVERSE_GEO_CODE");
    public static final TwitterMethod GEO_DETAILS = new TwitterMethod("GEO_DETAILS");
    public static final TwitterMethod CREATE_PLACE = new TwitterMethod("CREATE_PLACE");

    /* Legal Resources */
    public static final TwitterMethod TERMS_OF_SERVICE = new TwitterMethod("TERMS_OF_SERVICE");
    public static final TwitterMethod PRIVACY_POLICY = new TwitterMethod("PRIVACY_POLICY");

    /* #newtwitter Methods */
    public static final TwitterMethod RELATED_RESULTS = new TwitterMethod("RELATED_RESULTS");

    /*Help Methods*/
    public static final TwitterMethod TEST = new TwitterMethod("TEST");
    public static final TwitterMethod CONFIGURATION = new TwitterMethod("CONFIGURATION");
    public static final TwitterMethod LANGUAGES = new TwitterMethod("LANGUAGES");
}
