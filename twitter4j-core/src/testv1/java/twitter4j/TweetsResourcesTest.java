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
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import twitter4j.v1.*;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
@Execution(ExecutionMode.CONCURRENT)
class TweetsResourcesTest extends TwitterTestBase {

    @Test
    void testShowStatus() throws Exception {
        Status status;
        status = TwitterObjectFactory.createStatus("{\"text\":\"\\\\u5e30%u5e30 &lt;%\\u007d& foobar &lt;&Cynthia&gt;\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":null,\"truncated\":false,\"entities\":{\"urls\":[],\"hashtags\":[],\"user_mentions\":[]},\"in_reply_to_status_id_str\":null,\"id\":12029015787307008,\"in_reply_to_user_id_str\":null,\"source\":\"web\",\"favorited\":false,\"in_reply_to_status_id\":null,\"in_reply_to_user_id\":null,\"created_at\":\"Tue Dec 07 06:21:55 +0000 2010\",\"retweet_count\":0,\"id_str\":\"12029015787307008\",\"place\":null,\"user\":{\"location\":\"location:\",\"statuses_count\":13405,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0000ff\",\"id\":6358482,\"following\":true,\"favourites_count\":2,\"protected\":false,\"profile_text_color\":\"000000\",\"contributors_enabled\":false,\"description\":\"Hi there, I do test a lot!new\",\"verified\":false,\"profile_sidebar_border_color\":\"87bc44\",\"name\":\"twit4j\",\"profile_background_color\":\"9ae4e8\",\"created_at\":\"Sun May 27 09:52:09 +0000 2007\",\"followers_count\":24,\"geo_enabled\":true,\"profile_background_image_url\":\"http://a3.twimg.com/profile_background_images/179009017/t4j-reverse.gif\",\"follow_request_sent\":false,\"url\":\"http://yusuke.homeip.net/twitter4j/\",\"utc_offset\":-32400,\"time_zone\":\"Alaska\",\"notifications\":false,\"friends_count\":4,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"e0ff92\",\"screen_name\":\"twit4j\",\"id_str\":\"6358482\",\"profile_image_url\":\"http://a3.twimg.com/profile_images/1184543043/t4j-reverse_normal.jpeg\",\"show_all_inline_media\":false,\"listed_count\":3},\"coordinates\":null}");
        assertEquals("\\u5e30%u5e30 <%}& foobar <&Cynthia>", status.getText());

        status = twitter2.v1().tweets().showStatus(1000L);
        assertNotNull(TwitterObjectFactory.getRawJSON(status));
        assertEquals(status, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status)));
        assertEquals("en", status.getLang());
        assertEquals(52, status.getUser().getId());
        Status status2 = twitter1.v1().tweets().showStatus(1000L);
        assertEquals(52, status2.getUser().getId());
        assertNotNull(status.getRateLimitStatus());
        assertNotNull(TwitterObjectFactory.getRawJSON(status2));
        assertEquals(status2, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status2)));

        status2 = twitter1.v1().tweets().showStatus(999383469L);
        assertEquals("und", status2.getLang());
        assertNotNull(TwitterObjectFactory.getRawJSON(status2));
        assertEquals(status2, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status2)));
        assertEquals("01010100 01110010 01101001 01110101 01101101 01110000 01101000       <3", status2.getText());
        status2 = twitter1.v1().tweets().showStatus(12029015787307008L);
        assertNotNull(TwitterObjectFactory.getRawJSON(status2));
        assertEquals(status2, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status2)));
        assertEquals("\\u5e30%u5e30 <%}& foobar <&Cynthia>", status2.getText());
    }

    @Test
    void testStatusMethods() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.toString();
        String date = dateStr + "test mytweet @" + id2.screenName + " #twitter4jtest";
        Status status = twitter1.v1().tweets().updateStatus(date);
        assertTrue(Math.abs(status.getCreatedAt().toEpochSecond(ZoneOffset.UTC) - now.toEpochSecond(ZoneOffset.UTC)) < 10);
        assertNotNull(TwitterObjectFactory.getRawJSON(status));
        assertEquals(status, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status)));

        assertTrue(status.getText().contains("#twitter4jtest"));

        // http://jira.twitter4j.org/browse/TFJ-715
        // current_user_retweet contains only id
        Status retweeted = twitter2.v1().tweets().retweetStatus(status.getId());
        assertTrue(retweeted.getText().endsWith(status.getText()));

        Status status2 = twitter2.v1().tweets().updateStatus(StatusUpdate.of("@" + id1.screenName + " " + date).inReplyToStatusId(status.getId()));
        assertNotNull(TwitterObjectFactory.getRawJSON(status2));
        assertEquals(status2, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status2)));
        assertTrue(status2.getText().contains("#twitter4jtest"));
        assertEquals(status.getId(), status2.getInReplyToStatusId());
        assertEquals(id1.id, status2.getInReplyToUserId());
        status = twitter1.v1().tweets().destroyStatus(status.getId());
        assertNotNull(TwitterObjectFactory.getRawJSON(status));
        assertEquals(status, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status)));

        date = LocalDateTime.now().toString();
        String tweet = date + "test @" + id2.screenName + " #twitter4jtest";
        status = twitter1.v1().tweets().updateStatus(StatusUpdate.of(tweet).possiblySensitive(false).media(getRandomlyChosenFile()));
        assertNotNull(TwitterObjectFactory.getRawJSON(status));
        assertEquals(status, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status)));

        assertTrue(status.getText().startsWith(date));
        assertEquals(1, status.getMediaEntities().length);

        // testing un-retweet
        String dateStr3 = LocalDateTime.now().toString();
        String date3 = dateStr3 + "test http://t.co/VEDROet @" + id2.screenName + " #twitter4jtest";
        Status status3 = twitter1.v1().tweets().updateStatus(date3);
        // since updateStatus is already tested, just checking if the returned status is non-null
        assertNotNull(TwitterObjectFactory.getRawJSON(status3));

        Status retweetedStatus = twitter2.v1().tweets().retweetStatus(status3.getId());
        assertTrue(retweetedStatus.getText().endsWith(status3.getText()));

        // testing un retweet
        Status unRetweetedStatus = twitter2.v1().tweets().unRetweetStatus(status3.getId());
        assertTrue(unRetweetedStatus.getText().endsWith(status3.getText()));

        // deleting the status
        status3 = twitter1.v1().tweets().destroyStatus(status3.getId());
        assertNotNull(TwitterObjectFactory.getRawJSON(status3));
    }

    @Test
    void testUploadMediaByFile() throws Exception {
        twitter1.v1().tweets().uploadMedia(getRandomlyChosenFile());
    }

    @Test
    void testUploadMediaByStream() throws Exception {

        File randomFile = getRandomlyChosenFile();
        FileInputStream fis = new FileInputStream(randomFile);
        twitter1.v1().tweets().uploadMedia("fromInputStream", fis);
    }

    @Test
    void testRetweetMethods() throws Exception {
        List<Status> statuses;

        statuses = twitter1.v1().tweets().getRetweets(1021608606934822912L);
        assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
        assertIsRetweet(statuses);
        assertTrue(2 < statuses.size());

        IDs retweeters = twitter1.v1().tweets().getRetweeterIds(1021608606934822912L, -1);
        assertTrue(0 < retweeters.getIDs().length);
    }

    private void assertIsRetweet(List<Status> statuses) {
        for (Status status : statuses) {
            assertTrue(status.getText().startsWith("RT "));
        }
    }

    @Test
    void testOEmbed() throws TwitterException {
        OEmbed oembed = twitter1.v1().tweets().getOEmbed(OEmbedRequest.of(240192632003911681L, "http://samuraism.com/"));
        assertNotNull(TwitterObjectFactory.getRawJSON(oembed));
        assertEquals(oembed, TwitterObjectFactory.createOEmbed(TwitterObjectFactory.getRawJSON(oembed)));

        assertNotNull(oembed.getHtml());
        assertEquals("Jason Costa", oembed.getAuthorName());
        assertEquals("https://twitter.com/jasoncosta/status/240192632003911681", oembed.getURL());
        assertEquals("1.0", oembed.getVersion());
        assertEquals(3153600000L, oembed.getCacheAge());
        assertEquals("https://twitter.com/jasoncosta", oembed.getAuthorURL());
        assertTrue(0 < oembed.getWidth());

        twitter1.v1().tweets().getOEmbed(OEmbedRequest.of(273685580615913473L, "http://samuraism.com/"));

    }

    @Test
    void testOEmbedWithWidgetType() throws TwitterException {
        OEmbedRequest req =  OEmbedRequest.of(1011622565469548550L, "").widgetType(OEmbedRequest.WidgetType.VIDEO);
        // use this if want to see impact:
        // req.setHideTweet(true);

        OEmbed oembed = twitter1.v1().tweets().getOEmbed(req);

        assertNotNull(TwitterObjectFactory.getRawJSON(oembed));
        assertEquals(oembed, TwitterObjectFactory.createOEmbed(TwitterObjectFactory.getRawJSON(oembed)));

        assertNotNull(oembed.getHtml());
        assertTrue(oembed.getHtml().contains("<blockquote class=\"twitter-video\""));
        // use this assert if include req.setHideTweet(true)
        // assertTrue(oembed.getHtml().contains("<blockquote class=\"twitter-video\" data-status=\"hidden\""));

    }

    @Test
    void testLookup() throws TwitterException {
        // from "Example Result" of https://dev.twitter.com/docs/api/1.1/get/statuses/lookup
        ResponseList<Status> statuses = twitter1.v1().tweets().lookup(20L, 432656548536401920L);

        assertEquals(2, statuses.size());
        Status first = statuses.get(0);
        assertEquals("just setting up my twttr", first.getText());

        Status second = statuses.get(1);
        assertEquals("POST statuses/update. Great way to start. https://t.co/9S8YO69xzf (disclaimer, this was not posted via the API).", second.getText());
    }

    @Test
    void testEntities() throws Exception {
        Status status = twitter2.v1().tweets().showStatus(332341548203261953L);
        SymbolEntity[] symbolEntities = status.getSymbolEntities();
        assertEquals(4, symbolEntities.length);
        assertEquals("$APPL", status.getText().substring(symbolEntities[0].getStart(), symbolEntities[0].getEnd()));
        assertEquals("$C", status.getText().substring(symbolEntities[1].getStart(), symbolEntities[1].getEnd()));
        assertEquals("$LNKD", status.getText().substring(symbolEntities[2].getStart(), symbolEntities[2].getEnd()));
        assertEquals("$FB", status.getText().substring(symbolEntities[3].getStart(), symbolEntities[3].getEnd()));

        status = twitter2.v1().tweets().showStatus(268294645526708226L);
        assertNotNull(TwitterObjectFactory.getRawJSON(status));
        assertEquals(status, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status)));
        symbolEntities = status.getSymbolEntities();
        assertEquals(0, symbolEntities.length);

        URLEntity[] urlEntities = status.getURLEntities();
        assertEquals(1, urlEntities.length);
        assertEquals("http://t.co/HwbSpYFr", urlEntities[0].getURL());
        assertEquals("http://twitter4j.org/en/index.html#download", urlEntities[0].getExpandedURL());
        assertEquals("twitter4j.org/en/index.html#…", urlEntities[0].getDisplayURL());
        assertTrue(0 < urlEntities[0].getStart());
        assertTrue(urlEntities[0].getStart() < urlEntities[0].getEnd());
        assertEquals(urlEntities[0].getURL(), status.getText().substring(urlEntities[0].getStart(), urlEntities[0].getEnd()));

        UserMentionEntity[] userMentions = status.getUserMentionEntities();
        assertEquals(2, userMentions.length);
        assertEquals(72297675, userMentions[1].getId());
        assertEquals("t4j_news", userMentions[1].getScreenName());
        assertEquals("@" + userMentions[1].getScreenName(), status.getText().substring(userMentions[1].getStart(), userMentions[1].getEnd()));

        HashtagEntity[] hashtags = status.getHashtagEntities();
        assertEquals(1, hashtags.length);
        assertEquals("test", hashtags[0].getText());
        assertEquals("#" + hashtags[0].getText(), status.getText().substring(hashtags[0].getStart(), hashtags[0].getEnd()));

        status = twitter1.v1().tweets().showStatus(76360760606986241L);
        assertNotNull(TwitterObjectFactory.getRawJSON(status));
        assertEquals(status, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status)));

        MediaEntity[] medias = status.getMediaEntities();
        assertEquals(1, medias.length);
        MediaEntity media = medias[0];
        assertEquals("pic.twitter.com/qbJx26r", media.getDisplayURL());
        assertEquals("https://twitter.com/twitter/status/76360760606986241/photo/1", media.getExpandedURL());
        assertEquals(76360760611180544L, media.getId());
        assertEquals("http://pbs.twimg.com/media/AQ9JtQsCEAA7dEN.jpg", media.getMediaURL());
        assertEquals("https://pbs.twimg.com/media/AQ9JtQsCEAA7dEN.jpg", media.getMediaURLHttps());
        assertEquals("http://t.co/qbJx26r", media.getURL());
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
        assertEquals(700, medium.getWidth());
        assertEquals(466, medium.getHeight());

        MediaEntity.Size small = sizes.get(MediaEntity.Size.SMALL);
        assertEquals(MediaEntity.Size.FIT, small.getResize());
        assertEquals(680, small.getWidth());
        assertEquals(453, small.getHeight());

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
