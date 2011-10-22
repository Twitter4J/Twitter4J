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

import twitter4j.Paging;
import twitter4j.StatusUpdate;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface StatusMethodsAsync {
    /**
     * Returns a single status, specified by the id parameter below. The status's author will be returned inline.
     * <br>This method calls http://api.twitter.com/1/statuses/show
     *
     * @param id int
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/show/:id">GET statuses/show/:id | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void showStatus(long id);

    /**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status String
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void updateStatus(String status);

    /**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
     * <br>Statuses over 140 characters will be forcibly truncated.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status the latest status to be updated.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/update">POST statuses/update | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void updateStatus(StatusUpdate status);

    /**
     * Destroys the status specified by the required ID parameter.<br>
     * Usage note: The authenticating user must be the author of the specified status.
     * <br>This method calls http://api.twitter.com/1/statuses/destroy
     *
     * @param statusId String
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/destroy/:id">POST statuses/destroy/:id | Twitter Developers</a>
     * @since 1.1.2
     */
    void destroyStatus(long statusId);

    /**
     * Retweets a tweet. Returns the original tweet with retweet details embedded.
     * <br>This method calls http://api.twitter.com/1/statuses/retweet
     *
     * @param statusId The ID of the status to retweet.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/statuses/retweet/:id">POST statuses/retweet/:id | Twitter Developers</a>
     * @since Twitter4J 2.0.10
     */
    void retweetStatus(long statusId);

    /**
     * Returns up to 100 of the first retweets of a given tweet.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets
     *
     * @param statusId The numerical ID of the tweet you want the retweets of.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/retweets/:id">Tweets Resources › statuses/retweets/:id</a>
     * @since Twitter4J 2.1.0
     */
    void getRetweets(long statusId);

    /**
     * Show user objects of up to 100 members who retweeted the status.
     * <br>This method calls http://api.twitter.com/1/statuses/:id/retweeted_by
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/:id/retweeted_by">GET statuses/:id/retweeted_by | Twitter Developers</a>
     * @since Twitter4J 2.1.3
     * @deprecated use {@link #getRetweetedBy(long, twitter4j.Paging)} instead
     */
    void getRetweetedBy(long statusId);

    /**
     * Show user objects of up to 100 members who retweeted the status.
     * <br>This method calls http://api.twitter.com/1/statuses/:id/retweeted_by
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @param paging   controls pagination. Supports count and page parameters.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/:id/retweeted_by">GET statuses/:id/retweeted_by | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void getRetweetedBy(long statusId, Paging paging);

    /**
     * Show user ids of up to 100 users who retweeted the status represented by id
     * <br />This method calls http://api.twitter.com/1/statuses/:id/retweeted_by/ids.format
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/:id/retweeted_by/ids">GET statuses/:id/retweeted_by/ids | Twitter Developers</a>
     * @since Twitter4J 2.1.3
     * @deprecated use {@link #getRetweetedByIDs(long, twitter4j.Paging)} instead
     */
    void getRetweetedByIDs(long statusId);

    /**
     * Show user ids of up to 100 users who retweeted the status represented by id
     * <br />This method calls http://api.twitter.com/1/statuses/:id/retweeted_by/ids.format
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @param paging   controls pagination. Supports count and page parameters.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/statuses/:id/retweeted_by/ids">GET statuses/:id/retweeted_by/ids | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void getRetweetedByIDs(long statusId, Paging paging);
}
