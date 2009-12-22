package twitter4j.api;

import twitter4j.TwitterListener;

public interface HelpMethodsAsync
{
	/**
	 * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
	 * <br>This method calls http://api.twitter.com/1/help/test
	 *
	 * @since Twitter4J 2.1.0
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-help%C2%A0test">Twitter API Wiki / Twitter REST API Method: help%C2%A0test</a>
	 */
	void testAsync(TwitterListener listener);
}
