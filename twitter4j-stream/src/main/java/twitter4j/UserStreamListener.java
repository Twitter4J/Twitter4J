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
 * @author yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
public interface UserStreamListener extends StatusListener {
    void onDeletionNotice(long directMessageId, long userId);

    /**
     * @param friendIds friend ids
     * @since Twitter4J 2.1.3
     */
    void onFriendList(long[] friendIds);

    /**
     * @param source          source user of the event
     * @param target          target user of the event
     * @param favoritedStatus status favorited
     * @since Twitter4J 2.1.3
     */
    void onFavorite(User source, User target, Status favoritedStatus);

    /**
     * @param source            source user of the event
     * @param target            target user of the event
     * @param unfavoritedStatus status unfavorited
     * @since Twitter4J 2.1.3
     */
    void onUnfavorite(User source, User target, Status unfavoritedStatus);

    /**
     * @param source       source user of the event
     * @param followedUser user followed
     * @since Twitter4J 2.1.3
     */
    void onFollow(User source, User followedUser);

    /**
     * @param source         source user of the event
     * @param unfollowedUser user unfollowed
     * @since Twitter4J 4.0.1
     */
    void onUnfollow(User source, User unfollowedUser);

    /**
     * @param directMessage direct message
     * @since Twitter4J 2.1.3
     */
    void onDirectMessage(DirectMessage directMessage);

    /**
     * @param addedMember member added
     * @param listOwner   owner of the list
     * @param list        the list
     * @since Twitter4J 2.1.11
     */
    void onUserListMemberAddition(User addedMember, User listOwner, UserList list);

    /**
     * @param deletedMember user deleted
     * @param listOwner     owner of the list
     * @param list          the list
     * @since Twitter4J 2.1.11
     */
    void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list);

    /**
     * @param subscriber the user subscribed
     * @param listOwner  owner of the list
     * @param list       the list
     * @since Twitter4J 2.1.3
     */
    void onUserListSubscription(User subscriber, User listOwner, UserList list);

    /**
     * @param subscriber the user subscribed
     * @param listOwner  owner of the list
     * @param list       the list
     * @since Twitter4J 2.1.11
     */
    void onUserListUnsubscription(User subscriber, User listOwner, UserList list);

    /**
     * @param listOwner owner of the list
     * @param list      the list
     * @since Twitter4J 2.1.3
     */
    void onUserListCreation(User listOwner, UserList list);

    /**
     * @param listOwner owner of the list
     * @param list      the list
     * @since Twitter4J 2.1.3
     */
    void onUserListUpdate(User listOwner, UserList list);

    /**
     * @param listOwner owner of the list
     * @param list      the list
     * @since Twitter4J 2.1.3
     */
    void onUserListDeletion(User listOwner, UserList list);

    /**
     * @param updatedUser updated user
     * @since Twitter4J 2.1.9
     */
    void onUserProfileUpdate(User updatedUser);

    /**
     * @param suspendedUser suspended user id
     * @since Twitter4J 4.0.3
     */
    void onUserSuspension(long suspendedUser);

    /**
     * @param deletedUser deleted user id
     * @since Twitter4J 4.0.3
     */
    void onUserDeletion(long deletedUser);

    /**
     * @param source      source user of the event
     * @param blockedUser the user blocked
     * @since Twitter4J 2.1.3
     */
    void onBlock(User source, User blockedUser);

    /**
     * @param source        source user of the event
     * @param unblockedUser the user unblocked
     * @since Twitter4J 2.1.3
     */
    void onUnblock(User source, User unblockedUser);

    /**
     * @param source          source user of the event
     * @param target          target user of the event
     * @param retweetedStatus status retweeted retweet
     * @since Twitter4J 4.0.4
     */
    void onRetweetedRetweet(User source,User target, Status retweetedStatus);

    /**
     * @param source          source user of the event
     * @param target          target user of the event
     * @param favoritedRetweeet status favorited retweet
     * @since Twitter4J 4.0.4
     */
    void onFavoritedRetweet(User source,User target, Status favoritedRetweeet);

    /**
     * @param source          source user of the event
     * @param target          target user of the event
     * @param quotingTweet    status quoting the tweet
     * @since Twitter4J 4.0.4
     */
    void onQuotedTweet(User source, User target, Status quotingTweet);
}
