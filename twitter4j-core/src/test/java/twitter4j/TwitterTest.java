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

import junit.framework.Assert;
import twitter4j.json.DataObjectFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterTest extends TwitterTestBase {

    public TwitterTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetScreenName() throws Exception {
        assertEquals(id1.screenName, twitter1.getScreenName());
        assertEquals(id1.id, twitter1.getId());
    }

    RateLimitStatus rateLimitStatus = null;
    boolean accountLimitStatusAcquired;
    boolean ipLimitStatusAcquired;

    //need to think of a way to test this, perhaps mocking out Twitter is the way to go
    public void testRateLimitStatus() throws Exception {
        Map<String, RateLimitStatus> rateLimitStatus = twitter1.getRateLimitStatus();
        assertNotNull(DataObjectFactory.getRawJSON(rateLimitStatus));
        assertEquals(rateLimitStatus, DataObjectFactory.createRateLimitStatus(DataObjectFactory.getRawJSON(rateLimitStatus)));
        RateLimitStatus status = rateLimitStatus.values().iterator().next();
        assertTrue(10 < status.getLimit());
        assertTrue(10 < status.getRemaining());
        assertTrue(0 < status.getSecondsUntilReset());

        rateLimitStatus = twitter1.getRateLimitStatus("block", "statuses");
        assertTrue(rateLimitStatus.values().size() < 10);

        twitter1.addRateLimitStatusListener(new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                System.out.println("onRateLimitStatus" + event);
                accountLimitStatusAcquired = event.isAccountRateLimitStatus();
                ipLimitStatusAcquired = event.isIPRateLimitStatus();
                TwitterTest.this.rateLimitStatus = event.getRateLimitStatus();
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {

            }

        });
        // the listener doesn't implement serializable and deserialized form should not be equal to the original object
        assertDeserializedFormIsNotEqual(twitter1);

        twitter1.addRateLimitStatusListener(new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                accountLimitStatusAcquired = event.isAccountRateLimitStatus();
                ipLimitStatusAcquired = event.isIPRateLimitStatus();
                TwitterTest.this.rateLimitStatus = event.getRateLimitStatus();
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        });
        // the listener doesn't implement serializable and deserialized form should not be equal to the original object
        assertDeserializedFormIsNotEqual(twitter1);

        twitter1.getMentions();
        assertTrue(accountLimitStatusAcquired);
        assertFalse(ipLimitStatusAcquired);
        RateLimitStatus previous = this.rateLimitStatus;
        twitter1.getMentions();
        assertTrue(accountLimitStatusAcquired);
        assertFalse(ipLimitStatusAcquired);
        assertTrue(previous.getRemaining() > this.rateLimitStatus.getRemaining());
        assertEquals(previous.getLimit(), this.rateLimitStatus.getLimit());
    }

    public void testGetAccessLevel() throws Exception {
        TwitterResponse response;
        response = twitter1.verifyCredentials();
        assertEquals(TwitterResponse.READ_WRITE, response.getAccessLevel());
        response = rwPrivateMessage.verifyCredentials();
        assertEquals(TwitterResponse.READ_WRITE_DIRECTMESSAGES, response.getAccessLevel());
    }

    public static Object assertDeserializedFormIsNotEqual(Object obj) throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
        oos.writeObject(obj);
        byteOutputStream.close();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(byteInputStream);
        Object that = ois.readObject();
        byteInputStream.close();
        ois.close();
        Assert.assertFalse(obj.equals(that));
        return that;
    }
}
