package twitter4j.examples;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Example application that sends a message to specified Twitter-er from specified account.<br>
 * Usage: java twitter4j.examples.DirectMessage senderID senderPassword message recipientId
 */
public class SendDirectMessage {
    /**
     * Usage: java twitter4j.examples.DirectMessage senderID senderPassword message recipientId
     * @param args String[]
     */
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("No TwitterID/Password specified.");
            System.out.println("Usage: java twitter4j.examples.DirectMessage senderID senderPassword message recipientId");
            System.exit( -1);
        }
        Twitter twitter = new Twitter(args[0], args[1]);
        try {
            DirectMessage message = twitter.sendDirectMessage(args[2], args[3]);
            System.out.println("Direct message successfully sent to " +
                               message.getRecipientScreenName());
            System.exit(0);
        } catch (TwitterException te) {
            System.out.println("Failed to send message: " + te.getMessage());
            System.exit( -1);
        }
    }
}
