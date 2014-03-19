package twitter4j;

import twitter4j.conf.ConfigurationContext;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
final class StdOutLogger extends Logger {
    private static final boolean DEBUG = ConfigurationContext.getInstance().isDebugEnabled();

    @Override
    public boolean isDebugEnabled() {
        return DEBUG;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void debug(String message) {
        if (DEBUG) {
            System.out.println("[" + new java.util.Date() + "]" + message);
        }
    }

    @Override
    public void debug(String message, String message2) {
        if (DEBUG) {
            debug(message + message2);
        }
    }

    @Override
    public void info(String message) {
        System.out.println("[" + new java.util.Date() + "]" + message);
    }

    @Override
    public void info(String message, String message2) {
        info(message + message2);
    }

    @Override
    public void warn(String message) {
        System.out.println("[" + new java.util.Date() + "]" + message);
    }

    @Override
    public void warn(String message, String message2) {
        warn(message + message2);
    }

    @Override
    public void error(String message) {
        System.out.println("[" + new java.util.Date() + "]" + message);
    }

    @Override
    public void error(String message, Throwable th) {
        System.out.println(message);
        th.printStackTrace(System.err);
    }
}
