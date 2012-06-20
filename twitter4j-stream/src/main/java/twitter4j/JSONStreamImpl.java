package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JSONStreamImpl extends AbstractStreamImplementation implements JSONStream {

    protected String line;

    protected StreamListener[] listeners;

    JSONStreamImpl(Dispatcher dispatcher, InputStream stream, Configuration conf) throws IOException {
        super(dispatcher, stream, conf);
    }

    JSONStreamImpl(Dispatcher dispatcher, HttpResponse response, Configuration conf) throws IOException {
        super(dispatcher, response, conf);
    }

    public void next(JSONListener listener) throws TwitterException  {
        StreamListener[] list = new StreamListener[1];
        list[0] = listener;
        this.listeners = list;
        handleNextElement();
    }

    public void next(StreamListener[] listeners) throws TwitterException {
        this.listeners = listeners;
        handleNextElement();
    }

    public void onException(Exception e) {
        for (StreamListener listener : listeners) {
            listener.onException(e);
        }
    }

    @Override
    protected void onMessage(JSONObject json) {
        for (StreamListener listener : listeners) {
            ((JSONListener) listener).onMessage(json);
        }
    }
}
