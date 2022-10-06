package twitter4j;

import twitter4j.api.FriendsFollowersResources;

import java.util.List;
import java.util.function.Consumer;

import static twitter4j.HttpParameter.getParameterArray;

class FriendsFollowersResourcesImpl extends APIResourceBase implements FriendsFollowersResources {
    FriendsFollowersResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                                  String IMPLICIT_PARAMS_STR,
                                  List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                                  List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public IDs getNoRetweetsFriendships() throws TwitterException {
        return factory.createIDs(get(restBaseURL + "friendships/no_retweets/ids.json"));
    }

    @Override
    public IDs getFriendsIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "friends/ids.json?cursor=" + cursor));
    }

    @Override
    public IDs getFriendsIDs(long userId, long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "friends/ids.json?user_id=" + userId + "&cursor=" + cursor));
    }

    @Override
    public IDs getFriendsIDs(long userId, long cursor, int count) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "friends/ids.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public IDs getFriendsIDs(String screenName, long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "friends/ids.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor)));
    }

    @Override
    public IDs getFriendsIDs(String screenName, long cursor, int count) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "friends/ids.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }

    @Override
    public IDs getFollowersIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "followers/ids.json?cursor=" + cursor));
    }

    @Override
    public IDs getFollowersIDs(long userId, long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "followers/ids.json?user_id=" + userId + "&cursor=" + cursor));
    }

    @Override
    public IDs getFollowersIDs(long userId, long cursor, int count) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "followers/ids.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public IDs getFollowersIDs(String screenName, long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "followers/ids.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor)));
    }

    @Override
    public IDs getFollowersIDs(String screenName, long cursor, int count) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "followers/ids.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }

    @Override
    public ResponseList<Friendship> lookupFriendships(long... ids) throws TwitterException {
        return factory.createFriendshipList(get(restBaseURL + "friendships/lookup.json?user_id=" + StringUtil.join(ids)));
    }

    @Override
    public ResponseList<Friendship> lookupFriendships(String... screenNames) throws TwitterException {
        return factory.createFriendshipList(get(restBaseURL + "friendships/lookup.json?screen_name=" + StringUtil.join(screenNames)));
    }

    @Override
    public IDs getIncomingFriendships(long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "friendships/incoming.json?cursor=" + cursor));
    }

    @Override
    public IDs getOutgoingFriendships(long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "friendships/outgoing.json?cursor=" + cursor));
    }

    @Override
    public User createFriendship(long userId) throws TwitterException {
        return factory.createUser(post(restBaseURL + "friendships/create.json?user_id=" + userId));
    }

    @Override
    public User createFriendship(String screenName) throws TwitterException {
        return factory.createUser(post(restBaseURL + "friendships/create.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public User createFriendship(long userId, boolean follow) throws TwitterException {
        return factory.createUser(post(restBaseURL + "friendships/create.json?user_id=" + userId + "&follow=" + follow));
    }

    @Override
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        return factory.createUser(post(restBaseURL + "friendships/create.json", new HttpParameter("screen_name", screenName), new HttpParameter("follow", follow)));
    }

    @Override
    public User destroyFriendship(long userId) throws TwitterException {
        return factory.createUser(post(restBaseURL + "friendships/destroy.json?user_id=" + userId));
    }

    @Override
    public User destroyFriendship(String screenName) throws TwitterException {
        return factory.createUser(post(restBaseURL + "friendships/destroy.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public Relationship updateFriendship(long userId, boolean enableDeviceNotification
            , boolean retweets) throws TwitterException {
        return factory.createRelationship((post(restBaseURL + "friendships/update.json", new HttpParameter("user_id", userId), new HttpParameter("device", enableDeviceNotification), new HttpParameter("retweets", retweets))));
    }

    @Override
    public Relationship updateFriendship(String screenName, boolean enableDeviceNotification
            , boolean retweets) throws TwitterException {
        return factory.createRelationship(post(restBaseURL + "friendships/update.json", new HttpParameter("screen_name", screenName), new HttpParameter("device", enableDeviceNotification), new HttpParameter("retweets", retweets)));
    }

    @Override
    public Relationship showFriendship(long sourceId, long targetId) throws TwitterException {
        return factory.createRelationship(get(restBaseURL + "friendships/show.json", new HttpParameter("source_id", sourceId), new HttpParameter("target_id", targetId)));
    }

    @Override
    public Relationship showFriendship(String sourceScreenName, String targetScreenName) throws TwitterException {
        return factory.createRelationship(get(restBaseURL + "friendships/show.json", getParameterArray("source_screen_name", sourceScreenName, "target_screen_name", targetScreenName)));
    }

    @Override
    public PagableResponseList<User> getFriendsList(long userId, long cursor) throws TwitterException {
        return getFriendsList(userId, cursor, 20);
    }

    @Override
    public PagableResponseList<User> getFriendsList(long userId, long cursor, int count) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "friends/list.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public PagableResponseList<User> getFriendsList(String screenName, long cursor) throws TwitterException {
        return getFriendsList(screenName, cursor, 20);
    }

    @Override
    public PagableResponseList<User> getFriendsList(String screenName, long cursor, int count) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "friends/list.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }

    @Override
    public PagableResponseList<User> getFriendsList(long userId, long cursor, int count, boolean skipStatus, boolean includeUserEntities) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "friends/list.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count + "&skip_status=" + skipStatus + "&include_user_entities=" + includeUserEntities));
    }

    @Override
    public PagableResponseList<User> getFriendsList(String screenName, long cursor, int count,
                                                    boolean skipStatus, boolean includeUserEntities) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "friends/list.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count), new HttpParameter("skip_status", skipStatus), new HttpParameter("include_user_entities", includeUserEntities)));
    }

    @Override
    public PagableResponseList<User> getFollowersList(long userId, long cursor) throws TwitterException {
        return getFollowersList(userId, cursor, 20);
    }

    @Override
    public PagableResponseList<User> getFollowersList(String screenName, long cursor) throws TwitterException {
        return getFollowersList(screenName, cursor, 20);
    }

    @Override
    public PagableResponseList<User> getFollowersList(long userId, long cursor, int count) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "followers/list.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public PagableResponseList<User> getFollowersList(String screenName, long cursor, int count) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "followers/list.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }

    @Override
    public PagableResponseList<User> getFollowersList(long userId, long cursor, int count,
                                                      boolean skipStatus, boolean includeUserEntities) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "followers/list.json?user_id=" + userId + "&cursor=" + cursor + "&count=" + count + "&skip_status=" + skipStatus + "&include_user_entities=" + includeUserEntities));
    }

    @Override
    public PagableResponseList<User> getFollowersList(String screenName, long cursor, int count,
                                                      boolean skipStatus, boolean includeUserEntities) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "followers/list.json", new HttpParameter("screen_name", screenName), new HttpParameter("cursor", cursor), new HttpParameter("count", count), new HttpParameter("skip_status", skipStatus), new HttpParameter("include_user_entities", includeUserEntities)));
    }

}
