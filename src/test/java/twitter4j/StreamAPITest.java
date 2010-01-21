/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

public class StreamAPITest extends TwitterTestBase implements StatusListener {
    protected TwitterStream twitterStream = null;
    protected Twitter protectedTwitter = null;
    protected Properties p = new Properties();

    public StreamAPITest(String name) {
        super(name);
    }

//    protected String id, id4, pass, pass4;

    protected void setUp() throws Exception {
        super.setUp();
        twitterStream = new TwitterStream(id1.screenName, id1.pass, this);
        protectedTwitter = new TwitterFactory().getBasicAuthorizedInstance(id4.screenName, id4.pass);
        this.status = null;
        this.deletionNotice = null;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testStatusStream() throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/streamingapi-testcase.json");
        StatusStream stream = new StatusStream(fis);
        stream.next(this);
        assertEquals(6832057002l, deletionNotice.getStatusId());
        assertEquals(18378841, deletionNotice.getUserId());
        stream.next(this);
        assertEquals("aaa minha irma ta enchendo aki querendo entra --'", status.getText());
        stream.next(this);
        assertEquals("Acho retartado ter que esperar para usar o script de novo, por isso só uso o Twitter Followers, o site da empresa é: http://bit.ly/5tNlDp", status.getText());
        stream.next(this);
        assertEquals(121564, trackLimit);
        stream.next(this);
        assertEquals("ngantuk banget nguap mulu", status.getText());
        
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
        assertNotNull(status.getText());
        assertTrue("web".equals(status.getSource()) || -1 != status.getSource().indexOf("<a href=\""));
        twitterStream.cleanup();
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
        twitterStream.filter(0,null, new String[]{"twitter", "iphone"});
        waitForStatus();
        assertNotNull(status.getText());
        assertTrue("web".equals(status.getSource()) || -1 != status.getSource().indexOf("<a href=\""));
        twitterStream.cleanup();
    }

    class TestThread extends Thread {
        boolean alive = true;

        public void run() {
            while (alive) {
                String newStatus = "streaming test:" + new Date();
                try {
                    twitterAPI1.updateStatus(newStatus);
                    protectedTwitter.updateStatus(newStatus);
                    Thread.sleep(10000);
                } catch (TwitterException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void shutdown() {
            this.alive = false;
        }
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

    Status status;

    public void onStatus(Status status) {
        this.status = status;
//        System.out.println("got status from stream:" + status.toString());
        assertNotNull(status.getText());
        assertTrue("web".equals(status.getSource()) || -1 != status.getSource().indexOf("<a href=\""));
//        System.out.println(status.getCreatedAt() + ":" + status.getText() + " from:" + status.getSource());
        if(status.getText().startsWith("RT")){
            Status retweetedStatus = status.getRetweetedStatus();
            System.out.println("got a retweet!: id:" + status.getId());
            if(null != retweetedStatus){
                System.out.println("it's an official retweet:" + status.toString());
            } else {
                System.out.println("it's not an official retweet:" + status.toString());
            }
        }
        notifyResponse();
    }
    StatusDeletionNotice deletionNotice;
//onDeletionNoice
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        this.deletionNotice = statusDeletionNotice;
        System.out.println("got status deletionNotice notification:" + statusDeletionNotice.toString());
    }

    int trackLimit;
//onTrackLimitNotice
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        this.trackLimit = numberOfLimitedStatuses;
        System.out.println("got limit notice:" + numberOfLimitedStatuses);
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
        notifyResponse();
    }
}