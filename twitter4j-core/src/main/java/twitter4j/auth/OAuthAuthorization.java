/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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
package twitter4j.auth;

import twitter4j.conf.Configuration;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://oauth.net/core/1.0a/">OAuth Core 1.0a</a>
 * @since Twitter4J 2.1.13
 */
public class OAuthAuthorization extends twitter4j.http.OAuthAuthorization {
    // constructors

    /**
     * @param conf configuration
     */
    public OAuthAuthorization(Configuration conf) {
        super(conf);
    }

    /**
     * @param conf           configuration
     * @param consumerKey    consumer key
     * @param consumerSecret consumer secret
     * @deprecated use {@link #OAuthAuthorization(twitter4j.conf.Configuration)} instead
     */
    public OAuthAuthorization(Configuration conf, String consumerKey, String consumerSecret) {
        super(conf, consumerKey, consumerSecret);
    }

    /**
     * @param conf           configuration
     * @param consumerKey    consumer key
     * @param consumerSecret consumer secret
     * @param accessToken    access token
     * @deprecated use {@link #OAuthAuthorization(twitter4j.conf.Configuration)} instead
     */
    public OAuthAuthorization(Configuration conf, String consumerKey, String consumerSecret, AccessToken accessToken) {
        super(conf, consumerKey, consumerSecret, accessToken);
    }
}
