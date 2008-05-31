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
    private static final long serialVersionUID = 3400480876549514356L;

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
        dispatcher.invokeLater(new AsyncTask(PUBLIC_TIMELINE, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotPublicTimeline(getPublicTimeline());
            }
        });
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     * @param sinceID String
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response

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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void getFriendsAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS, listener, null) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends());
            }
        });
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * @param page number of the page to retrieve friends
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void getFriendsAsync(int page,TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS, listener, new Object[]{page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends((Integer)args[0]));
            }
        });
    }
    /**
     * Returns the user's friends, each with current status inline.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void getFriendsAsync(String id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends( (String) args[0]));
            }
        });
    }

    /**
     * Returns the user's friends, each with current status inline.
     * @param id String
     * @param page int
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void getFriendsAsync(String id,int page, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FRIENDS, listener, new Object[] {id,page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFriends(getFriends( (String) args[0],(Integer)args[1]));
            }
        });
    }


    /**
     * Returns the authenticating user's followers, each with current status inline.
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void getSentDirectMessagesAsync(int sinceId, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {sinceId}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotSentDirectMessages(getSentDirectMessages( (Integer) args[0]));
            }
        });
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimed if the length of the text is exceeding 140 characters.
     * @param id String
     * @param text String
     * @param listener TwitterListener a listener object that receives the response
     */
    public synchronized void sendDirectMessageAsync(String id, String text, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, listener, new String[] {id, text}) {
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
        dispatcher.invokeLater(new AsyncTask(SEND_DIRECT_MESSAGE, null, new String[] {id, text}) {
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
    public final synchronized void deleteDirectMessageAsync(int id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(DIRECT_MESSAGES, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.deletedDirectMessage(deleteDirectMessage((Integer) args[0]));
            }
        });
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
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
     * @param listener TwitterListener a listener object that receives the response
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
    /**
     * Returns 80 statuses per page for the authenticating user, ordered by descending date of posting.  Use this method to rapidly export your archive of statuses.
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void archiveAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(ARCHIVE, listener, new Object[] {}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotArchive(archive());
            }
        });
    }
    /**
     * Returns 80 statuses per page for the authenticating user, ordered by descending date of posting.  Use this method to rapidly export your archive of statuses.
     *
     * @param page number of the page to retrieve archive
     * @param listener a listener object that receives the response
     */
    public final synchronized void archiveAsync(int page, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(ARCHIVE, listener, new Object[] {page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotArchive(archive((Integer)args[0]));
            }
        });
    }

    /**
     * Update the location
     *
     * @param location the current location of the user
     * @param listener a listener object that receives the response
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized void updateLocationAsync(String location,TwitterListener listener) throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(UPDATE_LOCATION, listener, new Object[] {location}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updatedLocation(updateLocation((String)args[0]));
            }
        });
    }


    /**
     * Sets which device Twitter delivers updates to for the authenticating user.  Sending none as the device parameter will disable IM or SMS updates.
     *
     * @param device new Delivery device. Must be one of: IM, SMS, NONE.
     * @param listener a listener object that receives the response
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized void updateDeliverlyDeviceAsync(Device device,TwitterListener listener) throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(UPDATE_LOCATION, listener, new Object[] {device}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.updatedDeliverlyDevice(updateDeliverlyDevice((Device)args[0]));
            }
        });
    }
    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void favoritesAsync(TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FAVORITES, listener, null) {
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
    public final synchronized void favoritesAsync(int page, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {page}) {
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
    public final synchronized void favoritesAsync(String id,TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FAVORITES, listener, new Object[]{id}) {
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
    public final synchronized void favoritesAsync(String id,int page, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {id,page}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotFavorites(favorites((String)args[0],(Integer)args[1]));
            }
        });
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void createFavoriteAsync(int id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.createdFavorite(createFavorite((Integer)args[0]));
            }
        });
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
     */
    public final synchronized void createFavoriteAsync(int id) {
        dispatcher.invokeLater(new AsyncTask(FAVORITES, new TwitterAdapter(), new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.createdFavorite(createFavorite((Integer)args[0]));
            }
        });
    }
    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * @param id the ID or screen name of the user for whom to request a list of un-favorite statuses.
     * @param listener TwitterListener a listener object that receives the response
     */
    public final synchronized void destroyFavoriteAsync(int id, TwitterListener listener) {
        dispatcher.invokeLater(new AsyncTask(FAVORITES, listener, new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyedFavorite(destroyFavorite((Integer)args[0]));
            }
        });
    }
    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * @param id the ID or screen name of the user for whom to request a list of un-favorite statuses.
     */
    public final synchronized void destroyFavoriteAsync(int id) {
        dispatcher.invokeLater(new AsyncTask(FAVORITES,  new TwitterAdapter(), new Object[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.destroyedFavorite(destroyFavorite((Integer)args[0]));
            }
        });
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized void followAsync(String id,TwitterListener listener) throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(FOLLOW, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.followed(follow( (String) args[0]));
            }
        });
    }
    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * @param id String
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized void followAsync(String id) throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(FOLLOW, new TwitterAdapter(), new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.followed(follow( (String) args[0]));
            }
        });
    }
    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * @param id String
     * @param listener TwitterListener a listener object that receives the response
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized void leaveAsync(String id,TwitterListener listener) throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(LEAVE, listener, new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.left(leave( (String) args[0]));
            }
        });
    }


    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * @param id String
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized void leaveAsync(String id) throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(LEAVE, new TwitterAdapter(), new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.left(leave( (String) args[0]));
            }
        });
    }


    /* Block Methods */

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     *
     * @param id the ID or screen_name of the user to block
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized void blockAsync(String id) throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(BLOCK, new TwitterAdapter(), new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.blocked(block( (String) args[0]));
            }
        });
    }


    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     *
     * @param id the ID or screen_name of the user to block
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized void unblockAsync(String id) throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(UNBLOCK, new TwitterAdapter(), new String[] {id}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.unblocked(unblock( (String) args[0]));
            }
        });
    }

    /* Help Methods */

    /**
     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
     *
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized void testAsync() throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(TEST, new TwitterAdapter(), new Object[] {}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.tested(test());
            }
        });
    }

    /**
     * Returns the same text displayed on http://twitter.com/home when a maintenance window is scheduled, in the requested format.
     *
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized void getDowntimeScheduleAsync() throws TwitterException {
        dispatcher.invokeLater(new AsyncTask(GET_DOWNTIME_SCHEDULE, new TwitterAdapter(), new Object[] {}) {
            public void invoke(TwitterListener listener,Object[] args) throws TwitterException {
                listener.gotDowntimeSchedule(getDowntimeSchedule());
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
    public final static int FOLLOW = 14;
    public final static int LEAVE = 15;
    public final static int ARCHIVE = 16;
    public final static int FAVORITES = 17;
    public final static int CREATE_FAVORITE = 18;
    public final static int DESTROY_FAVORITE = 19;
    public final static int UPDATE_LOCATION = 20;
    public final static int UPDATE_DELIVERLY_DEVICE = 21;
    public final static int BLOCK = 22;
    public final static int UNBLOCK = 23;
    public final static int TEST = 24;
    public final static int GET_DOWNTIME_SCHEDULE = 25;
}
