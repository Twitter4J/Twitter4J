package twitter4j.auth;

/**
 * @author Amine Bezzarga - abezzarg at gmail.com
 */
public interface AsyncOAuth2Support {

    /**
     * Obtains an OAuth 2 Bearer token.
     *
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth2/token">POST oauth2/token | Twitter Developers</a>
     */
    void getOAuth2TokenAsync();
}
