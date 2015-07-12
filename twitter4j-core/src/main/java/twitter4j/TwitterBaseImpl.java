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
import twitter4j.util.function.Consumer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import static twitter4j.HttpResponseCode.*;

/**
 * Base class of Twitter / AsyncTwitter / TwitterStream supports OAuth.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class TwitterBaseImpl implements TwitterBase, java.io.Serializable, OAuthSupport, OAuth2Support, HttpResponseListener {
    private static final String WWW_DETAILS = "See http://twitter4j.org/en/configuration.html for details. See and register at http://apps.twitter.com/";
    private static final long serialVersionUID = -7824361938865528554L;

    Configuration conf;
    private transient String screenName = null;
    private transient long id = 0;

    transient HttpClient http;
    private List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<RateLimitStatusListener>(0);

    ObjectFactory factory;

    Authorization auth;

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
                if (conf.isApplicationOnlyAuthEnabled()) {
                    OAuth2Authorization oauth2 = new OAuth2Authorization(conf);
                    String tokenType = conf.getOAuth2TokenType();
                    String accessToken = conf.getOAuth2AccessToken();
                    if (tokenType != null && accessToken != null) {
                        oauth2.setOAuth2Token(new OAuth2Token(tokenType, accessToken));
                    }
                    this.auth = oauth2;

                } else {
                    OAuthAuthorization oauth = new OAuthAuthorization(conf);
                    String accessToken = conf.getOAuthAccessToken();
                    String accessTokenSecret = conf.getOAuthAccessTokenSecret();
                    if (accessToken != null && accessTokenSecret != null) {
                        oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                    }
                    this.auth = oauth;
                }
            } else {
                this.auth = NullAuthorization.getInstance();
            }
        }
        http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        setFactory();
    }

    void setFactory() {
        factory = new JSONImplFactory(conf);
    }

    @Override
    public String getScreenName() throws TwitterException, IllegalStateException {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (null == screenName) {
            if (auth instanceof BasicAuthorization) {
                screenName = ((BasicAuthorization) auth).getUserId();
                if (screenName.contains("@")) {
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

    User fillInIDAndScreenName() throws TwitterException {
        ensureAuthorizationEnabled();
        User user = new UserJSONImpl(http.get(conf.getRestBaseURL() + "account/verify_credentials.json", null, auth, this), conf);
        this.screenName = user.getScreenName();
        this.id = user.getId();
        return user;
    }

    @Override
    public void addRateLimitStatusListener(RateLimitStatusListener listener) {
        rateLimitStatusListeners.add(listener);
    }

    @Override
    public void onRateLimitStatus(final Consumer<RateLimitStatusEvent> action) {
        rateLimitStatusListeners.add(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                action.accept(event);
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        });
    }

    @Override
    public void onRateLimitReached(final Consumer<RateLimitStatusEvent> action) {
        rateLimitStatusListeners.add(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                action.accept(event);
            }
        });
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
                rateLimitStatus = JSONImplFactory.createRateLimitStatusFromResponseHeader(res);
                statusCode = res.getStatusCode();
            }
            if (rateLimitStatus != null) {
                RateLimitStatusEvent statusEvent
                        = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                if (statusCode == ENHANCE_YOUR_CLAIM
                        || statusCode == SERVICE_UNAVAILABLE
                        || statusCode == TOO_MANY_REQUESTS) {
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

    @Override
    public final Authorization getAuthorization() {
        return auth;
    }

    @Override
    public Configuration getConfiguration() {
        return this.conf;
    }

    final void ensureAuthorizationEnabled() {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                "Authentication credentials are missing. " + WWW_DETAILS);
        }
    }

    final void ensureOAuthEnabled() {
        if (!(auth instanceof OAuthAuthorization)) {
            throw new IllegalStateException(
                "OAuth required. Authentication credentials are missing. " + WWW_DETAILS);
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
        http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        setFactory();
    }


    // methods declared in OAuthSupport interface

    @Override
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret) {
        if (null == consumerKey) {
            throw new NullPointerException("consumer key is null");
        }
        if (null == consumerSecret) {
            throw new NullPointerException("consumer secret is null");
        }
        if (auth instanceof NullAuthorization) {
            if (conf.isApplicationOnlyAuthEnabled()) {
                OAuth2Authorization oauth2 = new OAuth2Authorization(conf);
                oauth2.setOAuthConsumer(consumerKey, consumerSecret);
                auth = oauth2;
            } else {
                OAuthAuthorization oauth = new OAuthAuthorization(conf);
                oauth.setOAuthConsumer(consumerKey, consumerSecret);
                auth = oauth;
            }
        } else if (auth instanceof BasicAuthorization) {
            XAuthAuthorization xauth = new XAuthAuthorization((BasicAuthorization) auth);
            xauth.setOAuthConsumer(consumerKey, consumerSecret);
            auth = xauth;
        } else if (auth instanceof OAuthAuthorization || auth instanceof OAuth2Authorization) {
            throw new IllegalStateException("consumer key/secret pair already set.");
        }
    }

    @Override
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    @Override
    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl);
    }

    @Override
    public RequestToken getOAuthRequestToken(String callbackUrl, String xAuthAccessType) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl, xAuthAccessType);
    }

    @Override
    public RequestToken getOAuthRequestToken(String callbackUrl, String xAuthAccessType, String xAuthMode) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl, xAuthAccessType, xAuthMode);
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


    @Override
    public synchronized AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        AccessToken oauthAccessToken = getOAuth().getOAuthAccessToken(oauthVerifier);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    @Override
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        OAuthSupport oauth = getOAuth();
        AccessToken oauthAccessToken = oauth.getOAuthAccessToken(requestToken);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    @Override
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
    }

    @Override
    public synchronized void setOAuthAccessToken(AccessToken accessToken) {
        getOAuth().setOAuthAccessToken(accessToken);
    }

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
    public synchronized OAuth2Token getOAuth2Token() throws TwitterException {
        return getOAuth2().getOAuth2Token();
    }

    @Override
    public void setOAuth2Token(OAuth2Token oauth2Token) {
        getOAuth2().setOAuth2Token(oauth2Token);
    }

    @Override
    public synchronized void invalidateOAuth2Token() throws TwitterException {
        getOAuth2().invalidateOAuth2Token();
    }

    private OAuth2Support getOAuth2() {
        if (!(auth instanceof OAuth2Support)) {
            throw new IllegalStateException(
                "OAuth consumer key/secret combination not supplied");
        }
        return (OAuth2Support) auth;
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
