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

package twitter4j.internal.json;

import junit.framework.TestCase;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import twitter4j.internal.org.json.JSONObject;

public class HTMLEntityTest extends TestCase {
    public HTMLEntityTest(String name) {
        super(name);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testUnescapeAndSlideEntityIncdices() throws Exception {
        // @null &lt; #test &gt; &amp;\u307b\u3052\u307b\u3052 @t4j_news %&amp; http:\/\/t.co\/HwbSpYFr http:\/\/t.co\/d4G7MQ62
        // 01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
        // 0         1         2         3         4         5         6         7         8         9         10        11

//"entities":{"hashtags":[{"text":"test","indices":[11,16]}],"urls":[{"url":"http:\/\/t.co\/HwbSpYFr","expanded_url":"http:\/\/twitter4j.org\/en\/index.html#download","display_url":"twitter4j.org\/en\/index.html#\u2026","indices":[49,69]}],"user_mentions":[{"screen_name":"null","name":"not quite nothing","id":3562471,"id_str":"3562471","indices":[0,5]},{"screen_name":"t4j_news","name":"t4j_news","id":72297675,"id_str":"72297675","indices":[32,41]}],"media":[{"id":268294645535096832,"id_str":"268294645535096832","indices":[70,90],"media_url":"http:\/\/pbs.twimg.com\/media\/A7ksjwJCQAAyvx5.jpg","media_url_https":"https:\/\/pbs.twimg.com\/media\/A7ksjwJCQAAyvx5.jpg","url":"http:\/\/t.co\/d4G7MQ62","display_url":"pic.twitter.com\/d4G7MQ62","expanded_url":"http:\/\/twitter.com\/yusuke\/status\/268294645526708226\/photo\/1","type":"photo","sizes":{"medium":{"w":600,"h":450,"resize":"fit"},"thumb":{"w":150,"h":150,"resize":"crop"},"small":{"w":340,"h":255,"resize":"fit"},"large":{"w":640,"h":480,"resize":"fit"}}}]}

        HashtagEntityJSONImpl test = new HashtagEntityJSONImpl(11, 16, "test");
        URLEntityJSONImpl t4jURL = new URLEntityJSONImpl(49, 69, "http://t.co/HwbSpYFr"
                , "http://twitter4j.org/en/index.html#download", "twitter4j.org/en/index.html#\u2026");
        UserMentionEntityJSONImpl t4j_news = new UserMentionEntityJSONImpl(32, 41, "t4j_news", "t4j_news", 11);
        UserMentionEntityJSONImpl nil = new UserMentionEntityJSONImpl(0, 5, "null", "null", 10);
        MediaEntityJSONImpl media = new MediaEntityJSONImpl(new JSONObject("{\"id\":268294645535096832,\"id_str\":\"268294645535096832\",\"indices\":[70,90],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/A7ksjwJCQAAyvx5.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/A7ksjwJCQAAyvx5.jpg\",\"url\":\"http:\\/\\/t.co\\/d4G7MQ62\",\"display_url\":\"pic.twitter.com\\/d4G7MQ62\",\"expanded_url\":\"http:\\/\\/twitter.com\\/yusuke\\/status\\/268294645526708226\\/photo\\/1\",\"type\":\"photo\",\"sizes\":{\"medium\":{\"w\":600,\"h\":450,\"resize\":\"fit\"},\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"},\"small\":{\"w\":340,\"h\":255,\"resize\":\"fit\"},\"large\":{\"w\":640,\"h\":480,\"resize\":\"fit\"}}}]}"));

        String rawJSON = "{\"text\":\"@null &lt; #test &gt; &amp;\\u307b\\u3052\\u307b\\u3052 @t4j_news %&amp; http:\\/\\/t.co\\/HwbSpYFr http:\\/\\/t.co\\/d4G7MQ62\"}";

        JSONObject json = new JSONObject(rawJSON);
        String escaped = HTMLEntity.unescapeAndSlideEntityIncdices(json.getString("text"),
                new UserMentionEntity[]{t4j_news, nil}, new URLEntity[]{t4jURL}, new HashtagEntity[]{test},
                new MediaEntity[]{media});
        assertEquals("@null < #test > &ほげほげ @t4j_news %& http://t.co/HwbSpYFr http://t.co/d4G7MQ62"
                , escaped);
        assertEquals("#test", escaped.substring(test.getStart(), test.getEnd()));
        assertEquals("http://t.co/HwbSpYFr", escaped.substring(t4jURL.getStart(), t4jURL.getEnd()));
        assertEquals("@t4j_news", escaped.substring(t4j_news.getStart(), t4j_news.getEnd()));
        assertEquals("@null", escaped.substring(nil.getStart(), nil.getEnd()));
        assertEquals("http://t.co/d4G7MQ62", escaped.substring(media.getStart(), media.getEnd()));

    }
    
    public void testUnescapeAndSlideEntityIncdicesWithNullParameters() throws Exception {
        String rawJSON = "{\"text\":\"@null &lt; #test &gt; &amp;\\u307b\\u3052\\u307b\\u3052 @t4j_news %&amp; http:\\/\\/t.co\\/HwbSpYFr http:\\/\\/t.co\\/d4G7MQ62\"}";
        JSONObject json = new JSONObject(rawJSON);
        String escaped = HTMLEntity.unescapeAndSlideEntityIncdices(json.getString("text"),
                null, null, null, null);
        assertEquals("@null < #test > &ほげほげ @t4j_news %& http://t.co/HwbSpYFr http://t.co/d4G7MQ62"
                , escaped);
    }
    
    public void testUnescapeAndSlideEntityIncdicesWithURLEntitiesOnly() throws Exception {
        URLEntityJSONImpl t4jURL = new URLEntityJSONImpl(49, 69, "http://t.co/HwbSpYFr"
                , "http://twitter4j.org/en/index.html#download", "twitter4j.org/en/index.html#\u2026");

        String rawJSON = "{\"text\":\"@null &lt; #test &gt; &amp;\\u307b\\u3052\\u307b\\u3052 @t4j_news %&amp; http:\\/\\/t.co\\/HwbSpYFr http:\\/\\/t.co\\/d4G7MQ62\"}";
        JSONObject json = new JSONObject(rawJSON);
        String escaped = HTMLEntity.unescapeAndSlideEntityIncdices(json.getString("text"),
                null, new URLEntity[]{t4jURL}, null, null);
        assertEquals("@null < #test > &ほげほげ @t4j_news %& http://t.co/HwbSpYFr http://t.co/d4G7MQ62"
                , escaped);
        assertEquals("http://t.co/HwbSpYFr", escaped.substring(t4jURL.getStart(), t4jURL.getEnd()));
    }

    public void testEscape() {
        String original = "<=% !>";
        String expected = "&lt;=% !&gt;";
        assertEquals(expected, HTMLEntity.escape(original));
        StringBuilder buf = new StringBuilder(original);
        HTMLEntity.escape(buf);
        assertEquals(expected, buf.toString());
    }

    public void testUnescape() {
        String original = "&lt;&lt;=% !&nbsp;&gt;";
        String expected = "<<=% !\u00A0>";
        assertEquals(expected, HTMLEntity.unescape(original));
        StringBuilder buf = new StringBuilder(original);
        HTMLEntity.unescape(buf);
        assertEquals(expected, buf.toString());

        original = "&asd&gt;";
        expected = "&asd>";
        assertEquals(expected, HTMLEntity.unescape(original));
        buf = new StringBuilder(original);
        HTMLEntity.unescape(buf);
        assertEquals(expected, buf.toString());

        original = "&quot;;&;asd&;gt;";
        expected = "\";&;asd&;gt;";
        assertEquals(expected, HTMLEntity.unescape(original));
        buf = new StringBuilder(original);
        HTMLEntity.unescape(buf);
        assertEquals(expected, buf.toString());

        original = "\\u5e30%u5e30 &lt;%}& foobar &lt;&Cynthia&gt;";
        expected = "\\u5e30%u5e30 <%}& foobar <&Cynthia>";
        assertEquals(expected, HTMLEntity.unescape(original));
        buf = new StringBuilder(original);
        HTMLEntity.unescape(buf);
        assertEquals(expected, buf.toString());


        original = "\\u5e30%u5e30 &lt;%}& foobar &lt;&Cynthia&gt;";
        expected = "\\u5e30%u5e30 <%}& foobar <&Cynthia>";
        assertEquals(expected, HTMLEntity.unescapeAndSlideEntityIncdices(original, new UserMentionEntity[]{},
                new URLEntity[]{}, new HashtagEntity[]{}, new MediaEntity[]{}));
    }
}