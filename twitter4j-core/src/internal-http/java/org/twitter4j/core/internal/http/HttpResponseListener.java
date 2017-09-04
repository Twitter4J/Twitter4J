package org.twitter4j.core.internal.http;

/**
 * @author Andrew Hedges - andrew.hedges at gmail.com
 */
public interface HttpResponseListener {

    void httpResponseReceived(HttpResponseEvent event);

}
