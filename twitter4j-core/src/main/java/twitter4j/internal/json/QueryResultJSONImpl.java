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

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static twitter4j.internal.json.z_T4JInternalParseUtil.*;

/**
 * A data class representing search API response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class QueryResultJSONImpl extends TwitterResponseImpl implements QueryResult, java.io.Serializable {

    private static final long serialVersionUID = -6781654399437121238L;
    private long sinceId;
    private long maxId;
    private String refreshUrl;
    private int count;
    private double completedIn;
    private String query;
    private List<Status> tweets;
    private String nextResults;

    // private static factory method to instantiate Query class with "next_page"
    // http://jira.twitter4j.org/browse/TFJ-549
    static Method queryFactoryMethod;

    static {
        Method[] methods = Query.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("createWithNextPageQuery")) {
                queryFactoryMethod = method;
                queryFactoryMethod.setAccessible(true);
                break;
            }
        }
        if (queryFactoryMethod == null) {
            throw new ExceptionInInitializerError(new NoSuchMethodException("twitter4j.Query.createWithNextPageQuery(java.lang.String)"));
        }
    }

    /*package*/ QueryResultJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        try {
            JSONObject searchMetaData = json.getJSONObject("search_metadata");
            completedIn = getDouble("completed_in", searchMetaData);
            count = getInt("count", searchMetaData);
            maxId = getLong("max_id", searchMetaData);
            nextResults = searchMetaData.has("next_results") ? searchMetaData.getString("next_results") : null;
            query = getURLDecodedString("query", searchMetaData);
            refreshUrl = getUnescapedString("refresh_url", searchMetaData);
            sinceId = getLong("since_id", searchMetaData);

            JSONArray array = json.getJSONArray("statuses");
            tweets = new ArrayList<Status>(array.length());
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSinceId() {
        return sinceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMaxId() {
        return maxId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRefreshUrl() {
        return getRefreshURL();
    }

    @Override
    public String getRefreshURL() {
        return refreshUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getCompletedIn() {
        return completedIn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getQuery() {
        return query;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Status> getTweets() {
        return tweets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query nextQuery() {
        if (nextResults == null) {
            return null;
        }
        try {
            return (Query) queryFactoryMethod.invoke(null, new String[]{nextResults});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
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
        if (refreshUrl != null ? !refreshUrl.equals(that.getRefreshUrl()) : that.getRefreshUrl() != null)
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
