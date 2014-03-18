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

import junit.framework.Assert;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.5
 */
public class ParseUtilTest extends TestCase {
    public ParseUtilTest(String name) {
        super(name);
    }

    public void testParseLongReturns101() {
        Assert.assertEquals(101, ParseUtil.getLong("100+"));
    }

    public void testParseIntOverflow() {
        Assert.assertEquals(-1, ParseUtil.getInt("4294967295"));
    }

    public void testParseTrendDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Assert.assertEquals("2011-07-11"
                , sdf.format(ParseUtil.parseTrendsDate("2011-07-11T05:31:52Z")));
    }

}
