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
     * @see <a href="http://dev.twitter.com/doc/get/statuses/show/:id">GET statuses/show/:id | dev.twitter.com</a>
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
     * @see <a href="http://dev.twitter.com/doc/post/statuses/update">POST statuses/update | dev.twitter.com</a>
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
     * @see <a href="http://dev.twitter.com/doc/post/statuses/update">POST statuses/update | dev.twitter.com</a>
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
     * @see <a href="http://dev.twitter.com/doc/post/statuses/destroy/:id">POST statuses/destroy/:id | dev.twitter.com</a>
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
     * @see <a href="http://dev.twitter.com/doc/post/statuses/retweet/:id">POST statuses/retweet/:id | dev.twitter.com</a>
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
     * @see <a href="http://dev.twitter.com/doc/get/statuses/retweets/:id">Tweets Resources › statuses/retweets/:id</a>
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
     * @see <a href="http://dev.twitter.com/doc/get/statuses/:id/retweeted_by">GET statuses/:id/retweeted_by | dev.twitter.com</a>
     * @since Twitter4J 2.1.3
     * @deprecated use {@link #getRetweetedBy(long, twitter4j.Paging)} instead
     */
    ResponseList<User> getRetweetedBy(long statusId) throws TwitterException;

    /**
     * Show user objects of up to 100 members who retweeted the status.
     * <br>This method calls http://api.twitter.com/1/statuses/:id/retweeted_by
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @param paging controls pagination. Supports count and page parameters.
     * @return the list of users who retweeted your status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/statuses/:id/retweeted_by">GET statuses/:id/retweeted_by | dev.twitter.com</a>
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
     * @see <a href="http://dev.twitter.com/doc/get/statuses/:id/retweeted_by/ids">GET statuses/:id/retweeted_by/ids | dev.twitter.com</a>
     * @since Twitter4J 2.1.3
     * @deprecated use {@link #getRetweetedByIDs(long, twitter4j.Paging)} instead
     */
    IDs getRetweetedByIDs(long statusId) throws TwitterException;

    /**
     * Show user ids of up to 100 users who retweeted the status represented by id
     * <br />This method calls http://api.twitter.com/1/statuses/:id/retweeted_by/ids.format
     *
     * @param statusId The ID of the status you want to get retweeters of
     * @return IDs of users who retweeted the stats
     * @param paging controls pagination. Supports count and page parameters.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/statuses/:id/retweeted_by/ids">GET statuses/:id/retweeted_by/ids | dev.twitter.com</a>
     * @since Twitter4J 2.2.3
     */
    IDs getRetweetedByIDs(long statusId, Paging paging) throws TwitterException;
}
