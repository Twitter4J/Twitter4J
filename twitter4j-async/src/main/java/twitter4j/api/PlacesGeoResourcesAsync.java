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
import twitter4j.GeoQuery;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public interface PlacesGeoResourcesAsync {
    /**
     * Find out more details of a place that was returned from the {@link PlacesGeoResourcesAsync#reverseGeoCode(twitter4j.GeoQuery)} method.
     * <br>This method calls https://api.twitter.com/1.1/geo/id/:id.json
     *
     * @param id The ID of the location to query about.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/geo/id/:place_id">GET geo/id/:place_id | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getGeoDetails(String id);

    /**
     * Locates places near the given coordinates which are similar in name.
     * <br>Conceptually you would use this method to get a list of known places to choose from first. Then, if the desired place doesn't exist, make a request to post/geo/place to create a new one.
     * <br>The token contained in the response is the token needed to be able to create a new place.
     * <br>This method calls https://api.twitter.com/1.1/geo/similar_places.json
     *
     * @param location        The latitude and longitude to search around.
     * @param name            The name a place is known as.
     * @param containedWithin optional: the place_id which you would like to restrict the search results to. Setting this value means only places within the given place_id will be found.
     * @param streetAddress   optional: This parameter searches for places which have this given street address. There are other well-known, and application specific attributes available. Custom attributes are also permitted. Learn more about Place Attributes.
     * @since Twitter4J 2.1.7
     */
    void getSimilarPlaces(GeoLocation location, String name, String containedWithin, String streetAddress);

    /**
     * Search for places (cities and neighborhoods) that can be attached to a statuses/update. Given a latitude and a longitude, return a list of all the valid places that can be used as a place_id when updating a status. Conceptually, a query can be made from the user's location, retrieve a list of places, have the user validate the location he or she is at, and then send the ID of this location up with a call to statuses/update.<br>
     * There are multiple granularities of places that can be returned -- "neighborhoods", "cities", etc. At this time, only United States data is available through this method.<br>
     * This API call is meant to be an informative call and will deliver generalized results about geography.
     * <br>This method calls https://api.twitter.com/1.1/geo/reverse_geocode.json
     *
     * @param query search query
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/geo/reverse_geocode">GET geo/reverse_geocode | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void reverseGeoCode(GeoQuery query);

    /**
     * Search for places that can be attached to a statuses/update. Given a latitude and a longitude pair, an IP address, or a name, this request will return a list of all the valid places that can be used as the place_id when updating a status.
     * <br>Conceptually, a query can be made from the user's location, retrieve a list of places, have the user validate the location he or she is at, and then send the ID of this location with a call to statuses/update.
     * <br>This is the recommended method to use find places that can be attached to statuses/update. Unlike geo/reverse_geocode which provides raw data access, this endpoint can potentially re-order places with regards to the user who is authenticated. This approach is also preferred for interactive place matching with the user.
     * <br>This method calls https://api.twitter.com/1.1/geo/search.json
     *
     * @param query search query
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/geo/search">GET geo/search | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    void searchPlaces(GeoQuery query);
}
