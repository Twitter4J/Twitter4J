/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpClientWrapperConfiguration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.logging.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * A java representation of the <a href="http://apiwiki.twitter.com/Streaming-API-Documentation">Twitter Streaming API</a><br>
 * Note that this class is NOT compatible with Google App Engine as GAE is not capable of handling requests longer than 30 seconds.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
public final class TwitterStream extends TwitterOAuthSupportBaseImpl implements java.io.Serializable {
    private final HttpClientWrapper http;
    private static final Logger logger = Logger.getLogger(TwitterStream.class);

    private StatusListener statusListener;
    private StreamHandlingThread handler = null;

    private static final long serialVersionUID = -762817147320767897L;

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     * @deprecated use {@link TwitterStreamFactory#getInstance()} instead.
     */
    public TwitterStream() {
        super(ConfigurationContext.getInstance());
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
    }

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     * @param screenName screen name
     * @param password password
     * @deprecated use {@link TwitterStreamFactory#getInstance()} instead.
     */
    public TwitterStream(String screenName, String password) {
        super(ConfigurationContext.getInstance(), screenName, password);
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
    }

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     * @param screenName screen name
     * @param password password
     * @param listener listener
     * @deprecated use {@link TwitterStreamFactory#getInstance()} instead.
     */
    public TwitterStream(String screenName, String password, StatusListener listener) {
        super(ConfigurationContext.getInstance(), screenName, password);
        this.statusListener = listener;
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
    }

    /*package*/
    TwitterStream(Configuration conf, Authorization auth, StatusListener listener) {
        super(conf, auth);
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
        this.statusListener = listener;
    }

    /* Streaming API */

    /**
     * Starts listening on all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/firehose">Twitter API Wiki / Streaming API Documentation - firehose</a>
     * @since Twitter4J 2.0.4
     */
    public void firehose(final int count) {
        ensureAuthorizationEnabled();
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
        ensureAuthorizationEnabled();
        return getCountStream("statuses/firehose.json", count);
    }

    /**
     * Starts listening on all public statuses containing links. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the links stream. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/links">Twitter API Wiki / Streaming API Documentation - links</a>
     * @since Twitter4J 2.1.1
     */
    public void links(final int count) {
        ensureAuthorizationEnabled();
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
        ensureAuthorizationEnabled();
        return getCountStream("statuses/links.json", count);
    }

    /**
     * Starts listening on a tweet stream.
     *
     * @param relativeUrl The relative url of the feed, for example "statuses/firehose.json" for the firehose.
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     */
    public void stream(final String relativeUrl, final int count, final boolean handleUserStream) {
        ensureAuthorizationEnabled();
      startHandler(new StreamHandlingThread(handleUserStream) {
          public StatusStream getStream() throws TwitterException {
              return getCountStream(relativeUrl, count);
          }
      });
    }

    private StatusStream getCountStream(String relativeUrl, int count) throws TwitterException {
        ensureAuthorizationEnabled();
      ensureBasicEnabled();
      try {
          return new StatusStreamImpl(http.post(conf.getStreamBaseURL() + relativeUrl
                  , new HttpParameter[]{new HttpParameter("count"
                          , String.valueOf(count))}, auth));
      } catch (IOException e) {
          throw new TwitterException(e);
      }
    }

    /**
     * Starts listening on all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/retweet">Twitter API Wiki / Streaming API Documentation - retweet</a>
     * @since Twitter4J 2.0.10
     */
    public void retweet() {
        ensureAuthorizationEnabled();
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
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(http.post(conf.getStreamBaseURL() + "statuses/retweet.json"
                    , new HttpParameter[]{}, auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Starts listening on random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/sample">Twitter API Wiki / Streaming API Documentation - sample</a>
     * @since Twitter4J 2.0.10
     */
    public void sample() {
        ensureAuthorizationEnabled();
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
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(http.get(conf.getStreamBaseURL() + "statuses/sample.json"
                    , auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * User Stream is currently in beta.<br>
     * Read through and follow the doc! Do not release products using UserStream during the preview / beta period.
     * @see <a href="http://apiwiki.twitter.com/ChirpUserStreams">Twitter API Wiki / ChirpUserStreams</a>
     */
   public void user() {
        ensureAuthorizationEnabled();
        startHandler(new StreamHandlingThread(true) {
            public UserStream getStream() throws TwitterException {
                return getUserStream();
            }
        });
    }

    /**
     * User Stream is currently in beta.<br>
     * Read through and follow the doc! Do not release products using UserStream during the preview / beta period.
     * @return UserStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/ChirpUserStreams">Twitter API Wiki / ChirpUserStreams</a>
     */
    public UserStream getUserStream() throws TwitterException {
        ensureAuthorizationEnabled();
        if (!(statusListener instanceof UserStreamListener)) {
            logger.warn("Use of UserStreamListener is suggested.");
        }
        try {
            return new StatusStreamImpl(http.get(conf.getUserStreamBaseURL () + "user.json"
                    + (conf.isUserStreamRepliesAllEnabled() ? "?replies=all" : "")
                    , auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }
    
    /**
     * Start consuming public statuses that match one or more filter predicates. At least one predicate parameter, follow, locations, or track must be specified. Multiple parameters may be specified which allows most clients to use a single connection to the Streaming API. Placing long parameters in the URL may cause the request to be rejected for excessive URL length.<br>
     * The default access level allows up to 200 track keywords, 400 follow userids and 10 1-degree location boxes. Increased access levels allow 80,000 follow userids ("shadow" role), 400,000 follow userids ("birddog" role), 10,000 track keywords ("restricted track" role),  200,000 track keywords ("partner track" role), and 200 10-degree location boxes ("locRestricted" role). Increased track access levels also pass a higher proportion of statuses before limiting the stream.
     *
     * @param query Filter query
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/filter">Twitter API Wiki / Streaming API Documentation - filter</a>
     * @since Twitter4J 2.1.2
     */
    public void filter(final FilterQuery query) throws TwitterException {
        ensureAuthorizationEnabled();
        startHandler(new StreamHandlingThread() {
            public StatusStream getStream() throws TwitterException {
                return getFilterStream(query);
            }
        });
    }


    /**
     * Returns public statuses that match one or more filter predicates. At least one predicate parameter, follow, locations, or track must be specified. Multiple parameters may be specified which allows most clients to use a single connection to the Streaming API. Placing long parameters in the URL may cause the request to be rejected for excessive URL length.<br>
     * The default access level allows up to 200 track keywords, 400 follow userids and 10 1-degree location boxes. Increased access levels allow 80,000 follow userids ("shadow" role), 400,000 follow userids ("birddog" role), 10,000 track keywords ("restricted track" role),  200,000 track keywords ("partner track" role), and 200 10-degree location boxes ("locRestricted" role). Increased track access levels also pass a higher proportion of statuses before limiting the stream.
     *
     * @param query Filter query
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/filter">Twitter API Wiki / Streaming API Documentation - filter</a>
     * @since Twitter4J 2.1.2
     */
    public StatusStream getFilterStream(FilterQuery query) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(http.post(conf.getStreamBaseURL()
                    + "statuses/filter.json"
                    , query.asHttpParameterArray(), auth));
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }


    /**
     * Start consuming public statuses that match one or more filter predicates. At least one predicate parameter, follow, locations, or track must be specified. Multiple parameters may be specified which allows most clients to use a single connection to the Streaming API. Placing long parameters in the URL may cause the request to be rejected for excessive URL length.<br>
     * The default access level allows up to 200 track keywords, 400 follow userids and 10 1-degree location boxes. Increased access levels allow 80,000 follow userids ("shadow" role), 400,000 follow userids ("birddog" role), 10,000 track keywords ("restricted track" role),  200,000 track keywords ("partner track" role), and 200 10-degree location boxes ("locRestricted" role). Increased track access levels also pass a higher proportion of statuses before limiting the stream.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @param track  Specifies keywords to track.
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/filter">Twitter API Wiki / Streaming API Documentation - filter</a>
     * @since Twitter4J 2.0.10
     * @deprecated use {@link #filter(FilterQuery)} instead
     */
    public void filter(final int count, final int[] follow, final String[] track) {
        ensureAuthorizationEnabled();
        startHandler(new StreamHandlingThread() {
            public StatusStream getStream() throws TwitterException {
                return getFilterStream(count, follow, track);
            }
        });
    }

    /**
     * Returns public statuses that match one or more filter predicates. At least one predicate parameter, follow, locations, or track must be specified. Multiple parameters may be specified which allows most clients to use a single connection to the Streaming API. Placing long parameters in the URL may cause the request to be rejected for excessive URL length.<br>
     * The default access level allows up to 200 track keywords, 400 follow userids and 10 1-degree location boxes. Increased access levels allow 80,000 follow userids ("shadow" role), 400,000 follow userids ("birddog" role), 10,000 track keywords ("restricted track" role),  200,000 track keywords ("partner track" role), and 200 10-degree location boxes ("locRestricted" role). Increased track access levels also pass a higher proportion of statuses before limiting the stream.
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#statuses/filter">Twitter API Wiki / Streaming API Documentation - filter</a>
     * @since Twitter4J 2.0.10
     * @deprecated use {@link #getFilterStream(FilterQuery)} instead
     */
    public StatusStream getFilterStream(int count, int[] follow, String[] track)
            throws TwitterException {
        ensureAuthorizationEnabled();
        return getFilterStream(new FilterQuery(count, follow, track, null));
    }


    private synchronized void startHandler(StreamHandlingThread handler) {
        cleanup();
        if (null == statusListener) {
            throw new IllegalStateException("StatusListener is not set.");
        }
        this.handler = handler;
        this.handler.start();
    }

    private synchronized void startUserStreamHandler(StreamHandlingThread handler) {
        cleanup();
        if (null == statusListener) {
            throw new IllegalStateException("UserStreamListener is not set.");
        }
        if (!(statusListener instanceof UserStreamListener)) {
            throw new IllegalStateException("UserStreamListener is not set.");
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

    public void setUserStreamListener(UserStreamListener statusListener) {
        this.statusListener = statusListener;
    }

    /*
     http://apiwiki.twitter.com/Streaming-API-Documentation#Connecting
     When a network error (TCP/IP level) is encountered, back off linearly. Perhaps start at 250 milliseconds, double, and cap at 16 seconds
     When a HTTP error (> 200) is returned, back off exponentially.
     Perhaps start with a 10 second wait, double on each subsequent failure, and finally cap the wait at 240 seconds. Consider sending an alert to a human operator after multiple HTTP errors, as there is probably a client configuration issue that is unlikely to be resolved without human intervention. There's not much point in polling any faster in the face of HTTP error codes and your client is may run afoul of a rate limit.
     */
    private static final int TCP_ERROR_INITIAL_WAIT = 250;
    private static final int TCP_ERROR_WAIT_CAP = 16 * 1000;

    private static final int HTTP_ERROR_INITIAL_WAIT = 10 * 1000;
    private static final int HTTP_ERROR_WAIT_CAP = 240 * 1000;

    private static final int NO_WAIT = 0;

    abstract class StreamHandlingThread extends Thread {
        private StatusStream stream = null;
        private UserStreamListener userStreamListener;
        private final boolean handleUserStream;
        private static final String NAME = "Twitter Stream Handling Thread";
        private boolean closed = false;

        StreamHandlingThread() {
            this(false);
        }

        StreamHandlingThread(boolean handleUserStream) {
            super(NAME + "[initializing]");
            this.handleUserStream = handleUserStream;

        }

        public void run() {
            int timeToSleep = NO_WAIT;
            while (!closed) {
                try {
                    if (!closed && null == stream) {
                        // try establishing connection
                        setStatus("[Establishing connection]");
                        stream = getStream();
                        // connection established successfully
                        timeToSleep = NO_WAIT;
                        setStatus("[Receiving stream]");
                        while (!closed) {
                            if(handleUserStream){
                                ((UserStream)stream).next((UserStreamListener)statusListener);
                            }else{
                                stream.next(statusListener);
                            }
                        }
                    }
                } catch (TwitterException te) {
                    if (!closed) {
                        if (NO_WAIT == timeToSleep) {
                            if (te.getStatusCode() > 200) {
                                timeToSleep = HTTP_ERROR_INITIAL_WAIT;
                            } else if (0 == timeToSleep) {
                                timeToSleep = TCP_ERROR_INITIAL_WAIT;
                            }
                        }
                        // there was a problem establishing the connection, or the connection closed by peer
                        if (!closed) {
                            // wait for a moment not to overload Twitter API
                            setStatus("[Waiting for " + (timeToSleep) + " milliseconds]");
                            try {
                                Thread.sleep(timeToSleep);
                            } catch (InterruptedException ignore) {
                            }
                            timeToSleep = Math.min(timeToSleep * 2, (te.getStatusCode() > 200) ? HTTP_ERROR_WAIT_CAP : TCP_ERROR_WAIT_CAP);

                        }
                        stream = null;
                        logger.debug(te.getMessage());
                        statusListener.onException(te);
                    }

                }
            }
            try {
                if (null != this.stream) {
                    this.stream.close();
                }
            } catch (IOException ignore) {
            }
        }

        public synchronized void close() throws IOException {
            setStatus("[Disposing thread]");
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

    public int getHttpMaxTotalConnections() {
      return nestedConf.getHttpMaxTotalConnections();
    }

    public int getHttpDefaultMaxPerRoute() {
      return nestedConf.getHttpDefaultMaxPerRoute();
    }

    public Map<String, String> getRequestHeaders() {
        return nestedConf.getRequestHeaders();
    }

}
