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

import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;

/**
 * Example application that uploads multiple images.<br>
 *
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 */
public final class UploadMultipleImages {
    /**
     * Usage: java twitter4j.examples.tweets.UploadMultipleImages [text] [file1] [file2] ...
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.tweets.UploadMultipleImages [text] [file1] [file2] ...");
            System.exit(-1);
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            
            long[] mediaIds = new long[args.length-1];
            for (int i=1; i<args.length; i++) {
                System.out.println("Uploading...[" + i + "/" + (args.length-1) + "][" + args[i] + "]");
                UploadedMedia media = twitter.uploadMedia(new File(args[i]));
                System.out.println("Uploaded: id=" + media.getMediaId()
                        + ", w=" + media.getImageWidth() + ", h=" + media.getImageHeight()
                        + ", type=" + media.getImageType() + ", size=" + media.getSize());
                mediaIds[i-1] = media.getMediaId();
            }
            
            StatusUpdate update = new StatusUpdate(args[0]);
            update.setMediaIds(mediaIds);
            Status status = twitter.updateStatus(update);
            System.out.println("Successfully updated the status to [" + status.getText() + "][" + status.getId() + "].");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to update status: " + te.getMessage());
            System.exit(-1);
        }
    }
}
