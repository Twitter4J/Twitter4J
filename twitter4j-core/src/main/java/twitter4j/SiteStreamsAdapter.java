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
    public void onStatus(int forUser, Status status) {
    }

    public void onDeletionNotice(int forUser, StatusDeletionNotice statusDeletionNotice) {
    }

    public void onFriendList(int forUser, int[] friendIds) {
    }

    public void onFavorite(int forUser, User source, User target, Status favoritedStatus) {
    }

    public void onUnfavorite(int forUser, User source, User target, Status unfavoritedStatus) {
    }

    public void onFollow(int forUser, User source, User followedUser) {
    }

    public void onUnfollow(int forUser, User source, User followedUser) {
    }

    public void onDirectMessage(int forUser, DirectMessage directMessage) {
    }

    public void onDeletionNotice(int forUser, int directMessageId, int userId) {
    }

    public void onUserListMemberAddition(int forUser, User addedUser, User listOwner, UserList list) {
    }

    public void onUserListMemberDeletion(int forUser, User deletedUser, User listOwner, UserList list) {
    }

    public void onUserListSubscription(int forUser, User subscriber, User listOwner, UserList list) {
    }

    public void onUserListUnsubscription(int forUser, User subscriber, User listOwner, UserList list) {
    }

    public void onUserListCreation(int forUser, User listOwner, UserList list) {
    }

    public void onUserListUpdate(int forUser, User listOwner, UserList list) {
    }

    public void onUserListDeletion(int forUser, User listOwner, UserList list) {
    }

    public void onUserProfileUpdate(int forUser, User updatedUser) {
    }

    public void onBlock(int forUser, User source, User blockedUser) {
    }

    public void onUnblock(int forUser, User source, User unblockedUser) {
    }

    public void onException(Exception ex) {
    }
}
