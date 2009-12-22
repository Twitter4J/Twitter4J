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

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface FavoriteMethods
{
	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites.json
	 *
	 * @return List<Status>
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	ResponseList<Status> getFavorites()
			throws TwitterException;

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites.json
	 *
	 * @param page the number of page
	 * @return ResponseList<Status>
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	ResponseList<Status> getFavorites(int page)
			throws TwitterException;

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 *
	 * @param id the ID or screen name of the user for whom to request a list of favorite statuses
	 * @return ResponseList<Status>
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 * @since Twitter4J 2.0.1
	 */
	ResponseList<Status> getFavorites(String id)
			throws TwitterException;

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites/[id].json
	 *
	 * @param id   the ID or screen name of the user for whom to request a list of favorite statuses
	 * @param page the number of page
	 * @return ResponseList<Status>
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites">Twitter API Wiki / Twitter REST API Method: favorites</a>
	 */
	ResponseList<Status> getFavorites(String id, int page)
			throws TwitterException;

	/**
	 * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
	 * <br>This method calls http://api.twitter.com/1/favorites/create/[id].json
	 *
	 * @param id the ID of the status to favorite
	 * @return Status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites%C2%A0create">Twitter API Wiki / Twitter REST API Method: favorites create</a>
	 */
	Status createFavorite(long id)
			throws TwitterException;

	/**
	 * Un-favorites the status specified in the ID parameter as the authenticating user.  Returns the un-favorited status in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/favorites/destroy/[id].json
	 *
	 * @param id the ID of the status to un-favorite
	 * @return Status
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-favorites%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: favorites destroy</a>
	 */
	Status destroyFavorite(long id)
			throws TwitterException;
}
