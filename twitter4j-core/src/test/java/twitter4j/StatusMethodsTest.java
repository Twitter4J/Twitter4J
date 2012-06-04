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

import twitter4j.json.DataObjectFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class StatusMethodsTest extends TwitterTestBase {
    public StatusMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testShowStatus() throws Exception {
//        JSONObject json = new JSONObject();
//        json.append("text", " <%}&lt; foobar <&Cynthia>");
//        System.out.println(json.toString());
//        System.out.println(json.getString("text"));
        Status status;
        status = DataObjectFactory.createStatus("{\"text\":\"\\\\u5e30%u5e30 &lt;%}& foobar &lt;&Cynthia&gt;\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":null,\"truncated\":false,\"entities\":{\"urls\":[],\"hashtags\":[],\"user_mentions\":[]},\"in_reply_to_status_id_str\":null,\"id\":12029015787307008,\"in_reply_to_user_id_str\":null,\"source\":\"web\",\"favorited\":false,\"in_reply_to_status_id\":null,\"in_reply_to_user_id\":null,\"created_at\":\"Tue Dec 07 06:21:55 +0000 2010\",\"retweet_count\":0,\"id_str\":\"12029015787307008\",\"place\":null,\"user\":{\"location\":\"location:\",\"statuses_count\":13405,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0000ff\",\"id\":6358482,\"following\":true,\"favourites_count\":2,\"protected\":false,\"profile_text_color\":\"000000\",\"contributors_enabled\":false,\"description\":\"Hi there, I do test a lot!new\",\"verified\":false,\"profile_sidebar_border_color\":\"87bc44\",\"name\":\"twit4j\",\"profile_background_color\":\"9ae4e8\",\"created_at\":\"Sun May 27 09:52:09 +0000 2007\",\"followers_count\":24,\"geo_enabled\":true,\"profile_background_image_url\":\"http://a3.twimg.com/profile_background_images/179009017/t4j-reverse.gif\",\"follow_request_sent\":false,\"url\":\"http://yusuke.homeip.net/twitter4j/\",\"utc_offset\":-32400,\"time_zone\":\"Alaska\",\"notifications\":false,\"friends_count\":4,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"e0ff92\",\"screen_name\":\"twit4j\",\"id_str\":\"6358482\",\"profile_image_url\":\"http://a3.twimg.com/profile_images/1184543043/t4j-reverse_normal.jpeg\",\"show_all_inline_media\":false,\"listed_count\":3},\"coordinates\":null}");
        assertEquals("\\u5e30%u5e30 <%}& foobar <&Cynthia>", status.getText());

        status = twitter2.showStatus(1000l);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        assertEquals(52, status.getUser().getId());
        Status status2 = twitter1.showStatus(1000l);
        assertEquals(52, status2.getUser().getId());
        assertNotNull(status.getRateLimitStatus());
        assertNotNull(DataObjectFactory.getRawJSON(status2));
        assertEquals(status2, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status2)));

        status2 = twitter1.showStatus(999383469l);
        assertNotNull(DataObjectFactory.getRawJSON(status2));
        assertEquals(status2, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status2)));
        assertEquals("01010100 01110010 01101001 01110101 01101101 01110000 01101000       <3", status2.getText());
        status2 = twitter1.showStatus(12029015787307008l);
        assertNotNull(DataObjectFactory.getRawJSON(status2));
        assertEquals(status2, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status2)));
        System.out.println(DataObjectFactory.getRawJSON(status2));
        assertEquals("\\u5e30%u5e30 <%}& foobar <&Cynthia>", status2.getText());
    }

    public void testStatusMethods() throws Exception {
        String dateStr = new java.util.Date().toString();
        String date = dateStr + "test http://t.co/VEDROet @twit4j2 #twitter4jtest";
        Status status = twitter1.updateStatus(date);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));
        System.out.println(status.getText());

        assertTrue(status.getText().matches(dateStr + "test http://t.co/.* @twit4j2 #twitter4jtest"));
        Status status2 = twitter2.updateStatus(new StatusUpdate("@" + id1.screenName + " " + date).inReplyToStatusId(status.getId()));
        assertNotNull(DataObjectFactory.getRawJSON(status2));
        assertEquals(status2, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status2)));
        assertTrue(status2.getText().matches("@" + id1.screenName + " " + dateStr + "test http://t.co/.* @twit4j2 #twitter4jtest"));
        assertEquals(status.getId(), status2.getInReplyToStatusId());
        assertEquals(id1.id, status2.getInReplyToUserId());
        status = twitter1.destroyStatus(status.getId());
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));

        date = new java.util.Date().toString();
        String tweet = date + "test http://t.co/VEDROet @twit4j2 #twitter4jtest";
        status = twitter1.updateStatus(new StatusUpdate(tweet).possiblySensitive(false).media(getRandomlyChosenFile()));
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));

        assertTrue(status.getText().startsWith(date));
        assertNotNull(status.getMediaEntities());
        assertEquals(1, status.getMediaEntities().length);
    }

    public void testRetweetMethods() throws Exception {
        List<Status> statuses = twitter1.getRetweetedByMe();
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetedByMe(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetedByUser(id1.id, new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetedByUser(id1.screenName, new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetedToMe();
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetedToMe(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetedToUser(id1.id, new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetedToUser(id1.screenName, new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetsOfMe();
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
//        assertIsRetweet(statuses);
        statuses = twitter1.getRetweetsOfMe(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
//        assertIsRetweet(statuses);
        statuses = twitter1.getRetweets(18594701629l);
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        assertTrue(20 < statuses.size());

        List<User> users = unauthenticated.getRetweetedBy(47621163517624320L, new Paging(1, 100));
        assertTrue(users.size() > 50);
        users = unauthenticated.getRetweetedBy(47621163517624320L, new Paging(2, 100));
        assertTrue(users.size() > 10);

        IDs ids = twitter1.getRetweetedByIDs(47621163517624320L, new Paging(1, 100));
        assertTrue(ids.getIDs().length > 50);
        ids = twitter1.getRetweetedByIDs(47621163517624320L, new Paging(2, 100));
        assertTrue(ids.getIDs().length > 10);
    }

    private void assertIsRetweet(List<Status> statuses) {
        for (Status status : statuses) {
            assertTrue(status.getText().startsWith("RT "));
        }
    }


    public void testEntities() throws Exception {
        Status status = twitter2.showStatus(25733871525957632l);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));

        URLEntity[] entities = status.getURLEntities();
        assertEquals(2, entities.length);
        assertEquals("http://t.co/ppLTMVO", entities[0].getURL().toString());
        assertEquals("http://twitter4j.org/en/index.html#download", entities[0].getExpandedURL().toString());
        assertEquals("twitter4j.org/en/index.html#…", entities[0].getDisplayURL());
        assertTrue(0 < entities[0].getStart());
        assertTrue(entities[0].getStart() < entities[0].getEnd());

        UserMentionEntity[] userMentions = status.getUserMentionEntities();
        assertEquals(1, userMentions.length);
        assertEquals(15928023, userMentions[0].getId());
        assertEquals("SonatypeNexus", userMentions[0].getScreenName());
        assertEquals("Sonatype Nexus", userMentions[0].getName());
        assertEquals(111, userMentions[0].getStart());
        assertEquals(125, userMentions[0].getEnd());

        HashtagEntity[] hashtags = status.getHashtagEntities();
        assertEquals(1, hashtags.length);
        assertEquals("twitter4j", hashtags[0].getText());
        assertEquals(126, hashtags[0].getStart());
        assertEquals(136, hashtags[0].getEnd());

        status = twitter1.showStatus(76360760606986241L);
        assertNotNull(DataObjectFactory.getRawJSON(status));
        assertEquals(status, DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(status)));

        MediaEntity[] medias = status.getMediaEntities();
        assertEquals(1, medias.length);
        MediaEntity media = medias[0];
        assertEquals("pic.twitter.com/qbJx26r", media.getDisplayURL());
        assertEquals("http://twitter.com/twitter/status/76360760606986241/photo/1", media.getExpandedURL().toString());
        assertEquals(76360760611180544L, media.getId());
        assertEquals("http://p.twimg.com/AQ9JtQsCEAA7dEN.jpg", media.getMediaURL().toString());
        assertEquals("https://p.twimg.com/AQ9JtQsCEAA7dEN.jpg", media.getMediaURLHttps().toString());
        assertEquals("http://t.co/qbJx26r", media.getURL().toString());
        assertEquals(34, media.getStart());
        assertEquals(53, media.getEnd());
        assertEquals("photo", media.getType());
        Map<Integer, MediaEntity.Size> sizes = media.getSizes();
        assertEquals(4, sizes.size());
        MediaEntity.Size large = sizes.get(MediaEntity.Size.LARGE);
        assertEquals(MediaEntity.Size.FIT, sizes.get(MediaEntity.Size.LARGE).getResize());
        assertEquals(700, large.getWidth());
        assertEquals(466, large.getHeight());

        MediaEntity.Size medium = sizes.get(MediaEntity.Size.MEDIUM);
        assertEquals(MediaEntity.Size.FIT, medium.getResize());
        assertEquals(600, medium.getWidth());
        assertEquals(399, medium.getHeight());

        MediaEntity.Size small = sizes.get(MediaEntity.Size.SMALL);
        assertEquals(MediaEntity.Size.FIT, small.getResize());
        assertEquals(340, small.getWidth());
        assertEquals(226, small.getHeight());

        MediaEntity.Size thumb = sizes.get(MediaEntity.Size.THUMB);

        assertEquals(MediaEntity.Size.CROP, thumb.getResize());
        assertEquals(150, thumb.getWidth());
        assertEquals(150, thumb.getHeight());
    }

    static final String[] files = {"src/test/resources/t4j-reverse.jpeg",
        "src/test/resources/t4j-reverse.png",
        "src/test/resources/t4j-reverse.gif",
        "src/test/resources/t4j.jpeg",
        "src/test/resources/t4j.png",
        "src/test/resources/t4j.gif",
    };

    private static File getRandomlyChosenFile() {
        int rand = (int) (System.currentTimeMillis() % 6);
        File file = new File(files[rand]);
        if (!file.exists()) {
            file = new File("twitter4j-core/" + files[rand]);
        }
        return file;
    }

}
