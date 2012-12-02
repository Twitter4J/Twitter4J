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
 * A listener for receiving asynchronous responses from Twitter Async APIs.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see {@link twitter4j.AsyncTwitter}
 * @see {@link twitter4j.TwitterAdapter}
 */
public interface TwitterListener {
    /* Timelines Resources */
    void gotMentions(ResponseList<Status> statuses);
    void gotHomeTimeline(ResponseList<Status> statuses);
    void gotUserTimeline(ResponseList<Status> statuses);
    void gotRetweetsOfMe(ResponseList<Status> statuses);

    /* Tweets Resources */
    void gotRetweets(ResponseList<Status> retweets);
    void gotShowStatus(Status status);
    void destroyedStatus(Status destroyedStatus);
    void updatedStatus(Status status);
    void retweetedStatus(Status retweetedStatus);
    void gotOEmbed(OEmbed oembed);

    /* Search Resources */
    void searched(QueryResult queryResult);

    /* Direct Messages Resources */
    void gotDirectMessages(ResponseList<DirectMessage> messages);
    void gotSentDirectMessages(ResponseList<DirectMessage> messages);
    void gotDirectMessage(DirectMessage message);
    void destroyedDirectMessage(DirectMessage message);
    void sentDirectMessage(DirectMessage message);

    /* Friends & Followers Resources */
    void gotFriendsIDs(IDs ids);
    void gotFollowersIDs(IDs ids);
    void lookedUpFriendships(ResponseList<Friendship> friendships);
    void gotIncomingFriendships(IDs ids);
    void gotOutgoingFriendships(IDs ids);
    void createdFriendship(User user);
    void destroyedFriendship(User user);
    void updatedFriendship(Relationship relationship);
    void gotShowFriendship(Relationship relationship);
    void gotFriendsList(PagableResponseList<User> users);
    void gotFollowersList(PagableResponseList<User> users);

    /* Users Resources */
    void gotAccountSettings(AccountSettings settings);
    void verifiedCredentials(User user);
    void updatedAccountSettings(AccountSettings settings);
    // updatedDeliveryDevice
    void updatedProfile(User user);
    void updatedProfileBackgroundImage(User user);
    void updatedProfileColors(User user);
    void updatedProfileImage(User user);
    void gotBlocksList(ResponseList<User> blockingUsers);
    void gotBlockIDs(IDs blockingUsersIDs);
    void createdBlock(User user);
    void destroyedBlock(User user);
    void lookedupUsers(ResponseList<User> users);
    void gotUserDetail(User user);
    void searchedUser(ResponseList<User> userList);
    void gotContributees(ResponseList<User> users);
    void gotContributors(ResponseList<User> users);
    void removedProfileBanner();
    void updatedProfileBanner();

    /* Suggested Users Resources */
    void gotUserSuggestions(ResponseList<User> users);
    void gotSuggestedUserCategories(ResponseList<Category> category);
    void gotMemberSuggestions(ResponseList<User> users);

    /* Favorites Resources */
    void gotFavorites(ResponseList<Status> statuses);
    void createdFavorite(Status status);
    void destroyedFavorite(Status status);

    /* Lists Resources */
    void gotUserLists(ResponseList<UserList> userLists);
    void gotUserListStatuses(ResponseList<Status> statuses);
    void destroyedUserListMember(UserList userList);
    void gotUserListMemberships(PagableResponseList<UserList> userLists);
    void gotUserListSubscribers(PagableResponseList<User> users);
    void subscribedUserList(UserList userList);
    void checkedUserListSubscription(User user);
    void unsubscribedUserList(UserList userList);
    void createdUserListMembers(UserList userList);
    void checkedUserListMembership(User users);
    void createdUserListMember(UserList userList);
    void destroyedUserList(UserList userList);
    void updatedUserList(UserList userList);
    void createdUserList(UserList userList);
    void gotShowUserList(UserList userList);
    void gotUserListSubscriptions(PagableResponseList<UserList> userLists);
    void gotUserListMembers(PagableResponseList<User> users);

    /* Saved Searches Resources */
    void gotSavedSearches(ResponseList<SavedSearch> savedSearches);
    void gotSavedSearch(SavedSearch savedSearch);
    void createdSavedSearch(SavedSearch savedSearch);
    void destroyedSavedSearch(SavedSearch savedSearch);

    /* Places & Geo Resources */
    void gotGeoDetails(Place place);
    void gotReverseGeoCode(ResponseList<Place> places);
    void searchedPlaces(ResponseList<Place> places);
    void gotSimilarPlaces(SimilarPlaces places);
    void createdPlace(Place place);

    /* Trends Resources */
    void gotPlaceTrends(Trends trends);
    void gotAvailableTrends(ResponseList<Location> locations);
    void gotClosestTrends(ResponseList<Location> locations);

    /* Spam Reporting Resources */
    void reportedSpam(User reportedSpammer);

    /* OAuth Resources */
    void gotOAuthRequestToken(RequestToken token);
    void gotOAuthAccessToken(AccessToken token);

    /* Help Resources */
    void gotAPIConfiguration(TwitterAPIConfiguration conf);
    void gotLanguages(ResponseList<HelpResources.Language> languages);
    void gotPrivacyPolicy(String privacyPolicy);
    void gotTermsOfService(String tof);
    void gotRateLimitStatus(Map<String, RateLimitStatus> rateLimitStatus);
    void onException(TwitterException te, TwitterMethod method);

    /* Undocumented Resources */
    void gotRelatedResults(RelatedResults relatedResults);
}
