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

import java.io.File;
import java.io.InputStream;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface StatusMethods {
    /**
     * Returns a single status, specified by the id parameter below. The status's author will be returned inline.
     * <br>This method calls http://api.twitter.com/1/statuses/show
     *
     * @param id the numerical ID of the status you're trying to retrieve
     * @return a single status
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/show/:id">GET statuses/show/:id | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    Status showStatus(long id) throws TwitterException;

    /**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status the text of your status update
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    Status updateStatus(String status) throws TwitterException;

    /**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param latestStatus the latest status to be updated.
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    Status updateStatus(StatusUpdate latestStatus) throws TwitterException;

    /**
     * Destroys the status specified by the required ID parameter.<br>
     * Usage note: The authenticating user must be the author of the specified status.
     * <br>This method calls http://api.twitter.com/1/statuses/destroy
     *
     * @param statusId The ID of the status to destroy.
     * @return the deleted status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/destroy/:id">POST statuses/destroy/:id | Twitter Developers</a>
     * @since 1.0.5
     */
    Status destroyStatus(long statusId) throws TwitterException;

    /**
     * Retweets a tweet. Returns the original tweet with retweet details embedded.
     * <br>This method calls http://api.twitter.com/1/statuses/retweet
     *
     * @param statusId The ID of the status to retweet.
     * @return the retweeted status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/retweet/:id">POST statuses/retweet/:id | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    Status retweetStatus(long statusId) throws TwitterException;

    /**
     * Returns up to 100 of the first retweets of a given tweet.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets
     *
     * @param statusId The numerical ID of the tweet you want the retweets of.
     * @return the retweets of a given tweet
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/retweets/:id">Tweets Resources › statuses/retweets/:id</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweets(long statusId) throws TwitterException;

    /**
     * Show user objects of up to 100 members who retweeted the status.
     * <br>This method calls http://api.twitter.com/1/statuses/:id/retweeted_by
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @return the list of users who retweeted your status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/:id/retweeted_by">GET statuses/:id/retweeted_by | Twitter Developers</a>
     * @since Twitter4J 2.1.3
     * @deprecated use {@link #getRetweetedBy(long, twitter4j.Paging)} instead
     */
    ResponseList<User> getRetweetedBy(long statusId) throws TwitterException;

    /**
     * Show user objects of up to 100 members who retweeted the status.
     * <br>This method calls http://api.twitter.com/1/statuses/:id/retweeted_by
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @param paging   controls pagination. Supports count and page parameters.
     * @return the list of users who retweeted your status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/:id/retweeted_by">GET statuses/:id/retweeted_by | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    ResponseList<User> getRetweetedBy(long statusId, Paging paging) throws TwitterException;

    /**
     * Show user ids of up to 100 users who retweeted the status represented by id
     * <br />This method calls http://api.twitter.com/1/statuses/:id/retweeted_by/ids.format
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @return IDs of users who retweeted the stats
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/:id/retweeted_by/ids">GET statuses/:id/retweeted_by/ids | Twitter Developers</a>
     * @since Twitter4J 2.1.3
     * @deprecated use {@link #getRetweetedByIDs(long, twitter4j.Paging)} instead
     */
    IDs getRetweetedByIDs(long statusId) throws TwitterException;

    /**
     * Show user ids of up to 100 users who retweeted the status represented by id
     * <br />This method calls http://api.twitter.com/1/statuses/:id/retweeted_by/ids.format
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @param paging   controls pagination. Supports count and page parameters.
     * @return IDs of users who retweeted the stats
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/:id/retweeted_by/ids">GET statuses/:id/retweeted_by/ids | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    IDs getRetweetedByIDs(long statusId, Paging paging) throws TwitterException;

    /**
     * Updates the authenticating user's status and attaches media for upload. The Tweet text will be rewritten to include the media URL(s), which will reduce the number of characters allowed in the Tweet text. If the URL(s) cannot be appended without text truncation, the tweet will be rejected and this method will return an HTTP 403 error.
     * <br>This method calls http://upload.twitter.com/1/statuses/update_with_media
     *
     * @param status the text of your status update
     * @param possiblySensitive Set to true for content which may not be suitable for every audience
     * @param file the file of the media uploaded
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/update_with_media">POST statuses/update_with_media | Twitter Developers</a>
     * @since Twitter4J 2.2.5
     */
    Status updateStatusWithMedia(String status, boolean possiblySensitive, File file) throws TwitterException;

    /**
     * Updates the authenticating user's status and attaches media for upload. The Tweet text will be rewritten to include the media URL(s), which will reduce the number of characters allowed in the Tweet text. If the URL(s) cannot be appended without text truncation, the tweet will be rejected and this method will return an HTTP 403 error.
     * <br>This method calls http://upload.twitter.com/1/statuses/update_with_media
     *
     * @param status the text of your status update
     * @param possiblySensitive Set to true for content which may not be suitable for every audience
     * @param mediaFilename the filename of the media uploaded
     * @param mediaBody the body of the media uploaded
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/update_with_media">POST statuses/update_with_media | Twitter Developers</a>
     * @since Twitter4J 2.2.5
     */
    Status updateStatusWithMedia(String status, boolean possiblySensitive, String mediaFilename, InputStream mediaBody) throws TwitterException;

    /**
     * Updates the authenticating user's status and attaches media for upload. The Tweet text will be rewritten to include the media URL(s), which will reduce the number of characters allowed in the Tweet text. If the URL(s) cannot be appended without text truncation, the tweet will be rejected and this method will return an HTTP 403 error.
     * <br>This method calls http://upload.twitter.com/1/statuses/update_with_media
     *
     * @param latestStatus the latest status to be updated.
     * @param possiblySensitive Set to true for content which may not be suitable for every audience
     * @param file the file of the media uploaded
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @since Twitter4J 2.2.5
     */
    Status updateStatusWithMedia(StatusUpdate latestStatus, boolean possiblySensitive, File file) throws TwitterException;

    /**
     * Updates the authenticating user's status and attaches media for upload. The Tweet text will be rewritten to include the media URL(s), which will reduce the number of characters allowed in the Tweet text. If the URL(s) cannot be appended without text truncation, the tweet will be rejected and this method will return an HTTP 403 error.
     * <br>This method calls http://upload.twitter.com/1/statuses/update_with_media
     *
     * @param latestStatus the latest status to be updated.
     * @param possiblySensitive Set to true for content which may not be suitable for every audience
     * @param mediaFilename the filename of the media uploaded
     * @param mediaBody the body of the media uploaded
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @since Twitter4J 2.2.5
     */
    Status updateStatusWithMedia(StatusUpdate latestStatus, boolean possiblySensitive, String mediaFilename, InputStream mediaBody) throws TwitterException;
}
