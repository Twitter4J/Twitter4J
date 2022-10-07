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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A tiny parse utility class.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
final class ParseUtil {
    private ParseUtil() {
        // should never be instantiated
        throw new AssertionError();
    }

    static String getUnescapedString(String str, JSONObject json) {
        return HTMLEntity.unescape(getRawString(str, json));
    }

    public static String getRawString(String name, JSONObject json) {
        try {
            if (json.isNull(name)) {
                return null;
            } else {
                return json.getString(name);
            }
        } catch (Exception e) {
            return null;
        }
    }

    static String getURLDecodedString(String name, JSONObject json) {
        String returnValue = getRawString(name, json);
        if (returnValue != null) {
            try {
                returnValue = URLDecoder.decode(returnValue, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return returnValue;
    }

    static DateTimeFormatter formatterYYYY = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    static DateTimeFormatter formatterEEE = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
    static DateTimeFormatter formatterEEEYYYY = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss Z yyyy", Locale.US);


    public static LocalDateTime parseTrendsDate(String asOfStr) throws TwitterException {
        LocalDateTime parsed;
        switch (asOfStr.length()) {
            case 10:
                parsed = Instant.ofEpochMilli(Long.parseLong(asOfStr) * 1000)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                break;
            case 20:
                parsed = getDate(asOfStr, formatterYYYY);
                break;
            default:
                parsed = getDate(asOfStr, formatterEEE);
        }
        return parsed;
    }


    public static LocalDateTime getDate(String name, JSONObject json) throws TwitterException {
        return getDate(name, json, formatterEEEYYYY);
    }

    public static LocalDateTime getDate(String name, JSONObject json, String format) throws TwitterException {
        return getDate(name, json, getFormat(format));
    }

    private static DateTimeFormatter getFormat(String format) {
        return formatterMap.computeIfAbsent(format, pattern -> DateTimeFormatter.ofPattern(pattern, Locale.US));
    }

    public static LocalDateTime getDate(String name, JSONObject json, DateTimeFormatter format) throws TwitterException {
        String dateStr = getUnescapedString(name, json);
        if ("null".equals(dateStr) || null == dateStr) {
            return null;
        } else {
            return getDate(dateStr, format);
        }
    }

    private static final Map<String, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();

    public static LocalDateTime getDate(String dateString, String format) throws TwitterException {
        return getDate(dateString, getFormat(format));
    }

    public static LocalDateTime getDate(String dateString, DateTimeFormatter format) throws TwitterException {
        try {
            return LocalDateTime.from(format.parse(dateString));
        } catch (DateTimeParseException pe) {
            throw new TwitterException("Unexpected date format(" + dateString + ") returned from twitter.com", pe);
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
                return Integer.parseInt(str);
            } catch (NumberFormatException nfe) {
                // workaround for the API side issue http://issue.twitter4j.org/youtrack/issue/TFJ-484
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
                return Long.parseLong(str) + 1;
            }
            return Long.parseLong(str);
        }
    }

    public static double getDouble(String name, JSONObject json) {
        String str2 = getRawString(name, json);
        if (null == str2 || "".equals(str2) || "null".equals(str2)) {
            return -1;
        } else {
            return Double.parseDouble(str2);
        }
    }

    public static boolean getBoolean(String name, JSONObject json) {
        String str = getRawString(name, json);
        if (null == str || "null".equals(str)) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }


    public static TwitterResponse.AccessLevel toAccessLevel(HttpResponse res) {
        if (null == res) {
            return TwitterResponse.AccessLevel.NONE;
        }
        String xAccessLevel = res.getResponseHeader("X-Access-Level");
        TwitterResponse.AccessLevel accessLevel;
        if (null == xAccessLevel) {
            accessLevel = TwitterResponse.AccessLevel.NONE;
        } else {
            // https://dev.twitter.com/pages/application-permission-model-faq#how-do-we-know-what-the-access-level-of-a-user-token-is
            switch (xAccessLevel.length()) {
                // “read” (Read-only)
                case 4:
                    accessLevel = TwitterResponse.AccessLevel.READ;
                    break;
                case 10:
                    // “read-write” (Read & Write)
                    accessLevel = TwitterResponse.AccessLevel.READ_WRITE;
                    break;
                case 25:
                case 26:
                    // “read-write-privatemessages” (Read, Write, & Direct Message)
                    // “read-write-directmessages” (Read, Write, & Direct Message)
                    accessLevel = TwitterResponse.AccessLevel.READ_WRITE_DIRECTMESSAGES;
                    break;
                default:
                    accessLevel = TwitterResponse.AccessLevel.NONE;
                    // unknown access level;
            }
        }
        return accessLevel;
    }
}
