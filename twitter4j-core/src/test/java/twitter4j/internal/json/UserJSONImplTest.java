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
    
    public void testGetDescriptionUrlEntities1() throws JSONException, TwitterException {
        String rawJson = "{\"id\":219570539,\"id_str\":\"219570539\",\"name\":\"\\u9577\\u3044\\u9577\\u3044\\u9577\\u3044\\u9577\\u3044\\u3042\\u3042\\u3042\\u3042\\u3042\\u3042\\u304b\\u3055\\u305f\\u306a\\u306f\\u3046\",\"screen_name\":\"gjmp10\",\"location\":\"\\u4eac\\u90fd\\u5e9c\\u4eac\\u90fd\\u5e02\\u6771\\u5c71\\u533a\\u4e09\\u6761\\u901a\\u5357\\u4e8c\\u7b4b\\u76ee\\u767d\\u5ddd\\u7b4b\\u897f\\u5165\\u30eb\\u4e8c\\u4e01\\u76ee\",\"description\":\"aa\\n09012345678\\n@kouda12345 #test http:\\/\\/t.co\\/n578Jr6S http:\\/\\/t.co\\/i2cX7pEY     http:\\/\\/t.co\\/PlItS8jh 0123456789\\n\\u3066\\u3059\\u3068 @gjmp9\",\"url\":\"https:\\/\\/www.google.co.jp\\/search?q=adgjm\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https:\\/\\/www.google.co.jp\\/search?q=adgjm\",\"expanded_url\":null,\"indices\":[0,39]}]},\"description\":{\"urls\":[{\"url\":\"http:\\/\\/t.co\\/n578Jr6S\",\"expanded_url\":\"http:\\/\\/jigtwi.jp\\/\",\"display_url\":\"jigtwi.jp\",\"indices\":[33,53]},{\"url\":\"http:\\/\\/t.co\\/i2cX7pEY\",\"expanded_url\":\"http:\\/\\/twitpic.com\\/7tx0wk\",\"display_url\":\"twitpic.com\\/7tx0wk\",\"indices\":[54,74]},{\"url\":\"http:\\/\\/t.co\\/PlItS8jh\",\"expanded_url\":\"http:\\/\\/twitter.com\\/ak_12\\/status\\/12813969077051392\",\"display_url\":\"twitter.com\\/ak_12\\/status\\/1\\u2026\",\"indices\":[79,99]}]}},\"protected\":false,\"followers_count\":9,\"friends_count\":13,\"listed_count\":6,\"created_at\":\"Thu Nov 25 06:48:26 +0000 2010\",\"favourites_count\":249,\"utc_offset\":32400,\"time_zone\":\"Osaka\",\"geo_enabled\":true,\"verified\":false,\"statuses_count\":964,\"lang\":\"ja\",\"status\":{\"created_at\":\"Wed Dec 05 09:03:31 +0000 2012\",\"id\":276250432630833152,\"id_str\":\"276250432630833152\",\"text\":\"test3 http:\\/\\/t.co\\/oUG7DSeN\",\"source\":\"\\u003ca href=\\\"http:\\/\\/jigtwi.jp\\/?p=1\\\" rel=\\\"nofollow\\\"\\u003ejigtwi\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[],\"media\":[{\"id\":276250432635027456,\"id_str\":\"276250432635027456\",\"indices\":[6,26],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/A9VwTgpCQAAInTL.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/A9VwTgpCQAAInTL.jpg\",\"url\":\"http:\\/\\/t.co\\/oUG7DSeN\",\"display_url\":\"pic.twitter.com\\/oUG7DSeN\",\"expanded_url\":\"http:\\/\\/twitter.com\\/gjmp10\\/status\\/276250432630833152\\/photo\\/1\",\"type\":\"photo\",\"sizes\":{\"medium\":{\"w\":600,\"h\":901,\"resize\":\"fit\"},\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"},\"small\":{\"w\":340,\"h\":510,\"resize\":\"fit\"},\"large\":{\"w\":1024,\"h\":1537,\"resize\":\"fit\"}}}]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false},\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"FF8F00\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/729801918\\/e6f0992a68eda21a2809efbec2f88050.jpeg\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/729801918\\/e6f0992a68eda21a2809efbec2f88050.jpeg\",\"profile_background_tile\":true,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/2934786416\\/e6f0992a68eda21a2809efbec2f88050_normal.jpeg\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/2934786416\\/e6f0992a68eda21a2809efbec2f88050_normal.jpeg\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":false,\"default_profile_image\":false,\"following\":false,\"follow_request_sent\":false,\"notifications\":false}";
        JSONObject json = new JSONObject(rawJson);
        UserJSONImpl user = new UserJSONImpl(json);
        
        URLEntity[] descriptionUrlEntities = user.getDescriptionUrlEntities();
        assertNotNull(descriptionUrlEntities);
        assertEquals(3, descriptionUrlEntities.length);
        
        assertEquals("http://jigtwi.jp/", descriptionUrlEntities[0].getExpandedURL());
        assertEquals("jigtwi.jp", descriptionUrlEntities[0].getDisplayURL());
        assertEquals("http://t.co/n578Jr6S", descriptionUrlEntities[0].getURL());
        assertEquals(33, descriptionUrlEntities[0].getStart());
        assertEquals(53, descriptionUrlEntities[0].getEnd());
        
        assertEquals("http://twitpic.com/7tx0wk", descriptionUrlEntities[1].getExpandedURL());
        assertEquals("twitpic.com/7tx0wk", descriptionUrlEntities[1].getDisplayURL());
        assertEquals("http://t.co/i2cX7pEY", descriptionUrlEntities[1].getURL());
        assertEquals(54, descriptionUrlEntities[1].getStart());
        assertEquals(74, descriptionUrlEntities[1].getEnd());
        
        assertEquals("http://twitter.com/ak_12/status/12813969077051392", descriptionUrlEntities[2].getExpandedURL());
        assertEquals("twitter.com/ak_12/status/1…", descriptionUrlEntities[2].getDisplayURL());
        assertEquals("http://t.co/PlItS8jh", descriptionUrlEntities[2].getURL());
        assertEquals(79, descriptionUrlEntities[2].getStart());
        assertEquals(99, descriptionUrlEntities[2].getEnd());
    }
    
}
