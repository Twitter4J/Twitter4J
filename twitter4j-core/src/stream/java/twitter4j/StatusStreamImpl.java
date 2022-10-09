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

import twitter4j.v1.RawStreamListener;
import twitter4j.v1.StatusListener;
import twitter4j.v1.StreamListener;

import java.io.IOException;
import java.util.List;

/**
 * StatusStream implementation. This class is NOT intended to be extended but left non-final for the ease of mock testing.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
class StatusStreamImpl extends StatusStreamBase {
    StatusStreamImpl(HttpResponse response, List<StreamListener> streamListeners
            , List<RawStreamListener> rawStreamListeners, boolean jsonStoreEnabled, boolean prettyDebug) throws IOException {
        super(response, streamListeners
                , rawStreamListeners, jsonStoreEnabled, prettyDebug);
    }

    String line;

    @Override
    protected void onClose() {
    }

    @Override
    public void next(List<StreamListener> listeners) throws TwitterException {
        handleNextElement(listeners, null);
    }

    @Override
    public void next(List<StreamListener> listeners, List<RawStreamListener> rawStreamListeners) throws TwitterException {
        handleNextElement(listeners, rawStreamListeners);
    }

    @Override
    protected String parseLine(String line) {
        this.line = line;
        return line;
    }

    @Override
    protected void onMessage(String rawString, RawStreamListener listener) {
        if (listener != null) {
            listener.onMessage(rawString);
        }
    }

    @Override
    protected void onStatus(JSONObject json, List<StreamListener> listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onStatus(asStatus(json));
        }
    }

    @Override
    protected void onDelete(JSONObject json, List<StreamListener> listeners) throws JSONException {
        JSONObject deletionNotice = json.getJSONObject("delete");
        if (deletionNotice.has("status")) {
            for (StreamListener listener : listeners) {
                ((StatusListener) listener).onDeletionNotice(new StatusDeletionNoticeImpl(deletionNotice.getJSONObject("status")));
            }
        }
    }

    @Override
    protected void onLimit(JSONObject json, List<StreamListener> listeners) throws JSONException {
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onTrackLimitationNotice(ParseUtil.getInt("track", json.getJSONObject("limit")));
        }
    }

    @Override
    protected void onStallWarning(JSONObject json, List<StreamListener> listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onStallWarning(new StallWarning(json));
        }
    }

    @Override
    protected void onScrubGeo(JSONObject json, List<StreamListener> listeners) throws TwitterException, JSONException {
        JSONObject scrubGeo = json.getJSONObject("scrub_geo");
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onScrubGeo(ParseUtil.getLong("user_id", scrubGeo)
                    , ParseUtil.getLong("up_to_status_id", scrubGeo));
        }

    }

    @Override
    public void onException(Exception e, List<StreamListener> listeners) {
        for (StreamListener listener : listeners) {
            listener.onException(e);
        }
    }
}
