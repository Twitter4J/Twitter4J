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
     * @param friendIds friend ids
     */
    void onFriendList(long forUser, long[] friendIds);

    /**
     * @param forUser         the user id to whom sent the event
     * @param source          source user of the event
     * @param target          target user of the event
     * @param favoritedStatus the status favorited
     */
    void onFavorite(long forUser, User source, User target, Status favoritedStatus);

    /**
     * @param forUser           the user id to whom sent the event
     * @param source            source user of the event
     * @param target            target user of the event
     * @param unfavoritedStatus the status unfavorited
     */
    void onUnfavorite(long forUser, User source, User target, Status unfavoritedStatus);

    /**
     * @param forUser      the user id to whom sent the event
     * @param source       source user of the event
     * @param followedUser user followed
     */
    void onFollow(long forUser, User source, User followedUser);

    /**
     * @param forUser        the user id to whom sent the event
     * @param source         source user of the event
     * @param unfollowedUser user unfollowed
     * @since Twitter4J 2.1.11
     */
    void onUnfollow(long forUser, User source, User unfollowedUser);

    /**
     * @param forUser       the user id to whom sent the event
     * @param directMessage direct message received
     */
    void onDirectMessage(long forUser, DirectMessage directMessage);

    void onDeletionNotice(long forUser, long directMessageId, long userId);

    /**
     * @param forUser     the user id to whom sent the event
     * @param addedMember member added
     * @param listOwner   owner of the list
     * @param list        the list
     */
    void onUserListMemberAddition(long forUser, User addedMember, User listOwner, UserList list);

    /**
     * @param forUser       the user id to whom sent the event
     * @param deletedMember member deleted
     * @param listOwner     owner of the list
     * @param list          the list
     */
    void onUserListMemberDeletion(long forUser, User deletedMember, User listOwner, UserList list);

    /**
     * @param forUser    the user id to whom sent the event
     * @param subscriber member subscribed
     * @param listOwner  owner of the list
     * @param list       the list
     */
    void onUserListSubscription(long forUser, User subscriber, User listOwner, UserList list);

    /**
     * @param forUser    the user id to whom sent the event
     * @param subscriber user subscribed
     * @param listOwner  owner of the list
     * @param list       the list
     */
    void onUserListUnsubscription(long forUser, User subscriber, User listOwner, UserList list);

    /**
     * @param forUser   the user id to whom sent the event
     * @param listOwner owner of the list
     * @param list      the list
     */
    void onUserListCreation(long forUser, User listOwner, UserList list);

    /**
     * @param forUser   the user id to whom sent the event
     * @param listOwner owner of the list
     * @param list      the list
     */
    void onUserListUpdate(long forUser, User listOwner, UserList list);

    /**
     * @param forUser   the user id to whom sent the event
     * @param listOwner owner of the list
     * @param list      the list
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
     * @param suspendedUser suspended user id
     * @since Twitter4J 4.0.3
     */
    void onUserSuspension(long forUser, long suspendedUser);

    /**
     * @param forUser     the user id to whom sent the event
     * @param deletedUser deleted user id
     * @since Twitter4J 4.0.3
     */
    void onUserDeletion(long forUser, long deletedUser);

    /**
     * @param forUser     the user id to whom sent the event
     * @param source      the user user blocked
     * @param blockedUser the user got blocked
     */
    void onBlock(long forUser, User source, User blockedUser);

    /**
     * @param forUser       the user id to whom sent the event
     * @param source        the user unblocked
     * @param unblockedUser the user got unblocked
     */
    void onUnblock(long forUser, User source, User unblockedUser);

    /**
     * @param source          source user of the event
     * @param target          target user of the event
     * @param retweetedStatus status retweeted retweet
     * @since Twitter4J 4.0.x
     */
    void onRetweetedRetweet(User source,User target, Status retweetedStatus);

    /**
     * @param source          source user of the event
     * @param target          target user of the event
     * @param favoritedStatus status favorited retweet
     * @since Twitter4J 4.0.x
     */
    void onFavoritedRetweet(User source,User target, Status favoritedStatus);

    /**
     * callback method for {@link StreamController#removeUsers(long[])}
     * @param line notice
     */
    void onDisconnectionNotice(String line);

    @Override
    void onException(Exception ex);
}
