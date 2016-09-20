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

import java.util.Date;

/**
 * A data class representing a Saved Search
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.8
 */
/*package*/ final class SavedSearchJSONImpl extends TwitterResponseImpl implements SavedSearch {

    private static final long serialVersionUID = -2281949861485441692L;
    private Date createdAt;
    private String query;
    private int position;
    private String name;
    private long id;

    /*package*/ SavedSearchJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.registerJSONObject(this, json);
        }
    }

    /*package*/ SavedSearchJSONImpl(JSONObject savedSearch) throws TwitterException {
        init(savedSearch);
    }

    /*package*/
    static ResponseList<SavedSearch> createSavedSearchList(HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
        }
        JSONArray json = res.asJSONArray();
        ResponseList<SavedSearch> savedSearches;
        try {
            savedSearches = new ResponseListImpl<SavedSearch>(json.length(), res);
            for (int i = 0; i < json.length(); i++) {
                JSONObject savedSearchesJSON = json.getJSONObject(i);
                SavedSearch savedSearch = new SavedSearchJSONImpl(savedSearchesJSON);
                savedSearches.add(savedSearch);
                if (conf.isJSONStoreEnabled()) {
                    TwitterObjectFactory.registerJSONObject(savedSearch, savedSearchesJSON);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.registerJSONObject(savedSearches, json);
            }
            return savedSearches;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + res.asString(), jsone);
        }
    }

    private void init(JSONObject savedSearch) throws TwitterException {
        createdAt = ParseUtil.getDate("created_at", savedSearch, "EEE MMM dd HH:mm:ss z yyyy");
        query = ParseUtil.getUnescapedString("query", savedSearch);
        position = ParseUtil.getInt("position", savedSearch);
        name = ParseUtil.getUnescapedString("name", savedSearch);
        id = ParseUtil.getLong("id", savedSearch);
    }

    @Override
    public int compareTo(SavedSearch that) {
        return (int)(this.id - that.getId());
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavedSearch)) return false;

        SavedSearch that = (SavedSearch) o;

        if (id != that.getId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = createdAt.hashCode();
        result = 31 * result + query.hashCode();
        result = 31 * result + position;
        result = 31 * result + name.hashCode();
        result = 31 * result + (int)id;
        return result;
    }

    @Override
    public String toString() {
        return "SavedSearchJSONImpl{" +
                "createdAt=" + createdAt +
                ", query='" + query + '\'' +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
