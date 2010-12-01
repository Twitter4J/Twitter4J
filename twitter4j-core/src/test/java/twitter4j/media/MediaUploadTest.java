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
package twitter4j.media;

import java.io.InputStream;

import twitter4j.TwitterTestBase;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import twitter4j.media.impl.ImgLyOAuthUploader;
import twitter4j.media.impl.TweetPhotoOAuthUploader;
import twitter4j.media.impl.TwippleUploader;
import twitter4j.media.impl.TwitgooOAuthUploader;
import twitter4j.media.impl.TwitpicOAuthUploader;
import twitter4j.media.impl.YFrogOAuthUploader;

/**
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @author withgod - noname at withgod.jp
 * @since Twitter4J 2.1.8
 */
public class MediaUploadTest extends TwitterTestBase {
    public MediaUploadTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        oauthAuthorization = new OAuthAuthorization(
                ConfigurationContext.getInstance()
                , p.getProperty("desktopConsumerKey")
                , p.getProperty("desktopConsumerSecret")
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
            String url = new TwitpicOAuthUploader(twitpicApiKey, oauthAuthorization).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testYFrogOAuthUploader() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            String url = new YFrogOAuthUploader(oauthAuthorization).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testTweetPhotoOAuthUploader() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            String url = new TweetPhotoOAuthUploader(tweetPhotoApiKey, oauthAuthorization).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testImgLyOAuthUploader() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            String url = new ImgLyOAuthUploader(oauthAuthorization).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testTwitgooOAuthUploader() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            String url = new TwitgooOAuthUploader(oauthAuthorization).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testTwippleUploader() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            String url = new TwippleUploader(oauthAuthorization).upload(fileName, is);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    private OAuthAuthorization oauthAuthorization;
    private String twitpicApiKey;
    private String tweetPhotoApiKey;
    private String fileName = "t4j.jpeg";
    private String message = "Twitter4J image upload test";
}
