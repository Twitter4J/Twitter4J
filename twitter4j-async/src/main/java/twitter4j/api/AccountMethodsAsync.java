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

import java.io.File;
import java.io.InputStream;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface AccountMethodsAsync {
    /**
     * Returns an HTTP 200 OK response code and a representation of the requesting user if authentication was successful; returns a 401 status code and an error message if not.  Use this method to test if supplied user credentials are valid.
     * <br>This method calls http://api.twitter.com/1/account/verify_credentials.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/account/verify_credentials">GET account/verify_credentials | Twitter Developers</a>
     * @since Twitter4J 2.1.3
     */
    void verifyCredentials();

    /**
     * Gets the remaining number of API requests available to the requesting user before the API limit is reached for the current hour. Calls to rate_limit_status do not count against the rate limit.  If authentication credentials are provided, the rate limit status for the authenticating user is returned.  Otherwise, the rate limit status for the requester's IP address is returned.
     * <br>This method calls http://api.twitter.com/1/account/rate_limit_status
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/account/rate_limit_status">GET account/rate_limit_status | Twitter Developers</a>
     * @since Twitter4J 1.1.4
     */
    void getRateLimitStatus();

    /**
     * Sets one or more hex values that control the color scheme of the authenticating user's profile page on twitter.com. Each parameter's value must be a valid hexadecimal value, and may be either three or six characters (ex: #fff or #ffffff).
     * <br>This method calls http://api.twitter.com/1/account/update_profile_colors
     *
     * @param profileBackgroundColor    optional, can be null
     * @param profileTextColor          optional, can be null
     * @param profileLinkColor          optional, can be null
     * @param profileSidebarFillColor   optional, can be null
     * @param profileSidebarBorderColor optional, can be null
     * @see <a href="https://dev.twitter.com/docs/api/1/post/account/update_profile_colors">POST account/update_profile_colors | Twitter Developers</a>
     * @since Twitter4J 2.0.0
     */
    void updateProfileColors(String profileBackgroundColor, String profileTextColor, String profileLinkColor, String profileSidebarFillColor, String profileSidebarBorderColor);

    /**
     * Updates the authenticating user's profile image.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_image.json
     *
     * @param image Must be a valid GIF, JPG, or PNG image of less than 700 kilobytes in size.  Images with width larger than 500 pixels will be scaled down.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/account/update_profile_image">POST account/update_profile_image | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void updateProfileImage(File image);

    /**
     * Updates the authenticating user's profile image.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_image.json
     *
     * @param image Must be a valid GIF, JPG, or PNG image of less than 700 kilobytes in size.  Images with width larger than 500 pixels will be scaled down.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/account/update_profile_image">POST account/update_profile_image | Twitter Developers</a>
     * @since Twitter4J 2.1.11
     */
    void updateProfileImage(InputStream image);

    /**
     * Updates the authenticating user's profile background image.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_background_image.json
     *
     * @param image Must be a valid GIF, JPG, or PNG image of less than 800 kilobytes in size.  Images with width larger than 2048 pixels will be forcibly scaled down.
     * @param tile  If set to true the background image will be displayed tiled. The image will not be tiled otherwise.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/account/update_profile_background_image">POST account/update_profile_background_image | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void updateProfileBackgroundImage(File image, boolean tile);

    /**
     * Updates the authenticating user's profile background image.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_background_image.json
     *
     * @param image Must be a valid GIF, JPG, or PNG image of less than 800 kilobytes in size.  Images with width larger than 2048 pixels will be forcibly scaled down.
     * @param tile  If set to true the background image will be displayed tiled. The image will not be tiled otherwise.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/account/update_profile_background_image">POST account/update_profile_background_image | Twitter Developers</a>
     * @since Twitter4J 2.1.11
     */
    void updateProfileBackgroundImage(InputStream image, boolean tile);

    /**
     * Sets values that users are able to set under the "Account" tab of their settings page. Only the parameters specified(non-null) will be updated.
     *
     * @param name        Optional. Maximum of 20 characters.
     * @param url         Optional. Maximum of 100 characters. Will be prepended with "http://" if not present.
     * @param location    Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/account/update_profile">POST account/update_profile | Twitter Developers</a>
     * @since Twitter4J 2.1.8
     */
    void updateProfile(String name, String url, String location, String description);

    /**
     * Returns the current count of friends, followers, updates (statuses) and favorites of the authenticating user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/account/totals.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/account/totals">GET account/totals | Twitter Developers</a>
     * @since Twitter4J 2.1.9
     */
    void getAccountTotals();

    /**
     * Returns the current trend, geo and sleep time information for the authenticating user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/account/settings.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/account/totals">GET account/settings | Twitter Developers</a>
     * @since Twitter4J 2.1.9
     */
    void getAccountSettings();

    /**
     * Updates the current trend, geo, language, timezone and sleep time information for the authenticating user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/account/settings.json
     *
     * @param trendLocationWoeid Optional. The Yahoo! Where On Earth ID to use as the user's default trend location.
     * @param sleepTimeEnabled   Optional. Whether sleep time is enabled for the user
     * @param startSleepTime     Optional. The hour that sleep time should begin if it is enabled.
     * @param endSleepTime       Optional. The hour that sleep time should end if it is enabled.
     * @param timeZone           Optional. The timezone dates and times should be displayed in for the user.
     * @param lang               Optional. The language which Twitter should render in for this user. (two letter ISO 639-1)
     * @see <a href="https://dev.twitter.com/docs/api/1/post/account/settings">POST account/settings | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    void updateAccountSettings(Integer trendLocationWoeid, Boolean sleepTimeEnabled, String startSleepTime, String endSleepTime, String timeZone, String lang);
}
