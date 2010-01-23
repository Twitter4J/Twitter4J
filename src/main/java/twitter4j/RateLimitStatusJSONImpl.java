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
import twitter4j.org.json.JSONObject;

import java.util.Date;

import static twitter4j.ParseUtil.*;

/**
 * A data class representing Twitter REST API's rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/Rate-limiting">Twitter API Wiki / Rate limiting</a>
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

    static RateLimitStatus createFromJSONResponse(HttpResponse res) throws TwitterException {
        JSONObject json = res.asJSONObject();
        return new RateLimitStatusJSONImpl(getInt("hourly_limit", json),
                getInt("remaining_hits", json),
                getInt("reset_time_in_seconds", json),
                getDate("reset_time", json, "EEE MMM d HH:mm:ss Z yyyy"));
    }

    static RateLimitStatus createFromResponseHeader(HttpResponse res) {
        int remainingHits;//"X-RateLimit-Remaining"
        int hourlyLimit;//"X-RateLimit-Limit"
        int resetTimeInSeconds;//not included in the response header. Need to be calculated.
        Date resetTime;//new Date("X-RateLimit-Reset")

        String limit = res.getResponseHeader("X-RateLimit-Limit");
        if (null != limit) {
            hourlyLimit = Integer.parseInt(limit);
        } else {
            return null;
        }
        String remaining = res.getResponseHeader("X-RateLimit-Remaining");
        if (null != remaining) {
            remainingHits = Integer.parseInt(remaining);
        } else {
            return null;
        }
        String reset = res.getResponseHeader("X-RateLimit-Reset");
        if (null != reset) {
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
