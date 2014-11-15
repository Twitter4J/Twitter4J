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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.2
 */
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

    public OEmbedRequest(long statusId, String url) {
        this.statusId = statusId;
        this.url = url;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public OEmbedRequest MaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public void setHideMedia(boolean hideMedia) {
        this.hideMedia = hideMedia;
    }

    public OEmbedRequest HideMedia(boolean hideMedia) {
        this.hideMedia = hideMedia;
        return this;
    }

    public void setHideThread(boolean hideThread) {
        this.hideThread = hideThread;
    }

    public OEmbedRequest HideThread(boolean hideThread) {
        this.hideThread = hideThread;
        return this;
    }

    public void setOmitScript(boolean omitScript) {
        this.omitScript = omitScript;
    }

    public OEmbedRequest omitScript(boolean omitScript) {
        this.omitScript = omitScript;
        return this;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public OEmbedRequest align(Align align) {
        this.align = align;
        return this;
    }

    public void setRelated(String[] related) {
        this.related = related;
    }

    public OEmbedRequest related(String[] related) {
        this.related = related;
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public OEmbedRequest lang(String lang) {
        this.lang = lang;
        return this;
    }

    public enum Align {
        LEFT,
        CENTER,
        RIGHT,
        NONE
    }

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<HttpParameter>(12);
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
        if (lang != null ? !lang.equals(that.lang) : that.lang != null) return false;
        if (!Arrays.equals(related, that.related)) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
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
                '}';
    }
}
