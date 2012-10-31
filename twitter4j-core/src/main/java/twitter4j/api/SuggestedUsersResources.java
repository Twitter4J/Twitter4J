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

import twitter4j.Category;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.0
 */
public interface SuggestedUsersResources {
    /**
     * Access the users in a given category of the Twitter suggested user list.<br>
     * It is recommended that end clients cache this data for no more than one hour.
     * <br>This method calls http://api.twitter.com/1.1/users/suggestions/:slug.json
     *
     * @param categorySlug slug
     * @return list of suggested users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/suggestions/:slug">GET users/suggestions/:slug | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> getUserSuggestions(String categorySlug) throws TwitterException;

    /**
     * Access to Twitter's suggested user list. This returns the list of suggested user categories. The category can be used in the users/suggestions/category endpoint to get the users in that category.
     * <br>This method calls http://api.twitter.com/1.1/users/suggestions
     *
     * @return list of suggested user categories.
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/suggestions">GET users/suggestions | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<Category> getSuggestedUserCategories() throws TwitterException;

    /**
     * Access the users in a given category of the Twitter suggested user list and return their most recent status if they are not a protected user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1.1/users/suggestions/:slug/members.json
     *
     * @param categorySlug slug
     * @return list of suggested users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/suggestions/%3Aslug/members">GET users/suggestions/:slug/members | Twitter Developers</a>
     * @since Twitter4J 2.1.9
     */
    ResponseList<User> getMemberSuggestions(String categorySlug) throws TwitterException;

}
