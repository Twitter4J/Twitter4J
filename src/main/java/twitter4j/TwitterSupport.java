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

import twitter4j.conf.Configuration;
import twitter4j.http.AccessToken;
import twitter4j.http.Authentication;
import twitter4j.http.BasicAuthentication;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpRequestFactory;
import twitter4j.http.NullAuthentication;
import twitter4j.http.OAuthAuthentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class TwitterSupport implements java.io.Serializable {
    protected static final Configuration conf = Configuration.getInstance();

    protected static final HttpRequestFactory requestFactory;

    static{
        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("X-Twitter-Client-Version", conf.getClientVersion());
        requestHeaders.put("X-Twitter-Client-URL", conf.getClientURL());
        requestHeaders.put("X-Twitter-Client", conf.getSource());

        requestHeaders.put("User-Agent", conf.getUserAgent());
        requestHeaders.put("Accept-Encoding", "gzip");
        requestHeaders.put("Connection", "close");
        requestFactory = new HttpRequestFactory(requestHeaders);

    }

    protected HttpClient http = new HttpClient();

    protected List<RateLimitStatusListener> accountRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    protected List<RateLimitStatusListener> ipRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    private static final long serialVersionUID = -4779804628175934804L;
    Authentication auth = null;

    /*package*/ TwitterSupport(){
        init();
    }
    /*package*/ TwitterSupport(String userId, String password){
        useBasic(userId, password);
        init();
    }
    private void useBasic(String userId, String password){
        auth = new BasicAuthentication(userId, password);
    }

    /**
     *
     * @param consumerKey OAuth consumer key
     * @param consumerSecret OAuth consumer secret
     * @since Twitter 2.0.0
     */
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
        auth = new OAuthAuthentication(consumerKey, consumerSecret, http);
    }

    protected void ensureAuthenticationEnabled() {
        if (!auth.isAuthenticationEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
    }

    protected void ensureBasicAuthenticationEnabled() {
        if (!(auth instanceof BasicAuthentication)) {
            throw new IllegalStateException(
                    "user ID/password combination not supplied");
        }
    }
    protected OAuthAuthentication getOAuth() {
        if (!(auth instanceof OAuthAuthentication)) {
            throw new IllegalStateException(
                    "OAuth consumer key/secret combination not supplied");
        }
        return (OAuthAuthentication)auth;
    }
    private void init(){
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == auth) {
            // firstly try to find oauth tokens in the configuration
            boolean consumerKeyFound = false;
            if (null != consumerKey && null != consumerSecret) {
                setOAuthConsumer(consumerKey, consumerSecret);
                consumerKeyFound = true;
            }
            boolean accessTokenFound = false;
            String accessToken = conf.getOAuthAccessToken();
            String accessTokenSecret = conf.getOAuthAccessTokenSecret();
            if (null != accessToken && null != accessTokenSecret) {
                getOAuth().setAccessToken(new AccessToken(accessToken, accessTokenSecret));
                accessTokenFound = true;
            }
            // if oauth tokens are not found in the configuration, try to find basic auth credentials
            String userId = conf.getUser();
            String password = conf.getPassword();
            if (!consumerKeyFound && !accessTokenFound && userId != null && password != null) {
                auth = new BasicAuthentication(userId, password);
            }
            if(null == auth){
                auth = NullAuthentication.getInstance();
            }
        }
    }

    /**
     * Registers a RateLimitStatusListener for account associated rate limits
     * @param listener the listener to be added
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public void addAccountRateLimitStatusListener(RateLimitStatusListener listener){
    	accountRateLimitStatusListeners.add(listener);
    }


    /**
     * Registers a RateLimitStatusListener for ip associated rate limits
     *
     * @param listener the listener to be added
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public void addIpRateLimitStatusListener(RateLimitStatusListener listener){
    	ipRateLimitStatusListeners.add(listener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterSupport)) return false;

        TwitterSupport that = (TwitterSupport) o;

        if (!accountRateLimitStatusListeners.equals(that.accountRateLimitStatusListeners))
            return false;
        if (!http.equals(that.http)) return false;
        if (!ipRateLimitStatusListeners.equals(that.ipRateLimitStatusListeners))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = http.hashCode();
        result = 31 * result + accountRateLimitStatusListeners.hashCode();
        result = 31 * result + ipRateLimitStatusListeners.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TwitterSupport{" +
                "http=" + http +
                ", accountRateLimitStatusListeners=" + accountRateLimitStatusListeners +
                ", ipRateLimitStatusListeners=" + ipRateLimitStatusListeners +
                '}';
    }
}
