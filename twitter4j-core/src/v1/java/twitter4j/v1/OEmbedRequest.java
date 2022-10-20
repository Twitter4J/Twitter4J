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
package twitter4j.v1;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.2
 */
@SuppressWarnings("unused")
public final class OEmbedRequest implements Serializable {
    private static final long serialVersionUID = 7454130135274547901L;
    /**
     * statusId
     */
    public final long statusId;
    /**
     * URL
     */
    public final String url;
    /**
     * maxWidth
     */
    public final int maxWidth;
    /**
     * hideMedia
     */
    public final boolean hideMedia;
    /**
     * hideThread
     */
    public final boolean hideThread;
    /**
     * omitScript
     */
    public final boolean omitScript;
    /**
     * align
     */
    public final Align align;
    /**
     * related
     */
    public final String[] related;
    /**
     * lang
     */
    public final String lang;
    /**
     * widgetType
     */
    public final WidgetType widgetType;
    /**
     * hideTweet
     */
    public final boolean hideTweet = false;

    /**
     * @param statusId status id
     * @param url      URL
     * @return OEmbedRequest
     */
    public static OEmbedRequest of(long statusId, String url) {
        return new OEmbedRequest(statusId, url, -1, true, true, false, Align.NONE, new String[]{}, null, WidgetType.NONE);
    }

    private OEmbedRequest(long statusId, String url, int maxWidth, boolean hideMedia, boolean hideThread,
                          boolean omitScript, Align align, String[] related, String lang, WidgetType widgetType) {
        this.statusId = statusId;
        this.url = url;
        this.maxWidth = maxWidth;
        this.hideMedia = hideMedia;
        this.hideThread = hideThread;
        this.omitScript = omitScript;
        this.align = align;
        this.related = related;
        this.lang = lang;
        this.widgetType = widgetType;
    }

    /**
     * @param maxWidth max width
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest MaxWidth(int maxWidth) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }

    /**
     * @param hideMedia hide media
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest HideMedia(boolean hideMedia) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }

    /**
     * @param hideThread hide thread
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest HideThread(boolean hideThread) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }

    /**
     * @param omitScript omit script
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest omitScript(boolean omitScript) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }

    /**
     * @param align align
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest align(Align align) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }

    /**
     * @param related related
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest related(String[] related) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }

    /**
     * @param lang language
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest lang(String lang) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }

    /**
     * @param widgetType widget type
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest widgetType(WidgetType widgetType) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }

    /**
     * @param hideTweet hide tweet
     * @return new OEmbedRequest instance
     */
    public OEmbedRequest hideTweet(boolean hideTweet) {
        return new OEmbedRequest(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, related, lang, widgetType);
    }


    /**
     * align
     */
    public enum Align {
        /**
         * left
         */
        LEFT,
        /**
         * center
         */
        CENTER,
        /**
         * right
         */
        RIGHT,
        /**
         * none
         */
        NONE
    }

    /**
     * widget type
     */
    public enum WidgetType {
        /**
         * video
         */
        VIDEO,
        /**
         * none
         */
        NONE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OEmbedRequest that = (OEmbedRequest) o;
        return statusId == that.statusId && maxWidth == that.maxWidth && hideMedia == that.hideMedia && hideThread == that.hideThread && omitScript == that.omitScript && hideTweet == that.hideTweet && Objects.equals(url, that.url) && align == that.align && Arrays.equals(related, that.related) && Objects.equals(lang, that.lang) && widgetType == that.widgetType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(statusId, url, maxWidth, hideMedia, hideThread, omitScript, align, lang, widgetType, hideTweet);
        result = 31 * result + Arrays.hashCode(related);
        return result;
    }

    @Override
    public String toString() {
        return "OEmbedRequest{" +
                "statusId=" + statusId +
                ", url='" + url + '\'' +
                ", maxWidth=" + maxWidth +
                ", hideMedia=" + hideMedia +
                ", hideThread=" + hideThread +
                ", omitScript=" + omitScript +
                ", align=" + align +
                ", related=" + Arrays.toString(related) +
                ", lang='" + lang + '\'' +
                ", widgetType=" + widgetType +
                ", hideTweet=" + hideTweet +
                '}';
    }
}
