package twitter4j.v1;

import twitter4j.TwitterResponse;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public interface Place extends TwitterResponse, Comparable<Place>, java.io.Serializable {
    /**
     * @return name
     */
    String getName();

    /**
     * @return street address
     */
    String getStreetAddress();

    /**
     * @return country code
     */
    String getCountryCode();

    /**
     * @return id
     */
    String getId();

    /**
     * @return country
     */
    String getCountry();

    /**
     * @return place type
     */
    String getPlaceType();

    /**
     * @return url
     */
    String getURL();

    /**
     * @return full name
     */
    String getFullName();

    /**
     * @return bounding box type
     */
    String getBoundingBoxType();

    /**
     * @return bounding box corrdinates
     */
    GeoLocation[][] getBoundingBoxCoordinates();

    /**
     * @return geometry type
     */
    String getGeometryType();

    /**
     * @return geometry coordinates
     */
    GeoLocation[][] getGeometryCoordinates();

    /**
     * @return contained within
     */
    Place[] getContainedWithIn();
}
