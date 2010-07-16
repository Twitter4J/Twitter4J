/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.api;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface ListMembersMethodsAsync {
    /**
     * Returns the members of the specified list.
     * <br>This method calls http://api.twitter.com/1/[user]/[list_id]/members.json
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId The id of the list
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="http://dev.twitter.com/doc/get/:user/:list_id/members">GET :user/:list_id/members | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    void getUserListMembers(String listOwnerScreenName, int listId, long cursor);

    /**
     * Adds a member to a list. The authenticated user must own the list to be able to add members to it. Lists are limited to having 500 members.
     * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members.json
     * @param listId The id of the list.
     * @param userId The id of the user to add as a member of the list.
     * @see <a href="http://dev.twitter.com/doc/post/:user/:list_id/members">POST :user/:list_id/members | dev.twitter.com</a>
     * @since Twitter4J 2.1.1
     */
    void addUserListMember(int listId, int userId);

    /**
     * Removes the specified member from the list. The authenticated user must be the list's owner to remove members from the list.
     * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members.json
     * @param listId The id of the list.
     * @param userId The screen name of the member you wish to remove from the list.
     * @see <a href="http://dev.twitter.com/doc/delete/:user/:id/members">DELETE :user/:id/members | dev.twitter.com</a>
     * @since Twitter4J 2.1.0
     */
    void deleteUserListMember(int listId, int userId);

    /**
     * Check if a user is a member of the specified list.<br>
     * <br>This method calls http://api.twitter.com/1/[listOwner]/[listId]/members/id.json
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId The id of the list.
     * @param userId The id of the user who you want to know is a member or not of the specified list.
     * @see <a href="http://dev.twitter.com/doc/get/:user/:list_id/members/:id">GET :user/:list_id/members/:id | dev.twitter.com</a>
     * @since Twitter4J 2.1.0
     */
    void checkUserListMembership(String listOwnerScreenName, int listId, int userId);
}
