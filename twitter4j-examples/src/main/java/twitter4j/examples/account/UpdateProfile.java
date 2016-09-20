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

package twitter4j.examples.account;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Updates profile.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class UpdateProfile {
    /**
     * Usage: java twitter4j.examples.account.UpdateProfile [name] [url] [location] [description]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java twitter4j.examples.account.UpdateProfile [name] [url] [location] [description]");
            System.exit(-1);
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            twitter.updateProfile(args[0], args[1], args[2], args[3]);
            System.out.println("Successfully updated profile.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to update profile: " + te.getMessage());
            System.exit(-1);
        }
    }
}
