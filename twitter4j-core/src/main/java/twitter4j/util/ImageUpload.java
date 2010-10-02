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

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * @author Rémy Rakic - remy.rakic at gmail.com
 * @since Twitter4J 2.1.3
 */
public abstract class ImageUpload {
    public static String DEFAULT_TWITPIC_API_KEY = null;


    public abstract String upload(File image) throws TwitterException;

    public abstract String upload(File image, String message) throws TwitterException;

    public abstract String upload(String imageFileName, InputStream imageBody) throws TwitterException;

    public abstract String upload(String imageFileName, InputStream imageBody, String message) throws TwitterException;


    /**
     * Returns an image uploader to Twitpic. Handles both BasicAuth and OAuth.
     * Note: When using OAuth, the Twitpic API Key needs to be specified, either with the field ImageUpload.DEFAULT_TWITPIC_API_KEY,
     * or using the getTwitpicUploader (String twitpicAPIKey, OAuthAuthorization auth) method
     */
    public static ImageUpload getTwitpicUploader(Twitter twitter) throws TwitterException {
        Authorization auth = twitter.getAuthorization();
        if (auth instanceof OAuthAuthorization)
            return getTwitpicUploader(DEFAULT_TWITPIC_API_KEY, (OAuthAuthorization) auth);

        ensureBasicEnabled(auth);
        return getTwitpicUploader((BasicAuthorization) auth);
    }

    /**
     * Returns a BasicAuth image uploader to Twitpic
     */
    public static ImageUpload getTwitpicUploader(BasicAuthorization auth) {
        return new TwitpicBasicAuthUploader(auth);
    }

    /**
     * Returns an OAuth image uploader to Twitpic
     */
    public static ImageUpload getTwitpicUploader(String twitpicAPIKey, OAuthAuthorization auth) {
        return new TwitpicOAuthUploader(twitpicAPIKey, auth);
    }

    /**
     * Returns an OAuth image uploader to TweetPhoto
     */
    public static ImageUpload getTweetPhotoUploader(String tweetPhotoAPIKey, OAuthAuthorization auth) {
        return new TweetPhotoOAuthUploader(tweetPhotoAPIKey, auth);
    }

    /**
     * Returns an image uploader to YFrog. Handles both BasicAuth and OAuth
     */
    public static ImageUpload getYFrogUploader(Twitter twitter) throws TwitterException {
        Authorization auth = twitter.getAuthorization();
        if (auth instanceof OAuthAuthorization)
            return getYFrogUploader(twitter.getScreenName(), (OAuthAuthorization) auth);

        ensureBasicEnabled(auth);
        return getYFrogUploader((BasicAuthorization) auth);
    }

    /**
     * Returns a BasicAuth image uploader to YFrog
     */
    public static ImageUpload getYFrogUploader(BasicAuthorization auth) {
        return new YFrogBasicAuthUploader(auth);
    }

    /**
     * Returns an OAuth image uploader to YFrog
     */
    public static ImageUpload getYFrogUploader(String userId, OAuthAuthorization auth) {
        return new YFrogOAuthUploader(userId, auth);
    }

    /**
     * Returns an OAuth image uploader to img.ly
     */
    public static ImageUpload getImgLyUploader (OAuthAuthorization auth) {
        return new ImgLyOAuthUploader (auth);
    }

    /**
     * Returns an OAuth image uploader to Twitgoo
     */
    public static ImageUpload getTwitgooUploader(OAuthAuthorization auth) {
        return new TwitgooOAuthUploader (auth);
    }

    private static void ensureBasicEnabled(Authorization auth) {
        if (!(auth instanceof BasicAuthorization)) {
            throw new IllegalStateException(
                    "user ID/password combination not supplied");
        }
    }

    private static class YFrogOAuthUploader extends ImageUpload {
        private String user;
        private OAuthAuthorization auth;

        // uses the secure upload URL, not the one specified in the YFrog FAQ
        private static final String YFROG_UPLOAD_URL = "https://yfrog.com/api/upload";
        private static final String TWITTER_VERIFY_CREDENTIALS = "https://api.twitter.com/1/account/verify_credentials.xml";

        public YFrogOAuthUploader(String user, OAuthAuthorization auth) {
            this.user = user;
            this.auth = auth;
        }

        @Override
        public String upload(File image) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image)
            });
        }

        @Override
        public String upload(File image, String message) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image),
                    new HttpParameter("message", message)
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody)
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody, String message) throws TwitterException {
            try {
                message = URLEncoder.encode(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody),
                    new HttpParameter("message", message)
            });
        }

        private String upload(HttpParameter[] additionalParams) throws TwitterException {
            // step 1 - generate verification URL
            String signedVerifyCredentialsURL = generateSignedVerifyCredentialsURL();

            // step 2 - generate HTTP parameters
            HttpParameter[] params = {
                    new HttpParameter("auth", "oauth"),
                    new HttpParameter("username", user),
                    new HttpParameter("verify_url", signedVerifyCredentialsURL),
            };
            params = appendHttpParameters(params, additionalParams);

            // step 3 - upload the file
            HttpClientWrapper client = new HttpClientWrapper();
            HttpResponse httpResponse = client.post(YFROG_UPLOAD_URL, params);

            // step 4 - check the response
            int statusCode = httpResponse.getStatusCode();
            if (statusCode != 200) {
                throw new TwitterException("YFrog image upload returned invalid status code", httpResponse);
            }

            String response = httpResponse.asString();
            if (-1 != response.indexOf("<rsp stat=\"fail\">")) {
                String error = response.substring(response.indexOf("msg") + 5, response.lastIndexOf("\""));
                throw new TwitterException("YFrog image upload failed with this error message: " + error, httpResponse);
            }
            if (-1 != response.indexOf("<rsp stat=\"ok\">")) {
                String media = response.substring(response.indexOf("<mediaurl>") + "<mediaurl>".length(), response.indexOf("</mediaurl>"));
                return media;
            }

            throw new TwitterException("Unknown YFrog response", httpResponse);
        }

        private String generateSignedVerifyCredentialsURL() {
            List<HttpParameter> oauthSignatureParams = auth.generateOAuthSignatureHttpParams("GET", TWITTER_VERIFY_CREDENTIALS);
            return TWITTER_VERIFY_CREDENTIALS + "?" + OAuthAuthorization.encodeParameters(oauthSignatureParams);
        }
    }

    private static class YFrogBasicAuthUploader extends ImageUpload {
        private BasicAuthorization auth;

        // uses the secure upload URL, not the one specified in the YFrog FAQ
        private static final String YFROG_UPLOAD_URL = "https://yfrog.com/api/upload";

        public YFrogBasicAuthUploader(BasicAuthorization auth) {
            this.auth = auth;
        }

        @Override
        public String upload(File image) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image)
            });
        }

        @Override
        public String upload(File image, String message) throws TwitterException {
            // step 1 - generate HTTP parameters
            try {
                message = URLEncoder.encode(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image),
                    new HttpParameter("message", message)
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody),
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody, String message) throws TwitterException {
            try {
                message = URLEncoder.encode(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody),
                    new HttpParameter("message", message),
            });
        }

        private String upload(HttpParameter[] additionalParams) throws TwitterException {
            // step 1 - generate HTTP parameters
            HttpParameter[] params = {
                    new HttpParameter("username", auth.getUserId()),
                    new HttpParameter("password", auth.getPassword()),
            };
            params = appendHttpParameters(params, additionalParams);

            // step 2 - upload the file
            HttpClientWrapper client = new HttpClientWrapper();
            HttpResponse httpResponse = client.post(YFROG_UPLOAD_URL, params);

            // step 3 - check the response
            int statusCode = httpResponse.getStatusCode();
            if (statusCode != 200)
                throw new TwitterException("YFrog image upload returned invalid status code", httpResponse);

            String response = httpResponse.asString();
            if (-1 != response.indexOf("<rsp stat=\"fail\">")) {
                String error = response.substring(response.indexOf("msg") + 5, response.lastIndexOf("\""));
                throw new TwitterException("YFrog image upload failed with this error message: " + error, httpResponse);
            }

            if (-1 != response.indexOf("<rsp stat=\"ok\">")) {
                String media = response.substring(response.indexOf("<mediaurl>") + "<mediaurl>".length(), response.indexOf("</mediaurl>"));
                return media;
            }

            throw new TwitterException("Unknown YFrog response", httpResponse);
        }
    }

    // Described at http://dev.twitpic.com/docs/2/upload/

    private static class TwitpicOAuthUploader extends ImageUpload {
        private String twitpicAPIKey;
        private OAuthAuthorization auth;

        // uses the secure upload URL, not the one specified in the Twitpic FAQ
        private static final String TWITPIC_UPLOAD_URL = "https://twitpic.com/api/2/upload.json";
        private static final String TWITTER_VERIFY_CREDENTIALS = "https://api.twitter.com/1/account/verify_credentials.json";

        public TwitpicOAuthUploader(String twitpicAPIKey, OAuthAuthorization auth) {
            if (twitpicAPIKey == null || "".equals(twitpicAPIKey))
                throw new IllegalArgumentException("The Twitpic API Key supplied to the OAuth image uploader can't be null or empty");

            this.twitpicAPIKey = twitpicAPIKey;
            this.auth = auth;
        }

        @Override
        public String upload(File image) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image)
            });
        }

        @Override
        public String upload(File image, String message) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image),
                    new HttpParameter("message", message),
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody)
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody, String message) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody),
                    new HttpParameter("message", message)
            });
        }

        private String upload(HttpParameter[] additionalParams) throws TwitterException {
            // step 1 - generate HTTP request headers
            String verifyCredentialsAuthorizationHeader = generateVerifyCredentialsAuthorizationHeader();

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("X-Auth-Service-Provider", TWITTER_VERIFY_CREDENTIALS);
            headers.put("X-Verify-Credentials-Authorization", verifyCredentialsAuthorizationHeader);

            // step 2 - generate HTTP parameters
            HttpParameter[] params = {
                    new HttpParameter("key", twitpicAPIKey),
            };
            params = appendHttpParameters(params, additionalParams);

            // step 3 - upload the file
            HttpClientWrapper client = new HttpClientWrapper();
            HttpResponse httpResponse = client.post(TWITPIC_UPLOAD_URL, params, headers);

            // step 4 - check the response
            int statusCode = httpResponse.getStatusCode();
            if (statusCode != 200)
                throw new TwitterException("Twitpic image upload returned invalid status code", httpResponse);

            String response = httpResponse.asString();

            try {
                JSONObject json = new JSONObject(response);
                if (!json.isNull("url"))
                    return json.getString("url");
            }
            catch (JSONException e) {
                throw new TwitterException("Invalid Twitpic response: " + response, e);
            }

            throw new TwitterException("Unknown Twitpic response", httpResponse);
        }

        private String generateVerifyCredentialsAuthorizationHeader() {
            List<HttpParameter> oauthSignatureParams = auth.generateOAuthSignatureHttpParams("GET", TWITTER_VERIFY_CREDENTIALS);
            return "OAuth realm=\"http://api.twitter.com/\"," + OAuthAuthorization.encodeParameters(oauthSignatureParams, ",", true);
        }
    }

    private static class TwitpicBasicAuthUploader extends ImageUpload {
        private BasicAuthorization auth;

        // uses the secure upload URL, not the one specified in the Twitpic FAQ
        private static final String TWITPIC_UPLOAD_URL = "https://twitpic.com/api/upload";

        public TwitpicBasicAuthUploader(BasicAuthorization auth) {
            this.auth = auth;
        }

        @Override
        public String upload(File image) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image)
            });
        }

        @Override
        public String upload(File image, String message) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image),
                    new HttpParameter("message", message)
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody)
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody, String message) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody),
                    new HttpParameter("message", message)
            });
        }

        private String upload(HttpParameter[] additionalParams) throws TwitterException {
            // step 1 - generate HTTP parameters
            HttpParameter[] params = {
                    new HttpParameter("username", auth.getUserId()),
                    new HttpParameter("password", auth.getPassword()),
            };
            params = appendHttpParameters(params, additionalParams);

            // step 2 - upload the file
            HttpClientWrapper client = new HttpClientWrapper();
            HttpResponse httpResponse = client.post(TWITPIC_UPLOAD_URL, params);

            // step 3 - check the response
            int statusCode = httpResponse.getStatusCode();
            if (statusCode != 200)
                throw new TwitterException("Twitpic image upload returned invalid status code", httpResponse);

            String response = httpResponse.asString();
            if (-1 != response.indexOf("<rsp stat=\"fail\">")) {
                String error = response.substring(response.indexOf("msg") + 5, response.lastIndexOf("\""));
                throw new TwitterException("Twitpic image upload failed with this error message: " + error, httpResponse);
            }

            if (-1 != response.indexOf("<rsp stat=\"ok\">")) {
                String media = response.substring(response.indexOf("<mediaurl>") + "<mediaurl>".length(), response.indexOf("</mediaurl>"));
                return media;
            }

            throw new TwitterException("Unknown Twitpic response", httpResponse);
        }
    }

    // Described at http://groups.google.com/group/tweetphoto/web/multipart-form-data-upload
    //  and http://groups.google.com/group/tweetphoto/web/oauth-echo
    public static class TweetPhotoOAuthUploader extends ImageUpload
    {
        private String tweetPhotoAPIKey;
        private OAuthAuthorization auth;

        // uses the secure upload URL, not the one specified in the Twitpic FAQ
        private static final String TWEETPHOTO_UPLOAD_URL = "http://tweetphotoapi.com/api/upload.aspx";//"https://tweetphotoapi.com/api/tpapi.svc/upload2";
        private static final String TWITTER_VERIFY_CREDENTIALS = "https://api.twitter.com/1/account/verify_credentials.xml";

        public TweetPhotoOAuthUploader(String tweetPhotoAPIKey, OAuthAuthorization auth) {
            if (tweetPhotoAPIKey == null || "".equals(tweetPhotoAPIKey))
                throw new IllegalArgumentException("The TweetPhoto API Key supplied to the OAuth image uploader can't be null or empty");

            this.tweetPhotoAPIKey = tweetPhotoAPIKey;
            this.auth = auth;
        }

        @Override
        public String upload(File image) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image)
            });
        }

        @Override
        public String upload(File image, String message) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", image),
                    new HttpParameter("message", message)
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody),
            });
        }

        @Override
        public String upload(String imageFileName, InputStream imageBody, String message) throws TwitterException {
            return upload(new HttpParameter[]{
                    new HttpParameter("media", imageFileName, imageBody),
                    new HttpParameter("message", message)
            });
        }

        private String upload(HttpParameter[] additionalParams) throws TwitterException {
            // step 1 - generate HTTP request headers
            String verifyCredentialsAuthorizationHeader = generateVerifyCredentialsAuthorizationHeader();

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("X-Auth-Service-Provider", TWITTER_VERIFY_CREDENTIALS);
            headers.put("X-Verify-Credentials-Authorization", verifyCredentialsAuthorizationHeader);

            // step 2 - generate HTTP parameters
            HttpParameter[] params = {
                    new HttpParameter("api_key", tweetPhotoAPIKey),
            };
            params = appendHttpParameters(params, additionalParams);

            // step 3 - upload the file
            HttpClientWrapper client = new HttpClientWrapper();
            HttpResponse httpResponse = client.post(TWEETPHOTO_UPLOAD_URL, params, headers);

            // step 4 - check the response
            int statusCode = httpResponse.getStatusCode();
            if (statusCode != 201)
                throw new TwitterException("Twitpic image upload returned invalid status code", httpResponse);

            String response = httpResponse.asString();

            if (-1 != response.indexOf("<Error><ErrorCode>")) {
                String error = response.substring(response.indexOf("<ErrorCode>") + "<ErrorCode>".length(), response.lastIndexOf("</ErrorCode>"));
                throw new TwitterException("TweetPhoto image upload failed with this error message: " + error, httpResponse);
            }
            if (-1 != response.indexOf("<Status>OK</Status>")) {
                String media = response.substring(response.indexOf("<MediaUrl>") + "<MediaUrl>".length(), response.indexOf("</MediaUrl>"));
                return media;
            }

            throw new TwitterException("Unknown TweetPhoto response", httpResponse);
        }

        private String generateVerifyCredentialsAuthorizationHeader() {
            List<HttpParameter> oauthSignatureParams = auth.generateOAuthSignatureHttpParams("GET", TWITTER_VERIFY_CREDENTIALS);
            return "OAuth realm=\"http://api.twitter.com/\"," + OAuthAuthorization.encodeParameters(oauthSignatureParams, ",", true);
        }
    }

    // Described at http://dev.twitpic.com/docs/2/upload/
    public static class ImgLyOAuthUploader extends ImageUpload
    {
        private OAuthAuthorization auth;
        
        // uses the secure upload URL, not the one specified in the Twitpic FAQ
        private static final String IMGLY_UPLOAD_URL = "http://img.ly/api/2/upload.json";
        private static final String TWITTER_VERIFY_CREDENTIALS = "https://api.twitter.com/1/account/verify_credentials.json";
        
        public ImgLyOAuthUploader (OAuthAuthorization auth)
        {
            this.auth = auth;
        }

        @Override
        public String upload (File image) throws TwitterException
        {
            return upload(new HttpParameter[]{
                    new HttpParameter ("media", image)
                    });
        }

        @Override
        public String upload (File image, String message) throws TwitterException
        {
            return upload(new HttpParameter[]{
                    new HttpParameter ("media", image),
                    new HttpParameter ("message", message)
                    });
        }

        @Override
        public String upload (String imageFileName, InputStream imageBody) throws TwitterException
        {
            return upload(new HttpParameter[]{
                    new HttpParameter ("media", imageFileName, imageBody),
                    });
        }

        @Override
        public String upload (String imageFileName, InputStream imageBody, String message) throws TwitterException
        {
            return upload(new HttpParameter[]{
                    new HttpParameter ("media", imageFileName, imageBody),
                    new HttpParameter ("message", message)
                    });
        }

        private String upload (HttpParameter[] additionalParams) throws TwitterException
        {
            // step 1 - generate HTTP request headers
            String verifyCredentialsAuthorizationHeader = generateVerifyCredentialsAuthorizationHeader ();
            
            Map<String, String> headers = new HashMap<String, String>();
            headers.put ("X-Auth-Service-Provider", TWITTER_VERIFY_CREDENTIALS);
            headers.put ("X-Verify-Credentials-Authorization", verifyCredentialsAuthorizationHeader);
            
            // step 2 - generate HTTP parameters
            HttpParameter[] params = {
                    };
            params = appendHttpParameters(params, additionalParams);

            // step 3 - upload the file
            HttpClientWrapper client = new HttpClientWrapper ();
            HttpResponse httpResponse = client.post (IMGLY_UPLOAD_URL, params, headers);
            
            // step 4 - check the response
            int statusCode = httpResponse.getStatusCode ();
            if (statusCode != 200)
                throw new TwitterException ("ImgLy image upload returned invalid status code", httpResponse);
            
            String response = httpResponse.asString ();
            
            try
            {
                JSONObject json = new JSONObject (response);
                if (! json.isNull ("url"))
                    return json.getString ("url");
            }
            catch (JSONException e)
            {
                throw new TwitterException ("Invalid ImgLy response: " + response, e);
            }
            
            throw new TwitterException ("Unknown ImgLy response", httpResponse);
        }
        
        private String generateVerifyCredentialsAuthorizationHeader ()
        {
            List<HttpParameter> oauthSignatureParams = auth.generateOAuthSignatureHttpParams ("GET", TWITTER_VERIFY_CREDENTIALS);
            return "OAuth realm=\"http://api.twitter.com/\"," + OAuthAuthorization.encodeParameters (oauthSignatureParams, ",", true);
        }
    }
   
    public static class TwitgooOAuthUploader extends ImageUpload
    {
        private OAuthAuthorization auth;
        
        // uses the secure upload URL, not the one specified in the Twitpic FAQ
        private static final String TWITGOO_UPLOAD_URL = "http://twitgoo.com/api/uploadAndPost";
        private static final String TWITTER_VERIFY_CREDENTIALS = "https://api.twitter.com/1/account/verify_credentials.json";
        
        public TwitgooOAuthUploader(OAuthAuthorization auth)
        {
            this.auth = auth;
        }

        @Override
        public String upload (File image) throws TwitterException
        {
            return upload(new HttpParameter[]{
                    new HttpParameter ("media", image)
                    });
        }

        @Override
        public String upload (File image, String message) throws TwitterException
        {
            return upload(new HttpParameter[]{
                    new HttpParameter ("media", image),
                    new HttpParameter ("message", message)
                    });
        }

        @Override
        public String upload (String imageFileName, InputStream imageBody) throws TwitterException
        {
            return upload(new HttpParameter[]{
                    new HttpParameter ("media", imageFileName, imageBody),
                    });
        }

        @Override
        public String upload (String imageFileName, InputStream imageBody, String message) throws TwitterException
        {
            return upload(new HttpParameter[]{
                    new HttpParameter ("media", imageFileName, imageBody),
                    new HttpParameter ("message", message)
                    });
        }

        private String upload (HttpParameter[] additionalParams) throws TwitterException
        {
            // step 1 - generate HTTP request headers
            String verifyCredentialsAuthorizationHeader = generateVerifyCredentialsAuthorizationHeader ();
            
            Map<String, String> headers = new HashMap<String, String>();
            headers.put ("X-Auth-Service-Provider", TWITTER_VERIFY_CREDENTIALS);
            headers.put ("X-Verify-Credentials-Authorization", verifyCredentialsAuthorizationHeader);
            
            // step 2 - generate HTTP parameters
            HttpParameter[] params = {
            		new HttpParameter("no_twitter_post", "1")
                    };
            params = appendHttpParameters(params, additionalParams);

            // step 3 - upload the file
            HttpClientWrapper client = new HttpClientWrapper ();
            HttpResponse httpResponse = client.post (TWITGOO_UPLOAD_URL, params, headers);
            
            // step 4 - check the response
            int statusCode = httpResponse.getStatusCode ();
            if (statusCode != 200)
                throw new TwitterException ("Twitgoo image upload returned invalid status code", httpResponse);
            
            String response = httpResponse.asString ();
            if(-1 != response.indexOf("<rsp status=\"ok\">")){
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
            
            throw new TwitterException ("Unknown Twitgoo response", httpResponse);
        }
        
        private String generateVerifyCredentialsAuthorizationHeader ()
        {
            List<HttpParameter> oauthSignatureParams = auth.generateOAuthSignatureHttpParams ("GET", TWITTER_VERIFY_CREDENTIALS);
            return "OAuth realm=\"http://api.twitter.com/\"," + OAuthAuthorization.encodeParameters (oauthSignatureParams, ",", true);
        }
    }
   
    private static HttpParameter[] appendHttpParameters(HttpParameter[] src, HttpParameter[] dst) {
        int srcLen = src.length;
        int dstLen = dst.length;
        HttpParameter[] ret = new HttpParameter[srcLen + dstLen];
        for (int i = 0; i < srcLen; i++) {
            ret[i] = src[i];
        }
        for (int i = 0; i < dstLen; i++) {
            ret[srcLen + i] = dst[i];
        }
        return ret;
    }
}
