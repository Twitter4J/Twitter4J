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

import twitter4j.http.HttpResponse;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import static twitter4j.ParseUtil.*;
/**
 * A data class representing search API response
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class QueryResultJSONImpl implements QueryResult, java.io.Serializable {

    private long sinceId;
    private long maxId;
    private String refreshUrl;
    private int resultsPerPage;
    private String warning;
    private double completedIn;
    private int page;
    private String query;
    private List<Tweet> tweets;
    private static final long serialVersionUID = -9059136565234613286L;

    /*package*/ QueryResultJSONImpl(HttpResponse res) throws TwitterException {
        JSONObject json = res.asJSONObject();
        try {
            sinceId = getLong("since_id", json);
            maxId = getLong("max_id", json);
            refreshUrl = getUnescapedString("refresh_url", json);

            resultsPerPage = getInt("results_per_page", json);
            warning = getRawString("warning", json);
            completedIn = getDouble("completed_in", json);
            page = getInt("page", json);
            query = getURLDecodedString("query", json);
            JSONArray array = json.getJSONArray("results");
            tweets = new ArrayList<Tweet>(array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject tweet = array.getJSONObject(i);
                tweets.add(new TweetJSONImpl(tweet));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }
    /*package*/ QueryResultJSONImpl(Query query) throws TwitterException {
        super();
        sinceId = query.getSinceId();
        resultsPerPage = query.getRpp();
        page = query.getPage();
        tweets = new ArrayList<Tweet>(0);
    }

    /**
     * {@inheritDoc}
     */
    public long getSinceId() {
        return sinceId;
    }

    /**
     * {@inheritDoc}
     */
    public long getMaxId() {
        return maxId;
    }

    /**
     * {@inheritDoc}
     */
    public String getRefreshUrl() {
        return refreshUrl;
    }

    /**
     * {@inheritDoc}
     */
    public int getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    public String getWarning() {
        return warning;
    }

    /**
     * {@inheritDoc}
     */
    public double getCompletedIn() {
        return completedIn;
    }

    /**
     * {@inheritDoc}
     */
    public int getPage() {
        return page;
    }

    /**
     * {@inheritDoc}
     */
    public String getQuery() {
        return query;
    }

    /**
     * {@inheritDoc}
     */
    public List<Tweet> getTweets() {
        return tweets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryResult that = (QueryResult) o;

        if (Double.compare(that.getCompletedIn(), completedIn) != 0) return false;
        if (maxId != that.getMaxId()) return false;
        if (page != that.getPage()) return false;
        if (resultsPerPage != that.getResultsPerPage()) return false;
        if (sinceId != that.getSinceId()) return false;
        if (!query.equals(that.getQuery())) return false;
        if (refreshUrl != null ? !refreshUrl.equals(that.getRefreshUrl()) : that.getRefreshUrl() != null)
            return false;
        if (tweets != null ? !tweets.equals(that.getTweets()) : that.getTweets() != null)
            return false;
        if (warning != null ? !warning.equals(that.getWarning()) : that.getWarning() != null)
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
        result = 31 * result + resultsPerPage;
        result = 31 * result + (warning != null ? warning.hashCode() : 0);
        temp = completedIn != +0.0d ? Double.doubleToLongBits(completedIn) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + page;
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
                ", resultsPerPage=" + resultsPerPage +
                ", warning='" + warning + '\'' +
                ", completedIn=" + completedIn +
                ", page=" + page +
                ", query='" + query + '\'' +
                ", tweets=" + tweets +
                '}';
    }
}
