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
 * Show tweet timeline for members of the specified list.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetUserListStatuses {
    /**
     * Usage: java twitter4j.examples.list.GetUserListStatuses [list id]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.list.GetUserListStatuses [list id]");
            System.exit(-1);
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Paging page = new Paging(1);
            ResponseList<Status> statuses;
            do {
                statuses = twitter.getUserListStatuses(Integer.parseInt(args[0]), page);
                for (Status status : statuses) {
                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                }
                page.setPage(page.getPage() + 1);
            } while (statuses.size() > 0 && page.getPage() <= 10);
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to list statuses: " + te.getMessage());
            System.exit(-1);
        }
    }
}
