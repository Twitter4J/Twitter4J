package twitter4j;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A data class representing sent/received direct message.
 */
public class DirectMessage extends TwitterResponse implements java.io.Serializable {
    private int id;
    private String text;
    private int sender_id;
    private int recipient_id;
    private Date created_at;
    private String sender_screen_name;
    private String recipient_screen_name;
    private static final long serialVersionUID = -3253021825891789737L;

    /*package*/DirectMessage(Element elem, Twitter twitter) throws TwitterException {
        super();
        ensureRootNodeNameIs("direct_message", elem);
        sender = new User((Element) elem.getElementsByTagName("sender").item(0),
                twitter);
        recipient = new User((Element) elem.getElementsByTagName("recipient").item(0),
                twitter);
        id = getChildInt("id", elem);
        text = getChildText("text", elem);
        sender_id = getChildInt("sender_id", elem);
        recipient_id = getChildInt("recipient_id", elem);
        created_at = getChildDate("created_at", elem);
        sender_screen_name = getChildText("sender_screen_name", elem);
        recipient_screen_name = getChildText("recipient_screen_name", elem);
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getSenderId() {
        return sender_id;
    }

    public int getRecipientId() {
        return recipient_id;
    }

    /**
     * @since twitter4j 1.1.0
     */
    public Date getCreatedAt() {
        return created_at;
    }

    public String getSenderScreenName() {
        return sender_screen_name;
    }

    public String getRecipientScreenName() {
        return recipient_screen_name;
    }

    private User sender;

    public User getSender() {
        return sender;
    }

    private User recipient;

    public User getRecipient() {
        return recipient;
    }

    /*package*/
    static List<DirectMessage> constructDirectMessages(Document doc,
                                                       Twitter twitter) throws TwitterException {
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<DirectMessage>(0);
        } else {
            try {
                ensureRootNodeNameIs("direct-messages", doc);
                NodeList list = doc.getDocumentElement().getElementsByTagName(
                        "direct_message");
                int size = list.getLength();
                List<DirectMessage> messages = new ArrayList<DirectMessage>(size);
                for (int i = 0; i < size; i++) {
                    Element status = (Element) list.item(i);
                    messages.add(new DirectMessage(status, twitter));
                }
                return messages;
            } catch (TwitterException te) {
                if (isRootNodeNilClasses(doc)) {
                    return new ArrayList<DirectMessage>(0);
                } else {
                    throw te;
                }
            }
        }
    }

    /*
     <?xml version="1.0" encoding="UTF-8"?>
  <direct_message>
    <id>3611242</id>
    <text>test</text>
    <sender_id>4933401</sender_id>
    <recipient_id>6459452</recipient_id>
    <created_at>Thu Jun 07 06:36:21 +0000 2007</created_at>
    <sender_screen_name>yusukey</sender_screen_name>
    <recipient_screen_name>fast_ts</recipient_screen_name>
    <sender>
      <id>4933401</id>
      <name>Yusuke Yamamoto</name>
      <screen_name>yusukey</screen_name>
      <location>Tokyo</location>
      <description>http://yusuke.homeip.net/diary/</description>
      <profile_image_url>http://assets3.twitter.com/system/user/profile_image/4933401/normal/1023824_2048059614.jpg?1176769649</profile_image_url>
      <url>http://yusuke.homeip.net/diary/</url>
      <protected>false</protected>
    </sender>
    <recipient>
      <id>6459452</id>
      <name>fast_ts</name>
      <screen_name>fast_ts</screen_name>
      <location></location>
      <description></description>
      <profile_image_url>http://assets1.twitter.com/system/user/profile_image/6459452/normal/_____-1.gif?1180974738</profile_image_url>
      <url></url>
      <protected>true</protected>
    </recipient>
  </direct_message>
     */
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
        if (obj instanceof DirectMessage) {
            return ((DirectMessage) obj).id == this.id;
        }
        return false;
    }
}
