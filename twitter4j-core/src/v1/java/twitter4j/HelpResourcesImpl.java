package twitter4j;

import twitter4j.api.HelpResources;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class HelpResourcesImpl extends APIResourceBase implements HelpResources {

    HelpResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                      String IMPLICIT_PARAMS_STR,
                      List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                      List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }
    /* Help Resources */

    @Override
    public ResponseList<Language> getLanguages() throws TwitterException {
        return factory.createLanguageList(get(restBaseURL + "help/languages.json"));
    }

    @Override
    public Map<String, RateLimitStatus> getRateLimitStatus() throws TwitterException {
        return factory.createRateLimitStatuses(get(restBaseURL + "application/rate_limit_status.json"));
    }

    @Override
    public Map<String, RateLimitStatus> getRateLimitStatus(String... resources) throws TwitterException {
        return factory.createRateLimitStatuses(get(restBaseURL + "application/rate_limit_status.json?resources=" + StringUtil.join(resources)));
    }
}
