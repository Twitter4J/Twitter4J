/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.internal.logging;

import java.security.AccessControlException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public abstract class Logger {
    private static final LoggerFactory LOGGER_FACTORY;
    private static final String LOGGER_FACTORY_IMPLEMENTATION = "twitter4j.loggerFactory";

    static {
        LoggerFactory loggerFactory = null;
        try{
            //-Dtwitter4j.debug=true -Dtwitter4j.loggerFactory=twitter4j.internal.logging.StdOutLoggerFactory
            String loggerFactoryImpl = System.getProperty(LOGGER_FACTORY_IMPLEMENTATION);
            if (null != loggerFactoryImpl) {
                loggerFactory = (LoggerFactory) Class.forName(loggerFactoryImpl).newInstance();
            }
        } catch (NoClassDefFoundError ignore) {
        } catch (ClassNotFoundException ignore) {
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException ignore) {
        } catch (AccessControlException ignore) {
        }
        // use SLF4J if it's found in the classpath
        if (null == loggerFactory) {
            try {
                // To use SLF4J, StaticLoggerBinder should be existing in the classpath
                // http://www.slf4j.org/codes.html#StaticLoggerBinder
                Class.forName("org.slf4j.impl.StaticLoggerBinder");
                loggerFactory = getLoggerFactory("org.slf4j.Logger", "twitter4j.internal.logging.SLF4JLoggerFactory");
            } catch (ClassNotFoundException ignore) {
            }
        }
        // otherwise, use commons-logging if it's found in the classpath
        if (null == loggerFactory) {
            loggerFactory = getLoggerFactory("org.apache.commons.logging.Log", "twitter4j.internal.logging.CommonsLoggingLoggerFactory");
        }
        // otherwise, use log4j if it's found in the classpath
        if (null == loggerFactory) {
            loggerFactory = getLoggerFactory("org.apache.log4j.Logger", "twitter4j.internal.logging.Log4JLoggerFactory");
        }
        // otherwise, use the default logger
        if (null == loggerFactory) {
            loggerFactory = new StdOutLoggerFactory();
        }
        LOGGER_FACTORY = loggerFactory;
        loggerFactory.getLogger(Logger.class).info("Will use "+loggerFactory.getClass() + " as logging factory.");
    }

    private static LoggerFactory getLoggerFactory(String checkClassName, String implementationClass) {
        LoggerFactory logger = null;
        try {
            Class.forName(checkClassName);
            logger = (LoggerFactory) Class.forName(implementationClass).newInstance();
        } catch (ClassNotFoundException ignore) {
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
        return logger;
    }

    /**
     * Returns a Logger instance associated with the specified class.
     * @param clazz class
     * @return logger instance
     */
    public static Logger getLogger(Class clazz){
        return LOGGER_FACTORY.getLogger(clazz);
    }

    /**
     * tests if debug level logging is enabled
     * @return if debug level logging is enabled
     */
    public abstract boolean isDebugEnabled();

    /**
     * tests if info level logging is enabled
     * @return if info level logging is enabled
     */
    public abstract boolean isInfoEnabled();

    /**
     * tests if warn level logging is enabled
     * @return if warn level logging is enabled
     */
    public abstract boolean isWarnEnabled();

    /**
     *
     * @param message message
     */
    public abstract void debug(String message);

    /**
     *
     * @param message message
     * @param message2 message2
     */
    public abstract void debug(String message, String message2);

    /**
     *
     * @param message message
     */
    public abstract void info(String message);

    /**
     *
     * @param message message
     * @param message2 message2
     */
    public abstract void info(String message, String message2);

    /**
     *
     * @param message message
     */
    public abstract void warn(String message);

    /**
     *
     * @param message message
     * @param message2 message2
     */
    public abstract void warn(String message, String message2);

}
