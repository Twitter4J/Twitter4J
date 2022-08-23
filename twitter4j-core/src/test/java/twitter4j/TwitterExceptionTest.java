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


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
@Execution(ExecutionMode.CONCURRENT)
public class TwitterExceptionTest {

    @Test
    void testException() {
        TwitterException te1, te2, te3;
        te1 = new TwitterException("test");
        te2 = new TwitterException("test");
        te3 = new TwitterException(te1);

        assertNotEquals(te1.getExceptionCode(), te2.getExceptionCode());
        assertEquals(17, te1.getExceptionCode().length());

        String code1 = te1.getExceptionCode();

        String code2 = te2.getExceptionCode();

        assertEquals(35, te3.getExceptionCode().length());

        assertNotEquals(code1, code2);
    }

    @Test
    void testEncodedMessage() {
        TwitterException te = new TwitterException("{\"errors\":[{\"message\":\"Sorry, that page does not exist\",\"code\":34}]}");
        assertTrue(te.getMessage().contains("Sorry, that page does not exist"));
        assertTrue(te.isErrorMessageAvailable());
        assertEquals("Sorry, that page does not exist", te.getErrorMessage());
        assertEquals(34, te.getErrorCode());

        te = new TwitterException("error message");
        assertFalse(te.isErrorMessageAvailable());

    }

    @Test
    void testGetLong() {
        JSONObject json = new JSONObject("{\"value\":\"13857270119014401\"}");
        assertEquals(13857270119014401L, json.getLong("value"));
    }


}
