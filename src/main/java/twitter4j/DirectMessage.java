package twitter4j;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import java.util.List;
import java.util.ArrayList;
import org.w3c.dom.NodeList;

/**
 * A data class representing sent/received direct message.
 */
public class DirectMessage extends TwitterResponse implements java.io.
    Serializable {
    /*package*/DirectMessage(Element elem, Twitter twitter)throws TwitterException {
        super(elem, twitter);
        ensureRootNodeNameIs("direct_message");
    }

    public int getId() {
        return getChildInt("id");
    }

    public String getText() {
        return getChildText("text");
    }

    public int getSenderId() {
        return getChildInt("sender_id");
    }

    public int getRecipientId() {
        return getChildInt("recipient_id");
    }

    public String getCreatedAt() {
        return getChildText("created_at");
    }

    public String getSenderScreenName() {
        return getChildText("sender_screen_name");
    }

    public String getRecipientScreenName() {
        return getChildText("recipient_screen_name");
    }

    /*package*/ static List<DirectMessage> constructDirectMessages(Document doc,
        Twitter twitter) throws TwitterException{
        if (null == doc) {
            return new ArrayList<DirectMessage> (0);
        } else {
            try {
                ensureRootNodeNameIs("direct-messages", doc.getDocumentElement());
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
                ensureRootNodeNameIs("nil-classes", doc.getDocumentElement());
                return new ArrayList<DirectMessage>(0);
            }
        }
    }

    /*
     <?xml version="1.0" encoding="UTF-8"?>
        <direct-messages>
          <direct_message>
            <id>3255402</id>
            <text>Wed May 30 21:55:58 JST 2007:directmessage test</text>
            <sender_id>6377362</sender_id>
            <recipient_id>4933401</recipient_id>
            <created_at>Wed May 30 12:55:59 +0000 2007</created_at>
            <sender_screen_name>twit4j2</sender_screen_name>
            <recipient_screen_name>yusukey</recipient_screen_name>
          </direct_message>
          <direct_message>
            <id>3255322</id>
            <text>Wed May 30 21:54:43 JST 2007:directmessage test</text>
            <sender_id>6377362</sender_id>
            <recipient_id>4933401</recipient_id>
            <created_at>Wed May 30 12:54:44 +0000 2007</created_at>
            <sender_screen_name>twit4j2</sender_screen_name>
            <recipient_screen_name>yusukey</recipient_screen_name>
          </direct_message>
        </direct-messages>
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
        if (obj instanceof DirectMessage) {
            ( (DirectMessage) obj).elem.equals(this.elem);
        }
        return false;
    }
}
