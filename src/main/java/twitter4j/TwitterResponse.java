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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import twitter4j.http.HTMLEntity;
import twitter4j.http.Response;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Super class of Twitter Response objects.
 *
 * @see twitter4j.DirectMessage
 * @see twitter4j.Status
 * @see twitter4j.User
 * @see twitter4j.UserWithStatus
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterResponse implements java.io.Serializable {
    private Map<String,SimpleDateFormat> formatMap = new HashMap<String,SimpleDateFormat>();
    private static final long serialVersionUID = 3519962197957449562L;
    private transient int rateLimitLimit = -1;
    private transient int rateLimitRemaining = -1;
    private transient long rateLimitReset = -1;

    public TwitterResponse() {
    }

    public TwitterResponse(Response res) {
        String limit = res.getResponseHeader("X-RateLimit-Limit");
        if(null != limit){
            rateLimitLimit = Integer.parseInt(limit);
        }
        String remaining = res.getResponseHeader("X-RateLimit-Remaining");
        if(null != remaining){
            rateLimitRemaining = Integer.parseInt(remaining);
        }
        String reset = res.getResponseHeader("X-RateLimit-Reset");
        if(null != reset){
            rateLimitReset = Long.parseLong(reset);
        }
    }

    protected void ensureRootNodeNameIs(String rootName, Element elem) throws TwitterException {
        if (!rootName.equals(elem.getNodeName())) {
            throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + rootName + ". Check Twitter service availability.\n" + toString(elem));
        }
    }

    protected void ensureRootNodeNameIs(String[] rootNames, Element elem) throws TwitterException {
        String actualRootName = elem.getNodeName();
        for (String rootName : rootNames) {
            if (rootName.equals(actualRootName)) {
                return;
            }
        }
        String expected = "";
        for (int i = 0; i < rootNames.length; i++) {
            if (i != 0) {
                expected += " or ";
            }
            expected += rootNames[i];
        }
        throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + expected + ". Check Twitter service availability.\n" + toString(elem));
    }

    protected static void ensureRootNodeNameIs(String rootName, Document doc) throws TwitterException {
        Element elem = doc.getDocumentElement();
        if (!rootName.equals(elem.getNodeName())) {
            throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + rootName + ". Check Twitter service availability.\n" + toString(elem));
        }
    }

    protected static boolean isRootNodeNilClasses(Document doc) {
        String root = doc.getDocumentElement().getNodeName();
        return "nil-classes".equals(root) || "nilclasses".equals(root);
    }

    private static String toString(Element doc) {
        try {
            StringWriter output = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(output));
            return output.toString();
        } catch (TransformerException tfe) {
            return "";
        }
    }

    protected String getChildText( String str, Element elem ) {
        return HTMLEntity.unescape(elem.getElementsByTagName(str).getLength() > 0 ? elem.getElementsByTagName(str).item(0).getTextContent() : "");
    }

    protected int getChildInt(String str, Element elem) {
        String str2 = elem.getElementsByTagName(str).getLength() > 0 ? elem.getElementsByTagName(str).item(0).getTextContent() : null;
        if (null == str2 || "".equals(str2)) {
            return -1;
        } else {
            return Integer.valueOf(str2);
        }
    }

    protected long getChildLong(String str, Element elem) {
        String str2 = elem.getElementsByTagName(str).getLength() > 0 ? elem.getElementsByTagName(str).item(0).getTextContent() : null;        
        if (null == str2 || "".equals(str2)) {
            return -1;
        } else {
            return Long.valueOf(str2);
        }
    }

    protected String getString(String name, JSONObject json){
        String returnValue = null;
        try {
            try {
                returnValue = URLDecoder.decode(json.getString(name), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                returnValue = json.getString(name);
            }
        } catch (JSONException ignore) {
            // refresh_url could be missing
        }
        return returnValue;
    }

    protected boolean getChildBoolean(String str, Element elem) {
        return elem.getElementsByTagName(str).getLength() > 0 ? Boolean.valueOf(elem.getElementsByTagName(str).item(0).getTextContent()) : false;
    }
    protected Date getChildDate(String str, Element elem) throws TwitterException {
        return getChildDate(str, elem, "EEE MMM d HH:mm:ss z yyyy");
    }

    protected Date getChildDate(String str, Element elem, String format) throws TwitterException {
        return encodeDate(getChildText(str, elem),format);
    }
    protected Date encodeDate(String str, String format) throws TwitterException{
        SimpleDateFormat sdf = formatMap.get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            formatMap.put(format, sdf);
        }
        try {
            return sdf.parse(str);
        } catch (ParseException pe) {
            throw new TwitterException("Unexpected format(" + str + ") returned from twitter.com");
        }
    }

    public int getRateLimitLimit() {
        return rateLimitLimit;
    }

    public int getRateLimitRemaining() {
        return rateLimitRemaining;
    }

    public long getRateLimitReset() {
        return rateLimitReset;
    }
}
