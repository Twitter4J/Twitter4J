package twitter4j;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A data class representing one single status of a user.
 */
public class Status extends TwitterResponse implements java.io.Serializable {
    private static final long serialVersionUID = 4261125762955745621L;
    private Date createdAt;

    /*package*/Status(Element elem, Twitter twitter) throws TwitterException{
        super(elem, twitter);
        ensureRootNodeNameIs("status");
        SimpleDateFormat format = new SimpleDateFormat(
                "EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            createdAt = format.parse(getChildText("created_at"));
        } catch (ParseException pe) {
            throw new TwitterException("Unexpected format(created_at) returned from twitter.com:" + getChildText("created_at"));
        }
        user = new User( (Element) elem.getElementsByTagName("user").item(0),
                        twitter);
    }

    /**
     * Return the created_at
     *
     * @return created_at
     */

    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the id of the status
     *
     * @return the id
     */
    public long getId() {
        return getChildInt("id");
    }

    /**
     * Returns the text of the status
     *
     * @return the text
     */
    public String getText() {
        return getChildText("text");
    }

    /**
     * Returns the source
     *
     * @since twitter4j 1.0.4
     * @return the source
     */
    public String getSource() {
        return getChildText("source");
    }


    /**
     * Test if the status is truncated
     *
     * @since twitter4j 1.0.4
     * @return true if truncated
     */
    public boolean isTruncated() {
        return getChildBoolean("truncated");
    }

    /**
     * Returns the in_reply_tostatus_id
     *
     * @since twitter4j 1.0.4
     * @return the in_reply_tostatus_id
     */
    public int getInReplyToStatusId() {
        return getChildInt("in_reply_to_status_id");
    }

    /**
     * Returns the in_reply_user_id
     *
     * @since twitter4j 1.0.4
     * @return the in_reply_tostatus_id
     */
    public int getInReplyToUserId() {
        return getChildInt("in_reply_to_user_id");
    }

    /**
     * Test if the status is favorited
     *
     * @since twitter4j 1.0.4
     * @return true if favorited
     */
    public boolean isFavorited() {
        return getChildBoolean("favorited");
    }



    private User user = null;

    /**
     * Return the user
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /*package*/ static List<Status> constructStatuses(Document doc,
        Twitter twitter) throws TwitterException{
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<Status> (0);
        } else {
            try {
                ensureRootNodeNameIs("statuses", doc);
                NodeList list = doc.getDocumentElement().getElementsByTagName(
                        "status");
                int size = list.getLength();
                List<Status> statuses = new ArrayList<Status>(size);
                for (int i = 0; i < size; i++) {
                    Element status = (Element) list.item(i);
                    statuses.add(new Status(status, twitter));
                }
                return statuses;
            } catch (TwitterException te) {
                ensureRootNodeNameIs("nil-classes", doc);
                return new ArrayList<Status>(0);
            }
        }
    }

    /*
  <status>
    <created_at>Fri May 30 17:04:22 +0000 2008</created_at>
    <id>823477057</id>
    <text>double double at in n out on Magnolia</text>
    <source>web</source>
    <truncated>false</truncated>
    <in_reply_to_status_id></in_reply_to_status_id>
    <in_reply_to_user_id></in_reply_to_user_id>
    <favorited>false</favorited>
    <user>
      <id>14500444</id>
      <name>arenson</name>
      <screen_name>arenson</screen_name>
      <location>Texas</location>
      <description>I like girls, Mexican Food, and laughter. </description>
      <profile_image_url>http://s3.amazonaws.com/twitter_production/profile_images/54044033/s7958437_39956964_9393_normal.jpg</profile_image_url>
      <url></url>
      <protected>false</protected>
      <followers_count>12</followers_count>
    </user>
  </status>*/
    @Override public int hashCode() {
        return elem.hashCode();
    }

    @Override public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Status) {
            ( (Status) obj).elem.equals(this.elem);
        }
        return false;
    }
}
