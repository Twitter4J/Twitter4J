package twitter4j;

import junit.framework.TestCase;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * <p>Title: Twitter4J</p>
 *
 * <p>Description: </p>
 *
 */
public class AsyncTwitterTestUnit extends TestCase implements TwitterListener {
    private List<Status> statuses = null;
    private List<User> users = null;
    private List<DirectMessage> messages = null;
    private Status status = null;
    private User user = null;
    private UserWithStatus userWithStatus = null;
    private DirectMessage message = null;
    private TwitterException te = null;
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

    /**
     *
     * @param te TwitterException
     * @param method int
     */

    public void onException(TwitterException te, int method) {
        System.out.println(te.getMessage());
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
        twitterAPI1.getPublicTimelineAsync("12345", this);
        Thread.sleep(5000);
        assertTrue("size", 5 < statuses.size());
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
        assertTrue("size" , 5<  statuses.size());
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
    }

    public void testShow() throws Exception {
        twitterAPI2.showAsync(1000, this);
        Thread.sleep(3000);
        assertEquals(52, status.getUser().getId());
    }

    public void testUpdate() throws Exception {
        String date = new java.util.Date().toString() + "test";
        twitterAPI1.updateAsync(date, this);
        Thread.sleep(3000);
        assertEquals("", date, status.getText());
    }

    public void testGetFriends() throws Exception {
        twitterAPI1.getFriendsAsync(id2,this);
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
    }

    public void testFollowers() throws Exception {
        twitterAPI1.getFollowersAsync(this);
        Thread.sleep(3000);
        assertTrue(users.size() > 0);

        twitterAPI2.getFollowersAsync(this);
        Thread.sleep(3000);
        assertTrue(users.size() > 0);
    }

    public void testFeatured() throws Exception {
        twitterAPI1.getFeaturedAsync(this);
        Thread.sleep(3000);
        assertTrue(users.size() > 9);
    }

    public void testGetDirectMessages() throws Exception {
        String expectedReturn = new Date() + ":directmessage test";
        twitterAPI2.sendDirectMessageAsync(id1, expectedReturn, this);
        Thread.sleep(3000);
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
        twitterAPI2.createAsync("doesnotexist", this);
        Thread.sleep(3000);
        assertEquals(403, te.getStatusCode());

    }
}
