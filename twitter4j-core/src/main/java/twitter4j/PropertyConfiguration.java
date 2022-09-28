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

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
final class PropertyConfiguration {

    private static final Logger logger = Logger.getLogger();
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
    private static final String STREAM_THREAD_NAME = "streamThreadName";

    private static final String CONTRIBUTING_TO = "contributingTo";
    private static final String INCLUDE_MY_RETWEET = "includeMyRetweet";
    private static final String INCLUDE_ENTITIES = "includeEntities";
    private static final String INCLUDE_EMAIL = "includeEmail";
    private static final String INCLUDE_EXT_ALT_TEXT = "includeExtAltText";
    private static final String TWEET_MODE_EXTENDED = "tweetModeExtended";
    private static final String JSON_STORE_ENABLED = "jsonStoreEnabled";
    private static final String MBEAN_ENABLED = "mbeanEnabled";
    private static final String STREAM_STALL_WARNINGS_ENABLED = "stream.enableStallWarnings";
    private static final String APPLICATION_ONLY_AUTH_ENABLED = "enableApplicationOnlyAuth";

    static void load(Configuration conf, Properties props) {
        setFieldsWithPrefix(conf, props);
    }

    static void loadDefaultProperties(Configuration conf) {
        Properties props = (Properties) System.getProperties().clone();
        // load from system properties
        try {
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
        } catch (SecurityException | FileNotFoundException ignore) {
        }
        setFieldsWithPrefix(conf, props);
    }

    private static boolean notNull(Properties props, String name) {
        return props.getProperty(name) != null;
    }

    private static void loadProperties(Properties props, String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                props.load(Files.newInputStream(file.toPath()));
            } catch (IOException ioe) {
                logger.warn("failed to load properties:" + file.getAbsolutePath(), ioe);
            }
            normalize(props);
        }
    }

    private static void loadProperties(Properties props, InputStream is) {
        try {
            props.load(is);
            normalize(props);
        } catch (Exception ignore) {
        }
    }

    private static void normalize(Properties props) {
        ArrayList<String> toBeNormalized = new ArrayList<>(10);
        for (Object key : props.keySet()) {
            String keyStr = (String) key;
            if (keyStr.contains("twitter4j.")) {
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


    private static void setFieldsWithPrefix(Configuration conf, Properties props) {
        if (notNull(props, USER)) {
            conf.setUser(getString(props, USER));
        }
        if (notNull(props, PASSWORD)) {
            conf.setPassword(getString(props, PASSWORD));
        }
        if (notNull(props, HTTP_PRETTY_DEBUG)) {
            conf.setPrettyDebugEnabled(getBoolean(props, HTTP_PRETTY_DEBUG));
        }
        if (notNull(props, HTTP_GZIP)) {
            conf.setGZIPEnabled(getBoolean(props, HTTP_GZIP));
        }
        if (notNull(props, HTTP_PROXY_HOST)) {
            conf.setHttpProxyHost(getString(props, HTTP_PROXY_HOST));
        } else if (notNull(props, HTTP_PROXY_HOST_FALLBACK)) {
            conf.setHttpProxyHost(getString(props, HTTP_PROXY_HOST_FALLBACK));
        }
        if (notNull(props, HTTP_PROXY_USER)) {
            conf.setHttpProxyUser(getString(props, HTTP_PROXY_USER));
        }
        if (notNull(props, HTTP_PROXY_PASSWORD)) {
            conf.setHttpProxyPassword(getString(props, HTTP_PROXY_PASSWORD));
        }
        if (notNull(props, HTTP_PROXY_PORT)) {
            conf.setHttpProxyPort(getIntProperty(props, HTTP_PROXY_PORT));
        } else if (notNull(props, HTTP_PROXY_PORT_FALLBACK)) {
            conf.setHttpProxyPort(getIntProperty(props, HTTP_PROXY_PORT_FALLBACK));
        }
        if (notNull(props, HTTP_CONNECTION_TIMEOUT)) {
            conf.setHttpConnectionTimeout(getIntProperty(props, HTTP_CONNECTION_TIMEOUT));
        }
        if (notNull(props, HTTP_READ_TIMEOUT)) {
            conf.setHttpReadTimeout(getIntProperty(props, HTTP_READ_TIMEOUT));
        }
        if (notNull(props, HTTP_STREAMING_READ_TIMEOUT)) {
            conf.setHttpStreamingReadTimeout(getIntProperty(props, HTTP_STREAMING_READ_TIMEOUT));
        }
        if (notNull(props, HTTP_RETRY_COUNT)) {
            conf.setHttpRetryCount(getIntProperty(props, HTTP_RETRY_COUNT));
        }
        if (notNull(props, HTTP_RETRY_INTERVAL_SECS)) {
            conf.setHttpRetryIntervalSeconds(getIntProperty(props, HTTP_RETRY_INTERVAL_SECS));
        }
        if (notNull(props, OAUTH_CONSUMER_KEY)) {
            conf.setOAuthConsumerKey(getString(props, OAUTH_CONSUMER_KEY));
        }
        if (notNull(props, OAUTH_CONSUMER_SECRET)) {
            conf.setOAuthConsumerSecret(getString(props, OAUTH_CONSUMER_SECRET));
        }
        if (notNull(props, OAUTH_ACCESS_TOKEN)) {
            conf.setOAuthAccessToken(getString(props, OAUTH_ACCESS_TOKEN));
        }
        if (notNull(props, OAUTH_ACCESS_TOKEN_SECRET)) {
            conf.setOAuthAccessTokenSecret(getString(props, OAUTH_ACCESS_TOKEN_SECRET));
        }
        if (notNull(props, OAUTH2_TOKEN_TYPE)) {
            conf.setOAuth2TokenType(getString(props, OAUTH2_TOKEN_TYPE));
        }
        if (notNull(props, OAUTH2_ACCESS_TOKEN)) {
            conf.setOAuth2AccessToken(getString(props, OAUTH2_ACCESS_TOKEN));
        }
        if (notNull(props, OAUTH2_SCOPE)) {
            conf.setOAuth2Scope(getString(props, OAUTH2_SCOPE));
        }
        if (notNull(props, STREAM_THREAD_NAME)) {
            conf.setStreamThreadName(getString(props, STREAM_THREAD_NAME));
        }
        if (notNull(props, CONTRIBUTING_TO)) {
            conf.setContributingTo(getLongProperty(props, CONTRIBUTING_TO));
        }
        if (notNull(props, OAUTH_REQUEST_TOKEN_URL)) {
            conf.setOAuthRequestTokenURL(getString(props, OAUTH_REQUEST_TOKEN_URL));
        }

        if (notNull(props, OAUTH_AUTHORIZATION_URL)) {
            conf.setOAuthAuthorizationURL(getString(props, OAUTH_AUTHORIZATION_URL));
        }

        if (notNull(props, OAUTH_ACCESS_TOKEN_URL)) {
            conf.setOAuthAccessTokenURL(getString(props, OAUTH_ACCESS_TOKEN_URL));
        }

        if (notNull(props, OAUTH_AUTHENTICATION_URL)) {
            conf.setOAuthAuthenticationURL(getString(props, OAUTH_AUTHENTICATION_URL));
        }

        if (notNull(props, OAUTH2_TOKEN_URL)) {
            conf.setOAuth2TokenURL(getString(props, OAUTH2_TOKEN_URL));
        }

        if (notNull(props, OAUTH2_INVALIDATE_TOKEN_URL)) {
            conf.setOAuth2InvalidateTokenURL(getString(props, OAUTH2_INVALIDATE_TOKEN_URL));
        }

        if (notNull(props, REST_BASE_URL)) {
            conf.setRestBaseURL(getString(props, REST_BASE_URL));
        }

        if (notNull(props, STREAM_BASE_URL)) {
            conf.setStreamBaseURL(getString(props, STREAM_BASE_URL));
        }
        if (notNull(props, INCLUDE_MY_RETWEET)) {
            conf.setIncludeMyRetweetEnabled(getBoolean(props, INCLUDE_MY_RETWEET));
        }
        if (notNull(props, INCLUDE_ENTITIES)) {
            conf.setIncludeEntitiesEnabled(getBoolean(props, INCLUDE_ENTITIES));
        }
        if (notNull(props, INCLUDE_EMAIL)) {
            conf.setIncludeEmailEnabled(getBoolean(props, INCLUDE_EMAIL));
        }
        if (notNull(props, INCLUDE_EXT_ALT_TEXT)) {
            conf.setIncludeExtAltTextEnabled(getBoolean(props, INCLUDE_EXT_ALT_TEXT));
        }
        if (notNull(props, TWEET_MODE_EXTENDED)) {
            conf.setTweetModeExtended(getBoolean(props, TWEET_MODE_EXTENDED));
        }
        if (notNull(props, JSON_STORE_ENABLED)) {
            conf.setJSONStoreEnabled(getBoolean(props, JSON_STORE_ENABLED));
        }
        if (notNull(props, MBEAN_ENABLED)) {
            conf.setMBeanEnabled(getBoolean(props, MBEAN_ENABLED));
        }
        if (notNull(props, STREAM_STALL_WARNINGS_ENABLED)) {
            conf.setStallWarningsEnabled(getBoolean(props, STREAM_STALL_WARNINGS_ENABLED));
        }
        if (notNull(props, APPLICATION_ONLY_AUTH_ENABLED)) {
            conf.setApplicationOnlyAuthEnabled(getBoolean(props, APPLICATION_ONLY_AUTH_ENABLED));
        }
    }

    private static boolean getBoolean(Properties props, String name) {
        String value = props.getProperty(name);
        return Boolean.parseBoolean(value);
    }

    private static int getIntProperty(Properties props, String name) {
        String value = props.getProperty(name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    private static long getLongProperty(Properties props, @SuppressWarnings("SameParameterValue") String name) {
        String value = props.getProperty(name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return -1L;
        }
    }

    private static String getString(Properties props, String name) {
        return props.getProperty(name);
    }
}
