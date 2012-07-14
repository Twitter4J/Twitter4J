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

package twitter4j.examples.geo;

import twitter4j.*;

/**
 * Creates a place.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class CreatePlace {
    /**
     * Usage: java twitter4j.examples.geo.CreatePlace [name] [contained within] [token] [lat] [long] [street address]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 5) {
            System.out.println("Usage: java twitter4j.examples.geo.CreatePlace [name] [contained within] [token] [lat] [long] [street address]");
            System.exit(-1);
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            String name = args[0];
            String containedWithin = args[1];
            String token = args[2];
            GeoLocation location = new GeoLocation(Double.parseDouble(args[3]), Double.parseDouble(args[4]));
            String streetAddress = null;
            if (args.length >= 6) {
                streetAddress = args[5];
            }
            Place place = twitter.createPlace(name, containedWithin, token, location, streetAddress);
            System.out.println("Successfully created a place [" + place.toString() + "].");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to create a place: " + te.getMessage());
            System.exit(-1);
        }
    }
}
