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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
public interface SiteStreamsListener extends StreamListener {
    void onStatus(long forUser, Status status);

    void onDeletionNotice(long forUser, StatusDeletionNotice statusDeletionNotice);

    /**
     * @param forUser   the user id to whom sent the event
     * @param friendIds
     */
    void onFriendList(long forUser, long[] friendIds);

    /**
     * @param forUser         the user id to whom sent the event
     * @param source
     * @param target
     * @param favoritedStatus
     */
    void onFavorite(long forUser, User source, User target, Status favoritedStatus);

    /**
     * @param forUser           the user id to whom sent the event
     * @param target
     * @param unfavoritedStatus
     */
    void onUnfavorite(long forUser, User source, User target, Status unfavoritedStatus);

    /**
     * @param forUser      the user id to whom sent the event
     * @param source
     * @param followedUser
     */
    void onFollow(long forUser, User source, User followedUser);

    /**
     * @param forUser        the user id to whom sent the event
     * @param source
     * @param unfollowedUser
     * @since Twitter4J 2.1.11
     */
    void onUnfollow(long forUser, User source, User unfollowedUser);

    /**
     * @param forUser       the user id to whom sent the event
     * @param directMessage
     */
    void onDirectMessage(long forUser, DirectMessage directMessage);

    void onDeletionNotice(long forUser, long directMessageId, long userId);

    /**
     * @param forUser     the user id to whom sent the event
     * @param addedMember
     * @param listOwner
     * @param list
     */
    void onUserListMemberAddition(long forUser, User addedMember, User listOwner, UserList list);

    /**
     * @param forUser       the user id to whom sent the event
     * @param deletedMember
     * @param listOwner
     * @param list
     */
    void onUserListMemberDeletion(long forUser, User deletedMember, User listOwner, UserList list);

    /**
     * @param forUser    the user id to whom sent the event
     * @param subscriber
     * @param listOwner
     * @param list
     */
    void onUserListSubscription(long forUser, User subscriber, User listOwner, UserList list);

    /**
     * @param forUser    the user id to whom sent the event
     * @param subscriber
     * @param listOwner
     * @param list
     */
    void onUserListUnsubscription(long forUser, User subscriber, User listOwner, UserList list);

    /**
     * @param forUser   the user id to whom sent the event
     * @param listOwner
     * @param list
     */
    void onUserListCreation(long forUser, User listOwner, UserList list);

    /**
     * @param forUser   the user id to whom sent the event
     * @param listOwner
     * @param list
     */
    void onUserListUpdate(long forUser, User listOwner, UserList list);

    /**
     * @param forUser   the user id to whom sent the event
     * @param listOwner
     * @param list
     */
    void onUserListDeletion(long forUser, User listOwner, UserList list);

    /**
     * @param forUser     the user id to whom sent the event
     * @param updatedUser updated user
     * @since Twitter4J 2.1.9
     */
    void onUserProfileUpdate(long forUser, User updatedUser);

    /**
     * @param forUser     the user id to whom sent the event
     * @param source
     * @param blockedUser
     */
    void onBlock(long forUser, User source, User blockedUser);

    /**
     * @param forUser       the user id to whom sent the event
     * @param source
     * @param unblockedUser
     */
    void onUnblock(long forUser, User source, User unblockedUser);

    /**
     * callback method for {@link StreamController#removeUsers(long[])}
     */
    void onDisconnectionNotice(String line);

    void onException(Exception ex);
}
