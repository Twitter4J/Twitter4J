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
package twitter4j;

import twitter4j.api.*;
import twitter4j.auth.AsyncOAuth2Support;
import twitter4j.auth.AsyncOAuthSupport;
import twitter4j.auth.OAuth2Support;
import twitter4j.auth.OAuthSupport;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface AsyncTwitter extends java.io.Serializable,
        OAuthSupport,
        OAuth2Support,
        AsyncOAuthSupport,
        AsyncOAuth2Support,
        TwitterBase,
        TimelinesResourcesAsync,
        TweetsResourcesAsync,
        SearchResourceAsync,
        DirectMessagesResourcesAsync,
        FriendsFollowersResourcesAsync,
        UsersResourcesAsync,
        SuggestedUsersResourcesAsync,
        FavoritesResourcesAsync,
        ListsResourcesAsync,
        SavedSearchesResourcesAsync,
        PlacesGeoResourcesAsync,
        TrendsResourcesAsync,
        SpamReportingResourceAsync,
        HelpResourcesAsync {

    /**
     * Adds twitter listener
     *
     * @param listener TwitterListener
     */
    void addListener(TwitterListener listener);

    /**
     * Shuts down internal dispatcher thread shared across all AsyncTwitter instances.<br>
     *
     * @since Twitter4J 2.1.9
     */
    void shutdown();
}
