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

import java.util.Date;

/**
 * A data interface representing Twitter REST API's rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/Rate-limiting">Twitter API Wiki / Rate limiting</a>
 */
public interface RateLimitStatus extends java.io.Serializable {
    /**
     * Returns the remaining number of API requests available.<br>
     * This value is identical to the &quot;X-RateLimit-Remaining&quot; response header.
     *
     * @return the remaining number of API requests available
     */
    int getRemainingHits();

    /**
     * Returns the current limit in effect<br>
     * This value is identical to the &quot;X-RateLimit-Limit&quot; response header.
     *
     * @return the current limit in effect
     */
    int getHourlyLimit();

    /**
     * Returns the seconds the current rate limiting period ends.<br>
     * This should be a same as getResetTime().getTime()/1000.
     *
     * @return the seconds the current rate limiting period ends
     * @since Twitter4J 2.0.9
     */
    int getResetTimeInSeconds();

    /**
     * Returns the amount of seconds until the current rate limiting period ends.<br>
     * This is a value provided/calculated only by Twitter4J for handiness and not a part of the twitter API spec.
     *
     * @return the amount of seconds until next rate limiting period
     * @since Twitter4J 2.1.0
     */
    int getSecondsUntilReset();


    /**
     * Returns the time the current rate limiting period ends.<br>
     * This value is a java.util.Date-typed variation of the &quot;X-RateLimit-Reset&quot; response header.
     *
     * @return the time the current rate limiting period ends
     * @since Twitter4J 2.0.9
     */
    Date getResetTime();

}
