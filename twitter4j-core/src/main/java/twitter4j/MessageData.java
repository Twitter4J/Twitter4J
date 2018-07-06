package twitter4j;

/**
 * @author Norbert Bartels - n.bartels at phpmonkeys.de
 * <p>
 * Container object for sending direct messages
 */
public class MessageData {

    private Long userId;

    private String messageText;

    /**
     * Constructor for the message data object.
     * <p>
     * A recipient is needed to send a message, so it should be given on constructing.
     *
     * @param userId the user id of the recipient
     */
    public MessageData(Long userId) {
        this.userId = userId;
    }

    /**
     * Sets the message text of the message
     *
     * @param messageText
     */
    public void setText(String messageText) {
        this.messageText = messageText;
    }

    public JSONObject asJson() throws TwitterException {
        JSONObject result = new JSONObject();
        try {
            JSONObject target = new JSONObject();
            target.put("recipient_id", String.valueOf(userId));
            JSONObject messageData = new JSONObject();
            messageData.put("text", this.messageText);
            JSONObject messageCreate = new JSONObject();
            messageCreate.put("target", target);
            messageCreate.put("message_data", messageData);
            JSONObject event = new JSONObject();
            event.put("type", "message_create");
            event.put("message_create", messageCreate);

            result.put("event", event);
        } catch (JSONException jse) {
            throw new TwitterException("Cannot built message data", jse);
        }

        return result;
    }
}
