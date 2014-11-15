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
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.0
 */
public interface SavedSearchesResourcesAsync {
    /**
     * Returns the authenticated user's saved search queries.
     * <br>This method calls https://api.twitter.com/1.1/saved_searches.json
     *
blocking.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/saved_searches">GET saved_searches | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void getSavedSearches();

    /**
     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
     * <br>This method calls https://api.twitter.com/1.1/saved_searches/show/:id.json
     *
     * @param id The id of the saved search to be retrieved.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/saved_searches/show/:id">GET saved_searches/show/:id | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void showSavedSearch(int id);

    /**
     * Creates a saved search for the authenticated user.
     * <br>This method calls https://api.twitter.com/1.1/saved_searches/saved_searches/create.json
     *
     * @param query the query string
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/saved_searches/create">POST saved_searches/create | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void createSavedSearch(String query);

    /**
     * Destroys a saved search for the authenticated user. The search specified by id must be owned by the authenticating user.
     * <br>This method calls https://api.twitter.com/1.1/saved_searches/destroy/id.json
     *
     * @param id The id of the saved search to be deleted.
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/saved_searches/destroy/:id">POST saved_searches/destroy/:id | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void destroySavedSearch(int id);
}
