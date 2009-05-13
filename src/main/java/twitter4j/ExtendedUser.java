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

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import twitter4j.http.Response;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * A data class representing Extended user information element
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/REST%20API%20Documentation#Extendeduserinformationelement">Extended user information element</a>
 */
public class ExtendedUser extends UserWithStatus {
    private String profileBackgroundColor;
    private String profileTextColor;
    private String profileLinkColor;
    private String profileSidebarFillColor;
    private String profileSidebarBorderColor;
    private int friendsCount;
    private Date createdAt;
    private int favouritesCount;
    private int utcOffset;
    private String timeZone;
    private String profileBackgroundImageUrl;
    private String profileBackgroundTile;
    private boolean following;
    private boolean notificationEnabled;
    private int statusesCount;
    private static final long serialVersionUID = -8486230870587454252L;

    public ExtendedUser(Response res, Twitter twitter) throws TwitterException {
        super(res, res.asDocument().getDocumentElement(), twitter);
        init(res.asDocument().getDocumentElement());
    }
    public ExtendedUser(Response res, Element elem, Twitter twitter) throws TwitterException {
        super(res, elem, twitter);
        init(elem);
    }
    private void init(Element elem) throws TwitterException{
        profileBackgroundColor = getChildText("profile_background_color", elem);
        profileTextColor = getChildText("profile_text_color", elem);
        profileLinkColor = getChildText("profile_link_color", elem);
        profileSidebarFillColor = getChildText("profile_sidebar_fill_color", elem);
        profileSidebarBorderColor = getChildText("profile_sidebar_border_color", elem);
        friendsCount = getChildInt("friends_count", elem);
        createdAt = getChildDate("created_at", elem);
        favouritesCount = getChildInt("favourites_count", elem);
        utcOffset = getChildInt("utc_offset", elem);
        timeZone = getChildText("time_zone", elem);
        profileBackgroundImageUrl = getChildText("profile_background_image_url", elem);
        profileBackgroundTile = getChildText("profile_background_tile", elem);
        following = getChildBoolean("following", elem);
        notificationEnabled = getChildBoolean("notifications", elem);
        statusesCount = getChildInt("statuses_count", elem);
    }

    public static List<ExtendedUser> constructExtendedUsers(Response res, Twitter twitter) throws TwitterException {
        Document doc = res.asDocument();
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<ExtendedUser>(0);
        } else {
            try {
                ensureRootNodeNameIs("users", doc);
                NodeList list = doc.getDocumentElement().getElementsByTagName(
                        "user");
                int size = list.getLength();
                List<ExtendedUser> users = new ArrayList<ExtendedUser>(size);
                for (int i = 0; i < size; i++) {
                    users.add(new ExtendedUser(res, (Element) list.item(i), twitter));
                }
                return users;
            } catch (TwitterException te) {
                if (isRootNodeNilClasses(doc)) {
                    return new ArrayList<ExtendedUser>(0);
                } else {
                    throw te;
                }
            }
        }
    }
    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public String getProfileTextColor() {
        return profileTextColor;
    }

    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getProfileBackgroundTile() {
        return profileBackgroundTile;
    }

    /**
     *
     * @deprecated <a href="http://groups.google.com/group/twitter-development-talk/browse_frm/thread/42ba883b9f8e3c6e">Deprecation of following and notification elements</a>
     */
    public boolean isFollowing() {
        return following;
    }

    /**
     * @deprecated use isNotificationsEnabled() instead
     */

    public boolean isNotifications() {
        return notificationEnabled;
    }

    /**
     *
     * @since Twitter4J 2.0.1
     * @deprecated <a href="http://groups.google.com/group/twitter-development-talk/browse_frm/thread/42ba883b9f8e3c6e">Deprecation of following and notification elements</a>
     */
    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExtendedUser that = (ExtendedUser) o;

        if (favouritesCount != that.favouritesCount) return false;
        if (following != that.following) return false;
        if (friendsCount != that.friendsCount) return false;
        if (notificationEnabled != that.notificationEnabled) return false;
        if (statusesCount != that.statusesCount) return false;
        if (utcOffset != that.utcOffset) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null)
            return false;
        if (profileBackgroundColor != null ? !profileBackgroundColor.equals(that.profileBackgroundColor) : that.profileBackgroundColor != null)
            return false;
        if (profileBackgroundImageUrl != null ? !profileBackgroundImageUrl.equals(that.profileBackgroundImageUrl) : that.profileBackgroundImageUrl != null)
            return false;
        if (profileBackgroundTile != null ? !profileBackgroundTile.equals(that.profileBackgroundTile) : that.profileBackgroundTile != null)
            return false;
        if (profileLinkColor != null ? !profileLinkColor.equals(that.profileLinkColor) : that.profileLinkColor != null)
            return false;
        if (profileSidebarBorderColor != null ? !profileSidebarBorderColor.equals(that.profileSidebarBorderColor) : that.profileSidebarBorderColor != null)
            return false;
        if (profileSidebarFillColor != null ? !profileSidebarFillColor.equals(that.profileSidebarFillColor) : that.profileSidebarFillColor != null)
            return false;
        if (profileTextColor != null ? !profileTextColor.equals(that.profileTextColor) : that.profileTextColor != null)
            return false;
        if (timeZone != null ? !timeZone.equals(that.timeZone) : that.timeZone != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (profileBackgroundColor != null ? profileBackgroundColor.hashCode() : 0);
        result = 31 * result + (profileTextColor != null ? profileTextColor.hashCode() : 0);
        result = 31 * result + (profileLinkColor != null ? profileLinkColor.hashCode() : 0);
        result = 31 * result + (profileSidebarFillColor != null ? profileSidebarFillColor.hashCode() : 0);
        result = 31 * result + (profileSidebarBorderColor != null ? profileSidebarBorderColor.hashCode() : 0);
        result = 31 * result + friendsCount;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + favouritesCount;
        result = 31 * result + utcOffset;
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (profileBackgroundImageUrl != null ? profileBackgroundImageUrl.hashCode() : 0);
        result = 31 * result + (profileBackgroundTile != null ? profileBackgroundTile.hashCode() : 0);
        result = 31 * result + (following ? 1 : 0);
        result = 31 * result + (notificationEnabled ? 1 : 0);
        result = 31 * result + statusesCount;
        return result;
    }

    @Override
    public String toString() {
        return "ExtendedUser{" +
                "profileBackgroundColor='" + profileBackgroundColor + '\'' +
                ", profileTextColor='" + profileTextColor + '\'' +
                ", profileLinkColor='" + profileLinkColor + '\'' +
                ", profileSidebarFillColor='" + profileSidebarFillColor + '\'' +
                ", profileSidebarBorderColor='" + profileSidebarBorderColor + '\'' +
                ", friendsCount=" + friendsCount +
                ", createdAt=" + createdAt +
                ", favouritesCount=" + favouritesCount +
                ", utcOffset=" + utcOffset +
                ", timeZone='" + timeZone + '\'' +
                ", profileBackgroundImageUrl='" + profileBackgroundImageUrl + '\'' +
                ", profileBackgroundTile='" + profileBackgroundTile + '\'' +
                ", following=" + following +
                ", notifications=" + notificationEnabled +
                ", statusesCount=" + statusesCount +
                '}';
    }
}
