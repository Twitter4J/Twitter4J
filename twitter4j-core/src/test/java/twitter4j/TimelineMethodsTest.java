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

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class TimelineMethodsTest extends TwitterTestBase {
    public TimelineMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPublicTimeline() throws Exception {
        List<Status> statuses;
        twitter1 = new TwitterFactory(new ConfigurationBuilder().setOAuthConsumerKey(desktopConsumerKey)
                .setOAuthConsumerSecret(desktopConsumerSecret).setOAuthAccessToken(id1.accessToken).setOAuthConsumerSecret(id1.accessTokenSecret)
                .setJSONStoreEnabled(false).build()).getInstance();
        statuses = twitter1.getPublicTimeline();
        assertTrue("size", 0 < statuses.size());
        // should be null because json store is disabled programatically
        assertNull(DataObjectFactory.getRawJSON(statuses));
    }

    public void testGetHomeTimeline() throws Exception {
        List<Status> statuses = twitter1.getHomeTimeline();
        assertTrue(0 < statuses.size());
        assertTrue(twitter1.getConfiguration().isJSONStoreEnabled());
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
    }

    public void testUserTimeline() throws Exception {
        List<Status> statuses;
        statuses = twitter1.getUserTimeline();
        assertTrue("size", 0 < statuses.size());
        try {
            statuses = unauthenticated.getUserTimeline("1000");
            assertNotNull(DataObjectFactory.getRawJSON(statuses));
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertTrue("size", 0 < statuses.size());
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertEquals(9737332, statuses.get(0).getUser().getId());
            statuses = unauthenticated.getUserTimeline(1000);
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertNotNull(DataObjectFactory.getRawJSON(statuses));
            assertTrue("size", 0 < statuses.size());
            assertEquals(1000, statuses.get(0).getUser().getId());

            statuses = unauthenticated.getUserTimeline(id1.screenName, new Paging().count(10));
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertNotNull(DataObjectFactory.getRawJSON(statuses));
            assertTrue("size", 0 < statuses.size());
            statuses = unauthenticated.getUserTimeline(id1.screenName, new Paging(999383469l));
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
            assertNotNull(DataObjectFactory.getRawJSON(statuses));
            assertTrue("size", 0 < statuses.size());
        } catch (TwitterException te) {
            // is being rate limited
            assertEquals(400, te.getStatusCode());
        }

        statuses = twitter1.getUserTimeline(new Paging(999383469l));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue("size", 0 < statuses.size());
        statuses = twitter1.getUserTimeline(new Paging(999383469l).count(15));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertTrue("size", 0 < statuses.size());


        statuses = twitter1.getUserTimeline(new Paging(1).count(30));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        List<Status> statuses2 = twitter1.getUserTimeline(new Paging(2).count(15));
        assertEquals(statuses2.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses2.get(0))));
        assertEquals(statuses.get(statuses.size() - 1), statuses2.get(statuses2.size() - 1));
    }

    public void testGetMentions() throws Exception {
        Status status = twitter2.updateStatus("@" + id1.screenName + " reply to id1 " + new java.util.Date());
        assertNotNull(DataObjectFactory.getRawJSON(status));
        List<Status> statuses = twitter1.getMentions();
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);

        statuses = twitter1.getMentions(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);
        statuses = twitter1.getMentions(new Paging(1));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);
        statuses = twitter1.getMentions(new Paging(1, 1l));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);
        statuses = twitter1.getMentions(new Paging(1l));
        assertNotNull(DataObjectFactory.getRawJSON(statuses));
        assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        assertTrue(statuses.size() > 0);
    }
}
