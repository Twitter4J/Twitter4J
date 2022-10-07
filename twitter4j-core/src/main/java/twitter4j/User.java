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


import java.time.LocalDateTime;

/**
 * A data interface representing Basic user information element
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface User extends Comparable<User>, TwitterResponse, java.io.Serializable {
    /**
     * Returns the id of the user
     *
     * @return the id of the user
     */
    long getId();

    /**
     * Returns the name of the user
     *
     * @return the name of the user
     */
    String getName();

    /**
     * Returns the email of the user, if the app is whitelisted by Twitter
     *
     * @return the email of the user
     */
    @SuppressWarnings("unused")
    String getEmail();

    /**
     * Returns the screen name of the user
     *
     * @return the screen name of the user
     */
    String getScreenName();

    /**
     * Returns the location of the user
     *
     * @return the location of the user
     */
    String getLocation();

    /**
     * Returns the description of the user
     *
     * @return the description of the user
     */
    String getDescription();

    /**
     * Tests if the user is enabling contributors
     *
     * @return if the user is enabling contributors
     * @since Twitter4J 2.1.2
     */
    @SuppressWarnings("unused")
    boolean isContributorsEnabled();

    /**
     * Returns the profile image url of the user
     *
     * @return the profile image url of the user
     */
    String getProfileImageURL();

    /**
     * @return bigger profile image URL
     */
    String getBiggerProfileImageURL();

    /**
     * @return mini profile image URL
     */
    String getMiniProfileImageURL();

    /**
     * @return original profile image URL
     */
    String getOriginalProfileImageURL();

    /**
     * @return profile image url
     * @since Twitter4J 4.0.7
     */
    @SuppressWarnings("unused")
    String get400x400ProfileImageURL();

    /**
     * @return profile image URL
     */
    String getProfileImageURLHttps();

    /**
     * @return profile image URL
     */
    String getBiggerProfileImageURLHttps();

    /**
     * @return mini profile image URL
     */
    String getMiniProfileImageURLHttps();

    /**
     * @return original profile image URL
     */
    String getOriginalProfileImageURLHttps();

    /**
     * @return profile image url
     * @since Twitter4J 4.0.7
     */
    @SuppressWarnings("unused")
    String get400x400ProfileImageURLHttps();

    /**
     * Tests if the user has not uploaded their own avatar
     *
     * @return if the user has not uploaded their own avatar
     */
    boolean isDefaultProfileImage();

    /**
     * Returns the url of the user
     *
     * @return the url of the user
     */
    String getURL();

    /**
     * Test if the user status is protected
     *
     * @return true if the user status is protected
     */
    boolean isProtected();

    /**
     * Returns the number of followers
     *
     * @return the number of followers
     * @since Twitter4J 1.0.4
     */
    int getFollowersCount();

    /**
     * Returns the current status of the user<br>
     * This can be null if the instance if from Status.getUser().
     *
     * @return current status of the user
     * @since Twitter4J 2.1.1
     */
    Status getStatus();

    /**
     * @return profile background color
     */
    String getProfileBackgroundColor();

    /**
     * @return profile text color
     */
    String getProfileTextColor();

    /**
     * @return profile link color
     */
    String getProfileLinkColor();

    /**
     * @return profile sidebar fill color
     */
    String getProfileSidebarFillColor();

    /**
     * @return profile sidebar border color
     */
    String getProfileSidebarBorderColor();

    /**
     * @return profile use background image
     */
    @SuppressWarnings("unused")
    boolean isProfileUseBackgroundImage();

    /**
     * Tests if the user has not altered the theme or background
     *
     * @return if the user has not altered the theme or background
     */
    @SuppressWarnings("unused")
    boolean isDefaultProfile();

    /**
     * @return show all inline media
     */
    @SuppressWarnings("unused")
    boolean isShowAllInlineMedia();

    /**
     * Returns the number of users the user follows (AKA "followings")
     *
     * @return the number of users the user follows
     */
    int getFriendsCount();

    /**
     * @return created at
     */
    LocalDateTime getCreatedAt();

    /**
     * @return favorites count
     */
    int getFavouritesCount();

    /**
     * @return UTC offset
     */
    @SuppressWarnings("unused")
    int getUtcOffset();

    /**
     * @return time zone
     */
    String getTimeZone();

    /**
     * @return profile background image URL
     */
    String getProfileBackgroundImageURL();

    /**
     * @return profile background image URL
     */
    @SuppressWarnings("unused")
    String getProfileBackgroundImageUrlHttps();

    /**
     * @return profile banner URL
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerURL();

    /**
     * @return profile banner retina URL
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerRetinaURL();

    /**
     * @return profile banner iPad URL
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerIPadURL();

    /**
     * @return profile banner iPad retina URL
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerIPadRetinaURL();

    /**
     * @return profile banner mobile URL
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerMobileURL();

    /**
     * @return profile banner mobile retina URL
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerMobileRetinaURL();

    /**
     * @return profile banner 300x100 URL
     * @since Twitter4J 4.0.7
     */
    @SuppressWarnings("unused")
    String getProfileBanner300x100URL();

    /**
     * @return profile banner 600x200 URL
     * @since Twitter4J 4.0.7
     */
    @SuppressWarnings("unused")
    String getProfileBanner600x200URL();

    /**
     * @return profile banner 1500x500 URL
     * @since Twitter4J 4.0.7
     */
    @SuppressWarnings("unused")
    String getProfileBanner1500x500URL();

    /**
     * @return profile background tiled
     */
    @SuppressWarnings("unused")
    boolean isProfileBackgroundTiled();

    /**
     * Returns the preferred language of the user
     *
     * @return the preferred language of the user
     * @since Twitter4J 2.1.2
     */
    String getLang();

    /**
     * @return status count
     */
    int getStatusesCount();

    /**
     * @return the user is enabling geo location
     * @since Twitter4J 2.0.10
     */
    boolean isGeoEnabled();

    /**
     * @return returns true if the user is a verified celebrity
     * @since Twitter4J 2.0.10
     */
    boolean isVerified();

    /**
     * @return returns true if the user is a translator
     * @since Twitter4J 2.1.9
     */
    @SuppressWarnings("unused")
    boolean isTranslator();

    /**
     * Returns the number of public lists the user is listed on, or -1
     * if the count is unavailable.
     *
     * @return the number of public lists the user is listed on.
     * @since Twitter4J 2.1.4
     */
    int getListedCount();

    /**
     * Returns true if the authenticating user has requested to follow this user,
     * otherwise false.
     *
     * @return true if the authenticating user has requested to follow this user.
     * @since Twitter4J 2.1.4
     */
    boolean isFollowRequestSent();

    /**
     * Returns URL entities for user description.
     *
     * @return URL entities for user description
     * @since Twitter4J 3.0.3
     */
    URLEntity[] getDescriptionURLEntities();

    /**
     * Returns URL entity for user's URL.
     *
     * @return URL entity for user's URL.
     * @since Twitter4J 3.0.3
     */
    URLEntity getURLEntity();

    /**
     * Returns the list of country codes where the user is withheld
     *
     * @return list of country codes where the tweet is withheld - null if not withheld
     * @since Twitter4j 4.0.3
     */
    @SuppressWarnings("MismatchedJavadocCode")
    String[] getWithheldInCountries();

}
