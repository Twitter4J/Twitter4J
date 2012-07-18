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
 * A listner for receiving asynchronous responses from Twitter Async APIs.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterAdapter
 */
public interface TwitterListener {
    /*Search API Methods*/
    void searched(QueryResult queryResult);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotCurrentTrends(Trends trends);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotDailyTrends(ResponseList<Trends> trendsList);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotWeeklyTrends(ResponseList<Trends> trendsList);

    /**
     * @since Twitter4J 2.0.10
     */
    void gotHomeTimeline(ResponseList<Status> statuses);

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

    /**
     * @since Twitter4J 2.1.9
     */
    void gotRetweetedByUser(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.1.9
     */
    void gotRetweetedToUser(ResponseList<Status> statuses);

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

    /**
     * @since Twitter4J 2.1.3
     */
    void gotRetweetedBy(ResponseList<User> users);

    /**
     * @since Twitter4J 2.1.3
     */
    void gotRetweetedByIDs(IDs ids);

    /*User Methods*/
    void gotUserDetail(User user);

    /**
     * @since Twitter4J 2.1.1
     */
    void lookedupUsers(ResponseList<User> users);

    /**
     * @since Twitter4J 2.1.0
     */
    void searchedUser(ResponseList<User> userList);

    /**
     * @since Twitter4J 2.1.1
     */
    void gotSuggestedUserCategories(ResponseList<Category> category);

    /**
     * @since Twitter4J 2.1.1
     */
    void gotUserSuggestions(ResponseList<User> users);

    /**
     * @since Twitter4J 2.1.9
     */
    void gotMemberSuggestions(ResponseList<User> users);

    /**
     * @since Twitter4J 2.1.7
     */
    void gotProfileImage(ProfileImage image);

    /**
     * @since Twitter4J 3.0.0
     */
    void gotContributors(ResponseList<User> users);

    /**
     * @since Twitter4J 3.0.0
     */
    void gotContributees(ResponseList<User> users);

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
    void destroyedUserList(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListStatuses(ResponseList<Status> statuses);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListMemberships(PagableResponseList<UserList> userLists);

    /**
     * @since Twitter4J 2.1.0
     */
    void gotUserListSubscriptions(PagableResponseList<UserList> userLists);

    /**
     * @since Twitter4J 2.1.9
     */
    void gotAllUserLists(ResponseList<UserList> lists);

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
     * @since Twitter4J 2.1.7
     */
    void addedUserListMembers(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void deletedUserListMember(UserList userList);

    /**
     * @since Twitter4J 2.1.0
     */
    void checkedUserListMembership(User users);

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
     * @since Twitter4J 2.1.9
     */
    void gotDirectMessage(DirectMessage message);

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

    /**
     * @since Twitter4J 2.1.2
     */
    void gotIncomingFriendships(IDs ids);

    /**
     * @since Twitter4J 2.1.2
     */
    void gotOutgoingFriendships(IDs ids);

    /*Social Graph Methods*/
    void gotFriendsIDs(IDs ids);

    void gotFollowersIDs(IDs ids);

    /**
     * @since Twitter4J 2.1.9
     */
    void lookedUpFriendships(ResponseList<Friendship> friendships);

    /**
     * @since Twitter4J 2.1.9
     */
    void updatedFriendship(Relationship relationship);

    /**
     * @since
     */
    void gotNoRetweetIds(IDs ids);

    /*Account Methods*/

    void verifiedCredentials(User user);

    void gotRateLimitStatus(RateLimitStatus rateLimitStatus);

    void updatedProfileColors(User user);

    /**
     * @param totals account totals
     * @since Twitter4J 2.1.9
     */
    void gotAccountTotals(AccountTotals totals);

    /**
     * @param settings account settings
     * @since Twitter4J 2.1.9
     */
    void gotAccountSettings(AccountSettings settings);

    /**
     * @param settings account settings
     * @since Twitter4J 2.2.4
     */
    void updatedAccountSettings(AccountSettings settings);

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
    void reportedSpam(User reportedSpammer);

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
    void gotAvailableTrends(ResponseList<Location> locations);

    /**
     * @param trends trends
     * @since Twitter4J 2.1.1
     */
    void gotLocationTrends(Trends trends);

    /*Geo Methods*/
    void searchedPlaces(ResponseList<Place> places);

    void gotSimilarPlaces(SimilarPlaces places);

    void gotReverseGeoCode(ResponseList<Place> places);

    void gotGeoDetails(Place place);

    void createdPlace(Place place);

    /* Legal Resources*/

    /**
     * @since Twitter4J 2.1.7
     */
    void gotTermsOfService(String tof);

    /**
     * @since Twitter4J 2.1.7
     */
    void gotPrivacyPolicy(String privacyPolicy);

    /* #newtwitter Methods */

    void gotRelatedResults(RelatedResults relatedResults);

    /*Help Methods*/
    void tested(boolean test);

    void gotAPIConfiguration(TwitterAPIConfiguration conf);

    void gotLanguages(ResponseList<HelpMethods.Language> languages);

    /**
     * @param te     TwitterException
     * @param method
     */
    void onException(TwitterException te, TwitterMethod method);

    /*OAuth Methods*/
    void gotOAuthRequestToken(RequestToken token);

    void gotOAuthAccessToken(AccessToken token);
}
