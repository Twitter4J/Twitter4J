/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: Twitter4J</p>
 * <p/>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AsyncTwitterTest extends TwitterTestBase implements TwitterListener {

    private AsyncTwitter twitterAPI1 = null;
    private AsyncTwitter twitterAPI2 = null;

    public AsyncTwitterTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        twitterAPI1 = new AsyncTwitter(id1.name, id1.pass);
        twitterAPI2 = new AsyncTwitter(id2.name, id2.pass);

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


    /*User Methods*/
    public void gotUserDetail(User user) {
        this.user = user;
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

    public void deletedUserList(UserList userList) {
        this.userList = userList;
    }

    public void gotUserListStatuses(PagableResponseList<UserList> userLists) {
        this.userLists = userLists;
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

    public void deletedUserListMember(UserList userList) {
        this.userList = userList;
    }

    public void checkedUserListMembership(PagableResponseList<User> users) {
        this.users = users;
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
    //reportSpam()

    /*Saved Searches Methods*/
    //getSavedSearches()
    //showSavedSearch()
    //createSavedSearch()
    //destroySavedSearch()

    /*Local Trends Methods*/

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



    public void testGetPublicTimeline() throws Exception {
        twitterAPI1.getPublicTimelineAsync(this);
        waitForResponse();
        assertTrue("size", 5 < statuses.size());
        assertSerializable(statuses);
    }

    public void testGetFriendsTimeline() throws Exception {
        twitterAPI1.getFriendsTimelineAsync(this);
        waitForResponse();
        assertTrue(statuses.size() > 0);

        assertSerializable(statuses);
    }

    public void testShowUser() throws Exception{
        twitterAPI1.showUserAsync(id1.name,this);
        waitForResponse();
        User user = this.user;
        assertEquals(id1.name, user.getScreenName());
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

    public void testGetUserTimeline_Show() throws Exception {
        twitterAPI2.getUserTimelineAsync(this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(new Paging(999383469l), this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1.name, this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1.name, new Paging(999383469l), this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1.name, new Paging().count(10), this);
        waitForResponse();
        assertTrue("size", 5 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1.name, new Paging(999383469l).count(15), this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI1.getUserTimelineAsync(new Paging(999383469l).count(25), this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        assertSerializable(statuses);
    }
    public void testAccountProfileImageUpdates() throws Exception {
        te = null;
        twitterAPI1.updateProfileImageAsync(TwitterTestUnit.getRandomlyChosenFile(), this);
        waitForResponse();
        assertNull(te);
        // tile randomly
        twitterAPI1.updateProfileBackgroundImageAsync(TwitterTestUnit.getRandomlyChosenFile(),
                (5 < System.currentTimeMillis() % 5), this);
        waitForResponse();
        assertNull(te);
    }


    public void testFavorite() throws Exception {
        Status status = twitterAPI1.updateStatus(new Date().toString());
        twitterAPI2.createFavoriteAsync(status.getId(), this);
        waitForResponse();
        assertEquals(status, this.status);
        this.status = null;
        //need to wait for a second to get it destoryable
        Thread.sleep(5000);
        twitterAPI2.destroyFavoriteAsync(status.getId(), this);
        waitForResponse();
        if(null != te && te.getStatusCode() == 404){
            // sometimes destorying favorite fails with 404
        }else{
            assertEquals(status, this.status);
        }
    }

    public void testSocialGraphMethods() throws Exception {
        twitterAPI1.getFriendsIDsAsync(this);
        waitForResponse();
        int yusukey = 4933401;
        assertIDExsits("twit4j is following yusukey", ids, yusukey);
        int JBossNewsJP = 28074579;
        int RedHatNewsJP = 28074306;
        twitterAPI1.getFriendsIDsAsync(JBossNewsJP, this);
        waitForResponse();
        assertIDExsits("JBossNewsJP is following RedHatNewsJP", ids, RedHatNewsJP);
        twitterAPI1.getFriendsIDsAsync("RedHatNewsJP", this);
        waitForResponse();
        assertIDExsits("RedHatNewsJP is following JBossNewsJP", ids, 28074579);

        try {
            twitterAPI2.createFriendship(id1.name);
        } catch (TwitterException te) {
        }
        twitterAPI1.getFollowersIDsAsync(this);
        waitForResponse();
        assertIDExsits("twit4j2(6377362) is following twit4j(6358482)", ids, 6377362);
        twitterAPI1.getFollowersIDsAsync(28074579, this);
        waitForResponse();
        assertIDExsits("RedHatNewsJP is following JBossNewsJP", ids, 28074306);
        twitterAPI1.getFollowersIDsAsync("JBossNewsJP", this);
        waitForResponse();
        assertIDExsits("RedHatNewsJP is following JBossNewsJP", ids, 28074306);
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
        User original = twitterAPI1.verifyCredentials();

        String newName, newURL, newLocation, newDescription;
        String neu = "new";
        newName = original.getName() + neu;
        newURL = original.getURL() + neu;
        newLocation = original.getLocation()+neu;
        newDescription = original.getDescription()+neu;

        twitterAPI1.updateProfileAsync(
                newName, null, newURL, newLocation, newDescription,this);
        waitForResponse();
        twitterAPI1.updateProfile(original.getName()
                , null, original.getURL().toString(), original.getLocation(), original.getDescription());
        assertEquals(newName, user.getName());
        assertEquals(newURL, user.getURL().toString());
        assertEquals(newLocation, user.getLocation());
        assertEquals(newDescription, user.getDescription());

        twitterAPI1.createFriendshipAsync(id2.name, this);
        waitForResponse();
        twitterAPI1.enableNotificationAsync(id2.name, this);
        waitForResponse();
        twitterAPI2.createFriendshipAsync(id1.name, this);
        waitForResponse();
        twitterAPI1.existsFriendshipAsync(id1.name,id2.name,this);
        waitForResponse();
        assertTrue(exists);

        twitterAPI1.updateProfileColorsAsync("f00", "f0f", "0ff", "0f0", "f0f", this);
        waitForResponse();
        assertEquals("f00", user.getProfileBackgroundColor());
        assertEquals("f0f", user.getProfileTextColor());
        assertEquals("0ff", user.getProfileLinkColor());
        assertEquals("0f0", user.getProfileSidebarFillColor());
        assertEquals("f0f", user.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync("f0f", "f00", "f0f", "0ff", "0f0", this);
        waitForResponse();
        assertEquals("f0f", user.getProfileBackgroundColor());
        assertEquals("f00", user.getProfileTextColor());
        assertEquals("f0f", user.getProfileLinkColor());
        assertEquals("0ff", user.getProfileSidebarFillColor());
        assertEquals("0f0", user.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync("87bc44", "9ae4e8", "000000", "0000ff", "e0ff92", this);
        waitForResponse();
        assertEquals("87bc44", user.getProfileBackgroundColor());
        assertEquals("9ae4e8", user.getProfileTextColor());
        assertEquals("000000", user.getProfileLinkColor());
        assertEquals("0000ff", user.getProfileSidebarFillColor());
        assertEquals("e0ff92", user.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync("f0f", null, "f0f", null, "0f0", this);
        waitForResponse();
        assertEquals("f0f", user.getProfileBackgroundColor());
        assertEquals("9ae4e8", user.getProfileTextColor());
        assertEquals("f0f", user.getProfileLinkColor());
        assertEquals("0000ff", user.getProfileSidebarFillColor());
        assertEquals("0f0", user.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync(null, "f00", null, "0ff", null, this);
        waitForResponse();
        assertEquals("f0f", user.getProfileBackgroundColor());
        assertEquals("f00", user.getProfileTextColor());
        assertEquals("f0f", user.getProfileLinkColor());
        assertEquals("0ff", user.getProfileSidebarFillColor());
        assertEquals("0f0", user.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync("9ae4e8", "000000", "0000ff", "e0ff92", "87bc44", this);
        waitForResponse();
        assertEquals("9ae4e8", user.getProfileBackgroundColor());
        assertEquals("000000", user.getProfileTextColor());
        assertEquals("0000ff", user.getProfileLinkColor());
        assertEquals("e0ff92", user.getProfileSidebarFillColor());
        assertEquals("87bc44", user.getProfileSidebarBorderColor());
    }

    public void testShow() throws Exception {
        twitterAPI2.showStatusAsync(1000l, this);
        waitForResponse();
        assertEquals(52, status.getUser().getId());
        assertSerializable(status);
    }

    public void testBlock() throws Exception {
        twitterAPI2.createBlockAsync(id1.name,this);
        waitForResponse();
        twitterAPI2.destroyBlockAsync(id1.name,this);
        waitForResponse();

        twitterAPI1.existsBlockAsync("twit4j2", this);
        assertFalse(blockExists);
        waitForResponse();
        twitterAPI1.existsBlockAsync("twit4jblock", this);
        waitForResponse();
        assertTrue(blockExists);

        twitterAPI1.getBlockingUsersAsync(this);
        waitForResponse();
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());
        twitterAPI1.getBlockingUsersAsync(1,this);
        waitForResponse();
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());
        twitterAPI1.getBlockingUsersIDsAsync(this);
        waitForResponse();
        assertEquals(1, ids.getIDs().length);
        assertEquals(39771963, ids.getIDs()[0]);
    }
    public void testUpdate() throws Exception {
        String date = new java.util.Date().toString() + "test";
        twitterAPI1.updateStatusAsync(date, this);
        waitForResponse();
        assertEquals("", date, status.getText());

        long id = status.getId();

        twitterAPI2.updateStatusAsync("@" + id1.name + " " + date, id, this);
        waitForResponse();
        assertEquals("", "@" + id1.name + " " + date, status.getText());
        assertEquals("", id, status.getInReplyToStatusId());
        assertEquals(twitterAPI1.verifyCredentials().getId(), status.getInReplyToUserId());


        id = status.getId();
        this.status = null;
        twitterAPI2.destroyStatusAsync(id, this);
        waitForResponse();
        assertEquals("", "@" + id1.name + " " + date, status.getText());
        assertSerializable(status);
    }

    public void testGetFriendsStatuses() throws Exception {
        users = null;
        twitterAPI1.getFriendsStatusesAsync(id2.name, this);
        waitForResponse();
        assertNotNull(users);

        users = null;
        twitterAPI2.getFriendsStatusesAsync(this);
        waitForResponse();
        assertNotNull(users);
        assertSerializable(users);
    }

    public void testFollowers() throws Exception {
        twitterAPI1.getFollowersStatusesAsync(this);
        waitForResponse();
        assertTrue(users.size() > 0);

        twitterAPI2.getFollowersStatusesAsync(this);
        waitForResponse();
        assertTrue(users.size() > 0);
        assertSerializable(users);
    }

    public void testDirectMessages() throws Exception {
        String expectedReturn = new Date() + ":directmessage test";
        twitterAPI1.sendDirectMessageAsync("twit4jnoupdate", expectedReturn, this);
        waitForResponse();
        assertEquals(expectedReturn, message.getText());
        twitterAPI1.getDirectMessagesAsync(this);
        waitForResponse();
        assertTrue(1<= messages.size());
    }

    public void testCreateDestroyFriend() throws Exception {
        twitterAPI2.destroyFriendshipAsync(id1.name, this);
        waitForResponse();

//        twitterAPI2.destroyFriendshipAsync(id1.name, this);
//        waitForResponse();
//        assertEquals(403, te.getStatusCode());
        twitterAPI2.createFriendshipAsync(id1.name, true, this);
        // the Twitter API is not returning appropriate notifications value
        // http://code.google.com/p/twitter-api/issues/detail?id=474
//        user detail = twitterAPI2.showUser(id1.name);
//        assertTrue(detail.isNotificationEnabled());
        waitForResponse();
        assertEquals(id1.name, user.getScreenName());

//        te = null;
//        twitterAPI2.createFriendshipAsync(id2.name, this);
//        waitForResponse();
//        assertEquals(403, te.getStatusCode());
        te = null;
        twitterAPI2.createFriendshipAsync("doesnotexist--", this);
        waitForResponse();
        //now befriending with non-existing user returns 404
        //http://groups.google.com/group/twitter-development-talk/browse_thread/thread/bd2a912b181bc39f
        assertEquals(404, te.getStatusCode());

    }

    public void testRateLimitStatus() throws Exception{
        twitterAPI1.getRateLimitStatusAsync(this);
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
        twitterAPI1.enableNotificationAsync("twit4jprotected", this);
        waitForResponse();
        assertNull(te);
        twitterAPI1.disableNotificationAsync("twit4jprotected", this);
        waitForResponse();
        assertNull(te);
        assertSerializable(user);

    }

    public void testSearchAsync() throws Exception {
        String queryStr = "test source:twitter4j";
        Query query = new Query(queryStr);
        twitterAPI1.searchAsync(query, this);
        waitForResponse();
        assertTrue("sinceId", -1 != queryResult.getSinceId());
        assertTrue(1265204883 < queryResult.getMaxId());
        assertTrue(-1 != queryResult.getRefreshUrl().indexOf(queryStr));
        assertEquals(15, queryResult.getResultsPerPage());
        assertTrue(0 < queryResult.getCompletedIn());
        assertEquals(1, queryResult.getPage());
        assertEquals(queryStr, queryResult.getQuery());

        List<Tweet> tweets = queryResult.getTweets();
        assertTrue(1 <= tweets.size());
        assertNotNull(tweets.get(0).getCreatedAt());
        assertNotNull("from user", tweets.get(0).getFromUser());
        assertTrue("fromUserId", -1 != tweets.get(0).getFromUserId());
        assertTrue(-1 != tweets.get(0).getId());
//        assertNotNull(tweets.get(0).getIsoLanguageCode());
        assertTrue(-1 != tweets.get(0).getProfileImageUrl().indexOf(".jpg") || -1 != tweets.get(0).getProfileImageUrl().indexOf(".png"));
        assertTrue(-1 != tweets.get(0).getSource().indexOf("twitter"));
    }

    public void testTrendsAsync() throws Exception {
        this.twitterAPI1.getTrendsAsync(this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertEquals(10, trends.getTrends().length);
        for (int i = 0; i < 10; i++) {
            assertNotNull(trends.getTrends()[i].getName());
            assertNotNull(trends.getTrends()[i].getUrl());
            assertNull(trends.getTrends()[i].getQuery());
        }

        twitterAPI1.getCurrentTrendsAsync(this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertEquals(10, trends.getTrends().length);
        for (Trend trend : trends.getTrends()) {
            assertNotNull(trend.getName());
            assertNull(trend.getUrl());
            assertNotNull(trend.getQuery());
        }

        twitterAPI1.getCurrentTrendsAsync(true, this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        Trend[] trendArray = trends.getTrends();
        assertEquals(10, trendArray.length);
        for (Trend trend : trends.getTrends()) {
            assertNotNull(trend.getName());
            assertNull(trend.getUrl());
            assertNotNull(trend.getQuery());
        }


        twitterAPI1.getDailyTrendsAsync(this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrue(20 < trendsList.size());
        assertTrends(trendsList, 20);

        twitterAPI1.getDailyTrendsAsync(new Date(), true, this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrue(0 <= trendsList.size());
        assertTrends(trendsList, 20);

        twitterAPI1.getWeeklyTrendsAsync(this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertEquals(7, trendsList.size());
        assertTrends(trendsList, 30);

        twitterAPI1.getWeeklyTrendsAsync(new Date(), true, this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrue(1 <= trendsList.size());
        assertTrends(trendsList, 30);
    }

    private void assertTrends(List<Trends> trendsArray, int expectedSize) throws Exception{
        Date trendAt = null;
         for(Trends singleTrends : trendsArray){
             assertEquals(expectedSize, singleTrends.getTrends().length);
             if(null != trendAt){
                 assertTrue(trendAt.before(singleTrends.getTrendAt()));
             }
             trendAt = singleTrends.getTrendAt();
             for (int i = 0; i < singleTrends.getTrends().length; i++) {
                 assertNotNull(singleTrends.getTrends()[i].getName());
                 assertNull(singleTrends.getTrends()[i].getUrl());
                 assertNotNull(singleTrends.getTrends()[i].getQuery());
             }
         }
    }

    private void assertSerializable(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        oos.writeObject(obj);
    }
}
