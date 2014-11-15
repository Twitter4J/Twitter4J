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

import java.io.IOException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
interface StatusStream {

    /**
     * Reads next status from this stream.
     *
     * @param listener a StatusListener implementation
     * @throws TwitterException      when the end of the stream has been reached.
     * @throws IllegalStateException when the end of the stream had been reached.
     */
    void next(StatusListener listener) throws TwitterException;

    void close() throws IOException;
}
