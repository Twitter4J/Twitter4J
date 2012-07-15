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

import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;


/**
 * A factory class for AsyncTwitter.<br>
 * An instance of this class is completely thread safe and can be re-used and used concurrently.<br>
 * Note that currently AsyncTwitter is NOT compatible with Google App Engine as it is maintaining threads internally.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class AsyncTwitterFactory implements java.io.Serializable {
    private final Configuration conf;
    private static final long serialVersionUID = -2565686715640816219L;
    private static final AsyncTwitter SINGLETON;

    static {
        SINGLETON = new AsyncTwitterImpl(ConfigurationContext.getInstance(), TwitterFactory.DEFAULT_AUTHORIZATION);
    }

    /**
     * Creates an AsyncTwitterFactory with the root configuration, with no listener. AsyncTwitter instances will not perform callbacks when using this constructor.
     */
    public AsyncTwitterFactory() {
        this(ConfigurationContext.getInstance());
    }

    /**
     * Creates an AsyncTwitterFactory with the given configuration.
     *
     * @param conf the configuration to use
     * @since Twitter4J 2.1.1
     */
    public AsyncTwitterFactory(Configuration conf) {
        if (conf == null) {
            throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf;
    }

    /**
     * Creates a AsyncTwitterFactory with the specified config tree, with given listener
     *
     * @param configTreePath the path
     * @since Twitter4J 2.1.12
     */
    public AsyncTwitterFactory(String configTreePath) {
        this.conf = ConfigurationContext.getInstance(configTreePath);
    }

    /**
     * Returns an instance associated with the configuration bound to this factory.
     *
     * @return default singleton instance
     */
    public AsyncTwitter getInstance() {
        return getInstance(AuthorizationFactory.getInstance(conf));
    }

    /**
     * Returns a OAuth Authenticated instance.<br>
     * consumer key and consumer Secret must be provided by twitter4j.properties, or system properties.<br>
     * Unlike {@link AsyncTwitter#setOAuthAccessToken(twitter4j.auth.AccessToken)}, this factory method potentially returns a cached instance.
     *
     * @param accessToken access token
     * @return an instance
     */
    public AsyncTwitter getInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf);
        oauth.setOAuthConsumer(consumerKey, consumerSecret);
        oauth.setOAuthAccessToken(accessToken);
        return new AsyncTwitterImpl(conf, oauth);
    }

    /**
     * @param auth authorization
     * @return an instance
     */
    public AsyncTwitter getInstance(Authorization auth) {
        return new AsyncTwitterImpl(conf, auth);
    }

    /**
     * a kind of copy factory method constructs an AsyncTwitter from Twitter instance
     * @param twitter Twitter instance
     * @return an instance
     */
    public AsyncTwitter getInstance(Twitter twitter) {
        return new AsyncTwitterImpl(twitter.getConfiguration(), twitter.getAuthorization());
    }

    /**
     * Returns default singleton AsyncTwitter instance.
     *
     * @return default singleton AsyncTwitter instance
     * @since Twitter4J 2.2.4
     */
    public static AsyncTwitter getSingleton() {
        return SINGLETON;
    }
}
