/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.api;

import twitter4j.GeoQuery;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.TwitterException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public interface GeoMethods {
    /**
     * Search for places (cities and neighborhoods) that can be attached to a <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">statuses/update</a>.  Given a latitude and a longitude pair, or an IP address, return a list of all the valid cities and neighborhoods that can be used as a place_id when updating a status.  Conceptually, a query can be made from the user's location, retrieve a list of places, have the user validate the location he or she is at, and then send the ID of this location up with a call to <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">statuses/update</a>.
     * <br>This method calls http://api.twitter.com/1/geo/nearby_places.json
     * @param query search query
     * @return places (cities and neighborhoods) that can be attached to a statuses/update
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-geo-nearby_places">Twitter API Wiki / Twitter REST API Method: GET geo nearby_places</a>
     * @deprecated <a href="http://code.google.com/p/twitter-api/issues/detail?id=1754">Issue 1754</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<Place> getNearbyPlaces(GeoQuery query) throws TwitterException;

    /**
     * Search for places (cities and neighborhoods) that can be attached to a statuses/update. Given a latitude and a longitude, return a list of all the valid places that can be used as a place_id when updating a status. Conceptually, a query can be made from the user's location, retrieve a list of places, have the user validate the location he or she is at, and then send the ID of this location up with a call to statuses/update.<br>
     * There are multiple granularities of places that can be returned -- "neighborhoods", "cities", etc. At this time, only United States data is available through this method.<br>
     * This API call is meant to be an informative call and will deliver generalized results about geography.
     * <br>This method calls http://api.twitter.com/1/geo/reverse_geocode.json
     * @param query search query
     * @return places (cities and neighborhoods) that can be attached to a statuses/update
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/geo/reverse_geocode">GET geo/reverse_geocode | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<Place> reverseGeoCode(GeoQuery query) throws TwitterException;

    /**
     * Find out more details of a place that was returned from the {@link twitter4j.api.GeoMethodsAsync#reverseGeoCode(twitter4j.GeoQuery)} method.
     * <br>This method calls http://api.twitter.com/1/geo/id/:id.json
     * @param id The ID of the location to query about.
     * @return details of the specified place
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/geo/id/:place_id">GET geo/id/:place_id | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    Place getGeoDetails(String id) throws TwitterException;
}
