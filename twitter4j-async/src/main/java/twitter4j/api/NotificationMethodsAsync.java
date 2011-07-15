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

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface NotificationMethodsAsync {
    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/follow.json
     *
     * @param screenName Specifies the screen name of the user to follow with device updates.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/notifications/follow">POST notifications/follow | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void enableNotification(String screenName);

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/follow.json
     *
     * @param userId Specifies the ID of the user to follow with device updates.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/notifications/follow">POST notifications/follow | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void enableNotification(long userId);

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/leave.json
     *
     * @param screenName Specifies the screen name of the user to disable device notifications.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/notifications/leave">POST notifications/leave | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     */
    void disableNotification(String screenName);

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/leave.json
     *
     * @param userId Specifies the ID of the user to disable device notifications.
     * @see <a href="https://dev.twitter.com/docs/api/1/post/notifications/leave">POST notifications/leave | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    void disableNotification(long userId);
}
