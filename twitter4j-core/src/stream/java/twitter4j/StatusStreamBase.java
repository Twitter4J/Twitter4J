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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
@SuppressWarnings({"unused", "RedundantThrows"})
abstract class StatusStreamBase implements StatusStream {
    static final Logger logger = Logger.getLogger();

    private boolean streamAlive = true;
    private final BufferedReader br;
    private final InputStream is;
    private HttpResponse response;
    private final ExecutorService dispatcher = Executors.newFixedThreadPool(1);
    private final boolean jsonStoreEnabled;
    private final boolean prettyDebug;
    private final List<StreamListener> streamListeners;
    private final List<RawStreamListener> rawStreamListeners;

    /*package*/

    StatusStreamBase(InputStream stream, List<StreamListener> streamListeners
            , List<RawStreamListener> rawStreamListeners, boolean jsonStoreEnabled, boolean prettyDebug) {
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        this.streamListeners = streamListeners;
        this.rawStreamListeners = rawStreamListeners;
        this.jsonStoreEnabled = jsonStoreEnabled;
        this.prettyDebug = prettyDebug;
    }
    /*package*/

    StatusStreamBase(HttpResponse response, List<StreamListener> streamListeners
            , List<RawStreamListener> rawStreamListeners, boolean jsonStoreEnabled, boolean prettyDebug) {
        this(response.asStream(), streamListeners
                , rawStreamListeners, jsonStoreEnabled, prettyDebug);
        this.response = response;
    }

    String parseLine(String line) {
        return line;
    }

    abstract static class StreamEvent implements Runnable {
        String line;

        StreamEvent(String line) {
            this.line = line;
        }
    }

    void handleNextElement(final List<StreamListener> listeners,
                           final List<RawStreamListener> rawStreamListeners) throws TwitterException {
        if (!streamAlive) {
            throw new IllegalStateException("Stream already closed.");
        }
        try {
            String line = br.readLine();
            if (null == line) {
                //invalidate this status stream
                throw new IOException("the end of the stream has been reached");
            }
            dispatcher.execute(new StreamEvent(line) {
                @Override
                public void run() {
                    try {
                        rawStreamListeners.forEach(listener -> {
                            try {
                                onMessage(line,listener);
                            } catch (Exception ex) {
                                logger.warn(ex.getMessage());
                            }
                        });
                        // SiteStreamsImpl will parse "forUser" attribute
                        line = parseLine(line);
                        if (line != null && line.length() > 0) {
                            // parsing JSON is an expensive process and can be avoided when all listener are instanceof RawStreamListener
                            if (jsonStoreEnabled) {
                                TwitterObjectFactory.clearThreadLocalMap();
                            }
                            JSONObject json = new JSONObject(line);
                            JSONObjectType.Type event = JSONObjectType.determine(json);
                            if (logger.isDebugEnabled()) {
                                logger.debug("Received:", prettyDebug ? json.toString(1) : json.toString());
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
                                        onUserListCreation(json.getJSONObject("source"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case USER_LIST_UPDATED:
                                        onUserListUpdated(json.getJSONObject("source"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case USER_LIST_DESTROYED:
                                        onUserListDestroyed(json.getJSONObject("source"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case USER_UPDATE:
                                        onUserUpdate(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case USER_DELETE:
                                        onUserDeletion(json.getLong("target"), listeners);
                                        break;
                                    case USER_SUSPEND:
                                        onUserSuspension(json.getLong("target"), listeners);
                                        break;
                                    case BLOCK:
                                        onBlock(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case UNBLOCK:
                                        onUnblock(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case RETWEETED_RETWEET:
                                        onRetweetedRetweet(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case FAVORITED_RETWEET:
                                        onFavoritedRetweet(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case QUOTED_TWEET:
                                        onQuotedTweet(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"), listeners);
                                        break;
                                    case DISCONNECTION:
                                        onDisconnectionNotice(line, listeners);
                                        break;
                                    case MUTE:
                                        onMute(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case UNMUTE:
                                        onUnmute(json.getJSONObject("source"), json.getJSONObject("target"), listeners);
                                        break;
                                    case UNKNOWN:
                                    default:
                                        logger.warn("Received unknown event:", prettyDebug ? json.toString(1) : json.toString());
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
            onClose();
            if (isUnexpectedException) {
                throw new TwitterException("Stream closed.", ioe);
            }
        }
    }

    void onMessage(String rawString, RawStreamListener listener) throws TwitterException {
        logger.warn("Unhandled event: onMessage");
    }

    void onSender(JSONObject json, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onSender");
    }

    void onStatus(JSONObject json, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onStatus");
    }

    void onDirectMessage(JSONObject json, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onDirectMessage");
    }

    void onDelete(JSONObject json, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onDelete");
    }

    void onLimit(JSONObject json, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onLimit");
    }

    void onStallWarning(JSONObject json, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onStallWarning");
    }

    void onScrubGeo(JSONObject json, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onScrubGeo");
    }

    void onFriends(JSONObject json, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onFriends");
    }

    void onFavorite(JSONObject source, JSONObject target, JSONObject targetObject, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onFavorite");
    }

    void onUnfavorite(JSONObject source, JSONObject target, JSONObject targetObject, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onUnfavorite");
    }

    void onFollow(JSONObject source, JSONObject target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onFollow");
    }

    void onUnfollow(JSONObject source, JSONObject target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onUnfollow");
    }

    void onUserListMemberAddition(JSONObject addedMember, JSONObject owner, JSONObject userList, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListMemberAddition");
    }

    void onUserListMemberDeletion(JSONObject deletedMember, JSONObject owner, JSONObject userList, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListMemberDeletion");
    }

    void onUserListSubscription(JSONObject source, JSONObject owner, JSONObject userList, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListSubscription");
    }

    void onUserListUnsubscription(JSONObject source, JSONObject owner, JSONObject userList, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListUnsubscription");
    }

    void onUserListCreation(JSONObject source, JSONObject userList, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListCreation");
    }

    void onUserListUpdated(JSONObject source, JSONObject userList, List<StreamListener> listeners) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListUpdated");
    }

    void onUserListDestroyed(JSONObject source, JSONObject userList, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onUserListDestroyed");
    }

    void onUserUpdate(JSONObject source, JSONObject target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onUserUpdate");
    }

    void onUserDeletion(long target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onUserDeletion");
    }

    void onUserSuspension(long target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onUserSuspension");
    }

    void onBlock(JSONObject source, JSONObject target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onBlock");
    }

    void onUnblock(JSONObject source, JSONObject target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onUnblock");
    }

    void onRetweetedRetweet(JSONObject source, JSONObject target, JSONObject targetObject, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onRetweetedRetweet");
    }

    void onFavoritedRetweet(JSONObject source, JSONObject target, JSONObject targetObject, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onFavoritedRetweet");
    }

    void onQuotedTweet(JSONObject source, JSONObject target, JSONObject targetObject, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onQuotedTweet");
    }

    void onMute(JSONObject source, JSONObject target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onMute");
    }

    void onUnmute(JSONObject source, JSONObject target, List<StreamListener> listeners) throws TwitterException {
        logger.warn("Unhandled event: onUnmute");
    }

    void onDisconnectionNotice(String line, List<StreamListener> listeners) {
        logger.warn("Unhandled event: ", line);
    }

    void onException(Exception e, List<StreamListener> listeners) {
        logger.warn("Unhandled event: ", e.getMessage());
    }

    protected abstract void onClose();

    @Override
    public void close() throws IOException {
        streamAlive = false;
        is.close();
        br.close();
        if (response != null) {
            response.disconnect();
        }
        onClose();
    }

    Status asStatus(JSONObject json) throws TwitterException {
        Status status = new StatusJSONImpl(json);

        if (jsonStoreEnabled) {
            TwitterObjectFactory.registerJSONObject(status, json);
        }
        return status;
    }

    DirectMessage asDirectMessage(JSONObject json) throws TwitterException {
        try {
            JSONObject dmJSON = json.getJSONObject("direct_message");
            DirectMessage directMessage = new DirectMessageJSONImpl(dmJSON);
            if (jsonStoreEnabled) {
                TwitterObjectFactory.registerJSONObject(directMessage, dmJSON);
            }
            return directMessage;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    long[] asFriendList(JSONObject json) throws TwitterException {
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

    User asUser(JSONObject json) throws TwitterException {
        User user = new UserJSONImpl(json);
        if (jsonStoreEnabled) {
            TwitterObjectFactory.registerJSONObject(user, json);
        }
        return user;
    }

    UserList asUserList(JSONObject json) throws TwitterException {
        UserList userList = new UserListJSONImpl(json);
        if (jsonStoreEnabled) {
            TwitterObjectFactory.registerJSONObject(userList, json);
        }
        return userList;
    }

    @Override
    public abstract void next(List<StreamListener> listeners) throws TwitterException;

    public abstract void next(List<StreamListener> listeners, List<RawStreamListener> rawStreamListeners) throws TwitterException;

    public void onException(Exception e) {
        streamListeners.forEach(listener -> {
            try {
                listener.onException(e);
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
            }
        });
        rawStreamListeners.forEach(listener -> {
            try {
                listener.onException(e);
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
            }
        });
    }
}
