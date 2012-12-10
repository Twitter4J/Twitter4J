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
        
        assertEquals("http://longurl.com/abcdefghijklmnopqrstuvwxyz", descriptionUrlEntities[1].getExpandedURL());
        assertEquals("longurl.com/abcdefghijklmn…", descriptionUrlEntities[1].getDisplayURL());
        assertEquals("http://t.co/dRuJ7wCm", descriptionUrlEntities[1].getURL());
        assertEquals(39, descriptionUrlEntities[1].getStart());
        assertEquals(59, descriptionUrlEntities[1].getEnd());
        
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
    
}
