package twitter4j.api;

import twitter4j.PagableResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

public interface ListMembersMethods
{
	/**
	 * Returns the members of the specified list.
	 * <br>This method calls http://api.twitter.com/1/[user]/[list_id]/members.json
	 * @param user The id of the user to get the member of the list.
	 * @param listId The id of the list
	 * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the members of the specified list.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-members">Twitter REST API Method: GET /:user/:list_id/members</a>
	 * @since Twitter4J 2.1.0
	 */
	PagableResponseList<User> getUserListMembers(String user, int listId, long cursor)
			throws TwitterException;

	/**
	 * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
	 * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members.json
	 * @param listId The id of the list.
	 * @param userId The id of the user to add as a member of the list.
	 * @return the updated list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-POST-list-members">Twitter REST API Method: POST /:user/:id/members</a>
	 * @since Twitter4J 2.1.0
	 */
	UserList addUserListMember(int listId, int userId)
			throws TwitterException;

	/**
	 * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
	 * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members.json
	 * @param listId The id of the list.
	 * @param userId The id of the member you wish to remove from the list.
	 * @return the updated list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-DELETE-list-members">Twitter REST API Method: DELETE /:user/:list_id/members</a>
	 * @since Twitter4J 2.1.0
	 */
	UserList deleteUserListMember(int listId, int userId)
			throws TwitterException;

	/**
	 * Check if a user is a member of the specified list.<br>
	 * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members/id.json
	 * @param listOwner The id of the user who owns the list.
	 * @param listId The id of the list.
	 * @param userId The id of the user who you want to know is a member or not of the specified list.
	 * @return the updated list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-members-id">Twitter REST API Method:  GET /:user/:list_id/members/:id</a>
	 * @since Twitter4J 2.1.0
	 */
	User checkUserListMembership(String listOwner, int listId, int userId)
			throws TwitterException;
}
