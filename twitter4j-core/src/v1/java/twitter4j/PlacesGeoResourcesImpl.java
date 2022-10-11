package twitter4j;

import twitter4j.v1.Place;
import twitter4j.v1.PlacesGeoResources;
import twitter4j.v1.ResponseList;

import java.util.List;
import java.util.function.Consumer;

class PlacesGeoResourcesImpl extends APIResourceBase implements PlacesGeoResources {
    PlacesGeoResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                           String IMPLICIT_PARAMS_STR,
                           List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                           List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public Place getGeoDetails(String placeId) throws TwitterException {
        return factory.createPlace(get(restBaseURL + "geo/id/" + placeId + ".json"));
    }

    @Override
    public ResponseList<Place> reverseGeoCode(GeoQuery query) throws TwitterException {
        try {
            return factory.createPlaceList(get(restBaseURL + "geo/reverse_geocode.json", query.asHttpParameterArray()));
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return factory.createEmptyResponseList();
            } else {
                throw te;
            }
        }
    }

    @Override
    public ResponseList<Place> searchPlaces(GeoQuery query) throws TwitterException {
        return factory.createPlaceList(get(restBaseURL + "geo/search.json", query.asHttpParameterArray()));
    }
}
