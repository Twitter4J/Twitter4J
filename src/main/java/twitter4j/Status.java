package twitter4j;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A data class representing one single status of a user.
 */
public class Status extends TwitterResponse implements java.io.Serializable {

    /*package*/Status(Element elem, Twitter twitter) throws TwitterException{
        super(elem, twitter);
        ensureRootNodeNameIs("status");
        user = new User( (Element) elem.getElementsByTagName("user").item(0),
                        twitter);
}

    public String getCreatedAt() {
        return getChildText("created_at");
    }

    public int getId() {
        return getChildInt("id");
    }

    public String getText() {
        return getChildText("text");
    }

    private User user = null;
    public User getUser() {
        return user;
    }

    /*package*/ static List<Status> constructStatuses(Document doc,
        Twitter twitter) throws TwitterException{
        if (null == doc) {
            return new ArrayList<Status> (0);
        } else {
            ensureRootNodeNameIs("statuses",doc.getDocumentElement());
            NodeList list = doc.getDocumentElement().getElementsByTagName(
                "status");
            int size = list.getLength();
            List<Status> statuses = new ArrayList<Status> (size);
            for (int i = 0; i < size; i++) {
                Element status = (Element) list.item(i);
                statuses.add(new Status(status, twitter));
            }
            return statuses;
        }
    }

    /*        <status>
                    <created_at>Sun May 27 13:58:44 +0000 2007</created_at>
                    <id>80430442</id>
                    <text>&#12469;&#12508;&#12486;&#12531;&#12289;&#36890;&#36009;&#12391;&#36023;&#12387;&#12383;&#12290;&#21021;&#12469;&#12508;&#12486;&#12531;&#12290;</text>
                    <user>
                      <id>6106852</id>
                      <name>iseki</name>
                      <screen_name>iseki</screen_name>
                      <location></location>
                      <description></description>
                      <profile_image_url>http://assets1.twitter.com/system/user/profile_image/6106852/normal/1.jpg?1179407975</profile_image_url>
                      <url></url>
                      <protected>false</protected>
                    </user>
          </status>    */
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
