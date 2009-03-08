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
 * <p>Description: </p>
 */
public class AsyncTwitterTestUnit extends TestCase implements TwitterListener {
    private List<Status> statuses = null;
    private List<User> users = null;
    private List<DirectMessage> messages = null;
    private Status status = null;
    private User user = null;
    private UserWithStatus userWithStatus = null;
    private boolean test;
    private String schedule;
    private DirectMessage message = null;
    private TwitterException te = null;
    private RateLimitStatus rateLimitStatus;
    private boolean exists;
    private QueryResult queryResult;

    public void gotPublicTimeline(List<Status> statuses) {
        this.statuses = statuses;
    }

    public void gotFriendsTimeline(List<Status> statuses) {
        this.statuses = statuses;
    }

    public void gotUserTimeline(List<Status> statuses) {
        this.statuses = statuses;
    }

    public void gotShow(Status status) {
        this.status = status;
    }

    public void updated(Status status) {
        this.status = status;
    }

    public void gotReplies(List<Status> statuses) {
        this.statuses = statuses;
    }

    public void destroyedStatus(Status destroyedStatus) {
        this.status = destroyedStatus;
    }

    public void gotFriends(List<User> users) {
        this.users = users;
    }

    public void gotFollowers(List<User> users) {
        this.users = users;
    }

    public void gotFeatured(List<User> users) {
        this.users = users;
    }

    public void gotUserDetail(UserWithStatus userWithStatus) {
        this.userWithStatus = userWithStatus;
    }

    public void gotDirectMessages(List<DirectMessage> messages) {
        this.messages = messages;
    }

    public void gotSentDirectMessages(List<DirectMessage> messages) {
        this.messages = messages;
    }

    public void sentDirectMessage(DirectMessage message) {
        this.message = message;
    }

    public void deletedDirectMessage(DirectMessage message) {
        this.message = message;
    }

    public void created(User user) {
        this.user = user;
    }

    public void destroyed(User user) {
        this.user = user;
    }

    public void gotExists(boolean exists) {
        this.exists = exists;
    }

    public void updatedLocation(User user) {
        this.user = user;
    }
    public void gotRateLimitStatus(RateLimitStatus status){
        this.rateLimitStatus = status;
    }

    public void updatedDeliverlyDevice(User user) {
        this.user = user;
    }

    public void gotFavorites(List<Status> statuses) {
        this.statuses = statuses;
    }

    public void createdFavorite(Status status) {
        this.status = status;
    }

    public void destroyedFavorite(Status status) {
        this.status = status;
    }

    public void followed(User user) {
        this.user = user;
    }

    public void left(User user) {
        this.user = user;
    }

    public void blocked(User user) {
        this.user = user;
    }

    public void unblocked(User user) {
        this.user = user;
    }

    public void tested(boolean test) {
        this.test = test;
    }

    public void gotDowntimeSchedule(String schedule) {
        this.schedule = schedule;
    }
    public void searched(QueryResult result) {
        this.queryResult = result;
    }

    /**
     * @param te     TwitterException
     * @param method int
     */

    public void onException(TwitterException te, int method) {
        System.out.println("Got exception:" + te.getMessage());
        this.te = te;
    }

    private AsyncTwitter twitterAPI1 = null;
    private AsyncTwitter twitterAPI2 = null;

    public AsyncTwitterTestUnit(String name) {
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
        twitterAPI1.setRetryCount(3);
        twitterAPI1.setRetryIntervalSecs(10);
        twitterAPI2 = new AsyncTwitter(id2, pass2);
        twitterAPI2.setRetryCount(3);
        twitterAPI2.setRetryIntervalSecs(10);
        statuses = null;
        users = null;
        messages = null;
        status = null;
        user = null;
        userWithStatus = null;
        message = null;
        te = null;

    }

    protected void tearDown() throws Exception {
        twitterAPI1 = null;
        super.tearDown();
    }

    public void testGetPublicTimeline() throws Exception {
        twitterAPI1.getPublicTimelineAsync(this);
        Thread.sleep(5000);
        assertTrue("size", 5 < statuses.size());
        trySerializable(statuses);
    }

    public void testGetFriendsTimeline() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String dateStr = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE);

        String id1status = dateStr + ":id1";
        String id2status = dateStr + ":id2";
        twitterAPI1.updateAsync(id1status, this);
        Thread.sleep(5000);
        assertEquals(id1status, status.getText());
        Thread.sleep(3000);
        twitterAPI2.updateAsync(id2status, this);
        Thread.sleep(3000);
        assertEquals(id2status, status.getText());

        twitterAPI1.getFriendsTimelineAsync(this);
        Thread.sleep(3000);
        assertTrue(statuses.size() > 0);

        twitterAPI1.getFriendsTimelineAsync(new Date(0), this);
        Thread.sleep(3000);
        assertTrue(statuses.size() > 0);

        twitterAPI2.getFriendsTimelineAsync(id1, this);
        Thread.sleep(3000);
        assertTrue(statuses.size() > 0);

        twitterAPI1.getFriendsTimelineAsync(id2, new Date(0), this);
        Thread.sleep(3000);
        assertTrue("size", 5 < statuses.size());
        trySerializable(statuses);
    }

    public void testGetUserDetail() throws Exception{
        twitterAPI1.getUserDetailAsync(id1,this);
        Thread.sleep(3000);
        UserWithStatus uws = this.userWithStatus;
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

        this.userWithStatus = null;
        twitterAPI1.getAuthenticatedUserAsync(this);
        Thread.sleep(3000);
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
        twitterAPI2.getUserTimelineAsync(id1, this);
        Thread.sleep(3000);
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1, 10, this);
        Thread.sleep(3000);
        assertEquals("size", 10, statuses.size());
        twitterAPI1.getUserTimelineAsync(15, new Date(0), this);
        Thread.sleep(3000);
        //        System.out.println("lastURL:"+twitterAPI1.lastURL);
        assertTrue("size", 5 < statuses.size());
        twitterAPI1.getUserTimelineAsync(id1, new Date(0), this);
        Thread.sleep(3000);
        assertTrue("size", 5 < statuses.size());
        twitterAPI1.getUserTimelineAsync(id1, 20, new Date(0), this);
        Thread.sleep(3000);
        assertTrue("size", 5 < statuses.size());
        trySerializable(statuses);
    }

    public void testFavorite() throws Exception {
        Status status = twitterAPI1.update(new Date().toString());
        twitterAPI2.createFavoriteAsync(status.getId(), this);
        Thread.sleep(3000);
        assertEquals(status, this.status);
        this.status = null;
        twitterAPI2.destroyFavoriteAsync(status.getId(), this);
        Thread.sleep(3000);
        assertEquals(status, this.status);
    }
    public void testAccountMethods() throws Exception{
        twitterAPI1.createAsync(id2);
        twitterAPI1.followAsync(id2);
        twitterAPI2.createAsync(id1);
        twitterAPI2.followAsync(id1);
        twitterAPI1.existsAsync(id1,id2,this);
        Thread.sleep(3000);
        assertTrue(exists);
    }

    public void testShow() throws Exception {
        twitterAPI2.showAsync(1000l, this);
        Thread.sleep(3000);
        assertEquals(52, status.getUser().getId());
        trySerializable(status);
    }

    public void testUpdate() throws Exception {
        String date = new java.util.Date().toString() + "test";
        twitterAPI1.updateAsync(date, this);
        Thread.sleep(3000);
        assertEquals("", date, status.getText());

        long id = status.getId();

        twitterAPI2.updateAsync(date,id, this);
        Thread.sleep(3000);
        assertEquals("", date, status.getText());
        assertEquals("", id, status.getInReplyToStatusId());
        assertEquals(twitterAPI1.getAuthenticatedUser().getId(), status.getInReplyToUserId());


        id = status.getId();
        this.status = null;
        twitterAPI2.destroyStatusAsync(id, this);
        Thread.sleep(3000);
        assertEquals("", date, status.getText());
        trySerializable(status);
    }

    public void testGetFriends() throws Exception {
        twitterAPI1.getFriendsAsync(id2, this);
        Thread.sleep(3000);
        boolean found = false;
        for (User user : users) {
            found = found || user.getName().equals(id1);
        }
        assertTrue(found);

        twitterAPI2.getFriendsAsync(this);
        Thread.sleep(3000);
        found = false;
        for (User user : users) {
            found = found || user.getName().equals(id1);
        }
        assertTrue(found);
        trySerializable(users);
    }

    public void testFollowers() throws Exception {
        twitterAPI1.getFollowersAsync(this);
        Thread.sleep(3000);
        assertTrue(users.size() > 0);

        twitterAPI2.getFollowersAsync(this);
        Thread.sleep(3000);
        assertTrue(users.size() > 0);
        trySerializable(users);
    }

    public void testFeatured() throws Exception {
        twitterAPI1.getFeaturedAsync(this);
        Thread.sleep(3000);
        assertTrue(users.size() > 9);
        trySerializable(users);
    }

    public void testGetDirectMessages() throws Exception {
        try {
            twitterAPI2.create(id1);
        } catch (TwitterException ignore) {
        }
        try {
            twitterAPI1.create(id2);
        } catch (TwitterException ignore) {
        }

        String expectedReturn = new Date() + ":directmessage test";
        twitterAPI2.sendDirectMessageAsync(id1, expectedReturn, this);
        Thread.sleep(10000);
//        twitterAPI2.sendDirectMessage("yusukey",expectedReturn);
        twitterAPI1.getDirectMessagesAsync(this);
        Thread.sleep(3000);
        assertEquals("", expectedReturn, messages.get(0).getText());
//        String expectedReturn = new Date()+":directmessage test";
        twitterAPI1.sendDirectMessageAsync(id2, expectedReturn, this);
        Thread.sleep(5000);
        assertEquals("", expectedReturn, message.getText());
        twitterAPI2.getDirectMessagesAsync(new Date(System.currentTimeMillis() - (1000 * 60 * 100)), this);
        Thread.sleep(5000);
        assertEquals("", expectedReturn, messages.get(0).getText());
        trySerializable(messages);
    }

    public void testCreateDestroyFriend() throws Exception {
        twitterAPI2.destroyAsync(id1, this);
        Thread.sleep(3000);

        twitterAPI2.destroyAsync(id1, this);
        Thread.sleep(3000);
        assertEquals(403, te.getStatusCode());
        twitterAPI2.createAsync(id1, this);
        Thread.sleep(3000);
        assertEquals(id1, user.getName());

        te = null;
        twitterAPI2.createAsync(id2, this);
        Thread.sleep(3000);
        assertEquals(403, te.getStatusCode());
        te = null;
        twitterAPI2.createAsync("doesnotexist--", this);
        Thread.sleep(3000);
        assertEquals(403, te.getStatusCode());

    }

    public void testRateLimitStatus() throws Exception{
        twitterAPI1.rateLimitStatusAsync(this);
        Thread.sleep(3000);
        assertTrue(10 < rateLimitStatus.getHourlyLimit());
        assertTrue(10 < rateLimitStatus.getRemainingHits());
    }

    public void testFollowLeave() throws Exception {
        try {
            twitterAPI2.create(id1);
        } catch (TwitterException te) {
        }
        try {
            twitterAPI2.follow(id1);
        } catch (TwitterException te) {
        }
        twitterAPI2.leaveAsync(id1, this);
        Thread.sleep(3000);
        assertEquals(id1, user.getName());
        twitterAPI2.followAsync(id2, this);
        Thread.sleep(3000);
        assertEquals(id1, user.getName());
        trySerializable(user);

    }
    public void testSearchAsync() throws Exception {
        Query query = new Query("source:twitter4j yusukey");
        twitterAPI1.searchAcync(query, this);
        Thread.sleep(3000);
        assertTrue(1265204000 < queryResult.getSinceId());
        assertTrue(1265204883 < queryResult.getMaxId());
        assertTrue(queryResult.getRefreshUrl().contains("q=source"));
        assertEquals(15, queryResult.getResultsPerPage());
        assertEquals(1, queryResult.getTotal());
        assertTrue(queryResult.getWarning().contains("adjusted"));
        assertTrue(1 > queryResult.getCompletedIn());
        assertEquals(1, queryResult.getPage());
        assertEquals("source%3Atwitter4j+yusukey", queryResult.getQuery());

        List<Tweet> tweets = queryResult.getTweets();
        assertEquals(1, tweets.size());
        assertEquals("test", tweets.get(0).getText());
        assertNull(tweets.get(0).getToUser());
        assertEquals(-1, tweets.get(0).getToUserId());
        assertNotNull(tweets.get(0).getCreatedAt());
        assertEquals("yusukey", tweets.get(0).getFromUser());
        assertEquals(10248, tweets.get(0).getFromUserId());
        assertEquals(1283504696, tweets.get(0).getId());
        assertNull(tweets.get(0).getIsoLanguageCode());
        assertTrue(tweets.get(0).getProfileImageUrl().contains(".jpg"));
        assertTrue(tweets.get(0).getSource().contains("twitter"));

    }

    private void trySerializable(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        oos.writeObject(obj);
    }
}
