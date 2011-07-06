package twitter4j;/*
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

import twitter4j.json.DataObjectFactory;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class NotificationMethodsTest extends TwitterTestBase {
    public NotificationMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNotification() throws Exception {
        try {
            twitter1.disableNotification("twit4jprotected");
        } catch (TwitterException te) {
        }
        User user1 = twitter1.enableNotification("twit4jprotected");
        assertNotNull(DataObjectFactory.getRawJSON(user1));
        assertEquals(user1, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user1)));
        User user2 = twitter1.disableNotification("twit4jprotected");
        assertNotNull(DataObjectFactory.getRawJSON(user2));
        assertEquals(user2, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user2)));
    }
}
