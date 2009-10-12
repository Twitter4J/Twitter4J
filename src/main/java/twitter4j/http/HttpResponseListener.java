package twitter4j.http;

/**
 * @author Andrew Hedges - andrew.hedges at gmail.com
 */
public interface HttpResponseListener {

	public void httpResponseReceived(HttpResponseEvent event);
	
}
