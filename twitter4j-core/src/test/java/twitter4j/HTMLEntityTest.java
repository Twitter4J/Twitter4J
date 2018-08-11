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
import twitter4j.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTMLEntityTest {

    @Test
    void testUnescapeAndSlideEntityIncdices() throws Exception {
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

    @Test
    void testUnescapeAndSlideEntityIncdicesWithNullParameters() throws Exception {
        String rawJSON = "{\"text\":\"@null &lt; #test &gt; &amp;\\u307b\\u3052\\u307b\\u3052 @t4j_news %&amp; http:\\/\\/t.co\\/HwbSpYFr http:\\/\\/t.co\\/d4G7MQ62\"}";
        JSONObject json = new JSONObject(rawJSON);
        String escaped = HTMLEntity.unescapeAndSlideEntityIncdices(json.getString("text"),
                null, null, null, null);
        assertEquals("@null < #test > &ほげほげ @t4j_news %& http://t.co/HwbSpYFr http://t.co/d4G7MQ62"
                , escaped);
    }

    @Test
    void testUnescapeAndSlideEntityIncdicesWithURLEntitiesOnly() throws Exception {
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

    @Test
    void testEscape() {
        String original = "<=% !>";
        String expected = "&lt;=% !&gt;";
        assertEquals(expected, HTMLEntity.escape(original));
        StringBuilder buf = new StringBuilder(original);
        HTMLEntity.escape(buf);
        assertEquals(expected, buf.toString());
    }

    @Test
    void testUnescape() {
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

    @Test
    void testUnescapeAndSlideEntityIncdicesWithCorrectedIndices() throws Exception {
        // #test&amp;test &amp;#test #test&amp; #test&gt;
        // 0123456789012345678901234567890123456789012345
        // 0         1         2         3         4
        //"entities":{"hashtags":[{"text":"test","indices":[0,5]},{"text":"test","indices":[20,25]},{"text":"test","indices":[26,31]},{"text":"test","indices":[37,42]}],"symbols":[],"urls":[],"user_mentions":[]}
        HashtagEntityJSONImpl test1 = new HashtagEntityJSONImpl(0, 5, "test");
        HashtagEntityJSONImpl test2 = new HashtagEntityJSONImpl(20, 25, "test");
        HashtagEntityJSONImpl test3 = new HashtagEntityJSONImpl(26, 31, "test");
        HashtagEntityJSONImpl test4 = new HashtagEntityJSONImpl(37, 42, "test");
        String rawJSON = "{\"text\":\"#test&amp;test &amp;#test #test&amp; #test&gt;\"}";

        JSONObject json = new JSONObject(rawJSON);
        String escaped = HTMLEntity.unescapeAndSlideEntityIncdices(json.getString("text"), null, null, new HashtagEntity[]{test1, test2, test3, test4}, null);
        assertEquals("#test&test &#test #test& #test>", escaped);
        assertEquals("#test", escaped.substring(test1.getStart(), test1.getEnd()));
        assertEquals("#test", escaped.substring(test2.getStart(), test2.getEnd()));
        assertEquals("#test", escaped.substring(test3.getStart(), test3.getEnd()));
        assertEquals("#test", escaped.substring(test4.getStart(), test4.getEnd()));
    }

	/**
	 * When Twitter reports indices of entities, it counts surrogate code points
	 * as a single character. Java, however, treats surrogate code points as two
	 * characters. This means that any entity occurring after a surrogate code
	 * point will be incorrect unless counting code points instead of straight
	 * Java string indexes. This is particularly important to keep in mind when
	 * sliding indices.
	 * <p>
	 * Note that the text of the example tweet used in this test comes from:
	 * https://twitter.com/WHO/status/874656370829799424
	 *
	 * @author Philip Hachey - philip dot hachey at gmail dot com
	 */
	@Test
    void testUnescapeAndSlideEntityIncdicesWithSurrogateCodePoints() throws TwitterException, JSONException {
		// SETUP
		String expectedText = "GREAT NEWS! #Bhutan🇧🇹 & #Maldives🇲🇻 eliminated #measles!\nA landmark achievement, "
				+ "congratulations https://t.co/ywbgldKm1A via @WHOSEARO https://t.co/kJ5dcRR02G";
		String textFromTwitterAPI =
				"GREAT NEWS! #Bhutan🇧🇹 &amp; #Maldives🇲🇻 eliminated #measles!\nA landmark achievement, "
						+ "congratulations https://t.co/ywbgldKm1A via @WHOSEARO https://t.co/kJ5dcRR02G";
		UserMentionEntity[] userMentionEntities =
				{ new UserMentionEntityJSONImpl(129, 138, "WHO South-East Asia", "WHOSEARO", 1545915336L) };
		URLEntity[] urlEntities = { new URLEntityJSONImpl(101, 124, "https://t.co/ywbgldKm1A",
				"http://bit.ly/MeaslesBTNMDV", "bit.ly/MeaslesBTNMDV") };
		HashtagEntityJSONImpl maldivesHashtag = new HashtagEntityJSONImpl(28, 37, "Maldives");
		HashtagEntityJSONImpl measlesHashtag = new HashtagEntityJSONImpl(51, 59, "measles");
		HashtagEntity[] hashtagEntities =
				{ new HashtagEntityJSONImpl(12, 19, "Bhutan"), maldivesHashtag, measlesHashtag };
		MediaEntityJSONImpl mediaEntity =
				new MediaEntityJSONImpl(new JSONObject("{\"id\":874655886366707715,\"id_str\":\"874655886366707715\","
						/* This is the important bit: */ + "\"indices\":[139,162],"
						+ "\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/DCNm5P-XkAMGDy8.jpg\","
						+ "\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/DCNm5P-XkAMGDy8.jpg\","
						+ "\"url\":\"https:\\/\\/t.co\\/kJ5dcRR02G\",\"display_url\":\"pic.twitter.com\\/kJ5dcRR02G\","
						+ "\"expanded_url\":\"https:\\/\\/twitter.com\\/WHO\\/status\\/874656370829799424\\/photo\\/1\","
						+ "\"type\":\"photo\",\"sizes\":{\"small\":{\"w\":680,\"h\":680,\"resize\":\"fit\"},"
						+ "\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"},"
						+ "\"large\":{\"w\":800,\"h\":800,\"resize\":\"fit\"},"
						+ "\"medium\":{\"w\":800,\"h\":800,\"resize\":\"fit\"}}}"));
		MediaEntity[] mediaEntities = { mediaEntity };

		// EXERCISE
		String actualText = HTMLEntity.unescapeAndSlideEntityIncdices(textFromTwitterAPI, userMentionEntities,
				urlEntities, hashtagEntities, mediaEntities);

		// VERIFY
		assertEquals(expectedText, actualText);
		/*
		 * Assert indexes are as Java's String.substring would understand them,
		 * not as Twitter does
		 */
		assertEquals("#Maldives", actualText.substring(maldivesHashtag.getStart(), maldivesHashtag.getEnd()));
		assertEquals("#measles", actualText.substring(measlesHashtag.getStart(), measlesHashtag.getEnd()));
		assertEquals("https://t.co/kJ5dcRR02G", actualText.substring(mediaEntity.getStart(), mediaEntity.getEnd()));
		assertEquals(actualText.length(), mediaEntity.getEnd());
	}

	@Test
    void testUnescapeAndSlideEntityIncdicesAtBoundariesWithSurrogateCodePoints() throws Exception {
		// SETUP
		HashtagEntityJSONImpl test1 = new HashtagEntityJSONImpl(0, 4, "one");
		HashtagEntityJSONImpl test2 = new HashtagEntityJSONImpl(21, 25, "two");
		HashtagEntityJSONImpl test3 = new HashtagEntityJSONImpl(26, 32, "three");
		HashtagEntityJSONImpl test4 = new HashtagEntityJSONImpl(38, 43, "four");
		String rawJSON = "{\"text\":\"#one🇧&amp;🇻test &amp;#two #three&amp; #four&gt;\"}";
		JSONObject json = new JSONObject(rawJSON);

		// EXERCISE
		String escaped = HTMLEntity.unescapeAndSlideEntityIncdices(json.getString("text"), null, null,
				new HashtagEntity[] { test1, test2, test3, test4 }, null);

		// VERIFY
		assertEquals("#one🇧&🇻test &#two #three& #four>", escaped);
		assertEquals("#one", escaped.substring(test1.getStart(), test1.getEnd()));
		assertEquals("#two", escaped.substring(test2.getStart(), test2.getEnd()));
		assertEquals("#three", escaped.substring(test3.getStart(), test3.getEnd()));
		assertEquals("#four", escaped.substring(test4.getStart(), test4.getEnd()));
	}

	@Test
    void testUnescapeAndSlideEntityIncdicesWithSurrogateCodePointsAtBoundaries() {

		String expectedText = "🇧 & #Maldives🇻";
		String textFromTwitterAPI = "🇧 &amp; #Maldives🇻";
		HashtagEntityJSONImpl maldivesHashtag = new HashtagEntityJSONImpl(8, 17, "Maldives");
		HashtagEntity[] hashtagEntities = { maldivesHashtag };

		// EXERCISE
		String actualText =
				HTMLEntity.unescapeAndSlideEntityIncdices(textFromTwitterAPI, null, null, hashtagEntities, null);

		// VERIFY
		assertEquals(expectedText, actualText);
		assertEquals("#Maldives", actualText.substring(maldivesHashtag.getStart(), maldivesHashtag.getEnd()));
	}

	@Test
    void testUnescapeAndSlideEntityIncdicesWithSurrogateCodePointsInEntities() {

		String expectedText = "#🇧hutan& #Maldi🇻es";
		String textFromTwitterAPI = "#🇧hutan&amp; #Maldi🇻es";
		HashtagEntityJSONImpl bhutanHashtag = new HashtagEntityJSONImpl(0, 7, "🇧hutan");
		HashtagEntityJSONImpl maldivesHashtag = new HashtagEntityJSONImpl(13, 22, "Maldi🇻es");
		HashtagEntity[] hashtagEntities = { bhutanHashtag, maldivesHashtag };

		// EXERCISE
		String actualText =
				HTMLEntity.unescapeAndSlideEntityIncdices(textFromTwitterAPI, null, null, hashtagEntities, null);

		// VERIFY
		assertEquals(expectedText, actualText);
		assertEquals("#🇧hutan", actualText.substring(bhutanHashtag.getStart(), bhutanHashtag.getEnd()));
		assertEquals("#Maldi🇻es", actualText.substring(maldivesHashtag.getStart(), maldivesHashtag.getEnd()));
	}
}
