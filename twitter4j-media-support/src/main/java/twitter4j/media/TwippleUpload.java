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

import twitter4j.HttpParameter;
import twitter4j.TwitterException;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;

/**
 * @author withgod - noname at withgod.jp
 * @since Twitter4J 2.1.8
 */
class TwippleUpload extends AbstractImageUploadImpl {

    public TwippleUpload(Configuration conf, OAuthAuthorization oauth) {
        super(conf, oauth);
    }

    @Override
    protected String postUpload() throws TwitterException {
        int statusCode = httpResponse.getStatusCode();
        if (statusCode != 200) {
            throw new TwitterException("Twipple image upload returned invalid status code", httpResponse);
        }

        String response = httpResponse.asString();
        if (response.contains("<rsp stat=\"fail\">")) {
            String error = response.substring(response.indexOf("msg") + 5, response.lastIndexOf("\""));
            throw new TwitterException("Twipple image upload failed with this error message: " + error, httpResponse);
        }
        if (response.contains("<rsp stat=\"ok\">")) {
            return response.substring(response.indexOf("<mediaurl>") + "<mediaurl>".length(), response.indexOf("</mediaurl>"));
        }

        throw new TwitterException("Unknown Twipple response", httpResponse);
    }

    @Override
    protected void preUpload() throws TwitterException {
        uploadUrl = "http://p.twipple.jp/api/upload";
        String signedVerifyCredentialsURL = generateVerifyCredentialsAuthorizationURL(TWITTER_VERIFY_CREDENTIALS_JSON_V1_1);

        this.postParameter = new HttpParameter[]{
                new HttpParameter("verify_url", signedVerifyCredentialsURL),
                this.image};
    }
}
