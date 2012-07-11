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

package twitter4j.internal.http;


public final class HTMLEntity {
	public static String escape(String original) {
		return HTMLEntityString.escape(original).getConvertedText().toString();
    }

    public static void escape(StringBuffer original) {
		HTMLEntityString.escape(original);
    }

    public static String unescape(String original) {
        String returnValue = null;
        if (original != null) {
            StringBuffer buf = new StringBuffer(original);
            unescape(buf);
            returnValue = buf.toString();
        }
        return returnValue;
    }

    public static void unescape(StringBuffer original) {
    	HTMLEntityString.unescape(original);
    }
}
