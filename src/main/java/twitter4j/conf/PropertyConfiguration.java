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
package twitter4j.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class PropertyConfiguration extends Configuration {
    public static final String DEBUG = "twitter4j.debug";
    public static final String SOURCE = "twitter4j.source";
    public static final String HTTP_USER_AGENT = "twitter4j.http.userAgent";
    public static final String USER = "twitter4j.user";
    public static final String PASSWORD = "twitter4j.password";
    public static final String HTTP_USE_SSL = "twitter4j.http.useSSL";
    public static final String HTTP_PROXY_HOST = "twitter4j.http.proxyHost";
    public static final String HTTP_PROXY_HOST_FALLBACK = "http.proxyHost";
    public static final String HTTP_PROXY_USER = "twitter4j.http.proxyUser";
    public static final String HTTP_PROXY_PASSWORD = "twitter4j.http.proxyPassword";
    public static final String HTTP_PROXY_PORT = "twitter4j.http.proxyPort";
    public static final String HTTP_PROXY_PORT_FALLBACK = "http.proxyPort";
    public static final String HTTP_CONNECTION_TIMEOUT = "twitter4j.http.connectionTimeout";
    public static final String HTTP_READ_TIMEOUT = "twitter4j.http.readTimeout";
    public static final String HTTP_RETRY_COUNT = "twitter4j.http.retryCount";
    public static final String HTTP_RETRY_INTERVAL_SECS = "twitter4j.http.retryIntervalSecs";
    public static final String OAUTH_CONSUMER_KEY = "twitter4j.oauth.consumerKey";
    public static final String OAUTH_CONSUMER_SECRET = "twitter4j.oauth.consumerSecret";
    public static final String OAUTH_ACCESS_TOKEN = "twitter4j.oauth.accessToken";
    public static final String OAUTH_ACCESS_TOKEN_SECRET = "twitter4j.oauth.accessTokenSecret";


    public static final String OAUTH_REQUEST_TOKEN_URL = "twitter4j.oauth.requestTokenURL";
    public static final String OAUTH_AUTHORIZATION_URL = "twitter4j.oauth.authorizationURL";
    public static final String OAUTH_ACCESS_TOKEN_URL = "twitter4j.oauth.accessTokenURL";
    public static final String OAUTH_AUTHENTICATION_URL = "twitter4j.oauth.authenticationURL";

    public static final String REST_BASE_URL = "twitter4j.restBaseURL";
    public static final String SEARCH_BASE_URL = "twitter4j.searchBaseURL";
    public static final String STREAM_BASE_URL = "twitter4j.streamBaseURL";


    public static final String ASYNC_NUM_THREADS = "twitter4j.async.numThreads";

    // hidden portion
    public static final String CLIENT_VERSION = "twitter4j.clientVersion";
    public static final String CLIENT_URL = "twitter4j.clientURL";

    PropertyConfiguration() {
        super();
        Properties props;
        // load from system properties
        try {
            props = System.getProperties();
        } catch (AccessControlException ace) {
            // Unsigned applets are not allowed to access System properties
            props = new Properties();
        }
        final String TWITTER4J_PROPERTIES = "twitter4j.properties";
        // override System properties with ./twiter4j.properties in the classpath
        loadProperties(props, "." + File.separatorChar + TWITTER4J_PROPERTIES);
        // then, override with /twiter4j.properties in the classpath
        loadProperties(props, Configuration.class.getResourceAsStream("/" + TWITTER4J_PROPERTIES));
        // then, override with /WEB/INF/twiter4j.properties in the classpath
        loadProperties(props, Configuration.class.getResourceAsStream("/WEB-INF/" + TWITTER4J_PROPERTIES));

        if (notNull(props, DEBUG)) {
            setDebug(getBoolean(props, DEBUG));
        }

        if (notNull(props, SOURCE)) {
            setSource(getString(props, SOURCE));
        }
        if (notNull(props, USER)) {
            setUser(getString(props, USER));
        }
        if (notNull(props, PASSWORD)) {
            setPassword(getString(props, PASSWORD));
        }
        if (notNull(props, HTTP_USE_SSL)) {
            setUseSSL(getBoolean(props, HTTP_USE_SSL));
        }
        if (notNull(props, HTTP_PROXY_HOST)) {
            setHttpProxyHost(getString(props, HTTP_PROXY_HOST));
        } else if (notNull(props, HTTP_PROXY_HOST_FALLBACK)) {
            setHttpProxyHost(getString(props, HTTP_PROXY_HOST_FALLBACK));
        }
        if (notNull(props, HTTP_PROXY_USER)) {
            setHttpProxyUser(getString(props, HTTP_PROXY_USER));
        }
        if (notNull(props, HTTP_PROXY_PASSWORD)) {
            setHttpProxyPassword(getString(props, HTTP_PROXY_PASSWORD));
        }
        if (notNull(props, HTTP_PROXY_PORT)) {
            setHttpProxyPort(getIntProperty(props, HTTP_PROXY_PORT));
        } else if (notNull(props, HTTP_PROXY_PORT_FALLBACK)) {
            setHttpProxyPort(getIntProperty(props, HTTP_PROXY_PORT_FALLBACK));
        }
        if (notNull(props, HTTP_CONNECTION_TIMEOUT)) {
            setHttpConnectionTimeout(getIntProperty(props, HTTP_CONNECTION_TIMEOUT));
        }
        if (notNull(props, HTTP_READ_TIMEOUT)) {
            setHttpReadTimeout(getIntProperty(props, HTTP_READ_TIMEOUT));
        }
        if (notNull(props, HTTP_RETRY_COUNT)) {
            setHttpRetryCount(getIntProperty(props, HTTP_RETRY_COUNT));
        }
        if (notNull(props, HTTP_RETRY_INTERVAL_SECS)) {
            setHttpRetryIntervalSecs(getIntProperty(props, HTTP_RETRY_INTERVAL_SECS));
        }
        if (notNull(props, OAUTH_CONSUMER_KEY)) {
            setOAuthConsumerKey(getString(props, OAUTH_CONSUMER_KEY));
        }
        if (notNull(props, OAUTH_CONSUMER_SECRET)) {
            setOAuthConsumerSecret(getString(props, OAUTH_CONSUMER_SECRET));
        }
        if (notNull(props, OAUTH_ACCESS_TOKEN)) {
            setOAuthAccessToken(getString(props, OAUTH_ACCESS_TOKEN));
        }
        if (notNull(props, OAUTH_ACCESS_TOKEN_SECRET)) {
            setOAuthAccessTokenSecret(getString(props, OAUTH_ACCESS_TOKEN_SECRET));
        }
        if (notNull(props, ASYNC_NUM_THREADS)) {
            setAsyncNumThreads(getIntProperty(props, ASYNC_NUM_THREADS));
        }
        if (notNull(props, CLIENT_VERSION)) {
            setClientVersion(getString(props, CLIENT_VERSION));
        }
        if (notNull(props, CLIENT_URL)) {
            setClientURL(getString(props, CLIENT_URL));
        }
        if (notNull(props, HTTP_USER_AGENT)) {
            setUserAgent(getString(props, HTTP_USER_AGENT));
        }

        if (notNull(props, OAUTH_REQUEST_TOKEN_URL)) {
            setOAuthRequestTokenURL(getString(props, OAUTH_REQUEST_TOKEN_URL));
        }

        if (notNull(props, OAUTH_AUTHORIZATION_URL)) {
            setOAuthAuthorizationURL(getString(props, OAUTH_AUTHORIZATION_URL));
        }

        if (notNull(props, OAUTH_ACCESS_TOKEN_URL)) {
            setOAuthAccessTokenURL(getString(props, OAUTH_ACCESS_TOKEN_URL));
        }

        if (notNull(props, OAUTH_AUTHENTICATION_URL)) {
            setOAuthAuthenticationURL(getString(props, OAUTH_AUTHENTICATION_URL));
        }

        if (notNull(props, REST_BASE_URL)) {
            setRestBaseURL(getString(props, REST_BASE_URL));
        }

        if (notNull(props, SEARCH_BASE_URL)) {
            setSearchBaseURL(getString(props, SEARCH_BASE_URL));
        }

        if (notNull(props, STREAM_BASE_URL)) {
            setStreamBaseURL(getString(props, STREAM_BASE_URL));
        }

    }

    private boolean notNull(Properties props, String name) {
        return null != props.getProperty(name);
    }

    private boolean loadProperties(Properties props, String path) {
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                fis = new FileInputStream(file);
                props.load(new FileInputStream(file));
                return true;
            }
        } catch (Exception ignore) {
        } finally {
            try {
                if (null != fis) {
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
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    protected boolean getBoolean(Properties props, String name) {
        String value = props.getProperty(name);
        return Boolean.valueOf(value);
    }

    protected int getIntProperty(Properties props, String name) {
        String value = props.getProperty(name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    protected String getString(Properties props, String name) {
        return props.getProperty(name);
    }
}
