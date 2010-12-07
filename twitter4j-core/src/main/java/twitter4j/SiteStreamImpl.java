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

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
class SiteStreamImpl implements StreamImplementation, UserStreamListener, StreamListener {
    private int forUser;
    private final Dispatcher dispatcher;

    class InnerUserStream extends UserStreamImpl {
        /*package*/ InnerUserStream(InputStream stream) throws IOException {
            super(stream);
        }

        /*package*/ InnerUserStream(HttpResponse response) throws IOException {
            super(response);
        }

        protected String parseLine(String line) {
            if ("".equals(line) || null == line) {
                return line;
            }
            int userIdEnd = line.indexOf(',', 12);
            // in the documentation for_user is not quoted, but actually it is quoted
            // we need to be able to handle either situation
            if (line.charAt(12) == '"') {
                forUser = Integer.parseInt(line.substring(13, userIdEnd - 1));
                return line.substring(userIdEnd + 11, line.length() - 1);
            } else {
                forUser = Integer.parseInt(line.substring(12, userIdEnd));
                return line.substring(userIdEnd + 11, line.length() - 1);
            }
        }

    }

    InnerUserStream innerUserStream;
    SiteStreamListener listener;
    StreamListener[] listeners = new StreamListener[1];

    /*package*/ SiteStreamImpl(Configuration conf, InputStream stream) throws IOException {
        innerUserStream = new InnerUserStream(stream);
        listeners[0] = this;
        dispatcher = new DispatcherFactory(conf).getInstance();
    }

    /*package*/ SiteStreamImpl(Configuration conf, HttpResponse response) throws IOException {
        innerUserStream = new InnerUserStream(response);
        listeners[0] = this;
        dispatcher = new DispatcherFactory(conf).getInstance();
    }

    public void next(StreamListener[] listeners) throws TwitterException {
        this.listener = (SiteStreamListener) listeners[0];
        innerUserStream.next(this.listeners);
    }

    public void close() throws IOException {
        innerUserStream.close();
    }

    abstract class SiteStreamEvent implements Runnable {
        final int FOR_USER;

        SiteStreamEvent(int forUser) {
            FOR_USER = forUser;
        }
    }

    public void onFriendList(final int[] friendIds) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onFriendList(FOR_USER, friendIds);
            }

        });
    }

    public void onFavorite(final User source, final User target, final Status targetObject) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onFavorite(FOR_USER, source, target, targetObject);
            }

        });
    }

    public void onUnfavorite(final User source, final User target, final Status targetObject) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onUnfavorite(FOR_USER, source, target, targetObject);
            }

        });
    }

    public void onFollow(final User source, final User target) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onFollow(FOR_USER, source, target);
            }

        });
    }

    public void onUnfollow(final User source, final User target) {
//        listener.onUnfollow(forUser, final source, final target);
    }

    public void onRetweet(final User source, final User target, final Status targetObject) {
//        listener.onRetweet(forUser, final source, final target, final targetObject);
    }

    public void onDirectMessage(final DirectMessage directMessage) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onDirectMessage(FOR_USER, directMessage);
            }

        });
    }

    public void onUserListSubscribed(final User subscriber, final User listOwner, final UserList list) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onUserListSubscribed(FOR_USER, subscriber, listOwner, list);
            }

        });
    }

    public void onUserListCreated(final User listOwner, final UserList list) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onUserListCreated(FOR_USER, listOwner, list);
            }

        });
    }

    public void onUserListUpdated(final User listOwner, final UserList list) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onUserListUpdated(FOR_USER, listOwner, list);
            }

        });
    }

    public void onUserListDestroyed(final User listOwner, final UserList list) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onUserListDestroyed(forUser, listOwner, list);
            }

        });
    }

    public void onBlock(final User source, final User target) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onBlock(forUser, source, target);
            }

        });
    }

    public void onUnblock(final User source, final User target) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onUnblock(forUser, source, target);
            }

        });
    }

    public void onStatus(final Status status) {
        dispatcher.invokeLater(new SiteStreamEvent(forUser) {
            public void run() {
                listener.onStatus(forUser, status);
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