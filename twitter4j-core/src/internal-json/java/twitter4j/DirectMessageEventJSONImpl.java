package twitter4j;

import twitter4j.conf.Configuration;

import java.util.Arrays;
import java.util.Date;

/**
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
/*package*/ final class DirectMessageEventJSONImpl extends TwitterResponseImpl implements DirectMessageEvent, java.io.Serializable {
    private static final long serialVersionUID = -7387326608536231792L;
    private String type;
    private long id;
    private Date createdTimestamp;
    private long recipientId;
    private long senderId;
    private String text;
    private UserMentionEntity[] userMentionEntities;
    private URLEntity[] urlEntities;
    private HashtagEntity[] hashtagEntities;
    private MediaEntity[] mediaEntities;
    private SymbolEntity[] symbolEntities;


    /*package*/DirectMessageEventJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        try {
            init(json.getJSONObject("event"));
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
                TwitterObjectFactory.registerJSONObject(this, json);
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /*package*/DirectMessageEventJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject event) throws TwitterException {

        try {
            type = ParseUtil.getUnescapedString("type", event);
            id = ParseUtil.getLong("id", event);
            createdTimestamp = new Date(event.getLong("created_timestamp"));

            final JSONObject messageCreate = event.getJSONObject("message_create");
            recipientId = ParseUtil.getLong("recipient_id", messageCreate.getJSONObject("target"));
            senderId = ParseUtil.getLong("sender_id", messageCreate);

            final JSONObject messageData = messageCreate.getJSONObject("message_data");
            text = ParseUtil.getUnescapedString("text", messageData);

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

    @Override
    public String getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Date getCreatedTimestamp() {
        return createdTimestamp;
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
    public URLEntity[] getUrlEntities() {
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

        DirectMessageEventJSONImpl that = (DirectMessageEventJSONImpl) o;

        if (id != that.id) return false;
        if (recipientId != that.recipientId) return false;
        if (senderId != that.senderId) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (createdTimestamp != null ? !createdTimestamp.equals(that.createdTimestamp) : that.createdTimestamp != null)
            return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
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
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (createdTimestamp != null ? createdTimestamp.hashCode() : 0);
        result = 31 * result + (int) (recipientId ^ (recipientId >>> 32));
        result = 31 * result + (int) (senderId ^ (senderId >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(userMentionEntities);
        result = 31 * result + Arrays.hashCode(urlEntities);
        result = 31 * result + Arrays.hashCode(hashtagEntities);
        result = 31 * result + Arrays.hashCode(mediaEntities);
        result = 31 * result + Arrays.hashCode(symbolEntities);
        return result;
    }

    @Override
    public String toString() {
        return "DirectMessageEventJSONImpl{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", createdTimestamp=" + createdTimestamp +
                ", recipientId=" + recipientId +
                ", senderId=" + senderId +
                ", text='" + text + '\'' +
                ", userMentionEntities=" + Arrays.toString(userMentionEntities) +
                ", urlEntities=" + Arrays.toString(urlEntities) +
                ", hashtagEntities=" + Arrays.toString(hashtagEntities) +
                ", mediaEntities=" + Arrays.toString(mediaEntities) +
                ", symbolEntities=" + Arrays.toString(symbolEntities) +
                '}';
    }
}
