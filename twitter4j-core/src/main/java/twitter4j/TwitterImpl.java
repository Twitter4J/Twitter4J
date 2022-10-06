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
import twitter4j.api.*;

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


    private transient PlacesGeoResourcesImpl placeGeoResources;
    private transient TimelinesResources timelinesResources;
    private transient TweetsResources tweetsResources;
    private transient SearchResource searchResource;
    private transient DirectMessagesResources directMessagesResources;
    private transient FriendsFollowersResources friendsFollowersResources;
    private transient UsersResources usersResources;
    private transient List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners;
    private transient List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners;

    @NotNull
    private final Authorization auth;
    private transient SavedSearchesResources savedSearchesResources;
    private transient FavoritesResources favoritesResources;
    private transient ListsResources listResources;
    private transient HelpResources helpResources;
    private transient SpamReportingResource spamReportingResource;
    private transient TrendsResources trendResources;


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

        this.IMPLICIT_PARAMS = params.toArray(new HttpParameter[0]);
        this.IMPLICIT_PARAMS_STR = implicitParamsStr;
        initTransients();
    }

   void initTransients() {
       helpResources = new HelpResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       spamReportingResource = new SpamReportingResourceImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       trendResources = new TrendsResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       placeGeoResources = new PlacesGeoResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       savedSearchesResources = new SavedSearchesResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       listResources = new ListsResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       timelinesResources = new TimelinesResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       tweetsResources = new TweetsResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners, uploadBaseURL);
       searchResource = new SearchResourceImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       directMessagesResources = new DirectMessagesResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       friendsFollowersResources = new FriendsFollowersResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       favoritesResources = new FavoritesResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
       usersResources = new UsersResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);

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

    @Override
    public TimelinesResources timelines() {
        return timelinesResources;
    }

    @Override
    public TweetsResources tweets() {
        return tweetsResources;
    }

    @Override
    public SearchResource search() {
        return searchResource;
    }

    @Override
    public DirectMessagesResources directMessages() {
        return directMessagesResources;
    }

    @Override
    public FriendsFollowersResources friendsFollowers() {
        return friendsFollowersResources;
    }

    @Override
    public UsersResources users() {
        return usersResources;
    }

    @Override
    public FavoritesResources favorites() {
        return favoritesResources;
    }

    @Override
    public ListsResources list() {
        return listResources;
    }

    @Override
    public SavedSearchesResources savedSearches() {
        return savedSearchesResources;
    }

    @Override
    public PlacesGeoResources placesGeo() {
        return placeGeoResources;
    }

    @Override
    public TrendsResources trends() {
        return trendResources;
    }

    @Override
    public SpamReportingResource spamReporting() {
        return spamReportingResource;
    }

    @Override
    public HelpResources help() {
        return helpResources;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwitterImpl twitter = (TwitterImpl) o;
        return mbeanEnabled == twitter.mbeanEnabled && Objects.equals(http, twitter.http) && Objects.equals(IMPLICIT_PARAMS_STR, twitter.IMPLICIT_PARAMS_STR) && Arrays.equals(IMPLICIT_PARAMS, twitter.IMPLICIT_PARAMS) && Objects.equals(factory, twitter.factory) && Objects.equals(restBaseURL, twitter.restBaseURL) && Objects.equals(uploadBaseURL, twitter.uploadBaseURL) && Objects.equals(placeGeoResources, twitter.placeGeoResources) && Objects.equals(timelinesResources, twitter.timelinesResources) && Objects.equals(tweetsResources, twitter.tweetsResources) && Objects.equals(searchResource, twitter.searchResource) && Objects.equals(directMessagesResources, twitter.directMessagesResources) && Objects.equals(friendsFollowersResources, twitter.friendsFollowersResources) && Objects.equals(usersResources, twitter.usersResources) && Objects.equals(rateLimitStatusListeners, twitter.rateLimitStatusListeners) && Objects.equals(rateLimitReachedListeners, twitter.rateLimitReachedListeners) && auth.equals(twitter.auth) && Objects.equals(savedSearchesResources, twitter.savedSearchesResources) && Objects.equals(favoritesResources, twitter.favoritesResources) && Objects.equals(listResources, twitter.listResources) && Objects.equals(helpResources, twitter.helpResources) && Objects.equals(spamReportingResource, twitter.spamReportingResource) && Objects.equals(trendResources, twitter.trendResources);
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
                ", placeGeoResources=" + placeGeoResources +
                ", timelinesResources=" + timelinesResources +
                ", tweetsResources=" + tweetsResources +
                ", searchResource=" + searchResource +
                ", directMessagesResources=" + directMessagesResources +
                ", friendsFollowersResources=" + friendsFollowersResources +
                ", usersResources=" + usersResources +
                ", rateLimitStatusListeners=" + rateLimitStatusListeners +
                ", rateLimitReachedListeners=" + rateLimitReachedListeners +
                ", auth=" + auth +
                ", savedSearchesResources=" + savedSearchesResources +
                ", favoritesResources=" + favoritesResources +
                ", listResources=" + listResources +
                ", helpResources=" + helpResources +
                ", spamReportingResource=" + spamReportingResource +
                ", trendResources=" + trendResources +
                '}';
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        // http://docs.oracle.com/javase/6/docs/platform/serialization/spec/input.html#2971
        stream.defaultReadObject();

        rateLimitReachedListeners = new ArrayList<>();
        rateLimitStatusListeners = new ArrayList<>();
        initTransients();
    }

}
