package twitter4j;

import twitter4j.v1.Location;
import twitter4j.v1.ResponseList;
import twitter4j.v1.Trends;
import twitter4j.v1.TrendsResources;

import java.util.List;
import java.util.function.Consumer;

class TrendsResourcesImpl extends APIResourceBase implements TrendsResources {
    TrendsResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                        String IMPLICIT_PARAMS_STR,
                        List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                        List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public Trends getPlaceTrends(int woeid) throws TwitterException {
        return factory.createTrends(get(restBaseURL + "trends/place.json?id=" + woeid));
    }

    @Override
    public ResponseList<Location> getAvailableTrends() throws TwitterException {
        return factory.createLocationList(get(restBaseURL + "trends/available.json"));
    }

    @Override
    public ResponseList<Location> getClosestTrends(GeoLocation location) throws TwitterException {
        return factory.createLocationList(get(restBaseURL + "trends/closest.json", new HttpParameter("lat", location.getLatitude()), new HttpParameter("long", location.getLongitude())));
    }
}
