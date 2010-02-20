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
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.NullAuthorization;

/**
 * Base class of Twitter / AsyncTwitter / TwitterStream supports Basic Authorization.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class TwitterBase implements java.io.Serializable {
    protected final Configuration conf;

    protected Authorization auth;
    private static final long serialVersionUID = -3812176145960812140L;

    /*package*/ TwitterBase(Configuration conf){
        this.conf = conf;
        initBasicAuthorization(conf.getUser(), conf.getPassword());
    }

    /*package*/ TwitterBase(Configuration conf, String userId, String password){
        this.conf = conf;
        initBasicAuthorization(userId, password);
    }

    private void initBasicAuthorization(String screenName, String password){
        if (null != screenName && null != password) {
            auth = new BasicAuthorization(screenName, password);
        }
        if(null == auth){
            auth = NullAuthorization.getInstance();
        }
    }

    /*package*/ TwitterBase(Configuration conf, Authorization auth) {
        this.conf = conf;
        this.auth = auth;
    }

    /**
     * tests if the instance is authenticated by Basic
     * @return returns true if the instance is authenticated by Basic
     */
    public boolean isBasicAuthEnabled() {
        return auth instanceof BasicAuthorization && auth.isEnabled();
    }

    protected void ensureAuthorizationEnabled() {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
    }

    protected void ensureBasicEnabled() {
        if (!(auth instanceof BasicAuthorization)) {
            throw new IllegalStateException(
                    "user ID/password combination not supplied");
        }
    }

    /**
     * Returns the authorization scheme for this instance.<br>
     * The returned type will be either of BasicAuthorization, OAuthAuthorization, or NullAuthorization
     * @return the authorization scheme for this instance
     */
    public Authorization getAuthorization(){
        return auth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterBase)) return false;

        TwitterBase that = (TwitterBase) o;

        if (!auth.equals(that.auth)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return auth != null ? auth.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TwitterBase{" +
                "auth=" + auth +
                '}';
    }
}
