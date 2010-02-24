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
 * A data class representing a Tweet in the search response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface Tweet extends java.io.Serializable {
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
    int getToUserId();

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
    int getFromUserId();

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

}
