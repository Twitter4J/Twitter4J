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

package twitter4j.http;

import junit.framework.TestCase;
import twitter4j.internal.http.HTMLEntity;

public class HTMLEntityTest extends TestCase {
    public HTMLEntityTest(String name) {
        super(name);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testEscape() {
        String original = "<=% !>";
        String expected = "&lt;=% !&gt;";
        assertEquals(expected, HTMLEntity.escape(original));
        StringBuffer buf = new StringBuffer(original);
        HTMLEntity.escape(buf);
        assertEquals(expected, buf.toString());
    }

    public void testUnescape() {
        String original = "&lt;&lt;=% !&nbsp;&gt;";
        String expected = "<<=% !\u00A0>";
        assertEquals(expected, HTMLEntity.unescape(original));
        StringBuffer buf = new StringBuffer(original);
        HTMLEntity.unescape(buf);
        assertEquals(expected, buf.toString());

        original = "&asd&gt;";
        expected = "&asd>";
        assertEquals(expected, HTMLEntity.unescape(original));
        buf = new StringBuffer(original);
        HTMLEntity.unescape(buf);
        assertEquals(expected, buf.toString());

        original = "&quot;;&;asd&;gt;";
        expected = "\";&;asd&;gt;";
        assertEquals(expected, HTMLEntity.unescape(original));
        buf = new StringBuffer(original);
        HTMLEntity.unescape(buf);
        assertEquals(expected, buf.toString());

        original = "\\u5e30%u5e30 &lt;%}& foobar &lt;&Cynthia&gt;";
        expected = "\\u5e30%u5e30 <%}& foobar <&Cynthia>";
        assertEquals(expected, HTMLEntity.unescape(original));
        buf = new StringBuffer(original);
        HTMLEntity.unescape(buf);
        assertEquals(expected, buf.toString());


    }
}
