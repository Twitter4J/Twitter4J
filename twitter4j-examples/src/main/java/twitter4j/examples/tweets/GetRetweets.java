/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j.examples.tweets;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.List;

/**
 * Shows up to 100 of the first retweets of a given tweet.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetRetweets {
    /**
     * Usage: java twitter4j.examples.tweets.GetRetweets [status id]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.tweets.GetRetweets [status id]");
            System.exit(-1);
        }
        System.out.println("Showing up to 100 of the first retweets of the status id - [" + args[0] + "].");
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            List<Status> statuses = twitter.getRetweets(Long.parseLong(args[0]));
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get retweets: " + te.getMessage());
            System.exit(-1);
        }
    }
}
