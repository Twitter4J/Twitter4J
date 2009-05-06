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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A data class representing sent/received direct message.
 * @author Yusuke Yamamoto - yusuke at mac.com
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

    /*package*/DirectMessage(Response res, Twitter twitter) throws TwitterException {
        super(res);
        init(res, res.asDocument().getDocumentElement(), twitter);
    }
    /*package*/DirectMessage(Response res, Element elem, Twitter twitter) throws TwitterException {
        super(res);
        init(res, elem, twitter);
    }
    private void init(Response res, Element elem, Twitter twitter) throws TwitterException{
        ensureRootNodeNameIs("direct_message", elem);
        sender = new User(res, (Element) elem.getElementsByTagName("sender").item(0),
                twitter);
        recipient = new User(res, (Element) elem.getElementsByTagName("recipient").item(0),
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
     * @return created_at
     * @since Twitter4J 1.1.0
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
    static List<DirectMessage> constructDirectMessages(Response res,
                                                       Twitter twitter) throws TwitterException {
        Document doc = res.asDocument();
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
                    messages.add(new DirectMessage(res, status, twitter));
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
        return obj instanceof DirectMessage && ((DirectMessage) obj).id == this.id;
    }

    @Override
    public String toString() {
        return "DirectMessage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", sender_id=" + sender_id +
                ", recipient_id=" + recipient_id +
                ", created_at=" + created_at +
                ", sender_screen_name='" + sender_screen_name + '\'' +
                ", recipient_screen_name='" + recipient_screen_name + '\'' +
                ", sender=" + sender +
                ", recipient=" + recipient +
                '}';
    }
}
