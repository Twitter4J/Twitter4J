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

package twitter4j.examples.user;

import twitter4j.ProfileImage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Gets specified user's profile image.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetProfileImage {
    /**
     * Usage: java twitter4j.examples.user.GetProfileImage [screen name] ['mini', 'normal' or 'bigger']
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.user.GetProfileImage [screen name] ['mini', 'normal' or 'bigger']");
            System.exit(-1);
        }
        ProfileImage.ImageSize imageSize = ProfileImage.NORMAL;
        if (args.length >= 2) {
            String sizeArg = args[1].toLowerCase();
            if ("mini".equals(sizeArg)) {
                imageSize = ProfileImage.MINI;
            } else if ("normal".equals(sizeArg)) {
                imageSize = ProfileImage.NORMAL;
            } else if ("bigger".equals(sizeArg)) {
                imageSize = ProfileImage.BIGGER;
            }
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            ProfileImage image = twitter.getProfileImage(args[0], imageSize);
            System.out.println(image.getURL());
            System.out.println("Successfully got profile image URL of [@" + args[0] + "].");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get profile image: " + te.getMessage());
            System.exit(-1);
        }
    }
}
