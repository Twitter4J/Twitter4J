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

/**
 * Represents result of "/1.1/media/upload.json"
 * 
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.2
 */
public final class UploadedMedia implements java.io.Serializable {

    private static final long serialVersionUID = 5393092535610604718L;
    
    private int imageWidth;
    private int imageHeight;
    private String imageType;
    private long mediaId;
    private long size;

    // chunked uploading
    private String processingState;
    private int processingCheckAfterSecs;
    private int progressPercent;
    private String processingErrorMessage;
    private String processingErrorName;
    private int processingErrorCode = -1;

    private String videoType;

    /*package*/ UploadedMedia(JSONObject json) throws TwitterException {
        init(json);
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public String getImageType() {
        return imageType;
    }

    public long getMediaId() {
        return mediaId;
    }

    public long getSize() {
        return size;
    }
    
    public String getProcessingState() {
        return processingState;
    }
    
    public int getProcessingCheckAfterSecs() {
        return processingCheckAfterSecs;
    }
  
    public int getProgressPercent() {
        return progressPercent;
    }

    public String getProcessingErrorMessage() {
        return processingErrorMessage;
    }

    public String getProcessingErrorName() {
        return processingErrorName;
    }

    public int getProcessingErrorCode() {
        return processingErrorCode;
    }

    public String getVideoType() {
        return videoType;
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

            if (!json.isNull("video")) {
                JSONObject video = json.getJSONObject("video");
                videoType = ParseUtil.getUnescapedString("video_type", video);
            }

            if (!json.isNull("processing_info")) {
                JSONObject processingInfo = json.getJSONObject("processing_info");
                processingState = ParseUtil.getUnescapedString("state", processingInfo);
                processingCheckAfterSecs = ParseUtil.getInt("check_after_secs", processingInfo);
                progressPercent = ParseUtil.getInt("progress_percent", processingInfo);

                if (!processingInfo.isNull("error")) {
                    JSONObject error = processingInfo.getJSONObject("error");
                    processingErrorCode = ParseUtil.getInt("code", error);
                    processingErrorName = error.getString("name");
                    processingErrorMessage = error.getString("message");
                }
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
        if (mediaId != that.mediaId) return false;
        if (size != that.size) return false;
        if (processingCheckAfterSecs != that.processingCheckAfterSecs) return false;
        if (progressPercent != that.progressPercent) return false;
        if (processingErrorCode != that.processingErrorCode) return false;
        if (imageType != null ? !imageType.equals(that.imageType) : that.imageType != null)
            return false;
        if (processingState != null ? !processingState.equals(that.processingState) : that.processingState != null)
            return false;
        if (processingErrorMessage != null ? !processingErrorMessage.equals(that.processingErrorMessage) : that.processingErrorMessage != null)
            return false;
        return processingErrorName != null ? processingErrorName.equals(that.processingErrorName) : that.processingErrorName == null;
    }

    @Override
    public int hashCode() {
        int result = imageWidth;
        result = 31 * result + imageHeight;
        result = 31 * result + (imageType != null ? imageType.hashCode() : 0);
        result = 31 * result + (int) (mediaId ^ (mediaId >>> 32));
        result = 31 * result + (int) (size ^ (size >>> 32));
        result = 31 * result + (processingState != null ? processingState.hashCode() : 0);
        result = 31 * result + processingCheckAfterSecs;
        result = 31 * result + progressPercent;
        result = 31 * result + (processingErrorMessage != null ? processingErrorMessage.hashCode() : 0);
        result = 31 * result + (processingErrorName != null ? processingErrorName.hashCode() : 0);
        result = 31 * result + processingErrorCode;
        return result;
    }

    @Override
    public String toString() {
        return "UploadedMedia{" +
                "imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", imageType='" + imageType + '\'' +
                ", mediaId=" + mediaId +
                ", size=" + size +
                ", processingState='" + processingState + '\'' +
                ", processingCheckAfterSecs=" + processingCheckAfterSecs +
                ", progressPercent=" + progressPercent +
                ", processingErrorMessage='" + processingErrorMessage + '\'' +
                ", processingErrorName='" + processingErrorName + '\'' +
                ", processingErrorCode=" + processingErrorCode +
                '}';
    }
}
