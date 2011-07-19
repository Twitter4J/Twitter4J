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
import twitter4j.Relationship;
import twitter4j.TwitterException;
import twitter4j.TwitterRuntimeException;

import javax.annotation.Generated;


/**
 * A data class that has detailed information about a relationship between two users
 *
 * @author Perry Sakkaris - psakkaris at gmail.com
 * @see <a href="http://dev.twitter.com/doc/get/friendships/show">GET friendships/show | dev.twitter.com</a>
 * @since Twitter4J 2.1.0
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2011-07-13"
)
final class LazyRelationship implements twitter4j.Relationship {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private Relationship target = null;

    LazyRelationship(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private Relationship getTarget() {
        if (target == null) {
            try {
                target = factory.createRelationship(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    /**
     * Returns the source user id
     *
     * @return the source user id
     */
    public long getSourceUserId() {
        return getTarget().getSourceUserId();
    }


    /**
     * Returns the target user id
     *
     * @return target user id
     */
    public long getTargetUserId() {
        return getTarget().getTargetUserId();
    }


    /**
     * Returns if the source user is blocking the target user
     *
     * @return if the source is blocking the target
     */
    public boolean isSourceBlockingTarget() {
        return getTarget().isSourceBlockingTarget();
    }


    /**
     * Returns the source user screen name
     *
     * @return returns the source user screen name
     */
    public String getSourceUserScreenName() {
        return getTarget().getSourceUserScreenName();
    }


    /**
     * Returns the target user screen name
     *
     * @return the target user screen name
     */
    public String getTargetUserScreenName() {
        return getTarget().getTargetUserScreenName();
    }


    /**
     * Checks if source user is following target user
     *
     * @return true if source user is following target user
     */
    public boolean isSourceFollowingTarget() {
        return getTarget().isSourceFollowingTarget();
    }


    /**
     * Checks if target user is following source user.<br>
     * This method is equivalent to isSourceFollowedByTarget().
     *
     * @return true if target user is following source user
     */
    public boolean isTargetFollowingSource() {
        return getTarget().isTargetFollowingSource();
    }


    /**
     * Checks if source user is being followed by target user
     *
     * @return true if source user is being followed by target user
     */
    public boolean isSourceFollowedByTarget() {
        return getTarget().isSourceFollowedByTarget();
    }


    /**
     * Checks if target user is being followed by source user.<br>
     * This method is equivalent to isSourceFollowingTarget().
     *
     * @return true if target user is being followed by source user
     */
    public boolean isTargetFollowedBySource() {
        return getTarget().isTargetFollowedBySource();
    }


    /**
     * Checks if the source user has enabled notifications for updates of the target user
     *
     * @return true if source user enabled notifications for target user
     */
    public boolean isSourceNotificationsEnabled() {
        return getTarget().isSourceNotificationsEnabled();
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
        if (!(o instanceof Relationship)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyRelationship{" +
                "target=" + getTarget() +
                "}";
    }
}
