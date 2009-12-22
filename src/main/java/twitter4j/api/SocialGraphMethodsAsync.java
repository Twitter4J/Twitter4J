package twitter4j.api;

import twitter4j.TwitterListener;

public interface SocialGraphMethodsAsync
{
	/**
	 * Returns an array of numeric IDs for every user the authenticating user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
	 *
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
	 */
	void getFriendsIDsAsync(TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the authenticating user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
	 *
	 * @param listener a listener object that receives the response
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
	 * @since Twitter4J 2.0.10
	 */
	void getFriendsIDsAsync(long cursor, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
	 *
	 * @param userId   Specfies the ID of the user for whom to return the friends list.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
	 * @since Twitter4J 2.0.0
	 */
	void getFriendsIDsAsync(int userId, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
	 *
	 * @param userId   Specifies the ID of the user for whom to return the friends list.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
	 * @since Twitter4J 2.0.10
	 */
	void getFriendsIDsAsync(int userId, long cursor, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
	 *
	 * @param screenName Specifies the screen name of the user for whom to return the friends list.
	 * @param listener   a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
	 * @since Twitter4J 2.0.0
	 */
	void getFriendsIDsAsync(String screenName, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * <br>This method calls http://api.twitter.com/1/friends/ids%C2%A0%C2%A0
	 *
	 * @param screenName Specfies the screen name of the user for whom to return the friends list.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @param listener   a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends%C2%A0ids</a>
	 * @since Twitter4J 2.0.10
	 */
	void getFriendsIDsAsync(String screenName, long cursor, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids
	 *
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
	 * @since Twitter4J 2.0.0
	 */
	void getFollowersIDsAsync(TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids
	 *
	 * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
	 * @since Twitter4J 2.0.10
	 */
	void getFollowersIDsAsync(long cursor, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids
	 *
	 * @param userId   Specfies the ID of the user for whom to return the followers list.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
	 * @since Twitter4J 2.0.0
	 */
	void getFollowersIDsAsync(int userId, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids
	 *
	 * @param userId   Specfies the ID of the user for whom to return the followers list.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
	 * @since Twitter4J 2.0.10
	 */
	void getFollowersIDsAsync(int userId, long cursor, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids
	 *
	 * @param screenName Specfies the screen name of the user for whom to return the followers list.
	 * @param listener   a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
	 * @since Twitter4J 2.0.0
	 */
	void getFollowersIDsAsync(String screenName, TwitterListener listener);

	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * <br>This method calls http://api.twitter.com/1/followers/ids
	 *
	 * @param screenName Specfies the screen name of the user for whom to return the followers list.
	 * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @param listener   a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers%C2%A0ids</a>
	 * @since Twitter4J 2.0.10
	 */
	void getFollowersIDsAsync(String screenName, long cursor, TwitterListener listener);
}
