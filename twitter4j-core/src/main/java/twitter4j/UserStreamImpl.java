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

import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
class UserStreamImpl extends StatusStreamImpl implements UserStream{
    /*package*/ UserStreamImpl(Dispatcher dispatcher, InputStream stream) throws IOException {
        super(dispatcher, stream);
    }
    /*package*/ UserStreamImpl(Dispatcher dispatcher, HttpResponse response) throws IOException {
        super(dispatcher, response);
    }

    /**
     * {@inheritDoc}
     */
    public void next(UserStreamListener listener) throws TwitterException {
        StreamListener[] list = new StreamListener[1];
        list[0] = listener;
        this.listeners = list;
        handleNextElement();
    }
    public void next(StreamListener[] listeners) throws TwitterException{
        this.listeners = listeners;
        handleNextElement();
    }

    protected String parseLine(String line){
        DataObjectFactoryUtil.clearThreadLocalMap();
        this.line = line;
        return line;
    }

    @Override
    protected void onSender(JSONObject json) throws TwitterException{
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onDirectMessage(new DirectMessageJSONImpl(json));
        }
    }

    @Override
    protected void onDirectMessage(JSONObject json) throws TwitterException, JSONException {
        DirectMessage directMessage = asDirectMessage(json);
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onDirectMessage(directMessage);
        }
    }

    @Override
    protected void onScrubGeo(JSONObject json) throws TwitterException {
        // Not implemented yet
        logger.info("Geo-tagging deletion notice (not implemented yet): " + line);
    }

    @Override
    protected void onFriends(JSONObject json) throws TwitterException ,JSONException{
        int[] friendIds = asFriendList(json);
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onFriendList(friendIds);
        }
    }

    @Override
    protected void onFavorite(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onFavorite(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    protected void onUnfavorite(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUnfavorite(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    protected void onRetweet(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onRetweet(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    protected void onFollow(JSONObject source, JSONObject target) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onFollow(asUser(source), asUser(target));
        }
    }

    @Override
    protected void onUserListSubscribed(JSONObject source, JSONObject owner, JSONObject target) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListSubscription(asUser(source), asUser(owner), asUserList(target));
        }
    }

    @Override
    protected void onUserListCreated(JSONObject source, JSONObject target) throws TwitterException , JSONException{
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListCreation(asUser(source), asUserList(target));
        }
    }

    @Override
    protected void onUserListUpdated(JSONObject source, JSONObject target) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListUpdate(asUser(source), asUserList(target));
        }
    }

    @Override
    protected void onUserListDestroyed(JSONObject source, JSONObject target) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListDeletion(asUser(source), asUserList(target));
        }
    }

    @Override
    protected void onUserUpdate(JSONObject source, JSONObject target) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserProfileUpdate(asUser(source));
        }
    }

    @Override
    protected void onBlock(JSONObject source, JSONObject target) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onBlock(asUser(source), asUser(target));
        }
    }

    @Override
    protected void onUnblock(JSONObject source, JSONObject target) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUnblock(asUser(source), asUser(target));
        }
    }

    @Override
    public void onException(Exception e) {
        for (StreamListener listener : listeners) {
            listener.onException(e);
        }
    }
}

