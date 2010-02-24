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

import twitter4j.Version;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration base class with default settings.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class ConfigurationBase implements Configuration, java.io.Serializable {

    private boolean debug;
    private String source;
    private String userAgent;
    private String user;
    private String password;
    private boolean useSSL;
    private String httpProxyHost;
    private String httpProxyUser;
    private String httpProxyPassword;
    private int httpProxyPort;
    private int httpConnectionTimeout;
    private int httpReadTimeout;

    private int httpStreamingReadTimeout;
    private int httpRetryCount;
    private int httpRetryIntervalSeconds;
    private String oAuthConsumerKey;
    private String oAuthConsumerSecret;
    private String oAuthAccessToken;
    private String oAuthAccessTokenSecret;

    private String oAuthRequestTokenURL;
    private String oAuthAuthorizationURL;
    private String oAuthAccessTokenURL;
    private String oAuthAuthenticationURL;

    private String restBaseURL;
    private String searchBaseURL;
    private String streamBaseURL;


    private int asyncNumThreads;

    // hidden portion
    private String clientVersion;
    private String clientURL;

    public static final String DALVIK = "twitter4j.dalvik";

    private boolean IS_DALVIK;
    private static final long serialVersionUID = -6610497517837844232L;

    protected ConfigurationBase() {
        setDebug(false);
        setSource("Twitter4J");
        setUser(null);
        setPassword(null);
        setUseSSL(true);
        setHttpProxyHost(null);
        setHttpProxyUser(null);
        setHttpProxyPassword(null);
        setHttpProxyPort(-1);
        setHttpConnectionTimeout(20000);
        setHttpReadTimeout(120000);
        setHttpStreamingReadTimeout(60*5*1000);
        setHttpRetryCount(0);
        setHttpRetryIntervalSeconds(5);
        setOAuthConsumerKey(null);
        setOAuthConsumerSecret(null);
        setOAuthAccessToken(null);
        setOAuthAccessTokenSecret(null);
        setAsyncNumThreads(1);
        setClientVersion(Version.getVersion());
        setClientURL("http://twitter4j.org/en/twitter4j-" + Version.getVersion() + ".xml");
        setUserAgent("twitter4j http://twitter4j.org/ /" + Version.getVersion());


        setOAuthRequestTokenURL("http://twitter.com/oauth/request_token");
        setOAuthAuthorizationURL("http://twitter.com/oauth/authorize");
        setOAuthAccessTokenURL("http://twitter.com/oauth/access_token");
        setOAuthAuthenticationURL("http://twitter.com/oauth/authenticate");

        setRestBaseURL("http://api.twitter.com/1/");
        setSearchBaseURL("http://search.twitter.com/");
        setStreamBaseURL("http://stream.twitter.com/1/");

        // detecting dalvik (Android platform)
        String dalvikDetected;
        try {
            // dalvik.system.VMRuntime class should be existing on Android platform.
            // @see http://developer.android.com/reference/dalvik/system/VMRuntime.html
            Class.forName("dalvik.system.VMRuntime");
            dalvikDetected = "true";
        } catch (ClassNotFoundException cnfe) {
            dalvikDetected = "false";
        }
        IS_DALVIK = Boolean.valueOf(System.getProperty(DALVIK, dalvikDetected));


    }


    public final boolean isDalvik() {
        return IS_DALVIK;
    }

    public final boolean isDebugEnabled() {
        return debug;
    }

    protected final void setDebug(boolean debug) {
        this.debug = debug;
    }

    public final String getUserAgent() {
        return this.userAgent;
    }

    protected final void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        initRequestHeaders();
    }

    public final String getSource() {
        return source;
    }

    protected final void setSource(String source) {
        this.source = source;
        initRequestHeaders();
    }

    public final String getUser() {
        return user;
    }

    protected final void setUser(String user) {
        this.user = user;
    }

    public final String getPassword() {
        return password;
    }

    protected final void setPassword(String password) {
        this.password = password;
    }

    protected final void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        setRestBaseURL(getRestBaseURL());
        setSearchBaseURL(getSearchBaseURL());
        setStreamBaseURL(getStreamBaseURL());
        setOAuthRequestTokenURL(getOAuthRequestTokenURL());
        setOAuthAuthorizationURL(getOAuthAuthorizationURL());
        setOAuthAccessTokenURL(getOAuthAccessTokenURL());
        setOAuthAuthenticationURL(getOAuthAuthenticationURL());
    }

    // method for HttpRequestFactoryConfiguration
    Map<String, String> requestHeaders;
    private void initRequestHeaders() {
        requestHeaders = new HashMap<String, String>();
        requestHeaders.put("X-Twitter-Client-Version", getClientVersion());
        requestHeaders.put("X-Twitter-Client-URL", getClientURL());
        requestHeaders.put("X-Twitter-Client", getSource());

        requestHeaders.put("User-Agent", getUserAgent());
        requestHeaders.put("Accept-Encoding", "gzip");
        requestHeaders.put("Connection", "close");

    }
    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    // methods for HttpClientConfiguration

    public final String getHttpProxyHost() {
        return httpProxyHost;
    }

    protected final void setHttpProxyHost(String proxyHost) {
        this.httpProxyHost = proxyHost;
    }

    public final String getHttpProxyUser() {
        return httpProxyUser;
    }

    protected final void setHttpProxyUser(String proxyUser) {
        this.httpProxyUser = proxyUser;
    }

    public final String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    protected final void setHttpProxyPassword(String proxyPassword) {
        this.httpProxyPassword = proxyPassword;
    }

    public final int getHttpProxyPort() {
        return httpProxyPort;
    }

    protected final void setHttpProxyPort(int proxyPort) {
        this.httpProxyPort = proxyPort;
    }

    public final int getHttpConnectionTimeout() {
        return httpConnectionTimeout;
    }

    protected final void setHttpConnectionTimeout(int connectionTimeout) {
        this.httpConnectionTimeout = connectionTimeout;
    }

    public final int getHttpReadTimeout() {
        return httpReadTimeout;
    }

    protected final void setHttpReadTimeout(int readTimeout) {
        this.httpReadTimeout = readTimeout;
    }

    public int getHttpStreamingReadTimeout() {
        return httpStreamingReadTimeout;
    }

    protected void setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
        this.httpStreamingReadTimeout = httpStreamingReadTimeout;
    }


    public final int getHttpRetryCount() {
        return httpRetryCount;
    }

    protected final void setHttpRetryCount(int retryCount) {
        this.httpRetryCount = retryCount;
    }

    public final int getHttpRetryIntervalSeconds() {
        return httpRetryIntervalSeconds;
    }

    protected final void setHttpRetryIntervalSeconds(int retryIntervalSeconds) {
        this.httpRetryIntervalSeconds = retryIntervalSeconds;
    }

    // oauth related setter/getters

    public final String getOAuthConsumerKey() {
        return oAuthConsumerKey;
    }

    protected final void setOAuthConsumerKey(String oAuthConsumerKey) {
        this.oAuthConsumerKey = oAuthConsumerKey;
    }

    public final String getOAuthConsumerSecret() {
        return oAuthConsumerSecret;
    }

    protected final void setOAuthConsumerSecret(String oAuthConsumerSecret) {
        this.oAuthConsumerSecret = oAuthConsumerSecret;
    }

    public String getOAuthAccessToken() {
        return oAuthAccessToken;
    }

    protected void setOAuthAccessToken(String oAuthAccessToken) {
        this.oAuthAccessToken = oAuthAccessToken;
    }

    public String getOAuthAccessTokenSecret() {
        return oAuthAccessTokenSecret;
    }

    protected void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
    }

    public final int getAsyncNumThreads() {
        return asyncNumThreads;
    }

    protected final void setAsyncNumThreads(int asyncNumThreads) {
        this.asyncNumThreads = asyncNumThreads;
    }

    public final String getClientVersion() {
        return clientVersion;
    }

    protected final void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        initRequestHeaders();
    }

    public final String getClientURL() {
        return clientURL;
    }

    protected final void setClientURL(String clientURL) {
        this.clientURL = clientURL;
        initRequestHeaders();
    }

    public String getRestBaseURL() {
        return restBaseURL;
    }

    protected void setRestBaseURL(String restBaseURL) {
        this.restBaseURL = fixURL(useSSL, restBaseURL);
    }

    public String getSearchBaseURL() {
        return searchBaseURL;
    }

    protected void setSearchBaseURL(String searchBaseURL) {
        // search api tends to fail with SSL as of 12/31/2009
        this.searchBaseURL = fixURL(false, searchBaseURL);
    }

    public String getStreamBaseURL() {
        return streamBaseURL;
    }

    protected void setStreamBaseURL(String streamBaseURL) {
        // streaming api doesn't support SSL as of 12/30/2009
        this.streamBaseURL = fixURL(false, streamBaseURL);
    }

    public String getOAuthRequestTokenURL() {
        return oAuthRequestTokenURL;
    }

    protected void setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
        this.oAuthRequestTokenURL = fixURL(useSSL, oAuthRequestTokenURL);
    }

    public String getOAuthAuthorizationURL() {
        return oAuthAuthorizationURL;
    }

    protected void setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
        this.oAuthAuthorizationURL = fixURL(useSSL, oAuthAuthorizationURL);
    }

    public String getOAuthAccessTokenURL() {
        return oAuthAccessTokenURL;
    }

    protected void setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
        this.oAuthAccessTokenURL = fixURL(useSSL, oAuthAccessTokenURL);
    }

    public String getOAuthAuthenticationURL() {
        return oAuthAuthenticationURL;
    }

    protected void setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
        this.oAuthAuthenticationURL = fixURL(useSSL, oAuthAuthenticationURL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigurationBase)) return false;

        ConfigurationBase that = (ConfigurationBase) o;

        if (IS_DALVIK != that.IS_DALVIK) return false;
        if (asyncNumThreads != that.asyncNumThreads) return false;
        if (httpConnectionTimeout != that.httpConnectionTimeout) return false;
        if (debug != that.debug) return false;
        if (httpProxyPort != that.httpProxyPort) return false;
        if (httpReadTimeout != that.httpReadTimeout) return false;
        if (httpRetryCount != that.httpRetryCount) return false;
        if (httpRetryIntervalSeconds != that.httpRetryIntervalSeconds) return false;
        if (useSSL != that.useSSL) return false;
        if (clientURL != null ? !clientURL.equals(that.clientURL) : that.clientURL != null)
            return false;
        if (clientVersion != null ? !clientVersion.equals(that.clientVersion) : that.clientVersion != null)
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
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;
        if (httpProxyHost != null ? !httpProxyHost.equals(that.httpProxyHost) : that.httpProxyHost != null)
            return false;
        if (httpProxyPassword != null ? !httpProxyPassword.equals(that.httpProxyPassword) : that.httpProxyPassword != null)
            return false;
        if (httpProxyUser != null ? !httpProxyUser.equals(that.httpProxyUser) : that.httpProxyUser != null)
            return false;
        if (restBaseURL != null ? !restBaseURL.equals(that.restBaseURL) : that.restBaseURL != null)
            return false;
        if (searchBaseURL != null ? !searchBaseURL.equals(that.searchBaseURL) : that.searchBaseURL != null)
            return false;
        if (source != null ? !source.equals(that.source) : that.source != null)
            return false;
        if (streamBaseURL != null ? !streamBaseURL.equals(that.streamBaseURL) : that.streamBaseURL != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null)
            return false;
        if (userAgent != null ? !userAgent.equals(that.userAgent) : that.userAgent != null)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "ConfigurationBase{" +
                "debug=" + debug +
                ", source='" + source + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", useSSL=" + useSSL +
                ", httpProxyHost='" + httpProxyHost + '\'' +
                ", httpProxyUser='" + httpProxyUser + '\'' +
                ", httpProxyPassword='" + httpProxyPassword + '\'' +
                ", httpProxyPort=" + httpProxyPort +
                ", httpConnectionTimeout=" + httpConnectionTimeout +
                ", httpReadTimeout=" + httpReadTimeout +
                ", httpRetryCount=" + httpRetryCount +
                ", httpRetryIntervalMilliSecs=" + httpRetryIntervalSeconds +
                ", oAuthConsumerKey='" + oAuthConsumerKey + '\'' +
                ", oAuthConsumerSecret='" + oAuthConsumerSecret + '\'' +
                ", oAuthAccessToken='" + oAuthAccessToken + '\'' +
                ", oAuthAccessTokenSecret='" + oAuthAccessTokenSecret + '\'' +
                ", oAuthRequestTokenURL='" + oAuthRequestTokenURL + '\'' +
                ", oAuthAuthorizationURL='" + oAuthAuthorizationURL + '\'' +
                ", oAuthAccessTokenURL='" + oAuthAccessTokenURL + '\'' +
                ", oAuthAuthenticationURL='" + oAuthAuthenticationURL + '\'' +
                ", restBaseURL='" + restBaseURL + '\'' +
                ", searchBaseURL='" + searchBaseURL + '\'' +
                ", streamBaseURL='" + streamBaseURL + '\'' +
                ", asyncNumThreads=" + asyncNumThreads +
                ", clientVersion='" + clientVersion + '\'' +
                ", clientURL='" + clientURL + '\'' +
                ", IS_DALVIK=" + IS_DALVIK +
                '}';
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
    //@todo implement readresolve to save memory usage
}