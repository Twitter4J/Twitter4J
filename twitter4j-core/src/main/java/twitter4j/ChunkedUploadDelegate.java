package twitter4j;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import twitter4j.conf.ChunkedUploadConfiguration;

/**
 * Delegate class for chunked uploading
 *
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
/*package*/ class ChunkedUploadDelegate {

    private final static int MAX_VIDEO_SIZE = 512 * 1024 * 1024; // 512MB is a constraint imposed by Twitter for video files

    private final TwitterImpl twitter;

    private ChunkedUploadConfiguration uploadConfiguration;

    /*package*/ ChunkedUploadDelegate(TwitterImpl twitter) {
        this.twitter = twitter;
    }

    /*package*/ UploadedMedia uploadMediaChunked(ChunkedUploadConfiguration uploadConfiguration) throws TwitterException {
        File mediaFile = uploadConfiguration.getFile();
        this.uploadConfiguration = uploadConfiguration;
        if (mediaFile != null) {
            twitter.checkFileValidity(mediaFile);
            try {
                return upload(
                        uploadConfiguration.getMediaType(), uploadConfiguration.getMediaCategory(),
                        mediaFile.getName(), new FileInputStream(mediaFile), mediaFile.length());
            } catch (IOException e) {
                throw new TwitterException(e);
            }
        } else {
            return upload(uploadConfiguration.getMediaType(), uploadConfiguration.getMediaCategory(),
                    uploadConfiguration.getFilename(), uploadConfiguration.getStream(), uploadConfiguration.getLength());
        }

    }

    private UploadedMedia upload(String mediaType, String mediaCategory, String fileName, InputStream stream, long mediaLength) throws TwitterException {
        if (mediaLength > MAX_VIDEO_SIZE) {
            throw new TwitterException(String.format(Locale.US,
                    "video file can't be longer than: %d MBytes",
                    MAX_VIDEO_SIZE / (1024 * 1024)));
        }
        try {
            onProgress("Initializing", 0, mediaLength, "", 0);
            UploadedMedia uploadedMedia = sendInit(mediaType, mediaCategory, mediaLength);
            onProgress("Initialized", 0, mediaLength, "", 0);
            BufferedInputStream buffered = new BufferedInputStream(stream);

            byte[] segmentData = new byte[uploadConfiguration.getSegmentSizeBytes()];
            long totalRead = 0;

            for (int bytesRead, segmentIndex = 0; (bytesRead = buffered.read(segmentData)) > 0; ++segmentIndex) {
                totalRead += bytesRead;
                //no need to close ByteArrayInputStream
                sendAppend(fileName, new ByteArrayInputStream(segmentData, 0, bytesRead), segmentIndex, uploadedMedia.getMediaId());
                onProgress("Appended", totalRead, mediaLength, "", 0);
            }
            return sendAndWaitForFinalize(uploadedMedia.getMediaId(), mediaLength);
        } catch (IOException e) {
            throw new TwitterException(e);
        }
    }

    private UploadedMedia sendInit(String mediaType, String mediaCategory, long size) throws TwitterException {
        return new UploadedMedia(
                twitter.post(twitter.conf.getUploadBaseURL() + "media/upload.json",
                        new HttpParameter("command", "INIT"),
                        new HttpParameter("media_type", mediaType),
                        new HttpParameter("media_category", mediaCategory),
                        new HttpParameter("total_bytes", size)).asJSONObject());
    }

    private void sendAppend(String fileName, InputStream segmentData, int segmentIndex, long mediaId) throws TwitterException {
        twitter.post(twitter.conf.getUploadBaseURL() + "media/upload.json",
                new HttpParameter("command", "APPEND"),
                new HttpParameter("media_id", mediaId),
                new HttpParameter("segment_index", segmentIndex),
                new HttpParameter("media", fileName, segmentData));
    }

    private UploadedMedia sendAndWaitForFinalize(long mediaId, long mediaLength) throws TwitterException {
        int tries = 0;
        int maxTries = 20;
        int lastProgressPercent = 0;
        int currentProgressPercent = 0;
        int totalWaitSec = 0;
        UploadedMedia uploadedMedia = sendFinalize(mediaId);
        while (tries < maxTries) {
            if (lastProgressPercent == currentProgressPercent) {
                tries++;
            }
            lastProgressPercent = currentProgressPercent;

            String state = uploadedMedia.getProcessingState();
            if (state.equals("failed")) {
                if (uploadedMedia.getProcessingErrorMessage() != null) {
                    throw new TwitterException(
                            uploadedMedia.getProcessingErrorMessage() + " (" + uploadedMedia.getProcessingErrorName() + ")",
                            uploadedMedia.getProcessingErrorCode());
                } else {
                    throw new TwitterException("Failed to finalize the chunked upload.");
                }
            }
            if (state.equals("pending") || state.equals("in_progress")) {
                currentProgressPercent = uploadedMedia.getProgressPercent();
                int waitSec = uploadedMedia.getProcessingCheckAfterSecs();
                if (waitSec <= 0) {
                    throw new TwitterException("Failed to finalize the chunked upload, invalid check_after_secs value " + waitSec);
                }
                totalWaitSec += waitSec;
                if (totalWaitSec > uploadConfiguration.getFinalizeTimeout()) {
                    throw new TwitterException("Failed to finalize the chunked upload, timed out after " + totalWaitSec + " seconds");
                }

                onProgress("Finalizing... wait for:" + waitSec + " sec", mediaLength, mediaLength,
                        state, currentProgressPercent < 0 ? 0 : currentProgressPercent);

                try {
                    Thread.sleep(waitSec * 1000);
                } catch (InterruptedException e) {
                    throw new TwitterException("Failed to finalize the chunked upload.", e);
                }
            }
            if (state.equals("succeeded")) {
                onProgress("Finalized", mediaLength, mediaLength, state, uploadedMedia.getProgressPercent());
                return uploadedMedia;
            }
            uploadedMedia = getStatus(mediaId);
        }
        throw new TwitterException("Failed to finalize the chunked upload, progress has stopped, tried " + tries+1 + " times.");
    }

    private UploadedMedia sendFinalize(long mediaId) throws TwitterException {
        return new UploadedMedia(
                twitter.post(twitter.conf.getUploadBaseURL() + "media/upload.json",
                            new HttpParameter("command", "FINALIZE"),
                            new HttpParameter("media_id", mediaId)).asJSONObject());
    }

    private UploadedMedia getStatus(long mediaId) throws TwitterException {
        return new UploadedMedia(
                twitter.get(twitter.conf.getUploadBaseURL() + "media/upload.json",
                        new HttpParameter("command", "STATUS"),
                        new HttpParameter("media_id", mediaId)).asJSONObject());
    }

    private void onProgress(String progress, long uploadedBytes, long totalBytes, String finalizeProcessingState, int finalizeProgressPercent) {
        if (uploadConfiguration.getCallback() != null) {
            uploadConfiguration.getCallback().onProgress(progress, uploadedBytes, totalBytes, finalizeProcessingState, finalizeProgressPercent);
        }
    }

}
