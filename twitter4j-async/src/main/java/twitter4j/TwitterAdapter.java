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

import twitter4j.api.HelpResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.Map;

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
    @Override
    public void gotCurrentTrends(Trends trends) {
    }
    /*Timeline Methods*/
    @Override
    public void gotHomeTimeline(ResponseList<Status> statuses) {
    }
    @Override
    public void gotUserTimeline(ResponseList<Status> statuses) {
    }
    @Override
    public void gotMentions(ResponseList<Status> statuses) {
    }

    /*Status Methods*/
    @Override
    public void gotShowStatus(Status statuses) {
    }
    @Override
    public void updatedStatus(Status statuses) {
    }

    @Override
    public void destroyedStatus(Status destroyedStatus) {
    }

    @Override
    public void retweetedStatus(Status retweetedStatus) {
    }

    @Override
    public void gotRetweets(ResponseList<Status> retweets) {
    }


    /*User Methods*/
    @Override
    public void gotUserDetail(User user) {
    }

    @Override
    public void lookedupUsers(ResponseList<User> users) {
    }

    @Override
    public void searchedUser(ResponseList<User> userList) {
    }

    @Override
    public void gotSuggestedUserCategories(ResponseList<Category> categories) {
    }

    @Override
    public void gotUserSuggestions(ResponseList<User> users) {
    }

    @Override
    public void gotMemberSuggestions(ResponseList<User> users) {
    }

    @Override
    public void gotContributors(ResponseList<User> users) {
    }

    @Override
    public void gotContributees(ResponseList<User> users) {
    }

    /*List Methods*/

    @Override
    public void createdUserList(UserList userList) {
    }

    @Override
    public void updatedUserList(UserList userList) {
    }

    @Override
    public void gotUserLists(PagableResponseList<UserList> userLists) {
    }

    @Override
    public void gotShowUserList(UserList userList) {
    }

    @Override
    public void destroyedUserList(UserList userList) {
    }

    @Override
    public void gotUserListStatuses(ResponseList<Status> statuses) {
    }

    @Override
    public void gotUserListMemberships(PagableResponseList<UserList> userLists) {
    }

    @Override
    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists) {
    }

    /*List Members Methods*/

    @Override
    public void gotUserListMembers(PagableResponseList<User> users) {
    }

    @Override
    public void addedUserListMember(UserList userList) {
    }

    @Override
    public void addedUserListMembers(UserList userList) {
    }

    @Override
    public void deletedUserListMember(UserList userList) {
    }

    @Override
    public void checkedUserListMembership(User user) {
    }

    /*List Subscribers Methods*/

    @Override
    public void gotUserListSubscribers(PagableResponseList<User> users) {
    }

    @Override
    public void subscribedUserList(UserList userList) {
    }

    @Override
    public void unsubscribedUserList(UserList userList) {
    }

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

    @Override
    public void destroyedDirectMessage(DirectMessage message) {
    }

    @Override
    public void gotDirectMessage(DirectMessage message) {
    }
    /*Friendship Methods*/

    @Override
    public void createdFriendship(User user) {
    }

    @Override
    public void destroyedFriendship(User user) {
    }

    @Override
    public void gotShowFriendship(Relationship relationship) {
    }

    @Override
    public void gotIncomingFriendships(IDs ids) {
    }

    @Override
    public void gotOutgoingFriendships(IDs ids) {
    }

    @Override
    public void lookedUpFriendships(ResponseList<Friendship> friendships) {
    }

    @Override
    public void updatedFriendship(Relationship relationship) {
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
    public void gotRateLimitStatus(Map<String, RateLimitStatus> rateLimitStatus) {

    }

    @Override
    public void updatedProfileColors(User user) {
    }

    @Override
    public void gotAccountSettings(AccountSettings settings) {
    }

    @Override
    public void updatedAccountSettings(AccountSettings settings) {
    }

    @Override
    public void updatedProfileImage(User user) {
    }

    @Override
    public void updatedProfileBackgroundImage(User user) {
    }

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

    /*Block Methods*/

    @Override
    public void createdBlock(User user) {
    }

    @Override
    public void destroyedBlock(User user) {
    }

    @Override
    public void gotBlocksList(ResponseList<User> blockingUsers) {
    }

    @Override
    public void gotBlockIDs(IDs blockingUsersIDs) {
    }

    /*Spam Reporting Methods*/

    @Override
    public void reportedSpam(User reportedSpammer) {
    }


    /*Saved Searches Methods*/
    public void gotSavedSearches(ResponseList<SavedSearch> savedSearches){}
    public void gotSavedSearch(SavedSearch savedSearch){}
    public void createdSavedSearch(SavedSearch savedSearch){}
    public void destroyedSavedSearch(SavedSearch savedSearch){}

    /*Local Trends Methods*/

    @Override
    public void gotAvailableTrends(ResponseList<Location> locations) {
    }

    @Override
    public void gotLocationTrends(Trends trends) {
    }

    /*Geo Methods*/

    @Override
    public void searchedPlaces(ResponseList<Place> places) {
    }

    @Override
    public void gotSimilarPlaces(SimilarPlaces places) {
    }

    @Override
    public void gotReverseGeoCode(ResponseList<Place> places) {
    }

    @Override
    public void gotGeoDetails(Place place) {
    }

    @Override
    public void createdPlace(Place place) {
    }

    /* Legal Resources*/

    @Override
    public void gotTermsOfService(String tof) {

    }

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
    public void gotAPIConfiguration(TwitterAPIConfiguration conf) {
    }

    @Override
    public void gotLanguages(ResponseList<HelpResources.Language> languages) {
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
