package twitter4j;

import twitter4j.v1.ResponseList;
import twitter4j.v1.SavedSearch;
import twitter4j.v1.SavedSearchesResources;

import java.util.List;
import java.util.function.Consumer;

class SavedSearchesResourcesImpl extends APIResourceBase implements SavedSearchesResources {
    SavedSearchesResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                               String IMPLICIT_PARAMS_STR,
                               List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                               List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public ResponseList<SavedSearch> getSavedSearches() throws TwitterException {
        return factory.createSavedSearchList(get(restBaseURL + "saved_searches/list.json"));
    }

    @Override
    public SavedSearch showSavedSearch(long id) throws TwitterException {
        return factory.createSavedSearch(get(restBaseURL + "saved_searches/show/" + id + ".json"));
    }

    @Override
    public SavedSearch createSavedSearch(String query) throws TwitterException {
        return factory.createSavedSearch(post(restBaseURL + "saved_searches/create.json", new HttpParameter("query", query)));
    }

    @Override
    public SavedSearch destroySavedSearch(long id) throws TwitterException {
        return factory.createSavedSearch(post(restBaseURL + "saved_searches/destroy/" + id + ".json"));
    }


}
