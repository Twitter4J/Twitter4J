package twitter4j.examples;

import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Example application that gets recent direct messages from specified account.<br>
 * Usage: java twitter4j.examples.GetDirectMessages ID Password
 */
public class GetDirectMessages {
    /**
     * Usage: java twitter4j.examples.GetDirectMessages ID Password
     * @param args String[]
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("No TwitterID/Password specified.");
            System.out.println(
                "Usage: java twitter4j.examples.GetDirectMessages ID Password");
            System.exit( -1);
        }
        Twitter twitter = new Twitter(args[0], args[1]);
        try {
            List<DirectMessage> messages = twitter.getDirectMessages();
            for (DirectMessage message : messages) {
                System.out.println("Sender:" + message.getSenderScreenName());
                System.out.println("Text:" + message.getText() + "\n");
            }
            System.exit(0);
        } catch (TwitterException te) {
            System.out.println("Failed to get messages: " + te.getMessage());
            System.exit( -1);
        }
    }
}
