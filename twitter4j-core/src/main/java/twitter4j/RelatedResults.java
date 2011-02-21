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
 * @author Mocel - mocel at guma.jp
 * @since Twitter4J 2.1.8
 */
public interface RelatedResults extends TwitterResponse, java.io.Serializable {

    /**
     * Returns the 8 or less statuses with conversation
     *
     * @return list of statuses with conversation
     */
    ResponseList<Status> getTweetsWithConversation();

    /**
     * Returns the 8 or less statuses with reply.
     *
     * @return list of statuses with reply
     */
    ResponseList<Status> getTweetsWithReply();

    /**
     * Return the 3 or less latest statuses from the user who sent the origin tweet.
     *
     * @return list of latest statuses
     */
    ResponseList<Status> getTweetsFromUser();
}
