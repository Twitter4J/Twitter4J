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
 * A data interface representing one single Hashtag entity.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
public interface HashtagEntity extends TweetEntity, java.io.Serializable {
    /**
     * Returns the text of the hashtag without #.
     *
     * @return the text of the hashtag
     */
    String getText();

    /**
     * Returns the index of the start character of the hashtag.
     *
     * @return the index of the start character of the hashtag
     */
    int getStart();

    /**
     * Returns the index of the end character of the hashtag.
     *
     * @return the index of the end character of the hashtag
     */
    int getEnd();
}
