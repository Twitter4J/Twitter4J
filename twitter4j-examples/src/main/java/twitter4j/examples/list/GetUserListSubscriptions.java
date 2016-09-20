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

package twitter4j.examples.list;

import twitter4j.*;

/**
 * List the lists the specified user follows.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetUserListSubscriptions {
    /**
     * Usage: java twitter4j.examples.list.GetUserListSubscriptions [screen name]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.list.GetUserListSubscriptions [screen name]");
            System.exit(-1);
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            long cursor = -1;
            PagableResponseList<UserList> lists;
            do {
                lists = twitter.getUserListSubscriptions(args[0], cursor);
                for (UserList list : lists) {
                    System.out.println("id:" + list.getId() + ", name:" + list.getName() + ", description:"
                            + list.getDescription() + ", slug:" + list.getSlug() + "");
                }
            } while ((cursor = lists.getNextCursor()) != 0);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to list the lists: " + te.getMessage());
            System.exit(-1);
        }
    }
}
