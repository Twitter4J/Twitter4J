package twitter4j;

/**
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
public final class MessageData {

    private final long recipientId;
    private final String text;
    private String type = null;
    private long mediaId = -1;

    public MessageData(long recipientId, String text) {
        this.recipientId = recipientId;
        this.text = text;
    }

    public MessageData setMediaId(long mediaId) {
        this.type = "media";
        this.mediaId = mediaId;
        return this;
    }

    public JSONObject createMessageCreateJsonObject() throws JSONException {

        final JSONObject json = new JSONObject();

        final JSONObject target = new JSONObject();
        target.put("recipient_id", this.recipientId);
        json.put("target", target);

        final JSONObject messageData = new JSONObject();
        messageData.put("text", this.text);
        if (this.type != null && this.mediaId != -1) {
            final JSONObject attachment = new JSONObject();
            attachment.put("type", this.type);
            if (type.equals("media")) {
                final JSONObject media = new JSONObject();
                media.put("id", this.mediaId);
                attachment.put("media", media);
            }
            messageData.put("attachment", attachment);
        }
        json.put("message_data", messageData);

        return json;
    }

}
