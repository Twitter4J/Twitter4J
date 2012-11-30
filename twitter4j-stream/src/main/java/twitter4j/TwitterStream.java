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

import twitter4j.auth.OAuthSupport;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface TwitterStream extends OAuthSupport, TwitterBase {
    /**
     * Adds a ConnectionLifeCycleListener
     *
     * @param listener listener to be added
     * @since Twitter4J 2.1.7
     */
    void addConnectionLifeCycleListener(ConnectionLifeCycleListener listener);

    /**
     * @param listener listener to add
     * @since Twitter4J 2.1.8
     */
    void addListener(UserStreamListener listener);

    /**
     * @param listener listener to add
     * @since Twitter4J 2.1.8
     */
    void addListener(StatusListener listener);

    /**
     * @param listener listener to add
     * @since Twitter4J 2.1.8
     */
    void addListener(SiteStreamsListener listener);

    /**
     * @param listener listener to add
     * @since Twitter4J 3.0.2
     */
    void addListener(RawStreamListener listener);

    /**
     * Starts listening on all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/firehose</a>
     * @since Twitter4J 2.0.4
     */
    void firehose(final int count);

    /**
     * Returns a status stream of all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/firehose</a>
     * @since Twitter4J 2.0.4
     * @deprecated use {@link #firehose(int)} instead
     */
    StatusStream getFirehoseStream(int count) throws TwitterException;

    /**
     * Starts listening on all public statuses containing links. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the links stream. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/links</a>
     * @since Twitter4J 2.1.1
     */
    void links(final int count);

    /**
     * Returns a status stream of all public statuses containing links. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the links stream. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/links</a>
     * @since Twitter4J 2.1.1
     * @deprecated use {@link #links(int)} instead
     */
    StatusStream getLinksStream(int count) throws TwitterException;

    /**
     * Starts listening on all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/retweet</a>
     * @since Twitter4J 2.0.10
     */
    void retweet();

    /**
     * Returns a stream of all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods statuses/retweet</a>
     * @since Twitter4J 2.0.10
     * @deprecated use {@link #getRetweetStream()} instead
     */
    StatusStream getRetweetStream() throws TwitterException;

    /**
     * Starts listening on random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods statuses/sample</a>
     * @since Twitter4J 2.0.10
     */
    void sample();

    /**
     * Returns a stream of random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @return StatusStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods statuses/sample</a>
     * @since Twitter4J 2.0.10
     * @deprecated use {@link #sample()} instead
     */
    StatusStream getSampleStream() throws TwitterException;

    /**
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @throws IllegalStateException when non-UserStreamListener is set, or no listener is set
     * @see <a href="https://dev.twitter.com/docs/streaming-api/user-streams">User Streams</a>
     */
    void user();

    /**
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @param track keywords to track
     * @throws IllegalStateException when non-UserStreamListener is set, or no listener is set
     * @see <a href="https://dev.twitter.com/docs/streaming-api/user-streams">User Streams</a>
     * @since Twitter4J 2.1.9
     */
    void user(final String[] track);

    /**
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @return UserStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/streaming-api/user-streams">User Streams</a>
     * @deprecated use {@link #user()} instead
     */
    UserStream getUserStream() throws TwitterException;

    /**
     * User Streams provides real-time updates of all data needed to update a desktop application display. Applications can request startup back-fill from the REST API and then transition to Streaming for nearly all subsequent reads. Rate limits and latency are practically eliminated. Desktop developers can stop managing rate limits and use this new data to create an entirely new user experience. On our end, we hope to reduce costs and increase site reliability.
     *
     * @param track keywords to track
     * @return UserStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/streaming-api/user-streams">User Streams</a>
     * @since Twitter4J 2.1.9
     * @deprecated use {@link #user()} instead
     */
    UserStream getUserStream(String[] track) throws TwitterException;

    /**
     * Site Streams, a new feature on the Streaming API, is now available for beta testing. Site Streams allows services, such as web sites or mobile push services, to receive real-time updates for a large number of users without any of the hassles of managing REST API rate limits. The initial version delivers events created by, or directed to, users that have shared their OAuth token with your application. The following events are streamed immediately, and without rate limits: Home Timelines, Mentions Timelines, User Timelines, Direct Messages, Mentions, Follows, Favorites, Tweets, Retweets, Profile changes, and List changes.
     * The following limitations must be respected during the beta period. These limitations may be changed with little advance notice. We intend to increase or remove these various limitations as we move from beta test into full production:<br>
     * Limit the follow count to 100 users per stream. Clients must occasionally compact users onto a smaller number of connections to minimize the total number of connections outstanding.<br>
     * Open no more than 25 new connections per second and exponentially back-off on errors.
     *
     * @param withFollowings whether to receive status updates from people following
     * @param follow         an array of users to include in the stream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/site-streams">Site Streams | Twitter Developers</a>
     * @since Twitter4J 2.1.8
     */
    StreamController site(final boolean withFollowings, final long[] follow);

    /**
     * Start consuming public statuses that match one or more filter predicates. At least one predicate parameter, follow, locations, or track must be specified. Multiple parameters may be specified which allows most clients to use a single connection to the Streaming API. Placing long parameters in the URL may cause the request to be rejected for excessive URL length.<br>
     * The default access level allows up to 200 track keywords, 400 follow userids and 10 1-degree location boxes. Increased access levels allow 80,000 follow userids ("shadow" role), 400,000 follow userids ("birddog" role), 10,000 track keywords ("restricted track" role),  200,000 track keywords ("partner track" role), and 200 10-degree location boxes ("locRestricted" role). Increased track access levels also pass a higher proportion of statuses before limiting the stream.
     *
     * @param query Filter query
     * @see twitter4j.StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/filter</a>
     * @since Twitter4J 2.1.2
     */
    void filter(final FilterQuery query);

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
     * @deprecated use {@link #filter(FilterQuery)} instead
     */
    StatusStream getFilterStream(FilterQuery query) throws TwitterException;

    /**
     * shutdown internal stream consuming thread
     *
     * @since Twitter4J 2.1.7
     */
    void cleanUp();

    /**
     * Shuts down internal dispatcher thread shared by all TwitterStream instances.<br>
     *
     * @since Twitter4J 2.1.9
     */
    void shutdown();
}
