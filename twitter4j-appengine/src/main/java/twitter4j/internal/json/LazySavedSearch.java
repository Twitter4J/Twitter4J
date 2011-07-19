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
import twitter4j.SavedSearch;
import twitter4j.TwitterException;
import twitter4j.TwitterRuntimeException;

import javax.annotation.Generated;
import java.util.Date;

/**
 * A data class representing a Saved Search
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.8
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2011-07-13"
)
final class LazySavedSearch implements twitter4j.SavedSearch {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private SavedSearch target = null;

    LazySavedSearch(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private SavedSearch getTarget() {
        if (target == null) {
            try {
                target = factory.createSavedSearch(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    public Date getCreatedAt() {
        return getTarget().getCreatedAt();
    }


    public String getQuery() {
        return getTarget().getQuery();
    }


    public int getPosition() {
        return getTarget().getPosition();
    }


    public String getName() {
        return getTarget().getName();
    }


    public int getId() {
        return getTarget().getId();
    }


    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }

    public int compareTo(SavedSearch target) {
        return getTarget().compareTo(target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavedSearch)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazySavedSearch{" +
                "target=" + getTarget() +
                "}";
    }
}
