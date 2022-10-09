package twitter4j;

import twitter4j.v1.TimelinesResources;

import java.util.List;
import java.util.function.Consumer;

class TimelinesResourcesImpl extends APIResourceBase implements TimelinesResources {


    TimelinesResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                           String IMPLICIT_PARAMS_STR,
                           List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                           List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public ResponseList<Status> getMentionsTimeline() throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/mentions_timeline.json"));
    }

    @Override
    public ResponseList<Status> getMentionsTimeline(Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/mentions_timeline.json", paging.asPostParameterArray()));
    }

    @Override
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/home_timeline.json", includeMyRetweet));
    }

    @Override
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/home_timeline.json", mergeParameters(paging.asPostParameterArray(), includeMyRetweet)));
    }

    @Override
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/retweets_of_me.json"));
    }

    @Override
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/retweets_of_me.json", paging.asPostParameterArray()));
    }

    @Override
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName), new HttpParameter("include_my_retweet", true)}, paging.asPostParameterArray())));
    }

    @Override
    public ResponseList<Status> getUserTimeline(long userId, Paging paging)
            throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId), includeMyRetweet}, paging.asPostParameterArray())));
    }

    @Override
    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, Paging.empty);
    }

    @Override
    public ResponseList<Status> getUserTimeline(long userId) throws TwitterException {
        return getUserTimeline(userId, Paging.empty);
    }

    @Override
    public ResponseList<Status> getUserTimeline() throws
            TwitterException {
        return getUserTimeline(Paging.empty);
    }

    @Override
    public ResponseList<Status> getUserTimeline(Paging paging) throws
            TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{includeMyRetweet}, paging.asPostParameterArray())));
    }

}
