/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j;

import twitter4j.auth.Authorization;

import java.util.Arrays;
import java.util.Map;

/**
 * HTTP Request parameter object
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class HttpRequest implements java.io.Serializable {

    private static final long serialVersionUID = 3365496352032493020L;
    private final RequestMethod method;

    private final String url;

    private final HttpParameter[] parameters;

    private final Authorization authorization;

    private final Map<String, String> requestHeaders;


    private static final HttpParameter[] NULL_PARAMETERS = new HttpParameter[0];

    /**
     * @param method         Specifies the HTTP method
     * @param url            the request to request
     * @param parameters     parameters
     * @param authorization  Authentication implementation. Currently BasicAuthentication, OAuthAuthentication and NullAuthentication are supported.
     * @param requestHeaders request headers
     */
    public HttpRequest(RequestMethod method, String url, HttpParameter[] parameters
            , Authorization authorization, Map<String, String> requestHeaders) {
        this.method = method;
        if (method != RequestMethod.POST && parameters != null && parameters.length != 0) {
            this.url = url + "?" + HttpParameter.encodeParameters(parameters);
            this.parameters = NULL_PARAMETERS;
        } else {
            this.url = url;
            this.parameters = parameters;
        }
        this.authorization = authorization;
        this.requestHeaders = requestHeaders;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public HttpParameter[] getParameters() {
        return parameters;
    }

    public String getURL() {
        return url;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpRequest that = (HttpRequest) o;

        if (authorization != null ? !authorization.equals(that.authorization) : that.authorization != null)
            return false;
        if (!Arrays.equals(parameters, that.parameters)) return false;
        if (requestHeaders != null ? !requestHeaders.equals(that.requestHeaders) : that.requestHeaders != null)
            return false;
        if (method != null ? !method.equals(that.method) : that.method != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (parameters != null ? Arrays.hashCode(parameters) : 0);
        result = 31 * result + (authorization != null ? authorization.hashCode() : 0);
        result = 31 * result + (requestHeaders != null ? requestHeaders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestMethod=" + method +
                ", url='" + url + '\'' +
                ", postParams=" + (parameters == null ? null : Arrays.asList(parameters)) +
                ", authentication=" + authorization +
                ", requestHeaders=" + requestHeaders +
                '}';
    }
}
