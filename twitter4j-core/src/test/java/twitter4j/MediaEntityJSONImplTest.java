package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Hiroaki TAKEUCHI
 */
class MediaEntityJSONImplTest {

    @Test
    void testVideoInfo() throws Exception {

        //given from https://twittercommunity.com/t/twitter-video-support-in-rest-and-streaming-api/31258
        String rawJson = "{\"extended_entities\":{\"media\":[{\"display_url\":\"pic.twitter.com\\/31JoMS50ha\",\"expanded_url\":\"http:\\/\\/twitter.com\\/twitter\\/status\\/560070183650213889\\/video\\/1\",\"features\":{},\"id\":560070131976392705,\"id_str\":\"560070131976392705\",\"indices\":[110,132],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/ext_tw_video_thumb\\/560070131976392705\\/pu\\/img\\/TcG_ep5t-iqdLV5R.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/ext_tw_video_thumb\\/560070131976392705\\/pu\\/img\\/TcG_ep5t-iqdLV5R.jpg\",\"sizes\":{\"large\":{\"h\":576,\"resize\":\"fit\",\"w\":1024},\"medium\":{\"h\":337,\"resize\":\"fit\",\"w\":600},\"small\":{\"h\":191,\"resize\":\"fit\",\"w\":340},\"thumb\":{\"h\":150,\"resize\":\"crop\",\"w\":150}},\"type\":\"video\",\"url\":\"http:\\/\\/t.co\\/31JoMS50ha\",\"video_info\":{\"aspect_ratio\":[16,9],\"duration_millis\":30033,\"variants\":[{\"bitrate\":2176000,\"content_type\":\"video\\/mp4\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/vid\\/1280x720\\/c4E56sl91ZB7cpYi.mp4\"},{\"bitrate\":320000,\"content_type\":\"video\\/mp4\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/vid\\/320x180\\/nXXsvs7vOhcMivwl.mp4\"},{\"bitrate\":832000,\"content_type\":\"video\\/webm\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/vid\\/640x360\\/vmLr5JlVs2kBLrXS.webm\"},{\"bitrate\":832000,\"content_type\":\"video\\/mp4\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/vid\\/640x360\\/vmLr5JlVs2kBLrXS.mp4\"},{\"content_type\":\"application\\/x-mpegURL\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/pl\\/r1kgzh5PmLgium3-.m3u8\"}]}}]}}";

        //when
        JSONObject json = new JSONObject(rawJson);
        MediaEntityJSONImpl mediaEntity = new MediaEntityJSONImpl(json.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));

        //then
        assertEquals(560070131976392705L, mediaEntity.getId());
        assertEquals("http://pbs.twimg.com/ext_tw_video_thumb/560070131976392705/pu/img/TcG_ep5t-iqdLV5R.jpg",
                mediaEntity.getMediaURL());
        assertEquals("video", mediaEntity.getType());

        assertEquals(16, mediaEntity.getVideoAspectRatioWidth());
        assertEquals(9, mediaEntity.getVideoAspectRatioHeight());

        assertEquals(30033, mediaEntity.getVideoDurationMillis());

        MediaEntity.Variant[] variants = mediaEntity.getVideoVariants();
        assertEquals(5, variants.length);

        assertEquals(2176000, variants[0].getBitrate());
        assertEquals("video/mp4", variants[0].getContentType());
        assertEquals("https://video.twimg.com/ext_tw_video/560070131976392705/pu/vid/1280x720/c4E56sl91ZB7cpYi.mp4",
                variants[0].getUrl());

        assertEquals(0, variants[4].getBitrate());
        assertEquals("application/x-mpegURL", variants[4].getContentType());
        assertEquals("https://video.twimg.com/ext_tw_video/560070131976392705/pu/pl/r1kgzh5PmLgium3-.m3u8",
                variants[4].getUrl());

        assertNull(mediaEntity.getExtAltText());
    }


    @Test
    void testAnimatedGifs() throws Exception {

        //given from https://twittercommunity.com/t/adding-animated-gifs-via-rest-and-streaming-apis/30070
        String rawJson = "{\"entities\":{\"hashtags\":[],\"trends\":[],\"urls\":[],\"user_mentions\":[],\"symbols\":[],\"media\":[{\"id\":100,\"id_str\":\"100\",\"indices\":[1,10],\"media_url\":\"http://media.url.here\",\"media_url_https\":\"https://media.url.here\",\"url\":\"gif1Url\",\"display_url\":\"testDisplayUrl\",\"expanded_url\":\"http://twitter.com/username/status/tweetid/photo/1\",\"type\":\"photo\",\"sizes\":{}}]},\"extended_entities\":{\"media\":[{\"id\":100,\"id_str\":\"100\",\"indices\":[11,10],\"media_url\":\"http://media.url.here\",\"media_url_https\":\"media.url.here\",\"url\":\"gif1Url\",\"display_url\":\"testDisplayUrl\",\"expanded_url\":\"http://twitter.com/username/status/tweetid/photo/1\",\"type\":\"animated_gif\",\"sizes\":{},\"video_info\":{\"aspect_ratio\":[114,131],\"variants\":[{\"bitrate\":123,\"content_type\":\"video/mp4\",\"url\":\"variantUrl1\"},{\"bitrate\":456,\"content_type\":\"video/mp4\",\"url\":\"variantUrl2\"}]}}]}}";

        //when
        JSONObject json = new JSONObject(rawJson);
        MediaEntityJSONImpl mediaEntity = new MediaEntityJSONImpl(json.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));

        //then
        assertEquals(100, mediaEntity.getId());
        assertEquals("http://media.url.here",
                mediaEntity.getMediaURL());
        assertEquals("animated_gif", mediaEntity.getType());

        assertEquals(114, mediaEntity.getVideoAspectRatioWidth());
        assertEquals(131, mediaEntity.getVideoAspectRatioHeight());

        // duration_millis is not appeared in animated_gif
        assertEquals(0, mediaEntity.getVideoDurationMillis());

        MediaEntity.Variant[] variants = mediaEntity.getVideoVariants();
        assertEquals(2, variants.length);

        assertEquals(123, variants[0].getBitrate());
        assertEquals("video/mp4", variants[0].getContentType());
        assertEquals("variantUrl1",
                variants[0].getUrl());

        assertNull(mediaEntity.getExtAltText());
    }


    @Test
    void testExtAltText() throws Exception {

        //given from https://api.twitter.com/1.1/statuses/show/715085258010406912.json?include_ext_alt_text=true
        String rawJson = "{\"extended_entities\":  {\"media\":  [{\"id\": 715085245385740300,\"id_str\": \"715085245385740288\",\"indices\":  [10,33],\"media_url\": \"http://pbs.twimg.com/media/Cex-PfNXEAA5X9y.jpg\",\"media_url_https\": \"https://pbs.twimg.com/media/Cex-PfNXEAA5X9y.jpg\",\"url\": \"https://t.co/30svb05LET\",\"display_url\": \"pic.twitter.com/30svb05LET\",\"expanded_url\": \"http://twitter.com/takke/status/715085258010406912/photo/1\",\"type\": \"photo\",\"sizes\":  {\"medium\":  {\"w\": 600,\"h\": 140,\"resize\": \"fit\"},\"thumb\":  {\"w\": 150,\"h\": 150,\"resize\": \"crop\"},\"small\":  {\"w\": 340,\"h\": 79,\"resize\": \"fit\"},\"large\":  {\"w\": 1024,\"h\": 239,\"resize\": \"fit\"}},\"ext_alt_text\": \"カレーパンマンのパズル的なやつ\"}]}}";

        //when
        JSONObject json = new JSONObject(rawJson);
        MediaEntityJSONImpl mediaEntity = new MediaEntityJSONImpl(json.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));

        //then
        assertEquals(715085245385740300L, mediaEntity.getId());
        assertEquals("http://pbs.twimg.com/media/Cex-PfNXEAA5X9y.jpg",
                mediaEntity.getMediaURL());

        assertEquals("カレーパンマンのパズル的なやつ", mediaEntity.getExtAltText());

        assertNull(mediaEntity.getAdditionalMediaTitle());
        assertNull(mediaEntity.getAdditionalMediaDescription());
        assertNull(mediaEntity.getAdditionalMediaEmbeddable());
        assertNull(mediaEntity.getAdditionalMediaMonetizable());
        assertNull(mediaEntity.getAdditionalMediaSourceUser());
    }


    @Test
    void testAdditionalMediaInfo_NoTitleDescription() throws Exception {

        //given from https://api.twitter.com/1.1/statuses/show/1240874970198097920.json?include_ext_alt_text=true
        String rawJson = "{\"extended_entities\":{\"media\":[{\"id\":1236671586075316200,\"id_str\":\"1236671586075316225\",\"indices\":[5,28],\"media_url\":\"http://pbs.twimg.com/ext_tw_video_thumb/1236671586075316225/pu/img/xRVEs8EhJqVfFW3C.jpg\",\"media_url_https\":\"https://pbs.twimg.com/ext_tw_video_thumb/1236671586075316225/pu/img/xRVEs8EhJqVfFW3C.jpg\",\"url\":\"https://t.co/3n6G7n7PJS\",\"display_url\":\"pic.twitter.com/3n6G7n7PJS\",\"expanded_url\":\"https://twitter.com/Velovebikes/status/1236671921565114369/video/1\",\"type\":\"video\",\"sizes\":{\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"},\"medium\":{\"w\":1200,\"h\":675,\"resize\":\"fit\"},\"small\":{\"w\":680,\"h\":383,\"resize\":\"fit\"},\"large\":{\"w\":1280,\"h\":720,\"resize\":\"fit\"}},\"source_status_id\":1236671921565114400,\"source_status_id_str\":\"1236671921565114369\",\"source_user_id\":3155196136,\"source_user_id_str\":\"3155196136\",\"video_info\":{\"aspect_ratio\":[16,9],\"duration_millis\":48882,\"variants\":[{\"content_type\":\"application/x-mpegURL\",\"url\":\"https://video.twimg.com/ext_tw_video/1236671586075316225/pu/pl/H66SGUD8VuLr06Ts.m3u8?tag=10\"},{\"bitrate\":832000,\"content_type\":\"video/mp4\",\"url\":\"https://video.twimg.com/ext_tw_video/1236671586075316225/pu/vid/640x360/Uj_6_08M8TVgtmLG.mp4?tag=10\"},{\"bitrate\":256000,\"content_type\":\"video/mp4\",\"url\":\"https://video.twimg.com/ext_tw_video/1236671586075316225/pu/vid/480x270/897atp68fJA_UyLo.mp4?tag=10\"},{\"bitrate\":2176000,\"content_type\":\"video/mp4\",\"url\":\"https://video.twimg.com/ext_tw_video/1236671586075316225/pu/vid/1280x720/WYTvCJm0C3QmLOhY.mp4?tag=10\"}]},\"ext_alt_text\":null,\"additional_media_info\":{\"monetizable\":false,\"source_user\":{\"id\":3155196136,\"id_str\":\"3155196136\",\"name\":\"Velove\",\"screen_name\":\"Velovebikes\",\"location\":\"Goteborg, Sverige\",\"description\":\"Velove solutions are improving productivity in last mile delivery. Try the Armadillo electric cargo bike and get a smile on your face!\",\"url\":\"http://t.co/D0OLfakwLf\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"http://t.co/D0OLfakwLf\",\"expanded_url\":\"http://www.velove.se\",\"display_url\":\"velove.se\",\"indices\":[0,22]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":2038,\"friends_count\":422,\"listed_count\":52,\"created_at\":\"Sat Apr 11 04:25:03 +0000 2015\",\"favourites_count\":578,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"verified\":false,\"statuses_count\":1301,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"000000\",\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http://pbs.twimg.com/profile_images/1097046595944808448/cb7FlNbw_normal.png\",\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/1097046595944808448/cb7FlNbw_normal.png\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/3155196136/1543163176\",\"profile_image_extensions_alt_text\":null,\"profile_banner_extensions_alt_text\":null,\"profile_link_color\":\"1F2A44\",\"profile_sidebar_border_color\":\"000000\",\"profile_sidebar_fill_color\":\"000000\",\"profile_text_color\":\"000000\",\"profile_use_background_image\":false,\"has_extended_profile\":false,\"default_profile\":false,\"default_profile_image\":false,\"following\":false,\"follow_request_sent\":false,\"notifications\":false,\"translator_type\":\"none\"}}}]}}";

        //when
        JSONObject json = new JSONObject(rawJson);
        MediaEntityJSONImpl mediaEntity = new MediaEntityJSONImpl(json.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));

        //then
        assertEquals(1236671586075316200L, mediaEntity.getId());
        assertNull(mediaEntity.getAdditionalMediaTitle());
        assertNull(mediaEntity.getAdditionalMediaDescription());
        assertEquals(false, mediaEntity.getAdditionalMediaEmbeddable());
        assertEquals(false, mediaEntity.getAdditionalMediaMonetizable());
        assertEquals(3155196136L, mediaEntity.getAdditionalMediaSourceUser().getId());
        assertEquals("Velovebikes", mediaEntity.getAdditionalMediaSourceUser().getScreenName());
    }

    @Test
    void testAdditionalMediaInfo_HasTitleDescription() throws Exception {

        //given from https://api.twitter.com/1.1/statuses/show/1242632305895587840.json?include_ext_alt_text=true
        String rawJson = "{\"extended_entities\":{\"media\":[{\"id\":924685332347469800,\"id_str\":\"924685332347469824\",\"indices\":[8,31],\"media_url\":\"http://pbs.twimg.com/media/DNUkdLMVwAEzj8K.jpg\",\"media_url_https\":\"https://pbs.twimg.com/media/DNUkdLMVwAEzj8K.jpg\",\"url\":\"https://t.co/6F89Oxb5UB\",\"display_url\":\"pic.twitter.com/6F89Oxb5UB\",\"expanded_url\":\"https://twitter.com/nyjets/status/924685391524798464/video/1\",\"type\":\"photo\",\"sizes\":{\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"},\"medium\":{\"w\":1200,\"h\":675,\"resize\":\"fit\"},\"small\":{\"w\":680,\"h\":383,\"resize\":\"fit\"},\"large\":{\"w\":1280,\"h\":720,\"resize\":\"fit\"}},\"source_status_id\":924685391524798500,\"source_status_id_str\":\"924685391524798464\",\"source_user_id\":17076218,\"source_user_id_str\":\"17076218\",\"ext_alt_text\":null,\"additional_media_info\":{\"title\":\"#ATLvsNYJ: Tomlinson TD from McCown\",\"description\":\"NFL\",\"embeddable\":false,\"monetizable\":true,\"source_user\":{\"id\":17076218,\"id_str\":\"17076218\",\"name\":\"New York Jets\",\"screen_name\":\"nyjets\",\"location\":\"New York\",\"description\":\"#TakeFlight\",\"url\":\"https://t.co/PdZCEFcc1B\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https://t.co/PdZCEFcc1B\",\"expanded_url\":\"http://nyjets.com\",\"display_url\":\"nyjets.com\",\"indices\":[0,23]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":1249925,\"friends_count\":11585,\"listed_count\":8168,\"created_at\":\"Thu Oct 30 22:40:49 +0000 2008\",\"favourites_count\":3289,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"verified\":true,\"statuses_count\":62178,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"131516\",\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme14/bg.gif\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme14/bg.gif\",\"profile_background_tile\":false,\"profile_image_url\":\"http://pbs.twimg.com/profile_images/1204029633286590464/T584Ys8V_normal.jpg\",\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/1204029633286590464/T584Ys8V_normal.jpg\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/17076218/1582228846\",\"profile_image_extensions_alt_text\":null,\"profile_banner_extensions_alt_text\":null,\"profile_link_color\":\"009999\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"EFEFEF\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":false,\"default_profile_image\":false,\"following\":false,\"follow_request_sent\":false,\"notifications\":false,\"translator_type\":\"none\"}}}]}}";

        //when
        JSONObject json = new JSONObject(rawJson);
        MediaEntityJSONImpl mediaEntity = new MediaEntityJSONImpl(json.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));

        //then
        assertEquals(924685332347469800L, mediaEntity.getId());
        assertEquals("#ATLvsNYJ: Tomlinson TD from McCown", mediaEntity.getAdditionalMediaTitle());
        assertEquals("NFL", mediaEntity.getAdditionalMediaDescription());
        assertEquals(false, mediaEntity.getAdditionalMediaEmbeddable());
        assertEquals(true, mediaEntity.getAdditionalMediaMonetizable());
        assertEquals(17076218L, mediaEntity.getAdditionalMediaSourceUser().getId());
        assertEquals("nyjets", mediaEntity.getAdditionalMediaSourceUser().getScreenName());
    }

}
