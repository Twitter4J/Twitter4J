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

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface ListMembersMethodsAsync {
    /**
     * Returns the members of the specified list.
     * <br>This method calls http://api.twitter.com/1/[user]/[list_id]/members.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId              The id of the list
     * @param cursor              Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="http://dev.twitter.com/doc/get/:user/:list_id/members">GET :user/:list_id/members | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    void getUserListMembers(String listOwnerScreenName, int listId, long cursor);

    /**
     * Returns the members of the specified list.
     * <br>This method calls http://api.twitter.com/1/[user]/[list_id]/members.json
     *
     * @param listOwnerId The id of the list owner
     * @param listId      The id of the list
     * @param cursor      Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="http://dev.twitter.com/doc/get/:user/:list_id/members">GET :user/:list_id/members | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    void getUserListMembers(long listOwnerId, int listId, long cursor);

    /**
     * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
     * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user to add as a member of the list.
     * @see <a href="http://dev.twitter.com/doc/post/:user/:list_id/members">POST :user/:list_id/members | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    void addUserListMember(int listId, long userId);

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1/[user]/[list_id]/members/create_all.json
     *
     * @param listId  The id of the list.
     * @param userIds The array of ids of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="http://dev.twitter.com/doc/post/:user/:list_id/create_all">POST :user/:list_id/create_all | dev.twitter.com</a>
     * @since Twitter4J 2.1.7
     */
    void addUserListMembers(int listId, long[] userIds);

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1/[user]/[list_id]/members/create_all.json
     *
     * @param listId      The id of the list.
     * @param screenNames The array of screen names of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="http://dev.twitter.com/doc/post/:user/:list_id/create_all">POST :user/:list_id/create_all | dev.twitter.com</a>
     * @since Twitter4J 2.1.7
     */
    void addUserListMembers(int listId, String[] screenNames);

    /**
     * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
     * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members.json
     *
     * @param listId The id of the list.
     * @param userId The screen name of the member you wish to remove from the list.
     * @see <a href="http://dev.twitter.com/doc/delete/:user/:list_id/members">DELETE :user/:id/members | dev.twitter.com</a>
     * @since Twitter4J 2.1.0
     */
    void deleteUserListMember(int listId, long userId);

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members/id.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId              The id of the list.
     * @param userId              The id of the user who you want to know is a member or not of the specified list.
     * @see <a href="http://dev.twitter.com/doc/get/:user/:list_id/members/:id">GET :user/:list_id/members/:id | dev.twitter.com</a>
     * @since Twitter4J 2.1.0
     */
    void checkUserListMembership(String listOwnerScreenName, int listId, long userId);
}
