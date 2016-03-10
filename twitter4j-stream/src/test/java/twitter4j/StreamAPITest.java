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

import twitter4j.auth.AccessToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamAPITest extends TwitterTestBase implements StatusListener, ConnectionLifeCycleListener {
    private long userId;
    private long upToStatusId;
    private StallWarning warning;

    public StreamAPITest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.status = null;
        this.deletionNotice = null;
    }

    public void testToString() throws Exception {
        new TwitterStreamFactory().getInstance().toString();
    }

    public void testEquality() throws Exception {
        Map<TwitterStream, String> map = new HashMap<TwitterStream, String>();
        TwitterStream twitterStream1 = new TwitterStreamFactory().getInstance();
        TwitterStream twitterStream2 = new TwitterStreamFactory().getInstance();
        map.put(twitterStream1, "value");
        map.put(twitterStream2, "value");
        assertEquals(2, map.size());
    }

    final List<String> received = new ArrayList<String>();
    final Object lock = new Object();

    public void testRawStreamListener() throws Exception {
        TwitterStream twitterStream1 = new TwitterStreamFactory(bestFriend1Conf).getInstance();
        twitterStream1.addListener(new RawStreamListener() {
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
        });
        twitterStream1.sample();
        synchronized (lock) {
            lock.wait();
        }
        assertTrue(received.size() > 0);
        twitterStream1.shutdown();
    }

    public void testNoListener() throws Exception {
        TwitterStream twitterStream;
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.setOAuthConsumer("dummy", "dummy");
        twitterStream.setOAuthAccessToken(new AccessToken("dummy", "dummy"));
        try {
            twitterStream.sample();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException ignored) {
        }
        try {
            twitterStream.filter(new FilterQuery().track(new String[]{"twitter"}));
            fail("expecting IllegalStateException");
        } catch (IllegalStateException ignored) {
        }
        try {
            twitterStream.user();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException ignored) {
        }
        try {
            twitterStream.firehose(0);
            fail("expecting IllegalStateException");
        } catch (IllegalStateException ignored) {
        }
        try {
            twitterStream.retweet();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException ignored) {
        }

        twitterStream.addListener(new RawStreamListener() {
            @Override
            public void onMessage(String rawString) {
            }

            @Override
            public void onException(Exception ex) {
            }
        });

        twitterStream.sample();
        twitterStream.cleanUp();
        twitterStream.shutdown();
    }

    public void testStatusStream() throws Exception {
        InputStream is = TwitterTestBase.class.getResourceAsStream("/streamingapi-testcase.json");
        StatusStream stream = new StatusStreamImpl(new DispatcherFactory().getInstance(), is, conf1);
        stream.next(this);
        waitForNotification();
        assertEquals(6832057002l, deletionNotice.getStatusId());
        assertEquals(18378841, deletionNotice.getUserId());
        stream.next(this);
        waitForNotification();
        assertEquals("aaa minha irma ta enchendo aki querendo entra --'", status.getText());
        stream.next(this);
        waitForNotification();
        assertEquals("Acho retartado ter que esperar para usar o script de novo, por isso só uso o Twitter Followers, o site da empresa é: http://bit.ly/5tNlDp", status.getText());
        stream.next(this);
        waitForNotification();
        assertEquals(121564, trackLimit);
        stream.next(this);
        waitForNotification();
        assertEquals("ngantuk banget nguap mulu", status.getText());
        stream.next(this);
        waitForNotification();
        assertEquals(14090452, userId);
        assertEquals(23260136625l, upToStatusId);
        try {
            stream.next(this);
            waitForNotification();
            fail("expecting TwitterException");
        } catch (TwitterException ignored) {

        }
        try {
            stream.next(this);
            waitForNotification();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException ignored) {

        }
        is.close();
    }

    public void testSample() throws Exception {
        TwitterStream twitterStream2 = new TwitterStreamFactory(conf3).getInstance();
        twitterStream2.addListener(this);
        twitterStream2.sample();
        waitForStatus();
        assertTrue(status != null || deletionNotice != null);
        final List<Status> statuses = new ArrayList<Status>();
        StatusListener listener = new StatusAdapter() {
            @Override
            public synchronized void onStatus(Status status) {
                statuses.add(status);
                this.notifyAll();
            }

        };
        twitterStream2.replaceListener(this, listener);
        waitForStatus();
        status = null;
        waitForStatus();
        assertTrue(statuses.size() > 0);
        assertNull("ensure that original listener doesn't receive any statuses", status);
        twitterStream2.shutdown();
    }

    public void testShutdownAndRestart() throws Exception {
        TwitterStream twitterStream3 = new TwitterStreamFactory(conf3).getInstance();
        twitterStream3.addListener(this);
        twitterStream3.sample();
        waitForStatus();
        twitterStream3.shutdown();
        twitterStream3.shutdown();
        twitterStream3.sample();
        waitForStatus();
        twitterStream3.cleanUp();
        twitterStream3.shutdown();
    }

    public void testFilterTrackPush() throws Exception {
        TwitterStream twitterStream1 = new TwitterStreamFactory(conf2).getInstance();
        twitterStream1.addListener(this);
        twitterStream1.addConnectionLifeCycleListener(this);
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
        twitterStream1.filter(new FilterQuery(0, null).track(new String[]{"twitter4j java", "ipad"}));
        waitForStatus();
        assertNull(ex);

        twitterStream1.cleanUp();
        Thread.sleep(1000);

        assertTrue(onConnectCalled);
        assertTrue(onDisconnectCalled);
        assertTrue(onCleanUpCalled);
        twitterStream1.shutdown();
    }

    public void testFilterIncludesEntities() throws Exception {
        this.ex = null;

        FilterQuery query = new FilterQuery(0, null, new String[]{"http", "#", "@"});
        TwitterStream twitterStream2 = new TwitterStreamFactory(conf2).getInstance();
        twitterStream2.addListener(this);
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

    public void testUnAuthorizedStreamMethods() throws Exception {
        TwitterStream twitterStream3 = null;
        try {
            twitterStream3 = new TwitterStreamFactory(conf2).getInstance();
            twitterStream3.addListener(this);
            twitterStream3 = new TwitterStreamFactory().getInstance();
            StatusStream stream = ((TwitterStreamImpl) twitterStream3).getFirehoseStream(0);
            fail();
        } catch (IllegalStateException ignored) {
        } catch (TwitterException ignored) {

        }
        try {
            twitterStream3 = new TwitterStreamFactory().getInstance();
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
            this.wait(2000);
            System.out.println("notified.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void waitForNotification() {
        try {
            this.wait(2000);
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
