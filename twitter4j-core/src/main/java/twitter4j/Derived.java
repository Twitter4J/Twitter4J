package twitter4j;

/**
 * A data interface representing the derived of a user
 * see https://developer.twitter.com/en/docs/tweets/enrichments/overview/profile-geo
 *
 * @author Vincent Demay - vincent at demay-fr.net
 * @since Twitter4J 4.0.7
 */
public interface Derived extends java.io.Serializable {
    /**
     * Returns a list of derived location
     *
     * @return a list of derived location
     */
    DerivedLocation[] getLocations();
}
