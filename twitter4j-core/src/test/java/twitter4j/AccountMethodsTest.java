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

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class AccountMethodsTest extends TwitterTestBase {
    public AccountMethodsTest(String name) {
        super(name);
    }


    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
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

        assertTrue(twitterAPIBestFriend1.existsFriendship(bestFriend1.screenName, bestFriend2.screenName));
        assertFalse(twitter1.existsFriendship(id1.screenName, "al3x"));

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

        AccountTotals totals = twitter1.getAccountTotals();
        assertTrue(0 < totals.getFavorites());
        assertTrue(0 < totals.getFollowers());
        assertTrue(0 < totals.getFriends());
        assertTrue(0 < totals.getUpdates());
        assertEquals(totals, DataObjectFactory.createAccountTotals(DataObjectFactory.getRawJSON(totals)));

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

    static final String[] files = {"src/test/resources/t4j-reverse.jpeg",
            "src/test/resources/t4j-reverse.png",
            "src/test/resources/t4j-reverse.gif",
            "src/test/resources/t4j.jpeg",
            "src/test/resources/t4j.png",
            "src/test/resources/t4j.gif",
    };

    private static File getRandomlyChosenFile() {
        int rand = (int) (System.currentTimeMillis() % 6);
        File file = new File(files[rand]);
        if (!file.exists()) {
            file = new File("twitter4j-core/" + files[rand]);
        }
        return file;
    }
}
