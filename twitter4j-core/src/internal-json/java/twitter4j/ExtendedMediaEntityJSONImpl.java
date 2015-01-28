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

import java.util.Arrays;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 4.x.x
 */
public class ExtendedMediaEntityJSONImpl extends MediaEntityJSONImpl implements ExtendedMediaEntity {
    private static final long serialVersionUID = -3889082303259253211L;
    private int videoAspectRatioWidth;
    private int videoAspectRatioHeight;
    private long videoDurationMillis;
    private Variant[] videoVariants;


    ExtendedMediaEntityJSONImpl(JSONObject json) throws TwitterException {

        super(json);

        try {
            if (json.has("video_info")) {
                JSONObject videoInfo = json.getJSONObject("video_info");
                JSONArray aspectRatio = videoInfo.getJSONArray("aspect_ratio");
                this.videoAspectRatioWidth = aspectRatio.getInt(0);
                this.videoAspectRatioHeight = aspectRatio.getInt(1);

                // not in animated_gif
                if (!videoInfo.isNull("duration_millis")) {
                    this.videoDurationMillis = videoInfo.getLong("duration_millis");
                }

                JSONArray variants = videoInfo.getJSONArray("variants");
                this.videoVariants = new Variant[variants.length()];
                for (int i=0; i<variants.length(); i++) {
                    this.videoVariants[i] = new Variant(variants.getJSONObject(i));
                }
            } else {
                this.videoVariants = new Variant[0];
            }

        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /* For serialization purposes only. */
    /* package */ ExtendedMediaEntityJSONImpl() {

    }

    @Override
    public int getVideoAspectRatioWidth() {
        return videoAspectRatioWidth;
    }

    @Override
    public int getVideoAspectRatioHeight() {
        return videoAspectRatioHeight;
    }

    @Override
    public long getVideoDurationMillis() {
        return videoDurationMillis;
    }

    @Override
    public ExtendedMediaEntity.Variant[] getVideoVariants() {
        return videoVariants;
    }

    static class Variant implements ExtendedMediaEntity.Variant {
        private static final long serialVersionUID = 1027236588556797980L;
        int bitrate;
        String contentType;
        String url;

        Variant(JSONObject json) throws JSONException {
            bitrate = json.has("bitrate") ? json.getInt("bitrate") : 0;
            contentType = json.getString("content_type");
            url =json.getString("url");
        }

        /* For serialization purposes only. */
        /* package */ Variant() {
        }

        @Override
        public int getBitrate() {
            return bitrate;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Variant)) return false;

            Variant variant = (Variant) o;

            if (bitrate != variant.bitrate) return false;
            if (!contentType.equals(variant.contentType)) return false;
            if (!url.equals(variant.url)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = bitrate;
            result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
            result = 31 * result + (url != null ? url.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Variant{" +
                    "bitrate=" + bitrate +
                    ", contentType=" + contentType +
                    ", url=" + url +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtendedMediaEntityJSONImpl)) return false;

        ExtendedMediaEntityJSONImpl that = (ExtendedMediaEntityJSONImpl) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ExtendedMediaEntityJSONImpl{" +
                "id=" + id +
                ", url=" + url +
                ", mediaURL=" + mediaURL +
                ", mediaURLHttps=" + mediaURLHttps +
                ", expandedURL=" + expandedURL +
                ", displayURL='" + displayURL + '\'' +
                ", sizes=" + sizes +
                ", type=" + type +
                ", videoAspectRatioWidth=" + videoAspectRatioWidth +
                ", videoAspectRatioHeight=" + videoAspectRatioHeight +
                ", videoDurationMillis=" + videoDurationMillis +
                ", videoVariants=" + Arrays.toString(videoVariants) +
                '}';
    }
}
