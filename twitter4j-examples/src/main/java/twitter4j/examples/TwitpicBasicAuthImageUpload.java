package twitter4j.examples;

import java.io.File;
import java.io.FileNotFoundException;

import twitter4j.TwitterException;
import twitter4j.http.BasicAuthorization;
import twitter4j.util.ImageUpload;

/**
 * Sample of the ImageUpload utility class. Uploads an image to Twitpic with BasicAuth credentials.
 *  Usage java twitter4j.examples.TwitpicBasicAuthImageUpload <user id> <password> <image file to upload>.
 *  
 */
public class TwitpicBasicAuthImageUpload
{
    public static void main (String [] args) throws TwitterException, FileNotFoundException
    {
        if (args.length != 3)
            throw new IllegalArgumentException ("Usage java twitter4j.examples.TwitpicBasicAuthImageUpload <user id> <password> <image file to upload>");
        
        File image = new File (args[2]);
        if (! image.exists ())
            throw new FileNotFoundException ("The image to upload " + image.getAbsolutePath () + " does not exist");
        
        ImageUpload upload = ImageUpload.getTwitpicUploader (new BasicAuthorization (args[0], args[1]));
        // note: if you wanted to use YFrog with basic auth you'd use ImageUpload.getYFrogUploader
        
        String url = upload.upload (image);
        
        System.out.println ("Successfully uploaded image to Twitpic at " + url);
    }
}
