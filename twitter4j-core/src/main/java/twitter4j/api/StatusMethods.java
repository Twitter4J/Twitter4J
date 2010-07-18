/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.api;

import twitter4j.GeoLocation;
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
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
     *
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/statuses/retweets_of_me">GET statuses/retweets_of_me | dev.twitter.com</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetsOfMe() throws TwitterException;

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/statuses/retweets_of_me">GET statuses/retweets_of_me | dev.twitter.com</a>
     * @since Twitter4J 2.0.10
     */
    ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException;

	/**
     * Returns a single status, specified by the id parameter below. The status's author will be returned inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/show
	 *
	 * @param id the numerical ID of the status you're trying to retrieve
	 * @return a single status
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
     * @see <a href="http://dev.twitter.com/doc/get/statuses/show">GET statuses/show | dev.twitter.com</a>
	 */
	Status showStatus(long id) throws TwitterException;

	/**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
     * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status the text of your status update
	 * @return the latest status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
     * @see <a href="http://dev.twitter.com/doc/post/statuses/update">POST statuses/update | dev.twitter.com</a>
     */
	Status updateStatus(String status) throws TwitterException;

	/**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status the text of your status update
	 * @param location The location that this tweet refers to.
	 * @return the latest status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
     * @see <a href="http://dev.twitter.com/doc/post/statuses/update">POST statuses/update | dev.twitter.com</a>
     * @deprecated use {@link #updateStatus(StatusUpdate)} instead.
	 */
	Status updateStatus(String status, GeoLocation location) throws TwitterException;

	/**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status            the text of your status update
	 * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
	 * @return the latest status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
     * @see <a href="http://dev.twitter.com/doc/post/statuses/update">POST statuses/update | dev.twitter.com</a>
     * @deprecated use {@link #updateStatus(StatusUpdate)} instead.
	 */
	Status updateStatus(String status, long inReplyToStatusId)
			throws TwitterException;

	/**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status            the text of your status update
	 * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
     * @param location The location that this tweet refers to.
	 * @return the latest status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
     * @see <a href="http://dev.twitter.com/doc/post/statuses/update">POST statuses/update | dev.twitter.com</a>
     * @deprecated use {@link #updateStatus(StatusUpdate)} instead.
	 */
	Status updateStatus(String status, long inReplyToStatusId, GeoLocation location)
			throws TwitterException;

    /**
     * Updates the authenticating user's status. A status update with text identical to the authenticating user's text identical to the authenticating user's current status will be ignored to prevent duplicates.
     * <br>This method calls http://api.twitter.com/1/statuses/update
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
	 * @since 1.0.5
     * @see <a href="http://dev.twitter.com/doc/post/statuses/destroy">POST statuses/destroy | dev.twitter.com</a>
	 */
    Status destroyStatus(long statusId) throws TwitterException;

    /**
     * Retweets a tweet. Returns the original tweet with retweet details embedded.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweet
	 *
	 * @param statusId The ID of the status to retweet.
	 * @return the retweeted status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
     * @see <a href="http://dev.twitter.com/doc/post/statuses/retweet/:id">POST statuses/retweet/:id | dev.twitter.com</a>
	 */
    Status retweetStatus(long statusId) throws TwitterException;

    /**
	 * Returns up to 100 of the first retweets of a given tweet.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweets
	 *
	 * @param statusId The numerical ID of the tweet you want the retweets of.
	 * @return the retweets of a given tweet
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://dev.twitter.com/doc/get/statuses/retweets">Tweets Resources › statuses/retweets</a>
	 */
    ResponseList<Status> getRetweets(long statusId) throws TwitterException;

    /**
     * Show user objects of up to 100 members who retweeted the status.
     * <br>This method calls http://api.twitter.com/1/statuses/id/retweeted_by
     * @param statusId The ID of the status you want to get retweeters of
     * @return the list of users who retweeted your status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.3
     * @see <a href="http://dev.twitter.com/doc/get/statuses/:id/retweeted_by">GET statuses/:id/retweeted_by | dev.twitter.com</a>
     */
    ResponseList<User> getRetweetedBy(long statusId) throws TwitterException;

    /**
     * Show user objects of up to 100 members who retweeted the status.
     * <br>This method calls http://api.twitter.com/1/statuses/id/retweeted_by
     * @param statusId The ID of the status you want to get retweeters of
     * @param paging specify your paging requirements
     * @return the list of users who retweeted your status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.3
     * @see <a href="http://dev.twitter.com/doc/get/statuses/:id/retweeted_by">GET statuses/:id/retweeted_by | dev.twitter.com</a>
     */
    ResponseList<User> getRetweetedBy(long statusId, Paging paging)
            throws TwitterException;

    /**
     * Show user ids of up to 100 users who retweeted the status represented by id
     * <br />This method calls http://api.twitter.com/1/statuses/id/retweeted_by/ids.format
     * @param statusId The ID of the status you want to get retweeters of
     * @return IDs of users who retweeted the stats
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.3
     * @see <a href="http://dev.twitter.com/doc/get/statuses/:id/retweeted_by/ids">GET statuses/:id/retweeted_by/ids | dev.twitter.com</a>
     */
    IDs getRetweetedByIDs(long statusId) throws TwitterException;

    /**
     * Show user ids of up to 100 users who retweeted the status.
     * <br />This method calls http://api.twitter.com/1/statuses/id/retweeted_by/ids.format
     * @param statusId The ID of the status you want to get retweeters of
     * @param paging specify your paging requirements
     * @return IDs of users who retweeted the stats
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.3
     * @see <a href="http://dev.twitter.com/doc/get/statuses/:id/retweeted_by/ids">GET statuses/:id/retweeted_by/ids | dev.twitter.com</a>
     */
    IDs getRetweetedByIDs(long statusId, Paging paging) throws TwitterException;
}
