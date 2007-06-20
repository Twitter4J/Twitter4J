package twitter4j.examples;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Example application that gets public, user and friend timeline using specified account.<br>
 * Usage: java twitter4j.examples.GetTimelines ID Password
 */
public class GetTimelines {
    /**
     * Usage: java twitter4j.examples.GetTimelines ID Password
     * @param args String[]
     */
    public static void main(String[] args) {

        Twitter unauthenticatedTwitter = new Twitter();
        System.out.println("Showing public timeline.");
        try {
            List<Status> statuses = unauthenticatedTwitter.getPublicTimeline();
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":" +
                                   status.getText());
            }
            if (args.length < 2) {
                System.out.println(
                    "You need to specify TwitterID/Password combination to show UserTimelines.");
                System.out.println(
                    "Usage: java twitter4j.examples.GetTimelines ID Password");
                System.exit(0);
            }

            // Other methods require authentication
            Twitter twitter = new Twitter(args[0], args[1]);
            statuses = twitter.getFriendsTimeline();
            System.out.println("------------------------------");
            System.out.println("Showing " + args[0] + "'s friends timeline.");
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":" +
                                   status.getText());
            }
            statuses = twitter.getUserTimeline();
            System.out.println("------------------------------");
            System.out.println("Showing " + args[0] + "'s timeline.");
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":" +
                                   status.getText());
            }
            Status status = twitter.show(81642112);
            System.out.println("------------------------------");
            System.out.println("Showing " + status.getUser().getName() +
                               "'s status updated at " + status.getCreatedAt());
            System.out.println(status.getText());
            System.exit(0);
        } catch (TwitterException te) {
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit( -1);
        }
    }
}
