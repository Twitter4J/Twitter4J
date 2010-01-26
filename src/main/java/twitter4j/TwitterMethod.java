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

    private static TwitterMethod getInstance(String name){
        return instances.get(name);
    }

    // assures equality after deserialization
    private Object readResolve() throws ObjectStreamException {
        return getInstance(name);
    }

    /*Search API Methods*/
    public static final TwitterMethod SEARCH = getInstance("SEARCH");

    public static final TwitterMethod TRENDS = new TwitterMethod("TRENDS");
    public static final TwitterMethod CURRENT_TRENDS = new TwitterMethod("CURRENT_TRENDS");
    public static final TwitterMethod DAILY_TRENDS = new TwitterMethod("DAILY_TRENDS");
    public static final TwitterMethod WEEKLY_TRENDS = new TwitterMethod("WEEKLY_TRENDS");

    /*Timeline Methods*/
    public static final TwitterMethod PUBLIC_TIMELINE = new TwitterMethod("PUBLIC_TIMELINE");
    public static final TwitterMethod HOME_TIMELINE = new TwitterMethod("HOME_TIMELINE");
    public static final TwitterMethod FRIENDS_TIMELINE = new TwitterMethod("FRIENDS_TIMELINE");
    public static final TwitterMethod USER_TIMELINE = new TwitterMethod("USER_TIMELINE");
    public static final TwitterMethod MENTIONS = new TwitterMethod("MENTIONS");
    public static final TwitterMethod RETWEETED_BY_ME = new TwitterMethod("RETWEETED_BY_ME");
    public static final TwitterMethod RETWEETED_TO_ME = new TwitterMethod("RETWEETED_TO_ME");
    public static final TwitterMethod RETWEETS_OF_ME = new TwitterMethod("RETWEETS_OF_ME");

    /*Status Methods*/
    public static final TwitterMethod SHOW_STATUS = new TwitterMethod("SHOW_STATUS");
    public static final TwitterMethod UPDATE_STATUS = new TwitterMethod("UPDATE_STATUS");
    public static final TwitterMethod DESTROY_STATUS = new TwitterMethod("DESTROY_STATUS");
    public static final TwitterMethod RETWEET_STATUS = new TwitterMethod("RETWEET_STATUS");
    public static final TwitterMethod RETWEETS = new TwitterMethod("RETWEETS");

    /*User Methods*/
    public static final TwitterMethod SHOW_USER = new TwitterMethod("SHOW_USER");
    public static final TwitterMethod SEARCH_USERS = new TwitterMethod("SEARCH_USERS");
    public static final TwitterMethod FRIENDS_STATUSES = new TwitterMethod("FRIENDS_STATUSES");
    public static final TwitterMethod FOLLOWERS_STATUSES = new TwitterMethod("FOLLOWERS_STATUSES");

    /*List Methods*/
    public static final TwitterMethod CREATE_USER_LIST = new TwitterMethod("CREATE_USER_LIST");
    public static final TwitterMethod UPDATE_USER_LIST = new TwitterMethod("UPDATE_USER_LIST");
    public static final TwitterMethod USER_LISTS = new TwitterMethod("USER_LISTS");
    public static final TwitterMethod SHOW_USER_LIST = new TwitterMethod("SHOW_USER_LIST");
    public static final TwitterMethod DELETE_USER_LIST = new TwitterMethod("DELETE_USER_LIST");
    public static final TwitterMethod USER_LIST_STATUSES = new TwitterMethod("USER_LIST_STATUSES");
    public static final TwitterMethod USER_LIST_MEMBERSHIPS = new TwitterMethod("USER_LIST_MEMBERSHIPS");
    public static final TwitterMethod USER_LIST_SUBSCRIPTIONS = new TwitterMethod("USER_LIST_SUBSCRIPTIONS");

    /*List Members Methods*/
    public static final TwitterMethod LIST_MEMBERS = new TwitterMethod("LIST_MEMBERS");
    public static final TwitterMethod ADD_LIST_MEMBER = new TwitterMethod("ADD_LIST_MEMBER");
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
    public static final TwitterMethod DESTROY_DIRECT_MESSAGES = new TwitterMethod("DESTROY_DIRECT_MESSAGES");

    /*Friendship Methods*/
    public static final TwitterMethod CREATE_FRIENDSHIP = new TwitterMethod("CREATE_FRIENDSHIP");
    public static final TwitterMethod DESTROY_FRIENDSHIP = new TwitterMethod("DESTROY_FRIENDSHIP");
    public static final TwitterMethod EXISTS_FRIENDSHIP = new TwitterMethod("EXISTS_FRIENDSHIP");
    public static final TwitterMethod SHOW_FRIENDSHIP = new TwitterMethod("SHOW_FRIENDSHIP");

    /*Social Graph Methods*/
    public static final TwitterMethod FRIENDS_IDS = new TwitterMethod("FRIENDS_IDS");
    public static final TwitterMethod FOLLOWERS_IDS = new TwitterMethod("FOLLOWERS_IDS");

    /*Account Methods*/
    //verifyCredentials
    public static final TwitterMethod RATE_LIMIT_STATUS = new TwitterMethod("RATE_LIMIT_STATUS");
    public static final TwitterMethod UPDATE_DELIVERY_DEVICE = new TwitterMethod("UPDATE_DELIVERY_DEVICE");
    public static final TwitterMethod UPDATE_PROFILE_COLORS = new TwitterMethod("UPDATE_PROFILE_COLORS");
    public static final TwitterMethod UPDATE_PROFILE_IMAGE = new TwitterMethod("UPDATE_PROFILE_IMAGE");
    public static final TwitterMethod UPDATE_PROFILE_BACKGROUND_IMAGE = new TwitterMethod("UPDATE_PROFILE_BACKGROUND_IMAGE");
    public static final TwitterMethod UPDATE_PROFILE = new TwitterMethod("UPDATE_PROFILE");

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

    /*Help Methods*/
    public static final TwitterMethod TEST = new TwitterMethod("TEST");
}
