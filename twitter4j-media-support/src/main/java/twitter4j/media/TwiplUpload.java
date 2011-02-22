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
 * @author withgod - noname at withgod.jp
 * @see <a href="http://www.twipl.net/api/doc">Twipl Open API</a>
 * @since Twitter4J 2.1.12
 */
class TwiplUpload extends AbstractImageUploadImpl {

    public TwiplUpload(Configuration conf, String apiKey, OAuthAuthorization oauth) {
        super(conf, apiKey, oauth);
    }


    @Override
    protected String postUpload() throws TwitterException {
        int statusCode = httpResponse.getStatusCode();
        if (statusCode != 200)
            throw new TwitterException("Twipl image upload returned invalid status code", httpResponse);

        String response = httpResponse.asString();
        if (-1 != response.indexOf("<response status=\"ok\">")) {
            String h = "<mediaurl>";
            int i = response.indexOf(h);
            if (i != -1) {
                int j = response.indexOf("</mediaurl>", i + h.length());
                if (j != -1) {
                    return response.substring(i + h.length(), j);
                }
            }
        } else if (-1 != response.indexOf("<rsp status=\"fail\">")) {
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

        throw new TwitterException("Unknown Twipl response", httpResponse);
    }

    @Override
    protected void preUpload() throws TwitterException {
        uploadUrl = "http://api.twipl.net/2/upload.xml";
        String verifyCredentialsAuthorizationHeader = generateVerifyCredentialsAuthorizationHeader(TWITTER_VERIFY_CREDENTIALS_XML);

        headers.put("X-OAUTH-AUTHORIZATION ", verifyCredentialsAuthorizationHeader);
        headers.put("X-OAUTH-SP-URL ", TWITTER_VERIFY_CREDENTIALS_XML);

        if (null == apiKey) {
            throw new IllegalStateException("No API Key for Twipl specified. put media.providerAPIKey in twitter4j.properties.");
        }
        String fname = this.image.getFile().getName();
        HttpParameter[] params = {
                new HttpParameter("key", apiKey),
                new HttpParameter("media1", fname, this.image.getFileBody())
        };
        if (message != null) {
            params = appendHttpParameters(new HttpParameter[]{
                    this.message
            }, params);
        }
        this.postParameter = params;
    }
}
