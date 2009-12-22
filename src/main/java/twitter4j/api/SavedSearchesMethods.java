package twitter4j.api;

import twitter4j.SavedSearch;
import twitter4j.TwitterException;

import java.util.List;

public interface SavedSearchesMethods
{
	/**
	 * Returns the authenticated user's saved search queries.
	 * <br>This method calls http://api.twitter.com/1/saved_searches.json
	 * @return Returns an array of numeric user ids the authenticating user is blocking.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.8
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches">Twitter API Wiki / Twitter REST API Method: saved_searches</a>
	 */
	List<SavedSearch> getSavedSearches()
			throws TwitterException;

	/**
	 * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
	 * <br>This method calls http://api.twitter.com/1/saved_searches/show/id.json
	 * @param id The id of the saved search to be retrieved.
	 * @return the data for a saved search
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.8
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-show">Twitter API Wiki / Twitter REST API Method: saved_searches show</a>
	 */
	SavedSearch showSavedSearch(int id)
			throws TwitterException;

	/**
	 * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
	 * <br>This method calls http://api.twitter.com/1/saved_searches/saved_searches/create.json
	 * @param query the query string
	 * @return the data for a created saved search
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.8
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-create">Twitter API Wiki / Twitter REST API Method: saved_searches create</a>
	 */
	SavedSearch createSavedSearch(String query)
			throws TwitterException;

	/**
	 * Destroys a saved search for the authenticated user. The search specified by id must be owned by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/saved_searches/destroy/id.json
	 * @param id The id of the saved search to be deleted.
	 * @return the data for a destroyed saved search
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.8
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-destroy">Twitter API Wiki / Twitter REST API Method: saved_searches destroy</a>
	 */
	SavedSearch destroySavedSearch(int id)
			throws TwitterException;
}
