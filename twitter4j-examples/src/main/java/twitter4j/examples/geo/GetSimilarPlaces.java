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
 * Locates places near the given coordinates which are similar in name.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetSimilarPlaces {
    /**
     * Usage: java twitter4j.examples.geo.GetSimilarPlaces [latitude] [longitude] [place id]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java twitter4j.examples.geo.GetSimilarPlaces [latitude] [longitude] [name] [place id]");
            System.exit(-1);
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            GeoLocation location = new GeoLocation(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
            String name = args[2];
            String containedWithin = null;
            if (args.length >= 4) {
                containedWithin = args[3];
            }
            SimilarPlaces places = twitter.getSimilarPlaces(location, name, containedWithin, null);
            System.out.println("token: " + places.getToken());
            if (places.size() == 0) {
                System.out.println("No location associated with the specified condition");
            } else {
                for (Place place : places) {
                    System.out.println("id: " + place.getId() + " name: " + place.getFullName() + " name: " + place.getFullName());
                    Place[] containedWithinArray = place.getContainedWithIn();
                    if (containedWithinArray != null && containedWithinArray.length != 0) {
                        System.out.println("  contained within:");
                        for (Place containedWithinPlace : containedWithinArray) {
                            System.out.println("  id: " + containedWithinPlace.getId() + " name: " + containedWithinPlace.getFullName());
                        }
                    }
                }
            }
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to find similar places: " + te.getMessage());
            System.exit(-1);
        }
    }
}
