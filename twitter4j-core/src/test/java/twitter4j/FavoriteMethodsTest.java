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
public class FavoriteMethodsTest extends TwitterTestBase {
    public FavoriteMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFavoriteMethods() throws Exception {
        Status status = twitter1.getPublicTimeline().get(0);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        status = twitter2.createFavorite(status.getId());
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        assertTrue(twitter2.getFavorites().size() > 0);
        try {
            twitter2.destroyFavorite(status.getId());
        } catch (TwitterException te) {
            // sometimes destorying favorite fails with 404
            assertEquals(404, te.getStatusCode());
        }
    }

}
