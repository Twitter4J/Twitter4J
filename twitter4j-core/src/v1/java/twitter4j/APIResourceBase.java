package twitter4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static twitter4j.HttpResponseCode.*;

class APIResourceBase implements HttpResponseListener {
    private final HttpClient http;
    protected final ObjectFactory factory;
    protected final String restBaseURL;
    protected static final HttpParameter includeMyRetweet = new HttpParameter("include_my_retweet", true);
    private final Authorization auth;
    private final boolean mbeanEnabled;
    private final String IMPLICIT_PARAMS_STR;

    private final HttpParameter[] IMPLICIT_PARAMS;
    private final List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners;
    private final List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners;

    APIResourceBase(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                    String IMPLICIT_PARAMS_STR,
                    List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                    List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        this.http = http;
        this.factory = factory;
        this.restBaseURL = restBaseURL;
        this.auth = auth;
        this.mbeanEnabled = mbeanEnabled;
        this.IMPLICIT_PARAMS = IMPLICIT_PARAMS;
        this.IMPLICIT_PARAMS_STR = IMPLICIT_PARAMS_STR;
        this.rateLimitStatusListeners = rateLimitStatusListeners;
        this.rateLimitReachedListeners = rateLimitReachedListeners;
    }

    protected HttpResponse get(String url) throws TwitterException {
        if (IMPLICIT_PARAMS_STR.length() > 0) {
            if (url.contains("?")) {
                url = url + "&" + IMPLICIT_PARAMS_STR;
            } else {
                url = url + "?" + IMPLICIT_PARAMS_STR;
            }
        }
        if (!mbeanEnabled) {
            return http.get(url, null, auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.get(url, null, auth, this);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    protected HttpResponse get(String url, HttpParameter... params) throws TwitterException {
        if (!mbeanEnabled) {
            return http.get(url, mergeImplicitParams(params), auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.get(url, mergeImplicitParams(params), auth, this);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    protected HttpResponse post(String url) throws TwitterException {
        if (!mbeanEnabled) {
            return http.post(url, IMPLICIT_PARAMS, auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.post(url, IMPLICIT_PARAMS, auth, this);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    protected HttpResponse post(String url, HttpParameter... params) throws TwitterException {
        if (!mbeanEnabled) {
            return http.post(url, mergeImplicitParams(params), auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.post(url, mergeImplicitParams(params), auth, this);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    protected HttpResponse delete(String url) throws TwitterException {
        if (!mbeanEnabled) {
            return http.delete(url, null, auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.delete(url, null, auth, this);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }

    protected HttpResponse post(String url, JSONObject json) throws TwitterException {
        if (!mbeanEnabled) {
            return http.post(url, new HttpParameter[]{new HttpParameter(json)}, auth, this);
        } else {
            // intercept HTTP call for monitoring purposes
            HttpResponse response = null;
            long start = System.currentTimeMillis();
            try {
                response = http.post(url, new HttpParameter[]{new HttpParameter(json)}, auth, this);
            } finally {
                long elapsedTime = System.currentTimeMillis() - start;
                TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
            }
            return response;
        }
    }


    protected HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter[] params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[params1.length + params2.length];
            System.arraycopy(params1, 0, params, 0, params1.length);
            System.arraycopy(params2, 0, params, params1.length, params2.length);
            return params;
        }
        if (null == params1 && null == params2) {
            return new HttpParameter[0];
        }
        if (params1 != null) {
            return params1;
        } else {
            return params2;
        }
    }

    protected HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[params1.length + 1];
            System.arraycopy(params1, 0, params, 0, params1.length);
            params[params.length - 1] = params2;
            return params;
        }
        if (null == params1 && null == params2) {
            return new HttpParameter[0];
        }
        if (params1 != null) {
            return params1;
        } else {
            return new HttpParameter[]{params2};
        }
    }

    protected HttpParameter[] mergeImplicitParams(HttpParameter... params) {
        return mergeParameters(params, IMPLICIT_PARAMS);
    }

    protected boolean isOk(HttpResponse response) {
        return response != null && response.getStatusCode() < 300;
    }

    @Override
    public void httpResponseReceived(HttpResponseEvent event) {
        if (rateLimitStatusListeners.size() != 0 || rateLimitReachedListeners.size() != 0) {
            HttpResponse res = event.getResponse();
            TwitterException te = event.getTwitterException();
            RateLimitStatus rateLimitStatus;
            int statusCode;
            if (te != null) {
                rateLimitStatus = te.getRateLimitStatus();
                statusCode = te.getStatusCode();
            } else {
                rateLimitStatus = JSONImplFactory.createRateLimitStatusFromResponseHeader(res);
                statusCode = res.getStatusCode();
            }
            if (rateLimitStatus != null) {
                RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                if (statusCode == ENHANCE_YOUR_CLAIM || statusCode == SERVICE_UNAVAILABLE || statusCode == TOO_MANY_REQUESTS) {
                    // EXCEEDED_RATE_LIMIT_QUOTA is returned by Rest API
                    // SERVICE_UNAVAILABLE is returned by Search API
                    for (Consumer<RateLimitStatusEvent> listener : rateLimitStatusListeners) {
                        listener.accept(statusEvent);
                    }
                    for (Consumer<RateLimitStatusEvent> listener : rateLimitReachedListeners) {
                        listener.accept(statusEvent);
                    }
                } else {
                    for (Consumer<RateLimitStatusEvent> listener : rateLimitStatusListeners) {
                        listener.accept(statusEvent);
                    }
                }
            }
        }
    }

    /**
     * Check the existence, and the type of the specified file.
     *
     * @param image image to be uploaded
     * @throws TwitterException when the specified file is not found (FileNotFoundException will be nested)
     *                          , or when the specified file object is not representing a file(IOException will be nested).
     */
    protected void checkFileValidity(File image) throws TwitterException {
        if (!image.exists()) {
            //noinspection ThrowableInstanceNeverThrown
            throw new TwitterException(new FileNotFoundException(image + " is not found."));
        }
        if (!image.isFile()) {
            //noinspection ThrowableInstanceNeverThrown
            throw new TwitterException(new IOException(image + " is not a file."));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        APIResourceBase that = (APIResourceBase) o;
        return mbeanEnabled == that.mbeanEnabled && Objects.equals(http, that.http) && Objects.equals(factory, that.factory) && Objects.equals(restBaseURL, that.restBaseURL) && Objects.equals(auth, that.auth) && Objects.equals(IMPLICIT_PARAMS_STR, that.IMPLICIT_PARAMS_STR) && Arrays.equals(IMPLICIT_PARAMS, that.IMPLICIT_PARAMS) && Objects.equals(rateLimitStatusListeners, that.rateLimitStatusListeners) && Objects.equals(rateLimitReachedListeners, that.rateLimitReachedListeners);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        result = 31 * result + Arrays.hashCode(IMPLICIT_PARAMS);
        return result;
    }
}
