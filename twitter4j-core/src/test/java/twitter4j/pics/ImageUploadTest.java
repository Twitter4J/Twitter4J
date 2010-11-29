package twitter4j.pics;

import java.io.InputStream;

import twitter4j.TwitterTestBase;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import twitter4j.pics.impl.ImgLyOAuthUploader;
import twitter4j.pics.impl.TweetPhotoOAuthUploader;
import twitter4j.pics.impl.TwippleUploader;
import twitter4j.pics.impl.TwitgooOAuthUploader;
import twitter4j.pics.impl.TwitpicOAuthUploader;
import twitter4j.pics.impl.YFrogOAuthUploader;

public class ImageUploadTest extends TwitterTestBase {
    public ImageUploadTest(String name) {
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
