/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
