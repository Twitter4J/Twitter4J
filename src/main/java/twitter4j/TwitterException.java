package twitter4j;

/**
 * An exception class that will be thrown when TwitterAPI calls are failed.<br>
 * In case the Twitter server returned HTTP error code, you can get the HTTP status code using getStatusCode() method.
 */
public class TwitterException extends Exception {
    private int statusCode = -1;
    public TwitterException(String msg) {
        super(msg);
    }

    public TwitterException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;

    }

    public TwitterException(String msg, Exception cause) {
        super(msg, cause);
    }

    public TwitterException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;

    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
