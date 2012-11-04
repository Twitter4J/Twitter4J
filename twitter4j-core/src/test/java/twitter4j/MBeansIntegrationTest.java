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

/**
 * Integration tests for APIStatistics ensuring that the integration logic
 * in Twitter (API client) works.
 *
 * @author Nick Dellamaggiore (nick.dellamaggiore <at> gmail.com)
 */
public class MBeansIntegrationTest extends TwitterTestBase {

    public MBeansIntegrationTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testMonitoringIntegration() throws Exception {
        // monitoring is turned on with mbeanEnabled=true
        TwitterAPIMonitor monitor = TwitterAPIMonitor.getInstance();
        long count = monitor.getStatistics().getCallCount();
        twitter1.getHomeTimeline();
        assertEquals(count + 1, monitor.getStatistics().getCallCount());
    }
}
