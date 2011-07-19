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

import twitter4j.AccountTotals;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.TwitterRuntimeException;

import javax.annotation.Generated;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2011-07-13"
)
final class LazyAccountTotals implements twitter4j.AccountTotals {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private AccountTotals target = null;

    LazyAccountTotals(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private AccountTotals getTarget() {
        if (target == null) {
            try {
                target = factory.createAccountTotals(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    /**
     * Returns the number of total updates.
     *
     * @return the number of total updates
     */
    public int getUpdates() {
        return getTarget().getUpdates();
    }


    /**
     * Returns the number of total followers.
     *
     * @return the number of total followers
     */
    public int getFollowers() {
        return getTarget().getFollowers();
    }


    /**
     * Returns the number of total favorites.
     *
     * @return the number of total favorites
     */
    public int getFavorites() {
        return getTarget().getFavorites();
    }


    /**
     * Returns the number of total friends.
     *
     * @return the number of total friends
     */
    public int getFriends() {
        return getTarget().getFriends();
    }

    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountTotals)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyAccountTotals{" +
                "target=" + getTarget() +
                "}";
    }
}
