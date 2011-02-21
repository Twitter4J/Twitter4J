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

import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.Date;

import static twitter4j.internal.util.ParseUtil.getDate;
import static twitter4j.internal.util.ParseUtil.getInt;
import static twitter4j.internal.util.ParseUtil.getLong;
import static twitter4j.internal.util.ParseUtil.getRawString;
import static twitter4j.internal.util.ParseUtil.getUnescapedString;

/**
 * A data class representing a Tweet in the search response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class TweetJSONImpl implements Tweet, java.io.Serializable {
    private String text;
    private int toUserId = -1;
    private String toUser = null;
    private String fromUser;
    private long id;
    private int fromUserId;
    private String isoLanguageCode = null;
    private String source;
    private String profileImageUrl;
    private Date createdAt;
    private String location;

    private GeoLocation geoLocation = null;
    private Annotations annotations = null;
    private static final long serialVersionUID = 4299736733993211587L;

    /*package*/ TweetJSONImpl(JSONObject tweet) throws TwitterException {
        text = getUnescapedString("text", tweet);
        toUserId = getInt("to_user_id", tweet);
        toUser = getRawString("to_user", tweet);
        fromUser = getRawString("from_user", tweet);
        id = getLong("id", tweet);
        fromUserId = getInt("from_user_id", tweet);
        isoLanguageCode = getRawString("iso_language_code", tweet);
        source = getUnescapedString("source", tweet);
        profileImageUrl = getUnescapedString("profile_image_url", tweet);
        createdAt = getDate("created_at", tweet, "EEE, dd MMM yyyy HH:mm:ss z");
        location = getRawString("location", tweet);
        geoLocation = GeoLocation.getInstance(tweet);
        if (!tweet.isNull("annotations")) {
            try {
                JSONArray annotationsArray = tweet.getJSONArray("annotations");
                annotations = new Annotations(annotationsArray);
            } catch (JSONException ignore) {
            }
        }
    }

    public int compareTo(Tweet that) {
        long delta = this.id - that.getId();
        if (delta < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        } else if (delta > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return text;
    }

    /**
     * {@inheritDoc}
     */
    public int getToUserId() {
        return toUserId;
    }

    /**
     * {@inheritDoc}
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * {@inheritDoc}
     */
    public String getFromUser() {
        return fromUser;
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
    public int getFromUserId() {
        return fromUserId;
    }

    /**
     * {@inheritDoc}
     */
    public String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    /**
     * {@inheritDoc}
     */
    public String getSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
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
    public GeoLocation getGeoLocation() {
        return geoLocation;
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
    public Annotations getAnnotations() {
        return annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tweet)) return false;

        Tweet tweet = (Tweet) o;

        if (id != tweet.getId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + toUserId;
        result = 31 * result + (toUser != null ? toUser.hashCode() : 0);
        result = 31 * result + fromUser.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + fromUserId;
        result = 31 * result + (isoLanguageCode != null ? isoLanguageCode.hashCode() : 0);
        result = 31 * result + source.hashCode();
        result = 31 * result + profileImageUrl.hashCode();
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + (geoLocation != null ? geoLocation.hashCode() : 0);
        result = 31 * result + (annotations != null ? annotations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TweetJSONImpl{" +
                "text='" + text + '\'' +
                ", toUserId=" + toUserId +
                ", toUser='" + toUser + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", id=" + id +
                ", fromUserId=" + fromUserId +
                ", isoLanguageCode='" + isoLanguageCode + '\'' +
                ", source='" + source + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", createdAt=" + createdAt +
                ", geoLocation=" + geoLocation +
                ", annotations=" + annotations +
                '}';
    }
}
