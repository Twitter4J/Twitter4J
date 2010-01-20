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

import twitter4j.Paging;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface DirectMessageMethodsAsync {
    /**
	 * Returns a list of the direct messages sent to the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/direct_messages
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages">Twitter API Wiki / Twitter REST API Method: direct_messages</a>
	 */
	void getDirectMessages();

	/**
	 * Returns a list of the direct messages sent to the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/direct_messages
	 * @param paging controls pagination
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages">Twitter API Wiki / Twitter REST API Method: direct_messages</a>
	 */
	void getDirectMessages(Paging paging);

	/**
	 * Returns a list of the direct messages sent by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/direct_messages/sent
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0sent">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0sent</a>
	 */
	void getSentDirectMessages();

	/**
	 * Returns a list of the direct messages sent by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/direct_messages/sent
	 * @param paging controls pagination
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0sent">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0sent</a>
	 */
	void getSentDirectMessages(Paging paging);

	/**
	 * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
	 * The text will be trimed if the length of the text is exceeding 140 characters.
	 * <br>This method calls http://api.twitter.com/1/direct_messages/new
	 * @param screenName the screen name of the user to whom send the direct message
	 * @param text The text of your direct message.
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0new">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0new</a>
	 */
	void sendDirectMessage(String screenName, String text);

	/**
	 * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
	 * The text will be trimed if the length of the text is exceeding 140 characters.
	 * <br>This method calls http://api.twitter.com/1/direct_messages/new
	 * @param userId the screen name of the user to whom send the direct message
	 * @param text The text of your direct message.
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0new">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0new</a>
	 * @since Twitter4j 2.1.0
	 */
	void sendDirectMessage(int userId, String text);

	/**
	 * Delete specified direct message
	 * <br>This method calls http://api.twitter.com/1/direct_messages/destroy
	 * @param id int
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-direct_messages%C2%A0destroy">Twitter API Wiki / Twitter REST API Method: direct_messages%C2%A0destroy</a>
	 * @since Twitter4J 2.0.1
	 */
	void destroyDirectMessage(int id);
}
