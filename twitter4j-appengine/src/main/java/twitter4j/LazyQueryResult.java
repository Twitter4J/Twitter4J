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

import java.util.List;

/**
 * A data class representing search API response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
final class LazyQueryResult implements twitter4j.QueryResult {
    private static final long serialVersionUID = 2570528566436339617L;
    private final HttpResponse res;
    private final ObjectFactory factory;
    private QueryResult target = null;
    private final Query query;

    LazyQueryResult(HttpResponse res, ObjectFactory factory, Query query) {
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

    @Override
    public long getSinceId() {
        return getTarget().getSinceId();
    }


    @Override
    public long getMaxId() {
        return getTarget().getMaxId();
    }


    public String getRefreshUrl() {
        return getRefreshURL();
    }

    @Override
    public String getRefreshURL() {
        return getTarget().getRefreshURL();
    }

    @Override
    public int getCount() {
        return getTarget().getCount();
    }


    @Override
    public double getCompletedIn() {
        return getTarget().getCompletedIn();
    }

    @Override
    public String getQuery() {
        return getTarget().getQuery();
    }


    @Override
    public List<Status> getTweets() {
        return getTarget().getTweets();
    }

    @Override
    public Query nextQuery() {
        return getTarget().nextQuery();
    }

    @Override
    public boolean hasNext() {
        return getTarget().hasNext();
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

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    @Override
    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }
}
