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

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class RateLimitStatusJSONImplTest extends TestCase {
    public RateLimitStatusJSONImplTest(String name) {
        super(name);
    }

    public void testGetResetTimeInSeconds() throws Exception {
        RateLimitStatus status = RateLimitStatusJSONImpl.createFromResponseHeader(new MockHttpResponse());
//        System.out.println(status.getResetTimeInSeconds());
//        System.out.println((System.currentTimeMillis() / 1000) + 13 * 60);
//        System.out.println(status.getResetTimeInSeconds() - (System.currentTimeMillis() / 1000) + 13 * 60);
        // test case for TFJ-699
        assertTrue(status.getResetTimeInSeconds() > (System.currentTimeMillis() / 1000) + 13 * 60);
    }

    class MockHttpResponse extends HttpResponse {

        MockHttpResponse() {
            super(null);
        }

        @Override
        public String getResponseHeader(String name) {
            if (name.equals("X-Rate-Limit-Limit")) {
                return "180";
            } else if (name.equals("X-Rate-Limit-Remaining")) {
                return "178";
            } else if (name.equals("X-Rate-Limit-Reset")) {
                return String.valueOf((System.currentTimeMillis() + 14 * 60 * 1000) / 1000);
            }
            return null;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return null;
        }

        @Override
        public void disconnect() throws IOException {
        }
    }
}
