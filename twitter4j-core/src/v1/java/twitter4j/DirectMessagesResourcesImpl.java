package twitter4j;

import twitter4j.api.DirectMessagesResources;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

class DirectMessagesResourcesImpl extends APIResourceBase implements DirectMessagesResources {
    DirectMessagesResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                                String IMPLICIT_PARAMS_STR,
                                List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                                List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public DirectMessageList getDirectMessages(int count) throws TwitterException {
        return factory.createDirectMessageList(get(restBaseURL + "direct_messages/events/list.json", new HttpParameter("count", count)));
    }

    @Override
    public DirectMessageList getDirectMessages(int count, String cursor) throws TwitterException {
        return factory.createDirectMessageList(get(restBaseURL + "direct_messages/events/list.json", new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }


    @Override
    public DirectMessage showDirectMessage(long id) throws TwitterException {
        return factory.createDirectMessage(get(restBaseURL + "direct_messages/events/show.json?id=" + id));
    }

    @Override
    public void destroyDirectMessage(long id) throws TwitterException {
        delete(restBaseURL + "direct_messages/events/destroy.json?id=" + id);
    }

    @Override
    public DirectMessage sendDirectMessage(long recipientId, String text, QuickReply... quickReplies)
            throws TwitterException {
        try {
            return factory.createDirectMessage(post(restBaseURL + "direct_messages/events/new.json", createMessageCreateJsonObject(recipientId, text, -1L, null, quickReplies)));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    @Override
    public DirectMessage sendDirectMessage(long recipientId, String text, String quickReplyResponse)
            throws TwitterException {
        try {
            return factory.createDirectMessage(post(restBaseURL + "direct_messages/events/new.json", createMessageCreateJsonObject(recipientId, text, -1L, quickReplyResponse)));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    private static JSONObject createMessageCreateJsonObject(long recipientId, String text, long mediaId, String quickReplyResponse, QuickReply... quickReplies) throws JSONException {
        String type = mediaId == -1 ? null : "media";

        final JSONObject messageDataJSON = new JSONObject();

        final JSONObject target = new JSONObject();
        target.put("recipient_id", recipientId);
        messageDataJSON.put("target", target);

        final JSONObject messageData = new JSONObject();
        messageData.put("text", text);
        if (type != null && mediaId != -1) {
            final JSONObject attachment = new JSONObject();
            attachment.put("type", type);
            if (type.equals("media")) {
                final JSONObject media = new JSONObject();
                media.put("id", mediaId);
                attachment.put("media", media);
            }
            messageData.put("attachment", attachment);
        }
        // https://developer.twitter.com/en/docs/direct-messages/quick-replies/api-reference/options
        if (quickReplies.length > 0) {
            JSONObject quickReplyJSON = new JSONObject();
            quickReplyJSON.put("type", "options");
            JSONArray jsonArray = new JSONArray();
            for (QuickReply quickReply : quickReplies) {
                JSONObject option = new JSONObject();
                option.put("label", quickReply.getLabel());
                if (quickReply.getDescription() != null) {
                    option.put("description", quickReply.getDescription());
                }
                if (quickReply.getMetadata() != null) {
                    option.put("metadata", quickReply.getMetadata());
                }
                jsonArray.put(option);
            }
            quickReplyJSON.put("options", jsonArray);
            messageData.put("quick_reply", quickReplyJSON);
        }
        if (quickReplyResponse != null) {
            JSONObject quickReplyResponseJSON = new JSONObject();
            quickReplyResponseJSON.put("type", "options");
            quickReplyResponseJSON.put("metadata", quickReplyResponse);
            messageData.put("quick_reply_response", quickReplyResponseJSON);
        }
        messageDataJSON.put("message_data", messageData);

        final JSONObject json = new JSONObject();
        final JSONObject event = new JSONObject();
        event.put("type", "message_create");
        event.put("message_create", messageDataJSON);
        json.put("event", event);

        return json;
    }

    @Override
    public DirectMessage sendDirectMessage(long recipientId, String text, long mediaId)
            throws TwitterException {
        try {
            return factory.createDirectMessage(post(restBaseURL + "direct_messages/events/new.json", createMessageCreateJsonObject(recipientId, text, mediaId, null)));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    @Override
    public DirectMessage sendDirectMessage(long recipientId, String text) throws TwitterException {
        return this.sendDirectMessage(recipientId, text, -1L);
    }

    @Override
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        return this.sendDirectMessage(factory.createUser(get(restBaseURL + "users/show.json", new HttpParameter("screen_name", screenName))).getId(), text);
    }

    @Override
    public InputStream getDMImageAsStream(String url) throws TwitterException {
        return get(url).asStream();
    }
}
