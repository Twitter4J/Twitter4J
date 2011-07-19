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

import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A data class representing one single URL entity.
 *
 * @author Mocel - mocel at guma.jp
 * @since Twitter4J 2.1.9
 */
/* package */ final class URLEntityJSONImpl implements URLEntity {

    private int start = -1;
    private int end = -1;
    private URL url;
    private URL expandedURL;
    private String displayURL;

    private static final long serialVersionUID = 1165188478018146676L;

    /* package */ URLEntityJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            this.start = indicesArray.getInt(0);
            this.end = indicesArray.getInt(1);

            try {
                this.url = new URL(json.getString("url"));
            } catch (MalformedURLException ignore) {
            }

            if (!json.isNull("expanded_url")) {
                try {
                    this.expandedURL = new URL(json.getString("expanded_url"));
                } catch (MalformedURLException ignore) {
                }
            }
            if (!json.isNull("display_url")) {
                this.displayURL = json.getString("display_url");
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    public URL getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    public URL getExpandedURL() {
        return expandedURL;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayURL() {
        return displayURL;
    }

    /**
     * {@inheritDoc}
     */
    public int getStart() {
        return start;
    }

    /**
     * {@inheritDoc}
     */
    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URLEntityJSONImpl that = (URLEntityJSONImpl) o;

        if (end != that.end) return false;
        if (start != that.start) return false;
        if (displayURL != null ? !displayURL.equals(that.displayURL) : that.displayURL != null)
            return false;
        if (expandedURL != null ? !expandedURL.equals(that.expandedURL) : that.expandedURL != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (expandedURL != null ? expandedURL.hashCode() : 0);
        result = 31 * result + (displayURL != null ? displayURL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "URLEntityJSONImpl{" +
                "start=" + start +
                ", end=" + end +
                ", url=" + url +
                ", expandedURL=" + expandedURL +
                ", displayURL=" + displayURL +
                '}';
    }
}
