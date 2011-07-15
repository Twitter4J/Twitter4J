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

package twitter4j.api;

import twitter4j.GeoLocation;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface LocalTrendsMethodsAsync {
    /**
     * Retrieves the locations that Twitter has trending topic information for. The response is an array of &quot;locations&quot; that encode the location's WOEID (a <a href="http://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>) and some other human-readable information such as a canonical name and country the location belongs in.
     * <br>This method calls http://api.twitter.com/1/trends/available.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/trends/available">GET trends/available | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getAvailableTrends();

    /**
     * Retrieves the sorted locations that Twitter has trending topic information for. The response is an array of &quot;locations&quot; that encode the location's WOEID (a <a href="http://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>) and some other human-readable information such as a canonical name and country the location belongs in.
     * <br>This method calls http://api.twitter.com/1/trends/available.json
     *
     * @param location the available trend locations will be sorted by distance to the lat and long passed in. The sort is nearest to furthest.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/trends/available">GET trends/available | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getAvailableTrends(GeoLocation location);

    /**
     * Retrieves the top 10 trending topics for a specific location Twitter has trending topic information for. The response is an array of "trend" objects that encode the name of the trending topic, the query parameter that can be used to search for the topic on Search, and the direct URL that can be issued against Search. This information is cached for five minutes, and therefore users are discouraged from querying these endpoints faster than once every five minutes.  Global trends information is also available from this API by using a WOEID of 1.
     * <br>This method calls http://api.twitter.com/1/trends/:woeid.json
     *
     * @param woeid The WOEID of the location to be querying for
     * @see <a href="https://dev.twitter.com/docs/api/1/get/trends/:woeid">GET trends/:woeid | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getLocationTrends(int woeid);
}
