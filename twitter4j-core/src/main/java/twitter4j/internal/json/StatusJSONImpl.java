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
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;

import static twitter4j.internal.util.z_T4JInternalParseUtil.getBoolean;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getDate;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getLong;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getUnescapedString;

/**
 * A data class representing one single status of a user.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class StatusJSONImpl extends TwitterResponseImpl implements Status, java.io.Serializable {
    private static final Logger logger = Logger.getLogger(StatusJSONImpl.class);
    private static final long serialVersionUID = 7548618898682727465L;

    private Date createdAt;
    private long id;
    private String text;
    private String source;
    private boolean isTruncated;
    private long inReplyToStatusId;
    private long inReplyToUserId;
    private boolean isFavorited;
    private String inReplyToScreenName;
    private GeoLocation geoLocation = null;
    private Place place = null;
    private long retweetCount;
    private boolean wasRetweetedByMe;

    private String[] contributors = null;
    private long[] contributorsIDs;
    private Annotations annotations = null;

    private Status retweetedStatus;
    private UserMentionEntity[] userMentionEntities;
    private URLEntity[] urlEntities;
    private HashtagEntity[] hashtagEntities;
    private MediaEntity[] mediaEntities;
    private Status myRetweetedStatus;

    /*package*/StatusJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    /*package*/ StatusJSONImpl(JSONObject json) throws TwitterException {
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
        inReplyToUserId = getLong("in_reply_to_user_id", json);
        isFavorited = getBoolean("favorited", json);
        inReplyToScreenName = getUnescapedString("in_reply_to_screen_name", json);
        retweetCount = getLong("retweet_count", json);
        try {
            if (!json.isNull("user")) {
                user = new UserJSONImpl(json.getJSONObject("user"));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        geoLocation = z_T4JInternalJSONImplFactory.createGeoLocation(json);
        if (!json.isNull("place")) {
            try {
                place = new PlaceJSONImpl(json.getJSONObject("place"));
            } catch (JSONException ignore) {
                ignore.printStackTrace();
                logger.warn("failed to parse place:" + json);
            }
        }

        if (!json.isNull("retweeted_status")) {
            try {
                retweetedStatus = new StatusJSONImpl(json.getJSONObject("retweeted_status"));
            } catch (JSONException ignore) {
                ignore.printStackTrace();
                logger.warn("failed to parse retweeted_status:" + json);
            }
        }
        if (!json.isNull("contributors")) {
            try {
                JSONArray contributorsArray = json.getJSONArray("contributors");
                contributorsIDs = new long[contributorsArray.length()];
                for (int i = 0; i < contributorsArray.length(); i++) {
                    contributorsIDs[i] = Long.parseLong(contributorsArray.getString(i));
                }
            } catch (NumberFormatException ignore) {
                ignore.printStackTrace();
                logger.warn("failed to parse contributors:" + json);
            } catch (JSONException ignore) {
                ignore.printStackTrace();
                logger.warn("failed to parse contributors:" + json);
            }
        } else {
            contributors = null;
        }
        if (!json.isNull("entities")) {
            try {
                JSONObject entities = json.getJSONObject("entities");
                int len;
                if (!entities.isNull("user_mentions")) {
                    JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
                    len = userMentionsArray.length();
                    userMentionEntities = new UserMentionEntity[len];
                    for (int i = 0; i < len; i++) {
                        userMentionEntities[i] = new UserMentionEntityJSONImpl(userMentionsArray.getJSONObject(i));
                    }

                }
                if (!entities.isNull("urls")) {
                    JSONArray urlsArray = entities.getJSONArray("urls");
                    len = urlsArray.length();
                    urlEntities = new URLEntity[len];
                    for (int i = 0; i < len; i++) {
                        urlEntities[i] = new URLEntityJSONImpl(urlsArray.getJSONObject(i));
                    }
                }

                if (!entities.isNull("hashtags")) {
                    JSONArray hashtagsArray = entities.getJSONArray("hashtags");
                    len = hashtagsArray.length();
                    hashtagEntities = new HashtagEntity[len];
                    for (int i = 0; i < len; i++) {
                        hashtagEntities[i] = new HashtagEntityJSONImpl(hashtagsArray.getJSONObject(i));
                    }
                }

                if (!entities.isNull("media")) {
                    JSONArray mediaArray = entities.getJSONArray("media");
                    len = mediaArray.length();
                    mediaEntities = new MediaEntity[len];
                    for (int i = 0; i < len; i++) {
                        mediaEntities[i] = new MediaEntityJSONImpl(mediaArray.getJSONObject(i));
                    }
                }
            } catch (JSONException jsone) {
                throw new TwitterException(jsone);
            }
        }
        if (!json.isNull("annotations")) {
            try {
                JSONArray annotationsArray = json.getJSONArray("annotations");
                annotations = new Annotations(annotationsArray);
            } catch (JSONException ignore) {
            }
        }
        if (!json.isNull("current_user_retweet")) {
            try {
                myRetweetedStatus = new StatusJSONImpl(json.getJSONObject("current_user_retweet"));
                wasRetweetedByMe = true;
            } catch (JSONException ignore) {
                ignore.printStackTrace();
                logger.warn("failed to parse current_user_retweet:" + json);
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
    public long getInReplyToUserId() {
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
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    /**
     * {@inheritDoc}
     */
    public Place getPlace() {
        return place;
    }

    /**
     * {@inheritDoc}
     */
    public long[] getContributors() {
        if (contributors != null) {
            // http://twitter4j.org/jira/browse/TFJ-592
            // preserving serialized form compatibility with older versions
            contributorsIDs = new long[contributors.length];
            for (int i = 0; i < contributors.length; i++) {
                try {
                    contributorsIDs[i] = Long.parseLong(contributors[i]);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    logger.warn("failed to parse contributors:" + nfe);
                }
            }
            contributors = null;
        }
        return contributorsIDs;
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
    public boolean isRetweet() {
        return retweetedStatus != null;
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
    public UserMentionEntity[] getUserMentionEntities() {
        return userMentionEntities;
    }

    /**
     * {@inheritDoc}
     */
    public URLEntity[] getURLEntities() {
        return urlEntities;
    }

    /**
     * {@inheritDoc}
     */
    public HashtagEntity[] getHashtagEntities() {
        return hashtagEntities;
    }

    /**
     * {@inheritDoc}
     */
    public MediaEntity[] getMediaEntities() {
        return mediaEntities;
    }

    /*package*/
    static ResponseList<Status> createStatusList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Status> statuses = new ResponseListImpl<Status>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Status status = new StatusJSONImpl(json);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(status, json);
                }
                statuses.add(status);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(statuses, list);
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
                ", retweetCount=" + retweetCount +
                ", wasRetweetedByMe=" + wasRetweetedByMe +
                ", contributors=" + (contributorsIDs == null ? null : Arrays.asList(contributorsIDs)) +
                ", annotations=" + annotations +
                ", retweetedStatus=" + retweetedStatus +
                ", userMentionEntities=" + (userMentionEntities == null ? null : Arrays.asList(userMentionEntities)) +
                ", urlEntities=" + (urlEntities == null ? null : Arrays.asList(urlEntities)) +
                ", hashtagEntities=" + (hashtagEntities == null ? null : Arrays.asList(hashtagEntities)) +
                ", user=" + user +
                '}';
    }
}
