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

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface BlockMethods {
    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create/[id].json
     *
     * @param screenName the screen_name of the user to block
     * @return the blocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/blocks/create">POST blocks/create | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    User createBlock(String screenName) throws TwitterException;

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create/[id].json
     *
     * @param userId the ID of the user to block
     * @return the blocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/blocks/create">POST blocks/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    User createBlock(long userId) throws TwitterException;

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/destroy/[id].json
     *
     * @param screen_name the screen_name of the user to block
     * @return the unblocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/blocks/destroy">POST blocks/destroy | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    User destroyBlock(String screen_name) throws TwitterException;

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/destroy/[id].json
     *
     * @param userId the ID of the user to block
     * @return the unblocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/blocks/destroy">POST blocks/destroy | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    User destroyBlock(long userId) throws TwitterException;

    /**
     * Returns if the authenticating user is blocking a target user. Will return the blocked user's object if a block exists, and error with a HTTP 404 response code otherwise.
     * <br>This method calls http://api.twitter.com/1/blocks/exists/[id].json
     *
     * @param screenName The screen_name of the potentially blocked user.
     * @return if the authenticating user is blocking a target user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/exists">GET blocks/exists | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    boolean existsBlock(String screenName) throws TwitterException;

    /**
     * Returns if the authenticating user is blocking a target user. Will return the blocked user's object if a block exists, and error with a HTTP 404 response code otherwise.
     * <br>This method calls http://api.twitter.com/1/blocks/exists/[id].json
     *
     * @param userId The ID of the potentially blocked user.
     * @return if the authenticating user is blocking a target user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/exists">GET blocks/exists | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    boolean existsBlock(long userId) throws TwitterException;

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking.json
     *
     * @return a list of user objects that the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/blocking">GET blocks/blocking | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    ResponseList<User> getBlockingUsers() throws TwitterException;

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking.json
     *
     * @param page the number of page
     * @return a list of user objects that the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/blocking">GET blocks/blocking | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    ResponseList<User> getBlockingUsers(int page) throws TwitterException;

    /**
     * Returns an array of numeric user ids the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking/ids
     *
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/blocking/ids">GET blocks/blocking/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    IDs getBlockingUsersIDs() throws TwitterException;
}
