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

import twitter4j.conf.Configuration;

/**
 * A factory class for Twitter.
 * <br>An instance of this class is completely thread safe and can be re-used and used concurrently.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public class TwitterFactory implements java.io.Serializable {
    /*AsyncTwitterFactory and TWitterStream will access this field*/
    static final Authorization DEFAULT_AUTHORIZATION = AuthorizationFactory.getInstance(Configuration.getInstance());
    private static final Twitter SINGLETON = new TwitterImpl(Configuration.getInstance(), DEFAULT_AUTHORIZATION);

    private static final long serialVersionUID = -563983536986910054L;
    private final Configuration conf;

    /**
     * Creates a TwitterFactory with the root configuration.
     */
    public TwitterFactory() {
        this(Configuration.getInstance());
    }

    /**
     * Creates a TwitterFactory with the given configuration.
     *
     * @param conf the configuration to use
     * @since Twitter4J 2.1.1
     */
    public TwitterFactory(Configuration conf) {
        if (conf == null) {
            throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf;
    }

    /**
     * Returns a instance associated with the configuration bound to this factory.
     *
     * @return default singleton instance
     */
    public Twitter getInstance() {
        return getInstance(AuthorizationFactory.getInstance(conf));
    }

    /**
     * Returns a OAuth Authenticated instance.<br>
     * consumer key and consumer Secret must be provided by twitter4j.properties, or system properties.<br>
     * This factory method potentially returns a cached instance.
     *
     * @param accessToken access token
     * @return an instance
     * @since Twitter4J 2.1.9
     */
    public Twitter getInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf);
        oauth.setOAuthAccessToken(accessToken);
        return getInstance(oauth);
    }

    public Twitter getInstance(Authorization auth) {
        return new TwitterImpl(conf, auth);
    }

    /**
     * Returns default singleton Twitter instance.
     *
     * @return default singleton Twitter instance
     * @since Twitter4J 2.2.4
     */
    public static Twitter getSingleton() {
        return SINGLETON;
    }
}
