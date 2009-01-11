package twitter4j;

import org.w3c.dom.Element;

import java.util.Date;

/**
 * A data class representing Twitter User with status
 */
public class UserWithStatus extends User {
    private String profileBackgroundColor;
    private String profileTextColor;
    private String profileLinkColor;
    private String profileSidebarFillColor;
    private String profileSidebarBorderColor;
    private int friendsCount;
    private int favouritesCount;
    private int statusesCount;
    private Date statusCreatedAt;
    private long statusId = -1;
    private String statusText = null;
    private String statusSource = null;
    private boolean statusTruncated = false;
    private long statusInReplyToStatusId = -1;
    private int statusInReplyToUserId = -1;
    private boolean statusFavorited = false;
    private String statusInReplyToScreenName = null;
    private static final long serialVersionUID = -3338496376247577523L;

    public UserWithStatus(Element elem, Twitter twitter) throws TwitterException {
        super(elem, twitter);
        profileBackgroundColor = getChildText("profile_background_color", elem);
        profileTextColor = getChildText("profile_text_color", elem);
        profileLinkColor = getChildText("profile_link_color", elem);
        profileSidebarFillColor = getChildText("profile_sidebar_fill_color", elem);
        profileSidebarBorderColor = getChildText("profile_sidebar_border_color", elem);
        friendsCount = getChildInt("friends_count", elem);
        favouritesCount = getChildInt("favourites_count", elem);
        statusesCount = getChildInt("statuses_count", elem);
        if (!isProtected()) {
            Element status = (Element) elem.getElementsByTagName("status").item(0);
            statusCreatedAt = getChildDate("created_at", status);
            statusId = Long.valueOf(status.getElementsByTagName("id").item(0).getTextContent());
            statusText = getChildText("text", status);
            statusSource = getChildText("source", status);
            statusTruncated = getChildBoolean("truncated", status);
            statusInReplyToStatusId = getChildLong("in_reply_to_status_id", status);
            statusInReplyToUserId = getChildInt("in_reply_to_user_id", status);
            statusFavorited = getChildBoolean("favorited", status);
            statusInReplyToScreenName = getChildText("in_reply_to_screen_name", status);
        }
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public String getProfileTextColor() {
        return profileTextColor;
    }

    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    /**
     * @return created_at or null if the user is protected
     * @since twitter4j 1.1.0
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
        return getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof User && ((User) obj).getId() == this.getId();
    }

    @Override
     public String toString() {
        return "UserWithStatus{" +
                "profileBackgroundColor='" + profileBackgroundColor + '\'' +
                ", profileTextColor='" + profileTextColor + '\'' +
                ", profileLinkColor='" + profileLinkColor + '\'' +
                ", profileSidebarFillColor='" + profileSidebarFillColor + '\'' +
                ", profileSidebarBorderColor='" + profileSidebarBorderColor + '\'' +
                ", friendsCount=" + friendsCount +
                ", favouritesCount=" + favouritesCount +
                ", statusesCount=" + statusesCount +
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
