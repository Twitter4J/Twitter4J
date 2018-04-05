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

import twitter4j.DirectMessageEvent;
import twitter4j.DirectMessageEventList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Example application that gets all direct messages via event api.<br>
 *
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 */
public class GetDirectMessageEvents {
    /**
     * Usage: java twitter4j.examples.directmessage.GetDirectMessageEvents
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            String cursor = null;
            int count = 20;
            DirectMessageEventList messages;
            do {
                System.out.println("* cursor:" + cursor);
                messages = cursor == null ? twitter.getDirectMessageEvents(count) : twitter.getDirectMessageEvents(count, cursor);
                for (DirectMessageEvent message : messages) {
                    System.out.println("From: " + message.getSenderId() + " id:" + message.getId()
                            + " [" + message.getCreatedTimestamp() + "]"
                            + " - " + message.getText());
                    System.out.println("raw[" + message + "]");
                }
                cursor = messages.getNextCursor();
            } while (messages.size() > 0 && cursor != null);
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get messages: " + te.getMessage());
            System.exit(-1);
        }
    }
}
