/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface StatusMethodsAsync {
    /**
	 * Returns a single status, specified by the id parameter. The status's author will be returned inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/show
	 * @param id int
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0show">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0show</a>
	 */
	void showStatus(long id);

	/**
	 *
	 * Updates the user's status asynchronously
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status String
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0update</a>
	 * @since Twitter4J 2.0.1
	 */
	void updateStatus(String status);

    /**
     * Updates the user's status asynchronously.
     * <br>Statuses over 140 characters will be forcibly truncated.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status the text of your status update
     * @param location The location that this tweet refers to.
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
     */
    void updateStatus(String status, GeoLocation location);

	/**
	 *
	 * Updates the user's status asynchronously
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status String
	 * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0update</a>
	 * @since Twitter4J 2.0.1
	 */
	void updateStatus(String status, long inReplyToStatusId);

    /**
     * Updates the user's status.
     * <br>Statuses over 140 characters will be forcibly truncated.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status            the text of your status update
     * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
     * @param location The location that this tweet refers to.
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
     */
    void updateStatus(String status, long inReplyToStatusId, GeoLocation location);

	/**
	 * Destroys the status specified by the required ID parameter. asynchronously
	 * <br>This method calls http://api.twitter.com/1/statuses/destroy
	 *
	 * @param statusId String
	 * @since 1.1.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0destroy</a>
	 */
	void destroyStatus(long statusId);

	/**
	 * Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweet
	 * @param statusId The ID of the status to retweet.
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweet">Twitter API Wiki / Twitter REST API Method: statuses retweet</a>
	 */
	void retweetStatus(long statusId);

    /**
     * Returns up to 100 of the first retweets of a given tweet.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets
     *
     * @param statusId The numerical ID of the tweet you want the retweets of.
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets">Twitter API Wiki / Twitter REST API Method: statuses retweets</a>
     */
    void getRetweets(long statusId);
}
