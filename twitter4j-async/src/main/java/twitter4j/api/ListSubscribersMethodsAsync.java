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
public interface ListSubscribersMethodsAsync {
    /**
     * Returns the subscribers of the specified list.
     * <br>This method calls http://api.twitter.com/1/lists/subscribers.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId              The id of the list
     * @param cursor              Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/subscribers">GET lists/subscribers | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     * @deprecated use {@link #getUserListSubscribers(int, long)} instead
     */
    void getUserListSubscribers(String listOwnerScreenName, int listId, long cursor);

    /**
     * Returns the subscribers of the specified list.
     * <br>This method calls http://api.twitter.com/1/lists/subscribers.json
     *
     * @param listId The id of the list
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/subscribers">GET lists/subscribers | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    void getUserListSubscribers(int listId, long cursor);

    /**
     * Make the authenticated user follow the specified list.
     * <br>This method calls http://api.twitter.com/1/list/subscribers/create.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId              The id of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/lists/subscribers/create">POST lists/subscribers/create | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     * @deprecated use {@link #createUserListSubscription(int)} instead
     */
    void subscribeUserList(String listOwnerScreenName, int listId);

    /**
     * Make the authenticated user follow the specified list.
     * <br>This method calls http://api.twitter.com/1/list/subscribers/create.json
     *
     * @param listId The id of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/lists/subscribers/create">POST lists/subscribers/create | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void createUserListSubscription(int listId);

    /**
     * Unsubscribes the authenticated user form the specified list.
     * <br>This method calls http://api.twitter.com/1/:listOwner/:listId/subscribers.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId              The id of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/lists/subscribers/destroy">POST lists/subscribers/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     * @deprecated use {@link #destroyUserListSubscription(int)} instead
     */
    void unsubscribeUserList(String listOwnerScreenName, int listId);

    /**
     * Unsubscribes the authenticated user form the specified list.
     * <br>This method calls http://api.twitter.com/1/subscribers/destroy.json
     *
     * @param listId The id of the list.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/lists/subscribers/destroy">POST lists/subscribers/destroy | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void destroyUserListSubscription(int listId);

    /**
     * Check if the specified user is a subscriber of the specified list.
     * <br>This method calls http://api.twitter.com/1/lists/subscribers/show.json
     *
     * @param listOwnerScreenName The screen name of the list owner
     * @param listId              The id of the list.
     * @param userId              The id of the user who you want to know is a member or not of the specified list.
     *                            , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/subscribers/show">GET lists/subscribers/show | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     * @deprecated use {@link #showUserListSubscription(int, long)} instead
     */
    void checkUserListSubscription(String listOwnerScreenName, int listId, long userId);

    /**
     * Check if the specified user is a subscriber of the specified list.
     * <br>This method calls http://api.twitter.com/1/lists/subscribers/show.json
     *
     * @param listId The id of the list.
     * @param userId The id of the user who you want to know is a member or not of the specified list.
     *               , or the user is not a member of the specified list(TwitterException.getStatusCode() returns 404 in that case.)
     * @see <a href="https://dev.twitter.com/docs/api/1/get/lists/subscribers/show">GET lists/subscribers/show | Twitter Developers</a>
     * @since Twitter4J 2.2.3
     */
    void showUserListSubscription(int listId, long userId);
}
