/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.util;

import junit.framework.TestCase;
import twitter4j.util.TimeSpanUtil;

public class TimeSpanUtilTest extends TestCase {
    public TimeSpanUtilTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    private void assertTimeSpanString(String expected, long timeBack){
        assertEquals(expected, TimeSpanUtil.toTimeSpanString(System.currentTimeMillis() - timeBack));

    }
    public void testToTimeSpanString() throws Exception {
        int second = 1000;
        int minute = second * 60;
        int hour = minute * 60;
        int day = hour * 24;
        assertTimeSpanString("less than 5 seconds ago", second * 4);
        assertTimeSpanString("less than 10 seconds ago", second * 7);
        assertTimeSpanString("less than 20 seconds ago", second * 19);
        assertTimeSpanString("half a minute ago", second * 31);
        assertTimeSpanString("less than a minute ago", second * 58);
        assertTimeSpanString("1 minute ago", second * 61);
        assertTimeSpanString("3 minutes ago", minute * 3);
        assertTimeSpanString("about an hour ago", hour * 1);
        assertTimeSpanString("about 3 hours ago", hour * 3);
        assertTimeSpanString("1 day ago", day * 1);
        assertTimeSpanString("4 days ago", day * 4);
    }
}
