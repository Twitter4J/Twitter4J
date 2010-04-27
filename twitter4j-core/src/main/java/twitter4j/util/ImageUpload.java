package twitter4j.util;

import java.io.File;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpResponse;

public abstract class ImageUpload
{
    public abstract String upload (File image) throws TwitterException;
    
    /** Returns an image uploader to Twitpic. Only handles BasicAuth right now */
    public static ImageUpload getTwitpicUploader (Twitter twitter) throws TwitterException
    {
        return getTwitpicUploader (twitter.getAuthorization ());
    }
    
    /** Returns an image uploader to Twitpic. Only handles BasicAuth right now */
    public static ImageUpload getTwitpicUploader (Authorization auth)
    {
        ensureBasicEnabled (auth);
        return getTwitpicUploader ((BasicAuthorization) auth);
    }
    
    /** Returns a BasicAuth image uploader to Twitpic */
    public static ImageUpload getTwitpicUploader (BasicAuthorization auth)
    {
        return new TwitpicBasicAuthUploader (auth);
    }
    
    
    /** Returns an image uploader to YFrog. Handles both BasicAuth and OAuth */
    public static ImageUpload getYFrogUploader (Twitter twitter) throws TwitterException
    {
        Authorization auth = twitter.getAuthorization ();
        if (auth instanceof OAuthAuthorization)
            return getYFrogUploader (twitter.getScreenName (), (OAuthAuthorization) twitter.getAuthorization ());
        
        ensureBasicEnabled (auth);
        return getYFrogUploader ((BasicAuthorization) auth);
    }
    
    /** Returns a BasicAuth image uploader to YFrog */
    public static ImageUpload getYFrogUploader (String userId, String password)
    {
        return getYFrogUploader (new BasicAuthorization (userId, password));
    }
    
    /** Returns a BasicAuth image uploader to YFrog */
    public static ImageUpload getYFrogUploader (BasicAuthorization auth)
    {
        return new YFrogBasicAuthUploader (auth);
    }
    
    /** Returns an OAuth image uploader to YFrog */
    public static ImageUpload getYFrogUploader (String userId, OAuthAuthorization auth)
    {
        return new YFrogOAuthUploader (userId, auth);
    }
    
    private static void ensureBasicEnabled (Authorization auth)
    {
        if (!(auth instanceof BasicAuthorization)) {
            throw new IllegalStateException(
                    "user ID/password combination not supplied");
        }
    }
    
    private static class YFrogOAuthUploader extends ImageUpload
    {
        private String user;
        private OAuthAuthorization auth;
        
        // uses the secure upload URL, not the one specified in the YFrog FAQ
        private static final String YFROG_UPLOAD_URL = "https://yfrog.com/api/upload";
        private static final String TWITTER_VERIFY_CREDENTIALS = "https://api.twitter.com/1/account/verify_credentials.xml";
        
        public YFrogOAuthUploader (String user, OAuthAuthorization auth)
        {
            this.user = user;
            this.auth = auth;
        }

        @Override
        public String upload (File image) throws TwitterException
        {
            // step 1 - generate verification URL
            String signedVerifyCredentialsURL = generateSignedVerifyCredentialsURL ();
            
            // step 2 - generate HTTP parameters
            HttpParameter[] params =
            {
                new HttpParameter ("auth", "oauth"),
                new HttpParameter ("username", user),
                new HttpParameter ("verify_url", signedVerifyCredentialsURL),
                new HttpParameter ("media", image)
            };
            
            // step 3 - upload the file
            HttpClientWrapper client = new HttpClientWrapper ();
            HttpResponse httpResponse = client.post (YFROG_UPLOAD_URL, params);
            
            // step 4 - check the response
            int statusCode = httpResponse.getStatusCode ();
            if (statusCode != 200)
                throw new TwitterException ("YFrog image upload returned invalid status code", httpResponse);
            
            String response = httpResponse.asString ();
            if (response.contains ("<rsp stat=\"fail\">"))
            {
                String error = response.substring (response.indexOf ("msg") + 5, response.lastIndexOf ("\""));
                throw new TwitterException ("YFrog image upload failed with this error message: " + error, httpResponse);
            }
            
            if (response.contains ("<rsp stat=\"ok\">"))
            {
                String media = response.substring (response.indexOf ("<mediaurl>") + "<mediaurl>".length (), response.indexOf ("</mediaurl>"));
                return media;
            }
            
            throw new TwitterException ("Unknown YFrog response", httpResponse);
        }

        private String generateSignedVerifyCredentialsURL ()
        {
            List<HttpParameter> oauthSignatureParams = auth.generateOAuthSignatureHttpParams ("GET", TWITTER_VERIFY_CREDENTIALS);
            return TWITTER_VERIFY_CREDENTIALS + "?" + OAuthAuthorization.encodeParameters (oauthSignatureParams);
        }
    }
    
    private static class YFrogBasicAuthUploader extends ImageUpload
    {
        private BasicAuthorization auth;
        
        // uses the secure upload URL, not the one specified in the YFrog FAQ
        private static final String YFROG_UPLOAD_URL = "https://yfrog.com/api/upload";
        
        public YFrogBasicAuthUploader (BasicAuthorization auth)
        {
            this.auth = auth;
        }

        @Override
        public String upload (File image) throws TwitterException
        {
            // step 1 - generate HTTP parameters
            HttpParameter[] params =
            {
                new HttpParameter ("username", auth.getUserId ()),
                new HttpParameter ("password", auth.getPassword ()),
                new HttpParameter ("media", image)
            };
            
            // step 2 - upload the file
            HttpClientWrapper client = new HttpClientWrapper ();
            HttpResponse httpResponse = client.post (YFROG_UPLOAD_URL, params);
            
            // step 3 - check the response
            int statusCode = httpResponse.getStatusCode ();
            if (statusCode != 200)
                throw new TwitterException ("YFrog image upload returned invalid status code", httpResponse);
            
            String response = httpResponse.asString ();
            if (response.contains ("<rsp stat=\"fail\">"))
            {
                String error = response.substring (response.indexOf ("msg") + 5, response.lastIndexOf ("\""));
                throw new TwitterException ("YFrog image upload failed with this error message: " + error, httpResponse);
            }
            
            if (response.contains ("<rsp stat=\"ok\">"))
            {
                String media = response.substring (response.indexOf ("<mediaurl>") + "<mediaurl>".length (), response.indexOf ("</mediaurl>"));
                return media;
            }
            
            throw new TwitterException ("Unknown YFrog response", httpResponse);
        }
    }
    
    private static class TwitpicBasicAuthUploader extends ImageUpload
    {
        private BasicAuthorization auth;
        
        // uses the secure upload URL, not the one specified in the Twitpic FAQ
        private static final String TWITPIC_UPLOAD_URL = "https://twitpic.com/api/upload";
        
        public TwitpicBasicAuthUploader (BasicAuthorization auth)
        {
            this.auth = auth;
        }

        @Override
        public String upload (File image) throws TwitterException
        {
            // step 1 - generate HTTP parameters
            HttpParameter[] params =
            {
                new HttpParameter ("username", auth.getUserId ()),
                new HttpParameter ("password", auth.getPassword ()),
                new HttpParameter ("media", image)
            };
            
            // step 2 - upload the file
            HttpClientWrapper client = new HttpClientWrapper ();
            HttpResponse httpResponse = client.post (TWITPIC_UPLOAD_URL, params);
            
            // step 3 - check the response
            int statusCode = httpResponse.getStatusCode ();
            if (statusCode != 200)
                throw new TwitterException ("Twitpic image upload returned invalid status code", httpResponse);
            
            String response = httpResponse.asString ();
            if (response.contains ("<rsp stat=\"fail\">"))
            {
                String error = response.substring (response.indexOf ("msg") + 5, response.lastIndexOf ("\""));
                throw new TwitterException ("Twitpic image upload failed with this error message: " + error, httpResponse);
            }
            
            if (response.contains ("<rsp stat=\"ok\">"))
            {
                String media = response.substring (response.indexOf ("<mediaurl>") + "<mediaurl>".length (), response.indexOf ("</mediaurl>"));
                return media;
            }
            
            throw new TwitterException ("Unknown Twitpic response", httpResponse);
        }
    }
}
