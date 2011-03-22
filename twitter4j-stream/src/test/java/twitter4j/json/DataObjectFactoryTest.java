package twitter4j.json;

import junit.framework.TestCase;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.internal.org.json.JSONObject;

/**
 * Unit test for DataObjectFactory
 *
 * @author Dan Checkoway - dcheckoway at gmail.com
 * @since Twitter4J 2.1.9
 */
public class DataObjectFactoryTest extends TestCase {
    static final String statusJsonText = "{\"in_reply_to_status_id_str\":null,\"place\":null,\"in_reply_to_user_id\":null,\"text\":\"working\",\"contributors\":null,\"retweet_count\":0,\"in_reply_to_user_id_str\":null,\"retweeted\":false,\"id_str\":\"794626207\",\"source\":\"\\u003Ca href=\\\"http:\\/\\/twitterhelp.blogspot.com\\/2008\\/05\\/twitter-via-mobile-web-mtwittercom.html\\\" rel=\\\"nofollow\\\"\\u003Emobile web\\u003C\\/a\\u003E\",\"truncated\":false,\"geo\":null,\"in_reply_to_status_id\":null,\"favorited\":false,\"user\":{\"show_all_inline_media\":false,\"geo_enabled\":false,\"profile_background_tile\":false,\"time_zone\":null,\"favourites_count\":0,\"description\":null,\"friends_count\":0,\"profile_link_color\":\"0084B4\",\"location\":null,\"profile_sidebar_border_color\":\"C0DEED\",\"id_str\":\"14481043\",\"url\":null,\"follow_request_sent\":false,\"statuses_count\":1,\"profile_use_background_image\":true,\"lang\":\"en\",\"profile_background_color\":\"C0DEED\",\"profile_image_url\":\"http:\\/\\/a3.twimg.com\\/a\\/1292975674\\/images\\/default_profile_3_normal.png\",\"profile_background_image_url\":\"http:\\/\\/a3.twimg.com\\/a\\/1292975674\\/images\\/themes\\/theme1\\/bg.png\",\"followers_count\":44,\"protected\":false,\"contributors_enabled\":false,\"notifications\":false,\"screen_name\":\"Yusuke\",\"name\":\"Yusuke\",\"is_translator\":false,\"listed_count\":1,\"following\":false,\"verified\":false,\"profile_text_color\":\"333333\",\"id\":14481043,\"utc_offset\":null,\"created_at\":\"Tue Apr 22 21:49:13 +0000 2008\",\"profile_sidebar_fill_color\":\"DDEEF6\"},\"id\":794626207,\"coordinates\":null,\"in_reply_to_screen_name\":null,\"created_at\":\"Tue Apr 22 21:49:34 +0000 2008\"}";

    static final String deleteJsonText = "{\"delete\":{\"status\":{\"id\":1234,\"id_str\":\"1234\",\"user_id\":3,\"user_id_str\":\"3\"}}}";

    static final String randomJsonText = "{\"random\":\"meaningless\"}";

    public void testCreateObject() throws Exception {
        Object obj;

        obj = DataObjectFactory.createObject(statusJsonText);
        if (!(obj instanceof Status)) {
            throw new Exception("DataObjectFactory.createObject failed for Status");
        }

        obj = DataObjectFactory.createObject(deleteJsonText);
        if (!(obj instanceof StatusDeletionNotice)) {
            throw new Exception("DataObjectFactory.createObject failed for StatusDeletionNotice");
        }

        obj = DataObjectFactory.createObject(randomJsonText);
        if (!(obj instanceof JSONObject)) {
            throw new Exception("DataObjectFactory.createObject failed for random");
        }
    }
}
