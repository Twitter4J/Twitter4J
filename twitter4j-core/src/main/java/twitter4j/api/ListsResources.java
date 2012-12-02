/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j.api;

import twitter4j.*;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface ListsResources {
    /**
     * List the lists of the specified user. Private lists will be included if the authenticated users is the same as the user whose lists are being returned.
     * <br>This method calls http://api.twitter.com/1.1/lists.json
     *
     * @param listOwnerScreenName The screen name of the list owner
as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of lists
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists">GET lists | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    ResponseList<UserList> getUserLists(String listOwnerScreenName)
            throws TwitterException;

    /**
     * List the lists of the specified user. Private lists will be included if the authenticated users is the same as the user whose lists are being returned.
     * <br>This method calls http://api.twitter.com/1.1/lists.json
     *
     * @param listOwnerUserId The id of the list owner
     * @return the list of lists
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists">GET lists | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    ResponseList<UserList> getUserLists(long listOwnerUserId)
            throws TwitterException;


    /**
     * Show tweet timeline for members of the specified list.
     * <br>http://api.twitter.com/1/user/lists/list_id/statuses.json
     *
     * @param listId The id of the list
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of statuses for members of the specified list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/statuses">GET lists/statuses | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    ResponseList<Status> getUserListStatuses(int listId, Paging paging)
            throws TwitterException;

    /**
     * Show tweet timeline for members of the specified list.
     * <br>http://api.twitter.com/1/user/lists/list_id/statuses.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param paging  controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of statuses for members of the specified list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/statuses">GET lists/statuses | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<Status> getUserListStatuses(long ownerId, String slug, Paging paging)
            throws TwitterException;

    /**
     * Show tweet timeline for members of the specified list.
     * <br>http://api.twitter.com/1.1/lists/statuses.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param paging          controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of statuses for members of the specified list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/statuses">GET lists/statuses | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    ResponseList<Status> getUserListStatuses(String ownerScreenName, String slug, Paging paging)
            throws TwitterException;

    /**
     * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/destroy.json
     *
     * @param listId The id of the list.
     * @param userId The screen name of the member you wish to remove from the list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/destroy">POST lists/members/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    UserList destroyUserListMember(int listId, long userId) throws TwitterException;

    /**
     * @deprecated use {@link #destroyUserList(int)} instead
     */
    UserList deleteUserListMember(int listId, long userId) throws TwitterException;

    /**
     * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/destroy.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userId  The screen name of the member you wish to remove from the list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/destroy">POST lists/members/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList destroyUserListMember(long ownerId, String slug, long userId) throws TwitterException;

    /**
     * @deprecated use {@link #destroyUserListMember(long, String, long)} List(int)} instead
     */
    UserList deleteUserListMember(long ownerId, String slug, long userId) throws TwitterException;


    /**
     * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/destroy.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param userId          The screen name of the member you wish to remove from the list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/destroy">POST lists/members/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList destroyUserListMember(String ownerScreenName, String slug, long userId) throws TwitterException;

    /**
     * List the lists the authenticating user has been added to.
     * <br>This method calls http://api.twitter.com/1.1/lists/memberships.json
     *
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of lists
     * @throws TwitterException      when Twitter service or network is unavailable
     * @throws IllegalStateException when authorization is not enabled
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    PagableResponseList<UserList> getUserListMemberships(long cursor)
            throws TwitterException;

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls http://api.twitter.com/1.1/lists/memberships.json
     *
     * @param listMemberId The id of the list member
     * @param cursor       Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of lists
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor)
            throws TwitterException;

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls http://api.twitter.com/1.1/lists/memberships.json
     *
     * @param listMemberScreenName The screen name of the list member
     * @param cursor               Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of lists
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor)
            throws TwitterException;

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls http://api.twitter.com/1.1/lists/memberships.json
     *
     * @param listMemberScreenName The screen name of the list member
     * @param cursor               Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param filterToOwnedLists   Whether to return just lists the authenticating user owns, and the user represented by listMemberScreenName is a member of.
     * @return the list of lists
     * @throws TwitterException      when Twitter service or network is unavailable
     * @throws IllegalStateException when filerToOwnedLists is true but authorization is not enabled
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor, boolean filterToOwnedLists)
            throws TwitterException;

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls http://api.twitter.com/1.1/lists/memberships.json
     *
     * @param listMemberId       The id of the list member
     * @param cursor             Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param filterToOwnedLists Whether to return just lists the authenticating user owns, and the user represented by listMemberId is a member of.
     * @return the list of lists
     * @throws TwitterException      when Twitter service or network is unavailable
     * @throws IllegalStateException when filerToOwnedLists is true but authorization is not enabled
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor, boolean filterToOwnedLists)
            throws TwitterException;

    /**
     * Returns the subscribers of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/subscribers.json
     *
     * @param listId The id of the list
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the members of the specified list.
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers">GET lists/subscribers | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    PagableResponseList<User> getUserListSubscribers(int listId, long cursor) throws TwitterException;

    /**
     * Returns the subscribers of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/subscribers.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param cursor  Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the members of the specified list.
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers">GET lists/subscribers | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, long cursor) throws TwitterException;

    /**
     * Returns the subscribers of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/subscribers.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param cursor          Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the members of the specified list.
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers">GET lists/subscribers | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    PagableResponseList<User> getUserListSubscribers(String ownerScreenName, String slug, long cursor) throws TwitterException;

    /**
     * Make the authenticated user follow the specified list.
     * <br>This method calls http://api.twitter.com/1.1/list/subscribers/create.json
     *
     * @param listId The id of the list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/create">POST lists/subscribers/create | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    UserList createUserListSubscription(int listId) throws TwitterException;

    /**
     * Make the authenticated user follow the specified list.
     * <br>This method calls http://api.twitter.com/1.1/list/subscribers/create.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/create">POST lists/subscribers/create | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList createUserListSubscription(long ownerId, String slug) throws TwitterException;

    /**
     * Make the authenticated user follow the specified list.
     * <br>This method calls http://api.twitter.com/1.1/list/subscribers/create.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/create">POST lists/subscribers/create | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList createUserListSubscription(String ownerScreenName, String slug) throws TwitterException;

    /**
     * Check if the specified user is a subscriber of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/subscribers/show.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user who you want to know is a member or not of the specified list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     *                          , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers/show">GET lists/subscribers/show | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    User showUserListSubscription(int listId, long userId) throws TwitterException;

    /**
     * Check if the specified user is a subscriber of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/subscribers/show.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userId  The id of the user who you want to know is a member or not of the specified list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     *                          , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers/show">GET lists/subscribers/show | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    User showUserListSubscription(long ownerId, String slug, long userId) throws TwitterException;

    /**
     * Check if the specified user is a subscriber of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/subscribers/show.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param userId          The id of the user who you want to know is a member or not of the specified list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     *                          , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers/show">GET lists/subscribers/show | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    User showUserListSubscription(String ownerScreenName, String slug, long userId) throws TwitterException;

    /**
     * Unsubscribes the authenticated user form the specified list.
     * <br>This method calls http://api.twitter.com/1.1/subscribers/destroy.json
     *
     * @param listId The id of the list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/destroy">POST lists/subscribers/destroy | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    UserList destroyUserListSubscription(int listId) throws TwitterException;

    /**
     * Unsubscribes the authenticated user form the specified list.
     * <br>This method calls http://api.twitter.com/1.1/subscribers/destroy.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/destroy">POST lists/subscribers/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList destroyUserListSubscription(long ownerId, String slug) throws TwitterException;

    /**
     * Unsubscribes the authenticated user form the specified list.
     * <br>This method calls http://api.twitter.com/1.1/subscribers/destroy.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/destroy">POST lists/subscribers/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList destroyUserListSubscription(String ownerScreenName, String slug) throws TwitterException;

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param listId  The id of the list.
     * @param userIds The array of ids of the user to add as member of the list. up to 100 are allowed in a single request.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList createUserListMembers(int listId, long[] userIds) throws TwitterException;

    /**
     * @deprecated use {@link #createUserListMembers(int, long[])} instead
     */
    UserList addUserListMembers(int listId, long[] userIds) throws TwitterException;

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userIds The array of ids of the user to add as member of the list. up to 100 are allowed in a single request.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList createUserListMembers(long ownerId, String slug, long[] userIds) throws TwitterException;

    /**
     * @deprecated use {@link #createUserListMembers(int, long[])} instead
     */
    UserList addUserListMembers(long ownerId, String slug, long[] userIds) throws TwitterException;


    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param userIds         The array of ids of the user to add as member of the list. up to 100 are allowed in a single request.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList createUserListMembers(String ownerScreenName, String slug, long[] userIds) throws TwitterException;

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param listId      The id of the list.
     * @param screenNames The array of screen names of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    UserList createUserListMembers(int listId, String[] screenNames) throws TwitterException;

    /**
     * @deprecated use {@link #createUserListMembers(int, String[])} instead
     */
    UserList addUserListMembers(int listId, String[] screenNames) throws TwitterException;

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param ownerId     The user ID of the user who owns the list being requested by a slug.
     * @param slug        slug of the list
     * @param screenNames The array of screen names of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList createUserListMembers(long ownerId, String slug, String[] screenNames) throws TwitterException;

    /**
     * @deprecated use {@link #createUserListMembers(int, String[])} instead
     */
    UserList addUserListMembers(long ownerId, String slug, String[] screenNames) throws TwitterException;

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param screenNames     The array of screen names of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList createUserListMembers(String ownerScreenName, String slug, String[] screenNames) throws TwitterException;

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls http://api.twitter.com/1.1/lists/members/show.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user who you want to know is a member or not of the specified list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     *                          , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members/show">GET lists/members/show | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    User showUserListMembership(int listId, long userId) throws TwitterException;

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls http://api.twitter.com/1.1/lists/members/show.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userId  The id of the user who you want to know is a member or not of the specified list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     *                          , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members/show">GET lists/members/show | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    User showUserListMembership(long ownerId, String slug, long userId) throws TwitterException;

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls http://api.twitter.com/1.1/lists/members/show.json
     *
     * @param ownerScreenName Id The user ID of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param userId          The id of the user who you want to know is a member or not of the specified list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     *                          , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members/show">GET lists/members/show | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    User showUserListMembership(String ownerScreenName, String slug, long userId) throws TwitterException;

    /**
     * Returns the members of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/members.json
     *
     * @param listId The id of the list
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the members of the specified list.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members">GET lists/members | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    PagableResponseList<User> getUserListMembers(int listId, long cursor)
            throws TwitterException;

    /**
     * Returns the members of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/members.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param cursor  Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the members of the specified list.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members">GET lists/members | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    PagableResponseList<User> getUserListMembers(long ownerId, String slug, long cursor)
            throws TwitterException;

    /**
     * Returns the members of the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/members.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param cursor          Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the members of the specified list.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members">GET lists/members | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    PagableResponseList<User> getUserListMembers(String ownerScreenName, String slug, long cursor)
            throws TwitterException;

    /**
     * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user to add as a member of the list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create">POST lists/members/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    UserList createUserListMember(int listId, long userId) throws TwitterException;

    /**
     * @deprecated use {@link #createUserListMember(int, long)}} instead
     */
    UserList addUserListMember(int listId, long userId) throws TwitterException;

    /**
     * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userId  The id of the user to add as a member of the list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create">POST lists/members/create | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList createUserListMember(long ownerId, String slug, long userId) throws TwitterException;

    /**
     * @deprecated use {@link #createUserListMember(long, String, long)} instead
     */
    UserList addUserListMember(long ownerId, String slug, long userId) throws TwitterException;

    /**
     * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
     * <br>This method calls http://api.twitter.com/1.1/lists/members/create.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param userId          The id of the user to add as a member of the list.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create">POST lists/members/create | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList createUserListMember(String ownerScreenName, String slug, long userId) throws TwitterException;

    /**
     * Deletes the specified list. Must be owned by the authenticated user.
     * <br>This method calls http://api.twitter.com/1.1/lists/destroy.json
     *
     * @param listId The id of the list to delete
     * @return the deleted list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/destroy">POST lists/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    UserList destroyUserList(int listId) throws TwitterException;

    /**
     * Deletes the specified list. Must be owned by the authenticated user.
     * <br>This method calls http://api.twitter.com/1.1/lists/destroy.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @return the deleted list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/destroy">POST lists/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList destroyUserList(long ownerId, String slug) throws TwitterException;

    /**
     * Deletes the specified list. Must be owned by the authenticated user.
     * <br>This method calls http://api.twitter.com/1.1/lists/destroy.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @return the deleted list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/destroy">POST lists/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList destroyUserList(String ownerScreenName, String slug) throws TwitterException;

    /**
     * Updates the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/update.json
     *
     * @param listId         The id of the list to update.
     * @param newListName    What you'd like to change the list's name to.
     * @param isPublicList   Whether your list is public or private. Optional. Values can be public or private. Lists are public by default if no mode is specified.
     * @param newDescription What you'd like to change the list description to.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/update ">POST lists/update | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    UserList updateUserList(int listId, String newListName, boolean isPublicList, String newDescription)
            throws TwitterException;

    /**
     * Updates the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/update.json
     *
     * @param ownerId        The user ID of the user who owns the list being requested by a slug.
     * @param slug           slug of the list
     * @param newListName    What you'd like to change the list's name to.
     * @param isPublicList   Whether your list is public or private. Optional. Values can be public or private. Lists are public by default if no mode is specified.
     * @param newDescription What you'd like to change the list description to.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/update ">POST lists/update | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList updateUserList(long ownerId, String slug, String newListName, boolean isPublicList, String newDescription)
            throws TwitterException;

    /**
     * Updates the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/update.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @param newListName     What you'd like to change the list's name to.
     * @param isPublicList    Whether your list is public or private. Optional. Values can be public or private. Lists are public by default if no mode is specified.
     * @param newDescription  What you'd like to change the list description to.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/update ">POST lists/update | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList updateUserList(String ownerScreenName, String slug, String newListName, boolean isPublicList, String newDescription)
            throws TwitterException;

    /**
     * Creates a new list for the authenticated user. Accounts are limited to 20 lists.
     * <br>This method calls http://api.twitter.com/1.1/lists/create.json
     *
     * @param listName     The name of the list you are creating. Required.
     * @param isPublicList set true if you wish to make a public list
     * @param description  The description of the list you are creating. Optional.
     * @return the list that was created
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable, or the authenticated user already has 20 lists(TwitterException.getStatusCode() == 403).
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/create ">POST lists/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    UserList createUserList(String listName, boolean isPublicList, String description)
            throws TwitterException;

    /**
     * Show the specified list. Private lists will only be shown if the authenticated user owns the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/show.json
     *
     * @param listId The id of the list to show
     * @return the specified list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/show">https://dev.twitter.com/docs/api/1.1/get/lists/show | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    UserList showUserList(int listId) throws TwitterException;

    /**
     * Show the specified list. Private lists will only be shown if the authenticated user owns the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/show.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @return the specified list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/show">https://dev.twitter.com/docs/api/1.1/get/lists/show | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    UserList showUserList(long ownerId, String slug) throws TwitterException;

    /**
     * Show the specified list. Private lists will only be shown if the authenticated user owns the specified list.
     * <br>This method calls http://api.twitter.com/1.1/lists/show.json
     *
     * @param ownerScreenName The screen name of the user who owns the list being requested by a slug.
     * @param slug            slug of the list
     * @return the specified list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/show">https://dev.twitter.com/docs/api/1.1/get/lists/show | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    UserList showUserList(String ownerScreenName, String slug) throws TwitterException;

    /**
     * List the lists the specified user follows.
     * <br>This method calls http://api.twitter.com/1.1/lists/subscriptions.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param cursor              Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of lists
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscriptions">GET lists/subscriptions | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    PagableResponseList<UserList> getUserListSubscriptions(String listOwnerScreenName, long cursor)
            throws TwitterException;
}