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

package twitter4j.internal.util;

import junit.framework.TestCase;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.6
 */
public class StringUtilTest extends TestCase {
    public void testMaskString() throws Exception {
        assertEquals("******", z_T4JInternalStringUtil.maskString("foobar"));
    }

    public void testJoin() throws Exception {
        assertEquals("6358482", z_T4JInternalStringUtil.join(new long[]{6358482L}));
        assertEquals("6358482,6358483", z_T4JInternalStringUtil.join(new long[]{6358482L, 6358483L}));
    }

    public void testSplit() throws Exception {
        String[] expected = new String[]{"foo", "bar"};
        String[] actual = z_T4JInternalStringUtil.split("foo,bar", ",");
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);

        expected = new String[]{"foo", "bar", "hoge"};
        actual = z_T4JInternalStringUtil.split("foo,bar,hoge", ",");
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
        assertEquals(expected[2], actual[2]);

        expected = new String[]{"foobar"};
        actual = z_T4JInternalStringUtil.split("foobar", ",");
        assertEquals(expected[0], actual[0]);
    }
}
