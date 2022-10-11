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

import org.jetbrains.annotations.NotNull;
import twitter4j.v1.GeoLocation;
import twitter4j.v1.Query;
import twitter4j.v1.QueryResult;
import twitter4j.v1.Status;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A data class representing search API response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class QueryResultJSONImpl extends TwitterResponseImpl implements QueryResult, java.io.Serializable {

    private static final long serialVersionUID = -5359566235429947156L;
    private final long sinceId;
    private long maxId;
    private String refreshUrl;
    private final int count;
    private double completedIn;
    private String query;
    private final List<Status> tweets;
    private String nextResults;

    /*package*/ QueryResultJSONImpl(HttpResponse res, boolean jsonStoreEnabled) throws TwitterException {
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
            tweets = new ArrayList<>(array.length());
            if (jsonStoreEnabled) {
                TwitterObjectFactory.clearThreadLocalMap();
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject tweet = array.getJSONObject(i);
                tweets.add(new StatusJSONImpl(tweet, jsonStoreEnabled));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json, jsone);
        }
    }

    /*package*/ QueryResultJSONImpl(Query query) {
        super();
        sinceId = query.sinceId;
        count = query.count;
        tweets = new ArrayList<>(0);
    }

    /* package */
    static Query createWithNextPageQuery(@NotNull String nextPageQuery) {

        String nextPageParameters = nextPageQuery.substring(1);

        Map<String, String> params = new LinkedHashMap<>();
        for (HttpParameter param : HttpParameter.decodeParameters(nextPageParameters)) {
            // Yes, we'll overwrite duplicate parameters, but we should not
            // get duplicate parameters from this endpoint.
            params.put(param.getName(), param.getValue());
        }

        Query query = Query.of(params.getOrDefault("q", ""));
        if (params.containsKey("lang")) {
            query = query.lang(params.get("lang"));
        }
        if (params.containsKey("locale")) {
            query = query.locale(params.get("locale"));
        }
        if (params.containsKey("max_id")) {
            query = query.maxId(Long.parseLong(params.get("max_id")));
        }
        if (params.containsKey("count")) {
            query = query.count(Integer.parseInt(params.get("count")));
        }
        if (params.containsKey("geocode")) {
            String[] parts = params.get("geocode").split(",");
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);

            double radius;
            Query.Unit unit = null;
            String radiusstr = parts[2];

            for (Query.Unit value : Query.Unit.values())
                if (radiusstr.endsWith(value.name())) {
                    radius = Double.parseDouble(radiusstr.substring(0, radiusstr.length() - 2));
                    unit = value;
                    query = query.geoCode(GeoLocation.of(latitude, longitude), radius, unit);
                    break;
                }
            if (unit == null) {
                throw new IllegalArgumentException("unrecognized geocode radius: " + radiusstr);
            }

        }
        Query.ResultType resultType;
        if (params.containsKey("result_type")) {
            resultType = Query.ResultType.valueOf(params.get("result_type"));
            query = query.resultType(resultType);
        }
        return query;
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
        return createWithNextPageQuery(nextResults);
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
        return tweets != null ? tweets.equals(that.getTweets()) : that.getTweets() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (sinceId ^ (sinceId >>> 32));
        result = 31 * result + (int) (maxId ^ (maxId >>> 32));
        result = 31 * result + (refreshUrl != null ? refreshUrl.hashCode() : 0);
        result = 31 * result + count;
        temp = completedIn != 0.0d ? Double.doubleToLongBits(completedIn) : 0L;
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
