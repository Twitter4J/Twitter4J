package twitter4j;

import java.io.IOException;

interface JSONStream extends StreamImplementation {
    /**
     * Reads next status from this stream.
     *
     * @param listener a JSONListener implementation
     * @throws TwitterException      when the end of the stream has been reached.
     * @throws IllegalStateException when the end of the stream had been reached.
     */
    void next(JSONListener listener) throws TwitterException;

    void close() throws IOException;
}
