package twitter4j.api;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Trends;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.List;

public interface SearchMethods
{
	/**
	 * Returns tweets that match a specified query.
	 * <br>This method calls http://search.twitter.com/search
	 * @param query - the search condition
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 1.1.7
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-search">Twitter API Wiki / Twitter Search API Method: search</a>
	 */
	QueryResult search(Query query)
			throws TwitterException;

	/**
	 * Returns the top ten topics that are currently trending on Twitter.  The response includes the time of the request, the name of each trend, and the url to the <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
	 */
	Trends getTrends()
			throws TwitterException;

	/**
	 * Returns the current top 10 trending topics on Twitter.  The response includes the time of the request, the name of each trending topic, and query used on <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends/current
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
	 */
	Trends getCurrentTrends()
			throws TwitterException;

	/**
	 * Returns the current top 10 trending topics on Twitter.  The response includes the time of the request, the name of each trending topic, and query used on <a href="http://search.twitter.com/">Twitter Search</a> results page for that topic.
	 * <br>This method calls http://search.twitter.com/trends/current
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends">Twitter Search API Method: trends</a>
	 */
	Trends getCurrentTrends(boolean excludeHashTags)
			throws TwitterException;


	/**
	 * Returns the top 20 trending topics for each hour in a given day.
	 * <br>This method calls http://search.twitter.com/trends/daily
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-daily">Twitter Search API Method: trends daily</a>
	 */
	List<Trends> getDailyTrends()
			throws TwitterException;

	/**
	 * Returns the top 20 trending topics for each hour in a given day.
	 * <br>This method calls http://search.twitter.com/trends/daily
	 * @param date Permits specifying a start date for the report.
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-daily">Twitter Search API Method: trends daily</a>
	 */
	List<Trends> getDailyTrends(Date date, boolean excludeHashTags)
			throws TwitterException;

	/**
	 * Returns the top 30 trending topics for each day in a given week.
	 * <br>This method calls http://search.twitter.com/trends/weekly
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-weekly">Twitter Search API Method: trends weekly</a>
	 */
	List<Trends> getWeeklyTrends()
			throws TwitterException;

	/**
	 * Returns the top 30 trending topics for each day in a given week.
	 * <br>This method calls http://search.twitter.com/trends/weekly
	 * @param date Permits specifying a start date for the report.
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @return the result
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.2
	 * @see <a href="http://apiwiki.twitter.com/Twitter-Search-API-Method%3A-trends-weekly">Twitter Search API Method: trends weekly</a>
	 */
	List<Trends> getWeeklyTrends(Date date, boolean excludeHashTags)
			throws TwitterException;
}
