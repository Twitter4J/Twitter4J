/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
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
import twitter4j.TwitterException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
public interface TrendsResourcesAsync {
    /**
     * Returns the top 10 trending topics for a specific WOEID, if trending information is available for it.<br>
     * The response is an array of "trend" objects that encode the name of the trending topic, the query parameter that can be used to search for the topic on <a href="http://search.twitter.com/">Twitter Search</a>, and the Twitter Search URL.<br>
     * This information is cached for 5 minutes. Requesting more frequently than that will not return any more data, and will count against your rate limit usage.<br>
     * <br>This method calls http://api.twitter.com/1.1/trends/place.json
     *
     * @param woeid <a href="http://developer.yahoo.com/geo/geoplanet/">The Yahoo! Where On Earth ID</a> of the location to return trending information for. Global information is available by using 1 as the WOEID.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/trends/place">GET trends/place | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     * @deprecated use {@link #getPlaceTrends(int)} instead
     */
    void getLocationTrends(int woeid);

    /**
     * Returns the top 10 trending topics for a specific WOEID, if trending information is available for it.<br>
     * The response is an array of "trend" objects that encode the name of the trending topic, the query parameter that can be used to search for the topic on <a href="http://search.twitter.com/">Twitter Search</a>, and the Twitter Search URL.<br>
     * This information is cached for 5 minutes. Requesting more frequently than that will not return any more data, and will count against your rate limit usage.<br>
     * <br>This method calls http://api.twitter.com/1.1/trends/place.json
     *
     * @param woeid <a href="http://developer.yahoo.com/geo/geoplanet/">The Yahoo! Where On Earth ID</a> of the location to return trending information for. Global information is available by using 1 as the WOEID.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/trends/place">GET trends/place | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    void getPlaceTrends(int woeid);

    /**
     * Retrieves the locations that Twitter has trending topic information for. The response is an array of &quot;locations&quot; that encode the location's WOEID (a <a href="http://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>) and some other human-readable information such as a canonical name and country the location belongs in.
     * <br>This method calls http://api.twitter.com/1.1/trends/available.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/trends/available">GET trends/available | Twitter Developers</a>
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
     * @deprecated use {@link #getClosestTrends(twitter4j.GeoLocation)} instead
     */
    void getAvailableTrends(GeoLocation location);

    /**
     * Returns the locations that Twitter has trending topic information for, closest to a specified location.<br>
     * The response is an array of "locations" that encode the location's WOEID and some other human-readable information such as a canonical name and country the location belongs in.<br>
     * A WOEID is a <a href="http://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>.
     * <br>This method calls http://api.twitter.com/1.1/trends/closest.json
     *
     * @param location the available trend locations will be sorted by distance to the lat and long passed in. The sort is nearest to furthest.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/trends/closest">GET trends/closest | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    void getClosestTrends(GeoLocation location) throws TwitterException;
}
