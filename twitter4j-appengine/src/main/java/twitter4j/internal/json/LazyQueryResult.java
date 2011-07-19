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

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import twitter4j.TwitterRuntimeException;

import java.util.List;

/**
 * A data class representing search API response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
final class LazyQueryResult implements twitter4j.QueryResult {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private QueryResult target = null;
    private Query query;

    LazyQueryResult(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory, Query query) {
        this.res = res;
        this.factory = factory;
        this.query = query;
    }

    private QueryResult getTarget() {
        if (target == null) {
            try {
                target = factory.createQueryResult(res, query);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    public long getSinceId() {
        return getTarget().getSinceId();
    }


    public long getMaxId() {
        return getTarget().getMaxId();
    }


    public String getRefreshUrl() {
        return getTarget().getRefreshUrl();
    }


    public int getResultsPerPage() {
        return getTarget().getResultsPerPage();
    }


    public String getWarning() {
        return getTarget().getWarning();
    }


    public double getCompletedIn() {
        return getTarget().getCompletedIn();
    }


    public int getPage() {
        return getTarget().getPage();
    }


    public String getQuery() {
        return getTarget().getQuery();
    }


    public List<Tweet> getTweets() {
        return getTarget().getTweets();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryResult)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyQueryResult{" +
                "target=" + getTarget() +
                "}";
    }
}
