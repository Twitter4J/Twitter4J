package twitter4j.examples.trends;

import twitter4j.*;

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
            Twitter twitter = new TwitterFactory().getInstance();
            Trends trends = twitter.getPlaceTrends(woeid);

            System.out.println("Showing trends for " + trends.getLocation().getName());

            for (Trend trend : trends.getTrends()) {
                System.out.println(String.format("%s (tweet_volume: %d)", trend.getName(), trend.getTweetVolume()));
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
