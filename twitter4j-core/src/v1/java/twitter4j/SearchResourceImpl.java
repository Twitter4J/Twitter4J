package twitter4j;

import twitter4j.v1.Query;
import twitter4j.v1.QueryResult;
import twitter4j.v1.SearchResource;

import java.util.ArrayList;
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
        if (query.nextPageQuery != null) {
            return factory.createQueryResult(get(restBaseURL + "search/tweets.json" + query.nextPageQuery), query);
        } else {
            return factory.createQueryResult(get(restBaseURL + "search/tweets.json", asHttpParameterArray(query)), query);
        }
    }

    private static final HttpParameter WITH_TWITTER_USER_ID = new HttpParameter("with_twitter_user_id", "true");

    /*package*/ static HttpParameter[] asHttpParameterArray(Query query) {
        ArrayList<HttpParameter> params = new ArrayList<>(12);
        appendParameter("q", query.query, params);
        appendParameter("lang", query.lang, params);
        appendParameter("locale", query.locale, params);
        appendParameter("max_id", query.maxId, params);
        appendParameter("count", query.count, params);
        appendParameter("since", query.since, params);
        appendParameter("since_id", query.sinceId, params);
        appendParameter("geocode", query.geocode, params);
        appendParameter("until", query.until, params);
        if (query.resultType != null) {
            params.add(new HttpParameter("result_type", query.resultType.name()));
        }
        params.add(WITH_TWITTER_USER_ID);
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private static void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private static void appendParameter(String name, long value, List<HttpParameter> params) {
        if (0 <= value) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

}
