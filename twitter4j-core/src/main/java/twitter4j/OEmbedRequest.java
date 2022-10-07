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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.2
 */
@SuppressWarnings("unused")
public final class OEmbedRequest implements Serializable {
    private static final long serialVersionUID = 7454130135274547901L;
    private final long statusId;
    private final String url;
    private int maxWidth;
    private boolean hideMedia = true;
    private boolean hideThread = true;
    private boolean omitScript = false;
    private Align align = Align.NONE;
    private String[] related = {};
    private String lang;
    private WidgetType widgetType = WidgetType.NONE;
    private boolean hideTweet = false;

    /**
     * @param statusId status id
     * @param url URL
     */
    public OEmbedRequest(long statusId, String url) {
        this.statusId = statusId;
        this.url = url;
    }

    /**
     * @param maxWidth max width
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * @param maxWidth max width
     * @return this instance
     */
    public OEmbedRequest MaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    /**
     * @param hideMedia hide media
     */
    public void setHideMedia(boolean hideMedia) {
        this.hideMedia = hideMedia;
    }

    /**
     * @param hideMedia hide media
     * @return this instance
     */
    public OEmbedRequest HideMedia(boolean hideMedia) {
        this.hideMedia = hideMedia;
        return this;
    }

    /**
     * @param hideThread hide thread
     */
    public void setHideThread(boolean hideThread) {
        this.hideThread = hideThread;
    }

    /**
     * @param hideThread hide thread
     * @return this instance
     */
    public OEmbedRequest HideThread(boolean hideThread) {
        this.hideThread = hideThread;
        return this;
    }

    /**
     * @param omitScript omit script
     */
    public void setOmitScript(boolean omitScript) {
        this.omitScript = omitScript;
    }

    /**
     * @param omitScript omit script
     * @return this instance
     */
    public OEmbedRequest omitScript(boolean omitScript) {
        this.omitScript = omitScript;
        return this;
    }

    /**
     * @param align align
     */
    public void setAlign(Align align) {
        this.align = align;
    }

    /**
     * @param align align
     * @return this instance
     */
    public OEmbedRequest align(Align align) {
        this.align = align;
        return this;
    }

    /**
     * @param related related
     */
    public void setRelated(String[] related) {
        this.related = related;
    }

    /**
     * @param related related
     * @return this instance
     */
    public OEmbedRequest related(String[] related) {
        this.related = related;
        return this;
    }

    /**
     * @param lang language
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * @param lang language
     * @return this instance
     */
    public OEmbedRequest lang(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * @param widgetType widget type
     */
    public void setWidgetType(WidgetType widgetType) {
        this.widgetType = widgetType;
    }

    /**
     * @param widgetType widget type
     * @return this instance
     */
    public OEmbedRequest widgetType(WidgetType widgetType) {
        this.widgetType = widgetType;
        return this;
    }

    /**
     * @param hideTweet hide tweet
     */
    public void setHideTweet(boolean hideTweet) {
        this.hideTweet = hideTweet;
    }

    /**
     * @param hideTweet hide tweet
     * @return this instance
     */
    public OEmbedRequest hideTweet(boolean hideTweet) {
        this.hideTweet = hideTweet;
        return this;
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

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>(12);
        appendParameter("id", statusId, params);
        appendParameter("url", url, params);
        appendParameter("maxwidth", maxWidth, params);
        params.add(new HttpParameter("hide_media", hideMedia));
        params.add(new HttpParameter("hide_thread", hideThread));
        params.add(new HttpParameter("omit_script", omitScript));
        params.add(new HttpParameter("align", align.name().toLowerCase()));
        if (related.length > 0) {
            appendParameter("related", StringUtil.join(related), params);
        }
        appendParameter("lang", lang, params);
        if (widgetType != WidgetType.NONE) {
            params.add(new HttpParameter("widget_type", widgetType.name().toLowerCase()));
            params.add(new HttpParameter("hide_tweet", hideTweet));
        }

        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, long value, List<HttpParameter> params) {
        if (0 <= value) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OEmbedRequest that = (OEmbedRequest) o;

        if (hideMedia != that.hideMedia) return false;
        if (hideThread != that.hideThread) return false;
        if (maxWidth != that.maxWidth) return false;
        if (omitScript != that.omitScript) return false;
        if (statusId != that.statusId) return false;
        if (align != that.align) return false;
        if (!Objects.equals(lang, that.lang)) return false;
        if (!Arrays.equals(related, that.related)) return false;
        if (!Objects.equals(url, that.url)) return false;
        if (widgetType != that.widgetType) return false;
        return hideTweet == that.hideTweet;
    }

    @Override
    public int hashCode() {
        int result = (int) (statusId ^ (statusId >>> 32));
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + maxWidth;
        result = 31 * result + (hideMedia ? 1 : 0);
        result = 31 * result + (hideThread ? 1 : 0);
        result = 31 * result + (omitScript ? 1 : 0);
        result = 31 * result + (align != null ? align.hashCode() : 0);
        result = 31 * result + (related != null ? Arrays.hashCode(related) : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (widgetType != null ? widgetType.hashCode() : 0);
        result = 31 * result + (hideTweet ? 1 : 0);
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
                ", related=" + (related == null ? null : Arrays.asList(related)) +
                ", lang='" + lang + '\'' +
                ", widgetType=" + widgetType +
                ", hideTweet=" + hideTweet +
                '}';
    }
}
