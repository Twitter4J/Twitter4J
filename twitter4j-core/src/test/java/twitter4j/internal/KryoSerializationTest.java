package twitter4j.internal;

import com.esotericsoftware.kryo.Kryo;
import junit.framework.TestCase;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.json.DataObjectFactory;

import java.nio.ByteBuffer;

public class KryoSerializationTest extends TestCase {
    private final static String TEST_STATUS_JSON = "{\"text\":\"\\\\u5e30%u5e30 &lt;%}& foobar &lt;&Cynthia&gt;\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":null,\"truncated\":false,\"entities\":{\"urls\":[],\"hashtags\":[],\"user_mentions\":[]},\"in_reply_to_status_id_str\":null,\"id\":12029015787307008,\"in_reply_to_user_id_str\":null,\"source\":\"web\",\"favorited\":false,\"in_reply_to_status_id\":null,\"in_reply_to_user_id\":null,\"created_at\":\"Tue Dec 07 06:21:55 +0000 2010\",\"retweet_count\":0,\"id_str\":\"12029015787307008\",\"place\":null,\"user\":{\"location\":\"location:\",\"statuses_count\":13405,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0000ff\",\"id\":6358482,\"following\":true,\"favourites_count\":2,\"protected\":false,\"profile_text_color\":\"000000\",\"contributors_enabled\":false,\"description\":\"Hi there, I do test a lot!new\",\"verified\":false,\"profile_sidebar_border_color\":\"87bc44\",\"name\":\"twit4j\",\"profile_background_color\":\"9ae4e8\",\"created_at\":\"Sun May 27 09:52:09 +0000 2007\",\"followers_count\":24,\"geo_enabled\":true,\"profile_background_image_url\":\"http://a3.twimg.com/profile_background_images/179009017/t4j-reverse.gif\",\"follow_request_sent\":false,\"url\":\"http://yusuke.homeip.net/twitter4j/\",\"utc_offset\":-32400,\"time_zone\":\"Alaska\",\"notifications\":false,\"friends_count\":4,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"e0ff92\",\"screen_name\":\"twit4j\",\"id_str\":\"6358482\",\"profile_image_url\":\"http://a3.twimg.com/profile_images/1184543043/t4j-reverse_normal.jpeg\",\"show_all_inline_media\":false,\"listed_count\":3},\"coordinates\":null}";

    private Kryo kryo;

    protected void setUp() throws Exception {
        super.setUp();

        kryo = new Kryo();
        kryo.register(java.lang.String[].class);
        kryo.register(long[].class);
        kryo.register(java.util.Date.class);
        kryo.register(twitter4j.HashtagEntity[].class);
        kryo.register(twitter4j.URLEntity[].class);
        kryo.register(twitter4j.MediaEntity[].class);
        kryo.register(twitter4j.UserMentionEntity[].class);
        kryo.register(Class.forName("twitter4j.internal.json.UserJSONImpl"));
        kryo.register(Class.forName("twitter4j.internal.json.StatusJSONImpl"));
    }

    public void testKryoSerialization() throws TwitterException, ClassNotFoundException {
        Status status;
        status = DataObjectFactory.createStatus(TEST_STATUS_JSON);

        ByteBuffer buffer = ByteBuffer.allocate(512);
        kryo.writeObject(buffer, status);
        System.out.println(buffer.position() + " vs. " + TEST_STATUS_JSON.length());
        buffer.rewind();

        Status deserializedStatus = (Status) kryo.readObject(buffer, Class.forName("twitter4j.internal.json.StatusJSONImpl"));
        assertNotNull(deserializedStatus);
        assertEquals(status, deserializedStatus);
    }
}
