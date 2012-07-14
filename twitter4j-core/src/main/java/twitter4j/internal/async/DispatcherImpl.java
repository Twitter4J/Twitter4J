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

import twitter4j.conf.Configuration;
import twitter4j.internal.logging.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
final class DispatcherImpl implements Dispatcher {
    private ExecuteThread[] threads;
    private final List<Runnable> q = new LinkedList<Runnable>();

    public DispatcherImpl(Configuration conf) {
        threads = new ExecuteThread[conf.getAsyncNumThreads()];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ExecuteThread("Twitter4J Async Dispatcher", this, i);
            threads[i].setDaemon(true);
            threads[i].start();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (active) {
                    shutdown();
                }
            }
        });
    }

    @Override
    public synchronized void invokeLater(Runnable task) {
        synchronized (q) {
            q.add(task);
        }
        synchronized (ticket) {
            ticket.notify();
        }
    }

    final Object ticket = new Object();

    public Runnable poll() {
        while (active) {
            synchronized (q) {
                if (q.size() > 0) {
                    Runnable task = q.remove(0);
                    if (task != null) {
                        return task;
                    }
                }
            }
            synchronized (ticket) {
                try {
                    ticket.wait();
                } catch (InterruptedException ignore) {
                }
            }
        }
        return null;
    }

    private boolean active = true;

    @Override
    public synchronized void shutdown() {
        if (active) {
            active = false;
            for (ExecuteThread thread : threads) {
                thread.shutdown();
            }
            synchronized (ticket) {
                ticket.notify();
            }
        }
    }
}

class ExecuteThread extends Thread {
    private static Logger logger = Logger.getLogger(ExecuteThread.class);
    DispatcherImpl q;

    ExecuteThread(String name, DispatcherImpl q, int index) {
        super(name + "[" + index + "]");
        this.q = q;
    }

    public void shutdown() {
        alive = false;
    }

    private boolean alive = true;

    public void run() {
        while (alive) {
            Runnable task = q.poll();
            if (task != null) {
                try {
                    task.run();
                } catch (Exception ex) {
                    logger.error("Got an exception while running a task:", ex);
                }
            }
        }
    }
}
