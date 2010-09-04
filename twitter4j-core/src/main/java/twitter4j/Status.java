/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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

import java.net.URL;
import java.util.Date;

/**
 * A data interface representing one single status of a user.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface Status extends Comparable<Status>, TwitterResponse, java.io.Serializable {
    /**
     * Return the created_at
     *
     * @return created_at
     * @since Twitter4J 1.1.0
     */

    Date getCreatedAt();

    /**
     * Returns the id of the status
     *
     * @return the id
     */
    long getId();

    /**
     * Returns the text of the status
     *
     * @return the text
     */
    String getText();

    /**
     * Returns the source
     *
     * @return the source
     * @since Twitter4J 1.0.4
     */
    String getSource();


    /**
     * Test if the status is truncated
     *
     * @return true if truncated
     * @since Twitter4J 1.0.4
     */
    boolean isTruncated();

    /**
     * Returns the in_reply_tostatus_id
     *
     * @return the in_reply_tostatus_id
     * @since Twitter4J 1.0.4
     */
    long getInReplyToStatusId();

    /**
     * Returns the in_reply_user_id
     *
     * @return the in_reply_tostatus_id
     * @since Twitter4J 1.0.4
     */
    int getInReplyToUserId();

    /**
     * Returns the in_reply_to_screen_name
     *
     * @return the in_in_reply_to_screen_name
     * @since Twitter4J 2.0.4
     */
    String getInReplyToScreenName();

    /**
     * Returns The location that this tweet refers to if available.
     *
     * @return returns The location that this tweet refers to if available (can be null)
     * @since Twitter4J 2.1.0
     */
    GeoLocation getGeoLocation();

    /**
     * Returns the place attached to this status
     * @return The place attached to this status
     * @since Twitter4J 2.1.1
     */
    Place getPlace();

    /**
     * Test if the status is favorited
     *
     * @return true if favorited
     * @since Twitter4J 1.0.4
     */
    boolean isFavorited();

    /**
     * Return the user associated with the status.<br>
     * This can be null if the instance if from User.getStatus().
     *
     * @return the user
     */
    User getUser();

    /**
     * @since Twitter4J 2.0.10
     */
    boolean isRetweet();

    /**
     * @since Twitter4J 2.1.0
     */
    Status getRetweetedStatus();

    /**
     * Returns an array of contributors, or null if no contributor is associated with this status.
     * @since Twitter4J 2.1.2
     */
    String[] getContributors();

    /**
     * Returns the number of times this tweet has been retweeted, or -1 when the tweet was
     * created before this feature was enabled.
     *
     * @return the retweet count.
     */
    long getRetweetCount();

    /**
     * Returns true if the authenticating user has retweeted this tweet, or false when the tweet was
     * created before this feature was enabled.
     *
     * @return whether the authenticating user has retweeted this tweet.
     * @since Twitter4J 2.1.4
     */
    boolean isRetweetedByMe();

    /**
     * Returns an array of users mentioned in the tweet, or null if no users were mentioned.
     * Note that these users only have data for ID, screen name, and name.
     *
     * @return An array of users mentioned in the tweet.
     * @since Twitter4J 2.1.4
     */
    User[] getUserMentions();

    /**
     * Returns an array of URLs mentioned in the tweet, or null if no URLs were mentioned.
     *
     * @return An array of URLs mentioned in the tweet.
     * @since Twitter4J 2.1.4
     */
    URL[] getURLs();

    /**
     * Returns an array of hashtags mentioned in the tweet, or null if no hashtags were mentioned.
     *
     * @return An array of users mentioned in the tweet.
     * @since Twitter4J 2.1.4
     */
    String[] getHashtags();

    /**
     * Returns the annotations, or null if no annotations are associated with this status.
     * @since Twitter4J 2.1.4
     */
    Annotations getAnnotations();
}
