/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j.internal.json;

import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.Date;

import static twitter4j.internal.util.z_T4JInternalParseUtil.getDate;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getLong;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getUnescapedString;

/**
 * A data class representing sent/received direct message.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class DirectMessageJSONImpl extends TwitterResponseImpl implements DirectMessage, java.io.Serializable {
    private static final long serialVersionUID = -7104233663827757577L;
    private long id;
    private String text;
    private long senderId;
    private long recipientId;
    private Date createdAt;
    private String senderScreenName;
    private String recipientScreenName;


    /*package*/DirectMessageJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    /*package*/DirectMessageJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        id = getLong("id", json);
        text = getUnescapedString("text", json);
        senderId = getLong("sender_id", json);
        recipientId = getLong("recipient_id", json);
        createdAt = getDate("created_at", json);
        senderScreenName = getUnescapedString("sender_screen_name", json);
        recipientScreenName = getUnescapedString("recipient_screen_name", json);
        try {
            sender = new UserJSONImpl(json.getJSONObject("sender"));
            recipient = new UserJSONImpl(json.getJSONObject("recipient"));
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return text;
    }

    /**
     * {@inheritDoc}
     */
    public long getSenderId() {
        return senderId;
    }

    /**
     * {@inheritDoc}
     */
    public long getRecipientId() {
        return recipientId;
    }

    /**
     * {@inheritDoc}
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    public String getSenderScreenName() {
        return senderScreenName;
    }

    /**
     * {@inheritDoc}
     */
    public String getRecipientScreenName() {
        return recipientScreenName;
    }

    private User sender;

    /**
     * {@inheritDoc}
     */
    public User getSender() {
        return sender;
    }

    private User recipient;

    /**
     * {@inheritDoc}
     */
    public User getRecipient() {
        return recipient;
    }

    /*package*/
    static ResponseList<DirectMessage> createDirectMessageList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<DirectMessage> directMessages = new ResponseListImpl<DirectMessage>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                DirectMessage directMessage = new DirectMessageJSONImpl(json);
                directMessages.add(directMessage);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(directMessage, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(directMessages, list);
            }
            return directMessages;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof DirectMessage && ((DirectMessage) obj).getId() == this.id;
    }

    @Override
    public String toString() {
        return "DirectMessageJSONImpl{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", sender_id=" + senderId +
                ", recipient_id=" + recipientId +
                ", created_at=" + createdAt +
                ", sender_screen_name='" + senderScreenName + '\'' +
                ", recipient_screen_name='" + recipientScreenName + '\'' +
                ", sender=" + sender +
                ", recipient=" + recipient +
                '}';
    }
}
