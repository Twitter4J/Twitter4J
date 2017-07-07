package twitter4j;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A test suite that runs only those tests that do not rely on real Twitter
 * accounts in test.properties.
 * 
 * @author Philip Hachey - philip dot hachey at gmail dot com
 */
@RunWith(Suite.class)
@SuiteClasses({ DispatcherTest.class, HTMLEntityTest.class, KryoSerializationTest.class, MediaEntityJSONImplTest.class,
		PagingTest.class, ParseUtilTest.class, RateLimitStatusJSONImplTest.class, StatusJSONImplTest.class,
		StatusSerializationTest.class, StringUtilTest.class, TwitterExceptionTest.class, UserJSONImplTest.class, })
public class AllLocalTests {

}
