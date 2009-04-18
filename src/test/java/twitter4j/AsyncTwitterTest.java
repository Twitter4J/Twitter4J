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
    private ExtendedUser extendedUser = null;
    private boolean test;
    private String schedule;
    private DirectMessage message = null;
    private TwitterException te = null;
    private RateLimitStatus rateLimitStatus;
    private boolean exists;
    private QueryResult queryResult;
    private IDs ids;

    public void gotPublicTimeline(List<Status> statuses) {
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

    public void updated(Status status) {
        this.status = status;
        notifyResponse();
    }

    public void gotReplies(List<Status> statuses) {
        this.statuses = statuses;
        notifyResponse();
    }

    public void destroyedStatus(Status destroyedStatus) {
        this.status = destroyedStatus;
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

    public void gotUserDetail(ExtendedUser extendedUser) {
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

    public void destroyed(User user) {
        this.user = user;
        notifyResponse();
    }

    public void gotExists(boolean exists) {
        this.exists = exists;
        notifyResponse();
    }

    public void updatedLocation(User user) {
        this.user = user;
        notifyResponse();
    }

    public void updatedProfileColors(ExtendedUser extendedUser){
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

    public void left(User user) {
        this.user = user;
        notifyResponse();
    }

    public void blocked(User user) {
        this.user = user;
        notifyResponse();
    }

    public void unblocked(User user) {
        this.user = user;
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

    /**
     * @param te     TwitterException
     * @param method int
     */

    public void onException(TwitterException te, int method) {
        this.te = te;
        te.printStackTrace();
        notifyResponse();
    }

    private synchronized void notifyResponse(){
        this.notify();
    }

    private synchronized void waitForResponse(){
        try {
            this.wait();
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
        extendedUser = null;
        message = null;
        te = null;

    }

    protected void tearDown() throws Exception {
        twitterAPI1 = null;
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
        twitterAPI1.updateAsync(id1status, this);
        waitForResponse();
        assertEquals(id1status, status.getText());
        twitterAPI2.updateAsync(id2status, this);
        waitForResponse();
        assertEquals(id2status, status.getText());

        twitterAPI1.getFriendsTimelineAsync(this);
        waitForResponse();
        assertTrue(statuses.size() > 0);

        twitterAPI1.getFriendsTimelineAsync(new Date(0), this);
        waitForResponse();
        assertTrue(statuses.size() > 0);

        twitterAPI2.getFriendsTimelineAsync(id1, this);
        waitForResponse();
        assertTrue(statuses.size() > 0);

        twitterAPI1.getFriendsTimelineAsync(id2, new Date(0), this);
        waitForResponse();
        assertTrue("size", 5 < statuses.size());
        trySerializable(statuses);
    }

    public void testGetUserDetail() throws Exception{
        twitterAPI1.getUserDetailAsync(id1,this);
        waitForResponse();
        UserWithStatus uws = this.extendedUser;
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
        twitterAPI2.getUserTimelineAsync(id1, this);
        waitForResponse();
        assertTrue("size", 10 < statuses.size());
        twitterAPI2.getUserTimelineAsync(id1, 10, this);
        waitForResponse();
        assertEquals("size", 10, statuses.size());
        twitterAPI1.getUserTimelineAsync(15, new Date(0), this);
        waitForResponse();
        //        System.out.println("lastURL:"+twitterAPI1.lastURL);
        assertTrue("size", 5 < statuses.size());
        twitterAPI1.getUserTimelineAsync(id1, new Date(0), this);
        waitForResponse();
        assertTrue("size", 5 < statuses.size());
        twitterAPI1.getUserTimelineAsync(id1, 20, new Date(0), this);
        waitForResponse();
        assertTrue("size", 5 < statuses.size());
        trySerializable(statuses);
    }

    public void testFavorite() throws Exception {
        Status status = twitterAPI1.update(new Date().toString());
        twitterAPI2.createFavoriteAsync(status.getId(), this);
        waitForResponse();
        assertEquals(status, this.status);
        this.status = null;
        twitterAPI2.destroyFavoriteAsync(status.getId(), this);
        waitForResponse();
        assertEquals(status, this.status);
    }

    public void testSocialGraphMethods() throws Exception {
        twitterAPI1.getFriendsIDsAsync(this);
        waitForResponse();
        assertIDExsits(ids, 4933401);
        twitterAPI1.getFriendsIDsAsync(4933401, this);
        waitForResponse();
        assertIDExsits(ids, 6358482);
        twitterAPI1.getFriendsIDsAsync("yusukey", this);
        waitForResponse();
        assertIDExsits(ids, 6358482);

        twitterAPI1.getFollowersIDsAsync(this);
        waitForResponse();
        assertIDExsits(ids, 4933401);
        twitterAPI1.getFollowersIDsAsync(4933401, this);
        waitForResponse();
        assertIDExsits(ids, 6358482);
        twitterAPI1.getFollowersIDsAsync("yusukey", this);
        waitForResponse();
        assertIDExsits(ids, 6358482);
    }

    private void assertIDExsits(IDs ids, int idToFind){
        boolean found = false;
        for(int id : ids.getIDs()){
            if(id == idToFind){
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    public void testAccountMethods() throws Exception{
        twitterAPI1.createAsync(id2);
        twitterAPI1.followAsync(id2);
        twitterAPI2.createAsync(id1);
        twitterAPI2.followAsync(id1);
        twitterAPI1.existsAsync(id1,id2,this);
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
        twitterAPI2.showAsync(1000l, this);
        waitForResponse();
        assertEquals(52, status.getUser().getId());
        trySerializable(status);
    }

    public void testUpdate() throws Exception {
        String date = new java.util.Date().toString() + "test";
        twitterAPI1.updateAsync(date, this);
        waitForResponse();
        assertEquals("", date, status.getText());

        long id = status.getId();

        twitterAPI2.updateAsync("@" + id1 + " " + date, id, this);
        waitForResponse();
        assertEquals("", "@" + id1 + " " + date, status.getText());
        assertEquals("", id, status.getInReplyToStatusId());
        assertEquals(twitterAPI1.getAuthenticatedUser().getId(), status.getInReplyToUserId());


        id = status.getId();
        this.status = null;
        twitterAPI2.destroyStatusAsync(id, this);
        waitForResponse();
        assertEquals("", "@" + id1 + " " + date, status.getText());
        trySerializable(status);
    }

    public void testGetFriends() throws Exception {
        twitterAPI1.getFriendsAsync(id2, this);
        waitForResponse();
        boolean found = false;
        for (User user : users) {
            found = found || user.getName().equals(id1);
        }
        assertTrue(found);

        twitterAPI2.getFriendsAsync(this);
        waitForResponse();
        found = false;
        for (User user : users) {
            found = found || user.getName().equals(id1);
        }
        assertTrue(found);
        trySerializable(users);
    }

    public void testFollowers() throws Exception {
        twitterAPI1.getFollowersAsync(this);
        waitForResponse();
        assertTrue(users.size() > 0);

        twitterAPI2.getFollowersAsync(this);
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
        waitForResponse();
//        twitterAPI2.sendDirectMessage("yusukey",expectedReturn);
        twitterAPI1.getDirectMessagesAsync(this);
        waitForResponse();
        assertEquals("", expectedReturn, messages.get(0).getText());
//        String expectedReturn = new Date()+":directmessage test";
        twitterAPI1.sendDirectMessageAsync(id2, expectedReturn, this);
        waitForResponse();
        assertEquals("", expectedReturn, message.getText());
        twitterAPI2.getDirectMessagesAsync(new Date(System.currentTimeMillis() - (1000 * 60 * 100)), this);
        waitForResponse();
        assertEquals("", expectedReturn, messages.get(0).getText());
        trySerializable(messages);
    }

    public void testCreateDestroyFriend() throws Exception {
        twitterAPI2.destroyAsync(id1, this);
        waitForResponse();

        twitterAPI2.destroyAsync(id1, this);
        waitForResponse();
        assertEquals(403, te.getStatusCode());
        twitterAPI2.createAsync(id1, this);
        waitForResponse();
        assertEquals(id1, user.getName());

        te = null;
        twitterAPI2.createAsync(id2, this);
        waitForResponse();
        assertEquals(403, te.getStatusCode());
        te = null;
        twitterAPI2.createAsync("doesnotexist--", this);
        waitForResponse();
        assertEquals(403, te.getStatusCode());

    }

    public void testRateLimitStatus() throws Exception{
        twitterAPI1.rateLimitStatusAsync(this);
        waitForResponse();
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
        waitForResponse();
        assertEquals(id1, user.getName());
        twitterAPI2.followAsync(id2, this);
        waitForResponse();
        assertEquals(id1, user.getName());
        trySerializable(user);

    }
    public void testSearchAsync() throws Exception {
        Query query = new Query("source:web thisisarondomstringforatestcase");
        twitterAPI1.searchAcync(query, this);
        waitForResponse();
        assertTrue(1265204000 < queryResult.getSinceId());
        assertTrue(1265204883 < queryResult.getMaxId());
        assertTrue(queryResult.getRefreshUrl().contains("q=source"));
        assertEquals(15, queryResult.getResultsPerPage());
        assertEquals(-1, queryResult.getTotal());
        assertTrue(queryResult.getWarning().contains("adjusted"));
        assertTrue(1 > queryResult.getCompletedIn());
        assertEquals(1, queryResult.getPage());
        assertEquals("source:web thisisarondomstringforatestcase", queryResult.getQuery());

        List<Tweet> tweets = queryResult.getTweets();
        assertEquals(1, tweets.size());
        assertNull(tweets.get(0).getToUser());
        assertEquals(-1, tweets.get(0).getToUserId());
        assertNotNull(tweets.get(0).getCreatedAt());
        assertEquals("twit4j", tweets.get(0).getFromUser());
        assertEquals(1620730, tweets.get(0).getFromUserId());
        assertEquals(1525853472, tweets.get(0).getId());
        assertNotNull(tweets.get(0).getIsoLanguageCode());
        assertTrue(tweets.get(0).getProfileImageUrl().contains(".jpg") ||tweets.get(0).getProfileImageUrl().contains(".png") );
        assertTrue(tweets.get(0).getSource().contains("twitter"));

    }

    private void trySerializable(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        oos.writeObject(obj);
    }
}
