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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class GAETwitterTest extends TestCase {
    public GAETwitterTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGAETwitter() throws Exception {
        System.setProperty("twitter4j.http.httpClient", "twitter4j.internal.http.LazyHttpClientImpl");
        Twitter twitter = new TwitterFactory().getInstance();
        assertTrue(twitter instanceof GAETwitterImpl);
        try {
            twitter.showStatus(0L).toString();
        } catch (TwitterRuntimeException tre) {
            assertTrue(tre.getCause() instanceof TwitterException);
        }
        QueryResult result = twitter.search(new Query("aspofjaoprjraepofjapofawf"));
        result.getTweets();
    }
}
