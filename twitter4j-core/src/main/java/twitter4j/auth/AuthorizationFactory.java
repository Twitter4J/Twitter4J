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

import twitter4j.conf.Configuration;

/**
 * A static factory class for Authorization.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public final class AuthorizationFactory {
    /**
     * @param conf configuration
     * @return authorization instance
     * @since Twitter4J 2.1.11
     */
    public static Authorization getInstance(Configuration conf) {
        Authorization auth = null;
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();

        if (consumerKey != null && consumerSecret != null) {
            if (conf.isApplicationOnlyAuthEnabled()) {
                OAuth2Authorization oauth2 = new OAuth2Authorization(conf);
                String tokenType = conf.getOAuth2TokenType();
                String accessToken = conf.getOAuth2AccessToken();
                if (tokenType != null && accessToken != null) {
                    oauth2.setOAuth2Token(new OAuth2Token(tokenType, accessToken));
                }
                auth = oauth2;

            } else {
                OAuthAuthorization oauth;
                oauth = new OAuthAuthorization(conf);
                String accessToken = conf.getOAuthAccessToken();
                String accessTokenSecret = conf.getOAuthAccessTokenSecret();
                if (accessToken != null && accessTokenSecret != null) {
                    oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                auth = oauth;
            }
        } else {
            String screenName = conf.getUser();
            String password = conf.getPassword();
            if (screenName != null && password != null) {
                auth = new BasicAuthorization(screenName, password);
            }
        }
        if (null == auth) {
            auth = NullAuthorization.getInstance();
        }
        return auth;
    }
}
