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
package twitter4j.auth;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.0
 */
public interface AsyncOAuthSupport {
    /**
     * Retrieves a request token
     *
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | Twitter Developers</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/request_token">POST oauth/request_token | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getOAuthRequestTokenAsync();

    /**
     * Retrieves a request token
     *
     * @param callbackURL callback URL
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | Twitter Developers</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/request_token">POST oauth/request_token | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getOAuthRequestTokenAsync(String callbackURL);


    /**
     * Retrieves a request token
     *
     * @param callbackURL     callback URL
     * @param xAuthAccessType Overrides the access level an application requests to a users account. Supported values are read or write. This parameter is intended to allow a developer to register a read/write application but also request read only access when appropriate.
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | Twitter Developers</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/request_token">POST oauth/request_token | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getOAuthRequestTokenAsync(String callbackURL, String xAuthAccessType);


    /**
     * Retrieves a request token
     *
     * @param callbackURL     callback URL
     * @param xAuthAccessType Overrides the access level an application requests to a users account. Supported values are read or write. This parameter is intended to allow a developer to register a read/write application but also request read only access when appropriate.
     * @param xAuthMode       Set to reverse_auth to obtain a special request token to be used in the reverse auth process.
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | Twitter Developers</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/request_token">POST oauth/request_token | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getOAuthRequestTokenAsync(String callbackURL, String xAuthAccessType, String xAuthMode);


    /**
     * Returns an access token associated with this instance.<br>
     * If no access token is associated with this instance, this will retrieve a new access token.
     *
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getOAuthAccessTokenAsync();

    /**
     * Retrieves an access token.
     *
     * @param oauthVerifier OAuth verifier. AKA pin.
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getOAuthAccessTokenAsync(String oauthVerifier);

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param requestToken the request token
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getOAuthAccessTokenAsync(RequestToken requestToken);

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param requestToken  the request token
     * @param oauthVerifier OAuth verifier. AKA pin.
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter 3.0.0
     */
    void getOAuthAccessTokenAsync(RequestToken requestToken, String oauthVerifier);

    /**
     * Retrieves an access token associated with the supplied screen name and password using xAuth.<br>
     * In order to get access acquire AccessToken using xAuth, you must apply by sending an email to api@twitter.com — all other applications will receive an HTTP 401 error.  Web-based applications will not be granted access, except on a temporary basis for when they are converting from basic-authentication support to full OAuth support.<br>
     * Storage of Twitter usernames and passwords is forbidden. By using xAuth, you are required to store only access tokens and access token secrets. If the access token expires or is expunged by a user, you must ask for their login and password again before exchanging the credentials for an access token.
     *
     * @param screenName the screen name
     * @param password   the password
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="https://dev.twitter.com/docs/oauth/xauth">xAuth | Twitter Developers</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter 3.0.0
     */
    void getOAuthAccessTokenAsync(String screenName, String password);

}
