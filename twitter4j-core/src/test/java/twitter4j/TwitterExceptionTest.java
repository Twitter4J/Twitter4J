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
package twitter4j;

import junit.framework.TestCase;
import twitter4j.internal.org.json.JSONObject;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
public class TwitterExceptionTest extends TestCase {
    public TwitterExceptionTest(String name){
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
    }

    public void testEncodedMessage() throws Exception {
        TwitterException te = new TwitterException("{\"error\":\"\\u3042\\u306a\\u305f\\u3092\\u30d5\\u30a9\\u30ed\\u30fc\\u3057\\u3066\\u3044\\u306a\\u3044\\u30e6\\u30fc\\u30b6\\u30fc\\u306b\\u30c0\\u30a4\\u30ec\\u30af\\u30c8\\u30e1\\u30c3\\u30bb\\u30fc\\u30b8\\u3092\\u9001\\u308b\\u3053\\u3068\\u304c\\u3067\\u304d\\u307e\\u305b\\u3093\\u3002\",\"request\":\"\\/1\\/direct_messages\\/new.json\"}");
        assertTrue(-1 != te.getMessage().indexOf("あなたをフォローしていないユーザーにダイレクトメッセージを送ることができません。"));
     }

    public void testGetLong() throws Exception {
        JSONObject json = new JSONObject("{\"value\":\"13857270119014401\"}");
        assertEquals(13857270119014401l, json.getLong("value"));
    }


}
