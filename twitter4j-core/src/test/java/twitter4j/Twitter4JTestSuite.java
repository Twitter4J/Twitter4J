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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import twitter4j.auth.OAuthTest;
import twitter4j.conf.ConfigurationTest;
import twitter4j.internal.json.HTMLEntityTest;
import twitter4j.util.TimeSpanConverterTest;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Twitter4JTestSuite extends TestCase {
//    public static void main(String[] args) {
//        TestRunner.run(suite());
//    }

    public Twitter4JTestSuite(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Twitter4J Test Suite");
        suite.addTestSuite(ConfigurationTest.class);
        suite.addTestSuite(twitter4j.http.BASE64EncoderTest.class);
        suite.addTestSuite(HTMLEntityTest.class);
        suite.addTestSuite(twitter4j.http.HttpClientTest.class);
        suite.addTestSuite(OAuthTest.class);
        suite.addTestSuite(twitter4j.http.PostParameterTest.class);
        suite.addTestSuite(twitter4j.util.CharacterUtilTest.class);
        suite.addTestSuite(TimeSpanConverterTest.class);

        suite.addTestSuite(PagingTest.class);
        suite.addTestSuite(SearchAPITest.class);
        suite.addTestSuite(TwitterExceptionTest.class);

        return suite;
    }
}
