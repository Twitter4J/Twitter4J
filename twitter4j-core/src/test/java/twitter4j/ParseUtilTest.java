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

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.5
 */
@Execution(ExecutionMode.CONCURRENT)
class ParseUtilTest {

    @Test
    void testParseLongReturns101() {
        assertEquals(101, ParseUtil.getLong("100+"));
    }

    @Test
    void testParseIntOverflow() {
        assertEquals(-1, ParseUtil.getInt("4294967295"));
    }

    @Test
    void testParseTrendDate() throws Exception {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals("2011-07-11"
                , sdf.format(ParseUtil.parseTrendsDate("2011-07-11T05:31:52Z")));
    }

}
