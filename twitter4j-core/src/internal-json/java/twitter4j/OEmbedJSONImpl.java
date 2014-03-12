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

import twitter4j.conf.Configuration;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.2
 */
public class OEmbedJSONImpl extends TwitterResponseImpl implements OEmbed, java.io.Serializable {
    private static final long serialVersionUID = -2207801480251709819L;
    private String html;
    private String authorName;
    private String url;
    private String version;
    private long cacheAge;
    private String authorURL;
    private int width;

    /*package*/ OEmbedJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
            TwitterObjectFactory.registerJSONObject(this, json);
        }
    }

    /*package*/ OEmbedJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            html = json.getString("html");
            authorName = json.getString("author_name");
            url = json.getString("url");
            version = json.getString("version");
            cacheAge = json.getLong("cache_age");
            authorURL = json.getString("author_url");
            width = json.getInt("width");
            // provider_url provider_name, type always return same value
            // there is no need to parse them and expose the values via OEmbed interface
//            providerURL = json.getString("provider_url");
//            providerName = json.getString("provider_name");
//            type = json.getString("type");
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public String getHtml() {
        return html;
    }

    @Override
    public String getAuthorName() {
        return authorName;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public long getCacheAge() {
        return cacheAge;
    }

    @Override
    public String getAuthorURL() {
        return authorURL;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OEmbedJSONImpl that = (OEmbedJSONImpl) o;

        if (cacheAge != that.cacheAge) return false;
        if (width != that.width) return false;
        if (authorName != null ? !authorName.equals(that.authorName) : that.authorName != null) return false;
        if (authorURL != null ? !authorURL.equals(that.authorURL) : that.authorURL != null) return false;
        if (html != null ? !html.equals(that.html) : that.html != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = html != null ? html.hashCode() : 0;
        result = 31 * result + (authorName != null ? authorName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (int) (cacheAge ^ (cacheAge >>> 32));
        result = 31 * result + (authorURL != null ? authorURL.hashCode() : 0);
        result = 31 * result + width;
        return result;
    }

    @Override
    public String toString() {
        return "OEmbedJSONImpl{" +
                "html='" + html + '\'' +
                ", authorName='" + authorName + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", cacheAge=" + cacheAge +
                ", authorURL='" + authorURL + '\'' +
                ", width=" + width +
                '}';
    }
}
