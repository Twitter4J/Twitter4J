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

import org.jetbrains.annotations.Nullable;
import twitter4j.AuthorizationConfiguration;
import twitter4j.HttpClientConfiguration;

import java.io.Serializable;
import java.net.Proxy;
import java.util.Objects;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Configuration implements AuthorizationConfiguration, java.io.Serializable {
    private static final long serialVersionUID = 2235370978558949003L;
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
    private String oAuthInvalidateTokenURL = "https://api.twitter.com/1.1/oauth/invalidate_token";
    private String oAuth2TokenURL = "https://api.twitter.com/oauth2/token";
    private String oAuth2InvalidateTokenURL = "https://api.twitter.com/oauth2/invalidate_token";

    private String restBaseURL = "https://api.twitter.com/1.1/";
    private String streamBaseURL = "https://stream.twitter.com/1.1/";
    private String uploadBaseURL = "https://upload.twitter.com/1.1/";

    private long contributingTo = -1L;

    private boolean includeMyRetweetEnabled = true;
    private boolean includeEntitiesEnabled = true;
    private boolean trimUserEnabled = false;
    private boolean includeExtAltTextEnabled = true;
    private boolean tweetModeExtended = true;
    private boolean includeEmailEnabled = false;

    private boolean jsonStoreEnabled = false;

    private boolean mbeanEnabled = false;

    private boolean stallWarningsEnabled = true;

    private boolean applicationOnlyAuthEnabled = false;

    private String streamThreadName = "";

    Configuration() {
        httpConf = new Configuration.MyHttpClientConfiguration(null // proxy host
                , null // proxy user
                , null // proxy password
                , -1 // proxy port
                , false // proxy socks
                , 20000 // connection timeout
                , 120000 // read timeout
                , false // pretty debug
                , true // gzip enabled
        );
        PropertyConfiguration.loadDefaultProperties(this);
    }

    class MyHttpClientConfiguration implements HttpClientConfiguration, Serializable {
        private static final long serialVersionUID = 8226866124868861058L;
        private final String httpProxyHost;
        private final String httpProxyUser;
        private final String httpProxyPassword;
        private final boolean httpProxySocks;
        private final int httpProxyPort;
        private final int httpConnectionTimeout;
        private final int httpReadTimeout;
        private final boolean prettyDebug;
        private final boolean gzipEnabled;

        MyHttpClientConfiguration(String httpProxyHost, String httpProxyUser, String httpProxyPassword, int httpProxyPort, boolean httpProxySocks, int httpConnectionTimeout, int httpReadTimeout, boolean prettyDebug, boolean gzipEnabled) {
            this.httpProxyHost = httpProxyHost;
            this.httpProxyUser = httpProxyUser;
            this.httpProxyPassword = httpProxyPassword;
            this.httpProxyPort = httpProxyPort;
            this.httpProxySocks = httpProxySocks;
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
        public boolean isHttpProxySocks() {
            return httpProxySocks;
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
            if (httpProxySocks != that.httpProxySocks) return false;
            if (httpReadTimeout != that.httpReadTimeout) return false;
            if (prettyDebug != that.prettyDebug) return false;
            if (!Objects.equals(httpProxyHost, that.httpProxyHost))
                return false;
            if (!Objects.equals(httpProxyPassword, that.httpProxyPassword))
                return false;
            return Objects.equals(httpProxyUser, that.httpProxyUser);
        }

        @Override
        public int hashCode() {
            int result = httpProxyHost != null ? httpProxyHost.hashCode() : 0;
            result = 31 * result + (httpProxyUser != null ? httpProxyUser.hashCode() : 0);
            result = 31 * result + (httpProxyPassword != null ? httpProxyPassword.hashCode() : 0);
            result = 31 * result + httpProxyPort;
            result = 31 * result + (httpProxySocks ? 1 : 0);
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
                    ", proxyType=" + (httpProxySocks ? Proxy.Type.SOCKS : Proxy.Type.HTTP) +
                    ", httpConnectionTimeout=" + httpConnectionTimeout +
                    ", httpReadTimeout=" + httpReadTimeout +
                    ", prettyDebug=" + prettyDebug +
                    ", gzipEnabled=" + gzipEnabled +
                    '}';
        }
    }

    public static Configuration getInstance() {
        return new Configuration();
    }

    // oauth related setter/getters
    @Override
    public String getUser() {
        return user;
    }

    void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpConf;
    }

    void setPassword(String password) {
        this.password = password;
    }

    void setPrettyDebugEnabled(boolean prettyDebug) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.isHttpProxySocks()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , prettyDebug, httpConf.isGZIPEnabled()
        );
    }

    void setGZIPEnabled(boolean gzipEnabled) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.isHttpProxySocks()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled(), gzipEnabled
        );
    }

    // methods for HttpClientConfiguration

    void setHttpProxyHost(String proxyHost) {
        httpConf = new MyHttpClientConfiguration(proxyHost
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.isHttpProxySocks()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled(), httpConf.isGZIPEnabled()
        );
    }

    void setHttpProxyUser(String proxyUser) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , proxyUser
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.isHttpProxySocks()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled(), httpConf.isGZIPEnabled()
        );
    }

    void setHttpProxyPassword(String proxyPassword) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , proxyPassword
                , httpConf.getHttpProxyPort()
                , httpConf.isHttpProxySocks()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled(), httpConf.isGZIPEnabled()
        );
    }

    void setHttpProxyPort(int proxyPort) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , proxyPort
                , httpConf.isHttpProxySocks()
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled(), httpConf.isGZIPEnabled()
        );
    }

    void setHttpProxySocks(boolean isSocksProxy) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , isSocksProxy
                , httpConf.getHttpConnectionTimeout()
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled(), httpConf.isGZIPEnabled()
        );
    }

    void setHttpConnectionTimeout(int connectionTimeout) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.isHttpProxySocks()
                , connectionTimeout
                , httpConf.getHttpReadTimeout()
                , httpConf.isPrettyDebugEnabled(), httpConf.isGZIPEnabled()
        );
    }

    void setHttpReadTimeout(int readTimeout) {
        httpConf = new MyHttpClientConfiguration(httpConf.getHttpProxyHost()
                , httpConf.getHttpProxyUser()
                , httpConf.getHttpProxyPassword()
                , httpConf.getHttpProxyPort()
                , httpConf.isHttpProxySocks()
                , httpConf.getHttpConnectionTimeout()
                , readTimeout
                , httpConf.isPrettyDebugEnabled(), httpConf.isGZIPEnabled()
        );
    }

    public int getHttpStreamingReadTimeout() {
        return httpStreamingReadTimeout;
    }

    void setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
        this.httpStreamingReadTimeout = httpStreamingReadTimeout;
    }

    void setHttpRetryCount(int retryCount) {
        this.httpRetryCount = retryCount;
    }

    void setHttpRetryIntervalSeconds(int retryIntervalSeconds) {
        this.httpRetryIntervalSeconds = retryIntervalSeconds;
    }

    // oauth related setter/getters

    @Override
    public String getOAuthConsumerKey() {
        return oAuthConsumerKey;
    }

    void setOAuthConsumerKey(String oAuthConsumerKey) {
        this.oAuthConsumerKey = oAuthConsumerKey;
    }

    @Override
    public String getOAuthConsumerSecret() {
        return oAuthConsumerSecret;
    }

    void setOAuthConsumerSecret(String oAuthConsumerSecret) {
        this.oAuthConsumerSecret = oAuthConsumerSecret;
    }

    @Override
    public String getOAuthAccessToken() {
        return oAuthAccessToken;
    }

    void setOAuthAccessToken(String oAuthAccessToken) {
        this.oAuthAccessToken = oAuthAccessToken;
    }

    @Override
    public String getOAuthAccessTokenSecret() {
        return oAuthAccessTokenSecret;
    }

    void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
    }

    @Override
    public String getOAuth2TokenType() {
        return oAuth2TokenType;
    }

    void setOAuth2TokenType(String oAuth2TokenType) {
        this.oAuth2TokenType = oAuth2TokenType;
    }

    @Override
    public String getOAuth2AccessToken() {
        return oAuth2AccessToken;
    }

    public String getOAuth2Scope() {
        return oAuth2Scope;
    }

    void setOAuth2AccessToken(String oAuth2AccessToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
    }

    void setOAuth2Scope(String oAuth2Scope) {
        this.oAuth2Scope = oAuth2Scope;
    }


    public long getContributingTo() {
        return contributingTo;
    }

    void setContributingTo(long contributingTo) {
        this.contributingTo = contributingTo;
    }

    public String getRestBaseURL() {
        return restBaseURL;
    }

    void setRestBaseURL(String restBaseURL) {
        this.restBaseURL = restBaseURL;
    }

    public String getUploadBaseURL() {
        return uploadBaseURL;
    }

    void setUploadBaseURL(String uploadBaseURL) {
        this.uploadBaseURL = uploadBaseURL;
    }

    public String getStreamBaseURL() {
        return streamBaseURL;
    }

    void setStreamBaseURL(String streamBaseURL) {
        this.streamBaseURL = streamBaseURL;
    }

    public String getOAuthRequestTokenURL() {
        return oAuthRequestTokenURL;
    }

    void setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
        this.oAuthRequestTokenURL = oAuthRequestTokenURL;
    }

    public String getOAuthAuthorizationURL() {
        return oAuthAuthorizationURL;
    }

    void setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
        this.oAuthAuthorizationURL = oAuthAuthorizationURL;
    }

    public String getOAuthAccessTokenURL() {
        return oAuthAccessTokenURL;
    }

    void setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
        this.oAuthAccessTokenURL = oAuthAccessTokenURL;
    }

    public String getOAuthAuthenticationURL() {
        return oAuthAuthenticationURL;
    }

    void setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
        this.oAuthAuthenticationURL = oAuthAuthenticationURL;
    }

    public String getOAuth2TokenURL() {
        return oAuth2TokenURL;
    }

    void setOAuth2TokenURL(String oAuth2TokenURL) {
        this.oAuth2TokenURL = oAuth2TokenURL;
    }

    public String getOAuthInvalidateTokenURL() {
        return oAuthInvalidateTokenURL;
    }

    @SuppressWarnings("unused")
    void setOAuthInvalidateTokenURL(String oAuthInvalidateTokenURL) {
        this.oAuthInvalidateTokenURL = oAuthInvalidateTokenURL;
    }

    public String getOAuth2InvalidateTokenURL() {
        return oAuth2InvalidateTokenURL;
    }

    void setOAuth2InvalidateTokenURL(String oAuth2InvalidateTokenURL) {
        this.oAuth2InvalidateTokenURL = oAuth2InvalidateTokenURL;
    }

    public boolean isIncludeEntitiesEnabled() {
        return includeEntitiesEnabled;
    }

    void setIncludeEntitiesEnabled(boolean includeEntitiesEnabled) {
        this.includeEntitiesEnabled = includeEntitiesEnabled;
    }

    public boolean isIncludeMyRetweetEnabled() {
        return this.includeMyRetweetEnabled;
    }

    public void setIncludeMyRetweetEnabled(boolean enabled) {
        this.includeMyRetweetEnabled = enabled;
    }

    public boolean isTrimUserEnabled() {
        return this.trimUserEnabled;
    }

    public boolean isIncludeExtAltTextEnabled() {
        return this.includeExtAltTextEnabled;
    }

    public boolean isTweetModeExtended() {
        return this.tweetModeExtended;
    }

    public boolean isIncludeEmailEnabled() {
        return includeEmailEnabled;
    }

    void setIncludeEmailEnabled(boolean includeEmailEnabled) {
        this.includeEmailEnabled = includeEmailEnabled;
    }

    public void setTrimUserEnabled(boolean enabled) {
        this.trimUserEnabled = enabled;
    }

    public void setIncludeExtAltTextEnabled(boolean enabled) {
        this.includeExtAltTextEnabled = enabled;
    }

    public void setTweetModeExtended(boolean enabled) {
        this.tweetModeExtended = enabled;
    }

    public boolean isJSONStoreEnabled() {
        return this.jsonStoreEnabled;
    }

    void setJSONStoreEnabled(boolean enabled) {
        this.jsonStoreEnabled = enabled;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isMBeanEnabled() {
        return this.mbeanEnabled;
    }

    void setMBeanEnabled(boolean enabled) {
        this.mbeanEnabled = enabled;
    }

    public boolean isStallWarningsEnabled() {
        return stallWarningsEnabled;
    }

    void setStallWarningsEnabled(boolean stallWarningsEnabled) {
        this.stallWarningsEnabled = stallWarningsEnabled;
    }

    public boolean isApplicationOnlyAuthEnabled() {
        return applicationOnlyAuthEnabled;
    }

    void setApplicationOnlyAuthEnabled(boolean applicationOnlyAuthEnabled) {
        this.applicationOnlyAuthEnabled = applicationOnlyAuthEnabled;
    }

    public String getStreamThreadName() {
        return this.streamThreadName;
    }

    void setStreamThreadName(String streamThreadName) {
        this.streamThreadName = streamThreadName;
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
        Configuration that = (Configuration) o;
        return httpStreamingReadTimeout == that.httpStreamingReadTimeout && httpRetryCount == that.httpRetryCount && httpRetryIntervalSeconds == that.httpRetryIntervalSeconds && contributingTo == that.contributingTo && includeMyRetweetEnabled == that.includeMyRetweetEnabled && includeEntitiesEnabled == that.includeEntitiesEnabled && trimUserEnabled == that.trimUserEnabled && includeExtAltTextEnabled == that.includeExtAltTextEnabled && tweetModeExtended == that.tweetModeExtended && includeEmailEnabled == that.includeEmailEnabled && jsonStoreEnabled == that.jsonStoreEnabled && mbeanEnabled == that.mbeanEnabled && stallWarningsEnabled == that.stallWarningsEnabled && applicationOnlyAuthEnabled == that.applicationOnlyAuthEnabled && Objects.equals(user, that.user) && Objects.equals(password, that.password) && Objects.equals(httpConf, that.httpConf) && Objects.equals(oAuthConsumerKey, that.oAuthConsumerKey) && Objects.equals(oAuthConsumerSecret, that.oAuthConsumerSecret) && Objects.equals(oAuthAccessToken, that.oAuthAccessToken) && Objects.equals(oAuthAccessTokenSecret, that.oAuthAccessTokenSecret) && Objects.equals(oAuth2TokenType, that.oAuth2TokenType) && Objects.equals(oAuth2AccessToken, that.oAuth2AccessToken) && Objects.equals(oAuth2Scope, that.oAuth2Scope) && Objects.equals(oAuthRequestTokenURL, that.oAuthRequestTokenURL) && Objects.equals(oAuthAuthorizationURL, that.oAuthAuthorizationURL) && Objects.equals(oAuthAccessTokenURL, that.oAuthAccessTokenURL) && Objects.equals(oAuthAuthenticationURL, that.oAuthAuthenticationURL) && Objects.equals(oAuthInvalidateTokenURL, that.oAuthInvalidateTokenURL) && Objects.equals(oAuth2TokenURL, that.oAuth2TokenURL) && Objects.equals(oAuth2InvalidateTokenURL, that.oAuth2InvalidateTokenURL) && Objects.equals(restBaseURL, that.restBaseURL) && Objects.equals(streamBaseURL, that.streamBaseURL) && Objects.equals(uploadBaseURL, that.uploadBaseURL) && Objects.equals(streamThreadName, that.streamThreadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, password, httpConf, httpStreamingReadTimeout, httpRetryCount, httpRetryIntervalSeconds, oAuthConsumerKey, oAuthConsumerSecret, oAuthAccessToken, oAuthAccessTokenSecret, oAuth2TokenType, oAuth2AccessToken, oAuth2Scope, oAuthRequestTokenURL, oAuthAuthorizationURL, oAuthAccessTokenURL, oAuthAuthenticationURL, oAuthInvalidateTokenURL, oAuth2TokenURL, oAuth2InvalidateTokenURL, restBaseURL, streamBaseURL, uploadBaseURL, contributingTo, includeMyRetweetEnabled, includeEntitiesEnabled, trimUserEnabled, includeExtAltTextEnabled, tweetModeExtended, includeEmailEnabled, jsonStoreEnabled, mbeanEnabled, stallWarningsEnabled, applicationOnlyAuthEnabled, streamThreadName);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", httpConf=" + httpConf +
                ", httpStreamingReadTimeout=" + httpStreamingReadTimeout +
                ", httpRetryCount=" + httpRetryCount +
                ", httpRetryIntervalSeconds=" + httpRetryIntervalSeconds +
                ", oAuthConsumerKey='" + oAuthConsumerKey + '\'' +
                ", oAuthConsumerSecret='" + mask(oAuthConsumerSecret) + '\'' +
                ", oAuthAccessToken='" + oAuthAccessToken + '\'' +
                ", oAuthAccessTokenSecret='" + mask(oAuthAccessTokenSecret) + '\'' +
                ", oAuth2TokenType='" + oAuth2TokenType + '\'' +
                ", oAuth2AccessToken='" + mask(oAuth2AccessToken) + '\'' +
                ", oAuth2Scope='" + oAuth2Scope + '\'' +
                ", oAuthRequestTokenURL='" + oAuthRequestTokenURL + '\'' +
                ", oAuthAuthorizationURL='" + oAuthAuthorizationURL + '\'' +
                ", oAuthAccessTokenURL='" + oAuthAccessTokenURL + '\'' +
                ", oAuthAuthenticationURL='" + oAuthAuthenticationURL + '\'' +
                ", oAuthInvalidateTokenURL='" + oAuthInvalidateTokenURL + '\'' +
                ", oAuth2TokenURL='" + oAuth2TokenURL + '\'' +
                ", oAuth2InvalidateTokenURL='" + oAuth2InvalidateTokenURL + '\'' +
                ", restBaseURL='" + restBaseURL + '\'' +
                ", streamBaseURL='" + streamBaseURL + '\'' +
                ", uploadBaseURL='" + uploadBaseURL + '\'' +
                ", contributingTo=" + contributingTo +
                ", includeMyRetweetEnabled=" + includeMyRetweetEnabled +
                ", includeEntitiesEnabled=" + includeEntitiesEnabled +
                ", trimUserEnabled=" + trimUserEnabled +
                ", includeExtAltTextEnabled=" + includeExtAltTextEnabled +
                ", tweetModeExtended=" + tweetModeExtended +
                ", includeEmailEnabled=" + includeEmailEnabled +
                ", jsonStoreEnabled=" + jsonStoreEnabled +
                ", mbeanEnabled=" + mbeanEnabled +
                ", stallWarningsEnabled=" + stallWarningsEnabled +
                ", applicationOnlyAuthEnabled=" + applicationOnlyAuthEnabled +
                ", streamThreadName='" + streamThreadName + '\'' +
                '}';
    }

    String mask(@Nullable String strToMask) {
        if (strToMask == null) {
            return "(null)";
        }
        //noinspection SuspiciousRegexArgument
        return strToMask.replaceAll(".", "*");
    }
}
