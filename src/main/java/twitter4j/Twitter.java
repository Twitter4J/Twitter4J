package twitter4j;

import twitter4j.http.HttpClient;
import twitter4j.http.PostParameter;
import twitter4j.http.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A java reporesentation of the <a href="http://twitter.com/help/api">Twitter API</a>
 */
public class Twitter implements java.io.Serializable {
    HttpClient http = null;
    private String baseURL = "http://twitter.com/";
    private String source = "Twitter4J";

    private boolean usePostForcibly = false;
    private static final long serialVersionUID = 4346156413282535531L;
    private static final int MAX_COUNT = 200;

    public Twitter() {
        http = new HttpClient();
        setRequestHeader("X-Twitter-Client", "Twitter4J");
        setRequestHeader("X-Twitter-Client-Version", "1.1.0");
        setRequestHeader("X-Twitter-Client-URL",
                "http://yusuke.homeip.net/twitter4j/en/twitter4j-1.1.0.xml");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public Twitter(String baseURL) {
        this();
        this.baseURL = baseURL;
    }

    public Twitter(String id, String password) {
        this();
        setUserId(id);
        setPassword(password);
    }

    public Twitter(String id, String password, String baseURL) {
        this(id, password);
        this.baseURL = baseURL;
    }

    /**
     * Sets the base URL
     *
     * @param baseURL String the base URL
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Returns the base URL
     *
     * @return the base URL
     */
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * Sets the userid
     *
     * @param userId new userid
     */
    public void setUserId(String userId) {
        http.setUserId(userId);
    }

    /**
     * Returns authenticating userid
     *
     * @return userid
     */
    public String getUserId() {
        return http.getUserId();
    }

    /**
     * Sets the password
     *
     * @param password new password
     */
    public void setPassword(String password) {
        http.setPassword(password);
    }

    /**
     * Returns authenticating password
     *
     * @return password
     */
    public String getPassword() {
        return http.getPassword();
    }

    /**
     * Sets the source parameter that will be passed by updating methods
     * See below for details.
     * Twitter API Wiki > How do I get “from [my_application]” appended to updates sent from my API application?
     * http://apiwiki.twitter.com/REST+API+Documentation
     *
     * @param source the new source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Returns the source
     *
     * @return source
     */
    public String getSource() {
        return this.source;
    }

    /**
     * sets the request header name/value combination
     * see Twitter Fan Wiki for detail.
     * http://twitter.pbwiki.com/API-Docs#RequestHeaders
     *
     * @param name  the name of the request header
     * @param value the value of the request header
     */
    public void setRequestHeader(String name, String value) {
        http.setRequestHeader(name, value);
    }

    /**
     * set true to force using POST method communicating to the server
     *
     * @param forceUsePost if true POST method will be used forcibly
     */
    public void forceUsePost(boolean forceUsePost) {
        this.usePostForcibly = forceUsePost;
    }

    /**
     * @return true if POST is used forcibly
     */
    public boolean isUsePostForced() {
        return this.usePostForcibly;
    }

    /**
     * issues an HTTP GET request. POST method will be used instead in case forceUsePost is set true.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    private Response get(String url, boolean authenticate) throws TwitterException {
        return get(url, null, authenticate);
    }

    /**
     * issues an HTTP GET request. POST method will be used instead in case forceUsePost is set true.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    private Response get(String url, String name1, String value1, boolean authenticate) throws TwitterException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1)}, authenticate);
    }

    /**
     * issues an HTTP GET request. POST method will be used instead in case forceUsePost is set true.
     *
     * @param url          the request url
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @param name2        the name of the second parameter
     * @param value2       the value of the second parameter
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    private Response get(String url, String name1, String value1, String name2, String value2, boolean authenticate) throws TwitterException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1), new PostParameter(name2, value2)}, authenticate);
    }

    /**
     * issues an HTTP GET request. POST method will be used instead in case forceUsePost is set true.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    private Response get(String url, PostParameter[] params, boolean authenticate) throws TwitterException {
        if (usePostForcibly) {
            if (null == params) {
                return http.post(url, new PostParameter[0], authenticate);
            } else {
                return http.post(url, params, authenticate);
            }
        } else {
            if (null != params && params.length > 0) {
                url += "?" + HttpClient.encodeParameters(params);
            }
            return http.get(url, authenticate);
        }
    }

    /* Status Methods */

    /**
     * Returns the 20 most recent statuses from non-protected users who have set a custom user icon.
     *
     * @return list of statuses of the Public Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getPublicTimeline() throws
            TwitterException {
        return Status.constructStatuses(get(baseURL +
                "statuses/public_timeline.xml", false).
                asDocument(), this);
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     *
     * @param sinceID returns only public statuses with an ID greater than (that is, more recent than) the specified ID
     * @return the 20 most recent statuses
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getPublicTimeline(int sinceID) throws
            TwitterException {
        return Status.constructStatuses(get(baseURL +
                "statuses/public_timeline.xml", "since_id", String.valueOf(sinceID), false).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating1 user and that user's friends.
     * It's also possible to request another user's friends_timeline via the id parameter below.
     *
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getFriendsTimeline() throws
            TwitterException {
        return Status.constructStatuses(get(baseURL +
                "statuses/friends_timeline.xml", true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @param page the number of page
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getFriendsTimelineByPage(int page) throws
            TwitterException {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline.xml",
                "page", String.valueOf(page), true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
     *
     * @param id specifies the ID or screen name of the user for whom to return the friends_timeline
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getFriendsTimeline(String id) throws
            TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline/" + id + ".xml",
                true).asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
     *
     * @param id   specifies the ID or screen name of the user for whom to return the friends_timeline
     * @param page the number of page
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getFriendsTimelineByPage(String id, int page) throws
            TwitterException {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline/" + id + ".xml",
                "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getFriendsTimeline(Date since) throws
            TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline.xml",
                "since", format.format(since), true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified userid.
     *
     * @param id    specifies the ID or screen name of the user for whom to return the friends_timeline
     * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getFriendsTimeline(String id,
                                                              Date since) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline/" + id + ".xml",
                "since", format.format(since), true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified userid.
     *
     * @param id    specifies the ID or screen name of the user for whom to return the user_timeline
     * @param count specifies the number of statuses to retrieve.  May not be greater than 200 for performance purposes
     * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
     * @return list of the user Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id, int count,
                                                           Date since) throws TwitterException {
        if (MAX_COUNT < count) {
            throw new IllegalArgumentException("count may not be greater than " + MAX_COUNT + " for performance purposes.");
        }
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml",
                "since", format.format(since), "count", String.valueOf(count), true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified userid.
     *
     * @param id    specifies the ID or screen name of the user for whom to return the user_timeline
     * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id, Date since) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml",
                "since", format.format(since), true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified userid.
     *
     * @param id    specifies the ID or screen name of the user for whom to return the user_timeline
     * @param count specifies the number of statuses to retrieve.  May not be greater than 200 for performance purposes
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id, int count) throws
            TwitterException {
        if (MAX_COUNT < count) {
            throw new IllegalArgumentException("count may not be greater than " + MAX_COUNT + " for performance purposes.");
        }
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml",
                "count", String.valueOf(count), true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @param count specifies the number of statuses to retrieve.  May not be greater than 200 for performance purposes
     * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(int count, Date since) throws TwitterException {
        if (MAX_COUNT < count) {
            throw new IllegalArgumentException("count may not be greater than " + MAX_COUNT + " for performance purposes.");
        }
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline.xml",
                "since", format.format(since), "count", String.valueOf(count), true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified userid.
     *
     * @param id specifies the ID or screen name of the user for whom to return the user_timeline
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml", true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline() throws
            TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline.xml", true).
                asDocument(), this);
    }

    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     *
     * @param id the numerical ID of the status you're trying to retrieve
     * @return a single status
     * @throws TwitterException when Twitter service or network is unavailable
     * @deprecated Use show(long id) instead.
     */

    public final synchronized Status show(int id) throws TwitterException {
        return new Status(get(baseURL + "statuses/show/" + id + ".xml", false).asDocument().getDocumentElement(), this);
    }
    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     *
     * @param id the numerical ID of the status you're trying to retrieve
     * @return a single status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since 1.1.1
     */

    public final synchronized Status show(long id) throws TwitterException {
        return new Status(get(baseURL + "statuses/show/" + id + ".xml", false).asDocument().getDocumentElement(), this);
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     *
     * @param status the text of your status update
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public Status update(String status) throws TwitterException {
        if (status.length() > 160) {
            status = status.substring(0, 160);
        }
        return new Status(http.post(baseURL + "statuses/update.xml",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)}, true).asDocument().getDocumentElement(), this);
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     *
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getReplies() throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/replies.xml", true).asDocument(), this);
    }

    /**
     * Returns the most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     *
     * @param page the number of page
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getRepliesByPage(int page) throws TwitterException {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return Status.constructStatuses(get(baseURL + "statuses/replies.xml",
                "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Destroys the status specified by the required ID parameter.  The authenticating user must be the author of the specified status.
     *
     * @param statusId The ID of the status to destroy.
     * @return the deleted status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since 1.0.5
     */
    public Status destroyStatus(long statusId) throws TwitterException {
        return new Status(http.post(baseURL + "statuses/destroy/" + statusId + ".xml",
                new PostParameter[0], true).asDocument().getDocumentElement(), this);
    }

    /* User Methods */

    /**
     * Returns the specified user's friends, each with current status inline.
     *
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml", true).asDocument(), this);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     *
     * @param page number of page
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends(int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml", "page",
                String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns the user's friends, each with current status inline.
     *
     * @param id the ID or screen name of the user for whom to request a list of friends
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends(String id) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml",
                "id", id, true).asDocument(), this);
    }

    /**
     * Returns the user's friends, each with current status inline.
     *
     * @param id   the ID or screen name of the user for whom to request a list of friends
     * @param page the number of page
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends(String id, int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml",
                "id", id, "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFollowers() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers.xml", true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @param page Retrieves the next 100 followers.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.1.0
     */
    public final synchronized List<User> getFollowers(int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers.xml", "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @param id The ID or screen name of the user for whom to request a list of followers.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.1.0
     */
    public final synchronized List<User> getFollowers(String id) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers/" + id + ".xml", true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @param id   The ID or screen name of the user for whom to request a list of followers.
     * @param page Retrieves the next 100 followers.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.1.0
     */
    public final synchronized List<User> getFollowers(String id, int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers/" + id + ".xml", "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns a list of the users currently featured on the site with their current statuses inline.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFeatured() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/featured.xml", true).asDocument(), this);
    }

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     *
     * @param id the ID or screen name of the user for whom to request the detail
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized UserWithStatus getUserDetail(String id) throws TwitterException {
        return new UserWithStatus(get(baseURL + "users/show/" + id + ".xml", true).asDocument().getDocumentElement(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessages() throws TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL + "direct_messages.xml", true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @param page the number of page
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessagesByPage(int page) throws TwitterException {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return DirectMessage.constructDirectMessages(get(baseURL + "direct_messages.xml",
                "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @param sinceId int
     * @return list of direct messages
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessages(int sinceId) throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages.xml", "since_id", String.valueOf(sinceId), true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @param since narrows the resulting list of direct messages to just those sent after the specified HTTP-formatted date
     * @return list of direct messages
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessages(Date since) throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages.xml", "since", format.format(since), true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getSentDirectMessages() throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages/sent.xml", new PostParameter[0], true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     *
     * @param since narrows the resulting list of direct messages to just those sent after the specified HTTP-formatted date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getSentDirectMessages(Date since) throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages/sent.xml", "since", format.format(since), true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     *
     * @param sinceId returns only sent direct messages with an ID greater than (that is, more recent than) the specified ID
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getSentDirectMessages(int sinceId) throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages/sent.xml", "since_id", String.valueOf(sinceId), true).asDocument(), this);
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimed if the length of the text is exceeding 140 characters.
     *
     * @param id   the ID or screen name of the user to whom send the direct message
     * @param text String
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized DirectMessage sendDirectMessage(String id,
                                                              String text) throws TwitterException {
        if (text.length() > 160) {
            text = text.substring(0, 160);
        }
        return new DirectMessage(http.post(baseURL + "direct_messages/new.xml",
                new PostParameter[]{new PostParameter("user", id),
                        new PostParameter("text", text)}, true).
                asDocument().getDocumentElement(), this);
    }


    /**
     * Destroys the direct message specified in the required ID parameter.  The authenticating user must be the recipient of the specified direct message.
     *
     * @param id the ID of the direct message to destroy
     * @return the deleted direct message
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized DirectMessage deleteDirectMessage(int id) throws
            TwitterException {
        return new DirectMessage(http.post(baseURL +
                "direct_messages/destroy/" + id + ".xml", new PostParameter[0], true).asDocument().getDocumentElement(), this);
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     *
     * @param id the ID or screen name of the user to be befriended
     * @return the befriended user
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized User create(String id) throws TwitterException {
        return new User(http.post(baseURL + "friendships/create/" + id + ".xml", new PostParameter[0], true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Discontinues friendship with the user specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     *
     * @param id the ID or screen name of the user for whom to request a list of friends
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized User destroy(String id) throws TwitterException {
        return new User(http.post(baseURL + "friendships/destroy/" + id + ".xml", new PostParameter[0], true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Returns true if authentication was successful.  Use this method to test if supplied user credentials are valid with minimal overhead.
     *
     * @return success
     */
    public final synchronized boolean verifyCredentials() {
        try {
            return get(baseURL + "account/verify_credentials.xml", true).asString().contains("true");
        } catch (TwitterException te) {
            return false;
        }
    }

    /**
     * Update the location
     *
     * @param location the current location of the user
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized User updateLocation(String location) throws TwitterException {
        return new User(http.post(baseURL + "account/update_location.xml", new PostParameter[]{new PostParameter("location", location)}, true).
                asDocument().getDocumentElement(), this);
    }

    public final static Device IM = new Device("im");
    public final static Device SMS = new Device("sms");
    public final static Device NONE = new Device("none");

    static class Device {
        final String DEVICE;

        public Device(String device) {
            DEVICE = device;
        }
    }

    /**
     * Sets which device Twitter delivers updates to for the authenticating user.  Sending none as the device parameter will disable IM or SMS updates.
     *
     * @param device new Delivery device. Must be one of: IM, SMS, NONE.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized User updateDeliverlyDevice(Device device) throws TwitterException {
        return new User(http.post(baseURL + "account/update_delivery_device", new PostParameter[]{new PostParameter("device", device.DEVICE)}, true).
                asDocument().getDocumentElement(), this);
    }


    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> favorites() throws TwitterException {
        return Status.constructStatuses(get(baseURL + "favorites.xml", new PostParameter[0], true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @param page the number of page
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> favorites(int page) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "favorites.xml", "page", String.valueOf(page), true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> favorites(String id) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "favorites/" + id + ".xml", new PostParameter[0], true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @param id   the ID or screen name of the user for whom to request a list of favorite statuses
     * @param page the number of page
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> favorites(String id, int page) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "favorites/" + id + ".xml", "page", String.valueOf(page), true).
                asDocument(), this);
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     *
     * @param id the ID of the status to favorite
     * @return Status
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized Status createFavorite(long id) throws TwitterException {
        return new Status(http.post(baseURL + "favorites/create/" + id + ".xml", true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Un-favorites the status specified in the ID parameter as the authenticating user.  Returns the un-favorited status in the requested format when successful.
     *
     * @param id the ID of the status to un-favorite
     * @return Status
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized Status destroyFavorite(long id) throws TwitterException {
        return new Status(http.post(baseURL + "favorites/destroy/" + id + ".xml", true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id String
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized User follow(String id) throws TwitterException {
        return new User(http.post(baseURL + "notifications/follow/" + id + ".xml", true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     *
     * @param id String
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized User leave(String id) throws TwitterException {
        return new User(http.post(baseURL + "notifications/leave/" + id + ".xml", true).
                asDocument().getDocumentElement(), this);
    }

    /* Block Methods */

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     *
     * @param id the ID or screen_name of the user to block
     * @return the blocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized User block(String id) throws TwitterException {
        return new User(http.post(baseURL + "blocks/create/" + id + ".xml", true).
                asDocument().getDocumentElement(), this);
    }


    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     *
     * @param id the ID or screen_name of the user to block
     * @return the unblocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized User unblock(String id) throws TwitterException {
        return new User(http.post(baseURL + "blocks/destroy/" + id + ".xml", true).
                asDocument().getDocumentElement(), this);
    }

    /* Help Methods */
/*
test  New as of April 29th, 2008!

Returns the string "ok" in the requested format with a 200 OK HTTP status code.
URL:http://twitter.com/help/test.format
Formats: xml, json
*/

    /**
     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
     *
     * @return true if the API is working
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized boolean test() throws TwitterException {
        return -1 != get(baseURL + "help/test.xml", false).
                asString().indexOf("ok");
    }

/*
downtime_schedule  New as of April 29th, 2008!

Returns the same text displayed on http://twitter.com/home when a maintenance window is scheduled, in the requested format.
URL:http://twitter.com/help/downtime_schedule.format
Formats: xml, json
*/

    /**
     * Returns the same text displayed on http://twitter.com/home when a maintenance window is scheduled, in the requested format.
     *
     * @return the schedule
     * @throws TwitterException when Twitter service or network is unavailable
     * @since twitter4j 1.0.4
     */
    public final synchronized String getDowntimeSchedule() throws TwitterException {
        return get(baseURL + "help/downtime_schedule.xml", false).asString();
    }

    private SimpleDateFormat format = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    public void setRetryCount(int retryCount) {
        http.setRetryCount(retryCount);
    }

    public void setRetryIntervalSecs(int retryIntervalSecs) {
        http.setRetryIntervalSecs(retryIntervalSecs);
    }

    @Override
    public int hashCode() {
        return http.hashCode() + this.baseURL.hashCode() + http.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Twitter) {
            Twitter that = (Twitter) obj;
            return this.http.equals(that.http)
                    && this.baseURL.equals(that.baseURL)
                    && this.http.equals(that.http);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Twitter{" +
                "http=" + http +
                ", baseURL='" + baseURL + '\'' +
                ", source='" + source + '\'' +
                ", usePostForcibly=" + usePostForcibly +
                ", format=" + format +
                '}';
    }
}
