/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.AuthorizationFactory;
import twitter4j.http.OAuthAuthorization;

/**
 * A factory class for Twitter.
 * <br>An instance of this class is completely thread safe and can be re-used and used concurrently.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class TwitterFactory implements java.io.Serializable {
    private static final long serialVersionUID = 5193900138477709155L;
    private final Configuration conf;

    /**
     * Creates a TwitterFactory with the root configuration.
     */
    public TwitterFactory() {
        this.conf = ConfigurationContext.getInstance();
    }

    TwitterFactory(Configuration conf) {
        this.conf = conf;
    }

    /**
     * Creates a TwitterFactory with a specified config tree
     * @param configTreePath the path
     */
    public TwitterFactory(String configTreePath) {
        this.conf = ConfigurationContext.getInstance(configTreePath);
    }
    /**
     * Returns a instance.
     *
     * @return default singleton instance
     */
    public Twitter getInstance() {
        return getInstance(conf, AuthorizationFactory.getInstance(conf, true));
    }

    public Twitter getInstance(Authorization auth) {
        return getInstance(conf, auth);
    }

    /**
     * Returns a Basic Authenticated instance.
     *
     * @param screenName screen name
     * @param password password
     * @return an instance
     */
    public Twitter getInstance(String screenName, String password) {
        return getInstance(new BasicAuthorization(screenName, password));
    }

    // factory methods for OAuth support

    /**
     * Returns a OAuth Authenticated instance.
     *
     * @param consumerKey consumer key
     * @param consumerSecret consumer secret
     * @return an instance
     */
    public Twitter getOAuthAuthorizedInstance(String consumerKey, String consumerSecret) {
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        return getInstance(oauth);
    }

    /**
     * Returns a OAuth Authenticated instance.
     *
     * @param consumerKey consumer key
     * @param consumerSecret consumer secret
     * @param accessToken access token
     * @return an instance
     */
    public Twitter getOAuthAuthorizedInstance(String consumerKey, String consumerSecret, AccessToken accessToken) {
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        oauth.setOAuthAccessToken(accessToken);
        return getInstance(oauth);
    }

    /**
     * Returns a OAuth Authenticated instance.<br>
     * consumer key and consumer Secret must be provided by twitter4j.properties, or system properties.
     *
     * @param accessToken access token
     * @return an instance
     */
    public Twitter getOAuthAuthorizedInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret, accessToken);
        return getInstance(oauth);
    }
    private Twitter getInstance(Configuration conf, Authorization auth){
        return new Twitter(conf, auth);
    }
    private Twitter getInstance(Configuration conf){
        return new Twitter(conf, AuthorizationFactory.getInstance(conf, true));
    }
}