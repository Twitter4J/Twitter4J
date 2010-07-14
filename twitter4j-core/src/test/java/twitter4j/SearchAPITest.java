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

import twitter4j.internal.http.HttpParameter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class SearchAPITest extends TwitterTestBase {

    public SearchAPITest(String name) {
        super(name);
    }
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testQuery() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Query query = new Query("test")
                .until(format.format(new java.util.Date(System.currentTimeMillis() - 3600*24)));
        HttpParameter[] params = query.asHttpParameterArray();
        assertTrue(findParameter(params,"q"));
        assertTrue(findParameter(params,"until"));
    }
    private boolean findParameter(HttpParameter[] params, String paramName){
        boolean found = false;
        for(HttpParameter param: params){
            if(paramName.equals(param.getName())){
                found = true;
                break;
            }
        }
        return found;
    }
    public void testSearch() throws Exception {
        String queryStr = "test";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new java.util.Date(System.currentTimeMillis() - 24 * 3600 * 1000));
        Query query = new Query(queryStr).until(dateStr);
        QueryResult queryResult = unauthenticated.search(query);
        assertTrue("sinceId", -1 != queryResult.getSinceId());
        assertTrue(1265204883 < queryResult.getMaxId());
        assertTrue(-1 != queryResult.getRefreshUrl().indexOf(queryStr));
        assertEquals(15, queryResult.getResultsPerPage());
        assertTrue(0 < queryResult.getCompletedIn());
        assertEquals(1, queryResult.getPage());
        assertEquals(queryStr + " until:"+ dateStr, queryResult.getQuery());

        List<Tweet> tweets = queryResult.getTweets();
        assertTrue(1<=tweets.size());
        assertNotNull(tweets.get(0).getText());
        assertNotNull(tweets.get(0).getCreatedAt());
        assertNotNull("from user", tweets.get(0).getFromUser());
        assertTrue("fromUserId", -1 != tweets.get(0).getFromUserId());
        assertTrue(-1 !=  tweets.get(0).getId());
//        assertNotNull(tweets.get(0).getIsoLanguageCode());
        String profileImageURL = tweets.get(0).getProfileImageUrl();
        assertTrue(-1 != profileImageURL.toLowerCase().indexOf(".jpg")
                || -1 != profileImageURL.toLowerCase().indexOf(".png")
                || -1 != profileImageURL.toLowerCase().indexOf(".gif"));
        String source = tweets.get(0).getSource();
        assertTrue(-1 != source.indexOf("<a href=\"") || "web".equals(source) || "API".equals(source));


        query = new Query("from:twit4j doesnothit");
        queryResult = unauthenticated.search(query);
        assertEquals(0, queryResult.getSinceId());
//        assertEquals(-1, queryResult.getMaxId());
//        assertNull(queryResult.getRefreshUrl());
        assertEquals(15, queryResult.getResultsPerPage());
//        assertEquals(-1, queryResult.getTotal());
        assertNull(queryResult.getWarning());
        assertTrue(4 > queryResult.getCompletedIn());
        assertEquals(1, queryResult.getPage());
        assertEquals("from:twit4j doesnothit", queryResult.getQuery());

        queryStr = "%... 日本語 ";

        twitterAPI1.updateStatus(queryStr + new Date());
        query = new Query(queryStr);
        queryResult = unauthenticated.search(query);
        assertEquals(queryStr, queryResult.getQuery());
        assertTrue(0 < queryResult.getTweets().size());
        query.setQuery("from:al3x");
        query.setGeoCode(new GeoLocation(37.78233252646689,-122.39301681518555) ,10,Query.KILOMETERS);
        queryResult = unauthenticated.search(query);
        assertTrue(0 <= queryResult.getTweets().size());

        query = new Query("from:beastieboys");
        query.setSinceId(1671199128);
        queryResult = unauthenticated.search(query);
        assertEquals(0, queryResult.getTweets().size());

        query = new Query("\\u5e30%u5e30 <%}& foobar").rpp(100).page(1);
        QueryResult result = twitterAPI1.search(query);
    }

    public void testTrends() throws Exception{
        Trends trends;
        trends = unauthenticated.getTrends();

        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertEquals(10, trends.getTrends().length);
        for (int i = 0; i < 10; i++) {
            assertNotNull(trends.getTrends()[i].getName());
            assertNotNull(trends.getTrends()[i].getUrl());
            assertNull(trends.getTrends()[i].getQuery());
            trends.getTrends()[i].hashCode();
            trends.getTrends()[i].toString();
        }

        trends = unauthenticated.getCurrentTrends();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertEquals(10, trends.getTrends().length);
        for (Trend trend : trends.getTrends()) {
            assertNotNull(trend.getName());
            assertNull(trend.getUrl());
            assertNotNull(trend.getQuery());
            trend.hashCode();
            trend.toString();
        }

        trends = unauthenticated.getCurrentTrends(true);
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        Trend[] trendArray = trends.getTrends();
        assertEquals(10, trendArray.length);
        for (Trend trend : trends.getTrends()) {
            assertNotNull(trend.getName());
            assertNull(trend.getUrl());
            assertNotNull(trend.getQuery());
            trend.hashCode();
            trend.toString();
        }

        List<Trends> trendsList;

        trendsList = unauthenticated.getDailyTrends();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrends(trendsList,20);

        trendsList = unauthenticated.getDailyTrends(new Date(), true);
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrends(trendsList,20);

        trendsList = unauthenticated.getWeeklyTrends();
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrends(trendsList,30);

        trendsList = unauthenticated.getWeeklyTrends(new Date(), true);
        assertTrue(100000 > (trends.getAsOf().getTime() - System.currentTimeMillis()));
        assertTrends(trendsList,30);
    }

    private void assertTrends(List<Trends> trendsArray, int expectedSize) throws Exception{
        Date trendAt = null;
         for(Trends singleTrends : trendsArray){
             assertEquals(expectedSize, singleTrends.getTrends().length);
             if(null != trendAt){
                 assertTrue(trendAt.before(singleTrends.getTrendAt()));
             }
             trendAt = singleTrends.getTrendAt();
             for (int i = 0; i < singleTrends.getTrends().length; i++) {
                 assertNotNull(singleTrends.getTrends()[i].getName());
                 assertNull(singleTrends.getTrends()[i].getUrl());
                 assertNotNull(singleTrends.getTrends()[i].getQuery());
             }
         }
    }
}
