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

import twitter4j.internal.org.json.JSONObject;
import twitter4j.json.DataObjectFactory;

import static twitter4j.DAOTest.assertDeserializedFormIsEqual;
import static twitter4j.DAOTest.assertDeserializedFormIsNotEqual;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterTest extends TwitterTestBase {

    public TwitterTest(String name) {
        super(name);
    }


    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPublicTimeline() throws Exception {
        List<Status> statuses;
        statuses = twitterAPI1.getPublicTimeline();
        assertTrue("size", 0 < statuses.size());
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
    }

    public void testGetHomeTimeline() throws Exception {
        List<Status> statuses = twitterAPI1.getHomeTimeline();
        assertTrue(0 < statuses.size());
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
    }
    public void testSerializability() throws Exception {
        Twitter twitter = new TwitterFactory().getInstance("foo", "bar");
        Twitter deserialized = (Twitter)assertDeserializedFormIsEqual(twitter);

        assertEquals(deserialized.getScreenName(), twitter.getScreenName());
        assertEquals(deserialized.auth, twitter.auth);

        twitter = new TwitterFactory().getInstance();
        deserialized = (Twitter)assertDeserializedFormIsEqual(twitter);
        assertEquals(deserialized.auth, twitter.auth);
    }
    public void testGetScreenName() throws Exception {
        assertEquals(id1.screenName, twitterAPI1.getScreenName());
        assertEquals(id1.id, twitterAPI1.getId());
    }

    public void testGetFriendsTimeline() throws Exception {
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
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(actualReturn.get(0))));
    }

    public void testShowUser() throws Exception {
        User user = twitterAPI1.showUser(id1.screenName);
        assertEquals(id1.screenName, user.getScreenName());
        assertNotNull(user.getLocation());
        assertNotNull(user.getDescription());
        assertNotNull(user.getProfileImageURL());
        assertNotNull(user.getURL());
        assertFalse(user.isProtected());

        assertTrue(0 <= user.getFavouritesCount());
        assertTrue(0 <= user.getFollowersCount());
        assertTrue(0 <= user.getFriendsCount());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getTimeZone());
        assertNotNull(user.getProfileBackgroundImageUrl());

        assertTrue(0 <= user.getStatusesCount());
        assertNotNull(user.getProfileBackgroundColor());
        assertNotNull(user.getProfileTextColor());
        assertNotNull(user.getProfileLinkColor());
        assertNotNull(user.getProfileSidebarBorderColor());
        assertNotNull(user.getProfileSidebarFillColor());
        assertNotNull(user.getProfileTextColor());

        assertTrue(1 < user.getFollowersCount());
        assertNotNull(user.getStatusCreatedAt());
        assertNotNull(user.getStatusText());
        assertNotNull(user.getStatusSource());
        assertFalse(user.isStatusFavorited());
        assertEquals(-1, user.getStatusInReplyToStatusId());
        assertEquals(-1, user.getStatusInReplyToUserId());
        assertFalse(user.isStatusFavorited());
        assertNull(user.getStatusInReplyToScreenName());

        assertTrue(1 < user.getListedCount());
        assertFalse(user.isFollowRequestSent());

        //test case for TFJ-91 null pointer exception getting user detail on users with no statuses
        //http://yusuke.homeip.net/jira/browse/TFJ-91
        twitterAPI1.showUser("twit4jnoupdate");
        user = twitterAPI1.showUser("tigertest");
        User previousUser = user;
        assertNotNull(DataObjectFactory.getRawJSON(user));

        user = twitterAPI1.showUser(numberId);
        assertEquals(numberIdId, user.getId());
        assertNull(DataObjectFactory.getRawJSON(previousUser));
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));

        previousUser = user;
        user = twitterAPI1.showUser(numberIdId);
        assertEquals(numberIdId, user.getId());
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
    }

    public void testLookupUsers() throws TwitterException {
        ResponseList<User> users = twitterAPI1.lookupUsers(new String[]{id1.screenName, id2.screenName});
        assertEquals(2, users.size());
        assertContains(users, id1);
        assertContains(users, id2);

        users = twitterAPI1.lookupUsers(new int[]{id1.id, id2.id});
        assertEquals(2, users.size());
        assertContains(users, id1);
        assertContains(users, id2);
        assertNull(users.getFeatureSpecificRateLimitStatus());
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
    }

    private void assertContains(ResponseList<User> users, TestUserInfo user) {
        boolean found = false;
        for(User aUser : users){
            if(aUser.getId() == user.id && aUser.getScreenName().equals(user.screenName)){
                found = true;
                break;
            }
        }
        if(!found){
            fail(user.screenName + " not found in the result.");
        }

    }

    public void testSearchUser() throws TwitterException {
        ResponseList<User> users = twitterAPI1.searchUsers("Doug Williams",1);
        assertTrue(4 < users.size());
        assertNotNull(users.getFeatureSpecificRateLimitStatus());
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
    }
    public void testSuggestion() throws Exception {
        ResponseList<Category> categories = twitterAPI1.getSuggestedUserCategories();
        assertTrue(categories.size() > 0);
        assertNotNull(DataObjectFactory.getRawJSON(categories));
        assertNotNull(DataObjectFactory.getRawJSON(categories.get(0)));
        assertEquals(categories.get(0), DataObjectFactory.createCategory(DataObjectFactory.getRawJSON(categories.get(0))));
        ResponseList<User> users = twitterAPI1.getUserSuggestions(categories.get(0).getSlug());
        assertTrue(users.size() > 0);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
    }
    public void testProfileImage() throws Exception {
        ProfileImage image = twitterAPI1.getProfileImage(id1.screenName, ProfileImage.BIGGER);
        assertNotNull(image.getURL());
    }


    // list deletion doesn't work now.
    // http://groups.google.com/group/twitter-development-talk/t/4e4164a347da1c3b
    // http://code.google.com/p/twitter-api/issues/detail?id=1327
    public void testList() throws Exception {
        PagableResponseList<UserList> userLists;
        userLists = twitterAPI1.getUserLists(id1.screenName,-1l);
        assertNotNull(DataObjectFactory.getRawJSON(userLists));
        for(UserList alist : userLists){
            twitterAPI1.destroyUserList(alist.getId());
        }

        /*List Methods*/
        UserList userList;
        userList = twitterAPI1.createUserList("testpoint1", false, "description1");
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        assertEquals("testpoint1", userList.getName());
        assertEquals("description1", userList.getDescription());

        userList = twitterAPI1.updateUserList(userList.getId(), "testpoint2", true, "description2");
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertTrue(userList.isPublic());
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);
        assertEquals("testpoint2", userList.getName());
        assertEquals("description2", userList.getDescription());


        userLists = twitterAPI1.getUserLists(id1.screenName, -1l);
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertFalse(userLists.size() == 0);

        userList = twitterAPI1.showUserList(id1.screenName, userList.getId());
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        List<Status> statuses = twitterAPI1.getUserListStatuses(id1.screenName, userList.getId(), new Paging());
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(statuses);

        /*List Member Methods*/
        User user = null;
        try {
            user = twitterAPI1.checkUserListMembership(id1.screenName, id2.id, userList.getId());
            fail("id2 shouldn't be a member of the userList yet. expecting a TwitterException");
        } catch (TwitterException te) {
            assertEquals(404, te.getStatusCode());
        }
        userList = twitterAPI1.addUserListMember(userList.getId(), id2.id);
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        userList = twitterAPI1.addUserListMembers(userList.getId(), new int[]{id4.id,id3.id});
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        userList = twitterAPI1.addUserListMembers(userList.getId(), new String[]{"akr", "yusukey"});
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        PagableResponseList<User> users = twitterAPI1.getUserListMembers(id1.screenName, userList.getId(), -1);
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertNull(DataObjectFactory.getRawJSON(userList));
        // workaround issue 1301
        // http://code.google.com/p/twitter-api/issues/detail?id=1301
//        assertEquals(userList.getMemberCount(), users.size());
        assertTrue(0 < users.size());// workaround issue 1301

        userList = twitterAPI1.deleteUserListMember(userList.getId(), id2.id);
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        //
//        assertEquals(1, userList.getMemberCount());

        user = twitterAPI1.checkUserListMembership(id1.screenName, userList.getId(), id4.id);
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        assertEquals(id4.id, user.getId());

        userLists = twitterAPI1.getUserListMemberships(id1.screenName, -1l);
        assertNotNull(DataObjectFactory.getRawJSON(userLists));
        assertEquals(userLists.get(0), DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userLists.get(0))));
        assertNotNull(userLists);

        userLists = twitterAPI1.getUserListSubscriptions(id1.screenName, -1l);
        assertNotNull(DataObjectFactory.getRawJSON(userLists));
        if (userLists.size() > 0) {
            assertEquals(userLists.get(0), DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userLists.get(0))));
        }
        assertNotNull(userLists);
        assertEquals(0, userLists.size());

        /*List Subscribers Methods*/

        users = twitterAPI1.getUserListSubscribers(id1.screenName, userList.getId(), -1);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(0, users.size());
        try {
            twitterAPI2.subscribeUserList(id1.screenName, userList.getId());
        } catch (TwitterException te) {
            // workarounding issue 1300
            // http://code.google.com/p/twitter-api/issues/detail?id=1300
            assertEquals(404,te.getStatusCode());
        }
        // expected subscribers: id2
        try {
            twitterAPI4.subscribeUserList(id1.screenName, userList.getId());
        } catch (TwitterException te) {
            // workarounding issue 1300
            assertEquals(404, te.getStatusCode());
        }
        // expected subscribers: id2 and id4
        try {
            twitterAPI2.unsubscribeUserList(id1.screenName, userList.getId());
        } catch (TwitterException te) {
            // workarounding issue 1300
            assertEquals(404, te.getStatusCode());
        }
        // expected subscribers: id4
        users = twitterAPI1.getUserListSubscribers(id1.screenName, userList.getId(), -1);
//        assertEquals(1, users.size()); //only id4 should be subscribing the userList
        assertTrue(0 <= users.size()); // workarounding issue 1300
        try {
            user = twitterAPI1.checkUserListSubscription(id1.screenName, userList.getId(), id4.id);
            assertNotNull(DataObjectFactory.getRawJSON(user));
            assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
            assertEquals(id4.id, user.getId());
        } catch (TwitterException te) {
            // workarounding issue 1300
            assertEquals(404, te.getStatusCode());
        }

        userLists = twitterAPI1.getUserListSubscriptions(id4.screenName, -1l);
        assertNotNull(userLists);
        if (userLists.size() > 0) {
            assertEquals(userLists.get(0), DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userLists.get(0))));
        }
//        assertEquals(1, userLists.size()); workarounding issue 1300

        try {
            user = twitterAPI1.checkUserListSubscription(id1.screenName, id2.id, userList.getId());
            fail("id2 shouldn't be a subscriber the userList. expecting a TwitterException");
        } catch (TwitterException ignore) {
            assertEquals(404, ignore.getStatusCode());
        }

        userList = twitterAPI1.destroyUserList(userList.getId());
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
    }

    public void testUserTimeline() throws Exception {
        List<Status> statuses;
        statuses = twitterAPI1.getUserTimeline();
        assertTrue("size", 0 < statuses.size());
        try {
            statuses = unauthenticated.getUserTimeline("1000");
            assertNotNull(DataObjectFactory.getRawJSON(statuses));
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertTrue("size", 0 < statuses.size());
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertEquals(9737332, statuses.get(0).getUser().getId());
            statuses = unauthenticated.getUserTimeline(1000);
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertNotNull(DataObjectFactory.getRawJSON(statuses));
            assertTrue("size", 0 < statuses.size());
            assertEquals(1000, statuses.get(0).getUser().getId());

            statuses = unauthenticated.getUserTimeline(id1.screenName, new Paging().count(10));
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertNotNull(DataObjectFactory.getRawJSON(statuses));
            assertTrue("size", 0 < statuses.size());
            statuses = unauthenticated.getUserTimeline(id1.screenName, new Paging(999383469l));
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertNotNull(DataObjectFactory.getRawJSON(statuses));
            assertTrue("size", 0 < statuses.size());
        } catch (TwitterException te) {
            // is being rate limited
            assertEquals(400, te.getStatusCode());
        }

        statuses = twitterAPI1.getUserTimeline(new Paging(999383469l));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue("size", 0 < statuses.size());
        statuses = twitterAPI1.getUserTimeline(new Paging(999383469l).count(15));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertTrue("size", 0 < statuses.size());



        statuses = twitterAPI1.getUserTimeline(new Paging(1).count(30));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        List<Status> statuses2 = twitterAPI1.getUserTimeline(new Paging(2).count(15));
        assertEquals(statuses2.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses2.get(0))));
        assertEquals(statuses.get(statuses.size() - 1), statuses2.get(statuses2.size() - 1));
    }

    public void testShowStatus() throws Exception {
//        JSONObject json = new JSONObject();
//        json.append("text", " <%}&lt; foobar <&Cynthia>");
//        System.out.println(json.toString());
//        System.out.println(json.getString("text"));
        Status status;
        status = DataObjectFactory.createStatus("{\"text\":\"\\\\u5e30%u5e30 &lt;%}& foobar &lt;&Cynthia&gt;\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":null,\"truncated\":false,\"entities\":{\"urls\":[],\"hashtags\":[],\"user_mentions\":[]},\"in_reply_to_status_id_str\":null,\"id\":12029015787307008,\"in_reply_to_user_id_str\":null,\"source\":\"web\",\"favorited\":false,\"in_reply_to_status_id\":null,\"in_reply_to_user_id\":null,\"created_at\":\"Tue Dec 07 06:21:55 +0000 2010\",\"retweet_count\":0,\"id_str\":\"12029015787307008\",\"place\":null,\"user\":{\"location\":\"location:\",\"statuses_count\":13405,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0000ff\",\"id\":6358482,\"following\":true,\"favourites_count\":2,\"protected\":false,\"profile_text_color\":\"000000\",\"contributors_enabled\":false,\"description\":\"Hi there, I do test a lot!new\",\"verified\":false,\"profile_sidebar_border_color\":\"87bc44\",\"name\":\"twit4j\",\"profile_background_color\":\"9ae4e8\",\"created_at\":\"Sun May 27 09:52:09 +0000 2007\",\"followers_count\":24,\"geo_enabled\":true,\"profile_background_image_url\":\"http://a3.twimg.com/profile_background_images/179009017/t4j-reverse.gif\",\"follow_request_sent\":false,\"url\":\"http://yusuke.homeip.net/twitter4j/\",\"utc_offset\":-32400,\"time_zone\":\"Alaska\",\"notifications\":false,\"friends_count\":4,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"e0ff92\",\"screen_name\":\"twit4j\",\"id_str\":\"6358482\",\"profile_image_url\":\"http://a3.twimg.com/profile_images/1184543043/t4j-reverse_normal.jpeg\",\"show_all_inline_media\":false,\"listed_count\":3},\"coordinates\":null}");
        assertEquals("\\u5e30%u5e30 <%}& foobar <&Cynthia>", status.getText());

        status = twitterAPI2.showStatus(1000l);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        assertEquals(52, status.getUser().getId());
        Status status2 = twitterAPI1.showStatus(1000l);
        assertEquals(52, status2.getUser().getId());
        assertNotNull(status.getRateLimitStatus());
        assertNotNull(DataObjectFactory.getRawJSON(status2));
        assertEquals(status2, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status2)));

        status2 = twitterAPI1.showStatus(999383469l);
        assertNotNull(DataObjectFactory.getRawJSON(status2));
        assertEquals(status2, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status2)));
        assertEquals("01010100 01110010 01101001 01110101 01101101 01110000 01101000       <3", status2.getText());
        status2 = twitterAPI1.showStatus(12029015787307008l);
        assertNotNull(DataObjectFactory.getRawJSON(status2));
        assertEquals(status2, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status2)));
        System.out.println(DataObjectFactory.getRawJSON(status2));
        assertEquals("\\u5e30%u5e30 <%}& foobar <&Cynthia>", status2.getText());
    }

    public void testStatusMethods() throws Exception {
        String date = new java.util.Date().toString() + "test";
        Status status = twitterAPI1.updateStatus(date);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));

        assertEquals(date, status.getText());
        Status status2 = twitterAPI2.updateStatus("@" + id1.screenName + " " + date, status.getId());
        assertNotNull(DataObjectFactory.getRawJSON(status2));
        assertEquals(status2, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status2)));
        assertEquals("@" + id1.screenName + " " + date, status2.getText());
        assertEquals(status.getId(), status2.getInReplyToStatusId());
        assertEquals(twitterAPI1.verifyCredentials().getId(), status2.getInReplyToUserId());
        status = twitterAPI1.destroyStatus(status.getId());
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
    }

    public void testRetweetMethods() throws Exception {
        List<Status> statuses = twitterAPI1.getRetweetedByMe();
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitterAPI1.getRetweetedByMe(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitterAPI1.getRetweetedToMe();
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitterAPI1.getRetweetedToMe(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitterAPI1.getRetweetsOfMe();
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
//        assertIsRetweet(statuses);
        statuses = twitterAPI1.getRetweetsOfMe(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
//        assertIsRetweet(statuses);
        statuses = twitterAPI1.getRetweets(18594701629l);
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        assertTrue(20 < statuses.size());

        for(Status status: statuses){
            if(null != status.getRetweetedStatus()){
                List<User> users = twitterAPI1.getRetweetedBy(status.getRetweetedStatus().getId());
                assertNotNull(DataObjectFactory.getRawJSON(users));
                assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
                assertNotNull(users);
                assertTrue(users.size() > 1);
                break;
            }
        }
    }

    private void assertIsRetweet(List<Status> statuses) {
        for(Status status : statuses){
            assertTrue(status.getText().startsWith("RT "));
        }
    }

    public void testGeoLocation() throws Exception {
        final double LATITUDE = 12.3456;
        final double LONGITUDE = -34.5678;

        Status withgeo = twitterAPI1.updateStatus(new java.util.Date().toString() + ": updating geo location", new GeoLocation(LATITUDE, LONGITUDE));
        assertNotNull(DataObjectFactory.getRawJSON(withgeo));
        assertEquals(withgeo, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(withgeo)));
        assertTrue(withgeo.getUser().isGeoEnabled());
        assertEquals(LATITUDE, withgeo.getGeoLocation().getLatitude());
        assertEquals(LONGITUDE, withgeo.getGeoLocation().getLongitude());
        assertFalse(twitterAPI2.verifyCredentials().isGeoEnabled());
    }

//    public void testAnnotations() throws Exception {
//    	final String failMessage =
//    		"Annotations were not added to the status, please make sure that your account is whitelisted for Annotations by Twitter";
//    	Annotation annotation = new Annotation("review");
//    	annotation.attribute("content", "Yahoo! landing page").
//    		attribute("url", "http://yahoo.com").attribute("rating", "0.6");
//
//    	StatusUpdate update = new StatusUpdate(new java.util.Date().toString() + ": annotated status");
//    	update.addAnnotation(annotation);
//
//        Status withAnnos = twitterAPI1.updateStatus(update);
//        Annotations annotations = withAnnos.getAnnotations();
//        assertNotNull(failMessage, annotations);
//
//        List<Annotation> annos = annotations.getAnnotations();
//        assertEquals(1, annos.size());
//        assertEquals(annotation, annos.get(0));
//    }

    public void testGetFriendsStatuses() throws Exception {
        PagableResponseList<User> users = twitterAPI1.getFriendsStatuses();
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull("friendsStatuses", users);

        users = twitterAPI1.getFriendsStatuses(numberId);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull("friendsStatuses", users);
        assertEquals(id1.screenName, users.get(0).getScreenName());

        users = twitterAPI1.getFriendsStatuses(numberIdId);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull("friendsStatuses", users);
        assertEquals(id1.screenName, users.get(0).getScreenName());

        try {
            users = unauthenticated.getFriendsStatuses("yusukey");
            assertNotNull(DataObjectFactory.getRawJSON(users));
            assertNotNull("friendsStatuses", users);
            assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        } catch (TwitterException te) {
            // is being rate limited
            assertEquals(400, te.getStatusCode());
        }
    }

    public void testRelationship() throws Exception {
        //  TESTING PRECONDITIONS:
        //  1) id1 is followed by "followsOneWay", but not following "followsOneWay"
        Relationship rel1 = twitterAPI1.showFriendship(id1.screenName, followsOneWay);
        assertNotNull(DataObjectFactory.getRawJSON(rel1));
        assertEquals(rel1, DataObjectFactory.createRelationship(DataObjectFactory.getRawJSON(rel1)));

        // test second precondition
        assertNotNull(rel1);
        assertTrue(rel1.isSourceFollowedByTarget());
        assertFalse(rel1.isSourceFollowingTarget());
        assertTrue(rel1.isTargetFollowingSource());
        assertFalse(rel1.isTargetFollowedBySource());

        //  2) best_friend1 is following and followed by best_friend2
        Relationship rel2 = twitterAPI1.showFriendship(bestFriend1.screenName, bestFriend2.screenName);
        assertNull(DataObjectFactory.getRawJSON(rel1));
        assertNotNull(DataObjectFactory.getRawJSON(rel2));
        assertEquals(rel2, DataObjectFactory.createRelationship(DataObjectFactory.getRawJSON(rel2)));

        // test second precondition
        assertNotNull(rel2);
        assertTrue(rel2.isSourceFollowedByTarget());
        assertTrue(rel2.isSourceFollowingTarget());
        assertTrue(rel2.isTargetFollowingSource());
        assertTrue(rel2.isTargetFollowedBySource());

        // test equality
        Relationship rel3 = twitterAPI1.showFriendship(id1.screenName, followsOneWay);
        assertNotNull(DataObjectFactory.getRawJSON(rel3));
        assertEquals(rel3, DataObjectFactory.createRelationship(DataObjectFactory.getRawJSON(rel3)));
        assertEquals(rel1, rel3);
        assertFalse(rel1.equals(rel2));
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


    public void testFriendships() throws Exception {
        IDs ids;
        ids = twitterAPI4.getIncomingFriendships(-1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertTrue(ids.getIDs().length > 0);
        ids = twitterAPI2.getOutgoingFriendships(-1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertTrue(ids.getIDs().length > 0);
    }

    public void testSocialGraphMethods() throws Exception {
        IDs ids;
        ids = twitterAPI1.getFriendsIDs();
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        int yusukey = 4933401;
        assertIDExsits("twit4j is following yusukey", ids, yusukey);
        int ryunosukey = 48528137;
        ids = twitterAPI1.getFriendsIDs(ryunosukey);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertEquals("ryunosukey is not following anyone", 0, ids.getIDs().length);
        ids = twitterAPI1.getFriendsIDs("yusukey");
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertIDExsits("yusukey is following ryunosukey", ids, ryunosukey);
        IDs obamaFollowers;
        obamaFollowers = twitterAPI1.getFollowersIDs("barackobama");
        assertNotNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertEquals(obamaFollowers, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFollowers)));
        assertTrue(obamaFollowers.hasNext());
        assertFalse(obamaFollowers.hasPrevious());
        obamaFollowers = twitterAPI1.getFollowersIDs("barackobama", obamaFollowers.getNextCursor());
        assertEquals(obamaFollowers, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFollowers)));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertTrue(obamaFollowers.hasNext());
        assertTrue(obamaFollowers.hasPrevious());

        obamaFollowers = twitterAPI1.getFollowersIDs(813286);
        assertNotNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertEquals(obamaFollowers, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFollowers)));
        assertTrue(obamaFollowers.hasNext());
        assertFalse(obamaFollowers.hasPrevious());
        obamaFollowers = twitterAPI1.getFollowersIDs(813286, obamaFollowers.getNextCursor());
        assertNotNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertEquals(obamaFollowers, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFollowers)));
        assertTrue(obamaFollowers.hasNext());
        assertTrue(obamaFollowers.hasPrevious());

        IDs obamaFriends;
        obamaFriends = twitterAPI1.getFriendsIDs("barackobama");
        assertNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFriends));
        assertEquals(obamaFriends, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFriends)));
        assertTrue(obamaFriends.hasNext());
        assertFalse(obamaFriends.hasPrevious());
        obamaFriends = twitterAPI1.getFriendsIDs("barackobama", obamaFriends.getNextCursor());
        assertNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFriends));
        assertEquals(obamaFriends, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFriends)));
        assertTrue(obamaFriends.hasNext());
        assertTrue(obamaFriends.hasPrevious());

        obamaFriends = twitterAPI1.getFriendsIDs(813286);
        assertNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFriends));
        assertEquals(obamaFriends, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFriends)));
        assertTrue(obamaFriends.hasNext());
        assertFalse(obamaFriends.hasPrevious());
        obamaFriends = twitterAPI1.getFriendsIDs(813286, obamaFriends.getNextCursor());
        assertNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFriends));
        assertEquals(obamaFriends, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFriends)));
        assertTrue(obamaFriends.hasNext());
        assertTrue(obamaFriends.hasPrevious());

        try {
            twitterAPI2.createFriendship(id1.screenName);
        } catch (TwitterException ignore) {
        }
        ids = twitterAPI1.getFollowersIDs();
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertIDExsits("twit4j2 is following twit4j", ids, 6377362);
        ids = twitterAPI1.getFollowersIDs(ryunosukey);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertIDExsits("yusukey is following ryunosukey", ids, yusukey);
        ids = twitterAPI1.getFollowersIDs("ryunosukey");
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertIDExsits("yusukey is following ryunosukey", ids, yusukey);
    }

    public void testAccountMethods() throws Exception {
        User original = twitterAPI1.verifyCredentials();
        assertNotNull(DataObjectFactory.getRawJSON(original));
        assertEquals(original, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(original)));
        if (original.getScreenName().endsWith("new") ||
                original.getName().endsWith("new")) {
            original = twitterAPI1.updateProfile(
                    "twit4j", "http://yusuke.homeip.net/twitter4j/"
                    , "location:", "Hi there, I do test a lot!new");

        }
        String newName, newURL, newLocation, newDescription;
        String neu = "new";
        newName = original.getName() + neu;
        newURL = original.getURL() + neu;
        newLocation = original.getLocation() + neu;
        newDescription = original.getDescription() + neu;

        User altered = twitterAPI1.updateProfile(
                newName, newURL, newLocation, newDescription);
        assertNotNull(DataObjectFactory.getRawJSON(altered));
        assertEquals(original, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(original)));
        assertEquals(altered, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(altered)));
        twitterAPI1.updateProfile(original.getName(), original.getURL().toString(), original.getLocation(), original.getDescription());
        assertEquals(newName, altered.getName());
        assertEquals(newURL, altered.getURL().toString());
        assertEquals(newLocation, altered.getLocation());
        assertEquals(newDescription, altered.getDescription());

        try {
            new TwitterFactory().getInstance("doesnotexist--", "foobar").verifyCredentials();
            fail("should throw TwitterException");
        } catch (TwitterException te) {
        }

        assertTrue(twitterAPIBestFriend1.existsFriendship(bestFriend1.screenName, bestFriend2.screenName));
        assertFalse(twitterAPI1.existsFriendship(id1.screenName, "al3x"));

        User eu;
        eu = twitterAPI1.updateProfileColors("f00", "f0f", "0ff", "0f0", "f0f");
        assertNotNull(DataObjectFactory.getRawJSON(eu));
        assertEquals(eu, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(eu)));
        assertEquals("f00", eu.getProfileBackgroundColor());
        assertEquals("f0f", eu.getProfileTextColor());
        assertEquals("0ff", eu.getProfileLinkColor());
        assertEquals("0f0", eu.getProfileSidebarFillColor());
        assertEquals("f0f", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors("f0f", "f00", "f0f", "0ff", "0f0");
        assertNotNull(DataObjectFactory.getRawJSON(eu));
        assertEquals(eu, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(eu)));
        assertEquals("f0f", eu.getProfileBackgroundColor());
        assertEquals("f00", eu.getProfileTextColor());
        assertEquals("f0f", eu.getProfileLinkColor());
        assertEquals("0ff", eu.getProfileSidebarFillColor());
        assertEquals("0f0", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors("87bc44", "9ae4e8", "000000", "0000ff", "e0ff92");
        assertNotNull(DataObjectFactory.getRawJSON(eu));
        assertEquals(eu, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(eu)));
        assertEquals("87bc44", eu.getProfileBackgroundColor());
        assertEquals("9ae4e8", eu.getProfileTextColor());
        assertEquals("000000", eu.getProfileLinkColor());
        assertEquals("0000ff", eu.getProfileSidebarFillColor());
        assertEquals("e0ff92", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors("f0f", null, "f0f", null, "0f0");
        assertNotNull(DataObjectFactory.getRawJSON(eu));
        assertEquals(eu, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(eu)));
        assertEquals("f0f", eu.getProfileBackgroundColor());
        assertEquals("9ae4e8", eu.getProfileTextColor());
        assertEquals("f0f", eu.getProfileLinkColor());
        assertEquals("0000ff", eu.getProfileSidebarFillColor());
        assertEquals("0f0", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors(null, "f00", null, "0ff", null);
        assertNotNull(DataObjectFactory.getRawJSON(eu));
        assertEquals(eu, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(eu)));
        assertEquals("f0f", eu.getProfileBackgroundColor());
        assertEquals("f00", eu.getProfileTextColor());
        assertEquals("f0f", eu.getProfileLinkColor());
        assertEquals("0ff", eu.getProfileSidebarFillColor());
        assertEquals("0f0", eu.getProfileSidebarBorderColor());
        eu = twitterAPI1.updateProfileColors("9ae4e8", "000000", "0000ff", "e0ff92", "87bc44");
        assertNotNull(DataObjectFactory.getRawJSON(eu));
        assertEquals(eu, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(eu)));
        assertEquals("9ae4e8", eu.getProfileBackgroundColor());
        assertEquals("000000", eu.getProfileTextColor());
        assertEquals("0000ff", eu.getProfileLinkColor());
        assertEquals("e0ff92", eu.getProfileSidebarFillColor());
        assertEquals("87bc44", eu.getProfileSidebarBorderColor());
    }

    public void testAccountProfileImageUpdates() throws Exception {
        User user = twitterAPI1.updateProfileImage(getRandomlyChosenFile());
        assertNotNull(DataObjectFactory.getRawJSON(user));
        // tile randomly
        User user2 = twitterAPI1.updateProfileBackgroundImage(getRandomlyChosenFile(),
                (5 < System.currentTimeMillis() % 5));
        assertNotNull(DataObjectFactory.getRawJSON(user2));
        assertEquals(user2, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user2)));
    }
    static final String[] files = {"src/test/resources/t4j-reverse.jpeg",
            "src/test/resources/t4j-reverse.png",
            "src/test/resources/t4j-reverse.gif",
            "src/test/resources/t4j.jpeg",
            "src/test/resources/t4j.png",
            "src/test/resources/t4j.gif",
    };

    public static File getRandomlyChosenFile(){
        int rand = (int) (System.currentTimeMillis() % 6);
        File file = new File(files[rand]);
        if(!file.exists()){
            file = new File("twitter4j-core/"+ files[rand]);
        }
        return file;
    }

    public void testFavoriteMethods() throws Exception {
        Status status = twitterAPI1.getHomeTimeline(new Paging().count(1)).get(0);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        status = twitterAPI2.createFavorite(status.getId());
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        assertTrue(twitterAPI2.getFavorites().size() > 0);
        try {
            twitterAPI2.destroyFavorite(status.getId());
        } catch (TwitterException te) {
            // sometimes destorying favorite fails with 404
            assertEquals(404, te.getStatusCode());
        }
    }

    public void testFollowers() throws Exception {
        PagableResponseList<User> actualReturn = twitterAPI1.getFollowersStatuses();
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(actualReturn.get(0))));
        assertTrue(actualReturn.size() > 0);
        actualReturn = twitterAPI1.getFollowersStatuses();
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(actualReturn.get(0))));
        assertTrue(actualReturn.size() > 0);
        assertFalse(actualReturn.hasNext());
        assertFalse(actualReturn.hasPrevious());
        actualReturn = twitterAPI1.getFollowersStatuses(actualReturn.getNextCursor());
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(0, actualReturn.size());

        actualReturn = twitterAPI2.getFollowersStatuses();
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(actualReturn.get(0))));
        assertTrue(actualReturn.size() > 0);
        actualReturn = twitterAPI2.getFollowersStatuses("yusukey");
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(actualReturn.get(0))));
        assertTrue(actualReturn.size() > 60);
        actualReturn = twitterAPI2.getFollowersStatuses("yusukey", actualReturn.getNextCursor());
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(actualReturn.get(0))));
        assertTrue(actualReturn.size() > 10);
        // - Issue 1572: Lists previous cursor returns empty results
        // http://code.google.com/p/twitter-api/issues/detail?id=1572
//        actualReturn = twitterAPI2.getFollowersStatuses("yusukey", actualReturn.getPreviousCursor());
//        assertTrue(actualReturn.size() > 10);
    }

    public void testDirectMessages() throws Exception {
        String expectedReturn = new Date() + ":directmessage test";
        DirectMessage actualReturn = twitterAPI1.sendDirectMessage("twit4jnoupdate", expectedReturn);
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn, DataObjectFactory.createDirectMessage(DataObjectFactory.getRawJSON(actualReturn)));
        assertEquals(expectedReturn, actualReturn.getText());
        assertEquals(id1.screenName, actualReturn.getSender().getScreenName());
        assertEquals("twit4jnoupdate", actualReturn.getRecipient().getScreenName());
        List<DirectMessage> actualReturnList = twitterAPI1.getDirectMessages();
        assertNull(DataObjectFactory.getRawJSON(actualReturn));
        assertNotNull(DataObjectFactory.getRawJSON(actualReturnList));
        assertEquals(actualReturnList.get(0), DataObjectFactory.createDirectMessage(DataObjectFactory.getRawJSON(actualReturnList.get(0))));
        assertTrue(1 <= actualReturnList.size());
    }

    public void testCreateDestroyFriend() throws Exception {
        User user;
        try {
            user = twitterAPI2.destroyFriendship(id1.screenName);
            assertNotNull(DataObjectFactory.getRawJSON(user));
            assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        } catch (TwitterException te) {
            //ensure destory id1 before the actual test
            //ensure destory id1 before the actual test
        }

        try {
            user = twitterAPI2.destroyFriendship(id1.screenName);
            assertNotNull(DataObjectFactory.getRawJSON(user));
            assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        } catch (TwitterException te) {
            assertEquals(403, te.getStatusCode());
        }
        user = twitterAPI2.createFriendship(id1.screenName, true);
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        assertEquals(id1.screenName, user.getScreenName());
        // the Twitter API is not returning appropriate notifications value
        // http://code.google.com/p/twitter-api/issues/detail?id=474
//        User detail = twitterAPI2.showUser(id1);
//        assertTrue(detail.isNotificationEnabled());
        try {
            user = twitterAPI2.createFriendship(id2.screenName);
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

    public void testGetMentions() throws Exception {
        Status status = twitterAPI2.updateStatus("@" + id1.screenName + " reply to id1 " + new java.util.Date());
        assertNotNull(DataObjectFactory.getRawJSON(status));
        List<Status> statuses = twitterAPI1.getMentions();
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);

        statuses = twitterAPI1.getMentions(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);
        statuses = twitterAPI1.getMentions(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);
        statuses = twitterAPI1.getMentions(new Paging(1, 1l));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);
        statuses = twitterAPI1.getMentions(new Paging(1l));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);
    }

    public void testNotification() throws Exception {
        try {
            twitterAPI1.disableNotification("twit4jprotected");
        } catch (TwitterException te) {
        }
        User user1 = twitterAPI1.enableNotification("twit4jprotected");
        assertNotNull(DataObjectFactory.getRawJSON(user1));
        assertEquals(user1, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user1)));
        User user2 = twitterAPI1.disableNotification("twit4jprotected");
        assertNotNull(DataObjectFactory.getRawJSON(user2));
        assertEquals(user2, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user2)));
    }

    public void testBlock() throws Exception {
        User user1 = twitterAPI2.createBlock(id1.screenName);
        assertNotNull(DataObjectFactory.getRawJSON(user1));
        assertEquals(user1, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user1)));
        User user2 = twitterAPI2.destroyBlock(id1.screenName);
        assertNotNull(DataObjectFactory.getRawJSON(user2));
        assertEquals(user2, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user2)));
        assertFalse(twitterAPI1.existsBlock("twit4j2"));
        assertTrue(twitterAPI1.existsBlock("twit4jblock"));
        List<User> users = twitterAPI1.getBlockingUsers();
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());
        users = twitterAPI1.getBlockingUsers(1);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());

        IDs ids = twitterAPI1.getBlockingUsersIDs();
        assertNull(DataObjectFactory.getRawJSON(users));
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(1, ids.getIDs().length);
        assertEquals(39771963, ids.getIDs()[0]);
    }

    public void testLocalTrendsMethods() throws Exception {
        ResponseList<Location> locations;
        locations = twitterAPI1.getAvailableTrends();
        assertNotNull(DataObjectFactory.getRawJSON(locations));
        assertEquals(locations.get(0), DataObjectFactory.createLocation(DataObjectFactory.getRawJSON(locations.get(0))));
        assertTrue(locations.size() > 0);
        locations = twitterAPI1.getAvailableTrends(new GeoLocation(0,0));
        assertNotNull(DataObjectFactory.getRawJSON(locations));
        assertTrue(locations.size() > 0);

        Trends trends = twitterAPI1.getLocationTrends(locations.get(0).getWoeid());
        System.out.println(DataObjectFactory.getRawJSON(trends));
        assertEquals(trends, DataObjectFactory.createTrends(DataObjectFactory.getRawJSON(trends)));
        assertNull(DataObjectFactory.getRawJSON(locations));
        System.out.println(DataObjectFactory.getRawJSON(trends));
        assertNotNull(DataObjectFactory.getRawJSON(trends));
        assertEquals(locations.get(0), trends.getLocation());
        assertTrue(trends.getTrends().length > 0);

        try {
            trends = twitterAPI1.getLocationTrends(2345889/*woeid of Tokyo*/);
            fail("should fail.");
        } catch (Exception ignore) {
        }
        assertEquals(locations.get(0), trends.getLocation());
        assertTrue(trends.getTrends().length > 0);
    }

    RateLimitStatus rateLimitStatus = null;
    boolean accountLimitStatusAcquired;
    boolean ipLimitStatusAcquired;
    //need to think of a way to test this, perhaps mocking out Twitter is the way to go
    public void testRateLimitStatus() throws Exception {
        RateLimitStatus rateLimitStatus = twitterAPI1.getRateLimitStatus();
        assertNotNull(DataObjectFactory.getRawJSON(rateLimitStatus));
        assertEquals(rateLimitStatus, DataObjectFactory.createRateLimitStatus(DataObjectFactory.getRawJSON(rateLimitStatus)));
        assertTrue(10 < rateLimitStatus.getHourlyLimit());
        assertTrue(10 < rateLimitStatus.getRemainingHits());

        twitterAPI1.setRateLimitStatusListener(new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                System.out.println("onRateLimitStatus"+event);
                accountLimitStatusAcquired = event.isAccountRateLimitStatus();
                ipLimitStatusAcquired = event.isIPRateLimitStatus();
                TwitterTest.this.rateLimitStatus = event.getRateLimitStatus();
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {

            }

        });
        // the listener doesn't implement serializable and deserialized form should not be equal to the original object
        assertDeserializedFormIsNotEqual(twitterAPI1);

        unauthenticated.setRateLimitStatusListener(new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                accountLimitStatusAcquired = event.isAccountRateLimitStatus();
                ipLimitStatusAcquired = event.isIPRateLimitStatus();
                TwitterTest.this.rateLimitStatus = event.getRateLimitStatus();
            }
            public void onRateLimitReached(RateLimitStatusEvent event){
            }
        });
        // the listener doesn't implement serializable and deserialized form should not be equal to the original object
        assertDeserializedFormIsNotEqual(unauthenticated);

        twitterAPI1.getMentions();
        assertTrue(accountLimitStatusAcquired);
        assertFalse(ipLimitStatusAcquired);
        RateLimitStatus previous = this.rateLimitStatus;
        twitterAPI1.getMentions();
        assertTrue(accountLimitStatusAcquired);
        assertFalse(ipLimitStatusAcquired);
        assertTrue(previous.getRemainingHits() > this.rateLimitStatus.getRemainingHits());
        assertEquals(previous.getHourlyLimit(), this.rateLimitStatus.getHourlyLimit());

        try {
            unauthenticated.getPublicTimeline();
            assertFalse(accountLimitStatusAcquired);
            assertTrue(ipLimitStatusAcquired);
            previous = this.rateLimitStatus;
            unauthenticated.getPublicTimeline();
            assertFalse(accountLimitStatusAcquired);
            assertTrue(ipLimitStatusAcquired);
            assertTrue(previous.getRemainingHits() > this.rateLimitStatus.getRemainingHits());
            assertEquals(previous.getHourlyLimit(), this.rateLimitStatus.getHourlyLimit());
        } catch (TwitterException te) {
            // is being rate limited;
            assertEquals(400, te.getStatusCode());
        }
    }

    /* Spam Reporting Methods */
    public void testReportSpammerSavedSearches() throws Exception {
        // Not sure they're accepting multiple spam reports for the same user.
        // Do we really need to test this method? How?
    }

    /* Saved Searches Methods */
    public void testSavedSearches() throws Exception {
        List<SavedSearch> list = twitterAPI1.getSavedSearches();
        assertNotNull(DataObjectFactory.getRawJSON(list));
        for (SavedSearch savedSearch : list) {
            twitterAPI1.destroySavedSearch(savedSearch.getId());
        }
        SavedSearch ss1 = twitterAPI1.createSavedSearch("my search");
        assertNotNull(DataObjectFactory.getRawJSON(ss1));
        assertEquals(ss1, DataObjectFactory.createSavedSearch(DataObjectFactory.getRawJSON(ss1)));
        assertEquals("my search", ss1.getQuery());
        assertEquals(-1, ss1.getPosition());
        list = twitterAPI1.getSavedSearches();
        assertNotNull(DataObjectFactory.getRawJSON(list));
        assertEquals(list.get(0), DataObjectFactory.createSavedSearch(DataObjectFactory.getRawJSON(list.get(0))));
        // http://code.google.com/p/twitter-api/issues/detail?id=1032
        // the saved search may not be immediately available
        assertTrue(0 <= list.size());
        try {
            SavedSearch ss2 = twitterAPI1.destroySavedSearch(ss1.getId());
            assertEquals(ss1, ss2);
        } catch (TwitterException te) {
            // sometimes it returns 404 or 500 when its out of sync.
            assertTrue(404 == te.getStatusCode() || 500 == te.getStatusCode());
        }
    }

    public void testGeoMethods() throws Exception {
        GeoQuery query;
        ResponseList<Place> places;
        query = new GeoQuery(new GeoLocation(0, 0));

        places = twitterAPI1.reverseGeoCode(query);
        assertEquals(0, places.size());

        query = new GeoQuery(new GeoLocation(37.78215, -122.40060));
        places = twitterAPI1.reverseGeoCode(query);
        assertNotNull(DataObjectFactory.getRawJSON(places));
        assertEquals(places.get(0), DataObjectFactory.createPlace(DataObjectFactory.getRawJSON(places.get(0))));

        assertTrue(places.size() > 0);
        places = twitterAPI1.getNearbyPlaces(query);
        assertNotNull(DataObjectFactory.getRawJSON(places));
        for(Place place : places){
            System.out.println(place.getName());
        }
        assertTrue(places.size() > 0);
        places = twitterAPI1.searchPlaces(query);
        assertNotNull(DataObjectFactory.getRawJSON(places));
        assertEquals(places.get(0), DataObjectFactory.createPlace(DataObjectFactory.getRawJSON(places.get(0))));
        assertTrue(places.size() > 0);
        places = twitterAPI1.getSimilarPlaces(new GeoLocation(37.78215, -122.40060), "SoMa", null, null);
        assertNotNull(DataObjectFactory.getRawJSON(places));
        assertEquals(places.get(0), DataObjectFactory.createPlace(DataObjectFactory.getRawJSON(places.get(0))));
        assertTrue(places.size() > 0);

        try{
            Place place = this.unauthenticated.getGeoDetails("5a110d312052166f");
            assertNotNull(DataObjectFactory.getRawJSON(place));
            assertEquals(place, DataObjectFactory.createPlace(DataObjectFactory.getRawJSON(place)));
            assertEquals("San Francisco, CA", place.getFullName());
            assertEquals("California, US", place.getContainedWithIn()[0].getFullName());
        } catch (TwitterException te) {
            // is being rate limited
            assertEquals(400, te.getStatusCode());
        }
        String sanFrancisco = "5a110d312052166f";
        Status status = twitterAPI1.updateStatus(new StatusUpdate(new java.util.Date() + " status with place").
                placeId(sanFrancisco));
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        assertEquals(sanFrancisco, status.getPlace().getId());
        assertEquals(null, status.getContributors());
    }
    public void testLegalResources() throws Exception{
        assertNotNull(twitterAPI1.getTermsOfService());
        assertNotNull(twitterAPI1.getPrivacyPolicy());
    }

    public void testRelatedResults() throws Exception {
        RelatedResults relatedResults = twitterAPI1.getRelatedResults(999383469l);
        assertNotNull(relatedResults);
        ResponseList<Status> statuses;
        statuses = relatedResults.getTweetsFromUser();
        if(statuses.size() > 0){
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
        statuses = relatedResults.getTweetsWithConversation();
        if(statuses.size() > 0){
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
        statuses = relatedResults.getTweetsWithReply();
        if(statuses.size() > 0){
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
    }

    public void testTest() throws Exception {
        assertTrue(twitterAPI2.test());
    }

    public void testEntities() throws Exception {
        Status status = twitterAPI2.showStatus(22035985122L);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        assertEquals(2, status.getUserMentions().length);
        assertEquals(1, status.getURLs().length);

        User user1 = status.getUserMentions()[0];
        assertEquals(20263710, user1.getId());
        assertEquals("rabois", user1.getScreenName());
        assertEquals("Keith Rabois", user1.getName());
        assertEquals(new URL("http://j.mp/cHv0VS"), status.getURLs()[0]);

        status = twitterAPI2.showStatus(22043496385L);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        assertEquals(2, status.getHashtags().length);
        assertEquals("pilaf", status.getHashtags()[0]);
        assertEquals("recipe", status.getHashtags()[1]);
    }
}
