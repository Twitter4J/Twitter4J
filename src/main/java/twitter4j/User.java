package twitter4j;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A data class representing Twitter User
 */
public class User extends TwitterResponse implements java.io.Serializable {
    static final String[] POSSIBLE_ROOT_NAMES = new String[]{"user","sender","recipient"};
    private static final long serialVersionUID = 9043136917164367181L;

    /*package*/User(Element elem, Twitter twitter) throws TwitterException{
        super(elem, twitter);
        ensureRootNodeNameIs(POSSIBLE_ROOT_NAMES);
    }

    /**
     * Returns the id of the user
     *
     * @return the id of the user
     */
    public int getId() {
        return getChildInt("id");
    }

    /**
     * Returns the name of the user
     *
     * @return the name of the user
     */
    public String getName() {
        return getChildText("name");
    }

    /**
     * Returns the screen name of the user
     *
     * @return the screen name of the user
     */
    public String getScreenName() {
        return getChildText("screen_name");
    }

    /**
     * Returns the location of the user
     *
     * @return the location of the user
     */
    public String getLocation() {
        return getChildText("location");
    }

    /**
     * Returns the description of the user
     *
     * @return the description of the user
     */
    public String getDescription() {
        return getChildText("description");
    }

    /**
     * Returns the profile image url of the user
     *
     * @return the profile image url of the user
     */
    public URL getProfileImageURL() {
        try {
            return new URL(getChildText("profile_image_url"));
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
            return new URL(getChildText("url"));
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
        return getChildBoolean("protected");
    }


    /**
     * Returns the number of followers
     *
     * @since twitter4j 1.0.4
     * @return the number of followers
     */
    public int getFollowersCount() {
        return getChildInt("followers_count");
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
      <id>3516311</id>
      <name>&#12383;&#12384;&#12375;</name>
      <screen_name>_tad_</screen_name>
      <location>&#12388;&#12367;&#12400;&#24066;</location>
      <description>&#12360;&#12379;{hacker,Rubyist,&#27598;&#26085;&#12487;&#12470;&#12452;&#12531;&#32771;&#23519;&#20013;,&#12403;&#12424;&#12426;&#12377;&#12392;,&#32207;&#35676;&#12414;&#12395;&#12354;,&#12521;&#12531;&#12490;&#12540;,&#12407;&#12425;&#12368;&#12414;&#12385;&#12377;&#12392;} + &#12426;&#12354;&#12427;&#12395;&#12502;&#12524;&#12540;&#12461;&#22730;&#12428;&#12390;&#12427;&#20154;</description>
      <profile_image_url>http://assets1.twitter.com/system/user/profile_image/3516311/normal/NEC_0045.jpg?1182343831</profile_image_url>
      <url>http://www.coins.tsukuba.ac.jp/~i021179/blog/</url>
      <protected>false</protected>
      <followsers_count>274</followers_count>
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
