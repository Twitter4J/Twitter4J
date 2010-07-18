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

import twitter4j.Trends;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
public interface TrendsMethods {
    /**
     * Returns the top ten topics that are currently trending on Twitter. The response includes the time of the request, the name of each trend, and the url to the Twitter Search results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends.json
	 * @return the result
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
     * @see <a href="http://dev.twitter.com/doc/get/trends">GET trends | dev.twitter.com</a>
	 */
    Trends getTrends() throws TwitterException;

    /**
     * Returns the current top 10 trending topics on Twitter. The response includes the time of the request, the name of each trending topic, and query used on Twitter Search results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends/current.json
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
     * @see <a href="http://dev.twitter.com/doc/get/trends/current">GET trends/current | dev.twitter.com</a>
	 */
    Trends getCurrentTrends() throws TwitterException;

    /**
     * Returns the current top 10 trending topics on Twitter. The response includes the time of the request, the name of each trending topic, and query used on Twitter Search results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends/current.json
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
     * @see <a href="http://dev.twitter.com/doc/get/trends/current">GET trends/current | dev.twitter.com</a>
	 */
    Trends getCurrentTrends(boolean excludeHashTags) throws TwitterException;

    /**
     * Returns the top 20 trending topics for each hour in a given day.
	 * <br>This method calls http://search.twitter.com/trends/daily.json
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
     * @see <a href="http://dev.twitter.com/doc/get/trends/daily">GET trends/daily | dev.twitter.com</a>
	 */
    List<Trends> getDailyTrends() throws TwitterException;

    /**
     * Returns the top 20 trending topics for each hour in a given day.
	 * <br>This method calls http://search.twitter.com/trends/daily.json
	 * @param date Permits specifying a start date for the report.
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
     * @see <a href="http://dev.twitter.com/doc/get/trends/daily">GET trends/daily | dev.twitter.com</a>
	 */
	List<Trends> getDailyTrends(Date date, boolean excludeHashTags)
			throws TwitterException;

	/**
     * Returns the top 30 trending topics for each day in a given week.
	 * <br>This method calls http://search.twitter.com/trends/weekly.json
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
     * @see <a href="http://dev.twitter.com/doc/get/trends/weekly">GET trends/weekly | dev.twitter.com</a>
	 */
    List<Trends> getWeeklyTrends() throws TwitterException;

    /**
	 * Returns the top 30 trending topics for each day in a given week.
	 * <br>This method calls http://search.twitter.com/trends/weekly.json
	 * @param date Permits specifying a start date for the report.
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
     * @see <a href="http://dev.twitter.com/doc/get/trends/weekly">GET trends/weekly | dev.twitter.com</a>
	 */
	List<Trends> getWeeklyTrends(Date date, boolean excludeHashTags)
			throws TwitterException;
}
