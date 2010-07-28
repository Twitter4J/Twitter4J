/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j;

/**
 * @author Rémy Rakic at gmail.com
 * @since Twitter4J 2.1.3
 */
public class UserStreamAdapter extends StatusAdapter implements UserStreamListener {
    public void onFriendList(int[] friendIds) {
    }
    public void onFavorite(User source, User target, Status targetObject) {
    }

    public void onFollow(User source, User target) {
    }

    public void onUnfavorite(User source, User target, Status targetObject) {
    }

    public void onRetweet(User source, User target, Status targetObject) {
    }

    public void onUnfollow(User source, User target) {
    }

    public void onDirectMessage(DirectMessage directMessage) {
    }

    public void onUserListSubscribed(User subscriber, User listOwner, UserList list) {
    }

    public void onUserListCreated(User listOwner, UserList list) {
    }

    public void onUserListUpdated(User listOwner, UserList list) {
    }

    public void onUserListDestroyed(User listOwner, UserList list) {
    }

    public void onBlock(User source, User target) {
    }

    public void onUnblock(User source, User target) {
    }
}
