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


import java.util.Objects;

/**
 * Represents result of "/1.1/media/upload.json"
 *
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.2
 */
public final class UploadedMedia implements java.io.Serializable {

    private static final long serialVersionUID = 5393092535610604718L;

    /**
     * imageWidth
     */
    private int imageWidth;
    /**
     * imageHeight
     */
    private int imageHeight;
    /**
     * imageType
     */
    private String imageType;
    /**
     * mediaId
     */
    private long mediaId;
    /**
     * size
     */
    private long size;
    /**
     * processingState
     */
    private String processingState;
    /**
     * processingCheckAfterSecs
     */
    private int processingCheckAfterSecs;
    /**
     * progressPercent
     */
    private int progressPercent;

    /*package*/ UploadedMedia(JSONObject json) throws TwitterException {
        init(json);
    }

    /**
     * @return image width
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @return image height
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @return image type
     */
    public String getImageType() {
        return imageType;
    }

    /**
     * @return media id
     */
    public long getMediaId() {
        return mediaId;
    }

    /**
     * @return size
     */
    public long getSize() {
        return size;
    }

    /**
     * @return processing state
     */
    public String getProcessingState() {
        return processingState;
    }

    /**
     * @return processingCheckAfterSecs
     */
    public int getProcessingCheckAfterSecs() {
        return processingCheckAfterSecs;
    }

    /**
     * @return progressPercent
     */
    public int getProgressPercent() {
        return progressPercent;
    }

    private void init(JSONObject json) throws TwitterException {
        mediaId = ParseUtil.getLong("media_id", json);
        size = ParseUtil.getLong("size", json);
        try {
            if (!json.isNull("image")) {
                JSONObject image = json.getJSONObject("image");
                imageWidth = ParseUtil.getInt("w", image);
                imageHeight = ParseUtil.getInt("h", image);
                imageType = ParseUtil.getUnescapedString("image_type", image);
            }

            if (!json.isNull("processing_info")) {
                JSONObject processingInfo = json.getJSONObject("processing_info");
                processingState = ParseUtil.getUnescapedString("state", processingInfo);
                processingCheckAfterSecs = ParseUtil.getInt("check_after_secs", processingInfo);
                progressPercent = ParseUtil.getInt("progress_percent", processingInfo);

            }

        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UploadedMedia that = (UploadedMedia) o;

        if (imageWidth != that.imageWidth) return false;
        if (imageHeight != that.imageHeight) return false;
        if (!Objects.equals(imageType, that.imageType)) return false;
        if (mediaId != that.mediaId) return false;
        return size == that.size;
    }

    @Override
    public int hashCode() {
        int result = (int) (mediaId ^ (mediaId >>> 32));
        result = 31 * result + imageWidth;
        result = 31 * result + imageHeight;
        result = 31 * result + (imageType != null ? imageType.hashCode() : 0);
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "UploadedMedia{" +
                "mediaId=" + mediaId +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", imageType='" + imageType + '\'' +
                ", size=" + size +
                '}';
    }
}
