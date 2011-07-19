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

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
class UserStreamImpl extends StatusStreamImpl implements UserStream {
    /*package*/ UserStreamImpl(Dispatcher dispatcher, InputStream stream, Configuration conf) throws IOException {
        super(dispatcher, stream, conf);
    }

    /*package*/ UserStreamImpl(Dispatcher dispatcher, HttpResponse response, Configuration conf) throws IOException {
        super(dispatcher, response, conf);
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

    public void next(StreamListener[] listeners) throws TwitterException {
        this.listeners = listeners;
        handleNextElement();
    }

    protected String parseLine(String line) {
        this.line = line;
        return line;
    }

    @Override
    protected void onSender(JSONObject json) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onDirectMessage(factory.createDirectMessage(json));
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
    protected void onFriends(JSONObject json) throws TwitterException, JSONException {
        long[] friendIds = asFriendList(json);
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
    protected void onUserListMemberAddition(JSONObject addedMember, JSONObject owner, JSONObject target) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListMemberAddition(asUser(addedMember), asUser(owner), asUserList(target));
        }
    }

    @Override
    protected void onUserListMemberDeletion(JSONObject deletedMember, JSONObject owner, JSONObject target) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListMemberDeletion(asUser(deletedMember), asUser(owner), asUserList(target));
        }
    }


    @Override
    protected void onUserListSubscription(JSONObject source, JSONObject owner, JSONObject target) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListSubscription(asUser(source), asUser(owner), asUserList(target));
        }
    }

    @Override
    protected void onUserListUnsubscription(JSONObject source, JSONObject owner, JSONObject target) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListUnsubscription(asUser(source), asUser(owner), asUserList(target));
        }
    }

    @Override
    protected void onUserListCreation(JSONObject source, JSONObject target) throws TwitterException, JSONException {
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

