package twitter4j.api;

import twitter4j.PagableResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

public interface UserMethods
{
	/**
	 * Returns extended information of a given user, specified by screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
	 * <br>This method calls http://api.twitter.com/1/users/show.json
	 *
	 * @param screenName the screen name of the user for whom to request the detail
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-users%C2%A0show">Twitter API Wiki / Twitter REST API Method: users show</a>
	 */
	User showUser(String screenName)
			throws TwitterException;

	/**
	 * Returns extended information of a given user, specified by ID.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
	 * <br>This method calls http://api.twitter.com/1/users/show
	 *
	 * @param userId the ID of the user for whom to request the detail
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-users%C2%A0show">Twitter API Wiki / Twitter REST API Method: users show</a>
	 * @since Twitter4J 2.1.0
	 */
	User showUser(int userId)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 */
	PagableResponseList<User> getFriendsStatuses()
			throws TwitterException;

	/**
	 * Returns the user's friends, each with current status inline.<br>
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 */
	PagableResponseList<User> getFriendsStatuses(long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param screenName the screen name of the user for whom to request a list of friends
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 * @since Twitter4J 2.0.9
	 */
	PagableResponseList<User> getFriendsStatuses(String screenName)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param userId the ID of the user for whom to request a list of friends
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 * @since Twitter4J 2.1.0
	 */
	PagableResponseList<User> getFriendsStatuses(int userId)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param screenName the screen name of the user for whom to request a list of friends
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 */
	PagableResponseList<User> getFriendsStatuses(String screenName, long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends
	 *
	 * @param userId the ID of the user for whom to request a list of friends
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of friends
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
	 */
	PagableResponseList<User> getFriendsStatuses(int userId, long cursor)
			throws TwitterException;


	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).<br>
	 * This method automatically provides a value of cursor=-1 to begin paging.
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 * @since Twitter4J 2.0.9
	 */
	PagableResponseList<User> getFollowersStatuses()
			throws TwitterException;

	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param screenName The screen name of the user for whom to request a list of followers.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(String screenName)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param userId The ID of the user for whom to request a list of followers.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(int userId)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param screenName The screen name of the user for whom to request a list of followers.
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.9
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(String screenName, long cursor)
			throws TwitterException;

	/**
	 * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
	 * <br>This method calls http://api.twitter.com/1/statuses/followers
	 *
	 * @param userId   The ID of the user for whom to request a list of followers.
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return List
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
	 */
	PagableResponseList<User> getFollowersStatuses(int userId, long cursor)
			throws TwitterException;
}
