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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import twitter4j.http.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A data class representing Basic user information element
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#Basicuserinformationelement">REST API Documentation - Basic user information element</a>
 */
public class User extends TwitterResponse implements java.io.Serializable {

    static final String[] POSSIBLE_ROOT_NAMES = new String[]{"user", "sender", "recipient"};
    private Twitter twitter;
    private int id;
    private String name;
    private String screenName;
    private String location;
    private String description;
    private String profileImageUrl;
    private String url;
    private boolean isProtected;
    private int followersCount;

    private Date statusCreatedAt;
    private long statusId = -1;
    private String statusText = null;
    private String statusSource = null;
    private boolean statusTruncated = false;
    private long statusInReplyToStatusId = -1;
    private int statusInReplyToUserId = -1;
    private boolean statusFavorited = false;
    private String statusInReplyToScreenName = null;

    private static final long serialVersionUID = 3037057798600246529L;

    /*package*/User(Response res, Twitter twitter) throws TwitterException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        init(elem, twitter);
    }

    /*package*/User(Response res, Element elem, Twitter twitter) throws TwitterException {
        super(res);
        init(elem, twitter);
    }

    private void init(Element elem, Twitter twitter) throws TwitterException {
        this.twitter = twitter;
        ensureRootNodeNameIs(POSSIBLE_ROOT_NAMES, elem);
        id = getChildInt("id", elem);
        name = getChildText("name", elem);
        screenName = getChildText("screen_name", elem);
        location = getChildText("location", elem);
        description = getChildText("description", elem);
        profileImageUrl = getChildText("profile_image_url", elem);
        url = getChildText("url", elem);
        isProtected = getChildBoolean("protected", elem);
        followersCount = getChildInt("followers_count", elem);

        NodeList statuses = elem.getElementsByTagName("status");
        if (statuses.getLength() != 0) {
            Element status = (Element) statuses.item(0);
            statusCreatedAt = getChildDate("created_at", status);
            statusId = getChildLong("id", status);
            statusText = getChildText("text", status);
            statusSource = getChildText("source", status);
            statusTruncated = getChildBoolean("truncated", status);
            statusInReplyToStatusId = getChildLong("in_reply_to_status_id", status);
            statusInReplyToUserId = getChildInt("in_reply_to_user_id", status);
            statusFavorited = getChildBoolean("favorited", status);
            statusInReplyToScreenName = getChildText("in_reply_to_screen_name", status);
        }
    }

    /**
     * Returns the id of the user
     *
     * @return the id of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the user
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the screen name of the user
     *
     * @return the screen name of the user
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Returns the location of the user
     *
     * @return the location of the user
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the description of the user
     *
     * @return the description of the user
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the profile image url of the user
     *
     * @return the profile image url of the user
     */
    public URL getProfileImageURL() {
        try {
            return new URL(profileImageUrl);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * Returns the url of the user
     *
     * @return the url of the user
     */
    public URL getURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * Test if the user status is protected
     *
     * @return true if the user status is protected
     */
    public boolean isProtected() {
        return isProtected;
    }


    /**
     * Returns the number of followers
     *
     * @return the number of followers
     * @since Twitter4J 1.0.4
     */
    public int getFollowersCount() {
        return followersCount;
    }

    public DirectMessage sendDirectMessage(String text) throws TwitterException {
        return twitter.sendDirectMessage(this.getName(), text);
    }

    public static List<User> constructUsers(Response res, Twitter twitter) throws TwitterException {
        Document doc = res.asDocument();
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<User>(0);
        } else {
            try {
                ensureRootNodeNameIs("users", doc);
                NodeList list = doc.getDocumentElement().getElementsByTagName(
                        "user");
                int size = list.getLength();
                List<User> users = new ArrayList<User>(size);
                for (int i = 0; i < size; i++) {
                    users.add(new User(res, (Element) list.item(i), twitter));
                }
                return users;
            } catch (TwitterException te) {
                if (isRootNodeNilClasses(doc)) {
                    return new ArrayList<User>(0);
                } else {
                    throw te;
                }
            }
        }
    }

    /**
     * @return created_at or null if the user is protected
     * @since Twitter4J 1.1.0
     */
    public Date getStatusCreatedAt() {
        return statusCreatedAt;
    }

    /**
     *
     * @return status id or -1 if the user is protected
     */
    public long getStatusId() {
        return statusId;
    }

    /**
     *
     * @return status text or null if the user is protected
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     *
     * @return source or null if the user is protected
     * @since 1.1.4
     */
    public String getStatusSource() {
        return statusSource;
    }

    /**
     *
     * @return truncated or false if the user is protected
     * @since 1.1.4
     */
    public boolean isStatusTruncated() {
        return statusTruncated;
    }

    /**
     *
     * @return in_reply_to_status_id or -1 if the user is protected
     * @since 1.1.4
     */
    public long getStatusInReplyToStatusId() {
        return statusInReplyToStatusId;
    }

    /**
     *
     * @return in_reply_to_user_id or -1 if the user is protected
     * @since 1.1.4
     */
    public int getStatusInReplyToUserId() {
        return statusInReplyToUserId;
    }

    /**
     *
     * @return favorited or false if the user is protected
     * @since 1.1.4
     */
    public boolean isStatusFavorited() {
        return statusFavorited;
    }

    /**
     *
     * @return in_reply_to_screen_name or null if the user is protected
     * @since 1.1.4
     */

    public String getStatusInReplyToScreenName() {
        return -1 != statusInReplyToUserId ? statusInReplyToScreenName : null;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof User && ((User) obj).id == this.id;
    }

    @Override
    public String toString() {
        return "User{" +
                "twitter=" + twitter +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", url='" + url + '\'' +
                ", isProtected=" + isProtected +
                ", followersCount=" + followersCount +
                ", statusCreatedAt=" + statusCreatedAt +
                ", statusId=" + statusId +
                ", statusText='" + statusText + '\'' +
                ", statusSource='" + statusSource + '\'' +
                ", statusTruncated=" + statusTruncated +
                ", statusInReplyToStatusId=" + statusInReplyToStatusId +
                ", statusInReplyToUserId=" + statusInReplyToUserId +
                ", statusFavorited=" + statusFavorited +
                ", statusInReplyToScreenName='" + statusInReplyToScreenName + '\'' +
                '}';
    }
}
