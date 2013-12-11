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

package twitter4j.examples.friendship;

import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Shows friendship between two users.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class ShowFriendship {
    /**
     * Usage: java twitter4j.examples.friendship.ShowFriendship  [source screen name] [target screen name]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java twitter4j.examples.friendship.ShowFriendship [source screen name] [target screen name]");
            System.exit(-1);
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Relationship relationship = twitter.showFriendship(args[0], args[1]);
            System.out.println("isSourceBlockingTarget: " + relationship.isSourceBlockingTarget());
            System.out.println("isSourceFollowedByTarget: " + relationship.isSourceFollowedByTarget());
            System.out.println("isSourceFollowingByTarget: " + relationship.isSourceFollowingTarget());
            System.out.println("isSourceNotificationsEnabled: " + relationship.isSourceNotificationsEnabled());
            System.out.println("canSourceDm: " + relationship.canSourceDm());
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to show friendship: " + te.getMessage());
            System.exit(-1);
        }
    }
}
