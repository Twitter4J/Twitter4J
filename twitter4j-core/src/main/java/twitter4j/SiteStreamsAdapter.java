package twitter4j;/*
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


/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
public class SiteStreamsAdapter implements SiteStreamsListener {
    public void onStatus(long forUser, Status status) {
    }

    public void onDeletionNotice(long forUser, StatusDeletionNotice statusDeletionNotice) {
    }

    public void onFriendList(long forUser, long[] friendIds) {
    }

    public void onFavorite(long forUser, User source, User target, Status favoritedStatus) {
    }

    public void onUnfavorite(long forUser, User source, User target, Status unfavoritedStatus) {
    }

    public void onFollow(long forUser, User source, User followedUser) {
    }

    public void onUnfollow(long forUser, User source, User followedUser) {
    }

    public void onDirectMessage(long forUser, DirectMessage directMessage) {
    }

    public void onDeletionNotice(long forUser, long directMessageId, long userId) {
    }

    public void onUserListMemberAddition(long forUser, User addedUser, User listOwner, UserList list) {
    }

    public void onUserListMemberDeletion(long forUser, User deletedUser, User listOwner, UserList list) {
    }

    public void onUserListSubscription(long forUser, User subscriber, User listOwner, UserList list) {
    }

    public void onUserListUnsubscription(long forUser, User subscriber, User listOwner, UserList list) {
    }

    public void onUserListCreation(long forUser, User listOwner, UserList list) {
    }

    public void onUserListUpdate(long forUser, User listOwner, UserList list) {
    }

    public void onUserListDeletion(long forUser, User listOwner, UserList list) {
    }

    public void onUserProfileUpdate(long forUser, User updatedUser) {
    }

    public void onBlock(long forUser, User source, User blockedUser) {
    }

    public void onUnblock(long forUser, User source, User unblockedUser) {
    }

    public void onException(Exception ex) {
    }
}
