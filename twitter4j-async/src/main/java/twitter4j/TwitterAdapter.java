/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
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

package twitter4j;

import twitter4j.api.HelpMethods;

/**
 * A handy adapter of TwitterListener.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterListener
 */
public class TwitterAdapter implements TwitterListener {
    public TwitterAdapter() {
    }

    /*Search API Methods*/
    public void searched(QueryResult result) {
    }

    /**
     * @since Twitter4J 2.0.2
     */
    public void gotCurrentTrends(Trends trends) {
    }

    /**
     * @since Twitter4J 2.0.2
     */
    public void gotDailyTrends(ResponseList<Trends> trendsList) {
    }

    /**
     * @since Twitter4J 2.0.2
     */
    public void gotWeeklyTrends(ResponseList<Trends> trendsList) {
    }

    /*Timeline Methods*/
    public void gotPublicTimeline(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.0.10
     */
    public void gotHomeTimeline(ResponseList<Status> statuses) {
    }

    public void gotFriendsTimeline(ResponseList<Status> statuses) {
    }

    public void gotUserTimeline(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    public void gotMentions(ResponseList<Status> statuses) {
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

    /**
     * @since Twitter4J 2.1.9
     */
    public void gotRetweetedByUser(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void gotRetweetedToUser(ResponseList<Status> statuses) {
    }

    /*Status Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    public void gotShowStatus(Status statuses) {
    }

    public void updatedStatus(Status statuses) {
    }

    public void destroyedStatus(Status destroyedStatus) {
    }

    /**
     * @since Twitter4J 2.0.10
     */
    public void retweetedStatus(Status retweetedStatus) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotRetweets(ResponseList<Status> retweets) {
    }

    /**
     * @since Twitter4J 2.1.3
     */
    public void gotRetweetedBy(ResponseList<User> users) {

    }

    /**
     * @since Twitter4J 2.1.3
     */
    public void gotRetweetedByIDs(IDs ids) {

    }

    /*User Methods*/
    public void gotUserDetail(User user) {
    }

    /**
     * @since Twitter4J 2.1.1
     */
    public void lookedupUsers(ResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void searchedUser(ResponseList<User> userList) {
    }

    /**
     * @since Twitter4J 2.1.1
     */
    public void gotSuggestedUserCategories(ResponseList<Category> categories) {
    }

    /**
     * @since Twitter4J 2.1.1
     */
    public void gotUserSuggestions(ResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void gotMemberSuggestions(ResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.7
     */
    public void gotProfileImage(ProfileImage image) {
    }

    public void gotFriendsStatuses(PagableResponseList<User> users) {
    }

    public void gotFollowersStatuses(PagableResponseList<User> users) {
    }
    /*List Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    public void createdUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void updatedUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserLists(PagableResponseList<UserList> userLists) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotShowUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void destroyedUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListStatuses(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListMemberships(PagableResponseList<UserList> userLists) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void gotAllUserLists(ResponseList<UserList> lists) {
    }

    /*List Members Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListMembers(PagableResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void addedUserListMember(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.7
     */
    public void addedUserListMembers(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void deletedUserListMember(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void checkedUserListMembership(User user) {
    }

    /*List Subscribers Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotUserListSubscribers(PagableResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void subscribedUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void unsubscribedUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void checkedUserListSubscription(User user) {
    }

    /*Direct Message Methods*/
    public void gotDirectMessages(ResponseList<DirectMessage> messages) {
    }

    public void gotSentDirectMessages(ResponseList<DirectMessage> messages) {
    }

    public void sentDirectMessage(DirectMessage message) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedDirectMessage(DirectMessage message) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void gotDirectMessage(DirectMessage message) {
    }
    /*Friendship Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    public void createdFriendship(User user) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedFriendship(User user) {
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

    /**
     * @since Twitter4J 2.1.2
     */
    public void gotIncomingFriendships(IDs ids) {
    }

    /**
     * @since Twitter4J 2.1.2
     */
    public void gotOutgoingFriendships(IDs ids) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void lookedUpFriendships(ResponseList<Friendship> friendships) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void updatedFriendship(Relationship relationship) {
    }

    /**
     * @since
     */
    public void gotNoRetweetIds(IDs ids) {
    }

    /*Social Graph Methods*/
    public void gotFriendsIDs(IDs ids) {
    }

    public void gotFollowersIDs(IDs ids) {
    }

    /*Account Methods*/

    public void verifiedCredentials(User user) {
    }

    public void gotRateLimitStatus(RateLimitStatus status) {
    }

    public void updatedProfileColors(User user) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void gotAccountTotals(AccountTotals totals) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void gotAccountSettings(AccountSettings settings) {
    }

    /**
     * @param settings account settings
     * @since Twitter4J 2.2.4
     */
    public void updatedAccountSettings(AccountSettings settings) {
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
    public void updatedProfile(User user) {
    }

    /*Favorite Methods*/
    public void gotFavorites(ResponseList<Status> statuses) {
    }

    public void createdFavorite(Status status) {
    }

    public void destroyedFavorite(Status status) {
    }

    /*Notification Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    public void enabledNotification(User user) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    public void disabledNotification(User user) {
    }
    /*Block Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    public void createdBlock(User user) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedBlock(User user) {
    }

    /**
     * @since Twitter4J 2.0.4
     */
    public void gotExistsBlock(boolean blockExists) {
    }

    /**
     * @since Twitter4J 2.0.4
     */
    public void gotBlockingUsers(ResponseList<User> blockingUsers) {
    }

    /**
     * @since Twitter4J 2.0.4
     */
    public void gotBlockingUsersIDs(IDs blockingUsersIDs) {
    }

    /*Spam Reporting Methods*/

    public void reportedSpam(User reportedSpammer) {
    }


    /*Saved Searches Methods*/
    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /*Local Trends Methods*/

    /**
     * @param locations the locations
     * @since Twitter4J 2.1.1
     */
    public void gotAvailableTrends(ResponseList<Location> locations) {
    }

    /**
     * @param trends trends
     * @since Twitter4J 2.1.1
     */
    public void gotLocationTrends(Trends trends) {
    }

    /*Geo Methods*/

    /**
     * @since Twitter4J 2.1.7
     */
    public void searchedPlaces(ResponseList<Place> places) {
    }

    /**
     * @since Twitter4J 2.1.7
     */
    public void gotSimilarPlaces(SimilarPlaces places) {
    }

    public void gotNearByPlaces(ResponseList<Place> places) {
    }

    public void gotReverseGeoCode(ResponseList<Place> places) {
    }

    public void gotGeoDetails(Place place) {
    }

    /**
     * @since Twitter4J 2.1.7
     */
    public void createdPlace(Place place) {
    }

    /* Legal Resources*/

    /**
     * @since Twitter4J 2.1.7
     */
    public void gotTermsOfService(String tof) {

    }

    /**
     * @since Twitter4J 2.1.7
     */
    public void gotPrivacyPolicy(String privacyPolicy) {

    }

    /* #newtwitter Methods */

    /**
     *
     */
    public void gotRelatedResults(RelatedResults relatedResults) {
    }

    /*Help Methods*/
    public void tested(boolean test) {
    }

    public void gotAPIConfiguration(TwitterAPIConfiguration conf) {
    }

    public void gotLanguages(ResponseList<HelpMethods.Language> languages) {
    }

    /**
     * @param ex     TwitterException
     * @param method
     */
    public void onException(TwitterException ex, TwitterMethod method) {
    }
}
