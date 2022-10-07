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

import java.util.function.Function;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
@SuppressWarnings("unused")
abstract class Logger {
    private static final Function<String, Logger> factory;

    static {
        Function<String, Logger> factoryTemp;
        try {
            // use SLF4J if it's found in the classpath
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
            factoryTemp = (className) -> new SLF4JLogger(org.slf4j.LoggerFactory.getLogger(className));
        } catch (ClassNotFoundException ignore) {
            try {
                // use Log4J if it's found in the classpath
                Class.forName("org.apache.logging.log4j.Logger");
                factoryTemp = (className) -> new Log4JLogger(org.apache.logging.log4j.LogManager.getLogger(className));
            } catch (ClassNotFoundException e) {
                factoryTemp = (className) -> new JULLogger(java.util.logging.Logger.getLogger(className));
            }
        }
        factory = factoryTemp;
    }

    /**
     * Returns a Logger instance associated with the specified class.
     *
     * @return logger instance
     */
    public static Logger getLogger() {
        final String className = new Throwable().getStackTrace()[1].getClassName();
        return factory.apply(className);
    }

    /**
     * tests if debug level logging is enabled
     *
     * @return if debug level logging is enabled
     */
    public abstract boolean isDebugEnabled();

    /**
     * tests if info level logging is enabled
     *
     * @return if info level logging is enabled
     */
    public abstract boolean isInfoEnabled();

    /**
     * tests if warn level logging is enabled
     *
     * @return if warn level logging is enabled
     */
    public abstract boolean isWarnEnabled();

    /**
     * tests if error level logging is enabled
     *
     * @return if error level logging is enabled
     */
    public abstract boolean isErrorEnabled();

    /**
     * @param message message
     */
    public abstract void debug(String message);

    /**
     * @param message  message
     * @param message2 message2
     */
    public abstract void debug(String message, String message2);

    /**
     * @param message message
     */
    public abstract void info(String message);

    /**
     * @param message  message
     * @param message2 message2
     */
    public abstract void info(String message, String message2);

    /**
     * @param message message
     */
    public abstract void warn(String message);

    /**
     * @param message  message
     * @param message2 message2
     */
    public abstract void warn(String message, String message2);

    /**
     * @param message message
     * @param th      throwable
     */
    public abstract void warn(String message, Throwable th);

    /**
     * @param message message
     */
    public abstract void error(String message);

    /**
     * @param message message
     * @param th      throwable
     */
    public abstract void error(String message, Throwable th);

}
