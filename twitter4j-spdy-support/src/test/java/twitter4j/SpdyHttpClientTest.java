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
import twitter4j.internal.http.HttpClient;
import twitter4j.internal.http.alternative.HttpClientImpl;

/**
 * Test case for HttpCient
 *
 * @author Hiroaki Takeuchi - takke30 at gmail.com
 * @since Twitter4J 3.x.x
 */
public class SpdyHttpClientTest extends TestCase {
    public SpdyHttpClientTest(String name) {
        super(name);
    }

    HttpClient client;

    protected void setUp() throws Exception {
        super.setUp();
        client = new HttpClientImpl();
    }

    protected void tearDown() {
    }
    
    public void testSpdy() {
    }
}
