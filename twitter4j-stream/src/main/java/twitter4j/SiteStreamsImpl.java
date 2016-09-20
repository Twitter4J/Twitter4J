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

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
final class SiteStreamsImpl extends StatusStreamBase {

    private final StreamController cs;

    /*package*/ SiteStreamsImpl(Dispatcher dispatcher, InputStream stream, Configuration conf, StreamController cs) throws IOException {
        super(dispatcher, stream, conf);
        this.cs = cs;
    }

    /*package*/ SiteStreamsImpl(Dispatcher dispatcher, HttpResponse response, Configuration conf, StreamController cs) throws IOException {
        super(dispatcher, response, conf);
        this.cs = cs;
    }

    @Override
    protected String parseLine(String line) {
        if ("".equals(line) || null == line) {
            return line;
        }
        int userIdEnd = line.indexOf(',', 12);
        // in the documentation for_user is not quoted, but actually it is quoted
        if (cs.getControlURI() == null &&
                line.charAt(2) == 'c' &&
                line.charAt(3) == 'o' &&
                line.charAt(4) == 'n') {
            // control endpoint uri
            // https://dev.twitter.com/docs/streaming-api/control-streams
            JSONObject control = null;
            try {
                control = new JSONObject(line);
                cs.setControlURI(CONF.getSiteStreamBaseURL() + control.getJSONObject("control").getString("control_uri"));
                logger.info("control_uri: " + cs.getControlURI());
            } catch (JSONException e) {
                logger.warn("received unexpected event:" + line);
            }
            return null;
        }

        if (line.charAt(2) == 'd') {
            // disconnection notice
            // {"disconnect":{"code":3,"stream_name":"yusuke-sitestream6139-yusuke","reason":"control request for yusuke-sitestream6139 106.171.17.29 /1.1/site.json sitestream"}}
            return line;
        }
        if (line.charAt(12) == '"') {
            forUser.set(Long.parseLong(line.substring(13, userIdEnd - 1)));
        } else {
            forUser.set(Long.parseLong(line.substring(12, userIdEnd)));
        }
        return line.substring(userIdEnd + 11, line.length() - 1);
    }

    @Override
    protected void onClose() {
        cs.setControlURI(null);
    }

    private static final ThreadLocal<Long> forUser =
            new ThreadLocal<Long>() {
                @Override
                protected Long initialValue() {
                    return 0L;
                }
            };

    @Override
    protected void onMessage(String rawString, RawStreamListener[] listeners) throws TwitterException {
        for (RawStreamListener listener : listeners) {
            listener.onMessage(rawString);
        }
    }

    @Override
    protected void onStatus(final JSONObject json, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onStatus(forUser.get(), asStatus(json));
        }
    }

    @Override
    protected void onDelete(final JSONObject json, StreamListener[] listeners) throws JSONException {
        JSONObject deletionNotice = json.getJSONObject("delete");
        if (deletionNotice.has("status")) {
            for (StreamListener listener : listeners) {
                ((SiteStreamsListener) listener).onDeletionNotice(forUser.get(), new StatusDeletionNoticeImpl(deletionNotice.getJSONObject("status")));
            }
        } else {
            JSONObject directMessage = deletionNotice.getJSONObject("direct_message");
            for (StreamListener listener : listeners) {
                ((SiteStreamsListener) listener).onDeletionNotice(forUser.get(), ParseUtil.getInt("id", directMessage)
                        , ParseUtil.getLong("user_id", directMessage));
            }
        }
    }

    @Override
    protected void onDirectMessage(final JSONObject json, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onDirectMessage(forUser.get(), asDirectMessage(json));
        }
    }

    @Override
    protected void onFriends(final JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onFriendList(forUser.get(), asFriendList(json));
        }
    }

    @Override
    protected void onFavorite(final JSONObject source, final JSONObject target, final JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onFavorite(forUser.get(), asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    protected void onUnfavorite(final JSONObject source, final JSONObject target, final JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUnfavorite(forUser.get(), asUser(source)
                    , asUser(target), asStatus(targetObject));
        }
    }

    @Override
    protected void onFollow(final JSONObject source, final JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onFollow(forUser.get(), asUser(source), asUser(target));
        }
    }

    @Override
    protected void onUnfollow(final JSONObject source, final JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUnfollow(forUser.get(), asUser(source), asUser(target));
        }
    }

    @Override
    protected void onUserListMemberAddition(final JSONObject addedMember, final JSONObject owner, final JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserListMemberAddition(forUser.get(), asUser(addedMember)
                    , asUser(owner), asUserList(userList));
        }
    }

    @Override
    protected void onUserListMemberDeletion(final JSONObject deletedMember, final JSONObject owner, final JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserListMemberDeletion(forUser.get(), asUser(deletedMember)
                    , asUser(owner), asUserList(userList));
        }
    }

    @Override
    protected void onUserListSubscription(final JSONObject source, final JSONObject owner, final JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserListSubscription(forUser.get(), asUser(source)
                    , asUser(owner), asUserList(userList));
        }
    }

    @Override
    protected void onUserListUnsubscription(final JSONObject source, final JSONObject owner, final JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserListUnsubscription(forUser.get(), asUser(source)
                    , asUser(owner), asUserList(userList));
        }
    }

    @Override
    protected void onUserListCreation(final JSONObject source, final JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserListCreation(forUser.get(), asUser(source)
                    , asUserList(userList));
        }
    }

    @Override
    protected void onUserListUpdated(final JSONObject source, final JSONObject userList, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserListUpdate(forUser.get(), asUser(source)
                    , asUserList(userList));
        }
    }

    @Override
    protected void onUserListDestroyed(final JSONObject source, final JSONObject userList, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserListDeletion(forUser.get(), asUser(source)
                    , asUserList(userList));
        }
    }

    @Override
    protected void onUserUpdate(final JSONObject source, final JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserProfileUpdate(forUser.get(), asUser(source));
        }
    }

    @Override
    protected void onUserSuspension(final long target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserSuspension(forUser.get(), target);
        }
    }

    @Override
    protected void onUserDeletion(final long target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUserDeletion(forUser.get(), target);
        }
    }

    @Override
    protected void onBlock(final JSONObject source, final JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onBlock(forUser.get(), asUser(source), asUser(target));
        }
    }

    @Override
    protected void onUnblock(final JSONObject source, final JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onUnblock(forUser.get(), asUser(source), asUser(target));
        }
    }

    @Override
    void onRetweetedRetweet(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onRetweetedRetweet(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    void onFavoritedRetweet(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onFavoritedRetweet(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    public void onException(final Exception ex, StreamListener[] listeners) {
        for (StreamListener listener : listeners) {
            listener.onException(ex);
        }
    }

    protected static final RawStreamListener[] EMPTY = new RawStreamListener[0];

    @Override
    public void next(StatusListener listener) throws TwitterException {
        handleNextElement(new StatusListener[]{listener}, EMPTY);
    }

    @Override
    public void next(StreamListener[] listeners, RawStreamListener[] rawStreamListeners) throws TwitterException {
        handleNextElement(listeners, rawStreamListeners);
    }

    @Override
    public void onDisconnectionNotice(String line, StreamListener[] listeners) {
        for (StreamListener listener : listeners) {
            ((SiteStreamsListener) listener).onDisconnectionNotice(line);
        }
    }

}
