/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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

import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import static twitter4j.internal.util.ParseUtil.getBoolean;
import static twitter4j.internal.util.ParseUtil.getDate;
import static twitter4j.internal.util.ParseUtil.getInt;
import static twitter4j.internal.util.ParseUtil.getLong;
import static twitter4j.internal.util.ParseUtil.getUnescapedString;

/**
 * A data class representing one single status of a user.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class StatusJSONImpl extends TwitterResponseImpl implements Status, java.io.Serializable {

    private Date createdAt;
    private long id;
    private String text;
    private String source;
    private boolean isTruncated;
    private long inReplyToStatusId;
    private int inReplyToUserId;
    private boolean isFavorited;
    private String inReplyToScreenName;
    private GeoLocation geoLocation = null;
    private Place place = null;
    private long retweetCount;
    private boolean wasRetweetedByMe;

    private String[] contributors;
    private Annotations annotations = null;

    private Status retweetedStatus;
    private User[] userMentions;
    private URL[] urls;
    private String[] hashtags;

    private static final long serialVersionUID = 1608000492860584608L;

    /*package*/StatusJSONImpl(HttpResponse res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }

    /*package*/ StatusJSONImpl(JSONObject json) throws TwitterException, JSONException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        id = getLong("id", json);
        text = getUnescapedString("text", json);
        source = getUnescapedString("source", json);
        createdAt = getDate("created_at", json);
        isTruncated = getBoolean("truncated", json);
        inReplyToStatusId = getLong("in_reply_to_status_id", json);
        inReplyToUserId = getInt("in_reply_to_user_id", json);
        isFavorited = getBoolean("favorited", json);
        inReplyToScreenName = getUnescapedString("in_reply_to_screen_name", json);
        retweetCount = getLong("retweet_count", json);
        wasRetweetedByMe = getBoolean("retweeted", json);
        try {
            if (!json.isNull("user")) {
                user = new UserJSONImpl(json.getJSONObject("user"));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        geoLocation = GeoLocation.getInstance(json);
        if (!json.isNull("place")) {
            try {
                place = new PlaceJSONImpl(json.getJSONObject("place"), null);
            } catch (JSONException ignore) {
            }
        }

        if (!json.isNull("retweeted_status")) {
            try {
                retweetedStatus = new StatusJSONImpl(json.getJSONObject("retweeted_status"));
            } catch (JSONException ignore) {
            }
        }
        if (!json.isNull("contributors")) {
            try {
                JSONArray contributorsArray = json.getJSONArray("contributors");
                contributors = new String[contributorsArray.length()];
                for(int i=0;i<contributorsArray.length();i++){
                    contributors[i] = contributorsArray.getString(i);
                }
            } catch (JSONException ignore) {
            }
        } else{
            contributors = null;
        }
        if (!json.isNull("entities")) {
            try {
                JSONObject entities = json.getJSONObject("entities");

                JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
                userMentions = new User[userMentionsArray.length()];
                for(int i=0;i<userMentionsArray.length();i++){
                    userMentions[i] = new UserJSONImpl(userMentionsArray.getJSONObject(i));
                }

                JSONArray urlArray = entities.getJSONArray("urls");
                urls = new URL[urlArray.length()];
                for(int i=0;i<urlArray.length();i++){
                    try {
                        urls[i] = new URL(urlArray.getJSONObject(i).getString("url"));
                    } catch (MalformedURLException e) {
                        urls[i] = null;
                    }
                }

                JSONArray hashtagsArray = entities.getJSONArray("hashtags");
                hashtags = new String[hashtagsArray.length()];
                for(int i=0;i<hashtagsArray.length();i++){
                    hashtags[i] = hashtagsArray.getJSONObject(i).getString("text");
                }
            } catch (JSONException ignore) {
            }
        }
        if (!json.isNull("annotations")) {
            try {
                JSONArray annotationsArray = json.getJSONArray("annotations");
                annotations = new Annotations(annotationsArray);
            } catch (JSONException ignore) {
            }
        }
    }

    public int compareTo(Status that) {
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
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * {@inheritDoc}
     */
    public long getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return this.text;
    }

    /**
     * {@inheritDoc}
     */
    public String getSource() {
        return this.source;
    }


    /**
     * {@inheritDoc}
     */
    public boolean isTruncated() {
        return isTruncated;
    }

    /**
     * {@inheritDoc}
     */
    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    /**
     * {@inheritDoc}
     */
    public int getInReplyToUserId() {
        return inReplyToUserId;
    }

    /**
     * {@inheritDoc}
     */
    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    /**
     * {@inheritDoc}
     */
    public GeoLocation getGeoLocation(){
        return geoLocation;
    }

    /**
     * {@inheritDoc}
     */
    public Place getPlace(){
        return place;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getContributors() {
        return contributors;
    }

    
	/**
     * {@inheritDoc}
     */
    public Annotations getAnnotations() {
		return annotations;
	}

	/**
     * {@inheritDoc}
     */
    public boolean isFavorited() {
        return isFavorited;
    }


    private User user = null;

    /**
     * {@inheritDoc}
     */
    public User getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRetweet(){
        return null != retweetedStatus;
    }

    /**
     * {@inheritDoc}
     */
    public Status getRetweetedStatus() {
        return retweetedStatus;
    }

    /**
     * {@inheritDoc}
     */
    public long getRetweetCount() {
        return retweetCount;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRetweetedByMe() {
        return wasRetweetedByMe;
    }

    /**
     * {@inheritDoc}
     */
    public User[] getUserMentions() {
        return userMentions;
    }

    /**
     * {@inheritDoc}
     */
    public URL[] getURLs() {
        return urls;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getHashtags() {
        return hashtags;
    }

    /*package*/ static ResponseList<Status> createStatusList(HttpResponse res) throws TwitterException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Status> statuses = new ResponseListImpl<Status>(size, res);
            for (int i = 0; i < size; i++) {
                statuses.add(new StatusJSONImpl(list.getJSONObject(i)));
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
        return obj instanceof Status && ((Status) obj).getId() == this.id;
    }

    @Override
    public String toString() {
        return "StatusJSONImpl{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", isTruncated=" + isTruncated +
                ", inReplyToStatusId=" + inReplyToStatusId +
                ", inReplyToUserId=" + inReplyToUserId +
                ", isFavorited=" + isFavorited +
                ", inReplyToScreenName='" + inReplyToScreenName + '\'' +
                ", geoLocation=" + geoLocation +
                ", place=" + place +
                ", contributors=" + (contributors == null ? null : Arrays.asList(contributors)) +
                ", annotations=" + annotations +
                ", retweetedStatus=" + retweetedStatus +
                ", user=" + user +
                '}';
    }
}
