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

package twitter4j.api;

import java.util.Date;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
public interface TrendsMethodsAsync {
    /**
     * Returns the top ten topics that are currently trending on Twitter. The response includes the time of the request, the name of each trend, and the url to the Twitter Search results page for that topic.
     * <br>This method calls http://search.twitter.com/trends.json
     *
     * @see <a href="http://dev.twitter.com/doc/get/trends">GET trends | dev.twitter.com</a>
     * @since Twitter4J 2.0.2
     */
    void getTrends();

    /**
     * Returns the current top 10 trending topics on Twitter. The response includes the time of the request, the name of each trending topic, and query used on Twitter Search results page for that topic.
     * <br>This method calls http://search.twitter.com/trends/current.json
     *
     * @see <a href="http://dev.twitter.com/doc/get/trends/current">GET trends/current | dev.twitter.com</a>
     * @since Twitter4J 2.0.2
     */
    void getCurrentTrends();

    /**
     * Returns the current top 10 trending topics on Twitter. The response includes the time of the request, the name of each trending topic, and query used on Twitter Search results page for that topic.
     * <br>This method calls http://search.twitter.com/trends/current.json
     *
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @see <a href="http://dev.twitter.com/doc/get/trends/current">GET trends/current | dev.twitter.com</a>
     * @since Twitter4J 2.0.2
     */
    void getCurrentTrends(boolean excludeHashTags);

    /**
     * Returns the top 20 trending topics for each hour in a given day.
     * <br>This method calls http://search.twitter.com/trends/daily.json
     *
     * @see <a href="http://dev.twitter.com/doc/get/trends/daily">GET trends/daily | dev.twitter.com</a>
     * @since Twitter4J 2.0.2
     */
    void getDailyTrends();

    /**
     * Returns the top 20 trending topics for each hour in a given day.
     * <br>This method calls http://search.twitter.com/trends/daily.json
     *
     * @param date            Permits specifying a start date for the report.
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @see <a href="http://dev.twitter.com/doc/get/trends/daily">GET trends/daily | dev.twitter.com</a>
     * @since Twitter4J 2.0.2
     */
    void getDailyTrends(Date date, boolean excludeHashTags);

    /**
     * Returns the top 30 trending topics for each day in a given week.
     * <br>This method calls http://search.twitter.com/trends/weekly.json
     *
     * @see <a href="http://dev.twitter.com/doc/get/trends/weekly">GET trends/weekly | dev.twitter.com</a>
     * @since Twitter4J 2.0.2
     */
    void getWeeklyTrends();

    /**
     * Returns the top 30 trending topics for each day in a given week.
     * <br>This method calls http://search.twitter.com/trends/weekly.json
     *
     * @param date            Permits specifying a start date for the report.
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @see <a href="http://dev.twitter.com/doc/get/trends/weekly">GET trends/weekly | dev.twitter.com</a>
     * @since Twitter4J 2.0.2
     */
    void getWeeklyTrends(Date date, boolean excludeHashTags);
}
