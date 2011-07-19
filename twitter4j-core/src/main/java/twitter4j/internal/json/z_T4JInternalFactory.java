package twitter4j.internal.json;

import twitter4j.*;
import twitter4j.api.HelpMethods;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONObject;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public interface z_T4JInternalFactory extends java.io.Serializable {
    Status createStatus(JSONObject json) throws TwitterException;

    User createUser(JSONObject json) throws TwitterException;

    UserList createAUserList(JSONObject json) throws TwitterException;

    DirectMessage createDirectMessage(JSONObject json) throws TwitterException;

    RateLimitStatus createRateLimitStatus(HttpResponse res) throws TwitterException;

    Status createStatus(HttpResponse res) throws TwitterException;

    ResponseList<Status> createStatusList(HttpResponse res) throws TwitterException;

    Trends createTrends(HttpResponse res) throws TwitterException;

    ResponseList<Trends> createTrendsList(HttpResponse res) throws TwitterException;

    User createUser(HttpResponse res) throws TwitterException;

    ResponseList<User> createUserList(HttpResponse res) throws TwitterException;

    ResponseList<User> createUserListFromJSONArray(HttpResponse res) throws TwitterException;

    ResponseList<User> createUserListFromJSONArray_Users(HttpResponse res) throws TwitterException;

    QueryResult createQueryResult(HttpResponse res, Query query) throws TwitterException;

    IDs createIDs(HttpResponse res) throws TwitterException;

    PagableResponseList<User> createPagableUserList(HttpResponse res) throws TwitterException;

    UserList createAUserList(HttpResponse res) throws TwitterException;

    PagableResponseList<UserList> createPagableUserListList(HttpResponse res) throws TwitterException;

    ResponseList<UserList> createUserListList(HttpResponse res) throws TwitterException;

    ResponseList<Category> createCategoryList(HttpResponse res) throws TwitterException;

    ProfileImage createProfileImage(HttpResponse res) throws TwitterException;

    DirectMessage createDirectMessage(HttpResponse res) throws TwitterException;

    ResponseList<DirectMessage> createDirectMessageList(HttpResponse res) throws TwitterException;

    Relationship createRelationship(HttpResponse res) throws TwitterException;

    ResponseList<Friendship> createFriendshipList(HttpResponse res) throws TwitterException;

    AccountTotals createAccountTotals(HttpResponse res) throws TwitterException;

    AccountSettings createAccountSettings(HttpResponse res) throws TwitterException;

    SavedSearch createSavedSearch(HttpResponse res) throws TwitterException;

    ResponseList<SavedSearch> createSavedSearchList(HttpResponse res) throws TwitterException;

    ResponseList<Location> createLocationList(HttpResponse res) throws TwitterException;

    Place createPlace(HttpResponse res) throws TwitterException;

    ResponseList<Place> createPlaceList(HttpResponse res) throws TwitterException;

    SimilarPlaces createSimilarPlaces(HttpResponse res) throws TwitterException;

    RelatedResults createRelatedResults(HttpResponse res) throws TwitterException;

    TwitterAPIConfiguration createTwitterAPIConfiguration(HttpResponse res) throws TwitterException;

    ResponseList<HelpMethods.Language> createLanguageList(HttpResponse res) throws TwitterException;

}
