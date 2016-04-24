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

import twitter4j.conf.Configuration;

import java.util.Arrays;
import java.util.Date;

/**
 * A data class representing Basic user information element
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class UserJSONImpl extends TwitterResponseImpl implements User, java.io.Serializable {

    private static final long serialVersionUID = -5448266606847617015L;
    private long id;
    private String name;
    private String email;
    private String screenName;
    private String location;
    private String description;
    private URLEntity[] descriptionURLEntities;
    private URLEntity urlEntity;
    private boolean isContributorsEnabled;
    private String profileImageUrl;
    private String profileImageUrlHttps;
    private boolean isDefaultProfileImage;
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
    private boolean isDefaultProfile;
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
    private String[] withheldInCountries;

    /*package*/UserJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.registerJSONObject(this, json);
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
            id = ParseUtil.getLong("id", json);
            name = ParseUtil.getRawString("name", json);
            email = ParseUtil.getRawString("email", json);
            screenName = ParseUtil.getRawString("screen_name", json);
            location = ParseUtil.getRawString("location", json);

            // descriptionUrlEntities <=> entities/descriptions/urls[]
            descriptionURLEntities = getURLEntitiesFromJSON(json, "description");

            // urlEntity <=> entities/url/urls[]
            URLEntity[] urlEntities = getURLEntitiesFromJSON(json, "url");
            if (urlEntities.length > 0) {
                urlEntity = urlEntities[0];
            }

            description = ParseUtil.getRawString("description", json);
            if (description != null) {
                description = HTMLEntity.unescapeAndSlideEntityIncdices(description,
                        null, descriptionURLEntities, null, null);
            }

            isContributorsEnabled = ParseUtil.getBoolean("contributors_enabled", json);
            profileImageUrl = ParseUtil.getRawString("profile_image_url", json);
            profileImageUrlHttps = ParseUtil.getRawString("profile_image_url_https", json);
            isDefaultProfileImage = ParseUtil.getBoolean("default_profile_image", json);
            url = ParseUtil.getRawString("url", json);
            isProtected = ParseUtil.getBoolean("protected", json);
            isGeoEnabled = ParseUtil.getBoolean("geo_enabled", json);
            isVerified = ParseUtil.getBoolean("verified", json);
            translator = ParseUtil.getBoolean("is_translator", json);
            followersCount = ParseUtil.getInt("followers_count", json);

            profileBackgroundColor = ParseUtil.getRawString("profile_background_color", json);
            profileTextColor = ParseUtil.getRawString("profile_text_color", json);
            profileLinkColor = ParseUtil.getRawString("profile_link_color", json);
            profileSidebarFillColor = ParseUtil.getRawString("profile_sidebar_fill_color", json);
            profileSidebarBorderColor = ParseUtil.getRawString("profile_sidebar_border_color", json);
            profileUseBackgroundImage = ParseUtil.getBoolean("profile_use_background_image", json);
            isDefaultProfile = ParseUtil.getBoolean("default_profile", json);
            showAllInlineMedia = ParseUtil.getBoolean("show_all_inline_media", json);
            friendsCount = ParseUtil.getInt("friends_count", json);
            createdAt = ParseUtil.getDate("created_at", json, "EEE MMM dd HH:mm:ss z yyyy");
            favouritesCount = ParseUtil.getInt("favourites_count", json);
            utcOffset = ParseUtil.getInt("utc_offset", json);
            timeZone = ParseUtil.getRawString("time_zone", json);
            profileBackgroundImageUrl = ParseUtil.getRawString("profile_background_image_url", json);
            profileBackgroundImageUrlHttps = ParseUtil.getRawString("profile_background_image_url_https", json);
            profileBannerImageUrl = ParseUtil.getRawString("profile_banner_url", json);
            profileBackgroundTiled = ParseUtil.getBoolean("profile_background_tile", json);
            lang = ParseUtil.getRawString("lang", json);
            statusesCount = ParseUtil.getInt("statuses_count", json);
            listedCount = ParseUtil.getInt("listed_count", json);
            isFollowRequestSent = ParseUtil.getBoolean("follow_request_sent", json);
            if (!json.isNull("status")) {
                JSONObject statusJSON = json.getJSONObject("status");
                status = new StatusJSONImpl(statusJSON);
            }
            if (!json.isNull("withheld_in_countries")) {
                JSONArray withheld_in_countries = json.getJSONArray("withheld_in_countries");
                int length = withheld_in_countries.length();
                withheldInCountries = new String[length];
                for (int i = 0 ; i < length; i ++) {
                    withheldInCountries[i] = withheld_in_countries.getString(i);
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /**
     * Get URL Entities from JSON Object.
     * returns URLEntity array by entities/[category]/urls/url[]
     *
     * @param json     user json object
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
        return new URLEntity[0];
    }

    @Override
    public int compareTo(User that) {
        return (int) (this.id - that.getId());
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getScreenName() {
        return screenName;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isContributorsEnabled() {
        return isContributorsEnabled;
    }

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

    @Override
    public boolean isDefaultProfileImage() {
        return isDefaultProfileImage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getURL() {
        return url;
    }

    @Override
    public boolean isProtected() {
        return isProtected;
    }

    @Override
    public int getFollowersCount() {
        return followersCount;
    }

    @Override
    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    @Override
    public String getProfileTextColor() {
        return profileTextColor;
    }

    @Override
    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    @Override
    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    @Override
    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    @Override
    public boolean isProfileUseBackgroundImage() {
        return profileUseBackgroundImage;
    }

    @Override
    public boolean isDefaultProfile() {
        return isDefaultProfile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShowAllInlineMedia() {
        return showAllInlineMedia;
    }

    @Override
    public int getFriendsCount() {
        return friendsCount;
    }

    @Override
    public Status getStatus() {
        return status;
    }


    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public int getFavouritesCount() {
        return favouritesCount;
    }

    @Override
    public int getUtcOffset() {
        return utcOffset;
    }

    @Override
    public String getTimeZone() {
        return timeZone;
    }

    @Override
    public String getProfileBackgroundImageURL() {
        return profileBackgroundImageUrl;
    }

    @Override
    public String getProfileBackgroundImageUrlHttps() {
        return profileBackgroundImageUrlHttps;
    }

    @Override
    public String getProfileBannerURL() {
        return profileBannerImageUrl != null ? profileBannerImageUrl + "/web" : null;
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
        return profileBannerImageUrl != null ? profileBannerImageUrl + "/mobile_retina" : null;
    }

    @Override
    public boolean isProfileBackgroundTiled() {
        return profileBackgroundTiled;
    }

    @Override
    public String getLang() {
        return lang;
    }

    @Override
    public int getStatusesCount() {
        return statusesCount;
    }

    @Override
    public boolean isGeoEnabled() {
        return isGeoEnabled;
    }

    @Override
    public boolean isVerified() {
        return isVerified;
    }

    @Override
    public boolean isTranslator() {
        return translator;
    }

    @Override
    public int getListedCount() {
        return listedCount;
    }

    @Override
    public boolean isFollowRequestSent() {
        return isFollowRequestSent;
    }

    @Override
    public URLEntity[] getDescriptionURLEntities() {
        return descriptionURLEntities;
    }

    @Override
    public URLEntity getURLEntity() {
        if (urlEntity == null) {
            String plainURL = url == null ? "" : url;
            urlEntity = new URLEntityJSONImpl(0, plainURL.length(), plainURL, plainURL, plainURL);
        }
        return urlEntity;
    }

    @Override
    public String[] getWithheldInCountries() {
        return withheldInCountries;
    }

    /*package*/
    static PagableResponseList<User> createPagableUserList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
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
                    TwitterObjectFactory.registerJSONObject(user, userJson);
                }
                users.add(user);
            }
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.registerJSONObject(users, json);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
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
                TwitterObjectFactory.clearThreadLocalMap();
            }
            int size = list.length();
            ResponseList<User> users =
                    new ResponseListImpl<User>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                User user = new UserJSONImpl(json);
                users.add(user);
                if (conf.isJSONStoreEnabled()) {
                    TwitterObjectFactory.registerJSONObject(user, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.registerJSONObject(users, list);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
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
                ", email='" + email + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", isContributorsEnabled=" + isContributorsEnabled +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", profileImageUrlHttps='" + profileImageUrlHttps + '\'' +
                ", isDefaultProfileImage=" + isDefaultProfileImage +
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
                ", isDefaultProfile=" + isDefaultProfile +
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
                ", withheldInCountries=" + Arrays.toString(withheldInCountries) +
                '}';
    }

}
