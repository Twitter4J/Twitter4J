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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Configuration {
    public static final String CONFIGURATION_IMPL = "twitter4j.configuration.impl";

    private boolean debug;
    private String source;
    private String userAgent;
    private String user;
    private String password;
    private boolean useSSL;
    private String proxyHost;
    private String proxyUser;
    private String proxyPassword;
    private int proxyPort;
    private int connectionTimeout;
    private int readTimeout;
    private int retryCount;
    private int retryIntervalMilliSecs;
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
    public static final String DEFAULT_CONFIGURATION_IMPL = "twitter4j.conf.PropertyConfiguration";

    private boolean IS_DALVIK;
    private static final Configuration CONFIGURATION;

    static {
        String CONFIG_IMPL = System.getProperty(CONFIGURATION_IMPL, DEFAULT_CONFIGURATION_IMPL);
        try {
            Class configImplClass = Class.forName(CONFIG_IMPL);
            CONFIGURATION = (Configuration) configImplClass.newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new ExceptionInInitializerError(cnfe);
        } catch (InstantiationException ie) {
            throw new ExceptionInInitializerError(ie);
        } catch (IllegalAccessException iae) {
            throw new ExceptionInInitializerError(iae);
        }
    }

    protected Configuration() {
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
        setHttpRetryCount(1);
        setHttpRetryIntervalSecs(5);
        setOAuthConsumerKey(null);
        setOAuthConsumerSecret(null);
        setOAuthAccessToken(null);
        setOAuthAccessTokenSecret(null);
        setAsyncNumThreads(1);
        setClientVersion(Version.getVersion());
        setClientURL("http://yusuke.homeip.net/twitter4j/en/twitter4j-" + Version.getVersion() + ".xml");
        setUserAgent("twitter4j http://yusuke.homeip.net/twitter4j/ /" + Version.getVersion());


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

    public static Configuration getInstance() {
        return CONFIGURATION;
    }

    public final boolean isDalvik() {
        return IS_DALVIK;
    }

    public final boolean isDebug() {
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
    }

    public final String getSource() {
        return source;
    }

    protected final void setSource(String source) {
        this.source = source;
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

    public final String getProxyHost() {
        return proxyHost;
    }

    protected final void setHttpProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public final String getProxyUser() {
        return proxyUser;
    }

    protected final void setHttpProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public final String getProxyPassword() {
        return proxyPassword;
    }

    protected final void setHttpProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public final int getProxyPort() {
        return proxyPort;
    }

    protected final void setHttpProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public final int getHttpConnectionTimeout() {
        return connectionTimeout;
    }

    protected final void setHttpConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public final int getHttpReadTimeout() {
        return readTimeout;
    }

    protected final void setHttpReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public final int getRetryCount() {
        return retryCount;
    }

    protected final void setHttpRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public final int getRetryIntervalMilliSecs() {
        return retryIntervalMilliSecs;
    }

    protected final void setHttpRetryIntervalSecs(int retryIntervalSecs) {
        this.retryIntervalMilliSecs = retryIntervalSecs * 1000;
    }

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
    }

    public final String getClientURL() {
        return clientURL;
    }

    protected final void setClientURL(String clientURL) {
        this.clientURL = clientURL;
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
        this.searchBaseURL = fixURL(useSSL, searchBaseURL);
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
        if (!(o instanceof Configuration)) return false;

        Configuration that = (Configuration) o;

        if (IS_DALVIK != that.IS_DALVIK) return false;
        if (asyncNumThreads != that.asyncNumThreads) return false;
        if (connectionTimeout != that.connectionTimeout) return false;
        if (debug != that.debug) return false;
        if (proxyPort != that.proxyPort) return false;
        if (readTimeout != that.readTimeout) return false;
        if (retryCount != that.retryCount) return false;
        if (retryIntervalMilliSecs != that.retryIntervalMilliSecs) return false;
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
        if (proxyHost != null ? !proxyHost.equals(that.proxyHost) : that.proxyHost != null)
            return false;
        if (proxyPassword != null ? !proxyPassword.equals(that.proxyPassword) : that.proxyPassword != null)
            return false;
        if (proxyUser != null ? !proxyUser.equals(that.proxyUser) : that.proxyUser != null)
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
        return "Configuration{" +
                "debug=" + debug +
                ", source='" + source + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", useSSL=" + useSSL +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyUser='" + proxyUser + '\'' +
                ", proxyPassword='" + proxyPassword + '\'' +
                ", proxyPort=" + proxyPort +
                ", connectionTimeout=" + connectionTimeout +
                ", readTimeout=" + readTimeout +
                ", retryCount=" + retryCount +
                ", retryIntervalMilliSecs=" + retryIntervalMilliSecs +
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
}
