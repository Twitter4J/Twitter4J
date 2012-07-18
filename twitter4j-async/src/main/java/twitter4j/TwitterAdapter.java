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
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

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
    @Override
    public void searched(QueryResult result) {
    }

    /**
     * @since Twitter4J 2.0.2
     */
    @Override
    public void gotCurrentTrends(Trends trends) {
    }

    /**
     * @since Twitter4J 2.0.2
     */
    @Override
    public void gotDailyTrends(ResponseList<Trends> trendsList) {
    }

    /**
     * @since Twitter4J 2.0.2
     */
    @Override
    public void gotWeeklyTrends(ResponseList<Trends> trendsList) {
    }

    /*Timeline Methods*/

    /**
     * @since Twitter4J 2.0.10
     */
    @Override
    public void gotHomeTimeline(ResponseList<Status> statuses) {
    }

    @Override
    public void gotUserTimeline(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void gotMentions(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.0.10
     */
    @Override
    public void gotRetweetedByMe(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.0.10
     */
    @Override
    public void gotRetweetedToMe(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.0.10
     */
    @Override
    public void gotRetweetsOfMe(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void gotRetweetedByUser(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void gotRetweetedToUser(ResponseList<Status> statuses) {
    }

    /*Status Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void gotShowStatus(Status statuses) {
    }

    @Override
    public void updatedStatus(Status statuses) {
    }

    @Override
    public void destroyedStatus(Status destroyedStatus) {
    }

    /**
     * @since Twitter4J 2.0.10
     */
    @Override
    public void retweetedStatus(Status retweetedStatus) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotRetweets(ResponseList<Status> retweets) {
    }

    /**
     * @since Twitter4J 2.1.3
     */
    @Override
    public void gotRetweetedBy(ResponseList<User> users) {

    }

    /**
     * @since Twitter4J 2.1.3
     */
    @Override
    public void gotRetweetedByIDs(IDs ids) {

    }

    /*User Methods*/
    @Override
    public void gotUserDetail(User user) {
    }

    /**
     * @since Twitter4J 2.1.1
     */
    @Override
    public void lookedupUsers(ResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void searchedUser(ResponseList<User> userList) {
    }

    /**
     * @since Twitter4J 2.1.1
     */
    @Override
    public void gotSuggestedUserCategories(ResponseList<Category> categories) {
    }

    /**
     * @since Twitter4J 2.1.1
     */
    @Override
    public void gotUserSuggestions(ResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void gotMemberSuggestions(ResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void gotProfileImage(ProfileImage image) {
    }

    @Override
    public void gotContributors(ResponseList<User> users) {
    }

    @Override
    public void gotContributees(ResponseList<User> users) {
    }

    /*List Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void createdUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void updatedUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotUserLists(PagableResponseList<UserList> userLists) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotShowUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void destroyedUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotUserListStatuses(ResponseList<Status> statuses) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotUserListMemberships(PagableResponseList<UserList> userLists) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void gotAllUserLists(ResponseList<UserList> lists) {
    }

    /*List Members Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotUserListMembers(PagableResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void addedUserListMember(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void addedUserListMembers(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void deletedUserListMember(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void checkedUserListMembership(User user) {
    }

    /*List Subscribers Methods*/

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotUserListSubscribers(PagableResponseList<User> users) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void subscribedUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void unsubscribedUserList(UserList userList) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void checkedUserListSubscription(User user) {
    }

    /*Direct Message Methods*/
    @Override
    public void gotDirectMessages(ResponseList<DirectMessage> messages) {
    }

    @Override
    public void gotSentDirectMessages(ResponseList<DirectMessage> messages) {
    }

    @Override
    public void sentDirectMessage(DirectMessage message) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void destroyedDirectMessage(DirectMessage message) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void gotDirectMessage(DirectMessage message) {
    }
    /*Friendship Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void createdFriendship(User user) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void destroyedFriendship(User user) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void gotExistsFriendship(boolean exists) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotShowFriendship(Relationship relationship) {
    }

    /**
     * @since Twitter4J 2.1.2
     */
    @Override
    public void gotIncomingFriendships(IDs ids) {
    }

    /**
     * @since Twitter4J 2.1.2
     */
    @Override
    public void gotOutgoingFriendships(IDs ids) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void lookedUpFriendships(ResponseList<Friendship> friendships) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void updatedFriendship(Relationship relationship) {
    }

    /**
     * @since
     */
    @Override
    public void gotNoRetweetIds(IDs ids) {
    }

    /*Social Graph Methods*/
    @Override
    public void gotFriendsIDs(IDs ids) {
    }

    @Override
    public void gotFollowersIDs(IDs ids) {
    }

    /*Account Methods*/

    @Override
    public void verifiedCredentials(User user) {
    }

    @Override
    public void gotRateLimitStatus(RateLimitStatus status) {
    }

    @Override
    public void updatedProfileColors(User user) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void gotAccountTotals(AccountTotals totals) {
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void gotAccountSettings(AccountSettings settings) {
    }

    /**
     * @param settings account settings
     * @since Twitter4J 2.2.4
     */
    @Override
    public void updatedAccountSettings(AccountSettings settings) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void updatedProfileImage(User user) {
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void updatedProfileBackgroundImage(User user) {
    }

    /**
     * @since Twitter4J 2.0.2
     */
    @Override
    public void updatedProfile(User user) {
    }

    /*Favorite Methods*/
    @Override
    public void gotFavorites(ResponseList<Status> statuses) {
    }

    @Override
    public void createdFavorite(Status status) {
    }

    @Override
    public void destroyedFavorite(Status status) {
    }

    /*Notification Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void enabledNotification(User user) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void disabledNotification(User user) {
    }
    /*Block Methods*/

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void createdBlock(User user) {
    }

    /**
     * @since Twitter4J 2.0.1
     */
    @Override
    public void destroyedBlock(User user) {
    }

    /**
     * @since Twitter4J 2.0.4
     */
    @Override
    public void gotExistsBlock(boolean blockExists) {
    }

    /**
     * @since Twitter4J 2.0.4
     */
    @Override
    public void gotBlockingUsers(ResponseList<User> blockingUsers) {
    }

    /**
     * @since Twitter4J 2.0.4
     */
    @Override
    public void gotBlockingUsersIDs(IDs blockingUsersIDs) {
    }

    /*Spam Reporting Methods*/

    @Override
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
    @Override
    public void gotAvailableTrends(ResponseList<Location> locations) {
    }

    /**
     * @param trends trends
     * @since Twitter4J 2.1.1
     */
    @Override
    public void gotLocationTrends(Trends trends) {
    }

    /*Geo Methods*/

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void searchedPlaces(ResponseList<Place> places) {
    }

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void gotSimilarPlaces(SimilarPlaces places) {
    }

    @Override
    public void gotReverseGeoCode(ResponseList<Place> places) {
    }

    @Override
    public void gotGeoDetails(Place place) {
    }

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void createdPlace(Place place) {
    }

    /* Legal Resources*/

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void gotTermsOfService(String tof) {

    }

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void gotPrivacyPolicy(String privacyPolicy) {

    }

    /* #newtwitter Methods */

    /**
     *
     */
    @Override
    public void gotRelatedResults(RelatedResults relatedResults) {
    }

    /*Help Methods*/

    @Override
    public void tested(boolean test) {
    }


    @Override
    public void gotAPIConfiguration(TwitterAPIConfiguration conf) {
    }

    @Override
    public void gotLanguages(ResponseList<HelpMethods.Language> languages) {
    }

    /* OAuth Methods*/

    @Override
    public void gotOAuthRequestToken(RequestToken token) {
    }

    @Override
    public void gotOAuthAccessToken(AccessToken token) {
    }

    /**
     * @param ex     TwitterException
     * @param method
     */
    @Override
    public void onException(TwitterException ex, TwitterMethod method) {
    }
}
