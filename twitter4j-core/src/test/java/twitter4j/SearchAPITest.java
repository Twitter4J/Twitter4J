/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j;

import twitter4j.internal.http.HttpParameter;
import twitter4j.json.DataObjectFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
                .until(format.format(new java.util.Date(System.currentTimeMillis() - 3600 * 24)));
        HttpParameter[] params = query.asHttpParameterArray();
        assertTrue(findParameter(params, "q"));
        assertTrue(findParameter(params, "until"));
    }

    private boolean findParameter(HttpParameter[] params, String paramName) {
        boolean found = false;
        for (HttpParameter param : params) {
            if (paramName.equals(param.getName())) {
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
        QueryResult queryResult = twitter1.search(query);
        RateLimitStatus rateLimitStatus = queryResult.getRateLimitStatus();
        assertTrue("sinceId", -1 != queryResult.getSinceId());
        assertTrue(1265204883 < queryResult.getMaxId());
        assertTrue(-1 != queryResult.getRefreshURL().indexOf(queryStr));
        assertEquals(15, queryResult.getCount());
        assertTrue(0 < queryResult.getCompletedIn());
        assertEquals(queryStr + " until:" + dateStr, queryResult.getQuery());

        List<Status> tweets = queryResult.getTweets();
        assertTrue(1 <= tweets.size());
        assertEquals(tweets.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(tweets.get(0))));
        assertNotNull(tweets.get(0).getText());
        assertNotNull(tweets.get(0).getCreatedAt());
        assertNotNull("user", tweets.get(0).getUser());
        assertTrue(-1 != tweets.get(0).getId());
        assertNotNull(tweets.get(0).getUser().getProfileImageURL());
        String source = tweets.get(0).getSource();
        assertTrue(-1 != source.indexOf("<a href=\"") || "web".equals(source) || "API".equals(source));


        query = new Query("from:twit4j doesnothit");
        queryResult = twitter1.search(query);
        assertEquals(queryResult.getRateLimitStatus().getRemaining() + 1, rateLimitStatus.getRemaining());
        assertEquals(0, queryResult.getSinceId());
//        assertEquals(-1, queryResult.getMaxId());
//        assertNull(queryResult.getRefreshUrl());
        assertEquals(15, queryResult.getCount());
        assertTrue(4 > queryResult.getCompletedIn());
        assertEquals("from:twit4j doesnothit", queryResult.getQuery());

        queryStr = "%... 日本語";

        twitter1.updateStatus(queryStr + new Date());
        query = new Query(queryStr);
        queryResult = twitter1.search(query);
        assertEquals(queryStr, queryResult.getQuery());
        assertTrue(0 < queryResult.getTweets().size());
        query.setQuery("from:al3x");
        query.setGeoCode(new GeoLocation(37.78233252646689, -122.39301681518555), 10, Query.KILOMETERS);
        queryResult = twitter1.search(query);
        assertTrue(0 <= queryResult.getTweets().size());

        query = new Query("from:tsuda");
        query.setSinceId(1671199128);
        queryResult = twitter1.search(query);
        assertTrue(0 < queryResult.getTweets().size());
        assertEquals(4171231, queryResult.getTweets().get(0).getUser().getId());
        assertTrue(queryResult.hasNext());
        assertNotNull(queryResult.nextQuery());

        query = new Query("\\u5e30%u5e30 <%}& foobar").count(100);
        QueryResult result = twitter1.search(query);
    }

    public void testEasyPaging() throws Exception {
        Query query = new Query("from:twit4j doesnothit");
        QueryResult result = twitter1.search(query);
        assertFalse(result.hasNext());

        query = new Query("from:yusukey");
        do {
            result = twitter1.search(query);
            // do something
        } while ((query = result.nextQuery()) != null);
    }

}
