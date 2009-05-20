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

import junit.framework.TestCase;

import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class DispatcherTest extends TestCase {
    private Dispatcher dispatcher = null;

    public DispatcherTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        dispatcher = null;
    }

    public int count;

    public void testInvokeLater() throws Exception {
        boolean isJDK14orEarlier = false;
        String versionStr = System.getProperty("java.specification.version");
        if (null != versionStr) {
            isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
        }
        // this test runs only on JDK1.5 or later since Thread.getAllStackTraces() is available from JDK1.5
        String name = "test";
        int threadcount = 1;
        dispatcher = new Dispatcher(name, threadcount);
        count = 0;
        dispatcher.invokeLater(new IncrementTask());
        dispatcher.invokeLater(new IncrementTask());
        dispatcher.invokeLater(new IncrementTask());
        Thread.sleep(300);
        if (!isJDK14orEarlier) {
            assertTrue(existsThread(name));
        }
        assertEquals(3, count);
        dispatcher.shutdown();
        Thread.sleep(500);
        if (!isJDK14orEarlier) {
            assertFalse(existsThread(name));
        }
    }

    private boolean existsThread(String name) {
        boolean exists = false;
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        for (Thread thread : allThreads.keySet()) {
            if (-1 != thread.getName().indexOf(name)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    class IncrementTask implements Runnable {
        public void run() {
            System.out.println("executed");
            count++;
        }
    }

    ;

}
