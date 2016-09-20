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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.3
 */
public class MediaEntityJSONImpl extends EntityIndex implements MediaEntity {
    private static final long serialVersionUID = 3609683338035442290L;
    protected long id;
    protected String url;
    protected String mediaURL;
    protected String mediaURLHttps;
    protected String expandedURL;
    protected String displayURL;
    protected Map<Integer, MediaEntity.Size> sizes;
    protected String type;


    MediaEntityJSONImpl(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            setStart(indicesArray.getInt(0));
            setEnd(indicesArray.getInt(1));
            this.id = ParseUtil.getLong("id", json);

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
        if (!sizesJSON.isNull(key)) {
            sizes.put(size, new Size(sizesJSON.getJSONObject(key)));
        }
    }

    /* For serialization purposes only. */
    /* package */ MediaEntityJSONImpl() {

    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getMediaURL() {
        return mediaURL;
    }

    @Override
    public String getMediaURLHttps() {
        return mediaURLHttps;
    }

    @Override
    public String getText() {
        return url;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getDisplayURL() {
        return displayURL;
    }

    @Override
    public String getExpandedURL() {
        return expandedURL;
    }

    @Override
    public Map<Integer, MediaEntity.Size> getSizes() {
        return sizes;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getStart() {
        return super.getStart();
    }

    @Override
    public int getEnd() {
        return super.getEnd();
    }

    static class Size implements MediaEntity.Size {
        private static final long serialVersionUID = -2515842281909325169L;
        int width;
        int height;
        int resize;
        
        /* For serialization purposes only. */
		/* package */
		Size() {
		}

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
