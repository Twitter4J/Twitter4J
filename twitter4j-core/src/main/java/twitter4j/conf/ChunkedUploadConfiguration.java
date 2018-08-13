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

package twitter4j.conf;

import java.io.File;
import java.io.InputStream;

import twitter4j.TwitterException;

/**
 * Specification for {@link twitter4j.api.TweetsResources}.uploadMediaChunked
 *
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @since Twitter4J 4.1.0-beta4
 */
public final class ChunkedUploadConfiguration {

    private String mediaType = null;

    private String mediaCategory = null;

    private File file = null;

    private String filename = null;
    private InputStream stream = null;
    private long length = -1;

    private int finalizeTimeout = 30;           // When doing a chunked upload, bail out after 30 seconds by default
    private int segmentSizeBytes = 1024 * 1024; // Segment size defaults to 1MB

    private Callback callback = null;

    /**
     * Stream type, e.g. "video/mp4", "video/gif", "image/jpeg", ...
     *
     * @return media type
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Stream category, e.g. "tweet_video", "tweet_gif" or "tweet_image"
     *
     * @return media category
     */
    public String getMediaCategory() {
        return mediaCategory;
    }

    public File getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }

    public InputStream getStream() {
        return stream;
    }

    public long getLength() {
        return length;
    }

    public Callback getCallback() {
        return callback;
    }

    /**
     * Timeout (in seconds) for chunked upload finalization.
     *
     * @return Timeout (in seconds) for chunked upload finalization.
     */
    public int getFinalizeTimeout() {
        return finalizeTimeout;
    }

    /**
     * Size of segments (in bytes) when performing a chunked upload
     *
     * @return Size of segments (in bytes) when performing a chunked upload
     */
    public int getSegmentSizeBytes() {
        return segmentSizeBytes;
    }

    public final static class Builder {

        private ChunkedUploadConfiguration conf = new ChunkedUploadConfiguration();

        public Builder movie() {
            conf.mediaType = "video/mp4";
            conf.mediaCategory = "tweet_video";
            return this;
        }

        public Builder gif() {
            conf.mediaType = "video/gif";
            conf.mediaCategory = "tweet_gif";
            return this;
        }

        public Builder from(File file) {
            conf.file = file;
            return this;
        }

        public Builder from(String filename, InputStream stream, long length) {
            conf.filename = filename;
            conf.stream = stream;
            conf.length = length;
            return this;
        }

        public Builder callback(Callback callback) {
            conf.callback = callback;
            return this;
        }

        public Builder finalizeTimeout(int timeout) {
            conf.finalizeTimeout = timeout;
            return this;
        }

        public Builder segmentSizeBytes(int segmentSizeBytes) {
            conf.segmentSizeBytes = segmentSizeBytes;
            return this;
        }

        public ChunkedUploadConfiguration build() throws TwitterException {

            // must specify file or filename/stream/length
            if (conf.file == null && conf.filename == null) {
                throw new TwitterException("specify file");
            }
            if (conf.file != null && conf.filename != null) {
                throw new TwitterException("specify file with File or Filename");
            }

            if (conf.mediaType == null) {
                throw new TwitterException("specify media type");
            }

            return conf;
        }
    }

    public interface Callback {
        void onProgress(String progress, long uploadedBytes, long totalBytes, String finalizeProcessingState, int finalizeProgressPercent);
    }
}
