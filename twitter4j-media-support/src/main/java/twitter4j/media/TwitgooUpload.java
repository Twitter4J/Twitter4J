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

package twitter4j.media;

import twitter4j.TwitterException;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpParameter;

/**
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @author withgod - noname at withgod.jp
 * @since Twitter4J 2.1.8
 */
class TwitgooUpload extends AbstractImageUploadImpl {

    public TwitgooUpload(Configuration conf, OAuthAuthorization oauth) {
        super(conf, oauth);
    }


    @Override
    protected String postUpload() throws TwitterException {
        int statusCode = httpResponse.getStatusCode();
        if (statusCode != 200)
            throw new TwitterException("Twitgoo image upload returned invalid status code", httpResponse);

        String response = httpResponse.asString();
        if (response.contains("<rsp status=\"ok\">")) {
            String h = "<mediaurl>";
            int i = response.indexOf(h);
            if (i != -1) {
                int j = response.indexOf("</mediaurl>", i + h.length());
                if (j != -1) {
                    return response.substring(i + h.length(), j);
                }
            }
        } else if (response.contains("<rsp status=\"fail\">")) {
            String h = "msg=\"";
            int i = response.indexOf(h);
            if (i != -1) {
                int j = response.indexOf("\"", i + h.length());
                if (j != -1) {
                    String msg = response.substring(i + h.length(), j);
                    throw new TwitterException("Invalid Twitgoo response: " + msg);
                }
            }
        }

        throw new TwitterException("Unknown Twitgoo response", httpResponse);
    }

    @Override
    protected void preUpload() throws TwitterException {
        uploadUrl = "http://twitgoo.com/api/uploadAndPost";
        String verifyCredentialsAuthorizationHeader = generateVerifyCredentialsAuthorizationHeader(TWITTER_VERIFY_CREDENTIALS_JSON_V1);

        headers.put("X-Auth-Service-Provider", TWITTER_VERIFY_CREDENTIALS_JSON_V1);
        headers.put("X-Verify-Credentials-Authorization", verifyCredentialsAuthorizationHeader);

        HttpParameter[] params = {
                new HttpParameter("no_twitter_post", "1"),
                this.image
        };
        if (message != null) {
            params = appendHttpParameters(new HttpParameter[]{
                    this.message
            }, params);
        }
        this.postParameter = params;
    }
}
