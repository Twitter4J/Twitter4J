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
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ">Twitter API Wiki - OAuth FAQ</a>
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
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ">Twitter API Wiki - OAuth FAQ</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @since Twitter 2.0.0
     */
    RequestToken getOAuthRequestToken(String callbackURL) throws TwitterException;

    /**
     * Retrieves an access token.
     *
     * @return access token
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @throws IllegalStateException when RequestToken has never been acquired
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
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
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
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
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
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
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException;

    /**
     * Sets the access token
     *
     * @param accessToken accessToken
     * @since Twitter 2.0.0
     */
    void setOAuthAccessToken(AccessToken accessToken);
}
