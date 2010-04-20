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
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * StatusStream implementation. This class is NOT intended to be extended but left non-final for the ease of mock testing.
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
class StatusStreamImpl implements StatusStream {
    private static final Logger logger = Logger.getLogger(StatusStreamImpl.class);

    private boolean streamAlive = true;
    private BufferedReader br;
    private InputStream is;
    private HttpResponse response;

    /*package*/

    StatusStreamImpl(InputStream stream) throws IOException {
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    }
    /*package*/

    StatusStreamImpl(HttpResponse response) throws IOException {
        this(response.asStream());
        this.response = response;
    }

    /**
     * {@inheritDoc}
     */
    public void next(StatusListener listener) throws TwitterException {
        if (!streamAlive) {
            throw new IllegalStateException("Stream already closed.");
        }
        try {
            String line;
            line = br.readLine();
            if (null == line) {
                //invalidate this status stream
                throw new IOException("the end of the stream has been reached");
            }
            if (line.length() > 0) {
                logger.debug("received:", line);
                try {
                    JSONObject json = new JSONObject(line);
                    if (!json.isNull("text")) {
                        // the status could be filtered here: ATM the user stream returns statuses' containing activity the API doesn't return
                        //  eg replies to people you don't follow
                        
                        listener.onStatus(new StatusJSONImpl(json));
                    } else if (!json.isNull("delete")) {
                        listener.onDeletionNotice(new StatusDeletionNoticeImpl(json));
                    } else if (!json.isNull("limit")) {
                        listener.onTrackLimitationNotice(ParseUtil.getInt("track", json.getJSONObject("limit")));
                    }
                    else if (!json.isNull ("friends")) {
                        JSONArray friends = json.getJSONArray ("friends");
                        int [] friendIds = new int [friends.length ()];
                        for (int i = 0; i < friendIds.length; ++i)
                            friendIds[i] = friends.getInt (i);
                        
                        listener.onFriendList (friendIds);
                    }
                    else if (!json.isNull ("event")) {
                        String event = json.getString ("event");
                        int source = json.getJSONObject ("source").getInt ("id");
                        int target = json.getJSONObject ("target").getInt ("id");
                        
                        if ("favorite".equals (event))
                        {
//                            System.out.println ("fav: " + line);
                            
                            long targetObject = json.getJSONObject ("target_object").getLong ("id");
                            listener.onFavorite (source, target, targetObject);
                            
                        }
                        else if ("unfavorite".equals (event))
                        {
//                            System.out.println ("Unfavoriting event: " + line);
                            
                            long targetObject = json.getJSONObject ("target_object").getLong ("id");
                            listener.onUnfavorite (source, target, targetObject);
                        }
                        else if ("retweet".equals (event))
                        {
//                            System.out.println ("Retweet event: " + line);
                            
                            // note: retweet events also show up as statuses
                        }
                        else if ("follow".equals (event))
                        {
//                            System.out.println ("Follow event: " + line);
                            
                            listener.onFollow (source, target);
                        }
                        else if ("unfollow".equals (event))
                        {
                            System.out.println ("Unfollow event: " + line);
                            
                            listener.onFollow (source, target);
                        }
                    }
                    else {
                        // tmp: just checking what kind of unknown event we're receiving on this stream
                        System.out.println ("Received unknown event: " + line);
                    }
                } catch (JSONException jsone) {
                    listener.onException(jsone);
                }
            }
        } catch (IOException ioe) {
            try {
                is.close();
            } catch (IOException ignore) {
            }
            streamAlive = false;
            throw new TwitterException("Stream closed.", ioe);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void close() throws IOException {
        is.close();
        br.close();
        if (null != response) {
            response.disconnect();
        }
    }
}
