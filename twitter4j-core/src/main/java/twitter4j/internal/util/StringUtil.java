/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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
package twitter4j.internal.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.4
 */
public class StringUtil {
    private StringUtil() {
        throw new AssertionError();
    }

    public static String maskString(String str) {
        StringBuffer buf = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            buf.append("*");
        }
        return buf.toString();
    }

    // for JDK1.4 compatibility

    public static String[] split(String str, String separator) {
        String[] returnValue;
        int index = str.indexOf(separator);
        if (index == -1) {
            returnValue = new String[]{str};
        } else {
            List<String> strList = new ArrayList<String>();
            int oldIndex = 0;
            while (index != -1) {
                String subStr = str.substring(oldIndex, index);
                strList.add(subStr);
                oldIndex = index + separator.length();
                index = str.indexOf(separator, oldIndex);
            }
            if (oldIndex != str.length()) {
                strList.add(str.substring(oldIndex));
            }
            returnValue = strList.toArray(new String[strList.size()]);
        }

        return returnValue;
    }

    public static String join(int[] follows) {
        StringBuffer buf = new StringBuffer(11 * follows.length);
        for (int follow : follows) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    public static String join(String[] track) {
        StringBuffer buf = new StringBuffer(11 * track.length);
        for (String str : track) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(str);
        }
        return buf.toString();
    }
}
