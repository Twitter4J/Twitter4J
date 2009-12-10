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
package twitter4j.http;

import junit.framework.TestCase;
import twitter4j.User;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Test case for HttpCient
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class HttpClientTest extends TestCase {
    public HttpClientTest(String name) {
        super(name);
    }

    HttpClient client;

    protected void setUp() throws Exception {
        Properties p = new Properties();

        p.load(new FileInputStream("test.properties"));
        String id1 = p.getProperty("id1");
        String pass1 = p.getProperty("pass1");
        client = new HttpClient();
        client.setUserId(id1);
        client.setPassword(pass1);
    }

    protected void tearDown() {
    }

    public void testUpload() throws Exception {
        client.setRetryCount(0);
//        User user = new User(client.post("http://api.twitter.com/1/account/update_profile_image.json", new PostParameter[]{new PostParameter("image", new File("src/test/resources/t4j-reverse.jpeg"))}, true));
//        System.out.println(user.getProfileImageURL());
    }
}
