package twitter4j;

import twitter4j.v1.SpamReportingResource;
import twitter4j.v1.User;

import java.util.List;
import java.util.function.Consumer;

class SpamReportingResourceImpl extends APIResourceBase implements SpamReportingResource {
    SpamReportingResourceImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                              String IMPLICIT_PARAMS_STR,
                              List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                              List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public User reportSpam(long userId) throws TwitterException {
        return factory.createUser(post(restBaseURL + "users/report_spam.json?user_id=" + userId));
    }

    @Override
    public User reportSpam(String screenName) throws TwitterException {
        return factory.createUser(post(restBaseURL + "users/report_spam.json", new HttpParameter("screen_name", screenName)));
    }
}
