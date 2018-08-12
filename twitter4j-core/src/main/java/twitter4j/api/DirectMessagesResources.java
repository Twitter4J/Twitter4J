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

import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.TwitterException;

import java.io.InputStream;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface DirectMessagesResources {
    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/direct_messages">GET direct_messages | Twitter Developers</a>
     * @deprecated use {@link #getDirectMessages(int)} instead
     */
    ResponseList<DirectMessage> getDirectMessages()
        throws TwitterException;

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/direct_messages">GET direct_messages | Twitter Developers</a>
     * @deprecated use {@link #getDirectMessages(int)} instead
     */
    ResponseList<DirectMessage> getDirectMessages(Paging paging)
        throws TwitterException;

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/sent
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/direct_messages/sent">GET direct_messages/sent | Twitter Developers</a>
     * @deprecated use {@link #getDirectMessages(int)} instead
     */
    ResponseList<DirectMessage> getSentDirectMessages()
        throws TwitterException;

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/sent
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/direct_messages/sent">GET direct_messages/sent | Twitter Developers</a>
     * @since Twitter4J 2.0.1
     * @deprecated use {@link #getDirectMessages(int)} instead
     */
    ResponseList<DirectMessage> getSentDirectMessages(Paging paging)
        throws TwitterException;

    /**
     * Returns all Direct Message events (both sent and received) within the last 30 days. Sorted in reverse-chronological order.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/events/list.json
     *
     * @param count Max number of events to be returned. 20 default. 50 max.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/list-events.html">GET direct_messages/events/list — Twitter Developers</a>
     * @since Twitter4J 4.0.7
     */
    DirectMessageList getDirectMessages(int count)
            throws TwitterException;

    /**
     * Returns all Direct Message events (both sent and received) within the last 30 days. Sorted in reverse-chronological order.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/events/list.json
     *
     * @param count Max number of events to be returned. 20 default. 50 max.
     * @param cursor For paging through result sets greater than 1 page, use the “next_cursor” property from the previous request.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/list-events.html">GET direct_messages/events/list — Twitter Developers</a>
     * @since Twitter4J 4.0.7
     */
    DirectMessageList getDirectMessages(int count, String cursor)
            throws TwitterException;

    /**
     * Returns a single Direct Message event by the given id.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/events/show.json
     *
     * @param id message id
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    DirectMessage showDirectMessage(long id) throws TwitterException;

    /**
     * Deletes the direct message specified in the required ID parameter.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/events/destroy.json
     *
     * @param id The id of the Direct Message event that should be deleted.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/delete-message-event.html">DELETE direct_messages/events/destroy — Twitter Developers</a>
     * @return a dummy DirectMessage object. Starting from Twitter4J 4.0.7, all getters will throw IllegalStateException due to the API limitation. The return value will be changed to void in the furure release.
     * @since Twitter4J 2.0.1
     */
    DirectMessage destroyDirectMessage(long id)
        throws TwitterException;

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimmed if the length of the text is exceeding 140 characters.
     * <br>This method calls https://dev.twitter.com/rest/reference/post/direct_messages/events/new
     *
     * @param userId the user id of the user to whom send the direct message
     * @param text   The text of your direct message.
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/new-event">POST direct_messages/events/new (message_create) — Twitter Developers</a>
     * @since Twitter4j 2.1.0
     */
    DirectMessage sendDirectMessage(long userId, String text)
        throws TwitterException;

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimmed if the length of the text is exceeding 140 characters.
     * <br>This method calls https://dev.twitter.com/rest/reference/post/direct_messages/events/new
     *
     * @param userId the user id of the user to whom send the direct message
     * @param text   The text of your direct message.
     * @param mediaId id of media attachment
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/new-event">POST direct_messages/events/new (message_create) — Twitter Developers</a>
     * @since Twitter4J 4.0.7
     */
    DirectMessage sendDirectMessage(long userId, String text, long mediaId)
            throws TwitterException;

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimmed if the length of the text is exceeding 140 characters.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/new
     *
     * @param screenName the screen name of the user to whom send the direct message
     * @param text       The text of your direct message.
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/new-event">POST direct_messages/events/new (message_create) — Twitter Developers</a>
     */
    DirectMessage sendDirectMessage(String screenName, String text)
        throws TwitterException;

    /**
     * Returns a stream of the image included in direct messages.
     *
     * @param url image url
     * @return InputStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/discussions/24255">Access media shared in direct messages | Twitter Developers</a>
     * @since Twitter4J 3.0.6
     */
    InputStream getDMImageAsStream(String url)
        throws TwitterException;
}
