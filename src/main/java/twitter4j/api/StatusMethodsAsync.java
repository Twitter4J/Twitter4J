package twitter4j.api;

import twitter4j.TwitterListener;

public interface StatusMethodsAsync
{
	/**
	 * Returns a single status, specified by the id parameter. The status's author will be returned inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/show
	 * @param id int
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0show">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0show</a>
	 */
	void showStatusAsync(long id, TwitterListener listener);

	/**
	 *
	 * Updates the user's status asynchronously
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status String
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0update</a>
	 * @since Twitter4J 2.0.1
	 */
	void updateStatusAsync(String status, TwitterListener listener);

	/**
	 *
	 * Updates the user's status asynchronously
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status String
	 * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0update</a>
	 * @since Twitter4J 2.0.1
	 */
	void updateStatusAsync(String status, long inReplyToStatusId, TwitterListener listener);

	/**
	 * Destroys the status specified by the required ID parameter. asynchronously
	 * <br>This method calls http://api.twitter.com/1/statuses/destroy
	 *
	 * @param statusId String
	 * @param listener a listener object that receives the response
	 * @since 1.1.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: statuses%C2%A0destroy</a>
	 */
	void destroyStatusAsync(long statusId, TwitterListener listener);

	/**
	 * Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweet
	 * @param statusId The ID of the status to retweet.
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweet">Twitter API Wiki / Twitter REST API Method: statuses retweet</a>
	 */
	void retweetStatusAsync(long statusId, TwitterListener listener);

	// TODO: getRetweetsAsync?
}
