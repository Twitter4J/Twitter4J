package twitter4j.api;

import twitter4j.Query;
import twitter4j.TwitterListener;

import java.util.Date;

public interface SearchMethodsAsync
{
	/**
	 * Returns tweets that match a specified query.
	 * <br>This method calls http://search.twitter.com/search
	 *
	 * @param query - the search condition
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 1.1.7
	 * @see <a href="http://apiwiki.twitter.com/Search-API-Documentation">Twitter API / Search API Documentation</a>
	 * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
	 */
	void searchAsync(Query query, TwitterListener listener);

	/**
	 * Returns the top ten topics that are currently trending on Twitter.  The response includes the time of the request, the name of each trend, and the url to the <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends
	 * @since Twitter4J 2.0.2
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
	 */
	void getTrendsAsync(TwitterListener listener);

	/**
	 * Returns the current top 10 trending topics on Twitter.  The response includes the time of the request, the name of each trending topic, and query used on <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends/current
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
	 */
	void getCurrentTrendsAsync(TwitterListener listener);

	/**
	 * Returns the current top 10 trending topics on Twitter.  The response includes the time of the request, the name of each trending topic, and query used on <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends/current
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @since Twitter4J 2.0.2
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
	 */
	void getCurrentTrendsAsync(boolean excludeHashTags, TwitterListener listener);

	/**
	 * Returns the top 20 trending topics for each hour in a given day.
	 * <br>This method calls http://search.twitter.com/trends/daily
	 * @since Twitter4J 2.0.2
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-daily">Twitter Search API Method: trends daily</a>
	 */
	void getDailyTrendsAsync(TwitterListener listener);

	/**
	 * Returns the top 20 trending topics for each hour in a given day.
	 * <br>This method calls http://search.twitter.com/trends/daily
	 * @param date Permits specifying a start date for the report.
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-daily">Twitter Search API Method: trends daily</a>
	 */
	void getDailyTrendsAsync(Date date, boolean excludeHashTags, TwitterListener listener);

	/**
	 * Returns the top 30 trending topics for each day in a given week.
	 * <br>This method calls http://search.twitter.com/trends/weekly
	 * @since Twitter4J 2.0.2
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-weekly">Twitter Search API Method: trends weekly</a>
	 */
	void getWeeklyTrendsAsync(TwitterListener listener);

	/**
	 * Returns the top 30 trending topics for each day in a given week.
	 * <br>This method calls http://search.twitter.com/trends/weekly
	 * @param date Permits specifying a start date for the report.
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @since Twitter4J 2.0.2
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-weekly">Twitter Search API Method: trends weekly</a>
	 */
	void getWeeklyTrendsAsync(Date date, boolean excludeHashTags, TwitterListener listener);
}
