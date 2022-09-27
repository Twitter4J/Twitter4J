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

/**
 * A builder that can be used to construct a Twitter4J configuration with desired settings.  This
 * builder has sensible defaults such that {@code new ConfigurationBuilder().build()} would create a
 * usable configuration.  This configuration builder is useful for clients that wish to configure
 * Twitter4J in unit tests or from command line flags for example.
 *
 * @author John Sirois - john.sirois at gmail.com
 */
@SuppressWarnings("unused")
public final class ConfigurationBuilder {

    private ConfigurationBase configurationBean = new PropertyConfiguration();

    public ConfigurationBuilder prettyDebugEnabled(boolean prettyDebugEnabled) {
        checkNotBuilt();
        configurationBean.setPrettyDebugEnabled(prettyDebugEnabled);
        return this;
    }

    public ConfigurationBuilder gzipEnabled(boolean gzipEnabled) {
        checkNotBuilt();
        configurationBean.setGZIPEnabled(gzipEnabled);
        return this;
    }

    public ConfigurationBuilder applicationOnlyAuthEnabled(boolean applicationOnlyAuthEnabled) {
        checkNotBuilt();
        configurationBean.setApplicationOnlyAuthEnabled(applicationOnlyAuthEnabled);
        return this;
    }

    public ConfigurationBuilder user(String user) {
        checkNotBuilt();
        configurationBean.setUser(user);
        return this;
    }

    public ConfigurationBuilder password(String password) {
        checkNotBuilt();
        configurationBean.setPassword(password);
        return this;
    }

    public ConfigurationBuilder httpProxyHost(String httpProxyHost) {
        checkNotBuilt();
        configurationBean.setHttpProxyHost(httpProxyHost);
        return this;
    }

    public ConfigurationBuilder httpProxyUser(String httpProxyUser) {
        checkNotBuilt();
        configurationBean.setHttpProxyUser(httpProxyUser);
        return this;
    }

    public ConfigurationBuilder httpProxyPassword(String httpProxyPassword) {
        checkNotBuilt();
        configurationBean.setHttpProxyPassword(httpProxyPassword);
        return this;
    }

    public ConfigurationBuilder httpProxyPort(int httpProxyPort) {
        checkNotBuilt();
        configurationBean.setHttpProxyPort(httpProxyPort);
        return this;
    }

    public ConfigurationBuilder httpProxySocks(boolean httpProxySocks) {
        checkNotBuilt();
        configurationBean.setHttpProxySocks(httpProxySocks);
        return this;
    }
    
    public ConfigurationBuilder httpConnectionTimeout(int httpConnectionTimeout) {
        checkNotBuilt();
        configurationBean.setHttpConnectionTimeout(httpConnectionTimeout);
        return this;
    }

    public ConfigurationBuilder httpReadTimeout(int httpReadTimeout) {
        checkNotBuilt();
        configurationBean.setHttpReadTimeout(httpReadTimeout);
        return this;
    }

    public ConfigurationBuilder httpStreamingReadTimeout(int httpStreamingReadTimeout) {
        checkNotBuilt();
        configurationBean.setHttpStreamingReadTimeout(httpStreamingReadTimeout);
        return this;
    }

    public ConfigurationBuilder httpRetryCount(int httpRetryCount) {
        checkNotBuilt();
        configurationBean.setHttpRetryCount(httpRetryCount);
        return this;
    }

    public ConfigurationBuilder httpRetryIntervalSeconds(int httpRetryIntervalSeconds) {
        checkNotBuilt();
        configurationBean.setHttpRetryIntervalSeconds(httpRetryIntervalSeconds);
        return this;
    }

    public ConfigurationBuilder oAuthConsumerKey(String oAuthConsumerKey) {
        checkNotBuilt();
        configurationBean.setOAuthConsumerKey(oAuthConsumerKey);
        return this;
    }

    public ConfigurationBuilder oAuthConsumerSecret(String oAuthConsumerSecret) {
        checkNotBuilt();
        configurationBean.setOAuthConsumerSecret(oAuthConsumerSecret);
        return this;
    }

    public ConfigurationBuilder oAuthAccessToken(String oAuthAccessToken) {
        checkNotBuilt();
        configurationBean.setOAuthAccessToken(oAuthAccessToken);
        return this;
    }

    public ConfigurationBuilder oAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        checkNotBuilt();
        configurationBean.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        return this;
    }

    public ConfigurationBuilder oAuth2TokenType(String oAuth2TokenType) {
        checkNotBuilt();
        configurationBean.setOAuth2TokenType(oAuth2TokenType);
        return this;
    }

    public ConfigurationBuilder oAuth2AccessToken(String oAuth2AccessToken) {
        checkNotBuilt();
        configurationBean.setOAuth2AccessToken(oAuth2AccessToken);
        return this;
    }

    public ConfigurationBuilder oAuth2Scope(String oAuth2Scope) {
        checkNotBuilt();
        configurationBean.setOAuth2Scope(oAuth2Scope);
        return this;
    }

    public ConfigurationBuilder oAuthRequestTokenURL(String oAuthRequestTokenURL) {
        checkNotBuilt();
        configurationBean.setOAuthRequestTokenURL(oAuthRequestTokenURL);
        return this;
    }

    public ConfigurationBuilder oAuthAuthorizationURL(String oAuthAuthorizationURL) {
        checkNotBuilt();
        configurationBean.setOAuthAuthorizationURL(oAuthAuthorizationURL);
        return this;
    }

    public ConfigurationBuilder oAuthAccessTokenURL(String oAuthAccessTokenURL) {
        checkNotBuilt();
        configurationBean.setOAuthAccessTokenURL(oAuthAccessTokenURL);
        return this;
    }

    public ConfigurationBuilder oAuthAuthenticationURL(String oAuthAuthenticationURL) {
        checkNotBuilt();
        configurationBean.setOAuthAuthenticationURL(oAuthAuthenticationURL);
        return this;
    }

    public ConfigurationBuilder oAuth2TokenURL(String oAuth2TokenURL) {
        checkNotBuilt();
        configurationBean.setOAuth2TokenURL(oAuth2TokenURL);
        return this;
    }

    public ConfigurationBuilder oAuth2InvalidateTokenURL(String invalidateTokenURL) {
        checkNotBuilt();
        configurationBean.setOAuth2InvalidateTokenURL(invalidateTokenURL);
        return this;
    }

    public ConfigurationBuilder restBaseURL(String restBaseURL) {
        checkNotBuilt();
        configurationBean.setRestBaseURL(restBaseURL);
        return this;
    }

    public ConfigurationBuilder uploadBaseURL(String uploadBaseURL) {
        checkNotBuilt();
        configurationBean.setUploadBaseURL(uploadBaseURL);
        return this;
    }

    public ConfigurationBuilder streamBaseURL(String streamBaseURL) {
        checkNotBuilt();
        configurationBean.setStreamBaseURL(streamBaseURL);
        return this;
    }

    public ConfigurationBuilder contributingTo(long contributingTo) {
        checkNotBuilt();
        configurationBean.setContributingTo(contributingTo);
        return this;
    }

    public ConfigurationBuilder trimUserEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setTrimUserEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder includeExtAltTextEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setIncludeExtAltTextEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder tweetModeExtended(boolean enabled) {
        checkNotBuilt();
        configurationBean.setTweetModeExtended(enabled);
        return this;
    }

    public ConfigurationBuilder includeMyRetweetEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setIncludeMyRetweetEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder includeEntitiesEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setIncludeEntitiesEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder includeEmailEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setIncludeEmailEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder jsonStoreEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setJSONStoreEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder mBeanEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setMBeanEnabled(enabled);
        return this;
    }

    public Configuration build() {
        checkNotBuilt();
        configurationBean.cacheInstance();
        try {
            return configurationBean;
        } finally {
            configurationBean = null;
        }
    }

    private void checkNotBuilt() {
        if (configurationBean == null) {
            throw new IllegalStateException("Cannot use this builder any longer, build() has already been called");
        }
    }
}
