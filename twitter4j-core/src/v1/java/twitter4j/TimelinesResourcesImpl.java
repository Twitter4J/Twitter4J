package twitter4j;

import org.jetbrains.annotations.NotNull;
import twitter4j.v1.Paging;
import twitter4j.v1.ResponseList;
import twitter4j.v1.Status;
import twitter4j.v1.TimelinesResources;

import java.util.ArrayList;
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
        return factory.createStatusList(get(restBaseURL + "statuses/mentions_timeline.json", asPostParameterArray(paging)));
    }

    @Override
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/home_timeline.json", includeMyRetweet));
    }

    @Override
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/home_timeline.json", mergeParameters(asPostParameterArray(paging), includeMyRetweet)));
    }

    @Override
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/retweets_of_me.json"));
    }

    @Override
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/retweets_of_me.json", asPostParameterArray(paging)));
    }

    @Override
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName), new HttpParameter("include_my_retweet", true)}, asPostParameterArray(paging))));
    }

    @Override
    public ResponseList<Status> getUserTimeline(long userId, Paging paging)
            throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId), includeMyRetweet}, asPostParameterArray(paging))));
    }

    @Override
    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, empty);
    }

    @Override
    public ResponseList<Status> getUserTimeline(long userId) throws TwitterException {
        return getUserTimeline(userId, empty);
    }

    @Override
    public ResponseList<Status> getUserTimeline() throws
            TwitterException {
        return getUserTimeline(empty);
    }
   final static Paging empty = Paging.ofMaxId(Long.MAX_VALUE);


    @Override
    public ResponseList<Status> getUserTimeline(@NotNull Paging paging) throws
            TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{includeMyRetweet}, asPostParameterArray(paging))));
    }


    // since only
    static final char[] S = new char[]{'s'};
    // since, max_id, count, page
    static final char[] SMCP = new char[]{'s', 'm', 'c', 'p'};

    static final String COUNT = "count";
    // somewhat GET list statuses requires "per_page" instead of "count"
    // @see <a href="https://dev.twitter.com/docs/api/1.1/get/:user/lists/:id/statuses">GET :user/lists/:id/statuses | Twitter Developers</a>
    static final String PER_PAGE = "per_page";

    static List<HttpParameter> asPostParameterList(Paging paging) {
        return asPostParameterList(SMCP, COUNT, paging);
    }

    private static final HttpParameter[] NULL_PARAMETER_ARRAY = new HttpParameter[0];

    static HttpParameter[] asPostParameterArray(Paging paging) {
        List<HttpParameter> list = asPostParameterList(SMCP, COUNT, paging);
        if (list.size() == 0) {
            return NULL_PARAMETER_ARRAY;
        }
        return list.toArray(new HttpParameter[0]);
    }

    static List<HttpParameter> asPostParameterList(char[] supportedParams, Paging paging) {
        return asPostParameterList(supportedParams, COUNT, paging);
    }


    private static final List<HttpParameter> NULL_PARAMETER_LIST = new ArrayList<>(0);

    /**
     * Converts the pagination parameters into a List of PostParameter.<br>
     * This method also Validates the preset parameters, and throws
     * IllegalStateException if any unsupported parameter is set.
     *
     * @param supportedParams  char array representation of supported parameters
     * @param perPageParamName name used for per-page parameter. getUserListStatuses() requires "per_page" instead of "count".
     * @return list of PostParameter
     */
    static List<HttpParameter> asPostParameterList(char[] supportedParams, String perPageParamName, Paging paging) {
        List<HttpParameter> pagingParams = new ArrayList<>(supportedParams.length);
        if (paging != empty) {
            addPostParameter(supportedParams, 's', pagingParams, "since_id", paging.sinceId);
            addPostParameter(supportedParams, 'm', pagingParams, "max_id", paging.maxId);
            addPostParameter(supportedParams, 'c', pagingParams, perPageParamName, paging.count);
            addPostParameter(supportedParams, 'p', pagingParams, "page", paging.page);
        }
        return pagingParams;
    }

    /**
     * Converts the pagination parameters into a List of PostParameter.<br>
     * This method also Validates the preset parameters, and throws
     * IllegalStateException if any unsupported parameter is set.
     *
     * @param supportedParams  char array representation of supported parameters
     * @param perPageParamName name used for per-page parameter. getUserListStatuses() requires "per_page" instead of "count".
     * @return array of PostParameter
     */
    static HttpParameter[] asPostParameterArray(char[] supportedParams, String perPageParamName, Paging paging) {
        return asPostParameterList(supportedParams, perPageParamName, paging).toArray(new HttpParameter[0]);
    }

    private static void addPostParameter(char[] supportedParams, char paramKey
            , List<HttpParameter> pagingParams, String paramName, long paramValue) {
        boolean supported = false;
        for (char supportedParam : supportedParams) {
            if (supportedParam == paramKey) {
                supported = true;
                break;
            }
        }
        if (!supported && -1 != paramValue) {
            throw new IllegalStateException("Paging parameter [" + paramName
                    + "] is not supported with this operation.");
        }
        if (-1 != paramValue) {
            pagingParams.add(new HttpParameter(paramName, String.valueOf(paramValue)));
        }
    }}
