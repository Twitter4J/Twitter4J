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

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class UsersResourcesTest extends TwitterTestBase {
    private long twit4jblockID = 39771963L;

    public UsersResourcesTest(String name) {
        super(name);
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
        HttpClient http = HttpClientFactory.getInstance(conf1.getHttpClientConfiguration());
        http.head(user.getProfileBannerURL());
        http.head(user.getProfileBannerRetinaURL());
        http.head(user.getProfileBannerIPadURL());
        http.head(user.getProfileBannerIPadRetinaURL());
        http.head(user.getProfileBannerMobileURL());
        http.head(user.getProfileBannerMobileRetinaURL());
        assertNotNull(user.getURL());
        assertFalse(user.isProtected());

        assertTrue(0 <= user.getFavouritesCount());
        assertTrue(0 <= user.getFollowersCount());
        assertTrue(0 <= user.getFriendsCount());
        assertNotNull(user.getCreatedAt());
        // timezone can be
//        assertNotNull(user.getTimeZone());
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
        assertEquals(user, twitter1.showUser("@yusuke"));

        //test case for TFJ-91 null pointer exception getting user detail on users with no statuses
        //http://yusuke.homeip.net/jira/browse/TFJ-91
        user = twitter1.showUser("twit4jnoupdate");
        assertNull(user.getProfileBannerURL());
        user = twitter1.showUser("tigertest");
        User previousUser = user;
        assertNotNull(TwitterObjectFactory.getRawJSON(user));

        user = twitter1.showUser(numberId);
        assertEquals(numberIdId, user.getId());
        assertNull(TwitterObjectFactory.getRawJSON(previousUser));
        assertNotNull(TwitterObjectFactory.getRawJSON(user));
        assertEquals(user, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user)));

        previousUser = user;
        user = twitter1.showUser(numberIdId);
        assertEquals(numberIdId, user.getId());
        assertNotNull(TwitterObjectFactory.getRawJSON(user));
        assertEquals(user, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user)));
    }

    public void testLookupUsers() throws TwitterException {
        ResponseList<User> users = twitter1.lookupUsers(id1.screenName, id2.screenName);
        assertEquals(2, users.size());
        assertContains(users, id1);
        assertContains(users, id2);

        users = twitter1.lookupUsers(id1.id, id2.id);
        assertEquals(2, users.size());
        assertContains(users, id1);
        assertContains(users, id2);
        assertNotNull(TwitterObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
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
        assertNotNull(TwitterObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(users.get(0))));
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
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
        assertNotNull(TwitterObjectFactory.getRawJSON(original));
        assertEquals(original, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(original)));
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
        assertNotNull(TwitterObjectFactory.getRawJSON(altered));
        assertEquals(original, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(original)));
        assertEquals(altered, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(altered)));
        twitter1.updateProfile(original.getName(), original.getURL().toString(), original.getLocation(), original.getDescription());
        assertEquals(newName, altered.getName());
        assertEquals(newURL, altered.getURL().toString());
        assertEquals(newLocation, altered.getLocation());
        assertEquals(newDescription, altered.getDescription());

        User eu;
        eu = twitter1.updateProfileColors("f00", "f0f", "0ff", "0f0", "f0f");
        assertNotNull(TwitterObjectFactory.getRawJSON(eu));
        assertEquals(eu, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(eu)));
        assertEquals("FF0000", eu.getProfileBackgroundColor());
        assertEquals("FF00FF", eu.getProfileTextColor());
        assertEquals("00FFFF", eu.getProfileLinkColor());
        assertEquals("00FF00", eu.getProfileSidebarFillColor());
        assertEquals("FF00FF", eu.getProfileSidebarBorderColor());
        assertFalse(eu.isProfileUseBackgroundImage());
        assertFalse(eu.isShowAllInlineMedia());
        assertTrue(0 <= eu.getListedCount());
        eu = twitter1.updateProfileColors("87bc44", "9ae4e8", "000000", "0000ff", "e0ff92");
        assertNotNull(TwitterObjectFactory.getRawJSON(eu));
        assertEquals(eu, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(eu)));
        assertEquals("87BC44", eu.getProfileBackgroundColor());
        assertEquals("9AE4E8", eu.getProfileTextColor());
        assertEquals("000000", eu.getProfileLinkColor());
        assertEquals("0000FF", eu.getProfileSidebarFillColor());
        assertEquals("E0FF92", eu.getProfileSidebarBorderColor());

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
        assertNotNull(TwitterObjectFactory.getRawJSON(user));
        // tile randomly
        User user2 = twitter1.updateProfileBackgroundImage(getRandomlyChosenFile(),
                (5 < System.currentTimeMillis() % 5));
        assertNotNull(TwitterObjectFactory.getRawJSON(user2));
        assertEquals(user2, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user2)));
    }

    private static final String[] profileImages = {"src/test/resources/t4j-reverse.jpeg",
            "src/test/resources/t4j-reverse.png",
            "src/test/resources/t4j-reverse.gif",
            "src/test/resources/t4j.jpeg",
            "src/test/resources/t4j.png",
            "src/test/resources/t4j.gif",
    };

    private static final String[] banners = {
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
        assertNotNull(TwitterObjectFactory.getRawJSON(user1));
        assertEquals(user1, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user1)));
        User user2 = twitter2.destroyBlock(id1.screenName);
        assertNotNull(TwitterObjectFactory.getRawJSON(user2));
        assertEquals(user2, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user2)));

        user1 = twitter2.createBlock("@"+id1.screenName);
        assertNotNull(TwitterObjectFactory.getRawJSON(user1));
        assertEquals(user1, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user1)));
        user2 = twitter2.destroyBlock("@"+id1.screenName);
        assertNotNull(TwitterObjectFactory.getRawJSON(user2));
        assertEquals(user2, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user2)));

        PagableResponseList<User> users = twitter1.getBlocksList();
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(users.get(0))));
        assertEquals(1, users.size());
        assertEquals(twit4jblockID, users.get(0).getId());

        IDs ids = twitter1.getBlocksIDs();
        assertNull(TwitterObjectFactory.getRawJSON(users));
        assertNotNull(TwitterObjectFactory.getRawJSON(ids));
        assertEquals(1, ids.getIDs().length);
        assertEquals(twit4jblockID, ids.getIDs()[0]);

        ids = twitter1.getBlocksIDs(-1);
        assertTrue(ids.getIDs().length > 0);
    }

    public void testMuteMethods() throws Exception {
        User user1 = twitter2.createMute(id1.screenName);
        assertNotNull(TwitterObjectFactory.getRawJSON(user1));
        assertEquals(user1, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user1)));
        User user2 = twitter2.destroyMute(id1.screenName);
        assertNotNull(TwitterObjectFactory.getRawJSON(user2));
        assertEquals(user2, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user2)));

        user1 = twitter2.createMute("@"+id1.screenName);
        assertNotNull(TwitterObjectFactory.getRawJSON(user1));
        assertEquals(user1, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user1)));
        try {
            user2 = twitter2.destroyMute("@"+id1.screenName);
//            assertNotNull(TwitterObjectFactory.getRawJSON(user2));
//            assertEquals(user2, TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(user2)));
        } catch (TwitterException e) {
//          The request with '@'+screen_name could not make mute user.
            assertEquals(e.getStatusCode(), 403);
            assertEquals(e.getErrorCode(), 272);
        }

        twitter1.createMute(twit4jblockID);
        PagableResponseList<User> users = twitter1.getMutesList(-1L);
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(users.get(0))));
        assertEquals(1, users.size());
        assertEquals(twit4jblockID, users.get(0).getId());

        IDs ids = twitter1.getMutesIDs(-1L);
        assertNull(TwitterObjectFactory.getRawJSON(users));
        assertNotNull(TwitterObjectFactory.getRawJSON(ids));
        assertEquals(1, ids.getIDs().length);
        assertEquals(twit4jblockID, ids.getIDs()[0]);

    }
}
