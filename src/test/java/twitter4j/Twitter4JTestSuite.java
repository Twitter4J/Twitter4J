package twitter4j;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>Title: Twitter4J</p>
 *
 * <p>Description: </p>
 *
 */
public class Twitter4JTestSuite extends TestSuite {

    public Twitter4JTestSuite(String s) {
        super(s);
    }

    public static Test suite() {
        Twitter4JTestSuite suite = new Twitter4JTestSuite("suite");
        suite.addTestSuite(twitter4j.DispatcherTestUnit.class);
        suite.addTestSuite(twitter4j.TwitterTestUnit.class);
        return suite;
    }
}
