package twitter4j;

import twitter4j.v1.FavoritesResources;
import twitter4j.v1.Paging;
import twitter4j.v1.ResponseList;
import twitter4j.v1.Status;

import java.util.List;
import java.util.function.Consumer;

class FavoritesResourcesImpl extends  APIResourceBase implements FavoritesResources {
    FavoritesResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                       String IMPLICIT_PARAMS_STR,
                       List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                       List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }
    @Override
    public ResponseList<Status> getFavorites() throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "favorites/list.json"));
    }

    @Override
    public ResponseList<Status> getFavorites(long userId) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "favorites/list.json?user_id=" + userId));
    }

    @Override
    public ResponseList<Status> getFavorites(String screenName) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "favorites/list.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public ResponseList<Status> getFavorites(Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "favorites/list.json", TimelinesResourcesImpl.asPostParameterArray(paging)));
    }

    @Override
    public ResponseList<Status> getFavorites(long userId, Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "favorites/list.json", mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId)}, TimelinesResourcesImpl.asPostParameterArray(paging))));
    }

    @Override
    public ResponseList<Status> getFavorites(String screenName, Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "favorites/list.json", mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName)}, TimelinesResourcesImpl.asPostParameterArray(paging))));
    }

    @Override
    public Status destroyFavorite(long id) throws TwitterException {
        return factory.createStatus(post(restBaseURL + "favorites/destroy.json?id=" + id));
    }

    @Override
    public Status createFavorite(long id) throws TwitterException {
        return factory.createStatus(post(restBaseURL + "favorites/create.json?id=" + id));
    }


}
