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

/**
 * A data class that has detailed information about a relationship between two users
 *
 * @author Perry Sakkaris - psakkaris at gmail.com
 * @see <a href="https://dev.twitter.com/docs/api/1.1/get/friendships/show">GET friendships/show | Twitter Developers</a>
 * @since Twitter4J 2.1.0
 */
/*package*/ class RelationshipJSONImpl extends TwitterResponseImpl implements Relationship, java.io.Serializable {

    private static final long serialVersionUID = -2001484553401916448L;
    private final long targetUserId;
    private final String targetUserScreenName;
    private final boolean sourceBlockingTarget;
    private final boolean sourceNotificationsEnabled;
    private final boolean sourceFollowingTarget;
    private final boolean sourceFollowedByTarget;
    private final boolean sourceCanDm;
    private final boolean sourceMutingTarget;
    private final long sourceUserId;
    private final String sourceUserScreenName;
    private boolean wantRetweets;

    /*package*/ RelationshipJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res, res.asJSONObject());
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
            TwitterObjectFactory.registerJSONObject(this, res.asJSONObject());
        }
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
            sourceUserId = ParseUtil.getLong("id", sourceJson);
            targetUserId = ParseUtil.getLong("id", targetJson);
            sourceUserScreenName = ParseUtil.getUnescapedString("screen_name", sourceJson);
            targetUserScreenName = ParseUtil.getUnescapedString("screen_name", targetJson);
            sourceBlockingTarget = ParseUtil.getBoolean("blocking", sourceJson);
            sourceFollowingTarget = ParseUtil.getBoolean("following", sourceJson);
            sourceFollowedByTarget = ParseUtil.getBoolean("followed_by", sourceJson);
            sourceCanDm = ParseUtil.getBoolean("can_dm", sourceJson);
            sourceMutingTarget = ParseUtil.getBoolean("muting", sourceJson);
            sourceNotificationsEnabled = ParseUtil.getBoolean("notifications_enabled", sourceJson);
            wantRetweets = ParseUtil.getBoolean("want_retweets", sourceJson);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /*package*/
    static ResponseList<Relationship> createRelationshipList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Relationship> relationships = new ResponseListImpl<Relationship>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Relationship relationship = new RelationshipJSONImpl(json);
                if (conf.isJSONStoreEnabled()) {
                    TwitterObjectFactory.registerJSONObject(relationship, json);
                }
                relationships.add(relationship);
            }
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.registerJSONObject(relationships, list);
            }
            return relationships;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }


    @Override
    public long getSourceUserId() {
        return sourceUserId;
    }

    @Override
    public long getTargetUserId() {
        return targetUserId;
    }

    @Override
    public boolean isSourceBlockingTarget() {
        return sourceBlockingTarget;
    }
    
    @Override
    public String getSourceUserScreenName() {
        return sourceUserScreenName;
    }

    @Override
    public String getTargetUserScreenName() {
        return targetUserScreenName;
    }

    @Override
    public boolean isSourceFollowingTarget() {
        return sourceFollowingTarget;
    }

    @Override
    public boolean isTargetFollowingSource() {
        return sourceFollowedByTarget;
    }

    @Override
    public boolean isSourceFollowedByTarget() {
        return sourceFollowedByTarget;
    }

    @Override
    public boolean isTargetFollowedBySource() {
        return sourceFollowingTarget;
    }

    @Override
    public boolean canSourceDm() {
        return sourceCanDm;
    }

    @Override
    public boolean isSourceMutingTarget() {
        return sourceMutingTarget;
    }

    @Override
    public boolean isSourceNotificationsEnabled() {
        return sourceNotificationsEnabled;
    }

    @Override
    public boolean isSourceWantRetweets() {
        return wantRetweets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipJSONImpl that = (RelationshipJSONImpl) o;

        if (sourceBlockingTarget != that.sourceBlockingTarget) return false;
        if (sourceCanDm != that.sourceCanDm) return false;
        if (sourceFollowedByTarget != that.sourceFollowedByTarget) return false;
        if (sourceFollowingTarget != that.sourceFollowingTarget) return false;
        if (sourceMutingTarget != that.sourceMutingTarget) return false;
        if (sourceNotificationsEnabled != that.sourceNotificationsEnabled) return false;
        if (sourceUserId != that.sourceUserId) return false;
        if (targetUserId != that.targetUserId) return false;
        if (wantRetweets != that.wantRetweets) return false;
        if (sourceUserScreenName != null ? !sourceUserScreenName.equals(that.sourceUserScreenName) : that.sourceUserScreenName != null)
            return false;
        if (targetUserScreenName != null ? !targetUserScreenName.equals(that.targetUserScreenName) : that.targetUserScreenName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (targetUserId ^ (targetUserId >>> 32));
        result = 31 * result + (targetUserScreenName != null ? targetUserScreenName.hashCode() : 0);
        result = 31 * result + (sourceBlockingTarget ? 1 : 0);
        result = 31 * result + (sourceNotificationsEnabled ? 1 : 0);
        result = 31 * result + (sourceFollowingTarget ? 1 : 0);
        result = 31 * result + (sourceFollowedByTarget ? 1 : 0);
        result = 31 * result + (sourceCanDm ? 1 : 0);
        result = 31 * result + (sourceMutingTarget ? 1 : 0);
        result = 31 * result + (int) (sourceUserId ^ (sourceUserId >>> 32));
        result = 31 * result + (sourceUserScreenName != null ? sourceUserScreenName.hashCode() : 0);
        result = 31 * result + (wantRetweets ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelationshipJSONImpl{" +
                "targetUserId=" + targetUserId +
                ", targetUserScreenName='" + targetUserScreenName + '\'' +
                ", sourceBlockingTarget=" + sourceBlockingTarget +
                ", sourceNotificationsEnabled=" + sourceNotificationsEnabled +
                ", sourceFollowingTarget=" + sourceFollowingTarget +
                ", sourceFollowedByTarget=" + sourceFollowedByTarget +
                ", sourceCanDm=" + sourceCanDm +
                ", sourceMutingTarget=" + sourceMutingTarget +
                ", sourceUserId=" + sourceUserId +
                ", sourceUserScreenName='" + sourceUserScreenName + '\'' +
                ", wantRetweets=" + wantRetweets +
                '}';
    }
}