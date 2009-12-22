package twitter4j.api;

import twitter4j.PagableResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

public interface ListSubscribersMethods
{
	/**
	 * Returns the subscribers of the specified list.
	 * <br>This method calls http://api.twitter.com/1/[user]/[list_id]/subscribers.json
	 * @param listOwner The id of the user to get the subscriber of the list.
	 * @param listId The id of the list
	 * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the members of the specified list.
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-subscribers">Twitter REST API Method: GET /:user/:list_id/members</a>
	 * @since Twitter4J 2.1.0
	 */
	PagableResponseList<User> getUserListSubscribers(String listOwner, int listId, long cursor)
			throws TwitterException;

	/**
	 * Make the authenticated user follow the specified list.
	 * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/subscribers.json
	 * @param listOwner The user name of the list.
	 * @param listId The id of the list.
	 * @return the updated list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-POST-list-subscribers">Twitter REST API Method: POST /:user/:id/members</a>
	 * @since Twitter4J 2.1.0
	 */
	UserList subscribeUserList(String listOwner, int listId)
			throws TwitterException;

	/**
	 * Unsubscribes the authenticated user form the specified list.
	 * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/subscribers.json
	 * @param listOwner The id of the user to get the subscriber of the list.
	 * @param listId The id of the list.
	 * @return the updated list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-DELETE-list-subscribers">Twitter REST API Method: DELETE /:user/:list_id/members</a>
	 * @since Twitter4J 2.1.0
	 */
	UserList unsubscribeUserList(String listOwner, int listId)
			throws TwitterException;

	/**
	 * Check if the specified user is a subscriber of the specified list.
	 * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/subscribers/[userId].json
	 * @param listOwner The id of the user to get the subscriber of the list.
	 * @param listId The id of the list.
	 * @param userId The id of the user who you want to know is a member or not of the specified list.
	 * @return the updated list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-subscribers-id">Twitter REST API Method: GET /:user/:list_id/subscribers/:id</a>
	 * @since Twitter4J 2.1.0
	 */
	User checkUserListSubscription(String listOwner, int listId, int userId)
			throws TwitterException;
}
