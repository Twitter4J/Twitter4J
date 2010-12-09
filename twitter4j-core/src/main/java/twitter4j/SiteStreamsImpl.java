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

import twitter4j.conf.Configuration;
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.async.DispatcherFactory;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
class SiteStreamsImpl extends AbstractStreamImplementation implements StreamImplementation, StreamListener {
    private int forUser;
    private final Dispatcher dispatcher;

    SiteStreamsListener listener;

    /*package*/ SiteStreamsImpl(Configuration conf, InputStream stream) throws IOException {
        super(stream);
        dispatcher = new DispatcherFactory(conf).getInstance();
    }

    /*package*/ SiteStreamsImpl(Configuration conf, HttpResponse response) throws IOException {
        super(response);
        dispatcher = new DispatcherFactory(conf).getInstance();
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
            forUser = Integer.parseInt(line.substring(13, userIdEnd - 1));
            return line.substring(userIdEnd + 11, line.length() - 1);
        } else {
            forUser = Integer.parseInt(line.substring(12, userIdEnd));
            return line.substring(userIdEnd + 11, line.length() - 1);
        }
    }

    abstract class SiteStreamEvent implements Runnable {
        final int FOR_USER;

        SiteStreamEvent(int forUser) {
            FOR_USER = forUser;
        }
    }

    protected void onStatus(final JSONObject json) throws TwitterException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onStatus(FOR_USER, asStatus(json));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onDirectMessage(final JSONObject json) throws TwitterException, JSONException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onDirectMessage(FOR_USER, asDirectMessage(json));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onFriends(final JSONObject json) throws TwitterException, JSONException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onFriendList(FOR_USER, asFriendList(json));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onFavorite(final JSONObject source, final JSONObject target, final JSONObject targetObject) throws TwitterException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onFavorite(FOR_USER, asUser(source), asUser(target)
                            , asStatus(targetObject));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onUnfavorite(final JSONObject source, final JSONObject target, final JSONObject targetObject) throws TwitterException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onUnfavorite(FOR_USER, asUser(source)
                            , asUser(target), asStatus(targetObject));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onFollow(final JSONObject source, final JSONObject target) throws TwitterException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onFollow(FOR_USER, asUser(source), asUser(target));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onUserListSubscribed(final JSONObject source, final JSONObject owner, final JSONObject userList) throws TwitterException, JSONException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onUserListSubscribed(FOR_USER, asUser(source)
                            , asUser(owner), asUserList(userList));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onUserListCreated(final JSONObject source, final JSONObject userList) throws TwitterException, JSONException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onUserListCreated(FOR_USER, asUser(source)
                            , asUserList(userList));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onUserListUpdated(final JSONObject source, final JSONObject userList) throws TwitterException, JSONException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onUserListUpdated(FOR_USER, asUser(source)
                            , asUserList(userList));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onUserListDestroyed(final JSONObject source, final JSONObject userList) throws TwitterException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onUserListDestroyed(FOR_USER, asUser(source)
                            , asUserList(userList));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onBlock(final JSONObject source, final JSONObject target) throws TwitterException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onBlock(FOR_USER, asUser(source), asUser(target));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    protected void onUnblock(final JSONObject source, final JSONObject target) throws TwitterException {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                try {
                    listener.onUnblock(FOR_USER, asUser(source), asUser(target));
                } catch (TwitterException te) {
                    listener.onException(te);
                }
            }
        });
    }

    public void onException(final Exception ex) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onException(ex);
            }

        });
    }
}