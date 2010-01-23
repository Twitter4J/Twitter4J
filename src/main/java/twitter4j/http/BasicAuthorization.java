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

import twitter4j.conf.Configuration;
import twitter4j.logging.Logger;

import java.net.HttpURLConnection;

/**
 * An authentication implementation implements Basic authentication
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class BasicAuthorization implements Authorization, java.io.Serializable {
    private static final Logger logger = Logger.getLogger();

    private String userId;
    private String password;
    private String basic;
    private static final long serialVersionUID = -5861104407848415060L;

    public BasicAuthorization(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.basic = encodeBasicAuthenticationString();
    }

    public String getUserId() {
        return userId;
    }

    private String encodeBasicAuthenticationString() {
        if (null != userId && null != password) {
            return "Basic " + BASE64Encoder.encode((userId + ":" + password).getBytes());
        }
        return null;
    }

    public void setAuthorizationHeader(String method, String url, HttpParameter[] params, HttpURLConnection con) {
        logger.debug("Authorization: Basic ************************");
        con.addRequestProperty("Authorization", basic);
    }

    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicAuthorization)) return false;

        BasicAuthorization that = (BasicAuthorization) o;

        return basic.equals(that.basic);

    }

    @Override
    public int hashCode() {
        return basic.hashCode();
    }

    @Override
    public String toString() {
        return "BasicAuthorization{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
