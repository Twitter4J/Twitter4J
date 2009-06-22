/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j;

import twitter4j.http.PostParameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A java reporesentation of the <a href="http://apiwiki.twitter.com/Streaming-API-Documentation">Twitter Streaming API</a>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
public class TwitterStream extends TwitterSupport {
    private final static boolean DEBUG = Configuration.getDebug();

    private static final String STREAM_BASE_URL = "http://stream.twitter.com/";
    private StatusListener statusListener;
    private StreamHandlingThread handler = null;
    private int retryPerMinutes = 1;

    public TwitterStream(String userId, String password) {
        super(userId, password);
    }

    public TwitterStream(String userId, String password, StatusListener listener) {
        super(userId, password);
        this.statusListener = listener;
    }

    /* Streaming API */
    /**
     * Starts listening on all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#firehose">Twitter API Wiki / Streaming API Documentation - firehose</a>
     * @since Twitter4J 2.0.4
     */
    public void firehose(int count) throws TwitterException {
        startHandler(new StreamHandlingThread(new Object[]{count}) {
            public StatusStream getStream() throws TwitterException {
                return getFirehoseStream((Integer) args[0]);
            }
        });
    }

    /**
     * Returns a status stream for all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#firehose">Twitter API Wiki / Streaming API Documentation - firehose</a>
     * @since Twitter4J 2.0.4
     */
    public StatusStream getFirehoseStream(int count) throws TwitterException {

        try {
            return new StatusStream(http.post(STREAM_BASE_URL + "firehose.json"
                    , new PostParameter[]{new PostParameter("count"
                            , String.valueOf(count))}, true));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Starts listening on a percentage of all public statuses, suitable for data mining and research applications that require a statistically significant sample. Available only to approved parties and requires a signed agreement to access.
     *
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#gardenhose">Twitter API Wiki / Streaming API Documentation - gardenhose</a>
     * @since Twitter4J 2.0.4
     */
    public void gardenhose() throws TwitterException {
        startHandler(new StreamHandlingThread(null) {
            public StatusStream getStream() throws TwitterException {
                return getGardenhoseStream();
            }
        });
    }

    /**
     * Returns a status stream for a percentage of all public statuses, suitable for data mining and research applications that require a statistically significant sample. Available only to approved parties and requires a signed agreement to access.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#gardenhose">Twitter API Wiki / Streaming API Documentation - gardenhose</a>
     * @since Twitter4J 2.0.4
     */
    public StatusStream getGardenhoseStream() throws TwitterException {
        try {
            return new StatusStream(http.get(STREAM_BASE_URL + "gardenhose.json"
                    , true));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Starts listening on a percentage of all public statuses, suitable for small projects that don't require a statistically significant sample. Publicly available.
     *
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#spritzer">Twitter API Wiki / Streaming API Documentation - spritizer</a>
     * @since Twitter4J 2.0.4
     */
    public void spritzer() throws TwitterException {
        startHandler(new StreamHandlingThread(null) {
            public StatusStream getStream() throws TwitterException {
                return getSpritzerStream();
            }
        });
    }

    /**
     * Returns a status stream for a percentage of all public statuses, suitable for small projects that don't require a statistically significant sample. Publicly available.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#spritzer">Twitter API Wiki / Streaming API Documentation - spritizer</a>
     * @since Twitter4J 2.0.4
     */
    public StatusStream getSpritzerStream() throws TwitterException {
        try {
            return new StatusStream(http.get(STREAM_BASE_URL + "spritzer.json"
                    , true));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Starts listening on public statuses from a specified set of users, by ID. Requires use of the "follow" parameter, documented below. Allows following up to 200,000 users. Available only to approved parties and requires a signed agreement to access.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#birddog">Twitter API Wiki / Streaming API Documentation - birddog</a>
     * @since Twitter4J 2.0.4
     */
    public void birddog(int count, int[] follow) throws TwitterException {
        startHandler(new StreamHandlingThread(new Object[]{count, follow}) {
            public StatusStream getStream() throws TwitterException {
                return getBirddogStream((Integer) args[0], (int[]) args[1]);
            }
        });
    }

    /**
     * Returns a status stream for public statuses from a specified set of users, by ID. Requires use of the "follow" parameter, documented below. Allows following up to 200,000 users. Available only to approved parties and requires a signed agreement to access.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#birddog">Twitter API Wiki / Streaming API Documentation - birddog</a>
     * @since Twitter4J 2.0.4
     */
    public StatusStream getBirddogStream(int count, int[] follow) throws TwitterException {
        try {
            return new StatusStream(http.post(STREAM_BASE_URL + "birddog.json"
                    , new PostParameter[]{new PostParameter("count"
                            , String.valueOf(count)), new PostParameter("follow"
                            , toFollowString(follow))}, true));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * See birddog above. Allows following up to 2,000 users.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#firehose">Twitter API Wiki / Streaming API Documentation - shadow</a>
     * @since Twitter4J 2.0.4
     */
    public void shadow(int count, int[] follow) throws TwitterException {
        startHandler(new StreamHandlingThread(new Object[]{count, follow}) {
            public StatusStream getStream() throws TwitterException {
                return getShadowStream((Integer) args[0], (int[]) args[1]);
            }
        });
    }

    /**
     * See birddog above. Allows following up to 2,000 users.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#firehose">Twitter API Wiki / Streaming API Documentation - shadow</a>
     * @since Twitter4J 2.0.4
     */
    public StatusStream getShadowStream(int count, int[] follow) throws TwitterException {
        try {
            return new StatusStream(http.post(STREAM_BASE_URL + "shadow.json"
                    , new PostParameter[]{new PostParameter("count"
                            , String.valueOf(count)), new PostParameter("follow"
                            , toFollowString(follow))}, true));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * See birddog above. Allows following up to 200 users. Publicly available.
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#follow">Twitter API Wiki / Streaming API Documentation - follow</a>
     * @since Twitter4J 2.0.4
     */
    public void follow(int[] follow) throws TwitterException {
        startHandler(new StreamHandlingThread(new Object[]{follow}) {
            public StatusStream getStream() throws TwitterException {
                return getFollowStream((int[]) args[0]);
            }
        });
    }

    /**
     * See birddog above. Allows following up to 200 users. Publicly available.
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#follow">Twitter API Wiki / Streaming API Documentation - follow</a>
     * @since Twitter4J 2.0.4
     */
    public StatusStream getFollowStream(int[] follow) throws TwitterException {
        try {
            return new StatusStream(http.post(STREAM_BASE_URL + "follow.json"
                    , new PostParameter[]{new PostParameter("follow"
                            , toFollowString(follow))}, true));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    private String toFollowString(int[] follows) {
        StringBuffer buf = new StringBuffer(11 * follows.length);
        for (int follow : follows) {
            if (0 != buf.length()) {
                buf.append(" ");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    private synchronized void startHandler(StreamHandlingThread handler) throws TwitterException {
        cleanup();
        if(null == statusListener){
            throw new IllegalStateException("StatusListener is not set.");
        }
        this.handler = handler;
        this.handler.start();
    }

    public synchronized void cleanup() {
        if (null != handler) {
            try {
                handler.close();
            } catch (IOException ignore) {
            }
        }
    }

    public StatusListener getStatusListener() {
        return statusListener;
    }

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    abstract class StreamHandlingThread extends Thread {
        StatusStream stream = null;
        Object[] args;
        private List<Long> retryHistory;
        private static final String NAME = "Twitter Stream Handling Thread";
        private boolean closed = false;

        StreamHandlingThread(Object[] args) {
            super(NAME + "[initializing]");
            this.args = args;
            retryHistory = new ArrayList<Long>(retryPerMinutes);
        }

        public void run() {
            Status status;
            while (!closed) {
                try {
                    // dispose outdated retry history
                    if(retryHistory.size() > 0){
                        if((System.currentTimeMillis() - retryHistory.get(0)) > 60000){
                            retryHistory.remove(0);
                        }
                    }
                    if(retryHistory.size() < retryPerMinutes){
                        // try establishing connection
                        setStatus("[establishing connection]");

                        while (!closed && null == stream) {
                            if (retryHistory.size() < retryPerMinutes) {
                                retryHistory.add(System.currentTimeMillis());
                                stream = getStream();
                            }
                        }
                    }else{
                        // exceeded retry limit, wait to a moment not to overload Twitter API
                        long timeToSleep = 60000 - (System.currentTimeMillis() - retryHistory.get(retryHistory.size() - 1));
                        setStatus("[retry limit reached. sleeping for " + (timeToSleep / 1000) + " secs]");
                        try {
                            Thread.sleep(timeToSleep);
                        } catch (InterruptedException ignore) {
                        }

                    }
                    if(null != stream){
                        // stream established
                        setStatus("[receiving stream]");
                        while (!closed && null != (status = stream.next())) {
                            log("received:", status.toString());
                            if (null != statusListener) {
                                statusListener.onStatus(status);
                            }
                        }
                    }
                } catch (TwitterException te) {
                    stream = null;
                    te.printStackTrace();
                    log(te.getMessage());
                    statusListener.onException(te);
                }
            }
        }

        public synchronized void close() throws IOException {
            setStatus("[disposing thread]");
            if (null != stream) {
                this.stream.close();
                closed = true;
            }
        }
        private void setStatus(String message){
            String actualMessage = NAME + message;
            setName(actualMessage);
            log(actualMessage);
        }

        abstract StatusStream getStream() throws TwitterException;

    }

    private void log(String message) {
        if (DEBUG) {
            System.out.println("[" + new java.util.Date() + "]" + message);
        }
    }

    private void log(String message, String message2) {
        if (DEBUG) {
            log(message + message2);
        }
    }
}
