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

import twitter4j.http.Response;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A data class representing one single status of a user.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Status extends TwitterResponseImpl implements java.io.Serializable {

    private Date createdAt;
    private long id;
    private String text;
    private String source;
    private boolean isTruncated;
    private long inReplyToStatusId;
    private int inReplyToUserId;
    private boolean isFavorited;
    private String inReplyToScreenName;
    private double latitude = -1;
    private double longitude = -1;
    
    private Status retweetedStatus;
    private static final long serialVersionUID = 1608000492860584608L;

    /*package*/Status(Response res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }

    /*package*/Status(Response res, JSONObject json) throws
            TwitterException {
        super(res);
        init(json);
    }

    /*package*/ Status(JSONObject json) throws TwitterException, JSONException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        id = getChildLong("id", json);
        text = getChildText("text", json);
        source = getChildText("source", json);
        createdAt = getChildDate("created_at", json);
        isTruncated = getChildBoolean("truncated", json);
        inReplyToStatusId = getChildLong("in_reply_to_status_id", json);
        inReplyToUserId = getChildInt("in_reply_to_user_id", json);
        isFavorited = getChildBoolean("favorited", json);
        inReplyToScreenName = getChildText("in_reply_to_screen_name", json);
        try {
            if (!json.isNull("user")) {
                user = new User(json.getJSONObject("user"));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        try {
            if (!json.isNull("geo")) {
                String coordinates = json.getJSONObject("geo")
                        .getString("coordinates");
                coordinates = coordinates.substring(1, coordinates.length() - 1);
                String[] point = coordinates.split(",");
                latitude = Double.parseDouble(point[0]);
                longitude = Double.parseDouble(point[1]);
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        if (!json.isNull("retweeted_status")) {
            try {
                retweetedStatus = new Status(json.getJSONObject("retweeted_status"));
            } catch (JSONException ignore) {
            }
        }
    }

    /**
     * Return the created_at
     *
     * @return created_at
     * @since Twitter4J 1.1.0
     */

    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Returns the id of the status
     *
     * @return the id
     */
    public long getId() {
        return this.id;
    }

    /**
     * Returns the text of the status
     *
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Returns the source
     *
     * @return the source
     * @since Twitter4J 1.0.4
     */
    public String getSource() {
        return this.source;
    }


    /**
     * Test if the status is truncated
     *
     * @return true if truncated
     * @since Twitter4J 1.0.4
     */
    public boolean isTruncated() {
        return isTruncated;
    }

    /**
     * Returns the in_reply_tostatus_id
     *
     * @return the in_reply_tostatus_id
     * @since Twitter4J 1.0.4
     */
    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    /**
     * Returns the in_reply_user_id
     *
     * @return the in_reply_tostatus_id
     * @since Twitter4J 1.0.4
     */
    public int getInReplyToUserId() {
        return inReplyToUserId;
    }

    /**
     * Returns the in_reply_to_screen_name
     *
     * @return the in_in_reply_to_screen_name
     * @since Twitter4J 2.0.4
     */
    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    /**
     * returns The location's latitude that this tweet refers to.
     * @since Twitter4J 2.0.10
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * returns The location's longitude that this tweet refers to.
     * @since Twitter4J 2.0.10
     */
    public double getLongitude(){
        return longitude;
    }

    /**
     * Test if the status is favorited
     *
     * @return true if favorited
     * @since Twitter4J 1.0.4
     */
    public boolean isFavorited() {
        return isFavorited;
    }


    private User user = null;

    /**
     * Return the user
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @since Twitter4J 2.0.10
     */
    public boolean isRetweet(){
        return null != retweetedStatus;
    }

    /**
     *
     * @since Twitter4J 2.1.0
     */
    public Status getRetweetedStatus() {
        return retweetedStatus;
    }

    /*package*/ static ResponseList<Status> createStatusList(Response res) throws TwitterException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Status> statuses = new ResponseList<Status>(size, res);
            for (int i = 0; i < size; i++) {
                statuses.add(new Status(res, list.getJSONObject(i)));
            }
            return statuses;
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
        return obj instanceof Status && ((Status) obj).id == this.id;
    }

    @Override
    public String toString() {
        return "Status{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", isTruncated=" + isTruncated +
                ", inReplyToStatusId=" + inReplyToStatusId +
                ", inReplyToUserId=" + inReplyToUserId +
                ", isFavorited=" + isFavorited +
                ", inReplyToScreenName='" + inReplyToScreenName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", retweetedStatus=" + retweetedStatus +
                ", user=" + user +
                '}';
    }
}
