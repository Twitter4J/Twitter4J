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

import twitter4j.http.HttpResponse;
import twitter4j.org.json.JSONObject;
import twitter4j.org.json.JSONException;


import static twitter4j.ParseUtil.*;
/**
 * A data class that has detailed information about a relationship between two users
 * @author Perry Sakkaris - psakkaris at gmail.com
 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">REST API DOCUMENTATION</a>
 * @since Twitter4J 2.1.0
 */
/*package*/ final class RelationshipJSONImpl extends TwitterResponseImpl implements Relationship, java.io.Serializable {

    private int targetUserId;
    private String targetUserScreenName;
    private boolean sourceBlockingTarget;
    private boolean sourceNotificationsEnabled;
    private boolean sourceFollowingTarget;
    private boolean sourceFollowedByTarget;
    private int sourceUserId;
    private String sourceUserScreenName;
    private static final long serialVersionUID = 697705345506281849L;

    /*package*/ RelationshipJSONImpl(HttpResponse res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }

    /*package*/ RelationshipJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
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