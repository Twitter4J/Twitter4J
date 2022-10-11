package twitter4j;

import twitter4j.v1.GeoQuery;
import twitter4j.v1.Place;
import twitter4j.v1.PlacesGeoResources;
import twitter4j.v1.ResponseList;

import java.util.ArrayList;
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
            return factory.createPlaceList(get(restBaseURL + "geo/reverse_geocode.json", asHttpParameterArray(query)));
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
        return factory.createPlaceList(get(restBaseURL + "geo/search.json", asHttpParameterArray(query)));
    }
    private static HttpParameter[] asHttpParameterArray(GeoQuery query) {
        ArrayList<HttpParameter> params = new ArrayList<>();
        if (query.location != null) {
            appendParameter("lat", query.location.latitude, params);
            appendParameter("long", query.location.longitude, params);

        }
        if (query.ip != null) {
            appendParameter("ip", query.ip, params);

        }
        appendParameter("accuracy", query.accuracy, params);
        appendParameter("query", query.query, params);
        appendParameter("granularity", query.granularity, params);
        appendParameter("max_results", query.maxResults, params);
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private static void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private static void appendParameter(@SuppressWarnings("SameParameterValue") String name, int value, List<HttpParameter> params) {
        if (0 < value) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

    private static void appendParameter(String name, double value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

}
