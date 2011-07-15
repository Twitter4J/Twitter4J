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
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.async.DispatcherFactory;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpClientWrapperConfiguration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.z_T4JInternalStringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static twitter4j.internal.http.HttpResponseCode.FORBIDDEN;
import static twitter4j.internal.http.HttpResponseCode.NOT_ACCEPTABLE;

/**
 * A java representation of the <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods</a><br>
 * Note that this class is NOT compatible with Google App Engine as GAE is not capable of handling requests longer than 30 seconds.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
class TwitterStreamImpl extends TwitterBaseImpl implements TwitterStream {
    private static final long serialVersionUID = 5529611191443189901L;
    private final HttpClientWrapper http;
    private static final Logger logger = Logger.getLogger(TwitterStreamImpl.class);

    private StreamListener[] streamListeners = new StreamListener[0];
    private List<ConnectionLifeCycleListener> lifeCycleListeners = new ArrayList<ConnectionLifeCycleListener>(0);
    private TwitterStreamConsumer handler = null;

    /*package*/
    TwitterStreamImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
    }

    /* Streaming API */

    /**
     * {@inheritDoc}
     */
    public void firehose(final int count) {
        ensureAuthorizationEnabled();
        ensureListenerIsSet();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer() {
            public StatusStream getStream() throws TwitterException {
                return getFirehoseStream(count);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public StatusStream getFirehoseStream(int count) throws TwitterException {
        ensureAuthorizationEnabled();
        return getCountStream("statuses/firehose.json", count);
    }

    /**
     * {@inheritDoc}
     */
    public void links(final int count) {
        ensureAuthorizationEnabled();
        ensureListenerIsSet();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer() {
            public StatusStream getStream() throws TwitterException {
                return getLinksStream(count);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public StatusStream getLinksStream(int count) throws TwitterException {
        ensureAuthorizationEnabled();
        return getCountStream("statuses/links.json", count);
    }

    private StatusStream getCountStream(String relativeUrl, int count) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.post(conf.getStreamBaseURL() + relativeUrl
                    , new HttpParameter[]{new HttpParameter("count", String.valueOf(count))}, auth), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void retweet() {
        ensureAuthorizationEnabled();
        ensureListenerIsSet();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer() {
            public StatusStream getStream() throws TwitterException {
                return getRetweetStream();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public StatusStream getRetweetStream() throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.post(conf.getStreamBaseURL() + "statuses/retweet.json"
                    , new HttpParameter[]{}, auth), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sample() {
        ensureAuthorizationEnabled();
        ensureListenerIsSet();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer() {
            public StatusStream getStream() throws TwitterException {
                return getSampleStream();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public StatusStream getSampleStream() throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.get(conf.getStreamBaseURL() + "statuses/sample.json"
                    , auth), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void user() {
        user(null);
    }

    /**
     * {@inheritDoc}
     */
    public void user(final String[] track) {
        ensureAuthorizationEnabled();
        ensureListenerIsSet();
        for (StreamListener listener : streamListeners) {
            if (!(listener instanceof UserStreamListener)) {
                throw new IllegalStateException("Only UserStreamListener is supported. found: " + listener.getClass());
            }
        }
        startHandler(new TwitterStreamConsumer() {
            public UserStream getStream() throws TwitterException {
                return getUserStream(track);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public UserStream getUserStream() throws TwitterException {
        return getUserStream(null);
    }

    /**
     * {@inheritDoc}
     */
    public UserStream getUserStream(String[] track) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            List<HttpParameter> params = new ArrayList<HttpParameter>();
            if (conf.isUserStreamRepliesAllEnabled()) {
                params.add(new HttpParameter("replies", "all"));
            }
            if (track != null) {
                params.add(new HttpParameter("track", z_T4JInternalStringUtil.join(track)));
            }
            return new UserStreamImpl(getDispatcher(), http.post(conf.getUserStreamBaseURL() + "user.json"
                    , params.toArray(new HttpParameter[params.size()])
                    , auth), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void site(final boolean withFollowings, final long[] follow) {
        ensureOAuthEnabled();
        ensureListenerIsSet();
        for (StreamListener listener : streamListeners) {
            if (!(listener instanceof SiteStreamsListener)) {
                throw new IllegalStateException("Only SiteStreamListener is supported. found: " + listener.getClass());
            }
        }
        startHandler(new TwitterStreamConsumer() {
            public StreamImplementation getStream() throws TwitterException {
                try {
                    return new SiteStreamsImpl(getDispatcher(), getSiteStream(withFollowings, follow), conf);
                } catch (IOException e) {
                    throw new TwitterException(e);
                }
            }
        });
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

    private static transient Dispatcher dispatcher;

    InputStream getSiteStream(boolean withFollowings, long[] follow) throws TwitterException {
        ensureOAuthEnabled();
        return http.post(conf.getSiteStreamBaseURL() + "site.json",
                new HttpParameter[]{
                        new HttpParameter("with", withFollowings ? "followings" : "user")
                        , new HttpParameter("follow", z_T4JInternalStringUtil.join(follow))}
                , auth).asStream();
    }

    /**
     * {@inheritDoc}
     */
    public void filter(final FilterQuery query) {
        ensureAuthorizationEnabled();
        ensureListenerIsSet();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer() {
            public StatusStream getStream() throws TwitterException {
                return getFilterStream(query);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public StatusStream getFilterStream(FilterQuery query) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return new StatusStreamImpl(getDispatcher(), http.post(conf.getStreamBaseURL()
                    + "statuses/filter.json"
                    , query.asHttpParameterArray(), auth), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }


    /**
     * check if any listener is set. Throws IllegalStateException if no listener is set.
     *
     * @throws IllegalStateException when no listener is set.
     */

    private void ensureListenerIsSet() {
        if (streamListeners.length == 0) {
            throw new IllegalStateException("No listener is set.");
        }
    }

    private void ensureStatusStreamListenerIsSet() {
        for (StreamListener listener : streamListeners) {
            if (!(listener instanceof StatusListener)) {
                throw new IllegalStateException("Only StatusListener is supported. found: " + listener.getClass());
            }
        }
    }

    private static int numberOfHandlers = 0;

    private synchronized void startHandler(TwitterStreamConsumer handler) {
        cleanUp();
        if (streamListeners.length == 0) {
            throw new IllegalStateException("StatusListener is not set.");
        }
        this.handler = handler;
        this.handler.start();
        numberOfHandlers++;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void cleanUp() {
        if (handler != null) {
            handler.close();
            numberOfHandlers--;
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void shutdown() {
        super.shutdown();
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

    /**
     * {@inheritDoc}
     */
    public void addConnectionLifeCycleListener(ConnectionLifeCycleListener listener) {
        this.lifeCycleListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(UserStreamListener listener) {
        addListener((StreamListener) listener);
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(StatusListener listener) {
        addListener((StreamListener) listener);
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(SiteStreamsListener listener) {
        addListener((StreamListener) listener);
    }

    private synchronized void addListener(StreamListener listener) {
        StreamListener[] newListeners = new StreamListener[this.streamListeners.length + 1];
        System.arraycopy(this.streamListeners, 0, newListeners, 0, this.streamListeners.length);
        newListeners[newListeners.length - 1] = listener;
        this.streamListeners = newListeners;
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

    static int count = 0;

    abstract class TwitterStreamConsumer extends Thread {
        private StreamImplementation stream = null;
        private final String NAME = "Twitter Stream consumer-" + (++count);
        private volatile boolean closed = false;

        TwitterStreamConsumer() {
            super();
            setName(NAME + "[initializing]");
        }

        public void run() {
            int timeToSleep = NO_WAIT;
            boolean connected = false;
            while (!closed) {
                try {
                    if (!closed && null == stream) {
                        // try establishing connection
                        logger.info("Establishing connection.");
                        setStatus("[Establishing connection]");
                        stream = getStream();
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
                                stream.next(streamListeners);
                            } catch (IllegalStateException ise) {
                                logger.warn(ise.getMessage());
                                break;
                            } catch (TwitterException e) {
                                logger.info(e.getMessage());
                                stream.onException(e);
                                throw e;
                            } catch (Exception e) {
                                logger.info(e.getMessage());
                                stream.onException(e);
                                closed = true;
                                break;
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
                                break;
                            }
                            if (te.getStatusCode() == NOT_ACCEPTABLE) {
                                logger.warn("Parameter not accepted with the role. ", te.getMessage());
                                closed = true;
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
                        for (StreamListener statusListener : streamListeners) {
                            statusListener.onException(te);
                        }
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
            try {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignore) {
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.warn(e.getMessage());
                    }
                }
            } finally {
                closed = true;
            }
        }

        private void setStatus(String message) {
            String actualMessage = NAME + message;
            setName(actualMessage);
            logger.debug(actualMessage);
        }

        abstract StreamImplementation getStream() throws TwitterException;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TwitterStreamImpl that = (TwitterStreamImpl) o;

        if (handler != null ? !handler.equals(that.handler) : that.handler != null)
            return false;
        if (http != null ? !http.equals(that.http) : that.http != null)
            return false;
        if (lifeCycleListeners != null ? !lifeCycleListeners.equals(that.lifeCycleListeners) : that.lifeCycleListeners != null)
            return false;
        if (!Arrays.equals(streamListeners, that.streamListeners))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (http != null ? http.hashCode() : 0);
        result = 31 * result + (streamListeners != null ? Arrays.hashCode(streamListeners) : 0);
        result = 31 * result + (lifeCycleListeners != null ? lifeCycleListeners.hashCode() : 0);
        result = 31 * result + (handler != null ? handler.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TwitterStreamImpl{" +
                "http=" + http +
                ", streamListeners=" + (streamListeners == null ? null : Arrays.asList(streamListeners)) +
                ", lifeCycleListeners=" + lifeCycleListeners +
                ", handler=" + handler +
                '}';
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

    public boolean isPrettyDebugEnabled() {
        return nestedConf.isPrettyDebugEnabled();
    }

    public boolean isGZIPEnabled() {
        return nestedConf.isGZIPEnabled();
    }

}