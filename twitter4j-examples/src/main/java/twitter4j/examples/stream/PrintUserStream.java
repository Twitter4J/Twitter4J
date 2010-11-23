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
package twitter4j.examples.stream;

import twitter4j.*;

import java.util.HashSet;
import java.util.Set;



/**
 * <p>
 * This is a code example of Twitter4J Streaming API - user stream.<br>
 * Usage: java twitter4j.examples.PrintUserStream. Needs a valid twitter4j.properties file with Basic Auth _and_ OAuth properties set<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @author Rémy Rakic - remy dot rakic at gmail.com
 */
public final class PrintUserStream implements UserStreamListener {
    public static void main(String[] args) throws TwitterException {
        PrintUserStream printSampleStream = new PrintUserStream();
        printSampleStream.startConsuming();
    }

    private TwitterStream twitterStream;

    // used for getting user info
    private Twitter twitter;
    private int currentUserId;

    private  PrintUserStream() {
        twitterStream = new TwitterStreamFactory(this).getInstance();
        twitter = new TwitterFactory().getInstance();

        try {
            User currentUser = twitter.verifyCredentials();
            currentUserId = currentUser.getId();
        } catch (TwitterException e) {
            System.out.println("Unexpected exception caught while trying to retrieve the current user: " + e);
            e.printStackTrace();
        }
    }

    private void startConsuming() throws TwitterException {
        // the user() method internally creates a thread which manipulates
        // TwitterStream and calls these adequate listener methods continuously.
        twitterStream.user();
    }

    private Set<Integer> friends;

    public void onFriendList(int[] friendIds) {
        System.out.println("Received friends list - Following " + friendIds.length + " people");

        friends = new HashSet<Integer>(friendIds.length);
        for (int id : friendIds)
            friends.add(id);
    }

    public void onStatus(Status status) {
//        int replyTo = status.getInReplyToUserId();
//        if (replyTo > 0 && !friends.contains(replyTo) && currentUserId != replyTo){
//            System.out.print("[Out of band] "); // I've temporarily labeled "out of band" messages that are sent to people you don't follow
//        }
//
        User user = status.getUser();

        System.out.println(user.getName() + " [" + user.getScreenName() + "] : " + status.getText());
    }

    public void onDirectMessage(DirectMessage dm) {
        System.out.println("DM from " + dm.getSenderScreenName() + " to " + dm.getRecipientScreenName() + ": " + dm.getText());
    }

    public void onUserListSubscribed(User subscriber, User listOwner, UserList list) {
        System.out.println("List subscribed " + list.getName());
    }

    public void onUserListCreated(User listOwner, UserList list) {
        System.out.println("List created " + list.getName());
    }

    public void onUserListUpdated(User listOwner, UserList list) {
        System.out.println("List updated " + list.getName());
    }

    public void onUserListDestroyed(User listOwner, UserList list) {
        System.out.println("List destroyed " + list.getName());
    }

    public void onDeletionNotice(StatusDeletionNotice notice) {
        User user = friend(notice.getUserId());
        if (user == null)
            return;
        System.out.println(user.getName() + " [" + user.getScreenName() + "] deleted the tweet "
                + notice.getStatusId());
    }

    private User friend(int userId) {
        try {
            return twitter.showUser(userId);
        } catch (TwitterException e) {
            System.out.println("Unexpected exception caught while trying to show user " + userId + ": " + e);
            e.printStackTrace();
        }

        return null;
    }

    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("track limitation: " + numberOfLimitedStatuses);
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
    }

    public void onFavorite(User source, User target, Status favoritedStatus) {
        System.out.println(source.getName() + " [" + source.getScreenName() + "] favorited "
                + target.getName() + "'s [" + target.getScreenName() + "] tweet: " + favoritedStatus.getText());
    }

    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        System.out.println(source.getName() + " [" + source.getScreenName() + "] unfavorited "
                + target.getName() + "'s [" + target.getScreenName() + "] tweet: " + unfavoritedStatus.getText());
    }

    public void onFollow(User source, User target) {
        System.out.println(source.getName() + " [" + source.getScreenName() + "] started following "
                + target.getName() + " [" + target.getScreenName() + "]");
    }

    public void onUnfollow(User source, User target) {
        System.out.println(source.getName() + " [" + source.getScreenName() + "] unfollowed "
                + target.getName() + " [" + target.getScreenName() + "]");

        if (source.getId() == currentUserId)
            friends.remove(target);
    }

    public void onUserSubscribedToList(User subscriber, User listOwner, UserList list) {
        System.out.println(subscriber.getName() + " [" + subscriber.getScreenName() + "] subscribed to "
                + listOwner.getName() + "'s [" + listOwner.getScreenName() + "] list: " + list.getName()
                + " [" + list.getFullName() + "]");
    }

    public void onUserCreatedList(User listOwner, UserList list) {
        System.out.println(listOwner.getName() + " [" + listOwner.getScreenName() + "] created list: " + list.getName()
                + " [" + list.getFullName() + "]");
    }

    public void onUserUpdatedList(User listOwner, UserList list) {
        System.out.println(listOwner.getName() + " [" + listOwner.getScreenName() + "] updated list: " + list.getName()
                + " [" + list.getFullName() + "]");
    }

    public void onUserDestroyedList(User listOwner, UserList list) {
        System.out.println(listOwner.getName() + " [" + listOwner.getScreenName() + "] destroyed list: " + list.getName()
                + " [" + list.getFullName() + "]");
    }

    public void onRetweet(User source, User target, Status retweetedStatus) {
    }

    public void onBlock(User source, User target) {
        System.out.println(source.getName() + " [" + source.getScreenName() + "] blocked "
                + target.getName() + " [" + target.getScreenName() + "]");
    }

    public void onUnblock(User source, User target) {
        System.out.println(source.getName() + " [" + source.getScreenName() + "] unblocked "
                + target.getName() + " [" + target.getScreenName() + "]");
    }
}