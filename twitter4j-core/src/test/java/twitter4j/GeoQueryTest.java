/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Fran Garcia - fgarciarico at gmail.com
 */
class GeoQueryTest {

    @Test
    void testConstructors() {
        GeoQuery geoQuery = new GeoQuery("query", "4.5.3.2", new GeoLocation(38.2622, -0.7011));
        assertEquals(geoQuery.getQuery(), "query");
        assertEquals(geoQuery.getIp(), "4.5.3.2");
        assertEquals(geoQuery.getLocation().getLatitude(), 38.2622);
        assertEquals(geoQuery.getLocation().getLongitude(), -0.7011);
    }

    @Test
    void testEmptyConstructors() {
        GeoQuery geoQuery = new GeoQuery(null, null, null);
        assertNull(geoQuery.getQuery());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }
}
