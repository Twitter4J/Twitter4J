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
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.json.z_T4JInternalFactory;
import twitter4j.internal.json.z_T4JInternalJSONImplFactory;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.json.JSONObjectType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
abstract class StatusStreamBase implements StatusStream {
    protected static final Logger logger = Logger.getLogger(StatusStreamImpl.class);

    private boolean streamAlive = true;
    private BufferedReader br;
    private InputStream is;
    private HttpResponse response;
    protected final Dispatcher dispatcher;
    protected final Configuration CONF;
    protected z_T4JInternalFactory factory;

    /*package*/

    StatusStreamBase(Dispatcher dispatcher, InputStream stream, Configuration conf) throws IOException {
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        this.dispatcher = dispatcher;
        this.CONF = conf;
        this.factory = new z_T4JInternalJSONImplFactory(conf);
    }
    /*package*/

    StatusStreamBase(Dispatcher dispatcher, HttpResponse response, Configuration conf) throws IOException {
        this(dispatcher, response.asStream(), conf);
        this.response = response;
    }

    protected String parseLine(String line) {
        return line;
    }

    abstract class StreamEvent implements Runnable {
        String line;

        StreamEvent(String line) {
            this.line = line;
        }
    }

    protected void handleNextElement(final StreamListener[] listeners,
                                     final RawStreamListener[] rawStreamListeners) throws TwitterException {
        if (!streamAlive) {
            throw new IllegalStateException("Stream already closed.");
        }
        try {
            String line = br.readLine();
            if (null == line) {
                //invalidate this status stream
                throw new IOException("the end of the stream has been reached");
            }
            dispatcher.invokeLater(new StreamEvent(line) {
                public void run() {
                    try {
                        if (rawStreamListeners.length > 0) {
                            onMessage(line, rawStreamListeners);
                        }
                        // SiteStreamsImpl will parse "forUser" attribute
                        line = parseLine(line);
                        if (line != null && line.length() > 0) {
                            // parsing JSON is an expensive process and can be avoided when all listeners are instanceof RawStreamListener
                            if (listeners.length > 0) {
                                if (CONF.isJSONStoreEnabled()) {
                                    DataObjectFactoryUtil.clearThreadLocalMap();
                                }
                                JSONObject json = new JSONObject(line);
                                JSONObjectType.Type event = JSONObjectType.determine(json);
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Received:", CONF.isPrettyDebugEnabled() ? json.toString(1) : json.toString());
                                }
                                switch (event) {
                                    case SENDER:
                                        onSender(json, listeners);
                                        break;
                                    case STATUS:
                                        onStatus(json, listeners);
                                        break;
                                    case DIRECT_MESSAGE:
                                        onDirectMessage(json, listeners);
                                        break;
                                    case DELETE:
                                        onDelete(json, listeners);
                                        break;
                                    case LIMIT:
                                        onLimit(json, listeners);
                                        break;
                                    case STALL_WARNING:
                                        onStallWarning(json, listeners);
                                        break;
                                    case SCRUB_GEO:
                                        onScrubGeo(json, listeners);
                                        break;
                                    case FRIENDS:
                                        onFriends(json, listeners);
                                        break;
                                    case FAVORITE:
                                        onFavorite(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case UNFAVORITE:
                                        onUnfavorite(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case FOLLOW:
                                        onFollow(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case UNFOLLOW:
                                        onUnfollow(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case USER_LIST_MEMBER_ADDED:
                                        onUserListMemberAddition(json.getJSONObject("target"), json.getJSONObject("source"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case USER_LIST_MEMBER_DELETED:
                                        onUserListMemberDeletion(json.getJSONObject("target"), json.getJSONObject("source"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case USER_LIST_SUBSCRIBED:
                                        onUserListSubscription(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case USER_LIST_UNSUBSCRIBED:
                                        onUserListUnsubscription(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case USER_LIST_CREATED:
                                        onUserListCreation(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case USER_LIST_UPDATED:
                                        onUserListUpdated(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case USER_LIST_DESTROYED:
                                        onUserListDestroyed(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case USER_UPDATE:
                                        onUserUpdate(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case BLOCK:
                                        onBlock(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case UNBLOCK:
                                        onUnblock(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case DISCONNECTION:
                                        onDisconnectionNotice(line, listeners);
                                        break;
                                    case UNKNOWN:
                                    default:
                                        logger.warn("Received unknown event:", CONF.isPrettyDebugEnabled() ? json.toString(1) : json.toString());
                                }
                            }
                        }
                    } catch (Exception ex) {
                        onException(ex, listeners);
                    }
                }
            });

        } catch (IOException ioe) {
            try {
                is.close();
            } catch (IOException ignore) {
            }
            boolean isUnexpectedException = streamAlive;
            streamAlive = false;
            if (isUnexpectedException) {
                throw new TwitterException("Stream closed.", ioe);
            }
        }
    }

    protected void onMessage(String rawString, RawStreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onMessage");
    }

    protected void onSender(JSONObject json, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onSender");
    }

    protected void onStatus(JSONObject json, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onStatus");
    }

    protected void onDirectMessage(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onDirectMessage");
    }

    protected void onDelete(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onDelete");
    }

    protected void onLimit(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onLimit");
    }

    protected void onStallWarning(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onStallWarning");
    }

    protected void onScrubGeo(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onScrubGeo");
    }

    protected void onFriends(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onFriends");
    }

    protected void onFavorite(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onFavorite");
    }

    protected void onUnfavorite(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onUnfavorite");
    }

    protected void onFollow(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onFollow");
    }

    protected void onUnfollow(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onUnfollow");
    }

    protected void onUserListMemberAddition(JSONObject addedMember, JSONObject owner, JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListMemberAddition");
    }

    protected void onUserListMemberDeletion(JSONObject deletedMember, JSONObject owner, JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListMemberDeletion");
    }

    protected void onUserListSubscription(JSONObject source, JSONObject owner, JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListSubscription");
    }

    protected void onUserListUnsubscription(JSONObject source, JSONObject owner, JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListUnsubscription");
    }

    protected void onUserListCreation(JSONObject source, JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListCreation");
    }

    protected void onUserListUpdated(JSONObject source, JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListUpdated");
    }

    protected void onUserListDestroyed(JSONObject source, JSONObject userList, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onUserListDestroyed");
    }

    protected void onUserUpdate(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onUserUpdate");
    }

    protected void onBlock(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onBlock");
    }

    protected void onUnblock(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        logger.warn("Unhandled event: onUnblock");
    }

    protected void onDisconnectionNotice(String line, StreamListener[] listeners) {
        logger.warn("Unhandled event: ", line);
    }

    protected void onException(Exception e, StreamListener[] listeners) {
        logger.warn("Unhandled event: ", e.getMessage());
    }

    public void close() throws IOException {
        streamAlive = false;
        is.close();
        br.close();
        if (response != null) {
            response.disconnect();
        }
    }

    protected Status asStatus(JSONObject json) throws TwitterException {
        Status status = factory.createStatus(json);
        if (CONF.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(status, json);
        }
        return status;
    }

    protected DirectMessage asDirectMessage(JSONObject json) throws TwitterException {
        DirectMessage directMessage;
        try {
            directMessage = factory.createDirectMessage(json.getJSONObject("direct_message"));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
        if (CONF.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(directMessage, json);
        }
        return directMessage;
    }

    protected long[] asFriendList(JSONObject json) throws TwitterException {
        JSONArray friends;
        try {
            friends = json.getJSONArray("friends");
            long[] friendIds = new long[friends.length()];
            for (int i = 0; i < friendIds.length; ++i) {
                friendIds[i] = Long.parseLong(friends.getString(i));
            }
            return friendIds;
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    protected User asUser(JSONObject json) throws TwitterException {
        User user = factory.createUser(json);
        if (CONF.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(user, json);
        }
        return user;
    }

    protected UserList asUserList(JSONObject json) throws TwitterException {
        UserList userList = factory.createAUserList(json);
        if (CONF.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(userList, json);
        }
        return userList;
    }

    public abstract void next(StatusListener listener) throws TwitterException;

    public abstract void next(StreamListener[] listeners, RawStreamListener[] rawStreamListeners) throws TwitterException;

    public void onException(Exception e, StreamListener[] listeners, RawStreamListener[] rawStreamListeners) {
        for (StreamListener listener : listeners) {
            listener.onException(e);
        }
        for (RawStreamListener listener : rawStreamListeners) {
            listener.onException(e);
        }
    }
}
