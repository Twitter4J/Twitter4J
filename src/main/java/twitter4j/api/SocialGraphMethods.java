package twitter4j.api;

import twitter4j.IDs;
import twitter4j.TwitterException;

public interface SocialGraphMethods
{
	/**
	 * Returns an array of numeric IDs for every user the authenticating user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids.json
	 * @return an array of numeric IDs for every user the authenticating user is following
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
	 */
	IDs getFriendsIDs()
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the authenticating user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids.json
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return an array of numeric IDs for every user the authenticating user is following
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
	 */
	IDs getFriendsIDs(long cursor)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is following.<br>
	 * all IDs are attempted to be returned, but large sets of IDs will likely fail with timeout errors.
	 * <br>This method calls http://api.twitter.com/1/friends/ids.json
	 * @param userId Specfies the ID of the user for whom to return the friends list.
	 * @return an array of numeric IDs for every user the specified user is following
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
	 */
	IDs getFriendsIDs(int userId)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids.json
	 * @param userId Specifies the ID of the user for whom to return the friends list.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return an array of numeric IDs for every user the specified user is following
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
	 */
	IDs getFriendsIDs(int userId, long cursor)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids.json
	 * @param screenName Specfies the screen name of the user for whom to return the friends list.
	 * @return an array of numeric IDs for every user the specified user is following
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.0
	 * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
	 */
	IDs getFriendsIDs(String screenName)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids.json
	 * @param screenName Specfies the screen name of the user for whom to return the friends list.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return an array of numeric IDs for every user the specified user is following
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
	 */
	IDs getFriendsIDs(String screenName, long cursor)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids.json
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
	 */
	IDs getFollowersIDs()
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids.json
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
	 */
	IDs getFollowersIDs(long cursor)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids.json
	 * @param userId Specfies the ID of the user for whom to return the followers list.
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
	 */
	IDs getFollowersIDs(int userId)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids.json
	 * @param userId Specifies the ID of the user for whom to return the followers list.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
	 */
	IDs getFollowersIDs(int userId, long cursor)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids.json
	 * @param screenName Specfies the screen name of the user for whom to return the followers list.
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
	 */
	IDs getFollowersIDs(String screenName)
			throws TwitterException;

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids.json
	 *
	 * @param screenName Specfies the screen name of the user for whom to return the followers list.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
	 */
	IDs getFollowersIDs(String screenName, long cursor)
			throws TwitterException;
}
