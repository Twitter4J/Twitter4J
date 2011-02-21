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

import java.io.Serializable;

/**
 * A data interface representing one single user mention entity.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
public interface UserMentionEntity extends Serializable {
    /**
     * Returns the name mentioned in the status.
     *
     * @return the name mentioned in the status
     */
    String getName();

    /**
     * Returns the screen name mentioned in the status.
     *
     * @return the screen name mentioned in the status
     */
    String getScreenName();

    /**
     * Returns the user id mentioned in the status.
     *
     * @return the user id mentioned in the status
     */
    long getId();

    /**
     * Returns the index of the start character of the user mention.
     *
     * @return the index of the start character of the user mention
     */
    int getStart();

    /**
     * Returns the index of the end character of the user mention.
     *
     * @return the index of the end character of the user mention
     */
    int getEnd();
}
