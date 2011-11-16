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

import junit.framework.Assert;
import twitter4j.api.HelpMethods;
import twitter4j.json.DataObjectFactory;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AsyncTwitterTest extends TwitterTestBase implements TwitterListener {

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
    private ResponseList<HelpMethods.Language> languages;
    private TwitterAPIConfiguration apiConf;

    public AsyncTwitterTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
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
        user = null;
        message = null;
        te = null;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPublicTimeline() throws Exception {
        async1.getPublicTimeline();
        waitForResponse();
        Assert.assertTrue("size", 0 < statuses.size());
        assertDeserializedFormIsEqual(statuses);
    }

    public void testShowUser() throws Exception {
        async1.showUser(id1.screenName);
        waitForResponse();
        User user = this.user;
        Assert.assertEquals(id1.screenName, user.getScreenName());
        Assert.assertTrue(0 <= user.getFavouritesCount());
        Assert.assertTrue(0 <= user.getFollowersCount());
        Assert.assertTrue(0 <= user.getFriendsCount());
        Assert.assertTrue(0 <= user.getStatusesCount());
        Assert.assertNotNull(user.getProfileBackgroundColor());
        Assert.assertNotNull(user.getProfileTextColor());
        Assert.assertNotNull(user.getProfileLinkColor());
        Assert.assertNotNull(user.getProfileSidebarBorderColor());
        Assert.assertNotNull(user.getProfileSidebarFillColor());
        Assert.assertNotNull(user.getProfileTextColor());

        this.user = null;
    }

    public void testSearchUser() throws TwitterException {
        async1.searchUsers("Doug Williams", 1);
        waitForResponse();
        Assert.assertTrue(4 < users.size());
    }

    public void testGetUserTimeline_Show() throws Exception {
        async2.getUserTimeline();
        waitForResponse();
        Assert.assertTrue("size", 10 < statuses.size());
        async2.getUserTimeline(new Paging(999383469l));
    }

    public void testAccountProfileImageUpdates() throws Exception {
        te = null;
        async1.updateProfileImage(getRandomlyChosenFile());
        waitForResponse();
        Assert.assertNull(te);
        // tile randomly
        async1.updateProfileBackgroundImage(getRandomlyChosenFile(),
                (5 < System.currentTimeMillis() % 5));
        waitForResponse();
        Assert.assertNull(te);
    }


    public void testFavorite() throws Exception {
        Status status = twitter1.updateStatus(new Date().toString());
        async2.createFavorite(status.getId());
        waitForResponse();
        Assert.assertEquals(status, this.status);
        this.status = null;
        //need to wait for a second to get it destoryable
        Thread.sleep(5000);
        async2.destroyFavorite(status.getId());
        waitForResponse();
        if (te != null && te.getStatusCode() == 404) {
            // sometimes destorying favorite fails with 404
        } else {
            Assert.assertEquals(status, this.status);
        }
    }

    public void testSocialGraphMethods() throws Exception {
        async1.getFriendsIDs(-1);
        waitForResponse();
        int yusukey = 4933401;
        assertIDExsits("twit4j is following yusukey", ids, yusukey);
        int ryunosukey = 48528137;
        async1.getFriendsIDs(ryunosukey, -1);
        waitForResponse();
        Assert.assertEquals("ryunosukey is not following anyone", 0, ids.getIDs().length);
        async1.getFriendsIDs("yusukey", -1);
        waitForResponse();
        assertIDExsits("yusukey is following ryunosukey", ids, ryunosukey);

        try {
            twitter2.createFriendship(id1.screenName);
        } catch (TwitterException te) {
        }
        async1.getFollowersIDs(-1);
        waitForResponse();
        assertIDExsits("twit4j2(6377362) is following twit4j(6358482)", ids, 6377362);
        async1.getFollowersIDs(ryunosukey, -1);
        waitForResponse();
        assertIDExsits("yusukey is following ryunosukey", ids, yusukey);
        async1.getFollowersIDs("ryunosukey", -1);
        waitForResponse();
        assertIDExsits("yusukey is following ryunosukey", ids, yusukey);
    }

    private void assertIDExsits(String assertion, IDs ids, int idToFind) {
        boolean found = false;
        for (long id : ids.getIDs()) {
            if (id == idToFind) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(assertion, found);
    }

    public void testAccountMethods() throws Exception {

        async1.verifyCredentials();
        waitForResponse();
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getName());
        Assert.assertNotNull(user.getURL());
        Assert.assertNotNull(user.getLocation());
        Assert.assertNotNull(user.getDescription());

        String oldName, oldURL, oldLocation, oldDescription;
        oldName = user.getName();
        oldURL = user.getURL().toString();
        oldLocation = user.getLocation();
        oldDescription = user.getDescription();

        String newName, newURL, newLocation, newDescription;
        String neu = "new";
        newName = user.getName() + neu;
        newURL = user.getURL() + neu;
        newLocation = new Date().toString();
        newDescription = user.getDescription() + neu;

        async1.updateProfile(newName, newURL, newLocation, newDescription);

        waitForResponse();
        Assert.assertEquals(newName, user.getName());
        Assert.assertEquals(newURL, user.getURL().toString());
        Assert.assertEquals(newLocation, user.getLocation());
        Assert.assertEquals(newDescription, user.getDescription());

        //revert the profile
        async1.updateProfile(oldName, oldURL, oldLocation, oldDescription);
        waitForResponse();

        bestFriend1Async.existsFriendship(bestFriend1.screenName, bestFriend2.screenName);
        waitForResponse();
        Assert.assertTrue(exists);

        async1.updateProfileColors("f00", "f0f", "0ff", "0f0", "f0f");
        waitForResponse();
        Assert.assertEquals("f00", user.getProfileBackgroundColor());
        Assert.assertEquals("f0f", user.getProfileTextColor());
        Assert.assertEquals("0ff", user.getProfileLinkColor());
        Assert.assertEquals("0f0", user.getProfileSidebarFillColor());
        Assert.assertEquals("f0f", user.getProfileSidebarBorderColor());
        async1.updateProfileColors("f0f", "f00", "f0f", "0ff", "0f0");
        waitForResponse();
        Assert.assertEquals("f0f", user.getProfileBackgroundColor());
        Assert.assertEquals("f00", user.getProfileTextColor());
        Assert.assertEquals("f0f", user.getProfileLinkColor());
        Assert.assertEquals("0ff", user.getProfileSidebarFillColor());
        Assert.assertEquals("0f0", user.getProfileSidebarBorderColor());
        async1.updateProfileColors("87bc44", "9ae4e8", "000000", "0000ff", "e0ff92");
        waitForResponse();
        Assert.assertEquals("87bc44", user.getProfileBackgroundColor());
        Assert.assertEquals("9ae4e8", user.getProfileTextColor());
        Assert.assertEquals("000000", user.getProfileLinkColor());
        Assert.assertEquals("0000ff", user.getProfileSidebarFillColor());
        Assert.assertEquals("e0ff92", user.getProfileSidebarBorderColor());
        async1.updateProfileColors("f0f", null, "f0f", null, "0f0");
        waitForResponse();
        Assert.assertEquals("f0f", user.getProfileBackgroundColor());
        Assert.assertEquals("9ae4e8", user.getProfileTextColor());
        Assert.assertEquals("f0f", user.getProfileLinkColor());
        Assert.assertEquals("0000ff", user.getProfileSidebarFillColor());
        Assert.assertEquals("0f0", user.getProfileSidebarBorderColor());
        async1.updateProfileColors(null, "f00", null, "0ff", null);
        waitForResponse();
        Assert.assertEquals("f0f", user.getProfileBackgroundColor());
        Assert.assertEquals("f00", user.getProfileTextColor());
        Assert.assertEquals("f0f", user.getProfileLinkColor());
        Assert.assertEquals("0ff", user.getProfileSidebarFillColor());
        Assert.assertEquals("0f0", user.getProfileSidebarBorderColor());
        async1.updateProfileColors("9ae4e8", "000000", "0000ff", "e0ff92", "87bc44");
        waitForResponse();
        Assert.assertEquals("9ae4e8", user.getProfileBackgroundColor());
        Assert.assertEquals("000000", user.getProfileTextColor());
        Assert.assertEquals("0000ff", user.getProfileLinkColor());
        Assert.assertEquals("e0ff92", user.getProfileSidebarFillColor());
        Assert.assertEquals("87bc44", user.getProfileSidebarBorderColor());
    }

    public void testShow() throws Exception {
        async2.showStatus(1000l);
        waitForResponse();
        Assert.assertEquals(52, status.getUser().getId());
        assertDeserializedFormIsEqual(status);
    }

    public void testBlock() throws Exception {
        async2.createBlock(id1.screenName);
        waitForResponse();
        async2.destroyBlock(id1.screenName);
        waitForResponse();

        async1.existsBlock("twit4j2");
        Assert.assertFalse(blockExists);
        waitForResponse();
        async1.existsBlock("twit4jblock");
        waitForResponse();
        Assert.assertTrue(blockExists);

        async1.getBlockingUsers();
        waitForResponse();
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(39771963, users.get(0).getId());
        async1.getBlockingUsers(1);
        waitForResponse();
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(39771963, users.get(0).getId());
        async1.getBlockingUsersIDs();
        waitForResponse();
        Assert.assertEquals(1, ids.getIDs().length);
        Assert.assertEquals(39771963, ids.getIDs()[0]);
    }

    public void testUpdate() throws Exception {
        String date = new java.util.Date().toString() + "test";
        async1.updateStatus(date);
        waitForResponse();
        Assert.assertEquals("", date, status.getText());

        long id = status.getId();

        async2.updateStatus(new StatusUpdate("@" + id1.screenName + " " + date).inReplyToStatusId(id));
        waitForResponse();
        Assert.assertEquals("", "@" + id1.screenName + " " + date, status.getText());
        Assert.assertEquals("", id, status.getInReplyToStatusId());
        Assert.assertEquals(twitter1.verifyCredentials().getId(), status.getInReplyToUserId());


        id = status.getId();
        this.status = null;
        async2.destroyStatus(id);
        waitForResponse();
        Assert.assertEquals("", "@" + id1.screenName + " " + date, status.getText());
        assertDeserializedFormIsEqual(status);
    }

    public void testDirectMessages() throws Exception {
        String expectedReturn = new Date() + ":directmessage test";
        async1.sendDirectMessage(id3.id, expectedReturn);
        waitForResponse();
        Assert.assertEquals(expectedReturn, message.getText());
        async3.getDirectMessages();
        waitForResponse();
        Assert.assertTrue(1 <= messages.size());
    }

    public void testCreateDestroyFriend() throws Exception {
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
        Assert.assertEquals(id1.screenName, user.getScreenName());

//        te = null;
//        twitterAPI2.createFriendshipAsync(id2.name);
//        waitForResponse();
//        assertEquals(403, te.getStatusCode());
        te = null;
        async2.createFriendship("doesnotexist--");
        waitForResponse();
        //now befriending with non-existing user returns 404
        //http://groups.google.com/group/twitter-development-talk/browse_thread/thread/bd2a912b181bc39f
        Assert.assertEquals(404, te.getStatusCode());

    }

    public void testRateLimitStatus() throws Exception {
        async1.getRateLimitStatus();
        waitForResponse();
        Assert.assertTrue(10 < rateLimitStatus.getHourlyLimit());
        Assert.assertTrue(10 < rateLimitStatus.getRemainingHits());
    }

    public void testFollowLeave() throws Exception {
        try {
            twitter1.disableNotification("twit4jprotected");
        } catch (TwitterException te) {
        }
        te = null;
        async1.enableNotification("twit4jprotected");
        waitForResponse();
        Assert.assertNull(te);
        async1.disableNotification("twit4jprotected");
        waitForResponse();
        Assert.assertNull(te);
        assertDeserializedFormIsEqual(user);

    }

    public void testNoRetweet() throws Exception {
        async1.getNoRetweetIds();
        waitForResponse();
        assertNotNull(this.ids);
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
    private RateLimitStatus rateLimitStatus;
    private boolean exists;
    private QueryResult queryResult;
    private IDs ids;
    private List<Trends> trendsList;
    private Trends trends;
    private boolean blockExists;
    private RelatedResults relatedResults;

    /*Search API Methods*/
    public void searched(QueryResult result) {
        this.queryResult = result;
        notifyResponse();
    }

    public void gotTrends(Trends trends) {
        this.trends = trends;
        notifyResponse();
    }

    public void gotCurrentTrends(Trends trends) {
        this.trends = trends;
        notifyResponse();
    }

    public void gotDailyTrends(ResponseList<Trends> trendsList) {
        this.trendsList = trendsList;
        notifyResponse();
    }

    public void gotWeeklyTrends(ResponseList<Trends> trendsList) {
        this.trendsList = trendsList;
        notifyResponse();
    }

    /*Timeline Methods*/
    public void gotPublicTimeline(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotHomeTimeline(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotFriendsTimeline(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotUserTimeline(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotMentions(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotRetweetedByMe(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotRetweetedToMe(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotRetweetsOfMe(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotRetweetedByUser(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotRetweetedToUser(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    /*Status Methods*/
    public void gotShowStatus(Status status) {
        this.status = status;
        notifyResponse();
    }

    public void updatedStatus(Status status) {
        this.status = status;
        notifyResponse();
    }

    public void destroyedStatus(Status destroyedStatus) {
        this.status = destroyedStatus;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.0.10
     */
    public void retweetedStatus(Status retweetedStatus) {
        this.status = retweetedStatus;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotRetweets(ResponseList<Status> retweets) {
        this.statuses = retweets;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.3
     */
    public void gotRetweetedBy(ResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.3
     */
    public void gotRetweetedByIDs(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    /*User Methods*/
    public void gotUserDetail(User user) {
        this.user = user;
        notifyResponse();
    }

    public void lookedupUsers(ResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    public void searchedUser(ResponseList<User> userList) {
        this.users = userList;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.1
     */
    public void gotSuggestedUserCategories(ResponseList<Category> categories) {
        this.categories = categories;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.1
     */
    public void gotUserSuggestions(ResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.9
     */
    public void gotMemberSuggestions(ResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.7
     */
    public void gotProfileImage(ProfileImage image) {
        notifyResponse();
    }

    public void gotFriendsStatuses(PagableResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    public void gotFollowersStatuses(PagableResponseList<User> users) {
        this.users = users;
        notifyResponse();
    }

    /*List Methods*/

    public void createdUserList(UserList userList) {
        this.userList = userList;
        notifyResponse();
    }

    public void updatedUserList(UserList userList) {
        this.userList = userList;
        notifyResponse();
    }

    public void gotUserLists(PagableResponseList<UserList> userLists) {
        this.pagableUserLists = userLists;
        notifyResponse();
    }

    public void gotShowUserList(UserList userList) {
        this.userList = userList;
        notifyResponse();
    }

    public void destroyedUserList(UserList userList) {
        this.userList = userList;
        notifyResponse();
    }

    public void gotUserListStatuses(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotUserListMemberships(PagableResponseList<UserList> userLists) {
        this.pagableUserLists = userLists;
        notifyResponse();
    }

    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists) {
        this.pagableUserLists = userLists;
        notifyResponse();
    }

    public void gotAllUserLists(ResponseList<UserList> userLists) {
        this.userLists = userLists;
        notifyResponse();
    }

    /*List Members Methods*/

    public void gotUserListMembers(PagableResponseList<User> users) {
        this.users = users;
    }

    public void addedUserListMember(UserList userList) {
        this.userList = userList;
    }

    public void addedUserListMembers(UserList userList) {
        this.userList = userList;
    }

    public void deletedUserListMember(UserList userList) {
        this.userList = userList;
    }

    public void checkedUserListMembership(User user) {
        this.user = user;
    }

    /*List Subscribers Methods*/

    public void gotUserListSubscribers(PagableResponseList<User> users) {
        this.users = users;
    }

    public void subscribedUserList(UserList userList) {
        this.userList = userList;
    }

    public void unsubscribedUserList(UserList userList) {
        this.userList = userList;
    }

    public void checkedUserListSubscription(User user) {
        this.user = user;
    }

    /*Direct Message Methods*/
    public void gotDirectMessages(ResponseList<DirectMessage> messages) {
        this.messages = messages;
        notifyResponse();
    }

    public void gotSentDirectMessages(ResponseList<DirectMessage> messages) {
        this.messages = messages;
        notifyResponse();
    }

    public void sentDirectMessage(DirectMessage message) {
        this.message = message;
        notifyResponse();
    }

    public void destroyedDirectMessage(DirectMessage message) {
        this.message = message;
        notifyResponse();
    }

    public void gotDirectMessage(DirectMessage message) {
        this.message = message;
        notifyResponse();
    }

    /*Friendship Methods*/
    public void createdFriendship(User user) {
        this.user = user;
        notifyResponse();
    }

    public void destroyedFriendship(User user) {
        this.user = user;
        notifyResponse();
    }

    public void gotExistsFriendship(boolean exists) {
        this.exists = exists;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void gotShowFriendship(Relationship relationship) {
        this.relationship = relationship;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.2
     */
    public void gotIncomingFriendships(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.2
     */
    public void gotOutgoingFriendships(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    /*Social Graph Methods*/
    public void gotFriendsIDs(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    public void gotFollowersIDs(IDs ids) {
        this.ids = ids;
        notifyResponse();
    }

    public void lookedUpFriendships(ResponseList<Friendship> friendships) {
        this.friendships = friendships;
        notifyResponse();
    }


    public void updatedFriendship(Relationship relationship) {
        this.relationship = relationship;
        notifyResponse();
    }

    public void gotNoRetweetIds(IDs ids) {
        this.ids = ids;
        assertNotNull(DataObjectFactory.getRawJSON(this.ids));
        try {
            assertEquals(this.ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        } catch (TwitterException e) {
            fail("");
        }
        notifyResponse();
    }

    /*Account Methods*/

    public void gotRateLimitStatus(RateLimitStatus status) {
        this.rateLimitStatus = status;
        notifyResponse();
    }

    public void verifiedCredentials(User user) {
        this.user = user;
        notifyResponse();
    }

    public void updatedProfileColors(User user) {
        this.user = user;
        notifyResponse();
    }

    public void gotAccountTotals(AccountTotals totals) {
        this.totals = totals;
        notifyResponse();
    }

    public void gotAccountSettings(AccountSettings settings) {
        this.settings = settings;
        notifyResponse();
    }

    public void updatedAccountSettings(AccountSettings settings) {
        this.settings = settings;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void updatedProfileImage(User user) {
        this.user = user;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.0
     */
    public void updatedProfileBackgroundImage(User user) {
        this.user = user;
        notifyResponse();

    }

    public void updatedProfile(User user) {
        this.user = user;
        notifyResponse();
    }

    /*Favorite Methods*/
    public void gotFavorites(ResponseList<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void createdFavorite(Status status) {
        this.status = status;
        notifyResponse();
    }

    public void destroyedFavorite(Status status) {
        this.status = status;
        notifyResponse();
    }

    /*Notification Methods*/
    public void enabledNotification(User user) {
        this.user = user;
        notifyResponse();
    }

    public void disabledNotification(User user) {
        this.user = user;
        notifyResponse();
    }

    /*Block Methods*/
    public void createdBlock(User user) {
        this.user = user;
        notifyResponse();
    }

    public void destroyedBlock(User user) {
        this.user = user;
        notifyResponse();
    }

    public void gotExistsBlock(boolean exists) {
        this.blockExists = exists;
        notifyResponse();
    }

    public void gotBlockingUsers(ResponseList<User> blockingUsers) {
        this.users = blockingUsers;
        notifyResponse();
    }

    public void gotBlockingUsersIDs(IDs blockingUsersIDs) {
        this.ids = blockingUsersIDs;
        notifyResponse();
    }

    /*Spam Reporting Methods*/

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
    public void gotAvailableTrends(ResponseList<Location> locations) {
        this.locations = locations;
        notifyResponse();
    }

    /**
     * @param trends trends
     * @since Twitter4J 2.1.1
     */
    public void gotLocationTrends(Trends trends) {
        this.trends = trends;
        notifyResponse();
    }

    /*Geo Methods*/
    public void searchedPlaces(ResponseList<Place> places) {
        this.places = places;
        notifyResponse();
    }

    public void gotSimilarPlaces(SimilarPlaces places) {
        this.places = places;
        notifyResponse();
    }


    public void gotNearByPlaces(ResponseList<Place> places) {
        this.places = places;
        notifyResponse();
    }

    public void gotReverseGeoCode(ResponseList<Place> places) {
        this.places = places;
        notifyResponse();
    }

    public void gotGeoDetails(Place place) {
        this.place = place;
        notifyResponse();
    }

    public void createdPlace(Place place) {
        this.place = place;
        notifyResponse();
    }

    /* Legal Resources */

    /**
     * @since Twitter4J 2.1.7
     */
    public void gotTermsOfService(String str) {
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.7
     */
    public void gotPrivacyPolicy(String str) {
        notifyResponse();
    }

    /* #newtwitter Methods */

    /**
     *
     */
    public void gotRelatedResults(RelatedResults relatedResults) {
        this.relatedResults = relatedResults;
        notifyResponse();
    }

    /*Help Methods*/
    public void tested(boolean test) {
        this.test = test;
        notifyResponse();
    }

    public void gotAPIConfiguration(TwitterAPIConfiguration conf) {
        this.apiConf = conf;
        notifyResponse();
    }

    public void gotLanguages(ResponseList<HelpMethods.Language> languages) {
        this.languages = languages;
        notifyResponse();
    }

    /**
     * @param te     TwitterException
     * @param method int
     */
    public void onException(TwitterException te, TwitterMethod method) {
        this.te = te;
        System.out.println("onexception on " + method.name());
        te.printStackTrace();
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
        Assert.assertEquals(obj, that);
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
