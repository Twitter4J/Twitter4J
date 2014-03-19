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
 * A data class representing one single URL entity.
 *
 * @author Mocel - mocel at guma.jp
 * @since Twitter4J 2.1.9
 */
/* package */ final class URLEntityJSONImpl extends EntityIndex implements URLEntity {

    private static final long serialVersionUID = 7333552738058031524L;
    private String url;
    private String expandedURL;
    private String displayURL;

    /* package */ URLEntityJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    /* package */ URLEntityJSONImpl(int start, int end, String url, String expandedURL, String displayURL) {
        super();
        setStart(start);
        setEnd(end);
        this.url = url;
        this.expandedURL = expandedURL;
        this.displayURL = displayURL;
    }

    /* For serialization purposes only. */
    /* package */ URLEntityJSONImpl() {

    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            setStart(indicesArray.getInt(0));
            setEnd(indicesArray.getInt(1));

            this.url = json.getString("url");
            if (!json.isNull("expanded_url")) {
                // sets expandedURL to url if expanded_url is null
                // http://jira.twitter4j.org/browse/TFJ-704
                this.expandedURL = json.getString("expanded_url");
            } else {
                this.expandedURL = url;
            }

            if (!json.isNull("display_url")) {
                // sets displayURL to url if expanded_url is null
                // http://jira.twitter4j.org/browse/TFJ-704
                this.displayURL = json.getString("display_url");
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

        URLEntityJSONImpl that = (URLEntityJSONImpl) o;

        if (displayURL != null ? !displayURL.equals(that.displayURL) : that.displayURL != null) return false;
        if (expandedURL != null ? !expandedURL.equals(that.expandedURL) : that.expandedURL != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
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
        return "URLEntityJSONImpl{" +
                "url='" + url + '\'' +
                ", expandedURL='" + expandedURL + '\'' +
                ", displayURL='" + displayURL + '\'' +
                '}';
    }
}
