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
package twitter4j.internal.json;

import twitter4j.MediaEntity;
import twitter4j.TwitterException;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static twitter4j.internal.json.z_T4JInternalParseUtil.getLong;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.3
 */
public class MediaEntityJSONImpl extends EntityIndex implements MediaEntity {
    private static final long serialVersionUID = 224487082931268487L;
    private long id;
    private String url;
    private String mediaURL;
    private String mediaURLHttps;
    private String expandedURL;
    private String displayURL;
    private Map<Integer, MediaEntity.Size> sizes;
    private String type;

    MediaEntityJSONImpl(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            setStart(indicesArray.getInt(0));
            setEnd(indicesArray.getInt(1));
            this.id = getLong("id", json);

            this.url = json.getString("url");
            this.expandedURL = json.getString("expanded_url");
            this.mediaURL = json.getString("media_url");
            this.mediaURLHttps = json.getString("media_url_https");
            this.displayURL = json.getString("display_url");

            JSONObject sizes = json.getJSONObject("sizes");
            this.sizes = new HashMap<Integer, MediaEntity.Size>(4);
            // thumbworkarounding API side issue
            addMediaEntitySizeIfNotNull(this.sizes, sizes, MediaEntity.Size.LARGE, "large");
            addMediaEntitySizeIfNotNull(this.sizes, sizes, MediaEntity.Size.MEDIUM, "medium");
            addMediaEntitySizeIfNotNull(this.sizes, sizes, MediaEntity.Size.SMALL, "small");
            addMediaEntitySizeIfNotNull(this.sizes, sizes, MediaEntity.Size.THUMB, "thumb");
            if (!json.isNull("type")) {
                this.type = json.getString("type");
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    private void addMediaEntitySizeIfNotNull(Map<Integer, MediaEntity.Size> sizes, JSONObject sizesJSON, Integer size, String key) throws JSONException {
        if(!sizesJSON.isNull(key)){
            sizes.put(size, new Size(sizesJSON.getJSONObject(key)));
        }
    }

    /* For serialization purposes only. */
    /* package */ MediaEntityJSONImpl() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMediaURL() {
        return mediaURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMediaURLHttps() {
        return mediaURLHttps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayURL() {
        return displayURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExpandedURL() {
        return expandedURL;
    }

    @Override
    public Map<Integer, MediaEntity.Size> getSizes() {
        return sizes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStart() {
        return super.getStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEnd() {
        return super.getEnd();
    }

    static class Size implements MediaEntity.Size {
        private static final long serialVersionUID = 8681853416159361581L;
        int width;
        int height;
        int resize;

        Size(JSONObject json) throws JSONException {
            width = json.getInt("w");
            height = json.getInt("h");
            resize = "fit".equals(json.getString("resize")) ? MediaEntity.Size.FIT : MediaEntity.Size.CROP;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public int getResize() {
            return resize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Size)) return false;

            Size size = (Size) o;

            if (height != size.height) return false;
            if (resize != size.resize) return false;
            if (width != size.width) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = width;
            result = 31 * result + height;
            result = 31 * result + resize;
            return result;
        }

        @Override
        public String toString() {
            return "Size{" +
                    "width=" + width +
                    ", height=" + height +
                    ", resize=" + resize +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaEntityJSONImpl)) return false;

        MediaEntityJSONImpl that = (MediaEntityJSONImpl) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "MediaEntityJSONImpl{" +
                "id=" + id +
                ", url=" + url +
                ", mediaURL=" + mediaURL +
                ", mediaURLHttps=" + mediaURLHttps +
                ", expandedURL=" + expandedURL +
                ", displayURL='" + displayURL + '\'' +
                ", sizes=" + sizes +
                ", type=" + type +
                '}';
    }
}
