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

import java.util.logging.Level;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
final class JULLogger extends Logger {
    private final java.util.logging.Logger LOGGER;

    JULLogger(java.util.logging.Logger logger) {
        LOGGER = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return LOGGER.isLoggable(Level.FINE);
    }

    @Override
    public boolean isInfoEnabled() {
        return LOGGER.isLoggable(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return LOGGER.isLoggable(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return LOGGER.isLoggable(Level.SEVERE);
    }

    @Override
    public void debug(String message) {
        LOGGER.fine(message);
    }

    @Override
    public void debug(String message, String message2) {
        LOGGER.fine(message + message2);
    }

    @Override
    public void info(String message) {
        LOGGER.info(message);
    }

    @Override
    public void info(String message, String message2) {
        LOGGER.info(message + message2);
    }

    @Override
    public void warn(String message) {
        LOGGER.warning(message);
    }

    @Override
    public void warn(String message, String message2) {
        LOGGER.warning(message + message2);
    }

    @Override
    public void error(String message) {
        LOGGER.severe(message);
    }

    @Override
    public void error(String message, Throwable th) {
        LOGGER.severe(message + th.getMessage());
    }
}
