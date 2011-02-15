/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.async.DispatcherFactory;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpClientWrapperConfiguration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A java representation of the <a href="http://dev.twitter.com/pages/streaming_api_methods">Streaming API: Methods</a><br>
 * Note that this class is NOT compatible with Google App Engine as GAE is not capable of handling requests longer than 30 seconds.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
public final class TwitterStream extends TwitterOAuthSupportBaseImpl {
    private static final long serialVersionUID = 5529611191443189901L;
    private final HttpClientWrapper http;
    private static final Logger logger = Logger.getLogger(TwitterStream.class);

    private StreamListener[] streamListeners = new StreamListener[0];
    private List<ConnectionLifeCycleListener> lifeCycleListeners = new ArrayList<ConnectionLifeCycleListener>(0);
    private TwitterStreamConsumer handler = null;

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     *
     * @deprecated use {@link TwitterStreamFactory#getInstance()} instead.
     */
    public TwitterStream() {
        super(ConfigurationContext.getInstance());
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
    }

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     *
     * @param screenName screen name
     * @param password   password
     * @deprecated use {@link TwitterStreamFactory#getInstance()} instead.
     */
    public TwitterStream(String screenName, String password) {
        super(ConfigurationContext.getInstance(), screenName, password);
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
    }

    /**
     * Constructs a TwitterStream instance. UserID and password should be provided by either twitter4j.properties or system property.
     * since Twitter4J 2.0.10
     *
     * @param screenName screen name
     * @param password   password
     * @param listener   listener
     * @deprecated use {@link TwitterStreamFactory#getInstance()} instead.
     */
    public TwitterStream(String screenName, String password, StreamListener listener) {
        super(ConfigurationContext.getInstance(), screenName, password);
        if (null != listener) {
            addListener(listener);
        }
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
    }

    /*package*/
    TwitterStream(Configuration conf, Authorization auth, StreamListener listener) {
        super(conf, auth);
        if (null != listener) {
            addListener(listener);
        }
        http = new HttpClientWrapper(new StreamingReadTimeoutConfiguration(conf));
    }

    /* Streaming API */

    /**
     * Starts listening on all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @see twitter4j.StatusStream
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-firehose">Streaming API: Methods statuses/firehose</a>
     * @since Twitter4J 2.0.4
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
     * Returns a status stream of all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-firehose">Streaming API: Methods statuses/firehose</a>
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
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-links">Streaming API: Methods statuses/links</a>
     * @since Twitter4J 2.1.1
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
     * Returns a status stream of all public statuses containing links. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the links stream. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-links">Streaming API: Methods statuses/links</a>
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
     * @param count       Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @deprecated
     */
    public void stream(final String relativeUrl, final int count, final boolean handleUserStream) {
        ensureAuthorizationEnabled();
        ensureListenerIsSet();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer() {
            public StatusStream getStream() throws TwitterException {
                return getCountStream(relativeUrl, count);
            }
        });
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
     * Starts listening on all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @see twitter4j.StatusStream
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-retweet">Streaming API: Methods statuses/retweet</a>
     * @since Twitter4J 2.0.10
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
     * Returns a stream of all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-retweet">Streaming API: Methods statuses/retweet</a>
     * @since Twitter4J 2.0.10
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
     * Starts listening on random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @see twitter4j.StatusStream
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-sample">Streaming API: Methods statuses/sample</a>
     * @since Twitter4J 2.0.10
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
     * Returns a stream of random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-sample">Streaming API: Methods statuses/sample</a>
     * @since Twitter4J 2.0.10
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
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @throws IllegalStateException when non-UserStreamListener is set, or no listener is set
     * @see <a href="http://dev.twitter.com/pages/user_streams">User Streams</a>
     */
    public void user() {
        user(null);
    }

    /**
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @param track keywords to track
     * @throws IllegalStateException when non-UserStreamListener is set, or no listener is set
     * @see <a href="http://dev.twitter.com/pages/user_streams">User Streams</a>
     * @since Twitter4J 2.1.9
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
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @return UserStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/pages/user_streams">User Streams</a>
     */
    public UserStream getUserStream() throws TwitterException {
        return getUserStream(null);
    }

    /**
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @param track keywords to track
     * @return UserStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/pages/user_streams">User Streams</a>
     * @since Twitter4J 2.1.9
     */
    public UserStream getUserStream(String[] track) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            List<HttpParameter> params = new ArrayList<HttpParameter>();
            if (conf.isUserStreamRepliesAllEnabled()) {
                params.add(new HttpParameter("replies", "all"));
            }
            if (null != track) {
                params.add(new HttpParameter("track", StringUtil.join(track)));
            }
            return new UserStreamImpl(getDispatcher(), http.post(conf.getUserStreamBaseURL() + "user.json"
                    , params.toArray(new HttpParameter[params.size()])
                    , auth), conf);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    /**
     * Site Streams, a new feature on the Streaming API, is now available for beta testing. Site Streams allows services, such as web sites or mobile push services, to receive real-time updates for a large number of users without any of the hassles of managing REST API rate limits. The initial version delivers events created by, or directed to, users that have shared their OAuth token with your application. The following events are streamed immediately, and without rate limits: Home Timelines, Mentions Timelines, User Timelines, Direct Messages, Mentions, Follows, Favorites, Tweets, Retweets, Profile changes, and List changes.
     * The following limitations must be respected during the beta period. These limitations may be changed with little advance notice. We intend to increase or remove these various limitations as we move from beta test into full production:<br>
     * Limit the follow count to 100 users per stream. Clients must occasionally compact users onto a smaller number of connections to minimize the total number of connections outstanding.<br>
     * Open no more than 25 new connections per second and exponentially back-off on errors.
     *
     * @param withFollowings whether to receive status updates from people following
     * @param follow         an array of users to include in the stream
     * @see <a href="http://dev.twitter.com/pages/site_streams">Site Streams | dev.twitter.com</a>
     * @since Twitter4J 2.1.8
     */
    public void site(final boolean withFollowings, final int[] follow) {
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
        if (null == TwitterStream.dispatcher) {
            synchronized (TwitterStream.class) {
                if (null == TwitterStream.dispatcher) {
                    // dispatcher is held statically, but it'll be instantiated with
                    // the configuration instance associated with this TwitterStream
                    // instance which invokes getDispatcher() on the first time.
                    TwitterStream.dispatcher = new DispatcherFactory(conf).getInstance();
                }
            }
        }
        return TwitterStream.dispatcher;
    }

    private static transient Dispatcher dispatcher;

    /**
     * Shuts down internal dispatcher thread shared by all TwitterStream instances.<br>
     *
     * @since Twitter4J 2.1.9
     */
    @Override
    public synchronized void shutdown() {
        super.shutdown();
        cleanUp();
        synchronized (TwitterStream.class) {
            if (0 == numberOfHandlers) {
                if (dispatcher != null) {
                    dispatcher.shutdown();
                    dispatcher = null;
                }
            }
        }
    }

    InputStream getSiteStream(boolean withFollowings, int[] follow) throws TwitterException {
        ensureOAuthEnabled();
        return http.post(conf.getSiteStreamBaseURL() + "site.json",
                new HttpParameter[]{
                        new HttpParameter("with", withFollowings ? "followings" : "user")
                        , new HttpParameter("follow", StringUtil.join(follow))}
                , auth).asStream();
    }

    /**
     * Start consuming public statuses that match one or more filter predicates. At least one predicate parameter, follow, locations, or track must be specified. Multiple parameters may be specified which allows most clients to use a single connection to the Streaming API. Placing long parameters in the URL may cause the request to be rejected for excessive URL length.<br>
     * The default access level allows up to 200 track keywords, 400 follow userids and 10 1-degree location boxes. Increased access levels allow 80,000 follow userids ("shadow" role), 400,000 follow userids ("birddog" role), 10,000 track keywords ("restricted track" role),  200,000 track keywords ("partner track" role), and 200 10-degree location boxes ("locRestricted" role). Increased track access levels also pass a higher proportion of statuses before limiting the stream.
     *
     * @param query Filter query
     * @see twitter4j.StatusStream
     * @see <a href="http://dev.twitter.com/pages/streaming_api_methods#statuses-filter">Streaming API: Methods statuses/filter</a>
     * @since Twitter4J 2.1.2
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
            return new StatusStreamImpl(getDispatcher(), http.post(conf.getStreamBaseURL()
                    + "statuses/filter.json"
                    , query.asHttpParameterArray(), auth), conf);
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
        ensureListenerIsSet();
        ensureStatusStreamListenerIsSet();
        startHandler(new TwitterStreamConsumer() {
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
     * shutdown internal stream consuming thread
     *
     * @since Twitter4J 2.1.7
     */
    public synchronized void cleanUp() {
        if (null != handler) {
            handler.close();
            numberOfHandlers--;
        }
    }

    /**
     * @deprecated use #cleanUp instead
     */
    public void cleanup() {
        cleanUp();
    }

    /**
     * Adds a ConnectionLifeCycleListener
     *
     * @param listener listener to be added
     * @since Twitter4J 2.1.7
     */
    public void addConnectionLifeCycleListener(ConnectionLifeCycleListener listener) {
        this.lifeCycleListeners.add(listener);
    }

    /**
     * Clear existing listeners and sets a StatusListener
     *
     * @param listener listener to be set
     * @deprecated use {@link #addListener(StatusListener)} instead.
     */
    public void setStatusListener(StatusListener listener) {
        this.streamListeners = new StreamListener[1];
        this.streamListeners[0] = listener;
    }

    /**
     * @param statusListener listener to be added
     * @since Twitter4J 2.1.7
     * @deprecated use {@link #addListener(StatusListener)} instead.
     */
    public void addStatusListener(StatusListener statusListener) {
        addListener((StreamListener) statusListener);
    }

    /**
     * Clear existing listeners and sets a UserStreamListener
     *
     * @param listener listener to be set
     * @deprecated use {@link #addListener(UserStreamListener)} instead.
     */
    public void setUserStreamListener(UserStreamListener listener) {
        this.streamListeners = new StreamListener[1];
        this.streamListeners[0] = listener;
    }

    /**
     * @param userStreamListener listener to be added
     * @since Twitter4J 2.1.7
     * @deprecated use {@link #addListener(UserStreamListener)} instead.
     */
    public void addUserStreamListener(UserStreamListener userStreamListener) {
        addListener((StreamListener) userStreamListener);
    }

    /**
     * @param listener
     * @since Twitter4J 2.1.8
     */
    public void addListener(UserStreamListener listener) {
        addListener((StreamListener) listener);
    }

    /**
     * @param listener
     * @since Twitter4J 2.1.8
     */
    public void addListener(StatusListener listener) {
        addListener((StreamListener) listener);
    }

    /**
     * @param listener
     * @since Twitter4J 2.1.8
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
            if (null != this.stream && connected) {
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
                if (null != stream) {
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

        TwitterStream that = (TwitterStream) o;

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
