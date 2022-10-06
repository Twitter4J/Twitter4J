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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@Execution(ExecutionMode.CONCURRENT)
class RateLimitStatusJSONImplTest {
    @Test
    void testGetResetTimeInSeconds() {
        RateLimitStatus status = RateLimitStatusJSONImpl.createFromResponseHeader(new MockHttpResponse());
        // test case for TFJ-699
        assertTrue(status.getResetTimeInSeconds() > (System.currentTimeMillis() / 1000) + 13 * 60);
    }

    static class MockHttpResponse extends HttpResponse {

        MockHttpResponse() {
            super(false);
        }

        @Override
        public String getResponseHeader(String name) {
            return switch (name) {
                case "X-Rate-Limit-Limit" -> "180";
                case "X-Rate-Limit-Remaining" -> "178";
                case "X-Rate-Limit-Reset" -> String.valueOf((System.currentTimeMillis() + 14 * 60 * 1000) / 1000);
                default -> null;
            };
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return null;
        }

        @Override
        public void disconnect() {
        }
    }
}
