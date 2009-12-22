package twitter4j.api;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

public interface BlockMethods
{
	/**
	 * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create/[id].json
	 *
	 * @param screenName the screen_name of the user to block
	 * @return the blocked user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks%C2%A0create">Twitter API Wiki / Twitter REST API Method: blocks create</a>
	 */
	User createBlock(String screenName) throws TwitterException;

	/**
	 * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create/[id].json
	 *
	 * @param userId the ID of the user to block
	 * @return the blocked user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks%C2%A0create">Twitter API Wiki / Twitter REST API Method: blocks create</a>
	 */
	User createBlock(int userId) throws TwitterException;

	/**
	 * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/destroy/[id].json
	 *
	 * @param screen_name the screen_name of the user to block
	 * @return the unblocked user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: blocks destroy</a>
	 */
	User destroyBlock(String screen_name) throws TwitterException;

	/**
	 * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/destroy/[id].json
	 *
	 * @param userId the ID of the user to block
	 * @return the unblocked user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: blocks destroy</a>
	 */
	User destroyBlock(int userId) throws TwitterException;

	/**
	 * Tests if a friendship exists between two users.
	 * <br>This method calls http://api.twitter.com/1/blocks/exists/[id].json
	 *
	 * @param screenName The screen_name of the potentially blocked user.
	 * @return  if the authenticating user is blocking a target user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-exists">Twitter API Wiki / Twitter REST API Method: blocks exists</a>
	 */
	boolean existsBlock(String screenName) throws TwitterException;

	/**
	 * Tests if a friendship exists between two users.
	 * <br>This method calls http://api.twitter.com/1/blocks/exists/[id].json
	 *
	 * @param userId The ID of the potentially blocked user.
	 * @return  if the authenticating user is blocking a target user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-exists">Twitter API Wiki / Twitter REST API Method: blocks exists</a>
	 */
	boolean existsBlock(int userId) throws TwitterException;

	/**
	 * Returns a list of user objects that the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking.json
	 *
	 * @return a list of user objects that the authenticating user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking">Twitter API Wiki / Twitter REST API Method: blocks blocking</a>
	 */
	ResponseList<User> getBlockingUsers() throws TwitterException;

	/**
	 * Returns a list of user objects that the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking.json
	 *
	 * @param page the number of page
	 * @return a list of user objects that the authenticating user
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking">Twitter API Wiki / Twitter REST API Method: blocks blocking</a>
	 */
	ResponseList<User> getBlockingUsers(int page) throws TwitterException;

	/**
	 * Returns an array of numeric user ids the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking/ids
	 * @return Returns an array of numeric user ids the authenticating user is blocking.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking-ids">Twitter API Wiki / Twitter REST API Method: blocks blocking ids</a>
	 */
	IDs getBlockingUsersIDs() throws TwitterException;
}
