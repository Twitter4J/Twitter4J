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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import twitter4j.v1.SavedSearch;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
@Execution(ExecutionMode.CONCURRENT)
class SavedSearchesResourcesTest extends TwitterTestBase {

    /* Saved Searches Methods */
    @Test
    void testSavedSearches() throws Exception {
        List<SavedSearch> list = twitter1.v1().savedSearches().getSavedSearches();
        assertNotNull(TwitterObjectFactory.getRawJSON(list));
        for (SavedSearch savedSearch : list) {
            twitter1.v1().savedSearches().destroySavedSearch(savedSearch.getId());
        }
        String listName = String.valueOf(System.currentTimeMillis());
        SavedSearch ss1 = twitter1.v1().savedSearches().createSavedSearch(listName);

        assertTrue(Math.abs(ss1.getCreatedAt().toEpochSecond(ZoneOffset.UTC)
                - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) < 10);

        assertNotNull(TwitterObjectFactory.getRawJSON(ss1));
        assertEquals(ss1, TwitterObjectFactory.createSavedSearch(TwitterObjectFactory.getRawJSON(ss1)));
        assertEquals(listName, ss1.getQuery());
        assertEquals(-1, ss1.getPosition());
        list = twitter1.v1().savedSearches().getSavedSearches();
        assertNotNull(TwitterObjectFactory.getRawJSON(list));
        assertEquals(list.get(0), TwitterObjectFactory.createSavedSearch(TwitterObjectFactory.getRawJSON(list.get(0))));
        // http://code.google.com/p/twitter-api/issues/detail?id=1032
        // the saved search may not be immediately available
//        assertTrue(0 <= list.size());
        try {
            SavedSearch ss2 = twitter1.v1().savedSearches().destroySavedSearch(ss1.getId());
            assertEquals(ss1, ss2);
        } catch (TwitterException te) {
            // sometimes it returns 404 or 500 when its out of sync.
            assertTrue(404 == te.getStatusCode() || 500 == te.getStatusCode());
        }
    }
}
