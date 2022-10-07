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

import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.3
 */
public interface MediaEntity extends URLEntity {
    /**
     * Returns the id of the media.
     *
     * @return the id of the media
     */
    long getId();

    /**
     * Returns the media URL.
     *
     * @return the media URL
     */
    String getMediaURL();

    /**
     * Returns the media secure URL.
     *
     * @return the media secure URL
     */
    String getMediaURLHttps();

    /**
     * Returns size variations of the media.
     *
     * @return size variations of the media
     */
    Map<Integer, Size> getSizes();

    /**
     * size
     */
    interface Size extends java.io.Serializable {
        /**
         * thumbnail
         */
        Integer THUMB = 0;
        /**
         * small
         */
        Integer SMALL = 1;
        /**
         * medium
         */
        Integer MEDIUM = 2;
        /**
         * large
         */
        Integer LARGE = 3;
        /**
         * fit
         */
        int FIT = 100;
        /**
         * crop
         */
        int CROP = 101;

        /**
         * @return width
         */
        int getWidth();

        /**
         * @return height
         */
        int getHeight();

        /**
         * @return resize
         */
        int getResize();
    }

    /**
     * Returns the media type ("photo", "video", "animated_gif").
     *
     * @return the media type ("photo", "video", "animated_gif").
     */
    String getType();

    /**
     * @return video aspect ratio width
     */
    int getVideoAspectRatioWidth();

    /**
     * @return video aspect ratio height
     */
    int getVideoAspectRatioHeight();

    /**
     * @return video duration in milliseconds
     */
    long getVideoDurationMillis();

    /**
     * Variant
     */
    interface Variant extends java.io.Serializable {

        /**
         * @return bit rate
         */
        int getBitrate();

        /**
         * @return content type
         */
        String getContentType();

        /**
         * @return url
         */
        String getUrl();
    }

    /**
     * @return array of video variants
     */
    Variant[] getVideoVariants();

    /**
     * @return alt text
     */
    String getExtAltText();
}
