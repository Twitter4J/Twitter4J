/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j;

import org.jetbrains.annotations.NotNull;
import twitter4j.v1.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static twitter4j.HttpResponseCode.*;

/**
 * A java representation of the <a href="https://dev.twitter.com/docs/api">Twitter REST API</a><br>
 * This class is thread safe and can be cached/re-used and used concurrently.<br>
 * Currently this class is not carefully designed to be extended. It is suggested to extend this class only for mock testing purpose.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@SuppressWarnings("rawtypes")
class TwitterImpl implements Twitter, HttpResponseListener, Serializable {
    private static final long serialVersionUID = 9170943084096085770L;

    private final HttpClient http;
    private final String IMPLICIT_PARAMS_STR;
    private final HttpParameter[] IMPLICIT_PARAMS;
    private final ObjectFactory factory;
    private final String restBaseURL;
    private final boolean mbeanEnabled;
    private final String uploadBaseURL;
    private final String streamBaseURL;
    private final String streamThreadName;
    private transient List<ConnectionLifeCycleListener> connectionLifeCycleListeners;
    private transient List<StreamListener> streamListeners;
    private transient List<RawStreamListener> rawStreamListeners;
    private final boolean stallWarningsEnabled;
    private final boolean prettyDebug;
    private final boolean jsonStoreEnabled;


    private transient List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners;
    private transient List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners;
    private transient TwitterV1 twitterV1;

    @NotNull
    private final Authorization auth;


    TwitterImpl(Configuration conf) {
        conf.ensureAuthorizationEnabled();
        this.factory = conf.factory;

        this.restBaseURL = conf.restBaseURL;
        this.uploadBaseURL = conf.uploadBaseURL;

        this.http = conf.http;
        this.auth = conf.auth;
        this.mbeanEnabled = conf.mbeanEnabled;


        //noinspection unchecked
        this.rateLimitStatusListeners = conf.rateLimitStatusListeners;
        //noinspection unchecked
        this.rateLimitReachedListeners = conf.rateLimitReachedListeners;

        String implicitParamsStr = conf.includeEntitiesEnabled ? "include_entities=" + true : "";
        boolean contributorsEnabled = conf.contributingTo != -1L;
        if (contributorsEnabled) {
            if (!"".equals(implicitParamsStr)) {
                implicitParamsStr += "&";
            }
            implicitParamsStr += "contributingto=" + conf.contributingTo;
        }

        if (conf.tweetModeExtended) {
            if (!"".equals(implicitParamsStr)) {
                implicitParamsStr += "&";
            }
            implicitParamsStr += "tweet_mode=extended";
        }

        List<HttpParameter> params = new ArrayList<>(3);
        if (conf.includeEntitiesEnabled) {
            params.add(new HttpParameter("include_entities", "true"));
        }
        if (contributorsEnabled) {
            params.add(new HttpParameter("contributingto", conf.contributingTo));
        }
        if (conf.trimUserEnabled) {
            params.add(new HttpParameter("trim_user", "1"));
        }
        if (conf.includeExtAltTextEnabled) {
            params.add(new HttpParameter("include_ext_alt_text", "true"));
        }
        if (conf.tweetModeExtended) {
            params.add(new HttpParameter("tweet_mode", "extended"));
        }
        streamBaseURL = conf.streamBaseURL;
        streamThreadName = conf.streamThreadName;
        jsonStoreEnabled = conf.jsonStoreEnabled;
        prettyDebug = conf.prettyDebug;
        if (conf instanceof TwitterBuilder) {
            TwitterBuilder builder = (TwitterBuilder) conf;
            connectionLifeCycleListeners = builder.connectionLifeCycleListeners;
            streamListeners = builder.streamListeners;
            rawStreamListeners = builder.rawStreamListeners;
            stallWarningsEnabled = builder.stallWarningsEnabled;
        } else {
            connectionLifeCycleListeners = new ArrayList<>();
            streamListeners = new ArrayList<>();
            rawStreamListeners = new ArrayList<>();
            stallWarningsEnabled = false;
        }
        this.IMPLICIT_PARAMS = params.toArray(new HttpParameter[0]);
        this.IMPLICIT_PARAMS_STR = implicitParamsStr;
        initTransients();
    }

    void initTransients() {
        twitterV1 = new TwitterV1Impl(http, factory, restBaseURL, streamBaseURL, uploadBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS,
                IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners, streamThreadName, connectionLifeCycleListeners,
                streamListeners, rawStreamListeners, jsonStoreEnabled, prettyDebug, stallWarningsEnabled);
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

    public TwitterV1 v1() {
        return twitterV1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwitterImpl twitter = (TwitterImpl) o;
        return mbeanEnabled == twitter.mbeanEnabled && stallWarningsEnabled == twitter.stallWarningsEnabled && prettyDebug == twitter.prettyDebug && jsonStoreEnabled == twitter.jsonStoreEnabled && Objects.equals(http, twitter.http) && Objects.equals(IMPLICIT_PARAMS_STR, twitter.IMPLICIT_PARAMS_STR) && Arrays.equals(IMPLICIT_PARAMS, twitter.IMPLICIT_PARAMS) && Objects.equals(factory, twitter.factory) && Objects.equals(restBaseURL, twitter.restBaseURL) && Objects.equals(uploadBaseURL, twitter.uploadBaseURL) && Objects.equals(streamBaseURL, twitter.streamBaseURL) && Objects.equals(streamThreadName, twitter.streamThreadName) && Objects.equals(connectionLifeCycleListeners, twitter.connectionLifeCycleListeners) && Objects.equals(streamListeners, twitter.streamListeners) && Objects.equals(rawStreamListeners, twitter.rawStreamListeners) && Objects.equals(rateLimitStatusListeners, twitter.rateLimitStatusListeners) && Objects.equals(rateLimitReachedListeners, twitter.rateLimitReachedListeners) && Objects.equals(twitterV1, twitter.twitterV1) && auth.equals(twitter.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auth);
    }

    @Override
    public String toString() {
        return "TwitterImpl{" +
                "http=" + http +
                ", IMPLICIT_PARAMS_STR='" + IMPLICIT_PARAMS_STR + '\'' +
                ", IMPLICIT_PARAMS=" + Arrays.toString(IMPLICIT_PARAMS) +
                ", factory=" + factory +
                ", restBaseURL='" + restBaseURL + '\'' +
                ", mbeanEnabled=" + mbeanEnabled +
                ", uploadBaseURL='" + uploadBaseURL + '\'' +
                ", streamBaseURL='" + streamBaseURL + '\'' +
                ", streamThreadName='" + streamThreadName + '\'' +
                ", connectionLifeCycleListeners=" + connectionLifeCycleListeners +
                ", streamListeners=" + streamListeners +
                ", rawStreamListeners=" + rawStreamListeners +
                ", stallWarningsEnabled=" + stallWarningsEnabled +
                ", prettyDebug=" + prettyDebug +
                ", jsonStoreEnabled=" + jsonStoreEnabled +
                ", rateLimitStatusListeners=" + rateLimitStatusListeners +
                ", rateLimitReachedListeners=" + rateLimitReachedListeners +
                ", twitterV1=" + twitterV1 +
                ", auth=" + auth +
                '}';
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        // http://docs.oracle.com/javase/6/docs/platform/serialization/spec/input.html#2971
        stream.defaultReadObject();

        rateLimitReachedListeners = new ArrayList<>();
        rateLimitStatusListeners = new ArrayList<>();
        connectionLifeCycleListeners = new ArrayList<>();
        streamListeners = new ArrayList<>();
        rawStreamListeners = new ArrayList<>();

        initTransients();
    }

}
