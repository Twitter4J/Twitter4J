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

import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface SavedSearchesResources {
    /**
     * Returns the authenticated user's saved search queries.
     * <br>This method calls http://api.twitter.com/1.1/saved_searches.json
     *
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/saved_searches">GET saved_searches | Twitter Developers</a>
     * @since Twitter4J 2.0.8
     */
    ResponseList<SavedSearch> getSavedSearches() throws TwitterException;

    /**
     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
     * <br>This method calls http://api.twitter.com/1.1/saved_searches/show/:id.json
     *
     * @param id The id of the saved search to be retrieved.
     * @return the data for a saved search
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/saved_searches/show/:id">GET saved_searches/show/:id | Twitter Developers</a>
     * @since Twitter4J 2.0.8
     */
    SavedSearch showSavedSearch(int id) throws TwitterException;

    /**
     * Creates a saved search for the authenticated user.
     * <br>This method calls http://api.twitter.com/1.1/saved_searches/saved_searches/create.json
     *
     * @param query the query string
     * @return the data for a created saved search
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/saved_searches/create">POST saved_searches/create | Twitter Developers</a>
     * @since Twitter4J 2.0.8
     */
    SavedSearch createSavedSearch(String query)
            throws TwitterException;

    /**
     * Destroys a saved search for the authenticated user. The search specified by id must be owned by the authenticating user.
     * <br>This method calls http://api.twitter.com/1.1/saved_searches/destroy/id.json
     *
     * @param id The id of the saved search to be deleted.
     * @return the data for a destroyed saved search
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/saved_searches/destroy/:id">POST saved_searches/destroy/:id | Twitter Developers</a>
     * @since Twitter4J 2.0.8
     */
    SavedSearch destroySavedSearch(int id)
            throws TwitterException;
}
