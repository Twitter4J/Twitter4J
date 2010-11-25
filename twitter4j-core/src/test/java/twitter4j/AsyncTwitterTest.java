/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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

import twitter4j.http.AccessToken;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import static twitter4j.DAOTest.*;
/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AsyncTwitterTest extends TwitterTestBase implements TwitterListener {

    private AsyncTwitter async1 = null;
    private AsyncTwitter async2 = null;
    private AsyncTwitter bestFriend1Async = null;
    private ResponseList<Location> locations;
    private ResponseList<Place> places;
    private Place place;
    private ResponseList<Category> categories;

    public AsyncTwitterTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        AsyncTwitterFactory factory = new AsyncTwitterFactory(this);
        async1 = factory.getInstance();
        async1.setOAuthConsumer(desktopConsumerKey,desktopConsumerSecret);
        async1.setOAuthAccessToken(new AccessToken(id1.accessToken, id1.accessTokenSecret));

        async2 = factory.getInstance();
        async2 = factory.getInstance();
        async2.setOAuthConsumer(desktopConsumerKey,desktopConsumerSecret);
        async2.setOAuthAccessToken(new AccessToken(id2.accessToken, id2.accessTokenSecret));

        bestFriend1Async = factory.getInstance();
        bestFriend1Async.setOAuthConsumer(desktopConsumerKey,desktopConsumerSecret);
        bestFriend1Async.setOAuthAccessToken(new AccessToken(bestFriend1.accessToken, bestFriend1.accessTokenSecret));

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
        assertTrue("size", 0 < statuses.size());
        assertDeserializedFormIsEqual(statuses);
    }

    public void testGetFriendsTimeline() throws Exception {
        async1.getFriendsTimeline();
        waitForResponse();
        assertNotNull(statuses);
        assertTrue(statuses.size() > 0);

        assertDeserializedFormIsEqual(statuses);
    }

    public void testShowUser() throws Exception{
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
        //@todo replace with verifyCredentialsAsync(this);
//        twitterAPI1.getAuthenticatedUserAsync(this);
//        waitForResponse();
//        assertEquals(id1.name, user.getName());
//        assertTrue(0 <= user.getFavouritesCount());
//        assertTrue(0 <= user.getFollowersCount());
//        assertTrue(0 <= user.getFriendsCount());
//        assertTrue(0 <= user.getStatusesCount());
//        assertNotNull(user.getProfileBackgroundColor());
//        assertNotNull(user.getProfileTextColor());
//        assertNotNull(user.getProfileLinkColor());
//        assertNotNull(user.getProfileSidebarBorderColor());
//        assertNotNull(user.getProfileSidebarFillColor());
//        assertNotNull(user.getProfileTextColor());
    }

    public void testSearchUser() throws TwitterException {
        async1.searchUsers("Doug Williams",1);
        waitForResponse();
        assertTrue(4 < users.size());
    }

    public void testGetUserTimeline_Show() throws Exception {
        async2.getUserTimeline();
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        async2.getUserTimeline(new Paging(999383469l));
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        async2.getUserTimeline(id1.screenName);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        async2.getUserTimeline(id1.screenName, new Paging(999383469l));
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        async2.getUserTimeline(id1.screenName, new Paging().count(10));
        waitForResponse();
        assertTrue("size", 5 < statuses.size());
        async2.getUserTimeline(id1.screenName, new Paging(999383469l).count(15));
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        async1.getUserTimeline(new Paging(999383469l).count(25));
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        assertDeserializedFormIsEqual(statuses);
    }
    public void testAccountProfileImageUpdates() throws Exception {
        te = null;
        async1.updateProfileImage(TwitterTest.getRandomlyChosenFile());
        waitForResponse();
        assertNull(te);
        // tile randomly
        async1.updateProfileBackgroundImage(TwitterTest.getRandomlyChosenFile(),
                (5 < System.currentTimeMillis() % 5));
        waitForResponse();
        assertNull(te);
    }


    public void testFavorite() throws Exception {
        Status status = twitterAPI1.updateStatus(new Date().toString());
        async2.createFavorite(status.getId());
        waitForResponse();
        assertEquals(status, this.status);
        this.status = null;
        //need to wait for a second to get it destoryable
        Thread.sleep(5000);
        async2.destroyFavorite(status.getId());
        waitForResponse();
        if(null != te && te.getStatusCode() == 404){
            // sometimes destorying favorite fails with 404
        }else{
            assertEquals(status, this.status);
        }
    }

    public void testSocialGraphMethods() throws Exception {
        async1.getFriendsIDs();
        waitForResponse();
        int yusukey = 4933401;
        assertIDExsits("twit4j is following yusukey", ids, yusukey);
        int ryunosukey = 48528137;
        async1.getFriendsIDs(ryunosukey);
        waitForResponse();
        assertEquals("ryunosukey is not following anyone", 0, ids.getIDs().length);
        async1.getFriendsIDs("yusukey");
        waitForResponse();
        assertIDExsits("yusukey is following ryunosukey", ids, ryunosukey);

        try {
            twitterAPI2.createFriendship(id1.screenName);
        } catch (TwitterException te) {
        }
        async1.getFollowersIDs();
        waitForResponse();
        assertIDExsits("twit4j2(6377362) is following twit4j(6358482)", ids, 6377362);
        async1.getFollowersIDs(ryunosukey);
        waitForResponse();
        assertIDExsits("yusukey is following ryunosukey", ids, yusukey);
        async1.getFollowersIDs("ryunosukey");
        waitForResponse();
        assertIDExsits("yusukey is following ryunosukey", ids, yusukey);
    }

    private void assertIDExsits(String assertion, IDs ids, int idToFind){
        boolean found = false;
        for(int id : ids.getIDs()){
            if(id == idToFind){
                found = true;
                break;
            }
        }
        assertTrue(assertion, found);
    }
    public void testAccountMethods() throws Exception{

        async1.verifyCredentials();
        waitForResponse();
        assertNotNull(user);
        assertNotNull(user.getName());
        assertNotNull(user.getURL());
        assertNotNull(user.getLocation());
        assertNotNull(user.getDescription());

        String oldName, oldURL, oldLocation, oldDescription;
        oldName = user.getName();
        oldURL = user.getURL().toString();
        oldLocation = user.getLocation();
        oldDescription = user.getDescription();

        String newName, newURL, newLocation, newDescription;
        String neu = "new";
        newName = user.getName() + neu;
        newURL = user.getURL() + neu;
        newLocation = user.getLocation()+neu;
        newDescription = user.getDescription()+neu;

        async1.updateProfile(
                newName, null, newURL, newLocation, newDescription);

        waitForResponse();
        assertEquals(newName, user.getName());
        assertEquals(newURL, user.getURL().toString());
        assertEquals(newLocation, user.getLocation());
        assertEquals(newDescription, user.getDescription());

        //revert the profile
        async1.updateProfile(oldName, null, oldURL, oldLocation, oldDescription);
        waitForResponse();

        bestFriend1Async.existsFriendship(bestFriend1.screenName,bestFriend2.screenName);
        waitForResponse();
        assertTrue(exists);

        async1.updateProfileColors("f00", "f0f", "0ff", "0f0", "f0f");
        waitForResponse();
        assertEquals("f00", user.getProfileBackgroundColor());
        assertEquals("f0f", user.getProfileTextColor());
        assertEquals("0ff", user.getProfileLinkColor());
        assertEquals("0f0", user.getProfileSidebarFillColor());
        assertEquals("f0f", user.getProfileSidebarBorderColor());
        async1.updateProfileColors("f0f", "f00", "f0f", "0ff", "0f0");
        waitForResponse();
        assertEquals("f0f", user.getProfileBackgroundColor());
        assertEquals("f00", user.getProfileTextColor());
        assertEquals("f0f", user.getProfileLinkColor());
        assertEquals("0ff", user.getProfileSidebarFillColor());
        assertEquals("0f0", user.getProfileSidebarBorderColor());
        async1.updateProfileColors("87bc44", "9ae4e8", "000000", "0000ff", "e0ff92");
        waitForResponse();
        assertEquals("87bc44", user.getProfileBackgroundColor());
        assertEquals("9ae4e8", user.getProfileTextColor());
        assertEquals("000000", user.getProfileLinkColor());
        assertEquals("0000ff", user.getProfileSidebarFillColor());
        assertEquals("e0ff92", user.getProfileSidebarBorderColor());
        async1.updateProfileColors("f0f", null, "f0f", null, "0f0");
        waitForResponse();
        assertEquals("f0f", user.getProfileBackgroundColor());
        assertEquals("9ae4e8", user.getProfileTextColor());
        assertEquals("f0f", user.getProfileLinkColor());
        assertEquals("0000ff", user.getProfileSidebarFillColor());
        assertEquals("0f0", user.getProfileSidebarBorderColor());
        async1.updateProfileColors(null, "f00", null, "0ff", null);
        waitForResponse();
        assertEquals("f0f", user.getProfileBackgroundColor());
        assertEquals("f00", user.getProfileTextColor());
        assertEquals("f0f", user.getProfileLinkColor());
        assertEquals("0ff", user.getProfileSidebarFillColor());
        assertEquals("0f0", user.getProfileSidebarBorderColor());
        async1.updateProfileColors("9ae4e8", "000000", "0000ff", "e0ff92", "87bc44");
        waitForResponse();
        assertEquals("9ae4e8", user.getProfileBackgroundColor());
        assertEquals("000000", user.getProfileTextColor());
        assertEquals("0000ff", user.getProfileLinkColor());
        assertEquals("e0ff92", user.getProfileSidebarFillColor());
        assertEquals("87bc44", user.getProfileSidebarBorderColor());
    }

    public void testShow() throws Exception {
        async2.showStatus(1000l);
        waitForResponse();
        assertEquals(52, status.getUser().getId());
        assertDeserializedFormIsEqual(status);
    }

    public void testBlock() throws Exception {
        async2.createBlock(id1.screenName);
        waitForResponse();
        async2.destroyBlock(id1.screenName);
        waitForResponse();

        async1.existsBlock("twit4j2");
        assertFalse(blockExists);
        waitForResponse();
        async1.existsBlock("twit4jblock");
        waitForResponse();
        assertTrue(blockExists);

        async1.getBlockingUsers();
        waitForResponse();
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());
        async1.getBlockingUsers(1);
        waitForResponse();
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());
        async1.getBlockingUsersIDs();
        waitForResponse();
        assertEquals(1, ids.getIDs().length);
        assertEquals(39771963, ids.getIDs()[0]);
    }
    public void testUpdate() throws Exception {
        String date = new java.util.Date().toString() + "test";
        async1.updateStatus(date);
        waitForResponse();
        assertEquals("", date, status.getText());

        long id = status.getId();

        async2.updateStatus("@" + id1.screenName + " " + date, id);
        waitForResponse();
        assertEquals("", "@" + id1.screenName + " " + date, status.getText());
        assertEquals("", id, status.getInReplyToStatusId());
        assertEquals(twitterAPI1.verifyCredentials().getId(), status.getInReplyToUserId());


        id = status.getId();
        this.status = null;
        async2.destroyStatus(id);
        waitForResponse();
        assertEquals("", "@" + id1.screenName + " " + date, status.getText());
        assertDeserializedFormIsEqual(status);
    }

    public void testGetFriendsStatuses() throws Exception {
        users = null;
        async1.getFriendsStatuses(id2.screenName);
        waitForResponse();
        assertNotNull(users);

        users = null;
        async2.getFriendsStatuses();
        waitForResponse();
        assertNotNull(users);
        assertDeserializedFormIsEqual(users);
    }

    public void testFollowers() throws Exception {
        async1.getFollowersStatuses();
        waitForResponse();
        assertTrue(users.size() > 0);

        async2.getFollowersStatuses();
        waitForResponse();
        assertTrue(users.size() > 0);
        assertDeserializedFormIsEqual(users);
    }

    public void testDirectMessages() throws Exception {
        String expectedReturn = new Date() + ":directmessage test";
        async1.sendDirectMessage("twit4jnoupdate", expectedReturn);
        waitForResponse();
        assertEquals(expectedReturn, message.getText());
        async1.getDirectMessages();
        waitForResponse();
        assertTrue(1<= messages.size());
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
        assertEquals(404, te.getStatusCode());

    }

    public void testRateLimitStatus() throws Exception{
        async1.getRateLimitStatus();
        waitForResponse();
        assertTrue(10 < rateLimitStatus.getHourlyLimit());
        assertTrue(10 < rateLimitStatus.getRemainingHits());
    }

    public void testFollowLeave() throws Exception {
        try {
            twitterAPI1.disableNotification("twit4jprotected");
        } catch (TwitterException te) {
        }
        te = null;
        async1.enableNotification("twit4jprotected");
        waitForResponse();
        assertNull(te);
        async1.disableNotification("twit4jprotected");
        waitForResponse();
        assertNull(te);
        assertDeserializedFormIsEqual(user);

    }

    private ResponseList<Status> statuses = null;
    private ResponseList<User> users = null;
    private ResponseList<DirectMessage> messages = null;
    private Status status = null;
    private User user = null;
    private boolean test;
    private UserList userList;
    private PagableResponseList<UserList> userLists;
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
    public void gotDailyTrends(List<Trends> trendsList) {
        this.trendsList = trendsList;
        notifyResponse();
    }
    public void gotWeeklyTrends(List<Trends> trendsList) {
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
    public void gotRetweets(ResponseList<Status> retweets){
        this.statuses = retweets;
        notifyResponse();
    }
    /**
     * @since Twitter4J 2.1.3
     */
    public void gotRetweetedBy(ResponseList<User> users){
        this.users = users;
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.3
     */
    public void gotRetweetedByIDs(IDs ids){
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
     * @since Twitter4J 2.1.7
     */
    public void gotProfileImage(ProfileImage image){
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
    }

    public void updatedUserList(UserList userList) {
        this.userList = userList;
    }

    public void gotUserLists(PagableResponseList<UserList> userLists) {
        this.userLists = userLists;
    }

    public void gotShowUserList(UserList userList) {
        this.userList = userList;
    }

    public void destroyedUserList(UserList userList) {
        this.userList = userList;
    }

    public void gotUserListStatuses(ResponseList<Status> statuses) {
        this.statuses = statuses;
    }

    public void gotUserListMemberships(PagableResponseList<UserList> userLists) {
        this.userLists = userLists;
    }

    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists) {
        this.userLists = userLists;
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
    public void gotFriendsIDs(IDs ids){
        this.ids = ids;
        notifyResponse();
    }

    public void gotFollowersIDs(IDs ids){
        this.ids = ids;
        notifyResponse();
    }

    /*Account Methods*/

    public void gotRateLimitStatus(RateLimitStatus status){
        this.rateLimitStatus = status;
        notifyResponse();
    }

	public void verifiedCredentials(User user) {
        this.user = user;
        notifyResponse();
	}

	public void updatedDeliveryDevice(User user) {
        this.user = user;
        notifyResponse();
    }

    public void updatedProfileColors(User user){
        this.user = user;
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
    public void gotExistsBlock(boolean exists){
        this.blockExists = exists;
        notifyResponse();
    }

    public void gotBlockingUsers(ResponseList<User> blockingUsers){
        this.users = blockingUsers;
        notifyResponse();
    }
    public void gotBlockingUsersIDs(IDs blockingUsersIDs){
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
    public void gotAvailableTrends(ResponseList<Location> locations){
        this.locations = locations;
        notifyResponse();
    }
    /**
     * @param trends trends
     * @since Twitter4J 2.1.1
     */
    public void gotLocationTrends(Trends trends){
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


    public void gotNearByPlaces(ResponseList<Place> places){
        this.places = places;
        notifyResponse();
    }
    public void gotReverseGeoCode(ResponseList<Place> places){
        this.places = places;
        notifyResponse();
    }

    public void gotGeoDetails(Place place){
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
    public void gotTermsOfService(String str){
        notifyResponse();
    }

    /**
     * @since Twitter4J 2.1.7
     */
    public void gotPrivacyPolicy(String str){
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

    private synchronized void notifyResponse(){
        this.notify();
    }

    private synchronized void waitForResponse(){
        try {
            this.wait(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
