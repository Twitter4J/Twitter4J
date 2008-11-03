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
    private int followersCount;
    private int favouritesCount;
    private int statusesCount;
    private Date createdAt;
    private String text;
    private long statusId;
    private static final long serialVersionUID = -1186848883283901709L;

    public UserWithStatus(Element elem, Twitter twitter) throws TwitterException {
        super(elem, twitter);
        profileBackgroundColor = getChildText("profile_background_color", elem);
        profileTextColor = getChildText("profile_text_color", elem);
        profileLinkColor = getChildText("profile_link_color", elem);
        profileSidebarFillColor = getChildText("profile_sidebar_fill_color", elem);
        profileSidebarBorderColor = getChildText("profile_sidebar_border_color", elem);
        friendsCount = getChildInt("friends_count", elem);
        followersCount = getChildInt("followers_count", elem);
        favouritesCount = getChildInt("favourites_count", elem);
        statusesCount = getChildInt("statuses_count", elem);
        createdAt = getChildDate("created_at", elem);
        text = getChildText("text", elem);
        statusId = Long.valueOf(((Element) elem.getElementsByTagName("status").item(0)).getElementsByTagName("id").item(0).getTextContent());
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

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    /**
     * @return created_at
     * @since twitter4j 1.1.0
     */
    public Date getStatusCreatedAt() {
        return createdAt;
    }

    public long getStatusId() {
        return statusId;
    }

    public String getStatusText() {
        return text;
    }

    /*<?xml version="1.0" encoding="UTF-8"?>
    <user>
      <id>4933401</id>
      <name>Yusuke Yamamoto</name>
      <screen_name>yusukey</screen_name>
      <location>Tokyo</location>
      <description>log4twitter!  http://yusuke.homeip.net/log4twitter/ja/index.html</description>
      <profile_image_url>http://assets3.twitter.com/system/user/profile_image/4933401/normal/1023824_2048059614.jpg?1176769649</profile_image_url>
      <url>http://yusuke.homeip.net/diary/</url>
      <protected>false</protected>
      <profile_background_color>9ae4e8</profile_background_color>
      <profile_text_color>000000</profile_text_color>
      <profile_link_color>0000ff</profile_link_color>
      <profile_sidebar_fill_color>e0ff92</profile_sidebar_fill_color>
      <profile_sidebar_border_color>87bc44</profile_sidebar_border_color>
      <friends_count>12</friends_count>
      <followers_count>12</followers_count>
      <favourites_count>3</favourites_count>
      <statuses_count>186</statuses_count>
      <status>
        <created_at>Wed May 30 13:06:11 +0000 2007</created_at>
        <id>83978352</id>
        <text>&#12358;&#12435;&#12358;&#12435;&#12290;Preview &#24555;&#36969;&#12377;&#12366;&#65281; &#38598;&#20013;&#12375;&#12390;&#35501;&#12416;&#12392;&#12365;&#12399; PDFView! http://pdfview.sourceforge.net/</text>
      </status>
    </user>
*/
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
                ", followersCount=" + followersCount +
                ", favouritesCount=" + favouritesCount +
                ", statusesCount=" + statusesCount +
                ", createdAt=" + createdAt +
                ", text='" + text + '\'' +
                ", statusId=" + statusId +
                '}';
    }
}
