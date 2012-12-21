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

import java.util.List;

/**
 * A data interface representing search API response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface QueryResult extends TwitterResponse, java.io.Serializable {
    long getSinceId();

    long getMaxId();

    /**
     * @deprecated use {@link #getRefreshURL()} instead
     */
    String getRefreshUrl();

    String getRefreshURL();

    int getCount();

    double getCompletedIn();

    String getQuery();

    List<Status> getTweets();

    /**
     * Returns a Query instance to fetch next page or null if there is no next page.
     *
     * @return Query instance to fetch next page
     * @since Twitter4J 3.0.0
     */
    Query nextQuery();

    /**
     * test if there is next page
     *
     * @return if there is next page
     * @since Twitter4J 3.0.0
     */
    boolean hasNext();
}
