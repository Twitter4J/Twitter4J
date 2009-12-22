package twitter4j.api;

import twitter4j.Paging;
import twitter4j.TwitterListener;

public interface DirectMessageMethodsAsync
{
	/**
	 * Returns a list of the direct messages sent to the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/direct_messages
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages">Twitter API Wiki / Twitter REST API Method: direct_messages</a>
	 */
	void getDirectMessagesAsync(TwitterListener listener);

	/**
	 * Returns a list of the direct messages sent to the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/direct_messages
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages">Twitter API Wiki / Twitter REST API Method: direct_messages</a>
	 */
	void getDirectMessagesAsync(Paging paging, TwitterListener listener);

	/**
	 * Returns a list of the direct messages sent by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/direct_messages/sent
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0sent">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0sent</a>
	 */
	void getSentDirectMessagesAsync(TwitterListener listener);

	/**
	 * Returns a list of the direct messages sent by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/direct_messages/sent
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0sent">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0sent</a>
	 */
	void getSentDirectMessagesAsync(Paging paging, TwitterListener listener);

	/**
	 * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
	 * The text will be trimed if the length of the text is exceeding 140 characters.
	 * <br>This method calls http://api.twitter.com/1/direct_messages/new
	 * @param screenName the screen name of the user to whom send the direct message
	 * @param text The text of your direct message.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0new">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0new</a>
	 */
	void sendDirectMessageAsync(String screenName, String text, TwitterListener listener);

	/**
	 * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
	 * The text will be trimed if the length of the text is exceeding 140 characters.
	 * <br>This method calls http://api.twitter.com/1/direct_messages/new
	 * @param userId the screen name of the user to whom send the direct message
	 * @param text The text of your direct message.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0new">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0new</a>
	 * @since Twitter4j 2.1.0
	 */
	void sendDirectMessageAsync(int userId, String text, TwitterListener listener);

	/**
	 * Delete specified direct message
	 * <br>This method calls http://api.twitter.com/1/direct_messages/destroy
	 * @param id int
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0destroy</a>
	 * @since Twitter4J 2.0.1
	 */
	void destroyDirectMessageAsync(int id, TwitterListener listener);
}
