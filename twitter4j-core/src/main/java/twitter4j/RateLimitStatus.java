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

/**
 * A data interface representing Twitter REST API's rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
 */
public interface RateLimitStatus extends java.io.Serializable {
    /**
     * Returns the remaining number of API requests available.<br>
     * This value is identical to the &quot;X-Rate-Limit-Remaining&quot; response header.
     *
     * @return the remaining number of API requests available
     * @since Twitter4J 3.0.0
     */
    int getRemaining();

    /**
     * Returns the current limit in effect<br>
     * This value is identical to the &quot;X-Rate-Limit-Limit&quot; response header.
     *
     * @return the current limit in effect
     */
    int getLimit();

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

}
