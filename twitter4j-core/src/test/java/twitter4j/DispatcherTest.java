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

    protected void tearDown() throws Exception {
        super.tearDown();
        dispatcher = null;
    }

    private int count;

    public void testInvokeLater() throws Exception {
        String name = "Twitter4J Async Dispatcher";
        int threadCount = ConfigurationContext.getInstance().getAsyncNumThreads();
        dispatcher = new DispatcherFactory(ConfigurationContext.getInstance()).getInstance();
        count = 0;
        dispatcher.invokeLater(new IncrementTask());
        dispatcher.invokeLater(new IncrementTask());
        dispatcher.invokeLater(new IncrementTask());
        Thread.sleep(300);
        assertEquals(threadCount, countThread(name));
        assertEquals(3, count);
        dispatcher.shutdown();
        Thread.sleep(1000);
        assertEquals(0, countThread(name));
    }

    private int countThread(String name) {
        int count = 0;
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        for (Thread thread : allThreads.keySet()) {
            if (thread.getName().contains(name)) {
                count++;
            }
        }
        return count;
    }

    private class IncrementTask implements Runnable {
        public void run() {
            System.out.println("executed");
            count++;
        }
    }
}
