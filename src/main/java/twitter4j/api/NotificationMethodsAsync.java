package twitter4j.api;

import twitter4j.TwitterListener;

public interface NotificationMethodsAsync
{
	/**
	 * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/follow
	 *
	 * @param screenName Specifies the screen name of the user to follow with device updates.
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications%C2%A0follow</a>
	 */
	void enableNotificationAsync(String screenName, TwitterListener listener);

	/**
	 * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/follow
	 *
	 * @param userId Specifies the ID of the user to follow with device updates.
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications%C2%A0follow</a>
	 */
	void enableNotificationAsync(int userId, TwitterListener listener);

	/**
	 * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/leave
	 *
	 * @param screenName Specifies the screen name of the user to disable device notifications.
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications%C2%A0leave</a>
	 */
	void disableNotificationAsync(String screenName, TwitterListener listener);

	/**
	 * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/leave
	 *
	 * @param userId Specifies the ID of the user to disable device notifications.
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications%C2%A0leave</a>
	 */
	void disableNotificationAsync(int userId, TwitterListener listener);
}
