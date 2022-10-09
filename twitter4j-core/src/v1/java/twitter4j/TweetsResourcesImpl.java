package twitter4j;

import twitter4j.v1.TweetsResources;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

class TweetsResourcesImpl extends APIResourceBase implements TweetsResources {
    private static final Logger logger = Logger.getLogger();

    private static final String CHUNKED_INIT = "INIT";
    private static final String CHUNKED_APPEND = "APPEND";
    private static final String CHUNKED_FINALIZE = "FINALIZE";
    private static final String CHUNKED_STATUS = "STATUS";
    private static final int MB = 1024 * 1024; // 1 MByte
    private static final int MAX_VIDEO_SIZE = 512 * MB; // 512MB is a constraint  imposed by Twitter for video files
    private static final int CHUNK_SIZE = 2 * MB; // max chunk size
    private final String uploadBaseURL;


    TweetsResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                        String IMPLICIT_PARAMS_STR,
                        List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                        List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners,
                        String uploadBaseURL) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        this.uploadBaseURL = uploadBaseURL;
    }

    @Override
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/retweets/" + statusId + ".json?count=100"));
    }

    @Override
    public IDs getRetweeterIds(long statusId, long cursor) throws TwitterException {
        return getRetweeterIds(statusId, 100, cursor);
    }

    @Override
    public IDs getRetweeterIds(long statusId, int count, long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "statuses/retweeters/ids.json?id=" + statusId + "&cursor=" + cursor + "&count=" + count));
    }

    @Override
    public Status showStatus(long id) throws TwitterException {
        return factory.createStatus(get(restBaseURL + "statuses/show/" + id + ".json", includeMyRetweet));
    }

    @Override
    public Status destroyStatus(long statusId) throws TwitterException {
        return factory.createStatus(post(restBaseURL + "statuses/destroy/" + statusId + ".json"));
    }

    @Override
    public Status updateStatus(String status) throws TwitterException {
        return factory.createStatus(post(restBaseURL + "statuses/update.json", new HttpParameter("status", status)));
    }

    @Override
    public Status updateStatus(StatusUpdate status) throws TwitterException {
        String url = restBaseURL + (status.isForUpdateWithMedia() ? "statuses/update_with_media.json" : "statuses/update.json");
        return factory.createStatus(post(url, status.asHttpParameterArray()));
    }

    @Override
    public Status retweetStatus(long statusId) throws TwitterException {
        return factory.createStatus(post(restBaseURL + "statuses/retweet/" + statusId + ".json"));
    }

    @Override
    public Status unRetweetStatus(long statusId) throws TwitterException {
        return factory.createStatus(post(restBaseURL + "statuses/unretweet/" + statusId + ".json"));
    }

    @Override
    public OEmbed getOEmbed(OEmbedRequest req) throws TwitterException {
        return factory.createOEmbed(get(restBaseURL + "statuses/oembed.json", req.asHttpParameterArray()));
    }

    @Override
    public ResponseList<Status> lookup(long... ids) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "statuses/lookup.json?id=" + StringUtil.join(ids)));
    }

    @Override
    public UploadedMedia uploadMedia(File image) throws TwitterException {
        checkFileValidity(image);
        return new UploadedMedia(post(uploadBaseURL + "media/upload.json", new HttpParameter("media", image)).asJSONObject());
    }

    @Override
    public UploadedMedia uploadMedia(String fileName, InputStream image) throws TwitterException {
        return new UploadedMedia(post(uploadBaseURL + "media/upload.json", new HttpParameter("media", fileName, image)).asJSONObject());
    }

    @Override
    public UploadedMedia uploadMediaChunked(String fileName, InputStream media) throws TwitterException {
        //If the InputStream is remote, this is will download it into memory speeding up the chunked upload process
        byte[] dataBytes;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(256 * 1024);
            byte[] buffer = new byte[32768];
            int n;
            while ((n = media.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            dataBytes = baos.toByteArray();
            if (dataBytes.length > MAX_VIDEO_SIZE) {
                throw new TwitterException(String.format(Locale.US, "video file can't be longer than: %d MBytes", MAX_VIDEO_SIZE / MB));
            }
        } catch (IOException ioe) {
            throw new TwitterException("Failed to download the file.", ioe);
        }

        try {

            UploadedMedia uploadedMedia = uploadMediaChunkedInit(dataBytes.length);
            //no need to close ByteArrayInputStream
            ByteArrayInputStream dataInputStream = new ByteArrayInputStream(dataBytes);

            byte[] segmentData = new byte[CHUNK_SIZE];
            int segmentIndex = 0;
            int totalRead = 0;
            int bytesRead;

            while ((bytesRead = dataInputStream.read(segmentData)) > 0) {
                totalRead = totalRead + bytesRead;
                logger.debug("Chunked appened, segment index:" + segmentIndex + " bytes:" + totalRead + "/" + dataBytes.length);
                //no need to close ByteArrayInputStream
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(segmentData, 0, bytesRead);
                uploadMediaChunkedAppend(fileName, byteArrayInputStream, segmentIndex, uploadedMedia.getMediaId());
                segmentData = new byte[CHUNK_SIZE];
                segmentIndex++;
            }
            return uploadMediaChunkedFinalize(uploadedMedia.getMediaId());
        } catch (Exception e) {
            throw new TwitterException(e);
        }
    }

    // twurl -H upload.twitter.com "/1.1/media/upload.json" -d
    // "command=INIT&media_type=video/mp4&total_bytes=4430752"

    private UploadedMedia uploadMediaChunkedInit(long size) throws TwitterException {
        return new UploadedMedia(post(uploadBaseURL + "media/upload.json", new HttpParameter("command", CHUNKED_INIT), new HttpParameter("media_type", "video/mp4"), new HttpParameter("media_category", "tweet_video"), new HttpParameter("total_bytes", size)).asJSONObject());
    }

    // twurl -H upload.twitter.com "/1.1/media/upload.json" -d
    // "command=APPEND&media_id=601413451156586496&segment_index=0" --file
    // /path/to/video.mp4 --file-field "media"

    private void uploadMediaChunkedAppend(String fileName, InputStream media, int segmentIndex, long mediaId) throws TwitterException {
        post(uploadBaseURL + "media/upload.json", new HttpParameter("command", CHUNKED_APPEND), new HttpParameter("media_id", mediaId), new HttpParameter("segment_index", segmentIndex), new HttpParameter("media", fileName, media));
    }

    // twurl -H upload.twitter.com "/1.1/media/upload.json" -d
    // "command=FINALIZE&media_id=601413451156586496"

    private UploadedMedia uploadMediaChunkedFinalize(long mediaId) throws TwitterException {
        int tries = 0;
        int maxTries = 20;
        int lastProgressPercent = 0;
        int currentProgressPercent = 0;
        UploadedMedia uploadedMedia = uploadMediaChunkedFinalize0(mediaId);
        while (tries < maxTries) {
            if (lastProgressPercent == currentProgressPercent) {
                tries++;
            }
            lastProgressPercent = currentProgressPercent;
            String state = uploadedMedia.getProcessingState();
            if (state.equals("failed")) {
                throw new TwitterException("Failed to finalize the chuncked upload.");
            }
            if (state.equals("pending") || state.equals("in_progress")) {
                currentProgressPercent = uploadedMedia.getProgressPercent();
                int waitSec = Math.max(uploadedMedia.getProcessingCheckAfterSecs(), 1);
                logger.debug("Chunked finalize, wait for:" + waitSec + " sec");
                try {
                    Thread.sleep(waitSec * 1000L);
                } catch (InterruptedException e) {
                    throw new TwitterException("Failed to finalize the chuncked upload.", e);
                }
            }
            if (state.equals("succeeded")) {
                return uploadedMedia;
            }
            uploadedMedia = uploadMediaChunkedStatus(mediaId);
        }
        throw new TwitterException("Failed to finalize the chuncked upload, progress has stopped, tried " + tries + 1 + " times.");
    }

    private UploadedMedia uploadMediaChunkedFinalize0(long mediaId) throws TwitterException {
        JSONObject json = post(uploadBaseURL + "media/upload.json", new HttpParameter("command", CHUNKED_FINALIZE), new HttpParameter("media_id", mediaId)).asJSONObject();
        logger.debug("Finalize response:" + json);
        return new UploadedMedia(json);
    }

    private UploadedMedia uploadMediaChunkedStatus(long mediaId) throws TwitterException {
        JSONObject json = get(uploadBaseURL + "media/upload.json", new HttpParameter("command", CHUNKED_STATUS), new HttpParameter("media_id", mediaId)).asJSONObject();
        logger.debug("Status response:" + json);
        return new UploadedMedia(json);
    }

}
