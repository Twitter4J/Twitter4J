/*
 * Copyright 2007 Yusuke Yamamoto
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
import twitter4j.v1.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
@Execution(ExecutionMode.CONCURRENT)
class TimelineResourcesTest extends TwitterTestBase {

    @Test
    void testGetHomeTimeline() throws Exception {
        List<Status> statuses = twitter1.v1().timelines().getHomeTimeline();
        assertTrue(0 < statuses.size());
        assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
    }

    @Test
    void testUserTimeline() throws Exception {
        List<Status> statuses;
        statuses = twitter1.v1().timelines().getUserTimeline();
        assertTrue(0 < statuses.size(), "size");
        try {
            statuses = twitter1.v1().timelines().getUserTimeline("1000");
            assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertTrue(0 < statuses.size(), "size");
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertEquals(9737332, statuses.get(0).getUser().getId());
            try {
                twitter1.v1().timelines().getUserTimeline(1000);
            } catch (TwitterException te) {
                // id 1000 / @percep2al is now protected
                assertEquals(401, te.getStatusCode());
            }
            statuses = twitter1.v1().timelines().getUserTimeline(id1.screenName, Paging.ofCount(10));
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
            assertTrue(0 < statuses.size(), "size");
            statuses = twitter1.v1().timelines().getUserTimeline(id1.screenName, Paging.ofSinceId(999383469L));
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
            assertTrue(0 < statuses.size(), "size");
        } catch (TwitterException te) {
            // is being rate limited
            assertEquals(400, te.getStatusCode());
        }

        statuses = twitter1.v1().timelines().getUserTimeline(Paging.ofSinceId(999383469L));
        assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(0 < statuses.size(), "size");
        statuses = twitter1.v1().timelines().getUserTimeline(Paging.ofSinceId(999383469L).count(15));
        assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
        assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
        assertTrue(0 < statuses.size(), "size");


        statuses = twitter1.v1().timelines().getUserTimeline(Paging.ofPage(1).count(30));
        assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
        assertNotNull(TwitterObjectFactory.getRawJSON(statuses));

        List<Status> statuses2 = twitter1.v1().timelines().getUserTimeline(Paging.ofPage(2).count(15));
        assertEquals(statuses2.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses2.get(0))));
    }

    @Test
    void testGetMentions() throws Exception {
        Status status = twitter2.v1().tweets().updateStatus("@" + id1.screenName + " reply to id1 " + LocalDateTime.now());
        assertNotNull(TwitterObjectFactory.getRawJSON(status));
        List<Status> statuses = twitter1.v1().timelines().getMentionsTimeline();
        // mention can be invisible due to Twitter's spam isolation mechanism
        if (statuses.size() != 0) {
            assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertTrue(statuses.size() > 0);

            statuses = twitter1.v1().timelines().getMentionsTimeline(Paging.ofPage(1));
            assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertTrue(statuses.size() > 0);
            statuses = twitter1.v1().timelines().getMentionsTimeline(Paging.ofPage(1));
            assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertTrue(statuses.size() > 0);
            statuses = twitter1.v1().timelines().getMentionsTimeline(Paging.ofPage(1).sinceId(1L));
            assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertTrue(statuses.size() > 0);
            statuses = twitter1.v1().timelines().getMentionsTimeline(Paging.ofSinceId(1L));
            assertNotNull(TwitterObjectFactory.getRawJSON(statuses));
            assertEquals(statuses.get(0), TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(statuses.get(0))));
            assertTrue(statuses.size() > 0);
        }
    }

    @Test
    void testRetweetsOfMe() throws Exception {
        twitter2.v1().timelines().getRetweetsOfMe();
    }
}
