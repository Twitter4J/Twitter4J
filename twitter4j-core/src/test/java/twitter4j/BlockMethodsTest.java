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

import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class BlockMethodsTest extends TwitterTestBase {
    public BlockMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBlockMethods() throws Exception {
        User user1 = twitter2.createBlock(id1.screenName);
        assertNotNull(DataObjectFactory.getRawJSON(user1));
        assertEquals(user1, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user1)));
        User user2 = twitter2.destroyBlock(id1.screenName);
        assertNotNull(DataObjectFactory.getRawJSON(user2));
        assertEquals(user2, DataObjectFactory.createUser(DataObjectFactory.getRawJSON(user2)));
        assertFalse(twitter1.existsBlock("twit4j2"));
        assertTrue(twitter1.existsBlock("twit4jblock"));
        List<User> users = twitter1.getBlockingUsers();
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());
        users = twitter1.getBlockingUsers(1);
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
        assertEquals(1, users.size());
        assertEquals(39771963, users.get(0).getId());

        IDs ids = twitter1.getBlockingUsersIDs();
        assertNull(DataObjectFactory.getRawJSON(users));
        assertNotNull(DataObjectFactory.getRawJSON(ids));
        assertEquals(1, ids.getIDs().length);
        assertEquals(39771963, ids.getIDs()[0]);
    }

}
