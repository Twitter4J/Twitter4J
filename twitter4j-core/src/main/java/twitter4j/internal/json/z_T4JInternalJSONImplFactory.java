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
package twitter4j.internal.json;

import twitter4j.*;
import twitter4j.api.HelpMethods;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.util.z_T4JInternalStringUtil;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class z_T4JInternalJSONImplFactory implements z_T4JInternalFactory {
    private Configuration conf;

    public z_T4JInternalJSONImplFactory(Configuration conf) {
        this.conf = conf;
    }

    public Status createStatus(JSONObject json) throws TwitterException {
        return new StatusJSONImpl(json);
    }

    public User createUser(JSONObject json) throws TwitterException {
        return new UserJSONImpl(json);
    }

    public UserList createAUserList(JSONObject json) throws TwitterException {
        return new UserListJSONImpl(json);
    }

    public DirectMessage createDirectMessage(JSONObject json) throws TwitterException {
        return new DirectMessageJSONImpl(json);
    }

    public RateLimitStatus createRateLimitStatus(HttpResponse res) throws TwitterException {
        return new RateLimitStatusJSONImpl(res, conf);
    }

    public Status createStatus(HttpResponse res) throws TwitterException {
        return new StatusJSONImpl(res, conf);
    }

    public ResponseList<Status> createStatusList(HttpResponse res) throws TwitterException {
        return StatusJSONImpl.createStatusList(res, conf);
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
            if (!json.isNull("geo")) {
                String coordinates = json.getJSONObject("geo")
                        .getString("coordinates");
                coordinates = coordinates.substring(1, coordinates.length() - 1);
                String[] point = z_T4JInternalStringUtil.split(coordinates, ",");
                return new GeoLocation(Double.parseDouble(point[0]),
                        Double.parseDouble(point[1]));
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

    public static RateLimitStatus createFeatureSpecificRateLimitStatusFromResponseHeader(HttpResponse res) {
        return RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(res);
    }

    public Trends createTrends(HttpResponse res) throws TwitterException {
        return new TrendsJSONImpl(res, conf);
    }

    public ResponseList<Trends> createTrendsList(HttpResponse res) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(res, conf.isJSONStoreEnabled());
    }

    public User createUser(HttpResponse res) throws TwitterException {
        return new UserJSONImpl(res, conf);
    }

    public ResponseList<User> createUserList(HttpResponse res) throws TwitterException {
        return UserJSONImpl.createUserList(res, conf);
    }

    public ResponseList<User> createUserListFromJSONArray(HttpResponse res) throws TwitterException {
        return UserJSONImpl.createUserList(res.asJSONArray(), res, conf);
    }

    public ResponseList<User> createUserListFromJSONArray_Users(HttpResponse res) throws TwitterException {
        try {
            return UserJSONImpl.createUserList(res.asJSONObject().getJSONArray("users"), res, conf);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    public QueryResult createQueryResult(HttpResponse res, Query query) throws TwitterException {
        try {
            return new QueryResultJSONImpl(res, conf);
        } catch (TwitterException te) {
            if (404 == te.getStatusCode()) {
                return new QueryResultJSONImpl(query);
            } else {
                throw te;
            }
        }
    }

    public IDs createIDs(HttpResponse res) throws TwitterException {
        return new IDsJSONImpl(res, conf);
    }

    public PagableResponseList<User> createPagableUserList(HttpResponse res) throws TwitterException {
        return UserJSONImpl.createPagableUserList(res, conf);
    }

    public UserList createAUserList(HttpResponse res) throws TwitterException {
        return new UserListJSONImpl(res, conf);
    }

    public PagableResponseList<UserList> createPagableUserListList(HttpResponse res) throws TwitterException {
        return UserListJSONImpl.createPagableUserListList(res, conf);
    }

    public ResponseList<UserList> createUserListList(HttpResponse res) throws TwitterException {
        return UserListJSONImpl.createUserListList(res, conf);
    }

    public ResponseList<Category> createCategoryList(HttpResponse res) throws TwitterException {
        return CategoryJSONImpl.createCategoriesList(res, conf);
    }

    public ProfileImage createProfileImage(HttpResponse res) throws TwitterException {
        return new ProfileImageImpl(res);
    }

    public DirectMessage createDirectMessage(HttpResponse res) throws TwitterException {
        return new DirectMessageJSONImpl(res, conf);
    }

    public ResponseList<DirectMessage> createDirectMessageList(HttpResponse res) throws TwitterException {
        return DirectMessageJSONImpl.createDirectMessageList(res, conf);
    }

    public Relationship createRelationship(HttpResponse res) throws TwitterException {
        return new RelationshipJSONImpl(res, conf);
    }

    public ResponseList<Friendship> createFriendshipList(HttpResponse res) throws TwitterException {
        return FriendshipJSONImpl.createFriendshipList(res, conf);
    }

    public AccountTotals createAccountTotals(HttpResponse res) throws TwitterException {
        return new AccountTotalsJSONImpl(res, conf);
    }

    public AccountSettings createAccountSettings(HttpResponse res) throws TwitterException {
        return new AccountSettingsJSONImpl(res, conf);
    }

    public SavedSearch createSavedSearch(HttpResponse res) throws TwitterException {
        return new SavedSearchJSONImpl(res, conf);
    }

    public ResponseList<SavedSearch> createSavedSearchList(HttpResponse res) throws TwitterException {
        return SavedSearchJSONImpl.createSavedSearchList(res, conf);
    }

    public ResponseList<Location> createLocationList(HttpResponse res) throws TwitterException {
        return LocationJSONImpl.createLocationList(res, conf);
    }

    public Place createPlace(HttpResponse res) throws TwitterException {
        return new PlaceJSONImpl(res, conf);
    }

    public ResponseList<Place> createPlaceList(HttpResponse res) throws TwitterException {
        try {
            return PlaceJSONImpl.createPlaceList(res, conf);
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return new ResponseListImpl<Place>(0, null);
            } else {
                throw te;
            }
        }
    }

    public SimilarPlaces createSimilarPlaces(HttpResponse res) throws TwitterException {
        return SimilarPlacesImpl.createSimilarPlaces(res, conf);
    }

    public RelatedResults createRelatedResults(HttpResponse res) throws TwitterException {
        return new RelatedResultsJSONImpl(res, conf);
    }

    public TwitterAPIConfiguration createTwitterAPIConfiguration(HttpResponse res) throws TwitterException {
        return new TwitterAPIConfigurationJSONImpl(res, conf);
    }

    public ResponseList<HelpMethods.Language> createLanguageList(HttpResponse res) throws TwitterException {
        return LanguageJSONImpl.createLanguageList(res, conf);
    }

    public <T> ResponseList<T> createEmptyResponseList() {
        return new ResponseListImpl<T>(0, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof z_T4JInternalJSONImplFactory)) return false;

        z_T4JInternalJSONImplFactory that = (z_T4JInternalJSONImplFactory) o;

        if (conf != null ? !conf.equals(that.conf) : that.conf != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return conf != null ? conf.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "JSONImplFactory{" +
                "conf=" + conf +
                '}';
    }
}
