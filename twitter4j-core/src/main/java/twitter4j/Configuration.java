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

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Configuration<T> implements AuthorizationConfiguration, HttpClientConfiguration, java.io.Serializable {
    private static final long serialVersionUID = 2235370978558949003L;
    String user = null;
    String password = null;

    // HttpConf fields
    String httpProxyHost = null;
    String httpProxyUser = null;
    String httpProxyPassword = null;
    boolean httpProxySocks = false;
    int httpProxyPort = -1;
    int httpConnectionTimeout = 20000;
    int httpReadTimeout = 120000;
    boolean prettyDebug = false;
    boolean gzipEnabled = true;


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
    String oAuthRequestTokenURL = "https://api.twitter.com/oauth/request_token";
    String oAuthAuthorizationURL = "https://api.twitter.com/oauth/authorize";
    String oAuthAccessTokenURL = "https://api.twitter.com/oauth/access_token";
    String oAuthAuthenticationURL = "https://api.twitter.com/oauth/authenticate";
    String oAuthInvalidateTokenURL = "https://api.twitter.com/1.1/oauth/invalidate_token";
    String oAuth2TokenURL = "https://api.twitter.com/oauth2/token";
    String oAuth2InvalidateTokenURL = "https://api.twitter.com/oauth2/invalidate_token";

    String restBaseURL = "https://api.twitter.com/1.1/";
    String streamBaseURL = "https://stream.twitter.com/1.1/";
    String uploadBaseURL = "https://upload.twitter.com/1.1/";

    long contributingTo = -1L;

    boolean includeMyRetweetEnabled = true;
    boolean includeEntitiesEnabled = true;
    boolean trimUserEnabled = false;
    boolean includeExtAltTextEnabled = true;
    boolean tweetModeExtended = true;
    boolean includeEmailEnabled = false;

    boolean jsonStoreEnabled = false;

    boolean mbeanEnabled = false;

    boolean stallWarningsEnabled = true;

    boolean applicationOnlyAuthEnabled = false;

    String streamThreadName = "";

    Configuration() {
        PropertyConfiguration.loadDefaultProperties(this);
    }

    Function<Configuration<T>, T> factory ;
    Configuration(Function<Configuration<T>, T> factory){
        this();
        this.factory = factory;
    }

    public static Configuration<Twitter> getInstance() {
        return new Configuration<>();
    }

    public Authorization getAuthorization(){
        Authorization auth = null;
        String consumerKey = this.oAuthConsumerKey;
        String consumerSecret = this.oAuthConsumerSecret;

        if (consumerKey != null && consumerSecret != null) {
            if (this.applicationOnlyAuthEnabled){
                OAuth2Authorization oauth2 = new OAuth2Authorization(this);
                String tokenType = this.oAuth2TokenType;
                String accessToken = this.oAuth2AccessToken;
                if (tokenType != null && accessToken != null) {
                    oauth2.setOAuth2Token(new OAuth2Token(tokenType, accessToken));
                }
                auth = oauth2;

            } else {
                OAuthAuthorization oauth;
                oauth = new OAuthAuthorization(this);
                String accessToken = this.oAuthAccessToken;
                String accessTokenSecret = this.oAuthAccessTokenSecret;
                if (accessToken != null && accessTokenSecret != null) {
                    oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                auth = oauth;
            }
        }
        if (null == auth) {
            auth = NullAuthorization.getInstance();
        }
        return auth;

    }
    // oauth related setter/getters
    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public HttpClientConfiguration getHttpClientConfiguration() {
        return this;
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

    // methods for HttpClientConfiguration


    public int getHttpStreamingReadTimeout() {
        return httpStreamingReadTimeout;
    }

    // oauth related setter/getters

    @Override
    public String getOAuthConsumerKey() {
        return oAuthConsumerKey;
    }

    @Override
    public String getOAuthConsumerSecret() {
        return oAuthConsumerSecret;
    }

    @Override
    public String getOAuthAccessToken() {
        return oAuthAccessToken;
    }

    @Override
    public String getOAuthAccessTokenSecret() {
        return oAuthAccessTokenSecret;
    }

    @Override
    public String getOAuth2TokenType() {
        return oAuth2TokenType;
    }

    @Override
    public String getOAuth2AccessToken() {
        return oAuth2AccessToken;
    }

    public String getOAuth2Scope() {
        return oAuth2Scope;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration<?> that = (Configuration<?>) o;
        return httpProxySocks == that.httpProxySocks && httpProxyPort == that.httpProxyPort && httpConnectionTimeout == that.httpConnectionTimeout && httpReadTimeout == that.httpReadTimeout && prettyDebug == that.prettyDebug && gzipEnabled == that.gzipEnabled && httpStreamingReadTimeout == that.httpStreamingReadTimeout && httpRetryCount == that.httpRetryCount && httpRetryIntervalSeconds == that.httpRetryIntervalSeconds && contributingTo == that.contributingTo && includeMyRetweetEnabled == that.includeMyRetweetEnabled && includeEntitiesEnabled == that.includeEntitiesEnabled && trimUserEnabled == that.trimUserEnabled && includeExtAltTextEnabled == that.includeExtAltTextEnabled && tweetModeExtended == that.tweetModeExtended && includeEmailEnabled == that.includeEmailEnabled && jsonStoreEnabled == that.jsonStoreEnabled && mbeanEnabled == that.mbeanEnabled && stallWarningsEnabled == that.stallWarningsEnabled && applicationOnlyAuthEnabled == that.applicationOnlyAuthEnabled && built == that.built && Objects.equals(user, that.user) && Objects.equals(password, that.password) && Objects.equals(httpProxyHost, that.httpProxyHost) && Objects.equals(httpProxyUser, that.httpProxyUser) && Objects.equals(httpProxyPassword, that.httpProxyPassword) && Objects.equals(oAuthConsumerKey, that.oAuthConsumerKey) && Objects.equals(oAuthConsumerSecret, that.oAuthConsumerSecret) && Objects.equals(oAuthAccessToken, that.oAuthAccessToken) && Objects.equals(oAuthAccessTokenSecret, that.oAuthAccessTokenSecret) && Objects.equals(oAuth2TokenType, that.oAuth2TokenType) && Objects.equals(oAuth2AccessToken, that.oAuth2AccessToken) && Objects.equals(oAuth2Scope, that.oAuth2Scope) && Objects.equals(oAuthRequestTokenURL, that.oAuthRequestTokenURL) && Objects.equals(oAuthAuthorizationURL, that.oAuthAuthorizationURL) && Objects.equals(oAuthAccessTokenURL, that.oAuthAccessTokenURL) && Objects.equals(oAuthAuthenticationURL, that.oAuthAuthenticationURL) && Objects.equals(oAuthInvalidateTokenURL, that.oAuthInvalidateTokenURL) && Objects.equals(oAuth2TokenURL, that.oAuth2TokenURL) && Objects.equals(oAuth2InvalidateTokenURL, that.oAuth2InvalidateTokenURL) && Objects.equals(restBaseURL, that.restBaseURL) && Objects.equals(streamBaseURL, that.streamBaseURL) && Objects.equals(uploadBaseURL, that.uploadBaseURL) && Objects.equals(streamThreadName, that.streamThreadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, password, httpProxyHost, httpProxyUser, httpProxyPassword, httpProxySocks, httpProxyPort, httpConnectionTimeout, httpReadTimeout, prettyDebug, gzipEnabled, httpStreamingReadTimeout, httpRetryCount, httpRetryIntervalSeconds, oAuthConsumerKey, oAuthConsumerSecret, oAuthAccessToken, oAuthAccessTokenSecret, oAuth2TokenType, oAuth2AccessToken, oAuth2Scope, oAuthRequestTokenURL, oAuthAuthorizationURL, oAuthAccessTokenURL, oAuthAuthenticationURL, oAuthInvalidateTokenURL, oAuth2TokenURL, oAuth2InvalidateTokenURL, restBaseURL, streamBaseURL, uploadBaseURL, contributingTo, includeMyRetweetEnabled, includeEntitiesEnabled, trimUserEnabled, includeExtAltTextEnabled, tweetModeExtended, includeEmailEnabled, jsonStoreEnabled, mbeanEnabled, stallWarningsEnabled, applicationOnlyAuthEnabled, streamThreadName);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", httpProxyHost='" + httpProxyHost + '\'' +
                ", httpProxyUser='" + httpProxyUser + '\'' +
                ", httpProxyPassword='" + httpProxyPassword + '\'' +
                ", httpProxySocks=" + httpProxySocks +
                ", httpProxyPort=" + httpProxyPort +
                ", httpConnectionTimeout=" + httpConnectionTimeout +
                ", httpReadTimeout=" + httpReadTimeout +
                ", prettyDebug=" + prettyDebug +
                ", gzipEnabled=" + gzipEnabled +
                ", httpStreamingReadTimeout=" + httpStreamingReadTimeout +
                ", httpRetryCount=" + httpRetryCount +
                ", httpRetryIntervalSeconds=" + httpRetryIntervalSeconds +
                ", oAuthConsumerKey='" + oAuthConsumerKey + '\'' +
                ", oAuthConsumerSecret='" + mask(oAuthConsumerSecret) + '\'' +
                ", oAuth2AccessToken='" + mask(oAuth2AccessToken) + '\'' +
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

    public Configuration<T> prettyDebugEnabled(boolean prettyDebugEnabled) {
        checkNotBuilt();
        this.prettyDebug = prettyDebugEnabled;
        return this;
    }

    public Configuration<T> gzipEnabled(boolean gzipEnabled) {
        checkNotBuilt();
        this.gzipEnabled = gzipEnabled;
        return this;
    }

    public Configuration<T> applicationOnlyAuthEnabled(boolean applicationOnlyAuthEnabled) {
        checkNotBuilt();
        this.applicationOnlyAuthEnabled = applicationOnlyAuthEnabled;
        return this;
    }
    public Configuration<T> load(Properties props){
        checkNotBuilt();
        PropertyConfiguration.load(this, props);
        return this;
    }

    public Configuration<T> user(String user) {
        checkNotBuilt();
        this.user = user;
        return this;
    }

    public Configuration<T> password(String password) {
        checkNotBuilt();
        this.password = password;
        return this;
    }

    public Configuration<T> httpProxyHost(String httpProxyHost) {
        checkNotBuilt();
        this.httpProxyHost = httpProxyHost;
        return this;
    }

    public Configuration<T> httpProxyUser(String httpProxyUser) {
        checkNotBuilt();
        this.httpProxyUser = httpProxyUser;
        return this;
    }

    public Configuration<T> httpProxyPassword(String httpProxyPassword) {
        checkNotBuilt();
        this.httpProxyPassword = httpProxyPassword;
        return this;
    }

    public Configuration<T> httpProxyPort(int httpProxyPort) {
        checkNotBuilt();
        this.httpProxyPort = httpProxyPort;
        return this;
    }

    public Configuration<T> httpProxySocks(boolean httpProxySocks) {
        checkNotBuilt();
        this.httpProxySocks = httpProxySocks;
        return this;
    }

    public Configuration<T> httpConnectionTimeout(int httpConnectionTimeout) {
        checkNotBuilt();
        this.httpConnectionTimeout = httpConnectionTimeout;
        return this;
    }

    public Configuration<T> httpReadTimeout(int httpReadTimeout) {
        checkNotBuilt();
        this.httpReadTimeout = httpReadTimeout;
        return this;
    }

    public Configuration<T> httpStreamingReadTimeout(int httpStreamingReadTimeout) {
        checkNotBuilt();
        this.httpStreamingReadTimeout = httpStreamingReadTimeout;
        return this;
    }

    public Configuration<T> httpRetryCount(int httpRetryCount) {
        checkNotBuilt();
        this.httpRetryCount = httpRetryCount;
        return this;
    }

    public Configuration<T> httpRetryIntervalSeconds(int httpRetryIntervalSeconds) {
        checkNotBuilt();
        this.httpRetryIntervalSeconds = httpRetryIntervalSeconds;
        return this;
    }

    public Configuration<T> oAuthConsumerKey(String oAuthConsumerKey) {
        checkNotBuilt();
        this.oAuthConsumerKey = oAuthConsumerKey;
        return this;
    }

    public Configuration<T> oAuthConsumerSecret(String oAuthConsumerSecret) {
        checkNotBuilt();
        this.oAuthConsumerSecret = oAuthConsumerSecret;
        return this;
    }

    public Configuration<T> oAuthAccessToken(String oAuthAccessToken) {
        checkNotBuilt();
        this.oAuthAccessToken = oAuthAccessToken;
        return this;
    }

    public Configuration<T> oAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        checkNotBuilt();
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
        return this;
    }

    public Configuration<T> oAuth2TokenType(String oAuth2TokenType) {
        checkNotBuilt();
        this.oAuth2TokenType = oAuth2TokenType;
        return this;
    }

    public Configuration<T> oAuth2AccessToken(String oAuth2AccessToken) {
        checkNotBuilt();
        this.oAuth2AccessToken = oAuth2AccessToken;
        return this;
    }

    public Configuration<T> oAuth2Scope(String oAuth2Scope) {
        checkNotBuilt();
        this.oAuth2Scope = oAuth2Scope;
        return this;
    }

    public Configuration<T> oAuthRequestTokenURL(String oAuthRequestTokenURL) {
        checkNotBuilt();
        this.oAuthRequestTokenURL = oAuthRequestTokenURL;
        return this;
    }

    public Configuration<T> oAuthAuthorizationURL(String oAuthAuthorizationURL) {
        checkNotBuilt();
        this.oAuthAuthorizationURL =oAuthAuthorizationURL;
        return this;
    }

    public Configuration<T> oAuthAccessTokenURL(String oAuthAccessTokenURL) {
        checkNotBuilt();
        this.oAuthAccessTokenURL =oAuthAccessTokenURL;
        return this;
    }

    public Configuration<T> oAuthAuthenticationURL(String oAuthAuthenticationURL) {
        checkNotBuilt();
        this.oAuthAuthenticationURL =oAuthAuthenticationURL;
        return this;
    }

    public Configuration<T> oAuth2TokenURL(String oAuth2TokenURL) {
        checkNotBuilt();
        this.oAuth2TokenURL =oAuth2TokenURL;
        return this;
    }

    public Configuration<T> oAuth2InvalidateTokenURL(String invalidateTokenURL) {
        checkNotBuilt();
        this.oAuth2InvalidateTokenURL =invalidateTokenURL;
        return this;
    }

    public Configuration<T> restBaseURL(String restBaseURL) {
        checkNotBuilt();
        this.restBaseURL =restBaseURL;
        return this;
    }

    public Configuration<T> uploadBaseURL(String uploadBaseURL) {
        checkNotBuilt();
        this.uploadBaseURL =uploadBaseURL;
        return this;
    }

    public Configuration<T> streamBaseURL(String streamBaseURL) {
        checkNotBuilt();
        this.streamBaseURL =streamBaseURL;
        return this;
    }

    public Configuration<T> contributingTo(long contributingTo) {
        checkNotBuilt();
        this.contributingTo =contributingTo;
        return this;
    }

    public Configuration<T> trimUserEnabled(boolean enabled) {
        checkNotBuilt();
        this.trimUserEnabled =enabled;
        return this;
    }

    public Configuration<T> includeExtAltTextEnabled(boolean enabled) {
        checkNotBuilt();
        this.includeExtAltTextEnabled =enabled;
        return this;
    }

    public Configuration<T> tweetModeExtended(boolean enabled) {
        checkNotBuilt();
        this.tweetModeExtended =enabled;
        return this;
    }

    public Configuration<T> includeMyRetweetEnabled(boolean enabled) {
        checkNotBuilt();
        this.includeMyRetweetEnabled =enabled;
        return this;
    }

    public Configuration<T> includeEntitiesEnabled(boolean enabled) {
        checkNotBuilt();
        this.includeEntitiesEnabled =enabled;
        return this;
    }

    public Configuration<T> includeEmailEnabled(boolean enabled) {
        checkNotBuilt();
        this.includeEmailEnabled =enabled;
        return this;
    }

    public Configuration<T> jsonStoreEnabled(boolean enabled) {
        checkNotBuilt();
        this.jsonStoreEnabled =enabled;
        return this;
    }

    public Configuration<T> mBeanEnabled(boolean enabled) {
        checkNotBuilt();
        this.mbeanEnabled =enabled;
        return this;
    }

    Configuration<T> buildConfiguration() {
        checkNotBuilt();
        try {
            return this;
        } finally {
            built = true;
        }
    }

    /**
     * Constructs Twitter instance
     * @return Twitter instance
     */
    public T build(){
        return factory.apply(this);
    }

    boolean built = false;
    private void checkNotBuilt() {
        if (built) {
            throw new IllegalStateException("Cannot use this builder any longer, build() has already been called");
        }
    }
}
