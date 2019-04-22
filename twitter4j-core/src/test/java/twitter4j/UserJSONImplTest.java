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
 *
 * @author Naoya Hatayama - applepedlar at gmail.com
 */

package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserJSONImplTest {

    @Test
    void testGetDescriptionURLEntities1() throws JSONException, TwitterException {
        String rawJson = "{\"id\":219570417,\"id_str\":\"219570417\",\"name\":\"\\u3066\\u3059\\u3068\",\"screen_name\":\"gjmp9\",\"location\":\"\\u65e5\\u672c\",\"description\":\"&lt;test&gt; url: http:\\/\\/t.co\\/UcHD19ZC url2: http:\\/\\/t.co\\/dRuJ7wCm subaccount: @gjmp10 hashtag: #test\",\"url\":\"http:\\/\\/fdghj.com\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"http:\\/\\/fdghj.com\",\"expanded_url\":null,\"indices\":[0,16]}]},\"description\":{\"urls\":[{\"url\":\"http:\\/\\/t.co\\/UcHD19ZC\",\"expanded_url\":\"http:\\/\\/test.com\\/\",\"display_url\":\"test.com\",\"indices\":[18,38]},{\"url\":\"http:\\/\\/t.co\\/dRuJ7wCm\",\"expanded_url\":\"http:\\/\\/longurl.com\\/abcdefghijklmnopqrstuvwxyz\",\"display_url\":\"longurl.com\\/abcdefghijklmn\\u2026\",\"indices\":[45,65]}]}},\"protected\":false,\"followers_count\":8,\"friends_count\":11,\"listed_count\":0,\"created_at\":\"Thu Nov 25 06:47:37 +0000 2010\",\"favourites_count\":1,\"utc_offset\":-36000,\"time_zone\":\"Hawaii\",\"geo_enabled\":false,\"verified\":false,\"statuses_count\":280,\"lang\":\"ja\",\"status\":{\"created_at\":\"Wed Dec 05 01:53:33 +0000 2012\",\"id\":276142234003468288,\"id_str\":\"276142234003468288\",\"text\":\"\\u307b\\u3052\\u307b\\u3052\",\"source\":\"\\u003ca href=\\\"http:\\/\\/jigtwi.jp\\/?p=1001\\\" rel=\\\"nofollow\\\"\\u003ejigtwi for Android\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false},\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"CBC1E5\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/722705453\\/321f2aba5dcbbc97ec0dd0bf9969cb48.jpeg\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/722705453\\/321f2aba5dcbbc97ec0dd0bf9969cb48.jpeg\",\"profile_background_tile\":true,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/2607446059\\/bip392byfu5669roobg5_normal.png\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/2607446059\\/bip392byfu5669roobg5_normal.png\",\"profile_link_color\":\"B40B43\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"E5507E\",\"profile_text_color\":\"362720\",\"profile_use_background_image\":true,\"default_profile\":false,\"default_profile_image\":false,\"following\":true,\"follow_request_sent\":false,\"notifications\":false}";
        JSONObject json = new JSONObject(rawJson);
        UserJSONImpl user = new UserJSONImpl(json);

        URLEntity[] descriptionUrlEntities = user.getDescriptionURLEntities();
        assertNotNull(descriptionUrlEntities);
        assertEquals(2, descriptionUrlEntities.length);

        assertEquals("http://test.com/", descriptionUrlEntities[0].getExpandedURL());
        assertEquals("test.com", descriptionUrlEntities[0].getDisplayURL());
        assertEquals("http://t.co/UcHD19ZC", descriptionUrlEntities[0].getURL());
        assertEquals(12, descriptionUrlEntities[0].getStart());
        assertEquals(32, descriptionUrlEntities[0].getEnd());
        assertEquals("http://t.co/UcHD19ZC", user.getDescription().substring(descriptionUrlEntities[0].getStart(), descriptionUrlEntities[0].getEnd()));

        assertEquals("http://longurl.com/abcdefghijklmnopqrstuvwxyz", descriptionUrlEntities[1].getExpandedURL());
        assertEquals("longurl.com/abcdefghijklmn…", descriptionUrlEntities[1].getDisplayURL());
        assertEquals("http://t.co/dRuJ7wCm", descriptionUrlEntities[1].getURL());
        assertEquals(39, descriptionUrlEntities[1].getStart());
        assertEquals(59, descriptionUrlEntities[1].getEnd());
        assertEquals("http://t.co/dRuJ7wCm", user.getDescription().substring(descriptionUrlEntities[1].getStart(), descriptionUrlEntities[1].getEnd()));

        assertEquals("<test> url: http://t.co/UcHD19ZC url2: http://t.co/dRuJ7wCm subaccount: @gjmp10 hashtag: #test", user.getDescription());
    }

    @Test
    void testGetDescriptionURLEntities2() throws JSONException, TwitterException {
        String rawJsonWithEmptyDescription = "{\"id\":219570417,\"id_str\":\"219570417\",\"name\":\"\\u3066\\u3059\\u3068\",\"screen_name\":\"gjmp9\",\"location\":\"\\u65e5\\u672c\",\"description\":\"\",\"url\":\"http:\\/\\/fdghj.com\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"http:\\/\\/fdghj.com\",\"expanded_url\":null,\"indices\":[0,16]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":8,\"friends_count\":11,\"listed_count\":0,\"created_at\":\"Thu Nov 25 06:47:37 +0000 2010\",\"favourites_count\":1,\"utc_offset\":-36000,\"time_zone\":\"Hawaii\",\"geo_enabled\":false,\"verified\":false,\"statuses_count\":280,\"lang\":\"ja\",\"status\":{\"created_at\":\"Wed Dec 05 01:53:33 +0000 2012\",\"id\":276142234003468288,\"id_str\":\"276142234003468288\",\"text\":\"\\u307b\\u3052\\u307b\\u3052\",\"source\":\"\\u003ca href=\\\"http:\\/\\/jigtwi.jp\\/?p=1001\\\" rel=\\\"nofollow\\\"\\u003ejigtwi for Android\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false},\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"CBC1E5\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/722705453\\/321f2aba5dcbbc97ec0dd0bf9969cb48.jpeg\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/722705453\\/321f2aba5dcbbc97ec0dd0bf9969cb48.jpeg\",\"profile_background_tile\":true,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/2607446059\\/bip392byfu5669roobg5_normal.png\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/2607446059\\/bip392byfu5669roobg5_normal.png\",\"profile_link_color\":\"B40B43\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"E5507E\",\"profile_text_color\":\"362720\",\"profile_use_background_image\":true,\"default_profile\":false,\"default_profile_image\":false,\"following\":true,\"follow_request_sent\":false,\"notifications\":false}";
        JSONObject jsonWithEmptyDescription = new JSONObject(rawJsonWithEmptyDescription);
        UserJSONImpl userWithEmptyDescription = new UserJSONImpl(jsonWithEmptyDescription);
        assertEquals("", userWithEmptyDescription.getDescription());
        assertEquals(0, userWithEmptyDescription.getDescriptionURLEntities().length);

        String rawJsonWithoutDescription = "{\"id\":219570417,\"id_str\":\"219570417\",\"name\":\"\\u3066\\u3059\\u3068\",\"screen_name\":\"gjmp9\",\"location\":\"\\u65e5\\u672c\",\"url\":\"http:\\/\\/fdghj.com\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"http:\\/\\/fdghj.com\",\"expanded_url\":null,\"indices\":[0,16]}]}},\"protected\":false,\"followers_count\":8,\"friends_count\":11,\"listed_count\":0,\"created_at\":\"Thu Nov 25 06:47:37 +0000 2010\",\"favourites_count\":1,\"utc_offset\":-36000,\"time_zone\":\"Hawaii\",\"geo_enabled\":false,\"verified\":false,\"statuses_count\":280,\"lang\":\"ja\",\"status\":{\"created_at\":\"Wed Dec 05 01:53:33 +0000 2012\",\"id\":276142234003468288,\"id_str\":\"276142234003468288\",\"text\":\"\\u307b\\u3052\\u307b\\u3052\",\"source\":\"\\u003ca href=\\\"http:\\/\\/jigtwi.jp\\/?p=1001\\\" rel=\\\"nofollow\\\"\\u003ejigtwi for Android\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false},\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"CBC1E5\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/722705453\\/321f2aba5dcbbc97ec0dd0bf9969cb48.jpeg\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/722705453\\/321f2aba5dcbbc97ec0dd0bf9969cb48.jpeg\",\"profile_background_tile\":true,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/2607446059\\/bip392byfu5669roobg5_normal.png\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/2607446059\\/bip392byfu5669roobg5_normal.png\",\"profile_link_color\":\"B40B43\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"E5507E\",\"profile_text_color\":\"362720\",\"profile_use_background_image\":true,\"default_profile\":false,\"default_profile_image\":false,\"following\":true,\"follow_request_sent\":false,\"notifications\":false}";
        JSONObject jsonWithoutDescription = new JSONObject(rawJsonWithoutDescription);
        UserJSONImpl userWithoutDescription = new UserJSONImpl(jsonWithoutDescription);
        assertEquals(null, userWithoutDescription.getDescription());
        assertEquals(0, userWithoutDescription.getDescriptionURLEntities().length);
    }

    @Test
    void testProfileImageURL() throws JSONException, TwitterException {
        String rawJsonWithoutProfileImageExtension = "{\"id\":400609977,\"id_str\":\"400609977\",\"name\":\"Chris Bautista\",\"screen_name\":\"ayecrispy\",\"location\":\"Jacksonville, FL\",\"description\":\"I'm a gamer and will always be one. I like to keep up with the entertainment life. Where it's celebrities, technology or trying to keep up with latest trend\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":17,\"friends_count\":177,\"listed_count\":0,\"created_at\":\"Sat Oct 29 09:23:10 +0000 2011\",\"favourites_count\":0,\"utc_offset\":-18000,\"time_zone\":\"Eastern Time (US & Canada)\",\"geo_enabled\":false,\"verified\":false,\"statuses_count\":113,\"lang\":\"en\",\"status\":{\"created_at\":\"Sun Dec 16 02:37:57 +0000 2012\",\"id\":280139673333035008,\"id_str\":\"280139673333035008\",\"text\":\"Gotta love olive Garden!\",\"source\":\"\\u003ca href=\\\"http:\\/\\/tweedleapp.com\\/\\\" rel=\\\"nofollow\\\"\\u003e Tweedle\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false},\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"1A1B1F\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/images\\/themes\\/theme9\\/bg.gif\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/images\\/themes\\/theme9\\/bg.gif\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/1835646533\\/gu44kEhi_normal\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/1835646533\\/gu44kEhi_normal\",\"profile_link_color\":\"2FC2EF\",\"profile_sidebar_border_color\":\"181A1E\",\"profile_sidebar_fill_color\":\"252429\",\"profile_text_color\":\"666666\",\"profile_use_background_image\":true,\"default_profile\":false,\"default_profile_image\":false,\"following\":false,\"follow_request_sent\":false,\"notifications\":false}";
        JSONObject jsonWithoutProfileImageExtension = new JSONObject(rawJsonWithoutProfileImageExtension);
        UserJSONImpl userWithoutProfileImageExtension = new UserJSONImpl(jsonWithoutProfileImageExtension);

        assertEquals("https://si0.twimg.com/profile_images/1835646533/gu44kEhi_bigger", userWithoutProfileImageExtension.getBiggerProfileImageURLHttps());
        assertEquals("https://si0.twimg.com/profile_images/1835646533/gu44kEhi", userWithoutProfileImageExtension.getOriginalProfileImageURLHttps());
        assertFalse(userWithoutProfileImageExtension.isDefaultProfileImage());
    }

    @Test
    void testUserWithheldInCountry() throws JSONException, TwitterException {
        String rawJsonWithUserWithheldInCountry = "{\"id\":1635783247,\"id_str\":\"1635783247\",\"name\":\"Legalitolko\",\"screen_name\":\"Legalitolko\",\"location\":\"\",\"description\":\"http:\\/\\/t.co\\/tRceV4DclG - \\u041c\\u0430\\u0433\\u0430\\u0437\\u0438\\u043d \\u0440\\u0435\\u0430\\u0433\\u0435\\u043d\\u0442\\u043e\\u0432 \\u0438 \\u043c\\u0438\\u043a\\u0441\\u043e\\u0432. \\u041d\\u0430 \\u0440\\u044b\\u043d\\u043a\\u0435 \\u0443\\u0436\\u0435 2 \\u0433\\u043e\\u0434\\u0430.\",\"url\":null,\"entities\":{\"description\":{\"urls\":[{\"url\":\"http:\\/\\/t.co\\/tRceV4DclG\",\"expanded_url\":\"http:\\/\\/Legalitolko.com\",\"display_url\":\"Legalitolko.com\",\"indices\":[0,22]}]}},\"protected\":false,\"followers_count\":9,\"friends_count\":0,\"listed_count\":0,\"created_at\":\"Wed Jul 31 15:36:56 +0000 2013\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":5,\"lang\":\"ru\",\"status\":{\"created_at\":\"Tue Aug 13 11:15:18 +0000 2013\",\"id\":367242956035465217,\"id_str\":\"367242956035465217\",\"text\":\"\\u0426\\u0435\\u043d\\u044b \\u043e\\u043f\\u043d\\u0438\\u0436\\u0435\\u043d\\u044b \\u043d\\u0430 10%. \\u0421\\u043a\\u043e\\u0440\\u043e \\u0432 \\u043d\\u0430\\u043b\\u0438\\u0447\\u0438\\u0438 \\u0441\\u043a\\u043e\\u0440\\u043e\\u0441\\u0442\\u044c a-PVT \\u0438 \\u043d\\u043e\\u0432\\u044b\\u0439 \\u043c\\u0438\\u043a\\u0441 \\u043d\\u0430 \\u043e\\u0441\\u043d\\u043e\\u0432\\u0435 LTI355+LTI-390\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web Client\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"withheld_in_countries\":[\"RU\"],\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false,\"lang\":\"ru\"},\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/378800000220401367\\/42fd37e349a4bece164ffab5e644589b_normal.png\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/378800000220401367\\/42fd37e349a4bece164ffab5e644589b_normal.png\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":true,\"default_profile_image\":false,\"following\":false,\"follow_request_sent\":false,\"notifications\":false,\"withheld_in_countries\":[\"RU\"]}";
        JSONObject jsonWithUserWithheldInCountry = new JSONObject(rawJsonWithUserWithheldInCountry);
        UserJSONImpl userWithheldInCountry = new UserJSONImpl(jsonWithUserWithheldInCountry);

        String[] withheldInCountries = userWithheldInCountry.getWithheldInCountries();
        assertEquals(1, withheldInCountries.length);
        assertEquals("RU", withheldInCountries[0]);
    }

    @Test
    void testAfter20190520Changes() throws TwitterException {
        // https://twittercommunity.com/t/upcoming-changes-to-user-object-and-get-users-suggestions-endpoints/124732

        // Settings values (these will only be available via GET account/settings)
        // - lang
        // - geo_enabled

        // Follow relationship values (available only via GET friendships/lookup)
        // - follow_request_sent

        // Obsolete values
        // - profile_location
        // - is_translation_enabled
        // - translator_type
        // - has_extended_profile
        // - profile_background_color
        // - profile_background_image_url
        // - profile_background_image_url_https
        // - profile_background_tile
        // - profile_image_url
        // - profile_link_color
        // - profile_sidebar_border_color
        // - profile_sidebar_fill_color
        // - profile_text_color
        // - profile_use_background_image

        // before changes
        // - has settings values
        // - has follow_request_sent
        // - has obsolete values
        {
            //language=JSON
            String rawJson = "{\"id\":72297675,\"id_str\":\"72297675\",\"name\":\"Tw\\u00edtter4J\",\"screen_name\":\"t4j_news\",\"location\":\"timeline\",\"profile_location\":null,\"description\":\"Award winning library - Twitter4J\",\"url\":\"https:\\/\\/t.co\\/j1dV74W2db\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https:\\/\\/t.co\\/j1dV74W2db\",\"expanded_url\":\"http:\\/\\/twitter4j.org\\/\",\"display_url\":\"twitter4j.org\",\"indices\":[0,23]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":1912,\"friends_count\":19,\"listed_count\":137,\"created_at\":\"Mon Sep 07 14:50:12 +0000 2009\",\"favourites_count\":3,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"verified\":false,\"statuses_count\":980,\"lang\":\"en\",\"status\":{\"created_at\":\"Thu Dec 13 14:29:06 +0000 2018\",\"id\":1073223291995734020,\"id_str\":\"1073223291995734020\",\"text\":\"RT @cscheerleader: I appreciate that @twitter is cracking down on spam bots, but the result is that my students are having a harder time do\\u2026\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"cscheerleader\",\"name\":\"Ria Galanos\",\"id\":1250080500,\"id_str\":\"1250080500\",\"indices\":[3,17]},{\"screen_name\":\"Twitter\",\"name\":\"Twitter\",\"id\":783214,\"id_str\":\"783214\",\"indices\":[37,45]}],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/sites.google.com\\/site\\/yorufukurou\\/\\\" rel=\\\"nofollow\\\"\\u003eYoruFukurou\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Thu Dec 13 14:23:18 +0000 2018\",\"id\":1073221831715360768,\"id_str\":\"1073221831715360768\",\"text\":\"I appreciate that @twitter is cracking down on spam bots, but the result is that my students are having a harder ti\\u2026 https:\\/\\/t.co\\/5N603Vh6mP\",\"truncated\":true,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"Twitter\",\"name\":\"Twitter\",\"id\":783214,\"id_str\":\"783214\",\"indices\":[18,26]}],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/5N603Vh6mP\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1073221831715360768\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1\\u2026\",\"indices\":[117,140]}]},\"source\":\"\\u003ca href=\\\"https:\\/\\/about.twitter.com\\/products\\/tweetdeck\\\" rel=\\\"nofollow\\\"\\u003eTweetDeck\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":2,\"favorite_count\":6,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"is_quote_status\":false,\"retweet_count\":2,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/708189314073501696\\/jjNlTEQk_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/708189314073501696\\/jjNlTEQk_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/72297675\\/1540491420\",\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":false,\"following\":true,\"follow_request_sent\":false,\"notifications\":false,\"translator_type\":\"none\"}";
            JSONObject json = new JSONObject(rawJson);
            UserJSONImpl user = new UserJSONImpl(json);
            assertEquals("t4j_news", user.getScreenName());
            assertEquals("en", user.getLang());
            assertTrue(user.isGeoEnabled());
            assertFalse(user.isFollowRequestSent());
        }

        // user object via GET account/settings:
        // - has settings values
        // - without follow_request_sent
        // - without obsolete values
        {
            //language=JSON
            String rawJson = "{\"id\":72297675,\"id_str\":\"72297675\",\"name\":\"Tw\\u00edtter4J\",\"screen_name\":\"t4j_news\",\"location\":\"timeline\",\"description\":\"Award winning library - Twitter4J\",\"url\":\"https:\\/\\/t.co\\/j1dV74W2db\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https:\\/\\/t.co\\/j1dV74W2db\",\"expanded_url\":\"http:\\/\\/twitter4j.org\\/\",\"display_url\":\"twitter4j.org\",\"indices\":[0,23]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":1912,\"friends_count\":19,\"listed_count\":137,\"created_at\":\"Mon Sep 07 14:50:12 +0000 2009\",\"favourites_count\":3,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"verified\":false,\"statuses_count\":980,\"lang\":\"en\",\"status\":{\"created_at\":\"Thu Dec 13 14:29:06 +0000 2018\",\"id\":1073223291995734020,\"id_str\":\"1073223291995734020\",\"text\":\"RT @cscheerleader: I appreciate that @twitter is cracking down on spam bots, but the result is that my students are having a harder time do\\u2026\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"cscheerleader\",\"name\":\"Ria Galanos\",\"id\":1250080500,\"id_str\":\"1250080500\",\"indices\":[3,17]},{\"screen_name\":\"Twitter\",\"name\":\"Twitter\",\"id\":783214,\"id_str\":\"783214\",\"indices\":[37,45]}],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/sites.google.com\\/site\\/yorufukurou\\/\\\" rel=\\\"nofollow\\\"\\u003eYoruFukurou\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Thu Dec 13 14:23:18 +0000 2018\",\"id\":1073221831715360768,\"id_str\":\"1073221831715360768\",\"text\":\"I appreciate that @twitter is cracking down on spam bots, but the result is that my students are having a harder ti\\u2026 https:\\/\\/t.co\\/5N603Vh6mP\",\"truncated\":true,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"Twitter\",\"name\":\"Twitter\",\"id\":783214,\"id_str\":\"783214\",\"indices\":[18,26]}],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/5N603Vh6mP\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1073221831715360768\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1\\u2026\",\"indices\":[117,140]}]},\"source\":\"\\u003ca href=\\\"https:\\/\\/about.twitter.com\\/products\\/tweetdeck\\\" rel=\\\"nofollow\\\"\\u003eTweetDeck\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":2,\"favorite_count\":6,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"is_quote_status\":false,\"retweet_count\":2,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"contributors_enabled\":false,\"is_translator\":false,\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/708189314073501696\\/jjNlTEQk_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/72297675\\/1540491420\",\"default_profile\":true,\"default_profile_image\":false,\"following\":true,\"notifications\":false}";
            UserJSONImpl user = new UserJSONImpl(new JSONObject(rawJson));
            assertEquals("t4j_news", user.getScreenName());
            assertEquals("en", user.getLang());
            assertTrue(user.isGeoEnabled());
            assertFalse(user.isFollowRequestSent());
        }

        // user object via GET friendships/lookup
        // - without settings values
        // - has follow_request_sent
        // - without obsolete values
        {
            //language=JSON
            String rawJson = "{\"id\":72297675,\"id_str\":\"72297675\",\"name\":\"Tw\\u00edtter4J\",\"screen_name\":\"t4j_news\",\"location\":\"timeline\",\"description\":\"Award winning library - Twitter4J\",\"url\":\"https:\\/\\/t.co\\/j1dV74W2db\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https:\\/\\/t.co\\/j1dV74W2db\",\"expanded_url\":\"http:\\/\\/twitter4j.org\\/\",\"display_url\":\"twitter4j.org\",\"indices\":[0,23]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":1912,\"friends_count\":19,\"listed_count\":137,\"created_at\":\"Mon Sep 07 14:50:12 +0000 2009\",\"favourites_count\":3,\"utc_offset\":null,\"time_zone\":null,\"verified\":false,\"statuses_count\":980,\"status\":{\"created_at\":\"Thu Dec 13 14:29:06 +0000 2018\",\"id\":1073223291995734020,\"id_str\":\"1073223291995734020\",\"text\":\"RT @cscheerleader: I appreciate that @twitter is cracking down on spam bots, but the result is that my students are having a harder time do\\u2026\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"cscheerleader\",\"name\":\"Ria Galanos\",\"id\":1250080500,\"id_str\":\"1250080500\",\"indices\":[3,17]},{\"screen_name\":\"Twitter\",\"name\":\"Twitter\",\"id\":783214,\"id_str\":\"783214\",\"indices\":[37,45]}],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/sites.google.com\\/site\\/yorufukurou\\/\\\" rel=\\\"nofollow\\\"\\u003eYoruFukurou\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Thu Dec 13 14:23:18 +0000 2018\",\"id\":1073221831715360768,\"id_str\":\"1073221831715360768\",\"text\":\"I appreciate that @twitter is cracking down on spam bots, but the result is that my students are having a harder ti\\u2026 https:\\/\\/t.co\\/5N603Vh6mP\",\"truncated\":true,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"Twitter\",\"name\":\"Twitter\",\"id\":783214,\"id_str\":\"783214\",\"indices\":[18,26]}],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/5N603Vh6mP\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1073221831715360768\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1\\u2026\",\"indices\":[117,140]}]},\"source\":\"\\u003ca href=\\\"https:\\/\\/about.twitter.com\\/products\\/tweetdeck\\\" rel=\\\"nofollow\\\"\\u003eTweetDeck\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":2,\"favorite_count\":6,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"is_quote_status\":false,\"retweet_count\":2,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"contributors_enabled\":false,\"is_translator\":false,\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/708189314073501696\\/jjNlTEQk_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/72297675\\/1540491420\",\"default_profile\":true,\"default_profile_image\":false,\"following\":true,\"follow_request_sent\":false,\"notifications\":false}";
            UserJSONImpl user = new UserJSONImpl(new JSONObject(rawJson));
            assertEquals("t4j_news", user.getScreenName());
            assertNull(user.getLang());
            assertFalse(user.isGeoEnabled());
            assertFalse(user.isFollowRequestSent());
        }

        // user object via the other endpoints
        // - without settings values
        // - without follow_request_sent
        // - without obsolete values
        {
            //language=JSON
            String rawJson = "{\"id\":72297675,\"id_str\":\"72297675\",\"name\":\"Tw\\u00edtter4J\",\"screen_name\":\"t4j_news\",\"location\":\"timeline\",\"description\":\"Award winning library - Twitter4J\",\"url\":\"https:\\/\\/t.co\\/j1dV74W2db\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https:\\/\\/t.co\\/j1dV74W2db\",\"expanded_url\":\"http:\\/\\/twitter4j.org\\/\",\"display_url\":\"twitter4j.org\",\"indices\":[0,23]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":1912,\"friends_count\":19,\"listed_count\":137,\"created_at\":\"Mon Sep 07 14:50:12 +0000 2009\",\"favourites_count\":3,\"utc_offset\":null,\"time_zone\":null,\"verified\":false,\"statuses_count\":980,\"status\":{\"created_at\":\"Thu Dec 13 14:29:06 +0000 2018\",\"id\":1073223291995734020,\"id_str\":\"1073223291995734020\",\"text\":\"RT @cscheerleader: I appreciate that @twitter is cracking down on spam bots, but the result is that my students are having a harder time do\\u2026\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"cscheerleader\",\"name\":\"Ria Galanos\",\"id\":1250080500,\"id_str\":\"1250080500\",\"indices\":[3,17]},{\"screen_name\":\"Twitter\",\"name\":\"Twitter\",\"id\":783214,\"id_str\":\"783214\",\"indices\":[37,45]}],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/sites.google.com\\/site\\/yorufukurou\\/\\\" rel=\\\"nofollow\\\"\\u003eYoruFukurou\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Thu Dec 13 14:23:18 +0000 2018\",\"id\":1073221831715360768,\"id_str\":\"1073221831715360768\",\"text\":\"I appreciate that @twitter is cracking down on spam bots, but the result is that my students are having a harder ti\\u2026 https:\\/\\/t.co\\/5N603Vh6mP\",\"truncated\":true,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"Twitter\",\"name\":\"Twitter\",\"id\":783214,\"id_str\":\"783214\",\"indices\":[18,26]}],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/5N603Vh6mP\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1073221831715360768\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1\\u2026\",\"indices\":[117,140]}]},\"source\":\"\\u003ca href=\\\"https:\\/\\/about.twitter.com\\/products\\/tweetdeck\\\" rel=\\\"nofollow\\\"\\u003eTweetDeck\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":2,\"favorite_count\":6,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"is_quote_status\":false,\"retweet_count\":2,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"contributors_enabled\":false,\"is_translator\":false,\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/708189314073501696\\/jjNlTEQk_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/72297675\\/1540491420\",\"default_profile\":true,\"default_profile_image\":false,\"following\":true,\"notifications\":false}";
            UserJSONImpl user = new UserJSONImpl(new JSONObject(rawJson));
            assertEquals("t4j_news", user.getScreenName());
            assertNull(user.getLang());
            assertFalse(user.isGeoEnabled());
            assertFalse(user.isFollowRequestSent());
        }
    }
}
