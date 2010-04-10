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
package twitter4j.http;

import twitter4j.TwitterTestBase;
import twitter4j.internal.http.HttpClient;
import twitter4j.internal.http.HttpClientImpl;

/**
 * Test case for HttpCient
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class HttpClientTest extends TwitterTestBase {
    public HttpClientTest(String name) {
        super(name);
    }

    HttpClient client;

    protected void setUp() throws Exception {
        super.setUp();
        client = new HttpClientImpl();
//        client.setUserId(id1.name);
//        client.setPassword(id1.pass);
    }

    protected void tearDown() {
    }

    public void testUpload() throws Exception {
//        client.setRetryCount(0);
//        PostParameter[] params = new PostParameter[]{
//                new PostParameter("theText","texttext"),
//                new PostParameter("theFile", new File("src/test/resources/t4j-reverse.jpeg"))
//        };
//        client.post("http://localhost:9000/struts-examples-1.3.5/upload/upload-submit.do?queryParam=Successful",
//                params).asString();

//        PostParameter[] params = new PostParameter[]{
//                new PostParameter("image", new File("src/test/resources/t4j-reverse.gif"))
//                new PostParameter("image", new File("src/test/resources/t4j.gif"))
//        };

//        User user = new User(client.post("http://api.twitter.com/1/account/update_profile_image.json",params, true));
//        User user = new User(client.post("http://localhost:9001/account/update_profile_image.json",params, true));
//        System.out.println(user.getProfileImageURL().toString());
    }
}
