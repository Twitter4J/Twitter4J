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
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Reply to a status.
 *
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 */
public final class ReplyStatus {
    /**
     * Usage: java twitter4j.examples.tweets.ReplyStatus [status id]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java twitter4j.examples.tweets.ReplyStatus [reply-to-status-id] [text]");
            System.exit(-1);
        }
        long replyToStatusId = Long.parseLong(args[0]);
        String text = args[1];
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Status status = twitter.showStatus(replyToStatusId);
            System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());

            StatusUpdate statusUpdate = new StatusUpdate(text);
            statusUpdate.setInReplyToStatusId(replyToStatusId);

            // comment-out if you need auto_populate_reply_metadata and exclude_reply_user_ids
//            statusUpdate.setAutoPopulateReplyMetadata(true);
//            statusUpdate.setExcludeReplyUserIds(8379212);   // exclude takke

            Status replyStatus = twitter.updateStatus(statusUpdate);
            System.out.println("Successfully updated the status to [" + replyStatus.getText() + "], inReplyToStatusId[" + replyStatus.getInReplyToStatusId() + "].");
            System.out.println(replyStatus);
            System.out.println(status);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to reply status: " + te.getMessage());
            System.exit(-1);
        }
    }
}
