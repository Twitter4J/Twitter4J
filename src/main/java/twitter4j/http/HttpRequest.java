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

import java.util.Arrays;
import java.util.Map;

/**
 * HTTP Request parameter object
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class HttpRequest implements java.io.Serializable {
    /*package*/
    final RequestMethod requestMethod;
    /*package*/

    final String url;
    /*package*/
    final HttpParameter[] httpParams;
    /*package*/
    final Authorization authorization;
    /*package*/
    Map<String, String> requestHeaders;
    private static final long serialVersionUID = -3463594029098858381L;


    private static final HttpParameter[] NULL_PARAMETERS = new HttpParameter[0];

    /**
     * @param method         Specifies the HTTP method
     * @param url            the request to request
     * @param httpParams     parameters
     * @param authorization  Authentication implementation. Currently BasicAuthentication, OAuthAuthentication and NullAuthentication are supported.
     * @param requestHeaders
     */
    public HttpRequest(RequestMethod method, String url, HttpParameter[] httpParams
            , Authorization authorization, Map<String, String> requestHeaders) {
        this.requestMethod = method;
        if (method != RequestMethod.POST && null != httpParams && httpParams.length != 0) {
            this.url = url + "?" + HttpParameter.encodeParameters(httpParams);
            this.httpParams = NULL_PARAMETERS;
        } else {
            this.url = url;
            this.httpParams = httpParams;
        }
        this.authorization = authorization;
        this.requestHeaders = requestHeaders;
    }

    public String getURL() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpRequest that = (HttpRequest) o;

        if (authorization != null ? !authorization.equals(that.authorization) : that.authorization != null)
            return false;
        if (!Arrays.equals(httpParams, that.httpParams)) return false;
        if (requestHeaders != null ? !requestHeaders.equals(that.requestHeaders) : that.requestHeaders != null)
            return false;
        if (requestMethod != null ? !requestMethod.equals(that.requestMethod) : that.requestMethod != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestMethod != null ? requestMethod.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (httpParams != null ? Arrays.hashCode(httpParams) : 0);
        result = 31 * result + (authorization != null ? authorization.hashCode() : 0);
        result = 31 * result + (requestHeaders != null ? requestHeaders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestMethod=" + requestMethod +
                ", url='" + url + '\'' +
                ", postParams=" + (httpParams == null ? null : Arrays.asList(httpParams)) +
                ", authentication=" + authorization +
                ", requestHeaders=" + requestHeaders +
                '}';
    }
}
