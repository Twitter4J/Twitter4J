package twitter4j;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
interface StreamImplementation {
    void next(StreamListener[] listeners, RawStreamListener[] rawStreamListeners);

    void close();

    void onException(Exception ex, StreamListener[] listeners, RawStreamListener[] rawStreamListeners);
}
