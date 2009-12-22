package twitter4j.api;

import twitter4j.TwitterException;
import twitter4j.User;

public interface NotificationMethods
{
	/**
	 * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/follow/[id].json
	 *
	 * @param screenName Specifies the screen name of the user to follow with device updates.
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications follow</a>
	 */
	User enableNotification(String screenName)
			throws TwitterException;

	/**
	 * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/follow/[id].json
	 *
	 * @param userId Specifies the ID of the user to follow with device updates.
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications follow</a>
	 */
	User enableNotification(int userId)
			throws TwitterException;

	/**
	 * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/leave/[id].json
	 *
	 * @param screenName Specifies the screen name of the user to disable device notifications.
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications leave</a>
	 */
	User disableNotification(String screenName)
			throws TwitterException;

	/**
	 * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/leave/[id].json
	 *
	 * @param userId Specifies the ID of the user to disable device notifications.
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications leave</a>
	 */
	User disableNotification(int userId)
			throws TwitterException;
}
