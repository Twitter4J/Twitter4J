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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class SpamReportingResourceTest extends TwitterTestBase {
    public SpamReportingResourceTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /* Spam Reporting Methods */
    public void testReportSpammerSavedSearches() throws Exception {
        // Not sure they're accepting multiple spam reports for the same user.
        // Do we really need to test this method? How?
        //String reportUserId = "_xxxx_xxxxxx_";
        //long   reportUserLongId = 0x0;
        //assertNotNull(twitter2.reportSpam(reportUserId));
        //assertNotNull(twitter2.reportSpam(reportUserLongId));
    }
}
