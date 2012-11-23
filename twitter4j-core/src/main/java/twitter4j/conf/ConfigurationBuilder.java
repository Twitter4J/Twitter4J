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

import java.util.Properties;

/**
 * A builder that can be used to construct a twitter4j configuration with desired settings.  This
 * builder has sensible defaults such that {@code new ConfigurationBuilder().build()} would create a
 * usable configuration.  This configuration builder is useful for clients that wish to configure
 * twitter4j in unit tests or from command line flags for example.
 *
 * @author John Sirois - john.sirois at gmail.com
 */
public final class ConfigurationBuilder {

    private ConfigurationBase configurationBean = new PropertyConfiguration();

    public ConfigurationBuilder setUseSSL(boolean useSSL) {
        checkNotBuilt();
        configurationBean.setUseSSL(useSSL);
        return this;
    }

    public ConfigurationBuilder setPrettyDebugEnabled(boolean prettyDebugEnabled) {
        checkNotBuilt();
        configurationBean.setPrettyDebugEnabled(prettyDebugEnabled);
        return this;
    }

    public ConfigurationBuilder setGZIPEnabled(boolean gzipEnabled) {
        checkNotBuilt();
        configurationBean.setGZIPEnabled(gzipEnabled);
        return this;
    }

    public ConfigurationBuilder setDebugEnabled(boolean debugEnabled) {
        checkNotBuilt();
        configurationBean.setDebug(debugEnabled);
        return this;
    }

    public ConfigurationBuilder setUser(String user) {
        checkNotBuilt();
        configurationBean.setUser(user);
        return this;
    }

    public ConfigurationBuilder setPassword(String password) {
        checkNotBuilt();
        configurationBean.setPassword(password);
        return this;
    }

    public ConfigurationBuilder setHttpProxyHost(String httpProxyHost) {
        checkNotBuilt();
        configurationBean.setHttpProxyHost(httpProxyHost);
        return this;
    }

    public ConfigurationBuilder setHttpProxyUser(String httpProxyUser) {
        checkNotBuilt();
        configurationBean.setHttpProxyUser(httpProxyUser);
        return this;
    }

    public ConfigurationBuilder setHttpProxyPassword(String httpProxyPassword) {
        checkNotBuilt();
        configurationBean.setHttpProxyPassword(httpProxyPassword);
        return this;
    }

    public ConfigurationBuilder setHttpProxyPort(int httpProxyPort) {
        checkNotBuilt();
        configurationBean.setHttpProxyPort(httpProxyPort);
        return this;
    }

    public ConfigurationBuilder setHttpConnectionTimeout(int httpConnectionTimeout) {
        checkNotBuilt();
        configurationBean.setHttpConnectionTimeout(httpConnectionTimeout);
        return this;
    }

    public ConfigurationBuilder setHttpReadTimeout(int httpReadTimeout) {
        checkNotBuilt();
        configurationBean.setHttpReadTimeout(httpReadTimeout);
        return this;
    }

    public ConfigurationBuilder setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
        checkNotBuilt();
        configurationBean.setHttpStreamingReadTimeout(httpStreamingReadTimeout);
        return this;
    }

    public ConfigurationBuilder setHttpRetryCount(int httpRetryCount) {
        checkNotBuilt();
        configurationBean.setHttpRetryCount(httpRetryCount);
        return this;
    }


    public ConfigurationBuilder setHttpMaxTotalConnections(int httpMaxConnections) {
        checkNotBuilt();
        configurationBean.setHttpMaxTotalConnections(httpMaxConnections);
        return this;
    }

    public ConfigurationBuilder setHttpDefaultMaxPerRoute(int httpDefaultMaxPerRoute) {
        checkNotBuilt();
        configurationBean.setHttpDefaultMaxPerRoute(httpDefaultMaxPerRoute);
        return this;
    }

    public ConfigurationBuilder setHttpRetryIntervalSeconds(int httpRetryIntervalSeconds) {
        checkNotBuilt();
        configurationBean.setHttpRetryIntervalSeconds(httpRetryIntervalSeconds);
        return this;
    }

    public ConfigurationBuilder setOAuthConsumerKey(String oAuthConsumerKey) {
        checkNotBuilt();
        configurationBean.setOAuthConsumerKey(oAuthConsumerKey);
        return this;
    }

    public ConfigurationBuilder setOAuthConsumerSecret(String oAuthConsumerSecret) {
        checkNotBuilt();
        configurationBean.setOAuthConsumerSecret(oAuthConsumerSecret);
        return this;
    }

    public ConfigurationBuilder setOAuthAccessToken(String oAuthAccessToken) {
        checkNotBuilt();
        configurationBean.setOAuthAccessToken(oAuthAccessToken);
        return this;
    }

    public ConfigurationBuilder setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        checkNotBuilt();
        configurationBean.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        return this;
    }

    public ConfigurationBuilder setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
        checkNotBuilt();
        configurationBean.setOAuthRequestTokenURL(oAuthRequestTokenURL);
        return this;
    }

    public ConfigurationBuilder setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
        checkNotBuilt();
        configurationBean.setOAuthAuthorizationURL(oAuthAuthorizationURL);
        return this;
    }

    public ConfigurationBuilder setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
        checkNotBuilt();
        configurationBean.setOAuthAccessTokenURL(oAuthAccessTokenURL);
        return this;
    }

    public ConfigurationBuilder setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
        checkNotBuilt();
        configurationBean.setOAuthAuthenticationURL(oAuthAuthenticationURL);
        return this;
    }

    public ConfigurationBuilder setRestBaseURL(String restBaseURL) {
        checkNotBuilt();
        configurationBean.setRestBaseURL(restBaseURL);
        return this;
    }

    public ConfigurationBuilder setStreamBaseURL(String streamBaseURL) {
        checkNotBuilt();
        configurationBean.setStreamBaseURL(streamBaseURL);
        return this;
    }

    public ConfigurationBuilder setUserStreamBaseURL(String userStreamBaseURL) {
        checkNotBuilt();
        configurationBean.setUserStreamBaseURL(userStreamBaseURL);
        return this;
    }

    public ConfigurationBuilder setSiteStreamBaseURL(String siteStreamBaseURL) {
        checkNotBuilt();
        configurationBean.setSiteStreamBaseURL(siteStreamBaseURL);
        return this;
    }

    public ConfigurationBuilder setAsyncNumThreads(int asyncNumThreads) {
        checkNotBuilt();
        configurationBean.setAsyncNumThreads(asyncNumThreads);
        return this;
    }

    public ConfigurationBuilder setContributingTo(long contributingTo) {
        checkNotBuilt();
        configurationBean.setContributingTo(contributingTo);
        return this;
    }

    public ConfigurationBuilder setClientVersion(String clientVersion) {
        checkNotBuilt();
        configurationBean.setClientVersion(clientVersion);
        return this;
    }

    public ConfigurationBuilder setClientURL(String clientURL) {
        checkNotBuilt();
        configurationBean.setClientURL(clientURL);
        return this;
    }

    public ConfigurationBuilder setDispatcherImpl(String dispatcherImpl) {
        checkNotBuilt();
        configurationBean.setDispatcherImpl(dispatcherImpl);
        return this;
    }

    public ConfigurationBuilder setIncludeRTsEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setIncludeRTsEnbled(enabled);
        return this;
    }

    public ConfigurationBuilder setIncludeEntitiesEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setIncludeEntitiesEnbled(enabled);
        return this;
    }

    public ConfigurationBuilder setIncludeMyRetweetEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setIncludeMyRetweetEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder setJSONStoreEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setJSONStoreEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder setMBeanEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setMBeanEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder setUserStreamRepliesAllEnabled(boolean enabled) {
        checkNotBuilt();
        configurationBean.setUserStreamRepliesAllEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder setMediaProvider(String mediaProvider) {
        checkNotBuilt();
        configurationBean.setMediaProvider(mediaProvider);
        return this;
    }

    public ConfigurationBuilder setMediaProviderAPIKey(String mediaProviderAPIKey) {
        checkNotBuilt();
        configurationBean.setMediaProviderAPIKey(mediaProviderAPIKey);
        return this;
    }

    public ConfigurationBuilder setMediaProviderParameters(Properties props) {
        checkNotBuilt();
        configurationBean.setMediaProviderParameters(props);
        return this;
    }

    public ConfigurationBuilder setLoggerImpl(String loggerImpl) {
        checkNotBuilt();
        configurationBean.setLoggerFactory(loggerImpl);
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
