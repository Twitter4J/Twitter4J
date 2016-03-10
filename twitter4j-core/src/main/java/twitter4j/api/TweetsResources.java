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

package twitter4j.api;

import twitter4j.*;

import java.io.File;
import java.io.InputStream;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface TweetsResources {
    /**
     * Returns up to 100 of the first retweets of a given tweet.
     * <br>This method calls https://api.twitter.com/1.1/statuses/retweets
     *
     * @param statusId The numerical ID of the tweet you want the retweets of.
     * @return the retweets of a given tweet
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/retweets/:id">Tweets Resources › statuses/retweets/:id</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweets(long statusId) throws TwitterException;

    /**
     * Returns a collection of up to 100 user IDs belonging to users who have
     * retweeted the tweet specified by the id parameter.
     * <br>This method calls https://api.twitter.com/1.1/get/statuses/retweeters/ids
     *
     * @param statusId The numerical ID of the tweet you want the retweeters of.
     * @param cursor   The cursor of the page to fetch. Use -1 to start.
     * @return the retweets of a given tweet
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/retweeters/ids">Tweets Resources › statuses/retweeters/ids</a>
     * @since Twitter4J 3.0.5
     */
    IDs getRetweeterIds(long statusId, long cursor) throws TwitterException;

    /**
     * Returns a collection of up to {@code count} user IDs belonging to users
     * who have retweeted the tweet specified by the id parameter.
     * <br>This method calls https://api.twitter.com/1.1/get/statuses/retweeters/ids
     *
     * @param statusId The numerical ID of the tweet you want the retweeters of.
     * @param count    The maximum number of retweeter IDs to retrieve. Must be
     *                 between 1 and 200, inclusive.
     * @param cursor   The cursor of the page to fetch. Use -1 to start.
     * @return the retweets of a given tweet
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/retweeters/ids">Tweets Resources › statuses/retweeters/ids</a>
     * @since Twitter4J 3.0.5
     */
    IDs getRetweeterIds(long statusId, int count, long cursor) throws TwitterException;

    /**
     * Returns a single status, specified by the id parameter below. The status's author will be returned inline.
     * <br>This method calls https://api.twitter.com/1.1/statuses/show
     *
     * @param id the numerical ID of the status you're trying to retrieve
     * @return a single status
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/show/:id">GET statuses/show/:id | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    Status showStatus(long id) throws TwitterException;

    /**
     * Destroys the status specified by the required ID parameter.<br>
     * Usage note: The authenticating user must be the author of the specified status.
     * <br>This method calls https://api.twitter.com/1.1/statuses/destroy
     *
     * @param statusId The ID of the status to destroy.
     * @return the deleted status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/statuses/destroy/:id">POST statuses/destroy/:id | Twitter Developers</a>
     * @since 1.0.5
     */
    Status destroyStatus(long statusId) throws TwitterException;

    /**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
     * <br>This method calls https://api.twitter.com/1.1/statuses/update
     *
     * @param status the text of your status update
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    Status updateStatus(String status) throws TwitterException;

    /**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
     * <br>This method calls https://api.twitter.com/1.1/statuses/update or<br>
     * This method calls https://upload.twitter.com/1/statuses/update_with_media
     *
     * @param latestStatus the latest status to be updated.
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    Status updateStatus(StatusUpdate latestStatus) throws TwitterException;

    /**
     * Retweets a tweet. Returns the original tweet with retweet details embedded.
     * <br>This method calls https://api.twitter.com/1.1/statuses/retweet
     *
     * @param statusId The ID of the status to retweet.
     * @return the retweeted status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/statuses/retweet/:id">POST statuses/retweet/:id | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    Status retweetStatus(long statusId) throws TwitterException;

    /**
     * Returns information allowing the creation of an embedded representation of a Tweet on third party sites. See the <a href="http://oembed.com/">oEmbed</a> specification for information about the response format.
     * While this endpoint allows a bit of customization for the final appearance of the embedded Tweet, be aware that the appearance of the rendered Tweet may change over time to be consistent with Twitter's <a href="https://dev.twitter.com/terms/display-requirements">Display Requirements</a>. Do not rely on any class or id parameters to stay constant in the returned markup.
     * <br>This method calls https://api.twitter.com/1.1/statuses/oembed.json
     *
     * @param req request
     * @return information allowing the creation of an embedded representation of a Tweet on third party sites
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/oembed">GET statuses/oembed | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    OEmbed getOEmbed(OEmbedRequest req) throws TwitterException;

    /**
     * Returns fully-hydrated tweet objects for up to 100 tweets per request, as specified by comma-separated values passed to the id parameter.
     * This method is especially useful to get the details (hydrate) a collection of Tweet IDs.
     * <br>This method calls https://api.twitter.com/1.1/statuses/lookup.json
     *
     * @param ids array of the ids to lookup
     * @return list of the tweets
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/lookup">GET statuses/lookup</a>
     * @since Twitter4J 4.0.2
     */
    ResponseList<Status> lookup(long... ids) throws TwitterException;

    /**
     * Uploads media image to be attached via {@link #updateStatus(twitter4j.StatusUpdate)}
     * <br>This method calls https://api.twitter.com/1.1/media/upload.json
     *
     * @param mediaFile the latest status to be updated.
     * @return upload result
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @see <a href="https://dev.twitter.com/docs/api/multiple-media-extended-entities">Multiple Media Entities in Statuses</a>
     * @since Twitter4J 4.0.2
     */
    UploadedMedia uploadMedia(File mediaFile) throws TwitterException;

    /**
     * Uploads media image to be attached via {@link #updateStatus(twitter4j.StatusUpdate)}
     * <br>This method calls https://api.twitter.com/1.1/media/upload.json
     *
     * @param fileName media file name
     * @param media media body as stream
     * @return upload result
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @see <a href="https://dev.twitter.com/docs/api/multiple-media-extended-entities">Multiple Media Entities in Statuses</a>
     * @since Twitter4J 4.0.3
     */
    UploadedMedia uploadMedia(String fileName, InputStream media) throws TwitterException;
}
