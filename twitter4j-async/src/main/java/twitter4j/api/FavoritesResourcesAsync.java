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
public interface FavoritesResourcesAsync {
    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls https://api.twitter.com/1.1/favorites
     *
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/favorites">GET favorites | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void getFavorites();

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls https://api.twitter.com/1.1/favorites
     *
     * @param id the id of the user for whom to request a list of favorite statuses
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/favorites">GET favorites | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getFavorites(long id);

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls https://api.twitter.com/1.1/favorites
     *
     * @param screenName the screen name of the user for whom to request a list of favorite statuses
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/favorites">GET favorites | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void getFavorites(String screenName);

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls https://api.twitter.com/1.1/favorites.json
     *
     * @param paging controls pagination. Supports sinceId and page parameters.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/favorites">GET favorites | Twitter Developers</a>
     * @since Twitter4J 2.2.5
     */
    void getFavorites(Paging paging);

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls https://api.twitter.com/1.1/favorites/[id].json
     *
     * @param userId the id of the user for whom to request a list of favorite statuses
     * @param paging controls pagination. Supports sinceId and page parameters.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/favorites">GET favorites | Twitter Developers</a>
     * @since Twitter4J 2.2.5
     */
    void getFavorites(long userId, Paging paging);

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls https://api.twitter.com/1.1/favorites/[id].json
     *
     * @param screenName the screen name of the user for whom to request a list of favorite statuses
     * @param paging controls pagination. Supports sinceId and page parameters.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/favorites">GET favorites | Twitter Developers</a>
     * @since Twitter4J 2.2.5
     */
    void getFavorites(String screenName, Paging paging);

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * <br>This method calls https://api.twitter.com/1.1/favorites/create%C2%A0
     *
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/favorites/create/:id">POST favorites/create/:id | Twitter Developers</a>
     * @since 1.1.2
     */
    void createFavorite(long id);

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * <br>This method calls https://api.twitter.com/1.1/favorites/destroy
     *
     * @param id the ID or screen name of the user for whom to request a list of un-favorite statuses.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/favorites/destroy/:id">POST favorites/destroy/:id | Twitter Developers</a>
     * @since 1.1.2
     */
    void destroyFavorite(long id);
}
