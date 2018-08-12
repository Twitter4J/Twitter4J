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

package twitter4j;

import twitter4j.conf.Configuration;

import java.util.Arrays;
import java.util.Date;

/**
 * A data class representing sent/received direct message.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 */
/*package*/ final class DirectMessageJSONImpl extends TwitterResponseImpl implements DirectMessage, java.io.Serializable {
    private static final long serialVersionUID = 7092906238192790921L;
    private long id;
    private String text;
    private long senderId;
    private long recipientId;
    private Date createdAt;
    private UserMentionEntity[] userMentionEntities;
    private URLEntity[] urlEntities;
    private HashtagEntity[] hashtagEntities;
    private MediaEntity[] mediaEntities;
    private SymbolEntity[] symbolEntities;

    /*package*/DirectMessageJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        try {
            JSONObject event = json.getJSONObject("event");
            init(event);
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
                TwitterObjectFactory.registerJSONObject(this, event);
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /*package*/DirectMessageJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            id = ParseUtil.getLong("id", json);
            JSONObject messageCreate;
            final JSONObject messageData;
            if (!json.isNull("created_timestamp")) {
                createdAt = new Date(json.getLong("created_timestamp"));
                messageCreate = json.getJSONObject("message_create");
                recipientId = ParseUtil.getLong("recipient_id", messageCreate.getJSONObject("target"));
                senderId = ParseUtil.getLong("sender_id", messageCreate);
                messageData = messageCreate.getJSONObject("message_data");

            }else{
                // raw JSON data from Twitter4J 4.0.6 or before
                createdAt = ParseUtil.getDate("created_at", json);;
                senderId = ParseUtil.getLong("sender_id", json);
                recipientId = ParseUtil.getLong("recipient_id", json);
                messageData = json;
            }
            if (!messageData.isNull("entities")) {
                JSONObject entities = messageData.getJSONObject("entities");
                userMentionEntities = EntitiesParseUtil.getUserMentions(entities);
                urlEntities = EntitiesParseUtil.getUrls(entities);
                hashtagEntities = EntitiesParseUtil.getHashtags(entities);
                symbolEntities = EntitiesParseUtil.getSymbols(entities);
            }
            userMentionEntities = userMentionEntities == null ? new UserMentionEntity[0] : userMentionEntities;
            urlEntities = urlEntities == null ? new URLEntity[0] : urlEntities;
            hashtagEntities = hashtagEntities == null ? new HashtagEntity[0] : hashtagEntities;
            symbolEntities = symbolEntities == null ? new SymbolEntity[0] : symbolEntities;

            if (!messageData.isNull("attachment")) {
                JSONObject attachment = messageData.getJSONObject("attachment");
                // force parsing "type" as "media"
                if (!attachment.isNull("media")) {
                    mediaEntities = new MediaEntity[1];
                    mediaEntities[0] = new MediaEntityJSONImpl(attachment.getJSONObject("media"));
                }
            }
            mediaEntities = mediaEntities == null ? new MediaEntity[0] : mediaEntities;
            text = HTMLEntity.unescapeAndSlideEntityIncdices(messageData.getString("text"), userMentionEntities,
                    urlEntities, hashtagEntities, mediaEntities);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    static DirectMessageList createDirectMessageList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
            }
            JSONArray list;
            DirectMessageList directMessages;
            try {
                JSONObject jsonObject = res.asJSONObject();
                list = jsonObject.getJSONArray("events");
                directMessages = new DirectMessageListImpl(list.length(), jsonObject, res);
            }catch(TwitterException te){
                if (te.getCause() != null && te.getCause() instanceof JSONException) {
                    // serialized form from Twitter4J 4.0.6 or before
                    list = res.asJSONArray();
                    int size = list.length();
                    directMessages = new DirectMessageListImpl(size, res);

                }else{
                  throw  te;
                }
            }
            for (int i = 0; i < list.length(); i++) {
                    JSONObject json = list.getJSONObject(i);
                    DirectMessage directMessage = new DirectMessageJSONImpl(json);
                    directMessages.add(directMessage);
                    if (conf.isJSONStoreEnabled()) {
                        TwitterObjectFactory.registerJSONObject(directMessage, json);
                    }
                }
                if (conf.isJSONStoreEnabled()) {
                    TwitterObjectFactory.registerJSONObject(directMessages, list);
                }
                return directMessages;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public long getRecipientId() {
        return recipientId;
    }

    @Override
    public long getSenderId() {
        return senderId;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public UserMentionEntity[] getUserMentionEntities() {
        return userMentionEntities;
    }

    @Override
    public URLEntity[] getURLEntities() {
        return urlEntities;
    }

    @Override
    public HashtagEntity[] getHashtagEntities() {
        return hashtagEntities;
    }

    @Override
    public MediaEntity[] getMediaEntities() {
        return mediaEntities;
    }

    @Override
    public SymbolEntity[] getSymbolEntities() {
        return symbolEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectMessageJSONImpl that = (DirectMessageJSONImpl) o;

        if (id != that.id) return false;
        if (senderId != that.senderId) return false;
        if (recipientId != that.recipientId) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(userMentionEntities, that.userMentionEntities)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(urlEntities, that.urlEntities)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(hashtagEntities, that.hashtagEntities)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(mediaEntities, that.mediaEntities)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(symbolEntities, that.symbolEntities);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (int) (senderId ^ (senderId >>> 32));
        result = 31 * result + (int) (recipientId ^ (recipientId >>> 32));
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(userMentionEntities);
        result = 31 * result + Arrays.hashCode(urlEntities);
        result = 31 * result + Arrays.hashCode(hashtagEntities);
        result = 31 * result + Arrays.hashCode(mediaEntities);
        result = 31 * result + Arrays.hashCode(symbolEntities);
        return result;
    }

    @Override
    public String toString() {
        return "DirectMessageJSONImpl{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", createdAt=" + createdAt +
                ", userMentionEntities=" + Arrays.toString(userMentionEntities) +
                ", urlEntities=" + Arrays.toString(urlEntities) +
                ", hashtagEntities=" + Arrays.toString(hashtagEntities) +
                ", mediaEntities=" + Arrays.toString(mediaEntities) +
                ", symbolEntities=" + Arrays.toString(symbolEntities) +
                '}';
    }

    @Override
    public String getSenderScreenName() {
        throw new UnsupportedOperationException("Since Twitter4J 4.0.7, you are no longer able to use this method due to the API changes.");
    }

    @Override
    public String getRecipientScreenName() {
        throw new UnsupportedOperationException("Since Twitter4J 4.0.7, you are no longer able to use this method due to the API changes.");
    }

    @Override
    public User getSender() {
        throw new UnsupportedOperationException("Since Twitter4J 4.0.7, you are no longer able to use this method due to the API changes.");
    }

    @Override
    public User getRecipient() {
        throw new UnsupportedOperationException("Since Twitter4J 4.0.7, you are no longer able to use this method due to the API changes.");
    }

}
