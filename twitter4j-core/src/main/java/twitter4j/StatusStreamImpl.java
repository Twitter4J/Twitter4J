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
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.util.ParseUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * StatusStream implementation. This class is NOT intended to be extended but left non-final for the ease of mock testing.
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
class StatusStreamImpl implements StatusStream, UserStream {
    private static final Logger logger = Logger.getLogger(StatusStreamImpl.class);

    private boolean streamAlive = true;
    private BufferedReader br;
    private InputStream is;
    private HttpResponse response;

    /*package*/

    StatusStreamImpl(InputStream stream) throws IOException {
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    }
    /*package*/

    StatusStreamImpl(HttpResponse response) throws IOException {
        this(response.asStream());
        this.response = response;
    }

    /**
     * {@inheritDoc}
     */
    public void next(UserStreamListener listener) throws TwitterException {
        ArrayList<StatusListener> list = new ArrayList<StatusListener>(1);
        list.add(listener);
        handleNextElement(list);
    }

    /**
     * {@inheritDoc}
     */
    public void next(StatusListener listener) throws TwitterException {
        ArrayList<StatusListener> list = new ArrayList<StatusListener>(1);
        list.add(listener);
        handleNextElement(list);
    }

    void next(List<StatusListener> listeners) throws TwitterException {
        handleNextElement(listeners);
    }

    private void handleNextElement(List<StatusListener> listeners) throws TwitterException {
        if (!streamAlive) {
            throw new IllegalStateException("Stream already closed.");
        }
        try {
            String line;
            line = br.readLine();
            if (null == line) {
                //invalidate this status stream
                throw new IOException("the end of the stream has been reached");
            }
            if (line.length() > 0) {
                logger.debug("received:", line);
                try {
                    JSONObject json = new JSONObject(line);
                    if (!json.isNull ("sender")) {
                        for (StatusListener listener : listeners) {
                            if (listener instanceof UserStreamListener) {
                                ((UserStreamListener) listener).onDirectMessage(new DirectMessageJSONImpl(json));
                            }
                        }
                    } else if (!json.isNull("text")) {
                        for (StatusListener listener : listeners) {
                            listener.onStatus(new StatusJSONImpl(json));
                        }
                    } else if (!json.isNull("direct_message")) {
                        for (StatusListener listener : listeners) {
                            if (listener instanceof UserStreamListener) {
                                ((UserStreamListener) listener).onDirectMessage(new DirectMessageJSONImpl(json.getJSONObject("direct_message")));
                            }
                        }
                    } else if (!json.isNull("delete")) {
                        for (StatusListener listener : listeners) {
                            listener.onDeletionNotice(new StatusDeletionNoticeImpl(json));
                        }
                    }
                    else if (!json.isNull("limit")) {
                        for (StatusListener listener : listeners) {
                            listener.onTrackLimitationNotice(ParseUtil.getInt("track", json.getJSONObject("limit")));
                        }
                    } else if (!json.isNull ("scrub_geo")) {
                            // Not implemented yet
                            System.out.println ("Geo-tagging deletion notice (not implemented yet): " + line);
                    } else if (!json.isNull("friends")) {
                        JSONArray friends = json.getJSONArray("friends");
                        int[] friendIds = new int[friends.length()];
                        for (int i = 0; i < friendIds.length; ++i) {
                            friendIds[i] = friends.getInt(i);
                        }
                        for (StatusListener listener : listeners) {
                            if (listener instanceof UserStreamListener) {
                                ((UserStreamListener) listener).onFriendList(friendIds);
                            }
                        }
                    } else if (!json.isNull("event")) {
                        String event = json.getString("event");
                        User source = new UserJSONImpl(json.getJSONObject("source"));
                        User target = new UserJSONImpl(json.getJSONObject("target"));

                        if ("favorite".equals(event)) {
                            Status targetObject = new StatusJSONImpl(json.getJSONObject("target_object"));
                            for (StatusListener listener : listeners) {
                                if (listener instanceof UserStreamListener) {
                                    ((UserStreamListener) listener).onFavorite(source, target, targetObject);
                                }
                            }
                        } else if ("unfavorite".equals(event)) {
                            Status targetObject = new StatusJSONImpl(json.getJSONObject("target_object"));
                            for (StatusListener listener : listeners) {
                                if (listener instanceof UserStreamListener) {
                                    ((UserStreamListener) listener).onUnfavorite(source, target, targetObject);
                                }
                            }
                        } else if ("retweet".equals(event)) {
                            // note: retweet events also show up as statuses
                            Status targetObject = new StatusJSONImpl(json.getJSONObject("target_object"));
                            for (StatusListener listener : listeners) {
                                if (listener instanceof UserStreamListener) {
                                    ((UserStreamListener) listener).onRetweet(source, target, targetObject);
                                }
                            }
                        } else if ("follow".equals(event)) {
                            for (StatusListener listener : listeners) {
                                if (listener instanceof UserStreamListener) {
                                    ((UserStreamListener) listener).onFollow(source, target);
                                }
                            }
                        } else if ("unfollow".equals(event)) {
                            for (StatusListener listener : listeners) {
                                if (listener instanceof UserStreamListener) {
                                    ((UserStreamListener) listener).onUnfollow(source, target);
                                }
                            }
                        } else if (event.startsWith("list_")) {
                            UserList targetObject = new UserListJSONImpl (json.getJSONObject ("target_object"));

                             if ("list_user_subscribed".equals (event)) {
                                 for (StatusListener listener : listeners) {
                                     if (listener instanceof UserStreamListener) {
                                         ((UserStreamListener) listener).onUserListSubscribed(source, target, targetObject);
                                     }
                                 }
                             } else if ("list_created".equals (event)) {
                                 for (StatusListener listener : listeners) {
                                     if (listener instanceof UserStreamListener) {
                                         ((UserStreamListener) listener).onUserListCreated(source, targetObject);
                                     }
                                 }
                             } else if ("list_updated".equals (event)) {
                                 for (StatusListener listener : listeners) {
                                     if (listener instanceof UserStreamListener) {
                                         ((UserStreamListener) listener).onUserListUpdated(source, targetObject);
                                     }
                                 }
                             } else if ("list_destroyed".equals (event)) {
                                 for (StatusListener listener : listeners) {
                                     if (listener instanceof UserStreamListener) {
                                         ((UserStreamListener) listener).onUserListDestroyed(source, targetObject);
                                     }
                                 }
                             }
                         } else if ("block".equals(event)) {
                            for (StatusListener listener : listeners) {
                                if (listener instanceof UserStreamListener) {
                                    ((UserStreamListener) listener).onBlock(source, target);
                                }
                            }
                        } else if ("unblock".equals (event)) {
                            for (StatusListener listener : listeners) {
                                if (listener instanceof UserStreamListener) {
                                    ((UserStreamListener) listener).onUnblock(source, target);
                                }
                            }
                        } else {
                             logger.info("Received unknown event type '" + event + "': " + line);
                         }
                     } else {
                        // tmp: just checking what kind of unknown event we're receiving on this stream
                        logger.info("Received unknown event: " + line);
                    }
                } catch (JSONException jsone) {
                    for (StatusListener listener : listeners) {
                        listener.onException(jsone);
                    }
                }
            }
        } catch (IOException ioe) {
            try {
                is.close();
            } catch (IOException ignore) {
            }
            streamAlive = false;
            throw new TwitterException("Stream closed.", ioe);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws IOException {
        is.close();
        br.close();
        if (null != response) {
            response.disconnect();
        }
    }
}
