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
 * A data class representing Twitter rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class RateLimitStatus {
    private int remainingHits;//"X-RateLimit-Remaining"
    private int hourlyLimit;//"X-RateLimit-Limit"
    private int resetTimeInSeconds;
    private Date resetTime;//new Date("X-RateLimit-Reset")
    private static final long serialVersionUID = 933996804168952707L;

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
            long longReset =  Long.parseLong(reset);
            resetTime = new Date(longReset);
            resetTimeInSeconds =  (int)(longReset - System.currentTimeMillis()) / 1000;
        }else{
            return null;
        }
        return new RateLimitStatus(hourlyLimit, remainingHits, resetTimeInSeconds, resetTime);
    }

    public int getRemainingHits() {
        return remainingHits;
    }

    public int getHourlyLimit() {
        return hourlyLimit;
    }

    public int getResetTimeInSeconds() {
        return resetTimeInSeconds;
    }

    /**
     * @since Twitter4J 2.0.9
     */
    public Date getResetTime() {
        return resetTime;
    }
}
