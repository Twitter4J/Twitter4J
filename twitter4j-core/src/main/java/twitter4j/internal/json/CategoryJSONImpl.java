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

import twitter4j.Category;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
final class CategoryJSONImpl implements Category, java.io.Serializable {

    private String name;
    private String slug;
    private int size;
    private static final long serialVersionUID = -6703617743623288566L;

    CategoryJSONImpl(JSONObject json) throws JSONException {
        init(json);
    }

    void init(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        this.slug = json.getString("slug");
        this.size = z_T4JInternalParseUtil.getInt("size", json);
    }

    static ResponseList<Category> createCategoriesList(HttpResponse res, Configuration conf) throws TwitterException {
        return createCategoriesList(res.asJSONArray(), res, conf);
    }

    static ResponseList<Category> createCategoriesList(JSONArray array, HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            ResponseList<Category> categories =
                    new ResponseListImpl<Category>(array.length(), res);
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                Category category = new CategoryJSONImpl(json);
                categories.add(category);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(category, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(categories, array);
            }
            return categories;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSlug() {
        return slug;
    }

    /**
     * @return
     * @since Twitter4J 2.1.9
     */
    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryJSONImpl that = (CategoryJSONImpl) o;

        if (size != that.size) return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (slug != null ? !slug.equals(that.slug) : that.slug != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        return "CategoryJSONImpl{" +
                "name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", size=" + size +
                '}';
    }
}
