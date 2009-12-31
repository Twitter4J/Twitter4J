package twitter4j;

import twitter4j.http.*;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class OAuthTwitterSupport extends TwitterSupport implements HttpResponseListener, java.io.Serializable {

    protected List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    private static final long serialVersionUID = 6960663978976449394L;

    OAuthTwitterSupport() {
        super(); // this will populate BasicAuthorization from the configuration
        init();
    }

    OAuthTwitterSupport(String screenName, String password) {
        super(screenName, password);
        init();
    }
    OAuthTwitterSupport(Authorization auth) {
        super();
        this.auth = auth;
        init();
    }

    private void init(){
        if(auth instanceof NullAuthorization){
            // try to populate OAuthAuthorization if available in the configuration
            String consumerKey = conf.getOAuthConsumerKey();
            String consumerSecret = conf.getOAuthConsumerSecret();
            // try to find oauth tokens in the configuration
            if (null != consumerKey && null != consumerSecret) {
                OAuthAuthorization oauth = new OAuthAuthorization(consumerKey, consumerSecret);
                String accessToken = conf.getOAuthAccessToken();
                String accessTokenSecret = conf.getOAuthAccessTokenSecret();
                if (null != accessToken && null != accessTokenSecret) {
                    oauth.setAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                this.auth = oauth;
            }
        }
        http.addHttpResponseListener(this);
    }
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(rateLimitStatusListeners);
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        rateLimitStatusListeners = (ArrayList<RateLimitStatusListener>)stream.readObject();
        http.addHttpResponseListener(this);
    }
    protected OAuthAuthorization getOAuth() {
        if (!(auth instanceof OAuthAuthorization)) {
            throw new IllegalStateException(
                    "OAuth consumer key/secret combination not supplied");
        }
        return (OAuthAuthorization)auth;
    }

    /**
     * Retrieves a request token
     *
     * @return generated request token.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ">Twitter API Wiki - OAuth FAQ</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @since Twitter 2.0.0
     */
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return getOAuth().getRequestToken(callbackUrl);
    }

    protected String screenName = null;

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param requestToken the request token
     * @return access token associsted with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        OAuthAuthorization oauth = getOAuth();
        AccessToken oauthAccessToken = oauth.getAccessToken(requestToken);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param requestToken   the request token
     * @param oauthVerifier oauth_verifier or pin
     * @return access token associsted with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return getOAuth().getAccessToken(requestToken, oauthVerifier);
    }

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param token       request token
     * @param tokenSecret request token secret
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.1
     */
    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws TwitterException {
        return getOAuth().getAccessToken(new RequestToken(token,tokenSecret));
    }

    /**
     * Retrieves an access token associated with the supplied request token.
     *
     * @param token       request token
     * @param tokenSecret request token secret
     * @param pin         pin
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.8
     */
    public synchronized AccessToken getOAuthAccessToken(String token
            , String tokenSecret, String pin) throws TwitterException {
        return getOAuthAccessToken(new RequestToken(token, tokenSecret), pin);
    }

    /**
     * Sets the access token
     *
     * @param accessToken accessToken
     * @since Twitter 2.0.0
     * @deprecated
     */
    public void setOAuthAccessToken(AccessToken accessToken) {
        getOAuth().setAccessToken(accessToken);
    }

    /**
     * Sets the access token
     *
     * @param token       token
     * @param tokenSecret token secret
     * @since Twitter 2.0.0
     */
    public void setOAuthAccessToken(String token, String tokenSecret) {
        setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }


    /**
     *
     * @param consumerKey OAuth consumer key
     * @param consumerSecret OAuth consumer secret
     * @since Twitter 2.0.0
     */
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
        auth = new OAuthAuthorization(consumerKey, consumerSecret);
    }

    /**
     * Registers a RateLimitStatusListener for account associated rate limits
     * @param listener the listener to be added
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public void addRateLimitStatusListener(RateLimitStatusListener listener){
    	rateLimitStatusListeners.add(listener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OAuthTwitterSupport)) return false;
        if (!super.equals(o)) return false;

        OAuthTwitterSupport that = (OAuthTwitterSupport) o;

        if (!rateLimitStatusListeners.equals(that.rateLimitStatusListeners))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + rateLimitStatusListeners.hashCode();
        return result;
    }

    public void httpResponseReceived(HttpResponseEvent event) {
        if (0 < (rateLimitStatusListeners.size())) {
            Response res = event.getResponse();
            RateLimitStatus rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
            RateLimitStatusEvent statusEvent = null;
            if (null != rateLimitStatus) {
                statusEvent = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                for (RateLimitStatusListener listener : rateLimitStatusListeners) {
                    listener.rateLimitStatusUpdated(statusEvent);
                }
            }
            if(res.getStatusCode() == HttpClient.EXCEEDED_RATE_LIMIT_QUOTA
                    || res.getStatusCode() == HttpClient.SERVICE_UNAVAILABLE){
                // EXCEEDED_RATE_LIMIT_QUOTA is returned by Rest API
                // SERVICE_UNAVAILABLE is returned by Search API
                if (null == statusEvent) {
                    statusEvent = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                }
                for (RateLimitStatusListener listener : rateLimitStatusListeners) {
                    listener.onRateLimitReached(statusEvent);
                }
            }
        }
    }
}
