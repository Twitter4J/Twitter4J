package twitter4j.internal.http;

/**
 * @author Andrew Hedges - andrew.hedges at gmail.com
 */
public interface HttpResponseListener {

    public void httpResponseReceived(HttpResponseEvent event);

}
