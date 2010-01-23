package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.http.*;

import java.io.IOException;
import java.io.ObjectInputStream;

abstract class TwitterOAuthSupportBase extends TwitterBase implements HttpResponseListener, OAuthSupport, java.io.Serializable {
    protected transient HttpClientWrapper http;

    protected RateLimitStatusListener rateLimitStatusListener = null;
    private static final long serialVersionUID = 6960663978976449394L;

    TwitterOAuthSupportBase(Configuration conf) {
        super(conf); // this will populate BasicAuthorization from the configuration
        init();
    }

    TwitterOAuthSupportBase(Configuration conf, String screenName, String password) {
        super(conf, screenName, password);
        init();
    }
    /*package*/ TwitterOAuthSupportBase(Configuration conf, Authorization auth) {
        super(conf, auth);
        init();
    }

    private void init(){
        if(auth instanceof NullAuthorization){
            // try to populate OAuthAuthorization if available in the configuration
            String consumerKey = conf.getOAuthConsumerKey();
            String consumerSecret = conf.getOAuthConsumerSecret();
            // try to find oauth tokens in the configuration
            if (null != consumerKey && null != consumerSecret) {
                OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
                String accessToken = conf.getOAuthAccessToken();
                String accessTokenSecret = conf.getOAuthAccessTokenSecret();
                if (null != accessToken && null != accessTokenSecret) {
                    oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                this.auth = oauth;
            }
        }
        http = new HttpClientWrapper(conf);
        http.setHttpResponseListener(this);
    }
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (rateLimitStatusListener instanceof java.io.Serializable) {
            out.writeObject(rateLimitStatusListener);
        } else {
            out.writeObject(null);
        }
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        rateLimitStatusListener = (RateLimitStatusListener)stream.readObject();
        http = new HttpClientWrapper(conf);
        http.setHttpResponseListener(this);
    }


    /**
     * sets the OAuth consumer key and consumer secret
     * @param consumerKey OAuth consumer key
     * @param consumerSecret OAuth consumer secret
     * @since Twitter 2.0.0
     * @throws IllegalStateException when OAuth consumer has already been set, or the instance is using basic authorization
     * @deprecated Use TwitterFactory.getInstance(String consumerKey, String consumerSecret)
     */
    public abstract void setOAuthConsumer(String consumerKey, String consumerSecret);

    // implementation for OAuthSupport interface
    /**
     * {@inheritDoc}
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public abstract RequestToken getOAuthRequestToken() throws TwitterException;

    /**
     * {@inheritDoc}
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public abstract RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException;

    /**
     * {@inheritDoc}
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public abstract AccessToken getOAuthAccessToken() throws TwitterException;

    /**
     * {@inheritDoc}
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public abstract AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException;

    /**
     * {@inheritDoc}
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public abstract AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException;

    /**
     * {@inheritDoc}
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public abstract AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException;
    /**
     * {@inheritDoc}
     * @deprecated Use TwitterFactory.getInstance(AccessToken accessToken)
     */
    public abstract void setOAuthAccessToken(AccessToken accessToken);

    public abstract AccessToken getOAuthAccessToken(String token, String tokenSecret) throws TwitterException;

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
     * @deprecated Use getOAuthAccessToken(RequestToken requestToken, String oauthVerifier)
     */
    public abstract AccessToken getOAuthAccessToken(String token
            , String tokenSecret, String pin) throws TwitterException;

    /**
     * Sets the access token
     *
     * @param token access token
     * @param tokenSecret access token secret
     * @since Twitter 2.0.0
     * @deprecated Use Twitter getInstance(AccessToken accessToken)
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public abstract void setOAuthAccessToken(String token, String tokenSecret);

    /**
     * tests if the instance is authenticated by Basic
     * @return returns true if the instance is authenticated by Basic
     */
    public boolean isOAuthEnabled() {
        return auth instanceof OAuthAuthorization && auth.isEnabled();
    }

    /**
     * Registers a RateLimitStatusListener for account associated rate limits
     * @param listener the listener to be added
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public void setRateLimitStatusListener(RateLimitStatusListener listener){
    	this.rateLimitStatusListener = listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterOAuthSupportBase)) return false;
        if (!super.equals(o)) return false;

        TwitterOAuthSupportBase that = (TwitterOAuthSupportBase) o;

        if (rateLimitStatusListener != null ? !rateLimitStatusListener.equals(that.rateLimitStatusListener) : that.rateLimitStatusListener != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (rateLimitStatusListener != null ? rateLimitStatusListener.hashCode() : 0);
        return result;
    }

    public void httpResponseReceived(HttpResponseEvent event) {
        if (null != rateLimitStatusListener) {
            HttpResponse res = event.getResponse();
            RateLimitStatus rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
            RateLimitStatusEvent statusEvent = null;
            if (null != rateLimitStatus) {
                statusEvent = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                if (res.getStatusCode() == HttpClient.EXCEEDED_RATE_LIMIT_QUOTA
                        || res.getStatusCode() == HttpClient.SERVICE_UNAVAILABLE) {
                    // EXCEEDED_RATE_LIMIT_QUOTA is returned by Rest API
                    // SERVICE_UNAVAILABLE is returned by Search API
                    rateLimitStatusListener.onRateLimitStatus(statusEvent);
                    rateLimitStatusListener.onRateLimitReached(statusEvent);
                } else {
                    rateLimitStatusListener.onRateLimitStatus(statusEvent);
                }
            }
        }
    }
}
