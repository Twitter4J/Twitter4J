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
import twitter4j.internal.org.json.JSONObject;

import java.util.Date;

import static twitter4j.internal.util.z_T4JInternalParseUtil.getDate;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getInt;

/**
 * A data class representing Twitter REST API's rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
 */
/*package*/ final class RateLimitStatusJSONImpl implements RateLimitStatus, java.io.Serializable {

    private int remainingHits;
    private int hourlyLimit;
    private int resetTimeInSeconds;
    private int secondsUntilReset;
    private Date resetTime;
    private static final long serialVersionUID = 832355052293658614L;

    private RateLimitStatusJSONImpl(int hourlyLimit, int remainingHits, int resetTimeInSeconds, Date resetTime) {
        this.hourlyLimit = hourlyLimit;
        this.remainingHits = remainingHits;
        this.resetTime = resetTime;
        this.resetTimeInSeconds = resetTimeInSeconds;
        this.secondsUntilReset = (int) ((resetTime.getTime() - System.currentTimeMillis()) / 1000);
    }

    RateLimitStatusJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    RateLimitStatusJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    void init(JSONObject json) throws TwitterException {
        this.hourlyLimit = getInt("hourly_limit", json);
        this.remainingHits = getInt("remaining_hits", json);
        this.resetTime = getDate("reset_time", json, "EEE MMM d HH:mm:ss Z yyyy");
        this.resetTimeInSeconds = getInt("reset_time_in_seconds", json);
        this.secondsUntilReset = (int) ((resetTime.getTime() - System.currentTimeMillis()) / 1000);
    }

    static RateLimitStatus createFromResponseHeader(HttpResponse res) {
        if (null == res) {
            return null;
        }
        int remainingHits;//"X-RateLimit-Remaining"
        int hourlyLimit;//"X-RateLimit-Limit"
        int resetTimeInSeconds;//not included in the response header. Need to be calculated.
        Date resetTime;//new Date("X-RateLimit-Reset")

        String limit = res.getResponseHeader("X-RateLimit-Limit");
        if (limit != null) {
            hourlyLimit = Integer.parseInt(limit);
        } else {
            return null;
        }
        String remaining = res.getResponseHeader("X-RateLimit-Remaining");
        if (remaining != null) {
            remainingHits = Integer.parseInt(remaining);
        } else {
            return null;
        }
        String reset = res.getResponseHeader("X-RateLimit-Reset");
        if (reset != null) {
            long longReset = Long.parseLong(reset);
            resetTimeInSeconds = (int) (longReset / 1000);
            resetTime = new Date(longReset * 1000);
        } else {
            return null;
        }
        return new RateLimitStatusJSONImpl(hourlyLimit, remainingHits, resetTimeInSeconds, resetTime);
    }

    static RateLimitStatus createFeatureSpecificRateLimitStatusFromResponseHeader(HttpResponse res) {
        if (null == res) {
            return null;
        }
        int remainingHits;//"X-FeatureRateLimit-Remaining"
        int hourlyLimit;//"X-FeatureRateLimit-Limit"
        int resetTimeInSeconds;//not included in the response header. Need to be calculated.
        Date resetTime;//new Date("X-FeatureRateLimit-Reset")

        String limit = res.getResponseHeader("X-FeatureRateLimit-Limit");
        if (limit != null) {
            hourlyLimit = Integer.parseInt(limit);
        } else {
            return null;
        }
        String remaining = res.getResponseHeader("X-FeatureRateLimit-Remaining");
        if (remaining != null) {
            remainingHits = Integer.parseInt(remaining);
        } else {
            return null;
        }
        String reset = res.getResponseHeader("X-FeatureRateLimit-Reset");
        if (reset != null) {
            long longReset = Long.parseLong(reset);
            resetTimeInSeconds = (int) (longReset / 1000);
            resetTime = new Date(longReset * 1000);
        } else {
            return null;
        }
        return new RateLimitStatusJSONImpl(hourlyLimit, remainingHits, resetTimeInSeconds, resetTime);
    }

    /**
     * {@inheritDoc}
     */
    public int getRemainingHits() {
        return remainingHits;
    }

    /**
     * {@inheritDoc}
     */
    public int getHourlyLimit() {
        return hourlyLimit;
    }

    /**
     * {@inheritDoc}
     */
    public int getResetTimeInSeconds() {
        return resetTimeInSeconds;
    }

    /**
     * {@inheritDoc}
     */
    public int getSecondsUntilReset() {
        return secondsUntilReset;
    }

    /**
     * {@inheritDoc}
     */
    public Date getResetTime() {
        return resetTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitStatusJSONImpl)) return false;

        RateLimitStatusJSONImpl that = (RateLimitStatusJSONImpl) o;

        if (hourlyLimit != that.hourlyLimit) return false;
        if (remainingHits != that.remainingHits) return false;
        if (resetTimeInSeconds != that.resetTimeInSeconds) return false;
        if (secondsUntilReset != that.secondsUntilReset) return false;
        if (resetTime != null ? !resetTime.equals(that.resetTime) : that.resetTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = remainingHits;
        result = 31 * result + hourlyLimit;
        result = 31 * result + resetTimeInSeconds;
        result = 31 * result + secondsUntilReset;
        result = 31 * result + (resetTime != null ? resetTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RateLimitStatusJSONImpl{" +
                "remainingHits=" + remainingHits +
                ", hourlyLimit=" + hourlyLimit +
                ", resetTimeInSeconds=" + resetTimeInSeconds +
                ", secondsUntilReset=" + secondsUntilReset +
                ", resetTime=" + resetTime +
                '}';
    }
}
