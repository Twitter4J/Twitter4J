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
package twitter4j;

import twitter4j.http.HttpClient;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*protected*/ class TwitterSupport {
    protected HttpClient http = new HttpClient();
    protected String source = Configuration.getSource();
    protected final boolean USE_SSL;

    /*package*/ TwitterSupport(){
        this(null, null);
    }
    /*package*/ TwitterSupport(String userId, String password){
        USE_SSL = Configuration.useSSL();
        setClientVersion(null);
        setClientURL(null);
        setUserId(userId);
        setPassword(password);
    }

    /**
     * Sets the User-Agent header. System property -Dtwitter4j.http.userAgent overrides this attribute.
     * @param userAgent UserAgent
     * @since Twitter4J 1.1.8
     */
    public void setUserAgent(String userAgent){
        http.setUserAgent(userAgent);
    }

    /**
     *
     * @return UserAgent
     * @since Twitter4J 1.1.8
     */
    public String getUserAgent(){
        return http.getUserAgent();
    }

    /**
     * Sets the X-Twitter-Client-Version header. System property -Dtwitter4j.clientVersion overrides this attribute.
     * @param version client version
     * @since Twitter4J 1.1.8
     */
    public void setClientVersion(String version){
        setRequestHeader("X-Twitter-Client-Version", Configuration.getCilentVersion(version));
    }

    /**
     *
     * @return client version
     * @since Twitter4J 1.1.8
     */
    public String getClientVersion(){
        return http.getRequestHeader("X-Twitter-Client-Version");
    }

    /**
     * Sets the X-Twitter-Client-URL header. System property -Dtwitter4j.clientURL overrides this attribute.
     * @param clientURL client URL
     * @since Twitter4J 1.1.8
     */
    public void setClientURL(String clientURL){
        setRequestHeader("X-Twitter-Client-URL", Configuration.getClientURL(clientURL));
    }

    /**
     *
     * @return client URL
     * @since Twitter4J 1.1.8
     */
    public String getClientURL(){
        return http.getRequestHeader("X-Twitter-Client-URL");
    }

    /**
     * Sets the userid
     *
     * @param userId new userid
     */
    public synchronized void setUserId(String userId) {
        http.setUserId(Configuration.getUser(userId));
    }

    /**
     * Returns authenticating userid
     *
     * @return userid
     */
    public String getUserId() {
        return http.getUserId();
    }

    /**
     * Sets the password
     *
     * @param password new password
     */
    public synchronized void setPassword(String password) {
        http.setPassword(Configuration.getPassword(password));
    }

    /**
     * Returns authenticating password
     *
     * @return password
     */
    public String getPassword() {
        return http.getPassword();
    }

    /**
     * Enables use of HTTP proxy
     *
     * @param proxyHost proxy host, can be overridden system property -Dtwitter4j.http.proxyHost , -Dhttp.proxyHost
     * @param proxyPort proxy port, can be overridden system property -Dtwitter4j.http.proxyPort , -Dhttp.proxyPort
     * @since Twitter4J 1.1.6
     */
    public void setHttpProxy(String proxyHost, int proxyPort) {
        http.setProxyHost(proxyHost);
        http.setProxyPort(proxyPort);
    }

    /**
     * Adds authentication on HTTP proxy
     *
     * @param proxyUser proxy user, can be overridden system property -Dtwitter4j.http.proxyUser
     * @param proxyPass proxy password, can be overridden system property -Dtwitter4j.http.proxyPassword
     * @since Twitter4J 1.1.6
     */
    public void setHttpProxyAuth(String proxyUser, String proxyPass) {
        http.setProxyAuthUser(proxyUser);
        http.setProxyAuthPassword(proxyPass);
    }

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the Twitter API.
     * System property -Dtwitter4j.http.connectionTimeout overrides this attribute.
     *
     * @param connectionTimeout an int that specifies the connect timeout value in milliseconds
     * @since Twitter4J 1.1.6
     */
    public void setHttpConnectionTimeout(int connectionTimeout) {
        http.setConnectionTimeout(connectionTimeout);
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds.
     *
     * @param readTimeoutMilliSecs an int that specifies the timeout value to be used in milliseconds
     * @since Twitter4J 1.1.6
     */
    public void setHttpReadTimeout(int readTimeoutMilliSecs) {
        http.setReadTimeout(readTimeoutMilliSecs);
    }

    /**
     * Sets X-Twitter-Client http header and the source parameter that will be passed by updating methods. System property -Dtwitter4j.source overrides this attribute.
     * System property -Dtwitter4j.source overrides this attribute.
     *
     * @param source the new source
     * @see <a href='http://apiwiki.twitter.com/FAQ#HowdoIget“fromMyApp”appendedtoupdatessentfrommyAPIapplication'>How do I get "from [MyApp]" appended to updates sent from my API application?</a>
     * @see <a href="http://twitter.com/help/request_source">Twitter - Request a link to your application</a>
     */
    public void setSource(String source) {
        this.source = Configuration.getSource(source);
        setRequestHeader("X-Twitter-Client", this.source);
    }

    /**
     * Returns the source
     *
     * @return source
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Sets the request header name/value combination
     * see Twitter Fan Wiki for detail.
     * http://twitter.pbwiki.com/API-Docs#RequestHeaders
     *
     * @param name  the name of the request header
     * @param value the value of the request header
     */
    public void setRequestHeader(String name, String value) {
        http.setRequestHeader(name, value);
    }

    /**
     * Set true to force using POST method communicating to the server.<br>
     * This method doesn't take effect anymore
     *
     * @param forceUsePost if true POST method will be used forcibly
     * @deprecated some methods don't accept POST method anymore
     */
    public void forceUsePost(boolean forceUsePost) {
        // this method doesn't take effect anymore
    }

    /**
     * @return true if POST is used forcibly
     */
    public boolean isUsePostForced() {
        return false;
    }

    public void setRetryCount(int retryCount) {
        http.setRetryCount(retryCount);
    }

    public void setRetryIntervalSecs(int retryIntervalSecs) {
        http.setRetryIntervalSecs(retryIntervalSecs);
    }
}
