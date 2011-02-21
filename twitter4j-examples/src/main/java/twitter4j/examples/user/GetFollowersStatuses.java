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

package twitter4j.examples.user;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 * Shows the specified user's followers.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetFollowersStatuses {
    /**
     * Usage: java twitter4j.examples.user.GetFollowersStatuses [screen name]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.user.GetFollowersStatuses [screen name]");
            System.exit(-1);
        }
        System.out.println("Showing @" + args[0] + "'s followers.");
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            long cursor = -1;
            PagableResponseList<User> users;
            do {
                users = twitter.getFollowersStatuses(args[0], cursor);
                for (User user : users) {
                    if (null != user.getStatus()) {
                        System.out.println("@" + user.getScreenName() + " - " + user.getStatus().getText());
                    } else {
                        // the user is protected
                        System.out.println("@" + user.getScreenName());
                    }
                }
            } while ((cursor = users.getNextCursor()) != 0);
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to show followers: " + te.getMessage());
            System.exit(-1);
        }
    }
}
