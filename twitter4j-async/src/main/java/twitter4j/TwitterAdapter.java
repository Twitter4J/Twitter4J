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
    /* Timelines Resources */
    public void gotMentions(ResponseList<Status> statuses){}
    public void gotHomeTimeline(ResponseList<Status> statuses){}
    public void gotUserTimeline(ResponseList<Status> statuses){}

    /* Tweets Resources */
    public void gotRetweets(ResponseList<Status> retweets){}
    public void gotShowStatus(Status status){}
    public void destroyedStatus(Status destroyedStatus){}
    public void updatedStatus(Status status){}
    public void retweetedStatus(Status retweetedStatus){}

    /* Search Resources */
    public void searched(QueryResult queryResult){}

    /* Direct Messages Resources */
    public void gotDirectMessages(ResponseList<DirectMessage> messages){}
    public void gotSentDirectMessages(ResponseList<DirectMessage> messages){}
    public void gotDirectMessage(DirectMessage message){}
    public void destroyedDirectMessage(DirectMessage message){}
    public void sentDirectMessage(DirectMessage message){}

    /* Friends & Followers Resources */
    public void gotFriendsIDs(IDs ids){}
    public void gotFollowersIDs(IDs ids){}
    public void lookedUpFriendships(ResponseList<Friendship> friendships){}
    public void gotIncomingFriendships(IDs ids){}
    public void gotOutgoingFriendships(IDs ids){}
    public void createdFriendship(User user){}
    public void destroyedFriendship(User user){}
    public void updatedFriendship(Relationship relationship){}
    public void gotShowFriendship(Relationship relationship){}

    /* Users Resources */
    public void gotAccountSettings(AccountSettings settings){}
    public void verifiedCredentials(User user){}
    public void updatedAccountSettings(AccountSettings settings){}
    // updatedDeliveryDevice
    public void updatedProfile(User user){}
    public void updatedProfileBackgroundImage(User user){}
    public void updatedProfileColors(User user){}
    public void updatedProfileImage(User user){}
    public void gotBlocksList(ResponseList<User> blockingUsers){}
    public void gotBlockIDs(IDs blockingUsersIDs){}
    public void createdBlock(User user){}
    public void destroyedBlock(User user){}
    public void lookedupUsers(ResponseList<User> users){}
    public void gotUserDetail(User user){}
    public void searchedUser(ResponseList<User> userList){}
    public void gotContributees(ResponseList<User> users){}
    public void gotContributors(ResponseList<User> users){}

    /* Suggested Users Resources */
    public void gotUserSuggestions(ResponseList<User> users){}
    public void gotSuggestedUserCategories(ResponseList<Category> category){}
    public void gotMemberSuggestions(ResponseList<User> users){}

    /* Favorites Resources */
    public void gotFavorites(ResponseList<Status> statuses){}
    public void createdFavorite(Status status){}
    public void destroyedFavorite(Status status){}

    /* Lists Resources */
    public void gotUserLists(PagableResponseList<UserList> userLists){}
    public void gotUserListStatuses(ResponseList<Status> statuses){}
    public void deletedUserListMember(UserList userList){}
    public void gotUserListMemberships(PagableResponseList<UserList> userLists){}
    public void gotUserListSubscribers(PagableResponseList<User> users){}
    public void subscribedUserList(UserList userList){}
    public void checkedUserListSubscription(User user){}
    public void unsubscribedUserList(UserList userList){}
    public void addedUserListMembers(UserList userList){}
    public void checkedUserListMembership(User users){}
    public void addedUserListMember(UserList userList){}
    public void destroyedUserList(UserList userList){}
    public void updatedUserList(UserList userList){}
    public void createdUserList(UserList userList){}
    public void gotShowUserList(UserList userList){}
    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists){}
    public void gotUserListMembers(PagableResponseList<User> users){}

    /* Saved Searches Resources */
    public void gotSavedSearches(ResponseList<SavedSearch> savedSearches){}
    public void gotSavedSearch(SavedSearch savedSearch){}
    public void createdSavedSearch(SavedSearch savedSearch){}
    public void destroyedSavedSearch(SavedSearch savedSearch){}

    /* Places & Geo Resources */
    public void gotGeoDetails(Place place){}
    public void gotReverseGeoCode(ResponseList<Place> places){}
    public void searchedPlaces(ResponseList<Place> places){}
    public void gotSimilarPlaces(SimilarPlaces places){}
    public void createdPlace(Place place){}

    /* Trends Resources */
    public void gotAvailableTrends(ResponseList<Location> locations){}

    /* Spam Reporting Resources */
    public void reportedSpam(User reportedSpammer){}

    /* OAuth Resources */
    public void gotOAuthRequestToken(RequestToken token){}
    public void gotOAuthAccessToken(AccessToken token){}

    /* Help Resources */
    public void gotAPIConfiguration(TwitterAPIConfiguration conf){}
    public void gotLanguages(ResponseList<HelpResources.Language> languages){}
    public void gotPrivacyPolicy(String privacyPolicy){}
    public void gotTermsOfService(String tof){}
    public void gotRateLimitStatus(Map<String, RateLimitStatus> rateLimitStatus){}
    public void onException(TwitterException te, TwitterMethod method){}

    /* Undocumented Resources */
    public void gotRelatedResults(RelatedResults relatedResults){}
}
