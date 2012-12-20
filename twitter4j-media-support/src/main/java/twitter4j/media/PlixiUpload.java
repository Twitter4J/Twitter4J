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
class PlixiUpload extends AbstractImageUploadImpl {
// Described at http://groups.google.com/group/tweetphoto/web/multipart-form-data-upload
//  and http://groups.google.com/group/tweetphoto/web/oauth-echo

    public PlixiUpload(Configuration conf, String apiKey, OAuthAuthorization oauth) {
        super(conf, apiKey, oauth);
        this.uploadUrl = "http://api.plixi.com/api/upload.aspx";//"https://api.plixi.com/api/tpapi.svc/upload2";
    }

    @Override
    protected String postUpload() throws TwitterException {
        int statusCode = httpResponse.getStatusCode();
        if (statusCode != 201)
            throw new TwitterException("Plixi image upload returned invalid status code", httpResponse);

        String response = httpResponse.asString();

        if (response.contains("<Error><ErrorCode>")) {
            String error = response.substring(response.indexOf("<ErrorCode>") + "<ErrorCode>".length(), response.lastIndexOf("</ErrorCode>"));
            throw new TwitterException("Plixi image upload failed with this error message: " + error, httpResponse);
        }
        if (response.contains("<Status>OK</Status>")) {
            return response.substring(response.indexOf("<MediaUrl>") + "<MediaUrl>".length(), response.indexOf("</MediaUrl>"));
        }

        throw new TwitterException("Unknown Plixi response", httpResponse);
    }

    @Override
    protected void preUpload() throws TwitterException {
        String verifyCredentialsAuthorizationHeader = generateVerifyCredentialsAuthorizationHeader(TWITTER_VERIFY_CREDENTIALS_XML_V1);

        headers.put("X-Auth-Service-Provider", TWITTER_VERIFY_CREDENTIALS_XML_V1);
        headers.put("X-Verify-Credentials-Authorization", verifyCredentialsAuthorizationHeader);

        if (null == apiKey) {
            throw new IllegalStateException("No API Key for Plixi specified. put media.providerAPIKey in twitter4j.properties.");
        }
        HttpParameter[] params = {
                new HttpParameter("api_key", apiKey),
                this.image
        };
        if (message != null) {
            params = appendHttpParameters(new HttpParameter[]{
                    this.message}, params);
        }
        this.postParameter = params;
    }
}
