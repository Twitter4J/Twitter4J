package twitter4j.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.util.ImageUpload;

/**
 * Sample of the ImageUpload utility class. Uploads an image to YFrog with OAuth credentials specified in a properties file.
 *  Usage: java twitter4j.examples.YFrogOAuthImageUpload <location of the twitter4j properties file> <image file to upload>.
 */
public class YFrogOAuthImageUpload
{
    public static void main (String [] args) throws TwitterException, FileNotFoundException
    {
        if (args.length != 2)
            throw new IllegalArgumentException ("Usage java twitter4j.examples.YFrogOAuthImageUpload <location of the twitter4j properties file with oauth properties> <image file to upload>");
        
        String propertiesFile = args[0];
        if (! new File (propertiesFile).exists ())
            throw new FileNotFoundException ("The twitter4j properties file " + propertiesFile + " can't be found");
        
        File image = new File (args[1]);
        if (! image.exists ())
            throw new FileNotFoundException ("The image to upload " + image.getAbsolutePath () + " does not exist");
        
        Configuration conf = new PropertyConfiguration (new FileInputStream (propertiesFile));
        
        Twitter twitter = new TwitterFactory().getOAuthAuthorizedInstance (conf.getOAuthConsumerKey (), conf.getOAuthConsumerSecret (),
                new AccessToken (conf.getOAuthAccessToken (), conf.getOAuthAccessTokenSecret ()));
        
        ImageUpload upload = ImageUpload.getYFrogUploader (twitter);
        
        String url = upload.upload (image);
        
        System.out.println ("Successfully uploaded image to YFrog at " + url);
    }
}
