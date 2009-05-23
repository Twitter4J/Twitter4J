/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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
package twitter4j;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Twitter4JTestSuite extends TestCase {
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public Twitter4JTestSuite(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Twitter4J Test Suite");
        suite.addTestSuite(AsyncTwitterTest.class);
        suite.addTestSuite(DispatcherTest.class);
        suite.addTestSuite(TwitterBasicAuthTest.class);
        suite.addTestSuite(twitter4j.http.OAuthTest.class);
        suite.addTestSuite(StreamAPITest.class);
        suite.addTestSuite(SearchAPITest.class);
        suite.addTestSuite(twitter4j.http.BASE64EncoderTest.class);
        suite.addTestSuite(twitter4j.http.HTMLEntityTest.class);
        suite.addTestSuite(twitter4j.http.ResponseTest.class);
        return suite;
    }
}
