/*
Copyright (c) 2007-2011, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package twitter4j;

import twitter4j.http.AccessToken;
import twitter4j.internal.async.DispatcherFactory;
import twitter4j.json.DataObjectFactory;

import java.io.InputStream;
import java.util.Properties;

public class StreamAPITest extends TwitterTestBase implements StatusListener, ConnectionLifeCycleListener {
    protected TwitterStream twitterStream = null;
    protected Twitter protectedTwitter = null;
    protected Properties p = new Properties();
    private int userId;
    private long upToStatusId;

    public StreamAPITest(String name) {
        super(name);
    }

//    protected String id, id4, pass, pass4;

    protected void setUp() throws Exception {
        super.setUp();
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.setOAuthConsumer(desktopConsumerKey, desktopConsumerSecret);
        twitterStream.setOAuthAccessToken(new AccessToken(id1.accessToken, id1.accessTokenSecret));
        twitterStream.addListener(this);

        protectedTwitter = new TwitterFactory().getInstance(id3.screenName, id3.password);
        this.status = null;
        this.deletionNotice = null;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testStatusStream() throws Exception {
        InputStream is = TwitterTestBase.class.getResourceAsStream("/streamingapi-testcase.json");
        StatusStream stream = new StatusStreamImpl(new DispatcherFactory().getInstance(), is);
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
        assertTrue(null != status || null != deletionNotice);
        twitterStream.cleanUp();
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
        query.setIncludeEntities(true);
        twitterStream.filter(query);

        boolean sawURL, sawMention, sawHashtag;
        do {
            waitForStatus();
            sawURL = status.getURLs().length > 0;
            sawMention = status.getUserMentions().length > 0;
            sawHashtag = status.getHashtags().length > 0;
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
            twitterStream = new TwitterStream();
            StatusStream stream = twitterStream.getFirehoseStream(0);
            fail();
        } catch (IllegalStateException ise) {
        } catch (TwitterException te) {

        }
        try {
            twitterStream = new TwitterStream();
            StatusStream stream = twitterStream.getFilterStream(0, new int[]{6358482}, null);
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
            Status statusFromJSON = DataObjectFactory.createStatus(json);
            assertEquals(status, statusFromJSON);
        } catch (TwitterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

    public void onScrubGeo(int userId, long upToStatusId) {
        this.userId = userId;
        this.upToStatusId = upToStatusId;
        System.out.println("got onScrubGeo");
        notifyResponse();
    }

    Exception ex;

    public void onException(Exception ex) {
        this.ex = ex;
        ex.printStackTrace();
        notifyResponse();
    }
}
