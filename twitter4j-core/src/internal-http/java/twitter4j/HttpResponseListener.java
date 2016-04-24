package twitter4j;

/**
 * @author Andrew Hedges - andrew.hedges at gmail.com
 */
public interface HttpResponseListener {

    void httpResponseReceived(HttpResponseEvent event);

}
