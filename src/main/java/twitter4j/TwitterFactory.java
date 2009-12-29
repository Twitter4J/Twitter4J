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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public class TwitterFactory {
    private final static Twitter DEFAULT_INSTANCE;
    private static final Configuration conf = Configuration.getInstance();

    static {
        DEFAULT_INSTANCE = new Twitter();
    }

    private TwitterFactory() {
        throw new AssertionError();
    }

    /**
     * Returns a Twitter instance.
     *
     * @return
     */
    public static Twitter getInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * @param screenName screen name
     * @param password password
     * @return basic authenticated instance
     * @noinspection deprecation
     */
    public static Twitter getBasicAuthenticatedInstance(String screenName
            , String password) {
        return new Twitter(screenName, password);
    }

    public static Twitter getOAuthAuthenticatedInstance(String consumerKey
            , String consumerSecret, AccessToken accessToken) {
        Twitter twitter = new Twitter();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(accessToken);
        return twitter;
    }

    public static Twitter getOAuthAuthenticatedInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        Twitter twitter = new Twitter();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(accessToken);
        return twitter;
    }

//    public static Twitter getInstance(Authentication authentication) {
//
//    }

}
