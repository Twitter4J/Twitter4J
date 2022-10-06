package examples.trends;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Shows the trend for a place denoted by its WOEID. By default shows trends for "Worldwide" (WOEID 1).
 *
 * @author Mathias Kahl - mathias.kahl at gmail.com
 */
public final class GetPlaceTrends {
    /**
     * Usage: java twitter4j.examples.trends.GetPlaceTrends [WOEID=0]
     *
     * @param args message
     */
    public static void main(String[] args) {
        try {
            int woeid = args.length > 0 ? Integer.parseInt(args[0]) : 1;
            var twitter = Twitter.getInstance();
            Trends trends = twitter.trends().getPlaceTrends(woeid);

            System.out.println("Showing trends for " + trends.getLocation().getName());

            for (Trend trend : trends.getTrends()) {
                System.out.printf("%s (tweet_volume: %d)%n", trend.getName(), trend.getTweetVolume());
            }

            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get trends: " + te.getMessage());
            System.exit(-1);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            System.out.println("WOEID must be number");
            System.exit(-1);
        }
    }
}
