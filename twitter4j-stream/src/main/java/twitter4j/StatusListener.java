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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
public interface StatusListener extends StreamListener {
    void onStatus(Status status);

    /**
     * Called upon deletionNotice notices. Clients are urged to honor deletionNotice requests and discard deleted statuses immediately. At times, status deletionNotice messages may arrive before the status. Even in this case, the late arriving status should be deleted from your backing store.
     *
     * @param statusDeletionNotice the deletionNotice notice
     * @see <a href="https://dev.twitter.com/docs/streaming-api/concepts#parsing-responses">Streaming API Concepts | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void onDeletionNotice(StatusDeletionNotice statusDeletionNotice);

    /**
     * This notice will be sent each time a limited stream becomes unlimited.<br>
     * If this number is high and or rapidly increasing, it is an indication that your predicate is too broad, and you should consider a predicate with higher selectivity.
     *
     * @param numberOfLimitedStatuses an enumeration of statuses that matched the track predicate but were administratively limited.
     * @see <a href="https://dev.twitter.com/docs/streaming-api/concepts#filter-limiting">Streaming API Concepts - Filter Limiting | Twitter Developers</a>
     * @see <a href="https://dev.twitter.com/docs/streaming-api/concepts#parsing-responses">Streaming API Concepts - Parsing Responses | Twitter Developers</a>
     * @see <a href="http://groups.google.co.jp/group/twitter-development-talk/browse_thread/thread/15d0504b3dd7b939">Twitter Development Talk - Track API Limit message meaning</a>
     * @since Twitter4J 2.1.0
     */
    void onTrackLimitationNotice(int numberOfLimitedStatuses);

    /**
     * Called upon location deletion messages. Clients are urged to honor deletion requests and remove appropriate geolocation information from both the display and your backing store immediately. Note that in some cases the location deletion message may arrive before a tweet that lies within the deletion range arrives. You should still strip the location data.
     *
     * @param userId       user id
     * @param upToStatusId up to status id
     * @since Twitter4J 2.1.9
     */
    void onScrubGeo(long userId, long upToStatusId);


    /**
     * Called when receiving stall warnings.
     *
     * @param warning StallWaning
     * @see <a href="https://dev.twitter.com/docs/streaming-apis/parameters#stall_warnings">Streaming API request parameters - stall_warnings</a>
     * @since Twitter4J 3.0.0
     */
    void onStallWarning(StallWarning warning);
}
