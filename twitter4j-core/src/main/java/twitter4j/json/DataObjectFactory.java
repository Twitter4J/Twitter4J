/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.json;

import twitter4j.*;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.lang.Object;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public final class DataObjectFactory {
    private DataObjectFactory() {
        throw new AssertionError("not intended to be instantiated.");
    }

    private static final Constructor<Status> statusConstructor;
    private static final Constructor<User> userConstructor;
    private static final Constructor<Tweet> tweetConstructor;
    private static final Constructor<Relationship> relationshipConstructor;
    private static final Constructor<Place> placeConstructor;
    private static final Constructor<SavedSearch> savedSearchConstructor;
    private static final Constructor<Trend> trendConstructor;
    private static final Constructor<Trends> trendsConstructor;
    private static final Constructor<IDs> IDsConstructor;
    private static final Constructor<RateLimitStatus> rateLimitStatusConstructor;
    private static final Constructor<Category> categoryConstructor;
    private static final Constructor<DirectMessage> directMessageConstructor;
    private static final Constructor<Location> locationConstructor;
    private static final Constructor<UserList> userListConstructor;
    static {
        try {
            statusConstructor = (Constructor<Status>) Class.forName("twitter4j.StatusJSONImpl").getDeclaredConstructor(JSONObject.class);
            statusConstructor.setAccessible(true);

            userConstructor = (Constructor<User>) Class.forName("twitter4j.UserJSONImpl").getDeclaredConstructor(JSONObject.class);
            userConstructor.setAccessible(true);

            tweetConstructor = (Constructor<Tweet>) Class.forName("twitter4j.TweetJSONImpl").getDeclaredConstructor(JSONObject.class);
            tweetConstructor.setAccessible(true);

            relationshipConstructor = (Constructor<Relationship>) Class.forName("twitter4j.RelationshipJSONImpl").getDeclaredConstructor(JSONObject.class);
            relationshipConstructor.setAccessible(true);

            placeConstructor = (Constructor<Place>) Class.forName("twitter4j.PlaceJSONImpl").getDeclaredConstructor(JSONObject.class);
            placeConstructor.setAccessible(true);

            savedSearchConstructor = (Constructor<SavedSearch>) Class.forName("twitter4j.SavedSearchJSONImpl").getDeclaredConstructor(JSONObject.class);
            savedSearchConstructor.setAccessible(true);

            trendConstructor = (Constructor<Trend>) Class.forName("twitter4j.TrendJSONImpl").getDeclaredConstructor(JSONObject.class);
            trendConstructor.setAccessible(true);

            trendsConstructor = (Constructor<Trends>) Class.forName("twitter4j.TrendsJSONImpl").getDeclaredConstructor(String.class);
            trendsConstructor.setAccessible(true);

            IDsConstructor = (Constructor<IDs>) Class.forName("twitter4j.IDsJSONImpl").getDeclaredConstructor(String.class);
            IDsConstructor.setAccessible(true);

            rateLimitStatusConstructor = (Constructor<RateLimitStatus>) Class.forName("twitter4j.RateLimitStatusJSONImpl").getDeclaredConstructor(JSONObject.class);
            rateLimitStatusConstructor.setAccessible(true);

            categoryConstructor = (Constructor<Category>) Class.forName("twitter4j.CategoryJSONImpl").getDeclaredConstructor(JSONObject.class);
            categoryConstructor.setAccessible(true);

            directMessageConstructor = (Constructor<DirectMessage>) Class.forName("twitter4j.DirectMessageJSONImpl").getDeclaredConstructor(JSONObject.class);
            directMessageConstructor.setAccessible(true);

            locationConstructor = (Constructor<Location>) Class.forName("twitter4j.LocationJSONImpl").getDeclaredConstructor(JSONObject.class);
            locationConstructor.setAccessible(true);

            userListConstructor = (Constructor<UserList>) Class.forName("twitter4j.UserListJSONImpl").getDeclaredConstructor(JSONObject.class);
            userListConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static ThreadLocal<Map> rawJsonMap = new ThreadLocal<Map>() {
        @Override
        protected Map initialValue() {
            return new HashMap();
        }
    };

    /**
     * Returns a raw JSON form of the provided object.<br>
     * Note that raw JSON forms can be retrieved only from the same thread invoked the last method call and will become inaccessible once another method call
     *
     * @param obj
     * @return raw JSON
     * @since Twitter4J 2.1.7
     */
    public static String getRawJSON(Object obj) {
        Object json = rawJsonMap.get().get(obj);
        if (json instanceof String) {
            return (String) json;
        } else if (json != null) {
            // object must be instance of JSONObject
            return json.toString();
        } else {
            return null;
        }
    }

    /**
     * Constructs a Status object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return Status
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Status createStatus(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return statusConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a User object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return User
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static User createUser(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return userConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a Tweet object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return Tweet
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Tweet createTweet(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return tweetConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a Relationship object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return Relationship
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Relationship createRelationship(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return relationshipConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a Place object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return Place
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Place createPlace(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return placeConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a SavedSearch object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return SavedSearch
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static SavedSearch createSavedSearch(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return savedSearchConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a Trend object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return Trend
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Trend createTrend(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return trendConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a Trends object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return Trends
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Trends createTrends(String rawJSON) throws TwitterException {
        try {
            return trendsConstructor.newInstance(rawJSON);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Constructs a IDs object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return IDs
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static IDs createIDs(String rawJSON) throws TwitterException {
        try {
            return IDsConstructor.newInstance(rawJSON);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Constructs a RateLimitStatus object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return RateLimitStatus
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static RateLimitStatus createRateLimitStatus(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return rateLimitStatusConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a Category object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return Category
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Category createCategory(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return categoryConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a DirectMessage object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return DirectMessage
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static DirectMessage createDirectMessage(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return directMessageConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a Location object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return Location
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Location createLocation(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return locationConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Constructs a UserList object from rawJSON string.
     * @param rawJSON raw JSON form as String
     * @return UserList
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static UserList createUserList(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return userListConstructor.newInstance(json);
        } catch (InstantiationException e) {
            throw new TwitterException(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }


    /**
     * clear raw JSON forms associated with the current thread.<br>
     * Currently this method is called indirectly by twitter4j.internal.util.DataObjectFactoryUtil, and should be called directly once *JSONImpl classes are migrated to twitter4j.json.* package.
     * @since Twitter4J 2.1.7
     */
    static void clearThreadLocalMap() {
        rawJsonMap.get().clear();
    }

    /**
     * associate a raw JSON form to the current thread<br>
     * Currently this method is called indirectly by twitter4j.internal.util.DataObjectFactoryUtil, and should be called directly once *JSONImpl classes are migrated to twitter4j.json.* package.
     * @since Twitter4J 2.1.7
     */
    static <T> T registerJSONObject(T key, Object json) {
        rawJsonMap.get().put(key, json);
        return key;
    }
}