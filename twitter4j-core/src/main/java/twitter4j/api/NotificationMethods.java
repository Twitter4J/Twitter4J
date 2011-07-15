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

import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface NotificationMethods {
    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/follow.json
     *
     * @param screenName Specifies the screen name of the user to follow with device updates.
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/notifications/follow">POST notifications/follow | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    User enableNotification(String screenName)
            throws TwitterException;

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/follow.json
     *
     * @param userId Specifies the ID of the user to follow with device updates.
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/notifications/follow">POST notifications/follow | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    User enableNotification(long userId)
            throws TwitterException;

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/leave.json
     *
     * @param screenName Specifies the screen name of the user to disable device notifications.
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/notifications/leave">POST notifications/leave | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    User disableNotification(String screenName)
            throws TwitterException;

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/leave.json
     *
     * @param userId Specifies the ID of the user to disable device notifications.
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/post/notifications/leave">POST notifications/leave | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    User disableNotification(long userId)
            throws TwitterException;
}
