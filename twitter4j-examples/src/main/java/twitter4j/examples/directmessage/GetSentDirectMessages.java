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

package twitter4j.examples.directmessage;

import twitter4j.*;

import java.util.List;

/**
 * Lists sent direct messages.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetSentDirectMessages {
    /**
     * Usage: java twitter4j.examples.directmessages.GetSentDirectMessages
     *
     * @param args message
     */
    public static void main(String[] args) {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Paging page = new Paging(1);
            List<DirectMessage> directMessages;
            do {
                directMessages = twitter.getSentDirectMessages(page);
                for (DirectMessage message : directMessages) {
                    System.out.println("To: @" + message.getRecipientScreenName() + " id:" + message.getId() + " - "
                            + message.getText());
                }
                page.setPage(page.getPage() + 1);
            } while (directMessages.size() > 0 && page.getPage() < 10);
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get sent messages: " + te.getMessage());
            System.exit(-1);
        }
    }
}
