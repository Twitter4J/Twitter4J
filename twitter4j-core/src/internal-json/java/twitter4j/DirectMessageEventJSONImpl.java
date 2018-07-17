package twitter4j;

import twitter4j.conf.Configuration;

import java.util.Date;

final class DirectMessageEventJSONImpl extends TwitterResponseImpl implements DirectMessageEvent, java.io.Serializable {

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

    /*package*/DirectMessageEventJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
            TwitterObjectFactory.registerJSONObject(this, json);
        }
    }

    /*package*/ DirectMessageEventJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {

            if (json.has("event")) {
                init(json.getJSONObject("event"));
                return;
            }

            id = ParseUtil.getLong("id", json);
            createdAt = ParseUtil.getDate("created_timestamp", json);

            // message create field is used for the message information
            // @TDODO: check for different fields (update, edit or something?)
            if (!json.has("message_create")) {
                return;
            }

            JSONObject innerMessage = json.getJSONObject("message_create");
            senderId = ParseUtil.getLong("sender_id", innerMessage);

            if (innerMessage.has("target") && innerMessage.getJSONObject("target").has("recipient_id")) {
                recipientId = ParseUtil.getLong("recipient_id", innerMessage.getJSONObject("target"));
            }

            // the actual message data can be found in the message_data field
            JSONObject messageData = innerMessage.getJSONObject("message_data");

            if (!messageData.isNull("entities")) {
                JSONObject entities = messageData.getJSONObject("entities");
                int len;
                if (!entities.isNull("user_mentions")) {
                    JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
                    len = userMentionsArray.length();
                    userMentionEntities = new UserMentionEntity[len];
                    for (int i = 0; i < len; i++) {
                        userMentionEntities[i] = new UserMentionEntityJSONImpl(userMentionsArray.getJSONObject(i));
                    }

                }
                if (!entities.isNull("urls")) {
                    JSONArray urlsArray = entities.getJSONArray("urls");
                    len = urlsArray.length();
                    urlEntities = new URLEntity[len];
                    for (int i = 0; i < len; i++) {
                        urlEntities[i] = new URLEntityJSONImpl(urlsArray.getJSONObject(i));
                    }
                }

                if (!entities.isNull("hashtags")) {
                    JSONArray hashtagsArray = entities.getJSONArray("hashtags");
                    len = hashtagsArray.length();
                    hashtagEntities = new HashtagEntity[len];
                    for (int i = 0; i < len; i++) {
                        hashtagEntities[i] = new HashtagEntityJSONImpl(hashtagsArray.getJSONObject(i));
                    }
                }

                if (!entities.isNull("symbols")) {
                    JSONArray symbolsArray = entities.getJSONArray("symbols");
                    len = symbolsArray.length();
                    symbolEntities = new SymbolEntity[len];
                    for (int i = 0; i < len; i++) {
                        // HashtagEntityJSONImpl also implements SymbolEntities
                        symbolEntities[i] = new HashtagEntityJSONImpl(symbolsArray.getJSONObject(i));
                    }
                }

                if (!entities.isNull("media")) {
                    JSONArray mediaArray = entities.getJSONArray("media");
                    len = mediaArray.length();
                    mediaEntities = new MediaEntity[len];
                    for (int i = 0; i < len; i++) {
                        mediaEntities[i] = new MediaEntityJSONImpl(mediaArray.getJSONObject(i));
                    }
                }
            }
            userMentionEntities = userMentionEntities == null ? new UserMentionEntity[0] : userMentionEntities;
            urlEntities = urlEntities == null ? new URLEntity[0] : urlEntities;
            hashtagEntities = hashtagEntities == null ? new HashtagEntity[0] : hashtagEntities;
            symbolEntities = symbolEntities == null ? new SymbolEntity[0] : symbolEntities;
            mediaEntities = mediaEntities == null ? new MediaEntity[0] : mediaEntities;
            text = HTMLEntity.unescapeAndSlideEntityIncdices(messageData.getString("text"), userMentionEntities,
                    urlEntities, hashtagEntities, mediaEntities);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    static PagableResponseList<DirectMessageEvent> createDirectMessageList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
            }
            JSONObject responseJsonObj = res.asJSONObject();

            if (!responseJsonObj.has("events")) {
                throw new TwitterException("Invalid JSON response during fetching direct message event list");
            }

            JSONArray list = responseJsonObj.getJSONArray("events");

            int size = list.length();
            PagableResponseList<DirectMessageEvent> directMessages = new PagableResponseListImpl<DirectMessageEvent>(size, responseJsonObj, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                DirectMessageEvent directMessage = new DirectMessageEventJSONImpl(json);
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
        return obj instanceof DirectMessageEvent && ((DirectMessageEvent) obj).getId() == this.id;
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
    public long getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public long getSenderId() {
        return senderId;
    }

    @Override
    public long getRecipientId() {
        return recipientId;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
