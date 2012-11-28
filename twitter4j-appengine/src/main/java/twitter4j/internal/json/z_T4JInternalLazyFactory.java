package twitter4j.internal.json;

import twitter4j.*;
import twitter4j.api.HelpResources;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class z_T4JInternalLazyFactory implements z_T4JInternalFactory {
    private static final long serialVersionUID = 8032710811910749119L;
    private final z_T4JInternalFactory factory;
    private final Configuration conf;

    public z_T4JInternalLazyFactory(Configuration conf) {
        factory = new z_T4JInternalJSONImplFactory(conf);
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

    public Map<String ,RateLimitStatus> createRateLimitStatuses(HttpResponse res) throws TwitterException {
        return factory.createRateLimitStatuses(res);
    }

    public Status createStatus(HttpResponse res) throws TwitterException {
        return new LazyStatus(res, factory);
    }

    public ResponseList<Status> createStatusList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Status>() {
            @Override
            protected ResponseList<Status> createActualResponseList() throws TwitterException {
                return StatusJSONImpl.createStatusList(res, conf);
            }
        };
    }


    public Trends createTrends(HttpResponse res) throws TwitterException {
        return new LazyTrends(res, factory);
    }

    public ResponseList<Trends> createTrendsList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Trends>() {
            @Override
            protected ResponseList<Trends> createActualResponseList() throws TwitterException {
                return TrendsJSONImpl.createTrendsList(res, conf.isJSONStoreEnabled());
            }
        };
    }

    public User createUser(HttpResponse res) throws TwitterException {
        return new LazyUser(res, factory);
    }

    public ResponseList<User> createUserList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<User>() {
            @Override
            protected ResponseList<User> createActualResponseList() throws TwitterException {
                return UserJSONImpl.createUserList(res, conf);
            }
        };
    }

    public ResponseList<User> createUserListFromJSONArray(final HttpResponse res) throws TwitterException {
        return new LazyPagableResponseList<User>() {
            @Override
            protected ResponseList<User> createActualResponseList() throws TwitterException {
                return UserJSONImpl.createUserList(res.asJSONArray(), res, conf);
            }
        };
    }

    public ResponseList<User> createUserListFromJSONArray_Users(final HttpResponse res) throws TwitterException {
        return new LazyPagableResponseList<User>() {
            @Override
            protected ResponseList<User> createActualResponseList() throws TwitterException {
                try {
                    return UserJSONImpl.createUserList(res.asJSONObject().getJSONArray("users"), res, conf);
                } catch (JSONException jsone) {
                    throw new TwitterException(jsone);
                }
            }
        };
    }

    public QueryResult createQueryResult(HttpResponse res, Query query) throws TwitterException {
        return new LazyQueryResult(res, factory, query);
    }

    public IDs createIDs(HttpResponse res) throws TwitterException {
        return new LazyIDs(res, factory);
    }

    public PagableResponseList<User> createPagableUserList(final HttpResponse res) throws TwitterException {
        return new LazyPagableResponseList<User>() {
            @Override
            protected ResponseList<User> createActualResponseList() throws TwitterException {
                return UserJSONImpl.createUserList(res, conf);
            }
        };
    }

    public UserList createAUserList(HttpResponse res) throws TwitterException {
        return new LazyUserList(res, factory);
    }

    public PagableResponseList<UserList> createPagableUserListList(final HttpResponse res) throws TwitterException {
        return new LazyPagableResponseList<UserList>() {
            @Override
            protected PagableResponseList<UserList> createActualResponseList() throws TwitterException {
                return UserListJSONImpl.createPagableUserListList(res, conf);
            }
        };
    }

    public ResponseList<UserList> createUserListList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<UserList>() {
            @Override
            protected ResponseList<UserList> createActualResponseList() throws TwitterException {
                return UserListJSONImpl.createUserListList(res, conf);
            }
        };
    }

    public ResponseList<Category> createCategoryList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Category>() {
            @Override
            protected ResponseList<Category> createActualResponseList() throws TwitterException {
                return CategoryJSONImpl.createCategoriesList(res, conf);
            }
        };
    }

    public DirectMessage createDirectMessage(HttpResponse res) throws TwitterException {
        return new LazyDirectMessage(res, factory);
    }

    public ResponseList<DirectMessage> createDirectMessageList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<DirectMessage>() {
            @Override
            protected ResponseList<DirectMessage> createActualResponseList() throws TwitterException {
                return DirectMessageJSONImpl.createDirectMessageList(res, conf);
            }
        };
    }

    public Relationship createRelationship(HttpResponse res) throws TwitterException {
        return new LazyRelationship(res, factory);
    }

    public ResponseList<Friendship> createFriendshipList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Friendship>() {
            @Override
            protected ResponseList<Friendship> createActualResponseList() throws TwitterException {
                return FriendshipJSONImpl.createFriendshipList(res, conf);
            }
        };
    }

    public AccountTotals createAccountTotals(HttpResponse res) throws TwitterException {
        return new LazyAccountTotals(res, factory);
    }

    public AccountSettings createAccountSettings(HttpResponse res) throws TwitterException {
        return new LazyAccountSettings(res, factory);
    }

    public SavedSearch createSavedSearch(HttpResponse res) throws TwitterException {
        return new LazySavedSearch(res, factory);
    }

    public ResponseList<SavedSearch> createSavedSearchList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<SavedSearch>() {
            @Override
            protected ResponseList<SavedSearch> createActualResponseList() throws TwitterException {
                return SavedSearchJSONImpl.createSavedSearchList(res, conf);
            }
        };
    }

    public ResponseList<Location> createLocationList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Location>() {
            @Override
            protected ResponseList<Location> createActualResponseList() throws TwitterException {
                return LocationJSONImpl.createLocationList(res, conf);
            }
        };
    }

    public Place createPlace(HttpResponse res) throws TwitterException {
        return new LazyPlace(res, factory);
    }

    public ResponseList<Place> createPlaceList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Place>() {
            @Override
            protected ResponseList<Place> createActualResponseList() throws TwitterException {
                return PlaceJSONImpl.createPlaceList(res, conf);
            }
        };
    }

    public <T> ResponseList<T> createEmptyResponseList() {
        return new ResponseListImpl<T>(0, null);
    }

    @Override
    public OEmbed createOEmbed(HttpResponse res) throws TwitterException {
        return new LazyOEmbed(res, factory);
    }

    public SimilarPlaces createSimilarPlaces(HttpResponse res) throws TwitterException {
        return new LazySimilarPlaces(res, factory);
    }

    public RelatedResults createRelatedResults(HttpResponse res) throws TwitterException {
        return new LazyRelatedResults(res, factory);
    }

    public TwitterAPIConfiguration createTwitterAPIConfiguration(HttpResponse res) throws TwitterException {
        return new LazyTwitterAPIConfiguration(res, factory);
    }

    public ResponseList<HelpResources.Language> createLanguageList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<HelpResources.Language>() {
            @Override
            protected ResponseList<HelpResources.Language> createActualResponseList() throws TwitterException {
                return LanguageJSONImpl.createLanguageList(res, conf);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof z_T4JInternalLazyFactory)) return false;

        z_T4JInternalLazyFactory that = (z_T4JInternalLazyFactory) o;

        if (factory != null ? !factory.equals(that.factory) : that.factory != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return factory != null ? factory.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LazyFactory{" +
                "factory=" + factory +
                '}';
    }
}