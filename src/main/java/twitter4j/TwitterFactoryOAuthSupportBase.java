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
package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.OAuthAuthorization;

/**
 * A FactoryBase class supports both Basic Authorization and OAuth authorization
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class TwitterFactoryOAuthSupportBase<T> extends TwitterFactoryBase<T> {
    /**
     * Creates a Factory
     */
    protected TwitterFactoryOAuthSupportBase() {
        super();
    }

    /**
     * Creates a Factory with a specified config tree path.
     * @param configTreePath the path
     */
    protected TwitterFactoryOAuthSupportBase(String configTreePath) {
        super(configTreePath);
    }

    protected abstract T getInstance(Configuration conf, Authorization auth);

    /**
     * Creates a Factory with a specified config tree path.
     * @param conf the configuration
     */
    protected TwitterFactoryOAuthSupportBase(Configuration conf) {
        super(conf);
    }

    /**
     * Returns a OAuth Authenticated instance.
     *
     * @param consumerKey consumer key
     * @param consumerSecret consumer secret
     * @return an instance
     */
    public T getOAuthAuthorizedInstance(String consumerKey, String consumerSecret) {
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        return getOAuthSupportInstance(oauth);
    }

    /**
     * Returns a OAuth Authenticated instance.
     *
     * @param consumerKey consumer key
     * @param consumerSecret consumer secret
     * @param accessToken access token
     * @return an instance
     */
    public T getOAuthAuthorizedInstance(String consumerKey, String consumerSecret, AccessToken accessToken) {
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        oauth.setOAuthAccessToken(accessToken);
        return getOAuthSupportInstance(oauth);
    }

    /**
     * Returns a OAuth Authenticated instance.<br>
     * consumer key and consumer Secret must be provided by twitter4j.properties, or system properties.
     *
     * @param accessToken access token
     * @return an instance
     */
    public T getOAuthAuthorizedInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret, accessToken);
        return getOAuthSupportInstance(oauth);
    }

    protected abstract T getOAuthSupportInstance(Configuration conf, Authorization auth);

    public T getOAuthSupportInstance(Authorization auth) {
        return getInstance(conf, auth);
    }
}
