package twitter4j;

import org.w3c.dom.Element;

/**
 * A data class representing Twitter User with status
 */
public class UserWithStatus extends User {
    public UserWithStatus(Element elem,Twitter twitter) throws TwitterException{
        super(elem,twitter);
    }

    public String getStatusCreatedAt() {
        return getChildText("created_at");
    }

    public int getStatusId() {
        return Integer.valueOf(((Element)elem.getElementsByTagName("status").item(0)).getElementsByTagName("id").item(0).getTextContent());
    }

    public String getStatusText() {
        return getChildText("text");
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
}
