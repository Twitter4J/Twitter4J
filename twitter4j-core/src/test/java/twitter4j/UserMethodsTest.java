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
public class UserMethodsTest extends TwitterTestBase {
    public UserMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testShowUser() throws Exception {
        User user = twitter1.showUser(id1.screenName);
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
        if (user.getStatus() != null) {
            assertNotNull(user.getStatus().getCreatedAt());
            assertNotNull(user.getStatus().getText());
            assertNotNull(user.getStatus().getSource());
            assertFalse(user.getStatus().isFavorited());
            assertEquals(-1, user.getStatus().getInReplyToStatusId());
            assertEquals(-1, user.getStatus().getInReplyToUserId());
            assertFalse(user.getStatus().isFavorited());
            assertNull(user.getStatus().getInReplyToScreenName());
        }

        assertTrue(1 <= user.getListedCount());
        assertFalse(user.isFollowRequestSent());

        //test case for TFJ-91 null pointer exception getting user detail on users with no statuses
        //http://yusuke.homeip.net/jira/browse/TFJ-91
        twitter1.showUser("twit4jnoupdate");
        user = twitter1.showUser("tigertest");
        User previousUser = user;
        assertNotNull(DataObjectFactory.getRawJSON(user));

        user = twitter1.showUser(numberId);
        assertEquals(numberIdId, user.getId());
        assertNull(DataObjectFactory.getRawJSON(previousUser));
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));

        previousUser = user;
        user = twitter1.showUser(numberIdId);
        assertEquals(numberIdId, user.getId());
        assertNotNull(DataObjectFactory.getRawJSON(user));
        assertEquals(user, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user)));
    }

    public void testLookupUsers() throws TwitterException {
        ResponseList<User> users = twitter1.lookupUsers(new String[]{id1.screenName, id2.screenName});
        assertEquals(2, users.size());
        assertContains(users, id1);
        assertContains(users, id2);

        users = twitter1.lookupUsers(new long[]{id1.id, id2.id});
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
        for (User aUser : users) {
            if (aUser.getId() == user.id && aUser.getScreenName().equals(user.screenName)) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail(user.screenName + " not found in the result.");
        }

    }

    public void testSearchUser() throws TwitterException {
        ResponseList<User> users = twitter1.searchUsers("Doug Williams", 1);
        assertTrue(4 < users.size());
        assertNotNull(users.getFeatureSpecificRateLimitStatus());
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
    }

    public void testSuggestion() throws Exception {
        ResponseList<Category> categories = twitter1.getSuggestedUserCategories();
        assertTrue(categories.size() > 0);
        assertNotNull(DataObjectFactory.getRawJSON(categories));
        assertNotNull(DataObjectFactory.getRawJSON(categories.get(0)));
        assertEquals(categories.get(0), DataObjectFactory.createCategory(DataObjectFactory.getRawJSON(categories.get(0))));
        ResponseList<User> users = twitter1.getUserSuggestions(categories.get(0).getSlug());
        assertTrue(users.size() > 0);
        assertNull(users.get(0).getStatus());
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));

        users = twitter1.getMemberSuggestions(categories.get(0).getSlug());
        assertTrue(users.size() > 0);
        assertNotNull(users.get(0).getStatus());
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
    }

    public void testProfileImage() throws Exception {
        ProfileImage image = twitter1.getProfileImage(id1.screenName, ProfileImage.BIGGER);
        assertNotNull(image.getURL());
    }
}
