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

package twitter4j.util;

import junit.framework.TestCase;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public class CharacterUtilTest extends TestCase {
    public CharacterUtilTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCount() throws Exception {
        String str;
        int expectedLength;
        str = "a quick brown fox jumped over the lazy dog.";
        expectedLength = str.length();
        assertEquals(expectedLength, CharacterUtil.count(str));

        str = "café";
        expectedLength = 4;
        assertEquals(expectedLength, CharacterUtil.count(str));
        assertFalse(CharacterUtil.isExceedingLengthLimitation(str));

        str = "cafe";
        expectedLength = 4;
        assertEquals(expectedLength, CharacterUtil.count(str));
        assertFalse(CharacterUtil.isExceedingLengthLimitation(str));

        str = "０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９";
        expectedLength = 140;
        assertEquals(expectedLength, CharacterUtil.count(str));
        assertFalse(CharacterUtil.isExceedingLengthLimitation(str));

        str = "０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０";
        expectedLength = 141;
        assertEquals(expectedLength, CharacterUtil.count(str));
        assertTrue(CharacterUtil.isExceedingLengthLimitation(str));

        str = "０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５66778899";
        expectedLength = 144;
        assertEquals(expectedLength, CharacterUtil.count(str));
        assertTrue(CharacterUtil.isExceedingLengthLimitation(str));
    }

}
