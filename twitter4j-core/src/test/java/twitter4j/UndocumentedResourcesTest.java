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

import twitter4j.json.DataObjectFactory;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class UndocumentedResourcesTest extends TwitterTestBase {
    public UndocumentedResourcesTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRelatedResults() throws Exception {
        RelatedResults relatedResults = twitter1.getRelatedResults(999383469l);
        assertNotNull(relatedResults);
        ResponseList<Status> statuses;
        statuses = relatedResults.getTweetsFromUser();
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
        statuses = relatedResults.getTweetsWithConversation();
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
        statuses = relatedResults.getTweetsWithReply();
        if (statuses.size() > 0) {
            assertEquals(statuses.get(0), DataObjectFactory.createStatus(DataObjectFactory.getRawJSON(statuses.get(0))));
        }
    }
}
