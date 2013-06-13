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
import twitter4j.auth.OAuth2Support;
import twitter4j.auth.OAuthSupport;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface Twitter extends java.io.Serializable,
        OAuthSupport,
        OAuth2Support,
        TwitterBase,
        TimelinesResources,
        TweetsResources,
        SearchResource,
        DirectMessagesResources,
        FriendsFollowersResources,
        UsersResources,
        SuggestedUsersResources,
        FavoritesResources,
        ListsResources,
        SavedSearchesResources,
        PlacesGeoResources,
        TrendsResources,
        SpamReportingResource,
        HelpResources {
}
