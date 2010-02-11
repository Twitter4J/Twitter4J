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

import twitter4j.http.HttpResponse;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import static twitter4j.ParseUtil.*;

/**
 * A data class representing Basic user information element
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#Basicuserinformationelement">REST API Documentation - Basic user information element</a>
 */
/*package*/ final class UserJSONImpl extends TwitterResponseImpl implements User, java.io.Serializable {

    private int id;
    private String name;
    private String screenName;
    private String location;
    private String description;
    private String profileImageUrl;
    private String url;
    private boolean isProtected;
    private int followersCount;

    private Status status;

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
    private boolean profileBackgroundTiled;
    private int statusesCount;
    private boolean isGeoEnabled;
    private boolean isVerified;
    private static final long serialVersionUID = -6345893237975349030L;

    /*package*/UserJSONImpl(HttpResponse res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }

    /*package*/UserJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            id = getInt("id", json);
            name = getRawString("name", json);
            screenName = getRawString("screen_name", json);
            location = getRawString("location", json);
            description = getRawString("description", json);
            profileImageUrl = getRawString("profile_image_url", json);
            url = getRawString("url", json);
            isProtected = getBoolean("protected", json);
            isGeoEnabled = getBoolean("geo_enabled", json);
            isVerified = getBoolean("verified", json);
            followersCount = getInt("followers_count", json);

            profileBackgroundColor = getRawString("profile_background_color", json);
            profileTextColor = getRawString("profile_text_color", json);
            profileLinkColor = getRawString("profile_link_color", json);
            profileSidebarFillColor = getRawString("profile_sidebar_fill_color", json);
            profileSidebarBorderColor = getRawString("profile_sidebar_border_color", json);
            friendsCount = getInt("friends_count", json);
            createdAt = getDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
            favouritesCount = json.getInt("favourites_count");
            utcOffset = getInt("utc_offset", json);
            timeZone = getRawString("time_zone", json);
            profileBackgroundImageUrl = getRawString("profile_background_image_url", json);
            profileBackgroundTiled = getBoolean("profile_background_tile", json);
            statusesCount = getInt("statuses_count", json);
            if (!json.isNull("status")) {
                JSONObject statusJSON = json.getJSONObject("status");
                status = new StatusJSONImpl(statusJSON);
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * {@inheritDoc}
     */
    public String getLocation() {
        return location;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    public URL getProfileImageURL() {
        try {
            return new URL(profileImageUrl);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public URL getURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * {@inheritDoc}
     */
    public int getFollowersCount() {
        return followersCount;
    }

    /**
     * {@inheritDoc}
     */
    public Date getStatusCreatedAt() {
        return status.getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    public long getStatusId() {
        return status.getId();
    }

    /**
     * {@inheritDoc}
     */
    public String getStatusText() {
        return status.getText();
    }

    /**
     * {@inheritDoc}
     */
    public String getStatusSource() {
        return status.getSource();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStatusTruncated() {
        return status.isTruncated();
    }

    /**
     * {@inheritDoc}
     */
    public long getStatusInReplyToStatusId() {
        return status.getInReplyToStatusId();
    }

    /**
     * {@inheritDoc}
     */
    public int getStatusInReplyToUserId() {
        return status.getInReplyToUserId();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStatusFavorited() {
        return status.isFavorited();
    }

    /**
     * {@inheritDoc}
     */
    public String getStatusInReplyToScreenName() {
        return status.getInReplyToScreenName();
    }

    /**
     * {@inheritDoc}
     */
    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public String getProfileTextColor() {
        return profileTextColor;
    }

    /**
     * {@inheritDoc}
     */
    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    /**
     * {@inheritDoc}
     */
    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    /**
     * {@inheritDoc}
     */
    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    /**
     * {@inheritDoc}
     */
    public int getFriendsCount() {
        return friendsCount;
    }

    /**
     * {@inheritDoc}
     */
    public Status getStatus() {
        return status;
    }


    /**
     * {@inheritDoc}
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    public int getFavouritesCount() {
        return favouritesCount;
    }

    /**
     * {@inheritDoc}
     */
    public int getUtcOffset() {
        return utcOffset;
    }

    /**
     * {@inheritDoc}
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * {@inheritDoc}
     */
    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isProfileBackgroundTiled() {
        return profileBackgroundTiled;
    }

    /**
     * {@inheritDoc}
     */
    public int getStatusesCount() {
        return statusesCount;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isGeoEnabled() {
        return isGeoEnabled;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isVerified() {
        return isVerified;
    }

    /*package*/ static PagableResponseList<User> createPagableUserList(HttpResponse res) throws TwitterException {
        try {
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("users");
            int size = list.length();
            PagableResponseList<User> users =
                    new PagableResponseList<User>(size, json, res);
            for (int i = 0; i < size; i++) {
                users.add(new UserJSONImpl(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }
    /*package*/ static ResponseList<User> createUserList(HttpResponse res) throws TwitterException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<User> users =
                    new ResponseList<User>(size, res);
            for (int i = 0; i < size; i++) {
                users.add(new UserJSONImpl(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof User && ((User) obj).getId() == this.id;
    }

    @Override
    public String toString() {
        return "UserJSONImpl{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", url='" + url + '\'' +
                ", isProtected=" + isProtected +
                ", followersCount=" + followersCount +
                ", status=" + status +
                ", profileBackgroundColor='" + profileBackgroundColor + '\'' +
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
                ", profileBackgroundTile='" + profileBackgroundTiled + '\'' +
                ", statusesCount=" + statusesCount +
                ", geoEnabled=" + isGeoEnabled +
                ", verified=" + isVerified +
                '}';
    }
}
