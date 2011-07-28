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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A factory class for Twitter.
 * <br>An instance of this class is completely thread safe and can be re-used and used concurrently.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class TwitterFactory implements java.io.Serializable {
    private static final Constructor<Twitter> TWITTER_CONSTRUCTOR;
    /*AsyncTwitterFactory and TWitterStream will access this field*/
    static final Authorization DEFAULT_AUTHORIZATION = AuthorizationFactory.getInstance(ConfigurationContext.getInstance());
    private static final Twitter SINGLETON;
    private static final long serialVersionUID = 5193900138477709155L;
    private final Configuration conf;

    static {
        String className = null;
        if (ConfigurationContext.getInstance().isGAE()) {
            final String APP_ENGINE_TWITTER_IMPL = "twitter4j.AppEngineTwitterImpl";
            try {
                Class.forName(APP_ENGINE_TWITTER_IMPL);
                className = APP_ENGINE_TWITTER_IMPL;
            } catch (ClassNotFoundException ignore) {
            }
        }
        if (className == null) {
            className = "twitter4j.TwitterImpl";
        }
        Constructor<Twitter> constructor;
        Class clazz;
        try {
            clazz = Class.forName(className);
            constructor = clazz.getDeclaredConstructor(Configuration.class, Authorization.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
        TWITTER_CONSTRUCTOR = constructor;

        try {
            SINGLETON = TWITTER_CONSTRUCTOR.newInstance(ConfigurationContext.getInstance(), DEFAULT_AUTHORIZATION);
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Creates a TwitterFactory with the root configuration.
     */
    public TwitterFactory() {
        this(ConfigurationContext.getInstance());
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
     * Creates a TwitterFactory with a specified config tree
     *
     * @param configTreePath the path
     */
    public TwitterFactory(String configTreePath) {
        this(ConfigurationContext.getInstance(configTreePath));
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
     * Unlike {@link Twitter#setOAuthAccessToken(twitter4j.auth.AccessToken)}, this factory method potentially returns a cached instance.
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
        try {
            return TWITTER_CONSTRUCTOR.newInstance(conf, auth);
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
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
