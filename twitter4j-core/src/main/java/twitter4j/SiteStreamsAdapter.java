package twitter4j;/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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

    public void onDirectMessage(int forUser, DirectMessage directMessage) {
    }

    public void onDeletionNotice(int forUser, int directMessageId, int userId) {
    }

    public void onUserListSubscription(int forUser, User subscriber, User listOwner, UserList list) {
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
