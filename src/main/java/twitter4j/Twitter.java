package twitter4j;

import twitter4j.http.HttpClient;
import twitter4j.http.PostParameter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public Twitter() {
        http = new HttpClient();
        setRequestHeader("X-Twitter-Client", "Twitter4J");
        setRequestHeader("X-Twitter-Client-Version", "1.0.4");
        setRequestHeader("X-Twitter-Client-URL",
                "http://yusuke.homeip.net/twitter4j/en/twitter4j-1.0.4.xml");
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
     * sets the base URL
     *
     * @param baseURL String the base URL
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * returns the base URL
     *
     * @return the base URL
     */
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * sets the user id
     *
     * @param userId String
     */
    public void setUserId(String userId) {
        http.setUserId(userId);
    }

    /**
     * returns authenticating user id
     *
     * @return user id
     */
    public String getUserId() {
        return http.getUserId();
    }

    /**
     * sets the password
     *
     * @param password String
     */
    public void setPassword(String password) {
        http.setPassword(password);
    }

    /**
     * returns authenticating password
     *
     * @return password
     */
    public String getPassword() {
        return http.getPassword();
    }

    /**
     * sets the source parameter that will be passed by updating methods
     *
     * @param source String
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * returns the source
     *
     * @return source String
     */
    public String getSource() {
        return this.source;
    }

    /**
     * sets the request header name/value combination
     * see Twitter Fan Wiki for detail.
     * http://twitter.pbwiki.com/API-Docs#RequestHeaders
     *
     * @param name  String
     * @param value String
     */
    public void setRequestHeader(String name, String value) {
        http.setRequestHeader(name, value);
    }

    /**
     * set true to force using POST method communicating to the server
     *
     * @param forceUsePost
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
     * @param url
     * @param authenticate
     * @return
     * @throws TwitterException
     */

//    private twitter4j.http.Response get(String url,boolean authenticate) throws TwitterException{
//        return get(url,new PostParameter[0],authenticate);
//    }
    /**
     * issues an HTTP GET request. POST method will be used instead in case forceUsePost is set true.
     * @param url
     * @param authenticate
     * @return
     * @throws TwitterException
     */

    private twitter4j.http.Response get(String url,String name1,String value1,boolean authenticate) throws TwitterException{
        return get(url,new PostParameter[]{new PostParameter(name1,value1)},authenticate);
    }
    /**
     * issues an HTTP GET request. POST method will be used instead in case forceUsePost is set true.
     * @param url
     * @param authenticate
     * @return
     * @throws TwitterException
     */

    private twitter4j.http.Response get(String url,String name1,String value1,String name2,String value2,boolean authenticate) throws TwitterException{
        return get(url,new PostParameter[]{new PostParameter(name1,value1),new PostParameter(name2,value2)},authenticate);
    }
    /**
     * issues an HTTP GET request. POST method will be used instead in case forceUsePost is set true.
     * @param url
     * @param authenticate
     * @return
     * @throws TwitterException
     */

    private twitter4j.http.Response get(String url,String name1,String value1,String name2,String value2,String name3,String value3,boolean authenticate) throws TwitterException{
        return get(url,new PostParameter[]{new PostParameter(name1,value1),new PostParameter(name2,value2),new PostParameter(name3,value3)},authenticate);
    }

    /**
     * issues an HTTP GET request. POST method will be used instead in case forceUsePost is set true.
     *
     * @param url
     * @param authenticate
     * @return
     * @throws TwitterException
     */

    private twitter4j.http.Response get(String url, PostParameter[] params, boolean authenticate) throws TwitterException {
        if (usePostForcibly) {
            return http.post(url, params, authenticate);
        } else {
            if (null != params && params.length > 0) {
                url += "?" + HttpClient.encodeParameters(params);
            }
            return http.get(url, authenticate);
        }
    }

    /**
     * Returns the 20 most recent statuses from non-protected users who have set a custom user icon.
     *
     * @return List<Status> List of statuses of the Public Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getPublicTimeline() throws
            TwitterException {
        return Status.constructStatuses(get(baseURL +
                "statuses/public_timeline.xml", new PostParameter[0], false).
                asDocument(), this);
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     *
     * @param sinceID String
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @deprecated argument should be always numeric. Use getPublicTimeline(int sinceID) instead
     */
    public final synchronized List<Status> getPublicTimeline(String sinceID) throws
            TwitterException {
        return Status.constructStatuses(get(baseURL +
                "statuses/public_timeline.xml", new PostParameter[]{new PostParameter("since_id", sinceID)}, false).
                asDocument(), this);
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     *
     * @param sinceID int
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getPublicTimeline(int sinceID) throws
            TwitterException {
        return Status.constructStatuses(get(baseURL +
                "statuses/public_timeline.xml", new PostParameter[]{new PostParameter("since_id", String.valueOf(sinceID))}, false).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
     * It's also possible to request another user's friends_timeline via the id parameter below.
     *
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getFriendsTimeline() throws
            TwitterException {
        return Status.constructStatuses(get(baseURL +
                "statuses/friends_timeline.xml", new PostParameter[0], true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @param page int
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getFriendsTimelineByPage(int page) throws
            TwitterException {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline.xml",
                new PostParameter[]{new PostParameter("page", String.valueOf(page))}, true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id String user ID
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getFriendsTimeline(String id) throws
            TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline/" + id + ".xml", new PostParameter[0],
                true).asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id   String user ID
     * @param page int
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getFriendsTimelineByPage(String id, int page) throws
            TwitterException {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline/" + id + ".xml",
                new PostParameter[]{new PostParameter("page", String.valueOf(page))}, true).asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @param since Date
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getFriendsTimeline(Date since) throws
            TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline.xml",
                new PostParameter[]{new PostParameter("since", formatDate(since))}, true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id    String user ID
     * @param since Date
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getFriendsTimeline(String id,
                                                              Date since) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/friends_timeline/" + id + ".xml",
                new PostParameter[]{new PostParameter("since", formatDate(since))}, true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id    String
     * @param count int
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id, int count,
                                                           Date since) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml",
                new PostParameter[]{new PostParameter("since", formatDate(since)),
                        new PostParameter("count", String.valueOf(count))}, true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id    String
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id, Date since) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml",
                new PostParameter[]{new PostParameter("since", formatDate(since))}, true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id    String
     * @param count int
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id, int count) throws
            TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml",
                new PostParameter[]{new PostParameter("count", String.valueOf(count))}, true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @param count int
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(int count, Date since) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline.xml",
                new PostParameter[]{new PostParameter("since", formatDate(since)),
                        new PostParameter("count", String.valueOf(count))}, true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     *
     * @param id String
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml", new PostParameter[0], true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline() throws
            TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline.xml", new PostParameter[0], true).
                asDocument(), this);
    }

    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     *
     * @param id int
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized Status show(int id) throws TwitterException {
        return new Status(get(baseURL + "statuses/show/" + id + ".xml", new PostParameter[0], true).asDocument().getDocumentElement(), this);
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     *
     * @param status String
     * @return Status
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
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getReplies() throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/replies.xml", new PostParameter[0], true).asDocument(), this);
    }

    /**
     * Returns the most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     *
     * @param page int
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getRepliesByPage(int page) throws TwitterException {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return Status.constructStatuses(get(baseURL + "statuses/replies.xml",
                new PostParameter[]{new PostParameter("page", String.valueOf(page))}, true).asDocument(), this);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml", new PostParameter[0], true).asDocument(), this);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     *
     * @param page
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends(int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml", new PostParameter[]{new PostParameter("page",
                String.valueOf(page))}, true).asDocument(), this);
    }

    /**
     * Returns the user's friends, each with current status inline.
     *
     * @param id String
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends(String id) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml",
                new PostParameter[]{new PostParameter("id", id)}, true).asDocument(), this);
    }

    /**
     * Returns the user's friends, each with current status inline.
     *
     * @param id   String
     * @param page
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends(String id, int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml",
                new PostParameter[]{new PostParameter("id", id), new PostParameter("page", String.valueOf(page))}, true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFollowers() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers.xml", new PostParameter[0], true).asDocument(), this);
    }

    /**
     * Returns a list of the users currently featured on the site with their current statuses inline.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFeatured() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/featured.xml", new PostParameter[0], true).asDocument(), this);
    }

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     *
     * @param id String
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized UserWithStatus getUserDetail(String id) throws TwitterException {
        return new UserWithStatus(get(baseURL + "users/show/" + id + ".xml", new PostParameter[0], true).asDocument().getDocumentElement(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessages() throws TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL + "direct_messages.xml", new PostParameter[0], true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @param page int
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessagesByPage(int page) throws TwitterException {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return DirectMessage.constructDirectMessages(get(baseURL + "direct_messages.xml",
                new PostParameter[]{new PostParameter("page", String.valueOf(page))}, true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @param sinceId int
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessages(int sinceId) throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages.xml", new PostParameter[]{new PostParameter("since", String.valueOf(sinceId))}, true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessages(Date since) throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages.xml",new PostParameter[]{new PostParameter("since", formatDate(since))}, true).asDocument(), this);
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
                "direct_messages/sent.xml",new PostParameter[0], true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     *
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getSentDirectMessages(Date since) throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages/sent.xml","since",formatDate(since), true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     *
     * @param sinceId int
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getSentDirectMessages(int sinceId) throws
            TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL +
                "direct_messages/sent.xml","since_id" , String.valueOf(sinceId), true).asDocument(), this);
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimed if the length of the text is exceeding 140 characters.
     *
     * @param id   String
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
     * @param id int
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized DirectMessage deleteDirectMessage(int id) throws
            TwitterException {
        return new DirectMessage(get(baseURL +
                "direct_messages/destroy/" + id + ".xml",new PostParameter[0], true).asDocument().getDocumentElement(), this);
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     *
     * @param id String
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized User create(String id) throws TwitterException {
        return new User(get(baseURL + "friendships/create/" + id + ".xml",new PostParameter[0], true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Discontinues friendship with the user specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     *
     * @param id String
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized User destroy(String id) throws TwitterException {
        return new User(get(baseURL + "friendships/destroy/" + id + ".xml",new PostParameter[0], true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Returns true if authentication was successful.  Use this method to test if supplied user credentials are valid with minimal overhead.
     *
     * @return success
     */
    public final synchronized boolean verifyCredentials() {
        try {
            return get(baseURL + "account/verify_credentials.xml", new PostParameter[0],true).asString().contains("true");
        } catch (TwitterException te) {
            return false;
        }
    }

    /**
     * Returns 80 statuses per page for the authenticating user, ordered by descending date of posting. Use this method to rapidly export your archive of statuses.
     *
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> archive() throws TwitterException {
        return Status.constructStatuses(get(baseURL + "account/archive.xml",new PostParameter[0], true).
                asDocument(), this);
    }

    /**
     * Returns 80 statuses per page for the authenticating user, ordered by descending date of posting. Use this method to rapidly export your archive of statuses.
     *
     * @param page
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> archive(int page) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "account/archive.xml","page",String.valueOf(page), true).
                asDocument(), this);
    }

    /**
     * Update the location
     *
     * @param location
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized User updateLocation(String location) throws TwitterException {
        return new User(get(baseURL + "account/update_location.xml","location",location, true).
                asDocument().getDocumentElement(), this);
    }


    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> favorites() throws TwitterException {
        return Status.constructStatuses(get(baseURL + "favorites.xml",new PostParameter[0], true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @param page
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> favorites(int page) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "favorites.xml","page",String.valueOf(page), true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> favorites(String id) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "favorites/" + id + ".xml",new PostParameter[0], true).
                asDocument(), this);
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @param page
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> favorites(String id, int page) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "favorites/" + id + ".xml","page",String.valueOf(page), true).
                asDocument(), this);
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     *
     * @param id
     * @return Status
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized Status createFavorite(int id) throws TwitterException {
        return new Status(get(baseURL + "favorites/create/" + id + ".xml",new PostParameter[0], true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Un-favorites the status specified in the ID parameter as the authenticating user.  Returns the un-favorited status in the requested format when successful.
     *
     * @param id
     * @return Status
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized Status destroyFavorite(int id) throws TwitterException {
        return new Status(get(baseURL + "favorites/destroy/" + id + ".xml", new PostParameter[0],true).
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
        return new User(get(baseURL + "notifications/follow/" + id + ".xml",new PostParameter[0], true).
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
        return new User(get(baseURL + "notifications/leave/" + id + ".xml",new PostParameter[0], true).
                asDocument().getDocumentElement(), this);
    }


    private SimpleDateFormat format = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    private String formatDate(Date date) {
        try {
            return URLEncoder.encode(format.format(date), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return URLEncoder.encode(format.format(date));
        }
    }

    public void setRetryCount(int retryCount) {
        http.setRetryCount(retryCount);
    }

    public void setRetryIntervalSecs(int retryIntervalSecs) {
        http.setRetryIntervalSecs(retryIntervalSecs);
    }

    @Override
    public int hashCode() {
        return http.hashCode() + this.baseURL.hashCode();
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
                    && this.baseURL.equals(that.baseURL);
        }
        return false;
    }
}
