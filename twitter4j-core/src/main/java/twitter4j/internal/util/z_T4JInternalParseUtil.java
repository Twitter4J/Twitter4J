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

package twitter4j.internal.util;

import twitter4j.TwitterException;
import twitter4j.TwitterResponse;
import twitter4j.internal.http.HTMLEntity;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

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
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class z_T4JInternalParseUtil {
    private z_T4JInternalParseUtil() {
        // should never be instantiated
        throw new AssertionError();
    }

    private static ThreadLocal<Map<String, SimpleDateFormat>> formatMap = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new HashMap<String, SimpleDateFormat>();
        }
    };

    public static String getUnescapedString(String str, JSONObject json) {
        return HTMLEntity.unescape(getRawString(str, json));
    }

    public static String getRawString(String name, JSONObject json) {
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

    public static String getURLDecodedString(String name, JSONObject json) {
        String returnValue = getRawString(name, json);
        if (returnValue != null) {
            try {
                returnValue = URLDecoder.decode(returnValue, "UTF-8");
            } catch (UnsupportedEncodingException ignore) {
            }
        }
        return returnValue;
    }

    public static Date parseTrendsDate(String asOfStr) throws TwitterException {
        Date parsed;
        switch (asOfStr.length()) {
            case 10:
                parsed = new Date(Long.parseLong(asOfStr) * 1000);
                break;
            case 20:
                parsed = getDate(asOfStr, "yyyy-MM-dd'T'HH:mm:ss'Z'");
                break;
            default:
                parsed = getDate(asOfStr, "EEE, d MMM yyyy HH:mm:ss z");
        }
        return parsed;
    }


    public static Date getDate(String name, JSONObject json) throws TwitterException {
        return getDate(name, json, "EEE MMM d HH:mm:ss z yyyy");
    }

    public static Date getDate(String name, JSONObject json, String format) throws TwitterException {
        String dateStr = getUnescapedString(name, json);
        if ("null".equals(dateStr) || null == dateStr) {
            return null;
        } else {
            return getDate(dateStr, format);
        }
    }

    public static Date getDate(String name, String format) throws TwitterException {
        SimpleDateFormat sdf = formatMap.get().get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            formatMap.get().put(format, sdf);
        }
        try {
            return sdf.parse(name);
        } catch (ParseException pe) {
            throw new TwitterException("Unexpected date format(" + name + ") returned from twitter.com", pe);
        }
    }

    public static int getInt(String name, JSONObject json) {
        return getInt(getRawString(name, json));
    }

    public static int getInt(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return -1;
        } else {
            try {
                return Integer.valueOf(str);
            } catch (NumberFormatException nfe) {
                // workaround for the API side issue http://twitter4j.org/jira/browse/TFJ-484
                return -1;
            }
        }
    }

    public static long getLong(String name, JSONObject json) {
        return getLong(getRawString(name, json));
    }

    public static long getLong(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return -1;
        } else {
            // some count over 100 will be expressed as "100+"
            if (str.endsWith("+")) {
                str = str.substring(0, str.length() - 1);
                return Long.valueOf(str) + 1;
            }
            return Long.valueOf(str);
        }
    }

    public static double getDouble(String name, JSONObject json) {
        String str2 = getRawString(name, json);
        if (null == str2 || "".equals(str2) || "null".equals(str2)) {
            return -1;
        } else {
            return Double.valueOf(str2);
        }
    }

    public static boolean getBoolean(String name, JSONObject json) {
        String str = getRawString(name, json);
        if (null == str || "null".equals(str)) {
            return false;
        }
        return Boolean.valueOf(str);
    }


    public static int toAccessLevel(HttpResponse res) {
        if (null == res) {
            return -1;
        }
        String xAccessLevel = res.getResponseHeader("X-Access-Level");
        int accessLevel;
        if (null == xAccessLevel) {
            accessLevel = TwitterResponse.NONE;
        } else {
            // https://dev.twitter.com/pages/application-permission-model-faq#how-do-we-know-what-the-access-level-of-a-user-token-is
            switch (xAccessLevel.length()) {
                // “read” (Read-only)
                case 4:
                    accessLevel = TwitterResponse.READ;
                    break;
                case 10:
                    // “read-write” (Read & Write)
                    accessLevel = TwitterResponse.READ_WRITE;
                    break;
                case 25:
                    // “read-write-directmessages” (Read, Write, & Direct Message)
                    accessLevel = TwitterResponse.READ_WRITE_DIRECTMESSAGES;
                    break;
                case 26:
                    // “read-write-privatemessages” (Read, Write, & Direct Message)
                    accessLevel = TwitterResponse.READ_WRITE_DIRECTMESSAGES;
                    break;
                default:
                    accessLevel = TwitterResponse.NONE;
                    // unknown access level;
            }
        }
        return accessLevel;
    }
}