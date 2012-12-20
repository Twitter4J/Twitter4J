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
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static twitter4j.internal.json.z_T4JInternalParseUtil.*;

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
    private URLEntity[] descriptionURLEntities;
    private URLEntity urlEntity;
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
    private String profileBannerImageUrl;
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

    /* Only for serialization purposes. */
    /*package*/UserJSONImpl() {

    }

    private void init(JSONObject json) throws TwitterException {
        try {
            id = getLong("id", json);
            name = getRawString("name", json);
            screenName = getRawString("screen_name", json);
            location = getRawString("location", json);
            
            // descriptionUrlEntities <=> entities/descriptions/urls[]
            descriptionURLEntities = getURLEntitiesFromJSON(json, "description");
            descriptionURLEntities = descriptionURLEntities == null ? new URLEntity[0] : descriptionURLEntities;
            
            // urlEntity <=> entities/url/urls[]
            URLEntity[] urlEntities = getURLEntitiesFromJSON(json, "url");
            if (urlEntities != null && urlEntities.length > 0) {
                urlEntity = urlEntities[0];
            }
            
            description = getRawString("description", json);
            if (description != null) {
                description = HTMLEntity.unescapeAndSlideEntityIncdices(description, 
                        null, descriptionURLEntities, null, null);
            }
            
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
            profileBannerImageUrl = getRawString("profile_banner_url", json);
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
    
    /**
     * Get URL Entities from JSON Object.
     * returns URLEntity array by entities/[category]/urls/url[]
     * 
     * @param json user json object
     * @param category entities category. e.g. "description" or "url"
     * @return URLEntity array by entities/[category]/urls/url[]
     * @throws JSONException
     * @throws TwitterException
     */
    private static URLEntity[] getURLEntitiesFromJSON(JSONObject json, String category) throws JSONException, TwitterException {
        if (!json.isNull("entities")) {
            JSONObject entitiesJSON = json.getJSONObject("entities");
            if (!entitiesJSON.isNull(category)) {
                JSONObject descriptionEntitiesJSON = entitiesJSON.getJSONObject(category);
                if (!descriptionEntitiesJSON.isNull("urls")) {
                    JSONArray urlsArray = descriptionEntitiesJSON.getJSONArray("urls");
                    int len = urlsArray.length();
                    URLEntity[] urlEntities = new URLEntity[len];
                    for (int i = 0; i < len; i++) {
                        urlEntities[i] = new URLEntityJSONImpl(urlsArray.getJSONObject(i));
                    }
                    return urlEntities;
                }
            }
        }
        return null;
    }

    @Override
    public int compareTo(User that) {
        return (int) (this.id - that.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getScreenName() {
        return screenName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocation() {
        return location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isContributorsEnabled() {
        return isContributorsEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileImageURL() {
        return profileImageUrl;
    }

    @Override
    public String getBiggerProfileImageURL() {
        return toResizedURL(profileImageUrl, "_bigger");
    }

    @Override
    public String getMiniProfileImageURL() {
        return toResizedURL(profileImageUrl, "_mini");
    }

    @Override
    public String getOriginalProfileImageURL() {
        return toResizedURL(profileImageUrl, "");
    }

    private String toResizedURL(String originalURL, String sizeSuffix) {
        if (null != originalURL) {
            int index = originalURL.lastIndexOf("_");
            int suffixIndex = originalURL.lastIndexOf(".");
            int slashIndex = originalURL.lastIndexOf("/");
            String url = originalURL.substring(0, index) + sizeSuffix;
            if (suffixIndex > slashIndex) {
                url += originalURL.substring(suffixIndex);
            }
            return url;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getProfileImageUrlHttps() {
        try {
            return new URL(profileImageUrlHttps);
        } catch (MalformedURLException e) {
            return null;
        }
    }
    @Override
    public String getProfileImageURLHttps() {
        return profileImageUrlHttps;
    }

    @Override
    public String getBiggerProfileImageURLHttps() {
        return toResizedURL(profileImageUrlHttps, "_bigger");
    }

    @Override
    public String getMiniProfileImageURLHttps() {
        return toResizedURL(profileImageUrlHttps, "_mini");
    }

    @Override
    public String getOriginalProfileImageURLHttps() {
        return toResizedURL(profileImageUrlHttps, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFollowersCount() {
        return followersCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    @Override
    public String getProfileTextColor() {
        return profileTextColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isProfileUseBackgroundImage() {
        return profileUseBackgroundImage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShowAllInlineMedia() {
        return showAllInlineMedia;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFriendsCount() {
        return friendsCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        return status;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFavouritesCount() {
        return favouritesCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getUtcOffset() {
        return utcOffset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileBackgroundImageUrl() {
        return getProfileBackgroundImageURL();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileBackgroundImageURL() {
        return profileBackgroundImageUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileBackgroundImageUrlHttps() {
        return profileBackgroundImageUrlHttps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileBannerURL() {
        return profileBannerImageUrl != null ? profileBannerImageUrl+"/web" : null;
    }

    @Override
    public String getProfileBannerRetinaURL() {
        return profileBannerImageUrl != null ? profileBannerImageUrl + "/web_retina" : null;
    }

    @Override
    public String getProfileBannerIPadURL() {
        return profileBannerImageUrl != null ? profileBannerImageUrl + "/ipad" : null;
    }

    @Override
    public String getProfileBannerIPadRetinaURL() {
        return profileBannerImageUrl != null ? profileBannerImageUrl + "/ipad_retina" : null;
    }

    @Override
    public String getProfileBannerMobileURL() {
        return profileBannerImageUrl != null ? profileBannerImageUrl + "/mobile" : null;
    }

    @Override
    public String getProfileBannerMobileRetinaURL() {
        return profileBannerImageUrl != null ? profileBannerImageUrl + "/ipad_retina" : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isProfileBackgroundTiled() {
        return profileBackgroundTiled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLang() {
        return lang;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatusesCount() {
        return statusesCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGeoEnabled() {
        return isGeoEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVerified() {
        return isVerified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTranslator() {
        return translator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getListedCount() {
        return listedCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFollowRequestSent() {
        return isFollowRequestSent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URLEntity[] getDescriptionURLEntities() {
        return descriptionURLEntities;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public URLEntity getURLEntity() {
        if (urlEntity == null) {
            String plainURL = url == null ? "" : url;
            urlEntity = new URLEntityJSONImpl(0, plainURL.length(), plainURL, plainURL, plainURL);
        }
        return urlEntity;
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
