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
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.util.z_T4JInternalParseUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
class SiteStreamsImpl extends AbstractStreamImplementation implements StreamImplementation, StreamListener {

    SiteStreamsListener listener;

    /*package*/ SiteStreamsImpl(Dispatcher dispatcher, InputStream stream, Configuration conf) throws IOException {
        super(dispatcher, stream, conf);
    }

    /*package*/ SiteStreamsImpl(Dispatcher dispatcher, HttpResponse response, Configuration conf) throws IOException {
        super(dispatcher, response, conf);
    }

    public void next(StreamListener[] listeners) throws TwitterException {
        this.listener = (SiteStreamsListener) listeners[0];
        handleNextElement();
    }

    protected String parseLine(String line) {
        if ("".equals(line) || null == line) {
            return line;
        }
        int userIdEnd = line.indexOf(',', 12);
        // in the documentation for_user is not quoted, but actually it is quoted
        // n
        if (line.charAt(12) == '"') {
            forUser.set(Integer.parseInt(line.substring(13, userIdEnd - 1)));
            return line.substring(userIdEnd + 11, line.length() - 1);
        } else {
            forUser.set(Integer.parseInt(line.substring(12, userIdEnd)));
            return line.substring(userIdEnd + 11, line.length() - 1);
        }
    }

    private static ThreadLocal<Integer> forUser =
            new ThreadLocal<Integer>() {
                @Override
                protected Integer initialValue() {
                    return 0;
                }
            };

    protected void onStatus(final JSONObject json) throws TwitterException {
        listener.onStatus(forUser.get(), asStatus(json));
    }

    @Override
    protected void onDelete(final JSONObject json) throws JSONException {
        JSONObject deletionNotice = json.getJSONObject("delete");
        if (deletionNotice.has("status")) {
            listener.onDeletionNotice(forUser.get(), new StatusDeletionNoticeImpl(deletionNotice.getJSONObject("status")));
        } else {
            JSONObject directMessage = deletionNotice.getJSONObject("direct_message");
            listener.onDeletionNotice(forUser.get(), z_T4JInternalParseUtil.getInt("id", directMessage)
                    , z_T4JInternalParseUtil.getLong("user_id", directMessage));
        }
    }

    protected void onDirectMessage(final JSONObject json) throws TwitterException {
        listener.onDirectMessage(forUser.get(), asDirectMessage(json));
    }

    protected void onFriends(final JSONObject json) throws TwitterException, JSONException {
        listener.onFriendList(forUser.get(), asFriendList(json));
    }

    protected void onFavorite(final JSONObject source, final JSONObject target, final JSONObject targetObject) throws TwitterException {
        listener.onFavorite(forUser.get(), asUser(source), asUser(target), asStatus(targetObject));
    }

    protected void onUnfavorite(final JSONObject source, final JSONObject target, final JSONObject targetObject) throws TwitterException {
        listener.onUnfavorite(forUser.get(), asUser(source)
                , asUser(target), asStatus(targetObject));
    }

    protected void onFollow(final JSONObject source, final JSONObject target) throws TwitterException {
        listener.onFollow(forUser.get(), asUser(source), asUser(target));
    }

    protected void onUnfollow(final JSONObject source, final JSONObject target) throws TwitterException {
        listener.onUnfollow(forUser.get(), asUser(source), asUser(target));
    }

    protected void onUserListMemberAddition(final JSONObject addedMember, final JSONObject owner, final JSONObject userList) throws TwitterException, JSONException {
        listener.onUserListMemberAddition(forUser.get(), asUser(addedMember)
                , asUser(owner), asUserList(userList));
    }

    protected void onUserListMemberDeletion(final JSONObject deletedMember, final JSONObject owner, final JSONObject userList) throws TwitterException, JSONException {
        listener.onUserListMemberDeletion(forUser.get(), asUser(deletedMember)
                , asUser(owner), asUserList(userList));
    }

    protected void onUserListSubscription(final JSONObject source, final JSONObject owner, final JSONObject userList) throws TwitterException, JSONException {
        listener.onUserListSubscription(forUser.get(), asUser(source)
                , asUser(owner), asUserList(userList));
    }

    protected void onUserListUnsubscription(final JSONObject source, final JSONObject owner, final JSONObject userList) throws TwitterException, JSONException {
        listener.onUserListUnsubscription(forUser.get(), asUser(source)
                , asUser(owner), asUserList(userList));
    }

    protected void onUserListCreation(final JSONObject source, final JSONObject userList) throws TwitterException, JSONException {
        listener.onUserListCreation(forUser.get(), asUser(source)
                , asUserList(userList));
    }

    protected void onUserListUpdated(final JSONObject source, final JSONObject userList) throws TwitterException, JSONException {
        listener.onUserListUpdate(forUser.get(), asUser(source)
                , asUserList(userList));
    }

    protected void onUserListDestroyed(final JSONObject source, final JSONObject userList) throws TwitterException {
        listener.onUserListDeletion(forUser.get(), asUser(source)
                , asUserList(userList));
    }

    protected void onUserUpdate(final JSONObject source, final JSONObject target) throws TwitterException {
        listener.onUserProfileUpdate(forUser.get(), asUser(source));
    }

    protected void onBlock(final JSONObject source, final JSONObject target) throws TwitterException {
        listener.onBlock(forUser.get(), asUser(source), asUser(target));
    }

    protected void onUnblock(final JSONObject source, final JSONObject target) throws TwitterException {
        listener.onUnblock(forUser.get(), asUser(source), asUser(target));
    }

    public void onException(final Exception ex) {
        listener.onException(ex);
    }
}