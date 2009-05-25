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

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

public class StreamAPITest extends TestCase implements StatusListener{
    protected TwitterStream twitterStream = null;
    protected Twitter twitter = null;
    protected Twitter protectedTwitter = null;
    protected Properties p = new Properties();

    public StreamAPITest(String name) {
        super(name);
    }
    protected String id,id4, pass,pass4;
    protected void setUp() throws Exception {
        super.setUp();
        p.load(new FileInputStream("test.properties"));
        id = p.getProperty("id1");
        pass = p.getProperty("pass1");
        id4 = p.getProperty("id4");
        pass4 = p.getProperty("pass4");
        twitterStream = new TwitterStream(id, pass, this);
        twitter = new Twitter(id, pass);
        protectedTwitter = new Twitter(id4, pass4);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSpritzerPull() throws Exception {
        StatusStream stream = twitterStream.getSpritzerStream();
        Status status;
        for(int i=0;i<10;i++){
            status = stream.next();
            assertNotNull(status.getText());
            assertTrue("web".equals(status.getSource()) || -1 != status.getSource().indexOf("<a href=\""));
            System.out.println(status.getCreatedAt()+":"+status.getText()+" from:"+status.getSource());
        }
        stream.close();
    }
    public void testSpritzerPush() throws Exception {
        twitterStream.spritzer();
        waitForStatus();
        assertNotNull(status.getText());
        assertTrue("web".equals(status.getSource()) || -1 != status.getSource().indexOf("<a href=\""));
        twitterStream.cleanup();
    }
    public void testFollowPull() throws Exception {
        twitterStream.setHttpReadTimeout(120000);
        StatusStream stream = twitterStream.getFollowStream(new int[]{6358482});
        Thread.sleep(3000);
        String newStatus = "streaming test:" + new Date();
        twitter.updateStatus(newStatus);
        Thread.sleep(3000);
        newStatus = "streaming test:" + new Date();
        twitter.updateStatus(newStatus);

        Status status;
        status = stream.next();
        assertTrue(-1 != status.getText().indexOf("streaming test"));
        assertTrue(-1 != status.getSource().indexOf("Twitter4J"));
        stream.close();
    }
    public void testFollowPush() throws Exception {
        status = null;
        twitterStream.follow(new int[]{6358482,42419133});
        String newStatus = "streaming test:" + new Date();
        twitter.updateStatus(newStatus);
        Thread.sleep(3000);
        newStatus = "streaming test:" + new Date();
        twitter.updateStatus(newStatus);
        protectedTwitter.updateStatus(newStatus);

        waitForStatus();
        assertNotNull(status.getText());
        assertTrue("web".equals(status.getSource()) || -1 != status.getSource().indexOf("<a href=\""));
        twitterStream.cleanup();
    }
    public void testUnAuthorizedStreamMethods() throws Exception {
        try{
            StatusStream stream = twitterStream.getFirehoseStream(0);
            fail();
        }catch(TwitterException te){

        }
        try{
            StatusStream stream = twitterStream.getGardenhoseStream();
            fail();
        }catch(TwitterException te){
            // User not in required role
            assertEquals(403, te.getStatusCode());
        }
        try{
            StatusStream stream = twitterStream.getBirddogStream(0, new int[]{6358482});
            fail();
        }catch(TwitterException te){
            // User not in required role
            assertEquals(403, te.getStatusCode());
        }
        try{
            StatusStream stream = twitterStream.getShadowStream(0, new int[]{6358482});
            fail();
        }catch(TwitterException te){
            // User not in required role
            assertEquals(403, te.getStatusCode());
        }
    }
    private synchronized void notifyResponse(){
        this.notify();
    }
    private synchronized void waitForStatus(){
        try {
            this.wait(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Status status;

    public void onStatus(Status status) {
        this.status = status;
        System.out.println("got status-------------------:" + status);
        notifyResponse();
    }

    public void onException(Exception ex) {
        notifyResponse();
    }
}
