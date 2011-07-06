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
public class TrendMethodsTest extends TwitterTestBase {
    public TrendMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLocalTrendsMethods() throws Exception {
        ResponseList<Location> locations;
        locations = twitter1.getAvailableTrends();
        assertNotNull(DataObjectFactory.getRawJSON(locations));
        assertEquals(locations.get(0), DataObjectFactory.createLocation(DataObjectFactory.getRawJSON(locations.get(0))));
        assertTrue(locations.size() > 0);
        locations = twitter1.getAvailableTrends(new GeoLocation(0, 0));
        assertNotNull(DataObjectFactory.getRawJSON(locations));
        assertTrue(locations.size() > 0);

        Trends trends = twitter1.getLocationTrends(locations.get(0).getWoeid());
        System.out.println(DataObjectFactory.getRawJSON(trends));
        assertEquals(trends, DataObjectFactory.createTrends(DataObjectFactory.getRawJSON(trends)));
        assertNull(DataObjectFactory.getRawJSON(locations));
        System.out.println(DataObjectFactory.getRawJSON(trends));
        assertNotNull(DataObjectFactory.getRawJSON(trends));
        assertEquals(locations.get(0), trends.getLocation());
        assertTrue(trends.getTrends().length > 0);

        try {
            trends = twitter1.getLocationTrends(2345889/*woeid of Tokyo*/);
            fail("should fail.");
        } catch (Exception ignore) {
        }
        assertEquals(locations.get(0), trends.getLocation());
        assertTrue(trends.getTrends().length > 0);
    }
}
