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

import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface NotificationMethods
{
	/**
	 * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/follow/[id].json
	 *
	 * @param screenName Specifies the screen name of the user to follow with device updates.
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications follow</a>
	 */
	User enableNotification(String screenName)
			throws TwitterException;

	/**
	 * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/follow/[id].json
	 *
	 * @param userId Specifies the ID of the user to follow with device updates.
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications follow</a>
	 */
	User enableNotification(int userId)
			throws TwitterException;

	/**
	 * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/leave/[id].json
	 *
	 * @param screenName Specifies the screen name of the user to disable device notifications.
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications leave</a>
	 */
	User disableNotification(String screenName)
			throws TwitterException;

	/**
	 * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * <br>This method calls http://api.twitter.com/1/notifications/leave/[id].json
	 *
	 * @param userId Specifies the ID of the user to disable device notifications.
	 * @return User
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications leave</a>
	 */
	User disableNotification(int userId)
			throws TwitterException;
}
