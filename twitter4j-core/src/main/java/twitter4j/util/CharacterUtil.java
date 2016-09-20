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

package twitter4j.util;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public final class CharacterUtil {
    private CharacterUtil() {
        throw new AssertionError();
    }

    /**
     * Counts the length of the tweet
     *
     * @param text tweet to be counted
     * @return the length of the tweet
     */
    public static int count(String text) {
        return text.length();
    }

    /**
     * Returns true if the length of the string is exceeding length limitation
     *
     * @param text String to be examined
     * @return if the length of the string is exceeding length limitation
     */
    public static boolean isExceedingLengthLimitation(String text) {
        return count(text) > 140;
    }
}
