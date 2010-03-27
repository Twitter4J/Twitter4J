package twitter4j.api;

import twitter4j.GeoQuery;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.TwitterException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public interface GeoMethodsAsync {
    /**
     * Search for places (cities and neighborhoods) that can be attached to a <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">statuses/update</a>.  Given a latitude and a longitude pair, or an IP address, return a list of all the valid cities and neighborhoods that can be used as a place_id when updating a status.  Conceptually, a query can be made from the user's location, retrieve a list of places, have the user validate the location he or she is at, and then send the ID of this location up with a call to <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">statuses/update</a>.
     * <br>This method calls http://api.twitter.com/1/geo/nearby_places.json
     * @param query search query
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-geo-nearby_places">Twitter API Wiki / Twitter REST API Method: GET geo nearby_places</a>
     * @since Twitter4J 2.1.1
     */
    void getNearbyPlaces(GeoQuery query);

    /**
     * Search for places (cities and neighborhoods) that can be attached to a <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">statuses/update</a>.  Given a latitude and a longitude, return a list of all the valid places that can be used as a place_id when updating a status.  Conceptually, a query can be made from the user's location, retrieve a list of places, have the user validate the location he or she is at, and then send the ID of this location up with a call to <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">statuses/update</a>.
     * <br>This method calls http://api.twitter.com/1/geo/reverse_geocode.json
     * @param query search query
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-geo-reverse_geocode">Twitter API Wiki / Twitter REST API Method: GET geo reverse_geocode</a>
     * @since Twitter4J 2.1.1
     */
    void reverseGeoCode(GeoQuery query);
}
