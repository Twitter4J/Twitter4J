/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
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

import twitter4j.*;

import java.util.Date;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
public interface TrendsResources {
    /**
     * Returns the locations that Twitter has trending topic information for. The response is an array of &quot;locations&quot; that encode the location's WOEID (a <a href="http://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>) and some other human-readable information such as a canonical name and country the location belongs in.
     * <br>This method calls http://api.twitter.com/1.1/trends/available.json
     *
     * @return the locations
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1.1/get/trends/available">GET trends/available | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<Location> getAvailableTrends() throws TwitterException;
}
