/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j;

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
    private static final long serialVersionUID = -6703617743623288566L;

    CategoryJSONImpl(String name, String slug){
        this.name = name;
        this.slug = slug;
    }

    public static ResponseList<Category> createCategoriesList(HttpResponse res) throws TwitterException {
        return createCategoriesList(res.asJSONArray(), res);
    }

    public static ResponseList<Category> createCategoriesList(JSONArray array, HttpResponse res) throws TwitterException {
        try {
            ResponseList<Category> categories =
                    new ResponseListImpl<Category>(array.length(), res);
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                categories.add(new CategoryJSONImpl(json.getString("name"), json.getString("slug")));
            }
            return categories;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryJSONImpl that = (CategoryJSONImpl) o;

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
        return result;
    }

    @Override
    public String toString() {
        return "CategoryJSONImpl{" +
                "name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
