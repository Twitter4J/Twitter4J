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

package twitter4j.internal.async;

import junit.framework.TestCase;
import twitter4j.conf.ConfigurationContext;

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
        if (versionStr != null) {
            isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
        }
        // this test runs only on JDK1.5 or later since Thread.getAllStackTraces() is available from JDK1.5
        String name = "Twitter4J Async Dispatcher";
        int threadcount = countThread(name);
        dispatcher = new DispatcherFactory(ConfigurationContext.getInstance()).getInstance();
        count = 0;
        dispatcher.invokeLater(new IncrementTask());
        dispatcher.invokeLater(new IncrementTask());
        dispatcher.invokeLater(new IncrementTask());
        Thread.sleep(300);
        if (!isJDK14orEarlier) {
            assertTrue((threadcount + 1) == countThread(name));
        }
        assertEquals(3, count);
        dispatcher.shutdown();
        Thread.sleep(1000);
        if (!isJDK14orEarlier) {
            assertTrue(threadcount == countThread(name));
        }
    }

    private int countThread(String name) {
        int count = 0;
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        for (Thread thread : allThreads.keySet()) {
            if (-1 != thread.getName().indexOf(name)) {
                count++;
            }
        }
        return count;
    }

    class IncrementTask implements Runnable {
        public void run() {
            System.out.println("executed");
            count++;
        }
    }
}
