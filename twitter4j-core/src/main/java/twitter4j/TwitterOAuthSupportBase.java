package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.NullAuthorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.OAuthSupport;
import twitter4j.http.RequestToken;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseCode;
import twitter4j.internal.http.HttpResponseEvent;
import twitter4j.internal.http.HttpResponseListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

abstract class TwitterOAuthSupportBase extends TwitterBase implements HttpResponseCode, HttpResponseListener, OAuthSupport, java.io.Serializable {
    private static final long serialVersionUID = 1918940571188204638L;
    protected transient HttpClientWrapper http;

    private List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList<RateLimitStatusListener>(0);

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

    private void init() {
        if (auth instanceof NullAuthorization) {
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
            }
        }
        http = new HttpClientWrapper(conf);
        http.setHttpResponseListener(this);
    }

    /**
     * Free resources associated with this instance,
     * should be called on all instances constructed.
     */
    public void shutdown() {
        if (http != null) http.shutdown();
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


    /**
     * sets the OAuth consumer key and consumer secret
     *
     * @param consumerKey    OAuth consumer key
     * @param consumerSecret OAuth consumer secret
     * @throws IllegalStateException when OAuth consumer has already been set, or the instance is using basic authorization
     * @since Twitter 2.0.0
     */
    public abstract void setOAuthConsumer(String consumerKey, String consumerSecret);

    // implementation for OAuthSupport interface
    public abstract RequestToken getOAuthRequestToken() throws TwitterException;

    public abstract RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException;

    public abstract AccessToken getOAuthAccessToken() throws TwitterException;

    public abstract AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException;

    public abstract AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException;

    public abstract AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException;

    public abstract void setOAuthAccessToken(AccessToken accessToken);

    /**
     * Registers a RateLimitStatusListener for account associated rate limits
     *
     * @param listener the listener to be added
     * @see <a href="http://dev.twitter.com/pages/rate-limiting">Rate Limiting | dev.twitter.com</a>
     * @since Twitter4J 2.1.12
     */
    public void addRateLimitStatusListener(RateLimitStatusListener listener) {
        rateLimitStatusListeners.add(listener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TwitterOAuthSupportBase that = (TwitterOAuthSupportBase) o;

        if (http != null ? !http.equals(that.http) : that.http != null)
            return false;
        if (!rateLimitStatusListeners.equals(that.rateLimitStatusListeners))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (http != null ? http.hashCode() : 0);
        result = 31 * result + (rateLimitStatusListeners != null ? rateLimitStatusListeners.hashCode() : 0);
        return result;
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
}
