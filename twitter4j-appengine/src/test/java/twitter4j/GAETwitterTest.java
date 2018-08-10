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

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
class GAETwitterTest extends TwitterTestBase {

    @BeforeEach
    void before() throws Exception {
        new LocalServiceTestHelper().setUp();
    }

    @Test
    void testGAETwitter() throws Exception {
        assertTrue(twitter1 instanceof AppEngineTwitterImpl);
        try {
            twitter1.showStatus(0L).toString();
        } catch (TwitterRuntimeException tre) {
            tre.printStackTrace();
            assertTrue(tre.getCause() instanceof TwitterException);
        }
        String msg = new java.util.Date().toString() + "日本語";
        Status status = twitter1.updateStatus(msg);
        assertEquals(msg, status.getText());
    }
}
