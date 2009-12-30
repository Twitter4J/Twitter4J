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

import twitter4j.http.HTMLEntity;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

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
 * A tiny parse utility class.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class ParseUtil {
    private ParseUtil() {
        // should never be instantiated
        throw new AssertionError();
    }

    private static Map<String, SimpleDateFormat> formatMap = new HashMap<String, SimpleDateFormat>();

    static String getUnescapedString(String str, JSONObject json) {
        return HTMLEntity.unescape(getRawString(str, json));
    }

    static String getRawString(String name, JSONObject json) {
        try {
            if (json.isNull(name)) {
                return null;
            } else {
                return json.getString(name);
            }
        } catch (JSONException jsone) {
            return null;
        }
    }

    static String getURLDecodedString(String name, JSONObject json) {
        String returnValue = getRawString(name, json);
        if (null != returnValue) {
            try {
                returnValue = URLDecoder.decode(returnValue, "UTF-8");
            } catch (UnsupportedEncodingException ignore) {
            }
        }
        return returnValue;
    }

    static Date getDate(String name, JSONObject json) throws TwitterException {
        return getDate(name, json, "EEE MMM d HH:mm:ss z yyyy");
    }

    static Date getDate(String name, JSONObject json, String format) throws TwitterException {
        String dateStr = getUnescapedString(name, json);
        if ("null".equals(dateStr)) {
            return null;
        } else {
            return getDate(dateStr, format);
        }
    }

    static Date getDate(String name, String format) throws TwitterException {
        SimpleDateFormat sdf = formatMap.get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            formatMap.put(format, sdf);
        }
        try {
            synchronized (sdf) {
                // SimpleDateFormat is not thread safe
                return sdf.parse(name);
            }
        } catch (ParseException pe) {
            throw new TwitterException("Unexpected format(" + name + ") returned from twitter.com");
        }
    }

    static int getInt(String name, JSONObject elem) {
        String str2 = getRawString(name, elem);
        if (null == str2 || "".equals(str2) || "null".equals(str2)) {
            return -1;
        } else {
            return Integer.valueOf(str2);
        }
    }


    static long getLong(String name, JSONObject json) {
        String str2 = getRawString(name, json);
        if (null == str2 || "".equals(str2) || "null".equals(str2)) {
            return -1;
        } else {
            return Long.valueOf(str2);
        }
    }
    static double getDouble(String name, JSONObject json) {
        String str2 = getRawString(name, json);
        if (null == str2 || "".equals(str2) || "null".equals(str2)) {
            return -1;
        } else {
            return Double.valueOf(str2);
        }
    }

    static boolean getBoolean(String name, JSONObject json) {
        String str = getRawString(name, json);
        if (null == str || "null".equals(str)) {
            return false;
        }
        return Boolean.valueOf(str);
    }
}