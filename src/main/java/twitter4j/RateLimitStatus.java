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

import org.w3c.dom.Element;
import twitter4j.http.Response;

import java.util.Date;

/**
 * A data class representing Twitter rate limit status
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class RateLimitStatus extends TwitterResponse {
    private int remainingHits;
    private int hourlyLimit;
    private int resetTimeInSeconds;
    private Date resetTime;
    private static final long serialVersionUID = 933996804168952707L;

    /* package */ RateLimitStatus(Response res) throws TwitterException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        remainingHits = getChildInt("remaining-hits", elem);
        hourlyLimit = getChildInt("hourly-limit", elem);
        resetTimeInSeconds = getChildInt("reset-time-in-seconds", elem);
        resetTime = getChildDate("reset-time", elem, "yyyy-M-d'T'HH:mm:ss+00:00");
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
     *
     * @deprecated use getResetTime() instead
     */
    public Date getDateTime() {
        return resetTime;
    }

    /**
     * @since Twitter4J 2.0.9
     */
    public Date getResetTime() {
        return resetTime;
    }
}
