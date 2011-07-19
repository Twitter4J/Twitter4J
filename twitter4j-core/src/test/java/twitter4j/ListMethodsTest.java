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
public class ListMethodsTest extends TwitterTestBase {
    public ListMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testListMethods() throws Exception {
        PagableResponseList<UserList> userLists;
        UserList userList;
        userList = prepareListTest();
        /*List Methods*/
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        assertEquals("testpoint1", userList.getName());
//        assertEquals("@twit4j/testpoint1", userList.getFullName());
        assertEquals("description1", userList.getDescription());

        userLists = twitter1.getUserLists(id1.screenName, -1l);
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertFalse(userLists.size() == 0);

        userList = twitter1.showUserList(userList.getId());
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

        ResponseList<UserList> lists = twitter1.getAllUserLists(id1.id);
        assertTrue(0 < lists.size());
        lists = twitter1.getAllUserLists("yusukey");
        assertTrue(0 < lists.size());

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
        userList = twitter1.addUserListMember(userList.getId(), id2.id);
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        userList = twitter1.addUserListMembers(userList.getId(), new long[]{id3.id, id2.id});
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        userList = twitter1.addUserListMembers(userList.getId(), new String[]{"akr", "yusukey"});
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        PagableResponseList<User> users = twitter1.getUserListMembers(userList.getId(), -1);
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertNull(DataObjectFactory.getRawJSON(userList));
        // workaround issue 1301
        // http://code.google.com/p/twitter-api/issues/detail?id=1301
//        assertEquals(userList.getMemberCount(), users.size());
        assertTrue(0 < users.size());// workaround issue 1301

        users = twitter1.getUserListMembers(userList.getId(), -1);
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertTrue(0 < users.size());

        userList = twitter1.deleteUserListMember(userList.getId(), id2.id);
        assertNotNull(DataObjectFactory.getRawJSON(userList));
        assertEquals(userList, DataObjectFactory.createUserList(DataObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        //
//        assertEquals(1, userList.getMemberCount());

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

    public void testListSubscribersMethods() throws Exception {
        PagableResponseList<UserList> userLists;
        UserList userList;
        userList = prepareListTest();

        /*List Subscribers Methods*/
        PagableResponseList<User> users;

        users = twitter1.getUserListSubscribers(userList.getId(), -1);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(0, users.size());
        try {
            twitter2.createUserListSubscription(userList.getId());
        } catch (TwitterException te) {
            // workarounding issue 1300
            // http://code.google.com/p/twitter-api/issues/detail?id=1300
            assertEquals(404, te.getStatusCode());
        }
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

    private UserList prepareListTest() throws Exception {
        PagableResponseList<UserList> userLists;
        userLists = twitter1.getUserLists(id1.screenName, -1l);
        assertNotNull(DataObjectFactory.getRawJSON(userLists));
        for (UserList alist : userLists) {
            twitter1.destroyUserList(alist.getId());
        }
        return twitter1.createUserList("testpoint1", false, "description1");
    }

}
