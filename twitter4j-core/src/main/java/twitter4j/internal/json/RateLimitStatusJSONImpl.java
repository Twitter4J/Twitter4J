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

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.*;

import static twitter4j.internal.json.z_T4JInternalParseUtil.getDate;
import static twitter4j.internal.json.z_T4JInternalParseUtil.getInt;

/**
 * A data class representing Twitter REST API's rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
 */
/*package*/ final class RateLimitStatusJSONImpl implements RateLimitStatus, java.io.Serializable {

    private static final long serialVersionUID = 1625565652687304084L;
    private int remaining;
    private int limit;
    private int resetTimeInSeconds;
    private int secondsUntilReset;
    static Map<String,RateLimitStatus> createRateLimitStatuses(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = res.asJSONObject();
        Map<String, RateLimitStatus> map = createRateLimitStatuses(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(map, json);
        }
        return map;
    }

    static Map<String,RateLimitStatus> createRateLimitStatuses(JSONObject json) throws TwitterException {
        Map<String, RateLimitStatus> map = new HashMap<String, RateLimitStatus>();
        try {
            JSONObject resources = json.getJSONObject("resources");
            Iterator resourceKeys = resources.keys();
            while (resourceKeys.hasNext()) {
                JSONObject resource = resources.getJSONObject((String) resourceKeys.next());
                Iterator endpointKeys = resource.keys();
                while (endpointKeys.hasNext()) {
                    String endpoint = (String) endpointKeys.next();
                    JSONObject rateLimitStatusJSON = resource.getJSONObject(endpoint);
                    RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(rateLimitStatusJSON);
                    map.put(endpoint, rateLimitStatus);
                }
            }
            return Collections.unmodifiableMap(map);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    private RateLimitStatusJSONImpl(int limit, int remaining, int resetTimeInSeconds) {
        this.limit = limit;
        this.remaining = remaining;
        this.resetTimeInSeconds = resetTimeInSeconds;
        this.secondsUntilReset = (int) ((resetTimeInSeconds * 1000L - System.currentTimeMillis()) / 1000);
    }

    RateLimitStatusJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    void init(JSONObject json) throws TwitterException {
        this.limit = getInt("limit", json);
        this.remaining = getInt("remaining", json);
        this.resetTimeInSeconds = getInt("reset", json);
        this.secondsUntilReset = (int) ((resetTimeInSeconds * 1000L - System.currentTimeMillis()) / 1000);
    }

    static RateLimitStatus createFromResponseHeader(HttpResponse res) {
        if (null == res) {
            return null;
        }
        int remainingHits;//"X-Rate-Limit-Remaining"
        int limit;//"X-Rate-Limit-Limit"
        int resetTimeInSeconds;//not included in the response header. Need to be calculated.

        String strLimit = res.getResponseHeader("X-Rate-Limit-Limit");
        if (strLimit != null) {
            limit = Integer.parseInt(strLimit);
        } else {
            return null;
        }
        String remaining = res.getResponseHeader("X-Rate-Limit-Remaining");
        if (remaining != null) {
            remainingHits = Integer.parseInt(remaining);
        } else {
            return null;
        }
        String reset = res.getResponseHeader("X-Rate-Limit-Reset");
        if (reset != null) {
            long longReset = Long.parseLong(reset);
            resetTimeInSeconds = (int) longReset;
        } else {
            return null;
        }
        return new RateLimitStatusJSONImpl(limit, remainingHits, resetTimeInSeconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRemaining() {
        return remaining;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRemainingHits() {
        return getRemaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLimit() {
        return limit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResetTimeInSeconds() {
        return resetTimeInSeconds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSecondsUntilReset() {
        return secondsUntilReset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RateLimitStatusJSONImpl that = (RateLimitStatusJSONImpl) o;

        if (limit != that.limit) return false;
        if (remaining != that.remaining) return false;
        if (resetTimeInSeconds != that.resetTimeInSeconds) return false;
        if (secondsUntilReset != that.secondsUntilReset) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = remaining;
        result = 31 * result + limit;
        result = 31 * result + resetTimeInSeconds;
        result = 31 * result + secondsUntilReset;
        return result;
    }

    @Override
    public String toString() {
        return "RateLimitStatusJSONImpl{" +
                "remaining=" + remaining +
                ", limit=" + limit +
                ", resetTimeInSeconds=" + resetTimeInSeconds +
                ", secondsUntilReset=" + secondsUntilReset +
                '}';
    }

}
