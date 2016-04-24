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
 * @since Twitter4J 2.2.5
 */
public interface EntitySupport {
    /**
     * Returns an array of user mentions in the tweet. This method will an empty array if no users were mentioned in the tweet.
     *
     * @return An array of user mention entities in the tweet.
     * @since Twitter4J 2.1.9
     */
    UserMentionEntity[] getUserMentionEntities();

    /**
     * Returns an array if URLEntity mentioned in the tweet. This method will an empty array if no url were mentioned in the tweet.
     *
     * @return An array of URLEntity mentioned in the tweet.
     * @since Twitter4J 2.1.9
     */
    URLEntity[] getURLEntities();

    /**
     * Returns an array if hashtag mentioned in the tweet.  This method will an empty array if no hashtags were mentioned in the tweet.
     *
     * @return An array of Hashtag mentioned in the tweet.
     * @since Twitter4J 2.1.9
     */
    HashtagEntity[] getHashtagEntities();

    /**
     * Returns an array of MediaEntities if medias are available in the tweet. This method will an empty array if no medias were mentioned.
     * @return an array of MediaEntities.
     * @since Twitter4J 2.2.3
     */
    MediaEntity[] getMediaEntities();

    /**
     * Returns an array of ExtendedMediaEntities if media of extended_entities are available in the tweet. This method will an empty array if no extended-medias were mentioned.
     * @see <a href="https://dev.twitter.com/docs/api/multiple-media-extended-entities">Multiple Media Entities in Statuses</a>
     * @return an array of ExtendedMediaEntities.
     * @since Twitter4J 4.0.2
     */
    ExtendedMediaEntity[] getExtendedMediaEntities();

    /**
     * Returns an array of SymbolEntities if medias are available in the tweet. This method will an empty array if no symbols were mentioned.
     *
     * @return an array of SymbolEntities.
     * @since Twitter4J 3.0.4
     */
    SymbolEntity[] getSymbolEntities();
}
