/*
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
package twitter4j;

/**
 * @author Rémy Rakic at gmail.com
 * @since Twitter4J 2.1.3
 */
public interface UserStreamListener extends StatusListener {
    void onDeletionNotice(long directMessageId, int userId);

    /**
     * @param friendIds
     * @since Twitter4J 2.1.3
     */
    void onFriendList(int[] friendIds);

    /**
     * @param source
     * @param target
     * @param favoritedStatus
     * @since Twitter4J 2.1.3
     */
    void onFavorite(User source, User target, Status favoritedStatus);

    /**
     * @param source
     * @param target
     * @param unfavoritedStatus
     * @since Twitter4J 2.1.3
     */
    void onUnfavorite(User source, User target, Status unfavoritedStatus);

    /**
     * @param source
     * @param followedUser
     * @since Twitter4J 2.1.3
     */
    void onFollow(User source, User followedUser);

    /**
     * @param source
     * @param target
     * @param retweetedStatus
     * @since Twitter4J 2.1.3
     */
    void onRetweet(User source, User target, Status retweetedStatus);

    /**
     * @param directMessage
     * @since Twitter4J 2.1.3
     */
    void onDirectMessage(DirectMessage directMessage);

    /**
     * @param subscriber
     * @param listOwner
     * @param list
     * @since Twitter4J 2.1.3
     */
    void onUserListSubscription(User subscriber, User listOwner, UserList list);

    /**
     * @param listOwner
     * @param list
     * @since Twitter4J 2.1.3
     */
    void onUserListCreation(User listOwner, UserList list);

    /**
     * @param listOwner
     * @param list
     * @since Twitter4J 2.1.3
     */
    void onUserListUpdate(User listOwner, UserList list);

    /**
     * @param listOwner
     * @param list
     * @since Twitter4J 2.1.3
     */
    void onUserListDeletion(User listOwner, UserList list);

    /**
     * @param updatedUser updated user
     * @since Twitter4J 2.1.9
     */
    void onUserProfileUpdate(User updatedUser);

    /**
     * @param source
     * @param blockedUser
     * @since Twitter4J 2.1.3
     */
    void onBlock(User source, User blockedUser);

    /**
     * @param source
     * @param unblockedUser
     * @since Twitter4J 2.1.3
     */
    void onUnblock(User source, User unblockedUser);
}
