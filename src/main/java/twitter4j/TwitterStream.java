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

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.Authorization;
import twitter4j.http.HttpClientWrapper;
import twitter4j.http.HttpClientWrapperConfiguration;
import twitter4j.http.HttpParameter;
import twitter4j.logging.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A java reporesentation of the <a href="http://apiwiki.twitter.com/Streaming-API-Documentation">Twitter Streaming API</a><br>
 * Note that this class is NOT compatible with Google App Engine as GAE is not capable of handling requests longer than 30 seconds.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
public class TwitterStream extends TwitterBase implements java.io.Serializable {
    private final HttpClientWrapper http;
    private static final Logger logger = Logger.getLogger();

    private StatusListener statusListener;
    private StreamHandlingThread handler = null;
    private int retryPerMinutes = 1;
    private static final long serialVersionUID = -762817147320767897L;

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     * @deprecated use new TwitterStreamFactory.getInstance() instead.
     */
    public TwitterStream() {
        super(ConfigurationContext.getInstance());
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
        ensureBasicEnabled();
    }

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     * @param screenName screen name
     * @param password password
     * @deprecated use new TwitterStreamFactory.getInstance() instead.
     */
    public TwitterStream(String screenName, String password) {
        super(ConfigurationContext.getInstance(), screenName, password);
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
        ensureBasicEnabled();
    }

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     * @param screenName screen name
     * @param password password
     * @param listener listener
     * @deprecated use new TwitterStreamFactory.getInstance() instead.
     */
    public TwitterStream(String screenName, String password, StatusListener listener) {
        super(ConfigurationContext.getInstance(), screenName, password);
        this.statusListener = listener;
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
        ensureBasicEnabled();
    }

    /*package*/
    TwitterStream(Configuration conf, Authorization auth, StatusListener listener) {
        super(conf, auth);
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
        this.statusListener = listener;
        ensureBasicEnabled();
    }

    /* Streaming API */

    /**
     * Starts listening on all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/firehose">Twitter API Wiki / Streaming API Documentation - firehose</a>
     * @since Twitter4J 2.0.4
     */
    public void firehose(final int count) throws TwitterException {
        startHandler(new StreamHandlingThread() {
            public StatusStream getStream() throws TwitterException {
                return getFirehoseStream(count);
            }
        });
    }

    /**
     * Returns a status stream of all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/firehose">Twitter API Wiki / Streaming API Documentation - firehose</a>
     * @since Twitter4J 2.0.4
     */
    public StatusStream getFirehoseStream(int count) throws TwitterException {
        ensureBasicEnabled();
        try {
            return new StatusStream(http.post(conf.getStreamBaseURL() + "statuses/firehose.json"
                    , new HttpParameter[]{new HttpParameter("count"
                            , String.valueOf(count))}, auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Starts listening on all public statuses containing links. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the links stream. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/links">Twitter API Wiki / Streaming API Documentation - links</a>
     * @since Twitter4J 2.1.1
     */
    public void links(final int count) throws TwitterException {
        startHandler(new StreamHandlingThread() {
            public StatusStream getStream() throws TwitterException {
                return getLinksStream(count);
            }
        });
    }

    /**
     * Returns a status stream of all public statuses containing links. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the links stream. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/links">Twitter API Wiki / Streaming API Documentation - links</a>
     * @since Twitter4J 2.1.1
     */
    public StatusStream getLinksStream(int count) throws TwitterException {
        ensureBasicEnabled();
        try {
            return new StatusStream(http.post(conf.getStreamBaseURL() + "statuses/links.json"
                    , new HttpParameter[]{new HttpParameter("count"
                            , String.valueOf(count))}, auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Starts listening on all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/retweet">Twitter API Wiki / Streaming API Documentation - retweet</a>
     * @since Twitter4J 2.0.10
     */
    public void retweet() throws TwitterException {
        ensureBasicEnabled();
        startHandler(new StreamHandlingThread() {
            public StatusStream getStream() throws TwitterException {
                return getRetweetStream();
            }
        });
    }

    /**
     * Returns a stream of all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/retweet">Twitter API Wiki / Streaming API Documentation - retweet</a>
     * @since Twitter4J 2.0.10
     */
    public StatusStream getRetweetStream() throws TwitterException {
        ensureBasicEnabled();
        try {
            return new StatusStream(http.post(conf.getStreamBaseURL() + "statuses/retweet.json"
                    , new HttpParameter[]{}, auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Starts listening on random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/sample">Twitter API Wiki / Streaming API Documentation - sample</a>
     * @since Twitter4J 2.0.10
     */
    public void sample() throws TwitterException {
        ensureBasicEnabled();
        startHandler(new StreamHandlingThread() {
            public StatusStream getStream() throws TwitterException {
                return getSampleStream();
            }
        });
    }

    /**
     * Returns a stream of random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#Sampling">Twitter API Wiki / Streaming API Documentation - Sampling</a>
     * @since Twitter4J 2.0.10
     */
    public StatusStream getSampleStream() throws TwitterException {
        ensureBasicEnabled();
        try {
            return new StatusStream(http.get(conf.getStreamBaseURL() + "statuses/sample.json"
                    , auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }


    /**
     * See birddog above. Allows following up to 200 users. Publicly available.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @param track  Specifies keywords to track.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/filter">Twitter API Wiki / Streaming API Documentation - filter</a>
     * @since Twitter4J 2.0.10
     */
    public void filter(final int count, final int[] follow, final String[] track) throws TwitterException {
        startHandler(new StreamHandlingThread() {
            public StatusStream getStream() throws TwitterException {
                return getFilterStream(count, follow, track);
            }
        });
    }

    /**
     * Returns stream of public statuses that match one or more filter predicates. At least one predicate parameter, track or follow, must be specified. Both parameters may be specified which allows most clients to use a single connection to the Streaming API.
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/filter">Twitter API Wiki / Streaming API Documentation - filter</a>
     * @since Twitter4J 2.0.10
     */
    public StatusStream getFilterStream(int count, int[] follow, String[] track)
            throws TwitterException {
        ensureBasicEnabled();
        List<HttpParameter> postparams = new ArrayList<HttpParameter>();
        postparams.add(new HttpParameter("count", count));
        if (null != follow && follow.length > 0) {
            postparams.add(new HttpParameter("follow"
                    , toFollowString(follow)));
        }
        if (null != track && track.length > 0) {
            postparams.add(new HttpParameter("track"
                    , toTrackString(track)));
        }
        try {
            return new StatusStream(http.post(conf.getStreamBaseURL() + "statuses/filter.json"
                    , postparams.toArray(new HttpParameter[postparams.size()]), auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    private String toFollowString(int[] follows) {
        StringBuffer buf = new StringBuffer(11 * follows.length);
        for (int follow : follows) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    private String toTrackString(final String[] keywords) {
        final StringBuffer buf = new StringBuffer(20 * keywords.length * 4);
        for (String keyword : keywords) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(keyword);
        }
        return buf.toString();
    }

    private synchronized void startHandler(StreamHandlingThread handler) throws TwitterException {
        cleanup();
        if (null == statusListener) {
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

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    abstract class StreamHandlingThread extends Thread {
        StatusStream stream = null;
        private List<Long> retryHistory;
        private static final String NAME = "Twitter Stream Handling Thread";
        private boolean closed = false;

        StreamHandlingThread() {
            super(NAME + "[initializing]");
            retryHistory = new ArrayList<Long>(retryPerMinutes);
        }

        public void run() {
            while (!closed) {
                try {
                    // dispose outdated retry history
                    if (retryHistory.size() > 0) {
                        if ((System.currentTimeMillis() - retryHistory.get(0)) > 60000) {
                            retryHistory.remove(0);
                        }
                    }
                    if (retryHistory.size() < retryPerMinutes) {
                        // try establishing connection
                        setStatus("[establishing connection]");
                        while (!closed && null == stream) {
                            if (retryHistory.size() < retryPerMinutes) {
                                retryHistory.add(System.currentTimeMillis());
                                stream = getStream();
                            }
                        }
                    } else if (!closed) {
                        // exceeded retry limit, wait to a moment not to overload Twitter API
                        long timeToSleep = 60000 - (System.currentTimeMillis() - retryHistory.get(retryHistory.size() - 1));
                        setStatus("[retry limit reached. sleeping for " + (timeToSleep / 1000) + " secs]");
                        try {
                            Thread.sleep(timeToSleep);
                        } catch (InterruptedException ignore) {
                        }

                    }
                    if (null != stream) {
                        // stream established
                        setStatus("[receiving stream]");
                        while (!closed) {
                            stream.next(statusListener);
                        }
                    }
                } catch (TwitterException te) {
                    stream = null;
                    te.printStackTrace();
                    logger.debug(te.getMessage());
                    statusListener.onException(te);
                }
            }
            try {
                this.stream.close();
            } catch (IOException ignore) {
            }
        }

        public synchronized void close() throws IOException {
            setStatus("[disposing thread]");
            closed = true;
        }

        private void setStatus(String message) {
            String actualMessage = NAME + message;
            setName(actualMessage);
            logger.debug(actualMessage);
        }

        abstract StatusStream getStream() throws TwitterException;

    }
}

class StreamingReadTimeoutConfiguration implements HttpClientWrapperConfiguration {
    Configuration nestedConf;

    StreamingReadTimeoutConfiguration(Configuration httpConf) {
        this.nestedConf = httpConf;
    }

    public String getHttpProxyHost() {
        return nestedConf.getHttpProxyHost();
    }

    public int getHttpProxyPort() {
        return nestedConf.getHttpProxyPort();
    }

    public String getHttpProxyUser() {
        return nestedConf.getHttpProxyUser();
    }

    public String getHttpProxyPassword() {
        return nestedConf.getHttpProxyPassword();
    }

    public int getHttpConnectionTimeout() {
        return nestedConf.getHttpConnectionTimeout();
    }

    public int getHttpReadTimeout() {
        // this is the trick that overrides connection timeout
        return nestedConf.getHttpStreamingReadTimeout();
    }

    public int getHttpRetryCount() {
        return nestedConf.getHttpRetryCount();
    }

    public int getHttpRetryIntervalSeconds() {
        return nestedConf.getHttpRetryIntervalSeconds();
    }
    public Map<String, String> getRequestHeaders() {
        return nestedConf.getRequestHeaders();
    }

}
