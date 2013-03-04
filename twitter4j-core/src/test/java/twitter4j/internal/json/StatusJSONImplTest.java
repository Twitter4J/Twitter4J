package twitter4j.internal.json;

import static org.junit.Assert.*;

import junit.framework.TestCase;
import twitter4j.Status;
import twitter4j.json.DataObjectFactory;

/**
 * @author Cedric Meury - cedric at meury.com
 */
public class StatusJSONImplTest extends TestCase {

    public void testGetIsoLanguageCode() throws Exception {
        // given
        String json = "{\"contributors\":null,\"text\":\"@belbeer After four years of study in radiotechnical college I realized that the language skills are much more useful than fucking math.\",\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"belbeer\",\"truncated\":false,\"entities\":{\"urls\":[],\"hashtags\":[],\"user_mentions\":[{\"id\":74987101,\"name\":\"belbeer\",\"indices\":[0,8],\"screen_name\":\"belbeer\",\"id_str\":\"74987101\"}]},\"in_reply_to_status_id_str\":\"297250346298904576\",\"id\":297265580690513920,\"source\":\"web\",\"in_reply_to_user_id_str\":\"74987101\",\"favorited\":false,\"in_reply_to_status_id\":297250346298904576,\"retweet_count\":0,\"created_at\":\"Fri Feb 01 08:50:12 +0000 2013\",\"in_reply_to_user_id\":74987101,\"id_str\":\"297265580690513920\",\"place\":null,\"user\":{\"location\":\"where am i?\",\"default_profile\":true,\"profile_background_tile\":false,\"statuses_count\":1100,\"lang\":\"ru\",\"profile_link_color\":\"0084B4\",\"id\":386522307,\"following\":null,\"protected\":false,\"favourites_count\":13,\"profile_text_color\":\"333333\",\"description\":\"160 символов\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Безумный Рыбник\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Fri Oct 07 12:44:01 +0000 2011\",\"default_profile_image\":false,\"followers_count\":46,\"profile_image_url_https\":\"https://si0.twimg.com/profile_images/2785804589/b0b355c75d3f77658fc94ec05a7bb5af_normal.jpeg\",\"geo_enabled\":true,\"profile_background_image_url\":\"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://si0.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"entities\":{\"description\":{\"urls\":[]},\"url\":{\"urls\":[{\"expanded_url\":null,\"indices\":[0,29],\"url\":\"http://goodfoto.blogspot.com/\"}]}},\"url\":\"http://goodfoto.blogspot.com/\",\"utc_offset\":7200,\"time_zone\":\"Athens\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":46,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"LonliLokli2000\",\"id_str\":\"386522307\",\"profile_image_url\":\"http://a0.twimg.com/profile_images/2785804589/b0b355c75d3f77658fc94ec05a7bb5af_normal.jpeg\",\"listed_count\":1,\"is_translator\":false},\"coordinates\":null,\"metadata\":{\"result_type\":\"recent\",\"iso_language_code\":\"en\"}}";
        Status status = DataObjectFactory.createStatus(json);

        // when
        String lang = status.getIsoLanguageCode();

        // then
        assertEquals("en", lang);
    }

    public void testReturningNullForMissingIsoLanguageCode() throws Exception {
        // given
        String json = "{\"contributors\":null,\"text\":\"@belbeer After four years of study in radiotechnical college I realized that the language skills are much more useful than fucking math.\",\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"belbeer\",\"truncated\":false,\"entities\":{\"urls\":[],\"hashtags\":[],\"user_mentions\":[{\"id\":74987101,\"name\":\"belbeer\",\"indices\":[0,8],\"screen_name\":\"belbeer\",\"id_str\":\"74987101\"}]},\"in_reply_to_status_id_str\":\"297250346298904576\",\"id\":297265580690513920,\"source\":\"web\",\"in_reply_to_user_id_str\":\"74987101\",\"favorited\":false,\"in_reply_to_status_id\":297250346298904576,\"retweet_count\":0,\"created_at\":\"Fri Feb 01 08:50:12 +0000 2013\",\"in_reply_to_user_id\":74987101,\"id_str\":\"297265580690513920\",\"place\":null,\"user\":{\"location\":\"where am i?\",\"default_profile\":true,\"profile_background_tile\":false,\"statuses_count\":1100,\"lang\":\"ru\",\"profile_link_color\":\"0084B4\",\"id\":386522307,\"following\":null,\"protected\":false,\"favourites_count\":13,\"profile_text_color\":\"333333\",\"description\":\"160 символов\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Безумный Рыбник\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Fri Oct 07 12:44:01 +0000 2011\",\"default_profile_image\":false,\"followers_count\":46,\"profile_image_url_https\":\"https://si0.twimg.com/profile_images/2785804589/b0b355c75d3f77658fc94ec05a7bb5af_normal.jpeg\",\"geo_enabled\":true,\"profile_background_image_url\":\"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://si0.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"entities\":{\"description\":{\"urls\":[]},\"url\":{\"urls\":[{\"expanded_url\":null,\"indices\":[0,29],\"url\":\"http://goodfoto.blogspot.com/\"}]}},\"url\":\"http://goodfoto.blogspot.com/\",\"utc_offset\":7200,\"time_zone\":\"Athens\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":46,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"LonliLokli2000\",\"id_str\":\"386522307\",\"profile_image_url\":\"http://a0.twimg.com/profile_images/2785804589/b0b355c75d3f77658fc94ec05a7bb5af_normal.jpeg\",\"listed_count\":1,\"is_translator\":false},\"coordinates\":null}";
        Status status = DataObjectFactory.createStatus(json);

        // when
        String lang = status.getIsoLanguageCode();

        // then
        assertNull(lang);
    }

}
