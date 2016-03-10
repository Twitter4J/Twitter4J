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

import twitter4j.Paging;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface ListsResourcesAsync {
    /**
     * List the lists of the specified user. Private lists will be included if the authenticated users is the same as the user whose lists are being returned.
     * <br>This method calls https://api.twitter.com/1.1/lists.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     *                            as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists">GET lists | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getUserLists(String listOwnerScreenName);

    /**
     * List the lists of the specified user. Private lists will be included if the authenticated users is the same as the user whose lists are being returned.
     * <br>This method calls https://api.twitter.com/1.1/lists.json
     *
     * @param listOwnerUserId The id of the list owner
     *                        returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists">GET lists | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void getUserLists(long listOwnerUserId);

    /**
     * Show tweet timeline for members of the specified list.
     * <br>https://api.twitter.com/1/user/lists/list_id/statuses.json
     *
     * @param listId The id of the list
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/statuses">GET lists/statuses | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void getUserListStatuses(long listId, Paging paging);

    /**
     * Show tweet timeline for members of the specified list.
     * <br>https://api.twitter.com/1/user/lists/list_id/statuses.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param paging  controls pagination. Supports since_id, max_id, count and page parameters.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/statuses">GET lists/statuses | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getUserListStatuses(long ownerId, String slug, Paging paging);

    /**
     * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
     * <br>This method calls https://api.twitter.com/1.1/lists/members/destroy.json
     *
     * @param listId The id of the list.
     * @param userId The screen name of the member you wish to remove from the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/destroy">POST lists/members/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void destroyUserListMember(long listId, long userId);

    /**
     * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
     * <br>This method calls https://api.twitter.com/1.1/lists/members/destroy.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userId  The screen name of the member you wish to remove from the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/destroy">POST lists/members/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void destroyUserListMember(long ownerId, String slug, long userId);

    /**
     * List the lists the authenticating user has been added to.
     * <br>This method calls https://api.twitter.com/1.1/lists/memberships.json
     *
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    void getUserListMemberships(long cursor);

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls https://api.twitter.com/1.1/lists/memberships.json
     *
     * @param listMemberId The id of the list member
     * @param cursor       Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    void getUserListMemberships(long listMemberId, long cursor);

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls https://api.twitter.com/1.1/lists/memberships.json
     *
     * @param listMemberScreenName The screen name of the list member
     * @param cursor               Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getUserListMemberships(String listMemberScreenName, long cursor);

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls https://api.twitter.com/1.1/lists/memberships.json
     *
     * @param listMemberId       The id of the list member
     * @param cursor             Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param filterToOwnedLists Whether to return just lists the authenticating user owns, and the user represented by listMemberScreenName is a member of.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    void getUserListMemberships(long listMemberId, long cursor, boolean filterToOwnedLists);

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls https://api.twitter.com/1.1/lists/memberships.json
     *
     * @param listMemberScreenName The screen name of the list member
     * @param cursor               Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @param filterToOwnedLists   Whether to return just lists the authenticating user owns, and the user represented by listMemberScreenName is a member of.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/memberships">GET lists/memberships | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    void getUserListMemberships(String listMemberScreenName, long cursor, boolean filterToOwnedLists);

    /**
     * Returns the subscribers of the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/subscribers.json
     *
     * @param listId The id of the list
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers">GET lists/subscribers | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getUserListSubscribers(long listId, long cursor);

    /**
     * Returns the subscribers of the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/subscribers.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param cursor  Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers">GET lists/subscribers | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getUserListSubscribers(long ownerId, String slug, long cursor);

    /**
     * Make the authenticated user follow the specified list.
     * <br>This method calls https://api.twitter.com/1.1/list/subscribers/create.json
     *
     * @param listId The id of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/create">POST lists/subscribers/create | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void createUserListSubscription(long listId);

    /**
     * Make the authenticated user follow the specified list.
     * <br>This method calls https://api.twitter.com/1.1/list/subscribers/create.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/create">POST lists/subscribers/create | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void createUserListSubscription(long ownerId, String slug);

    /**
     * Check if the specified user is a subscriber of the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/subscribers/show.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user who you want to know is a member or not of the specified list.
     *               , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers/show">GET lists/subscribers/show | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void showUserListSubscription(long listId, long userId);

    /**
     * Check if the specified user is a subscriber of the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/subscribers/show.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userId  The id of the user who you want to know is a member or not of the specified list.
     *                , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscribers/show">GET lists/subscribers/show | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void showUserListSubscription(long ownerId, String slug, long userId);

    /**
     * Unsubscribes the authenticated user form the specified list.
     * <br>This method calls https://api.twitter.com/1.1/subscribers/destroy.json
     *
     * @param listId The id of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/destroy">POST lists/subscribers/destroy | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void destroyUserListSubscription(long listId);

    /**
     * Unsubscribes the authenticated user form the specified list.
     * <br>This method calls https://api.twitter.com/1.1/subscribers/destroy.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/subscribers/destroy">POST lists/subscribers/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void destroyUserListSubscription(long ownerId, String slug);

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls https://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param listId  The id of the list.
     * @param userIds The array of ids of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    void createUserListMembers(long listId, long... userIds);

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls https://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userIds The array of ids of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void createUserListMembers(long ownerId, String slug, long... userIds);

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls https://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param listId      The id of the list.
     * @param screenNames The array of screen names of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    void createUserListMembers(long listId, String... screenNames);

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls https://api.twitter.com/1.1/lists/members/create_all.json
     *
     * @param ownerId     The user ID of the user who owns the list being requested by a slug.
     * @param slug        slug of the list
     * @param screenNames The array of screen names of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void createUserListMembers(long ownerId, String slug, String... screenNames);

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls https://api.twitter.com/1.1/lists/members/show.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user who you want to know is a member or not of the specified list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members/show">GET lists/members/show | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void showUserListMembership(long listId, long userId);

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls https://api.twitter.com/1.1/lists/members/show.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userId  The id of the user who you want to know is a member or not of the specified list.
     *                .getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members/show">GET lists/members/show | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void showUserListMembership(long ownerId, String slug, long userId);

    /**
     * Returns the members of the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/members.json
     *
     * @param listId The id of the list
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members">GET lists/members | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void getUserListMembers(long listId, long cursor);

    /**
     * Returns the members of the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/members.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param cursor  Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/members">GET lists/members | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getUserListMembers(long ownerId, String slug, long cursor);

    /**
     * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
     * <br>This method calls https://api.twitter.com/1.1/lists/members/create.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user to add as a member of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create">POST lists/members/create | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void createUserListMember(long listId, long userId);

    /**
     * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
     * <br>This method calls https://api.twitter.com/1.1/lists/members/create.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @param userId  The id of the user to add as a member of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/members/create">POST lists/members/create | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void createUserListMember(long ownerId, String slug, long userId);

    /**
     * Deletes the specified list. Must be owned by the authenticated user.
     * <br>This method calls https://api.twitter.com/1.1/lists/destroy.json
     *
     * @param listId The id of the list to delete
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/destroy">POST lists/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void destroyUserList(long listId);

    /**
     * Deletes the specified list. Must be owned by the authenticated user.
     * <br>This method calls https://api.twitter.com/1.1/lists/destroy.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/destroy">POST lists/destroy | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void destroyUserList(long ownerId, String slug);

    /**
     * Updates the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/update.json
     *
     * @param listId         The id of the list to update.
     * @param newListName    What you'd like to change the list's name to.
     * @param isPublicList   Whether your list is public or private. Optional. Values can be public or private. Lists are public by default if no mode is specified.
     * @param newDescription What you'd like to change the list description to.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/update">POST lists/update | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void updateUserList(long listId, String newListName, boolean isPublicList, String newDescription);

    /**
     * Updates the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/update.json
     *
     * @param ownerId        The user ID of the user who owns the list being requested by a slug.
     * @param slug           slug of the list
     * @param newListName    What you'd like to change the list's name to.
     * @param isPublicList   Whether your list is public or private. Optional. Values can be public or private. Lists are public by default if no mode is specified.
     * @param newDescription What you'd like to change the list description to.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/update">POST lists/update | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void updateUserList(long ownerId, String slug, String newListName, boolean isPublicList, String newDescription);

    /**
     * Creates a new list for the authenticated user. Accounts are limited to 20 lists.
     * <br>This method calls https://api.twitter.com/1.1/lists/create.json
     *
     * @param listName     The name of the list you are creating. Required.
     * @param isPublicList set true if you wish to make a public list
     * @param description  The description of the list you are creating. Optional.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/lists/create">POST lists/create | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void createUserList(String listName, boolean isPublicList, String description);


    /**
     * Show the specified list. Private lists will only be shown if the authenticated user owns the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/show.json
     *
     * @param listId The id of the list to show
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/show">https://dev.twitter.com/docs/api/1.1/get/lists/show | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void showUserList(long listId);


    /**
     * Show the specified list. Private lists will only be shown if the authenticated user owns the specified list.
     * <br>This method calls https://api.twitter.com/1.1/lists/show.json
     *
     * @param ownerId The user ID of the user who owns the list being requested by a slug.
     * @param slug    slug of the list
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/show">https://dev.twitter.com/docs/api/1.1/get/lists/show | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void showUserList(long ownerId, String slug);

    /**
     * List the lists the specified user follows.
     * <br>This method calls https://api.twitter.com/1.1/lists/subscriptions.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param cursor              Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/lists/subscriptions">GET lists/subscriptions | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getUserListSubscriptions(String listOwnerScreenName, long cursor);
}
