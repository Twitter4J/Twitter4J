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
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Twitter implements java.io.Serializable {
    protected HttpClient http = null;
    private String baseURL = "http://twitter.com/";
    private String searchBaseURL = "http://search.twitter.com/";
    private String source;

    private boolean usePostForcibly = false;
    private static final int MAX_COUNT = 200;
    private static final long serialVersionUID = -7550633067620779906L;
    /*package*/ static final String VERSION = "1.1.8";

    public Twitter() {
        http = new HttpClient();
        setUserId(null);
        setPassword(null);
        setUserAgent("twitter4j http://yusuke.homeip.net/twitter4j/ /" + VERSION);
        setSource("Twitter4J");
        setClientVersion(VERSION);
        setClientURL("http://yusuke.homeip.net/twitter4j/en/twitter4j-" + VERSION + ".xml");
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
        this();
        setUserId(id);
        setPassword(password);
        this.baseURL = baseURL;
    }

    /**
     * sets the User-Agent header. System property -Dtwitter4j.http.userAgent overrides this attribute.
     * @param userAgent UserAgent
     * @since Twitter4J 1.1.8
     */
    public void setUserAgent(String userAgent){
        http.setUserAgent(System.getProperty("twitter4j.http.userAgent", userAgent));
    }

    /**
     *
     * @return UserAgent
     * @since Twitter4J 1.1.8
     */
    public String getUserAgent(){
        return http.getUserAgent();
    }

    /**
     * sets the X-Twitter-Client-Version header. System property -Dtwitter4j.clientVersion overrides this attribute.
     * @param version client version
     * @since Twitter4J 1.1.8
     */
    public void setClientVersion(String version){
        setRequestHeader("X-Twitter-Client-Version", System.getProperty("twitter4j.clientVersion", version));
    }

    /**
     *
     * @return client version
     * @since Twitter4J 1.1.8
     */
    public String getClientVersion(){
        return http.getRequestHeader("X-Twitter-Client-Version");
    }

    /**
     * sets the X-Twitter-Client-URL header. System property -Dtwitter4j.clientURL overrides this attribute.
     * @param clientURL client URL
     * @since Twitter4J 1.1.8
     */
    public void setClientURL(String clientURL){
        setRequestHeader("X-Twitter-Client-URL",System.getProperty("twitter4j.clientURL", clientURL));
    }

    /**
     *
     * @return client URL
     * @since Twitter4J 1.1.8
     */
    public String getClientURL(){
        return http.getRequestHeader("X-Twitter-Client-URL");
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
     * Sets the search base URL
     *
     * @param searchBaseURL the search base URL
     * @since Twitter4J 1.1.7
     */
    public void setSearchBaseURL(String searchBaseURL) {
        this.searchBaseURL = searchBaseURL;
    }

    /**
     * returns the search base url
     * @return search base url
     * @since Twitter4J 1.1.7
     */
    public String getSearchBaseURL(){
        return this.searchBaseURL;
    }

    /**
     * Sets the userid
     *
     * @param userId new userid
     */
    public void setUserId(String userId) {
        http.setUserId(System.getProperty("twitter4j.user", userId));
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
        http.setPassword(System.getProperty("twitter4j.password", password));
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
     * Enables use of HTTP proxy
     *
     * @param proxyHost proxy host, can be overridden system property -Dtwitter4j.http.proxyHost , -Dhttp.proxyHost
     * @param proxyPort proxy port, can be overridden system property -Dtwitter4j.http.proxyPort , -Dhttp.proxyPort
     * @since Twitter4J 1.1.6
     */
    public void setHttpProxy(String proxyHost, int proxyPort) {
        http.setProxyHost(proxyHost);
        http.setProxyPort(proxyPort);
    }

    /**
     * Adds authentication on HTTP proxy
     *
     * @param proxyUser proxy user, can be overridden system property -Dtwitter4j.http.proxyUser
     * @param proxyPass proxy password, can be overridden system property -Dtwitter4j.http.proxyPassword
     * @since Twitter4J 1.1.6
     */
    public void setHttpProxyAuth(String proxyUser, String proxyPass) {
        http.setProxyAuthUser(proxyUser);
        http.setProxyAuthPassword(proxyPass);
    }

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the Twitter API.
     * System property -Dtwitter4j.http.connectionTimeout overrides this attribute.
     *
     * @param connectionTimeout an int that specifies the connect timeout value in milliseconds
     * @since Twitter4J 1.1.6
     */
    public void setHttpConnectionTimeout(int connectionTimeout) {
        http.setConnectionTimeout(connectionTimeout);
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds.
     *
     * @param readTimeout an int that specifies the timeout value to be used in milliseconds
     * @since Twitter4J 1.1.6
     */
    public void setHttpReadTimeout(int readTimeout) {
        http.setReadTimeout(readTimeout);
    }

    /**
     * Sets X-Twitter-Client http header and the source parameter that will be passed by updating methods. System property -Dtwitter4j.source overrides this attribute.
     * System property -Dtwitter4j.source overrides this attribute.
     *
     * @param source the new source
     * @see <a href='http://apiwiki.twitter.com/FAQ#HowdoIget“fromMyApp”appendedtoupdatessentfrommyAPIapplication'>How do I get "from [MyApp]" appended to updates sent from my API application?</a>
     * @see <a href="http://twitter.com/help/request_source">Twitter - Request a link to your application</a>
     */
    public void setSource(String source) {
        this.source = System.getProperty("twitter4j.source", source);
        setRequestHeader("X-Twitter-Client", this.source);
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

    protected Response get(String url, String name1, String value1, boolean authenticate) throws TwitterException {
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

    protected Response get(String url, String name1, String value1, String name2, String value2, boolean authenticate) throws TwitterException {
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
    protected Response get(String url, PostParameter[] params, boolean authenticate) throws TwitterException {
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
    public synchronized List<Status> getPublicTimeline() throws
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
    public synchronized List<Status> getPublicTimeline(int sinceID) throws
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

    public synchronized List<Status> getFriendsTimeline() throws
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
    public synchronized List<Status> getFriendsTimelineByPage(int page) throws
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

    public synchronized List<Status> getFriendsTimeline(String id) throws
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

    public synchronized List<Status> getFriendsTimelineByPage(String id, int page) throws
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
    public synchronized List<Status> getFriendsTimeline(Date since) throws
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
    public synchronized List<Status> getFriendsTimeline(String id,
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
    public synchronized List<Status> getUserTimeline(String id, int count,
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
    public synchronized List<Status> getUserTimeline(String id, Date since) throws TwitterException {
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
    public synchronized List<Status> getUserTimeline(String id, int count) throws
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
    public synchronized List<Status> getUserTimeline(int count, Date since) throws TwitterException {
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
    public synchronized List<Status> getUserTimeline(String id) throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/user_timeline/" + id + ".xml", true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     *
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<Status> getUserTimeline() throws
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

    public synchronized Status show(int id) throws TwitterException {
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

    public synchronized Status show(long id) throws TwitterException {
        return new Status(get(baseURL + "statuses/show/" + id + ".xml", false).asDocument().getDocumentElement(), this);
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     *
     * @param status the text of your status update
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#update">Twitter API &gt; Status Methods &gt; update</a>
     */
    public Status update(String status) throws TwitterException {
        if (status.length() > 160) {
            status = status.substring(0, 160);
        }
        return new Status(http.post(baseURL + "statuses/update.xml",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)}, true).asDocument().getDocumentElement(), this);
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     *
     * @param status            the text of your status update
     * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#update">Twitter API &gt; Status Methods &gt; update</a>
     */
    public Status update(String status, long inReplyToStatusId) throws TwitterException {
        if (status.length() > 160) {
            status = status.substring(0, 160);
        }
        return new Status(http.post(baseURL + "statuses/update.xml",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("in_reply_to_status_id", String.valueOf(inReplyToStatusId)), new PostParameter("source", source)}, true).asDocument().getDocumentElement(), this);
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     *
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<Status> getReplies() throws TwitterException {
        return Status.constructStatuses(get(baseURL + "statuses/replies.xml", true).asDocument(), this);
    }

    /**
     * Returns the most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     *
     * @param page the number of page
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<Status> getRepliesByPage(int page) throws TwitterException {
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
    public synchronized List<User> getFriends() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml", true).asDocument(), this);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     *
     * @param page number of page
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<User> getFriends(int page) throws TwitterException {
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
    public synchronized List<User> getFriends(String id) throws TwitterException {
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
    public synchronized List<User> getFriends(String id, int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/friends.xml",
                "id", id, "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<User> getFollowers() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers.xml", true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @param page Retrieves the next 100 followers.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.1.0
     */
    public synchronized List<User> getFollowers(int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers.xml", "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @param id The ID or screen name of the user for whom to request a list of followers.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.1.0
     */
    public synchronized List<User> getFollowers(String id) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers/" + id + ".xml", true).asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     *
     * @param id   The ID or screen name of the user for whom to request a list of followers.
     * @param page Retrieves the next 100 followers.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.1.0
     */
    public synchronized List<User> getFollowers(String id, int page) throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/followers/" + id + ".xml", "page", String.valueOf(page), true).asDocument(), this);
    }

    /**
     * Returns a list of the users currently featured on the site with their current statuses inline.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<User> getFeatured() throws TwitterException {
        return User.constructUsers(get(baseURL + "statuses/featured.xml", true).asDocument(), this);
    }

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     *
     * @param id the ID or screen name of the user for whom to request the detail
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized UserWithStatus getUserDetail(String id) throws TwitterException {
        return new UserWithStatus(get(baseURL + "users/show/" + id + ".xml", true).asDocument().getDocumentElement(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<DirectMessage> getDirectMessages() throws TwitterException {
        return DirectMessage.constructDirectMessages(get(baseURL + "direct_messages.xml", true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     *
     * @param page the number of page
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<DirectMessage> getDirectMessagesByPage(int page) throws TwitterException {
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
    public synchronized List<DirectMessage> getDirectMessages(int sinceId) throws
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
    public synchronized List<DirectMessage> getDirectMessages(Date since) throws
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
    public synchronized List<DirectMessage> getSentDirectMessages() throws
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
    public synchronized List<DirectMessage> getSentDirectMessages(Date since) throws
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
    public synchronized List<DirectMessage> getSentDirectMessages(int sinceId) throws
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

    public synchronized DirectMessage sendDirectMessage(String id,
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
    public synchronized DirectMessage deleteDirectMessage(int id) throws
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

    public synchronized User create(String id) throws TwitterException {
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
    public synchronized User destroy(String id) throws TwitterException {
        return new User(http.post(baseURL + "friendships/destroy/" + id + ".xml", new PostParameter[0], true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Tests if a friendship exists between two users.
     *
     * @param user_a The ID or screen_name of the first user to test friendship for.
     * @param user_b The ID or screen_name of the second user to test friendship for.
     * @return if a friendship exists between two users.
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized boolean exists(String user_a, String user_b) throws TwitterException {
        return get(baseURL + "friendships/exists.xml", "user_a", user_a, "user_b", user_b, true).
                asString().contains("true");
    }

    /**
     * Returns true if authentication was successful.  Use this method to test if supplied user credentials are valid with minimal overhead.
     *
     * @return success
     */
    public synchronized boolean verifyCredentials() {
        try {
            return get(baseURL + "account/verify_credentials.xml", true).getStatusCode() == 200;
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
     * @since Twitter4J 1.0.4
     */
    public synchronized User updateLocation(String location) throws TwitterException {
        return new User(http.post(baseURL + "account/update_location.xml", new PostParameter[]{new PostParameter("location", location)}, true).
                asDocument().getDocumentElement(), this);
    }

    /**
     * Returns the remaining number of API requests available to the requesting user before the API limit is reached for the current hour. Calls to rate_limit_status do not count against the rate limit.  If authentication credentials are provided, the rate limit status for the authenticating user is returned.  Otherwise, the rate limit status for the requester's IP address is returned.<br>
     * See <a href="http://apiwiki.twitter.com/REST%20API%20Documentation#ratelimitstatus">Twitter REST API Documentation &gt; Account Methods &gt; rate_limit_status</a> for detail.
     *
     * @return the rate limit status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.1.4
     */
    public synchronized RateLimitStatus rateLimitStatus() throws TwitterException {
        return new RateLimitStatus(http.get(baseURL + "account/rate_limit_status.xml", null != getUserId() && null != getPassword()).
                asDocument().getDocumentElement());
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
     * @since Twitter4J 1.0.4
     */
    public synchronized User updateDeliverlyDevice(Device device) throws TwitterException {
        return new User(http.post(baseURL + "account/update_delivery_device.xml", new PostParameter[]{new PostParameter("device", device.DEVICE)}, true).
                asDocument().getDocumentElement(), this);
    }


    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public synchronized List<Status> favorites() throws TwitterException {
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
    public synchronized List<Status> favorites(int page) throws TwitterException {
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
    public synchronized List<Status> favorites(String id) throws TwitterException {
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
    public synchronized List<Status> favorites(String id, int page) throws TwitterException {
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
    public synchronized Status createFavorite(long id) throws TwitterException {
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
    public synchronized Status destroyFavorite(long id) throws TwitterException {
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
    public synchronized User follow(String id) throws TwitterException {
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
    public synchronized User leave(String id) throws TwitterException {
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
     * @since Twitter4J 1.0.4
     */
    public synchronized User block(String id) throws TwitterException {
        return new User(http.post(baseURL + "blocks/create/" + id + ".xml", true).
                asDocument().getDocumentElement(), this);
    }


    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     *
     * @param id the ID or screen_name of the user to block
     * @return the unblocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.0.4
     */
    public synchronized User unblock(String id) throws TwitterException {
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
     * @since Twitter4J 1.0.4
     */
    public synchronized boolean test() throws TwitterException {
        return -1 != get(baseURL + "help/test.xml", false).
                asString().indexOf("ok");
    }

    /**
     * Returns extended information of the authenticated user.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.<br>
     * The call Twitter.getAuthenticatedUser() is equivalent to the call:<br>
     * twitter.getUserDetail(twitter.getUserId());
     *
     * @return UserWithStatus
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.1.3
     */
    public synchronized UserWithStatus getAuthenticatedUser() throws TwitterException {
        if (null == getUserId()) {
            throw new IllegalStateException("User Id not specified.");
        }
        return getUserDetail(getUserId());
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
     * @since Twitter4J 1.0.4
     */
    public synchronized String getDowntimeSchedule() throws TwitterException {
        return get(baseURL + "help/downtime_schedule.xml", false).asString();
    }

    /**
     * @param query - the search condition
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.1.7
     * @see <a href="http://apiwiki.twitter.com/Search-API-Documentation">Twitter API / Search API Documentation</a>
     * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
     */

    public synchronized QueryResult search(Query query) throws TwitterException {
        return new QueryResult(get(searchBaseURL + "search.json", query.asPostParameters(), false).asJSONObject(), this);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Twitter twitter = (Twitter) o;

        if (usePostForcibly != twitter.usePostForcibly) return false;
        if (!baseURL.equals(twitter.baseURL)) return false;
        if (!format.equals(twitter.format)) return false;
        if (!http.equals(twitter.http)) return false;
        if (!searchBaseURL.equals(twitter.searchBaseURL)) return false;
        if (!source.equals(twitter.source)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = http.hashCode();
        result = 31 * result + baseURL.hashCode();
        result = 31 * result + searchBaseURL.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + (usePostForcibly ? 1 : 0);
        result = 31 * result + format.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Twitter{" +
                "http=" + http +
                ", baseURL='" + baseURL + '\'' +
                ", searchBaseURL='" + searchBaseURL + '\'' +
                ", source='" + source + '\'' +
                ", usePostForcibly=" + usePostForcibly +
                ", format=" + format +
                '}';
    }
}
