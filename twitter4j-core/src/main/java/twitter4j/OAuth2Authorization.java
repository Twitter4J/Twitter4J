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

package twitter4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * @author KOMIYA Atsushi - komiya.atsushi at gmail.com
 * @see <a href="https://dev.twitter.com/docs/auth/application-only-auth">Application-only authentication</a>
 */
@SuppressWarnings("rawtypes")
public class OAuth2Authorization implements Authorization, java.io.Serializable {

    private static final long serialVersionUID = -2895232598422218647L;

    private final String consumerKey;
    private final String consumerSecret;
    private OAuth2Token token;
    private final String oAuth2Scope;
    private final HttpClient http;
    private final String oAuth2TokenURL;
    private final String oAuth2InvalidateTokenURL;

    OAuth2Authorization(@SuppressWarnings("rawtypes") Configuration conf) {
        oAuth2Scope = conf.oAuth2Scope;
        http = conf.http;
        oAuth2TokenURL = conf.oAuth2TokenURL;
        oAuth2InvalidateTokenURL = conf.oAuth2InvalidateTokenURL;

        this.consumerKey = conf.oAuthConsumerKey != null ? conf.oAuthConsumerKey : "";
        this.consumerSecret = conf.oAuthConsumerSecret != null ? conf.oAuthConsumerSecret : "";
        if (conf.oAuth2TokenType != null && conf.oAuth2AccessToken != null) {
            token = new OAuth2Token(conf.oAuth2TokenType, conf.oAuth2AccessToken);
        }
    }

    public static OAuth2AuthorizationBuilder newBuilder() {
        return new OAuth2AuthorizationBuilder();
    }

    /**
     * Equivalent to OAuth2Authorization.newBuilder().oAuthConsumer(key, secret).build();
     *
     * @param consumerKey    consumer key
     * @param consumerSecret consumer secret
     * @return OAuth2Authorization
     */
    public static OAuth2Authorization getInstance(String consumerKey, String consumerSecret) {
        return newBuilder().oAuthConsumer(consumerKey, consumerSecret).build();
    }

    /**
     * Equivalent to OAuth2Authorization.newBuilder().build();
     *
     * @return OAuth2Authorization
     */
    public static OAuth2Authorization getInstance() {
        return newBuilder().build();
    }

    public static class OAuth2AuthorizationBuilder extends Configuration<OAuth2Authorization, OAuth2Authorization.OAuth2AuthorizationBuilder> {

        OAuth2AuthorizationBuilder() {
            super(OAuth2Authorization::new);
        }
    }


    /**
     * Obtains an OAuth 2 Bearer token.
     *
     * @return OAuth 2 Bearer token
     * @throws TwitterException      when Twitter service or network is unavailable, or connecting non-SSL endpoints.
     * @throws IllegalStateException when Bearer token is already available, or OAuth consumer is not available.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth2/token">POST oauth2/token | Twitter Developers</a>
     */
    public OAuth2Token getOAuth2Token() throws TwitterException {
        if (token != null) {
            throw new IllegalStateException("OAuth 2 Bearer Token is already available.");
        }

        HttpParameter[] params = new HttpParameter[oAuth2Scope == null ? 1 : 2];
        params[0] = new HttpParameter("grant_type", "client_credentials");
        if (oAuth2Scope != null) {
            params[1] = new HttpParameter("scope", oAuth2Scope);
        }

        HttpResponse res = http.post(oAuth2TokenURL, params, this, null);
        if (res.getStatusCode() != 200) {
            throw new TwitterException("Obtaining OAuth 2 Bearer Token failed.", res);
        }
        token = new OAuth2Token(res);
        return token;
    }

    /**
     * Revokes an issued OAuth 2 Bearer Token.
     *
     * @throws TwitterException      when Twitter service or network is unavailable, or connecting non-SSL endpoints.
     * @throws IllegalStateException when Bearer token is not available.
     */
    public void invalidateOAuth2Token() throws TwitterException {
        if (token == null) {
            throw new IllegalStateException("OAuth 2 Bearer Token is not available.");
        }

        HttpParameter[] params = new HttpParameter[1];
        params[0] = new HttpParameter("access_token", token.getAccessToken());

        OAuth2Token _token = token;
        boolean succeed = false;

        try {
            token = null;

            HttpResponse res = http.post(oAuth2InvalidateTokenURL, params, this, null);
            if (res.getStatusCode() != 200) {
                throw new TwitterException("Invalidating OAuth 2 Bearer Token failed.", res);
            }

            succeed = true;

        } finally {
            if (!succeed) {
                token = _token;
            }
        }
    }

    @Override
    public String getAuthorizationHeader(HttpRequest req) {
        if (token == null) {
            String credentials;
            try {
                credentials =
                        URLEncoder.encode(consumerKey, "UTF-8")
                                + ":"
                                + URLEncoder.encode(consumerSecret, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            return "Basic " + BASE64Encoder.encode(credentials.getBytes());

        } else {
            return token.generateAuthorizationHeader();
        }
    }

    @Override
    public boolean isEnabled() {
        return token != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuth2Authorization that = (OAuth2Authorization) o;
        return Objects.equals(consumerKey, that.consumerKey) && Objects.equals(consumerSecret, that.consumerSecret) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumerKey, consumerSecret, token);
    }

    @Override
    public String toString() {
        return "OAuth2Authorization{" +
                "consumerKey='" + consumerKey + '\'' +
                ", consumerSecret='******************************************'" +
                ", token=" + ((token == null) ? "null" : token.toString()) +
                '}';
    }
}
