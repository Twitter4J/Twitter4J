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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class SearchAPITest extends TwitterTestBase {

    @Test
    void testQuery() {
        var format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Query query = Query.of("test")
                .withUntil(format.format(LocalDateTime.now().minus(24,ChronoUnit.DAYS)));
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

    @Test
    void testSearch() throws Exception {
        String queryStr = "test";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = format.format(LocalDateTime.now().minus(1,ChronoUnit.DAYS));
        Query query = Query.of(queryStr).withUntil(dateStr);
        QueryResult queryResult = twitter1.search().search(query);
        RateLimitStatus rateLimitStatus = queryResult.getRateLimitStatus();
        assertTrue(-1 != queryResult.getSinceId(), "sinceId");
        assertTrue(1265204883 < queryResult.getMaxId());
        assertTrue(queryResult.getRefreshURL().contains(queryStr));
        assertEquals(15, queryResult.getCount());
        assertTrue(0 < queryResult.getCompletedIn());
        assertEquals(queryStr + " until:" + dateStr, queryResult.getQuery());

        List<Status> tweets = queryResult.getTweets();
        assertTrue(1 <= tweets.size());
        assertEquals(tweets.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(tweets.get(0))));
        assertNotNull(tweets.get(0).getText());
        assertNotNull(tweets.get(0).getCreatedAt());
        assertNotNull(tweets.get(0).getUser(), "user");
        assertTrue(-1 != tweets.get(0).getId());
        assertNotNull(tweets.get(0).getUser().getProfileImageURL());
        String source = tweets.get(0).getSource();
        assertTrue(source.contains("<a href=\"") || "web".equals(source) || "API".equals(source));


        query = Query.of("from:twit4j doesnothit");
        queryResult = twitter1.search().search(query);
        assertTrue(5 > (queryResult.getRateLimitStatus().getRemaining() - rateLimitStatus.getRemaining()));
        assertEquals(0, queryResult.getSinceId());
        assertEquals(15, queryResult.getCount());
        assertTrue(4 > queryResult.getCompletedIn());
        assertEquals("from:twit4j doesnothit", queryResult.getQuery());

        queryStr = "%... 日本語";

        twitter1.tweets().updateStatus(queryStr + LocalDateTime.now());
        query = Query.of(queryStr);
        queryResult = twitter1.search().search(query);
        assertEquals(queryStr, queryResult.getQuery());
        assertTrue(0 < queryResult.getTweets().size());
        query = Query.of("starbucks")
                .withGeoCode(new GeoLocation(47.6094651, -122.3411666), 10, Query.KILOMETERS);
        queryResult = twitter1.search().search(query);
        assertTrue(0 < queryResult.getTweets().size());

        query = Query.of("from:tsuda").withSinceId(1671199128);
        queryResult = twitter1.search().search(query);
        assertTrue(0 < queryResult.getTweets().size());
        assertEquals(4171231, queryResult.getTweets().get(0).getUser().getId());
        assertTrue(queryResult.hasNext());
        assertNotNull(queryResult.nextQuery());

        query = Query.of("\\u5e30%u5e30 <%}& foobar").withCount(100);
        twitter1.search().search(query);
    }

    @Test
    void testEasyPaging() throws Exception {
        Query query = Query.of("from:twit4j doesnothit").withResultType(Query.POPULAR);
        QueryResult result = twitter1.search().search(query);
        assertFalse(result.hasNext());

        query = Query.of("from:yusukey");
        do {
            result = twitter1.search().search(query);
            // do something
        } while ((query = result.nextQuery()) != null);
    }

    @Test
    void testEasyPaging2() throws Exception {
        LocalDateTime now = LocalDateTime.now().plus(1, ChronoUnit.MONTHS);
        String until = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();

        String lang = "en";
        Query query = Query.of("twitter")
                .withLang(lang)
                .withResultType(Query.ResultType.recent)
                .withSince("2017-01-01")
                .withUntil(until);
        assertEquals(lang, query.lang);
        QueryResult qr = twitter1.search().search(query);
        Query nextQuery = qr.nextQuery();
        if (nextQuery != null) {
            assertEquals(Query.ResultType.recent, nextQuery.resultType);
            assertTrue(nextQuery.maxId != -1L, "max id not set");
        }
    }

}
