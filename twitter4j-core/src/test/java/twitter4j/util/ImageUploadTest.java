package twitter4j.util;

import java.io.InputStream;

import twitter4j.TwitterTestBase;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;

public class ImageUploadTest extends TwitterTestBase{
    public ImageUploadTest(String name){
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

    public void testTwitPicOAuthUploader() throws Exception{
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try{
            String url = ImageUpload.getTwitpicUploader(
                    twitpicApiKey, oauthAuthorization
                    ).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally{
            is.close();
        }
    }

    public void testYFrogOAuthUploader() throws Exception{
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try{
            String url = ImageUpload.getYFrogUploader(
                    screenName, oauthAuthorization
                    ).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally{
            is.close();
        }
    }

    public void testTweetPhotoOAuthUploader() throws Exception{
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try{
            String url = ImageUpload.getTweetPhotoUploader(
                    tweetPhotoApiKey, oauthAuthorization
                    ).upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally{
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
