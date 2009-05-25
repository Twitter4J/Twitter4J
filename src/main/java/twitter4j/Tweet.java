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

/**
 * A data class representing a Tweet in the search response
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Tweet extends TwitterResponse{
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
    private static final long serialVersionUID = 4299736733993211587L;

    /*package*/Tweet(JSONObject tweet, TwitterSupport twitterSupport) throws TwitterException {
        super();
        try {
            text = getString("text", tweet, false);
            try{
                toUserId = tweet.getInt("to_user_id");
                toUser = tweet.getString("to_user");
            }catch(JSONException ignore){
                // to_user_id can be "null"
                // to_user can be missing
            }
            fromUser = tweet.getString("from_user");
            id = tweet.getLong("id");
            fromUserId = tweet.getInt("from_user_id");
            try{
                isoLanguageCode = tweet.getString("iso_language_code");
            }catch(JSONException ignore){
                // iso_language_code can be missing
            }
            source = getString("source", tweet, true);
            profileImageUrl = getString("profile_image_url", tweet, true);
            createdAt = parseDate(tweet.getString("created_at"), "EEE, dd MMM yyyy HH:mm:ss z");
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + tweet.toString(), jsone);
        }

    }

    /**
     * returns the text
     * @return the text
     */

    public String getText() {
        return text;
    }

    /**
     * returns the to_user_id
     * @return the to_user_id value or -1 if to_user_id is not specified by the tweet
     */
    public int getToUserId() {
        return toUserId;
    }

    /**
     * returns the to_user
     * @return the to_user value or null if to_user is not specified by the tweet
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * returns the from_user
     * @return the from_user
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * returns the status id of the tweet
     * @return the status id
     */
    public long getId() {
        return id;
    }

    /**
     * returns the user id of the tweet's owner.<br>
     * <font color="orange">Warning:</a> The user ids in the Search API are different from those in the REST API (about the two APIs). This defect is being tracked by Issue 214. This means that the to_user_id and from_user_id field vary from the actualy user id on Twitter.com. Applications will have to perform a screen name-based lookup with the users/show method to get the correct user id if necessary.
     * @return the user id of the tweet's owner
     * @see <a href="http://code.google.com/p/twitter-api/issues/detail?id=214">Issue 214:	Search API "from_user_id" doesn't match up with the proper Twitter "user_id"</a>
     */
    public int getFromUserId() {
        return fromUserId;
    }

    /**
     * returns the iso language code of the tweet
     * @return the iso language code of the tweet or null if iso_language_code is not specified by the tweet
     */
    public String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    /**
     * returns the source of the tweet
     * @return the source of the tweet
     */
    public String getSource() {
        return source;
    }

    /**
     * returns the profile_image_url
     * @return the profile_image_url
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    /**
     * returns the created_at
     * @return the created_at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet that = (Tweet) o;

        if (fromUserId != that.fromUserId) return false;
        if (id != that.id) return false;
        if (toUserId != that.toUserId) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null)
            return false;
        if (fromUser != null ? !fromUser.equals(that.fromUser) : that.fromUser != null)
            return false;
        if (isoLanguageCode != null ? !isoLanguageCode.equals(that.isoLanguageCode) : that.isoLanguageCode != null)
            return false;
        if (profileImageUrl != null ? !profileImageUrl.equals(that.profileImageUrl) : that.profileImageUrl != null)
            return false;
        if (source != null ? !source.equals(that.source) : that.source != null)
            return false;
        if (text != null ? !text.equals(that.text) : that.text != null)
            return false;
        if (toUser != null ? !toUser.equals(that.toUser) : that.toUser != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (int) (toUserId ^ (toUserId >>> 32));
        result = 31 * result + (toUser != null ? toUser.hashCode() : 0);
        result = 31 * result + (fromUser != null ? fromUser.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (fromUserId ^ (fromUserId >>> 32));
        result = 31 * result + (isoLanguageCode != null ? isoLanguageCode.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (profileImageUrl != null ? profileImageUrl.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tweet{" +
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
                '}';
    }
}
