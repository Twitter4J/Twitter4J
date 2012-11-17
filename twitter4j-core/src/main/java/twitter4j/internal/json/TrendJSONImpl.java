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

import twitter4j.Trend;
import twitter4j.internal.org.json.JSONObject;

import static twitter4j.internal.json.z_T4JInternalParseUtil.getRawString;

/**
 * A data class representing Trend.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.2
 */
/*package*/ final class TrendJSONImpl implements Trend, java.io.Serializable {
    private String name;
    private String url = null;
    private String query = null;
    private static final long serialVersionUID = 1925956704460743946L;

    /*package*/ TrendJSONImpl(JSONObject json, boolean storeJSON) {
        this.name = getRawString("name", json);
        this.url = getRawString("url", json);
        this.query = getRawString("query", json);
        if (storeJSON) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    /*package*/ TrendJSONImpl(JSONObject json) {
        this(json, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return getURL();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trend)) return false;

        Trend trend = (Trend) o;

        if (!name.equals(trend.getName())) return false;
        if (query != null ? !query.equals(trend.getQuery()) : trend.getQuery() != null)
            return false;
        if (url != null ? !url.equals(trend.getURL()) : trend.getURL() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (query != null ? query.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrendJSONImpl{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
