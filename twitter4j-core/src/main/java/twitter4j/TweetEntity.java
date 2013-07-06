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
 * super interface of UserMentionEntity, URLEntity and HashtagEntity
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.4
 */
public interface TweetEntity {
    /**
     * Returns the text of the entity
     *
     * @return the text of the entity
     */
    String getText();

    /**
     * Returns the index of the start character of the entity in the tweet.
     *
     * @return the index of the start character of the entity in the tweet
     */
    int getStart();

    /**
     * Returns the index of the end character of the entity in the tweet.
     *
     * @return the index of the end character of the entity in the tweet
     */
    int getEnd();
}
