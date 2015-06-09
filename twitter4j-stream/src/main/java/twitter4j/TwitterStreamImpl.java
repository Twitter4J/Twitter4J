/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package twitter4j;

import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;
import twitter4j.util.function.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static twitter4j.HttpResponseCode.FORBIDDEN;
import static twitter4j.HttpResponseCode.NOT_ACCEPTABLE;

/**
 * A java representation of the <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods</a><br>
 * Note that this class is NOT compatible with Google App Engine as GAE is not capable of handling requests longer than 30 seconds.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
class TwitterStreamImpl extends TwitterBaseImpl implements TwitterStream {
    private static final long serialVersionUID = 5621090317737561048L;
    private final HttpClient http;
    private static final Logger logger = Logger.getLogger(TwitterStreamImpl.class);

    private final List<ConnectionLifeCycleListener> lifeCycleListeners = new ArrayList<ConnectionLifeCycleListener>(0);
    private TwitterStreamConsumer handler = null;

    private final String stallWarningsGetParam;
    private final HttpParameter stallWarningsParam;

    /*package*/
    TwitterStreamImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        http = HttpClientFactory.getInstance(new StreamingReadTimeoutConfiguration(conf));
        // turning off keepalive connection explicitly because Streaming API doesn't need keepalive connection.
        // and this will reduce the shutdown latency of streaming api connection
        // see also - http://jira.twitter4j.org/browse/TFJ-556
        http.addDefaultRequestHeader("Connection", "close");

        stallWarningsGetParam = "stall_warnings=" + (conf.isStallWarningsEnabled() ? "true" : "false");
        stallWarningsParam = new HttpParameter("stall_warnings", conf.isStallWarningsEnabled());
    }

    /* Streaming API */

    @Override
    public void firehose(final int count) {
        ensureAuthorizationEnabled();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer(Mode.status) {
            @Override
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
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/firehose</a>
     * @since Twitter4J 2.0.4
     */
    StatusStream getFirehoseStream(int count) throws TwitterException {
        ensureAuthorizationEnabled();
        return getCountStream("statuses/firehose.json", count);
    }

    @Override
    public void links(final int count) {
        ensureAuthorizationEnabled();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer(Mode.status) {
            @Override
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
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/links</a>
     * @since Twitter4J 2.1.1
     */
    StatusStream getLinksStream(int count) throws TwitterException {
        ensureAuthorizationEnabled();
        return getCountStream("statuses/links.json", count);
    }

    private StatusStream getCountStream(String relativeUrl, int count) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.post(conf.getStreamBaseURL() + relativeUrl
                    , new HttpParameter[]{new HttpParameter("count", String.valueOf(count))
                    , stallWarningsParam}, auth, null), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    @Override
    public void retweet() {
        ensureAuthorizationEnabled();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer(Mode.status) {
            @Override
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
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods statuses/retweet</a>
     * @since Twitter4J 2.0.10
     */
    StatusStream getRetweetStream() throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.post(conf.getStreamBaseURL() + "statuses/retweet.json"
                    , new HttpParameter[]{stallWarningsParam}, auth, null), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    @Override
    public void sample() {
        ensureAuthorizationEnabled();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer(Mode.status) {
            @Override
            public StatusStream getStream() throws TwitterException {
                return getSampleStream();
            }
        });
    }

    @Override
    public void sample(final String language) {
        ensureAuthorizationEnabled();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer(Mode.status) {
            @Override
            public StatusStream getStream() throws TwitterException {
                return getSampleStream(language);
            }
        });
    }

    /**
     * Returns a stream of random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods statuses/sample</a>
     * @since Twitter4J 2.0.10
     */
    StatusStream getSampleStream() throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.get(conf.getStreamBaseURL() + "statuses/sample.json?"
                    + stallWarningsGetParam, null, auth, null), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Returns a stream of random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     * <p>
     * Only returns tweets in the given languages
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods statuses/sample</a>
     * @since Twitter4J 2.0.10
     */
    StatusStream getSampleStream(String language) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.get(conf.getStreamBaseURL() + "statuses/sample.json?"
                    + stallWarningsGetParam + "&language=" + language, null, auth, null), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    @Override
    public void user() {
        user(null);
    }

    @Override
    public void user(final String[] track) {
        ensureAuthorizationEnabled();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer(Mode.user) {
            @Override
            public StatusStream getStream() throws TwitterException {
                return getUserStream(track);
            }
        });
    }

    /**
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @param track keywords to track
     * @return UserStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/streaming-api/user-streams">User Streams</a>
     * @since Twitter4J 2.1.9
     */
    UserStream getUserStream(String[] track) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            List<HttpParameter> params = new ArrayList<HttpParameter>();
            params.add(stallWarningsParam);
            if (conf.isUserStreamRepliesAllEnabled()) {
                params.add(new HttpParameter("replies", "all"));
            }
            if (!conf.isUserStreamWithFollowingsEnabled()) {
                params.add(new HttpParameter("with", "user"));
            }
            if (track != null) {
                params.add(new HttpParameter("track", StringUtil.join(track)));
            }
            return new UserStreamImpl(getDispatcher(), http.post(conf.getUserStreamBaseURL() + "user.json"
                    , params.toArray(new HttpParameter[params.size()])
                    , auth, null), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    @Override
    public StreamController site(final boolean withFollowings, final long[] follow) {
        ensureOAuthEnabled();
        ensureSiteStreamsListenerIsSet();
        final StreamController cs = new StreamController(http, auth);
        startHandler(new TwitterStreamConsumer(Mode.site) {
            @Override
            public StatusStream getStream() throws TwitterException {
                try {
                    return new SiteStreamsImpl(getDispatcher(), getSiteStream(withFollowings, follow), conf, cs);
                } catch (IOException e) {
                    throw new TwitterException(e);
                }
            }
        });
        return cs;
    }

    private Dispatcher getDispatcher() {
        if (null == TwitterStreamImpl.dispatcher) {
            synchronized (TwitterStreamImpl.class) {
                if (null == TwitterStreamImpl.dispatcher) {
                    // dispatcher is held statically, but it'll be instantiated with
                    // the configuration instance associated with this TwitterStream
                    // instance which invokes getDispatcher() on the first time.
                    TwitterStreamImpl.dispatcher = new DispatcherFactory(conf).getInstance();
                }
            }
        }
        return TwitterStreamImpl.dispatcher;
    }

    private static transient volatile Dispatcher dispatcher;

    InputStream getSiteStream(boolean withFollowings, long[] follow) throws TwitterException {
        ensureOAuthEnabled();
        return http.post(conf.getSiteStreamBaseURL() + "site.json",
                new HttpParameter[]{
                        new HttpParameter("with", withFollowings ? "followings" : "user")
                        , new HttpParameter("follow", StringUtil.join(follow))
                        , stallWarningsParam}, auth, null
        ).asStream();
    }

    @Override
    public void filter(final FilterQuery query) {
        ensureAuthorizationEnabled();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer(Mode.status) {
            @Override
            public StatusStream getStream() throws TwitterException {
                return getFilterStream(query);
            }
        });
    }

    @Override
    public void filter(final String... track) {
        filter(new FilterQuery().track(track));
    }

    /**
     * Returns public statuses that match one or more filter predicates. At least one predicate parameter, follow, locations, or track must be specified. Multiple parameters may be specified which allows most clients to use a single connection to the Streaming API. Placing long parameters in the URL may cause the request to be rejected for excessive URL length.<br>
     * The default access level allows up to 200 track keywords, 400 follow userids and 10 1-degree location boxes. Increased access levels allow 80,000 follow userids ("shadow" role), 400,000 follow userids ("birddog" role), 10,000 track keywords ("restricted track" role),  200,000 track keywords ("partner track" role), and 200 10-degree location boxes ("locRestricted" role). Increased track access levels also pass a higher proportion of statuses before limiting the stream.
     *
     * @param query Filter query
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods | Twitter Developers</a>
     * @since Twitter4J 2.1.2
     */
    StatusStream getFilterStream(FilterQuery query) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.post(conf.getStreamBaseURL()
                    + "statuses/filter.json"
                    , query.asHttpParameterArray(stallWarningsParam), auth, null), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }


    /**
     * check if any listener is set. Throws IllegalStateException if no listener is set.
     *
     * @throws IllegalStateException when no listener is set.
     */

    private void ensureStatusStreamListenerIsSet() {
        if (streamListeners.size() == 0) {
            throw new IllegalStateException("StatusListener is not set.");
        }
    }

    private void ensureSiteStreamsListenerIsSet() {
        if (getSiteStreamsListeners().length == 0 && getRawStreamListeners().length == 0) {
            throw new IllegalStateException("SiteStreamsListener is not set.");
        }
    }

    private static int numberOfHandlers = 0;

    private synchronized void startHandler(TwitterStreamConsumer handler) {
        cleanUp();
        this.handler = handler;
        this.handler.start();
        numberOfHandlers++;
    }

    @Override
    public synchronized void cleanUp() {
        if (handler != null) {
            handler.close();
            numberOfHandlers--;
        }
    }

    @Override
    public synchronized void shutdown() {
        cleanUp();
        synchronized (TwitterStreamImpl.class) {
            if (0 == numberOfHandlers) {
                if (dispatcher != null) {
                    dispatcher.shutdown();
                    dispatcher = null;
                }
            }
        }
    }

    @Override
    public void addConnectionLifeCycleListener(ConnectionLifeCycleListener listener) {
        this.lifeCycleListeners.add(listener);
    }

    private final ArrayList<StreamListener> streamListeners = new ArrayList<StreamListener>(0);

    @Override
    public synchronized void addListener(StreamListener listener) {
        streamListeners.add(listener);
        updateListeners();
    }

    @Override
    public synchronized TwitterStream onStatus(final Consumer<Status> action) {
        streamListeners.add(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                action.accept(status);
            }
        });
        updateListeners();
        return this;
    }

    @Override
    public synchronized TwitterStream onException(final Consumer<Exception> action) {
        streamListeners.add(new StatusAdapter() {
            @Override
            public void onException(Exception ex) {
                action.accept(ex);
            }
        });
        updateListeners();
        return this;
    }

    @Override
    public synchronized void removeListener(StreamListener listener) {
        streamListeners.remove(listener);
        updateListeners();
    }

    @Override
    public synchronized void clearListeners() {
        streamListeners.clear();
        updateListeners();
    }

    @Override
    public synchronized void replaceListener(StreamListener toBeRemoved, StreamListener toBeAdded) {
        streamListeners.remove(toBeRemoved);
        streamListeners.add(toBeAdded);
        updateListeners();
    }

    private synchronized void updateListeners() {
        if (handler != null) {
            handler.updateListeners();
        }
    }

    private RawStreamListener[] getRawStreamListeners() {
        ArrayList<RawStreamListener> rawStreamListeners = new ArrayList<RawStreamListener>();
        for (StreamListener streamListener : streamListeners) {
            if (streamListener instanceof RawStreamListener) {
                rawStreamListeners.add((RawStreamListener) streamListener);
            }
        }
        return rawStreamListeners.toArray(new RawStreamListener[rawStreamListeners.size()]);
    }

    private SiteStreamsListener[] getSiteStreamsListeners() {
        ArrayList<SiteStreamsListener> siteStreamsListeners = new ArrayList<SiteStreamsListener>();
        for (StreamListener streamListener : streamListeners) {
            if (streamListener instanceof SiteStreamsListener) {
                siteStreamsListeners.add((SiteStreamsListener) streamListener);
            }
        }
        return siteStreamsListeners.toArray(new SiteStreamsListener[siteStreamsListeners.size()]);
    }

    private StatusListener[] getStatusListeners() {
        ArrayList<StatusListener> statusListeners = new ArrayList<StatusListener>();
        for (StreamListener streamListener : streamListeners) {
            if (streamListener instanceof StatusListener) {
                statusListeners.add((StatusListener) streamListener);
            }
        }
        return statusListeners.toArray(new StatusListener[statusListeners.size()]);
    }

    /*
     https://dev.twitter.com/docs/streaming-api/concepts#connecting
     When a network error (TCP/IP level) is encountered, back off linearly. Perhaps start at 250 milliseconds, double, and cap at 16 seconds
     When a HTTP error (> 200) is returned, back off exponentially.
     Perhaps start with a 10 second wait, double on each subsequent failure, and finally cap the wait at 240 seconds. Consider sending an alert to a human operator after multiple HTTP errors, as there is probably a client configuration issue that is unlikely to be resolved without human intervention. There's not much point in polling any faster in the face of HTTP error codes and your client is may run afoul of a rate limit.
     */
    private static final int TCP_ERROR_INITIAL_WAIT = 250;
    private static final int TCP_ERROR_WAIT_CAP = 16 * 1000;

    private static final int HTTP_ERROR_INITIAL_WAIT = 10 * 1000;
    private static final int HTTP_ERROR_WAIT_CAP = 240 * 1000;

    private static final int NO_WAIT = 0;

    private static int count = 0;

    enum Mode {
        user, status, site
    }

    abstract class TwitterStreamConsumer extends Thread {
        private StatusStreamBase stream = null;
        private final String NAME = "Twitter Stream consumer-" + (++count);
        private volatile boolean closed = false;
        private StreamListener[] streamListeners;
        private RawStreamListener[] rawStreamListeners;
        private final Mode mode;

        TwitterStreamConsumer(Mode mode) {
            super();
            this.mode = mode;
            updateListeners();
            setName(NAME + "[initializing]");
        }

        void updateListeners() {
            switch (mode) {
                case site:
                    this.streamListeners = getSiteStreamsListeners();
                    break;
                default:
                    this.streamListeners = getStatusListeners();
                    break;
            }
            this.rawStreamListeners = getRawStreamListeners();
        }

        @Override
        public void run() {
            int timeToSleep = NO_WAIT;
            boolean connected = false;
            while (!closed) {
                try {
                    if (!closed && null == stream) {
                        // try establishing connection
                        logger.info("Establishing connection.");
                        setStatus("[Establishing connection]");
                        stream = (StatusStreamBase) getStream();
                        connected = true;
                        logger.info("Connection established.");
                        for (ConnectionLifeCycleListener listener : lifeCycleListeners) {
                            try {
                                listener.onConnect();
                            } catch (Exception e) {
                                logger.warn(e.getMessage());
                            }
                        }
                        // connection established successfully
                        timeToSleep = NO_WAIT;
                        logger.info("Receiving status stream.");
                        setStatus("[Receiving stream]");
                        while (!closed) {
                            try {
                                stream.next(this.streamListeners, this.rawStreamListeners);
                            } catch (IllegalStateException ise) {
                                logger.warn(ise.getMessage());
                                break;
                            } catch (TwitterException e) {
                                logger.info(e.getMessage());
                                stream.onException(e, this.streamListeners, this.rawStreamListeners);
                                throw e;
                            } catch (Exception e) {
                                if (!(e instanceof NullPointerException) && !"Inflater has been closed".equals(e.getMessage())) {
                                    logger.info(e.getMessage());
                                    stream.onException(e, this.streamListeners, this.rawStreamListeners);
                                    closed = true;
                                    break;
                                }
                            }
                        }
                    }
                } catch (TwitterException te) {
                    logger.info(te.getMessage());
                    if (!closed) {
                        if (NO_WAIT == timeToSleep) {
                            if (te.getStatusCode() == FORBIDDEN) {
                                logger.warn("This account is not in required role. ", te.getMessage());
                                closed = true;
                                for (StreamListener statusListener : this.streamListeners) {
                                    statusListener.onException(te);
                                }
                                break;
                            }
                            if (te.getStatusCode() == NOT_ACCEPTABLE) {
                                logger.warn("Parameter not accepted with the role. ", te.getMessage());
                                closed = true;
                                for (StreamListener statusListener : streamListeners) {
                                    statusListener.onException(te);
                                }
                                break;
                            }
                            connected = false;
                            for (ConnectionLifeCycleListener listener : lifeCycleListeners) {
                                try {
                                    listener.onDisconnect();
                                } catch (Exception e) {
                                    logger.warn(e.getMessage());
                                }
                            }
                            if (te.getStatusCode() > 200) {
                                timeToSleep = HTTP_ERROR_INITIAL_WAIT;
                            } else if (0 == timeToSleep) {
                                timeToSleep = TCP_ERROR_INITIAL_WAIT;
                            }
                        }
                        if (te.getStatusCode() > 200 && timeToSleep < HTTP_ERROR_INITIAL_WAIT) {
                            timeToSleep = HTTP_ERROR_INITIAL_WAIT;
                        }
                        if (connected) {
                            for (ConnectionLifeCycleListener listener : lifeCycleListeners) {
                                try {
                                    listener.onDisconnect();
                                } catch (Exception e) {
                                    logger.warn(e.getMessage());
                                }
                            }
                        }
                        for (StreamListener statusListener : streamListeners) {
                            statusListener.onException(te);
                        }
                        // there was a problem establishing the connection, or the connection closed by peer
                        if (!closed) {
                            // wait for a moment not to overload Twitter API
                            logger.info("Waiting for " + (timeToSleep) + " milliseconds");
                            setStatus("[Waiting for " + (timeToSleep) + " milliseconds]");
                            try {
                                Thread.sleep(timeToSleep);
                            } catch (InterruptedException ignore) {
                            }
                            timeToSleep = Math.min(timeToSleep * 2, (te.getStatusCode() > 200) ? HTTP_ERROR_WAIT_CAP : TCP_ERROR_WAIT_CAP);
                        }
                        stream = null;
                        logger.debug(te.getMessage());
                        connected = false;
                    }
                }
            }
            if (this.stream != null && connected) {
                try {
                    this.stream.close();
                } catch (IOException ignore) {
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn(e.getMessage());
                } finally {
                    for (ConnectionLifeCycleListener listener : lifeCycleListeners) {
                        try {
                            listener.onDisconnect();
                        } catch (Exception e) {
                            logger.warn(e.getMessage());
                        }
                    }
                }
            }
            for (ConnectionLifeCycleListener listener : lifeCycleListeners) {
                try {
                    listener.onCleanUp();
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                }
            }
        }

        public synchronized void close() {
            setStatus("[Disposing thread]");
            closed = true;
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignore) {
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn(e.getMessage());
                }
            }
        }

        private void setStatus(String message) {
            String actualMessage = NAME + message;
            setName(actualMessage);
            logger.debug(actualMessage);
        }

        abstract StatusStream getStream() throws TwitterException;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TwitterStreamImpl that = (TwitterStreamImpl) o;

        if (handler != null ? !handler.equals(that.handler) : that.handler != null) return false;
        if (http != null ? !http.equals(that.http) : that.http != null) return false;
        if (lifeCycleListeners != null ? !lifeCycleListeners.equals(that.lifeCycleListeners) : that.lifeCycleListeners != null)
            return false;
        if (stallWarningsGetParam != null ? !stallWarningsGetParam.equals(that.stallWarningsGetParam) : that.stallWarningsGetParam != null)
            return false;
        if (stallWarningsParam != null ? !stallWarningsParam.equals(that.stallWarningsParam) : that.stallWarningsParam != null)
            return false;
        if (streamListeners != null ? !streamListeners.equals(that.streamListeners) : that.streamListeners != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (http != null ? http.hashCode() : 0);
        result = 31 * result + (lifeCycleListeners != null ? lifeCycleListeners.hashCode() : 0);
        result = 31 * result + (handler != null ? handler.hashCode() : 0);
        result = 31 * result + (stallWarningsGetParam != null ? stallWarningsGetParam.hashCode() : 0);
        result = 31 * result + (stallWarningsParam != null ? stallWarningsParam.hashCode() : 0);
        result = 31 * result + (streamListeners != null ? streamListeners.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TwitterStreamImpl{" +
                "http=" + http +
                ", lifeCycleListeners=" + lifeCycleListeners +
                ", handler=" + handler +
                ", stallWarningsGetParam='" + stallWarningsGetParam + '\'' +
                ", stallWarningsParam=" + stallWarningsParam +
                ", streamListeners=" + streamListeners +
                '}';
    }
}

class StreamingReadTimeoutConfiguration implements HttpClientConfiguration {
    final Configuration nestedConf;

    StreamingReadTimeoutConfiguration(Configuration httpConf) {
        this.nestedConf = httpConf;
    }

    @Override
    public String getHttpProxyHost() {
        return nestedConf.getHttpClientConfiguration().getHttpProxyHost();
    }

    @Override
    public int getHttpProxyPort() {
        return nestedConf.getHttpClientConfiguration().getHttpProxyPort();
    }

    @Override
    public String getHttpProxyUser() {
        return nestedConf.getHttpClientConfiguration().getHttpProxyUser();
    }

    @Override
    public String getHttpProxyPassword() {
        return nestedConf.getHttpClientConfiguration().getHttpProxyPassword();
    }

    @Override
    public int getHttpConnectionTimeout() {
        return nestedConf.getHttpClientConfiguration().getHttpConnectionTimeout();
    }

    @Override
    public int getHttpReadTimeout() {
        // this is the trick that overrides connection timeout
        return nestedConf.getHttpStreamingReadTimeout();
    }

    @Override
    public int getHttpRetryCount() {
        return nestedConf.getHttpClientConfiguration().getHttpRetryCount();
    }

    @Override
    public int getHttpRetryIntervalSeconds() {
        return nestedConf.getHttpClientConfiguration().getHttpRetryIntervalSeconds();
    }

    @Override
    public boolean isPrettyDebugEnabled() {
        return nestedConf.getHttpClientConfiguration().isPrettyDebugEnabled();
    }

    @Override
    public boolean isGZIPEnabled() {
        return nestedConf.getHttpClientConfiguration().isGZIPEnabled();
    }
}
