package twitter4j;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public interface Place extends TwitterResponse, Comparable<Place>, java.io.Serializable {
    String getName();

    String getStreetAddress();

    String getCountryCode();

    String getId();

    String getCountry();

    String getPlaceType();

    String getURL();

    String getFullName();

    String getBoundingBoxType();

    GeoLocation[][] getBoundingBoxCoordinates();

    String getGeometryType();

    GeoLocation[][] getGeometryCoordinates();

    Place[] getContainedWithIn();
}
