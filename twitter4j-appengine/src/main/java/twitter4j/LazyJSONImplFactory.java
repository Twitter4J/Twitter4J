package twitter4j;

import twitter4j.api.HelpResources;
import twitter4j.conf.Configuration;

import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
class LazyJSONImplFactory implements ObjectFactory {
    private static final long serialVersionUID = -8140872181877586582L;
    private final ObjectFactory factory;
    private final Configuration conf;

    public LazyJSONImplFactory(Configuration conf) {
        factory = new JSONImplFactory(conf);
        this.conf = conf;
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
        return factory.createRateLimitStatuses(res);
    }

    @Override
    public Status createStatus(HttpResponse res) throws TwitterException {
        return new LazyStatus(res, factory);
    }

    @Override
    public ResponseList<Status> createStatusList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Status>() {
            private static final long serialVersionUID = 7409380696188770069L;

            @Override
            protected ResponseList<Status> createActualResponseList() throws TwitterException {
                return StatusJSONImpl.createStatusList(res, conf);
            }
        };
    }


    @Override
    public Trends createTrends(HttpResponse res) throws TwitterException {
        return new LazyTrends(res, factory);
    }

    public ResponseList<Trends> createTrendsList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Trends>() {
            private static final long serialVersionUID = 4409084320158745041L;

            @Override
            protected ResponseList<Trends> createActualResponseList() throws TwitterException {
                return TrendsJSONImpl.createTrendsList(res, conf.isJSONStoreEnabled());
            }
        };
    }

    @Override
    public User createUser(HttpResponse res) throws TwitterException {
        return new LazyUser(res, factory);
    }

    @Override
    public ResponseList<User> createUserList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<User>() {
            private static final long serialVersionUID = -2342604239975503212L;

            @Override
            protected ResponseList<User> createActualResponseList() throws TwitterException {
                return UserJSONImpl.createUserList(res, conf);
            }
        };
    }

    @Override
    public ResponseList<User> createUserListFromJSONArray(final HttpResponse res) throws TwitterException {
        return new LazyPagableResponseList<User>() {
            private static final long serialVersionUID = -4560012795625599671L;

            @Override
            protected ResponseList<User> createActualResponseList() throws TwitterException {
                return UserJSONImpl.createUserList(res.asJSONArray(), res, conf);
            }
        };
    }

    @Override
    public ResponseList<User> createUserListFromJSONArray_Users(final HttpResponse res) throws TwitterException {
        return new LazyPagableResponseList<User>() {
            private static final long serialVersionUID = 3138828209116873524L;

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

    @Override
    public QueryResult createQueryResult(HttpResponse res, Query query) throws TwitterException {
        return new LazyQueryResult(res, factory, query);
    }

    @Override
    public IDs createIDs(HttpResponse res) throws TwitterException {
        return new LazyIDs(res, factory);
    }

    @Override
    public PagableResponseList<User> createPagableUserList(final HttpResponse res) throws TwitterException {
        return new LazyPagableResponseList<User>() {
            private static final long serialVersionUID = 5621780922424425436L;

            @Override
            protected ResponseList<User> createActualResponseList() throws TwitterException {
                return UserJSONImpl.createUserList(res, conf);
            }
        };
    }

    @Override
    public UserList createAUserList(HttpResponse res) throws TwitterException {
        return new LazyUserList(res, factory);
    }

    @Override
    public PagableResponseList<UserList> createPagableUserListList(final HttpResponse res) throws TwitterException {
        return new LazyPagableResponseList<UserList>() {
            private static final long serialVersionUID = -1744513840306430678L;

            @Override
            protected PagableResponseList<UserList> createActualResponseList() throws TwitterException {
                return UserListJSONImpl.createPagableUserListList(res, conf);
            }
        };
    }

    @Override
    public ResponseList<UserList> createUserListList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<UserList>() {
            private static final long serialVersionUID = -688537134684737190L;

            @Override
            protected ResponseList<UserList> createActualResponseList() throws TwitterException {
                return UserListJSONImpl.createUserListList(res, conf);
            }
        };
    }

    @Override
    public ResponseList<Category> createCategoryList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Category>() {
            private static final long serialVersionUID = -499989158452701469L;

            @Override
            protected ResponseList<Category> createActualResponseList() throws TwitterException {
                return CategoryJSONImpl.createCategoriesList(res, conf);
            }
        };
    }

    @Override
    public DirectMessage createDirectMessage(HttpResponse res) throws TwitterException {
        return new LazyDirectMessage(res, factory);
    }

    @Override
    public ResponseList<DirectMessage> createDirectMessageList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<DirectMessage>() {
            private static final long serialVersionUID = -4033757208726428299L;

            @Override
            protected ResponseList<DirectMessage> createActualResponseList() throws TwitterException {
                return DirectMessageJSONImpl.createDirectMessageList(res, conf);
            }
        };
    }

    @Override
    public Relationship createRelationship(HttpResponse res) throws TwitterException {
        return new LazyRelationship(res, factory);
    }

    @Override
    public ResponseList<Friendship> createFriendshipList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Friendship>() {
            private static final long serialVersionUID = 2000670419499499826L;

            @Override
            protected ResponseList<Friendship> createActualResponseList() throws TwitterException {
                return FriendshipJSONImpl.createFriendshipList(res, conf);
            }
        };
    }

    @Override
    public AccountTotals createAccountTotals(HttpResponse res) throws TwitterException {
        return new LazyAccountTotals(res, factory);
    }

    @Override
    public AccountSettings createAccountSettings(HttpResponse res) throws TwitterException {
        return new LazyAccountSettings(res, factory);
    }

    @Override
    public SavedSearch createSavedSearch(HttpResponse res) throws TwitterException {
        return new LazySavedSearch(res, factory);
    }

    @Override
    public ResponseList<SavedSearch> createSavedSearchList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<SavedSearch>() {
            private static final long serialVersionUID = 5885155689227833223L;

            @Override
            protected ResponseList<SavedSearch> createActualResponseList() throws TwitterException {
                return SavedSearchJSONImpl.createSavedSearchList(res, conf);
            }
        };
    }

    @Override
    public ResponseList<Location> createLocationList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Location>() {
            private static final long serialVersionUID = -5502050661491788149L;

            @Override
            protected ResponseList<Location> createActualResponseList() throws TwitterException {
                return LocationJSONImpl.createLocationList(res, conf);
            }
        };
    }

    @Override
    public Place createPlace(HttpResponse res) throws TwitterException {
        return new LazyPlace(res, factory);
    }

    @Override
    public ResponseList<Place> createPlaceList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<Place>() {
            private static final long serialVersionUID = 3477694658457823153L;

            @Override
            protected ResponseList<Place> createActualResponseList() throws TwitterException {
                return PlaceJSONImpl.createPlaceList(res, conf);
            }
        };
    }

    @Override
    public <T> ResponseList<T> createEmptyResponseList() {
        return new ResponseListImpl<T>(0, null);
    }

    @Override
    public OEmbed createOEmbed(HttpResponse res) throws TwitterException {
        return new LazyOEmbed(res, factory);
    }

    @Override
    public TwitterAPIConfiguration createTwitterAPIConfiguration(HttpResponse res) throws TwitterException {
        return new LazyTwitterAPIConfiguration(res, factory);
    }

    @Override
    public ResponseList<HelpResources.Language> createLanguageList(final HttpResponse res) throws TwitterException {
        return new LazyResponseList<HelpResources.Language>() {
            private static final long serialVersionUID = -7722971198897121377L;

            @Override
            protected ResponseList<HelpResources.Language> createActualResponseList() throws TwitterException {
                return LanguageJSONImpl.createLanguageList(res, conf);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LazyJSONImplFactory)) return false;

        LazyJSONImplFactory that = (LazyJSONImplFactory) o;

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