// generated with generate-lazy-objects.sh
package twitter4j.internal.json;

import twitter4j.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
final class LazyPlace implements twitter4j.Place {
    private twitter4j.internal.http.HttpResponse res;
    private zzzz_T4J_INTERNAL_Factory factory;
    private Place target = null;

    LazyPlace(twitter4j.internal.http.HttpResponse res, zzzz_T4J_INTERNAL_Factory factory) {
        this.res = res;
        this.factory = factory;
    }

    private Place getTarget() {
        if (target == null) {
            try {
                target = factory.createPlace(res);
            } catch (TwitterException e) {
                throw new RuntimeException(e);
            }
        }
        return target;
    }

    public String getName() {
        return getTarget().getName();
    }


    public String getStreetAddress() {
        return getTarget().getStreetAddress();
    }


    public String getCountryCode() {
        return getTarget().getCountryCode();
    }


    public String getId() {
        return getTarget().getId();
    }


    public String getCountry() {
        return getTarget().getCountry();
    }


    public String getPlaceType() {
        return getTarget().getPlaceType();
    }


    public String getURL() {
        return getTarget().getURL();
    }


    public String getFullName() {
        return getTarget().getFullName();
    }


    public String getBoundingBoxType() {
        return getTarget().getBoundingBoxType();
    }


    public GeoLocation[][] getBoundingBoxCoordinates() {
        return getTarget().getBoundingBoxCoordinates();
    }


    public String getGeometryType() {
        return getTarget().getGeometryType();
    }


    public GeoLocation[][] getGeometryCoordinates() {
        return getTarget().getGeometryCoordinates();
    }


    public Place[] getContainedWithIn() {
        return getTarget().getContainedWithIn();
    }

    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }

    public int compareTo(Place target) {
        return getTarget().compareTo(target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyPlace{" +
                "target=" + getTarget() +
                "}";
    }
}
