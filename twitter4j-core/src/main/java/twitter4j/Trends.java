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

import java.util.Date;

/**
 * A data class representing Trends.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.2
 */

public interface Trends extends TwitterResponse, Comparable<Trends>, java.io.Serializable {
    Trend[] getTrends();

    /**
     * Returns the location associated with the trends.<br>
     * This method is effective only with getLocalTrends() method.<br>
     * i.e. The return value of this method will be null with Search API Methods (getTrends(), getCurrentTrends(), getDailyTrends(), and getWeeklyTrends()).<br>
     *
     * @return location
     * @since Twitter4J 2.1.1
     */
    Location getLocation();

    Date getAsOf();

    Date getTrendAt();

}
