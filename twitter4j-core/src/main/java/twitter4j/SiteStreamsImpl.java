/*
Copyright (c) 2007-2011, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.util.ParseUtil;

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
            listener.onDeletionNotice(forUser.get(), ParseUtil.getInt("id", directMessage)
                    , ParseUtil.getInt("user_id", directMessage));
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