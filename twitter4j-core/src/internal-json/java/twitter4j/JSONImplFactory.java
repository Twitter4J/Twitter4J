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

import java.util.Map;
import java.util.Objects;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
class JSONImplFactory implements ObjectFactory {
    private static final long serialVersionUID = -1853541456182663343L;
    private final boolean jsonStoreEnabled;

    public JSONImplFactory(boolean jsonStoreEnabled) {
        this.jsonStoreEnabled = jsonStoreEnabled;
    }

    @Override
    public Status createStatus(JSONObject json) throws TwitterException {
        return new StatusJSONImpl(json);
    }

    @Override
    public User createUser(JSONObject json) throws TwitterException {
        return new UserJSONImpl(json);
    }

    @Override
    public UserList createAUserList(JSONObject json) throws TwitterException {
        return new UserListJSONImpl(json);
    }


    @Override
    public Map<String, RateLimitStatus> createRateLimitStatuses(HttpResponse res) throws TwitterException {
        return RateLimitStatusJSONImpl.createRateLimitStatuses(res, jsonStoreEnabled);
    }

    @Override
    public Status createStatus(HttpResponse res) throws TwitterException {
        return new StatusJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<Status> createStatusList(HttpResponse res) throws TwitterException {
        return StatusJSONImpl.createStatusList(res, jsonStoreEnabled);
    }

    /**
     * returns a GeoLocation instance if a "geo" element is found.
     *
     * @param json JSONObject to be parsed
     * @return GeoLocation instance
     * @throws TwitterException when coordinates is not included in geo element (should be an API side issue)
     */
    /*package*/
    static GeoLocation createGeoLocation(JSONObject json) throws TwitterException {
        try {
            if (!json.isNull("coordinates")) {
                String coordinates = json.getJSONObject("coordinates")
                        .getString("coordinates");
                coordinates = coordinates.substring(1, coordinates.length() - 1);
                String[] point = coordinates.split(",");
                return new GeoLocation(Double.parseDouble(point[1]),
                        Double.parseDouble(point[0]));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        return null;
    }

    /*package*/
    static GeoLocation[][] coordinatesAsGeoLocationArray(JSONArray coordinates) throws TwitterException {
        try {
            GeoLocation[][] boundingBox = new GeoLocation[coordinates.length()][];
            for (int i = 0; i < coordinates.length(); i++) {
                JSONArray array = coordinates.getJSONArray(i);
                boundingBox[i] = new GeoLocation[array.length()];
                for (int j = 0; j < array.length(); j++) {
                    JSONArray coordinate = array.getJSONArray(j);
                    boundingBox[i][j] = new GeoLocation(coordinate.getDouble(1), coordinate.getDouble(0));
                }
            }
            return boundingBox;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    public static RateLimitStatus createRateLimitStatusFromResponseHeader(HttpResponse res) {
        return RateLimitStatusJSONImpl.createFromResponseHeader(res);
    }

    @Override
    public Trends createTrends(HttpResponse res) throws TwitterException {
        return new TrendsJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public User createUser(HttpResponse res) throws TwitterException {
        return new UserJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<User> createUserList(HttpResponse res) throws TwitterException {
        return UserJSONImpl.createUserList(res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<User> createUserListFromJSONArray(HttpResponse res) throws TwitterException {
        return UserJSONImpl.createUserList(res.asJSONArray(), res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<User> createUserListFromJSONArray_Users(HttpResponse res) throws TwitterException {
        try {
            return UserJSONImpl.createUserList(res.asJSONObject().getJSONArray("users"), res, jsonStoreEnabled);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public QueryResult createQueryResult(HttpResponse res, Query query) throws TwitterException {
        try {
            return new QueryResultJSONImpl(res, jsonStoreEnabled);
        } catch (TwitterException te) {
            if (404 == te.getStatusCode()) {
                return new QueryResultJSONImpl(query);
            } else {
                throw te;
            }
        }
    }

    @Override
    public IDs createIDs(HttpResponse res) throws TwitterException {
        return new IDsJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public PagableResponseList<User> createPagableUserList(HttpResponse res) throws TwitterException {
        return UserJSONImpl.createPagableUserList(res, jsonStoreEnabled);
    }

    @Override
    public UserList createAUserList(HttpResponse res) throws TwitterException {
        return new UserListJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public PagableResponseList<UserList> createPagableUserListList(HttpResponse res) throws TwitterException {
        return UserListJSONImpl.createPagableUserListList(res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<UserList> createUserListList(HttpResponse res) throws TwitterException {
        return UserListJSONImpl.createUserListList(res, jsonStoreEnabled);
    }

    @Override
    public DirectMessage createDirectMessage(HttpResponse res) throws TwitterException {
        return new DirectMessageJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public DirectMessageList createDirectMessageList(HttpResponse res) throws TwitterException {
        return DirectMessageJSONImpl.createDirectMessageList(res, jsonStoreEnabled);
    }

    @Override
    public Relationship createRelationship(HttpResponse res) throws TwitterException {
        return new RelationshipJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<Friendship> createFriendshipList(HttpResponse res) throws TwitterException {
        return FriendshipJSONImpl.createFriendshipList(res, jsonStoreEnabled);
    }

    @Override
    public AccountTotals createAccountTotals(HttpResponse res) throws TwitterException {
        return new AccountTotalsJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public AccountSettings createAccountSettings(HttpResponse res) throws TwitterException {
        return new AccountSettingsJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public SavedSearch createSavedSearch(HttpResponse res) throws TwitterException {
        return new SavedSearchJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<SavedSearch> createSavedSearchList(HttpResponse res) throws TwitterException {
        return SavedSearchJSONImpl.createSavedSearchList(res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<Location> createLocationList(HttpResponse res) throws TwitterException {
        return LocationJSONImpl.createLocationList(res, jsonStoreEnabled);
    }

    @Override
    public Place createPlace(HttpResponse res) throws TwitterException {
        return new PlaceJSONImpl(res, jsonStoreEnabled);
    }

    @Override
    public ResponseList<Place> createPlaceList(HttpResponse res) throws TwitterException {
        try {
            return PlaceJSONImpl.createPlaceList(res, jsonStoreEnabled);
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return new ResponseListImpl<>(0, null);
            } else {
                throw te;
            }
        }
    }

    @Override
    public ResponseList<HelpResources.Language> createLanguageList(HttpResponse res) throws TwitterException {
        return LanguageJSONImpl.createLanguageList(res, jsonStoreEnabled);
    }

    @Override
    public <T> ResponseList<T> createEmptyResponseList() {
        return new ResponseListImpl<>(0, null);
    }

    @Override
    public OEmbed createOEmbed(HttpResponse res) throws TwitterException {
        return new OEmbedJSONImpl(res, jsonStoreEnabled);
    }

    /**
     * static factory method for twitter-text-java
     *
     * @return hashtag entity
     * @since Twitter4J 2.2.6
     */
    public static HashtagEntity createHashtagEntity(int start, int end, String text) {
        return new HashtagEntityJSONImpl(start, end, text);
    }

    /**
     * static factory method for twitter-text-java
     *
     * @return user mention entity
     * @since Twitter4J 2.2.6
     */
    public static UserMentionEntity createUserMentionEntity(int start, int end, String name, String screenName,
                                                            long id) {
        return new UserMentionEntityJSONImpl(start, end, name, screenName, id);
    }

    /**
     * static factory method for twitter-text-java
     *
     * @return url entity
     * @since Twitter4J 2.2.6
     */
    public static URLEntity createUrlEntity(int start, int end, String url, String expandedURL, String displayURL) {
        return new URLEntityJSONImpl(start, end, url, expandedURL, displayURL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONImplFactory that = (JSONImplFactory) o;
        return jsonStoreEnabled == that.jsonStoreEnabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonStoreEnabled);
    }

    @Override
    public String toString() {
        return "JSONImplFactory{" +
                "jsonStoreEnabled=" + jsonStoreEnabled +
                '}';
    }
}
