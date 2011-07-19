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
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;

import static twitter4j.internal.util.ParseUtil.getBoolean;
import static twitter4j.internal.util.ParseUtil.getDate;
import static twitter4j.internal.util.ParseUtil.getLong;
import static twitter4j.internal.util.ParseUtil.getUnescapedString;

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
    private String retweetCount;
    private boolean wasRetweetedByMe;

    private String[] contributors;
    private Annotations annotations = null;

    private Status retweetedStatus;
    private UserMentionEntity[] userMentionEntities;
    private URLEntity[] urlEntities;
    private HashtagEntity[] hashtagEntities;

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
    	if (json==null)
    		throw new TwitterException("trying to create a null StatusJSONImpl");
        id = getLong("id", json);
        text = getUnescapedString("text", json);
        source = getUnescapedString("source", json);
        createdAt = getDate("created_at", json);
        isTruncated = getBoolean("truncated", json);
        inReplyToStatusId = getLong("in_reply_to_status_id", json);
        inReplyToUserId = getLong("in_reply_to_user_id", json);
        isFavorited = getBoolean("favorited", json);
        inReplyToScreenName = getUnescapedString("in_reply_to_screen_name", json);
        retweetCount = getUnescapedString("retweet_count", json);
        wasRetweetedByMe = getBoolean("retweeted", json);
        try {
            if (!json.isNull("user")) {
            	JSONObject u = json.getJSONObject("user");
            	if (u!=null)
            		user = new UserJSONImpl(u);
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        geoLocation = GeoLocation.getInstance(json);
        if (!json.isNull("place")) {
            try {
<<<<<<< HEAD
                place = new PlaceJSONImpl(json.getJSONObject("place"));
=======
            	JSONObject p = json.getJSONObject("place");
            	if (p!=null)
            		place = new PlaceJSONImpl(p, null);
>>>>>>> Branch_2.1.4
            } catch (JSONException ignore) {
                ignore.printStackTrace();
                logger.warn("failed to parse place:" + json);
            }
        }

        if (!json.isNull("retweeted_status")) {
            try {
            	JSONObject rts = json.getJSONObject("retweeted_status");
            	if (rts!=null)
            		retweetedStatus = new StatusJSONImpl(rts);
            } catch (JSONException ignore) {
                ignore.printStackTrace();
                logger.warn("failed to parse retweeted_status:" + json);
            }
        }
        if (!json.isNull("contributors")) {
            try {
                JSONArray contributorsArray = json.getJSONArray("contributors");
                contributors = new String[contributorsArray.length()];
                for (int i = 0; i < contributorsArray.length(); i++) {
                    contributors[i] = contributorsArray.getString(i);
                }
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

                JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
                int len = userMentionsArray.length();
                userMentionEntities = new UserMentionEntity[len];
                for (int i = 0; i < len; i++) {
                    userMentionEntities[i] = new UserMentionEntityJSONImpl(userMentionsArray.getJSONObject(i));
                }

                JSONArray urlsArray = entities.getJSONArray("urls");
                len = urlsArray.length();
                urlEntities = new URLEntity[len];
                for (int i = 0; i < len; i++) {
                    urlEntities[i] = new URLEntityJSONImpl(urlsArray.getJSONObject(i));
                }

                JSONArray hashtagsArray = entities.getJSONArray("hashtags");
                len = hashtagsArray.length();
                hashtagEntities = new HashtagEntity[len];
                for (int i = 0; i < len; i++) {
                    hashtagEntities[i] = new HashtagEntityJSONImpl(hashtagsArray.getJSONObject(i));
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
    public boolean isRetweet() {
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
    public String getRetweetCount() {
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

<<<<<<< HEAD
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
=======
    /*package*/ static ResponseList<Status> createStatusList(HttpResponse res) throws TwitterException {
    	try {
    		JSONArray list = res.asJSONArray();
    		int size = list.length();
    		ResponseList<Status> statuses = new ResponseListImpl<Status>(list.getJSONObjectCount(), res);
    		for (int i = 0; i < size; i++) {
    			JSONObject json;
    			try {
    				json = list.getJSONObject(i);
    			} catch (JSONException e) {
    				continue;
    			}
    			if (json!=null)
    				statuses.add(new StatusJSONImpl(json));
    		}
    		return statuses;
    	} catch (JSONException jsone) {
    		throw new TwitterException(jsone);
    	} catch (TwitterException te) {
    		throw te;
    	}
>>>>>>> Branch_2.1.4
    }

    /*package*/ static ResponseList<Status> createResultStatusList(HttpResponse res) throws TwitterException {
    	try {
    		JSONArray results = res.asJSONArray();
			ResponseList<Status> statuses = new ResponseListImpl<Status>(0, res);

			for (int j=0; j < results.getJSONObjectCount(); ++j) {
				JSONObject obj1 = results.getJSONObject(j);

				//String kind = obj1.getString("groupName");
				//if (kind.equalsIgnoreCase("TweetsWithReply")) {
					JSONArray list = obj1.getJSONArray("results");
					int size = list.length();
					for (int i = 0; i < size; i++) {
						JSONObject json;
						double score;
						try {
							json = list.getJSONObject(i);
							score = json.getDouble("score");
							json = json.getJSONObject("value");
						} catch (JSONException e) {
							continue;
						}
						if (score==1.0 && json!=null)
							statuses.add(new StatusJSONImpl(json));
					}
				//}
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
                ", contributors=" + (contributors == null ? null : Arrays.asList(contributors)) +
                ", annotations=" + annotations +
                ", retweetedStatus=" + retweetedStatus +
                ", userMentionEntities=" + (userMentionEntities == null ? null : Arrays.asList(userMentionEntities)) +
                ", urlEntities=" + (urlEntities == null ? null : Arrays.asList(urlEntities)) +
                ", hashtagEntities=" + (hashtagEntities == null ? null : Arrays.asList(hashtagEntities)) +
                ", user=" + user +
                '}';
    }
}
