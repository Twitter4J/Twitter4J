package twitter4j.examples;

import twitter4j.AsyncTwitter;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import static twitter4j.AsyncTwitter.*;

/**
 * <p>This is a code example of Twitter4J async API.<br>
 * Usage: java twitter4j.examples.AsyncUpdate <i>TwitterID</i> <i>TwitterPassword</i> <i>text</i><br>
 * </p>
 */
public class AsyncUpdate {
    /**
     * Main entry for this application.
     * @param args String[] TwitterID TwitterPassword StatusString
     * @throws InterruptedException
     */

    static Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 3) {
            System.out.println(
                "Usage: java twitter4j.examples.AsyncUpdate ID Password text");
            System.exit( -1);
        }
        AsyncTwitter twitter = new AsyncTwitter(args[0], args[1]);
        twitter.updateAsync(args[2], new TwitterAdapter() {
            @Override public void updated(Status status) {
                System.out.println("Successfully updated the status to [" +
                                   status.getText() + "].");
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override public void onException(TwitterException e, int method) {
                if (method == UPDATE) {
                    e.printStackTrace();
                    synchronized (lock) {
                        lock.notify();
                    }
                } else {
                    synchronized (lock) {
                        lock.notify();
                    }
                    throw new AssertionError("Should not happen");
                }
            }
        }
        );
        synchronized (lock) {
            lock.wait();
        }
    }

}
