package twitter4j.api;

import twitter4j.TwitterListener;

public interface FavoriteMethodsAsync
{
	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFavoritesAsync(TwitterListener listener);

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites
	 * @param page number of page to retrieve favorites
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFavoritesAsync(int page, TwitterListener listener);

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites
	 * @param id the ID or screen name of the user for whom to request a list of favorite statuses
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFavoritesAsync(String id,TwitterListener listener);

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites
	 * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
	 * @param page retrieves the 20 next most recent favorite statuses.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFavoritesAsync(String id,int page, TwitterListener listener);

	/**
	 * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
	 * <br>This method calls http://api.twitter.com/1/favorites/create%C2%A0
	 *
	 * @param id       the ID or screen name of the user for whom to request a list of favorite statuses.
	 * @param listener a listener object that receives the response
	 * @since 1.1.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites%C2%A0create">Twitter API Wiki / Twitter REST API Method: favorites%C2%A0create</a>
	 */
	void createFavoriteAsync(long id, TwitterListener listener);

	/**
	 * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
	 * <br>This method calls http://api.twitter.com/1/favorites/destroy
	 *
	 * @param id       the ID or screen name of the user for whom to request a list of un-favorite statuses.
	 * @param listener a listener object that receives the response
	 * @since 1.1.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-favorites%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: favorites%C2%A0destroy</a>
	 */
	void destroyFavoriteAsync(long id, TwitterListener listener);
}
