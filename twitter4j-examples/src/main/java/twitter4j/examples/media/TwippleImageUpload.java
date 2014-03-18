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

package twitter4j.examples.media;

import twitter4j.TwitterException;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;

import java.io.File;

/**
 * Sample of the ImageUpload interface. Uploads an image to Twipple with OAuth credentials specified in a properties file.
 *
 * @author Rémy Rakic - remy.rakic at gmail.com
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
final class TwippleImageUpload {
    /**
     * Usage: java twitter4j.examples.media.TwippleImageUpload [image file path] [message]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.media.TwippleImageUpload [image file path] [message]");
            System.exit(-1);
        }
        try {
            ImageUpload upload = new ImageUploadFactory().getInstance(MediaProvider.TWIPPLE);
            String url;
            if (args.length >= 2) {
                url = upload.upload(new File(args[0]), args[1]);
            } else {
                url = upload.upload(new File(args[0]));
            }
            System.out.println("Successfully uploaded image to Twipple at " + url);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to upload the image: " + te.getMessage());
            System.exit(-1);
        }
    }
}
