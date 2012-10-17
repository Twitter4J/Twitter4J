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

import twitter4j.auth.*;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.*;
import twitter4j.internal.json.z_T4JInternalFactory;
import twitter4j.internal.json.z_T4JInternalJSONImplFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import static twitter4j.internal.http.HttpResponseCode.ENHANCE_YOUR_CLAIM;
import static twitter4j.internal.http.HttpResponseCode.SERVICE_UNAVAILABLE;

/**
 * Base class of Twitter / AsyncTwitter / TwitterStream supports OAuth.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class TwitterBaseImpl implements TwitterBase, java.io.Serializable, OAuthSupport, HttpResponseListener {
    protected Configuration conf;
    protected transient String screenName = null;
    protected transient long id = 0;

    protected transient HttpClientWrapper http;
    private List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<RateLimitStatusListener>(0);

    protected z_T4JInternalFactory factory;

    protected Authorization auth;
    private static final long serialVersionUID = -3812176145960812140L;

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
            if (consumerKey != null && consumerSecret != null) {
                OAuthAuthorization oauth = new OAuthAuthorization(conf);
                String accessToken = conf.getOAuthAccessToken();
                String accessTokenSecret = conf.getOAuthAccessTokenSecret();
                if (accessToken != null && accessTokenSecret != null) {
                    oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                this.auth = oauth;
            } else {
                this.auth = NullAuthorization.getInstance();
            }
        }
        http = new HttpClientWrapper(conf);
        http.setHttpResponseListener(this);
        setFactory();
    }

    protected void setFactory() {
        factory = new z_T4JInternalJSONImplFactory(conf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
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
        User user = factory.createUser(http.get(conf.getRestBaseURL() + "account/verify_credentials.json", auth));
        this.screenName = user.getScreenName();
        this.id = user.getId();
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRateLimitStatusListener(RateLimitStatusListener listener) {
        rateLimitStatusListeners.add(listener);
    }

    @Override
    public void httpResponseReceived(HttpResponseEvent event) {
        if (rateLimitStatusListeners.size() != 0) {
            HttpResponse res = event.getResponse();
            TwitterException te = event.getTwitterException();
            RateLimitStatus rateLimitStatus;
            int statusCode;
            if (te != null) {
                rateLimitStatus = te.getRateLimitStatus();
                statusCode = te.getStatusCode();
            } else {
                rateLimitStatus = z_T4JInternalJSONImplFactory.createRateLimitStatusFromResponseHeader(res);
                statusCode = res.getStatusCode();
            }
            if (rateLimitStatus != null) {
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
    @Override
    public final Authorization getAuthorization() {
        return auth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration() {
        return this.conf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        // http://docs.oracle.com/javase/6/docs/platform/serialization/spec/output.html#861
        out.putFields();
        out.writeFields();

        out.writeObject(conf);
        out.writeObject(auth);
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
        // http://docs.oracle.com/javase/6/docs/platform/serialization/spec/input.html#2971
        stream.readFields();

        conf = (Configuration) stream.readObject();
        auth = (Authorization) stream.readObject();
        rateLimitStatusListeners = (List<RateLimitStatusListener>) stream.readObject();
        http = new HttpClientWrapper(conf);
        http.setHttpResponseListener(this);
        setFactory();
    }


    // methods declared in OAuthSupport interface

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret) {
        if (null == consumerKey) {
            throw new NullPointerException("consumer key is null");
        }
        if (null == consumerSecret) {
            throw new NullPointerException("consumer secret is null");
        }
        if (auth instanceof NullAuthorization) {
            OAuthAuthorization oauth = new OAuthAuthorization(conf);
            oauth.setOAuthConsumer(consumerKey, consumerSecret);
            auth = oauth;
        } else if (auth instanceof BasicAuthorization) {
            XAuthAuthorization xauth = new XAuthAuthorization((BasicAuthorization) auth);
            xauth.setOAuthConsumer(consumerKey, consumerSecret);
            auth = xauth;
        } else if (auth instanceof OAuthAuthorization) {
            throw new IllegalStateException("consumer key/secret pair already set.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestToken getOAuthRequestToken(String callbackUrl, String xAuthAccessType) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl, xAuthAccessType);
    }

    /**
     * {@inheritDoc}
     * Basic authenticated instance of this class will try acquiring an AccessToken using xAuth.<br>
     * In order to get access acquire AccessToken using xAuth, you must apply by sending an email to <a href="mailto:api@twitter.com">api@twitter.com</a> all other applications will receive an HTTP 401 error.  Web-based applications will not be granted access, except on a temporary basis for when they are converting from basic-authentication support to full OAuth support.<br>
     * Storage of Twitter usernames and passwords is forbidden. By using xAuth, you are required to store only access tokens and access token secrets. If the access token expires or is expunged by a user, you must ask for their login and password again before exchanging the credentials for an access token.
     *
     * @throws TwitterException When Twitter service or network is unavailable, when the user has not authorized, or when the client application is not permitted to use xAuth
     * @see <a href="https://dev.twitter.com/docs/oauth/xauth">xAuth | Twitter Developers</a>
     */
    @Override
    public synchronized AccessToken getOAuthAccessToken() throws TwitterException {
        Authorization auth = getAuthorization();
        AccessToken oauthAccessToken;
        if (auth instanceof BasicAuthorization) {
            BasicAuthorization basicAuth = (BasicAuthorization) auth;
            auth = AuthorizationFactory.getInstance(conf);
            if (auth instanceof OAuthAuthorization) {
                this.auth = auth;
                OAuthAuthorization oauthAuth = (OAuthAuthorization) auth;
                oauthAccessToken = oauthAuth.getOAuthAccessToken(basicAuth.getUserId(), basicAuth.getPassword());
            } else {
                throw new IllegalStateException("consumer key / secret combination not supplied.");
            }
        } else {
            if (auth instanceof XAuthAuthorization) {
                XAuthAuthorization xauth = (XAuthAuthorization) auth;
                this.auth = xauth;
                OAuthAuthorization oauthAuth = new OAuthAuthorization(conf);
                oauthAuth.setOAuthConsumer(xauth.getConsumerKey(), xauth.getConsumerSecret());
                oauthAccessToken = oauthAuth.getOAuthAccessToken(xauth.getUserId(), xauth.getPassword());
            } else {
                oauthAccessToken = getOAuth().getOAuthAccessToken();
            }
        }
        screenName = oauthAccessToken.getScreenName();
        id = oauthAccessToken.getUserId();
        return oauthAccessToken;
    }


    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    @Override
    public synchronized AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        AccessToken oauthAccessToken = getOAuth().getOAuthAccessToken(oauthVerifier);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    @Override
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        OAuthSupport oauth = getOAuth();
        AccessToken oauthAccessToken = oauth.getOAuthAccessToken(requestToken);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    @Override
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setOAuthAccessToken(AccessToken accessToken) {
        getOAuth().setOAuthAccessToken(accessToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        return getOAuth().getOAuthAccessToken(screenName, password);
    }
    /* OAuth support methods */

    private OAuthSupport getOAuth() {
        if (!(auth instanceof OAuthSupport)) {
            throw new IllegalStateException(
                    "OAuth consumer key/secret combination not supplied");
        }
        return (OAuthSupport) auth;
    }


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
