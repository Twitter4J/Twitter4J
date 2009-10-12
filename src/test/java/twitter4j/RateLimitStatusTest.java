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

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.util.Properties;

public class RateLimitStatusTest extends TestCase {
    protected Twitter twitterAPI1 = null;
    protected Twitter unauthenticated = null;
    protected Properties p = new Properties();

    protected String id1, pass1;

    protected void setUp() throws Exception {
        super.setUp();
        p.load(new FileInputStream("test.properties"));
        id1 = p.getProperty("id1");
        pass1 = p.getProperty("pass1");
        twitterAPI1 = new Twitter(id1, pass1);
        unauthenticated = new Twitter();
    }

    RateLimitStatus rateLimitStatus = null;
    boolean accountLimitStatusAcquired;
    boolean ipLimitStatusAcquired;

    //need to think of a way to test this, perhaps mocking out Twitter is the way to go
    public void testRateLimitStatus() throws Exception {
        twitterAPI1.addAccountRateLimitStatusListener(new RateLimitStatusListener() {

            public void rateLimitStatusUpdated(RateLimitStatus status) {
                accountLimitStatusAcquired = true;
                ipLimitStatusAcquired = false;
                rateLimitStatus = status;
            }

        });

        unauthenticated.addIpRateLimitStatusListener(new RateLimitStatusListener() {
            public void rateLimitStatusUpdated(RateLimitStatus status) {
                ipLimitStatusAcquired = true;
                accountLimitStatusAcquired = false;
                rateLimitStatus = status;
            }

        });

        twitterAPI1.getMentions();
        assertTrue(accountLimitStatusAcquired);
        assertFalse(ipLimitStatusAcquired);
        RateLimitStatus previous = rateLimitStatus;
        twitterAPI1.getMentions();
        assertTrue(accountLimitStatusAcquired);
        assertFalse(ipLimitStatusAcquired);
        assertTrue(previous.getRemainingHits() > rateLimitStatus.getRemainingHits());
        assertEquals(previous.getHourlyLimit(), rateLimitStatus.getHourlyLimit());

        unauthenticated.getPublicTimeline();
        assertFalse(accountLimitStatusAcquired);
        assertTrue(ipLimitStatusAcquired);
        previous = rateLimitStatus;
        unauthenticated.getPublicTimeline();
        assertFalse(accountLimitStatusAcquired);
        assertTrue(ipLimitStatusAcquired);
        assertTrue(previous.getRemainingHits() > rateLimitStatus.getRemainingHits());
        assertEquals(previous.getHourlyLimit(), rateLimitStatus.getHourlyLimit());
    }

}
