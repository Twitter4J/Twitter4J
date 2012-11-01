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
public class SavedSearchesResourcesTest extends TwitterTestBase {
    public SavedSearchesResourcesTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /* Saved Searches Methods */
    public void testSavedSearches() throws Exception {
        List<SavedSearch> list = twitter1.getSavedSearches();
        assertNotNull(DataObjectFactory.getRawJSON(list));
        for (SavedSearch savedSearch : list) {
            twitter1.destroySavedSearch(savedSearch.getId());
        }
        SavedSearch ss1 = twitter1.createSavedSearch("my search");
        assertNotNull(DataObjectFactory.getRawJSON(ss1));
        assertEquals(ss1, DataObjectFactory.createSavedSearch(DataObjectFactory.getRawJSON(ss1)));
        assertEquals("my search", ss1.getQuery());
        assertEquals(-1, ss1.getPosition());
        list = twitter1.getSavedSearches();
        assertNotNull(DataObjectFactory.getRawJSON(list));
        assertEquals(list.get(0), DataObjectFactory.createSavedSearch(DataObjectFactory.getRawJSON(list.get(0))));
        // http://code.google.com/p/twitter-api/issues/detail?id=1032
        // the saved search may not be immediately available
        assertTrue(0 <= list.size());
        try {
            SavedSearch ss2 = twitter1.destroySavedSearch(ss1.getId());
            assertEquals(ss1, ss2);
        } catch (TwitterException te) {
            // sometimes it returns 404 or 500 when its out of sync.
            assertTrue(404 == te.getStatusCode() || 500 == te.getStatusCode());
        }
    }
}
