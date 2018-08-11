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
 * A data class representing permalink of the quoted status
 *
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
/* package */ final class QuotedStatusPermalinkJSONImpl extends EntityIndex implements URLEntity {

    private static final long serialVersionUID = -9029983811168784541L;
    private String url;
    private String expandedURL;
    private String displayURL;

    /* package */ QuotedStatusPermalinkJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    /* For serialization purposes only. */
    /* package */ QuotedStatusPermalinkJSONImpl() {
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            if (!json.isNull("url")) {
                this.url = json.getString("url");
            }

            if (!json.isNull("expanded")) {
                this.expandedURL = json.getString("expanded");
            } else {
                this.expandedURL = url;
            }

            if (!json.isNull("display")) {
                this.displayURL = json.getString("display");
            } else {
                this.displayURL = url;
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
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
    public String getExpandedURL() {
        return expandedURL;
    }

    @Override
    public String getDisplayURL() {
        return displayURL;
    }

    @Override
    public int getStart() {
        return super.getStart();
    }

    @Override
    public int getEnd() {
        return super.getEnd();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuotedStatusPermalinkJSONImpl that = (QuotedStatusPermalinkJSONImpl) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (expandedURL != null ? !expandedURL.equals(that.expandedURL) : that.expandedURL != null) return false;
        return displayURL != null ? displayURL.equals(that.displayURL) : that.displayURL == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (expandedURL != null ? expandedURL.hashCode() : 0);
        result = 31 * result + (displayURL != null ? displayURL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuotedStatusPermalinkJSONImpl{" +
                "url='" + url + '\'' +
                ", expandedURL='" + expandedURL + '\'' +
                ", displayURL='" + displayURL + '\'' +
                '}';
    }
}
