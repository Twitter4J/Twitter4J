package twitter4j;

import junit.framework.TestCase;

/**
 */
public class DispatcherTestUnit extends TestCase {
    private Dispatcher dispatcher = null;

    public DispatcherTestUnit(String name) {
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

    public void testInvokeLater() throws Exception{
        String name = "test";
        int threadcount = 1;
        dispatcher = new Dispatcher(name, threadcount);
        count = 0;
        dispatcher.invokeLater(new IncrementTask());
        dispatcher.invokeLater(new IncrementTask());
        dispatcher.invokeLater(new IncrementTask());
        Thread.sleep(1000);
        dispatcher.shutdown();
        assertEquals(3,count);
    }

    class IncrementTask implements Runnable {
        public void run() {
            System.out.println("executed");
            count++;
        }
    };

}
