/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.api;

import twitter4j.Device;

import java.io.File;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface AccountMethodsAsync {
	/**
	 * Gets the remaining number of API requests available to the requesting user before the API limit is reached for the current hour. Calls to rate_limit_status do not count against the rate limit.  If authentication credentials are provided, the rate limit status for the authenticating user is returned.  Otherwise, the rate limit status for the requester's IP address is returned.
	 * <br>This method calls http://api.twitter.com/1/account/rate_limit_status
	 *
	 * @since Twitter4J 1.1.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
	 */
	void getRateLimitStatus();

	/**
	 * Sets which device Twitter delivers updates to for the authenticating user.  Sending none as the device parameter will disable IM or SMS updates.
	 * <br>This method calls http://api.twitter.com/1/account/update_delivery_device
	 *
	 * @param device   new Delivery device. Must be one of: IM, SMS, NONE.
	 * @since Twitter4J 1.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-account%C2%A0update_delivery_device">Twitter API Wiki / Twitter REST API Method: account update_delivery_device</a>
	 */
	void updateDeliveryDevice(Device device);

	/**
	 * Sets one or more hex values that control the color scheme of the authenticating user's profile page on twitter.com.  These values are also returned in the getUserDetail() method.
	 * <br>This method calls http://api.twitter.com/1/account/update_profile_colors
	 * @param profileBackgroundColor optional, can be null
	 * @param profileTextColor optional, can be null
	 * @param profileLinkColor optional, can be null
	 * @param profileSidebarFillColor optional, can be null
	 * @param profileSidebarBorderColor optional, can be null
	 * @since Twitter4J 2.0.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-account%C2%A0update_profile_colors">Twitter API Wiki / Twitter REST API Method: account update_profile_colors</a>
	 */
	void updateProfileColors(String profileBackgroundColor, String profileTextColor, String profileLinkColor, String profileSidebarFillColor, String profileSidebarBorderColor);

	/**
	 * Updates the authenticating user's profile image.
	 * <br>This method calls http://api.twitter.com/1/account/update_profile_image.json
	 * @param image Must be a valid GIF, JPG, or PNG image of less than 700 kilobytes in size.  Images with width larger than 500 pixels will be scaled down.
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_profile_image">Twitter API Wiki / Twitter REST API Method: account update_profile_image</a>
	 */
	void updateProfileImage(File image);

	/**
	 * Updates the authenticating user's profile background image.
	 * <br>This method calls http://api.twitter.com/1/account/update_profile_background_image.json
	 * @param image Must be a valid GIF, JPG, or PNG image of less than 800 kilobytes in size.  Images with width larger than 2048 pixels will be forceably scaled down.
	 * @param tile If set to true the background image will be displayed tiled. The image will not be tiled otherwise.
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_profile_background_image">Twitter API Wiki / Twitter REST API Method: account update_profile_background_image</a>
	 */
	void updateProfileBackgroundImage(File image, boolean tile);

	/**
	 * Sets values that users are able to set under the "Account" tab of their settings page. Only the parameters specified(non-null) will be updated.
	 *
	 * @param name        Optional. Maximum of 20 characters.
	 * @param email       Optional. Maximum of 40 characters. Must be a valid email address.
	 * @param url         Optional. Maximum of 100 characters. Will be prepended with "http://" if not present.
	 * @param location    Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
	 * @param description Optional. Maximum of 160 characters.
	 * @since Twitter4J 2.0.2
	 */
	void updateProfile(String name, String email, String url, String location, String description);
}
