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
 * A handy adapter of TwitterListener.
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterListener
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterAdapter implements TwitterListener {
    public TwitterAdapter() {
    }
    /*Search API Methods*/
    public void searched(QueryResult result){
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void gotTrends(Trends trends) {
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void gotCurrentTrends(Trends trends) {
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void gotDailyTrends(List<Trends> trendsList) {
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void gotWeeklyTrends(List<Trends> trendsList) {
    }

    /*Timeline Methods*/
    public void gotPublicTimeline(ResponseList<Status> statuses){
    }

    /**
     * @since Twitter4J 2.0.10
     */
    public void gotHomeTimeline(ResponseList<Status> statuses) {
    }

    public void gotFriendsTimeline(ResponseList<Status> statuses){
    }
    public void gotUserTimeline(ResponseList<Status> statuses){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void gotMentions(ResponseList<Status> statuses){
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void gotRetweetedByMe(ResponseList<Status> statuses) {
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void gotRetweetedToMe(ResponseList<Status> statuses) {
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void gotRetweetsOfMe(ResponseList<Status> statuses) {
    }

    /*Status Methods*/
    /**
     * @since Twitter4J 2.0.1
     */
    public void gotShowStatus(Status statuses){
    }
    public void updatedStatus(Status statuses){
    }
    public void destroyedStatus(Status destroyedStatus){
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void retweetedStatus(Status retweetedStatus){
    }
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotRetweets(ResponseList<Status> retweets){
    }

    /*User Methods*/
    public void gotUserDetail(User user){
    }
    /**
     * @since Twitter4J 2.1.0
     */
    public void searchedUser(ResponseList<User> userList) {
    }

    public void gotFriendsStatuses(PagableResponseList<User> users){
    }
    public void gotFollowersStatuses(PagableResponseList<User> users){
    }
    /*List Methods*/
    /**
     * @since Twitter4J 2.1.0
     */
    public void createdUserList(UserList userList) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void updatedUserList(UserList userList) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserLists(PagableResponseList<UserList> userLists) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotShowUserList(UserList userList) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void deletedUserList(UserList userList) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListStatuses(PagableResponseList<UserList> userLists) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListMemberships(PagableResponseList<UserList> userLists) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists) {}

    /*List Members Methods*/
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListMembers(PagableResponseList<User> users) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void addedUserListMember(UserList userList) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void deletedUserListMember(UserList userList) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void checkedUserListMembership(PagableResponseList<User> users) {}

    /*List Subscribers Methods*/
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListSubscribers(PagableResponseList<User> users) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void subscribedUserList(UserList userList) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void unsubscribedUserList(UserList userList) {}
    /**
     * @since Twitter4J 2.1.0
     */
    public void checkedUserListSubscription(User user) {}

    /*Direct Message Methods*/
    public void gotDirectMessages(ResponseList<DirectMessage> messages){
    }
    public void gotSentDirectMessages(ResponseList<DirectMessage> messages){
    }
    public void sentDirectMessage(DirectMessage message){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedDirectMessage(DirectMessage message){
    }

    /*Friendship Methods*/
    /**
     * @since Twitter4J 2.0.1
     */
    public void createdFriendship(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedFriendship(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void gotExistsFriendship(boolean exists) {
    }
    /**
     * @since Twitter4J 2.1.0
     */
    public void gotShowFriendship(Relationship relationship) {
    }

    /*Social Graph Methods*/
    public void gotFriendsIDs(IDs ids){
    }

    public void gotFollowersIDs(IDs ids){
    }

    /*Account Methods*/

    public void gotRateLimitStatus(RateLimitStatus status) {
    }

    public void updatedDeliveryDevice(User user) {
    }

    public void updatedProfileColors(User user) {
    }
    /**
     * @since Twitter4J 2.1.0
     */
    public void updatedProfileImage(User user) {
    }
    /**
     * @since Twitter4J 2.1.0
     */
    public void updatedProfileBackgroundImage(User user) {
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void updatedProfile(User user){
    }
    /*Favorite Methods*/
    public void gotFavorites(ResponseList<Status> statuses){
    }
    public void createdFavorite(Status status){
    }
    public void destroyedFavorite(Status status){
    }

    /*Notification Methods*/
    /**
     * @since Twitter4J 2.0.1
     */
    public void enabledNotification(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void disabledNotification(User user){
    }
    /*Block Methods*/
    /**
     * @since Twitter4J 2.0.1
     */
    public void createdBlock(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedBlock(User user){
    }
    /**
     * @since Twitter4J 2.0.4
     */
    public void gotExistsBlock(boolean blockExists){
    }

    /**
     * @since Twitter4J 2.0.4
     */
    public void gotBlockingUsers(ResponseList<User> blockingUsers){
    }

    /**
     * @since Twitter4J 2.0.4
     */
    public void gotBlockingUsersIDs(IDs blockingUsersIDs) {
    }

    /*Spam Reporting Methods*/

    public void reportedSpam(User reportedSpammer) throws TwitterException {
    }


    /*Saved Searches Methods*/
    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /*Local Trends Methods*/

    /*Help Methods*/
    public void tested(boolean test){
    }

    /**
     * @param ex TwitterException
     * @param method
     */
    public void onException(TwitterException ex, TwitterMethod method) {
    }
}
