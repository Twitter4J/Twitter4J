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
import twitter4j.TwitterRuntimeException;

import javax.annotation.Generated;
import java.util.Date;

/**
 * A data class representing Twitter REST API's rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2011-07-13"
)
final class LazyRateLimitStatus implements twitter4j.RateLimitStatus {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private RateLimitStatus target = null;

    LazyRateLimitStatus(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private RateLimitStatus getTarget() {
        if (target == null) {
            try {
                target = factory.createRateLimitStatus(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    /**
     * Returns the remaining number of API requests available.<br>
     * This public value is identical to the &quot {
     * return getTarget().is identical to the &quot;
     * }
     * X-RateLimit-Remaining&quot; response header.
     *
     * @return the remaining number of API requests available
     */
    public int getRemainingHits() {
        return getTarget().getRemainingHits();
    }


    /**
     * Returns the current limit in effect<br>
     * This public value is identical to the &quot {
     * return getTarget().is identical to the &quot;
     * }
     * X-RateLimit-Limit&quot; response header.
     *
     * @return the current limit in effect
     */
    public int getHourlyLimit() {
        return getTarget().getHourlyLimit();
    }


    /**
     * Returns the seconds the current rate limiting period ends.<br>
     * This should be a same as getResetTime().getTime()/1000.
     *
     * @return the seconds the current rate limiting period ends
     * @since Twitter4J 2.0.9
     */
    public int getResetTimeInSeconds() {
        return getTarget().getResetTimeInSeconds();
    }


    /**
     * Returns the amount of seconds until the current rate limiting period ends.<br>
     * This is a value provided/calculated only by Twitter4J for handiness and not a part of the twitter API spec.
     *
     * @return the amount of seconds until next rate limiting period
     * @since Twitter4J 2.1.0
     */
    public int getSecondsUntilReset() {
        return getTarget().getSecondsUntilReset();
    }


    /**
     * Returns the time the current rate limiting period ends.<br>
     * This public value is a java.util.Date-typed variation of the &quot {
     * return getTarget().is a java.util.Date-typed variation of the &quot;
     * }
     * X-RateLimit-Reset&quot; response header.
     *
     * @return the time the current rate limiting period ends
     * @since Twitter4J 2.0.9
     */
    public Date getResetTime() {
        return getTarget().getResetTime();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateLimitStatus)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyRateLimitStatus{" +
                "target=" + getTarget() +
                "}";
    }
}
