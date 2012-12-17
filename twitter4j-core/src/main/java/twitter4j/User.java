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

import java.net.URL;
import java.util.Date;

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
    boolean isContributorsEnabled();

    /**
     * Returns the profile image url of the user
     *
     * @return the profile image url of the user
     */
    String getProfileImageURL();
    String getBiggerProfileImageURL();
    String getMiniProfileImageURL();
    String getOriginalProfileImageURL();

    /**
     * Returns the profile image url of the user, served over SSL
     *
     * @return the profile image url of the user, served over SSL
     * @deprecated use {@link #getProfileImageURL()} instead
     */
    URL getProfileImageUrlHttps();
    String getProfileImageURLHttps();
    String getBiggerProfileImageURLHttps();
    String getMiniProfileImageURLHttps();
    String getOriginalProfileImageURLHttps();

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

    String getProfileBackgroundColor();

    String getProfileTextColor();

    String getProfileLinkColor();

    String getProfileSidebarFillColor();

    String getProfileSidebarBorderColor();

    boolean isProfileUseBackgroundImage();

    boolean isShowAllInlineMedia();

    int getFriendsCount();

    Date getCreatedAt();

    int getFavouritesCount();

    int getUtcOffset();

    String getTimeZone();

    /**
     * @deprecated use {@link #getProfileImageURL()} instead
     */
    String getProfileBackgroundImageUrl();
    String getProfileBackgroundImageURL();

    String getProfileBackgroundImageUrlHttps();

    /**
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerURL();
    /**
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerRetinaURL();
    /**
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerIPadURL();
    /**
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerIPadRetinaURL();
    /**
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerMobileURL();
    /**
     * @since Twitter4J 3.0.0
     */
    String getProfileBannerMobileRetinaURL();

    boolean isProfileBackgroundTiled();

    /**
     * Returns the preferred language of the user
     *
     * @return the preferred language of the user
     * @since Twitter4J 2.1.2
     */
    String getLang();

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
    
    
}