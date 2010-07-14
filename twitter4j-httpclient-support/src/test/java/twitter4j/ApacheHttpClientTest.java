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

import junit.framework.TestCase;

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

    protected Twitter twitterAPI1;
    protected Properties p = new Properties();

    protected TestUserInfo id1;

    protected class TestUserInfo {
        public String screenName;
        public String password;
        public int id;

        TestUserInfo(String screenName) {
            this.screenName = p.getProperty(screenName);
            this.password = p.getProperty(screenName + "pass");
            this.id = Integer.valueOf(p.getProperty(screenName + "id"));
        }
    }


    protected void setUp() throws Exception {
        super.setUp();
        InputStream is = ApacheHttpClientTest.class.getResourceAsStream("/test.properties");
        p.load(is);
        is.close();
        id1 = new TestUserInfo("id1");

        twitterAPI1 = new TwitterFactory().getInstance(id1.screenName, id1.password);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        twitterAPI1.shutdown();
    }
    public void testBasic() throws Exception {
        //get
        twitterAPI1.verifyCredentials();
        //post
        twitterAPI1.updateStatus(new StatusUpdate(new java.util.Date() + " test"));
        //multipart-post
        twitterAPI1.updateProfileBackgroundImage(new File("src/test/resources/t4j-reverse.gif"),false);
    }
}
