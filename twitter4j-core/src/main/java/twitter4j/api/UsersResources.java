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

import twitter4j.*;

import java.io.File;
import java.io.InputStream;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface UsersResources {
    /**
     * Returns the current trend, geo, language, timezone and sleep time information for the authenticating user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls https://api.twitter.com/1.1/account/settings.json
     *
     * @return the current trend, geo and sleep time information for the authenticating user.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/account/settings">GET account/settings | Twitter Developers</a>
     * @since Twitter4J 2.1.9
     */
    AccountSettings getAccountSettings() throws TwitterException;

    /**
     * Returns an HTTP 200 OK response code and a representation of the requesting user if authentication was successful; returns a 401 status code and an error message if not.  Use this method to test if supplied user credentials are valid.
     * <br>This method calls https://api.twitter.com/1.1/account/verify_credentials.json
     *
     * @return user
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable, or if supplied credential is wrong (TwitterException.getStatusCode() == 401)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/account/verify_credentials">GET account/verify_credentials | Twitter Developers</a>
     * @since Twitter4J 2.0.0
     */
    User verifyCredentials() throws TwitterException;

    /**
     * Updates the current trend, geo, language, timezone and sleep time information for the authenticating user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls https://api.twitter.com/1.1/account/settings.json
     *
     * @param trendLocationWoeid Optional. The Yahoo! Where On Earth ID to use as the user's default trend location.
     * @param sleepTimeEnabled   Optional. Whether sleep time is enabled for the user
     * @param startSleepTime     Optional. The hour that sleep time should begin if it is enabled.
     * @param endSleepTime       Optional. The hour that sleep time should end if it is enabled.
     * @param timeZone           Optional. The timezone dates and times should be displayed in for the user.
     * @param lang               Optional. The language which Twitter should render in for this user. (two letter ISO 639-1)
     * @return the current trend, geo and sleep time information for the authenticating user.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/settings">POST account/settings | Twitter Developers</a>
     * @since Twitter4J 2.2.4
     */
    AccountSettings updateAccountSettings(Integer trendLocationWoeid, Boolean sleepTimeEnabled, String startSleepTime, String endSleepTime, String timeZone, String lang) throws TwitterException;

    // update delivery device

    /**
     * Sets values that users are able to set under the "Account" tab of their settings page. Only the parameters specified(non-null) will be updated.
     * <br>This method calls https://api.twitter.com/1.1/account/update_profile.json
     *
     * @param name        Optional. Maximum of 20 characters.
     * @param url         Optional. Maximum of 100 characters. Will be prepended with "http://" if not present.
     * @param location    Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile">POST account/update_profile | Twitter Developers</a>
     * @since Twitter4J 2.1.8
     */
    User updateProfile(String name, String url, String location, String description)
        throws TwitterException;

    /**
     * Updates the authenticating user's profile background image.
     * <br>This method calls https://api.twitter.com/1.1/account/update_profile_background_image.json
     *
     * @param image Must be a valid GIF, JPG, or PNG image of less than 800 kilobytes in size.  Images with width larger than 2048 pixels will be forceably scaled down.
     * @param tile  If set to true the background image will be displayed tiled. The image will not be tiled otherwise.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable,
     *                          or when the specified file is not found (FileNotFoundException will be nested),
     *                          or when the specified file object in not representing a file (IOException will be nested)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile_background_image">POST account/update_profile_background_image | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    User updateProfileBackgroundImage(File image, boolean tile)
        throws TwitterException;

    /**
     * Updates the authenticating user's profile background image.
     * <br>This method calls https://api.twitter.com/1.1/account/update_profile_background_image.json
     *
     * @param image Must be a valid GIF, JPG, or PNG image of less than 800 kilobytes in size.  Images with width larger than 2048 pixels will be forceably scaled down.
     * @param tile  If set to true the background image will be displayed tiled. The image will not be tiled otherwise.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable,
     *                          or when the specified file is not found (FileNotFoundException will be nested),
     *                          or when the specified file object in not representing a file (IOException will be nested)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile_background_image">POST account/update_profile_background_image | Twitter Developers</a>
     * @since Twitter4J 2.1.11
     */
    User updateProfileBackgroundImage(InputStream image, boolean tile)
        throws TwitterException;

    /**
     * Sets one or more hex values that control the color scheme of the authenticating user's profile page on twitter.com. Each parameter's value must be a valid hexidecimal value, and may be either three or six characters (ex: #fff or #ffffff).
     * <br>This method calls https://api.twitter.com/1.1/account/update_profile_colors.json
     *
     * @param profileBackgroundColor    optional, can be null
     * @param profileTextColor          optional, can be null
     * @param profileLinkColor          optional, can be null
     * @param profileSidebarFillColor   optional, can be null
     * @param profileSidebarBorderColor optional, can be null
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile_colors">POST account/update_profile_colors | Twitter Developers</a>
     * @since Twitter4J 2.0.0
     */
    User updateProfileColors(String profileBackgroundColor, String profileTextColor, String profileLinkColor, String profileSidebarFillColor, String profileSidebarBorderColor)
        throws TwitterException;

    /**
     * Updates the authenticating user's profile image.
     * <br>This method calls https://api.twitter.com/1.1/account/update_profile_image.json
     *
     * @param image Must be a valid GIF, JPG, or PNG image of less than 700 kilobytes in size.  Images with width larger than 500 pixels will be scaled down.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable,
     *                          or when the specified file is not found (FileNotFoundException will be nested),
     *                          or when the specified file object in not representing a file (IOException will be nested)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile_image">POST account/update_profile_image | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    User updateProfileImage(File image) throws TwitterException;

    /**
     * Updates the authenticating user's profile image.
     * <br>This method calls https://api.twitter.com/1.1/account/update_profile_image.json
     *
     * @param image Must be a valid GIF, JPG, or PNG image of less than 700 kilobytes in size.  Images with width larger than 500 pixels will be scaled down.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable,
     *                          or when the specified file is not found (FileNotFoundException will be nested),
     *                          or when the specified file object in not representing a file (IOException will be nested)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile_image">POST account/update_profile_image | Twitter Developers</a>
     * @since Twitter4J 2.1.11
     */
    User updateProfileImage(InputStream image) throws TwitterException;

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls https://api.twitter.com/1.1/blocks/blocking.json
     *
     * @return a list of user objects that the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/blocks/blocking">GET blocks/blocking | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    PagableResponseList<User> getBlocksList() throws TwitterException;

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls https://api.twitter.com/1.1/blocks/blocking.json
     *
     * @param cursor Causes the list of blocked users to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried. If no cursor is provided, a value of -1 will be assumed, which is the first "page."
     * @return a list of user objects that the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/blocks/blocking">GET blocks/blocking | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    PagableResponseList<User> getBlocksList(long cursor) throws TwitterException;

    /**
     * Returns an array of numeric user ids the authenticating user is blocking.
     * <br>This method calls https://api.twitter.com/1.1/blocks/ids
     *
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/blocks/ids">GET blocks/ids | Twitter Developers</a>
     * @since Twitter4J 2.0.4
     */
    IDs getBlocksIDs() throws TwitterException;

    /**
     * Returns an array of numeric user ids the authenticating user is blocking.
     * <br>This method calls https://api.twitter.com/1.1/blocks/ids
     *
     * @param cursor cursor
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/blocks/ids">GET blocks/ids | Twitter Developers</a>
     * @since Twitter4J 3.0.2
     */
    IDs getBlocksIDs(long cursor) throws TwitterException;

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls https://api.twitter.com/1.1/blocks/create/[id].json
     *
     * @param userId the ID of the user to block
     * @return the blocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/blocks/create">POST blocks/create | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    User createBlock(long userId) throws TwitterException;

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls https://api.twitter.com/1.1/blocks/create/[id].json
     *
     * @param screenName the screen_name of the user to block
     * @return the blocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/blocks/create">POST blocks/create | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    User createBlock(String screenName) throws TwitterException;

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls https://api.twitter.com/1.1/blocks/destroy/[id].json
     *
     * @param userId the ID of the user to block
     * @return the unblocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/blocks/destroy">POST blocks/destroy | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    User destroyBlock(long userId) throws TwitterException;

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls https://api.twitter.com/1.1/blocks/destroy/[id].json
     *
     * @param screen_name the screen_name of the user to block
     * @return the unblocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/blocks/destroy">POST blocks/destroy | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    User destroyBlock(String screen_name) throws TwitterException;

    /**
     * Returns a list of user objects that the authenticating user is muting.
     * <br>This method calls https://api.twitter.com/1.1/mutes/users/list
     *
     * @param cursor Causes the list of muted users to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried. If no cursor is provided, a value of -1 will be assumed, which is the first "page."
     * @return a list of user objects that the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/mutes/users/list">GET blocks/blocking | Twitter Developers</a>
     * @since Twitter4J 4.0.2
     */
    PagableResponseList<User> getMutesList(long cursor) throws TwitterException;

    /**
     * Returns an array of numeric user ids the authenticating user is muting.
     * <br>This method calls https://api.twitter.com/1.1/mutes/users/ids
     *
     * @param cursor Causes the list of muted users to be broken into pages of no more than 5000 IDs at a time. The number of IDs returned is not guaranteed to be 5000 as suspended users are filtered out after connections are queried. If no cursor is provided, a value of -1 will be assumed, which is the first "page."
     * @return Returns an array of numeric user ids the authenticating user is muting.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/mutes/users/ids">GET blocks/ids | Twitter Developers</a>
     * @since Twitter4J 4.0.2
     */
    IDs getMutesIDs(long cursor) throws TwitterException;

    /**
     * Mutes the user specified in the ID parameter as the authenticating user.  Returns the muted user in the requested format when successful.
     * <br>This method calls https://api.twitter.com/1.1/mutes/users/create
     *
     * @param userId the user_id of the user to mute
     * @return the muted user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/mutes/users/create">POST mutes/users/create | Twitter Developers</a>
     * @since Twitter4J 4.0.2
     */
    User createMute(long userId) throws TwitterException;

    /**
     * Mutes the user specified in the screen name parameter as the authenticating user.  Returns the muted user in the requested format when successful.
     * <br>This method calls https://api.twitter.com/1.1/mutes/users/create
     *
     * @param screenName the screen_name of the user to mute
     * @return the muted user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/mutes/users/create">POST mutes/users/create | Twitter Developers</a>
     * @since Twitter4J 4.0.2
     */
    User createMute(String screenName) throws TwitterException;

    /**
     * Un-mutes the user specified in the ID parameter as the authenticating user.  Returns the un-muted user in the requested format when successful.
     * <br>This method calls https://api.twitter.com/1.1/mutes/users/destroy
     *
     * @param userId the ID of the user to mute
     * @return the unmuted user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/mutes/users/destroy">POST mutes/users/destroy | Twitter Developers</a>
     * @since Twitter4J 4.0.2
     */
    User destroyMute(long userId) throws TwitterException;

    /**
     * Un-mutes the user specified in the screen name parameter as the authenticating user.  Returns the un-muted user in the requested format when successful.
     * <br>This method calls https://api.twitter.com/1.1/mutes/users/destroy
     *
     * @param screenName the screen_name of the user to mute
     * @return the unmuted user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/mutes/users/destroy">POST mutes/users/destroy | Twitter Developers</a>
     * @since Twitter4J 4.0.2
     */
    User destroyMute(String screenName) throws TwitterException;

    /**
     * Return up to 100 users worth of extended information, specified by either ID, screen name, or combination of the two. The author's most recent status (if the authenticating user has permission) will be returned inline.
     * <br>This method calls https://api.twitter.com/1.1/users/lookup.json
     *
     * @param ids Specifies the screen names of the users to return.
     * @return users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/lookup">GET users/lookup | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> lookupUsers(long... ids) throws TwitterException;

    /**
     * Return up to 100 users worth of extended information, specified by either ID, screen name, or combination of the two. The author's most recent status (if the authenticating user has permission) will be returned inline.
     * <br>This method calls https://api.twitter.com/1.1/users/lookup.json
     *
     * @param screenNames Specifies the screen names of the users to return.
     * @return users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/lookup">GET users/lookup | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> lookupUsers(String... screenNames) throws TwitterException;

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter. The author's most recent status will be returned inline.
     * <br>This method calls https://api.twitter.com/1.1/users/show.json
     *
     * @param userId the ID of the user for whom to request the detail
     * @return users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/show">GET users/show | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    User showUser(long userId) throws TwitterException;

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter. The author's most recent status will be returned inline.
     * <br>This method calls https://api.twitter.com/1.1/users/show.json
     *
     * @param screenName the screen name of the user for whom to request the detail
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/show">GET users/show | Twitter Developers</a>
     */
    User showUser(String screenName) throws TwitterException;

    /**
     * Run a search for users similar to the Find People button on Twitter.com; the same results returned by people search on Twitter.com will be returned by using this API.<br>
     * Usage note: It is only possible to retrieve the first 1000 matches from this API.
     * <br>This method calls https://api.twitter.com/1.1/users/search.json
     *
     * @param query The query to run against people search.
     * @param page  Specifies the page of results to retrieve. Number of statuses per page is fixed to 20.
     * @return the list of Users matches the provided
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/search">GET users/search | Twitter Developers</a>
     */
    ResponseList<User> searchUsers(String query, int page) throws TwitterException;

    /**
     * Returns an array of users that the specified user can contribute to.
     *
     * @param userId The user id of the user for whom to return results for
     * @return list of contributors
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/contributees">GET users/contributors | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<User> getContributees(long userId) throws TwitterException;

    /**
     * Returns an array of users that the specified user can contribute to.
     *
     * @param screenName The screen name of the user for whom to return results for
     * @return list of contributors
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/contributees">GET users/contributors | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<User> getContributees(String screenName) throws TwitterException;

    /**
     * Returns an array of users who can contribute to the specified account.
     *
     * @param userId The user id of the user for whom to return results for
     * @return list of contributors
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/contributors">GET users/contributors | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<User> getContributors(long userId) throws TwitterException;

    /**
     * Returns an array of users who can contribute to the specified account.
     *
     * @param screenName The screen name of the user for whom to return results for
     * @return list of contributors
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/users/contributors">GET users/contributors | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<User> getContributors(String screenName) throws TwitterException;

    /**
     * Removes the uploaded profile banner for the authenticating user. Returns HTTP 200 upon success.
     * <br>This method calls https://api.twitter.com/1.1/account/remove_profile_banner.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/remove_profile_banner">POST account/remove_profile_banner | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     * @throws TwitterException when Twitter service or network is unavailable
     */
    void removeProfileBanner() throws TwitterException;

    /**
     * Uploads a profile banner on behalf of the authenticating user. For best results, upload an &lt;5MB image that is exactly 1252px by 626px. Images will be resized for a number of display options. Users with an uploaded profile banner will have a profile_banner_url node in their <a href="https://dev.twitter.com/docs/platform-objects/users">Users</a> objects. More information about sizing variations can be found in <a href="https://dev.twitter.com/docs/user-profile-images-and-banners">User Profile Images and Banners</a>.<br>
     * Profile banner images are processed asynchronously. The profile_banner_url and its variant sizes will not necessary be available directly after upload.<br>
     * <br>This method calls https://api.twitter.com/1.1/account/update_profile_banner.json
     *
     * @param image For best results, upload an &lt;5MB image that is exactly 1252px by 626px.
     * @throws TwitterException when Twitter service or network is unavailable,
     *                          or when the specified file is not found (FileNotFoundException will be nested),
     *                          or when the specified file object in not representing a file (IOException will be nested)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile_banner">POST account/update_profile_banner | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void updateProfileBanner(File image) throws TwitterException;

    /**
     * Uploads a profile banner on behalf of the authenticating user. For best results, upload an &lt;5MB image that is exactly 1252px by 626px. Images will be resized for a number of display options. Users with an uploaded profile banner will have a profile_banner_url node in their <a href="https://dev.twitter.com/docs/platform-objects/users">Users</a> objects. More information about sizing variations can be found in <a href="https://dev.twitter.com/docs/user-profile-images-and-banners">User Profile Images and Banners</a>.<br>
     * Profile banner images are processed asynchronously. The profile_banner_url and its variant sizes will not necessary be available directly after upload.<br>
     * <br>This method calls https://api.twitter.com/1.1/account/update_profile_banner.json
     *
     * @param image For best results, upload an &lt;5MB image that is exactly 1252px by 626px.
     * @throws TwitterException when Twitter service or network is unavailable,
     *                          or when the specified file is not found (FileNotFoundException will be nested),
     *                          or when the specified file object in not representing a file (IOException will be nested)
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/account/update_profile_banner">POST account/update_profile_banner | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    void updateProfileBanner(InputStream image) throws TwitterException;
}
