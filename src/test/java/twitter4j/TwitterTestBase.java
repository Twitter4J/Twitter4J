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

public class TwitterTestBase  extends TestCase {
    protected Twitter twitterAPI1, twitterAPI2, unauthenticated, twitterAPI4;
    protected Properties p = new Properties();

    protected class TestUserInfo {
        public String name;
        public String pass;
        public int id;
        TestUserInfo(String name, Properties props){
            this.name = p.getProperty(name);
            this.pass = p.getProperty(name + "pass");
            this.id = Integer.valueOf( p.getProperty(name + "id"));
        }
    }

    public TwitterTestBase(String name) {
        super(name);
    }
    protected String numberId, numberPass, followsOneWay, bestFriend1, bestFriend2;
    protected int id1id, numberIdId;
    protected TestUserInfo id1, id2, id3, id4;

    protected void setUp() throws Exception {
        super.setUp();
        p.load(new FileInputStream("test.properties"));
        id1 = new TestUserInfo("id1", p);
        id2 = new TestUserInfo("id2", p);
        id3 = new TestUserInfo("id3", p);
        id4 = new TestUserInfo("id4", p);

        numberId = p.getProperty("numberid");
        numberPass = p.getProperty("numberpass");
        id1id = Integer.valueOf(p.getProperty("id1id"));
        numberIdId = Integer.valueOf(p.getProperty("numberidid"));

        twitterAPI1 = new Twitter(id1.name, id1.pass);
//        twitterAPI1.setRetryCount(5);
//        twitterAPI1.setRetryIntervalSecs(5);
        twitterAPI2 = new Twitter(id2.name, id2.pass);
        twitterAPI4 = new Twitter(id4.name, id4.pass);
//        twitterAPI2.setRetryCount(5);
//        twitterAPI2.setRetryIntervalSecs(5);
        unauthenticated = new Twitter();
//        unauthenticated.setRetryCount(5);
//        unauthenticated.setRetryIntervalSecs(5);
        followsOneWay = p.getProperty("followsOneWay");
        bestFriend1 = p.getProperty("bestFriend1");
        bestFriend2 = p.getProperty("bestFriend2");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
