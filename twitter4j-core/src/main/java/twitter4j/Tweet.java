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

import java.util.Date;

/**
 * A data class representing a Tweet in the search response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface Tweet extends Comparable<Tweet>, EntitySupport,
        java.io.Serializable {
    /**
     * returns the text
     *
     * @return the text
     */
    String getText();

    /**
     * returns the to_user_id
     *
     * @return the to_user_id value or -1 if to_user_id is not specified by the tweet
     */
    long getToUserId();

    /**
     * returns the to_user
     *
     * @return the to_user value or null if to_user is not specified by the tweet
     */
    String getToUser();

    /**
     * returns the from_user
     *
     * @return the from_user
     */
    String getFromUser();

    /**
     * returns the status id of the tweet
     *
     * @return the status id
     */
    long getId();

    /**
     * returns the user id of the tweet's owner.<br>
     * <font color="orange">Warning:</font> The user ids in the Search API are different from those in the REST API (about the two APIs). This defect is being tracked by Issue 214. This means that the to_user_id and from_user_id field vary from the actualy user id on Twitter.com. Applications will have to perform a screen name-based lookup with the users/show method to get the correct user id if necessary.
     *
     * @return the user id of the tweet's owner
     * @see <a href="http://code.google.com/p/twitter-api/issues/detail?id=214">Issue 214:	Search API "from_user_id" doesn't match up with the proper Twitter "user_id"</a>
     */
    long getFromUserId();

    /**
     * returns the iso language code of the tweet
     *
     * @return the iso language code of the tweet or null if iso_language_code is not specified by the tweet
     */
    String getIsoLanguageCode();

    /**
     * returns the source of the tweet
     *
     * @return the source of the tweet
     */
    String getSource();

    /**
     * returns the profile_image_url
     *
     * @return the profile_image_url
     */
    String getProfileImageUrl();

    /**
     * returns the created_at
     *
     * @return the created_at
     */
    Date getCreatedAt();

    /**
     * Returns The location that this tweet refers to if available.
     *
     * @return returns The location that this tweet refers to if available (can be null)
     * @since Twitter4J 2.1.0
     */
    GeoLocation getGeoLocation();

    /**
     * Returns the textual location where this tweet was posted. This location
     * is useful when no GeoLocation information is available, but must be translated
     * to coordinates via a GeoQuery.
     *
     * @return The textual location where this tweet was posted
     */
    String getLocation();

    /**
     * Returns the place associated with the post.
     *
     * @return The place associated with the post
     */
    Place getPlace();

    /**
     * Returns the annotations of the tweet.
     * At the moment this code is written (2010-08-18), Twitter Search API does
     * not support annotations yet (so even annotated tweets are returned without the annotations).
     * This method is included here for completeness and for future use.
     *
     * @return the annotations
     * @since Twitter4J 2.1.4
     */
    Annotations getAnnotations();

}
