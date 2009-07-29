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

import twitter4j.http.Response;
import twitter4j.org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
public class StatusStream {
    private boolean streamAlive = true;
    private BufferedReader br;
    private InputStream is;
    private Response response;

    /*package*/ StatusStream(InputStream stream) throws IOException {
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    }
    /*package*/ StatusStream(Response response) throws IOException {
        this(response.asStream());
        this.response = response;
    }
    public Status next() throws TwitterException{
        if(!streamAlive){
            throw new IllegalStateException("Stream already closed.");
        }
        try {
            String line;
            while (streamAlive) {
                line = br.readLine();
                if (null != line && line.length() > 0) {
                    try{
                        return new Status(line);
                    } catch (JSONException ignore) {
                        // ignoring JSONException as per:
                        // http://groups.google.com/group/twitter-development-talk/browse_thread/thread/acac6b17134c0fa8
                        // it would be beneficial to have a listener which
                        // gets notified every time when deleted tweets come in
                    }
                }
            }
            throw new TwitterException("Stream closed.");
        } catch (IOException e) {
            try {
                is.close();
            } catch (IOException ignore) {
            }
            streamAlive = false;
            throw new TwitterException("Stream closed.", e);
        }

    }
    public void close() throws IOException{
        is.close();
        br.close();
        if(null != response){
            response.disconnect();
        }
    }
}
