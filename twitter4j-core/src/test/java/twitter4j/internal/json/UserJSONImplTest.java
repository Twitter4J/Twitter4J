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

package twitter4j.internal.json;

import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import junit.framework.TestCase;

public class UserJSONImplTest extends TestCase {
    
    public void testGetDescriptionURLEntities1() throws JSONException, TwitterException {
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
    
    public void testGetDescriptionURLEntities2() throws JSONException, TwitterException {
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
    
    public void testGetURLEntity1() throws JSONException, TwitterException {
        // full url entity
        String rawJson = "{\"profile_sidebar_fill_color\":\"DDEEF6\",\"id\":219570539,\"favourites_count\":252,\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/736851794\\/e6f0992a68eda21a2809efbec2f88050.jpeg\",\"screen_name\":\"gjmp10\",\"following\":false,\"location\":\"\\u4eac\\u90fd\\u5e9c\\u4eac\\u90fd\\u5e02\\u6771\\u5c71\\u533a\\u4e09\\u6761\\u901a\\u5357\\u4e8c\\u7b4b\\u76ee\\u767d\\u5ddd\\u7b4b\\u897f\\u5165\\u30eb\\u4e8c\\u4e01\\u76ee\",\"contributors_enabled\":false,\"profile_background_color\":\"FF8F00\",\"time_zone\":\"Osaka\",\"utc_offset\":32400,\"name\":\"\\u9577\\u3044\\u9577\\u3044\\u9577\\u3044\\u9577\\u3044\\u3042\\u3042\\u3042\\u3042\\u3042\\u3042\\u304b\\u3055\\u305f\\u306a\\u306f\\u3046\",\"notifications\":false,\"geo_enabled\":true,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/2964940756\\/51e2ad9e42297e2be926ec4794b95267_normal.jpeg\",\"id_str\":\"219570539\",\"default_profile\":false,\"follow_request_sent\":false,\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/736851794\\/e6f0992a68eda21a2809efbec2f88050.jpeg\",\"protected\":false,\"profile_link_color\":\"0084B4\",\"verified\":false,\"entities\":{\"description\":{\"urls\":[{\"expanded_url\":\"http:\\/\\/jigtwi.jp\\/\",\"display_url\":\"jigtwi.jp\",\"url\":\"http:\\/\\/t.co\\/n578Jr6S\",\"indices\":[31,51]},{\"expanded_url\":\"http:\\/\\/twitpic.com\\/7tx0wk\",\"display_url\":\"twitpic.com\\/7tx0wk\",\"url\":\"http:\\/\\/t.co\\/i2cX7pEY\",\"indices\":[52,72]},{\"expanded_url\":\"http:\\/\\/twitter.com\\/ak_12\\/status\\/12813969077051392\",\"display_url\":\"twitter.com\\/ak_12\\/status\\/1\\u2026\",\"url\":\"http:\\/\\/t.co\\/PlItS8jh\",\"indices\":[77,97]}]},\"url\":{\"urls\":[{\"display_url\":\"google.co.jp\\/search?q=adgjg\",\"expanded_url\":\"https:\\/\\/www.google.co.jp\\/search?q=adgjg\",\"indices\":[0,21],\"url\":\"https:\\/\\/t.co\\/gLcBbz6G\"}]}},\"listed_count\":5,\"profile_use_background_image\":true,\"statuses_count\":979,\"profile_text_color\":\"333333\",\"created_at\":\"Thu Nov 25 06:48:26 +0000 2010\",\"lang\":\"ja\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/2964940756\\/51e2ad9e42297e2be926ec4794b95267_normal.jpeg\",\"is_translator\":false,\"profile_sidebar_border_color\":\"FFFFFF\",\"friends_count\":12,\"url\":\"https:\\/\\/t.co\\/gLcBbz6G\",\"default_profile_image\":false,\"followers_count\":10,\"description\":\"\\n09012345678\\n@kouda12345 #test http:\\/\\/t.co\\/n578Jr6S http:\\/\\/t.co\\/i2cX7pEY     http:\\/\\/t.co\\/PlItS8jh 0123456789\\n\\u3066\\u3059\\u3068 @gjmp9\",\"profile_background_tile\":true}";
        JSONObject json = new JSONObject(rawJson);
        UserJSONImpl user = new UserJSONImpl(json);
        
        URLEntity urlEntity = user.getURLEntity();
        assertNotNull(urlEntity);
        assertEquals("https://www.google.co.jp/search?q=adgjg", urlEntity.getExpandedURL());
        assertEquals("google.co.jp/search?q=adgjg", urlEntity.getDisplayURL());
        assertEquals("https://t.co/gLcBbz6G", urlEntity.getURL());
        assertEquals(0, urlEntity.getStart());
        assertEquals(21, urlEntity.getEnd());
    }
    
    public void testGetURLEntity2() throws JSONException, TwitterException {
        // url, indices only
        String rawJson = "{\"profile_sidebar_fill_color\":\"F3F3F3\",\"id\":4311171,\"favourites_count\":118,\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/725168446\\/fed71f230b033a13a69d803d464871ec.gif\",\"screen_name\":\"ApplePedlar\",\"following\":false,\"location\":\"Fukui, Japan\",\"contributors_enabled\":false,\"profile_background_color\":\"FDF4D3\",\"time_zone\":\"Tokyo\",\"utc_offset\":32400,\"name\":\"Naoya Hatayama\",\"notifications\":false,\"geo_enabled\":true,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/1197534673\\/100x100_normal.png\",\"id_str\":\"4311171\",\"default_profile\":false,\"follow_request_sent\":false,\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/725168446\\/fed71f230b033a13a69d803d464871ec.gif\",\"protected\":false,\"profile_link_color\":\"990000\",\"verified\":false,\"entities\":{\"description\":{\"urls\":[]},\"url\":{\"urls\":[{\"display_url\":null,\"expanded_url\":null,\"indices\":[0,30],\"url\":\"http:\\/\\/twitter.com\\/ApplePedlar\"}]}},\"listed_count\":74,\"profile_use_background_image\":true,\"statuses_count\":10130,\"profile_text_color\":\"333333\",\"created_at\":\"Thu Apr 12 06:27:44 +0000 2007\",\"lang\":\"ja\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/1197534673\\/100x100_normal.png\",\"is_translator\":false,\"profile_sidebar_border_color\":\"FFFFFF\",\"friends_count\":984,\"url\":\"http:\\/\\/twitter.com\\/ApplePedlar\",\"default_profile_image\":false,\"followers_count\":901,\"description\":\"\\u3081\\u304c\\u306d\\u4f1a\\u9928\\u3067\\u50cd\\u304f\\u30e2\\u30d0\\u30a4\\u30eb\\u7cfb\\u958b\\u767a\\u8005\\u3067\\u3059\\n#kosen #fukui #sabae #jigtwi #Twitter4J #DQ10\",\"profile_background_tile\":true}";
        JSONObject json = new JSONObject(rawJson);
        UserJSONImpl user = new UserJSONImpl(json);
        
        URLEntity urlEntity = user.getURLEntity();
        assertNotNull(urlEntity);
        assertEquals("http://twitter.com/ApplePedlar", urlEntity.getExpandedURL());
        assertEquals("http://twitter.com/ApplePedlar", urlEntity.getDisplayURL());
        assertEquals("http://twitter.com/ApplePedlar", urlEntity.getURL());
        assertEquals(0, urlEntity.getStart());
        assertEquals(30, urlEntity.getEnd());
    }
    
    public void testGetURLEntity3() throws JSONException, TwitterException {
        // url is null
        String rawJson = "{\"id\":78941611,\"id_str\":\"78941611\",\"name\":\"Harsha Bhogle\",\"screen_name\":\"bhogleharsha\",\"location\":\"Mumbai, India\",\"description\":\"once upon a time a chem engr and mgmt grad,now cricket and motivational speaking my calling!\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":540458,\"friends_count\":83,\"listed_count\":4229,\"created_at\":\"Thu Oct 01 16:18:03 +0000 2009\",\"favourites_count\":3,\"utc_offset\":19800,\"time_zone\":\"Chennai\",\"geo_enabled\":false,\"verified\":true,\"statuses_count\":12205,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/2769690929\\/61db65334849e8876c538051e885cdab_normal.png\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/2769690929\\/61db65334849e8876c538051e885cdab_normal.png\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null}";
        JSONObject json = new JSONObject(rawJson);
        UserJSONImpl user = new UserJSONImpl(json);
        
        assertNull(user.getURL());
        
        URLEntity urlEntity = user.getURLEntity();
        assertNotNull(urlEntity);
        assertEquals("", urlEntity.getExpandedURL());
        assertEquals("", urlEntity.getDisplayURL());
        assertEquals("", urlEntity.getURL());
        assertEquals(0, urlEntity.getStart());
        assertEquals(0, urlEntity.getEnd());
    }
    
    
}
