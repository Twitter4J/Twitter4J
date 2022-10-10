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

import twitter4j.v1.FilterQuery;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface TwitterStream {

    /**
     * Starts listening on all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/firehose</a>
     * @since Twitter4J 2.0.4
     * @return this instance
     */
    @SuppressWarnings("UnusedReturnValue")
    TwitterStream firehose(final int count);

    /**
     * Starts listening on all public statuses containing links. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the links stream. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/links</a>
     * @since Twitter4J 2.1.1
     * @return this instance
     */
    @SuppressWarnings("UnusedReturnValue")
    TwitterStream links(final int count);

    /**
     * Starts listening on all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/retweet</a>
     * @since Twitter4J 2.0.10
     * @return this instance
     */
    TwitterStream retweet();

    /**
     * Starts listening on random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @see StatusStream
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods statuses/sample</a>
     * @since Twitter4J 2.0.10
     * @return this instance
     */
    TwitterStream sample();

    /**
     * Starts listening on random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     * <p>
     * Only samples Tweets written in the given language.
     *
     * @param language language to be sampled
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API: Methods statuses/sample</a>
     * @since Twitter4J 2.0.10
     * @return this instance
     */
    @SuppressWarnings("unused")
    TwitterStream sample(final String language);

    /**
     * Start consuming public statuses that match one or more filter predicates. At least one predicate parameter, follow, locations, or track must be specified. Multiple parameters may be specified which allows most clients to use a single connection to the Streaming API. Placing long parameters in the URL may cause the request to be rejected for excessive URL length.<br>
     * The default access level allows up to 200 track keywords, 400 follow userids and 10 1-degree location boxes. Increased access levels allow 80,000 follow userids ("shadow" role), 400,000 follow userids ("birddog" role), 10,000 track keywords ("restricted track" role),  200,000 track keywords ("partner track" role), and 200 10-degree location boxes ("locRestricted" role). Increased track access levels also pass a higher proportion of statuses before limiting the stream.
     *
     * @param query Filter query
     * @see <a href="https://dev.twitter.com/docs/streaming-api/methods">Streaming API Methods statuses/filter</a>
     * @since Twitter4J 2.1.2
     * @return this instance
     */
    TwitterStream filter(final FilterQuery query);

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
