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

import java.net.URL;
import java.util.Date;

/**
 * A data interface representing Basic user information element
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#Basicuserinformationelement">REST API Documentation - Basic user information element</a>
 */
public interface User extends TwitterResponse, java.io.Serializable {
    /**
     * Returns the id of the user
     *
     * @return the id of the user
     */
    int getId();

    /**
     * Returns the name of the user
     *
     * @return the name of the user
     */
    String getName();

    /**
     * Returns the screen name of the user
     *
     * @return the screen name of the user
     */
    String getScreenName();

    /**
     * Returns the location of the user
     *
     * @return the location of the user
     */
    String getLocation();

    /**
     * Returns the description of the user
     *
     * @return the description of the user
     */
    String getDescription();

    /**
     * Returns the profile image url of the user
     *
     * @return the profile image url of the user
     */
    URL getProfileImageURL();

    /**
     * Returns the url of the user
     *
     * @return the url of the user
     */
    URL getURL();

    /**
     * Test if the user status is protected
     *
     * @return true if the user status is protected
     */
    boolean isProtected();

    /**
     * Returns the number of followers
     *
     * @return the number of followers
     * @since Twitter4J 1.0.4
     */
    int getFollowersCount();

    /**
     * Returns the current status of the user<br>
     * This can be null if the instance if from Status.getUser().
     * @return current status of the user
     * @since Twitter4J 2.1.1
     */
    Status getStatus();

    /**
     * @return created_at or null if the user is protected
     * @since Twitter4J 1.1.0
     * @deprecated use getStatus().getCreatedAt()
     */
    Date getStatusCreatedAt();

    /**
     * @return status id or -1 if the user is protected
     * @deprecated use getStatus().getId()
     */
    long getStatusId();

    /**
     * @return status text or null if the user is protected
     * @deprecated use getStatus().getText()
     */
    String getStatusText();

    /**
     * @return source or null if the user is protected
     * @since 1.1.4
     * @deprecated use getStatus().getSource()
     */
    String getStatusSource();

    /**
     * @return truncated or false if the user is protected
     * @since 1.1.4
     * @deprecated use getStatus().isTruncated()
     */
    boolean isStatusTruncated();

    /**
     * @return in_reply_to_status_id or -1 if the user is protected
     * @since 1.1.4
     * @deprecated use getStatus().getInReplyToStatusId()
     */
    long getStatusInReplyToStatusId();

    /**
     * @return in_reply_to_user_id or -1 if the user is protected
     * @since 1.1.4
     * @deprecated use getStatus().getInReplyToUserId()
     */
    int getStatusInReplyToUserId();

    /**
     * @return favorited or false if the user is protected
     * @since 1.1.4
     * @deprecated use getStatus().isFavorited()
     */
    boolean isStatusFavorited();

    /**
     * @return in_reply_to_screen_name or null if the user is protected
     * @since 1.1.4
     * @deprecated use getStatus().getInReplyToScreenName()
     */
    String getStatusInReplyToScreenName();

    String getProfileBackgroundColor();

    String getProfileTextColor();

    String getProfileLinkColor();

    String getProfileSidebarFillColor();

    String getProfileSidebarBorderColor();

    int getFriendsCount();

    Date getCreatedAt();

    int getFavouritesCount();

    int getUtcOffset();

    String getTimeZone();

    String getProfileBackgroundImageUrl();

    boolean isProfileBackgroundTiled();

    int getStatusesCount();

    /**
     * @return the user is enabling geo location
     * @since Twitter4J 2.0.10
     */
    boolean isGeoEnabled();

    /**
     * @return returns true if the user is a verified celebrity
     * @since Twitter4J 2.0.10
     */
    boolean isVerified();
}