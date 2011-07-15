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
public interface BlockMethodsAsync {
    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
     *
     * @param screenName the screen_name of the user to block
     * @see <a href="https://dev.twitter.com/docs/api/1/post/blocks/create">POST blocks/create | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void createBlock(String screenName);

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
     *
     * @param userId the screen_name of the user to block
     * @see <a href="https://dev.twitter.com/docs/api/1/post/blocks/create">POST blocks/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void createBlock(long userId);

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
     *
     * @param screenName the screen_name of the user to block
     * @see <a href="https://dev.twitter.com/docs/api/1/post/blocks/destroy">POST blocks/destroy | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void destroyBlock(String screenName);

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
     *
     * @param userId the ID of the user to block
     * @see <a href="https://dev.twitter.com/docs/api/1/post/blocks/destroy">POST blocks/destroy | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void destroyBlock(long userId);

    /**
     * Returns if the authenticating user is blocking a target user. Will return the blocked user's object if a block exists, and error with a HTTP 404 response code otherwise.
     * <br>This method calls http://api.twitter.com/1/blocks/exists/id.xml
     *
     * @param screenName The screen_name of the potentially blocked user.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/exists">GET blocks/exists | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    void existsBlock(String screenName);

    /**
     * Returns if the authenticating user is blocking a target user. Will return the blocked user's object if a block exists, and error with a HTTP 404 response code otherwise.
     * <br>This method calls http://api.twitter.com/1/blocks/exists/id.xml
     *
     * @param userId The ID of the potentially blocked user.
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/exists">GET blocks/exists | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void existsBlock(long userId);

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking.xml
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/blocking">GET blocks/blocking | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    void getBlockingUsers();

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking.xml
     *
     * @param page the number of page
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/blocking">GET blocks/blocking | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    void getBlockingUsers(int page);

    /**
     * Returns an array of numeric user ids the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking/ids
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/blocks/blocking/ids">GET blocks/blocking/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    void getBlockingUsersIDs();
}
