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
package org.twitter4j.core;

import org.twitter4j.core.api.*;
import org.twitter4j.core.auth.OAuth2Support;
import org.twitter4j.core.auth.OAuthSupport;

import java.io.Serializable;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface Twitter extends Serializable,
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

    /**
     * @return {@link org.twitter4j.core.api.TimelinesResources}
     * @since Twitter4J 3.0.4
     */
    TimelinesResources timelines();

    /**
     * @return {@link org.twitter4j.core.api.TweetsResources}
     * @since Twitter4J 3.0.4
     */
    TweetsResources tweets();

    /**
     * @return {@link org.twitter4j.core.api.SearchResource}
     * @since Twitter4J 3.0.4
     */
    SearchResource search();

    /**
     * @return {@link org.twitter4j.core.api.DirectMessagesResources}
     * @since Twitter4J 3.0.4
     */
    DirectMessagesResources directMessages();

    /**
     * @return {@link org.twitter4j.core.api.FriendsFollowersResources}
     * @since Twitter4J 3.0.4
     */
    FriendsFollowersResources friendsFollowers();

    /**
     * @return {@link org.twitter4j.core.api.UsersResources}
     * @since Twitter4J 3.0.4
     */
    UsersResources users();

    /**
     * @return {@link org.twitter4j.core.api.SuggestedUsersResources}
     * @since Twitter4J 3.0.4
     */
    SuggestedUsersResources suggestedUsers();

    /**
     * @return {@link org.twitter4j.core.api.FavoritesResources}
     * @since Twitter4J 3.0.4
     */
    FavoritesResources favorites();

    /**
     * @return {@link org.twitter4j.core.api.ListsResources}
     * @since Twitter4J 3.0.4
     */
    ListsResources list();

    /**
     * @return {@link org.twitter4j.core.api.SavedSearchesResources}
     * @since Twitter4J 3.0.4
     */
    SavedSearchesResources savedSearches();

    /**
     * @return {@link org.twitter4j.core.api.PlacesGeoResources}
     * @since Twitter4J 3.0.4
     */
    PlacesGeoResources placesGeo();

    /**
     * @return {@link org.twitter4j.core.api.TrendsResources}
     * @since Twitter4J 3.0.4
     */
    TrendsResources trends();

    /**
     * @return {@link org.twitter4j.core.api.SpamReportingResource}
     * @since Twitter4J 3.0.4
     */
    SpamReportingResource spamReporting();

    /**
     * @return {@link org.twitter4j.core.api.HelpResources}
     * @since Twitter4J 3.0.4
     */
    HelpResources help();
}
