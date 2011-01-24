/*
Copyright (c) 2007-2011, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.media;

import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.http.OAuthAuthorization;
import twitter4j.internal.http.HttpParameter;

/**
 * @see <a href="http://www.twipl.net/api/doc">Twipl Open API</a>
 * @author withgod - noname at withgod.jp
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
        if(-1 != response.indexOf("<response status=\"ok\">")){
            String h = "<mediaurl>";
            int i = response.indexOf(h);
            if(i != -1){
                int j = response.indexOf("</mediaurl>", i + h.length());
                if(j != -1){
                    return response.substring(i + h.length(), j);
                }
            }
        } else if(-1 != response.indexOf("<rsp status=\"fail\">")){
            String h = "msg=\"";
            int i = response.indexOf(h);
            if(i != -1){
                int j = response.indexOf("\"", i + h.length());
                if(j != -1){
                    String msg = response.substring(i + h.length(), j);
                    throw new TwitterException ("Invalid Twitgoo response: " + msg);
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
