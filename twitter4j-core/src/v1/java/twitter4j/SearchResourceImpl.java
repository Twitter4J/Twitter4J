package twitter4j;

import twitter4j.v1.SearchResource;

import java.util.List;
import java.util.function.Consumer;

class SearchResourceImpl extends APIResourceBase implements SearchResource {
    SearchResourceImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                       String IMPLICIT_PARAMS_STR,
                       List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                       List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public QueryResult search(Query query) throws TwitterException {
        if (query.nextPage() != null) {
            return factory.createQueryResult(get(restBaseURL + "search/tweets.json" + query.nextPage()), query);
        } else {
            return factory.createQueryResult(get(restBaseURL + "search/tweets.json", query.asHttpParameterArray()), query);
        }
    }

}
