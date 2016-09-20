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

package twitter4j.examples.suggestedusers;

import twitter4j.*;

/**
 * Shows suggested users in specified category.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetUserSuggestions {
    /**
     * Usage: java twitter4j.examples.suggestedusers.GetUserSuggestions [slug]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.suggestedusers.GetUserSuggestions [slug]");
            System.exit(-1);
        }
        System.out.println("Showing suggested users in " + args[0] + " category.");
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            ResponseList<User> users = twitter.getUserSuggestions(args[0]);
            for (User user : users) {
                if (user.getStatus() != null) {
                    System.out.println("@" + user.getScreenName() + " - " + user.getStatus().getText());
                } else {
                    // the user is protected
                    System.out.println("@" + user.getScreenName());
                }
            }
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get suggested users: " + te.getMessage());
            System.exit(-1);
        }
    }
}
