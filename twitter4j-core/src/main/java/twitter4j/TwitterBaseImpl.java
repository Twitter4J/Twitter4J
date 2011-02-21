/*
 * Copyright 2007 Yusuke Yamamoto
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

import twitter4j.conf.Configuration;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.NullAuthorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.OAuthSupport;
import twitter4j.http.RequestToken;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseEvent;
import twitter4j.internal.http.HttpResponseListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import static twitter4j.internal.http.HttpResponseCode.ENHANCE_YOUR_CLAIM;
import static twitter4j.internal.http.HttpResponseCode.SERVICE_UNAVAILABLE;

/**
 * Base class of Twitter / AsyncTwitter / TwitterStream supports Basic Authorization.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class TwitterBaseImpl implements java.io.Serializable, OAuthSupport, HttpResponseListener {
    protected final Configuration conf;
    protected transient String screenName = null;
    protected transient long id = 0;

    protected transient HttpClientWrapper http;
    private List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<RateLimitStatusListener>(0);

    protected Authorization auth;
    private static final long serialVersionUID = -3812176145960812140L;

    /*package*/ TwitterBaseImpl(Configuration conf) {
        this.conf = conf;
        init();
    }

    /*package*/ TwitterBaseImpl(Configuration conf, Authorization auth) {
        this.conf = conf;
        this.auth = auth;
        init();
    }

    private void init() {
        if (null == auth) {
            // try to populate OAuthAuthorization if available in the configuration
            String consumerKey = conf.getOAuthConsumerKey();
            String consumerSecret = conf.getOAuthConsumerSecret();
            // try to find oauth tokens in the configuration
            if (null != consumerKey && null != consumerSecret) {
                OAuthAuthorization oauth = new OAuthAuthorization(conf);
                String accessToken = conf.getOAuthAccessToken();
                String accessTokenSecret = conf.getOAuthAccessTokenSecret();
                if (null != accessToken && null != accessTokenSecret) {
                    oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                this.auth = oauth;
            } else {
                this.auth = NullAuthorization.getInstance();
            }
        }
        http = new HttpClientWrapper(conf);
        http.setHttpResponseListener(this);
    }

    /**
     * {@inheritDoc}
     */
    public String getScreenName() throws TwitterException, IllegalStateException {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (null == screenName) {
            if (auth instanceof BasicAuthorization) {
                screenName = ((BasicAuthorization) auth).getUserId();
                if (-1 != screenName.indexOf("@")) {
                    screenName = null;
                }
            }
            if (null == screenName) {
                // retrieve the screen name if this instance is authenticated with OAuth or email address
                fillInIDAndScreenName();
            }
        }
        return screenName;
    }

    /**
     * {@inheritDoc}
     */
    public long getId() throws TwitterException, IllegalStateException {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (0 == id) {
            fillInIDAndScreenName();
        }
        // retrieve the screen name if this instance is authenticated with OAuth or email address
        return id;
    }

    protected User fillInIDAndScreenName() throws TwitterException {
        ensureAuthorizationEnabled();
        User user = new UserJSONImpl(http.get(conf.getRestBaseURL() + "account/verify_credentials.json?include_entities="
                + conf.isIncludeEntitiesEnabled(), auth));
        this.screenName = user.getScreenName();
        this.id = user.getId();
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public void addRateLimitStatusListener(RateLimitStatusListener listener) {
        rateLimitStatusListeners.add(listener);
    }

    public void httpResponseReceived(HttpResponseEvent event) {
        if (rateLimitStatusListeners.size() != 0) {
            HttpResponse res = event.getResponse();
            TwitterException te = event.getTwitterException();
            RateLimitStatus rateLimitStatus;
            int statusCode;
            if (null != te) {
                rateLimitStatus = te.getRateLimitStatus();
                statusCode = te.getStatusCode();
            } else {
                rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
                statusCode = res.getStatusCode();
            }
            if (null != rateLimitStatus) {
                RateLimitStatusEvent statusEvent
                        = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                if (statusCode == ENHANCE_YOUR_CLAIM
                        || statusCode == SERVICE_UNAVAILABLE) {
                    // EXCEEDED_RATE_LIMIT_QUOTA is returned by Rest API
                    // SERVICE_UNAVAILABLE is returned by Search API
                    for (RateLimitStatusListener listener : rateLimitStatusListeners) {
                        listener.onRateLimitStatus(statusEvent);
                        listener.onRateLimitReached(statusEvent);
                    }
                } else {
                    for (RateLimitStatusListener listener : rateLimitStatusListeners) {
                        listener.onRateLimitStatus(statusEvent);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public final Authorization getAuthorization() {
        return auth;
    }

    /**
     * {@inheritDoc}
     */
    public Configuration getConfiguration() {
        return this.conf;
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        if (http != null) http.shutdown();
    }

    protected final void ensureAuthorizationEnabled() {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Authentication credentials are missing. See http://twitter4j.org/configuration.html for the detail.");
        }
    }

    protected final void ensureOAuthEnabled() {
        if (!(auth instanceof OAuthAuthorization)) {
            throw new IllegalStateException(
                    "OAuth required. Authentication credentials are missing. See http://twitter4j.org/configuration.html for the detail.");
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        List<RateLimitStatusListener> serializableRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>(0);
        for (RateLimitStatusListener listener : rateLimitStatusListeners) {
            if (listener instanceof java.io.Serializable) {
                serializableRateLimitStatusListeners.add(listener);
            }
        }
        out.writeObject(serializableRateLimitStatusListeners);
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        rateLimitStatusListeners = (List<RateLimitStatusListener>) stream.readObject();
        http = new HttpClientWrapper(conf);
        http.setHttpResponseListener(this);
    }


    // methods declared in OAuthSupport interface

    /**
     * {@inheritDoc}
     */
    public abstract void setOAuthConsumer(String consumerKey, String consumerSecret);

    /**
     * {@inheritDoc}
     */
    public abstract RequestToken getOAuthRequestToken() throws TwitterException;

    /**
     * {@inheritDoc}
     */
    public abstract RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException;

    /**
     * {@inheritDoc}
     */
    public abstract AccessToken getOAuthAccessToken() throws TwitterException;

    /**
     * {@inheritDoc}
     */
    public abstract AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException;

    /**
     * {@inheritDoc}
     */
    public abstract AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException;

    /**
     * {@inheritDoc}
     */
    public abstract AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException;

    public abstract void setOAuthAccessToken(AccessToken accessToken);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterBaseImpl)) return false;

        TwitterBaseImpl that = (TwitterBaseImpl) o;

        if (auth != null ? !auth.equals(that.auth) : that.auth != null)
            return false;
        if (!conf.equals(that.conf)) return false;
        if (http != null ? !http.equals(that.http) : that.http != null)
            return false;
        if (!rateLimitStatusListeners.equals(that.rateLimitStatusListeners))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = conf.hashCode();
        result = 31 * result + (http != null ? http.hashCode() : 0);
        result = 31 * result + rateLimitStatusListeners.hashCode();
        result = 31 * result + (auth != null ? auth.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TwitterBase{" +
                "conf=" + conf +
                ", http=" + http +
                ", rateLimitStatusListeners=" + rateLimitStatusListeners +
                ", auth=" + auth +
                '}';
    }
}
