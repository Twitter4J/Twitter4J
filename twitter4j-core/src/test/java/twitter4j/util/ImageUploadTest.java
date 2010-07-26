/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.util;

import java.io.InputStream;

import twitter4j.TwitterTestBase;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;

public class ImageUploadTest extends TwitterTestBase {
    public ImageUploadTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        screenName = p.getProperty("id1");
        oauthAuthorization = new OAuthAuthorization(
                ConfigurationContext.getInstance()
                , p.getProperty("browserConsumerKey")
                , p.getProperty("browserConsumerSecret")
                , new AccessToken(
                        p.getProperty("id1.oauth_token")
                        , p.getProperty("id1.oauth_token_secret")
                )
        );
        twitpicApiKey = p.getProperty("twitpic.apiKey");
        tweetPhotoApiKey = p.getProperty("tweetPhoto.apiKey");
    }

    public void testTwitPicOAuthUploader() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            String url = ImageUpload.getTwitpicUploader(
                    twitpicApiKey, oauthAuthorization
            ).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testYFrogOAuthUploader() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            String url = ImageUpload.getYFrogUploader(
                    screenName, oauthAuthorization
            ).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testTweetPhotoOAuthUploader() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            String url = ImageUpload.getTweetPhotoUploader(
                    tweetPhotoApiKey, oauthAuthorization
            ).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    private OAuthAuthorization oauthAuthorization;
    private String screenName;
    private String twitpicApiKey;
    private String tweetPhotoApiKey;
    private String fileName = "t4j.jpeg";
    private String message = "Twitter4J画像アップロードテスト";
}
