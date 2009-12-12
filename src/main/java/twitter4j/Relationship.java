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
import twitter4j.org.json.JSONObject;
import twitter4j.org.json.JSONException;

/**
 * A class that has detailed information about a relationship between two users
 * @author Perry Sakkaris - psakkaris at gmail.com
 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">REST API DOCUMENTATION</a>
 * @since Twitter4J 2.1.0
 */
public class Relationship extends TwitterResponseImpl implements java.io.Serializable {

    private int sourceUserId;
    private int targetUserId;
    private String sourceUserScreenName;
    private String targetUserScreenName;
    private boolean sourceFollowingTarget;
    private boolean sourceFollowedByTarget;
    private boolean sourceNotificationsEnabled;
    private static final long serialVersionUID = 697705345506281849L;

    /*package*/

    Relationship(Response res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }

    /*package*/

    Relationship(Response res, JSONObject json) throws TwitterException {
        super(res);
        init(json);
    }

    /*package*/

    Relationship(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONObject relationship = json.getJSONObject("relationship");
            JSONObject sourceJson = relationship.getJSONObject("source");
            JSONObject targetJson = relationship.getJSONObject("target");
            sourceUserId = getChildInt("id", sourceJson);
            targetUserId = getChildInt("id", targetJson);
            sourceUserScreenName = getChildText("screen_name", sourceJson);
            targetUserScreenName = getChildText("screen_name", targetJson);
            sourceFollowingTarget = getChildBoolean("following", sourceJson);
            sourceFollowedByTarget = getChildBoolean("followed_by", sourceJson);
            sourceNotificationsEnabled = getChildBoolean("notifications_enabled", sourceJson);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /**
     * Returns the source user id
     *
     * @return the source user id
     */
    public int getSourceUserId() {
        return sourceUserId;
    }

    /**
     * Returns the target user id
     *
     * @return target user id
     */
    public int getTargetUserId() {
        return targetUserId;
    }

    /**
     * Returns the source user screen name
     *
     * @return returns the source user screen name
     */
    public String getSourceUserScreenName() {
        return sourceUserScreenName;
    }

    /**
     * Returns the target user screen name
     *
     * @return the target user screen name
     */
    public String getTargetUserScreenName() {
        return targetUserScreenName;
    }

    /**
     * Checks if source user is following target user
     *
     * @return true if source user is following target user
     */
    public boolean isSourceFollowingTarget() {
        return sourceFollowingTarget;
    }

    /**
     * Checks if target user is following source user.<br>
     * This method is equivalent to isSourceFollowedByTarget().
     *
     * @return true if target user is following source user
     */
    public boolean isTargetFollowingSource() {
        return sourceFollowedByTarget;
    }

    /**
     * Checks if source user is being followed by target user
     *
     * @return true if source user is being followed by target user
     */
    public boolean isSourceFollowedByTarget() {
        return sourceFollowedByTarget;
    }

    /**
     * Checks if target user is being followed by source user.<br>
     * This method is equivalent to isSourceFollowingTarget().
     *
     * @return true if target user is being followed by source user
     */
    public boolean isTargetFollowedBySource() {
        return sourceFollowingTarget;
    }

    /**
     * Checks if the source user has enabled notifications for updates of the target user
     *
     * @return true if source user enabled notifications for target user
     */
    public boolean isSourceNotificationsEnabled() {
        return sourceNotificationsEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relationship)) return false;

        Relationship that = (Relationship) o;

        if (sourceUserId != that.sourceUserId) return false;
        if (targetUserId != that.targetUserId) return false;
        if (!sourceUserScreenName.equals(that.sourceUserScreenName))
            return false;
        if (!targetUserScreenName.equals(that.targetUserScreenName))
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
        return "Relationship{" +
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