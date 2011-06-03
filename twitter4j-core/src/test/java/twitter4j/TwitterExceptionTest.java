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

import junit.framework.TestCase;
import twitter4j.internal.org.json.JSONObject;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
public class TwitterExceptionTest extends TestCase {
    public TwitterExceptionTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testException() throws Exception {
        TwitterException te1, te2, te3;
        te1 = new TwitterException("test");
        te2 = new TwitterException("test");
        te3 = new TwitterException(te1);

        assertFalse(te1.getExceptionCode().equals(te2.getExceptionCode()));
        assertEquals(17, te1.getExceptionCode().length());

        String code1 = te1.getExceptionCode();

        String code2 = te2.getExceptionCode();

        assertEquals(35, te3.getExceptionCode().length());

        assertFalse(code1.equals(code2));
        new TwitterException("msg").toString();
    }

    public void testEncodedMessage() throws Exception {
        TwitterException te = new TwitterException("{\"error\":\"\\u3042\\u306a\\u305f\\u3092\\u30d5\\u30a9\\u30ed\\u30fc\\u3057\\u3066\\u3044\\u306a\\u3044\\u30e6\\u30fc\\u30b6\\u30fc\\u306b\\u30c0\\u30a4\\u30ec\\u30af\\u30c8\\u30e1\\u30c3\\u30bb\\u30fc\\u30b8\\u3092\\u9001\\u308b\\u3053\\u3068\\u304c\\u3067\\u304d\\u307e\\u305b\\u3093\\u3002\",\"request\":\"\\/1\\/direct_messages\\/new.json\"}");
        assertTrue(-1 != te.getMessage().indexOf("あなたをフォローしていないユーザーにダイレクトメッセージを送ることができません。"));
        assertTrue(te.isErrorMessageAvailable());
        assertEquals("あなたをフォローしていないユーザーにダイレクトメッセージを送ることができません。", te.getErrorMessage());
        assertEquals("/1/direct_messages/new.json", te.getRequestPath());

        te = new TwitterException("error message");
        assertFalse(te.isErrorMessageAvailable());

    }

    public void testGetLong() throws Exception {
        JSONObject json = new JSONObject("{\"value\":\"13857270119014401\"}");
        assertEquals(13857270119014401l, json.getLong("value"));
    }


}
