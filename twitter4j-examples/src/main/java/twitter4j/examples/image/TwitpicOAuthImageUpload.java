package twitter4j.examples.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import twitter4j.util.ImageUpload;

/**
 * Sample of the ImageUpload utility class. Uploads an image to Twitpic with OAuth credentials specified in a properties file.
 * Usage: java twitter4j.examples.TwitpicOAuthImageUpload <Twitpic OAuth API Key> <location of the twitter4j properties file> <image file to upload>.
 *
 * @author Rémy Rakic - remy.rakic at gmail.com
 * @since Twitter4J 2.1.3
 */
public class TwitpicOAuthImageUpload {
    public static void main(String[] args) throws TwitterException, FileNotFoundException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage java twitter4j.examples.TwitpicOAuthImageUpload <Twitpic OAuth API Key> <location of the twitter4j properties file with oauth properties> <image file to upload>");
        }

        String twitpicAPIKey = args[0];
        if (twitpicAPIKey == null || twitpicAPIKey.equals("")) {
            throw new IllegalArgumentException("The Twitpic OAuth API Key needs to be specified");
        }

        String propertiesFile = args[1];
        if (!new File(propertiesFile).exists()) {
            throw new FileNotFoundException("The twitter4j properties file " + propertiesFile + " can't be found");
        }

        File image = new File(args[2]);
        if (!image.exists()) {
            throw new FileNotFoundException("The image to upload " + image.getAbsolutePath() + " does not exist");
        }

        Configuration conf = new PropertyConfiguration(new FileInputStream(propertiesFile));

        OAuthAuthorization auth = new OAuthAuthorization(conf, conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret(),
                new AccessToken(conf.getOAuthAccessToken(), conf.getOAuthAccessTokenSecret()));

        ImageUpload upload = ImageUpload.getTwitpicUploader(twitpicAPIKey, auth);

        String url = upload.upload(image);

        System.out.println("Successfully uploaded image to Twitpic at " + url);
    }
}
