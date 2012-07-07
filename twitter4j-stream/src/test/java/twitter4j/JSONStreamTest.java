package twitter4j;

import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.async.DispatcherFactory;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.json.DataObjectFactory;

import java.io.InputStream;
import java.util.Properties;

public class JSONStreamTest extends TwitterTestBase implements JSONListener, ConnectionLifeCycleListener {
    protected TwitterStream twitterStream = null;
    protected Properties p = new Properties();

    public JSONStreamTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        twitterStream = new TwitterStreamFactory(new ConfigurationBuilder().setJSONStoreEnabled(true).build()).getInstance();
        twitterStream.setOAuthConsumer(desktopConsumerKey, desktopConsumerSecret);
        twitterStream.setOAuthAccessToken(new AccessToken(id1.accessToken, id1.accessTokenSecret));
        twitterStream.addListener(this);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testJSONStream() throws Exception {
        InputStream is = TwitterTestBase.class.getResourceAsStream("/streamingapi-testcase.json");
        JSONStream stream = new JSONStreamImpl(new DispatcherFactory().getInstance(), is, conf1);
        stream.next(this);
        waitForNotification();
        StatusDeletionNotice deletion = (StatusDeletionNotice) DataObjectFactory.createObject(message.toString());
        assertEquals(6832057002l, deletion.getStatusId());
        assertEquals(18378841, deletion.getUserId());
        stream.next(this);
        waitForNotification();
        Status status = DataObjectFactory.createStatus(message.toString());
        assertEquals("aaa minha irma ta enchendo aki querendo entra --'", status.getText());
        stream.next(this);
        waitForNotification();
        status = DataObjectFactory.createStatus(message.toString());
        assertEquals("Acho retartado ter que esperar para usar o script de novo, por isso só uso o Twitter Followers, o site da empresa é: http://bit.ly/5tNlDp", status.getText());
        stream.next(this);
        waitForNotification();
        // tracklimit
        stream.next(this);
        waitForNotification();
        status = DataObjectFactory.createStatus(message.toString());
        assertEquals("ngantuk banget nguap mulu", status.getText());
        stream.next(this);
        waitForNotification();
        // scrubGeo
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
        JSONStream stream = twitterStream.getJSONStream("statuses/sample.json");
        for (int i = 0; i < 10; i++) {
            stream.next(this);
        }
        stream.close();
    }

    private synchronized void waitForNotification() {
        try {
            this.wait(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    JSONObject message;

    @Override
    public void onMessage(JSONObject message) {
        this.message = message;
        assertNotNull(message);
        notifyResponse();
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

    Exception ex;

    public void onException(Exception ex) {
        this.ex = ex;
        ex.printStackTrace();
        notifyResponse();
    }

    private synchronized void notifyResponse() {
        this.notify();
    }

}
