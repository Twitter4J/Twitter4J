/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import java.util.List;

/**
 * A listner for receiving asynchronous responses from Twitter Async APIs.
 *
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterAdapter
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface TwitterListener {
    /*Search API Methods*/
    void searched(QueryResult queryResult);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotTrends(Trends trends);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotCurrentTrends(Trends trends);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotDailyTrends(List<Trends> trendsList);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotWeeklyTrends(List<Trends> trendsList);

    /*Timeline Methods*/
    void gotPublicTimeline(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.0.10
     */
    void gotHomeTimeline(ResponseList<Status> statuses);

    void gotFriendsTimeline(ResponseList<Status> statuses);

    void gotUserTimeline(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.0.1
     */
    void gotMentions(ResponseList<Status> statuses);
    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetedByMe(ResponseList<Status> statuses);
    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetedToMe(ResponseList<Status> statuses);
    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetsOfMe(ResponseList<Status> statuses);


    /*Status Methods*/
    /**
     * @since Twitter4J 2.0.1
     */
    void gotShowStatus(Status status);

    void updatedStatus(Status status);

    void destroyedStatus(Status destroyedStatus);

    /**
     * @since Twitter4J 2.0.10
     */
    void retweetedStatus(Status retweetedStatus);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotRetweets(ResponseList<Status> retweets);

    /*User Methods*/
    void gotUserDetail(User user);

    /**
     * @since Twitter4J 2.1.0
     */
    void searchedUser(ResponseList<User> userList);


    void gotFriendsStatuses(PagableResponseList<User> users);

    void gotFollowersStatuses(PagableResponseList<User> users);

    /*List Methods*/
    /**
     * @since Twitter4J 2.1.0
     */
    void createdUserList(UserList userList);
    /**
     * @since Twitter4J 2.1.0
     */
    void updatedUserList(UserList userList);
    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserLists(PagableResponseList<UserList> userLists);
    /**
     * @since Twitter4J 2.1.0
     */
    void gotShowUserList(UserList userList);
    /**
     * @since Twitter4J 2.1.0
     */
    void deletedUserList(UserList userList);
    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListStatuses(PagableResponseList<UserList> userLists);
    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListMemberships(PagableResponseList<UserList> userLists);
    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListSubscriptions(PagableResponseList<UserList> userLists);

    /*List Members Methods*/
    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListMembers(PagableResponseList<User> users);
    /**
     * @since Twitter4J 2.1.0
     */
    void addedUserListMember(UserList userList);
    /**
     * @since Twitter4J 2.1.0
     */
    void deletedUserListMember(UserList userList);
    /**
     * @since Twitter4J 2.1.0
     */
    void checkedUserListMembership(PagableResponseList<User> users);

    /*List Subscribers Methods*/
    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListSubscribers(PagableResponseList<User> users);
    /**
     * @since Twitter4J 2.1.0
     */
    void subscribedUserList(UserList userList);
    /**
     * @since Twitter4J 2.1.0
     */
    void unsubscribedUserList(UserList userList);
    /**
     * @since Twitter4J 2.1.0
     */
    void checkedUserListSubscription(User user);

    /*Direct Message Methods*/
    void gotDirectMessages(ResponseList<DirectMessage> messages);

    void gotSentDirectMessages(ResponseList<DirectMessage> messages);

    void sentDirectMessage(DirectMessage message);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedDirectMessage(DirectMessage message);

    /*Friendship Methods*/
    /**
     * @since Twitter4J 2.0.1
     */
    void createdFriendship(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedFriendship(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void gotExistsFriendship(boolean exists);
    /**
     * @since Twitter4J 2.1.0
     */
    void gotShowFriendship(Relationship relationship);

    /*Social Graph Methods*/
    void gotFriendsIDs(IDs ids);

    void gotFollowersIDs(IDs ids);

    /*Account Methods*/
    void gotRateLimitStatus(RateLimitStatus rateLimitStatus);

    void updatedDeliveryDevice(User user);

    void updatedProfileColors(User user);

    /**
     * @since Twitter4J 2.1.0
     */
    void updatedProfileImage(User user);

    /**
     * @since Twitter4J 2.1.0
     */
    void updatedProfileBackgroundImage(User user);

    /**
     * @since Twitter4J 2.0.2
     */
    void updatedProfile(User user);

    /*Favorite Methods*/
    void gotFavorites(ResponseList<Status> statuses);

    void createdFavorite(Status status);

    void destroyedFavorite(Status status);

    /*Notification Methods*/
    /**
     * @since Twitter4J 2.0.1
     */
    void enabledNotification(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void disabledNotification(User user);

    /*Block Methods*/
    /**
     * @since Twitter4J 2.0.1
     */
    void createdBlock(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedBlock(User user);

    /**
     * @since Twitter4J 2.0.4
     */
    void gotExistsBlock(boolean blockExists);
    
    /**
     * @since Twitter4J 2.0.4
     */
    void gotBlockingUsers(ResponseList<User> blockingUsers);

    /**
     * @since Twitter4J 2.0.4
     */
    void gotBlockingUsersIDs(IDs blockingUsersIDs);

    /*Spam Reporting Methods*/
    void reportedSpam(User reportedSpammer) throws TwitterException;

    /*Saved Searches Methods*/
    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /*Local Trends Methods*/

    /*Help Methods*/
    void tested(boolean test);

    /**
     * @param te     TwitterException
     * @param method
     */
    void onException(TwitterException te, TwitterMethod method);

}
