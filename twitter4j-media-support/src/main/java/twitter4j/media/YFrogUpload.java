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
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;

/**
 * @author Rémy Rakic - remy.rakic at gmail.com
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @author withgod - noname at withgod.jp
 * @since Twitter4J 2.1.8
 */
class YFrogUpload extends AbstractImageUploadImpl {

    public YFrogUpload(Configuration conf, OAuthAuthorization oauth) {
        super(conf, oauth);
    }

    @Override
    protected String postUpload() throws TwitterException {
        int statusCode = httpResponse.getStatusCode();
        if (statusCode != 200) {
            throw new TwitterException("YFrog image upload returned invalid status code", httpResponse);
        }

        String response = httpResponse.asString();
        if (response.contains("<rsp stat=\"fail\">")) {
            String error = response.substring(response.indexOf("msg") + 5, response.lastIndexOf("\""));
            throw new TwitterException("YFrog image upload failed with this error message: " + error, httpResponse);
        }
        if (response.contains("<rsp stat=\"ok\">")) {
            return response.substring(response.indexOf("<mediaurl>") + "<mediaurl>".length(), response.indexOf("</mediaurl>"));
        }

        throw new TwitterException("Unknown YFrog response", httpResponse);
    }

    @Override
    protected void preUpload() throws TwitterException {
        uploadUrl = "https://yfrog.com/api/xauth_upload";
        String signedVerifyCredentialsURL = generateVerifyCredentialsAuthorizationURL("https://api.twitter.com/1.1/account/verify_credentials.xml");
        Twitter tw = new TwitterFactory().getInstance(this.oauth);

        HttpParameter[] params = {
                new HttpParameter("auth", "oauth"),
                new HttpParameter("username", tw.verifyCredentials().getScreenName()),
                new HttpParameter("verify_url", signedVerifyCredentialsURL),
                this.image,
        };
        if (message != null) {
            params = appendHttpParameters(new HttpParameter[]{
                    this.message
            }, params);
        }
        this.postParameter = params;
    }
}
