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

import java.util.Date;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class DirectMessageMethodsTest extends TwitterTestBase {
    public DirectMessageMethodsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDirectMessages() throws Exception {
        String expectedReturn = new Date() + ":directmessage test";
        DirectMessage actualReturn = twitter1.sendDirectMessage(id3.id, expectedReturn);
        assertTrue(0 <= actualReturn.getId());
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn, DataObjectFactory.createDirectMessage(DataObjectFactory.getRawJSON(actualReturn)));
        assertEquals(expectedReturn, actualReturn.getText());
        assertEquals(id1.screenName, actualReturn.getSender().getScreenName());
        assertEquals(id3.screenName, actualReturn.getRecipient().getScreenName());
        List<DirectMessage> actualReturnList = twitter3.getDirectMessages();
        assertNotNull(DataObjectFactory.getRawJSON(actualReturnList));
        assertEquals(actualReturnList.get(0), DataObjectFactory.createDirectMessage(DataObjectFactory.getRawJSON(actualReturnList.get(0))));
        assertTrue(1 <= actualReturnList.size());
        try {
            actualReturn = twitter1.showDirectMessage(actualReturnList.get(0).getId());
        } catch (TwitterException te) {
            // twitter1 is not allowed to access or delete your direct messages
            assertEquals(403, te.getStatusCode());
        }
        actualReturn = twitter3.showDirectMessage(actualReturnList.get(0).getId());
        assertNotNull(DataObjectFactory.getRawJSON(actualReturn));
        assertEquals(actualReturn, DataObjectFactory.createDirectMessage(DataObjectFactory.getRawJSON(actualReturn)));
        assertEquals(actualReturnList.get(0).getId(), actualReturn.getId());
    }
}
