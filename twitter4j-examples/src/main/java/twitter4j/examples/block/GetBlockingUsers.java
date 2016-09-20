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

package twitter4j.examples.block;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import java.util.List;

/**
 * Lists blocking users.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetBlockingUsers {
    /**
     * Usage: java twitter4j.examples.block.GetBlockingUsers
     *
     * @param args message
     */
    public static void main(String[] args) {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            int page = 1;
            List<User> users;
            do {
                users = twitter.getBlocksList(page);
                for (User user : users) {
                    System.out.println("@" + user.getScreenName());
                }
                page++;
                // this code ends up in an infinite loop due to the issue 1988
                // http://code.google.com/p/twitter-api/issues/detail?id=1988
            } while (users.size() > 0 && page <= 10);
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get blocking users: " + te.getMessage());
            System.exit(-1);
        }
    }
}
