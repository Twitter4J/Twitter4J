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

package twitter4j;

import junit.framework.TestCase;

import java.io.File;


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
    }

    public void testOAuth() throws Exception {
        //get
        twitterAPI1.verifyCredentials();
        //post
        twitterAPI1.updateStatus(new StatusUpdate(new java.util.Date() + " test"));
        //multipart-post
        twitterAPI1.updateProfileBackgroundImage(new File("src/test/resources/t4j-reverse.gif"), false);
    }
}
