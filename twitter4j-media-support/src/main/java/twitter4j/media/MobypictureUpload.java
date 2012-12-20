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
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * @author withgod - noname at withgod.jp
 * @see <a href="http://developers.mobypicture.com/documentation/">mobyapi documentation</a>
 * @since Twitter4J 2.1.12
 */
class MobypictureUpload extends AbstractImageUploadImpl {

    public MobypictureUpload(Configuration conf, String apiKey, OAuthAuthorization oauth) {
        super(conf, apiKey, oauth);
    }

    @Override
    protected String postUpload() throws TwitterException {
        int statusCode = httpResponse.getStatusCode();
        if (statusCode != 200)
            throw new TwitterException("Mobypic image upload returned invalid status code", httpResponse);

        String response = httpResponse.asString();

        try {
            JSONObject json = new JSONObject(response);
            if (!json.isNull("media")) {
                return json.getJSONObject("media").getString("mediaurl");
            }
        } catch (JSONException e) {
            throw new TwitterException("Invalid Mobypic response: " + response, e);
        }

        throw new TwitterException("Unknown Mobypic response", httpResponse);
    }

    @Override
    protected void preUpload() throws TwitterException {
        uploadUrl = "https://api.mobypicture.com/2.0/upload.json";
        String verifyCredentialsAuthorizationHeader = generateVerifyCredentialsAuthorizationHeader(TWITTER_VERIFY_CREDENTIALS_JSON_V1_1);

        headers.put("X-Auth-Service-Provider", TWITTER_VERIFY_CREDENTIALS_JSON_V1_1);
        headers.put("X-Verify-Credentials-Authorization", verifyCredentialsAuthorizationHeader);

        if (null == apiKey) {
            throw new IllegalStateException("No API Key for Mobypic specified. put media.providerAPIKey in twitter4j.properties.");
        }
        HttpParameter[] params = {
                new HttpParameter("key", apiKey),
                this.image};
        if (message != null) {
            params = appendHttpParameters(new HttpParameter[]{
                    this.message
            }, params);
        }
        this.postParameter = params;
    }
}
