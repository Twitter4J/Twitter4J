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
import twitter4j.http.HttpClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterTestUnit extends TestCase {
    protected Twitter twitterAPI1 = null;
    protected Twitter twitterAPI2 = null;
    protected Twitter unauthenticated = null;
    protected Properties p = new Properties();

    public TwitterTestUnit(String name) {
        super(name);
    }
    protected String id1,id2,id3,pass1,pass2,pass3;
    protected void setUp() throws Exception {
        super.setUp();
        p.load(new FileInputStream("test.properties"));
        id1 = p.getProperty("id1");
        id2 = p.getProperty("id2");
        id3 = p.getProperty("id3");
        pass1 = p.getProperty("pass1");
        pass2 = p.getProperty("pass2");
        pass3 = p.getProperty("pass3");
        twitterAPI1 = new Twitter(id1, pass1);
//        twitterAPI1.setRetryCount(5);
//        twitterAPI1.setRetryIntervalSecs(5);
        twitterAPI2 = new Twitter(id2, pass2);
//        twitterAPI2.setRetryCount(5);
//        twitterAPI2.setRetryIntervalSecs(5);
        unauthenticated = new Twitter();
//        unauthenticated.setRetryCount(5);
//        unauthenticated.setRetryIntervalSecs(5);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPublicTimeline() throws Exception {
        List<Status> statuses;
        statuses = twitterAPI1.getPublicTimeline();
        assertTrue("size", 5 < statuses.size());
        statuses = twitterAPI1.getPublicTimeline(12345l);
        assertTrue("size", 5 < statuses.size());

    }

    public void testGetHomeTimeline() throws Exception {
        try {
            List<Status> status = twitterAPI1.getHomeTimeline();
        } catch (TwitterException te) {
            assertEquals("this method is not available as of 8.30.2009", 404, te.getStatusCode());
        }
    }

    public void testGetFriendsTimeline()throws Exception {
        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        String dateStr = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DATE);


        String id1status = dateStr+":id1";
        String id2status = dateStr+":id2";
        Status status = twitterAPI1.updateStatus(id1status);
        assertEquals(id1status, status.getText());
        Thread.sleep(3000);
        Status status2 = twitterAPI2.updateStatus(id2status);
        assertEquals(id2status, status2.getText());

        List<Status> actualReturn;

        actualReturn = twitterAPI1.getFriendsTimeline();
        assertTrue(actualReturn.size() > 0);
        actualReturn = twitterAPI1.getFriendsTimeline(new Paging(1l));
        assertTrue(actualReturn.size() > 0);
        //this is necessary because the twitter server's clock tends to delay
        actualReturn = twitterAPI1.getFriendsTimeline(new Paging(1000l));
        assertTrue(actualReturn.size() > 0);

        actualReturn = twitterAPI1.getFriendsTimeline(new Paging(1));
        assertTrue(actualReturn.size() > 0);
        actualReturn = twitterAPI1.getFriendsTimeline(new Paging(1));
        assertTrue(actualReturn.size() > 0);


    }
    public void testShowUser() throws Exception{
        User uws = twitterAPI1.showUser(id1);
        assertEquals(id1, uws.getName());
        assertEquals(id1,uws.getScreenName());
        assertNotNull(uws.getLocation());
        assertNotNull(uws.getDescription());
        assertNotNull(uws.getProfileImageURL());
        assertNotNull(uws.getURL());
        assertFalse(uws.isProtected());

        assertTrue(0 <= uws.getFavouritesCount());
        assertTrue(0 <= uws.getFollowersCount());
        assertTrue(0 <= uws.getFriendsCount());
        assertNotNull(uws.getCreatedAt());
        assertNotNull(uws.getTimeZone());
        assertNotNull(uws.getProfileBackgroundImageUrl());
        assertNotNull(uws.getProfileBackgroundTile());

        assertTrue(0 <= uws.getStatusesCount());
        assertNotNull(uws.getProfileBackgroundColor());
        assertNotNull(uws.getProfileTextColor());
        assertNotNull(uws.getProfileLinkColor());
        assertNotNull(uws.getProfileSidebarBorderColor());
        assertNotNull(uws.getProfileSidebarFillColor());
        assertNotNull(uws.getProfileTextColor());

        assertTrue(1 < uws.getFollowersCount());
        assertNotNull(uws.getStatusCreatedAt());
        assertNotNull(uws.getStatusText());
        assertNotNull(uws.getStatusSource());
        assertFalse(uws.isStatusFavorited());
        assertEquals(-1,uws.getStatusInReplyToStatusId());
        assertEquals(-1,uws.getStatusInReplyToUserId());
        assertFalse(uws.isStatusFavorited());
        assertNull(uws.getStatusInReplyToScreenName());

        //test case for TFJ-91 null pointer exception getting user detail on users with no statuses
        //http://yusuke.homeip.net/jira/browse/TFJ-91
        unauthenticated.showUser("twit4jnoupdate");
        twitterAPI1.showUser("tigertest");
    }

    public void testGetUserTimeline_Show() throws Exception{
        List<Status> statuses;
        statuses = twitterAPI1.getUserTimeline();
        assertTrue("size", 0 < statuses.size());
        statuses = unauthenticated.getUserTimeline(id1);
        assertTrue("size", 0 < statuses.size());
        statuses = twitterAPI1.getUserTimeline(new Paging(999383469l));
        assertTrue("size", 0 < statuses.size());
        statuses = twitterAPI2.getUserTimeline(id1, new Paging().count(10));
        assertTrue("size", 0 < statuses.size());
//        statuses = twitterAPI1.getUserTimeline(15, new Date(0));
//        assertTrue("size", 0 < statuses.size());
        statuses = twitterAPI1.getUserTimeline(new Paging(999383469l).count(15));
        assertTrue("size", 0 < statuses.size());
//        statuses = twitterAPI1.getUserTimeline(id1, new Date(0));
//        assertTrue("size", 0 < statuses.size());
        statuses = twitterAPI1.getUserTimeline(id1,new Paging(999383469l));
        assertTrue("size", 0 < statuses.size());
    }
    public void testShow() throws Exception{
        Status status = twitterAPI2.showStatus(1000l);
        assertEquals(52,status.getUser().getId());
        Status status2 = unauthenticated.showStatus(1000l);
        assertEquals(52,status2.getUser().getId());
        assertTrue(50 < status.getRateLimitLimit());
        assertTrue(1 < status.getRateLimitRemaining());
        assertTrue(1 < status.getRateLimitReset());

        status2 = unauthenticated.showStatus(999383469l);
        assertEquals("01010100 01110010 01101001 01110101 01101101 01110000 01101000       <3",status2.getText());



    }
    public void testStatusMethods() throws Exception{
        String date = new java.util.Date().toString()+"test";
        Status status = twitterAPI1.updateStatus(date);

        assertEquals(date, status.getText());
        Status status2 = twitterAPI2.updateStatus("@" + id1 + " " + date, status.getId());
        assertEquals("@" + id1 + " " + date, status2.getText());
        assertEquals(status.getId(), status2.getInReplyToStatusId());
        assertEquals(twitterAPI1.verifyCredentials().getId(), status2.getInReplyToUserId());
        twitterAPI1.destroyStatus(status.getId());
    }
    public void testRetweetMethods() throws Exception {
        HttpClient client = new HttpClient();
        Status withRetweet = new Status(client.get("http://yusuke.homeip.net/twitter4j/en/status-with-retweet.xml"), new Twitter());
        assertTrue(withRetweet.isRetweet());
        assertEquals(2245122541l, withRetweet.getRetweetDetails().getRetweetId());
        assertEquals(new Date(1245451273000l)/*Sat Jun 20 07:41:13 JST 2009*/
                , withRetweet.getRetweetDetails().getRetweetedAt());
        assertEquals(3191321,withRetweet.getRetweetDetails().getRetweetingUser().getId());
        assertEquals("Prabhakar Ragde",withRetweet.getUser().getName());
        assertEquals("plragde",withRetweet.getUser().getScreenName());
        assertEquals("Marcel Molina",withRetweet.getRetweetDetails().getRetweetingUser().getName());
        assertEquals("noradio",withRetweet.getRetweetDetails().getRetweetingUser().getScreenName());
        try {
            twitterAPI1.retweetStatus(2245071380l);
        } catch (TwitterException te) {
            // retweet api is not yet availble
            assertEquals(404, te.getStatusCode());
        }
        List<RetweetDetails> retweets = RetweetDetails.createRetweetDetails(client.get("http://yusuke.homeip.net/twitter4j/en/retweets.xml"), new Twitter());
        assertEquals(2, retweets.size());
        assertEquals(4017285870l,retweets.get(0).getRetweetId());
        assertEquals(4013267275l,retweets.get(1).getRetweetId());

        try {
            twitterAPI1.getRetweetedByMe();
        } catch (TwitterException te) {
            // retweet api is not yet availble
            assertEquals(404, te.getStatusCode());
        }
        try {
            twitterAPI1.getRetweetedByMe(new Paging(1));
        } catch (TwitterException te) {
            // retweet api is not yet availble
            assertEquals(404, te.getStatusCode());
        }
        try {
            twitterAPI1.getRetweetedToMe();
        } catch (TwitterException te) {
            // retweet api is not yet availble
            assertEquals(404, te.getStatusCode());
        }
        try {
            twitterAPI1.getRetweetedToMe(new Paging(1));
        } catch (TwitterException te) {
            // retweet api is not yet availble
            assertEquals(404, te.getStatusCode());
        }
        try {
            twitterAPI1.getRetweetsOfMe();
        } catch (TwitterException te) {
            // retweet api is not yet availble
            assertEquals(404, te.getStatusCode());
        }
        try {
            twitterAPI1.getRetweetsOfMe(new Paging(1));
        } catch (TwitterException te) {
            // retweet api is not yet availble
            assertEquals(404, te.getStatusCode());
        }

        try {
            twitterAPI1.getRetweets(1000);
        } catch (TwitterException te) {
            // retweet api is not yet availble
            assertEquals(404, te.getStatusCode());
        }
    }

    public void testGeoLocation() throws Exception {
        Status withgeo2 = twitterAPI1.updateStatus("with geo", 12.3456, -34.5678);
        // not yet available
        //        assertEquals(12.3456, withgeo2.getLatitude());
        //        assertEquals(-34.5678, withgeo2.getLongitude());

        HttpClient client = new HttpClient();
        Status nogeo = new Status(client.get("http://yusuke.homeip.net/twitter4j/en/status-nogeo.xml"), new Twitter());
        assertEquals(-1d, nogeo.getLatitude());
        assertEquals(-1d, nogeo.getLongitude());
        assertEquals(false, nogeo.getUser().isGeoEnabled());
        assertEquals(false, nogeo.getUser().isVerified());

        Status withgeo = new Status(client.get("http://yusuke.homeip.net/twitter4j/en/status-withgeo.xml"), new Twitter());
        assertEquals(37.78029, withgeo.getLatitude());
        assertEquals(-122.39697, withgeo.getLongitude());
        assertEquals(true, withgeo.getUser().isGeoEnabled());
        assertEquals(true, withgeo.getUser().isVerified());

//        nogeo = new Status(client.get("http://yusuke.homeip.net/twitter4j/en/status-nogeo.json").asString());
//        assertEquals(-1d, nogeo.getLatitude());
//        assertEquals(-1d, nogeo.getLongitude());
//        assertEquals(false, nogeo.getUser().isGeoEnabled());
//        assertEquals(false, nogeo.getUser().isVerified());
//
//        withgeo = new Status(client.get("http://yusuke.homeip.net/twitter4j/en/status-withgeo.json").asString());
//        assertEquals(37.78029, withgeo.getLatitude());
//        assertEquals(-122.39697, withgeo.getLongitude());
//        assertEquals(true, withgeo.getUser().isGeoEnabled());
//        assertEquals(true, withgeo.getUser().isVerified());

    }

    public void testGetFriendsStatuses() throws Exception{
        List<User> actualReturn = twitterAPI1.getFriendsStatuses();
        assertNotNull("friendsStatuses", actualReturn);
        actualReturn = twitterAPI1.getFriendsStatuses("yusukey");
        assertNotNull("friendsStatuses", actualReturn);
        actualReturn = unauthenticated.getFriendsStatuses("yusukey");
        assertNotNull("friendsStatuses", actualReturn);
    }

    public void testSocialGraphMethods() throws Exception {
        IDs ids;
        ids = twitterAPI1.getFriendsIDs();
        int yusukey = 4933401;
        assertIDExsits("twit4j is following yusukey", ids, yusukey);
        int JBossNewsJP = 28074579;
        int RedHatNewsJP = 28074306;
        ids = twitterAPI1.getFriendsIDs(JBossNewsJP);
        assertIDExsits("JBossNewsJP is following RedHatNewsJP", ids, RedHatNewsJP);
        ids = twitterAPI1.getFriendsIDs("RedHatNewsJP");
        assertIDExsits("RedHatNewsJP is following JBossNewsJP", ids, 28074579);
        IDs obamaFollowers;
        obamaFollowers = twitterAPI1.getFollowersIDs("barackobama");
        assertTrue(obamaFollowers.hasNext());
        assertFalse(obamaFollowers.hasPrevious());
        obamaFollowers = twitterAPI1.getFollowersIDs("barackobama", obamaFollowers.getNextCursor());
        assertTrue(obamaFollowers.hasNext());
        assertTrue(obamaFollowers.hasPrevious());

        obamaFollowers = twitterAPI1.getFollowersIDs(813286);
        assertTrue(obamaFollowers.hasNext());
        assertFalse(obamaFollowers.hasPrevious());
        obamaFollowers = twitterAPI1.getFollowersIDs(813286, obamaFollowers.getNextCursor());
        assertTrue(obamaFollowers.hasNext());
        assertTrue(obamaFollowers.hasPrevious());

        IDs obamaFriends;
        obamaFriends = twitterAPI1.getFriendsIDs("barackobama");
        assertTrue(obamaFriends.hasNext());
        assertFalse(obamaFriends.hasPrevious());
        obamaFriends = twitterAPI1.getFriendsIDs("barackobama", obamaFriends.getNextCursor());
        assertTrue(obamaFriends.hasNext());
        assertTrue(obamaFriends.hasPrevious());

        obamaFriends = twitterAPI1.getFriendsIDs(813286);
        assertTrue(obamaFriends.hasNext());
        assertFalse(obamaFriends.hasPrevious());
        obamaFriends = twitterAPI1.getFriendsIDs(813286, obamaFriends.getNextCursor());
        assertTrue(obamaFriends.hasNext());
        assertTrue(obamaFriends.hasPrevious());

        try{
            twitterAPI2.createFriendship(id1);
        } catch (TwitterException ignore) {
        }
        ids = twitterAPI1.getFollowersIDs();
        assertIDExsits("twit4j2 is following twit4j", ids, 6377362);
        ids = twitterAPI1.getFollowersIDs(28074579);
        assertIDExsits("RedHatNewsJP is following JBossNewsJP", ids, 28074306);
        ids = twitterAPI1.getFollowersIDs("JBossNewsJP");
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

    public void testAccountMethods() throws Exception {
        User original = twitterAPI1.verifyCredentials();
        if(original.getScreenName().endsWith("new") ||
                original.getName().endsWith("new")){
            original = twitterAPI1.updateProfile(
                    "twit4j", null, "http://yusuke.homeip.net/twitter4j/"
                    , "location:" , "Hi there, I do test a lot!new");

        }

        String newName, newURL, newLocation, newDescription;
        String neu = "new";
        newName = original.getName() + neu;
        newURL = original.getURL() + neu;
        newLocation = original.getLocation()+neu;
        newDescription = original.getDescription()+neu;

        User altered = twitterAPI1.updateProfile(
                newName, null, newURL, newLocation, newDescription);
        twitterAPI1.updateProfile(original.getName()
                , null, original.getURL().toString(), original.getLocation(), original.getDescription());
        assertEquals(newName, altered.getName());
        assertEquals(newURL, altered.getURL().toString());
        assertEquals(newLocation, altered.getLocation());
        assertEquals(newDescription, altered.getDescription());

        try {
            new Twitter("doesnotexist--", "foobar").verifyCredentials();
            fail("should throw TwitterException");
        } catch (TwitterException te) {
        }

        twitterAPI1.updateDeliverlyDevice(Twitter.SMS);

        assertTrue(twitterAPI1.existsFriendship("JBossNewsJP", "RedHatNewsJP"));
        assertFalse(twitterAPI1.existsFriendship(id1,"al3x"));

        User eu;
        eu = twitterAPI1.updateProfileColors("f00", "f0f", "0ff", "0f0", "f0f");
        assertEquals("f00", eu.getProfileBackgroundColor());
        assertEquals("f0f", eu.getProfileTextColor());
        assertEquals("0ff", eu.getProfileLinkColor());
        assertEquals("0f0", eu.getProfileSidebarFillColor());
        assertEquals("f0f", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors("f0f", "f00", "f0f", "0ff", "0f0");
        assertEquals("f0f", eu.getProfileBackgroundColor());
        assertEquals("f00", eu.getProfileTextColor());
        assertEquals("f0f", eu.getProfileLinkColor());
        assertEquals("0ff", eu.getProfileSidebarFillColor());
        assertEquals("0f0", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors("87bc44", "9ae4e8", "000000", "0000ff", "e0ff92");
        assertEquals("87bc44", eu.getProfileBackgroundColor());
        assertEquals("9ae4e8", eu.getProfileTextColor());
        assertEquals("000000", eu.getProfileLinkColor());
        assertEquals("0000ff", eu.getProfileSidebarFillColor());
        assertEquals("e0ff92", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors("f0f", null, "f0f", null, "0f0");
        assertEquals("f0f", eu.getProfileBackgroundColor());
        assertEquals("9ae4e8", eu.getProfileTextColor());
        assertEquals("f0f", eu.getProfileLinkColor());
        assertEquals("0000ff", eu.getProfileSidebarFillColor());
        assertEquals("0f0", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors(null, "f00", null, "0ff", null);
        assertEquals("f0f", eu.getProfileBackgroundColor());
        assertEquals("f00", eu.getProfileTextColor());
        assertEquals("f0f", eu.getProfileLinkColor());
        assertEquals("0ff", eu.getProfileSidebarFillColor());
        assertEquals("0f0", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors("9ae4e8", "000000", "0000ff", "e0ff92", "87bc44");
        assertEquals("9ae4e8", eu.getProfileBackgroundColor());
        assertEquals("000000", eu.getProfileTextColor());
        assertEquals("0000ff", eu.getProfileLinkColor());
        assertEquals("e0ff92", eu.getProfileSidebarFillColor());
        assertEquals("87bc44", eu.getProfileSidebarBorderColor());
    }
    public void testFavoriteMethods() throws Exception{
        Status status = twitterAPI1.updateStatus("test");
        twitterAPI2.createFavorite(status.getId());
        assertTrue(twitterAPI2.getFavorites().size() > 0);
        try {
            twitterAPI2.destroyFavorite(status.getId());
        } catch (TwitterException te) {
            // sometimes destorying favorite fails with 404
            assertEquals(404, te.getStatusCode());
        }
    }
    public void testFollowers() throws Exception{
        List<User> actualReturn = twitterAPI1.getFollowersStatuses();
        assertTrue(actualReturn.size() > 0);
        actualReturn = twitterAPI1.getFollowersStatuses(new Paging(1));
        assertTrue(actualReturn.size() > 0);
        actualReturn = twitterAPI1.getFollowersStatuses(new Paging(2));
        assertEquals(0,actualReturn.size());

        actualReturn = twitterAPI2.getFollowersStatuses();
        assertTrue(actualReturn.size() > 0);
        actualReturn = twitterAPI2.getFollowersStatuses("yusukey");
        assertTrue(actualReturn.size() > 60);
        actualReturn = twitterAPI2.getFollowersStatuses("yusukey",new Paging(2));
        assertTrue(actualReturn.size() > 10);
    }
    public void testFeatured() throws Exception{
        List<User> actualReturn = twitterAPI1.getFeatured();
        assertTrue(actualReturn.size() > 9);
    }

    public void testDirectMessages() throws Exception {
        String expectedReturn = new Date() + ":directmessage test";
        DirectMessage actualReturn = twitterAPI1.sendDirectMessage("twit4jnoupdate", expectedReturn);
        assertEquals(expectedReturn, actualReturn.getText());
        List<DirectMessage> actualReturnList = twitterAPI1.getDirectMessages();
        assertTrue(1<= actualReturnList.size());
    }

    private void followEachOther() {
        try {
            twitterAPI1.createFriendship(id2);
        } catch (TwitterException te) {
            te.printStackTrace();
        }
        try {
            twitterAPI2.createFriendship(id1);
        } catch (TwitterException te) {
            te.printStackTrace();
        }
    }

    public void testCreateDestroyFriend() throws Exception{
        User user;
        try {
            user = twitterAPI2.destroyFriendship(id1);
        } catch (TwitterException te) {
            //ensure destory id1 before the actual test
            //ensure destory id1 before the actual test
        }

        try {
            user = twitterAPI2.destroyFriendship(id1);
        } catch (TwitterException te) {
            assertEquals(403, te.getStatusCode());
        }
        user = twitterAPI2.createFriendship(id1, true);
        assertEquals(id1, user.getName());
        // the Twitter API is not returning appropriate notifications value
        // http://code.google.com/p/twitter-api/issues/detail?id=474
//        User detail = twitterAPI2.getUserDetail(id1);
//        assertTrue(detail.isNotificationEnabled());
        try {
            user = twitterAPI2.createFriendship(id2);
            fail("shouldn't be able to befrinend yourself");
        } catch (TwitterException te) {
            assertEquals(403, te.getStatusCode());
        }
        try {
            user = twitterAPI2.createFriendship("doesnotexist--");
            fail("non-existing user");
        } catch (TwitterException te) {
            //now befriending with non-existing user returns 404
            //http://groups.google.com/group/twitter-development-talk/browse_thread/thread/bd2a912b181bc39f
            assertEquals(404, te.getStatusCode());
        }

    }
    public void testGetReplies() throws Exception{
        twitterAPI2.updateStatus("@"+id1+" reply to id1");
        List<Status> statuses = twitterAPI1.getMentions();
        assertTrue(statuses.size() > 0);
        assertTrue(-1 != statuses.get(0).getText().indexOf(" reply to id1"));

        statuses = twitterAPI1.getMentions(new Paging(1));
        assertTrue(statuses.size() > 0);
        statuses = twitterAPI1.getMentions(new Paging(1));
        assertTrue(statuses.size() > 0);
        statuses = twitterAPI1.getMentions(new Paging(1, 1l));
        assertTrue(statuses.size() > 0);
        statuses = twitterAPI1.getMentions(new Paging(1l));
        assertTrue(statuses.size() > 0);
        assertTrue(-1 != statuses.get(0).getText().indexOf(" reply to id1"));
    }

    public void testNotification() throws Exception {
        try {
            twitterAPI1.disableNotification("twit4jprotected");
        } catch (TwitterException te) {
        }
        twitterAPI1.enableNotification("twit4jprotected");
        twitterAPI1.disableNotification("twit4jprotected");
    }
    public void testBlock() throws Exception {
        twitterAPI2.createBlock(id1);
        twitterAPI2.destroyBlock(id1);
        assertFalse(twitterAPI1.existsBlock("twit4j2"));
        assertTrue(twitterAPI1.existsBlock("twit4jblock"));
        List<User> users = twitterAPI1.getBlockingUsers();
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());
        users = twitterAPI1.getBlockingUsers(1);
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());

        IDs ids = twitterAPI1.getBlockingUsersIDs();
        assertEquals(1, ids.getIDs().length);
        assertEquals(39771963, ids.getIDs()[0]);
    }


    public void testRateLimitStatus() throws Exception{
        RateLimitStatus status = twitterAPI1.rateLimitStatus();
        assertTrue(10 < status.getHourlyLimit());
        assertTrue(10 < status.getRemainingHits());
    }

    public void testSavedSearches() throws Exception {
        List<SavedSearch> list = twitterAPI1.getSavedSearches();
        for(SavedSearch savedSearch : list){
            twitterAPI1.destroySavedSearch(savedSearch.getId());
        }
        SavedSearch ss1 = twitterAPI1.createSavedSearch("my search");
        assertEquals("my search", ss1.getQuery());
        assertEquals(-1, ss1.getPosition());
        list = twitterAPI1.getSavedSearches();
        assertEquals(1, list.size());
        SavedSearch ss2 = twitterAPI1.destroySavedSearch(ss1.getId());
        assertEquals(ss1, ss2);
    }

    public void testTest() throws Exception {
        assertTrue(twitterAPI2.test());
    }


    public void testProperties() throws Exception{
        TwitterSupport twitterSupport;
        String test = "t4j";
        String override = "system property";


        System.getProperties().remove("twitter4j.user");
        twitterSupport = new Twitter();
        assertNull(twitterSupport.getUserId());

        twitterSupport.setUserId(test);
        assertEquals(test, twitterSupport.getUserId());
        System.setProperty("twitter4j.user", override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.getUserId());
        twitterSupport.setUserId(test);
        assertEquals(override, twitterSupport.getUserId());
        System.getProperties().remove("twitter4j.user");

        System.getProperties().remove("twitter4j.password");
        twitterSupport = new Twitter();
        assertNull(twitterSupport.getPassword());

        twitterSupport.setPassword(test);
        assertEquals(test, twitterSupport.getPassword());
        System.setProperty("twitter4j.password", override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.getPassword());
        twitterSupport.setPassword(test);
        assertEquals(override, twitterSupport.getPassword());
        System.getProperties().remove("twitter4j.password");


        System.getProperties().remove("twitter4j.source");
        twitterSupport = new Twitter();
        assertEquals("Twitter4J", twitterSupport.getSource());

        twitterSupport.setSource(test);
        assertEquals(test, twitterSupport.getSource());
        System.setProperty("twitter4j.source", override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.getSource());
        twitterSupport.setSource(test);
        assertEquals(override, twitterSupport.getSource());
        System.getProperties().remove("twitter4j.source");


        System.getProperties().remove("twitter4j.clientVersion");
        twitterSupport = new Twitter();
        assertEquals(Version.getVersion(), twitterSupport.getClientVersion());

        twitterSupport.setClientVersion(test);
        assertEquals(test, twitterSupport.getClientVersion());
        System.setProperty("twitter4j.clientVersion", override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.getClientVersion());
        twitterSupport.setClientVersion(test);
        assertEquals(override, twitterSupport.getClientVersion());
        System.getProperties().remove("twitter4j.clientVersion");


        System.getProperties().remove("twitter4j.clientURL");
        twitterSupport = new Twitter();
        assertEquals("http://yusuke.homeip.net/twitter4j/en/twitter4j-" + Version.getVersion() + ".xml", twitterSupport.getClientURL());

        twitterSupport.setClientURL(test);
        assertEquals(test, twitterSupport.getClientURL());
        System.setProperty("twitter4j.clientURL", override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.getClientURL());
        twitterSupport.setClientURL(test);
        assertEquals(override, twitterSupport.getClientURL());
        System.getProperties().remove("twitter4j.clientURL");



        System.getProperties().remove("twitter4j.http.userAgent");
        twitterSupport = new Twitter();
        assertEquals("twitter4j http://yusuke.homeip.net/twitter4j/ /" + Version.getVersion(), twitterSupport.http.getRequestHeader("User-Agent"));

        twitterSupport.setUserAgent(test);
        assertEquals(test, twitterSupport.getUserAgent());
        System.setProperty("twitter4j.http.userAgent", override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.getUserAgent());
        twitterSupport.setUserAgent(test);
        assertEquals(override, twitterSupport.getUserAgent());
        System.getProperties().remove("twitter4j.http.userAgent");

        System.getProperties().remove("twitter4j.http.proxyHost");
        twitterSupport = new Twitter();
        assertEquals(null, twitterSupport.http.getProxyHost());

        twitterSupport.setHttpProxy(test,10);
        assertEquals(test, twitterSupport.http.getProxyHost());
        System.setProperty("twitter4j.http.proxyHost", override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.http.getProxyHost());
        twitterSupport.setHttpProxy(test,10);
        assertEquals(override, twitterSupport.http.getProxyHost());
        System.getProperties().remove("twitter4j.http.proxyHost");

        System.getProperties().remove("twitter4j.http.proxyPort");
        twitterSupport = new Twitter();
        assertEquals(-1, twitterSupport.http.getProxyPort());

        twitterSupport.setHttpProxy(test,10);
        assertEquals(10, twitterSupport.http.getProxyPort());
        System.setProperty("twitter4j.http.proxyPort", "100");
        twitterSupport = new Twitter();
        assertEquals(100, twitterSupport.http.getProxyPort());
        twitterSupport.setHttpProxy(test,10);
        assertEquals(100, twitterSupport.http.getProxyPort());
        System.getProperties().remove("twitter4j.http.proxyPort");


        System.getProperties().remove("twitter4j.http.proxyUser");
        twitterSupport = new Twitter();
        assertEquals(null, twitterSupport.http.getProxyAuthUser());

        twitterSupport.setHttpProxyAuth(test,test);
        assertEquals(test, twitterSupport.http.getProxyAuthUser());
        System.setProperty("twitter4j.http.proxyUser", override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.http.getProxyAuthUser());
        twitterSupport.setHttpProxyAuth(test,test);
        assertEquals(override, twitterSupport.http.getProxyAuthUser());
        System.getProperties().remove("twitter4j.http.proxyUser");


        System.getProperties().remove("twitter4j.http.proxyPassword");
        twitterSupport = new Twitter();
        assertEquals(null, twitterSupport.http.getProxyAuthPassword());

        twitterSupport.setHttpProxyAuth(test,test);
        assertEquals(test, twitterSupport.http.getProxyAuthPassword());
        System.setProperty("twitter4j.http.proxyPassword",  override);
        twitterSupport = new Twitter();
        assertEquals(override, twitterSupport.http.getProxyAuthPassword());
        twitterSupport.setHttpProxyAuth(test,test);
        assertEquals(override, twitterSupport.http.getProxyAuthPassword());
        System.getProperties().remove("twitter4j.http.proxyPassword");


        System.getProperties().remove("twitter4j.http.connectionTimeout");
        twitterSupport = new Twitter();
        assertEquals(20000, twitterSupport.http.getConnectionTimeout());

        twitterSupport.setHttpConnectionTimeout(10);
        assertEquals(10, twitterSupport.http.getConnectionTimeout());
        System.setProperty("twitter4j.http.connectionTimeout", "100");
        twitterSupport = new Twitter();
        assertEquals(100, twitterSupport.http.getConnectionTimeout());
        twitterSupport.setHttpConnectionTimeout(10);
        assertEquals(100, twitterSupport.http.getConnectionTimeout());
        System.getProperties().remove("twitter4j.http.connectionTimeout");


        System.getProperties().remove("twitter4j.http.readTimeout");
        twitterSupport = new Twitter();
        assertEquals(120000, twitterSupport.http.getReadTimeout());

        twitterSupport.setHttpReadTimeout(10);
        assertEquals(10, twitterSupport.http.getReadTimeout());
        System.setProperty("twitter4j.http.readTimeout",  "100");
        twitterSupport = new Twitter();
        assertEquals(100, twitterSupport.http.getReadTimeout());
        twitterSupport.setHttpConnectionTimeout(10);
        assertEquals(100, twitterSupport.http.getReadTimeout());
        System.getProperties().remove("twitter4j.http.readTimeout");

        assertFalse(Configuration.isDalvik());


        writeFile("./twitter4j.properties","twitter4j.http.readTimeout=1234");
        Configuration.init();
        assertEquals(1234, Configuration.getReadTimeout());
        writeFile("./twitter4j.properties","twitter4j.http.readTimeout=4321");
        Configuration.init();
        assertEquals(4321, Configuration.getReadTimeout());
        deleteFile("./twitter4j.properties");
        Configuration.init();
    }
    private void writeFile(String path, String content) throws IOException {
        File file = new File(path);
        file.delete();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.close();
    }
    private void deleteFile(String path) throws IOException {
        File file = new File(path);
        file.delete();
    }
}
