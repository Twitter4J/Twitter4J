package twitter4j;

import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpResponseListener;
import twitter4j.http.NullAuthorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.RequestToken;
import twitter4j.http.Response;

import java.util.ArrayList;
import java.util.List;

class OAuthTwitterSupport extends TwitterSupport implements java.io.Serializable {

    protected final List<RateLimitStatusListener> accountRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    protected final List<RateLimitStatusListener> ipRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
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
                OAuthAuthorization oauth = new OAuthAuthorization(consumerKey, consumerSecret, http, requestFactory);
                String accessToken = conf.getOAuthAccessToken();
                String accessTokenSecret = conf.getOAuthAccessTokenSecret();
                if (null != accessToken && null != accessTokenSecret) {
                    oauth.setAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                this.auth = oauth;
            }
        }
        HttpResponseListener httpResponseListener = new RateLimitListenerInvoker(accountRateLimitStatusListeners,ipRateLimitStatusListeners);
        http.addHttpResponseListener(httpResponseListener);
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
        auth = new OAuthAuthorization(consumerKey, consumerSecret, http);
    }

    /**
     * Registers a RateLimitStatusListener for account associated rate limits
     * @param listener the listener to be added
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public void addAccountRateLimitStatusListener(RateLimitStatusListener listener){
    	accountRateLimitStatusListeners.add(listener);
    }


    /**
     * Registers a RateLimitStatusListener for ip associated rate limits
     *
     * @param listener the listener to be added
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public void addIpRateLimitStatusListener(RateLimitStatusListener listener){
    	ipRateLimitStatusListeners.add(listener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OAuthTwitterSupport)) return false;
        if (!super.equals(o)) return false;

        OAuthTwitterSupport that = (OAuthTwitterSupport) o;

        if (!accountRateLimitStatusListeners.equals(that.accountRateLimitStatusListeners))
            return false;
        if (!ipRateLimitStatusListeners.equals(that.ipRateLimitStatusListeners))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + accountRateLimitStatusListeners.hashCode();
        result = 31 * result + ipRateLimitStatusListeners.hashCode();
        return result;
    }
}
class RateLimitListenerInvoker implements HttpResponseListener, java.io.Serializable{
    private List<RateLimitStatusListener> accountRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    private List<RateLimitStatusListener> ipRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    private static final long serialVersionUID = 2277134489548449905L;

    RateLimitListenerInvoker(List<RateLimitStatusListener> accountRateLimitStatusListeners
            , List<RateLimitStatusListener> ipRateLimitStatusListeners){
        this.accountRateLimitStatusListeners = accountRateLimitStatusListeners;
        this.ipRateLimitStatusListeners = ipRateLimitStatusListeners;
    }
    public void httpResponseReceived(HttpResponseEvent event) {
        if (0 < (accountRateLimitStatusListeners.size() + ipRateLimitStatusListeners.size())) {
            Response res = event.getResponse();
            RateLimitStatus rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
            if (null != rateLimitStatus) {
                if (event.isAuthenticated()) {
                    fireRateLimitStatusListenerUpdate(accountRateLimitStatusListeners, rateLimitStatus);
                } else {
                    fireRateLimitStatusListenerUpdate(ipRateLimitStatusListeners, rateLimitStatus);
                }
            }
        }
    }

    private void fireRateLimitStatusListenerUpdate(List<RateLimitStatusListener> listeners, RateLimitStatus status) {
        for (RateLimitStatusListener listener : listeners) {
            listener.rateLimitStatusUpdated(status);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitListenerInvoker)) return false;

        RateLimitListenerInvoker that = (RateLimitListenerInvoker) o;

        if (accountRateLimitStatusListeners != null ? !accountRateLimitStatusListeners.equals(that.accountRateLimitStatusListeners) : that.accountRateLimitStatusListeners != null)
            return false;
        if (ipRateLimitStatusListeners != null ? !ipRateLimitStatusListeners.equals(that.ipRateLimitStatusListeners) : that.ipRateLimitStatusListeners != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountRateLimitStatusListeners != null ? accountRateLimitStatusListeners.hashCode() : 0;
        result = 31 * result + (ipRateLimitStatusListeners != null ? ipRateLimitStatusListeners.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RateLimitListenerInvoker{" +
                "accountRateLimitStatusListeners=" + accountRateLimitStatusListeners +
                ", ipRateLimitStatusListeners=" + ipRateLimitStatusListeners +
                '}';
    }
}