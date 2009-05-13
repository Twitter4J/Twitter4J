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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ class Dispatcher {
    private ExecuteThread[] threads;
    private List<Runnable> q = new LinkedList<Runnable> ();
    public Dispatcher(String name){
        this(name,1);
    }
    public Dispatcher(String name, int threadcount) {
        threads = new ExecuteThread[threadcount];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ExecuteThread(name,this, i);
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

    public synchronized void invokeLater(Runnable task) {
        synchronized (q) {
            q.add(task);
        }
        synchronized (ticket) {
            ticket.notify();
        }
    }
    Object ticket = new Object();
    public Runnable poll(){
        while(active){
            synchronized(q){
                if (q.size() > 0) {
                    Runnable task = q.remove(0);
                    if (null != task) {
                        return task;
                    }
                }
            }
            synchronized (ticket) {
                try {
                    ticket.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
        return null;
    }

    private boolean active = true;

    public synchronized void shutdown() {
        if (active) {
            active = false;
            for (ExecuteThread thread : threads) {
                thread.shutdown();
            }
            synchronized (ticket) {
                ticket.notify();
            }
        } else {
            throw new IllegalStateException("Already shutdown");
        }
    }
}

class ExecuteThread extends Thread {
    Dispatcher q;
    ExecuteThread(String name, Dispatcher q, int index) {
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
            if (null != task) {
                try {
                    task.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

