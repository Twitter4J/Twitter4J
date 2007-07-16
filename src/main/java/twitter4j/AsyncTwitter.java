package twitter4j;

import java.util.Date;

/**
 * Twitter API with a series of asynchronous APIs.<br>
 * With this class, you can call TwitterAPI acynchronously.<br>
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterListener
 */
public class AsyncTwitter extends Twitter {
    private Dispatcher dispatcher = new Dispatcher("Twitter4J Async Dispatcher");

    public AsyncTwitter(String id, String password) {
        super(id, password);
    }

    public AsyncTwitter(String id, String password, String baseURL) {
        super(id, password, baseURL);
    }

    /**
     * Returns the 20 most recent statuses from non-protected users who have set a custom user icon.
     */

    public void getPublicTimelineAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline());
            }
        });
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     * @param sinceID String
     * @deprecated argument should be always numeric. Use getPublicTimelineAsync(int sinceID, TwitterListener listener) instead
     */
    public void getPublicTimelineAsync(String sinceID, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, new String[] {sinceID}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline( (String) args[0]));
            }
        });
    }
    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     * @param sinceID String
     */
    public void getPublicTimelineAsync(int sinceID, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, new Integer[] {sinceID}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline( (Integer) args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
     *  It's also possible to request another user's friends_timeline via the id parameter below.
     */

    public final synchronized void getFriendsTimelineAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline());
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param page int
     */
    public final synchronized void getFriendsTimelineByPageAsync(int page, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new Object[] {page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimelineByPage( (Integer) args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     */
    public final synchronized void getFriendsTimelineAsync(String id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline( (String) args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param page int
     */
    public final synchronized void getFriendsTimelineByPageAsync(String id,int page, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new Object[] {id,page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimelineByPage( (String) args[0],(Integer)args[1]));
            }
        });
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param since Date
     */
    public final synchronized void getFriendsTimelineAsync(Date since, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new Object[] {since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline( (Date) args[0]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param since Date
     */
    public final synchronized void getFriendsTimelineAsync(String id, Date since, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS_TIMELINE, listener, new Object[] {id, since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriendsTimeline(getFriendsTimeline( (String) args[0], (Date) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param count int
     * @param since Date
     */
    public final synchronized void getUserTimelineAsync(String id, int count, Date since, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {id, count, since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (String) args[0], (Integer) args[1], (Date) args[2]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param since Date
     */
    public final synchronized void getUserTimelineAsync(String id, Date since, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {id, since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (String) args[0], (Date) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param count int
     */
    public final synchronized void getUserTimelineAsync(String id, int count, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {id, count}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (String) args[0], (Integer) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * @param count int
     * @param since Date
     */
    public final synchronized void getUserTimelineAsync(int count, Date since, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {count, since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (Integer) args[0], (Date) args[1]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     */
    public final synchronized void getUserTimelineAsync(String id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(USER_TIMELINE, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline( (String) args[0]));
            }
        });
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     */
    public final synchronized void getUserTimelineAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(USER_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserTimeline(getUserTimeline());
            }
        });
    }

    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     * @param id int
     */
    public final synchronized void showAsync(int id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(SHOW, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotShow(show( (Integer) args[0]));
            }
        });
    }

    /**
     *
     * Updates the user's status asynchronously
     * @param status String
     * @param listener TwittterListener

     */
    public void updateAsync(String status, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(UPDATE, listener, new String[] {status}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updated(update( (String) args[0]));
            }
        });
    }
    /**
     *
     * Updates the user's status asynchronously
     * @param status String
     */
    public void updateAsync(String status) {
        dispatcher.invokeLater(new AsyncTask(UPDATE,  new TwitterAdapter(), new String[] {status}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updated(update( (String) args[0]));
            }
        });
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     */
    public final synchronized void getRepliesAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(REPLIES, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotReplies(getReplies());
            }
        });
    }
    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @param page int
     */
    public final synchronized void getRepliesByPageAsync(int page,TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(REPLIES, listener, new Object[]{page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotReplies(getRepliesByPage((Integer)args[0]));
            }
        });
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     */
    public final synchronized void getFriendsAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends());
            }
        });
    }

    /**
     * Returns the user's friends, each with current status inline.
     * @param id String
     */
    public final synchronized void getFriendsAsync(String id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends( (String) args[0]));
            }
        });
    }

    /**
     * Returns the authenticating user's followers, each with current status inline.
     */
    public final synchronized void getFollowersAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FOLLOWERS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFollowers(getFollowers());
            }
        });
    }

    /**
     * Returns a list of the users currently featured on the site with their current statuses inline.
     */
    public final synchronized void getFeaturedAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FEATURED, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFeatured(getFeatured());
            }
        });
    }

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     * @param id String
     */
    public final synchronized void getUserDetailAsync(String id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(USER_DETAIL, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotUserDetail(getUserDetail( (String) args[0]));
            }
        });
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     */
    public final synchronized void getDirectMessagesAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages());
            }
        });
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param page int
     */
    public final synchronized void getDirectMessagesByPageAsync(int page, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessagesByPage( (Integer) args[0]));
            }
        });
    }
    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param since Date
     */
    public final synchronized void getDirectMessagesAsync(Date since, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages( (Date) args[0]));
            }
        });
    }
    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param sinceId int
     */
    public final synchronized void getDirectMessagesAsync(int sinceId, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {sinceId}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDirectMessages(getDirectMessages( (Integer) args[0]));
            }
        });
    }
    /**
     * Returns a list of the direct messages sent by the authenticating user.
     */
    public final synchronized void getSentDirectMessagesAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener,null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages());
            }
        });
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * @param since Date
     */
    public final synchronized void getSentDirectMessagesAsync(Date since, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {since}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages( (Date) args[0]));
            }
        });
    }
    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * @param sinceId Date
     */
    public final synchronized void getSentDirectMessagesAsync(int sinceId, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {sinceId}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages( (Integer) args[0]));
            }
        });
    }


    public synchronized void sendDirectMessageAsync(String id, String text, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new String[] {id, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(sendDirectMessage( (String) args[0], (String) args[1]));
            }
        });
    }
    public synchronized void sendDirectMessageAsync(String id, String text) {
        dispatcher.invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, null, new String[] {id, text}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.sentDirectMessage(sendDirectMessage( (String) args[0], (String) args[1]));
            }
        });
    }

    /**
     * Delete specified direct message
     * @param id int
     */
    public final synchronized void deleteDirectMessageAsync(int id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.deletedDirectMessage(deleteDirectMessage( ((Integer) args[0]).intValue()));
            }
        });
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     * @param id String
     */
    public final synchronized void createAsync(String id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(CREATE, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.created(create( (String) args[0]));
            }
        });
    }
    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     * @param id String
     */
    public final synchronized void createAsync(String id) {
        dispatcher.invokeLater(new AsyncTask(CREATE,  new TwitterAdapter(), new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.created(create( (String) args[0]));
            }
        });
    }

    /**
     * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
     * @param id String
     */
    public final synchronized void destroyAsync(String id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DESTORY, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyed(destroy( (String) args[0]));
            }
        });

    }
    /**
     * Discontinues friendship with the specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.
     * @param id String
     */
    public final synchronized void destroyAsync(String id) {
        dispatcher.invokeLater(new AsyncTask(DESTORY, new TwitterAdapter(), new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyed(destroy( (String) args[0]));
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
    public final static int REPLIES = 5;
    public final static int FRIENDS = 6;
    public final static int FOLLOWERS = 7;
    public final static int FEATURED = 8;
    public final static int USER_DETAIL = 9;
    public final static int DIRECT_MESSAGES = 10;
    public final static int SEND_DIRECT_MESSAGE = 11;
    public final static int CREATE = 12;
    public final static int DESTORY = 13;
}
