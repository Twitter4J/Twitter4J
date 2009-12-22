package twitter4j.api;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public interface FavoriteMethods
{
	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites.json
	 *
	 * @return List<Status>
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	ResponseList<Status> getFavorites()
			throws TwitterException;

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites.json
	 *
	 * @param page the number of page
	 * @return ResponseList<Status>
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	ResponseList<Status> getFavorites(int page)
			throws TwitterException;

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 *
	 * @param id the ID or screen name of the user for whom to request a list of favorite statuses
	 * @return ResponseList<Status>
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	ResponseList<Status> getFavorites(String id)
			throws TwitterException;

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites/[id].json
	 *
	 * @param id   the ID or screen name of the user for whom to request a list of favorite statuses
	 * @param page the number of page
	 * @return ResponseList<Status>
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 */
	ResponseList<Status> getFavorites(String id, int page)
			throws TwitterException;

	/**
	 * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
	 * <br>This method calls http://api.twitter.com/1/favorites/create/[id].json
	 *
	 * @param id the ID of the status to favorite
	 * @return Status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites%C2%A0create">Twitter API Wiki / Twitter REST API Method: favorites create</a>
	 */
	Status createFavorite(long id)
			throws TwitterException;

	/**
	 * Un-favorites the status specified in the ID parameter as the authenticating user.  Returns the un-favorited status in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/favorites/destroy/[id].json
	 *
	 * @param id the ID of the status to un-favorite
	 * @return Status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: favorites destroy</a>
	 */
	Status destroyFavorite(long id)
			throws TwitterException;
}
