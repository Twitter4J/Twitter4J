/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import twitter4j.SavedSearch;
import twitter4j.TwitterException;

import java.util.List;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface SavedSearchesMethods
{
	/**
	 * Returns the authenticated user's saved search queries.
	 * <br>This method calls http://api.twitter.com/1/saved_searches.json
	 * @return Returns an array of numeric user ids the authenticating user is blocking.
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.8
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches">Twitter API Wiki / Twitter REST API Method: saved_searches</a>
	 */
	List<SavedSearch> getSavedSearches()
			throws TwitterException;

	/**
	 * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
	 * <br>This method calls http://api.twitter.com/1/saved_searches/show/id.json
	 * @param id The id of the saved search to be retrieved.
	 * @return the data for a saved search
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.8
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-show">Twitter API Wiki / Twitter REST API Method: saved_searches show</a>
	 */
	SavedSearch showSavedSearch(int id)
			throws TwitterException;

	/**
	 * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
	 * <br>This method calls http://api.twitter.com/1/saved_searches/saved_searches/create.json
	 * @param query the query string
	 * @return the data for a created saved search
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.8
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-create">Twitter API Wiki / Twitter REST API Method: saved_searches create</a>
	 */
	SavedSearch createSavedSearch(String query)
			throws TwitterException;

	/**
	 * Destroys a saved search for the authenticated user. The search specified by id must be owned by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/saved_searches/destroy/id.json
	 * @param id The id of the saved search to be deleted.
	 * @return the data for a destroyed saved search
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.8
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-saved_searches-destroy">Twitter API Wiki / Twitter REST API Method: saved_searches destroy</a>
	 */
	SavedSearch destroySavedSearch(int id)
			throws TwitterException;
}
