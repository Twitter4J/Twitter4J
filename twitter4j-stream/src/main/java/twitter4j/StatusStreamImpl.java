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

import twitter4j.conf.Configuration;
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.json.z_T4JInternalParseUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * StatusStream implementation. This class is NOT intended to be extended but left non-final for the ease of mock testing.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
class StatusStreamImpl extends StatusStreamBase {
    /*package*/

    StatusStreamImpl(Dispatcher dispatcher, InputStream stream, Configuration conf) throws IOException {
        super(dispatcher, stream, conf);
    }
    /*package*/

    StatusStreamImpl(Dispatcher dispatcher, HttpResponse response, Configuration conf) throws IOException {
        super(dispatcher, response, conf);
    }

    protected String line;

    protected static final RawStreamListener[] EMPTY = new RawStreamListener[0];
    @Override
    public void next(StatusListener listener) throws TwitterException {
        handleNextElement(new StatusListener[]{listener}, EMPTY);
    }

    @Override
    public void next(StreamListener[] listeners, RawStreamListener[] rawStreamListeners) throws TwitterException {
        handleNextElement(listeners, rawStreamListeners);
    }

    protected String parseLine(String line) {
        this.line = line;
        return line;
    }

    @Override
    protected void onMessage(String rawString, RawStreamListener[] listeners) throws TwitterException {
        for (RawStreamListener listener : listeners) {
            listener.onMessage(rawString);
        }
    }

    @Override
    protected void onStatus(JSONObject json, StreamListener[] listeners) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onStatus(asStatus(json));
        }
    }

    @Override
    protected void onDelete(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            JSONObject deletionNotice = json.getJSONObject("delete");
            if (deletionNotice.has("status")) {
                ((StatusListener) listener).onDeletionNotice(new StatusDeletionNoticeImpl(deletionNotice.getJSONObject("status")));
            } else {
                JSONObject directMessage = deletionNotice.getJSONObject("direct_message");
                ((UserStreamListener) listener).onDeletionNotice(z_T4JInternalParseUtil.getLong("id", directMessage)
                        , z_T4JInternalParseUtil.getLong("user_id", directMessage));
            }
        }
    }

    @Override
    protected void onLimit(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onTrackLimitationNotice(z_T4JInternalParseUtil.getInt("track", json.getJSONObject("limit")));
        }
    }

    @Override
    protected void onStallWarning(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onStallWarning(new StallWarning(json));
        }
    }

    @Override
    protected void onScrubGeo(JSONObject json, StreamListener[] listeners) throws TwitterException, JSONException {
        JSONObject scrubGeo = json.getJSONObject("scrub_geo");
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onScrubGeo(z_T4JInternalParseUtil.getLong("user_id", scrubGeo)
                    , z_T4JInternalParseUtil.getLong("up_to_status_id", scrubGeo));
        }

    }

    @Override
    public void onException(Exception e, StreamListener[] listeners) {
        for (StreamListener listener : listeners) {
            listener.onException(e);
        }
    }
}
