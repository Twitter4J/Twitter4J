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

import twitter4j.json.DataObjectFactory;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class FriendsFollowersResourcesTest extends TwitterTestBase {
    public FriendsFollowersResourcesTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSocialGraphMethods() throws Exception {
        IDs ids;
        ids = twitter1.getFriendsIDs(-1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        int yusukey = 4933401;
        assertIDExsits("twit4j is following yusukey", ids, yusukey);
        long ryunosukey = 48528137;
        ids = twitter1.getFriendsIDs(ryunosukey, -1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertEquals("ryunosukey is not following anyone", 0, ids.getIDs().length);
        ids = twitter1.getFriendsIDs("yusukey", -1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertIDExsits("yusukey is following ryunosukey", ids, ryunosukey);
        IDs obamaFollowers;
        obamaFollowers = twitter1.getFollowersIDs("barackobama", -1);
        assertNotNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertEquals(obamaFollowers, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFollowers)));
        assertTrue(obamaFollowers.hasNext());
        assertFalse(obamaFollowers.hasPrevious());
        obamaFollowers = twitter1.getFollowersIDs("barackobama", obamaFollowers.getNextCursor());
        assertEquals(obamaFollowers, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFollowers)));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertTrue(obamaFollowers.hasNext());
        assertTrue(obamaFollowers.hasPrevious());

        obamaFollowers = twitter1.getFollowersIDs(813286, -1);
        assertNotNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertEquals(obamaFollowers, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFollowers)));
        assertTrue(obamaFollowers.hasNext());
        assertFalse(obamaFollowers.hasPrevious());
        obamaFollowers = twitter1.getFollowersIDs(813286, obamaFollowers.getNextCursor());
        assertNotNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertEquals(obamaFollowers, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFollowers)));
        assertTrue(obamaFollowers.hasNext());
        assertTrue(obamaFollowers.hasPrevious());

        IDs obamaFriends;
        obamaFriends = twitter1.getFriendsIDs("barackobama", -1);
        assertNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFriends));
        assertEquals(obamaFriends, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFriends)));
        assertTrue(obamaFriends.hasNext());
        assertFalse(obamaFriends.hasPrevious());
        obamaFriends = twitter1.getFriendsIDs("barackobama", obamaFriends.getNextCursor());
        assertNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFriends));
        assertEquals(obamaFriends, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFriends)));
        assertTrue(obamaFriends.hasNext());
        assertTrue(obamaFriends.hasPrevious());

        obamaFriends = twitter1.getFriendsIDs(813286, -1);
        assertNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFriends));
        assertEquals(obamaFriends, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFriends)));
        assertTrue(obamaFriends.hasNext());
        assertFalse(obamaFriends.hasPrevious());
        obamaFriends = twitter1.getFriendsIDs(813286, obamaFriends.getNextCursor());
        assertNull(DataObjectFactory.getRawJSON(obamaFollowers));
        assertNotNull(DataObjectFactory.getRawJSON(obamaFriends));
        assertEquals(obamaFriends, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(obamaFriends)));
        assertTrue(obamaFriends.hasNext());
        assertTrue(obamaFriends.hasPrevious());

        try {
            twitter2.createFriendship(id1.screenName);
        } catch (TwitterException ignore) {
        }
        ids = twitter1.getFollowersIDs(-1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertIDExsits("twit4j2 is following twit4j", ids, 6377362);
        ids = twitter1.getFollowersIDs(ryunosukey, -1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertIDExsits("yusukey is following ryunosukey", ids, yusukey);
        ids = twitter1.getFollowersIDs("ryunosukey", -1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertIDExsits("yusukey is following ryunosukey", ids, yusukey);
    }

    private void assertIDExsits(String assertion, IDs ids, long idToFind) {
        boolean found = false;
        for (long id : ids.getIDs()) {
            if (id == idToFind) {
                found = true;
                break;
            }
        }
        assertTrue(assertion, found);
    }
    public void testCreateDestroyFriend() throws Exception {
        User user;
        try {
            user = twitter2.destroyFriendship(id1.screenName);
            assertNotNull(DataObjectFactory.getRawJSON(user));
            assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        } catch (TwitterException te) {
            //ensure destory id1 before the actual test
        }

        try {
            user = twitter2.destroyFriendship(id1.screenName);
            assertNotNull(DataObjectFactory.getRawJSON(user));
            assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        } catch (TwitterException te) {
            assertEquals(403, te.getStatusCode());
        }
        user = twitter2.createFriendship(id1.screenName, true);
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        assertEquals(id1.screenName, user.getScreenName());
        // the Twitter API is not returning appropriate notifications value
        // http://code.google.com/p/twitter-api/issues/detail?id=474
//        User detail = twitterAPI2.showUser(id1);
//        assertTrue(detail.isNotificationEnabled());
        try {
            user = twitter2.createFriendship(id2.screenName);
            fail("shouldn't be able to befrinend yourself");
        } catch (TwitterException te) {
            assertEquals(403, te.getStatusCode());
        }
        try {
            user = twitter2.createFriendship("doesnotexist--");
            fail("non-existing user");
        } catch (TwitterException te) {
            //now befriending with non-existing user returns 404
            //http://groups.google.com/group/twitter-development-talk/browse_thread/thread/bd2a912b181bc39f
            assertEquals(404, te.getStatusCode());
        }

    }

    public void testRelationship() throws Exception {
        //  TESTING PRECONDITIONS:
        //  1) id1 is followed by "followsOneWay", but not following "followsOneWay"
        Relationship rel1 = twitter1.showFriendship(id1.screenName, followsOneWay);
        assertNotNull(DataObjectFactory.getRawJSON(rel1));
        assertEquals(rel1, DataObjectFactory.createRelationship(DataObjectFactory.getRawJSON(rel1)));

        // test second precondition
        assertNotNull(rel1);
        assertTrue(rel1.isSourceFollowedByTarget());
        assertFalse(rel1.isSourceFollowingTarget());
        assertTrue(rel1.isTargetFollowingSource());
        assertFalse(rel1.isTargetFollowedBySource());

        //  2) best_friend1 is following and followed by best_friend2
        Relationship rel2 = twitter1.showFriendship(bestFriend1.screenName, bestFriend2.screenName);
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
        Relationship rel3 = twitter1.showFriendship(id1.screenName, followsOneWay);
        assertNotNull(DataObjectFactory.getRawJSON(rel3));
        assertEquals(rel3, DataObjectFactory.createRelationship(DataObjectFactory.getRawJSON(rel3)));
        assertEquals(rel1, rel3);
        assertFalse(rel1.equals(rel2));

        ResponseList<Friendship> friendshipList = twitter1.lookupFriendships(new String[]{"barakobama", id2.screenName, id3.screenName});

        assertEquals(3, friendshipList.size());
        assertEquals("barakobama", friendshipList.get(0).getScreenName());
        assertFalse(friendshipList.get(0).isFollowing());
        assertFalse(friendshipList.get(0).isFollowedBy());
        assertEquals(id3.screenName, friendshipList.get(2).getScreenName());
        assertTrue(friendshipList.get(2).isFollowing());
        assertTrue(friendshipList.get(2).isFollowedBy());
        friendshipList = twitter1.lookupFriendships(new long[]{id2.id, id3.id});
        assertEquals(2, friendshipList.size());

        Relationship relationship = twitter1.updateFriendship(id3.screenName, true, true);
        assertEquals(id3.screenName, relationship.getTargetUserScreenName());

        Relationship updatedRelationship = twitter1.updateFriendship(id3.id, false, false);
        assertEquals(id3.screenName, updatedRelationship.getTargetUserScreenName());
        assertFalse(updatedRelationship.isSourceNotificationsEnabled());
        assertFalse(updatedRelationship.isSourceWantRetweets());
    }

    public void testIncomingOutgoingFriendships() throws Exception {
        IDs ids;
        ids = twitter3.getIncomingFriendships(-1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertTrue(ids.getIDs().length > 0);
        ids = twitter2.getOutgoingFriendships(-1);
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(ids, DataObjectFactory.createIDs(DataObjectFactory.getRawJSON(ids)));
        assertTrue(ids.getIDs().length > 0);
        // needs the account has at least one user who is not received any retweets.
        //assertTrue(ids.getIDs().length > 0);
    }

    public void testFriendsFollowersList() throws Exception {
        PagableResponseList<User> t4jfriends = twitter1.getFriendsList("t4j_news", -1L);
        PagableResponseList<User> t4jfriends2 = twitter1.getFriendsList(72297675L, -1L);
        assertEquals(t4jfriends, t4jfriends2);
        assertTrue(t4jfriends.size() > 0);

        PagableResponseList<User> t4jfollowers = twitter1.getFollowersList("t4j_news", -1L);
        PagableResponseList<User> t4jfollowers2 = twitter1.getFollowersList(72297675L, -1L);
        assertEquals(t4jfollowers, t4jfollowers2);
        assertTrue(t4jfollowers.size() > 0);
    }
}
