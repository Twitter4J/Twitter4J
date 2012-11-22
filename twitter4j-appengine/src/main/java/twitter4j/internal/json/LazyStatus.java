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

import twitter4j.*;

import javax.annotation.Generated;
import java.util.Date;

/**
 * A data class representing one single status of a user.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2011-07-13"
)
final class LazyStatus implements twitter4j.Status {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private Status target = null;

    LazyStatus(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private Status getTarget() {
        if (target == null) {
            try {
                target = factory.createStatus(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    /**
     * Return the created_at
     *
     * @return created_at
     * @since Twitter4J 1.1.0
     */

    public Date getCreatedAt() {
        return getTarget().getCreatedAt();
    }


    /**
     * Returns the id of the status
     *
     * @return the id
     */
    public long getId() {
        return getTarget().getId();
    }


    /**
     * Returns the text of the status
     *
     * @return the text
     */
    public String getText() {
        return getTarget().getText();
    }


    /**
     * Returns the source
     *
     * @return the source
     * @since Twitter4J 1.0.4
     */
    public String getSource() {
        return getTarget().getSource();
    }


    /**
     * Test if the status is truncated
     *
     * @return true if truncated
     * @since Twitter4J 1.0.4
     */
    public boolean isTruncated() {
        return getTarget().isTruncated();
    }


    /**
     * Returns the in_reply_tostatus_id
     *
     * @return the in_reply_tostatus_id
     * @since Twitter4J 1.0.4
     */
    public long getInReplyToStatusId() {
        return getTarget().getInReplyToStatusId();
    }


    /**
     * Returns the in_reply_user_id
     *
     * @return the in_reply_tostatus_id
     * @since Twitter4J 1.0.4
     */
    public long getInReplyToUserId() {
        return getTarget().getInReplyToUserId();
    }


    /**
     * Returns the in_reply_to_screen_name
     *
     * @return the in_in_reply_to_screen_name
     * @since Twitter4J 2.0.4
     */
    public String getInReplyToScreenName() {
        return getTarget().getInReplyToScreenName();
    }


    /**
     * Returns The location that this tweet refers to if available.
     *
     * @return returns The location that this tweet refers to if available (can be null)
     * @since Twitter4J 2.1.0
     */
    public GeoLocation getGeoLocation() {
        return getTarget().getGeoLocation();
    }


    /**
     * Returns the place attached to this status
     *
     * @return The place attached to this status
     * @since Twitter4J 2.1.1
     */
    public Place getPlace() {
        return getTarget().getPlace();
    }


    /**
     * Test if the status is favorited
     *
     * @return true if favorited
     * @since Twitter4J 1.0.4
     */
    public boolean isFavorited() {
        return getTarget().isFavorited();
    }


    /**
     * Return the user associated with the status.<br>
     * This can be null if the instance if from User.getStatus().
     *
     * @return the user
     */
    public User getUser() {
        return getTarget().getUser();
    }


    /**
     * @since Twitter4J 2.0.10
     */
    public boolean isRetweet() {
        return getTarget().isRetweet();
    }


    /**
     * @since Twitter4J 2.1.0
     */
    public Status getRetweetedStatus() {
        return getTarget().getRetweetedStatus();
    }


    /**
     * Returns an array of contributors, or null if no contributor is associated with this status.
     *
     * @since Twitter4J 2.2.3
     */
    public long[] getContributors() {
        return getTarget().getContributors();
    }


    /**
     * Returns the number of times this tweet has been retweeted, or -1 when the tweet was
     * created before this feature was enabled.
     *
     * @return the retweet count.
     */
    public long getRetweetCount() {
        return getTarget().getRetweetCount();
    }


    /**
     * Returns true if the authenticating user has retweeted this tweet, or false when the tweet was
     * created before this feature was enabled.
     *
     * @return whether the authenticating user has retweeted this tweet.
     * @since Twitter4J 2.1.4
     */
    public boolean isRetweetedByMe() {
        return getTarget().isRetweetedByMe();
    }
    
    /**
     * Returns the authenticating user's retweet's id of this tweet, or -1L when the tweet was created
     * before this feature was enabled.
     *
     * @return the authenticating user's retweet's id of this tweet
     * @since Twitter4J 3.0.1
     */
    public long getCurrentUserRetweetId() {
    	return getTarget().getCurrentUserRetweetId();
    }

    @Override
    public boolean isPossiblySensitive() {
        return getTarget().isPossiblySensitive();
    }


    /**
     * Returns an array of user mentions in the tweet, or null if no users were mentioned.
     *
     * @return An array of user mention entities in the tweet.
     * @since Twitter4J 2.1.9
     */
    public UserMentionEntity[] getUserMentionEntities() {
        return getTarget().getUserMentionEntities();
    }


    /**
     * Returns an array if URLEntity mentioned in the tweet, or null if no URLs were mentioned.
     *
     * @return An array of URLEntity mentioned in the tweet.
     * @since Twitter4J 2.1.9
     */
    public URLEntity[] getURLEntities() {
        return getTarget().getURLEntities();
    }


    /**
     * Returns an array if hashtag mentioned in the tweet, or null if no hashtag were mentioned.
     *
     * @return An array of Hashtag mentioned in the tweet.
     * @since Twitter4J 2.1.9
     */
    public HashtagEntity[] getHashtagEntities() {
        return getTarget().getHashtagEntities();
    }

    /**
     * Returns an array of MediaEntities if medias are available in the tweet, or null if no media is included in the tweet.
     *
     * @return an array of MediaEntities.
     * @since Twitter4J 2.2.3
     */
    public MediaEntity[] getMediaEntities() {
        return getTarget().getMediaEntities();
    }

    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }

    public int compareTo(Status target) {
        return getTarget().compareTo(target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyStatus{" +
                "target=" + getTarget() +
                "}";
    }
}
