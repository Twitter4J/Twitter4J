package twitter4j.internal.logging;

import twitter4j.conf.ConfigurationContext;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public class StdOutLogger extends Logger{
    private static final boolean DEBUG = ConfigurationContext.getInstance().isDebugEnabled();

    public boolean isDebugEnabled() {
        return DEBUG;
    }

    public void debug(String message) {
        if (DEBUG) {
            System.out.println("[" + new java.util.Date() + "]" + message);
        }
    }

    public void debug(String message, String message2) {
        if (DEBUG) {
            debug(message + message2);
        }
    }
}
