package twitter4j.http;

import twitter4j.TwitterException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public interface OAuthSupport {
    /**
     * Retrieves a request token
     *
     * @return generated request token.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/pages/oauth_faq">OAuth FAQ | dev.twitter.com</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @since Twitter 2.0.0
     */
    RequestToken getOAuthRequestToken() throws TwitterException;

    /**
     * Retrieves a request token
     *
     * @param callbackURL callback URL
     * @return generated request token
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/pages/oauth_faq">OAuth FAQ | dev.twitter.com</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @since Twitter 2.0.0
     */
    RequestToken getOAuthRequestToken(String callbackURL) throws TwitterException;

    /**
     * Returns an access token associated with this instance.<br>
     * If no access token is associated with this instance, this will retrieve a new access token.
     *
     * @return access token
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @throws IllegalStateException when RequestToken has never been acquired
     * @see <a href="http://dev.twitter.com/pages/oauth_faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    AccessToken getOAuthAccessToken() throws TwitterException;

    /**
     * Retrieves an access token.
     *
     * @param oauthVerifier OAuth verifier. AKA pin.
     * @return access token
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://dev.twitter.com/pages/oauth_faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException;

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param requestToken the request token
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://dev.twitter.com/pages/oauth_faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException;

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param requestToken the request token
     * @param oauthVerifier OAuth verifier. AKA pin.
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.1.1
     */
    AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException;

    /**
     * Retrieves an access token associated with the supplied screen name and password using xAuth.<br>
     * In order to get access acquire AccessToken using xAuth, you must apply by sending an email to api@twitter.com — all other applications will receive an HTTP 401 error.  Web-based applications will not be granted access, except on a temporary basis for when they are converting from basic-authentication support to full OAuth support.<br>
     * Storage of Twitter usernames and passwords is forbidden. By using xAuth, you are required to store only access tokens and access token secrets. If the access token expires or is expunged by a user, you must ask for their login and password again before exchanging the credentials for an access token.
     *
     * @param screenName the screen name
     * @param password the password
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://dev.twitter.com/pages/oauth_faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-oauth-access_token-for-xAuth">Twitter REST API Method: oauth access_token for xAuth</a>
     * @since Twitter 2.1.1
     */
    AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException;

    /**
     * Sets the access token
     *
     * @param accessToken accessToken
     * @since Twitter 2.0.0
     */
    void setOAuthAccessToken(AccessToken accessToken);
}
