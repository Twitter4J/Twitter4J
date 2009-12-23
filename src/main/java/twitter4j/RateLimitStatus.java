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
import twitter4j.org.json.JSONObject;

import java.util.Date;
import static twitter4j.ParseUtil.*;
/**
 * A data class representing Twitter REST API's rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/Rate-limiting">Twitter API Wiki / Rate limiting</a>
 */
public class RateLimitStatus implements java.io.Serializable{
    private int remainingHits;
    private int hourlyLimit;
    private int resetTimeInSeconds;
    private Date resetTime;
    private static final long serialVersionUID = 753839064833831619L;

    // disabling the default constructor
    private RateLimitStatus() {
        throw new AssertionError();
    }
    private RateLimitStatus(int hourlyLimit, int remainingHits, int resetTimeInSeconds, Date resetTime){
        this.hourlyLimit = hourlyLimit;
        this.remainingHits = remainingHits;
        this.resetTimeInSeconds = resetTimeInSeconds;
        this.resetTime = resetTime;
    }

    static RateLimitStatus createFromJSONResponse(Response res) throws TwitterException {
        JSONObject json = res.asJSONObject();
        return new RateLimitStatus(getInt("hourly_limit", json),
                getInt("remaining_hits", json),
                getInt("reset_time_in_seconds", json),
                getDate("reset_time", json, "EEE MMM d HH:mm:ss Z yyyy"));
    }

    static RateLimitStatus createFromResponseHeader(Response res) {
        int remainingHits;//"X-RateLimit-Remaining"
        int hourlyLimit;//"X-RateLimit-Limit"
        int resetTimeInSeconds;//not included in the response header. Need to be calculated.
        Date resetTime;//new Date("X-RateLimit-Reset")


        String limit = res.getResponseHeader("X-RateLimit-Limit");
        if(null != limit){
            hourlyLimit = Integer.parseInt(limit);
        }else{
            return null;
        }
        String remaining = res.getResponseHeader("X-RateLimit-Remaining");
        if(null != remaining){
            remainingHits = Integer.parseInt(remaining);
        }else{
            return null;
        }
        String reset = res.getResponseHeader("X-RateLimit-Reset");
        if(null != reset){
            long longReset =  Long.parseLong(reset) * 1000;
            resetTime = new Date(longReset);
            resetTimeInSeconds =  (int)(longReset - System.currentTimeMillis()) / 1000;
        }else{
            return null;
        }
        return new RateLimitStatus(hourlyLimit, remainingHits, resetTimeInSeconds, resetTime);
    }

    /**
     * Returns the remaining number of API requests available.<br>
     * This value is identical to the &quot;X-RateLimit-Remaining&quot; response header.
     * @return the remaining number of API requests available
     */
    public int getRemainingHits() {
        return remainingHits;
    }

    /**
     * Returns the current limit in effect<br>
     * This value is identical to the &quot;X-RateLimit-Limit&quot; response header.
     * @return the current limit in effect
     */
    public int getHourlyLimit() {
        return hourlyLimit;
    }

    /**
     * Returns the seconds the current rate limiting period ends.<br>
     * @return the seconds the current rate limiting period ends
     * @since Twitter4J 2.0.9
     */
    public int getResetTimeInSeconds() {
        return resetTimeInSeconds;
    }

    /**
     * Returns the time the current rate limiting period ends.<br>
     * This value is a java.util.Date-typed variation of the &quot;X-RateLimit-Reset&quot; response header.
     * @return the time the current rate limiting period ends
     * @since Twitter4J 2.0.9
     */
    public Date getResetTime() {
        return resetTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitStatus)) return false;

        RateLimitStatus that = (RateLimitStatus) o;

        if (hourlyLimit != that.hourlyLimit) return false;
        if (remainingHits != that.remainingHits) return false;
        if (resetTimeInSeconds != that.resetTimeInSeconds) return false;
        if (!resetTime.equals(that.resetTime)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = remainingHits;
        result = 31 * result + hourlyLimit;
        result = 31 * result + resetTimeInSeconds;
        result = 31 * result + resetTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RateLimitStatus{" +
                "remainingHits=" + remainingHits +
                ", hourlyLimit=" + hourlyLimit +
                ", resetTimeInSeconds=" + resetTimeInSeconds +
                ", resetTime=" + resetTime +
                '}';
    }
}
