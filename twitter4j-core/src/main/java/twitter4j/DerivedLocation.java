package twitter4j;

/**
 * A data interface representing the derivedLocation of user
 * https://developer.twitter.com/en/docs/tweets/enrichments/overview/profile-geo
 *
 * @author Vincent Demay - vincent at demay-fr.net
 * @since Twitter4J 4.0.7
 */
public interface DerivedLocation extends java.io.Serializable {

    String getCountry();

    String getCountryCode();

    String getLocality();

    String getRegion();

    String getSubRegion();

    String getFullName();
}
