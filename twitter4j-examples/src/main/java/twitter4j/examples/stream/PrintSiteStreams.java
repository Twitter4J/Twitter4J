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

package twitter4j.examples.stream;

import twitter4j.*;

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

        String[] split = args[0].split(",");
        long[] followArray = new long[split.length];
        for (int i = 0; i < followArray.length; i++) {
            followArray[i] = Long.parseLong(split[i]);
        }

        // site() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.site(true, followArray);
    }

    static SiteStreamsListener listener = new SiteStreamsListener() {
        @Override
        public void onStatus(long forUser, Status status) {
            System.out.println("onStatus for_user:" + forUser + " @" + status.getUser().getScreenName() + " - " + status.getText());
        }

        @Override
        public void onDeletionNotice(long forUser, StatusDeletionNotice statusDeletionNotice) {
            System.out.println("Got a status deletion notice for_user:"
                    + forUser + " id:" + statusDeletionNotice.getStatusId());
        }

        @Override
        public void onFriendList(long forUser, long[] friendIds) {
            System.out.print("onFriendList for_user:" + forUser);
            for (long friendId : friendIds) {
                System.out.print(" " + friendId);
            }
            System.out.println();
        }

        @Override
        public void onFavorite(long forUser, User source, User target, Status favoritedStatus) {
            System.out.println("onFavorite for_user:" + forUser + " source:@"
                    + source.getScreenName() + " target:@"
                    + target.getScreenName() + " @"
                    + favoritedStatus.getUser().getScreenName() + " - "
                    + favoritedStatus.getText());
        }

        @Override
        public void onUnfavorite(long forUser, User source, User target, Status unfavoritedStatus) {
            System.out.println("onUnFavorite for_user:" + forUser + " source:@"
                    + source.getScreenName() + " target:@"
                    + target.getScreenName() + " @"
                    + unfavoritedStatus.getUser().getScreenName()
                    + " - " + unfavoritedStatus.getText());
        }

        @Override
        public void onFollow(long forUser, User source, User followedUser) {
            System.out.println("onFollow for_user:" + forUser + " source:@"
                    + source.getScreenName() + " target:@"
                    + followedUser.getScreenName());
        }

        @Override
        public void onUnfollow(long forUser, User source, User followedUser) {
            System.out.println("onUnfollow for_user:" + forUser + " source:@"
                    + source.getScreenName() + " target:@"
                    + followedUser.getScreenName());
        }

        @Override
        public void onDirectMessage(long forUser, DirectMessage directMessage) {
            System.out.println("onDirectMessage for_user:" + forUser + " text:"
                    + directMessage.getText());
        }

        @Override
        public void onDeletionNotice(long forUser, long directMessageId, long userId) {
            System.out.println("Got a direct message deletion notice for_user:"
                    + forUser + " id:" + directMessageId);
        }

        @Override
        public void onUserListMemberAddition(long forUser, User addedMember, User listOwner, UserList list) {
            System.out.println("onUserListMemberAddition for_user:" + forUser
                    + " member:@" + addedMember.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListMemberDeletion(long forUser, User deletedMember, User listOwner, UserList list) {
            System.out.println("onUserListMemberDeletion for_user:" + forUser
                    + " member:@" + deletedMember.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListSubscription(long forUser, User subscriber, User listOwner, UserList list) {
            System.out.println("onUserListSubscribed for_user:" + forUser
                    + " subscriber:@" + subscriber.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListUnsubscription(long forUser, User subscriber, User listOwner, UserList list) {
            System.out.println("onUserListUnsubscribed for_user:" + forUser
                    + " subscriber:@" + subscriber.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListCreation(long forUser, User listOwner, UserList list) {
            System.out.println("onUserListCreated for_user:" + forUser
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListUpdate(long forUser, User listOwner, UserList list) {
            System.out.println("onUserListUpdated for_user:" + forUser
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListDeletion(long forUser, User listOwner, UserList list) {
            System.out.println("onUserListDestroyed for_user:" + forUser
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserProfileUpdate(long forUser, User updatedUser) {
            System.out.println("onUserProfileUpdated for_user:" + forUser
                    + " user:@" + updatedUser.getScreenName());
        }

        @Override
        public void onBlock(long forUser, User source, User blockedUser) {
            System.out.println("onBlock for_user:" + forUser
                    + " source:@" + source.getScreenName()
                    + " target:@" + blockedUser.getScreenName());
        }

        @Override
        public void onUnblock(long forUser, User source, User unblockedUser) {
            System.out.println("onUnblock for_user:" + forUser
                    + " source:@" + source.getScreenName()
                    + " target:@" + unblockedUser.getScreenName());
        }

        @Override
        public void onDisconnectionNotice(String line) {
            System.out.println("onDisconnectionNotice:" + line);
        }

        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();
            System.out.println("onException:" + ex.getMessage());
        }
    };
}
