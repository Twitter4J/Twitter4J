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
import twitter4j.conf.ConfigurationFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.OAuthAuthorization;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class TwitterFactory {
    private final Configuration conf;


    private TwitterFactory(Configuration conf) {
        this.conf = conf;
    }

    /**
     * Creates a TwitterFactory
     */
    public TwitterFactory() {
        this(ConfigurationFactory.getInstance());
    }

    /**
     * Returns a Twitter instance.
     *
     * @return default singleton instance
     */
    public Twitter getInstance() {
        return new Twitter(conf);
    }

    public Twitter getInstance(Authorization auth) {
        return new Twitter(auth);
    }

    /**
     * @param screenName screen name
     * @param password password
     * @return basic authenticated instance
     * @noinspection deprecation
     */
    public Twitter getBasicAuthorizedInstance(String screenName
            , String password) {
        return getInstance(new BasicAuthorization(screenName, password));
    }

//    public static Twitter getOAuthAuthorizedInstance(String consumerKey, String consumerSecret
//            , AccessToken accessToken) {
//        if (null == consumerKey && null == consumerSecret) {
//            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
//        }
//        OAuthAuthorization oauth = new OAuthAuthorization(consumerKey, consumerSecret, accessToken);
//        return getInstance(oauth);
//    }
//
    public Twitter getOAuthAuthorizedInstance(String consumerKey, String consumerSecret) {
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
        return getInstance(oauth);
    }

    public Twitter getInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret, accessToken);
        return getInstance(oauth);
    }

}