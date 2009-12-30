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
/*package*/ class RateLimitStatusJSONImpl implements RateLimitStatus, java.io.Serializable {

    private int remainingHits;
    private int hourlyLimit;
    private int resetTimeInSeconds;
    private int secondsUntilReset;
    private Date resetTime;
    private static final long serialVersionUID = 753839064833831619L;
    /*package*/ RateLimitStatusJSONImpl() {
        // Just for protobuf support
        // Currently this constructor is never used in twitter4j artifact.
    }

    private RateLimitStatusJSONImpl(int hourlyLimit, int remainingHits, Date resetTime) {
        this.hourlyLimit = hourlyLimit;
        this.remainingHits = remainingHits;
        this.resetTime = resetTime;
        this.resetTimeInSeconds = (int)(resetTime.getTime()/1000);
        this.secondsUntilReset = (int)((resetTime.getTime() - System.currentTimeMillis())/1000);
    }

    static RateLimitStatus createFromJSONResponse(Response res) throws TwitterException {
        JSONObject json = res.asJSONObject();
        return new RateLimitStatusJSONImpl(getInt("hourly_limit", json),
                getInt("remaining_hits", json),
                new Date( ((long)getInt("reset_time_in_seconds", json)) * 1000 )
                );
    }

    static RateLimitStatus createFromResponseHeader(Response res) {
        int remainingHits;//"X-RateLimit-Remaining"
        int hourlyLimit;//"X-RateLimit-Limit"
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
            resetTime = new Date(Long.parseLong(reset) * 1000);
        }else{
            return null;
        }
        return new RateLimitStatusJSONImpl(hourlyLimit, remainingHits, resetTime);
    }

    /**
     * {@inheritDoc}
     */
    public int getRemainingHits() {
        return remainingHits;
    }

    public void setRemainingHits(int remainingHits) {
        this.remainingHits = remainingHits;
    }

    /**
     * {@inheritDoc}
     */
    public int getHourlyLimit() {
        return hourlyLimit;
    }

    public void setHourlyLimit(int hourlyLimit) {
        this.hourlyLimit = hourlyLimit;
    }

    /**
     * {@inheritDoc}
     */
    public int getResetTimeInSeconds() {
        return resetTimeInSeconds;
    }

    public void setResetTimeInSeconds(int resetTimeInSeconds) {
        this.resetTimeInSeconds = resetTimeInSeconds;
    }

    /**
     * {@inheritDoc}
     */
    public int getSecondsUntilReset() {
        return secondsUntilReset;
    }

    public void setSecondsUntilReset(int secondsUntilReset) {
        this.secondsUntilReset = secondsUntilReset;
    }

    /**
     * {@inheritDoc}
     */
    public Date getResetTime() {
        return resetTime;
    }

    public void setResetTime(Date resetTime) {
        this.resetTime = resetTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitStatus)) return false;

        RateLimitStatus that = (RateLimitStatus) o;

        if (hourlyLimit != that.getHourlyLimit()) return false;
        if (remainingHits != that.getRemainingHits()) return false;
        if (resetTimeInSeconds != that.getResetTimeInSeconds()) return false;
        if (!resetTime.equals(that.getResetTime())) return false;

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
        return "RateLimitStatusJSONImpl{" +
                "remainingHits=" + remainingHits +
                ", hourlyLimit=" + hourlyLimit +
                ", secondsUntilReset=" + secondsUntilReset +
                ", resetTime=" + resetTime +
                '}';
    }
}
