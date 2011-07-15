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

import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static twitter4j.internal.util.z_T4JInternalParseUtil.getBoolean;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getDate;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getInt;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getLong;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getRawString;

/**
 * A data class representing Basic user information element
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class UserJSONImpl extends TwitterResponseImpl implements User, java.io.Serializable {

    private long id;
    private String name;
    private String screenName;
    private String location;
    private String description;
    private boolean isContributorsEnabled;
    private String profileImageUrl;
    private String profileImageUrlHttps;
    private String url;
    private boolean isProtected;
    private int followersCount;

    private Status status;

    private String profileBackgroundColor;
    private String profileTextColor;
    private String profileLinkColor;
    private String profileSidebarFillColor;
    private String profileSidebarBorderColor;
    private boolean profileUseBackgroundImage;
    private boolean showAllInlineMedia;
    private int friendsCount;
    private Date createdAt;
    private int favouritesCount;
    private int utcOffset;
    private String timeZone;
    private String profileBackgroundImageUrl;
    private String profileBackgroundImageUrlHttps;
    private boolean profileBackgroundTiled;
    private String lang;
    private int statusesCount;
    private boolean isGeoEnabled;
    private boolean isVerified;
    private boolean translator;
    private int listedCount;
    private boolean isFollowRequestSent;
    private static final long serialVersionUID = -6345893237975349030L;

    /*package*/UserJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    /*package*/UserJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            id = getLong("id", json);
            name = getRawString("name", json);
            screenName = getRawString("screen_name", json);
            location = getRawString("location", json);
            description = getRawString("description", json);
            isContributorsEnabled = getBoolean("contributors_enabled", json);
            profileImageUrl = getRawString("profile_image_url", json);
            profileImageUrlHttps = getRawString("profile_image_url_https", json);
            url = getRawString("url", json);
            isProtected = getBoolean("protected", json);
            isGeoEnabled = getBoolean("geo_enabled", json);
            isVerified = getBoolean("verified", json);
            translator = getBoolean("is_translator", json);
            followersCount = getInt("followers_count", json);

            profileBackgroundColor = getRawString("profile_background_color", json);
            profileTextColor = getRawString("profile_text_color", json);
            profileLinkColor = getRawString("profile_link_color", json);
            profileSidebarFillColor = getRawString("profile_sidebar_fill_color", json);
            profileSidebarBorderColor = getRawString("profile_sidebar_border_color", json);
            profileUseBackgroundImage = getBoolean("profile_use_background_image", json);
            showAllInlineMedia = getBoolean("show_all_inline_media", json);
            friendsCount = getInt("friends_count", json);
            createdAt = getDate("created_at", json, "EEE MMM dd HH:mm:ss z yyyy");
            favouritesCount = getInt("favourites_count", json);
            utcOffset = getInt("utc_offset", json);
            timeZone = getRawString("time_zone", json);
            profileBackgroundImageUrl = getRawString("profile_background_image_url", json);
            profileBackgroundImageUrlHttps = getRawString("profile_background_image_url_https", json);
            profileBackgroundTiled = getBoolean("profile_background_tile", json);
            lang = getRawString("lang", json);
            statusesCount = getInt("statuses_count", json);
            listedCount = getInt("listed_count", json);
            isFollowRequestSent = getBoolean("follow_request_sent", json);
            if (!json.isNull("status")) {
                JSONObject statusJSON = json.getJSONObject("status");
                status = new StatusJSONImpl(statusJSON);
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    public int compareTo(User that) {
        return (int) (this.id - that.getId());
    }

    /**
     * {@inheritDoc}
     */
    public long getId() {
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
    public boolean isContributorsEnabled() {
        return isContributorsEnabled;
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
    public URL getProfileImageUrlHttps() {
        if (null == profileImageUrlHttps)
            return null;
        try {
            return new URL(profileImageUrlHttps);
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
    public boolean isProfileUseBackgroundImage() {
        return profileUseBackgroundImage;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isShowAllInlineMedia() {
        return showAllInlineMedia;
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
    public String getProfileBackgroundImageUrlHttps() {
        return profileBackgroundImageUrlHttps;
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
    public String getLang() {
        return lang;
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

    /**
     * {@inheritDoc}
     */
    public boolean isTranslator() {
        return translator;
    }

    /**
     * {@inheritDoc}
     */
    public int getListedCount() {
        return listedCount;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFollowRequestSent() {
        return isFollowRequestSent;
    }

    /*package*/
    static PagableResponseList<User> createPagableUserList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("users");
            int size = list.length();
            PagableResponseList<User> users =
                    new PagableResponseListImpl<User>(size, json, res);
            for (int i = 0; i < size; i++) {
                JSONObject userJson = list.getJSONObject(i);
                User user = new UserJSONImpl(userJson);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(user, userJson);
                }
                users.add(user);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, json);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    /*package*/
    static ResponseList<User> createUserList(HttpResponse res, Configuration conf) throws TwitterException {
        return createUserList(res.asJSONArray(), res, conf);
    }

    /*package*/
    static ResponseList<User> createUserList(JSONArray list, HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            int size = list.length();
            ResponseList<User> users =
                    new ResponseListImpl<User>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                User user = new UserJSONImpl(json);
                users.add(user);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(user, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, list);
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
        return (int) id;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", isContributorsEnabled=" + isContributorsEnabled +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", profileImageUrlHttps='" + profileImageUrlHttps + '\'' +
                ", url='" + url + '\'' +
                ", isProtected=" + isProtected +
                ", followersCount=" + followersCount +
                ", status=" + status +
                ", profileBackgroundColor='" + profileBackgroundColor + '\'' +
                ", profileTextColor='" + profileTextColor + '\'' +
                ", profileLinkColor='" + profileLinkColor + '\'' +
                ", profileSidebarFillColor='" + profileSidebarFillColor + '\'' +
                ", profileSidebarBorderColor='" + profileSidebarBorderColor + '\'' +
                ", profileUseBackgroundImage=" + profileUseBackgroundImage +
                ", showAllInlineMedia=" + showAllInlineMedia +
                ", friendsCount=" + friendsCount +
                ", createdAt=" + createdAt +
                ", favouritesCount=" + favouritesCount +
                ", utcOffset=" + utcOffset +
                ", timeZone='" + timeZone + '\'' +
                ", profileBackgroundImageUrl='" + profileBackgroundImageUrl + '\'' +
                ", profileBackgroundImageUrlHttps='" + profileBackgroundImageUrlHttps + '\'' +
                ", profileBackgroundTiled=" + profileBackgroundTiled +
                ", lang='" + lang + '\'' +
                ", statusesCount=" + statusesCount +
                ", isGeoEnabled=" + isGeoEnabled +
                ", isVerified=" + isVerified +
                ", translator=" + translator +
                ", listedCount=" + listedCount +
                ", isFollowRequestSent=" + isFollowRequestSent +
                '}';
    }
}
