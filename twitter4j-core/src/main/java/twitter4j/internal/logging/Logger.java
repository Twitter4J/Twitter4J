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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public abstract class Logger {
    private static final Logger SINGLETON;

    static {
        Logger logger = null;

        // use SLF4J if it's found in the classpath
        logger = getLogger("org.slf4j.Logger", "twitter4j.internal.logging.SLF4JLogger");
        // otherwise, use commons-logging if it's found in the classpath
        if (null == logger) {
            logger = getLogger("org.apache.commons.logging.Log", "twitter4j.internal.logging.CommonsLoggingLogger");
        }
        // otherwise, use log4j if it's found in the classpath
        if (null == logger) {
            logger = getLogger("org.apache.log4j.Logger", "twitter4j.internal.logging.Log4JLogger");
        }
        // otherwise, use the default logger
        if (null == logger) {
            logger = new StdOutLogger();
        }
        SINGLETON = logger;
    }

    private static Logger getLogger(String checkClassName, String implementationClass) {
        Logger logger = null;
        try {
            Class.forName(checkClassName);
            logger = (Logger) Class.forName(implementationClass).newInstance();
        } catch (ClassNotFoundException ignore) {
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
        return logger;
    }

    /**
     * Static factory method.
     *
     * @return singleton logger instance
     */
    public static Logger getLogger() {
        return SINGLETON;
    }

    abstract boolean isDebugEnabled();

    abstract void debug(String message);

    abstract void debug(String message, String message2);

}
