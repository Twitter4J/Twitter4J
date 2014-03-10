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

package twitter4j.internal.logging;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.4
 */
final class NullLogger extends Logger {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String message) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String message, String message2) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String message) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String message, String message2) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String message) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String message, String message2) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message, Throwable th) {
    }
}
