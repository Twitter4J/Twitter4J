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

import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.json.DataObjectFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class UsersResourcesTest extends TwitterTestBase {
    public UsersResourcesTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testShowUser() throws Exception {
        User user = twitter1.showUser("yusuke");
        assertEquals("yusuke", user.getScreenName());
        assertNotNull(user.getLocation());
        assertNotNull(user.getDescription());
        assertNotNull(user.getProfileImageURL());
        assertNotNull(user.getBiggerProfileImageURL());
        assertNotNull(user.getMiniProfileImageURL());
        assertNotNull(user.getOriginalProfileImageURL());

        assertNotNull(user.getProfileImageURLHttps());
        assertNotNull(user.getBiggerProfileImageURLHttps());
        assertNotNull(user.getMiniProfileImageURLHttps());
        assertNotNull(user.getOriginalProfileImageURLHttps());

        assertNotNull(user.getProfileBannerURL());
        HttpClientWrapper wrapper = new HttpClientWrapper();
        wrapper.head(user.getProfileBannerURL());
        wrapper.head(user.getProfileBannerRetinaURL());
        wrapper.head(user.getProfileBannerIPadURL());
        wrapper.head(user.getProfileBannerIPadRetinaURL());
        wrapper.head(user.getProfileBannerMobileURL());
        wrapper.head(user.getProfileBannerMobileRetinaURL());
        assertNotNull(user.getURL());
        assertFalse(user.isProtected());

        assertTrue(0 <= user.getFavouritesCount());
        assertTrue(0 <= user.getFollowersCount());
        assertTrue(0 <= user.getFriendsCount());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getTimeZone());
        assertNotNull(user.getProfileBackgroundImageURL());

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
        }

        assertTrue(1 <= user.getListedCount());
        assertFalse(user.isFollowRequestSent());

        //test case for TFJ-91 null pointer exception getting user detail on users with no statuses
        //http://yusuke.homeip.net/jira/browse/TFJ-91
        user = twitter1.showUser("twit4jnoupdate");
        assertNull(user.getProfileBannerURL());
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
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(users));
    }



    public void testContributors() throws Exception {
        ResponseList<User> users = twitter1.getContributors("twitter");
        assertTrue(users.size() > 0);
        users = twitter1.getContributees(users.get(0).getId());
        assertTrue(users.size() > 0);
    }
    public void testBanner() throws Exception {
        twitter1.updateProfileBanner(getRandomlyChosenFile(banners));
        User user = twitter1.verifyCredentials();
        if (user.getProfileBannerURL() != null) {
            twitter1.removeProfileBanner();
        }
    }
    public void testAccountMethods() throws Exception {
        User original = twitter1.verifyCredentials();
        assertNotNull(DataObjectFactory.getRawJSON(original));
        assertEquals(original, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(original)));
        if (original.getScreenName().endsWith("new") ||
                original.getName().endsWith("new")) {
            original = twitter1.updateProfile(
                    "twit4j", "http://yusuke.homeip.net/twitter4j/"
                    , "location:", "Hi there, I do test a lot!new");

        }
        String newName, newURL, newLocation, newDescription;
        String neu = "new";
        newName = original.getName() + neu;
        newURL = original.getURL() + neu;
        newLocation = new Date().toString();
        newDescription = original.getDescription() + neu;

        User altered = twitter1.updateProfile(
                newName, newURL, newLocation, newDescription);
        assertNotNull(DataObjectFactory.getRawJSON(altered));
        assertEquals(original, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(original)));
        assertEquals(altered, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(altered)));
        twitter1.updateProfile(original.getName(), original.getURL().toString(), original.getLocation(), original.getDescription());
        assertEquals(newName, altered.getName());
        assertEquals(newURL, altered.getURL().toString());
        assertEquals(newLocation, altered.getLocation());
        assertEquals(newDescription, altered.getDescription());

        User eu;
        eu = twitter1.updateProfileColors("f00", "f0f", "0ff", "0f0", "f0f");
        assertNotNull(DataObjectFactory.getRawJSON(eu));
        assertEquals(eu, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(eu)));
        assertEquals("f00", eu.getProfileBackgroundColor());
        assertEquals("f0f", eu.getProfileTextColor());
        assertEquals("0ff", eu.getProfileLinkColor());
        assertEquals("0f0", eu.getProfileSidebarFillColor());
        assertEquals("f0f", eu.getProfileSidebarBorderColor());
        assertTrue(eu.isProfileUseBackgroundImage());
        assertFalse(eu.isShowAllInlineMedia());
        assertTrue(0 <= eu.getListedCount());
        eu = twitter1.updateProfileColors("87bc44", "9ae4e8", "000000", "0000ff", "e0ff92");
        assertNotNull(DataObjectFactory.getRawJSON(eu));
        assertEquals(eu, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(eu)));
        assertEquals("87bc44", eu.getProfileBackgroundColor());
        assertEquals("9ae4e8", eu.getProfileTextColor());
        assertEquals("000000", eu.getProfileLinkColor());
        assertEquals("0000ff", eu.getProfileSidebarFillColor());
        assertEquals("e0ff92", eu.getProfileSidebarBorderColor());

        AccountSettings settings = twitter1.getAccountSettings();
        assertTrue(settings.isSleepTimeEnabled());
        assertTrue(settings.isGeoEnabled());
        assertEquals("en", settings.getLanguage());
        assertEquals("Rome", settings.getTimeZone().getName());
        assertTrue(settings.isAlwaysUseHttps());
        assertFalse(settings.isDiscoverableByEmail());
        Location[] locations = settings.getTrendLocations();
        assertTrue(0 < locations.length);

        AccountSettings intermSettings = twitter1.updateAccountSettings(1 /* GLOBAL */, true,
                "23", "08", "Helsinki", "it");
        assertTrue(intermSettings.isSleepTimeEnabled());
        assertEquals(intermSettings.getSleepStartTime(), "23");
        assertEquals(intermSettings.getSleepEndTime(), "8");
        assertTrue(intermSettings.isGeoEnabled());
        assertEquals("it", intermSettings.getLanguage());
        assertTrue(intermSettings.isAlwaysUseHttps());
        assertFalse(intermSettings.isDiscoverableByEmail());
        assertEquals("Helsinki", intermSettings.getTimeZone().getName());
        Location[] intermLocations = intermSettings.getTrendLocations();
        assertTrue(0 < intermLocations.length);

        AccountSettings lastSettings = twitter1.updateAccountSettings(settings.getTrendLocations()[0].getWoeid(), settings.isSleepTimeEnabled(),
                settings.getSleepStartTime(), settings.getSleepStartTime(), settings.getTimeZone().getName(), settings.getLanguage());
        assertEquals(settings.getLanguage(), lastSettings.getLanguage());
        assertEquals(settings.isSleepTimeEnabled(), lastSettings.isSleepTimeEnabled());
        assertEquals(settings.getTimeZone().getName(), lastSettings.getTimeZone().getName());
        assertEquals(settings.getSleepEndTime(), lastSettings.getSleepEndTime());
    }

    public void testAccountProfileImageUpdates() throws Exception {
        User user = twitter1.updateProfileImage(new FileInputStream(getRandomlyChosenFile()));
        assertNotNull(DataObjectFactory.getRawJSON(user));
        // tile randomly
        User user2 = twitter1.updateProfileBackgroundImage(getRandomlyChosenFile(),
                (5 < System.currentTimeMillis() % 5));
        assertNotNull(DataObjectFactory.getRawJSON(user2));
        assertEquals(user2, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user2)));
    }

    static final String[] profileImages = {"src/test/resources/t4j-reverse.jpeg",
            "src/test/resources/t4j-reverse.png",
            "src/test/resources/t4j-reverse.gif",
            "src/test/resources/t4j.jpeg",
            "src/test/resources/t4j.png",
            "src/test/resources/t4j.gif",
    };

    static final String[] banners = {
            // gif format fails with {"errors":[{"message":"Image error: is not an accepted format","code":211}]}
//            "src/test/resources/t4j-banner.gif",
            "src/test/resources/t4j-banner.jpeg",
            "src/test/resources/t4j-banner.png",
    };

    private static File getRandomlyChosenFile() {
        return getRandomlyChosenFile(profileImages);
    }

    private static File getRandomlyChosenFile(String[] files) {
        int rand = (int) (System.currentTimeMillis() % files.length);
        File file = new File(files[rand]);
        if (!file.exists()) {
            file = new File("twitter4j-core/" + files[rand]);
        }
        return file;
    }

    public void testBlockMethods() throws Exception {
        User user1 = twitter2.createBlock(id1.screenName);
        assertNotNull(DataObjectFactory.getRawJSON(user1));
        assertEquals(user1, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user1)));
        User user2 = twitter2.destroyBlock(id1.screenName);
        assertNotNull(DataObjectFactory.getRawJSON(user2));
        assertEquals(user2, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user2)));
        PagableResponseList<User> users = twitter1.getBlocksList();
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());

        IDs ids = twitter1.getBlocksIDs();
        assertNull(DataObjectFactory.getRawJSON(users));
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(1, ids.getIDs().length);
        assertEquals(39771963, ids.getIDs()[0]);

        ids = twitter1.getBlocksIDs(-1);
        assertTrue(ids.getIDs().length > 0);
    }
}
