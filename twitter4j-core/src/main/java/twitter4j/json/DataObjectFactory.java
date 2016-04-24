/*
 * Copyright 2007 Yusuke Yamamoto
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

package twitter4j.json;

import twitter4j.*;

import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 * @deprecated use {@link twitter4j.TwitterObjectFactory} instead
 */
public final class DataObjectFactory {
    private DataObjectFactory() {
        throw new AssertionError("not intended to be instantiated.");
    }

    /**
     * Returns a raw JSON form of the provided object.<br>
     * Note that raw JSON forms can be retrieved only from the same thread invoked the last method call and will become inaccessible once another method call
     *
     * @param obj target object to retrieve JSON
     * @return raw JSON
     * @since Twitter4J 2.1.7
     */
    public static String getRawJSON(Object obj) {
        return TwitterObjectFactory.getRawJSON(obj);
    }

    /**
     * Constructs a Status object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return Status
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Status createStatus(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createStatus(rawJSON);
    }

    /**
     * Constructs a User object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return User
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static User createUser(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createUser(rawJSON);
    }

    /**
     * Constructs an AccountTotals object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return AccountTotals
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.9
     */
    public static AccountTotals createAccountTotals(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createAccountTotals(rawJSON);
    }

    /**
     * Constructs a Relationship object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return Relationship
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Relationship createRelationship(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createRelationship(rawJSON);
    }

    /**
     * Constructs a Place object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return Place
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Place createPlace(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createPlace(rawJSON);
    }

    /**
     * Constructs a SavedSearch object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return SavedSearch
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static SavedSearch createSavedSearch(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createSavedSearch(rawJSON);
    }

    /**
     * Constructs a Trend object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return Trend
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Trend createTrend(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createTrend(rawJSON);
    }

    /**
     * Constructs a Trends object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return Trends
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Trends createTrends(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createTrends(rawJSON);
    }

    /**
     * Constructs a IDs object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return IDs
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static IDs createIDs(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createIDs(rawJSON);
    }

    /**
     * Constructs a RateLimitStatus object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return RateLimitStatus
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Map<String, RateLimitStatus> createRateLimitStatus(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createRateLimitStatus(rawJSON);
    }

    /**
     * Constructs a Category object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return Category
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Category createCategory(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createCategory(rawJSON);
    }

    /**
     * Constructs a DirectMessage object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return DirectMessage
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static DirectMessage createDirectMessage(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createDirectMessage(rawJSON);
    }

    /**
     * Constructs a Location object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return Location
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Location createLocation(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createLocation(rawJSON);
    }

    /**
     * Constructs a UserList object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return UserList
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static UserList createUserList(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createUserList(rawJSON);
    }

    /**
     * Constructs an OEmbed object from rawJSON string.
     *
     * @param rawJSON raw JSON form as String
     * @return OEmbed
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 3.0.2
     */
    public static OEmbed createOEmbed(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createOEmbed(rawJSON);
    }

    /**
     * Construct an object from rawJSON string.  This method may be called
     * when you do not know what a given raw JSON string contains.  It will
     * do the work of determining what type of object the JSON represents,
     * and constructing the respective object type.  For example, if the JSON
     * contents represents a Status, then a Status will be returned.  If it
     * represents a deletion notice, then a StatusDeletionNotice will be
     * returned.  The caller can simply use instanceof to handle the returned
     * object as applicable.
     * NOTE: the raw JSONObject will be returned in cases where there isn't
     * a discrete respective object type that can be constructed.  That way,
     * the caller can at least have access to the JSON itself.
     *
     * @param rawJSON raw JSON form as String
     * @return the respective constructed object, or the JSONObject in the
     * case where we cannot determine the object type.
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.9
     * @deprecated use {@link twitter4j.TwitterObjectFactory#createObject(String)} instead
     */
    public static Object createObject(String rawJSON) throws TwitterException {
        return TwitterObjectFactory.createObject(rawJSON);
    }

}