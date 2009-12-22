package twitter4j.api;

import twitter4j.TwitterException;

public interface HelpMethods
{
	/**
	 * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
	 *
	 * @return true if the API is working
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 1.0.4
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-help%C2%A0test">Twitter API Wiki / Twitter REST API Method: help test</a>
	 */
	boolean test()
			throws TwitterException;
}
