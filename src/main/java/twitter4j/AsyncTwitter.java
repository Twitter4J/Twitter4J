/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import java.util.Date;

/**
 * Twitter API with a series of asynchronous APIs.<br>
 * With this class, you can call TwitterAPI acynchronously.<br>
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterListener
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AsyncTwitter extends Twitter {
    private static final long serialVersionUID = -2008667933225051907L;

    public AsyncTwitter(String id, String password) {
        super(id, password);
    }

    public AsyncTwitter(String id, String password, String baseURL) {
        super(id, password, baseURL);
    }

    /**
     * Returns the 20 most recent statuses from non-protected users who have set a custom user icon.
     * @param listener TwitterListener a listener object that receives the response
     */

    public void getPublicTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline());
            }
        });
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     * @param sinceID String
     * @param listener TwitterListener a listener object that receives the response
     */
    public void getPublicTimelineAsync(int sinceID, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, new Integer[] {sinceID}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline( (Integer) args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
     *  It's also possible to request another user's friends_timeline via the id parameter below.
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     */

    public synchronized void getFriendsTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline());
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
     *  It's also possible to request another user's friends_timeline via the id parameter below.
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @since Twitter4J 2.0.1
     */

    public synchronized void getFriendsTimelineAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline((Paging)args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use getFriendsTimelineAsync(Paging paging, TwitterListener listener) instead
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     */
    public synchronized void getFriendsTimelineByPageAsync(int page, TwitterListener listener) {
        getFriendsTimelineAsync(new Paging(page),listener);
    }
    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.8
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineAsync(int page, TwitterListener listener) {
        getFriendsTimelineAsync(new Paging(page),listener);
    }


    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.8
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineAsync(long sinceId, int page, TwitterListener listener) {
        getFriendsTimelineAsync(new Paging(page,sinceId),listener);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     */
    public synchronized void getFriendsTimelineAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline( (String) args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @since Twitter4J 2.0.1
     */
    public synchronized void getFriendsTimelineAsync(String id, Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new Object[]{id, paging}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline((String) args[0], (Paging) args[1]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use getFriendsTimelineAsync(String id, int page, TwitterListener listener instead
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineByPageAsync(String id, int page, TwitterListener listener) {
        getFriendsTimelineAsync(id, new Paging(page), listener);
    }


    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.8
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineAsync(String id, int page, TwitterListener listener) {
        getFriendsTimelineAsync(id, new Paging(page), listener);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param id   specifies the ID or screen name of the user for whom to return the friends_timeline
     * @param page the number of page
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineAsync(long sinceId, String id, int page, TwitterListener listener) {
        getFriendsTimelineAsync(id, new Paging(page, sinceId), listener);
    }


    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param since Date
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineAsync(Date since, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new Object[] {since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline( (Date) args[0]));
            }
        });
    }


    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineAsync(long sinceId, TwitterListener listener) {
        getFriendsTimelineAsync(new Paging(sinceId), listener);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param since Date
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineAsync(String id, Date since, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new Object[] {id, since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline( (String) args[0], (Date) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/friendstimeline">Twitter API Wiki / REST API Documentation - statuses/friends_timeline</a>
     * @deprecated use getFriendsTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsTimelineAsync(String id, long sinceId, TwitterListener listener) {
        getFriendsTimelineAsync(id, new Paging(sinceId), listener);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param count int
     * @param since Date
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @deprecated using long sinceId is suggested.
     */
    public synchronized void getUserTimelineAsync(String id, int count, Date since, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {id, count, since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (String) args[0], (Integer) args[1], (Date) args[2]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id Specifies the ID or screen name of the user for whom to return the user_timeline.
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @since Twitter4J 2.0.1
     */
    public synchronized void getUserTimelineAsync(String id, Paging paging,
                                                  TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener,
                new Object[]{id, paging}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotUserTimeline(getUserTimeline((String) args[0],
                        (Paging) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id       String
     * @param page    int
     * @param sinceId  Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @since Twitter4J 2.0.0
     * @deprecated use getUserTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getUserTimelineAsync(String id, int page,
                                                  long sinceId,
                                                  TwitterListener listener) {
        getUserTimelineAsync(id, new Paging(page, sinceId), listener);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param since Date
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @deprecated using long sinceId is suggested.
     */
    public synchronized void getUserTimelineAsync(String id, Date since, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {id, since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (String) args[0], (Date) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param count int
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @deprecated use getUserTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getUserTimelineAsync(String id, int count, TwitterListener listener) {
        getUserTimelineAsync(id, new Paging().count(count), listener);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * @param count int
     * @param since Date
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @deprecated using long sinceId is suggested.
     */
    public synchronized void getUserTimelineAsync(int count, Date since, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {count, since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (Integer) args[0], (Date) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param paging   controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @since Twitter4J 2.0.1
     */
    public synchronized void getUserTimelineAsync(Paging paging,
                                                  TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener,
                new Object[]{paging}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.gotUserTimeline(getUserTimeline((Paging) args[0]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @param count    int
     * @param sinceId  Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @since Twitter4J 2.0.0
     * @deprecated use getUserTimelineAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getUserTimelineAsync(int count, long sinceId,
                                                  TwitterListener listener) {
        getUserTimelineAsync(new Paging(sinceId).count(count), listener);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     */
    public synchronized void getUserTimelineAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (String) args[0]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @since Twitter4J 2.0.0
     * @deprecated use getUserTimelineAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getUserTimelineAsync(String id, long sinceId,
                                                  TwitterListener listener) {
        getUserTimelineAsync(id, new Paging(sinceId),listener);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     */
    public synchronized void getUserTimelineAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline());
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/usertimeline">Twitter API Wiki / REST API Documentation - statuses/user_timeline</a>
     * @since Twitter4J 2.0.0
     * @deprecated use getUserTimelineAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getUserTimelineAsync(long sinceId, TwitterListener listener) {
        getUserTimelineAsync(new Paging(sinceId), listener);
    }

    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     * @param id int
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated Use showAsync(long id) instead.
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/show">Twitter API Wiki / REST API Documentation - statuses/show</a>
     */
    public synchronized void showAsync(int id, TwitterListener listener) {
        showAsync((long) id, listener);
    }

    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     * @param id int
     * @param listener TwitterListener a listener object that receives the response
     * @since 1.1.1
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#statuses/show">Twitter API Wiki / REST API Documentation - statuses/show</a>
     */
    public synchronized void showAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SHOW, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotShow(show( (Long) args[0]));
            }
        });
    }

    /**
     *
     * Updates the user's status asynchronously
     *
     * @param status String
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#update">Twitter API &gt; Status Methods &gt; update</a>

     */
    public void updateAsync(String status, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE, listener, new String[] {status}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updated(update( (String) args[0]));
            }
        });
    }
    /**
     *
     * Updates the user's status asynchronously
     *
     * @param status String
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#update">Twitter API &gt; Status Methods &gt; update</a>
     */
    public void updateAsync(String status) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE,  new TwitterAdapter(), new String[] {status}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updated(update( (String) args[0]));
            }
        });
    }

    /**
     *
     * Updates the user's status asynchronously
     *
     * @param status String
     * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
     * @param listener TwitterListener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#update">Twitter API &gt; Status Methods &gt; update</a>
     * @since Twitter4J 1.1.6
     */
    public void updateAsync(String status, long inReplyToStatusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE, listener, new Object[]{status, inReplyToStatusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updated(update((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     *
     * Updates the user's status asynchronously
     *
     * @param status String
     * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#update">Twitter API &gt; Status Methods &gt; update</a>
     * @since Twitter4J 1.1.6
     */
    public void updateAsync(String status, long inReplyToStatusId) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE, new TwitterAdapter(), new Object[]{status, inReplyToStatusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updated(update((String) args[0], (Long) args[1]));
            }
        });
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getRepliesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(REPLIES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotReplies(getReplies());
            }
        });
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void getMentionsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotMentions(getMentions());
            }
        });
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void getMentionsAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(MENTIONS, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotMentions(getMentions((Paging)args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.8
     * @deprecated use getMentionsAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getRepliesAsync(long sinceId, TwitterListener listener) {
        getMentionsAsync(new Paging(sinceId), listener);
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use getRepliesAsync(int page, TwitterListener listener) instead
     * @deprecated use getMentionsAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getRepliesByPageAsync(int page, TwitterListener listener) {
        getMentionsAsync(new Paging(page), listener);
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.8
     * @deprecated use getMentionsAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getRepliesAsync(int page,TwitterListener listener) {
        getMentionsAsync(new Paging(page), listener);
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.8
     * @deprecated use getMentionsAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getRepliesAsync(long sinceId, int page, TwitterListener listener) {
        getMentionsAsync(new Paging(page, sinceId), listener);
    }

    /**
     *
     * Destroys the status specified by the required ID parameter. asynchronously
     * @param statusId String
     * @since 1.0.5
     * @deprecated use destroyStatusAsync(long statuId) instead.
     */
    public void destoryStatusAsync(int statusId) {
        destroyStatusAsync((long) statusId);
    }

    /**
     *
     * Destroys the status specified by the required ID parameter. asynchronously
     * @param statusId String
     * @since 1.1.2
     * @deprecated use destroyStatusAsync(long statuId) instead.
     */
    public void destroyStatusAsync(int statusId) {
        destroyStatusAsync((long) statusId);
    }

    /**
     * Destroys the status specified by the required ID parameter. asynchronously
     *
     * @param statusId String
     * @since 1.1.2
     */
    public void destroyStatusAsync(long statusId) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_STATUS, new TwitterAdapter(), new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedStatus(destroyStatus(((Long) args[0])));
            }
        });
    }
    /**
     *
     * Destroys the status specified by the required ID parameter. asynchronously
     * @param statusId String
     * @param listener TwitterListener a listener object that receives the response
     * @since 1.0.6
     * @deprecated use destroyStatusAsync(long statuId) instead.
     */
    public void destoryStatusAsync(int statusId, TwitterListener listener) {
        destroyStatusAsync((long) statusId, listener);
    }
    /**
     *
     * Destroys the status specified by the required ID parameter. asynchronously
     * @param statusId String
     * @param listener TwitterListener a listener object that receives the response
     * @since 1.1.2
     * @deprecated use destroyStatusAsync(long statuId) instead.
     */
    public void destroyStatusAsync(int statusId, TwitterListener listener) {
        destroyStatusAsync((long) statusId, listener);
    }

    /**
     * Destroys the status specified by the required ID parameter. asynchronously
     *
     * @param statusId String
     * @param listener TwitterListener a listener object that receives the response
     * @since 1.1.2
     */
    public void destroyStatusAsync(long statusId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_STATUS, listener, new Long[]{statusId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedStatus(destroyStatus(((Long) args[0])));
            }
        });
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getFriendsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends());
            }
        });
    }


    /**
     * Returns the specified user's friends, each with current status inline.
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void getFriendsAsync(Paging paging,TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends((Paging)args[0]));
            }
        });
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * @param page number of the page to retrieve friends
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use getFriendsAsync(Paging paging,TwitterListener listener) instead
     */
    public synchronized void getFriendsAsync(int page,TwitterListener listener) {
        getFriendsTimelineAsync(new Paging(page), listener);
    }

    /**
     * Returns the user's friends, each with current status inline.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getFriendsAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends( (String) args[0]));
            }
        });
    }

    /**
     * Returns the user's friends, each with current status inline.
     * @param id String
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getFriendsAsync(String id, Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS, listener, new Object[] {id,paging}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriends(getFriends((String) args[0], (Paging) args[1]));
            }
        });
    }


    /**
     * Returns the user's friends, each with current status inline.
     * @param id String
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use getFriendsAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFriendsAsync(String id,int page, TwitterListener listener) {
        getFriendsAsync(id, new Paging(page), listener);
    }


    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getFollowersAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFollowers(getFollowers());
            }
        });
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void getFollowersAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS, listener, new Object[]{paging}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowers(getFollowers((Paging) args[0]));
            }
        });
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * @param page Retrieves the next 100 followers.
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.0
     * @deprecated use getFollowersAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFollowersAsync(int page, TwitterListener listener) {
        getFollowersAsync(new Paging(page), listener);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @param id       The ID or screen name of the user for whom to request a list of followers.
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.0
     */
    public synchronized void getFollowersAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowers(getFollowers((String) args[0]));
            }
        });
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * @param id The ID or screen name of the user for whom to request a list of followers.
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void getFollowersAsync(String id, Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS, listener, new Object[]{id, paging}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowers(getFollowers((String) args[0], (Paging) args[1]));
            }
        });
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * @param id The ID or screen name of the user for whom to request a list of followers.
     * @param page Retrieves the next 100 followers.
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 1.1.0
     * @deprecated use getFollowersAsync(String id, Paging paging, TwitterListener listener) instead
     */
    public synchronized void getFollowersAsync(String id, int page, TwitterListener listener) {
        getFollowersAsync(id, new Paging(page), listener);
    }

    /**
     * Returns a list of the users currently featured on the site with their current statuses inline.
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getFeaturedAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FEATURED, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFeatured(getFeatured());
            }
        });
    }

    /**
     * Retrieves extended information of a given user, specified by ID or screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getUserDetailAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(USER_DETAIL, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserDetail(getUserDetail( (String) args[0]));
            }
        });
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getDirectMessagesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages());
            }
        });
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void getDirectMessagesAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages( (Paging) args[0]));
            }
        });
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use getDirectMessagesAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getDirectMessagesByPageAsync(int page, TwitterListener listener) {
        getDirectMessagesAsync(new Paging(page), listener);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param page int
     * @param sinceId Returns only direct messages with an ID greater than (that is, more recent than) the specified ID.
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.0
     * @deprecated use getDirectMessagesAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getDirectMessagesByPageAsync(int page
            , int sinceId, TwitterListener listener) {
        getDirectMessagesAsync(new Paging(page,(long)sinceId), listener);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param since Date
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated using long sinceId is suggested.
     */
    public synchronized void getDirectMessagesAsync(Date since, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages( (Date) args[0]));
            }
        });
    }
    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param sinceId int
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use getDirectMessagesAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getDirectMessagesAsync(int sinceId, TwitterListener listener) {
        getDirectMessagesAsync(new Paging((long)sinceId), listener);
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void getSentDirectMessagesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages());
            }
        });
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * @param since Date
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated using long sinceId is suggested.
     */
    public synchronized void getSentDirectMessagesAsync(Date since, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages( (Date) args[0]));
            }
        });
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * @param paging controls pagination
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void getSentDirectMessagesAsync(Paging paging, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {paging}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages( (Paging) args[0]));
            }
        });
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * @param sinceId returns only sent direct messages with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use getSentDirectMessagesAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getSentDirectMessagesAsync(int sinceId, TwitterListener listener) {
        getSentDirectMessagesAsync(new Paging((long) sinceId), listener);
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * @param page Retrieves the 20 next most recent direct messages.
     * @param sinceId returns only sent direct messages with an ID greater than (that is, more recent than) the specified ID
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.0
     * @deprecated use getSentDirectMessagesAsync(Paging paging, TwitterListener listener) instead
     */
    public synchronized void getSentDirectMessagesAsync(int page
            , int sinceId, TwitterListener listener) {
        getSentDirectMessagesAsync(new Paging(page, (long) sinceId), listener);
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimed if the length of the text is exceeding 140 characters.
     * @param id String
     * @param text String
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void sendDirectMessageAsync(String id, String text, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new String[] {id, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(sendDirectMessage( (String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimed if the length of the text is exceeding 140 characters.
     * @param id String
     * @param text String
     */
    public synchronized void sendDirectMessageAsync(String id, String text) {
        getDispatcher().invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, null, new String[] {id, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(sendDirectMessage( (String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * Delete specified direct message
     * @param id int
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void deleteDirectMessageAsync(int id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.deletedDirectMessage(deleteDirectMessage((Integer) args[0]));
            }
        });
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use createFriendshipAsync(String id, TwitterListener listener) instead
     */
    public synchronized void createAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.created(create( (String) args[0]));
            }
        });
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     *
     * @param id       String
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void createFriendshipAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(CREATE_FRIENDSHIP, listener, new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFriendship(createFriendship((String) args[0]));
            }
        });
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     * @param id String
     * @deprecated use createFriendshipAsync(String id) instead
     */
    public synchronized void createAsync(String id) {
        getDispatcher().invokeLater(new AsyncTask(CREATE,  new TwitterAdapter(), new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.created(create( (String) args[0]));
            }
        });
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     *
     * @param id String
     * @since Twitter4J 2.0.1
     */
    public synchronized void createFriendshipAsync(String id) {
        createFriendshipAsync(id, new TwitterAdapter());
    }

    /**
     * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use destroyFriendshipAsync(String id, TwitterListener listener) instead
     */
    public synchronized void destroyAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyed(destroy( (String) args[0]));
            }
        });

    }

    /**
     * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void destroyFriendshipAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY_FRIENDSHIP, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyedFriendship(destroyFriendship((String) args[0]));
            }
        });
    }

    /**
     * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
     * @param id String
>>>>>>> ab5411a88db762b8623825e30f92d1b7edd06464:src/main/java/twitter4j/AsyncTwitter.java
     * @deprecated use destroyFriendshipAsync(String id) instead
     */
    public synchronized void destroyAsync(String id) {
        getDispatcher().invokeLater(new AsyncTask(DESTROY, new TwitterAdapter(), new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyed(destroy( (String) args[0]));
            }
        });

    }

    /**
     * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
     *
     * @param id String
     * @since Twitter4J 2.0.1
     */
    public synchronized void destroyFriendshipAsync(String id) {
        destroyFriendshipAsync(id, new TwitterAdapter());
    }

    /**
     * Tests if a friendship exists between two users.
     *
     * @param user_a The ID or screen_name of the first user to test friendship for.
     * @param user_b The ID or screen_name of the second user to test friendship for.
     * @deprecated existsFriendshipAsync(String user_a, String user_b, TwitterListener listener)
     */
    public synchronized void existsAsync(String user_a, String user_b, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS, listener, new String[]{user_a, user_b}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExists(exists((String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * Tests if a friendship exists between two users.
     *
     * @param user_a The ID or screen_name of the first user to test friendship for.
     * @param user_b The ID or screen_name of the second user to test friendship for.
     * @since Twitter4J 2.0.1
     */
    public synchronized void existsFriendshipAsync(String user_a, String user_b, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(EXISTS_FRIENDSHIP, listener, new String[]{user_a, user_b}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotExistsFriendship(existsFriendship((String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     *
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
     * @since Twitter4J 2.0.0
     */
    public synchronized void getFriendsIDsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs());
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     *
     * @param userId   Specfies the ID of the user for whom to return the friends list.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
     * @since Twitter4J 2.0.0
     */
    public synchronized void getFriendsIDsAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((Integer) args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     *
     * @param screenName Specfies the screen name of the user for whom to return the friends list.
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
     * @since Twitter4J 2.0.0
     */
    public synchronized void getFriendsIDsAsync(String screenName, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FRIENDS_IDS, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFriendsIDs(getFriendsIDs((String) args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     *
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#followers/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - followers/ids</a>
     * @since Twitter4J 2.0.0
     */
    public synchronized void getFollowersIDsAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, null) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs());
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     *
     * @param userId   Specfies the ID of the user for whom to return the followers list.
     * @param listener a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#followers/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - followers/ids</a>
     * @since Twitter4J 2.0.0
     */
    public synchronized void getFollowersIDsAsync(int userId, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, new Integer[]{userId}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((Integer) args[0]));
            }
        });
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     *
     * @param screenName Specfies the screen name of the user for whom to return the followers list.
     * @param listener   a listener object that receives the response
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#followers/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - followers/ids</a>
     * @since Twitter4J 2.0.0
     */
    public synchronized void getFollowersIDsAsync(String screenName, TwitterListener listener) throws TwitterException {
        getDispatcher().invokeLater(new AsyncTask(FOLLOWERS_IDS, listener, new String[]{screenName}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotFollowersIDs(getFollowersIDs((String) args[0]));
            }
        });
    }

    /**
     * Update the location
     *
     * @param location the current location of the user
     * @param listener a listener object that receives the response
     * @since Twitter4J 1.0.4
     */
    public synchronized void updateLocationAsync(String location, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_LOCATION, listener, new Object[]{location}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedLocation(updateLocation((String) args[0]));
            }
        });
    }

    /**
     * Sets one or more hex values that control the color scheme of the authenticating user's profile page on twitter.com.  These values are also returned in the getUserDetail() method.
     * @param profileBackgroundColor optional, can be null
     * @param profileTextColor optional, can be null
     * @param profileLinkColor optional, can be null
     * @param profileSidebarFillColor optional, can be null
     * @param profileSidebarBorderColor optional, can be null
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/REST%20API%20Documentation#account/updatelocation">Twitter REST API Documentation &gt; Account Methods &gt; account/update_location</a>
     */
    public synchronized void updateProfileColorsAsync(
            String profileBackgroundColor, String profileTextColor,
            String profileLinkColor, String profileSidebarFillColor,
            String profileSidebarBorderColor) {
        updateProfileColorsAsync(profileBackgroundColor, profileTextColor,
                profileLinkColor, profileSidebarFillColor,
                profileSidebarBorderColor, null);
    }

    /**
     * Sets one or more hex values that control the color scheme of the authenticating user's profile page on twitter.com.  These values are also returned in the getUserDetail() method.
     * @param profileBackgroundColor optional, can be null
     * @param profileTextColor optional, can be null
     * @param profileLinkColor optional, can be null
     * @param profileSidebarFillColor optional, can be null
     * @param profileSidebarBorderColor optional, can be null
     * @param listener a listener object that receives the response
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/REST%20API%20Documentation#account/updatelocation">Twitter REST API Documentation &gt; Account Methods &gt; account/update_location</a>
     */
    public synchronized void updateProfileColorsAsync(
            String profileBackgroundColor, String profileTextColor,
            String profileLinkColor, String profileSidebarFillColor,
            String profileSidebarBorderColor, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_PROFILE_COLORS,
                listener, new Object[]{profileBackgroundColor, profileTextColor,
                        profileLinkColor, profileSidebarFillColor,
                        profileSidebarBorderColor}) {
            public void invoke(TwitterListener listener, Object[] args)
                    throws TwitterException {
                listener.updatedProfileColors(updateProfileColors(
                        (String) args[0], (String) args[1], (String) args[2],
                        (String) args[3], (String) args[4]));
            }
        });
    }


    /**
     * Gets the remaining number of API requests available to the requesting user before the API limit is reached for the current hour. Calls to rate_limit_status do not count against the rate limit.  If authentication credentials are provided, the rate limit status for the authenticating user is returned.  Otherwise, the rate limit status for the requester's IP address is returned.
     * See <a href="http://apiwiki.twitter.com/REST%20API%20Documentation#ratelimitstatus">Twitter REST API Documentation &gt; Account Methods &gt; rate_limit_status</a> for detail.
     *
     * @since Twitter4J 1.1.4
     */
    public synchronized void rateLimitStatusAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(RATE_LIMIT_STATUS, listener, new Object[]{}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotRateLimitStatus(rateLimitStatus());
            }
        });
    }

    /**
     * Sets which device Twitter delivers updates to for the authenticating user.  Sending none as the device parameter will disable IM or SMS updates.
     *
     * @param device   new Delivery device. Must be one of: IM, SMS, NONE.
     * @param listener a listener object that receives the response
     * @since Twitter4J 1.0.4
     */
    public synchronized void updateDeliverlyDeviceAsync(Device device, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(UPDATE_LOCATION, listener, new Object[]{device}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.updatedDeliverlyDevice(updateDeliverlyDevice((Device) args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void favoritesAsync(TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(favorites());
            }
        });
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * @param page number of page to retrieve favorites
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void favoritesAsync(int page, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(favorites((Integer)args[0]));
            }
        });
    }
    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void favoritesAsync(String id,TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(favorites((String)args[0]));
            }
        });
    }
    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
     * @param page retrieves the 20 next most recent favorite statuses.
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void favoritesAsync(String id,int page, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {id,page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(favorites((String)args[0],(Integer)args[1]));
            }
        });
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use createFavoriteAsync(long id, TwitterListener listener) instead.
     */
    public synchronized void createFavoriteAsync(int id, TwitterListener listener) {
        createFavoriteAsync((long) id, listener);
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     *
     * @param id       the ID or screen name of the user for whom to request a list of favorite statuses.
     * @param listener TwitterListener a listener object that receives the response
     * @since 1.1.2
     */
    public synchronized void createFavoriteAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFavorite(createFavorite((Long) args[0]));
            }
        });
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
     * @deprecated use createFavoriteAsync(long id, TwitterListener listener) instead.
     */
    public synchronized void createFavoriteAsync(int id) {
        createFavoriteAsync((long) id);
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     *
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
     * @since 1.1.2
     */
    public synchronized void createFavoriteAsync(long id) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, new TwitterAdapter(), new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.createdFavorite(createFavorite((Long) args[0]));
            }
        });
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * @param id the ID or screen name of the user for whom to request a list of un-favorite statuses.
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use destroyFavoriteAsync(long id, TwitterListener listener) instead.
     */
    public synchronized void destroyFavoriteAsync(int id, TwitterListener listener) {
        destroyFavoriteAsync((long) id, listener);
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     *
     * @param id       the ID or screen name of the user for whom to request a list of un-favorite statuses.
     * @param listener TwitterListener a listener object that receives the response
     * @since 1.1.2
     */
    public synchronized void destroyFavoriteAsync(long id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, listener, new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFavorite(destroyFavorite((Long) args[0]));
            }
        });
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * @param id the ID or screen name of the user for whom to request a list of un-favorite statuses.
     * @deprecated use destroyFavoriteAsync(long id) instead.
     */
    public synchronized void destroyFavoriteAsync(int id) {
        destroyFavoriteAsync((long) id);
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     *
     * @param id the ID or screen name of the user for whom to request a list of un-favorite statuses.
     * @since 1.1.2
     */
    public synchronized void destroyFavoriteAsync(long id) {
        getDispatcher().invokeLater(new AsyncTask(FAVORITES, new TwitterAdapter(), new Object[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.destroyedFavorite(destroyFavorite((Long) args[0]));
            }
        });
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id       String
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use enableNotificationAsync(String id, TwitterListener listener) instead
     */
    public synchronized void followAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOW, listener, new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.followed(follow((String) args[0]));
            }
        });
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id       String
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void enableNotificationAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(ENABLE_NOTIFICATION, listener, new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.enabledNotification((enableNotification((String) args[0])));
            }
        });
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id String
     * @deprecated use enableNotificationAsync(String id) instead
     */
    public synchronized void followAsync(String id) {
        getDispatcher().invokeLater(new AsyncTask(FOLLOW, new TwitterAdapter(), new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.followed(follow((String) args[0]));
            }
        });
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id       String
     * @since Twitter4J 2.0.1
     */
    public synchronized void enableNotificationAsync(String id) {
        enableNotificationAsync(id, new TwitterAdapter());
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id       String
     * @param listener TwitterListener a listener object that receives the response
     * @deprecated use disableNotificationAsync(String id, TwitterListener listener) instead
     */
    public synchronized void leaveAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(LEAVE, listener, new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.left(leave((String) args[0]));
            }
        });
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id       String
     * @param listener TwitterListener a listener object that receives the response
     * @since Twitter4J 2.0.1
     */
    public synchronized void disableNotificationAsync(String id, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(DISABLE_NOTIFICATION, listener, new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.disabledNotification(disableNotification((String) args[0]));
            }
        });
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id String
     */
    public synchronized void leaveAsync(String id) {
        getDispatcher().invokeLater(new AsyncTask(LEAVE, new TwitterAdapter(), new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.left(leave((String) args[0]));
            }
        });
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id       String
     * @since Twitter4J 2.0.1
     */
    public synchronized void disableNotificationAsync(String id) {
        disableNotificationAsync(id, new TwitterAdapter());
    }


    /* Block Methods */

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     *
     * @param id the ID or screen_name of the user to block
     * @since Twitter4J 1.0.4
     */
    public synchronized void blockAsync(String id) {
        getDispatcher().invokeLater(new AsyncTask(BLOCK, new TwitterAdapter(), new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.blocked(block((String) args[0]));
            }
        });
    }


    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     *
     * @param id the ID or screen_name of the user to block
     * @since Twitter4J 1.0.4
     */
    public synchronized void unblockAsync(String id) {
        getDispatcher().invokeLater(new AsyncTask(UNBLOCK, new TwitterAdapter(), new String[]{id}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.unblocked(unblock((String) args[0]));
            }
        });
    }

    /* Help Methods */

    /**
     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
     *
     * @since Twitter4J 1.0.4
     */
    public synchronized void testAsync() {
        getDispatcher().invokeLater(new AsyncTask(TEST, new TwitterAdapter(), new Object[]{}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.tested(test());
            }
        });
    }

    private transient Dispatcher dispatcher;

    private Dispatcher getDispatcher(){
        if(null == dispatcher){
            dispatcher = new Dispatcher("Twitter4J Async Dispatcher");
        }
        return dispatcher;
    }

    /**
     * Returns the same text displayed on http://twitter.com/home when a maintenance window is scheduled, in the requested format.
     *
     * @since Twitter4J 1.0.4
     */
    public synchronized void getDowntimeScheduleAsync() {
        getDispatcher().invokeLater(new AsyncTask(GET_DOWNTIME_SCHEDULE, new TwitterAdapter(), new Object[]{}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.gotDowntimeSchedule(getDowntimeSchedule());
            }
        });
    }

    /**
     * Retrieves extended information of the authenticated user.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.<br>
     * The call Twitter.getAuthenticatedUser() is equivalent to the call:<br>
     * twitter.getUserDetailAsync(twitter.getUserId(), listener);
     *
     * @since Twitter4J 1.1.3
     */
    public synchronized void getAuthenticatedUserAsync(TwitterListener listener) {
        if (null == getUserId()) {
            throw new IllegalStateException("User Id not specified.");
        }
        getUserDetailAsync(getUserId(), listener);
    }

    /**
     * @param query - the search condition
     * @return the result
     * @throws TwitterException
     * @since Twitter4J 1.1.7
     * @see <a href="http://apiwiki.twitter.com/Search-API-Documentation">Twitter API / Search API Documentation</a>
     * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
     */
    public synchronized void searchAcync(Query query, TwitterListener listener) {
        getDispatcher().invokeLater(new AsyncTask(SEARCH, listener, new Object[]{query}) {
            public void invoke(TwitterListener listener, Object[] args) throws TwitterException {
                listener.searched(search((Query) args[0]));
            }
        });
    }


    abstract class AsyncTask implements Runnable {
        TwitterListener listener;
        Object[] args;
        int method;
        AsyncTask(int method, TwitterListener listener, Object[] args) {
            this.method = method;
            this.listener = listener;
            this.args = args;
        }

        abstract void invoke(TwitterListener listener,Object[] args) throws TwitterException;

        public void run() {
            try {
                   invoke(listener,args);
            } catch (TwitterException te) {
                if (null != listener) {
                    listener.onException(te,method);
                }
            }
        }
    }
    public final static int PUBLIC_TIMELINE = 0;
    public final static int FRIENDS_TIMELINE = 1;
    public final static int USER_TIMELINE = 2;
    public final static int SHOW = 3;
    public final static int UPDATE = 4;
    /**
     * @deprecated use DESTROY_FRIENDSHIP instead.
     */
    public final static int REPLIES = 5;
    public final static int MENTIONS = 37;
    public final static int FRIENDS = 6;
    public final static int FOLLOWERS = 7;
    public final static int FEATURED = 8;
    public final static int USER_DETAIL = 9;
    public final static int DIRECT_MESSAGES = 10;
    public final static int SEND_DIRECT_MESSAGE = 11;
    public final static int CREATE = 12;
    public final static int CREATE_FRIENDSHIP = 32;
    /**
     * @deprecated use DESTROY_FRIENDSHIP instead.
     */
    public final static int DESTORY = 13;
    /**
     * @deprecated use DESTROY_FRIENDSHIP instead.
     */
    public final static int DESTROY = 13;
    public final static int DESTROY_FRIENDSHIP = 33;
    /**
     * @deprecated use EXISTS_FRIENDSHIP instead.
     */
    public final static int EXISTS = 28;
    public final static int EXISTS_FRIENDSHIP = 34;
    /**
     * @deprecated use ENABLE_NOTIFICATION instead.
     */
    public final static int FOLLOW = 14;
    public final static int ENABLE_NOTIFICATION = 35;
    /**
     * @deprecated use DISABLE_NOTIFICATION instead.
     */
    public final static int LEAVE = 15;
    public final static int DISABLE_NOTIFICATION = 36;
    public final static int FAVORITES = 17;
    public final static int FRIENDS_IDS = 29;
    public final static int FOLLOWERS_IDS = 30;
    public final static int CREATE_FAVORITE = 18;
    public final static int DESTROY_FAVORITE = 19;
    public final static int UPDATE_LOCATION = 20;
    public final static int UPDATE_PROFILE_COLORS = 31;
    public final static int RATE_LIMIT_STATUS = 28;
    public final static int UPDATE_DELIVERLY_DEVICE = 21;
    public final static int BLOCK = 22;
    public final static int UNBLOCK = 23;
    public final static int TEST = 24;
    public final static int GET_DOWNTIME_SCHEDULE = 25;
    public final static int DESTROY_STATUS = 26;
    public final static int SEARCH = 27;
}
