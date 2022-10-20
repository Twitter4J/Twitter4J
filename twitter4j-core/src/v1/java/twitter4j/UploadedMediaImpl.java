package twitter4j;

import twitter4j.v1.UploadedMedia;

import java.io.Serializable;
import java.util.Objects;

final class UploadedMediaImpl implements UploadedMedia, Serializable {

    private static final long serialVersionUID = 3375172182790067615L;
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

    /*package*/ UploadedMediaImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    @Override
    public int getImageWidth() {
        return imageWidth;
    }

    @Override
    public int getImageHeight() {
        return imageHeight;
    }

    @Override
    public String getImageType() {
        return imageType;
    }

    @Override
    public long getMediaId() {
        return mediaId;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getProcessingState() {
        return processingState;
    }

    @Override
    public int getProcessingCheckAfterSecs() {
        return processingCheckAfterSecs;
    }

    @Override
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
        UploadedMediaImpl that = (UploadedMediaImpl) o;
        return imageWidth == that.imageWidth && imageHeight == that.imageHeight && mediaId == that.mediaId && size == that.size && processingCheckAfterSecs == that.processingCheckAfterSecs && progressPercent == that.progressPercent && Objects.equals(imageType, that.imageType) && Objects.equals(processingState, that.processingState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageWidth, imageHeight, imageType, mediaId, size, processingState, processingCheckAfterSecs, progressPercent);
    }

    @Override
    public String toString() {
        return "UploadedMediaImpl{" +
                "imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", imageType='" + imageType + '\'' +
                ", mediaId=" + mediaId +
                ", size=" + size +
                ", processingState='" + processingState + '\'' +
                ", processingCheckAfterSecs=" + processingCheckAfterSecs +
                ", progressPercent=" + progressPercent +
                '}';
    }
}
