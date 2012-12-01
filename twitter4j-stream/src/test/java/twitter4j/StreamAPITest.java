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
import twitter4j.auth.AuthorizationFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.async.DispatcherFactory;
import twitter4j.json.DataObjectFactory;

import java.io.InputStream;
import java.util.*;

public class StreamAPITest extends TwitterTestBase implements StatusListener, ConnectionLifeCycleListener {
    protected TwitterStream twitterStream = null;
    protected Properties p = new Properties();
    private long userId;
    private long upToStatusId;
    private StallWarning warning;

    public StreamAPITest(String name) {
        super(name);
    }

//    protected String id, id4, pass, pass4;

    protected void setUp() throws Exception {
        super.setUp();
        twitterStream = new TwitterStreamFactory(new ConfigurationBuilder().setJSONStoreEnabled(true).build()).getInstance();
        twitterStream.setOAuthConsumer(desktopConsumerKey, desktopConsumerSecret);
        twitterStream.setOAuthAccessToken(new AccessToken(id1.accessToken, id1.accessTokenSecret));
        twitterStream.addListener(this);

        this.status = null;
        this.deletionNotice = null;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        twitterStream.shutdown();
    }

    public void testToString() throws Exception {
        new TwitterStreamFactory().getInstance().toString();
    }

    public void testEquality() throws Exception {
        Map map = new HashMap();
        TwitterStream twitterStream1 = new TwitterStreamFactory().getInstance();
        TwitterStream twitterStream2 = new TwitterStreamFactory().getInstance();
        map.put(twitterStream1, "value");
        map.put(twitterStream2, "value");
        assertEquals(2, map.size());
    }

    List<String> received = new ArrayList<String>();
    Object lock = new Object();
    public void testRawStreamListener() throws Exception{
        twitterStream.addListener(new RawStreamListener() {
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
        twitterStream.sample();
        synchronized (lock) {
            lock.wait();
        }
        assertTrue(received.size() > 0);
    }
    public void testNoListener() throws Exception {
        TwitterStream twitterStream;
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.setOAuthConsumer("dummy","dummy");
        twitterStream.setOAuthAccessToken(new AccessToken("dummy", "dummy"));
        try {
            twitterStream.sample();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException expected) {
        }
        try {
            twitterStream.filter(new FilterQuery().track(new String[]{"twitter"}));
            fail("expecting IllegalStateException");
        } catch (IllegalStateException expected) {
        }
        try {
            twitterStream.user();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException expected) {
        }
        try {
            twitterStream.firehose(0);
            fail("expecting IllegalStateException");
        } catch (IllegalStateException expected) {
        }
        try {
            twitterStream.retweet();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException expected) {
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
        } catch (TwitterException te) {

        }
        try {
            stream.next(this);
            waitForNotification();
            fail("expecting IllegalStateException");
        } catch (IllegalStateException ise) {

        }
        is.close();
    }

    public void testSamplePull() throws Exception {
        StatusStream stream = twitterStream.getSampleStream();
        for (int i = 0; i < 10; i++) {
            stream.next(this);
        }
        stream.close();
    }

    public void testSamplePush() throws Exception {
        twitterStream.sample();
        waitForStatus();
        assertTrue(status != null || deletionNotice != null);
        twitterStream.cleanUp();
    }

    public void testShutdownAndRestart() throws Exception {
        twitterStream.sample();
        waitForStatus();
        twitterStream.shutdown();
        twitterStream.shutdown();
        twitterStream.sample();
        waitForStatus();
        twitterStream.cleanUp();
        twitterStream.shutdown();
    }

//    public void testFilterFollowPush() throws Exception {
//        twitterStream.setHttpReadTimeout(Integer.MAX_VALUE);
//        status = null;
//        twitterStream.filter(0, new int[]{18713}, null);
//        waitForStatus();

    //        assertNotNull(status);
//        assertNotNull(status.getUnescapedString());
//        assertTrue("web".equals(status.getSource()) || -1 != status.getSource().indexOf("<a href=\""));
//        twitterStream.cleanup();
//    }
    public void testFilterTrackPush() throws Exception {
        twitterStream.addConnectionLifeCycleListener(this);
        assertFalse(onConnectCalled);
        assertFalse(onDisconnectCalled);
        assertFalse(onCleanUpCalled);

        twitterStream.filter(new FilterQuery(0, null, new String[]{"twitter", "iphone"}));
        waitForStatus();
        assertTrue(onConnectCalled);
        assertFalse(onDisconnectCalled);
        assertFalse(onCleanUpCalled);

        assertNotNull(status.getText());
        assertTrue("web".equals(status.getSource()) || -1 != status.getSource().indexOf("<a href=\""));
        this.ex = null;
        twitterStream.filter(new FilterQuery(0, null).track(new String[]{"twitter4j java", "ipad"}));
        waitForStatus();
        assertNull(ex);

        twitterStream.cleanUp();
        Thread.sleep(1000);

        assertTrue(onConnectCalled);
        assertTrue(onDisconnectCalled);
        assertTrue(onCleanUpCalled);
    }

    public void testFilterIncludesEntities() throws Exception {
        this.ex = null;

        FilterQuery query = new FilterQuery(0, null, new String[]{"http", "#", "@"});
        twitterStream.filter(query);

        boolean sawURL, sawMention, sawHashtag;
        do {
            waitForStatus();
            sawURL = status.getURLEntities().length > 0;
            sawMention = status.getUserMentionEntities().length > 0;
            sawHashtag = status.getHashtagEntities().length > 0;
        } while (!sawURL || !sawMention || !sawHashtag);

        assertNull(ex);

        twitterStream.cleanUp();
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
        try {
            twitterStream = new TwitterStreamFactory().getInstance();
            StatusStream stream = twitterStream.getFirehoseStream(0);
            fail();
        } catch (IllegalStateException ise) {
        } catch (TwitterException te) {

        }
        try {
            twitterStream = new TwitterStreamFactory().getInstance();
            StatusStream stream = twitterStream.getFilterStream(new FilterQuery(new long[]{6358482}));
            fail();
        } catch (IllegalStateException ise) {
        } catch (TwitterException te) {
            // User not in required role
            assertEquals(403, te.getStatusCode());
        }
    }

    private synchronized void notifyResponse() {
        this.notify();
    }

    private synchronized void waitForStatus() {
        try {
            this.wait(Integer.MAX_VALUE);
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
        String json = DataObjectFactory.getRawJSON(status);
        try {
            assertNotNull(json);
            Status statusFromJSON = DataObjectFactory.createStatus(json);
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
