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
import twitter4j.v1.DirectMessage;
import twitter4j.v1.DirectMessageList;
import twitter4j.v1.QuickReply;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
@Execution(ExecutionMode.CONCURRENT)
class DirectMessagesResourcesTest extends TwitterTestBase {
    @Test
    void testQuickResponse() throws Exception {
        String message = "hello! message with quick reply " + LocalDateTime.now();
        DirectMessage directMessage = rwPrivateMessage.v1().directMessages().sendDirectMessage(id1.id, message,
                QuickReply.of("quick response label1", "quick response description1", "metadata1"),
                QuickReply.of("quick response label2", "quick response description2", "metadata2"));
        assertTrue(Math.abs(directMessage.getCreatedAt().toEpochSecond(ZoneOffset.UTC)
                - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) < 10);

        twitter1.v1().directMessages().sendDirectMessage(rwPrivate.id, "quick response label1" + System.currentTimeMillis(), "metadata1");
    }

    @Test
    void testNewDMAPIs() throws Exception {

        // send dm

        // ensure id1 is not blocking id2, and id2 is following id1
        twitter1.v1().users().destroyBlock(rwPrivate.id);
        rwPrivateMessage.v1().friendsFollowers().createFriendship(id1.id);
        String message = "hello " + LocalDateTime.now();
        DirectMessage sent = twitter1.v1().directMessages().sendDirectMessage(rwPrivate.id, message);
        assertEquals(rwPrivate.id, sent.getRecipientId());
        assertEquals(id1.id, sent.getSenderId());
        assertEquals(message, sent.getText());
        assertEquals(sent, TwitterObjectFactory.createDirectMessage(TwitterObjectFactory.getRawJSON(sent)));

        // receive dm
        DirectMessage received = rwPrivateMessage.v1().directMessages().showDirectMessage(sent.getId());
        assertEquals(rwPrivate.id, received.getRecipientId());
        assertEquals(id1.id, received.getSenderId());
        assertEquals(received, TwitterObjectFactory.createDirectMessage(TwitterObjectFactory.getRawJSON(received)));

        // destroy dm
        DirectMessageList directMessages = rwPrivateMessage.v1().directMessages().getDirectMessages(100);
        assertTrue(directMessages.size() > 0);

        // message with quick reply

        DirectMessage directMessageWithQuickReplies = twitter1.v1().directMessages().sendDirectMessage(rwPrivate.id, "hello" + LocalDateTime.now(), QuickReply.of("らべる１", "説明1", "めたでーた1")
                , QuickReply.of("label2", "description 2", "metadata 2")
                , QuickReply.of("label3", "description 3", null));
        QuickReply[] quickReplies = directMessageWithQuickReplies.getQuickReplies();
        assertEquals(3, quickReplies.length);
        assertEquals(QuickReply.of("らべる１", "説明1", "めたでーた1"), quickReplies[0]);
        assertNull(quickReplies[2].metadata);

        rwPrivateMessage.v1().directMessages().destroyDirectMessage(received.getId());
    }

}
