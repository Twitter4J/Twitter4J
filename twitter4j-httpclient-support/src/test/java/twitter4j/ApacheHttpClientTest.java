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
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class ApacheHttpClientTest extends TestCase {

    public ApacheHttpClientTest(String name) {
        super(name);
    }
    Twitter twitterAPI1 = null;

    protected void setUp() throws Exception {
        super.setUp();
        twitterAPI1 = new TwitterFactory().getInstance();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        twitterAPI1.shutdown();
    }
    public void testOAuth() throws Exception {
        //get
        twitterAPI1.verifyCredentials();
        //post
        twitterAPI1.updateStatus(new StatusUpdate(new java.util.Date() + " test"));
        //multipart-post
        twitterAPI1.updateProfileBackgroundImage(new File("src/test/resources/t4j-reverse.gif"),false);
    }
}
