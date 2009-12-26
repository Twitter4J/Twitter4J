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

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.util.Date;

import static twitter4j.ParseUtil.*;

/**
 * A data class representing a Tweet in the search response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ class TweetJSONImpl implements Tweet, java.io.Serializable {
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

    private GeoLocation geoLocation = null;
    private static final long serialVersionUID = 4299736733993211587L;

    /*package*/ TweetJSONImpl() {
        // Just for protobuf support
        // Currently this constructor is never used in twitter4j artifact.
    }

    /*package*/ TweetJSONImpl(JSONObject tweet) throws TwitterException {
        text = getURLDecodedString("text", tweet);
        toUserId = getInt("to_user_id", tweet);
        toUser = getRawString("to_user", tweet);
        fromUser = getRawString("from_user", tweet);
        id = getLong("id", tweet);
        fromUserId = getInt("from_user_id", tweet);
        isoLanguageCode = getRawString("iso_language_code", tweet);
        source = getURLDecodedString("source", tweet);
        profileImageUrl = getURLDecodedString("profile_image_url", tweet);
        createdAt = getDate("created_at", tweet, "EEE, dd MMM yyyy HH:mm:ss z");
        geoLocation = GeoLocation.getInstance(tweet);

    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    /**
     * {@inheritDoc}
     */
    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    /**
     * {@inheritDoc}
     */
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * {@inheritDoc}
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    /**
     * {@inheritDoc}
     */
    public String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    public void setIsoLanguageCode(String isoLanguageCode) {
        this.isoLanguageCode = isoLanguageCode;
    }

    /**
     * {@inheritDoc}
     */
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * {@inheritDoc}
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * {@inheritDoc}
     */
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
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
                '}';
    }
}
