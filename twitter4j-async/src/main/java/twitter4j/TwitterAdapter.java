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
    @Override
    public void gotMentions(ResponseList<Status> statuses){}
    @Override
    public void gotHomeTimeline(ResponseList<Status> statuses){}
    @Override
    public void gotUserTimeline(ResponseList<Status> statuses){}
    @Override
    public void gotRetweetsOfMe(ResponseList<Status> statuses){}

    /* Tweets Resources */
    @Override
    public void gotRetweets(ResponseList<Status> retweets){}
    @Override
    public void gotShowStatus(Status status){}
    @Override
    public void destroyedStatus(Status destroyedStatus){}
    @Override
    public void updatedStatus(Status status){}
    @Override
    public void retweetedStatus(Status retweetedStatus){}
    @Override
    public void gotOEmbed(OEmbed oembed) {}

    /* Search Resources */
    @Override
    public void searched(QueryResult queryResult){}

    /* Direct Messages Resources */
    @Override
    public void gotDirectMessages(ResponseList<DirectMessage> messages){}
    @Override
    public void gotSentDirectMessages(ResponseList<DirectMessage> messages){}
    @Override
    public void gotDirectMessage(DirectMessage message){}
    @Override
    public void destroyedDirectMessage(DirectMessage message){}
    @Override
    public void sentDirectMessage(DirectMessage message){}

    /* Friends & Followers Resources */
    @Override
    public void gotFriendsIDs(IDs ids){}
    @Override
    public void gotFollowersIDs(IDs ids){}
    @Override
    public void lookedUpFriendships(ResponseList<Friendship> friendships){}
    @Override
    public void gotIncomingFriendships(IDs ids){}
    @Override
    public void gotOutgoingFriendships(IDs ids){}
    @Override
    public void createdFriendship(User user){}
    @Override
    public void destroyedFriendship(User user){}
    @Override
    public void updatedFriendship(Relationship relationship){}
    @Override
    public void gotShowFriendship(Relationship relationship){}
    @Override
    public void gotFriendsList(PagableResponseList<User> users) {}
    @Override
    public void gotFollowersList(PagableResponseList<User> users) {}

    /* Users Resources */
    @Override
    public void gotAccountSettings(AccountSettings settings){}
    @Override
    public void verifiedCredentials(User user){}
    @Override
    public void updatedAccountSettings(AccountSettings settings){}
    // updatedDeliveryDevice
    @Override
    public void updatedProfile(User user){}
    @Override
    public void updatedProfileBackgroundImage(User user){}
    @Override
    public void updatedProfileColors(User user){}
    @Override
    public void updatedProfileImage(User user){}
    @Override
    public void gotBlocksList(ResponseList<User> blockingUsers){}
    @Override
    public void gotBlockIDs(IDs blockingUsersIDs){}
    @Override
    public void createdBlock(User user){}
    @Override
    public void destroyedBlock(User user){}
    @Override
    public void lookedupUsers(ResponseList<User> users){}
    @Override
    public void gotUserDetail(User user){}
    @Override
    public void searchedUser(ResponseList<User> userList){}
    @Override
    public void gotContributees(ResponseList<User> users){}
    @Override
    public void gotContributors(ResponseList<User> users){}
    @Override
    public void removedProfileBanner() {}
    @Override
    public void updatedProfileBanner() {}

    /* Suggested Users Resources */
    @Override
    public void gotUserSuggestions(ResponseList<User> users){}
    @Override
    public void gotSuggestedUserCategories(ResponseList<Category> category){}
    @Override
    public void gotMemberSuggestions(ResponseList<User> users){}

    /* Favorites Resources */
    @Override
    public void gotFavorites(ResponseList<Status> statuses){}
    @Override
    public void createdFavorite(Status status){}
    @Override
    public void destroyedFavorite(Status status){}

    /* Lists Resources */
    @Override
    public void gotUserLists(ResponseList<UserList> userLists){}
    @Override
    public void gotUserListStatuses(ResponseList<Status> statuses){}
    @Override
    public void destroyedUserListMember(UserList userList){}
    @Override
    public void gotUserListMemberships(PagableResponseList<UserList> userLists){}
    @Override
    public void gotUserListSubscribers(PagableResponseList<User> users){}
    @Override
    public void subscribedUserList(UserList userList){}
    @Override
    public void checkedUserListSubscription(User user){}
    @Override
    public void unsubscribedUserList(UserList userList){}
    @Override
    public void createdUserListMembers(UserList userList){}
    @Override
    public void checkedUserListMembership(User users){}
    @Override
    public void createdUserListMember(UserList userList){}
    @Override
    public void destroyedUserList(UserList userList){}
    @Override
    public void updatedUserList(UserList userList){}
    @Override
    public void createdUserList(UserList userList){}
    @Override
    public void gotShowUserList(UserList userList){}
    @Override
    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists){}
    @Override
    public void gotUserListMembers(PagableResponseList<User> users){}

    /* Saved Searches Resources */
    @Override
    public void gotSavedSearches(ResponseList<SavedSearch> savedSearches){}
    @Override
    public void gotSavedSearch(SavedSearch savedSearch){}
    @Override
    public void createdSavedSearch(SavedSearch savedSearch){}
    @Override
    public void destroyedSavedSearch(SavedSearch savedSearch){}

    /* Places & Geo Resources */
    @Override
    public void gotGeoDetails(Place place){}
    @Override
    public void gotReverseGeoCode(ResponseList<Place> places){}
    @Override
    public void searchedPlaces(ResponseList<Place> places){}
    @Override
    public void gotSimilarPlaces(SimilarPlaces places){}
    @Override
    public void createdPlace(Place place){}

    /* Trends Resources */
    @Override
    public void gotPlaceTrends(Trends trends) {}
    @Override
    public void gotAvailableTrends(ResponseList<Location> locations){}
    @Override
    public void gotClosestTrends(ResponseList<Location> locations) {}

    /* Spam Reporting Resources */
    @Override
    public void reportedSpam(User reportedSpammer){}

    /* OAuth Resources */
    @Override
    public void gotOAuthRequestToken(RequestToken token){}
    @Override
    public void gotOAuthAccessToken(AccessToken token){}

    /* Help Resources */
    @Override
    public void gotAPIConfiguration(TwitterAPIConfiguration conf){}
    @Override
    public void gotLanguages(ResponseList<HelpResources.Language> languages){}
    @Override
    public void gotPrivacyPolicy(String privacyPolicy){}
    @Override
    public void gotTermsOfService(String tof){}
    @Override
    public void gotRateLimitStatus(Map<String, RateLimitStatus> rateLimitStatus){}
    @Override
    public void onException(TwitterException te, TwitterMethod method){}

    /* Undocumented Resources */
    @Override
    public void gotRelatedResults(RelatedResults relatedResults){}
}
