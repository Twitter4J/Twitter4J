package twitter4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 4.0.0
 */
public final class TwitterObjectFactory {
    private TwitterObjectFactory() {
        throw new AssertionError("not intended to be instantiated.");
    }

    private static final ThreadLocal<Map> rawJsonMap = new ThreadLocal<Map>() {
        @Override
        protected Map initialValue() {
            return new HashMap();
        }
    };

    /**
     * Returns a raw JSON form of the provided object.<br>
     * Note that raw JSON forms can be retrieved only from the same thread invoked the last method call and will become inaccessible once another method call
     *
     * @param obj target object to retrieve JSON
     * @return raw JSON
     * @since Twitter4J 2.1.7
     */
    public static String getRawJSON(Object obj) {
        if (!registeredAtleastOnce) {
            throw new IllegalStateException("Apparently jsonStoreEnabled is not set to true.");
        }
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
     *
     * @param rawJSON raw JSON form as String
     * @return Status
     * @throws TwitterException when provided string is not a valid JSON string.
     * @since Twitter4J 2.1.7
     */
    public static Status createStatus(String rawJSON) throws TwitterException {
        try {
            return new StatusJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new UserJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new AccountTotalsJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new RelationshipJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new PlaceJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new SavedSearchJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new TrendJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        return new TrendsJSONImpl(rawJSON);
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
        return new IDsJSONImpl(rawJSON);
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
        try {
            return RateLimitStatusJSONImpl.createRateLimitStatuses(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new CategoryJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new DirectMessageJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new LocationJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new UserListJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
        try {
            return new OEmbedJSONImpl(new JSONObject(rawJSON));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
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
     */
    public static Object createObject(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            JSONObjectType.Type jsonObjectType = JSONObjectType.determine(json);
            switch (jsonObjectType) {
                case SENDER:
                    return registerJSONObject(new DirectMessageJSONImpl(json.getJSONObject("direct_message")), json);
                case STATUS:
                    return registerJSONObject(new StatusJSONImpl(json), json);
                case DIRECT_MESSAGE:
                    return registerJSONObject(new DirectMessageJSONImpl(json.getJSONObject("direct_message")), json);
                case DELETE:
                    return registerJSONObject(new StatusDeletionNoticeImpl(json.getJSONObject("delete").getJSONObject("status")), json);
                case LIMIT:
                    // TODO: Perhaps there should be a TrackLimitationNotice object?
                    // The onTrackLimitationNotice method could take that as an arg.
                    return json;
                case SCRUB_GEO:
                    return json;
                default:
                    // The object type is unrecognized...just return the json
                    return json;
            }
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * clear raw JSON forms associated with the current thread.<br>
     *
     * @since Twitter4J 2.1.7
     */
    static void clearThreadLocalMap() {
        rawJsonMap.get().clear();
    }

    private static boolean registeredAtleastOnce = false;

    /**
     * associate a raw JSON form to the current thread<br>
     *
     * @since Twitter4J 2.1.7
     */
    static <T> T registerJSONObject(T key, Object json) {
        registeredAtleastOnce = true;
        rawJsonMap.get().put(key, json);
        return key;
    }
}