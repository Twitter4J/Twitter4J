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

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * <p>Title: Twitter4J</p>
 * <p/>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AsyncTwitterTest extends TestCase implements TwitterListener {
    private List<Status> statuses = null;
    private List<User> users = null;
    private List<DirectMessage> messages = null;
    private Status status = null;
    private User user = null;
    private User extendedUser = null;
    private List<User> extendedUsers = null;
    private boolean test;
    private String schedule;
    private DirectMessage message = null;
    private TwitterException te = null;
    private RateLimitStatus rateLimitStatus;
    private boolean exists;
    private QueryResult queryResult;
    private IDs ids;
    private List<Trends> trendsList;
    private Trends trends;
    private boolean blockExists;

    public void gotPublicTimeline(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotHomeTimeline(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotFriendsTimeline(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotUserTimeline(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotShow(Status status) {
        this.status = status;
        notifyResponse();
    }

    public void gotShowStatus(Status status) {
        this.status = status;
        notifyResponse();
    }

    public void updated(Status status) {
        this.status = status;
        notifyResponse();
    }

    public void updatedStatus(Status status) {
        this.status = status;
        notifyResponse();
    }

    public void gotReplies(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotMentions(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotRetweetedByMe(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotRetweetedToMe(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void gotRetweetsOfMe(List<Status> statuses) {
        this.statuses = statuses;
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

    public void gotFriends(List<User> users) {
        this.users = users;
        notifyResponse();
    }

    public void gotFollowers(List<User> users) {
        this.users = users;
        notifyResponse();
    }

    public void gotFeatured(List<User> users) {
        this.users = users;
        notifyResponse();
    }

    public void gotUserDetail(User extendedUser) {
        this.extendedUser = extendedUser;
        notifyResponse();
    }

    public void gotDirectMessages(List<DirectMessage> messages) {
        this.messages = messages;
        notifyResponse();
    }

    public void gotSentDirectMessages(List<DirectMessage> messages) {
        this.messages = messages;
        notifyResponse();
    }

    public void sentDirectMessage(DirectMessage message) {
        this.message = message;
        notifyResponse();
    }

    public void deletedDirectMessage(DirectMessage message) {
        this.message = message;
        notifyResponse();
    }

    public void destroyedDirectMessage(DirectMessage message) {
        this.message = message;
        notifyResponse();
    }

    public void gotFriendsIDs(IDs ids){
        this.ids = ids;
        notifyResponse();
    }

    public void gotFollowersIDs(IDs ids){
        this.ids = ids;
        notifyResponse();
    }

    public void created(User user) {
        this.user = user;
        notifyResponse();
    }

    public void createdFriendship(User user) {
        this.user = user;
        notifyResponse();
    }

    public void destroyed(User user) {
        this.user = user;
        notifyResponse();
    }

    public void destroyedFriendship(User user) {
        this.user = user;
        notifyResponse();
    }

    public void gotExists(boolean exists) {
        this.exists = exists;
        notifyResponse();
    }

    public void gotExistsFriendship(boolean exists) {
        this.exists = exists;
        notifyResponse();
    }

    public void updatedLocation(User user) {
        this.user = user;
        notifyResponse();
    }
    public void updatedProfile(User user) {
        this.extendedUser = user;
        notifyResponse();
    }

    public void updatedProfileColors(User extendedUser){
        this.extendedUser = extendedUser;
        notifyResponse();
    }

    public void gotRateLimitStatus(RateLimitStatus status){
        this.rateLimitStatus = status;
        notifyResponse();
    }

    public void updatedDeliverlyDevice(User user) {
        this.user = user;
        notifyResponse();
    }

    public void gotFavorites(List<Status> statuses) {
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

    public void followed(User user) {
        this.user = user;
        notifyResponse();
    }

    public void enabledNotification(User user) {
        this.user = user;
        notifyResponse();
    }

    public void left(User user) {
        this.user = user;
        notifyResponse();
    }

    public void disabledNotification(User user) {
        this.user = user;
        notifyResponse();
    }

    public void blocked(User user) {
        this.user = user;
        notifyResponse();
    }

    public void createdBlock(User user) {
        this.user = user;
        notifyResponse();
    }

    public void unblocked(User user) {
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

    public void gotBlockingUsers(List<User> blockingUsers){
        this.extendedUsers = blockingUsers;
        notifyResponse();
    }
    public void gotBlockingUsersIDs(IDs blockingUsersIDs){
        this.ids = blockingUsersIDs;
        notifyResponse();
    }

    public void tested(boolean test) {
        this.test = test;
        notifyResponse();
    }

    public void gotDowntimeSchedule(String schedule) {
        this.schedule = schedule;
        notifyResponse();
    }
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

    /**
     * @param te     TwitterException
     * @param method int
     */

    public void onException(TwitterException te, int method) {
        this.te = te;
        System.out.println("onexception");
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

    private AsyncTwitter twitterAPI1 = null;
    private AsyncTwitter twitterAPI2 = null;

    public AsyncTwitterTest(String name) {
        super(name);
    }

    String id1, id2, pass1, pass2;

    protected void setUp() throws Exception {
        super.setUp();
        Properties p = new Properties();
        p.load(new FileInputStream("test.properties"));
        id1 = p.getProperty("id1");
        id2 = p.getProperty("id2");
        pass1 = p.getProperty("pass1");
        pass2 = p.getProperty("pass2");
        twitterAPI1 = new AsyncTwitter(id1, pass1);
//        twitterAPI1.setRetryCount(5);
//        twitterAPI1.setRetryIntervalSecs(5);
        twitterAPI2 = new AsyncTwitter(id2, pass2);
//        twitterAPI2.setRetryCount(5);
//        twitterAPI2.setRetryIntervalSecs(5);
        statuses = null;
        users = null;
        messages = null;
        status = null;
        user = null;
        extendedUser = null;
        message = null;
        te = null;

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPublicTimeline() throws Exception {
        twitterAPI1.getPublicTimelineAsync(this);
        waitForResponse();
        assertTrue("size", 5 < statuses.size());
        trySerializable(statuses);
    }

    public void testGetFriendsTimeline() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String dateStr = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE);

        String id1status = dateStr + ":id1";
        String id2status = dateStr + ":id2";
        twitterAPI1.updateStatusAsync(id1status, this);
        waitForResponse();
        assertEquals(id1status, status.getText());
        twitterAPI2.updateStatusAsync(id2status, this);
        waitForResponse();
        assertEquals(id2status, status.getText());

        twitterAPI1.getFriendsTimelineAsync(this);
        waitForResponse();
        assertTrue(statuses.size() > 0);

        trySerializable(statuses);
    }

    public void testShowUser() throws Exception{
        twitterAPI1.showUserAsync(id1,this);
        waitForResponse();
        User uws = this.extendedUser;
        assertEquals(id1, uws.getName());
        assertTrue(0 <= uws.getFavouritesCount());
        assertTrue(0 <= uws.getFollowersCount());
        assertTrue(0 <= uws.getFriendsCount());
        assertTrue(0 <= uws.getStatusesCount());
        assertNotNull(uws.getProfileBackgroundColor());
        assertNotNull(uws.getProfileTextColor());
        assertNotNull(uws.getProfileLinkColor());
        assertNotNull(uws.getProfileSidebarBorderColor());
        assertNotNull(uws.getProfileSidebarFillColor());
        assertNotNull(uws.getProfileTextColor());

        this.extendedUser = null;
        twitterAPI1.getAuthenticatedUserAsync(this);
        waitForResponse();
        assertEquals(id1, uws.getName());
        assertTrue(0 <= uws.getFavouritesCount());
        assertTrue(0 <= uws.getFollowersCount());
        assertTrue(0 <= uws.getFriendsCount());
        assertTrue(0 <= uws.getStatusesCount());
        assertNotNull(uws.getProfileBackgroundColor());
        assertNotNull(uws.getProfileTextColor());
        assertNotNull(uws.getProfileLinkColor());
        assertNotNull(uws.getProfileSidebarBorderColor());
        assertNotNull(uws.getProfileSidebarFillColor());
        assertNotNull(uws.getProfileTextColor());
    }

    public void testGetUserTimeline_Show() throws Exception {
        twitterAPI2.getUserTimelineAsync(this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(new Paging(999383469l),this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1, this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1,new Paging(999383469l), this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1,new Paging().count(10), this);
        waitForResponse();
        assertEquals("size", 10, statuses.size());
        twitterAPI2.getUserTimelineAsync(id1, new Paging(999383469l).count(15), this);
        waitForResponse();
        assertEquals("size", 15, statuses.size());
        twitterAPI1.getUserTimelineAsync(new Paging(999383469l).count(25), this);
        waitForResponse();
        assertEquals("size", 25, statuses.size());

        trySerializable(statuses);

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
        try {
            twitterAPI1.createFriendship(id2);
        } catch (twitter4j.TwitterException te) {
        }
        try {
            twitterAPI2.createFriendship(id1);
        } catch (twitter4j.TwitterException te) {
        }
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

        twitterAPI1.getFollowersIDsAsync(this);
        waitForResponse();
        assertIDExsits("twit4j2 is following twit2j", ids, 6377362);
        twitterAPI1.getFollowersIDsAsync(28074579, this);
        waitForResponse();
        assertIDExsits("RedHatNewsJP is following JBossNewsJP", ids, 28074306);
        twitterAPI1.getFollowersIDsAsync("JBossNewsJP", this);
        waitForResponse();
        assertIDExsits("RedHatNewsJP is following JBossNewsJP", ids, 28074306);
    }

    private void assertIDExsits(String assertion, IDs ids, int idToFind) {
        boolean found = false;
        for (int id : ids.getIDs()) {
            if (id == idToFind) {
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
        assertEquals(newName, extendedUser.getName());
        assertEquals(newURL, extendedUser.getURL().toString());
        assertEquals(newLocation, extendedUser.getLocation());
        assertEquals(newDescription, extendedUser.getDescription());

        twitterAPI1.createFriendshipAsync(id2);
        twitterAPI2.createFriendshipAsync(id1);
        twitterAPI1.existsFriendshipAsync(id1,id2,this);
        waitForResponse();
        assertTrue(exists);

        twitterAPI1.updateProfileColorsAsync("f00", "f0f", "0ff", "0f0", "f0f", this);
        waitForResponse();
        assertEquals("f00", extendedUser.getProfileBackgroundColor());
        assertEquals("f0f", extendedUser.getProfileTextColor());
        assertEquals("0ff", extendedUser.getProfileLinkColor());
        assertEquals("0f0", extendedUser.getProfileSidebarFillColor());
        assertEquals("f0f", extendedUser.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync("f0f", "f00", "f0f", "0ff", "0f0", this);
        waitForResponse();
        assertEquals("f0f", extendedUser.getProfileBackgroundColor());
        assertEquals("f00", extendedUser.getProfileTextColor());
        assertEquals("f0f", extendedUser.getProfileLinkColor());
        assertEquals("0ff", extendedUser.getProfileSidebarFillColor());
        assertEquals("0f0", extendedUser.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync("87bc44", "9ae4e8", "000000", "0000ff", "e0ff92", this);
        waitForResponse();
        assertEquals("87bc44", extendedUser.getProfileBackgroundColor());
        assertEquals("9ae4e8", extendedUser.getProfileTextColor());
        assertEquals("000000", extendedUser.getProfileLinkColor());
        assertEquals("0000ff", extendedUser.getProfileSidebarFillColor());
        assertEquals("e0ff92", extendedUser.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync("f0f", null, "f0f", null, "0f0", this);
        waitForResponse();
        assertEquals("f0f", extendedUser.getProfileBackgroundColor());
        assertEquals("9ae4e8", extendedUser.getProfileTextColor());
        assertEquals("f0f", extendedUser.getProfileLinkColor());
        assertEquals("0000ff", extendedUser.getProfileSidebarFillColor());
        assertEquals("0f0", extendedUser.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync(null, "f00", null, "0ff", null, this);
        waitForResponse();
        assertEquals("f0f", extendedUser.getProfileBackgroundColor());
        assertEquals("f00", extendedUser.getProfileTextColor());
        assertEquals("f0f", extendedUser.getProfileLinkColor());
        assertEquals("0ff", extendedUser.getProfileSidebarFillColor());
        assertEquals("0f0", extendedUser.getProfileSidebarBorderColor());
        twitterAPI1.updateProfileColorsAsync("9ae4e8", "000000", "0000ff", "e0ff92", "87bc44", this);
        waitForResponse();
        assertEquals("9ae4e8", extendedUser.getProfileBackgroundColor());
        assertEquals("000000", extendedUser.getProfileTextColor());
        assertEquals("0000ff", extendedUser.getProfileLinkColor());
        assertEquals("e0ff92", extendedUser.getProfileSidebarFillColor());
        assertEquals("87bc44", extendedUser.getProfileSidebarBorderColor());
    }

    public void testShow() throws Exception {
        twitterAPI2.showStatusAsync(1000l, this);
        waitForResponse();
        assertEquals(52, status.getUser().getId());
        trySerializable(status);
    }

    public void testBlock() throws Exception {
        twitterAPI2.createBlockAsync(id1,this);
        waitForResponse();
        twitterAPI2.destroyBlockAsync(id1,this);
        waitForResponse();

        twitterAPI1.existsBlockAsync("twit4j2", this);
        assertFalse(blockExists);
        waitForResponse();
        twitterAPI1.existsBlockAsync("twit4jblock", this);
        waitForResponse();
        assertTrue(blockExists);

        twitterAPI1.getBlockingUsersAsync(this);
        waitForResponse();
        assertEquals(1, extendedUsers.size());
        assertEquals(39771963, extendedUsers.get(0).getId());
        twitterAPI1.getBlockingUsersAsync(1,this);
        waitForResponse();
        assertEquals(1, extendedUsers.size());
        assertEquals(39771963, extendedUsers.get(0).getId());
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

        twitterAPI2.updateStatusAsync("@" + id1 + " " + date, id, this);
        waitForResponse();
        assertEquals("", "@" + id1 + " " + date, status.getText());
        assertEquals("", id, status.getInReplyToStatusId());
        assertEquals(twitterAPI1.verifyCredentials().getId(), status.getInReplyToUserId());


        id = status.getId();
        this.status = null;
        twitterAPI2.destroyStatusAsync(id, this);
        waitForResponse();
        assertEquals("", "@" + id1 + " " + date, status.getText());
        trySerializable(status);
    }

    public void testGetFriendsStatuses() throws Exception {
        users = null;
        twitterAPI1.getFriendsStatusesAsync(id2, this);
        waitForResponse();
        assertNotNull(users);
        users = null;
        twitterAPI2.getFriendsStatusesAsync(this);
        waitForResponse();
        assertNotNull(users);
        trySerializable(users);
    }

    public void testFollowers() throws Exception {
        twitterAPI1.getFollowersStatusesAsync(this);
        waitForResponse();
        assertTrue(users.size() > 0);

        twitterAPI2.getFollowersStatusesAsync(this);
        waitForResponse();
        assertTrue(users.size() > 0);
        trySerializable(users);
    }

    public void testFeatured() throws Exception {
        twitterAPI1.getFeaturedAsync(this);
        waitForResponse();
        assertTrue(users.size() > 9);
        trySerializable(users);
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
        twitterAPI2.destroyFriendshipAsync(id1, this);
        waitForResponse();

        twitterAPI2.createFriendshipAsync(id1, true, this);
        waitForResponse();
        assertEquals(id1, user.getName());

        te = null;
        twitterAPI2.createFriendshipAsync("doesnotexist--", this);
        waitForResponse();
        //now befriending with non-existing user returns 404
        //http://groups.google.com/group/twitter-development-talk/browse_thread/thread/bd2a912b181bc39f
        assertEquals(404, te.getStatusCode());

    }

    public void testRateLimitStatus() throws Exception{
        twitterAPI1.rateLimitStatusAsync(this);
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
        trySerializable(user);

    }
    public void testSearchAsync() throws Exception {
        String queryStr = "test source:twitter4j";
        Query query = new Query(queryStr);
        twitterAPI1.searchAcync(query, this);
        waitForResponse();
        assertTrue("sinceId", -1 != queryResult.getSinceId());
        assertTrue(1265204883 < queryResult.getMaxId());
        assertTrue(-1 != queryResult.getRefreshUrl().indexOf(queryStr));
        assertEquals(15, queryResult.getResultsPerPage());
        assertTrue(0 < queryResult.getCompletedIn());
        assertEquals(1, queryResult.getPage());
        assertEquals(queryStr, queryResult.getQuery());

        List<Tweet> tweets = queryResult.getTweets();
        assertTrue(1<=tweets.size());
        assertNotNull(tweets.get(0).getText());
        assertNull(tweets.get(0).getToUser());
        assertEquals(-1, tweets.get(0).getToUserId());
        assertNotNull(tweets.get(0).getCreatedAt());
        assertNotNull("from user", tweets.get(0).getFromUser());
        assertTrue("fromUserId", -1 != tweets.get(0).getFromUserId());
        assertTrue(-1 !=  tweets.get(0).getId());
//        assertNotNull(tweets.get(0).getIsoLanguageCode());
        assertTrue(-1 != tweets.get(0).getProfileImageUrl().indexOf(".jpg") ||-1 != tweets.get(0).getProfileImageUrl().indexOf(".png") );
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
        assertTrends(trendsList, 20);

        twitterAPI1.getDailyTrendsAsync(new Date(), true, this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrends(trendsList, 20);

        twitterAPI1.getWeeklyTrendsAsync(this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrends(trendsList, 30);

        twitterAPI1.getWeeklyTrendsAsync(new Date(), true, this);
        waitForResponse();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
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

    private void trySerializable(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        oos.writeObject(obj);
    }
}
