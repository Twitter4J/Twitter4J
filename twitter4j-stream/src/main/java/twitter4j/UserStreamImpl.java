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
final class UserStreamImpl extends StatusStreamImpl implements UserStream {
    /*package*/ UserStreamImpl(Dispatcher dispatcher, InputStream stream, Configuration conf) throws IOException {
        super(dispatcher, stream, conf);
    }

    /*package*/ UserStreamImpl(Dispatcher dispatcher, HttpResponse response, Configuration conf) throws IOException {
        super(dispatcher, response, conf);
    }

    @Override
    public void next(UserStreamListener listener) throws TwitterException {
        handleNextElement(new StreamListener[]{listener}, EMPTY);
    }

    @Override
    protected void onSender(JSONObject json, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onDirectMessage(new DirectMessageJSONImpl(json));
        }
    }

    @Override
    protected void onDirectMessage(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        DirectMessage directMessage = asDirectMessage(json);
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onDirectMessage(directMessage);
        }
    }

    @Override
    protected void onScrubGeo(JSONObject json, StreamListener[] listeners) throws TwitterException {
        // Not implemented yet
        logger.info("Geo-tagging deletion notice (not implemented yet): " + line);
    }

    @Override
    protected void onFriends(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        long[] friendIds = asFriendList(json);
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onFriendList(friendIds);
        }
    }

    @Override
    protected void onFavorite(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onFavorite(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    protected void onUnfavorite(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUnfavorite(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    protected void onFollow(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onFollow(asUser(source), asUser(target));
        }
    }

    @Override
    protected void onUnfollow(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUnfollow(asUser(source), asUser(target));
        }
    }

    @Override
    protected void onUserListMemberAddition(JSONObject addedMember, JSONObject owner, JSONObject target, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListMemberAddition(asUser(addedMember), asUser(owner), asUserList(target));
        }
    }

    @Override
    protected void onUserListMemberDeletion(JSONObject deletedMember, JSONObject owner, JSONObject target, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListMemberDeletion(asUser(deletedMember), asUser(owner), asUserList(target));
        }
    }


    @Override
    protected void onUserListSubscription(JSONObject source, JSONObject owner, JSONObject target, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListSubscription(asUser(source), asUser(owner), asUserList(target));
        }
    }

    @Override
    protected void onUserListUnsubscription(JSONObject source, JSONObject owner, JSONObject target, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListUnsubscription(asUser(source), asUser(owner), asUserList(target));
        }
    }

    @Override
    protected void onUserListCreation(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListCreation(asUser(source), asUserList(target));
        }
    }

    @Override
    protected void onUserListUpdated(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListUpdate(asUser(source), asUserList(target));
        }
    }

    @Override
    protected void onUserListDestroyed(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserListDeletion(asUser(source), asUserList(target));
        }
    }

    @Override
    protected void onUserUpdate(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserProfileUpdate(asUser(source));
        }
    }

    @Override
    protected void onUserSuspension(long target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserSuspension(target);
        }
    }

    @Override
    protected void onUserDeletion(long target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUserDeletion(target);
        }
    }

    @Override
    protected void onBlock(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onBlock(asUser(source), asUser(target));
        }
    }

    @Override
    protected void onUnblock(JSONObject source, JSONObject target, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onUnblock(asUser(source), asUser(target));
        }
    }

    @Override
    void onRetweetedRetweet(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onRetweetedRetweet(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    void onFavoritedRetweet(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onFavoritedRetweet(asUser(source), asUser(target), asStatus(targetObject));
        }
    }

    @Override
    protected void onQuotedTweet(JSONObject source, JSONObject target, JSONObject targetObject, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((UserStreamListener) listener).onQuotedTweet(asUser(source), asUser(target), asStatus(targetObject));
        }
    }
}