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

import twitter4j.auth.AuthorizationConfiguration;
import twitter4j.internal.http.HttpClientConfiguration;
import twitter4j.internal.http.HttpClientWrapperConfiguration;

import java.util.Map;
import java.util.Properties;


/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface Configuration extends HttpClientConfiguration
        , HttpClientWrapperConfiguration
        , AuthorizationConfiguration
        , java.io.Serializable {

    boolean isDalvik();

    boolean isGAE();

    boolean isDebugEnabled();

    String getUserAgent();

    String getUser();

    String getPassword();

    Map<String, String> getRequestHeaders();

    // methods for HttpClientConfiguration

    String getHttpProxyHost();

    String getHttpProxyUser();

    String getHttpProxyPassword();

    int getHttpProxyPort();

    int getHttpConnectionTimeout();

    int getHttpReadTimeout();

    int getHttpStreamingReadTimeout();

    int getHttpRetryCount();

    int getHttpRetryIntervalSeconds();

    int getHttpMaxTotalConnections();

    int getHttpDefaultMaxPerRoute();

    // oauth related setter/getters

    String getOAuthConsumerKey();

    String getOAuthConsumerSecret();

    String getOAuthAccessToken();

    String getOAuthAccessTokenSecret();

    String getClientVersion();

    String getClientURL();

    String getRestBaseURL();

    String getStreamBaseURL();

    String getOAuthRequestTokenURL();

    String getOAuthAuthorizationURL();

    String getOAuthAccessTokenURL();

    String getOAuthAuthenticationURL();

    String getUserStreamBaseURL();

    String getSiteStreamBaseURL();

	boolean isIncludeMyRetweetEnabled();

    boolean isJSONStoreEnabled();

    boolean isMBeanEnabled();

    boolean isUserStreamRepliesAllEnabled();

    boolean isStallWarningsEnabled();

    String getMediaProvider();

    String getMediaProviderAPIKey();

    Properties getMediaProviderParameters();

    int getAsyncNumThreads();

    long getContributingTo();

    String getDispatcherImpl();

    String getLoggerFactory();

    boolean isIncludeRTsEnabled();

    boolean isIncludeEntitiesEnabled();
}
