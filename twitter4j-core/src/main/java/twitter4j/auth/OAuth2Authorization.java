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

import twitter4j.*;
import twitter4j.conf.Configuration;

import java.io.Serial;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author KOMIYA Atsushi - komiya.atsushi at gmail.com
 * @see <a href="https://dev.twitter.com/docs/auth/application-only-auth">Application-only authentication</a>
 */
public class OAuth2Authorization implements Authorization, java.io.Serializable, OAuth2Support {

    @Serial
    private static final long serialVersionUID = -2895232598422218647L;
    private final Configuration conf;

    private final HttpClient http;

    private String consumerKey;

    private String consumerSecret;

    private OAuth2Token token;

    public OAuth2Authorization(Configuration conf) {
        this.conf = conf;
        setOAuthConsumer(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret());
        http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
    }

    @Override
    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey != null ? consumerKey : "";
        this.consumerSecret = consumerSecret != null ? consumerSecret : "";
    }

    @Override
    public OAuth2Token getOAuth2Token() throws TwitterException {
        if (token != null) {
            throw new IllegalStateException("OAuth 2 Bearer Token is already available.");
        }

        HttpParameter[] params = new HttpParameter[conf.getOAuth2Scope() == null ? 1 : 2];
        params[0] = new HttpParameter("grant_type", "client_credentials");
        if (conf.getOAuth2Scope() != null) {
            params[1] = new HttpParameter("scope", conf.getOAuth2Scope());
        }

        HttpResponse res = http.post(conf.getOAuth2TokenURL(), params, this, null);
        if (res.getStatusCode() != 200) {
            throw new TwitterException("Obtaining OAuth 2 Bearer Token failed.", res);
        }
        token = new OAuth2Token(res);
        return token;
    }

    @Override
    public void setOAuth2Token(OAuth2Token oauth2Token) {
        this.token = oauth2Token;
    }

    @Override
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

            HttpResponse res = http.post(conf.getOAuth2InvalidateTokenURL(), params, this, null);
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
            String credentials = "";
            credentials =
                    URLEncoder.encode(consumerKey, StandardCharsets.UTF_8)
                            + ":"
                            + URLEncoder.encode(consumerSecret, StandardCharsets.UTF_8);

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
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof OAuth2Authorization that)) {
            return false;
        }

        if (!Objects.equals(consumerKey, that.consumerKey)) {
            return false;
        }
        if (!Objects.equals(consumerSecret, that.consumerSecret)) {
            return false;
        }
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        int result = consumerKey != null ? consumerKey.hashCode() : 0;
        result = 31 * result + (consumerSecret != null ? consumerSecret.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
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
