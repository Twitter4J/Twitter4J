/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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

    public ConfigurationBuilder setDebugEnabled(boolean debugEnabled) {
        checkNotBuilt();
        configurationBean.setDebug(debugEnabled);
        return this;
    }

    /**
     * @param source source
     * @return this instance
     * @deprecated source parameter is no longer supported.
     */

    public ConfigurationBuilder setSource(String source) {
        checkNotBuilt();
        configurationBean.setSource(source);
        return this;
    }

    public ConfigurationBuilder setUserAgent(String userAgent) {
        checkNotBuilt();
        configurationBean.setUserAgent(userAgent);
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

    public ConfigurationBuilder setSearchBaseURL(String searchBaseURL) {
        checkNotBuilt();
        configurationBean.setSearchBaseURL(searchBaseURL);
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

    public Configuration build() {
        checkNotBuilt();
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
