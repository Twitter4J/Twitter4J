package twitter4j.api;

import twitter4j.TwitterListener;

public interface BlockMethodsAsync
{
	/**
	 * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
	 *
	 * @param screenName the screen_name of the user to block
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-blocks%C2%A0create">Twitter API Wiki / Twitter REST API Method: blocks%C2%A0create</a>
	 */
	void createBlockAsync(String screenName, TwitterListener listener);

	/**
	 * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
	 *
	 * @param userId the screen_name of the user to block
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-blocks%C2%A0create">Twitter API Wiki / Twitter REST API Method: blocks%C2%A0create</a>
	 */
	void createBlockAsync(int userId, TwitterListener listener);

	/**
	 * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
	 *
	 * @param screenName the screen_name of the user to block
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-blocks%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: blocks%C2%A0destroy</a>
	 */
	void destroyBlockAsync(String screenName, TwitterListener listener);

	/**
	 * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
	 *
	 * @param userId the ID of the user to block
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-blocks%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: blocks%C2%A0destroy</a>
	 */
	void destroyBlockAsync(int userId, TwitterListener listener);

	/**
	 * Tests if a friendship exists between two users.
	 * <br>This method calls http://api.twitter.com/1/blocks/exists/id.xml
	 *
	 * @param screenName The screen_name of the potentially blocked user.
	 * @since Twitter4J 2.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-exists">Twitter API Wiki / Twitter REST API Method: blocks exists</a>
	 */
	void existsBlockAsync(String screenName, TwitterListener listener);

	/**
	 * Tests if a friendship exists between two users.
	 * <br>This method calls http://api.twitter.com/1/blocks/exists/id.xml
	 *
	 * @param userId The ID of the potentially blocked user.
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-exists">Twitter API Wiki / Twitter REST API Method: blocks exists</a>
	 */
	void existsBlockAsync(int userId, TwitterListener listener);

	/**
	 * Returns a list of user objects that the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking.xml
	 *
	 * @since Twitter4J 2.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking">Twitter API Wiki / Twitter REST API Method: blocks blocking</a>
	 */
	void getBlockingUsersAsync(TwitterListener listener);

	/**
	 * Returns a list of user objects that the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking.xml
	 *
	 * @param page the number of page
	 * @since Twitter4J 2.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking">Twitter API Wiki / Twitter REST API Method: blocks blocking</a>
	 */
	void getBlockingUsersAsync(int page, TwitterListener listener);

	/**
	 * Returns an array of numeric user ids the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking/ids
	 * @since Twitter4J 2.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking-ids">Twitter API Wiki / Twitter REST API Method: blocks blocking ids</a>
	 */
	void getBlockingUsersIDsAsync(TwitterListener listener);
}
