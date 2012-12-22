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

import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class ListResourcesTest extends TwitterTestBase {
    public ListResourcesTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testListMethods() throws Exception {
        ResponseList<UserList> userLists;
        UserList userList;
        userList = prepareListTest();
        /*List Methods*/
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        assertEquals("testpoint1", userList.getName());
//        assertEquals("@twit4j/testpoint1", userList.getFullName());
        assertEquals("description1", userList.getDescription());

        userLists = twitter1.getUserLists(id1.screenName);
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertFalse(userLists.size() == 0);

        userList = twitter1.showUserList(userList.getId());
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        userList = twitter1.showUserList(twitter1.getId(), userList.getSlug());
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        List<Status> statuses = twitter1.getUserListStatuses(userList.getId(), new Paging());
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
        statuses = twitter1.getUserListStatuses(userList.getId(), new Paging());
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(statuses);

        userList = twitter1.updateUserList(userList.getId(), "testpoint2", true, "description2");
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        // workarounding issue 2166
        // http://code.google.com/p/twitter-api/issues/detail?id=2166
        userList = twitter1.showUserList(userList.getId());
        assertTrue(userList.isPublic());
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);
        assertEquals("testpoint2", userList.getName());
        assertEquals("description2", userList.getDescription());

        userList = twitter1.updateUserList(twitter1.getId(), userList.getSlug(), "testpoint3", true, "description3");
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertTrue(userList.isPublic());
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);
        assertEquals("testpoint3", userList.getName());
        assertEquals("description3", userList.getDescription());
    }

    public void testListMemberMethods() throws Exception {
        PagableResponseList<UserList> userLists;

        UserList userList;
        userList = prepareListTest();
        /*List Member Methods*/
        User user = null;
        try {
            user = twitter1.showUserListMembership(userList.getId(), id2.id);
            fail("id2 shouldn't be a member of the userList yet. expecting a TwitterException");
        } catch (TwitterException te) {
            assertEquals(404, te.getStatusCode());
        }
        userList = twitter1.createUserListMember(userList.getId(), id2.id);
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        userList = twitter1.createUserListMembers(userList.getId(), new long[]{id3.id});
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        userList = twitter1.createUserListMembers(userList.getId(), new String[]{"yusukey","yusuke"});
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        PagableResponseList<User> users = twitter1.getUserListMembers(userList.getId(), -1);
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList.getMemberCount(), users.size());

        users = twitter1.getUserListMembers(userList.getId(), -1);
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(4, users.size());

        userList = twitter1.destroyUserListMember(userList.getId(), id2.id);
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        // member_count don't get decreased by destroyUserListMember. need to retrieve user list again
        userList = twitter1.showUserList(userList.getId());
        assertEquals(3, userList.getMemberCount());

        user = twitter1.showUserListMembership(userList.getId(), id3.id);
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        assertEquals(id3.id, user.getId());

        userLists = twitter1.getUserListMemberships(id1.screenName, -1l);
        assertNotNull(DataObjectFactory.getRawJSON(userLists));
        assertEquals(userLists.get(0), DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userLists.get(0))));
        assertNotNull(userLists);

        userLists = twitter1.getUserListSubscriptions(id1.screenName, -1l);
        assertNotNull(DataObjectFactory.getRawJSON(userLists));
        if (userLists.size() > 0) {
            assertEquals(userLists.get(0), DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userLists.get(0))));
        }
        assertNotNull(userLists);
        assertEquals(0, userLists.size());
    }

    public void testUsingOwnerScreenName() throws Exception {
        UserList userList;
        userList = prepareListTest();
        String ownerScreenName = id1.screenName;
        String slug = userList.getSlug();

        User user;
        try {
            twitter1.showUserListMembership(ownerScreenName, slug, id2.id);
            fail("id2 shouldn't be a member of the userList yet. expecting a TwitterException");
        } catch (TwitterException te) {
            assertEquals(404, te.getStatusCode());
        }

        userList = twitter1.createUserListMember(ownerScreenName, slug, id2.id);
        assertNotNull(userList);
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));

        userList = twitter1.createUserListMembers(ownerScreenName, slug, new long[]{id3.id, id2.id});
        assertNotNull(userList);
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));

        userList = twitter1.createUserListMembers(ownerScreenName, slug, new String[]{"akr", "yusukey"});
        assertNotNull(userList);
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));

        PagableResponseList<User> users = twitter1.getUserListMembers(ownerScreenName, slug, -1);
        assertNotNull(users);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertTrue(users.size() > 0);
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));

        userList = twitter1.updateUserList(ownerScreenName, slug, slug, true, "new-description");
        assertTrue(userList.isPublic());
        assertEquals("new-description", userList.getDescription());
        assertEquals(0, userList.getSubscriberCount());

        userList = twitter2.createUserListSubscription(ownerScreenName, slug);
        assertNotNull(userList);
        assertNotNull(DataObjectFactory.getRawJSON(userList));

        List<Status> statuses = twitter2.getUserListStatuses(ownerScreenName, slug, new Paging());
        assertNotNull(statuses);
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }

        user = twitter1.showUserListSubscription(ownerScreenName, slug, id2.id);
        assertNotNull(user);
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
        assertEquals(id2.id, user.getId());

        userList = twitter2.showUserList(ownerScreenName, slug);
        assertNotNull(userList);
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertTrue(userList.isPublic());
        assertEquals("testpoint1", userList.getName());
        assertEquals("new-description", userList.getDescription());
        assertEquals(1, userList.getSubscriberCount());

        users = twitter1.getUserListSubscribers(ownerScreenName, slug, -1);
        assertNotNull(users);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(1, users.size());

        userList = twitter2.destroyUserListSubscription(ownerScreenName, slug);
        assertNotNull(userList);
        userList = twitter1.showUserList(ownerScreenName, slug);
        assertEquals(0, userList.getSubscriberCount());

        userList = twitter1.destroyUserListMember(ownerScreenName, slug, id2.id);
        assertNotNull(userList);
        userList = twitter1.showUserList(ownerScreenName, slug);
        assertEquals(3, userList.getMemberCount());

        twitter1.destroyUserList(ownerScreenName, slug);

        try {
            twitter1.showUserList(ownerScreenName, slug);
            fail(String.format("%s/%s was destroyed", ownerScreenName, slug));

        } catch (TwitterException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

    public void testListSubscribersMethods() throws Exception {
        PagableResponseList<UserList> userLists;
        UserList userList;
        userList = prepareListTest();

        /*List Subscribers Methods*/
        PagableResponseList<User> users;

        users = twitter1.getUserListSubscribers(twitter1.getId(), userList.getSlug(), -1);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(0, users.size());
//        try {
        twitter2.createUserListSubscription(userList.getId());
//        } catch (TwitterException te) {
//            workarounding issue 1300
//            http://code.google.com/p/twitter-api/issues/detail?id=1300
//            assertEquals(404, te.getStatusCode());
//        }
        // expected subscribers: id2
        try {
            twitter3.createUserListSubscription(userList.getId());
        } catch (TwitterException te) {
            // workarounding issue 1300
            assertEquals(404, te.getStatusCode());
        }
        // expected subscribers: id2 and id4
        try {
            twitter2.destroyUserListSubscription(userList.getId());
        } catch (TwitterException te) {
            // workarounding issue 1300
            assertEquals(404, te.getStatusCode());
        }
        // expected subscribers: id4
        users = twitter1.getUserListSubscribers(userList.getId(), -1);
//        assertEquals(1, users.size()); //only id4 should be subscribing the userList
        assertTrue(0 <= users.size()); // workarounding issue 1300
        User user;
        try {
            user = twitter1.showUserListSubscription(userList.getId(), id3.id);
            assertNotNull(DataObjectFactory.getRawJSON(user));
            assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
            assertEquals(id3.id, user.getId());
        } catch (TwitterException te) {
            // workarounding issue 1300
            assertEquals(404, te.getStatusCode());
        }

        userLists = twitter1.getUserListSubscriptions(id3.screenName, -1l);
        assertNotNull(userLists);
        if (userLists.size() > 0) {
            assertEquals(userLists.get(0), DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userLists.get(0))));
        }
//        assertEquals(1, userLists.size()); workarounding issue 1300

        try {
            user = twitter1.showUserListSubscription(userList.getId(), id2.id);
            fail("id2 shouldn't be a subscriber the userList. expecting a TwitterException");
        } catch (TwitterException ignore) {
            assertEquals(404, ignore.getStatusCode());
        }

        userList = twitter1.destroyUserList(userList.getId());
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
    }

    // test case for TFJ-726
    public void testPagingCountDosentWork1() throws TwitterException {
        final int COUNT = 10;

        Paging paging = new Paging();
        paging.count(COUNT);

        // twitterapi/team
        ResponseList<Status> res =
                twitter1
                        .getUserListStatuses(6253282L, "team", paging);

        int actual = res.size();

        assertTrue(
                String.format(
                        "Twitter#getUserListStatuses(userId, slug, new Paging().count(%d)).size() must be equal or less than %d, but %d",
                        COUNT,
                        COUNT,
                        actual),
                actual <= COUNT);
    }

    // test case for TFJ-726
    public void testPagingCountDosentWork2() throws TwitterException {
        final int COUNT = 10;

        Paging paging = new Paging();
        paging.count(COUNT);

        // twitterapi/team
        ResponseList<Status> res =
                twitter1
                        .getUserListStatuses(2031945, paging);

        int actual = res.size();

        assertTrue(
                String.format(
                        "Twitter#getUserListStatuses(userId, slug, new Paging().count(%d)).size() must be equal or less than %d, but %d",
                        COUNT,
                        COUNT,
                        actual),
                actual <= COUNT);
    }

    private UserList prepareListTest() throws Exception {
        ResponseList<UserList> userLists;
        userLists = twitter1.getUserLists(id1.screenName);
        assertNotNull(DataObjectFactory.getRawJSON(userLists));
        for (UserList alist : userLists) {
            twitter1.destroyUserList(twitter1.getId(), alist.getSlug());
        }
        return twitter1.createUserList("testpoint1", true, "description1");
    }

}
