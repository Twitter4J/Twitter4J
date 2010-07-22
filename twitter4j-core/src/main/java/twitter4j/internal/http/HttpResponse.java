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
package twitter4j.internal.http;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import twitter4j.TwitterException;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.org.json.JSONTokener;

/**
 * A data class representing HTTP Response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public abstract class HttpResponse {
    private static final Logger logger = Logger.getLogger(HttpResponseImpl.class);

    private static ThreadLocal<DocumentBuilder> builders =
            new ThreadLocal<DocumentBuilder>() {
                @Override
                protected DocumentBuilder initialValue() {
                    try {
                        return DocumentBuilderFactory.newInstance()
                                .newDocumentBuilder();
                    } catch (ParserConfigurationException ex) {
                        throw new ExceptionInInitializerError(ex);
                    }
                }
            };

    protected int statusCode;
    private Document responseAsDocument = null;
    protected String responseAsString = null;
    protected InputStream is;
    private boolean streamConsumed = false;

    public final int getStatusCode() {
        return statusCode;
    }

    public abstract String getResponseHeader(String name);

    /**
     * Returns the response stream.<br>
     * This method cannot be called after calling asString() or asDcoument()<br>
     * It is suggested to call disconnect() after consuming the stream.
     *
     * Disconnects the internal HttpURLConnection silently.
     * @return response body stream
     * @throws TwitterException
     * @see #disconnect()
     */
    public final InputStream asStream() {
        if(streamConsumed){
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return is;
    }

    /**
     * Returns the response body as string.<br>
     * Disconnects the internal HttpURLConnection silently.
     * @return response body
     * @throws TwitterException
     */
    public final String asString() throws TwitterException {
        if(null == responseAsString){
            BufferedReader br;
            InputStream stream = null;
            try {
                stream = asStream();
                if (null == stream) {
                    return null;
                }
                br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuffer buf = new StringBuffer();
                String line;
                while (null != (line = br.readLine())) {
                    buf.append(line).append("\n");
                }
                this.responseAsString = buf.toString();
                logger.debug(responseAsString);
                stream.close();
                streamConsumed = true;
            } catch (NullPointerException npe) {
                // don't remember in which case npe can be thrown
                throw new TwitterException(npe.getMessage(), npe);
            } catch (IOException ioe) {
                throw new TwitterException(ioe.getMessage(), ioe);
            }finally{
                if(null != stream){
                    try {
                        stream.close();
                    } catch (IOException ignore) {
                    }
                }
                disconnectForcibly();
            }
        }
        return responseAsString;
    }

    /**
     * Returns the response body as org.w3c.dom.Document.<br>
     * Disconnects the internal HttpURLConnection silently.
     * @return response body as org.w3c.dom.Document
     * @throws TwitterException
     */
    public final Document asDocument() throws TwitterException {
        if (null == responseAsDocument) {
            try {
                // it should be faster to read the inputstream directly.
                // but makes it difficult to troubleshoot
                this.responseAsDocument = builders.get().parse(new ByteArrayInputStream(asString().getBytes("UTF-8")));
            } catch (SAXException saxe) {
                throw new TwitterException("The response body was not well-formed:\n" + responseAsString, saxe);
            } catch (IOException ioe) {
                throw new TwitterException("There's something with the connection.", ioe);
            }finally{
                disconnectForcibly();
            }
        }
        return responseAsDocument;
    }

    /**
     * Returns the response body as twitter4j.internal.org.json.JSONObject.<br>
     * Disconnects the internal HttpURLConnection silently.
     * @return response body as twitter4j.internal.org.json.JSONObject
     * @throws TwitterException
     */
    public final JSONObject asJSONObject() throws TwitterException {
        JSONObject json = null;
        InputStreamReader reader = null;
        try {
            if (logger.isDebugEnabled()) {
                json =  new JSONObject(asString());
            } else {
                reader = asReader();
                json = new JSONObject(new JSONTokener(reader));
            }
        } catch (JSONException jsone) {
            if (logger.isDebugEnabled()) {
                throw new TwitterException(jsone.getMessage() + ":" + this.responseAsString, jsone);
            } else {
                throw new TwitterException(jsone.getMessage(), jsone);
            }
        }finally {
            if(null != reader){
                try{
                    reader.close();
                } catch (IOException ignore) {
                }
            }
            disconnectForcibly();
        }
        return json;
    }

    /**
     * Returns the response body as twitter4j.internal.org.json.JSONArray.<br>
     * Disconnects the internal HttpURLConnection silently.
     * @return response body as twitter4j.internal.org.json.JSONArray
     * @throws TwitterException
     */
    public final JSONArray asJSONArray() throws TwitterException {
        JSONArray json = null;
        InputStreamReader reader = null;
        try {
            if (logger.isDebugEnabled()) {
                json = new JSONArray(asString());
            } else {
                reader = asReader();
                json = new JSONArray(new JSONTokener(reader));
            }
        } catch (JSONException jsone) {
            if (logger.isDebugEnabled()) {
                throw new TwitterException(jsone.getMessage() + ":" + this.responseAsString, jsone);
            } else {
                throw new TwitterException(jsone.getMessage(), jsone);
            }
        }finally{
            if(null != reader){
                try{
                    reader.close();
                } catch (IOException ignore) {
                }
            }
            disconnectForcibly();
        }
        return json;
    }


    public final InputStreamReader asReader() {
        try {
            return new InputStreamReader(is, "UTF-8");
        } catch (java.io.UnsupportedEncodingException uee) {
            return new InputStreamReader(is);
        }
    }

    private void disconnectForcibly() {
        try{
            disconnect();
        }catch(Exception ignore){
        }
    }

    public abstract void disconnect() throws IOException;

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", responseAsDocument=" + responseAsDocument +
                ", responseAsString='" + responseAsString + '\'' +
                ", is=" + is +
                ", streamConsumed=" + streamConsumed +
                '}';
    }
}