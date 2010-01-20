package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.NullAuthorization;
import twitter4j.http.OAuthAuthorization;

abstract class TwitterFactoryBase<T extends OAuthSupportTwitter> {
    protected final Configuration conf;
    /**
     * Creates a Factory
     */
    protected TwitterFactoryBase() {
        this.conf = ConfigurationContext.getInstance();
    }

    /**
     * Creates a Factory with a specified config tree path.
     * @param configTreePath the path
     */
    protected TwitterFactoryBase(String configTreePath) {
        this.conf = ConfigurationContext.getInstance(configTreePath);
    }

    /**
     * Creates a Factory with a specified config tree path.
     */
    TwitterFactoryBase(Configuration conf) {
        this.conf = conf;
    }

    /**
     * Returns a instance.
     *
     * @return default singleton instance
     */
    public T getInstance() {
        return getInstance(conf, NullAuthorization.getInstance());
    }

    public T getInstance(Authorization auth) {
        return getInstance(conf, auth);
    }

    public T getBasicAuthorizedInstance(String screenName
            , String password) {
        return getInstance(new BasicAuthorization(screenName, password));
    }

    public T getOAuthAuthorizedInstance(String consumerKey, String consumerSecret) {
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        return getInstance(oauth);
    }

    public T getOAuthAuthorizedInstance(String consumerKey, String consumerSecret, AccessToken accessToken) {
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        oauth.setOAuthAccessToken(accessToken);
        return getInstance(oauth);
    }

    public T getOAuthAuthorizedInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret, accessToken);
        return getInstance(oauth);
    }

    protected abstract T getInstance(Configuration conf, Authorization auth);
}
