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

import java.util.ArrayList;
import java.util.List;

/**
 * A data class representing search API response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class QueryResultJSONImpl extends TwitterResponseImpl implements QueryResult, java.io.Serializable {

    private static final long serialVersionUID = -5359566235429947156L;
    private long sinceId;
    private long maxId;
    private String refreshUrl;
    private int count;
    private double completedIn;
    private String query;
    private List<Status> tweets;
    private String nextResults;

    /*package*/ QueryResultJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        try {
            JSONObject searchMetaData = json.getJSONObject("search_metadata");
            completedIn = ParseUtil.getDouble("completed_in", searchMetaData);
            count = ParseUtil.getInt("count", searchMetaData);
            maxId = ParseUtil.getLong("max_id", searchMetaData);
            nextResults = searchMetaData.has("next_results") ? searchMetaData.getString("next_results") : null;
            query = ParseUtil.getURLDecodedString("query", searchMetaData);
            refreshUrl = ParseUtil.getUnescapedString("refresh_url", searchMetaData);
            sinceId = ParseUtil.getLong("since_id", searchMetaData);

            JSONArray array = json.getJSONArray("statuses");
            tweets = new ArrayList<Status>(array.length());
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject tweet = array.getJSONObject(i);
                tweets.add(new StatusJSONImpl(tweet, conf));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /*package*/ QueryResultJSONImpl(Query query) {
        super();
        sinceId = query.getSinceId();
        count = query.getCount();
        tweets = new ArrayList<Status>(0);
    }

    @Override
    public long getSinceId() {
        return sinceId;
    }

    @Override
    public long getMaxId() {
        return maxId;
    }

    @Override
    public String getRefreshURL() {
        return refreshUrl;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getCompletedIn() {
        return completedIn;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public List<Status> getTweets() {
        return tweets;
    }

    @Override
    public Query nextQuery() {
        if (nextResults == null) {
            return null;
        }
        return Query.createWithNextPageQuery(nextResults);
    }

    @Override
    public boolean hasNext() {
        return nextResults != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryResult that = (QueryResult) o;

        if (Double.compare(that.getCompletedIn(), completedIn) != 0)
            return false;
        if (maxId != that.getMaxId()) return false;
        if (count != that.getCount()) return false;
        if (sinceId != that.getSinceId()) return false;
        if (!query.equals(that.getQuery())) return false;
        if (refreshUrl != null ? !refreshUrl.equals(that.getRefreshURL()) : that.getRefreshURL() != null)
            return false;
        if (tweets != null ? !tweets.equals(that.getTweets()) : that.getTweets() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (sinceId ^ (sinceId >>> 32));
        result = 31 * result + (int) (maxId ^ (maxId >>> 32));
        result = 31 * result + (refreshUrl != null ? refreshUrl.hashCode() : 0);
        result = 31 * result + count;
        temp = completedIn != +0.0d ? Double.doubleToLongBits(completedIn) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + query.hashCode();
        result = 31 * result + (tweets != null ? tweets.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QueryResultJSONImpl{" +
                "sinceId=" + sinceId +
                ", maxId=" + maxId +
                ", refreshUrl='" + refreshUrl + '\'' +
                ", count=" + count +
                ", completedIn=" + completedIn +
                ", query='" + query + '\'' +
                ", tweets=" + tweets +
                '}';
    }
}
