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

package twitter4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twitter4j.api.HelpResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class AsyncTwitterTest extends TwitterTestBase implements TwitterListener {

    private AsyncTwitter async1 = null;
    private AsyncTwitter async2 = null;
    private AsyncTwitter async3 = null;
    private AsyncTwitter bestFriend1Async = null;
    private ResponseList<Location> locations;
    private ResponseList<Place> places;
    private Place place;
    private ResponseList<Category> categories;
    private AccountTotals totals;
    private AccountSettings settings;
    private ResponseList<Friendship> friendships;
    private ResponseList<UserList> userLists;
    private ResponseList<HelpResources.Language> languages;
    private TwitterAPIConfiguration apiConf;
    private SavedSearch savedSearch;
    private ResponseList<SavedSearch> savedSearches;
    private OEmbed oembed;

    private long twit4jblockID = 39771963L;
    private long id;

    @BeforeEach
    protected void beforeEach() throws Exception {
        super.beforeEach();
        AsyncTwitterFactory factory = new AsyncTwitterFactory(conf1);
        async1 = factory.getInstance();
        async1.addListener(this);

        async2 = new AsyncTwitterFactory(conf2).getInstance();
        async2.addListener(this);

        async3 = new AsyncTwitterFactory(conf3).getInstance();
        async3.addListener(this);

        bestFriend1Async = new AsyncTwitterFactory(bestFriend1Conf).getInstance();
        bestFriend1Async.addListener(this);

        statuses = null;
        users = null;
        messages = null;
        status = null;
        user = null;
        message = null;
        te = null;
    }

    @Test
    void testShowUser() throws Exception {
        async1.showUser(id1.screenName);
        waitForResponse();
        User user = this.user;
        assertEquals(id1.screenName, user.getScreenName());
        assertTrue(0 <= user.getFavouritesCount());
        assertTrue(0 <= user.getFollowersCount());
        assertTrue(0 <= user.getFriendsCount());
        assertTrue(0 <= user.getStatusesCount());
        assertNotNull(user.getProfileBackgroundColor());
        assertNotNull(user.getProfileTextColor());
        assertNotNull(user.getProfileLinkColor());
        assertNotNull(user.getProfileSidebarBorderColor());
        assertNotNull(user.getProfileSidebarFillColor());
        assertNotNull(user.getProfileTextColor());

        this.user = null;
    }

    @Test
    void testSearchUser() throws TwitterException {
        async1.searchUsers("Doug Williams", 1);
        waitForResponse();
        assertTrue(4 < users.size());
    }

    @Test
    void testGetUserTimeline_Show() throws Exception {
        async2.getUserTimeline();
        waitForResponse();
        assertTrue(10 < statuses.size(), "size");
        async2.getUserTimeline(new Paging(999383469l));
    }

    @Test
    void testAccountProfileImageUpdates() throws Exception {
        te = null;
        async1.updateProfileImage(getRandomlyChosenFile());
        waitForResponse();
        assertNull(te);
    }


    @Test
    void testFavorite() throws Exception {
        Status status = twitter1.getHomeTimeline().get(0);
        try {
            twitter2.destroyFavorite(status.getId());
        } catch (TwitterException ignored) {
        }
        async2.createFavorite(status.getId());
        waitForResponse();
        assertEquals(status, this.status);
        this.status = null;
        //need to wait for a second to get it destoryable
        Thread.sleep(5000);
        async2.destroyFavorite(status.getId());
        waitForResponse();
        if (te != null && te.getStatusCode() == 404) {
            // sometimes destroying favorite fails with 404
        } else {
            assertEquals(status, this.status);
        }
    }

    // disable test case for now due to the rate limitation
//
//
// @Test
// void testSocialGraphMethods() throws Exception {
//        async1.getFriendsIDs(-1);
//        waitForResponse();
//        int yusuke = 4933401;
//        assertIDExsits("twit4j is following yusuke", ids, yusuke);
//        int ryunosukey = 48528137;
//        async1.getFriendsIDs(ryunosukey, -1);
//        waitForResponse();
//        assertEquals("ryunosukey is not following anyone", 0, ids.getIDs().length);
//        async1.getFriendsIDs("yusuke", -1);
//        waitForResponse();
//        assertIDExsits("yusukey is following ryunosukey", ids, ryunosukey);
//
//        try {
//            twitter2.createFriendship(id1.screenName);
//        } catch (TwitterException te) {
//        }
//        async1.getFollowersIDs(-1);
//        waitForResponse();
//        assertIDExsits("twit4j2(6377362) is following twit4j(6358482)", ids, 6377362);
//        async1.getFollowersIDs(ryunosukey, -1);
//        waitForResponse();
//        assertIDExsits("yusukey is following ryunosukey", ids, yusuke);
//        async1.getFollowersIDs("ryunosukey", -1);
//        waitForResponse();
//        assertIDExsits("yusukey is following ryunosukey", ids, yusuke);
//    }

    private void assertIDExsits(String assertion, IDs ids, int idToFind) {
        boolean found = false;
        for (long id : ids.getIDs()) {
            if (id == idToFind) {
                found = true;
                break;
            }
        }
        assertTrue(found, assertion);
    }

    @Test
    void testAccountMethods() throws Exception {

        async1.verifyCredentials();
        waitForResponse();
        assertNotNull(user);
        assertNotNull(user.getName());
        assertNotNull(user.getURL());
        assertNotNull(user.getLocation());
        assertNotNull(user.getDescription());

        String oldURL = user.getURL();

        String newName, newURL, newLocation, newDescription;
        newName = "name" + System.currentTimeMillis();
        newURL = "https://yusuke.blog/" + System.currentTimeMillis();
        newLocation = "location:" + System.currentTimeMillis();
        newDescription = "description:" + System.currentTimeMillis();

        async1.updateProfile(newName, newURL, newLocation, newDescription);

        waitForResponse();
        assertEquals(newName, user.getName());
        assertFalse(oldURL.equalsIgnoreCase(user.getURL()));
        assertEquals(newLocation, user.getLocation());
        assertEquals(newDescription, user.getDescription());

    }

    @Test
    void testShow() throws Exception {
        async2.showStatus(1000l);
        waitForResponse();
        assertEquals(52, status.getUser().getId());
        assertDeserializedFormIsEqual(status);
    }

    @Test
    void testBlock() throws Exception {
        async2.createBlock(id1.screenName);
        waitForResponse();
        async2.destroyBlock(id1.screenName);
        waitForResponse();

        async1.getBlocksList();
        waitForResponse();
        assertTrue(users.size() > 0);
        assertEquals(twit4jblockID, users.get(0).getId());
        async1.getBlocksList(-1L);
        waitForResponse();
        assertTrue(users.size() > 0);
        assertEquals(twit4jblockID, users.get(0).getId());
        async1.getBlocksIDs();
        waitForResponse();
        assertTrue(ids.getIDs().length > 0);
        assertEquals(twit4jblockID, ids.getIDs()[0]);
    }

    @Test
    void testMute() throws Exception {
        async2.createMute(id1.screenName);
        waitForResponse();
        async2.destroyMute(id1.screenName);
        waitForResponse();

        async1.getMutesList(-1L);
        waitForResponse();
        assertEquals(1, users.size());
        assertEquals(twit4jblockID, users.get(0).getId());
        async1.getMutesIDs(-1L);
        waitForResponse();
        assertEquals(1, ids.getIDs().length);
        assertEquals(twit4jblockID, ids.getIDs()[0]);
    }

    @Test
    void testUpdate() throws Exception {
        String date = new java.util.Date().toString() + "test";
        async1.updateStatus(date);
        waitForResponse();
        assertEquals(date, status.getText());

        long id = status.getId();

        async2.updateStatus(new StatusUpdate("@" + id1.screenName + " " + date).inReplyToStatusId(id));
        waitForResponse();
        assertEquals("@" + id1.screenName + " " + date, status.getText());
        assertEquals(id, status.getInReplyToStatusId());
        assertEquals(twitter1.verifyCredentials().getId(), status.getInReplyToUserId());


        id = status.getId();
        this.status = null;
        async2.destroyStatus(id);
        waitForResponse();
        assertEquals("@" + id1.screenName + " " + date, status.getText());
        assertDeserializedFormIsEqual(status);
    }

    @Test
    void testCreateDestroyFriend() throws Exception {
        async2.destroyFriendship(id1.screenName);
        waitForResponse();

//        twitterAPI2.destroyFriendshipAsync(id1.name);
//        waitForResponse();
//        assertEquals(403, te.getStatusCode());
        async2.createFriendship(id1.screenName, true);
        // the Twitter API is not returning appropriate notifications value
        // http://code.google.com/p/twitter-api/issues/detail?id=474
//        user detail = twitterAPI2.showUser(id1.name);
//        assertTrue(detail.isNotificationEnabled());
        waitForResponse();
        assertEquals(id1.screenName, user.getScreenName());

//        te = null;
//        twitterAPI2.createFriendshipAsync(id2.name);
//        waitForResponse();
//        assertEquals(403, te.getStatusCode());
        te = null;
        async2.createFriendship("doesnotexist--");
        waitForResponse();
        //now befriending with non-existing user returns 404
        //http://groups.google.com/group/twitter-development-talk/browse_thread/thread/bd2a912b181bc39f
        //assertEquals(404, te.getStatusCode());
        // now it returns 403
        assertEquals(403, te.getStatusCode());
        assertEquals(108, te.getErrorCode());

    }

    @Test
    void testRateLimitStatus() throws Exception {
        async1.getRateLimitStatus();
        waitForResponse();
        RateLimitStatus status = rateLimitStatus.values().iterator().next();
        assertTrue(1 < status.getLimit());
        assertTrue(1 < status.getRemaining());
    }

    @Test
    void testAppOnlyAuthWithBuildingConf1() throws Exception {
        // setup
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        final AsyncTwitter twitter = new AsyncTwitterFactory(builder.build()).getInstance();

        // exercise & verify
        twitter.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        OAuth2Token token = twitter.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());

        twitter.addListener(this);
        testRateLimitStatus();
    }

    @Test
    void testAppOnlyAuthAsyncWithBuildingConf1() throws Exception {
        // setup
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        final AsyncTwitter twitter = new AsyncTwitterFactory(builder.build()).getInstance();

        // exercise & verify
        twitter.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        twitter.addListener(this);
        twitter.getOAuth2TokenAsync();
        waitForResponse();
        testRateLimitStatus();
    }

    @Test
    void testAppOnlyAuthWithBuildingConf2() throws Exception {
        // setup
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        final AsyncTwitter twitter = new AsyncTwitterFactory(builder.build()).getInstance();

        // exercise & verify
        OAuth2Token token = twitter.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());

        TwitterListener listener = new TwitterAdapter() {

            @Override
            public void gotRateLimitStatus(Map<String, RateLimitStatus> rateLimitStatus) {
                super.gotRateLimitStatus(rateLimitStatus);
                RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
                assertNotNull(searchTweetsRateLimit);
                assertEquals(searchTweetsRateLimit.getLimit(), 450);
                notifyResponse();
            }


            @Override
            public void onException(TwitterException ex, TwitterMethod method) {
                assertEquals(403, ex.getStatusCode());
                assertEquals(220, ex.getErrorCode());
                assertEquals("Your credentials do not allow access to this resource", ex.getErrorMessage());
                notifyResponse();
            }


        };
        twitter.addListener(listener);
        twitter.getRateLimitStatus("search");
        waitForResponse();

    }


    @Test
    void testAppOnlyAuthAsyncWithBuildingConf2() throws Exception {
        // setup
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
        builder.setOAuthConsumerKey(browserConsumerKey).setOAuthConsumerSecret(browserConsumerSecret);
        final AsyncTwitter twitter = new AsyncTwitterFactory(builder.build()).getInstance();

        // exercise & verify
        twitter.addListener(this);
        twitter.getOAuth2TokenAsync();
        waitForResponse();

        TwitterListener listener = new TwitterAdapter() {

            @Override
            public void gotRateLimitStatus(Map<String, RateLimitStatus> rateLimitStatus) {
                super.gotRateLimitStatus(rateLimitStatus);
                RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
                assertNotNull(searchTweetsRateLimit);
                assertEquals(searchTweetsRateLimit.getLimit(), 450);
                notifyResponse();
            }


            @Override
            public void onException(TwitterException ex, TwitterMethod method) {
                assertEquals(403, ex.getStatusCode());
                assertEquals(220, ex.getErrorCode());
                assertEquals("Your credentials do not allow access to this resource", ex.getErrorMessage());
                notifyResponse();
            }


        };
        twitter.addListener(listener);
        twitter.getRateLimitStatus("search");
        waitForResponse();

    }

    @Test
    void testLookup() throws TwitterException {
        async1.lookup(20L, 432656548536401920L);
        waitForResponse();
        assertEquals(2, statuses.size());
    }


    private ResponseList<Status> statuses = null;
    private ResponseList<User> users = null;
    private ResponseList<DirectMessage> messages = null;
    private Status status = null;
    private User user = null;
    private boolean test;
    private UserList userList;
    private PagableResponseList<UserList> pagableUserLists;
    private Relationship relationship;
    private DirectMessage message = null;
    private TwitterException te = null;
    private Map<String, RateLimitStatus> rateLimitStatus;
    private boolean exists;
    private QueryResult queryResult;
    private IDs ids;
    private List<Trends> trendsList;
    private Trends trends;
    private boolean blockExists;

    /*Search API Methods*/
    @Override
    public void searched(QueryResult result) {
        this.queryResult = result;
        notifyResponse();
    }

    /*Timeline Methods*/
    @Override
    public void gotHomeTimeline(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    @Override
    public void gotUserTimeline(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    @Override
    public void gotRetweetsOfMe(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    @Override
    public void gotMentions(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    @Override
    public void lookedup(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    /*Status Methods*/
    @Override
    public void gotShowStatus(Status status) {
        this.status = status;
        notifyResponse();
    }

    @Override
    public void updatedStatus(Status status) {
        this.status = status;
        notifyResponse();
    }

    @Override
    public void destroyedStatus(Status destroyedStatus) {
        this.status = destroyedStatus;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.0.10
     */
    @Override
    public void retweetedStatus(Status retweetedStatus) {
        this.status = retweetedStatus;
        notifyResponse();
    }

    @Override
    public void gotOEmbed(OEmbed oembed) {
        this.oembed = oembed;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotRetweets(ResponseList<Status> retweets) {
        this.statuses = retweets;
        notifyResponse();
    }

    /*User Methods*/
    @Override
    public void gotUserDetail(User user) {
        this.user = user;
        notifyResponse();
    }

    @Override
    public void lookedupUsers(ResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    @Override
    public void searchedUser(ResponseList<User> userList) {
        this.users = userList;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.1
     */
    @Override
    public void gotSuggestedUserCategories(ResponseList<Category> categories) {
        this.categories = categories;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.1
     */
    @Override
    public void gotUserSuggestions(ResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.9
     */
    @Override
    public void gotMemberSuggestions(ResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    @Override
    public void gotContributors(ResponseList<User> users) {
        notifyResponse();
    }

    @Override
    public void removedProfileBanner() {
        notifyResponse();
    }

    @Override
    public void updatedProfileBanner() {
        notifyResponse();
    }

    @Override
    public void gotContributees(ResponseList<User> users) {
        notifyResponse();
    }

    /*List Methods*/

    @Override
    public void createdUserList(UserList userList) {
        this.userList = userList;
        notifyResponse();
    }

    @Override
    public void updatedUserList(UserList userList) {
        this.userList = userList;
        notifyResponse();
    }

    @Override
    public void gotUserLists(ResponseList<UserList> userLists) {
        this.userLists = userLists;
        notifyResponse();
    }

    @Override
    public void gotShowUserList(UserList userList) {
        this.userList = userList;
        notifyResponse();
    }

    @Override
    public void destroyedUserList(UserList userList) {
        this.userList = userList;
        notifyResponse();
    }

    @Override
    public void gotUserListStatuses(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    @Override
    public void gotUserListMemberships(PagableResponseList<UserList> userLists) {
        this.pagableUserLists = userLists;
        notifyResponse();
    }

    @Override
    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists) {
        this.pagableUserLists = userLists;
        notifyResponse();
    }

    /*List Members Methods*/

    @Override
    public void gotUserListMembers(PagableResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    @Override
    public void gotSavedSearches(ResponseList<SavedSearch> savedSearches) {
        this.savedSearches = savedSearches;
        notifyResponse();
    }

    @Override
    public void gotSavedSearch(SavedSearch savedSearch) {
        this.savedSearch = savedSearch;
        notifyResponse();
    }

    @Override
    public void createdSavedSearch(SavedSearch savedSearch) {
        this.savedSearch = savedSearch;
        notifyResponse();
    }

    @Override
    public void destroyedSavedSearch(SavedSearch savedSearch) {
        this.savedSearch = savedSearch;
        notifyResponse();
    }

    @Override
    public void createdUserListMember(UserList userList) {
        this.userList = userList;
    }

    @Override
    public void createdUserListMembers(UserList userList) {
        this.userList = userList;
    }

    @Override
    public void destroyedUserListMember(UserList userList) {
        this.userList = userList;
    }

    @Override
    public void checkedUserListMembership(User user) {
        this.user = user;
    }

    /*List Subscribers Methods*/

    @Override
    public void gotUserListSubscribers(PagableResponseList<User> users) {
        this.users = users;
    }

    @Override
    public void subscribedUserList(UserList userList) {
        this.userList = userList;
    }

    @Override
    public void unsubscribedUserList(UserList userList) {
        this.userList = userList;
    }

    @Override
    public void checkedUserListSubscription(User user) {
        this.user = user;
    }

    /*Direct Message Methods*/
    @Override
    public void gotDirectMessages(ResponseList<DirectMessage> messages) {
        this.messages = messages;
        notifyResponse();
    }

    @Override
    public void gotSentDirectMessages(ResponseList<DirectMessage> messages) {
        this.messages = messages;
        notifyResponse();
    }

    @Override
    public void sentDirectMessage(DirectMessage message) {
        this.message = message;
        notifyResponse();
    }

    @Override
    public void destroyedDirectMessage(long id) {
        this.id = id;
        notifyResponse();
    }

    @Override
    public void gotDirectMessage(DirectMessage message) {
        this.message = message;
        notifyResponse();
    }

    /*Friendship Methods*/
    @Override
    public void createdFriendship(User user) {
        this.user = user;
        notifyResponse();
    }

    @Override
    public void destroyedFriendship(User user) {
        this.user = user;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void gotShowFriendship(Relationship relationship) {
        this.relationship = relationship;
        notifyResponse();
    }

    @Override
    public void gotFriendsList(PagableResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    @Override
    public void gotFollowersList(PagableResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.2
     */
    @Override
    public void gotIncomingFriendships(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.2
     */
    @Override
    public void gotOutgoingFriendships(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    /*Social Graph Methods*/
    @Override
    public void gotFriendsIDs(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    @Override
    public void gotFollowersIDs(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    @Override
    public void lookedUpFriendships(ResponseList<Friendship> friendships) {
        this.friendships = friendships;
        notifyResponse();
    }


    @Override
    public void updatedFriendship(Relationship relationship) {
        this.relationship = relationship;
        notifyResponse();
    }

    /*Account Methods*/

    @Override
    public void gotRateLimitStatus(Map<String, RateLimitStatus> rateLimitStatus) {
        this.rateLimitStatus = rateLimitStatus;
        notifyResponse();
    }

    @Override
    public void verifiedCredentials(User user) {
        this.user = user;
        notifyResponse();
    }

    @Override
    public void gotAccountSettings(AccountSettings settings) {
        this.settings = settings;
        notifyResponse();
    }

    @Override
    public void updatedAccountSettings(AccountSettings settings) {
        this.settings = settings;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.0
     */
    @Override
    public void updatedProfileImage(User user) {
        this.user = user;
        notifyResponse();
    }


    @Override
    public void updatedProfile(User user) {
        this.user = user;
        notifyResponse();
    }

    /*Favorite Methods*/
    @Override
    public void gotFavorites(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    @Override
    public void createdFavorite(Status status) {
        this.status = status;
        notifyResponse();
    }

    @Override
    public void destroyedFavorite(Status status) {
        this.status = status;
        notifyResponse();
    }

    /*Block Methods*/
    @Override
    public void createdBlock(User user) {
        this.user = user;
        notifyResponse();
    }

    @Override
    public void destroyedBlock(User user) {
        this.user = user;
        notifyResponse();
    }

    @Override
    public void gotBlocksList(ResponseList<User> blockingUsers) {
        this.users = blockingUsers;
        notifyResponse();
    }

    @Override
    public void gotBlockIDs(IDs blockingUsersIDs) {
        this.ids = blockingUsersIDs;
        notifyResponse();
    }

    /*Mute Methods*/
    @Override
    public void createdMute(User user) {
        this.user = user;
        notifyResponse();
    }

    @Override
    public void destroyedMute(User user) {
        this.user = user;
        notifyResponse();
    }

    @Override
    public void gotMutesList(ResponseList<User> mutingUsers) {
        this.users = mutingUsers;
        notifyResponse();
    }

    @Override
    public void gotMuteIDs(IDs mutingUsersIDs) {
        this.ids = mutingUsersIDs;
        notifyResponse();
    }

    /*Spam Reporting Methods*/

    @Override
    public void reportedSpam(User reportedSpammer) {
        this.user = reportedSpammer;
        notifyResponse();
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
        this.locations = locations;
        notifyResponse();
    }

    @Override
    public void gotClosestTrends(ResponseList<Location> locations) {
        this.locations = locations;
        notifyResponse();
    }

    /*Geo Methods*/
    @Override
    public void searchedPlaces(ResponseList<Place> places) {
        this.places = places;
        notifyResponse();
    }

    @Override
    public void gotSimilarPlaces(ResponseList<Place> places) {
        this.places = places;
        notifyResponse();
    }

    @Override
    public void gotReverseGeoCode(ResponseList<Place> places) {
        this.places = places;
        notifyResponse();
    }

    @Override
    public void gotGeoDetails(Place place) {
        this.place = place;
        notifyResponse();
    }

    @Override
    public void gotPlaceTrends(Trends trends) {
        this.trends = trends;
        notifyResponse();
    }

    /* Legal Resources */

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void gotTermsOfService(String str) {
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.7
     */
    @Override
    public void gotPrivacyPolicy(String str) {
        notifyResponse();
    }

    /*Help Methods*/
    @Override
    public void gotAPIConfiguration(TwitterAPIConfiguration conf) {
        this.apiConf = conf;
        notifyResponse();
    }

    @Override
    public void gotLanguages(ResponseList<HelpResources.Language> languages) {
        this.languages = languages;
        notifyResponse();
    }

    /**
     * @param te     TwitterException
     * @param method int
     */
    @Override
    public void onException(TwitterException te, TwitterMethod method) {
        this.te = te;
        System.out.println("onexception on " + method.name());
        te.printStackTrace();
        notifyResponse();
    }

    @Override
    public void gotOAuthRequestToken(RequestToken token) {
    }

    @Override
    public void gotOAuthAccessToken(AccessToken token) {
    }

    @Override
    public void gotOAuth2Token(OAuth2Token token) {
        System.out.println("[gotOAuth2Token] token:" + token.getAccessToken().replaceAll("\\w","*") + " type:" + token.getTokenType());
        assertEquals("bearer", token.getTokenType());
        notifyResponse();
    }

    private synchronized void notifyResponse() {
        this.notify();
    }

    private synchronized void waitForResponse() {
        try {
            this.wait(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param obj the object to be asserted
     * @return the deserialized object
     * @throws Exception in the case the object is not (de)serializable
     */
    public static Object assertDeserializedFormIsEqual(Object obj) throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
        oos.writeObject(obj);
        byteOutputStream.close();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(byteInputStream);
        Object that = ois.readObject();
        byteInputStream.close();
        ois.close();
        assertEquals(obj, that);
        return that;
    }

    static final String[] files = {"src/test/resources/t4j-reverse.jpeg",
            "src/test/resources/t4j-reverse.png",
            "src/test/resources/t4j-reverse.gif",
            "src/test/resources/t4j.jpeg",
            "src/test/resources/t4j.png",
            "src/test/resources/t4j.gif",
    };

    private static File getRandomlyChosenFile() {
        int rand = (int) (System.currentTimeMillis() % 6);
        File file = new File(files[rand]);
        if (!file.exists()) {
            file = new File("twitter4j-core/" + files[rand]);
        }
        return file;
    }


}
