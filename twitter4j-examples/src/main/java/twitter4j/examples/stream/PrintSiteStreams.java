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
package twitter4j.examples.stream;

import twitter4j.DirectMessage;
import twitter4j.SiteStreamsListener;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;

/**
 * <p>This is a code example of Twitter4J Streaming API - Site Streams support.<br>
 * Usage: java twitter4j.examples.stream.PrintSiteStreams [follow(comma separated numerical user ids)]<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class PrintSiteStreams {
    /**
     * Main entry of this application.
     *
     * @param args follow(comma separated user ids) track(comma separated filter terms)
     * @throws twitter4j.TwitterException
     */
    public static void main(String[] args) throws TwitterException {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.PrintSiteStreams [follow(comma separated numerical user ids)]");
            System.exit(-1);
        }

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);

        String[] split  = args[0].split(",");
        int[] followArray = new int[split.length];
        for (int i = 0; i < followArray.length; i++) {
            followArray[i] = Integer.parseInt(split[i]);
        }

        // site() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.site(true, followArray);
    }

    static SiteStreamsListener listener = new SiteStreamsListener() {
        public void onStatus(int forUser, Status status) {
            System.out.println("onStatus for_user:" + forUser + " @" + status.getUser().getScreenName() + " - " + status.getText());
        }

        public void onDeletionNotice(int forUser, StatusDeletionNotice statusDeletionNotice) {
            System.out.println("Got a status deletion notice for_user:"
                    + forUser + " id:" + statusDeletionNotice.getStatusId());
        }

        public void onFriendList(int forUser, int[] friendIds) {
            System.out.print("onFriendList for_user:" + forUser);
            for(int friendId : friendIds){
                System.out.print(" " + friendId);
            }
            System.out.println();
        }

        public void onFavorite(int forUser, User source, User target, Status favoritedStatus) {
            System.out.println("onFavorite for_user:" + forUser + " source:@"
                    + source.getScreenName() + " target:@"
                    + target.getScreenName() + " @"
                    + favoritedStatus.getUser().getScreenName() + " - "
                    + favoritedStatus.getText());
        }

        public void onUnfavorite(int forUser, User source, User target, Status unfavoritedStatus) {
            System.out.println("onUnFavorite for_user:" + forUser + " source:@"
                    + source.getScreenName() + " target:@"
                    + target.getScreenName() + " @"
                    + unfavoritedStatus.getUser().getScreenName()
                    + " - " + unfavoritedStatus.getText());
        }

        public void onFollow(int forUser, User source, User followedUser) {
            System.out.println("onFollow for_user:" + forUser + " source:@"
                    + source.getScreenName() + " target:@"
                    + followedUser.getScreenName());
        }

        public void onDirectMessage(int forUser, DirectMessage directMessage) {
            System.out.println("onDirectMessage for_user:" + forUser + " text:"
                    + directMessage.getText());
        }

        public void onDeletionNotice(int forUser, int directMessageId, int userId) {
            System.out.println("Got a direct message deletion notice for_user:"
                    + forUser + " id:" + directMessageId);
        }

        public void onUserListSubscription(int forUser, User subscriber, User listOwner, UserList list) {
            System.out.println("onUserListSubscribed for_user:" + forUser
                    + " subscriber:@" + subscriber.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        public void onUserListCreation(int forUser, User listOwner, UserList list) {
            System.out.println("onUserListCreated for_user:" + forUser
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        public void onUserListUpdate(int forUser, User listOwner, UserList list) {
            System.out.println("onUserListUpdated for_user:" + forUser
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        public void onUserListDeletion(int forUser, User listOwner, UserList list) {
            System.out.println("onUserListDestroyed for_user:" + forUser
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        public void onUserProfileUpdate(int forUser, User updatedUser) {
            System.out.println("onUserProfileUpdated for_user:" + forUser
                    + " user:@" + updatedUser.getScreenName());
        }

        public void onBlock(int forUser, User source, User blockedUser) {
            System.out.println("onBlock for_user:" + forUser
                    + " source:@" + source.getScreenName()
                    + " target:@" + blockedUser.getScreenName());
        }

        public void onUnblock(int forUser, User source, User unblockedUser) {
            System.out.println("onUnblock for_user:" + forUser
                    + " source:@" + source.getScreenName()
                    + " target:@" + unblockedUser.getScreenName());
        }

        public void onException(Exception ex) {
            ex.printStackTrace();
            System.out.println("onException:" + ex.getMessage());
        }
    };
}
