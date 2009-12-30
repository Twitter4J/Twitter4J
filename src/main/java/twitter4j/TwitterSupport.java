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
import twitter4j.http.HttpClient;
import twitter4j.http.HttpRequestFactory;
import twitter4j.http.NullAuthorization;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class TwitterSupport implements java.io.Serializable {
    protected transient final static Configuration conf = Configuration.getInstance();

    protected transient static final HttpRequestFactory requestFactory = new HttpRequestFactory(conf);

    protected transient HttpClient http = HttpClient.getInstance(conf);

    protected Authorization auth;
    private static final long serialVersionUID = -3812176145960812140L;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(auth);
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        auth = (Authorization)stream.readObject();
        http = HttpClient.getInstance(conf);
    }


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

        return true;
    }

    @Override
    public int hashCode() {
        return auth != null ? auth.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TwitterSupport{" +
                "auth=" + auth +
                '}';
    }
}
