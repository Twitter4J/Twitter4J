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

import twitter4j.HttpClientConfiguration;
import twitter4j.Logger;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Configuration base class with default settings.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class ConfigurationBase implements Configuration, java.io.Serializable {
    private static final long serialVersionUID = 6175546394599249696L;
    private boolean debug = false;
    private String user = null;
    private String password = null;
    private HttpClientConfiguration httpConf;

    private int httpStreamingReadTimeout = 40 * 1000;
    private int httpRetryCount = 0;
    private int httpRetryIntervalSeconds = 5;

    private String oAuthConsumerKey = null;
    private String oAuthConsumerSecret = null;
    private String oAuthAccessToken = null;
    private String oAuthAccessTokenSecret = null;
    private String oAuth2TokenType;
    private String oAuth2AccessToken;
    private String oAuth2Scope;
    private String oAuthRequestTokenURL = "https://api.twitter.com/oauth/request_token";
    private String oAuthAuthorizationURL = "https://api.twitter.com/oauth/authorize";
    private String oAuthAccessTokenURL = "https://api.twitter.com/oauth/access_token";
    private String oAuthAuthenticationURL = "https://api.twitter.com/oauth/authenticate";
    private String oAuth2TokenURL = "https://api.twitter.com/oauth2/token";
    private String oAuth2InvalidateTokenURL = "https://api.twitter.com/oauth2/invalidate_token";

    private String restBaseURL = "https://api.twitter.com/1.1/";
    private String streamBaseURL = "https://stream.twitter.com/1.1/";
    private String userStreamBaseURL = "https://userstream.twitter.com/1.1/";
    private String siteStreamBaseURL = "https://sitestream.twitter.com/1.1/";
    private String uploadBaseURL = "https://upload.twitter.com/1.1/";

    private String dispatcherImpl = "twitter4j.DispatcherImpl";
    private int asyncNumThreads = 1;

    private String loggerFactory = null;

    private long contributingTo = -1L;

    private boolean includeMyRetweetEnabled = true;
    private boolean includeEntitiesEnabled = true;
    private boolean trimUserEnabled = false;

    private boolean jsonStoreEnabled = false;

    private boolean mbeanEnabled = false;

    private boolean userStreamRepliesAllEnabled = false;
    private boolean userStreamWithFollowingsEnabled = true;
    private boolean stallWarningsEnabled = true;

    private boolean applicationOnlyAuthEnabled = false;

    private String mediaProvider = "TWITTER";
    private String mediaProviderAPIKey = null;
    private Properties mediaProviderParameters = null;
    private boolean daemonEnabled = true;


    protected ConfigurationBase() {
        httpConf = new MyHttpClientConfiguration(null // proxy host
                , null // proxy user
                , null // proxy password
                , -1 // proxy port
                , 20000 // connection timeout
                , 120000 // read timeout
                , false // pretty debug
                , true // gzip enabled
        );
    }

    class MyHttpClientConfiguration implements HttpClientConfiguration, Serializable {
        private static final long serialVersionUID = 8226866124868861058L;
        private String httpProxyHost = null;
        private String httpProxyUser = null;
        private String httpProxyPassword = null;
        private int httpProxyPort = -1;
        private int httpConnectionTimeout = 20000;
        private int httpReadTimeout = 120000;
        private boolean prettyDebug = false;
        private boolean gzipEnabled = true;

        MyHttpClientConfiguration(String httpProxyHost, String httpProxyUser, String httpProxyPassword, int httpProxyPort, int httpConnectionTimeout, int httpReadTimeout, boolean prettyDebug, boolean gzipEnabled) {
            this.httpProxyHost = httpProxyHost;
            this.httpProxyUser = httpProxyUser;
            this.httpProxyPassword = httpProxyPassword;
            this.httpProxyPort = httpProxyPort;
            this.httpConnectionTimeout = httpConnectionTimeout;
            this.httpReadTimeout = httpReadTimeout;
            this.prettyDebug = prettyDebug;
            this.gzipEnabled = gzipEnabled;
        }

        @Override
        public String getHttpProxyHost() {
            return httpProxyHost;
        }

        @Override
        public int getHttpProxyPort() {
            return httpProxyPort;
        }

        @Override
        public String getHttpProxyUser() {
            return httpProxyUser;
        }

        @Override
        public String getHttpProxyPassword() {
            return httpProxyPassword;
        }

        @Override
        public int getHttpConnectionTimeout() {
            return httpConnectionTimeout;
        }

        @Override
        public int getHttpReadTimeout() {
            return httpReadTimeout;
        }

        @Override
        public int getHttpRetryCount() {
            return httpRetryCount;
        }

        @Override
        public int getHttpRetryIntervalSeconds() {
            return httpRetryIntervalSeconds;
        }

        @Override
        public boolean isPrettyDebugEnabled() {
            return prettyDebug;
        }

        @Override
        public boolean isGZIPEnabled() {
            return gzipEnabled;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyHttpClientConfiguration that = (MyHttpClientConfiguration) o;

            if (gzipEnabled != that.gzipEnabled) return false;
            if (httpConnectionTimeout != that.httpConnectionTimeout) return false;
            if (httpProxyPort != that.httpProxyPort) return false;
            if (httpReadTimeout != that.httpReadTimeout) return false;
            if (prettyDebug != that.prettyDebug) return false;
            if (httpProxyHost != null ? !httpProxyHost.equals(that.httpProxyHost) : that.httpProxyHost != null)
                return false;
            if (httpProxyPassword != null ? !httpProxyPassword.equals(that.httpProxyPassword) : that.httpProxyPassword != null)
                return false;
            if (httpProxyUser != null ? !httpProxyUser.equals(that.httpProxyUser) : that.httpProxyUser != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = httpProxyHost != null ? httpProxyHost.hashCode() : 0;
            result = 31 * result + (httpProxyUser != null ? httpProxyUser.hashCode() : 0);
            result = 31 * result + (httpProxyPassword != null ? httpProxyPassword.hashCode() : 0);
            result = 31 * result + httpProxyPort;
            result = 31 * result + httpConnectionTimeout;
            result = 31 * result + httpReadTimeout;
            result = 31 * result + (prettyDebug ? 1 : 0);
            result = 31 * result + (gzipEnabled ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "MyHttpClientConfiguration{" +
                    "httpProxyHost='" + httpProxyHost + '\'' +
                    ", httpProxyUser='" + httpProxyUser + '\'' +
                    ", httpProxyPassword='" + httpProxyPassword + '\'' +
                    ", httpProxyPort=" + httpProxyPort +
                    ", httpConnectionTimeout=" + httpConnectionTimeout +
                    ", httpReadTimeout=" + httpReadTimeout +
                    ", prettyDebug=" + prettyDebug +
                    ", gzipEnabled=" + gzipEnabled +
                    '}';
        }
    }


    public void dumpConfiguration() {
        Logger log = Logger.getLogger(ConfigurationBase.class);
        if (debug) {
            Field[] fields = ConfigurationBase.class.getDeclaredFields();
            for (Field field : fields) {
                try {
                    Object value = field.get(this);
                    String strValue = String.valueOf(value);
                    if (value != null && field.getName().matches("oAuthConsumerSecret|oAuthAccessTokenSecret|password")) {
                        strValue = String.valueOf(value).replaceAll(".", "*");
                    }
                    log.debug(field.getName() + ": " + strValue);
                } catch (IllegalAccessException ignore) {
                }
            }
        }
    }

    @Override
    public final boolean isDebugEnabled() {
        return debug;
    }

    protected final void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public final String getUser() {
        return user;
    }

    protected final void setUser(String user) {
        this.user = user;
    }

    @Override
    public final String getPassword() {
        return password;
    }

    @Override
    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpConf;
    }

    protected final void setPassword(String password) {
        this.password = password;
    }

    protected final void setPrettyDebugEnabled(boolean prettyDebug) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , prettyDebug
                , httpConf.isGZIPEnabled()
        );
    }

    protected final void setGZIPEnabled(boolean gzipEnabled) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled()
                , gzipEnabled
        );
    }

    // methods for HttpClientConfiguration

    protected final void setHttpProxyHost(String proxyHost) {
        httpConf = new MyHttpClientConfiguration(proxyHost
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled()
                , httpConf.isGZIPEnabled()
        );
    }

    protected final void setHttpProxyUser(String proxyUser) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , proxyUser
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled()
                , httpConf.isGZIPEnabled()
        );
    }

    protected final void setHttpProxyPassword(String proxyPassword) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , proxyPassword
                , httpConf.getHttpProxyPort()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled()
                , httpConf.isGZIPEnabled()
        );
    }

    protected final void setHttpProxyPort(int proxyPort) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , proxyPort
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled()
                , httpConf.isGZIPEnabled()
        );
    }

    protected final void setHttpConnectionTimeout(int connectionTimeout) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , connectionTimeout
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled()
                , httpConf.isGZIPEnabled()
        );
    }

    protected final void setHttpReadTimeout(int readTimeout) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.getHttpConnectionTimeout()
                , readTimeout
                , httpConf.isPrettyDebugEnabled()
                , httpConf.isGZIPEnabled()
        );
    }

    @Override
    public int getHttpStreamingReadTimeout() {
        return httpStreamingReadTimeout;
    }

    protected final void setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
        this.httpStreamingReadTimeout = httpStreamingReadTimeout;
    }

    protected final void setHttpRetryCount(int retryCount) {
        this.httpRetryCount = retryCount;
    }

    protected final void setHttpRetryIntervalSeconds(int retryIntervalSeconds) {
        this.httpRetryIntervalSeconds = retryIntervalSeconds;
    }

    // oauth related setter/getters

    @Override
    public final String getOAuthConsumerKey() {
        return oAuthConsumerKey;
    }

    protected final void setOAuthConsumerKey(String oAuthConsumerKey) {
        this.oAuthConsumerKey = oAuthConsumerKey;
    }

    @Override
    public final String getOAuthConsumerSecret() {
        return oAuthConsumerSecret;
    }

    protected final void setOAuthConsumerSecret(String oAuthConsumerSecret) {
        this.oAuthConsumerSecret = oAuthConsumerSecret;
    }

    @Override
    public String getOAuthAccessToken() {
        return oAuthAccessToken;
    }

    protected final void setOAuthAccessToken(String oAuthAccessToken) {
        this.oAuthAccessToken = oAuthAccessToken;
    }

    @Override
    public String getOAuthAccessTokenSecret() {
        return oAuthAccessTokenSecret;
    }

    protected final void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
    }

    @Override
    public String getOAuth2TokenType() {
        return oAuth2TokenType;
    }

    protected final void setOAuth2TokenType(String oAuth2TokenType) {
        this.oAuth2TokenType = oAuth2TokenType;
    }

    @Override
    public String getOAuth2AccessToken() {
        return oAuth2AccessToken;
    }

    @Override
    public String getOAuth2Scope() {
        return oAuth2Scope;
    }

    protected final void setOAuth2AccessToken(String oAuth2AccessToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
    }

    protected final void setOAuth2Scope(String oAuth2Scope) {
        this.oAuth2Scope = oAuth2Scope;
    }

    @Override
    public final int getAsyncNumThreads() {
        return asyncNumThreads;
    }

    protected final void setAsyncNumThreads(int asyncNumThreads) {
        this.asyncNumThreads = asyncNumThreads;
    }

    @Override
    public final long getContributingTo() {
        return contributingTo;
    }

    protected final void setContributingTo(long contributingTo) {
        this.contributingTo = contributingTo;
    }

    @Override
    public String getRestBaseURL() {
        return restBaseURL;
    }

    protected final void setRestBaseURL(String restBaseURL) {
        this.restBaseURL = restBaseURL;
    }

    @Override
    public String getUploadBaseURL() {
        return uploadBaseURL;
    }

    protected final void setUploadBaseURL(String uploadBaseURL) {
        this.uploadBaseURL = uploadBaseURL;
    }

    @Override
    public String getStreamBaseURL() {
        return streamBaseURL;
    }

    protected final void setStreamBaseURL(String streamBaseURL) {
        this.streamBaseURL = streamBaseURL;
    }

    @Override
    public String getUserStreamBaseURL() {
        return userStreamBaseURL;
    }

    protected final void setUserStreamBaseURL(String siteStreamBaseURL) {
        this.userStreamBaseURL = siteStreamBaseURL;
    }

    @Override
    public String getSiteStreamBaseURL() {
        return siteStreamBaseURL;
    }

    protected final void setSiteStreamBaseURL(String siteStreamBaseURL) {
        this.siteStreamBaseURL = siteStreamBaseURL;
    }

    @Override
    public String getOAuthRequestTokenURL() {
        return oAuthRequestTokenURL;
    }

    protected final void setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
        this.oAuthRequestTokenURL = oAuthRequestTokenURL;
    }

    @Override
    public String getOAuthAuthorizationURL() {
        return oAuthAuthorizationURL;
    }

    protected final void setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
        this.oAuthAuthorizationURL = oAuthAuthorizationURL;
    }

    @Override
    public String getOAuthAccessTokenURL() {
        return oAuthAccessTokenURL;
    }

    protected final void setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
        this.oAuthAccessTokenURL = oAuthAccessTokenURL;
    }

    @Override
    public String getOAuthAuthenticationURL() {
        return oAuthAuthenticationURL;
    }

    protected final void setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
        this.oAuthAuthenticationURL = oAuthAuthenticationURL;
    }

    @Override
    public String getOAuth2TokenURL() {
        return oAuth2TokenURL;
    }

    protected final void setOAuth2TokenURL(String oAuth2TokenURL) {
        this.oAuth2TokenURL = oAuth2TokenURL;
    }

    @Override
    public String getOAuth2InvalidateTokenURL() {
        return oAuth2InvalidateTokenURL;
    }

    protected final void setOAuth2InvalidateTokenURL(String oAuth2InvalidateTokenURL) {
        this.oAuth2InvalidateTokenURL = oAuth2InvalidateTokenURL;
    }

    @Override
    public String getDispatcherImpl() {
        return dispatcherImpl;
    }

    protected final void setDispatcherImpl(String dispatcherImpl) {
        this.dispatcherImpl = dispatcherImpl;
    }

    @Override
    public String getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public boolean isIncludeEntitiesEnabled() {
        return includeEntitiesEnabled;
    }

    protected void setIncludeEntitiesEnabled(boolean includeEntitiesEnabled) {
        this.includeEntitiesEnabled = includeEntitiesEnabled;
    }

    protected final void setLoggerFactory(String loggerImpl) {
        this.loggerFactory = loggerImpl;
    }

    @Override
    public boolean isIncludeMyRetweetEnabled() {
        return this.includeMyRetweetEnabled;
    }

    public void setIncludeMyRetweetEnabled(boolean enabled) {
        this.includeMyRetweetEnabled = enabled;
    }

    @Override
    public boolean isTrimUserEnabled() {
        return this.trimUserEnabled;
    }

    @Override
    public boolean isDaemonEnabled() {
        return daemonEnabled;
    }

    protected void setDaemonEnabled(boolean daemonEnabled) {
        this.daemonEnabled = daemonEnabled;
    }

    public void setTrimUserEnabled(boolean enabled) {
        this.trimUserEnabled = enabled;
    }

    @Override
    public boolean isJSONStoreEnabled() {
        return this.jsonStoreEnabled;
    }

    protected final void setJSONStoreEnabled(boolean enabled) {
        this.jsonStoreEnabled = enabled;
    }

    @Override
    public boolean isMBeanEnabled() {
        return this.mbeanEnabled;
    }

    protected final void setMBeanEnabled(boolean enabled) {
        this.mbeanEnabled = enabled;
    }

    @Override
    public boolean isUserStreamRepliesAllEnabled() {
        return this.userStreamRepliesAllEnabled;
    }

    @Override
    public boolean isUserStreamWithFollowingsEnabled() {
        return this.userStreamWithFollowingsEnabled;
    }

    protected final void setUserStreamRepliesAllEnabled(boolean enabled) {
        this.userStreamRepliesAllEnabled = enabled;
    }

    protected final void setUserStreamWithFollowingsEnabled(boolean enabled) {
        this.userStreamWithFollowingsEnabled = enabled;
    }

    @Override
    public boolean isStallWarningsEnabled() {
        return stallWarningsEnabled;
    }

    protected final void setStallWarningsEnabled(boolean stallWarningsEnabled) {
        this.stallWarningsEnabled = stallWarningsEnabled;
    }

    @Override
    public boolean isApplicationOnlyAuthEnabled() {
        return applicationOnlyAuthEnabled;
    }

    protected final void setApplicationOnlyAuthEnabled(boolean applicationOnlyAuthEnabled) {
        this.applicationOnlyAuthEnabled = applicationOnlyAuthEnabled;
    }

    @Override
    public String getMediaProvider() {
        return this.mediaProvider;
    }

    protected final void setMediaProvider(String mediaProvider) {
        this.mediaProvider = mediaProvider;
    }

    @Override
    public String getMediaProviderAPIKey() {
        return this.mediaProviderAPIKey;
    }

    protected final void setMediaProviderAPIKey(String mediaProviderAPIKey) {
        this.mediaProviderAPIKey = mediaProviderAPIKey;
    }

    @Override
    public Properties getMediaProviderParameters() {
        return this.mediaProviderParameters;
    }

    protected final void setMediaProviderParameters(Properties props) {
        this.mediaProviderParameters = props;
    }

    static String fixURL(boolean useSSL, String url) {
        if (null == url) {
            return null;
        }
        int index = url.indexOf("://");
        if (-1 == index) {
            throw new IllegalArgumentException("url should contain '://'");
        }
        String hostAndLater = url.substring(index + 3);
        if (useSSL) {
            return "https://" + hostAndLater;
        } else {
            return "http://" + hostAndLater;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigurationBase that = (ConfigurationBase) o;

        if (applicationOnlyAuthEnabled != that.applicationOnlyAuthEnabled) return false;
        if (asyncNumThreads != that.asyncNumThreads) return false;
        if (contributingTo != that.contributingTo) return false;
        if (daemonEnabled != that.daemonEnabled) return false;
        if (debug != that.debug) return false;
        if (httpRetryCount != that.httpRetryCount) return false;
        if (httpRetryIntervalSeconds != that.httpRetryIntervalSeconds) return false;
        if (httpStreamingReadTimeout != that.httpStreamingReadTimeout) return false;
        if (includeEntitiesEnabled != that.includeEntitiesEnabled) return false;
        if (includeMyRetweetEnabled != that.includeMyRetweetEnabled) return false;
        if (jsonStoreEnabled != that.jsonStoreEnabled) return false;
        if (mbeanEnabled != that.mbeanEnabled) return false;
        if (stallWarningsEnabled != that.stallWarningsEnabled) return false;
        if (trimUserEnabled != that.trimUserEnabled) return false;
        if (userStreamRepliesAllEnabled != that.userStreamRepliesAllEnabled) return false;
        if (userStreamWithFollowingsEnabled != that.userStreamWithFollowingsEnabled) return false;
        if (dispatcherImpl != null ? !dispatcherImpl.equals(that.dispatcherImpl) : that.dispatcherImpl != null)
            return false;
        if (httpConf != null ? !httpConf.equals(that.httpConf) : that.httpConf != null) return false;
        if (loggerFactory != null ? !loggerFactory.equals(that.loggerFactory) : that.loggerFactory != null)
            return false;
        if (mediaProvider != null ? !mediaProvider.equals(that.mediaProvider) : that.mediaProvider != null)
            return false;
        if (mediaProviderAPIKey != null ? !mediaProviderAPIKey.equals(that.mediaProviderAPIKey) : that.mediaProviderAPIKey != null)
            return false;
        if (mediaProviderParameters != null ? !mediaProviderParameters.equals(that.mediaProviderParameters) : that.mediaProviderParameters != null)
            return false;
        if (oAuth2AccessToken != null ? !oAuth2AccessToken.equals(that.oAuth2AccessToken) : that.oAuth2AccessToken != null)
            return false;
        if (oAuth2InvalidateTokenURL != null ? !oAuth2InvalidateTokenURL.equals(that.oAuth2InvalidateTokenURL) : that.oAuth2InvalidateTokenURL != null)
            return false;
        if (oAuth2TokenType != null ? !oAuth2TokenType.equals(that.oAuth2TokenType) : that.oAuth2TokenType != null)
            return false;
        if (oAuth2TokenURL != null ? !oAuth2TokenURL.equals(that.oAuth2TokenURL) : that.oAuth2TokenURL != null)
            return false;
        if (oAuth2Scope != null ? !oAuth2Scope.equals(that.oAuth2Scope) : that.oAuth2Scope != null)
            return false;
        if (oAuthAccessToken != null ? !oAuthAccessToken.equals(that.oAuthAccessToken) : that.oAuthAccessToken != null)
            return false;
        if (oAuthAccessTokenSecret != null ? !oAuthAccessTokenSecret.equals(that.oAuthAccessTokenSecret) : that.oAuthAccessTokenSecret != null)
            return false;
        if (oAuthAccessTokenURL != null ? !oAuthAccessTokenURL.equals(that.oAuthAccessTokenURL) : that.oAuthAccessTokenURL != null)
            return false;
        if (oAuthAuthenticationURL != null ? !oAuthAuthenticationURL.equals(that.oAuthAuthenticationURL) : that.oAuthAuthenticationURL != null)
            return false;
        if (oAuthAuthorizationURL != null ? !oAuthAuthorizationURL.equals(that.oAuthAuthorizationURL) : that.oAuthAuthorizationURL != null)
            return false;
        if (oAuthConsumerKey != null ? !oAuthConsumerKey.equals(that.oAuthConsumerKey) : that.oAuthConsumerKey != null)
            return false;
        if (oAuthConsumerSecret != null ? !oAuthConsumerSecret.equals(that.oAuthConsumerSecret) : that.oAuthConsumerSecret != null)
            return false;
        if (oAuthRequestTokenURL != null ? !oAuthRequestTokenURL.equals(that.oAuthRequestTokenURL) : that.oAuthRequestTokenURL != null)
            return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (restBaseURL != null ? !restBaseURL.equals(that.restBaseURL) : that.restBaseURL != null) return false;
        if (uploadBaseURL != null ? !uploadBaseURL.equals(that.uploadBaseURL) : that.uploadBaseURL != null) return false;
        if (siteStreamBaseURL != null ? !siteStreamBaseURL.equals(that.siteStreamBaseURL) : that.siteStreamBaseURL != null)
            return false;
        if (streamBaseURL != null ? !streamBaseURL.equals(that.streamBaseURL) : that.streamBaseURL != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (userStreamBaseURL != null ? !userStreamBaseURL.equals(that.userStreamBaseURL) : that.userStreamBaseURL != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (debug ? 1 : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (httpConf != null ? httpConf.hashCode() : 0);
        result = 31 * result + httpStreamingReadTimeout;
        result = 31 * result + httpRetryCount;
        result = 31 * result + httpRetryIntervalSeconds;
        result = 31 * result + (oAuthConsumerKey != null ? oAuthConsumerKey.hashCode() : 0);
        result = 31 * result + (oAuthConsumerSecret != null ? oAuthConsumerSecret.hashCode() : 0);
        result = 31 * result + (oAuthAccessToken != null ? oAuthAccessToken.hashCode() : 0);
        result = 31 * result + (oAuthAccessTokenSecret != null ? oAuthAccessTokenSecret.hashCode() : 0);
        result = 31 * result + (oAuth2TokenType != null ? oAuth2TokenType.hashCode() : 0);
        result = 31 * result + (oAuth2AccessToken != null ? oAuth2AccessToken.hashCode() : 0);
        result = 31 * result + (oAuth2Scope != null ? oAuth2Scope.hashCode() : 0);
        result = 31 * result + (oAuthRequestTokenURL != null ? oAuthRequestTokenURL.hashCode() : 0);
        result = 31 * result + (oAuthAuthorizationURL != null ? oAuthAuthorizationURL.hashCode() : 0);
        result = 31 * result + (oAuthAccessTokenURL != null ? oAuthAccessTokenURL.hashCode() : 0);
        result = 31 * result + (oAuthAuthenticationURL != null ? oAuthAuthenticationURL.hashCode() : 0);
        result = 31 * result + (oAuth2TokenURL != null ? oAuth2TokenURL.hashCode() : 0);
        result = 31 * result + (oAuth2InvalidateTokenURL != null ? oAuth2InvalidateTokenURL.hashCode() : 0);
        result = 31 * result + (restBaseURL != null ? restBaseURL.hashCode() : 0);
        result = 31 * result + (uploadBaseURL != null ? uploadBaseURL.hashCode() : 0);
        result = 31 * result + (streamBaseURL != null ? streamBaseURL.hashCode() : 0);
        result = 31 * result + (userStreamBaseURL != null ? userStreamBaseURL.hashCode() : 0);
        result = 31 * result + (siteStreamBaseURL != null ? siteStreamBaseURL.hashCode() : 0);
        result = 31 * result + (dispatcherImpl != null ? dispatcherImpl.hashCode() : 0);
        result = 31 * result + asyncNumThreads;
        result = 31 * result + (loggerFactory != null ? loggerFactory.hashCode() : 0);
        result = 31 * result + (int) (contributingTo ^ (contributingTo >>> 32));
        result = 31 * result + (includeMyRetweetEnabled ? 1 : 0);
        result = 31 * result + (includeEntitiesEnabled ? 1 : 0);
        result = 31 * result + (trimUserEnabled ? 1 : 0);
        result = 31 * result + (jsonStoreEnabled ? 1 : 0);
        result = 31 * result + (mbeanEnabled ? 1 : 0);
        result = 31 * result + (userStreamRepliesAllEnabled ? 1 : 0);
        result = 31 * result + (userStreamWithFollowingsEnabled ? 1 : 0);
        result = 31 * result + (stallWarningsEnabled ? 1 : 0);
        result = 31 * result + (applicationOnlyAuthEnabled ? 1 : 0);
        result = 31 * result + (mediaProvider != null ? mediaProvider.hashCode() : 0);
        result = 31 * result + (mediaProviderAPIKey != null ? mediaProviderAPIKey.hashCode() : 0);
        result = 31 * result + (mediaProviderParameters != null ? mediaProviderParameters.hashCode() : 0);
        result = 31 * result + (daemonEnabled ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConfigurationBase{" +
                "debug=" + debug +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", httpConf=" + httpConf +
                ", httpStreamingReadTimeout=" + httpStreamingReadTimeout +
                ", httpRetryCount=" + httpRetryCount +
                ", httpRetryIntervalSeconds=" + httpRetryIntervalSeconds +
                ", oAuthConsumerKey='" + oAuthConsumerKey + '\'' +
                ", oAuthConsumerSecret='" + oAuthConsumerSecret + '\'' +
                ", oAuthAccessToken='" + oAuthAccessToken + '\'' +
                ", oAuthAccessTokenSecret='" + oAuthAccessTokenSecret + '\'' +
                ", oAuth2TokenType='" + oAuth2TokenType + '\'' +
                ", oAuth2AccessToken='" + oAuth2AccessToken + '\'' +
                ", oAuth2Scope='" + oAuth2Scope + '\'' +
                ", oAuthRequestTokenURL='" + oAuthRequestTokenURL + '\'' +
                ", oAuthAuthorizationURL='" + oAuthAuthorizationURL + '\'' +
                ", oAuthAccessTokenURL='" + oAuthAccessTokenURL + '\'' +
                ", oAuthAuthenticationURL='" + oAuthAuthenticationURL + '\'' +
                ", oAuth2TokenURL='" + oAuth2TokenURL + '\'' +
                ", oAuth2InvalidateTokenURL='" + oAuth2InvalidateTokenURL + '\'' +
                ", restBaseURL='" + restBaseURL + '\'' +
                ", uploadBaseURL='" + uploadBaseURL + '\'' +
                ", streamBaseURL='" + streamBaseURL + '\'' +
                ", userStreamBaseURL='" + userStreamBaseURL + '\'' +
                ", siteStreamBaseURL='" + siteStreamBaseURL + '\'' +
                ", dispatcherImpl='" + dispatcherImpl + '\'' +
                ", asyncNumThreads=" + asyncNumThreads +
                ", loggerFactory='" + loggerFactory + '\'' +
                ", contributingTo=" + contributingTo +
                ", includeMyRetweetEnabled=" + includeMyRetweetEnabled +
                ", includeEntitiesEnabled=" + includeEntitiesEnabled +
                ", trimUserEnabled=" + trimUserEnabled +
                ", jsonStoreEnabled=" + jsonStoreEnabled +
                ", mbeanEnabled=" + mbeanEnabled +
                ", userStreamRepliesAllEnabled=" + userStreamRepliesAllEnabled +
                ", userStreamWithFollowingsEnabled=" + userStreamWithFollowingsEnabled +
                ", stallWarningsEnabled=" + stallWarningsEnabled +
                ", applicationOnlyAuthEnabled=" + applicationOnlyAuthEnabled +
                ", mediaProvider='" + mediaProvider + '\'' +
                ", mediaProviderAPIKey='" + mediaProviderAPIKey + '\'' +
                ", mediaProviderParameters=" + mediaProviderParameters +
                ", daemonEnabled=" + daemonEnabled +
                '}';
    }

    private static final List<ConfigurationBase> instances = new ArrayList<ConfigurationBase>();

    private static void cacheInstance(ConfigurationBase conf) {
        if (!instances.contains(conf)) {
            instances.add(conf);
        }
    }

    protected void cacheInstance() {
        cacheInstance(this);
    }

    private static ConfigurationBase getInstance(ConfigurationBase configurationBase) {
        int index;
        if ((index = instances.indexOf(configurationBase)) == -1) {
            instances.add(configurationBase);
            return configurationBase;
        } else {
            return instances.get(index);
        }
    }

    // assures equality after deserializedation
    protected Object readResolve() throws ObjectStreamException {
        return getInstance(this);
    }
}
