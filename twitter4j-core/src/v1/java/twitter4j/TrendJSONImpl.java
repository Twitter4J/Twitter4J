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

import twitter4j.v1.Trend;

import java.util.Objects;

/**
 * A data class representing Trend.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.2
 */
/*package*/ final class TrendJSONImpl implements Trend, java.io.Serializable {
    private static final long serialVersionUID = -4353426776065521132L;
    private final String name;
    private final String url;
    private final String query;
    private final int tweetVolume;

    /*package*/ TrendJSONImpl(JSONObject json, boolean storeJSON) {
        this.name = ParseUtil.getRawString("name", json);
        this.url = ParseUtil.getRawString("url", json);
        this.query = ParseUtil.getRawString("query", json);
        this.tweetVolume = ParseUtil.getInt("tweet_volume", json);
        if (storeJSON) {
            TwitterObjectFactory.registerJSONObject(this, json);
        }
    }

    /*package*/ TrendJSONImpl(JSONObject json) {
        this(json, false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public int getTweetVolume() {
        return tweetVolume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrendJSONImpl trendJSON = (TrendJSONImpl) o;
        return tweetVolume == trendJSON.tweetVolume && Objects.equals(name, trendJSON.name) && Objects.equals(url, trendJSON.url) && Objects.equals(query, trendJSON.query);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + tweetVolume;
        return result;
    }

    @Override
    public String toString() {
        return "TrendJSONImpl{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", query='" + query + '\'' +
                ", tweet_volume=" + tweetVolume +
                '}';
    }
}
