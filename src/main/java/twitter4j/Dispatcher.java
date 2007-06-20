package twitter4j;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
/*package*/ class Dispatcher {
    private ExecuteThread[] threads;
    private Queue<Runnable> q = new LinkedList<Runnable> ();
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
                shutdown();
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
        while(true){
            synchronized(q){
                Runnable task = q.poll();
                if (null != task) {
                    return task;
                }
            }
            synchronized (ticket) {
                try {
                    ticket.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    private boolean active = true;

    public synchronized void shutdown() {
        if (active) {
            for (int i = 0; i < threads.length; i++) {
                threads[i].shutdown();
            }
            synchronized (q) {
                q.notify();
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
            try{
                task.run();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}

