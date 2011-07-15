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
     * <br>This method calls http://api.twitter.com/1/lists/members.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId              The id of the list
     * @param cursor              Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/members">GET lists/members | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     * @deprecated use {@link #getUserListMembers(int, long)} instead
     */
    void getUserListMembers(String listOwnerScreenName, int listId, long cursor);

    /**
     * Returns the members of the specified list.
     * <br>This method calls http://api.twitter.com/1/lists/members.json
     *
     * @param listOwnerId The id of the list owner
     * @param listId      The id of the list
     * @param cursor      Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/members">GET lists/members | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     * @deprecated use {@link #getUserListMembers(int, long)} instead
     */
    void getUserListMembers(long listOwnerId, int listId, long cursor);

    /**
     * Returns the members of the specified list.
     * <br>This method calls http://api.twitter.com/1/lists/members.json
     *
     * @param listId The id of the list
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/members">GET lists/members | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void getUserListMembers(int listId, long cursor);

    /**
     * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
     * <br>This method calls http://api.twitter.com/1/lists/members/create.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user to add as a member of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/lists/members/create">POST lists/members/create | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void addUserListMember(int listId, long userId);

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1/lists/members/create_all.json
     *
     * @param listId  The id of the list.
     * @param userIds The array of ids of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    void addUserListMembers(int listId, long[] userIds);

    /**
     * Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members, and you are limited to adding up to 100 members to a list at a time with this method.
     * <br>This method calls http://api.twitter.com/1/lists/members/create_all.json
     *
     * @param listId      The id of the list.
     * @param screenNames The array of screen names of the user to add as member of the list. up to 100 are allowed in a single request.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/lists/members/create_all">POST lists/members/create_all | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    void addUserListMembers(int listId, String[] screenNames);

    /**
     * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
     * <br>This method calls http://api.twitter.com/1/lists/members/destroy.json
     *
     * @param listId The id of the list.
     * @param userId The screen name of the member you wish to remove from the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/lists/members/destroy">POST lists/members/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void deleteUserListMember(int listId, long userId);

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls http://api.twitter.com/1/lists/members/show.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId              The id of the list.
     * @param userId              The id of the user who you want to know is a member or not of the specified list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/members/show">GET lists/members/show | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     * @deprecated use {@link #showUserListMembership(int, long)} instead
     */
    void checkUserListMembership(String listOwnerScreenName, int listId, long userId);

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls http://api.twitter.com/1/lists/members/show.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user who you want to know is a member or not of the specified list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/members/show">GET lists/members/show | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void showUserListMembership(int listId, long userId);
}
