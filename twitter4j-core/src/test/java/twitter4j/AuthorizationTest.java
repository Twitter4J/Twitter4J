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
package twitter4j;

import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.NullAuthorization;
import twitter4j.http.OAuthAuthorization;


/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AuthorizationTest extends TwitterTestBase {

    public AuthorizationTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAnonymousInstance() throws Exception {
        Twitter twitter = new TwitterFactory().getInstance();
        assertFalse(twitter.isBasicAuthEnabled());
        assertFalse(twitter.isOAuthEnabled());
        Authorization auth = twitter.getAuthorization();
        assertTrue(auth instanceof NullAuthorization);
    }

    public void testBasicInstance() throws Exception {
        Twitter twitter = new TwitterFactory().getInstance(id1.screenName, id1.password);
        assertTrue(twitter.isBasicAuthEnabled());
        assertFalse(twitter.isOAuthEnabled());
        Authorization auth = twitter.getAuthorization();
        assertTrue(auth instanceof BasicAuthorization);
    }

    public void testOAuthInstance() throws Exception {
        String consumerSecret;
        String consumerKey;
        consumerSecret = p.getProperty("browserConsumerSecret");
        consumerKey = p.getProperty("browserConsumerKey");

        Twitter twitter = new TwitterFactory().getOAuthAuthorizedInstance(consumerKey, consumerSecret);
        assertFalse(twitter.isBasicAuthEnabled());
        assertFalse(twitter.isOAuthEnabled());
        try {
            twitter.setOAuthConsumer(consumerSecret, consumerKey);
            fail("should throw IllegalStateException");
        } catch (IllegalStateException ignore) {

        }

        Authorization auth = twitter.getAuthorization();
        assertTrue(auth instanceof OAuthAuthorization);
    }
}
