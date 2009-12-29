package twitter4j;

import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpResponseListener;
import twitter4j.http.Response;

import java.util.ArrayList;
import java.util.List;

public class RateLimitListenerSupport extends TwitterSupport implements java.io.Serializable {

    protected final List<RateLimitStatusListener> accountRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    protected final List<RateLimitStatusListener> ipRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    private static final long serialVersionUID = 6960663978976449394L;

    RateLimitListenerSupport() {
        super();
        init();
    }

    RateLimitListenerSupport(String screenName, String password) {
        super(screenName, password);
        init();
    }
    private void init(){
        HttpResponseListener httpResponseListener = new RateLimitListenerInvoker(accountRateLimitStatusListeners,ipRateLimitStatusListeners);
        http.addHttpResponseListener(httpResponseListener);
    }

    /**
     * Registers a RateLimitStatusListener for account associated rate limits
     * @param listener the listener to be added
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public void addAccountRateLimitStatusListener(RateLimitStatusListener listener){
    	accountRateLimitStatusListeners.add(listener);
    }


    /**
     * Registers a RateLimitStatusListener for ip associated rate limits
     *
     * @param listener the listener to be added
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public void addIpRateLimitStatusListener(RateLimitStatusListener listener){
    	ipRateLimitStatusListeners.add(listener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitListenerSupport)) return false;
        if (!super.equals(o)) return false;

        RateLimitListenerSupport that = (RateLimitListenerSupport) o;

        if (!accountRateLimitStatusListeners.equals(that.accountRateLimitStatusListeners))
            return false;
        if (!ipRateLimitStatusListeners.equals(that.ipRateLimitStatusListeners))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + accountRateLimitStatusListeners.hashCode();
        result = 31 * result + ipRateLimitStatusListeners.hashCode();
        return result;
    }
}
class RateLimitListenerInvoker implements HttpResponseListener, java.io.Serializable{
    private List<RateLimitStatusListener> accountRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    private List<RateLimitStatusListener> ipRateLimitStatusListeners = new ArrayList<RateLimitStatusListener>();
    private static final long serialVersionUID = 2277134489548449905L;

    RateLimitListenerInvoker(List<RateLimitStatusListener> accountRateLimitStatusListeners
            , List<RateLimitStatusListener> ipRateLimitStatusListeners){
        this.accountRateLimitStatusListeners = accountRateLimitStatusListeners;
        this.ipRateLimitStatusListeners = ipRateLimitStatusListeners;
    }
    public void httpResponseReceived(HttpResponseEvent event) {
        if (0 < (accountRateLimitStatusListeners.size() + ipRateLimitStatusListeners.size())) {
            Response res = event.getResponse();
            RateLimitStatus rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
            if (null != rateLimitStatus) {
                if (event.isAuthenticated()) {
                    fireRateLimitStatusListenerUpdate(accountRateLimitStatusListeners, rateLimitStatus);
                } else {
                    fireRateLimitStatusListenerUpdate(ipRateLimitStatusListeners, rateLimitStatus);
                }
            }
        }
    }

    private void fireRateLimitStatusListenerUpdate(List<RateLimitStatusListener> listeners, RateLimitStatus status) {
        for (RateLimitStatusListener listener : listeners) {
            listener.rateLimitStatusUpdated(status);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitListenerInvoker)) return false;

        RateLimitListenerInvoker that = (RateLimitListenerInvoker) o;

        if (accountRateLimitStatusListeners != null ? !accountRateLimitStatusListeners.equals(that.accountRateLimitStatusListeners) : that.accountRateLimitStatusListeners != null)
            return false;
        if (ipRateLimitStatusListeners != null ? !ipRateLimitStatusListeners.equals(that.ipRateLimitStatusListeners) : that.ipRateLimitStatusListeners != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountRateLimitStatusListeners != null ? accountRateLimitStatusListeners.hashCode() : 0;
        result = 31 * result + (ipRateLimitStatusListeners != null ? ipRateLimitStatusListeners.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RateLimitListenerInvoker{" +
                "accountRateLimitStatusListeners=" + accountRateLimitStatusListeners +
                ", ipRateLimitStatusListeners=" + ipRateLimitStatusListeners +
                '}';
    }
}