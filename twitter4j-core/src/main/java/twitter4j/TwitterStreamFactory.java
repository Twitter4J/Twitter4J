/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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
import twitter4j.http.AuthorizationFactory;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.OAuthAuthorization;

/**
 * A factory class for TwitterFactory.<br>
 * An instance of this class is completely thread safe and can be re-used and used concurrently.<br>
 * Note that TwitterStream is NOT compatible with Google App Engine as GAE is not capable of handling requests longer than 30 seconds.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class TwitterStreamFactory implements java.io.Serializable{
    private static final long serialVersionUID = 8146074704915782233L;
    private final StreamListener listener;
    private final Configuration conf;

    /**
     * Creates a TwitterStreamFactory with the root configuration.
     */
    public TwitterStreamFactory() {
        this(ConfigurationContext.getInstance());
    }

    /**
     * Creates a TwitterStreamFactory with the given configuration.
     * @param conf the configuration to use
     * @since Twitter4J 2.1.1
     */
    public TwitterStreamFactory(Configuration conf) {
        this.conf = conf;
        this.listener = null;
    }

    /**
     * Creates a TwitterStreamFactory with the root configuration and a specified status listener.
     * @param listener the listener
     * @deprecated use {@link TwitterStream#addListener(StatusListener)} instead.
     */
    public TwitterStreamFactory(StatusListener listener) {
        this.conf = ConfigurationContext.getInstance();
        this.listener = listener;
    }

    /**
     * Creates a TwitterStreamFactory with the root configuration and a specified status listener.
     * @param listener the listener
     * @since Twitter4J 2.1.8
     * @deprecated use {@link TwitterStream#addListener(UserStreamListener)}} instead.
     */
    public TwitterStreamFactory(UserStreamListener listener) {
        this.conf = ConfigurationContext.getInstance();
        this.listener = listener;
    }

    /**
     * Creates a TwitterStreamFactory with a specified config tree.
     * @param configTreePath the path
     */
    public TwitterStreamFactory(String configTreePath) {
        this(configTreePath, (StatusListener)null);
    }

    /**
     * Creates a TwitterStreamFactory with a specified config tree and a listener.
     * @param configTreePath the path
     * @param listener the listener
     * @deprecated use {@link TwitterStream#addListener(StatusListener)} instead.
     */
    public TwitterStreamFactory(String configTreePath, StatusListener listener) {
        this(ConfigurationContext.getInstance(configTreePath), listener);
    }

    /**
     * Creates a TwitterStreamFactory with a specified config tree and a listener.
     * @param configTreePath the path
     * @param listener the listener
     * @since Twitter4J 2.1.8
     * @deprecated use {@link TwitterStream#addListener(UserStreamListener)}} instead.
     */
    public TwitterStreamFactory(String configTreePath, UserStreamListener listener) {
        this(ConfigurationContext.getInstance(configTreePath), listener);
    }

    /**
     * Creates a TwitterStreamFactory with the specified config and a listener.
     * @param conf the configuration to use
     * @param listener an optional status listener
     * @since Twitter4J 2.1.1
     * @deprecated use {@link TwitterStream#addListener(StatusListener)} instead.
     */
    public TwitterStreamFactory(Configuration conf, StatusListener listener) {
        if (conf == null) {
          throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf;
        this.listener = listener;
    }

    /**
     * Creates a TwitterStreamFactory with the specified config and a listener.
     * @param conf the configuration to use
     * @param listener an optional status listener
     * @since Twitter4J 2.1.8
     * @deprecated use {@link TwitterStream#addListener(UserStreamListener)} instead.
     */
    public TwitterStreamFactory(Configuration conf, UserStreamListener listener) {
        if (conf == null) {
          throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf;
        this.listener = listener;
    }

    // implementations for BasicSupportFactory

    /**
     * Returns an instance.
     *
     * @return default instance
     */
    public TwitterStream getInstance(){
        return getInstance(conf);
    }

    /**
     * Returns an XAuth Authenticated instance.
     *
     * @param screenName screen name
     * @param password password
     * @return an instance
     */
    public TwitterStream getInstance(String screenName, String password){
        return getInstance(conf, new BasicAuthorization(screenName, password));
    }

    /**
     * Returns a OAuth Authenticated instance.<br>
     * consumer key and consumer Secret must be provided by twitter4j.properties, or system properties.
     * Unlike {@link TwitterStream#setOAuthAccessToken(twitter4j.http.AccessToken)}, this factory method potentially returns a cached instance.
     *
     * @param accessToken access token
     * @return an instance
     */
    public TwitterStream getInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret, accessToken);
        return getInstance(conf, oauth);
    }

    /**
     * Returns a OAuth Authenticated instance.
     *
     * @param consumerKey consumer key
     * @param consumerSecret consumer secret
     * @return an instance
     * @deprecated use {@link TwitterStream#setOAuthConsumer(String, String)}
     */
    public TwitterStream getOAuthAuthorizedInstance(String consumerKey, String consumerSecret) {
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        return getInstance(conf, oauth);
    }

    /**
     * Returns a OAuth Authenticated instance.<br>
     * consumer key and consumer Secret must be provided by twitter4j.properties, or system properties.
     *
     * @param accessToken access token
     * @return an instance
     * @deprecated use {@link #getInstance(twitter4j.http.AccessToken)} instead
     */
    public TwitterStream getOAuthAuthorizedInstance(AccessToken accessToken) {
        return getInstance(accessToken);
    }

    /**
     * Returns a instance.
     *
     * @return default singleton instance
     * @deprecated
     */
    public TwitterStream getInstance(Authorization auth){
        return getInstance(conf, auth);
    }
    private TwitterStream getInstance(Configuration conf, Authorization auth) {
        return new TwitterStream(conf, auth, listener);
    }
    private TwitterStream getInstance(Configuration conf) {
        return new TwitterStream(conf, AuthorizationFactory.getInstance(conf, true), listener);
    }
}
