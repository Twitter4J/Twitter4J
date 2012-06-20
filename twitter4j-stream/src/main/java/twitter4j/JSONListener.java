package twitter4j;

import twitter4j.internal.org.json.JSONObject;

public interface JSONListener extends StreamListener {
    void onMessage(JSONObject event);
}
