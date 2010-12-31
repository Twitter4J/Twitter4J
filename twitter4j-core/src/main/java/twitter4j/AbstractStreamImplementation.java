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
abstract class AbstractStreamImplementation {
    protected static final Logger logger = Logger.getLogger(StatusStreamImpl.class);

    private boolean streamAlive = true;
    private BufferedReader br;
    private InputStream is;
    private HttpResponse response;
    protected final Dispatcher dispatcher;

    /*package*/

    AbstractStreamImplementation(Dispatcher dispatcher, InputStream stream) throws IOException {
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        this.dispatcher = dispatcher;
    }
    /*package*/

    AbstractStreamImplementation(Dispatcher dispatcher, HttpResponse response) throws IOException {
        this(dispatcher, response.asStream());
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

    abstract void next(StreamListener[] listeners) throws TwitterException;

    protected void handleNextElement() throws TwitterException {
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
                    line = parseLine(line);
                    if (line.length() > 0) {
                        logger.debug("received:", line);
                        try {
                            JSONObject json = new JSONObject(line);
                            JSONObjectType jsonObjectType = JSONObjectType.determine(json);
                            if (JSONObjectType.SENDER == jsonObjectType) {
                                onSender(json);
                            } else if (JSONObjectType.STATUS == jsonObjectType) {
                                onStatus(json);
                            } else if (JSONObjectType.DIRECT_MESSAGE == jsonObjectType) {
                                onDirectMessage(json);
                            } else if (JSONObjectType.DELETE == jsonObjectType) {
                                onDelete(json);
                            } else if (JSONObjectType.LIMIT == jsonObjectType) {
                                onLimit(json);
                            } else if (JSONObjectType.SCRUB_GEO == jsonObjectType) {
                                onScrubGeo(json);
                            } else if (JSONObjectType.FRIENDS == jsonObjectType) {
                                onFriends(json);
                            } else if (JSONObjectType.FAVORITE == jsonObjectType) {
                                onFavorite(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
                            } else if (JSONObjectType.UNFAVORITE == jsonObjectType) {
                                onUnfavorite(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
                            } else if (JSONObjectType.RETWEET == jsonObjectType) {
                                // note: retweet events also show up as statuses
                                onRetweet(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
                            } else if (JSONObjectType.FOLLOW == jsonObjectType) {
                                onFollow(json.getJSONObject("source"), json.getJSONObject("target"));
                            } else if (JSONObjectType.USER_LIST_SUBSCRIBED == jsonObjectType) {
                                onUserListSubscribed(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
                            } else if (JSONObjectType.USER_LIST_CREATED == jsonObjectType) {
                                onUserListCreated(json.getJSONObject("source"), json.getJSONObject("target"));
                            } else if (JSONObjectType.USER_LIST_UPDATED == jsonObjectType) {
                                onUserListUpdated(json.getJSONObject("source"), json.getJSONObject("target"));
                            } else if (JSONObjectType.USER_LIST_DESTROYED == jsonObjectType) {
                                onUserListDestroyed(json.getJSONObject("source"), json.getJSONObject("target"));
                            } else if (JSONObjectType.USER_UPDATE == jsonObjectType) {
                                onUserUpdate(json.getJSONObject("source"), json.getJSONObject("target"));
                            } else if (JSONObjectType.BLOCK == jsonObjectType) {
                                onBlock(json.getJSONObject("source"), json.getJSONObject("target"));
                            } else if (JSONObjectType.UNBLOCK == jsonObjectType) {
                                onUnblock(json.getJSONObject("source"), json.getJSONObject("target"));
                            } else {
                                // tmp: just checking what kind of unknown event we're receiving on this stream
                                logger.info("Received unknown event: " + line);
                            }
                        } catch (Exception ex) {
                            onException(ex);
                        }
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

    protected void onSender(JSONObject json) throws TwitterException {
    }

    protected void onStatus(JSONObject json) throws TwitterException {
    }

    protected void onDirectMessage(JSONObject json) throws TwitterException, JSONException {
    }

    protected void onDelete(JSONObject json) throws TwitterException, JSONException {
    }

    protected void onLimit(JSONObject json) throws TwitterException, JSONException {
    }

    protected void onScrubGeo(JSONObject json) throws TwitterException, JSONException {
    }

    protected void onFriends(JSONObject json) throws TwitterException, JSONException {
    }

    protected void onFavorite(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
    }

    protected void onUnfavorite(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
    }

    protected void onRetweet(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
    }

    protected void onFollow(JSONObject source, JSONObject target) throws TwitterException {
    }

    protected void onUserListSubscribed(JSONObject source, JSONObject owner, JSONObject userList) throws TwitterException, JSONException {
    }

    protected void onUserListCreated(JSONObject source, JSONObject userList) throws TwitterException, JSONException {
    }

    protected void onUserListUpdated(JSONObject source, JSONObject userList) throws TwitterException, JSONException {
    }

    protected void onUserListDestroyed(JSONObject source, JSONObject userList) throws TwitterException {
    }

    protected void onUserUpdate(JSONObject source, JSONObject target) throws TwitterException {
    }

    protected void onBlock(JSONObject source, JSONObject target) throws TwitterException {
    }

    protected void onUnblock(JSONObject source, JSONObject target) throws TwitterException {
    }

    protected void onException(Exception e) {
    }

    public void close() throws IOException {
        streamAlive = false;
        is.close();
        br.close();
        if (null != response) {
            response.disconnect();
        }
    }

    protected Status asStatus(JSONObject json) throws TwitterException {
        Status status = new StatusJSONImpl(json);
        DataObjectFactoryUtil.registerJSONObject(status, json);
        return status;
    }

    protected DirectMessage asDirectMessage(JSONObject json) throws TwitterException {
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

    protected User asUser(JSONObject json) throws TwitterException {
        User user = new UserJSONImpl(json);
        DataObjectFactoryUtil.registerJSONObject(user, json);
        return user;
    }

    protected UserList asUserList(JSONObject json) throws TwitterException {
        UserList userList = new UserListJSONImpl(json);
        DataObjectFactoryUtil.registerJSONObject(userList, json);
        return userList;
    }

}
