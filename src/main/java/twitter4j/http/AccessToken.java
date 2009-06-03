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
package twitter4j.http;

import twitter4j.TwitterException;

/**
 * Representing authorized Access Token which is passed to the service provider in order to access protected resources.<br>
 * the token and token secret can be stored into some persistent stores such as file system or RDBMS for the further accesses.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AccessToken extends OAuthToken {
    private static final long serialVersionUID = -8344528374458826291L;
    private String screenName;
    private int userId;
    
    AccessToken(Response res) throws TwitterException {
        this(res.asString());
    }

    // for test unit
    AccessToken(String str) {
        super(str);
        screenName = getParameter("screen_name");
	String sUserId = getParameter("user_id");
	if (sUserId != null) userId = Integer.parseInt(sUserId);

    }

    public AccessToken(String token, String tokenSecret) {
        super(token, tokenSecret);
    }

    /**
     *
     * @return screen name
     * @since Twitter4J 2.0.4
     */

	public String getScreenName() {
		return screenName;
	}

    /**
     *
     * @return user id
     * @since Twitter4J 2.0.4
     */

	public int getUserId() {
		return userId;
	}
    
}
