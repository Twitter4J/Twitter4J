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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import twitter4j.v1.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
@Execution(ExecutionMode.CONCURRENT)
class ListResourcesTest extends TwitterTestBase {

    @Test
    void testListMethods() throws Exception {
        ResponseList<UserList> userLists;
        UserList userList;
        userList = prepareListTest();
        /*List Methods*/
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        assertNotNull(userList.getName());
        assertNotNull(userList.getDescription());

        userLists = twitter1.v1().list().getUserLists(id1.screenName);
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertNotEquals(0, userLists.size());

        userLists = twitter1.v1().list().getUserLists("@" + id1.screenName);
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertNotEquals(0, userLists.size());

        userList = twitter1.v1().list().showUserList(userList.getId());
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        userList = twitter1.v1().list().showUserList(id1.id, userList.getSlug());
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        List<Status> statuses = twitter1.v1().list().getUserListStatuses(userList.getId(), TimelinesResourcesImpl.empty);
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
        }
        statuses = twitter1.v1().list().getUserListStatuses(userList.getId(), TimelinesResourcesImpl.empty);
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
        }
        assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
        assertNull(TwitterObjectFactory.getRawJSON(userList));
        assertNotNull(statuses);

        userList = twitter1.v1().list().updateUserList(userList.getId(), "testpoint2", true, "description2");
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        // workarounding issue 2166
        // http://code.google.com/p/twitter-api/issues/detail?id=2166
        userList = twitter1.v1().list().showUserList(userList.getId());
        assertTrue(userList.isPublic());
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertNotNull(userList);
        assertEquals("testpoint2", userList.getName());
        assertEquals("description2", userList.getDescription());

        userList = twitter1.v1().list().updateUserList(id1.id, userList.getSlug(), "testpoint3", true, "description3");
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertTrue(userList.isPublic());
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertNotNull(userList);
        assertEquals("testpoint3", userList.getName());
        assertEquals("description3", userList.getDescription());
    }

    @Disabled
    @Test
    void testListMemberMethods() throws Exception {
        PagableResponseList<UserList> userLists;

        UserList userList;
        userList = prepareListTest();
        /*List Member Methods*/
        User user;
        userList = twitter1.v1().list().createUserListMember(userList.getId(), id2.id);
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        userList = twitter1.v1().list().createUserListMembers(userList.getId(), id3.id);
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        userList = twitter1.v1().list().createUserListMembers(userList.getId(), new String[]{"yusukey", "yusuke"});
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertNotNull(userList);

        PagableResponseList<User> users = twitter1.v1().list().getUserListMembers(userList.getId(), -1);
        assertEquals(users.get(0), TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
        assertNull(TwitterObjectFactory.getRawJSON(userList));

        users = twitter1.v1().list().getUserListMembers(userList.getId(), -1);
        assertEquals(users.get(0), TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
        assertEquals(4, users.size());

        userList = twitter1.v1().list().destroyUserListMember(userList.getId(), id2.id);
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        // member_count don't get decreased by destroyUserListMember. need to retrieve user list again
        userList = twitter1.v1().list().showUserList(userList.getId());
        assertEquals(3, userList.getMemberCount());

        user = twitter1.v1().list().showUserListMembership(userList.getId(), id3.id);
        assertNotNull(TwitterObjectFactory.getRawJSON(user));
        assertEquals(user, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user)));
        assertEquals(id3.id, user.getId());

        userLists = twitter1.v1().list().getUserListMemberships(id1.screenName, -1L);
        assertNotNull(TwitterObjectFactory.getRawJSON(userLists));
        if (userLists.size() > 0) {
            assertEquals(userLists.get(0), TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userLists.get(0))));
            assertNotNull(userLists);
        }

        userLists = twitter1.v1().list().getUserListMemberships("@" + id1.screenName, -1L);
        if (userLists.size() > 0) {
            assertNotNull(TwitterObjectFactory.getRawJSON(userLists));
            assertEquals(userLists.get(0), TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userLists.get(0))));
            assertNotNull(userLists);
        }

        userLists = twitter1.v1().list().getUserListSubscriptions(id1.screenName, -1L);
        assertNotNull(TwitterObjectFactory.getRawJSON(userLists));
        if (userLists.size() > 0) {
            assertEquals(userLists.get(0), TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userLists.get(0))));
        }
        assertNotNull(userLists);
        assertEquals(0, userLists.size());

        userLists = twitter1.v1().list().getUserListSubscriptions("@" + id1.screenName, -1L);
        assertNotNull(TwitterObjectFactory.getRawJSON(userLists));
        if (userLists.size() > 0) {
            assertEquals(userLists.get(0), TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userLists.get(0))));
        }
        assertNotNull(userLists);
        assertEquals(0, userLists.size());
    }

    @Disabled
    @Test
    void testRemoveListMembers() throws Exception {

        UserList userList;
        userList = prepareListTest();
        try {
            twitter1.v1().list().showUserListMembership(userList.getId(), id2.id);
        } catch (TwitterException te) {
            assertEquals(404, te.getStatusCode());
        }

        PagableResponseList<User> users = twitter1.v1().list().getUserListMembers(userList.getId(), -1);
        assertNotNull(TwitterObjectFactory.getRawJSON(users));

        // Add user by id
        userList = twitter1.v1().list().createUserListMember(userList.getId(), id2.id);
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));

        // Remove by screenName
        userList = twitter1.v1().list().destroyUserListMember(userList.getId(), id2.screenName);
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
        userList = twitter1.v1().list().showUserList(userList.getId());

        // Add 2 users by screenName
        String[] screenNames = new String[]{"yusukey", "yusuke"};
        userList = twitter1.v1().list().createUserListMembers(userList.getId(), screenNames);
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));

        twitter1.v1().list().getUserListMembers(userList.getId(), -1);

        // Remove 2 by screen name
        userList = twitter1.v1().list().destroyUserListMembers(userList.getUser().getScreenName(), userList.getSlug(), screenNames);
        userList = twitter1.v1().list().showUserList(userList.getId());

        userList = twitter1.v1().list().createUserListMembers(userList.getId(), screenNames);
        userList = twitter1.v1().list().destroyUserListMembers("@" + userList.getUser().getScreenName(), userList.getSlug(), screenNames);
        userList = twitter1.v1().list().showUserList(userList.getId());

        // Add 2 users by ids
        long[] userIds = new long[]{id1.id, id2.id};
        userList = twitter1.v1().list().createUserListMembers(userList.getId(), userIds);
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));

        twitter1.v1().list().getUserListMembers(userList.getId(), -1);

        // Remove 2 by screen name
        userList = twitter1.v1().list().destroyUserListMembers(userList.getId(), userIds);
        twitter1.v1().list().showUserList(userList.getId());
    }

    @Disabled
    @Test
    void testListSubscribersMethods() throws Exception {
        PagableResponseList<UserList> userLists;
        UserList userList;
        userList = prepareListTest();

        /*List Subscribers Methods*/
        PagableResponseList<User> users;

        users = twitter1.v1().list().getUserListSubscribers(id1.id, userList.getSlug(), -1L);
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
        twitter2.v1().list().createUserListSubscription(userList.getId());
        twitter2.v1().list().destroyUserListSubscription(userList.getId());
        users = twitter1.v1().list().getUserListSubscribers("twitterapi", "team", -1L);
        assertTrue(0 < users.size());
        User user;
        user = twitter1.v1().list().showUserListSubscription("twitterapi", "team", 4933401);
        assertNotNull(TwitterObjectFactory.getRawJSON(user));
        assertEquals(user, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user)));
        assertEquals("yusuke", user.getScreenName());
        userLists = twitter1.v1().list().getUserListSubscriptions("yusuke", -1L);
        assertNotNull(userLists);
        if (userLists.size() > 0) {
            assertEquals(userLists.get(0), TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userLists.get(0))));
        }

        try {
            userList = twitter1.v1().list().destroyUserList(userList.getId());
        } catch (TwitterException ignore) {
            // in some case destroying user list returns 404
        }
        assertNotNull(TwitterObjectFactory.getRawJSON(userList));
        assertEquals(userList, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(userList)));
        assertNotNull(userList);
    }

    // test case for TFJ-726
    @Test
    void testPagingCountDosentWork1() throws TwitterException {
        final int COUNT = 10;

        Paging paging = Paging.ofCount(COUNT);

        // twitterapi/team
        ResponseList<Status> res =
                twitter1
                        .v1().list()
                        .getUserListStatuses(6253282L, "team", paging);

        int actual = res.size();

        assertTrue(
                actual <= COUNT, String.format(
                        "Twitter#getUserListStatuses(userId, slug, new Paging().count(%d)).size() must be equal or less than %d, but %d",
                        COUNT,
                        COUNT,
                        actual)
        );
    }

    // test case for TFJ-726
    @Test
    void testPagingCountDosentWork2() throws TwitterException {
        final int COUNT = 10;

        Paging paging = Paging.ofCount(COUNT);

        // twitterapi/team
        ResponseList<Status> res =
                twitter1
                        .v1().list()
                        .getUserListStatuses(2031945, paging);

        int actual = res.size();

        assertTrue(
                actual <= COUNT,
                String.format(
                        "Twitter#getUserListStatuses(userId, slug, new Paging().count(%d)).size() must be equal or less than %d, but %d",
                        COUNT,
                        COUNT,
                        actual)
        );
    }

    @Test
    void testUserListsOwnerships() throws Exception {
        PagableResponseList<UserList> lists;
        lists = twitter1.v1().list().getUserListsOwnerships("t4jtest123", 3, -1);
        assertTrue(lists.size() > 0);
        lists = twitter1.v1().list().getUserListsOwnerships(4933401L, 3, -1);
        assertTrue(lists.size() > 0);
        assertNotNull(lists.get(0).getCreatedAt());
    }

    private UserList prepareListTest() throws Exception {
        ResponseList<UserList> userLists;
        userLists = twitter1.v1().list().getUserLists(id1.screenName);
        assertNotNull(TwitterObjectFactory.getRawJSON(userLists));
        UserList list = null;
        for (UserList alist : userLists) {
            if (alist.getSlug().equals("testpoint1")) {
                list = alist;
            } else {
                try {
                    UserList deletedLiet = twitter1.v1().list().destroyUserList(id1.id, alist.getSlug());
                    assertNotNull(TwitterObjectFactory.getRawJSON(deletedLiet));
                    assertEquals(deletedLiet, TwitterObjectFactory.createUserList(TwitterObjectFactory.getRawJSON(deletedLiet)));
                    assertNotNull(deletedLiet);
                } catch (TwitterException ignore) {
                }
            }

        }
        if (list == null) {
            list = twitter1.v1().list().createUserList("testpoint1", true, "description1");
            // the api returns "created_at": "Tue Jan 20 06:59:53 +0000 1970"
//            assertTrue(Math.abs(list.getCreatedAt().toEpochSecond(ZoneOffset.UTC)
//                    - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) < 10);
        }
        Thread.sleep(5000);
        return list;
    }

}
