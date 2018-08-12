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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
class DirectMessagesResourcesTest extends TwitterTestBase {

    @Test
    void testNewDMAPIs() throws Exception {

        // send dm

        // ensure id1 is not blocking id2, and id2 is following id1
        twitter1.destroyBlock(rwPrivate.id);
        rwPrivateMessage.createFriendship(id1.id);
        String message = "hello " + new Date().toString();
        DirectMessage sent = twitter1.directMessages().sendDirectMessage(rwPrivate.id, message);
        assertEquals(rwPrivate.id, sent.getRecipientId());
        assertEquals(id1.id, sent.getSenderId());
        assertEquals(message, sent.getText());
        assertEquals(sent, TwitterObjectFactory.createDirectMessage(TwitterObjectFactory.getRawJSON(sent)));

        // receive dm
        DirectMessage received = rwPrivateMessage.showDirectMessage(sent.getId());
        assertEquals(rwPrivate.id, received.getRecipientId());
        assertEquals(id1.id, received.getSenderId());
        assertEquals(received, TwitterObjectFactory.createDirectMessage(TwitterObjectFactory.getRawJSON(received)));

        // destroy dm
        DirectMessageList directMessages = rwPrivateMessage.getDirectMessages(100);
        assertTrue(directMessages.size() > 0);

        rwPrivateMessage.destroyDirectMessage(received.getId());
    }


    /*
    ResponseList<DirectMessage> getDirectMessages()
ResponseList<DirectMessage> getDirectMessages(Paging paging)
ResponseList<DirectMessage> getSentDirectMessages()
ResponseList<DirectMessage> getSentDirectMessages(Paging paging)
DirectMessage showDirectMessage(long id) throws TwitterException;
DirectMessage destroyDirectMessage(long id)
DirectMessage sendDirectMessage(long userId, String text)
DirectMessage sendDirectMessage(String screenName, String text)

     */
    @Test
    void testDeprecatedDMAPIs() throws Exception {

        // send dm

        // ensure id1 is not blocking id2, and id2 is following id1
        twitter1.destroyBlock(rwPrivate.id);
        rwPrivateMessage.createFriendship(id1.id);
        String message = "hello " + new Date().toString();
        DirectMessage sent = twitter1.directMessages().sendDirectMessage(rwPrivate.id, message);
        assertEquals(rwPrivate.id, sent.getRecipientId());
        assertEquals(id1.id, sent.getSenderId());
        assertEquals(message, sent.getText());
        String rawJSON = TwitterObjectFactory.getRawJSON(sent);
        assertEquals(sent, TwitterObjectFactory.createDirectMessage(rawJSON));

        // receive dm
        DirectMessage received = rwPrivateMessage.showDirectMessage(sent.getId());
        assertEquals(rwPrivate.id, received.getRecipientId());
        assertEquals(id1.id, received.getSenderId());
        assertEquals(received, TwitterObjectFactory.createDirectMessage(TwitterObjectFactory.getRawJSON(received)));

        // destroy dm
        ResponseList<DirectMessage> directMessages = rwPrivateMessage.getDirectMessages(new Paging(1, 100));
        assertTrue(directMessages.size() > 0);

        rwPrivateMessage.destroyDirectMessage(received.getId());
    }


}
