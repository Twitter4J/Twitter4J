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
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static twitter4j.internal.json.z_T4JInternalParseUtil.getRawString;

public class OAuth2Token implements java.io.Serializable {
    private static final long serialVersionUID = 358222644448390610L;

    private String tokenType;

    private String accessToken;

    OAuth2Token(HttpResponse res) throws TwitterException {
        JSONObject json = res.asJSONObject();
        tokenType = getRawString("token_type", json);
        try {
            accessToken = URLDecoder.decode(getRawString("access_token", json), "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
    }

    public OAuth2Token(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    /*package*/ String generateAuthorizationHeader() {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(accessToken, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        return "Bearer " + encoded;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof OAuth2Token)) {
            return false;
        }

        OAuth2Token that = (OAuth2Token) obj;
        if (tokenType != null ? !tokenType.equals(that.tokenType) : that.tokenType != null) {
            return false;
        }
        if (accessToken != null ? !accessToken.equals(that.accessToken) : that.accessToken != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = tokenType != null ? tokenType.hashCode() : 0;
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OAuth2Token{" +
                "tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}