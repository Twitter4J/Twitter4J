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
import twitter4j.http.BasicAuthorization;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpRequestFactory;
import twitter4j.http.NullAuthorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.RequestToken;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class TwitterSupport implements java.io.Serializable {
    protected static final Configuration conf = Configuration.getInstance();

    protected static final HttpRequestFactory requestFactory = new HttpRequestFactory(conf);

    protected final HttpClient http = HttpClient.getInstance(conf);

    private static final long serialVersionUID = -4779804628175934804L;
    Authorization auth;

    /*package*/ TwitterSupport(){
        this(conf.getUser(), conf.getPassword());
    }

    /*package*/ TwitterSupport(String userId, String password){
        if (null != userId && null != password) {
            auth = new BasicAuthorization(userId, password);
        }
        if(null == auth){
            auth = NullAuthorization.getInstance();
        }
    }

    protected void ensureAuthenticationEnabled() {
        if (!auth.isAuthenticationEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
    }

    protected void ensureBasicAuthenticationEnabled() {
        if (!(auth instanceof BasicAuthorization)) {
            throw new IllegalStateException(
                    "user ID/password combination not supplied");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterSupport)) return false;

        TwitterSupport that = (TwitterSupport) o;

        if (!auth.equals(that.auth)) return false;
        if (!http.equals(that.http)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = http.hashCode();
        result = 31 * result + auth.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TwitterSupport{" +
                "http=" + http +
                ", auth=" + auth +
                '}';
    }
}
