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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.4
 */
final class NullLogger extends Logger {
    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void debug(String message) {
    }

    @Override
    public void debug(String message, String message2) {
    }

    @Override
    public void info(String message) {
    }

    @Override
    public void info(String message, String message2) {
    }

    @Override
    public void warn(String message) {
    }

    @Override
    public void warn(String message, String message2) {
    }

    @Override
    public void error(String message) {
    }

    @Override
    public void error(String message, Throwable th) {
    }
}
