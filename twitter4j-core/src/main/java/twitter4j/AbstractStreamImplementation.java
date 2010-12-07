/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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

import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
abstract class AbstractStreamImplementation {
    protected static final Logger logger = Logger.getLogger(StatusStreamImpl.class);

    private boolean streamAlive = true;
    private BufferedReader br;
    private InputStream is;
    private HttpResponse response;
    /*package*/

    AbstractStreamImplementation(InputStream stream) throws IOException {
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    }
    /*package*/

    AbstractStreamImplementation(HttpResponse response) throws IOException {
        this(response.asStream());
        this.response = response;
    }
    protected String parseLine(String line){
        return line;
    }
    abstract void next(StreamListener[] listeners) throws TwitterException;

    protected void handleNextElement() throws TwitterException {
        if (!streamAlive) {
            throw new IllegalStateException("Stream already closed.");
        }
        try {
            String line;
            line = parseLine(br.readLine());
            if (null == line) {
                //invalidate this status stream
                throw new IOException("the end of the stream has been reached");
            }
            if (line.length() > 0) {
                logger.debug("received:", line);
                try {
                    JSONObject json = new JSONObject(line);
                    if (!json.isNull("sender")) {
                        onSender(json);
                    } else if (!json.isNull("text")) {
                        onText(json);
                    } else if (!json.isNull("direct_message")) {
                        onDirectMessage(json);
                    } else if (!json.isNull("delete")) {
                        onDelete(json);
                    } else if (!json.isNull("limit")) {
                        onLimit(json);
                    } else if (!json.isNull("scrub_geo")) {
                        onScrubGeo(json);
                        // Not implemented yet
                        System.out.println("Geo-tagging deletion notice (not implemented yet): " + line);
                    } else if (!json.isNull("friends")) {
                        onFriends(json);
                    } else if (!json.isNull("event")) {
                        String event = json.getString("event");
                        JSONObject sourceJSON = json.getJSONObject("source");
                        JSONObject targetJSON = json.getJSONObject("target");
                        if ("favorite".equals(event)) {
                            onFavorite(sourceJSON, targetJSON, json.getJSONObject("target_object"));
                        } else if ("unfavorite".equals(event)) {
                            onUnfavorite(sourceJSON, targetJSON, json.getJSONObject("target_object"));
                        } else if ("retweet".equals(event)) {
                            // note: retweet events also show up as statuses
                            onRetweet(sourceJSON, targetJSON, json.getJSONObject("target_object"));
                        } else if ("follow".equals(event)) {
                            onFollow(sourceJSON, targetJSON);
                        } else if ("unfollow".equals(event)) {
                            onUnfollow(sourceJSON, targetJSON);
                        } else if (event.startsWith("list_")) {
                            if ("list_user_subscribed".equals(event)) {
                                JSONObject targetObjectJSON = json.getJSONObject("target_object");
                                onUserListSubscribed(sourceJSON, targetJSON, targetObjectJSON);
                            } else if ("list_created".equals(event)) {
                                onUserListCreated(sourceJSON, targetJSON);
                            } else if ("list_updated".equals(event)) {
                                onUserListUpdated(sourceJSON, targetJSON);
                            } else if ("list_destroyed".equals(event)) {
                                onUserListDestroyed(sourceJSON, targetJSON);
                            }
                        } else if ("block".equals(event)) {
                            onBlock(sourceJSON, targetJSON);
                        } else if ("unblock".equals(event)) {
                            onUnblock(sourceJSON, targetJSON);
                        } else {
                            logger.info("Received unknown event type '" + event + "': " + line);
                        }
                    } else {
                        // tmp: just checking what kind of unknown event we're receiving on this stream
                        logger.info("Received unknown event: " + line);
                    }
                } catch (JSONException jsone) {
                    onException(jsone);
                }
            }
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

    protected void onSender(JSONObject json) throws TwitterException {}
    protected void onText(JSONObject json) throws TwitterException {}
    protected void onDirectMessage(JSONObject json) throws TwitterException , JSONException{}
    protected void onDelete(JSONObject json) throws TwitterException , JSONException{}
    protected void onLimit(JSONObject json) throws TwitterException , JSONException{}
    protected void onScrubGeo(JSONObject json) throws TwitterException {}
    protected void onFriends(JSONObject json) throws TwitterException  ,JSONException{}
    protected void onFavorite(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {}
    protected void onUnfavorite(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {}
    protected void onRetweet(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {}
    protected void onFollow(JSONObject source, JSONObject target) throws TwitterException {}
    protected void onUnfollow(JSONObject source, JSONObject target) throws TwitterException {}
    protected void onUserListSubscribed(JSONObject source, JSONObject owner, JSONObject userList) throws TwitterException , JSONException{}
    protected void onUserListCreated(JSONObject source, JSONObject userList) throws TwitterException , JSONException{}
    protected void onUserListUpdated(JSONObject source, JSONObject userList) throws TwitterException, JSONException {}
    protected void onUserListDestroyed(JSONObject source, JSONObject userList) throws TwitterException {}
    protected void onBlock(JSONObject source, JSONObject target) throws TwitterException {}
    protected void onUnblock(JSONObject source, JSONObject target) throws TwitterException {}

    protected void onException(Exception e){}

    /**
     * {@inheritDoc}
     */
    public void close() throws IOException {
        streamAlive = false;
        is.close();
        br.close();
        if (null != response) {
            response.disconnect();
        }
    }
    protected Status asStatus(JSONObject json) throws TwitterException{
        Status status = new StatusJSONImpl(json);
        DataObjectFactoryUtil.registerJSONObject(status, json);
        return status;
    }
    protected DirectMessage asDirectMessage(JSONObject json) throws TwitterException{
        DirectMessage directMessage = null;
        try {
            directMessage = new DirectMessageJSONImpl(json.getJSONObject("direct_message"));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
        DataObjectFactoryUtil.registerJSONObject(directMessage, json);
        return directMessage;
    }
    protected int[] asFriendList(JSONObject json) throws TwitterException {
        JSONArray friends = null;
        try {
            friends = json.getJSONArray("friends");
            int[] friendIds = new int[friends.length()];
            for (int i = 0; i < friendIds.length; ++i) {
                friendIds[i] = friends.getInt(i);
            }
            return friendIds;
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }
    protected User asUser(JSONObject json) throws TwitterException{
        User user = new UserJSONImpl(json);
        DataObjectFactoryUtil.registerJSONObject(user, json);
        return user;
    }
    protected UserList asUserList(JSONObject json) throws TwitterException{
        UserList userList = new UserListJSONImpl(json);
        DataObjectFactoryUtil.registerJSONObject(userList, json);
        return userList;
    }
}
