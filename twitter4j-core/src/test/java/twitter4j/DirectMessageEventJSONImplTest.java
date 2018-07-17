package twitter4j;

import junit.framework.TestCase;
import twitter4j.conf.Configuration;
import twitter4j.util.StringTestUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class DirectMessageEventJSONImplTest extends TestCase {

    public void testGetBasicDirectMessage() throws JSONException, TwitterException {
        String jsonString = "{\"type\":\"message_create\",\"id\":\"12345678987654242\",\"created_timestamp\":\"1530689611795\",\"message_create\":{\"target\":{\"recipient_id\":\"5432167\"},\"sender_id\":\"87654321\",\"source_app_id\":\"83263454\",\"message_data\":{\"text\":\"another test\",\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[]}}}}";
        JSONObject json = new JSONObject(jsonString);
        DirectMessageEventJSONImpl directMessageEvent = new DirectMessageEventJSONImpl(json);
        assertEquals("another test", directMessageEvent.getText());
        assertEquals(87654321l, directMessageEvent.getSenderId());
        assertEquals(5432167l, directMessageEvent.getRecipientId());
        assertEquals(1530689611795l, directMessageEvent.getCreatedAt().getTime());
        assertEquals(12345678987654242l, directMessageEvent.getId());
    }

    public void testGetBasicDirectMessageFromList() throws JSONException, TwitterException, UnsupportedEncodingException {
        HttpClientConfiguration conf = mock(HttpClientConfiguration.class);
        HttpResponse response = new MockitoHttpResponse(conf, getInputStreamFromFile("basic-directmessageevent-list"));
        Configuration configuration = mock(Configuration.class);
        PagableResponseList<DirectMessageEvent> responseList = DirectMessageEventJSONImpl.createDirectMessageList(response, configuration);

        ArrayList<Long> expectedIds = new ArrayList<Long>();
        expectedIds.add(1324354657687980970l);
        expectedIds.add(5443423141322313424l);
        expectedIds.add(1322314313121313243l);
        expectedIds.add(1342132521534523642l);
        expectedIds.add(1324375735232546432l);

        for(DirectMessageEvent directMessageEvent:responseList) {
            System.out.println(directMessageEvent.getId());
            //assertTrue(expectedIds.contains(directMessageEvent.getId()));
        }

        assertEquals(expectedIds.size(), responseList.size());
        assertFalse(responseList.isStringCursor());
        assertFalse(responseList.hasNext());
        assertFalse(responseList.hasPrevious());
    }

    public void testGetBasicDirectMessageFromListWithCursor() throws JSONException, TwitterException, UnsupportedEncodingException {
        HttpClientConfiguration conf = mock(HttpClientConfiguration.class);
        HttpResponse response = new MockitoHttpResponse(conf, getInputStreamFromFile("basic-directmessageevent-list-withcursor"));
        Configuration configuration = mock(Configuration.class);
        PagableResponseList<DirectMessageEvent> responseList = DirectMessageEventJSONImpl.createDirectMessageList(response, configuration);

        ArrayList<Long> expectedIds = new ArrayList<Long>();
        expectedIds.add(1324354657687980970l);
        expectedIds.add(1014440055329390596l);

        for(DirectMessageEvent directMessageEvent:responseList) {
           assertTrue(expectedIds.contains(directMessageEvent.getId()));
        }

        assertEquals(expectedIds.size(), responseList.size());

        assertTrue(responseList.isStringCursor());
        assertEquals("MTAxNDQ0MDA1NTMyOTM5MDU5Ng", responseList.getStringNextCursor());
        assertTrue(responseList.hasNext());
        assertFalse(responseList.hasPrevious());
    }

    public void testGetBasicDirectMessageWithEnclosingEvent() throws JSONException, TwitterException, IOException {
        String rawJson = StringTestUtil.fromInputStream(getInputStreamFromFile("basic-directmessageevent-attachment"));
        JSONObject json = new JSONObject(rawJson);

        DirectMessageEvent directMessageEvent = new DirectMessageEventJSONImpl(json);
        assertNotNull(directMessageEvent);
        assertEquals(1324354657687980970l, directMessageEvent.getId());
    }


    private InputStream getInputStreamFromFile(String jsonFilename) {
        return getClass().getResourceAsStream("/" + jsonFilename + ".json");
    }

    class MockitoHttpResponse extends HttpResponse {

        MockitoHttpResponse(HttpClientConfiguration conf, InputStream is) {
            super(conf);
            this.is = is;
        }

        @Override
        public String getResponseHeader(String name) {
            return null;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return null;
        }

        @Override
        public void disconnect() throws IOException {

        }
    }
}
