package twitter4j;

/**
 * A data interface representing the inwound of a single URL entity
 *
 * @author Vincent Demay - vincent at demay-fr.net
 * @since Twitter4J 4.0.7
 */
public interface Unwound extends java.io.Serializable {

    /**
     * Returns the unwound URL (akka the fully resolved url)
     *
     * @return the unwound URL (akka the fully resolved url)
     */
    String getUrl();

    /**
     * Returns the http status when Twitter resolved the url
     *
     * @return  the http status when Twitter resolved the url
     */
    int getStatus();

    /**
     * Returns the page title resolved by Twitter
     *
     * @return  the page title resolved by Twitter
     */
    String getTitle();

    /**
     * Returns the page description resolved by Twitter
     *
     * @return the page description resolved by Twitter
     */
    String getDescription();
}
