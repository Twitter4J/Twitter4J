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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.SAME_THREAD)
public class StreamAPITest extends TwitterTestBase implements StatusListener, ConnectionLifeCycleListener {
    private long userId;
    private long upToStatusId;
    private StallWarning warning;


    @BeforeEach
    protected void before() {
        this.status = null;
        this.deletionNotice = null;
    }

    @Test
    void testToString() {
        //noinspection ResultOfMethodCallIgnored
        TwitterStream.newBuilder().load(subProperty(p, "bestFriend1")).onStatus(System.out::println).build().toString();
    }

    @Test
    void testEquality() {
        Map<TwitterStream, String> map = new HashMap<>();
        TwitterStream twitterStream1 = TwitterStream.newBuilder().load(subProperty(p, "bestFriend1")).onStatus(System.out::println).build();
        TwitterStream twitterStream2 = TwitterStream.newBuilder().load(subProperty(p, "bestFriend1")).onStatus(System.out::println).build();
        assertEquals(twitterStream1, twitterStream2);
    }

    final List<String> received = new ArrayList<>();
    final Object lock = new Object();

    @Test
    void testRawStreamListener() throws Exception {
        TwitterStream twitterStream1 = TwitterStream.newBuilder().load(subProperty(p, "bestFriend1")).listener(new RawStreamListener() {
            @Override
            public void onMessage(String rawString) {
                received.add(rawString);
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public void onException(Exception ex) {
            }
        }).build().sample();
        synchronized (lock) {
            lock.wait();
        }
        assertTrue(received.size() > 0);
        twitterStream1.shutdown();
    }

    @Test
    void testNoListener() {
        try {
            TwitterStream.newBuilder()
                    .oAuthConsumer("dummy", "dummy")
                    .oAuthAccessToken("dummy", "dummy")
                    .build();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException ignored) {
        }
    }


    @Test
    void testSample() {
        TwitterStream twitterStream2 = TwitterStream.newBuilder().load(subProperty(p, "id3"))
                .listener(this).build();
        twitterStream2.sample();
        waitForStatus();
        assertTrue(status != null || deletionNotice != null);
        twitterStream2.shutdown();
    }

    @Test
    void testShutdownAndRestart() {
        TwitterStream twitterStream3 = TwitterStream.newBuilder().load(subProperty(p, "id3"))
                .listener(this)
                .build().sample();
        waitForStatus();
        twitterStream3.shutdown();
        twitterStream3.shutdown();
        twitterStream3.sample();
        waitForStatus();
        twitterStream3.cleanUp();
        twitterStream3.shutdown();
    }

    @Test
    void testFilterTrackPush() throws Exception {
        TwitterStream twitterStream1 = TwitterStream.newBuilder().load(subProperty(p, "id2"))
                .listener(this)
                .connectionLifeCycleListener(this).build();
        assertFalse(onConnectCalled);
        assertFalse(onDisconnectCalled);
        assertFalse(onCleanUpCalled);

        twitterStream1.filter(new FilterQuery(0, null, new String[]{"twitter", "iphone"}));
        waitForStatus();
        assertTrue(onConnectCalled);
        assertFalse(onDisconnectCalled);
        assertFalse(onCleanUpCalled);

        assertNotNull(status.getText());
        assertTrue("web".equals(status.getSource()) || status.getSource().contains("<a href=\""));
        this.ex = null;
        twitterStream1.filter(new FilterQuery(0, null).track("twitter4j java", "ipad"));
        waitForStatus();
        assertNull(ex);

        twitterStream1.cleanUp();
        Thread.sleep(1000);

        assertTrue(onConnectCalled);
        assertTrue(onDisconnectCalled);
        assertTrue(onCleanUpCalled);
        twitterStream1.shutdown();
    }

    @Test
    void testFilterIncludesEntities() {
        this.ex = null;

        FilterQuery query = new FilterQuery(0, null, new String[]{"http", "#", "@"});
        TwitterStream twitterStream2 = TwitterStream.newBuilder().load(subProperty(p, "id2"))
                .listener(this).build();
        twitterStream2.filter(query);

        boolean sawURL, sawMention, sawHashtag;
        do {
            waitForStatus();
            sawURL = status.getURLEntities().length > 0;
            sawMention = status.getUserMentionEntities().length > 0;
            sawHashtag = status.getHashtagEntities().length > 0;
        } while (!sawURL || !sawMention || !sawHashtag);

        assertNull(ex);

        twitterStream2.cleanUp();
        twitterStream2.shutdown();
    }

    boolean onConnectCalled = false;
    boolean onDisconnectCalled = false;
    boolean onCleanUpCalled = false;

    public void onConnect() {
        onConnectCalled = true;
    }

    public void onDisconnect() {
        onDisconnectCalled = true;
    }

    public void onCleanUp() {
        onCleanUpCalled = true;
    }

    @Test
    void testUnAuthorizedStreamMethods() {
        TwitterStream twitterStream3 = null;
        try {
            twitterStream3 = TwitterStream.newBuilder().load(subProperty(p, "id2")).listener(this).build();
            StatusStream stream = ((TwitterStreamImpl) twitterStream3).getFirehoseStream(0);
            fail();
        } catch (IllegalStateException | TwitterException ignored) {
        }
        try {
            twitterStream3 = TwitterStream.getInstance();
            StatusStream stream = ((TwitterStreamImpl) twitterStream3).getFilterStream(new FilterQuery(6358482L));
            fail();
        } catch (IllegalStateException ignored) {
        } catch (TwitterException te) {
            // User not in required role
            assertEquals(403, te.getStatusCode());
        }
        twitterStream3.shutdown();
    }

    private synchronized void notifyResponse() {
        this.notify();
    }

    private synchronized void waitForStatus() {
        try {
            this.wait(3000);
            System.out.println("notified.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void waitForNotification() {
        try {
            this.wait(3000);
            System.out.println("notified.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Status status;

    public void onStatus(Status status) {
        this.status = status;
        String json = TwitterObjectFactory.getRawJSON(status);
        try {
            assertNotNull(json);
            Status statusFromJSON = TwitterObjectFactory.createStatus(json);
            assertEquals(status, statusFromJSON);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
//        System.out.println("got status from stream:" + status.toString());
        assertNotNull(status.getText());
//        System.out.println(status.getCreatedAt() + ":" + status.getText() + " from:" + status.getSource());
        notifyResponse();
    }

    StatusDeletionNotice deletionNotice;

    //onDeletionNoice
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        this.deletionNotice = statusDeletionNotice;
        System.out.println("got status deletionNotice notification:" + statusDeletionNotice.toString());
        notifyResponse();
    }

    int trackLimit;

    //onTrackLimitNotice
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        this.trackLimit = numberOfLimitedStatuses;
        System.out.println("got limit notice:" + numberOfLimitedStatuses);
        notifyResponse();
    }

    public void onScrubGeo(long userId, long upToStatusId) {
        this.userId = userId;
        this.upToStatusId = upToStatusId;
        System.out.println("got onScrubGeo");
        notifyResponse();
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        this.warning = warning;
    }

    Exception ex;

    public void onException(Exception ex) {
        this.ex = ex;
        ex.printStackTrace();
        notifyResponse();
    }
}
