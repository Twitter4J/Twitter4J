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

package twitter4j.examples.friendsandfollowers;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Lists friends' ids
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetFriendsIDs {
    /**
     * Usage: java twitter4j.examples.friendsandfollowers.GetFriendsIDs [screen name]
     *
     * @param args message
     */
    public static void main(String[] args) {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            long cursor = -1;
            IDs ids;
            System.out.println("Listing following ids.");
            do {
                if (0 < args.length) {
                    ids = twitter.getFriendsIDs(args[0], cursor);
                } else {
                    ids = twitter.getFriendsIDs(cursor);
                }
                for (long id : ids.getIDs()) {
                    System.out.println(id);
                }
            } while ((cursor = ids.getNextCursor()) != 0);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get friends' ids: " + te.getMessage());
            System.exit(-1);
        }
    }
}
