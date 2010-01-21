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

public class TwitterTestBase extends TestCase {
    public TwitterTestBase(String name) {
        super(name);
    }

    protected Twitter twitterAPI1, twitterAPI2, twitterAPI3, twitterAPI4,
            unauthenticated, twitterAPIBestFriend1, twitterAPIBestFriend2;
    protected Properties p = new Properties();

    protected String numberId, numberPass, followsOneWay;
    protected int id1id, numberIdId;
    protected TestUserInfo id1, id2, id3, id4,bestFriend1, bestFriend2;

    protected class TestUserInfo {
        public String screenName;
        public String pass;
        public int id;

        TestUserInfo(String screenName, Properties props) {
            this.screenName = p.getProperty(screenName);
            this.pass = p.getProperty(screenName + "pass");
            this.id = Integer.valueOf(p.getProperty(screenName + "id"));
        }
    }


    protected void setUp() throws Exception {
        super.setUp();
        p.load(new FileInputStream("src/test/resources/test.properties"));
        id1 = new TestUserInfo("id1", p);
        id2 = new TestUserInfo("id2", p);
        id3 = new TestUserInfo("id3", p);
        id4 = new TestUserInfo("id4", p);
        bestFriend1 = new TestUserInfo("bestFriend1", p);
        bestFriend2 = new TestUserInfo("bestFriend2", p);

        numberId = p.getProperty("numberid");
        numberPass = p.getProperty("numberpass");
        id1id = Integer.valueOf(p.getProperty("id1id"));
        numberIdId = Integer.valueOf(p.getProperty("numberidid"));

        twitterAPI1 = new TwitterFactory().getBasicAuthorizedInstance(id1.screenName, id1.pass);
        twitterAPI2 = new TwitterFactory().getBasicAuthorizedInstance(id2.screenName, id2.pass);
        twitterAPI3 = new TwitterFactory().getBasicAuthorizedInstance(id3.screenName, id3.pass);
        twitterAPI4 = new TwitterFactory().getBasicAuthorizedInstance(id4.screenName, id4.pass);
        twitterAPIBestFriend1 = new TwitterFactory().getBasicAuthorizedInstance(bestFriend1.screenName, bestFriend1.pass);
        twitterAPIBestFriend2 = new TwitterFactory().getBasicAuthorizedInstance(bestFriend2.screenName, bestFriend2.pass);
        unauthenticated = new Twitter();
        followsOneWay = p.getProperty("followsOneWay");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
