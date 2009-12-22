package twitter4j.api;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public interface StatusMethods
{
	/**
	 * Returns a single status, specified by the id parameter. The status's author will be returned inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/show
	 *
	 * @param id the numerical ID of the status you're trying to retrieve
	 * @return a single status
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0show">Twitter API Wiki / Twitter REST API Method: statuses show</a>
	 */
	Status showStatus(long id) throws TwitterException;

	/**
	 * Updates the user's status.
	 * The text will be trimed if the length of the text is exceeding 160 characters.
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status the text of your status update
	 * @return the latest status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
	 */
	Status updateStatus(String status) throws TwitterException;

	/**
	 * Updates the user's status.
	 * The text will be trimed if the length of the text is exceeding 160 characters.
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status the text of your status update
	 * @param latitude The location's latitude that this tweet refers to.
	 * @param longitude The location's longitude that this tweet refers to.
	 * @return the latest status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
	 */
	Status updateStatus(String status, double latitude, double longitude) throws TwitterException;

	/**
	 * Updates the user's status.
	 * The text will be trimed if the length of the text is exceeding 160 characters.
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status            the text of your status update
	 * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
	 * @return the latest status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
	 */
	Status updateStatus(String status, long inReplyToStatusId)
			throws TwitterException;

	/**
	 * Updates the user's status.
	 * The text will be trimed if the length of the text is exceeding 160 characters.
	 * <br>This method calls http://api.twitter.com/1/statuses/update
	 *
	 * @param status            the text of your status update
	 * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
	 * @param latitude The location's latitude that this tweet refers to.
	 * @param longitude The location's longitude that this tweet refers to.
	 * @return the latest status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
	 */
	Status updateStatus(String status, long inReplyToStatusId, double latitude, double longitude)
			throws TwitterException;

	/**
	 * Destroys the status specified by the required ID parameter.  The authenticating user must be the author of the specified status.
	 * <br>This method calls http://api.twitter.com/1/statuses/destroy
	 *
	 * @param statusId The ID of the status to destroy.
	 * @return the deleted status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since 1.0.5
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: statuses destroy</a>
	 */
	Status destroyStatus(long statusId)
			throws TwitterException;

	/**
	 * Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweet
	 *
	 * @param statusId The ID of the status to retweet.
	 * @return the retweeted status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweet">Twitter API Wiki / Twitter REST API Method: statuses retweet</a>
	 */
	Status retweetStatus(long statusId)
			throws TwitterException;

	/**
	 * Returns up to 100 of the first retweets of a given tweet.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweets
	 *
	 * @param statusId The numerical ID of the tweet you want the retweets of.
	 * @return the retweets of a given tweet
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets">Twitter API Wiki / Twitter REST API Method: statuses retweets</a>
	 */
	ResponseList<Status> getRetweets(long statusId)
			throws TwitterException;
}
