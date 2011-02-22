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

import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.http.HttpResponse;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 *         representing unauthorized Request Token which is passed to the service provider when acquiring the authorized Access Token
 */
public final class RequestToken extends OAuthToken implements java.io.Serializable {
    private final Configuration conf;
    private OAuthSupport oauth;
    private static final long serialVersionUID = -8214365845469757952L;

    RequestToken(HttpResponse res, OAuthSupport oauth) throws TwitterException {
        super(res);
        conf = ConfigurationContext.getInstance();
        this.oauth = oauth;
    }

    public RequestToken(String token, String tokenSecret) {
        super(token, tokenSecret);
        conf = ConfigurationContext.getInstance();
    }

    RequestToken(String token, String tokenSecret, OAuthSupport oauth) {
        super(token, tokenSecret);
        conf = ConfigurationContext.getInstance();
        this.oauth = oauth;
    }

    /**
     * @return authorization URL
     *         since Twitter4J 2.0.0
     */
    public String getAuthorizationURL() {
        return conf.getOAuthAuthorizationURL() + "?oauth_token=" + getToken();
    }

    /**
     * @return authentication URL
     *         since Twitter4J 2.0.10
     */
    public String getAuthenticationURL() {
        return conf.getOAuthAuthenticationURL() + "?oauth_token=" + getToken();
    }

}
