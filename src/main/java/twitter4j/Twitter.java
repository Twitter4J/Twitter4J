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

import twitter4j.http.AccessToken;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpResponseListener;
import twitter4j.http.PostParameter;
import twitter4j.http.RequestToken;
import twitter4j.http.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A java reporesentation of the <a href="http://apiwiki.twitter.com/">Twitter API</a>
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Twitter extends TwitterSupport implements java.io.Serializable {
    private static final long serialVersionUID = -1486360080128882436L;

    public Twitter() {
        super();
        HttpResponseListener httpResponseListener = new MyHttpResponseListener();
        http.addHttpResponseListener(httpResponseListener);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        http.setRequestTokenURL(Configuration.getScheme() + "twitter.com/oauth/request_token");
        http.setAuthorizationURL(Configuration.getScheme() + "twitter.com/oauth/authorize");
        http.setAccessTokenURL(Configuration.getScheme() + "twitter.com/oauth/access_token");
    }

    public Twitter(String id, String password) {
        this();
        setUserId(id);
        setPassword(password);
    }

    /**
     * Returns the base URL
     *
     * @return the base URL
     */
    protected String getBaseURL() {
        return USE_SSL ? "https://api.twitter.com/1/" : "http://api.twitter.com/1/";
    }


    /**
     * Returns the search base url
     * @return search base url
     * @since Twitter4J 1.1.7
     */
    protected String getSearchBaseURL(){
        return USE_SSL ? "https://search.twitter.com/" : "http://search.twitter.com/";
    }

    /**
     *
     * @param consumerKey OAuth consumer key
     * @param consumerSecret OAuth consumer secret
     * @since Twitter 2.0.0
     */
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
        this.http.setOAuthConsumer(consumerKey, consumerSecret);
    }

    /**
     * Retrieves a request token
     * @return generated request token.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter 2.0.0
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ">Twitter API Wiki - OAuth FAQ</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step1">OAuth Core 1.0 - 6.1.  Obtaining an Unauthorized Request Token</a>
     */
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return http.getOAuthRequestToken();
    }

    public RequestToken getOAuthRequestToken(String callback_url) throws TwitterException {
      return http.getOauthRequestToken(callback_url);
    }

    /**
     * Retrieves an access token assosiated with the supplied request token and sets userId.
     * @param requestToken the request token
     * @return access token associsted with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        AccessToken accessToken = http.getOAuthAccessToken(requestToken);
        setUserId(accessToken.getScreenName());
        return accessToken;
    }

    /**
     * Retrieves an access token assosiated with the supplied request token and sets userId.
     * @param requestToken the request token
     * @param oauth_verifier oauth_verifier or pin
     * @return access token associsted with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauth_verifier) throws TwitterException {
        AccessToken accessToken = http.getOAuthAccessToken(requestToken, oauth_verifier);
        setUserId(accessToken.getScreenName());
        return accessToken;
    }

    /**
     * Retrieves an access token assosiated with the supplied request token and sets userId.
     * @param token request token
     * @param tokenSecret request token secret
     * @return access token associsted with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.1
     */
    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws TwitterException {
        AccessToken accessToken = http.getOAuthAccessToken(token, tokenSecret);
        setUserId(accessToken.getScreenName());
        return accessToken;
    }

    /**
     * Retrieves an access token assosiated with the supplied request token.
     * @param token request token
     * @param tokenSecret request token secret
     * @param pin pin
     * @return access token associsted with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.8
     */
    public synchronized AccessToken getOAuthAccessToken(String token
            , String tokenSecret, String pin) throws TwitterException {
        return http.getOAuthAccessToken(token, tokenSecret, pin);
    }

    /**
     * Sets the access token
     * @param accessToken accessToken
     * @since Twitter 2.0.0
     */
    public void setOAuthAccessToken(AccessToken accessToken){
        this.http.setOAuthAccessToken(accessToken);
    }

    /**
     * Sets the access token
     * @param token token
     * @param tokenSecret token secret
     * @since Twitter 2.0.0
     */
    public void setOAuthAccessToken(String token, String tokenSecret) {
        setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }


    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    protected Response get(String url, boolean authenticate) throws TwitterException {
        return get(url, null, authenticate);
    }

    /**
     * Issues an HTTP GET request.
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
     * Issues an HTTP GET request.
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
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, boolean authenticate) throws TwitterException {
        if (null != params && params.length > 0) {
            url += "?" + HttpClient.encodeParameters(params);
        }
        return http.get(url, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param pagingParams controls pagination
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, List<PostParameter> pagingParams, boolean authenticate) throws TwitterException {
        if (null != params) {
            if (null != pagingParams) {
                for(PostParameter param : params){
                    pagingParams.add(param);
                }
                return get(url, pagingParams.toArray(new PostParameter[pagingParams.size()]), authenticate);
            } else {
                return get(url, params, authenticate);
            }
        } else if (null != pagingParams) {
            return get(url, pagingParams.toArray(new PostParameter[pagingParams.size()]), authenticate);
        }else{
            return get(url, authenticate);
        }
    }

    class MyHttpResponseListener implements HttpResponseListener, java.io.Serializable {
        private static final long serialVersionUID = 5385389730784875997L;

        public void httpResponseReceived(HttpResponseEvent event) {
            Response res = event.getResponse();
            String limit = res.getResponseHeader("X-RateLimit-Limit");
            if (null != limit) {
                int rateLimitLimit = 0, rateLimitRemaining = 0;
                long rateLimitReset = 01;
                rateLimitLimit = Integer.parseInt(limit);
                String remaining = res.getResponseHeader("X-RateLimit-Remaining");
                if (null != remaining) {
                    rateLimitRemaining = Integer.parseInt(remaining);
                    String reset = res.getResponseHeader("X-RateLimit-Reset");
                    if (null != reset) {
                        rateLimitReset = Long.parseLong(reset);
                        RateLimitStatus rateLimitStatus = new RateLimitStatus(rateLimitLimit,
                                rateLimitRemaining, rateLimitReset);
                        if (event.isAuthenticated()) {
                            fireRateLimitStatusListenerUpdate(accountRateLimitStatusListeners, rateLimitStatus);
                        } else {
                            fireRateLimitStatusListenerUpdate(ipRateLimitStatusListeners, rateLimitStatus);
                        }
                    }
                }
            }

        }

        private void fireRateLimitStatusListenerUpdate(List<RateLimitStatusListener> listeners,RateLimitStatus status){
            for(RateLimitStatusListener listener : listeners){
                listener.rateLimitStatusUpdated(status);
            }
        }

        public boolean equals(Object that){
            return that instanceof MyHttpResponseListener;
        }
    };

    /**
     * Returns tweets that match a specified query.
     * <br>This method calls http://search.twitter.com/search
     * @param query - the search condition
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.1.7
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-search">Twitter API Wiki / Twitter Search API Method: search</a>
     */
    public QueryResult search(Query query) throws TwitterException {
        try{
        return new QueryResult(get(getSearchBaseURL() + "search.json", query.asPostParameters(), false));
        }catch(TwitterException te){
            if(404 == te.getStatusCode()){
                return new QueryResult(query);
            }else{
                throw te;
            }
        }
    }
    
    /**
     * Returns the top ten topics that are currently trending on Twitter.  The response includes the time of the request, the name of each trend, and the url to the <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
     * <br>This method calls http://search.twitter.com/trends
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
     */
    public Trends getTrends() throws TwitterException {
        return Trends.createTrends(get(getSearchBaseURL() + "trends.json", false));
    }

    /**
     * Returns the current top 10 trending topics on Twitter.  The response includes the time of the request, the name of each trending topic, and query used on <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
     * <br>This method calls http://search.twitter.com/trends/current
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
     */
    public Trends getCurrentTrends() throws TwitterException {
        return Trends.createTrendsList(get(getSearchBaseURL() + "trends/current.json"
                , false)).get(0);
    }

    /**
     * Returns the current top 10 trending topics on Twitter.  The response includes the time of the request, the name of each trending topic, and query used on <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
     * <br>This method calls http://search.twitter.com/trends/current
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
     */
    public Trends getCurrentTrends(boolean excludeHashTags) throws TwitterException {
        return Trends.createTrendsList(get(getSearchBaseURL() + "trends/current.json"
                + (excludeHashTags ? "?exclude=hashtags" : ""), false)).get(0);
    }


    /**
     * Returns the top 20 trending topics for each hour in a given day.
     * <br>This method calls http://search.twitter.com/trends/daily
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-daily">Twitter Search API Method: trends daily</a>
     */
    public List<Trends> getDailyTrends() throws TwitterException {
        return Trends.createTrendsList(get(getSearchBaseURL() + "trends/daily.json", false));
    }

    /**
     * Returns the top 20 trending topics for each hour in a given day.
     * <br>This method calls http://search.twitter.com/trends/daily
     * @param date Permits specifying a start date for the report.
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-daily">Twitter Search API Method: trends daily</a>
     */
    public List<Trends> getDailyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return Trends.createTrendsList(get(getSearchBaseURL()
                + "trends/daily.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : ""), false));
    }

    private String toDateStr(Date date){
        if(null == date){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * Returns the top 30 trending topics for each day in a given week.
     * <br>This method calls http://search.twitter.com/trends/weekly
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-weekly">Twitter Search API Method: trends weekly</a>
     */
    public List<Trends> getWeeklyTrends() throws TwitterException {
        return Trends.createTrendsList(get(getSearchBaseURL()
                + "trends/weekly.json", false));
    }

    /**
     * Returns the top 30 trending topics for each day in a given week.
     * <br>This method calls http://search.twitter.com/trends/weekly
     * @param date Permits specifying a start date for the report.
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @return the result
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-weekly">Twitter Search API Method: trends weekly</a>
     */
    public List<Trends> getWeeklyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return Trends.createTrendsList(get(getSearchBaseURL()
                + "trends/weekly.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : ""), false));
    }

    /* Status Methods */

    /**
     * Returns the 20 most recent statuses from non-protected users who have set a custom user icon. <a href="http://groups.google.com/group/twitter-development-talk/browse_thread/thread/f881564598a947a7#">The public timeline is cached for 60 seconds</a> so requesting it more often than that is a waste of resources.
     * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
     *
     * @return list of statuses of the Public Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-public_timeline">Twitter API Wiki / Twitter REST API Method: statuses public_timeline</a>
     */
    public ResponseList<Status> getPublicTimeline() throws
            TwitterException {
        return Status.createStatuseList(get(getBaseURL() +
                "statuses/public_timeline.json", false));
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
     *
     * @param sinceID returns only public statuses with an ID greater than (that is, more recent than) the specified ID
     * @return the 20 most recent statuses
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-public_timeline">Twitter API Wiki / Twitter REST API Method: statuses public_timeline</a>
     */
    public ResponseList<Status> getPublicTimeline(long sinceID) throws
            TwitterException {
        return Status.createStatuseList(get(getBaseURL() +
                "statuses/public_timeline.json", null, new Paging((long) sinceID).asPostParameterList(Paging.S)
                , false));
    }

    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
     * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
     *
     * @return list of the home Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
     * @since Twitter4J 2.0.10
     */
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/home_timeline.json", true));
    }


    /**
     * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
     * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the home Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
     * @since Twitter4J 2.0.10
     */
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/home_timeline.json", null, paging.asPostParameterList(), true));
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating1 user and that user's friends.
     * It's also possible to request another user's friends_timeline via the id parameter below.
     * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
     *
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     */
    public ResponseList<Status> getFriendsTimeline() throws
            TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/friends_timeline.json", true));
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
     * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     */
    public ResponseList<Status> getFriendsTimeline(Paging paging) throws
            TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/friends_timeline.json",null, paging.asPostParameterList(), true));
    }


    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified screen name.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline.json
     *
     * @param screenName specifies the screen name of the user for whom to return the user_timeline
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the user Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     */
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return Status.createStatuseList(get(getBaseURL()
                + "statuses/user_timeline.json",
                new PostParameter[]{new PostParameter("screen_name",screenName)}
                , paging.asPostParameterList(), http.isAuthenticationEnabled()));
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified screen name.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline.json
     *
     * @param userId specifies the ID of the user for whom to return the user_timeline
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of the user Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     */
    public ResponseList<Status> getUserTimeline(int userId, Paging paging)
            throws TwitterException {
        return Status.createStatuseList(get(getBaseURL()
                + "statuses/user_timeline.json",
                new PostParameter[]{new PostParameter("user_id", userId)}
                , paging.asPostParameterList(), http.isAuthenticationEnabled()));
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified userid.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param screenName specifies the screen name of the user for whom to return the user_timeline
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     */
    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, new Paging());
    }
    
    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified userid.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param user_id specifies the ID of the user for whom to return the user_timeline
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     * @since Twitter4J 2.1.0
     */
    public ResponseList<Status> getUserTimeline(int user_id) throws TwitterException {
        return getUserTimeline(user_id, new Paging());
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     */
    public ResponseList<Status> getUserTimeline() throws
            TwitterException {
        return getUserTimeline(new Paging());
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
     * @since Twitter4J 2.0.1
     */
    public ResponseList<Status> getUserTimeline(Paging paging) throws
            TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/user_timeline.json"
                , null, paging.asPostParameterList(), true));
    }

    /**
     * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/mentions
     *
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
     */
    public ResponseList<Status> getMentions() throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/mentions.json",
                null, true));
    }

    /**
     * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/mentions
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent replies
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
     */
    public ResponseList<Status> getMentions(Paging paging) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/mentions.json",
                null, paging.asPostParameterList(), true));
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
     *
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
     */
    public ResponseList<Status> getRetweetedByMe() throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/retweeted_by_me.json",
                null, true));
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
     */
    public ResponseList<Status> getRetweetedByMe(Paging paging) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/retweeted_by_me.json",
                null, paging.asPostParameterList(), true));
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
     *
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
     */
    public ResponseList<Status> getRetweetedToMe() throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/retweeted_to_me.json",
                null, true));
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
     */
    public ResponseList<Status> getRetweetedToMe(Paging paging) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/retweeted_to_me.json",
                null, paging.asPostParameterList(), true));
    }

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
     *
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
     */
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/retweets_of_me.json",
                null, true));
    }

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
     */
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "statuses/retweets_of_me.json",
                null, paging.asPostParameterList(), true));
    }

    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     * <br>This method calls http://api.twitter.com/1/statuses/show
     *
     * @param id the numerical ID of the status you're trying to retrieve
     * @return a single status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0show">Twitter API Wiki / Twitter REST API Method: statuses show</a>
     */
    public Status showStatus(long id) throws TwitterException {
        return new Status(get(getBaseURL() + "statuses/show/" + id + ".json", false));
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status the text of your status update
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
     */
    public Status updateStatus(String status) throws TwitterException {
        return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)}, true));
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status the text of your status update
     * @param latitude The location's latitude that this tweet refers to.
     * @param longitude The location's longitude that this tweet refers to.
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
     */
    public Status updateStatus(String status, double latitude, double longitude) throws TwitterException {
        return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", latitude),
                        new PostParameter("long", longitude),
                        new PostParameter("source", source)}, true));
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status            the text of your status update
     * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
     */
    public Status updateStatus(String status, long inReplyToStatusId) throws TwitterException {
        return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("in_reply_to_status_id", String.valueOf(inReplyToStatusId)), new PostParameter("source", source)}, true));
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     * <br>This method calls http://api.twitter.com/1/statuses/update
     *
     * @param status            the text of your status update
     * @param inReplyToStatusId The ID of an existing status that the status to be posted is in reply to.  This implicitly sets the in_reply_to_user_id attribute of the resulting status to the user ID of the message being replied to.  Invalid/missing status IDs will be ignored.
     * @param latitude The location's latitude that this tweet refers to.
     * @param longitude The location's longitude that this tweet refers to.
     * @return the latest status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update">Twitter API Wiki / Twitter REST API Method: statuses update</a>
     */
    public Status updateStatus(String status, long inReplyToStatusId
            , double latitude, double longitude) throws TwitterException {
        return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", latitude),
                        new PostParameter("long", longitude),
                        new PostParameter("in_reply_to_status_id",
                                String.valueOf(inReplyToStatusId)),
                        new PostParameter("source", source)}, true));
    }

    /**
     * Destroys the status specified by the required ID parameter.  The authenticating user must be the author of the specified status.
     * <br>This method calls http://api.twitter.com/1/statuses/destroy
     *
     * @param statusId The ID of the status to destroy.
     * @return the deleted status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since 1.0.5
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: statuses destroy</a>
     */
    public Status destroyStatus(long statusId) throws TwitterException {
        return new Status(http.post(getBaseURL() + "statuses/destroy/" + statusId + ".json",
                new PostParameter[0], true));
    }

    /**
     * Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.
     * <br>This method calls http://api.twitter.com/1/statuses/retweet
     *
     * @param statusId The ID of the status to retweet.
     * @return the retweeted status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweet">Twitter API Wiki / Twitter REST API Method: statuses retweet</a>
     */
    public Status retweetStatus(long statusId) throws TwitterException {
        return new Status(http.post(getBaseURL() + "statuses/retweet/" + statusId + ".json",
                new PostParameter[0], true));
    }

    /**
     * Returns up to 100 of the first retweets of a given tweet.
     * <br>This method calls http://api.twitter.com/1/statuses/retweets
     *
     * @param statusId The numerical ID of the tweet you want the retweets of.
     * @return the retweets of a given tweet
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets">Twitter API Wiki / Twitter REST API Method: statuses retweets</a>
     */
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL()
                + "statuses/retweets/" + statusId + ".json", true));
    }

    /**
     * Returns extended information of a given user, specified by screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     * <br>This method calls http://api.twitter.com/1/users/show.json
     *
     * @param screenName the screen name of the user for whom to request the detail
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-users%C2%A0show">Twitter API Wiki / Twitter REST API Method: users show</a>
     */
    public User showUser(String screenName) throws TwitterException {
        return new User(get(getBaseURL() + "users/show.json?screen_name="
                + screenName , http.isAuthenticationEnabled()));
    }

    /**
     * Returns extended information of a given user, specified by ID.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     * <br>This method calls http://api.twitter.com/1/users/show
     *
     * @param userId the ID of the user for whom to request the detail
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-users%C2%A0show">Twitter API Wiki / Twitter REST API Method: users show</a>
     * @since Twitter4J 2.1.0
     */
    public User showUser(int userId) throws TwitterException {
        return new User(get(getBaseURL() + "users/show.json?user_id="
                + userId , http.isAuthenticationEnabled()));
    }

    /* User Methods */

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
     */
    public PagableResponseList<User> getFriendsStatuses() throws TwitterException {
        return getFriendsStatuses(-1l);
    }

    /**
     * Returns the user's friends, each with current status inline.<br>
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
     */
    public PagableResponseList<User> getFriendsStatuses(long cursor) throws TwitterException {
        return User.createCursorSupportUserList(get(getBaseURL()
                + "statuses/friends.json?cursor=" + cursor, null,
                 http.isAuthenticationEnabled()));
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * This method automatically provides a value of cursor=-1 to begin paging.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @param screenName the screen name of the user for whom to request a list of friends
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
     * @since Twitter4J 2.0.9
     */
    public PagableResponseList<User> getFriendsStatuses(String screenName) throws TwitterException {
        return getFriendsStatuses(screenName, -1l);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * This method automatically provides a value of cursor=-1 to begin paging.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @param userId the ID of the user for whom to request a list of friends
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
     * @since Twitter4J 2.1.0
     */
    public PagableResponseList<User> getFriendsStatuses(int userId) throws TwitterException {
        return getFriendsStatuses(userId, -1l);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @param screenName the screen name of the user for whom to request a list of friends
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
     */
    public PagableResponseList<User> getFriendsStatuses(String screenName, long cursor) throws TwitterException {
        return User.createCursorSupportUserList(get(getBaseURL()
                + "statuses/friends.json?screen_name=" + screenName + "&cursor=" + cursor
                , null, false));
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * <br>This method calls http://api.twitter.com/1/statuses/friends
     *
     * @param userId the ID of the user for whom to request a list of friends
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of friends
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0friends">Twitter API Wiki / Twitter REST API Method: statuses friends</a>
     */
    public PagableResponseList<User> getFriendsStatuses(int userId, long cursor) throws TwitterException {
        return User.createCursorSupportUserList(get(getBaseURL()
                + "statuses/friends.json?user_id=" + userId + "&cursor=" + cursor
                , null, false));
    }


    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).<br>
     * This method automatically provides a value of cursor=-1 to begin paging.
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
     * @since Twitter4J 2.0.9
     */
    public PagableResponseList<User> getFollowersStatuses() throws TwitterException {
        return getFollowersStatuses(-1l);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     *
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
     */
    public PagableResponseList<User> getFollowersStatuses(long cursor) throws TwitterException {
        return User.createCursorSupportUserList(get(getBaseURL()
                + "statuses/followers.json?cursor=" + cursor, null, true));
    }

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     *
     * @param screenName The screen name of the user for whom to request a list of followers.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
     */
    public PagableResponseList<User> getFollowersStatuses(String screenName) throws TwitterException {
        return getFollowersStatuses(screenName, -1l);
    }

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     *
     * @param userId The ID of the user for whom to request a list of followers.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
     */
    public PagableResponseList<User> getFollowersStatuses(int userId) throws TwitterException {
        return getFollowersStatuses(userId, -1l);
    }

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     *
     * @param screenName The screen name of the user for whom to request a list of followers.
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.9
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
     */
    public PagableResponseList<User> getFollowersStatuses(String screenName, long cursor) throws TwitterException {
        return User.createCursorSupportUserList(get(getBaseURL() + "statuses/followers.json?screen_name=" + screenName +
                "&cursor=" + cursor, null, true));
    }

    /**
     * Returns the specified user's followers, each with current status inline. They are ordered by the order in which they joined Twitter (this is going to be changed).
     * <br>This method calls http://api.twitter.com/1/statuses/followers
     *
     * @param userId   The ID of the user for whom to request a list of followers.
     * @param cursor Breaks the results into pages. A single page contains 100 users. This is recommended for users who are followed by many other users. Provide a value of  -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0followers">Twitter API Wiki / Twitter REST API Method: statuses followers</a>
     */
    public PagableResponseList<User> getFollowersStatuses(int userId, long cursor) throws TwitterException {
        return User.createCursorSupportUserList(get(getBaseURL() + "statuses/followers.json?user_id=" + userId +
                "&cursor=" + cursor, null, true));
    }

    /*List Methods*/

    /**
     * Creates a new list for the authenticated user.
     * <br>This method calls http://api.twitter.com/1/user/lists.json
     * @param user The name of the authenticated user creating the list
     * @param name The name of the list you are creating. Required.
     * @param mode Whether your list is public or private. Optional. Values can be public or private. Lists are public by default if no mode is specified.
     * @param description The description of the list you are creating. Optional.
     * @return the list that was created
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-POST-lists">Twitter REST API Method: POST lists</a>
     * @since Twitter4J 2.1.0
     */
    public twitter4j.List createUserList(String user, String name, String mode, String description) throws TwitterException {
        List<PostParameter> postParams = new ArrayList<PostParameter>();
        postParams.add(new PostParameter("name", name));
        if (mode != null) {
            postParams.add(new PostParameter("mode", mode));
        }
        if (description != null) {
            postParams.add(new PostParameter("description", description));
        }
        return new twitter4j.List(http.post(getBaseURL() + user +
                                            "/lists.json",
                                            postParams.toArray(new PostParameter[0]),
                                            true));
    }

    /**
     * Updates the specified list.
     * <br>This method calls http://api.twitter.com/1/user/lists/id.json
     * @param user The name of the authenticated user creating the list
     * @param id The id of the list to update.
     * @param name What you'd like to change the list's name to.
     * @param mode Whether your list is public or private. Optional. Values can be public or private. Lists are public by default if no mode is specified.
     * @param description What you'd like to change the list description to.
     * @return the updated list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-POST-lists-id">Twitter REST API Method: POST lists id</a>
     * @since Twitter4J 2.1.0
     */
    public twitter4j.List updateUserList(String user, int id, String name, String mode, String description) throws TwitterException {
        List<PostParameter> postParams = new ArrayList<PostParameter>();
        if (name != null) {
            postParams.add(new PostParameter("name", name));
        }
        if (mode != null) {
            postParams.add(new PostParameter("mode", mode));
        }
        if (description != null) {
            postParams.add(new PostParameter("description", description));
        }
        return new twitter4j.List(http.post(getBaseURL() + user + "/lists/"
                + id + ".json", postParams.toArray(new PostParameter[0]), true));
    }

    /**
     * List the lists of the specified user. Private lists will be included if the authenticated users is the same as the user whose lists are being returned.
     * <br>This method calls http://api.twitter.com/1/user/lists.json
     * @param user The specified user
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of lists
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-lists">Twitter REST API Method: GET lists</a>
     * @since Twitter4J 2.1.0
     */
    public PagableResponseList<twitter4j.List> getUserLists(String user, long cursor) throws TwitterException {
        return twitter4j.List.createListList(get(getBaseURL() +
                user + "/lists.json?cursor=" + cursor, true));
    }

    /**
     * Show the specified list. Private lists will only be shown if the authenticated user owns the specified list.
     * <br>This method calls http://api.twitter.com/1/user/lists/id.json
     * @param user The name of the authenticated user creating the list
     * @param id The id of the list to show
     * @return the specified list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-id">Twitter REST API Method: GET list id</a>
     * @since Twitter4J 2.1.0
     */
    public twitter4j.List showUserList(String user, int id) throws TwitterException {
        return new twitter4j.List(get(getBaseURL() + user + "/lists/"
                + id + ".json", true));
    }

    /**
     * Deletes the specified list. Must be owned by the authenticated user.
     * <br>This method calls http://api.twitter.com/1/user/lists/id.json
     * @param user The name of the authenticated user deleting the list
     * @param id The id of the list to delete
     * @return the deleted list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-DELETE-list-id">Twitter REST API Method: DELETE /:user/lists/:id</a>
     * @since Twitter4J 2.1.0
     */
    public twitter4j.List deleteUserList(String user, int id) throws TwitterException {
        return new twitter4j.List(http.delete(getBaseURL() +user +
                "/lists/" + id + ".json", true));
    }

    /**
     * Show tweet timeline for members of the specified list.
     * <br>http://api.twitter.com/1/user/lists/list_id/statuses.json
     * @param user The name of the authenticated user deleting the list
     * @param id The id of the list to delete
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return list of statuses for members of the specified list
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-statuses">Twitter REST API Method: GET list statuses</a>
     * @since Twitter4J 2.1.0
     */
    public ResponseList<Status> getUserListStatuses(String user, int id, Paging paging) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + user +
                "/lists/" + id + "/statuses.json", new PostParameter[0],
                paging.asPostParameterList(Paging.SMCP, Paging.PER_PAGE), true));
    }

    /**
     * List the lists the specified user has been added to.
     * <br>This method calls http://api.twitter.com/1/user/lists/memberships.json
     * @param user The specified user
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of lists
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-memberships">Twitter REST API Method: GET /:user/lists/memberships</a>
     * @since Twitter4J 2.1.0
     */
    public PagableResponseList<twitter4j.List> getUserListMemberships(String user, long cursor) throws TwitterException {
        return twitter4j.List.createListList(get(getBaseURL() +
                user + "/lists/memberships.json?cursor=" + cursor, true));
    }

    /**
     * List the lists the specified user follows.
     * <br>This method calls http://api.twitter.com/1/user/lists/subscriptions.json
     * @param user The specified user
     * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
     * @return the list of lists
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-subscriptions">Twitter REST API Method: GET list subscriptions</a>
     * @since Twitter4J 2.1.0
     */
    public PagableResponseList<twitter4j.List> getUserListSubscriptions(String user, long cursor) throws TwitterException {
        return twitter4j.List.createListList(get(getBaseURL() +
                user + "/lists/subscriptions.json?cursor=" + cursor, true));
    }

    /*Direct Message Methods */

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * <br>This method calls http://api.twitter.com/1/direct_messages
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-direct_messages">Twitter API Wiki / Twitter REST API Method: direct_messages</a>
     */
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        return DirectMessage.createDirectMessageList(get(getBaseURL() + "direct_messages.json", true));
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * <br>This method calls http://api.twitter.com/1/direct_messages
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-direct_messages">Twitter API Wiki / Twitter REST API Method: direct_messages</a>
     */
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        return DirectMessage.createDirectMessageList(get(getBaseURL()
                + "direct_messages.json", null, paging.asPostParameterList(), true));
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/direct_messages/sent
     *
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-direct_messages%C2%A0sent">Twitter API Wiki / Twitter REST API Method: direct_messages sent</a>
     */
    public ResponseList<DirectMessage> getSentDirectMessages() throws
            TwitterException {
        return DirectMessage.createDirectMessageList(get(getBaseURL() +
                "direct_messages/sent.json", new PostParameter[0], true));
    }

    /**
     * Returns a list of the direct messages sent by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/direct_messages/sent
     *
     * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-direct_messages%C2%A0sent">Twitter API Wiki / Twitter REST API Method: direct_messages sent</a>
     */
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws
            TwitterException {
        return DirectMessage.createDirectMessageList(get(getBaseURL() +
                "direct_messages/sent.json", new PostParameter[0],
                paging.asPostParameterList(), true));
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimmed if the length of the text is exceeding 140 characters.
     * <br>This method calls http://api.twitter.com/1/direct_messages/new
     *
     * @param screenName the screen name of the user to whom send the direct message
     * @param text The text of your direct message.
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-direct_messages%C2%A0new">Twitter API Wiki / Twitter REST API Method: direct_messages new</a>
     */
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        return new DirectMessage(http.post(getBaseURL() + "direct_messages/new.json",
                new PostParameter[]{new PostParameter("screen_name", screenName),
                        new PostParameter("text", text)}, true));
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimmed if the length of the text is exceeding 140 characters.
     * <br>This method calls http://api.twitter.com/1/direct_messages/new
     *
     * @param userId the screen name of the user to whom send the direct message
     * @param text The text of your direct message.
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-direct_messages%C2%A0new">Twitter API Wiki / Twitter REST API Method: direct_messages new</a>
     * @since Twitter4j 2.1.0
     */
    public DirectMessage sendDirectMessage(int userId, String text)
            throws TwitterException {
        return new DirectMessage(http.post(getBaseURL() + "direct_messages/new.json",
                new PostParameter[]{new PostParameter("user_id", userId),
                        new PostParameter("text", text)}, true));
    }

    /**
     * Destroys the direct message specified in the required ID parameter.  The authenticating user must be the recipient of the specified direct message.
     * <br>This method calls http://api.twitter.com/1/direct_messages/destroy
     *
     * @param id the ID of the direct message to destroy
     * @return the deleted direct message
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-direct_messages%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: direct_messages destroy</a>
     * @since Twitter4J 2.0.1
     */
    public DirectMessage destroyDirectMessage(int id) throws
            TwitterException {
        return new DirectMessage(http.post(getBaseURL() +
                "direct_messages/destroy/" + id + ".json", new PostParameter[0], true));
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1/friendships/create/[id].json
     *
     * @param screenName the screen name of the user to be befriended
     * @return the befriended user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships create</a>
     */
    public User createFriendship(String screenName) throws TwitterException {
        return new User(http.post(getBaseURL() + "friendships/create.json?screen_name=" + screenName, new PostParameter[0], true));
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1/friendships/create/[id].json
     *
     * @param userId the ID of the user to be befriended
     * @return the befriended user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships create</a>
     */
    public User createFriendship(int userId) throws TwitterException {
        return new User(http.post(getBaseURL() + "friendships/create.json?user_id=" + userId, new PostParameter[0], true));
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1/friendships/create/[id].json
     *
     * @param screenName the screen name of the user to be befriended
     * @param follow Enable notifications for the target user in addition to becoming friends.
     * @return the befriended user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships create</a>
     */
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        return new User(http.post(getBaseURL() + "friendships/create.json?screen_name=" + screenName
                + "&follow=" + follow , true));
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1/friendships/create/[id].json
     *
     * @param userId the ID of the user to be befriended
     * @param follow Enable notifications for the target user in addition to becoming friends.
     * @return the befriended user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0create">Twitter API Wiki / Twitter REST API Method: friendships create</a>
     */
    public User createFriendship(int userId, boolean follow) throws TwitterException {
        return new User(http.post(getBaseURL() + "friendships/create.json?user_id=" + userId
                + "&follow=" + follow , true));
    }

    /**
     * Discontinues friendship with the user specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1/friendships/destroy/[id].json
     *
     * @param screenName the screen name of the user for whom to request a list of friends
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: friendships destroy</a>
     */
    public User destroyFriendship(String screenName) throws TwitterException {
        return new User(http.post(getBaseURL() + "friendships/destroy.json?screen_name="
                + screenName, true));
    }

    /**
     * Discontinues friendship with the user specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     * <br>This method calls http://api.twitter.com/1/friendships/destroy/[id].json
     *
     * @param userId the ID of the user for whom to request a list of friends
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: friendships destroy</a>
     * @since Twitter4J 2.1.0
     */
    public User destroyFriendship(int userId) throws TwitterException {
        return new User(http.post(getBaseURL() + "friendships/destroy.json?user_id="
                + userId, true));
    }

    /**
     * Tests if a friendship exists between two users.
     * <br>This method calls http://api.twitter.com/1/friendships/exists.json
     *
     * @param userA The ID or screen_name of the first user to test friendship for.
     * @param userB The ID or screen_name of the second user to test friendship for.
     * @return if a friendship exists between two users.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-exists">Twitter API Wiki / Twitter REST API Method: friendships exists</a>
     */
    public boolean existsFriendship(String userA, String userB) throws TwitterException {
        return -1 != get(getBaseURL() + "friendships/exists.json", "user_a", userA, "user_b", userB, true).
                asString().indexOf("true");
    }

    /**
     * Gets the detailed relationship status between a source user and a target user
     * <br>This method calls http://api.twitter.com/1/friendships/show.json
     * @param sourceScreenName the screen name of the source user
     * @param targetScreenName the screen name of the target user
     * @return Relationship
     *
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">Twitter API Wiki / Twitter REST API Method: friendships show</a>
     */
    public Relationship showFriendShip(String sourceScreenName, String targetScreenName) throws TwitterException {
        return new Relationship(get(getBaseURL() + "friendships/show.json", "source_screen_name", sourceScreenName,
                "target_screen_name", targetScreenName, true));
    }

    /**
     * Gets the detailed relationship status between a source user and a target user
     * <br>This method calls http://api.twitter.com/1/friendships/show.json
     *
     * @param sourceId the screen ID of the source user
     * @param targetId the screen ID of the target user
     * @return Relationship
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">Twitter API Wiki / Twitter REST API Method: friendships show</a>
     */
    public Relationship showFriendShip(int sourceId, int targetId) throws TwitterException {
        return new Relationship(get(getBaseURL() + "friendships/show.json", "source_id", String.valueOf(sourceId),
                "target_id", String.valueOf(targetId), true));
    }


    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     * @return an array of numeric IDs for every user the authenticating user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
     */
    public IDs getFriendsIDs() throws TwitterException {
        return getFriendsIDs(-1l);
    }

    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the authenticating user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
     */
    public IDs getFriendsIDs(long cursor) throws TwitterException {
        return IDs.getFriendsIDs(get(getBaseURL() + "friends/ids.json?cursor=" + cursor, true));
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.<br>
     * all IDs are attempted to be returned, but large sets of IDs will likely fail with timeout errors.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     * @param userId Specfies the ID of the user for whom to return the friends list.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
     */
    public IDs getFriendsIDs(int userId) throws TwitterException {
        return getFriendsIDs(userId, -1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     * @param userId Specifies the ID of the user for whom to return the friends list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friends%C2%A0ids">Twitter API Wiki / Twitter REST API Method: friends ids</a>
     */
    public IDs getFriendsIDs(int userId, long cursor) throws TwitterException {
        return IDs.getFriendsIDs(get(getBaseURL() + "friends/ids.json?user_id=" + userId +
                "&cursor=" + cursor, true));
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     * @param screenName Specfies the screen name of the user for whom to return the friends list.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
     */
    public IDs getFriendsIDs(String screenName) throws TwitterException {
        return getFriendsIDs(screenName, -1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * <br>This method calls http://api.twitter.com/1/friends/ids.json
     * @param screenName Specfies the screen name of the user for whom to return the friends list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/REST-API-Documentation#friends/ids">Twitter API Wiki / REST API Documentation - Social Graph Methods - friends/ids</a>
     */
    public IDs getFriendsIDs(String screenName, long cursor) throws TwitterException {
        return IDs.getFriendsIDs(get(getBaseURL() + "friends/ids.json?screen_name=" + screenName
                + "&cursor=" + cursor, true));
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     */
    public IDs getFollowersIDs() throws TwitterException {
        return getFollowersIDs(-1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     */
    public IDs getFollowersIDs(long cursor) throws TwitterException {
        return IDs.getFriendsIDs(get(getBaseURL() + "followers/ids.json?cursor=" + cursor
                , true));
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     * @param userId Specfies the ID of the user for whom to return the followers list.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     */
    public IDs getFollowersIDs(int userId) throws TwitterException {
        return getFollowersIDs(userId, -1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     * @param userId Specifies the ID of the user for whom to return the followers list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     */
    public IDs getFollowersIDs(int userId, long cursor) throws TwitterException {
        return IDs.getFriendsIDs(get(getBaseURL() + "followers/ids.json?user_id=" + userId
                + "&cursor=" + cursor, true));
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     * @param screenName Specfies the screen name of the user for whom to return the followers list.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     */
    public IDs getFollowersIDs(String screenName) throws TwitterException {
        return getFollowersIDs(screenName, -1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * <br>This method calls http://api.twitter.com/1/followers/ids.json
     * 
     * @param screenName Specfies the screen name of the user for whom to return the followers list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.10
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-followers%C2%A0ids">Twitter API Wiki / Twitter REST API Method: followers ids</a>
     */
    public IDs getFollowersIDs(String screenName, long cursor) throws TwitterException {
        return IDs.getFriendsIDs(get(getBaseURL() + "followers/ids.json?screen_name="
                + screenName + "&cursor=" + cursor, true));
    }

    /**
     * Returns an HTTP 200 OK response code and a representation of the requesting user if authentication was successful; returns a 401 status code and an error message if not.  Use this method to test if supplied user credentials are valid.
     * <br>This method calls http://api.twitter.com/1/account/verify_credentials.json
     *
     * @return user
     * @since Twitter4J 2.0.0
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0verify_credentials">Twitter API Wiki / Twitter REST API Method: account verify_credentials</a>
     */
    public User verifyCredentials() throws TwitterException {
        return new User(get(getBaseURL() + "account/verify_credentials.json"
                , true));
    }

    /**
     * Sets values that users are able to set under the "Account" tab of their settings page. Only the parameters specified(non-null) will be updated.
     * <br>This method calls http://api.twitter.com/1/account/update_profile.json
     *
     * @param name Optional. Maximum of 20 characters.
     * @param email Optional. Maximum of 40 characters. Must be a valid email address.
     * @param url Optional. Maximum of 100 characters. Will be prepended with "http://" if not present.
     * @param location Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.2
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_profile">Twitter REST API Documentation &gt; Account Methods &gt; account/update_location</a>
     */
    public User updateProfile(String name, String email, String url
            , String location, String description) throws TwitterException {
        List<PostParameter> profile = new ArrayList<PostParameter>(5);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "email", email);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return new User(http.post(getBaseURL() + "account/update_profile.json"
                , profile.toArray(new PostParameter[profile.size()]), true));
    }

    /**
     * Returns the remaining number of API requests available to the requesting user before the API limit is reached for the current hour. Calls to rate_limit_status do not count against the rate limit.  If authentication credentials are provided, the rate limit status for the authenticating user is returned.  Otherwise, the rate limit status for the requester's IP address is returned.<br>
     * <br>This method calls http://api.twitter.com/1/account/rate_limit_status.json
     *
     * @return the rate limit status
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.1.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0rate_limit_status">Twitter API Wiki / Twitter REST API Method: account rate_limit_status</a>
     */
    public RateLimitStatus rateLimitStatus() throws TwitterException {
        return new RateLimitStatus(http.get(getBaseURL() + "account/rate_limit_status.json", null != getUserId() && null != getPassword()));
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
     * <br>This method calls http://api.twitter.com/1/account/update_delivery_device.json
     *
     * @param device new Delivery device. Must be one of: IM, SMS, NONE.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_delivery_device">Twitter API Wiki / Twitter REST API Method: account update_delivery_device</a>
     */
    public User updateDeliveryDevice(Device device) throws TwitterException {
        return new User(http.post(getBaseURL() + "account/update_delivery_device.json", new PostParameter[]{new PostParameter("device", device.DEVICE)}, true));
    }


    /**
     * Sets one or more hex values that control the color scheme of the authenticating user's profile page on twitter.com.  These values are also returned in the getUserDetail() method.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_colors.json
     * @param profileBackgroundColor optional, can be null
     * @param profileTextColor optional, can be null
     * @param profileLinkColor optional, can be null
     * @param profileSidebarFillColor optional, can be null
     * @param profileSidebarBorderColor optional, can be null
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_profile_colors">Twitter API Wiki / Twitter REST API Method: account update_profile_colors</a>
     */
    public User updateProfileColors(
            String profileBackgroundColor,
            String profileTextColor,
            String profileLinkColor,
            String profileSidebarFillColor,
            String profileSidebarBorderColor)
            throws TwitterException {
        List<PostParameter> colors = new ArrayList<PostParameter>(5);
        addParameterToList(colors, "profile_background_color"
                , profileBackgroundColor);
        addParameterToList(colors, "profile_text_color"
                , profileTextColor);
        addParameterToList(colors, "profile_link_color"
                , profileLinkColor);
        addParameterToList(colors, "profile_sidebar_fill_color"
                , profileSidebarFillColor);
        addParameterToList(colors, "profile_sidebar_border_color"
                , profileSidebarBorderColor);
        return new User(http.post(getBaseURL() +
                "account/update_profile_colors.json",
                colors.toArray(new PostParameter[colors.size()]), true));
    }

    private void addParameterToList(List<PostParameter> colors,
                                      String paramName, String color) {
        if(null != color){
            colors.add(new PostParameter(paramName,color));
        }
    }

    /**
     * Updates the authenticating user's profile image.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_image.json
     * @param image Must be a valid GIF, JPG, or PNG image of less than 700 kilobytes in size.  Images with width larger than 500 pixels will be scaled down.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable,
     *  or when the specified file is not found (FileNotFoundException will be nested),
     *  or when the specified file object in not representing a file (IOException will be nexted)
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_profile_image">Twitter API Wiki / Twitter REST API Method: account update_profile_image</a>
     */
    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        return new User(http.post(getBaseURL()
                + "account/update_profile_image.json",
                new PostParameter[]{new PostParameter("image", image)}, true));
    }

    /**
     * Updates the authenticating user's profile background image.
     * <br>This method calls http://api.twitter.com/1/account/update_profile_background_image.json
     * @param image Must be a valid GIF, JPG, or PNG image of less than 800 kilobytes in size.  Images with width larger than 2048 pixels will be forceably scaled down.
     * @param tile If set to true the background image will be displayed tiled. The image will not be tiled otherwise.
     * @return the updated user
     * @throws TwitterException when Twitter service or network is unavailable,
     *  or when the specified file is not found (FileNotFoundException will be nested),
     *  or when the specified file object in not representing a file (IOException will be nexted)
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-account%C2%A0update_profile_background_image">Twitter API Wiki / Twitter REST API Method: account update_profile_background_image</a>
     */
    public User updateProfileBackgroundImage(File image, boolean tile)
            throws TwitterException {
        checkFileValidity(image);
        return new User(http.post(getBaseURL()
                + "account/update_profile_background_image.json",
                new PostParameter[]{new PostParameter("image", image),
                new PostParameter("tile", tile)}, true));
    }

    /**
     * Check the existence, and the type of the specified file.
     * @param image image to be uploaded
     * @throws TwitterException when the specified file is not found (FileNotFoundException is nested).
     * @throws IllegalArgumentException when the specified file object is not representing a file.
     */
    private void checkFileValidity(File image) throws TwitterException {
        if (!image.exists()) {
            new TwitterException(new FileNotFoundException(image +" is not found."));
        }
        if (!image.isFile()) {
            new TwitterException(new IOException(image +" is not a file."));
        }
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls http://api.twitter.com/1/favorites.json
     *
     * @return List<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
     * @since Twitter4J 2.0.1
     */
    public ResponseList<Status> getFavorites() throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "favorites.json", new PostParameter[0], true));
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls http://api.twitter.com/1/favorites.json
     *
     * @param page the number of page
     * @return ResponseList<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
     * @since Twitter4J 2.0.1
     */
    public ResponseList<Status> getFavorites(int page) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "favorites.json", "page", String.valueOf(page), true));
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses
     * @return ResponseList<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
     * @since Twitter4J 2.0.1
     */
    public ResponseList<Status> getFavorites(String id) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "favorites/" + id + ".json", new PostParameter[0], true));
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     * <br>This method calls http://api.twitter.com/1/favorites/[id].json
     *
     * @param id   the ID or screen name of the user for whom to request a list of favorite statuses
     * @param page the number of page
     * @return ResponseList<Status>
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
     */
    public ResponseList<Status> getFavorites(String id, int page) throws TwitterException {
        return Status.createStatuseList(get(getBaseURL() + "favorites/" + id + ".json", "page", String.valueOf(page), true));
    }

    /**
     * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
     * <br>This method calls http://api.twitter.com/1/favorites/create/[id].json
     *
     * @param id the ID of the status to favorite
     * @return Status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites%C2%A0create">Twitter API Wiki / Twitter REST API Method: favorites create</a>
     */
    public Status createFavorite(long id) throws TwitterException {
        return new Status(http.post(getBaseURL() + "favorites/create/" + id + ".json", true));
    }

    /**
     * Un-favorites the status specified in the ID parameter as the authenticating user.  Returns the un-favorited status in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/favorites/destroy/[id].json
     *
     * @param id the ID of the status to un-favorite
     * @return Status
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: favorites destroy</a>
     */
    public Status destroyFavorite(long id) throws TwitterException {
        return new Status(http.post(getBaseURL() + "favorites/destroy/" + id + ".json", true));
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/follow/[id].json
     *
     * @param screenName Specifies the screen name of the user to follow with device updates.
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications follow</a>
     */
    public User enableNotification(String screenName) throws TwitterException {
        return new User(http.post(getBaseURL() + "notifications/follow.json?screen_name=" + screenName, true));
    }

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/follow/[id].json
     *
     * @param userId Specifies the ID of the user to follow with device updates.
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0follow">Twitter API Wiki / Twitter REST API Method: notifications follow</a>
     */
    public User enableNotification(int userId) throws TwitterException {
        return new User(http.post(getBaseURL() + "notifications/follow.json?userId=" + userId, true));
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/leave/[id].json
     *
     * @param screenName Specifies the screen name of the user to disable device notifications.
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications leave</a>
     */
    public User disableNotification(String screenName) throws TwitterException {
        return new User(http.post(getBaseURL() + "notifications/leave.json?screen_name=" + screenName, true));
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * <br>This method calls http://api.twitter.com/1/notifications/leave/[id].json
     *
     * @param userId Specifies the ID of the user to disable device notifications.
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-notifications%C2%A0leave">Twitter API Wiki / Twitter REST API Method: notifications leave</a>
     */
    public User disableNotification(int userId) throws TwitterException {
        return new User(http.post(getBaseURL() + "notifications/leave.json?user_id=" + userId, true));
    }

    /* Block Methods */

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create/[id].json
     *
     * @param screenName the screen_name of the user to block
     * @return the blocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks%C2%A0create">Twitter API Wiki / Twitter REST API Method: blocks create</a>
     */
    public User createBlock(String screenName) throws TwitterException {
        return new User(http.post(getBaseURL() + "blocks/create.json?screen_name=" + screenName, true));
    }

    /**
     * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/create/[id].json
     *
     * @param userId the ID of the user to block
     * @return the blocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks%C2%A0create">Twitter API Wiki / Twitter REST API Method: blocks create</a>
     */
    public User createBlock(int userId) throws TwitterException {
        return new User(http.post(getBaseURL() + "blocks/create.json?user_id=" + userId, true));
    }

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/destroy/[id].json
     *
     * @param screen_name the screen_name of the user to block
     * @return the unblocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: blocks destroy</a>
     */
    public User destroyBlock(String screen_name) throws TwitterException {
        return new User(http.post(getBaseURL() + "blocks/destroy.json?screen_name=" + screen_name, true));
    }

    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * <br>This method calls http://api.twitter.com/1/blocks/destroy/[id].json
     *
     * @param userId the ID of the user to block
     * @return the unblocked user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.1
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: blocks destroy</a>
     */
    public User destroyBlock(int userId) throws TwitterException {
        return new User(http.post(getBaseURL() + "blocks/destroy.json?user_id=" + userId, true));
    }

    /**
     * Tests if a friendship exists between two users.
     * <br>This method calls http://api.twitter.com/1/blocks/exists/[id].json
     *
     * @param screenName The screen_name of the potentially blocked user.
     * @return  if the authenticating user is blocking a target user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-exists">Twitter API Wiki / Twitter REST API Method: blocks exists</a>
     */
    public boolean existsBlock(String screenName) throws TwitterException {
        try{
            return -1 == get(getBaseURL() + "blocks/exists.json?screen_name=" + screenName, true).
                    asString().indexOf("<error>You are not blocking this user.</error>");
        }catch(TwitterException te){
            if(te.getStatusCode() == 404){
                return false;
            }
            throw te;
        }
    }

    /**
     * Tests if a friendship exists between two users.
     * <br>This method calls http://api.twitter.com/1/blocks/exists/[id].json
     *
     * @param userId The ID of the potentially blocked user.
     * @return  if the authenticating user is blocking a target user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-exists">Twitter API Wiki / Twitter REST API Method: blocks exists</a>
     */
    public boolean existsBlock(int userId) throws TwitterException {
        try{
            return -1 == get(getBaseURL() + "blocks/exists.json?user_id=" + userId, true).
                    asString().indexOf("<error>You are not blocking this user.</error>");
        }catch(TwitterException te){
            if(te.getStatusCode() == 404){
                return false;
            }
            throw te;
        }
    }

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking.json
     *
     * @return a list of user objects that the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking">Twitter API Wiki / Twitter REST API Method: blocks blocking</a>
     */
    public ResponseList<User> getBlockingUsers() throws
            TwitterException {
        return User.createUsersList(get(getBaseURL() +
                "blocks/blocking.json", true));
    }

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking.json
     *
     * @param page the number of page
     * @return a list of user objects that the authenticating user
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking">Twitter API Wiki / Twitter REST API Method: blocks blocking</a>
     */
    public ResponseList<User> getBlockingUsers(int page) throws
            TwitterException {
        return User.createUsersList(get(getBaseURL() +
                "blocks/blocking.json?page=" + page, true));
    }

    /**
     * Returns an array of numeric user ids the authenticating user is blocking.
     * <br>This method calls http://api.twitter.com/1/blocks/blocking/ids
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-blocks-blocking-ids">Twitter API Wiki / Twitter REST API Method: blocks blocking ids</a>
     */
    public IDs getBlockingUsersIDs() throws TwitterException {
        return IDs.getBlockIDs(get(getBaseURL() + "blocks/blocking/ids.json", true));
    }

    /* Saved Searches Methods */
    /**
     * Returns the authenticated user's saved search queries.
     * <br>This method calls http://api.twitter.com/1/saved_searches.json
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.8
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches">Twitter API Wiki / Twitter REST API Method: saved_searches</a>
     */
    public List<SavedSearch> getSavedSearches() throws TwitterException {
        return SavedSearch.createSavedSearchList(get(getBaseURL() + "saved_searches.json", true));
    }

    /**
     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
     * <br>This method calls http://api.twitter.com/1/saved_searches/show/id.json
     * @param id The id of the saved search to be retrieved.
     * @return the data for a saved search
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.8
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-show">Twitter API Wiki / Twitter REST API Method: saved_searches show</a>
     */
    public SavedSearch showSavedSearch(int id) throws TwitterException {
        return new SavedSearch(get(getBaseURL() + "saved_searches/show/" + id
                + ".json", true));
    }

    /**
     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
     * <br>This method calls http://api.twitter.com/1/saved_searches/saved_searches/create.json
     * @return the data for a created saved search
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.8
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-create">Twitter API Wiki / Twitter REST API Method: saved_searches create</a>
     */
    public SavedSearch createSavedSearch(String query) throws TwitterException {
        return new SavedSearch(http.post(getBaseURL() + "saved_searches/create.json"
                , new PostParameter[]{new PostParameter("query", query)}, true));
    }

    /**
     * Destroys a saved search for the authenticated user. The search specified by id must be owned by the authenticating user.
     * <br>This method calls http://api.twitter.com/1/saved_searches/destroy/id.json
     * @param id The id of the saved search to be deleted.
     * @return the data for a destroyed saved search
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 2.0.8
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-destroy">Twitter API Wiki / Twitter REST API Method: saved_searches destroy</a>
     */
    public SavedSearch destroySavedSearch(int id) throws TwitterException {
        return new SavedSearch(http.post(getBaseURL()
                + "saved_searches/destroy/" + id + ".json", true));
    }

    /* Help Methods */
    /**
     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
     *
     * @return true if the API is working
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter4J 1.0.4
     * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-help%C2%A0test">Twitter API Wiki / Twitter REST API Method: help test</a>
     */
    public boolean test() throws TwitterException {
        return -1 != get(getBaseURL() + "help/test.json", false).
                asString().indexOf("ok");
    }

    private SimpleDateFormat format = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Twitter twitter = (Twitter) o;

        if (!getBaseURL().equals(twitter.getBaseURL())) return false;
        if (!format.equals(twitter.format)) return false;
        if (!http.equals(twitter.http)) return false;
        if (!getSearchBaseURL().equals(twitter.getSearchBaseURL())) return false;
        if (!source.equals(twitter.source)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = http.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + format.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Twitter{" +
                "http=" + http +
                ", source='" + source + '\'' +
                ", format=" + format +
                '}';
    }
}
