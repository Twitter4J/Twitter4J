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
        if (-1 != response.indexOf("<rsp stat=\"fail\">")) {
            String error = response.substring(response.indexOf("msg") + 5, response.lastIndexOf("\""));
            throw new TwitterException("Twipple image upload failed with this error message: " + error, httpResponse);
        }
        if (-1 != response.indexOf("<rsp stat=\"ok\">")) {
            return response.substring(response.indexOf("<mediaurl>") + "<mediaurl>".length(), response.indexOf("</mediaurl>"));
        }

        throw new TwitterException("Unknown Twipple response", httpResponse);
    }

    @Override
    protected void preUpload() throws TwitterException {
        uploadUrl = "http://p.twipple.jp/api/upload";
        String signedVerifyCredentialsURL = generateVerifyCredentialsAuthorizationURL(TWITTER_VERIFY_CREDENTIALS_XML);

        HttpParameter[] params = {
                new HttpParameter("verify_url", signedVerifyCredentialsURL),
                this.image};
        this.postParameter = params;
    }
}
