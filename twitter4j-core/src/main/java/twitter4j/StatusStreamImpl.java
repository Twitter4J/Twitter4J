/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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

import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.util.ParseUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * StatusStream implementation. This class is NOT intended to be extended but left non-final for the ease of mock testing.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
class StatusStreamImpl extends AbstractStreamImplementation implements StatusStream {
    /*package*/

    StatusStreamImpl(InputStream stream) throws IOException {
        super(stream);
    }
    /*package*/

    StatusStreamImpl(HttpResponse response) throws IOException {
        super(response);
    }

    protected String line;

    protected StreamListener[] listeners;

    /**
     * Reads next status from this stream.
     * @param listener a StatusListener implementation
     * @throws TwitterException when the end of the stream has been reached.
     * @throws IllegalStateException when the end of the stream had been reached.
     */
    public void next(StatusListener listener) throws TwitterException {
        StreamListener[] list = new StreamListener[1];
        list[0] = listener;
        this.listeners = list;
        handleNextElement();
    }
    public void next(StreamListener[] listeners) throws TwitterException{
        this.listeners = listeners;
        handleNextElement();
    }

    protected String parseLine(String line){
        DataObjectFactoryUtil.clearThreadLocalMap();
        this.line = line;
        return line;
    }

    @Override
    protected void onStatus(JSONObject json) throws TwitterException {
        for (StreamListener listener : listeners) {
            ((StatusListener)listener).onStatus(asStatus(json));
        }
    }
    @Override
    protected void onDelete(JSONObject json) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            JSONObject deletionNotice = json.getJSONObject("delete");
            if(deletionNotice.has("status")){
                ((StatusListener)listener).onDeletionNotice(new StatusDeletionNoticeImpl(deletionNotice.getJSONObject("status")));
            }else{
                JSONObject directMessage = deletionNotice.getJSONObject("direct_message");
                ((UserStreamListener)listener).onDeletionNotice(ParseUtil.getInt("id", directMessage)
                        , ParseUtil.getInt("user_id", directMessage));
            }
        }
    }
    @Override
    protected void onLimit(JSONObject json) throws TwitterException, JSONException {
        for (StreamListener listener : listeners) {
            ((StatusListener)listener).onTrackLimitationNotice(ParseUtil.getInt("track", json.getJSONObject("limit")));
        }
    }

    @Override
    protected void onScrubGeo(JSONObject json) throws TwitterException, JSONException {
        JSONObject scrubGeo = json.getJSONObject("scrub_geo");
        for (StreamListener listener : listeners) {
            ((StatusListener) listener).onScrubGeo(ParseUtil.getInt("user_id", scrubGeo)
                    , ParseUtil.getLong("up_to_status_id", scrubGeo));
        }

    }

    @Override
    public void onException(Exception e) {
        for (StreamListener listener : listeners) {
            listener.onException(e);
        }
    }
}
