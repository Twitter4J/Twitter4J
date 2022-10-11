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
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Fran Garcia - fgarciarico at gmail.com
 */
@Execution(ExecutionMode.CONCURRENT)
class GeoQueryTest {

    @Test
    void testConstructors() {
        GeoQuery geoQuery = GeoQuery.ofQuery("query").ip("4.5.3.2").geoLocation(38.2622, -0.7011);
        assertEquals(geoQuery.query, "query");
        assertEquals(geoQuery.ip, "4.5.3.2");
        assertEquals(geoQuery.location.latitude, 38.2622);
        assertEquals(geoQuery.location.longitude, -0.7011);
    }

    @Test
    void testEmptyConstructors() {
        GeoQuery geoQuery = GeoQuery.ofQuery(null);
        assertNull(geoQuery.query);
    }
}
