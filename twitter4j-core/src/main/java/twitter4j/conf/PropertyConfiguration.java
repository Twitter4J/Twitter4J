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

package twitter4j.conf;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class PropertyConfiguration extends ConfigurationBase implements java.io.Serializable {

    private static final String DEBUG = "debug";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private static final String HTTP_PRETTY_DEBUG = "http.prettyDebug";
    private static final String HTTP_GZIP = "http.gzip";
    private static final String HTTP_PROXY_HOST = "http.proxyHost";
    private static final String HTTP_PROXY_HOST_FALLBACK = "http.proxyHost";
    private static final String HTTP_PROXY_USER = "http.proxyUser";
    private static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";
    private static final String HTTP_PROXY_PORT = "http.proxyPort";
    private static final String HTTP_PROXY_PORT_FALLBACK = "http.proxyPort";
    private static final String HTTP_CONNECTION_TIMEOUT = "http.connectionTimeout";
    private static final String HTTP_READ_TIMEOUT = "http.readTimeout";

    private static final String HTTP_STREAMING_READ_TIMEOUT = "http.streamingReadTimeout";

    private static final String HTTP_RETRY_COUNT = "http.retryCount";
    private static final String HTTP_RETRY_INTERVAL_SECS = "http.retryIntervalSecs";

    private static final String OAUTH_CONSUMER_KEY = "oauth.consumerKey";
    private static final String OAUTH_CONSUMER_SECRET = "oauth.consumerSecret";
    private static final String OAUTH_ACCESS_TOKEN = "oauth.accessToken";
    private static final String OAUTH_ACCESS_TOKEN_SECRET = "oauth.accessTokenSecret";
    private static final String OAUTH2_TOKEN_TYPE = "oauth2.tokenType";
    private static final String OAUTH2_ACCESS_TOKEN = "oauth2.accessToken";
    private static final String OAUTH2_SCOPE = "oauth2.scope";

    private static final String OAUTH_REQUEST_TOKEN_URL = "oauth.requestTokenURL";
    private static final String OAUTH_AUTHORIZATION_URL = "oauth.authorizationURL";
    private static final String OAUTH_ACCESS_TOKEN_URL = "oauth.accessTokenURL";
    private static final String OAUTH_AUTHENTICATION_URL = "oauth.authenticationURL";
    private static final String OAUTH2_TOKEN_URL = "oauth2.tokenURL";
    private static final String OAUTH2_INVALIDATE_TOKEN_URL = "oauth2.invalidateTokenURL";

    private static final String REST_BASE_URL = "restBaseURL";
    private static final String STREAM_BASE_URL = "streamBaseURL";
    private static final String USER_STREAM_BASE_URL = "userStreamBaseURL";
    private static final String SITE_STREAM_BASE_URL = "siteStreamBaseURL";

    private static final String ASYNC_NUM_THREADS = "async.numThreads";
    private static final String ASYNC_DAEMON_ENABLED = "async.daemonEnabled";
    private static final String CONTRIBUTING_TO = "contributingTo";
    private static final String ASYNC_DISPATCHER_IMPL = "async.dispatcherImpl";
    private static final String INCLUDE_MY_RETWEET = "includeMyRetweet";
    private static final String INCLUDE_ENTITIES = "includeEntities";
    private static final String LOGGER_FACTORY = "loggerFactory";
    private static final String JSON_STORE_ENABLED = "jsonStoreEnabled";
    private static final String MBEAN_ENABLED = "mbeanEnabled";
    private static final String STREAM_USER_REPLIES_ALL = "stream.user.repliesAll";
    private static final String STREAM_USER_WITH_FOLLOWINGS = "stream.user.withFollowings";
    private static final String STREAM_STALL_WARNINGS_ENABLED = "stream.enableStallWarnings";
    private static final String APPLICATION_ONLY_AUTH_ENABLED = "enableApplicationOnlyAuth";

    private static final String MEDIA_PROVIDER = "media.provider";
    private static final String MEDIA_PROVIDER_API_KEY = "media.providerAPIKey";
    private static final String MEDIA_PROVIDER_PARAMETERS = "media.providerParameters";
    private static final long serialVersionUID = -7262615247923693252L;
    private String OAuth2Scope;


    public PropertyConfiguration(InputStream is) {
        super();
        Properties props = new Properties();
        loadProperties(props, is);
        setFieldsWithTreePath(props, "/");
    }

    public PropertyConfiguration(Properties props) {
        this(props, "/");
    }

    public PropertyConfiguration(Properties props, String treePath) {
        super();
        setFieldsWithTreePath(props, treePath);
    }

    PropertyConfiguration(String treePath) {
        super();
        Properties props;
        // load from system properties
        try {
            props = (Properties) System.getProperties().clone();
            try {
                Map<String, String> envMap = System.getenv();
                for (String key : envMap.keySet()) {
                    props.setProperty(key, envMap.get(key));
                }
            } catch (SecurityException ignore) {
            }
            normalize(props);
        } catch (SecurityException ignore) {
            // Unsigned applets are not allowed to access System properties
            props = new Properties();
        }
        final String TWITTER4J_PROPERTIES = "twitter4j.properties";
        // override System properties with ./twitter4j.properties in the classpath
        loadProperties(props, "." + File.separatorChar + TWITTER4J_PROPERTIES);
        // then, override with /twitter4j.properties in the classpath
        loadProperties(props, Configuration.class.getResourceAsStream("/" + TWITTER4J_PROPERTIES));
        // then, override with /WEB/INF/twitter4j.properties in the classpath
        loadProperties(props, Configuration.class.getResourceAsStream("/WEB-INF/" + TWITTER4J_PROPERTIES));
        // for Google App Engine
        try {
            loadProperties(props, new FileInputStream("WEB-INF/" + TWITTER4J_PROPERTIES));
        } catch (SecurityException ignore) {
        } catch (FileNotFoundException ignore) {
        }

        setFieldsWithTreePath(props, treePath);
    }

    /**
     * Creates a root PropertyConfiguration. This constructor is equivalent to new PropertyConfiguration("/").
     */
    PropertyConfiguration() {
        this("/");
    }

    private boolean notNull(Properties props, String prefix, String name) {
        return props.getProperty(prefix + name) != null;
    }

    private boolean loadProperties(Properties props, String path) {
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                fis = new FileInputStream(file);
                props.load(fis);
                normalize(props);
                return true;
            }
        } catch (Exception ignore) {
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ignore) {

            }
        }
        return false;
    }

    private boolean loadProperties(Properties props, InputStream is) {
        try {
            props.load(is);
            normalize(props);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    private void normalize(Properties props) {
        ArrayList<String> toBeNormalized = new ArrayList<String>(10);
        for (Object key : props.keySet()) {
            String keyStr = (String) key;
            if (-1 != (keyStr.indexOf("twitter4j."))) {
                toBeNormalized.add(keyStr);
            }
        }
        for (String keyStr : toBeNormalized) {
            String property = props.getProperty(keyStr);
            int index = keyStr.indexOf("twitter4j.");
            String newKey = keyStr.substring(0, index) + keyStr.substring(index + 10);
            props.setProperty(newKey, property);
        }
    }

    /**
     * passing "/foo/bar" as treePath will result:<br>
     * 1. load [twitter4j.]restBaseURL<br>
     * 2. override the value with foo.[twitter4j.]restBaseURL<br>
     * 3. override the value with foo.bar.[twitter4j.]restBaseURL<br>
     *
     * @param props    properties to be loaded
     * @param treePath the path
     */
    private void setFieldsWithTreePath(Properties props, String treePath) {
        setFieldsWithPrefix(props, "");
        String[] splitArray = treePath.split("/");
        String prefix = null;
        for (String split : splitArray) {
            if (!"".equals(split)) {
                if (null == prefix) {
                    prefix = split + ".";
                } else {
                    prefix += split + ".";
                }
                setFieldsWithPrefix(props, prefix);
            }
        }
    }

    private void setFieldsWithPrefix(Properties props, String prefix) {
        if (notNull(props, prefix, DEBUG)) {
            setDebug(getBoolean(props, prefix, DEBUG));
        }

        if (notNull(props, prefix, USER)) {
            setUser(getString(props, prefix, USER));
        }
        if (notNull(props, prefix, PASSWORD)) {
            setPassword(getString(props, prefix, PASSWORD));
        }
        if (notNull(props, prefix, HTTP_PRETTY_DEBUG)) {
            setPrettyDebugEnabled(getBoolean(props, prefix, HTTP_PRETTY_DEBUG));
        }
        if (notNull(props, prefix, HTTP_GZIP)) {
            setGZIPEnabled(getBoolean(props, prefix, HTTP_GZIP));
        }
        if (notNull(props, prefix, HTTP_PROXY_HOST)) {
            setHttpProxyHost(getString(props, prefix, HTTP_PROXY_HOST));
        } else if (notNull(props, prefix, HTTP_PROXY_HOST_FALLBACK)) {
            setHttpProxyHost(getString(props, prefix, HTTP_PROXY_HOST_FALLBACK));
        }
        if (notNull(props, prefix, HTTP_PROXY_USER)) {
            setHttpProxyUser(getString(props, prefix, HTTP_PROXY_USER));
        }
        if (notNull(props, prefix, HTTP_PROXY_PASSWORD)) {
            setHttpProxyPassword(getString(props, prefix, HTTP_PROXY_PASSWORD));
        }
        if (notNull(props, prefix, HTTP_PROXY_PORT)) {
            setHttpProxyPort(getIntProperty(props, prefix, HTTP_PROXY_PORT));
        } else if (notNull(props, prefix, HTTP_PROXY_PORT_FALLBACK)) {
            setHttpProxyPort(getIntProperty(props, prefix, HTTP_PROXY_PORT_FALLBACK));
        }
        if (notNull(props, prefix, HTTP_CONNECTION_TIMEOUT)) {
            setHttpConnectionTimeout(getIntProperty(props, prefix, HTTP_CONNECTION_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_READ_TIMEOUT)) {
            setHttpReadTimeout(getIntProperty(props, prefix, HTTP_READ_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_STREAMING_READ_TIMEOUT)) {
            setHttpStreamingReadTimeout(getIntProperty(props, prefix, HTTP_STREAMING_READ_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_RETRY_COUNT)) {
            setHttpRetryCount(getIntProperty(props, prefix, HTTP_RETRY_COUNT));
        }
        if (notNull(props, prefix, HTTP_RETRY_INTERVAL_SECS)) {
            setHttpRetryIntervalSeconds(getIntProperty(props, prefix, HTTP_RETRY_INTERVAL_SECS));
        }
        if (notNull(props, prefix, OAUTH_CONSUMER_KEY)) {
            setOAuthConsumerKey(getString(props, prefix, OAUTH_CONSUMER_KEY));
        }
        if (notNull(props, prefix, OAUTH_CONSUMER_SECRET)) {
            setOAuthConsumerSecret(getString(props, prefix, OAUTH_CONSUMER_SECRET));
        }
        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN)) {
            setOAuthAccessToken(getString(props, prefix, OAUTH_ACCESS_TOKEN));
        }
        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN_SECRET)) {
            setOAuthAccessTokenSecret(getString(props, prefix, OAUTH_ACCESS_TOKEN_SECRET));
        }
        if (notNull(props, prefix, OAUTH2_TOKEN_TYPE)) {
            setOAuth2TokenType(getString(props, prefix, OAUTH2_TOKEN_TYPE));
        }
        if (notNull(props, prefix, OAUTH2_ACCESS_TOKEN)) {
            setOAuth2AccessToken(getString(props, prefix, OAUTH2_ACCESS_TOKEN));
        }
        if (notNull(props, prefix, OAUTH2_SCOPE)) {
            setOAuth2Scope(getString(props, prefix, OAUTH2_SCOPE));
        }
        if (notNull(props, prefix, ASYNC_NUM_THREADS)) {
            setAsyncNumThreads(getIntProperty(props, prefix, ASYNC_NUM_THREADS));
        }
        if (notNull(props, prefix, ASYNC_DAEMON_ENABLED)) {
            setDaemonEnabled(getBoolean(props, prefix, ASYNC_DAEMON_ENABLED));
        }
        if (notNull(props, prefix, CONTRIBUTING_TO)) {
            setContributingTo(getLongProperty(props, prefix, CONTRIBUTING_TO));
        }
        if (notNull(props, prefix, ASYNC_DISPATCHER_IMPL)) {
            setDispatcherImpl(getString(props, prefix, ASYNC_DISPATCHER_IMPL));
        }

        if (notNull(props, prefix, OAUTH_REQUEST_TOKEN_URL)) {
            setOAuthRequestTokenURL(getString(props, prefix, OAUTH_REQUEST_TOKEN_URL));
        }

        if (notNull(props, prefix, OAUTH_AUTHORIZATION_URL)) {
            setOAuthAuthorizationURL(getString(props, prefix, OAUTH_AUTHORIZATION_URL));
        }

        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN_URL)) {
            setOAuthAccessTokenURL(getString(props, prefix, OAUTH_ACCESS_TOKEN_URL));
        }

        if (notNull(props, prefix, OAUTH_AUTHENTICATION_URL)) {
            setOAuthAuthenticationURL(getString(props, prefix, OAUTH_AUTHENTICATION_URL));
        }

        if (notNull(props, prefix, OAUTH2_TOKEN_URL)) {
            setOAuth2TokenURL(getString(props, prefix, OAUTH2_TOKEN_URL));
        }

        if (notNull(props, prefix, OAUTH2_INVALIDATE_TOKEN_URL)) {
            setOAuth2InvalidateTokenURL(getString(props, prefix, OAUTH2_INVALIDATE_TOKEN_URL));
        }

        if (notNull(props, prefix, REST_BASE_URL)) {
            setRestBaseURL(getString(props, prefix, REST_BASE_URL));
        }

        if (notNull(props, prefix, STREAM_BASE_URL)) {
            setStreamBaseURL(getString(props, prefix, STREAM_BASE_URL));
        }
        if (notNull(props, prefix, USER_STREAM_BASE_URL)) {
            setUserStreamBaseURL(getString(props, prefix, USER_STREAM_BASE_URL));
        }
        if (notNull(props, prefix, SITE_STREAM_BASE_URL)) {
            setSiteStreamBaseURL(getString(props, prefix, SITE_STREAM_BASE_URL));
        }
        if (notNull(props, prefix, INCLUDE_MY_RETWEET)) {
            setIncludeMyRetweetEnabled(getBoolean(props, prefix, INCLUDE_MY_RETWEET));
        }
        if (notNull(props, prefix, INCLUDE_ENTITIES)) {
            setIncludeEntitiesEnabled(getBoolean(props, prefix, INCLUDE_ENTITIES));
        }
        if (notNull(props, prefix, LOGGER_FACTORY)) {
            setLoggerFactory(getString(props, prefix, LOGGER_FACTORY));
        }
        if (notNull(props, prefix, JSON_STORE_ENABLED)) {
            setJSONStoreEnabled(getBoolean(props, prefix, JSON_STORE_ENABLED));
        }
        if (notNull(props, prefix, MBEAN_ENABLED)) {
            setMBeanEnabled(getBoolean(props, prefix, MBEAN_ENABLED));
        }
        if (notNull(props, prefix, STREAM_USER_REPLIES_ALL)) {
            setUserStreamRepliesAllEnabled(getBoolean(props, prefix, STREAM_USER_REPLIES_ALL));
        }
        if (notNull(props, prefix, STREAM_USER_WITH_FOLLOWINGS)) {
            setUserStreamWithFollowingsEnabled(getBoolean(props, prefix, STREAM_USER_WITH_FOLLOWINGS));
        }
        if (notNull(props, prefix, STREAM_STALL_WARNINGS_ENABLED)) {
            setStallWarningsEnabled(getBoolean(props, prefix, STREAM_STALL_WARNINGS_ENABLED));
        }
        if (notNull(props, prefix, APPLICATION_ONLY_AUTH_ENABLED)) {
            setApplicationOnlyAuthEnabled(getBoolean(props, prefix, APPLICATION_ONLY_AUTH_ENABLED));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER)) {
            setMediaProvider(getString(props, prefix, MEDIA_PROVIDER));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER_API_KEY)) {
            setMediaProviderAPIKey(getString(props, prefix, MEDIA_PROVIDER_API_KEY));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER_PARAMETERS)) {
            String[] propsAry = getString(props, prefix, MEDIA_PROVIDER_PARAMETERS).split("&");
            Properties p = new Properties();
            for (String str : propsAry) {
                String[] parameter = str.split("=");
                p.setProperty(parameter[0], parameter[1]);
            }
            setMediaProviderParameters(p);
        }
        cacheInstance();
    }

    boolean getBoolean(Properties props, String prefix, String name) {
        String value = props.getProperty(prefix + name);
        return Boolean.valueOf(value);
    }

    int getIntProperty(Properties props, String prefix, String name) {
        String value = props.getProperty(prefix + name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    long getLongProperty(Properties props, String prefix, String name) {
        String value = props.getProperty(prefix + name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return -1L;
        }
    }

    String getString(Properties props, String prefix, String name) {
        return props.getProperty(prefix + name);
    }

    // assures equality after deserialization
    @Override
    protected Object readResolve() throws ObjectStreamException {
        return super.readResolve();
    }

}
