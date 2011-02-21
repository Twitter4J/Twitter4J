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
 * @author Rémy Rakic at gmail.com
 * @since Twitter4J 2.1.3
 */
public class UserStreamAdapter extends StatusAdapter implements UserStreamListener {
    public void onDeletionNotice(long directMessageId, long userId) {
    }

    public void onFriendList(long[] friendIds) {
    }

    public void onFavorite(User source, User target, Status favoritedStatus) {
    }

    public void onFollow(User source, User followedUser) {
    }

    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
    }

    public void onRetweet(User source, User target, Status retweetedStatus) {
    }

    public void onDirectMessage(DirectMessage directMessage) {
    }

    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
    }

    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
    }

    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
    }

    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
    }

    public void onUserListCreation(User listOwner, UserList list) {
    }

    public void onUserListUpdate(User listOwner, UserList list) {
    }

    public void onUserListDeletion(User listOwner, UserList list) {
    }

    public void onUserProfileUpdate(User updatedUser) {
    }

    public void onBlock(User source, User blockedUser) {
    }

    public void onUnblock(User source, User unblockedUser) {
    }

    public void onException(Exception ex) {
    }
}
