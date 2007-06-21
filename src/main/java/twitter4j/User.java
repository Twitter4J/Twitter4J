package twitter4j;

import org.w3c.dom.Element;
import java.net.URL;
import java.net.MalformedURLException;
import org.w3c.dom.Document;
import java.util.List;
import java.util.ArrayList;
import org.w3c.dom.NodeList;

/**
 * A data class representing Twitter User
 */
public class User extends TwitterResponse implements java.io.Serializable {
    /*package*/User(Element elem, Twitter twitter) throws TwitterException{
        super(elem, twitter);
        ensureRootNodeNameIs("user");
    }

    public int getId() {
        return getChildInt("id");
    }

    public String getName() {
        return getChildText("name");
    }

    public String getScreenName() {
        return getChildText("screen_name");
    }

    public String getLocation() {
        return getChildText("location");
    }

    public String getDescription() {
        return getChildText("description");
    }

    public URL getProfileImageURL() {
        try {
            return new URL(getChildText("profile_image_url"));
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public URL getURL() {
        try {
            return new URL(getChildText("url"));
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public boolean isProtected() {
        return "true".equalsIgnoreCase(getChildText("protected"));
    }


    public DirectMessage sendDirectMessage(String text) throws TwitterException {
        return twitter.sendDirectMessage(this.getName(), text);
    }

    public static List<User> constructUsers(Document doc, Twitter twitter)throws TwitterException {
        if (null == doc) {
            return new ArrayList<User> (0);
        } else {
            try {
                ensureRootNodeNameIs("users", doc.getDocumentElement());
                NodeList list = doc.getDocumentElement().getElementsByTagName(
                        "user");
                int size = list.getLength();
                List<User> users = new ArrayList<User>(size);
                for (int i = 0; i < size; i++) {
                    users.add(new User((Element) list.item(i), twitter));
                }
                return users;
            } catch (TwitterException te) {
                ensureRootNodeNameIs("nil-classes", doc.getDocumentElement());
                return new ArrayList<User>(0);
            }
        }
    }

    /*<?xml version="1.0" encoding="UTF-8"?>
     <user>
       <id>6377362</id>
       <name>twit4j2</name>
       <screen_name>twit4j2</screen_name>
       <location></location>
       <description></description>
       <profile_image_url>http://assets1.twitter.com/images/default_image.gif?1180256973</profile_image_url>
       <url></url>
       <protected>false</protected>
       <profile_background_color>9ae4e8</profile_background_color>
       <profile_text_color>000000</profile_text_color>
       <profile_link_color>0000ff</profile_link_color>
       <profile_sidebar_fill_color>e0ff92</profile_sidebar_fill_color>
       <profile_sidebar_border_color>87bc44</profile_sidebar_border_color>
       <friends_count>1</friends_count>
       <followers_count>1</followers_count>
       <favourites_count>0</favourites_count>
       <statuses_count>0</statuses_count>
     </user>
     */
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
        if (obj instanceof User) {
            ( (User) obj).elem.equals(this.elem);
        }
        return false;
    }
}
