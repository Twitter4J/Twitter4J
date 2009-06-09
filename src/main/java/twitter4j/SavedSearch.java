/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import twitter4j.http.Response;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A data class representing a Saved Search
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.8
 */
public class SavedSearch extends TwitterResponse {
    private Date createdAt;
    private String query;
    private int position;
    private String name;
    private int id;
    private static final long serialVersionUID = 3083819860391598212L;

    /*package*/ SavedSearch(Response res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }

    /*package*/ SavedSearch(Response res, JSONObject json) throws TwitterException {
        super(res);
        init(json);
    }

    /*package*/ SavedSearch(JSONObject savedSearch) throws TwitterException {
        init(savedSearch);
    }

    /*package*/ static List<SavedSearch> constructSavedSearches(Response res) throws TwitterException {
            JSONArray json = res.asJSONArray();
            List<SavedSearch> savedSearches;
            try {
                savedSearches = new ArrayList<SavedSearch>(json.length());
                for(int i=0;i<json.length();i++){
                    savedSearches.add(new SavedSearch(res,json.getJSONObject(i)));
                }
                return savedSearches;
            } catch (JSONException jsone) {
                throw new TwitterException(jsone.getMessage() + ":" + res.asString(), jsone);
            }
        }

    private void init(JSONObject savedSearch) throws TwitterException {
        try {
            createdAt = parseDate(savedSearch.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
            query = getString("query", savedSearch, true);
            position = getInt("position", savedSearch);
            name = getString("name", savedSearch, true);
            id = getInt("id", savedSearch);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + savedSearch.toString(), jsone);
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getQuery() {
        return query;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavedSearch)) return false;

        SavedSearch that = (SavedSearch) o;

        if (id != that.id) return false;
        if (position != that.position) return false;
        if (!createdAt.equals(that.createdAt)) return false;
        if (!name.equals(that.name)) return false;
        if (!query.equals(that.query)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = createdAt.hashCode();
        result = 31 * result + query.hashCode();
        result = 31 * result + position;
        result = 31 * result + name.hashCode();
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "SavedSearch{" +
                "createdAt=" + createdAt +
                ", query='" + query + '\'' +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
