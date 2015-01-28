package twitter4j;

import junit.framework.TestCase;

/**
 * @author Hiroaki TAKEUCHI
 */
public class MediaEntityJSONImplTest extends TestCase {

    public void testVideoInfo() throws Exception {

        //given from https://twittercommunity.com/t/twitter-video-support-in-rest-and-streaming-api/31258
        String rawJson = "{\"extended_entities\":{\"media\":[{\"display_url\":\"pic.twitter.com\\/31JoMS50ha\",\"expanded_url\":\"http:\\/\\/twitter.com\\/twitter\\/status\\/560070183650213889\\/video\\/1\",\"features\":{},\"id\":560070131976392705,\"id_str\":\"560070131976392705\",\"indices\":[110,132],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/ext_tw_video_thumb\\/560070131976392705\\/pu\\/img\\/TcG_ep5t-iqdLV5R.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/ext_tw_video_thumb\\/560070131976392705\\/pu\\/img\\/TcG_ep5t-iqdLV5R.jpg\",\"sizes\":{\"large\":{\"h\":576,\"resize\":\"fit\",\"w\":1024},\"medium\":{\"h\":337,\"resize\":\"fit\",\"w\":600},\"small\":{\"h\":191,\"resize\":\"fit\",\"w\":340},\"thumb\":{\"h\":150,\"resize\":\"crop\",\"w\":150}},\"type\":\"video\",\"url\":\"http:\\/\\/t.co\\/31JoMS50ha\",\"video_info\":{\"aspect_ratio\":[16,9],\"duration_millis\":30033,\"variants\":[{\"bitrate\":2176000,\"content_type\":\"video\\/mp4\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/vid\\/1280x720\\/c4E56sl91ZB7cpYi.mp4\"},{\"bitrate\":320000,\"content_type\":\"video\\/mp4\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/vid\\/320x180\\/nXXsvs7vOhcMivwl.mp4\"},{\"bitrate\":832000,\"content_type\":\"video\\/webm\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/vid\\/640x360\\/vmLr5JlVs2kBLrXS.webm\"},{\"bitrate\":832000,\"content_type\":\"video\\/mp4\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/vid\\/640x360\\/vmLr5JlVs2kBLrXS.mp4\"},{\"content_type\":\"application\\/x-mpegURL\",\"url\":\"https:\\/\\/video.twimg.com\\/ext_tw_video\\/560070131976392705\\/pu\\/pl\\/r1kgzh5PmLgium3-.m3u8\"}]}}]}}";

        //when
        JSONObject json = new JSONObject(rawJson);
        ExtendedMediaEntityJSONImpl mediaEntity = new ExtendedMediaEntityJSONImpl(json.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));

        //then
        assertEquals(560070131976392705L, mediaEntity.getId());
        assertEquals("http://pbs.twimg.com/ext_tw_video_thumb/560070131976392705/pu/img/TcG_ep5t-iqdLV5R.jpg",
                mediaEntity.getMediaURL());
        assertEquals("video", mediaEntity.getType());

        assertEquals(16, mediaEntity.getVideoAspectRatioWidth());
        assertEquals(9, mediaEntity.getVideoAspectRatioHeight());

        assertEquals(30033, mediaEntity.getVideoDurationMillis());

        ExtendedMediaEntity.Variant[] variants = mediaEntity.getVideoVariants();
        assertEquals(5, variants.length);

        assertEquals(2176000, variants[0].getBitrate());
        assertEquals("video/mp4", variants[0].getContentType());
        assertEquals("https://video.twimg.com/ext_tw_video/560070131976392705/pu/vid/1280x720/c4E56sl91ZB7cpYi.mp4",
                variants[0].getUrl());

        assertEquals(0, variants[4].getBitrate());
        assertEquals("application/x-mpegURL", variants[4].getContentType());
        assertEquals("https://video.twimg.com/ext_tw_video/560070131976392705/pu/pl/r1kgzh5PmLgium3-.m3u8",
                variants[4].getUrl());

    }


    public void testAnimatedGifs() throws Exception {

        //given from https://twittercommunity.com/t/adding-animated-gifs-via-rest-and-streaming-apis/30070
        String rawJson = "{\"entities\":{\"hashtags\":[],\"trends\":[],\"urls\":[],\"user_mentions\":[],\"symbols\":[],\"media\":[{\"id\":100,\"id_str\":\"100\",\"indices\":[1,10],\"media_url\":\"http://media.url.here\",\"media_url_https\":\"https://media.url.here\",\"url\":\"gif1Url\",\"display_url\":\"testDisplayUrl\",\"expanded_url\":\"http://twitter.com/username/status/tweetid/photo/1\",\"type\":\"photo\",\"sizes\":{}}]},\"extended_entities\":{\"media\":[{\"id\":100,\"id_str\":\"100\",\"indices\":[11,10],\"media_url\":\"http://media.url.here\",\"media_url_https\":\"media.url.here\",\"url\":\"gif1Url\",\"display_url\":\"testDisplayUrl\",\"expanded_url\":\"http://twitter.com/username/status/tweetid/photo/1\",\"type\":\"animated_gif\",\"sizes\":{},\"video_info\":{\"aspect_ratio\":[114,131],\"variants\":[{\"bitrate\":123,\"content_type\":\"video/mp4\",\"url\":\"variantUrl1\"},{\"bitrate\":456,\"content_type\":\"video/mp4\",\"url\":\"variantUrl2\"}]}}]}}";

        //when
        JSONObject json = new JSONObject(rawJson);
        ExtendedMediaEntityJSONImpl mediaEntity = new ExtendedMediaEntityJSONImpl(json.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));

        //then
        assertEquals(100, mediaEntity.getId());
        assertEquals("http://media.url.here",
                mediaEntity.getMediaURL());
        assertEquals("animated_gif", mediaEntity.getType());

        assertEquals(114, mediaEntity.getVideoAspectRatioWidth());
        assertEquals(131, mediaEntity.getVideoAspectRatioHeight());

        // duration_millis is not appeared in animated_gif
        assertEquals(0, mediaEntity.getVideoDurationMillis());

        ExtendedMediaEntity.Variant[] variants = mediaEntity.getVideoVariants();
        assertEquals(2, variants.length);

        assertEquals(123, variants[0].getBitrate());
        assertEquals("video/mp4", variants[0].getContentType());
        assertEquals("variantUrl1",
                variants[0].getUrl());
    }


}
