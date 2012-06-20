package twitter4j;

import twitter4j.internal.org.json.JSONObject;

interface JSONListener extends StreamListener {
    void onMessage(JSONObject event);
}
