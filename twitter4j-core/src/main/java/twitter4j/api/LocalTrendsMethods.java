/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.api;

import twitter4j.GeoLocation;
import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.Location;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface LocalTrendsMethods {
    /**
	 * Returns the locations that Twitter has trending topic information for. The response is an array of &quot;locations&quot; that encode the location's WOEID (a <a href="http://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>) and some other human-readable information such as a canonical name and country the location belongs in.
	 * <br>This method calls http://api.twitter.com/1/trends/available.json
	 * @return the locations
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/trends/available">GET trends/available | dev.twitter.com</a>
	 * @since Twitter4J 2.1.1
	 */
    ResponseList<Location> getAvailableTrends() throws TwitterException;

    /**
	 * Returns the sorted locations that Twitter has trending topic information for. The response is an array of &quot;locations&quot; that encode the location's WOEID (a <a href="http://developer.yahoo.com/geo/geoplanet/">Yahoo! Where On Earth ID</a>) and some other human-readable information such as a canonical name and country the location belongs in.
	 * <br>This method calls http://api.twitter.com/1/trends/available.json
     * @param location the available trend locations will be sorted by distance to the lat and long passed in. The sort is nearest to furthest.
     * @return the locations
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/trends/available">GET trends/available | dev.twitter.com</a>
	 * @since Twitter4J 2.1.1
	 */
    ResponseList<Location> getAvailableTrends(GeoLocation location) throws TwitterException;

    /**
	 * Returns the top 10 trending topics for a specific location Twitter has trending topic information for. The response is an array of "trend" objects that encode the name of the trending topic, the query parameter that can be used to search for the topic on Search, and the direct URL that can be issued against Search. This information is cached for five minutes, and therefore users are discouraged from querying these endpoints faster than once every five minutes.  Global trends information is also available from this API by using a WOEID of 1.
     * <br>This method calls http://api.twitter.com/1/trends/:woeid.json
     * @param woeid The WOEID of the location to be querying for
     * @return trends
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/get/trends/location/:woeid">GET trends/location/:woeid | dev.twitter.com</a>
	 * @since Twitter4J 2.1.1
	 */
    Trends getLocationTrends(int woeid) throws TwitterException;
}
