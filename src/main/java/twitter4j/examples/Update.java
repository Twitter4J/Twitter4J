package twitter4j.examples;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
/**
 * <p>This is a code example of Twitter4J update API.<br>
 * Usage: java twitter4j.examples.Update <i>TwitterID</i> <i>TwitterPassword</i> <i>text</i><br>
 * </p>
 */
public class Update {
    /**
     * Main entry for this application.
     * @param args String[] TwitterID TwitterPassword StatusString
     */

    public static void main(String[] args)throws TwitterException{
        if (args.length < 3) {
            System.out.println(
                "Usage: java twitter4j.examples.Update ID Password text");
            System.exit( -1);
        }
        Twitter twitter = new Twitter(args[0], args[1]);
        Status status = twitter.update(args[2]);
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
    }
}
