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

import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import static twitter4j.internal.util.ParseUtil.getBoolean;
import static twitter4j.internal.util.ParseUtil.getInt;
import static twitter4j.internal.util.ParseUtil.getUnescapedString;

/**
 * A data class that has detailed information about a relationship between two users
 *
 * @author Perry Sakkaris - psakkaris at gmail.com
 * @see <a href="http://dev.twitter.com/doc/get/friendships/show">GET friendships/show | dev.twitter.com</a>
 * @since Twitter4J 2.1.0
 */
/*package*/ class RelationshipJSONImpl extends TwitterResponseImpl implements Relationship, java.io.Serializable {

    private final int targetUserId;
    private final String targetUserScreenName;
    private final boolean sourceBlockingTarget;
    private final boolean sourceNotificationsEnabled;
    private final boolean sourceFollowingTarget;
    private final boolean sourceFollowedByTarget;
    private final int sourceUserId;
    private final String sourceUserScreenName;
    private static final long serialVersionUID = 697705345506281849L;

    /*package*/ RelationshipJSONImpl(HttpResponse res) throws TwitterException {
        this(res, res.asJSONObject());
        DataObjectFactoryUtil.clearThreadLocalMap();
        DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
    }

    /*package*/ RelationshipJSONImpl(JSONObject json) throws TwitterException {
        this(null, json);
    }

    /*package*/ RelationshipJSONImpl(HttpResponse res, JSONObject json) throws TwitterException {
        super(res);
        try {
            JSONObject relationship = json.getJSONObject("relationship");
            JSONObject sourceJson = relationship.getJSONObject("source");
            JSONObject targetJson = relationship.getJSONObject("target");
            sourceUserId = getInt("id", sourceJson);
            targetUserId = getInt("id", targetJson);
            sourceUserScreenName = getUnescapedString("screen_name", sourceJson);
            targetUserScreenName = getUnescapedString("screen_name", targetJson);
            sourceBlockingTarget = getBoolean("blocking", sourceJson);
            sourceFollowingTarget = getBoolean("following", sourceJson);
            sourceFollowedByTarget = getBoolean("followed_by", sourceJson);
            sourceNotificationsEnabled = getBoolean("notifications_enabled", sourceJson);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /*package*/
    static ResponseList<Relationship> createRelationshipList(HttpResponse res) throws TwitterException {
        try {
            DataObjectFactoryUtil.clearThreadLocalMap();
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Relationship> relationships = new ResponseListImpl<Relationship>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Relationship relationship = new RelationshipJSONImpl(json);
                DataObjectFactoryUtil.registerJSONObject(relationship, json);
                relationships.add(relationship);
            }
            DataObjectFactoryUtil.registerJSONObject(relationships, list);
            return relationships;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }


    /**
     * {@inheritDoc}
     */
    public int getSourceUserId() {
        return sourceUserId;
    }

    /**
     * {@inheritDoc}
     */
    public int getTargetUserId() {
        return targetUserId;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSourceBlockingTarget() {
        return sourceBlockingTarget;
    }

    /**
     * {@inheritDoc}
     */
    public String getSourceUserScreenName() {
        return sourceUserScreenName;
    }

    /**
     * {@inheritDoc}
     */
    public String getTargetUserScreenName() {
        return targetUserScreenName;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSourceFollowingTarget() {
        return sourceFollowingTarget;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTargetFollowingSource() {
        return sourceFollowedByTarget;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSourceFollowedByTarget() {
        return sourceFollowedByTarget;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTargetFollowedBySource() {
        return sourceFollowingTarget;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSourceNotificationsEnabled() {
        return sourceNotificationsEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relationship)) return false;

        Relationship that = (Relationship) o;

        if (sourceUserId != that.getSourceUserId()) return false;
        if (targetUserId != that.getTargetUserId()) return false;
        if (!sourceUserScreenName.equals(that.getSourceUserScreenName()))
            return false;
        if (!targetUserScreenName.equals(that.getTargetUserScreenName()))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceUserId;
        result = 31 * result + targetUserId;
        result = 31 * result + sourceUserScreenName.hashCode();
        result = 31 * result + targetUserScreenName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RelationshipJSONImpl{" +
                "sourceUserId=" + sourceUserId +
                ", targetUserId=" + targetUserId +
                ", sourceUserScreenName='" + sourceUserScreenName + '\'' +
                ", targetUserScreenName='" + targetUserScreenName + '\'' +
                ", sourceFollowingTarget=" + sourceFollowingTarget +
                ", sourceFollowedByTarget=" + sourceFollowedByTarget +
                ", sourceNotificationsEnabled=" + sourceNotificationsEnabled +
                '}';
    }
}