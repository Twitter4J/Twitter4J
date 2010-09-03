/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.AuthorizationFactory;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.NullAuthorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.OAuthSupport;
import twitter4j.http.RequestToken;
import twitter4j.internal.http.XAuthAuthorization;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
class TwitterOAuthSupportBaseImpl extends TwitterOAuthSupportBase {

    protected transient String screenName = null;
    protected transient int id = 0;

    TwitterOAuthSupportBaseImpl(Configuration conf) {
        super(conf);
    }


    TwitterOAuthSupportBaseImpl() {
        super(ConfigurationContext.getInstance());
    }

    TwitterOAuthSupportBaseImpl(String screenName, String password) {
        super(ConfigurationContext.getInstance(), screenName, password);
    }
    /*package*/

    TwitterOAuthSupportBaseImpl(Configuration conf, String screenName, String password) {
        super(conf, screenName, password);
    }
    /*package*/

    TwitterOAuthSupportBaseImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
    }


    // implementation for OAuthSupport interface

    /**
     * {@inheritDoc}
     */
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    /**
     * {@inheritDoc}
     */
    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl);
    }

    /**
     * {@inheritDoc}
     * Basic authenticated instance of this class will try acquiring an AccessToken using xAuth.<br>
     * In order to get access acquire AccessToken using xAuth, you must apply by sending an email to <a href="mailto:api@twitter.com">api@twitter.com</a> all other applications will receive an HTTP 401 error.  Web-based applications will not be granted access, except on a temporary basis for when they are converting from basic-authentication support to full OAuth support.<br>
     * Storage of Twitter usernames and passwords is forbidden. By using xAuth, you are required to store only access tokens and access token secrets. If the access token expires or is expunged by a user, you must ask for their login and password again before exchanging the credentials for an access token.
     *
     * @throws TwitterException When Twitter service or network is unavailable, when the user has not authorized, or when the client application is not permitted to use xAuth
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-oauth-access_token-for-xAuth">Twitter REST API Method: oauth access_token for xAuth</a>
     */
    public synchronized AccessToken getOAuthAccessToken() throws TwitterException {
        Authorization auth = getAuthorization();
        AccessToken oauthAccessToken;
        if (auth instanceof BasicAuthorization) {
            BasicAuthorization basicAuth = (BasicAuthorization) auth;
            auth = AuthorizationFactory.getInstance(conf, true);
            if (auth instanceof OAuthAuthorization) {
                this.auth = auth;
                OAuthAuthorization oauthAuth = (OAuthAuthorization) auth;
                oauthAccessToken = oauthAuth.getOAuthAccessToken(basicAuth.getUserId(), basicAuth.getPassword());
            } else {
                throw new IllegalStateException("consumer key / secret combination not supplied.");
            }
        } else {
            if (auth instanceof XAuthAuthorization) {
                XAuthAuthorization xauth = (XAuthAuthorization) auth;
                this.auth = xauth;
                OAuthAuthorization oauthAuth = new OAuthAuthorization(conf, xauth.getConsumerKey(), xauth.getConsumerSecret());
                oauthAccessToken = oauthAuth.getOAuthAccessToken(xauth.getUserId(), xauth.getPassword());
            }else{
            oauthAccessToken = getOAuth().getOAuthAccessToken();
            }
        }
        screenName = oauthAccessToken.getScreenName();
        id = oauthAccessToken.getUserId();
        return oauthAccessToken;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public synchronized AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        AccessToken oauthAccessToken = getOAuth().getOAuthAccessToken(oauthVerifier);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        OAuthSupport oauth = getOAuth();
        AccessToken oauthAccessToken = oauth.getOAuthAccessToken(requestToken);
        screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Use {@link TwitterFactory#getInstance(twitter4j.http.Authorization)} instead
     */
    public synchronized void setOAuthAccessToken(AccessToken accessToken) {
        getOAuth().setOAuthAccessToken(accessToken);
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Use {@link TwitterFactory#getInstance(twitter4j.http.Authorization)} instead
     */
    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws TwitterException {
        return getOAuth().getOAuthAccessToken(new RequestToken(token, tokenSecret));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized AccessToken getOAuthAccessToken(String token
            , String tokenSecret, String pin) throws TwitterException {
        return getOAuthAccessToken(new RequestToken(token, tokenSecret), pin);
    }

    /**
     * Sets the access token
     *
     * @param token       access token
     * @param tokenSecret access token secret
     * @throws IllegalStateException when AccessToken has already been retrieved or set
     * @since Twitter 2.0.0
     * @deprecated Use {@link TwitterFactory#getInstance(twitter4j.http.Authorization)} instead
     */
    public void setOAuthAccessToken(String token, String tokenSecret) {
        getOAuth().setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }

    /**
     * tests if the instance is authenticated by Basic
     *
     * @return returns true if the instance is authenticated by Basic
     */
    public boolean isOAuthEnabled() {
        return auth instanceof OAuthAuthorization && auth.isEnabled();
    }

    /* OAuth support methods */

    private OAuthSupport getOAuth() {
        if (!(auth instanceof OAuthSupport)) {
            throw new IllegalStateException(
                    "OAuth consumer key/secret combination not supplied");
        }
        return (OAuthSupport) auth;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret) {
        if(null == consumerKey){
            throw new NullPointerException("consumer key is null");
        }
        if(null == consumerSecret){
            throw new NullPointerException("consumer secret is null");
        }
        if (auth instanceof NullAuthorization) {
            auth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        } else if (auth instanceof BasicAuthorization) {
            XAuthAuthorization xauth = new XAuthAuthorization((BasicAuthorization)auth);
            xauth.setOAuthConsumer(consumerKey, consumerSecret);
            auth = xauth;
        } else if (auth instanceof OAuthAuthorization) {
            throw new IllegalStateException("consumer key/secret pair already set.");
        }
    }

}
