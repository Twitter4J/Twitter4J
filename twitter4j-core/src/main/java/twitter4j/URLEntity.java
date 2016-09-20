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
 * A data interface representing one single URL entity.
 *
 * @author Mocel - mocel at guma.jp
 * @since Twitter4J 2.1.9
 */
public interface URLEntity extends TweetEntity, java.io.Serializable {

    /**
     * Returns the URL mentioned in the tweet.<br>
     * This method implementation is to meet TweetEntity interface and the behavior is equivalent to {@link #getURL()}
     *
     * @return the mentioned URL
     */
    @Override
    String getText();

    /**
     * Returns the URL mentioned in the tweet.
     *
     * @return the mentioned URL
     */
    String getURL();

    /**
     * Returns the expanded URL if mentioned URL is shorten.
     *
     * @return the expanded URL if mentioned URL is shorten, or null if no shorten URL was mentioned.
     */
    String getExpandedURL();

    /**
     * Returns the display URL if mentioned URL is shorten.
     *
     * @return the display URL if mentioned URL is shorten, or null if no shorten URL was mentioned.
     */
    String getDisplayURL();

    /**
     * Returns the index of the start character of the URL mentioned in the tweet.
     *
     * @return the index of the start character of the URL mentioned in the tweet
     */
    @Override
    int getStart();

    /**
     * Returns the index of the end character of the URL mentioned in the tweet.
     *
     * @return the index of the end character of the URL mentioned in the tweet
     */
    @Override
    int getEnd();
}
